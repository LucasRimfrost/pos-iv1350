package se.kth.iv1350.pos.dto;

import se.kth.iv1350.pos.util.Amount;

/**
 * Data Transfer Object (DTO) for an item in inventory.
 * This class is immutable.
 */
public final class ItemDTO {
    private final String itemID;
    private final String name;
    private final String description;
    private final Amount price;
    private final double vatRate;

    /**
     * Creates a new instance representing an item.
     *
     * @param itemID The item identifier.
     * @param name The item name.
     * @param description The item description.
     * @param price The item price, without VAT.
     * @param vatRate The VAT rate for the item (e.g., 0.25 for 25%).
     */
    public ItemDTO(String itemID, String name, String description, Amount price, double vatRate) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.vatRate = vatRate;
    }

    /**
     * Gets the item identifier.
     *
     * @return The item identifier.
     */
    public String getItemID() {
        return itemID;
    }

    /**
     * Gets the item name.
     *
     * @return The item name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the item description.
     *
     * @return The item description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the item price, excluding VAT.
     *
     * @return The item price, excluding VAT.
     */
    public Amount getPrice() {
        return price;
    }

    /**
     * Gets the VAT rate for the item.
     *
     * @return The VAT rate, for example 0.25 for 25%.
     */
    public double getVatRate() {
        return vatRate;
    }

    /**
     * Gets the VAT amount for this item.
     *
     * @return The VAT amount.
     */
    public Amount getVatAmount() {
        return price.multiply(vatRate);
    }

    /**
     * Gets the price including VAT.
     *
     * @return The price including VAT.
     */
    public Amount getPriceWithVat() {
        return price.add(getVatAmount());
    }
}
