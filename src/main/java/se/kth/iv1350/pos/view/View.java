package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.dto.ItemDTO;
import se.kth.iv1350.pos.util.Amount;

/**
 * This is a placeholder for the real view. It contains hardcoded execution with calls
 * to the controller, simulating user interactions that would be triggered by GUI events.
 */
public class View {
    private final Controller controller;
    private Amount runningTotal = new Amount();

    /**
     * Creates a new instance.
     *
     * @param controller The controller that is used for all operations.
     */
    public View(Controller controller) {
        this.controller = controller;
    }

    /**
     * Simulates a user interaction with the system.
     * This method performs a complete sale following the basic flow from requirements.
     */
    public void runFakeExecution() {
        initiateSale();
        registerItems();

        simulateDiscountRequest();

        completeSale();
        processPayment();
    }


    /**
     * Initiates a new sale session.
     */
    private void initiateSale() {
        printDivider("Starting New Sale");
        controller.startNewSale();
        runningTotal = new Amount();
    }

    /**
     * Registers all items in the simulated sale.
     */
    private void registerItems() {
        // Register first item
        printActionHeader("Add 1 item with item id 1:");
        scanItem("1", 1);

        // Register same item again to test duplicate handling
        printActionHeader("Add 1 item with item id 1:");
        scanItem("1", 1);

        // Register different items
        printActionHeader("Add 1 item with item id 3:");
        scanItem("3", 1);

        printActionHeader("Add 1 item with item id 2:");
        scanItem("2", 1);
    }

    /**
     * Simulates a customer requesting a discount.
     */
    private void simulateDiscountRequest() {
        // This simulates the alternative flow 9a where a customer asks for a discount
        printDivider("Customer requests discount");

        // Simulate customer with ID 1001 (10% discount in our test data)
        String customerID = "1001";
        System.out.println("Customer says they are eligible for a discount.");
        System.out.println("Cashier enters customer ID: " + customerID);

        // Request discount through controller
        Amount totalBeforeDiscount = controller.getCurrentSale().calculateTotalWithVat();
        Amount totalAfterDiscount = controller.requestDiscount(customerID);
        Amount discountAmount = totalBeforeDiscount.subtract(totalAfterDiscount);

        // Display results
        System.out.println("Total before discount: " + formatAmount(totalBeforeDiscount) + " SEK");
        System.out.println("Discount amount: " + formatAmount(discountAmount) + " SEK");
        System.out.println("Total after discount: " + formatAmount(totalAfterDiscount) + " SEK");
        System.out.println();
    }

    /**
     * Completes the sale by calculating final totals.
     */
    private void completeSale() {
        printActionHeader("End sale:");
        Amount total = controller.endSale();
        System.out.println("Total cost (incl VAT): " + formatAmount(total) + " SEK");
    }

    /**
     * Processes payment for the completed sale.
     */
    private void processPayment() {
        Amount paymentAmount = new Amount(100);
        printActionHeader("Customer pays " + paymentAmount + ":");

        Amount change = controller.processPayment(paymentAmount);

        System.out.println("\nChange to give the customer: " + formatAmount(change) + " SEK");
    }

    /**
     * Handles the scanning of an item, displaying appropriate information.
     *
     * @param itemID The identifier of the item being scanned
     * @param quantity The quantity of the item
     */
    private void scanItem(String itemID, int quantity) {
        Controller.ItemWithRunningTotal result = controller.enterItem(itemID, quantity);

        if (result != null) {
            displayItemInfo(result.getItem());

            if (result.isDuplicate()) {
                System.out.println("This item was already in the sale. Quantity has been updated.");
            }

            displayRunningTotal(result.getRunningTotal());
        } else {
            displayItemNotFound(itemID);
        }
    }

    /**
     * Displays item information in the required format.
     *
     * @param item The item to display information for
     */
    private void displayItemInfo(ItemDTO item) {
        System.out.println("Item ID : " + item.getItemID());
        System.out.println("Item name : " + item.getName());
        System.out.println("Item cost : " + formatAmount(item.getPrice()) + " SEK");
        System.out.println("VAT : " + (int)(item.getVatRate() * 100) + "%");
        System.out.println("Item description : " + item.getDescription());
        System.out.println();
    }

    /**
     * Displays the running total for the current sale.
     *
     * @param total The current running total
     */
    private void displayRunningTotal(Amount total) {
        this.runningTotal = total;
        System.out.println("Total cost (incl VAT): " + formatAmount(runningTotal) + " SEK");
        Amount totalVAT = controller.getCurrentTotalVAT();
        System.out.println("Total VAT : " + formatAmount(totalVAT) + " SEK");
        System.out.println();
    }

    /**
     * Displays a message when an item is not found.
     *
     * @param itemID The identifier of the item that was not found
     */
    private void displayItemNotFound(String itemID) {
        System.out.println("Item with ID " + itemID + " not found in inventory!");
        System.out.println();
    }

    /**
     * Formats a monetary amount according to requirements.
     *
     * @param amount The amount to format
     * @return Formatted string with colon as decimal separator
     */
    private String formatAmount(Amount amount) {
        return String.format("%.2f", amount.getValue().doubleValue()).replace('.', ':');
    }

    /**
     * Prints a section header with a consistent format.
     *
     * @param action The action being performed
     */
    private void printActionHeader(String action) {
        System.out.println(action);
    }

    /**
     * Prints a divider with title for major sections.
     *
     * @param title The section title
     */
    private void printDivider(String title) {
        System.out.println(title);
    }
}
