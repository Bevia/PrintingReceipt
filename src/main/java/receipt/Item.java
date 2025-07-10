package receipt;

import java.math.BigDecimal;

public class Item {
    private String name;
    private String currency;
    private BigDecimal unitPrice;
    private BigDecimal itemPrice;
    private int quantity;

    // Getters and setters...
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getItemPrice() { return itemPrice; }
    public void setItemPrice(BigDecimal itemPrice) { this.itemPrice = itemPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getTotalPrice() { return itemPrice.multiply(BigDecimal.valueOf(quantity)); }
    public void setTotalPrice(BigDecimal totalPrice) { this.itemPrice = totalPrice; }

}