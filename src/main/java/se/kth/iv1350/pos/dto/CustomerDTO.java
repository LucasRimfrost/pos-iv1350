package se.kth.iv1350.pos.dto;

/**
 * Data Transfer Object (DTO) for customer information.
 * This class is immutable.
 */
public final class CustomerDTO {
    private final String customerID;
    private final String name;

    /**
     * Creates a new instance representing a customer.
     *
     * @param customerID The customer identifier.
     * @param name The customer name. May be null if not provided.
     */
    public CustomerDTO(String customerID, String name) {
        this.customerID = customerID;
        this.name = name;
    }

    /**
     * Creates a new instance with only customer ID.
     *
     * @param customerID The customer identifier.
     */
    public CustomerDTO(String customerID) {
        this(customerID, null);
    }

    /**
     * Gets the customer identifier.
     *
     * @return The customer identifier.
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * Gets the customer name.
     *
     * @return The customer name.
     */
    public String getName() {
        return name;
    }
}
