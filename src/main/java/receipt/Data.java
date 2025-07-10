package receipt;

import java.util.List;

public class Data {
    private String printedOn;
    private Merchant merchant;
    private Payment payment;
    private Order order;
    private Item item;
    private List<RelatedTransaction> relatedTransactions;

    // Getters and Setters
    public String getPrintedOn() { return printedOn; }
    public void setPrintedOn(String printedOn) { this.printedOn = printedOn; }

    public Merchant getMerchant() { return merchant; }
    public void setMerchant(Merchant merchant) { this.merchant = merchant; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public List<RelatedTransaction> getRelatedTransactions() { return relatedTransactions; }
    public void setRelatedTransactions(List<RelatedTransaction> relatedTransactions) { this.relatedTransactions = relatedTransactions;

    }
}
