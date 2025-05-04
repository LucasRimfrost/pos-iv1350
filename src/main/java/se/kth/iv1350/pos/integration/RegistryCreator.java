package se.kth.iv1350.pos.integration;

/**
 * This class creates all registry classes, providing a single access point
 * to all external systems.
 */
public class RegistryCreator {
    private final ItemRegistry itemRegistry;
    private final DiscountRegistry discountRegistry;
    private final AccountingSystem accountingSystem;
    private final Printer printer;

    /**
     * Creates a new instance and initializes all needed registry classes and external system interfaces.
     */
    public RegistryCreator() {
        itemRegistry = new ItemRegistry();
        discountRegistry = new DiscountRegistry();
        accountingSystem = new AccountingSystem();
        printer = new Printer();
    }

    /**
     * Gets the item registry.
     *
     * @return The item registry.
     */
    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    /**
     * Gets the discount registry.
     *
     * @return The discount registry.
     */
    public DiscountRegistry getDiscountRegistry() {
        return discountRegistry;
    }

    /**
     * Gets the accounting system interface.
     *
     * @return The accounting system interface.
     */
    public AccountingSystem getAccountingSystem() {
        return accountingSystem;
    }

    /**
     * Gets the printer interface.
     *
     * @return The printer interface.
     */
    public Printer getPrinter() {
        return printer;
    }
}
