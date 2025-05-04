package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

/**
 * Represents a cash register. Contains all sales-related payments.
 */
public class CashRegister {
    private Amount balance;

    /**
     * Creates a new instance with an initial balance of zero.
     */
    public CashRegister() {
        this.balance = new Amount();
    }

    /**
     * Creates a new instance with the specified initial balance.
     *
     * @param initialBalance The initial balance.
     */
    public CashRegister(Amount initialBalance) {
        this.balance = initialBalance;
    }

    /**
     * Records a payment. The paid amount is added to the balance.
     *
     * @param payment The payment to record.
     */
    public void addPayment(CashPayment payment) {
        balance = balance.add(payment.getAmount());
    }

    /**
     * Gets the current balance.
     *
     * @return The current balance.
     */
    public Amount getBalance() {
        return balance;
    }
}
