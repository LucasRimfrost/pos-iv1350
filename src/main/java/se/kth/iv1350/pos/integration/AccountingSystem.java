package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.Sale;
import se.kth.iv1350.pos.util.Amount;

/**
 * Contains all calls to the external accounting system.
 * This class is responsible for sending sales information for accounting purposes,
 * recording sales data, and updating sales statistics.
 */
public class AccountingSystem {
    /**
     * Creates a new instance with a connection to the accounting system.
     */
    public AccountingSystem() {
    }

    /**
     * Records a completed sale in the external accounting system.
     * Transmits sale information for financial record-keeping.
     *
     * @param sale The completed sale to record.
     */
    public void recordSale(Sale sale) {
        // Extract financial data from the sale
        Amount totalPrice = sale.calculateTotal();
        Amount totalVAT = sale.calculateTotalVat();

        // For demo purposes, output to console
        logSaleRecorded(totalPrice, totalVAT);
    }

    /**
     * Updates daily sales statistics in the accounting system.
     * This allows for real-time sales performance tracking.
     *
     * @param saleAmount The total amount from the completed sale.
     */
    public void updateSalesStatistics(Amount saleAmount) {
        // For demo purposes, output to console
        logStatisticsUpdated(saleAmount);
    }

    /**
     * Logs information about a recorded sale.
     *
     * @param totalPrice The total price of the sale (excluding VAT)
     * @param totalVAT The total VAT amount of the sale
     */
    private void logSaleRecorded(Amount totalPrice, Amount totalVAT) {
        System.out.println("Sale recorded in accounting system:");
        System.out.println("  Total amount: " + totalPrice);
        System.out.println("  Total VAT: " + totalVAT);
    }

    /**
     * Logs information about updated sales statistics.
     *
     * @param saleAmount The amount that was added to the statistics
     */
    private void logStatisticsUpdated(Amount saleAmount) {
        System.out.println("Sales statistics updated. Amount: " + saleAmount);
    }
}
