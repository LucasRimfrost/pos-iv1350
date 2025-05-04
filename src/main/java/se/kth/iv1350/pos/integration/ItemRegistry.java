package se.kth.iv1350.pos.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import se.kth.iv1350.pos.dto.ItemDTO;
import se.kth.iv1350.pos.model.SaleLineItem;
import se.kth.iv1350.pos.util.Amount;

/**
 * Contains all calls to the external inventory system.
 * This class is responsible for retrieving item information and updating inventory.
 */
public class ItemRegistry {
    // Store items and their inventory levels
    private final Map<String, ItemDTO> items = new HashMap<>();
    private final Map<String, Integer> inventory = new HashMap<>();

    /**
     * Creates a new instance and initializes with some default test items.
     */
    public ItemRegistry() {
        loadTestItemCatalog();
        initializeTestInventory();
    }

    /**
     * Searches for an item with the specified identifier.
     *
     * @param itemID The item identifier.
     * @return The DTO containing item information, or null if no matching item was found.
     */
    public ItemDTO findItem(String itemID) {
        return items.get(itemID);
    }

    /**
     * Checks if the specified item is available in inventory.
     *
     * @param itemID The item identifier.
     * @param quantity The quantity to check.
     * @return true if the requested quantity is available, false otherwise.
     */
    public boolean checkItemAvailability(String itemID, int quantity) {
        Integer availableQuantity = inventory.get(itemID);
        return availableQuantity != null && availableQuantity >= quantity;
    }

    /**
     * Updates inventory for multiple items from a completed sale.
     * This method would connect to the external inventory system in a production environment.
     *
     * @param saleItems List of sale line items to update in inventory.
     * @return true if all updates were successful, false if any failed.
     */
    public boolean updateInventoryForCompletedSale(List<SaleLineItem> saleItems) {
        boolean allSuccessful = true;

        System.out.println("Updating inventory for completed sale...");

        for (SaleLineItem item : saleItems) {
            String itemID = item.getItem().getItemID();
            int quantity = item.getQuantity();

            boolean success = updateInventory(itemID, quantity);

            if (!success) {
                allSuccessful = false;
                logInventoryUpdateFailure(itemID);
            }
        }

        logUpdateSummary(allSuccessful);

        return allSuccessful;
    }

    /**
     * Updates the inventory with the specified item quantity.
     * This method would connect to the external inventory system in a production environment.
     *
     * @param itemID The item identifier.
     * @param quantity The quantity to decrease from inventory.
     * @return true if update was successful, false otherwise.
     */
    public boolean updateInventory(String itemID, int quantity) {
        Integer currentQuantity = inventory.get(itemID);

        if (!isInventoryUpdatePossible(currentQuantity, quantity, itemID)) {
            return false;
        }

        // Update inventory and log the change
        inventory.put(itemID, currentQuantity - quantity);
        logInventoryDecrease(itemID, quantity);

        return true;
    }

    /**
     * Checks if an inventory update is possible based on available quantity.
     */
    private boolean isInventoryUpdatePossible(Integer currentQuantity, int requestedQuantity, String itemID) {
        if (currentQuantity == null || currentQuantity < requestedQuantity) {
            System.out.println("Inventory update failed: Insufficient quantity for item: " + itemID);
            return false;
        }
        return true;
    }

    /**
     * Logs a message about inventory decrease.
     */
    private void logInventoryDecrease(String itemID, int quantity) {
        System.out.println("Told external inventory system to decrease inventory quantity");
        System.out.println("of item " + itemID + " by " + quantity + " units.");
    }

    /**
     * Logs a failure to update inventory for a specific item.
     */
    private void logInventoryUpdateFailure(String itemID) {
        System.out.println("Warning: Failed to update inventory for item: " + itemID);
    }

    /**
     * Logs the summary of an inventory update operation.
     */
    private void logUpdateSummary(boolean allSuccessful) {
        if (allSuccessful) {
            System.out.println("Inventory successfully updated for all items");
        } else {
            System.out.println("Some inventory updates failed. Manual verification required.");
        }
    }

    /**
     * Loads test item catalog with product information.
     */
    private void loadTestItemCatalog() {
        // Food items (12% VAT)
        items.put("1", new ItemDTO("1",
                "Kellogg's Cornflakes",
                "500g, whole grain, fortified with vitamins",
                new Amount(10.0), 0.12));

        items.put("2", new ItemDTO("2",
                "Barilla Pasta",
                "500g, spaghetti, bronze cut",
                new Amount(15.0), 0.12));

        items.put("3", new ItemDTO("3",
                "Arla Milk",
                "1L, organic whole milk, pasteurized",
                new Amount(22.0), 0.12));

        // Other items (25% VAT)
        items.put("4", new ItemDTO("4",
                "Wasa Crispbread",
                "275g, whole grain, low sugar",
                new Amount(30.0), 0.25));

        items.put("5", new ItemDTO("5",
                "Fazer Chocolate",
                "200g, milk chocolate, Finnish quality",
                new Amount(75.0), 0.25));
    }

    /**
     * Initializes test inventory with default quantities.
     */
    private void initializeTestInventory() {
        // Initialize test inventory with 50 of each item
        for (String itemID : items.keySet()) {
            inventory.put(itemID, 50);
        }
    }
}
