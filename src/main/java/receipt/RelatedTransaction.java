package receipt;

public class RelatedTransaction {
    private long transactionId;
    private int amount;
    private long referenceTransactionId;
    private String orderId;
    private String currency;
    private PaymentDetails paymentDetails;

    // Getters and Setters
    public long getTransactionId() { return transactionId; }
    public void setTransactionId(long transactionId) { this.transactionId = transactionId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public long getReferenceTransactionId() { return referenceTransactionId; }
    public void setReferenceTransactionId(long referenceTransactionId) { this.referenceTransactionId = referenceTransactionId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public PaymentDetails getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(PaymentDetails paymentDetails) { this.paymentDetails = paymentDetails; }
}
