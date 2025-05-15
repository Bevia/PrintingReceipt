package receipt;

import java.math.BigDecimal;
import java.util.HashMap;

import java.math.BigDecimal;
import java.util.HashMap;

public class ReceiptPrinter {
    private JsonResponse jsonResponse;
    private HashMap<String, String> receiptData;

    // Constructor: Accept both JsonResponse and HashMap
    public ReceiptPrinter(JsonResponse jsonResponse, HashMap<String, String> receiptData) {
        this.jsonResponse = jsonResponse;
        this.receiptData = receiptData;
    }

    // Print receipt method
    public void printReceipt() {
        System.out.println("--------- Receipt ---------");

        // Retrieve from JsonResponse (model) or fallback to HashMap
        System.out.println("Printed On: " + getValue("printed_on", jsonResponse.getData().getPrintedOn()));
        System.out.println("Merchant Name: " + getValue("merchant_name", jsonResponse.getData().getMerchant().getName()));
        System.out.println("Merchant Address: " + getValue("merchant_address", jsonResponse.getData().getMerchant().getAddress()));
        System.out.println("Payment Method: " + getValue("payment_method", jsonResponse.getData().getPayment().getPaymentMethod()));
        System.out.println("Transaction ID: " + getValue("order_transaction_id", jsonResponse.getData().getOrder().getTransactionId()));

        System.out.println("AMOUNT: " + getValue("order_amount", jsonResponse.getData().getOrder().getAmount().toString()));

        // Handle TIP safely
        BigDecimal tipAmount = jsonResponse.getData().getOrder().getTip();
        if (tipAmount.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("TIP: " + tipAmount);
        }

        System.out.println("CURRENCY: " + getValue("order_currency", jsonResponse.getData().getOrder().getCurrency()));

        // Related Transactions (print only if present)
        String relatedTransactionIds = receiptData.getOrDefault("related_transaction_ids", "None");
        if (!relatedTransactionIds.equals("None")) {
            System.out.println("Related Transaction IDs: " + relatedTransactionIds);
        }

        System.out.println("---------------------------");
    }

    // Helper method to retrieve value from JsonResponse or HashMap
    private String getValue(String key, String modelValue) {
        String mapValue = receiptData.getOrDefault(key, "N/A");
        return !mapValue.equals("N/A") ? mapValue : modelValue;
    }
}

