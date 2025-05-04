package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.dto.ItemDTO;
import se.kth.iv1350.pos.util.Amount;

/**
 * Represents a line item in a sale, containing information about
 * a specific item and its quantity.
 */
public class SaleLineItem {
    private final ItemDTO item;
    private int quantity;

    /**
     * Creates a new instance.
     *
     * @param item The item this line item represents.
     * @param quantity The quantity of the specified item.
     */
    public SaleLineItem(ItemDTO item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Increases the quantity by the specified amount.
     *
     * @param quantityToAdd The quantity to add.
     */
    public void incrementQuantity(int quantityToAdd) {
        this.quantity += quantityToAdd;
    }

    /**
     * Gets the total price of this line item, excluding VAT.
     *
     * @return The total price.
     */
    public Amount getSubtotal() {
        return item.getPrice().multiply(quantity);
    }

    /**
     * Gets the total VAT amount for this line item.
     *
     * @return The total VAT amount.
     */
    public Amount getVatAmount() {
        return item.getVatAmount().multiply(quantity);
    }

    /**
     * Gets the total price including VAT for this line item.
     *
     * @return The total price including VAT.
     */
    public Amount getTotalWithVat() {
        return item.getPriceWithVat().multiply(quantity);
    }

    /**
     * Gets the item DTO for this line item.
     *
     * @return The item DTO.
     */
    public ItemDTO getItem() {
        return item;
    }

    /**
     * Gets the quantity of this line item.
     *
     * @return The quantity.
     */
    public int getQuantity() {
        return quantity;
    }
}
