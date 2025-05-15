package receipt;

public class Order {
    private String transactionId;
    private int amountRefunded;
    private int amount;
    private String currency;
    private String completed;

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public int getAmountRefunded() { return amountRefunded; }
    public void setAmountRefunded(int amountRefunded) { this.amountRefunded = amountRefunded; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCompleted() { return completed; }
    public void setCompleted(String completed) { this.completed = completed; }
}
