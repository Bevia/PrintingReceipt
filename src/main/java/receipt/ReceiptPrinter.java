package receipt;

import java.math.BigDecimal;
import java.util.HashMap;

public class ReceiptPrinter {
    private HashMap<String, String> receiptData;

    // Constructor
    public ReceiptPrinter(HashMap<String, String> receiptData) {
        this.receiptData = receiptData;
    }

    // Print receipt method
    public void printReceipt() {
        System.out.println("--------- Receipt ---------");
        System.out.println("Printed On: " + receiptData.getOrDefault("printed_on", "N/A"));
        System.out.println("Merchant Address: " + receiptData.getOrDefault("merchant_address", "N/A"));
        System.out.println("Payment Method: " + receiptData.getOrDefault("payment_method", "N/A"));
        System.out.println("Transaction ID: " + receiptData.getOrDefault("order_transaction_id", "N/A"));

        System.out.println("AMOUNT: " + receiptData.getOrDefault("order_amount", "N/A"));

        String tipValue = receiptData.getOrDefault("order_tip", "0");

        try {
            BigDecimal tipAmount = new BigDecimal(tipValue); // Convert String to BigDecimal
            if (tipAmount.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("TIP: " + tipValue);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid tip value: " + tipValue);
        }

        System.out.println("CURRENCY: " + receiptData.getOrDefault("order_currency", "N/A"));

        String relatedTransactionIds = receiptData.getOrDefault("related_transaction_ids", "N/A");

        if (!relatedTransactionIds.equals("None")) {
            System.out.println("Related Transaction IDs: " + relatedTransactionIds);
        }

        System.out.println("---------------------------");
    }
}
