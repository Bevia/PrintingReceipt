package receipt;

import java.math.BigDecimal;

public class RelatedTransaction {
    private long transactionId;
    private BigDecimal amount;
    private long referenceTransactionId;
    private String orderId;
    private String currency;
    private PaymentDetails paymentDetails;

    // Getters and Setters
    public long getTransactionId() { return transactionId; }
    public void setTransactionId(long transactionId) { this.transactionId = transactionId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public long getReferenceTransactionId() { return referenceTransactionId; }
    public void setReferenceTransactionId(long referenceTransactionId) { this.referenceTransactionId = referenceTransactionId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public PaymentDetails getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(PaymentDetails paymentDetails) { this.paymentDetails = paymentDetails; }
}
