package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

/**
 * Represents a cash payment in a sale.
 * This class implements the Payment interface.
 */
public class CashPayment {
    private final Amount paidAmount;

    /**
     * Creates a new instance.
     *
     * @param paidAmount The amount paid.
     */
    public CashPayment(Amount paidAmount) {
        this.paidAmount = paidAmount;
    }

    /**
     * Gets the paid amount.
     *
     * @return The paid amount.
     */
    public Amount getAmount() {
        return paidAmount;
    }

    /**
     * Calculates the change to return to the customer.
     *
     * @param totalAmount The total amount of the sale.
     * @return The amount of change to return.
     */
    public Amount getChange(Amount totalAmount) {
        return paidAmount.subtract(totalAmount);
    }
}
