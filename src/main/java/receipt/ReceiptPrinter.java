package receipt;

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
        System.out.println("Related Transaction IDs: " + receiptData.getOrDefault("related_transaction_ids", "N/A"));
        System.out.println("Success: " + receiptData.getOrDefault("success", "N/A"));
        System.out.println("---------------------------");
    }
}
