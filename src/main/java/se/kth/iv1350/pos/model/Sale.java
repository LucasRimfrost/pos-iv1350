package se.kth.iv1350.pos.model;

import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.pos.dto.CustomerDTO;
import se.kth.iv1350.pos.dto.ItemDTO;
import se.kth.iv1350.pos.integration.ItemRegistry;
import se.kth.iv1350.pos.integration.Printer;
import se.kth.iv1350.pos.util.Amount;

/**
 * Represents a single sale transaction.
 * Handles items, pricing, discounts, payments, and receipt generation.
 */
public class Sale {
    private final List<SaleLineItem> items;
    private Amount discountAmount;
    private Receipt receipt;

    /**
     * Creates a new instance, representing the beginning of a sale.
     */
    public Sale() {
        this.items = new ArrayList<>();
        this.discountAmount = new Amount();
    }

    /**
     * Adds an item to the sale or increases quantity if already present.
     *
     * @param itemDTO The item to add.
     * @param quantity The quantity of the specified item.
     */
    public void addItem(ItemDTO itemDTO, int quantity) {
        // Check if item already exists in sale
        SaleLineItem existingItem = findExistingItem(itemDTO.getItemID());

        if (existingItem != null) {
            // Update quantity for existing item
            existingItem.incrementQuantity(quantity);
        } else {
            // Add new item to sale
            addNewItem(itemDTO, quantity);
        }
    }

    /**
     * Finds an existing item in the sale by ID.
     *
     * @param itemID The item identifier to search for.
     * @return The matching SaleLineItem or null if not found.
     */
    private SaleLineItem findExistingItem(String itemID) {
        for (SaleLineItem item : items) {
            if (item.getItem().getItemID().equals(itemID)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Adds a new item with the specified quantity to the sale.
     *
     * @param itemDTO The item to add.
     * @param quantity The quantity of the item.
     */
    private void addNewItem(ItemDTO itemDTO, int quantity) {
        SaleLineItem newLineItem = new SaleLineItem(itemDTO, quantity);
        items.add(newLineItem);
    }

    /**
     * Gets the list of items in the sale.
     *
     * @return A copy of the list of sale line items.
     */
    public List<SaleLineItem> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Calculates the total price of all items, excluding VAT.
     *
     * @return The total price, excluding VAT.
     */
    public Amount calculateTotal() {
        Amount total = new Amount();
        for (SaleLineItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    /**
     * Calculates the total VAT for all items.
     *
     * @return The total VAT amount.
     */
    public Amount calculateTotalVat() {
        Amount totalVat = new Amount();
        for (SaleLineItem item : items) {
            totalVat = totalVat.add(item.getVatAmount());
        }
        return totalVat;
    }

    /**
     * Gets the total price including VAT and applying any discounts.
     *
     * @return The total price including VAT, after discounts.
     */
    public Amount calculateTotalWithVat() {
        Amount total = calculateTotal().add(calculateTotalVat());
        return total.subtract(discountAmount);
    }

    /**
     * Applies the specified discount to this sale.
     *
     * @param customer The customer receiving the discount.
     * @param discountAmount The discount amount.
     * @return The total price after discount.
     */
    public Amount applyDiscount(CustomerDTO customer, Amount discountAmount) {
        this.discountAmount = discountAmount;
        return calculateTotalWithVat();
    }

    /**
     * Checks if a discount has been applied to this sale.
     *
     * @return True if a discount has been applied, false otherwise.
     */
    public boolean hasDiscount() {
        return discountAmount.isPositive();
    }

    /**
     * Gets the discount amount applied to this sale.
     *
     * @return The discount amount.
     */
    public Amount getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Processes payment for this sale and generates a receipt.
     *
     * @param payment The payment used to pay for the sale.
     * @return The change amount to be given back to the customer.
     */
    public Amount pay(CashPayment payment) {
        Amount totalToPay = calculateTotalWithVat();
        Amount change = payment.getChange(totalToPay);

        // Generate receipt with sale, payment, and change details
        this.receipt = new Receipt(this, payment.getAmount(), change);

        return change;
    }

    /**
     * Prints a receipt for the current sale on the specified printer.
     *
     * @param printer The printer where the receipt is printed.
     */
    public void printReceipt(Printer printer) {
        if (receipt != null) {
            printer.printReceipt(receipt);
        }
    }

    /**
     * Updates inventory with all items in this sale.
     *
     * @param itemRegistry The registry to update inventory in
     * @return true if all updates were successful, false if any failed
     */
    public boolean updateInventory(ItemRegistry itemRegistry) {
        return itemRegistry.updateInventoryForCompletedSale(items);
    }
}
