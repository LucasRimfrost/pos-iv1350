package se.kth.iv1350.pos.integration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import se.kth.iv1350.pos.dto.ItemDTO;
import se.kth.iv1350.pos.model.SaleLineItem;
import se.kth.iv1350.pos.util.Amount;

/**
 * Contains all calls to the external discount database.
 * This class is responsible for retrieving and calculating discount information.
 */
public class DiscountRegistry {
    // Discount database tables
    private final Map<String, Double> customerDiscounts = new HashMap<>();
    private final Map<String, Double> itemDiscounts = new HashMap<>();
    private final Map<String, Set<String>> itemCombinationDiscounts = new HashMap<>();
    private final Map<String, Double> combinationDiscountRates = new HashMap<>();

    // Constants for volume discounts
    private static final double LARGE_PURCHASE_THRESHOLD = 1000.0;
    private static final double MEDIUM_PURCHASE_THRESHOLD = 500.0;
    private static final double LARGE_PURCHASE_DISCOUNT_RATE = 0.03; // 3%
    private static final double MEDIUM_PURCHASE_DISCOUNT_RATE = 0.02; // 2%

    /**
     * Creates a new instance and initializes with some default discounts.
     */
    public DiscountRegistry() {
        initializeTestDiscounts();
    }

    /**
     * Calculates the total discount based on sale items, total amount, and customer ID.
     * Applies all eligible discount types according to business rules.
     *
     * @param items The items in the sale.
     * @param totalAmount The total amount of the sale.
     * @param customerID The customer identifier.
     * @return The total discount amount to be applied.
     */
    public Amount getDiscount(List<SaleLineItem> items, Amount totalAmount, String customerID) {
        // Initialize with zero discount
        Amount totalDiscount = new Amount(0);

        // Calculate all discount types
        Amount customerDiscount = calculateCustomerDiscount(totalAmount, customerID);
        Amount volumeDiscount = calculateVolumeDiscount(totalAmount);
        Amount itemDiscount = calculateItemSpecificDiscounts(items);
        Amount combinationDiscount = calculateItemCombinationDiscounts(items);

        // Combine all discounts
        totalDiscount = totalDiscount
                         .add(customerDiscount)
                         .add(volumeDiscount)
                         .add(itemDiscount)
                         .add(combinationDiscount);

        // Log discount details for accounting purposes
        logDiscountSummary(customerDiscount, volumeDiscount,
                          itemDiscount, combinationDiscount, totalDiscount);

        return totalDiscount;
    }

    /**
     * Verifies if a customer exists in the discount registry.
     *
     * @param customerID The customer identifier.
     * @return true if the customer exists, false otherwise.
     */
    public boolean customerExists(String customerID) {
        return customerDiscounts.containsKey(customerID);
    }

    /**
     * Gets the discount rate for a specific customer.
     *
     * @param customerID The customer identifier.
     * @return The customer's discount rate as a decimal, or 0 if no discount exists.
     */
    public double getCustomerDiscountRate(String customerID) {
        Double discountRate = customerDiscounts.get(customerID);
        return discountRate != null ? discountRate : 0;
    }

    /**
     * Gets the discount rate for a specific item.
     *
     * @param itemID The item identifier.
     * @return The item's discount rate as a decimal, or 0 if no discount exists.
     */
    public double getItemDiscountRate(String itemID) {
        Double discountRate = itemDiscounts.get(itemID);
        return discountRate != null ? discountRate : 0;
    }

    /**
     * Calculates customer-specific discount based on customer ID.
     *
     * @param totalAmount The total sale amount
     * @param customerID The customer's ID
     * @return The customer-specific discount amount
     */
    private Amount calculateCustomerDiscount(Amount totalAmount, String customerID) {
        Double customerDiscountPercent = customerDiscounts.get(customerID);

        if (customerDiscountPercent != null) {
            Amount discount = totalAmount.multiply(customerDiscountPercent);
            return discount;
        }

        return new Amount(0);
    }

    /**
     * Calculates volume-based discount based on total purchase amount.
     * Applies tiered discounts based on purchase thresholds.
     *
     * @param totalAmount The total sale amount
     * @return The volume-based discount amount
     */
    private Amount calculateVolumeDiscount(Amount totalAmount) {
        double purchaseAmount = totalAmount.getValue().doubleValue();

        if (purchaseAmount > LARGE_PURCHASE_THRESHOLD) {
            // 3% discount for purchases over 1000
            return totalAmount.multiply(LARGE_PURCHASE_DISCOUNT_RATE);
        } else if (purchaseAmount > MEDIUM_PURCHASE_THRESHOLD) {
            // 2% discount for purchases over 500
            return totalAmount.multiply(MEDIUM_PURCHASE_DISCOUNT_RATE);
        }

        // No volume discount applies
        return new Amount(0);
    }

    /**
     * Calculates discounts for individual items that have specific discount rates.
     *
     * @param items The items in the sale
     * @return The total item-specific discount amount
     */
    private Amount calculateItemSpecificDiscounts(List<SaleLineItem> items) {
        Amount totalItemDiscount = new Amount(0);

        for (SaleLineItem item : items) {
            ItemDTO itemDTO = item.getItem();
            String itemID = itemDTO.getItemID();
            Double discountRate = itemDiscounts.get(itemID);

            if (discountRate != null) {
                // Calculate discount for this specific item
                Amount itemPrice = itemDTO.getPrice().multiply(item.getQuantity());
                Amount itemDiscount = itemPrice.multiply(discountRate);
                totalItemDiscount = totalItemDiscount.add(itemDiscount);

                logItemDiscount(itemDTO, discountRate, itemDiscount);
            }
        }

        return totalItemDiscount;
    }

    /**
     * Calculates discounts for combinations of items purchased together.
     * Applies when all items in a defined combination are present in the sale.
     *
     * @param items The items in the sale
     * @return The total combination discount amount
     */
    private Amount calculateItemCombinationDiscounts(List<SaleLineItem> items) {
        Amount totalCombinationDiscount = new Amount(0);

        // Collect all item IDs in this sale
        Set<String> saleItemIDs = extractItemIDsFromSale(items);

        // Check each defined combination against the sale items
        for (Map.Entry<String, Set<String>> entry : itemCombinationDiscounts.entrySet()) {
            String combinationID = entry.getKey();
            Set<String> requiredItems = entry.getValue();

            // If all required items for this combination are present in the sale
            if (saleItemIDs.containsAll(requiredItems)) {
                Amount combinationDiscount = calculateDiscountForCombination(
                    combinationID, requiredItems, items);
                totalCombinationDiscount = totalCombinationDiscount.add(combinationDiscount);
            }
        }

        return totalCombinationDiscount;
    }

    /**
     * Extracts all item IDs from a sale.
     *
     * @param items The items in the sale
     * @return A set of all item IDs in the sale
     */
    private Set<String> extractItemIDsFromSale(List<SaleLineItem> items) {
        Set<String> saleItemIDs = new HashSet<>();
        for (SaleLineItem item : items) {
            saleItemIDs.add(item.getItem().getItemID());
        }
        return saleItemIDs;
    }

    /**
     * Calculates the discount for a specific combination of items.
     *
     * @param combinationID The identifier for the combination
     * @param requiredItems The set of items required for this combination
     * @param saleItems All items in the sale
     * @return The discount amount for this combination
     */
    private Amount calculateDiscountForCombination(
            String combinationID, Set<String> requiredItems, List<SaleLineItem> saleItems) {

        Double discountRate = combinationDiscountRates.get(combinationID);
        if (discountRate == null) {
            return new Amount(0);
        }

        // Calculate total price of the items in this combination
        Amount combinationPrice = sumPriceOfItemsInCombination(requiredItems, saleItems);
        Amount combinationDiscount = combinationPrice.multiply(discountRate);

        logCombinationDiscount(combinationID, discountRate, combinationDiscount);

        return combinationDiscount;
    }

    /**
     * Calculates the total price of items in a combination.
     *
     * @param requiredItems The items in the combination
     * @param saleItems All items in the sale
     * @return The total price of the items in the combination
     */
    private Amount sumPriceOfItemsInCombination(Set<String> requiredItems, List<SaleLineItem> saleItems) {
        Amount combinationPrice = new Amount(0);

        for (SaleLineItem item : saleItems) {
            if (requiredItems.contains(item.getItem().getItemID())) {
                Amount itemPrice = item.getItem().getPrice().multiply(item.getQuantity());
                combinationPrice = combinationPrice.add(itemPrice);
            }
        }

        return combinationPrice;
    }

    /**
     * Logs details about an item-specific discount.
     */
    private void logItemDiscount(ItemDTO item, double discountRate, Amount discountAmount) {
        System.out.println("Applied item discount for " + item.getDescription() +
                         " (" + discountRate * 100 + "%): " + discountAmount);
    }

    /**
     * Logs details about a combination discount.
     */
    private void logCombinationDiscount(String combinationID, double discountRate, Amount discountAmount) {
        System.out.println("Applied combination discount for combination " + combinationID +
                         " (" + discountRate * 100 + "%): " + discountAmount);
    }

    /**
     * Logs a summary of all applied discounts.
     */
    private void logDiscountSummary(Amount customerDiscount, Amount volumeDiscount,
                                  Amount itemDiscount, Amount combinationDiscount, Amount totalDiscount) {
        System.out.println("Discount details:");
        System.out.println("  Customer discount: " + customerDiscount);
        System.out.println("  Volume discount: " + volumeDiscount);
        System.out.println("  Item-specific discounts: " + itemDiscount);
        System.out.println("  Combination discounts: " + combinationDiscount);
        System.out.println("  Total discount: " + totalDiscount);
    }

    /**
     * Initializes test discount data for development and testing.
     */
    private void initializeTestDiscounts() {
        initializeCustomerDiscounts();
        initializeItemDiscounts();
        initializeItemCombinations();
    }

    /**
     * Sets up customer-specific discount rates.
     */
    private void initializeCustomerDiscounts() {
        // Loyal customers receive percentage discounts on their total purchase
        customerDiscounts.put("1001", 0.10); // 10% discount for customer 1001
        customerDiscounts.put("1002", 0.15); // 15% discount for customer 1002
    }

    /**
     * Sets up item-specific discount rates.
     */
    private void initializeItemDiscounts() {
        // Special promotions on individual items
        itemDiscounts.put("1", 0.05);  // 5% discount on Cornflakes (item #1)
        itemDiscounts.put("5", 0.10);  // 10% discount on Chocolate (item #5)
    }

    /**
     * Sets up combination discounts for groups of items purchased together.
     */
    private void initializeItemCombinations() {
        // Combination 1: Crispbread and Chocolate (snack combo)
        Set<String> snackCombo = new HashSet<>();
        snackCombo.add("4"); // Wasa Crispbread
        snackCombo.add("5"); // Fazer Chocolate
        itemCombinationDiscounts.put("COMBO1", snackCombo);
        combinationDiscountRates.put("COMBO1", 0.15); // 15% discount on snack combo

        // Combination 2: Breakfast combo (cereal and milk)
        Set<String> breakfastCombo = new HashSet<>();
        breakfastCombo.add("1"); // Kellogg's Cornflakes
        breakfastCombo.add("3"); // Arla Milk
        itemCombinationDiscounts.put("COMBO2", breakfastCombo);
        combinationDiscountRates.put("COMBO2", 0.20); // 20% discount on breakfast combo

        // Combination 3: Pasta meal combo
        Set<String> pastaCombo = new HashSet<>();
        pastaCombo.add("2"); // Barilla Pasta
        pastaCombo.add("5"); // Fazer Chocolate (dessert)
        itemCombinationDiscounts.put("COMBO3", pastaCombo);
        combinationDiscountRates.put("COMBO3", 0.10); // 10% discount on pasta meal
    }
}
