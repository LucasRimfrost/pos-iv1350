package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.Receipt;

/**
 * Represents the printer, used for printing receipts.
 * This is a system integration point to the printer hardware.
 */
public class Printer {

    /**
     * Creates a new instance.
     */
    public Printer() {
        // Real implementation would initialize printer hardware integration
    }

    /**
     * Prints the specified receipt.
     * In this implementation, the receipt is just printed to System.out.
     *
     * @param receipt The receipt to print.
     */
    public void printReceipt(Receipt receipt) {
        System.out.println("Printing receipt...");
        System.out.println(receipt.format());
    }
}
