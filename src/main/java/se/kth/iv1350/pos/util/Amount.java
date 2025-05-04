package se.kth.iv1350.pos.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents an immutable amount of money.
 */
public final class Amount {
    private final BigDecimal value;

    /**
     * Creates a new instance representing the specified amount.
     *
     * @param value The amount represented by the newly created instance.
     */
    public Amount(BigDecimal value) {
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Creates a new instance representing the specified amount.
     *
     * @param value The amount represented by the newly created instance.
     */
    public Amount(double value) {
        this(BigDecimal.valueOf(value));
    }

    /**
     * Creates a new instance with zero value.
     */
    public Amount() {
        this(0.0);
    }

    /**
     * Adds the specified amount to this amount.
     *
     * @param other The amount to add.
     * @return The sum of this and the specified amount.
     */
    public Amount add(Amount other) {
        return new Amount(this.value.add(other.value));
    }

    /**
     * Subtracts the specified amount from this amount.
     *
     * @param other The amount to subtract.
     * @return The difference between this and the specified amount.
     */
    public Amount subtract(Amount other) {
        return new Amount(this.value.subtract(other.value));
    }

    /**
     * Multiplies this amount with the specified factor.
     *
     * @param factor The factor to multiply with.
     * @return The product of this amount and the specified factor.
     */
    public Amount multiply(double factor) {
        return new Amount(this.value.multiply(BigDecimal.valueOf(factor)));
    }

    /**
     * Gets the value of this amount.
     *
     * @return The value of this amount.
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Checks if this amount is greater than zero.
     *
     * @return true if this amount is greater than zero, false otherwise.
     */
    public boolean isPositive() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public String toString() {
        return String.format("%.2f SEK", value.doubleValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Amount)) {
            return false;
        }
        Amount other = (Amount) obj;
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
