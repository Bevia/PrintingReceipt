package receipt;

import java.math.BigDecimal;

public class Order {
    private String transactionId;
    private BigDecimal amountRefunded;
    private BigDecimal amount;
    private BigDecimal tip;
    private String currency;
    private String completed;

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public BigDecimal getAmountRefunded() { return amountRefunded; }
    public void setAmountRefunded(BigDecimal amountRefunded) { this.amountRefunded = amountRefunded; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCompleted() { return completed; }
    public void setCompleted(String completed) { this.completed = completed; }

    public BigDecimal getTip() {return tip;}
    public void setTip(BigDecimal tip) {this.tip = tip;}
}
