package receipt;

import java.math.BigDecimal;
import java.util.Map;

public class ReceiptPrinter {
    private static final String RECEIPT_HEADER = "--------- Receipt ---------";
    private static final String RECEIPT_FOOTER = "---------------------------";
    private static final String NONE = "None";
    private static final String CURRENCY_PREFIX = "CURRENCY: ";
    private static final String AMOUNT_PREFIX = "AMOUNT: ";
    private static final String TIP_PREFIX = "TIP: ";

    private final JsonResponse jsonResponse;
    private final Map<String, String> receiptData;
    private final StringBuilder receiptBuilder;

    public ReceiptPrinter(JsonResponse jsonResponse, Map<String, String> receiptData) {
        this.jsonResponse = jsonResponse;
        this.receiptData = receiptData;
        this.receiptBuilder = new StringBuilder();
    }

    public void printReceipt() {
        buildReceiptHeader();
        appendBasicInfo();
        appendMonetaryInfo();
        appendRelatedTransactions();
        buildReceiptFooter();

        System.out.println(receiptBuilder.toString());
    }

    private void buildReceiptHeader() {
        receiptBuilder.append(RECEIPT_HEADER).append("\n");
    }

    private void appendBasicInfo() {
        Data data = jsonResponse.getData();
        appendLine("Printed On", getValue("printed_on", data.getPrintedOn()));
        appendLine("Merchant Name", getValue("merchant_name", data.getMerchant().getName()));
        appendLine("Merchant Address", getValue("merchant_address", data.getMerchant().getAddress()));
        appendLine("Payment Method", getValue("payment_method", data.getPayment().getPaymentMethod()));
        appendLine("Transaction ID", getValue("order_transaction_id", data.getOrder().getTransactionId()));
    }

    private void appendMonetaryInfo() {
        Order order = jsonResponse.getData().getOrder();
        appendMoneyAmount(AMOUNT_PREFIX, order.getAmount());
        appendMoneyAmount(TIP_PREFIX, order.getTip());
        receiptBuilder.append(CURRENCY_PREFIX)
                .append(getValue("order_currency", order.getCurrency()))
                .append("\n");
    }

    private void appendMoneyAmount(String prefix, BigDecimal amount) {
        if (isPositiveAmount(amount)) {
            receiptBuilder.append(prefix).append(amount).append("\n");
        }
    }

    private void appendRelatedTransactions() {
        String relatedTransactionIds = receiptData.getOrDefault("related_transaction_ids", NONE);
        if (!NONE.equals(relatedTransactionIds)) {
            appendLine("Related Transaction IDs", relatedTransactionIds);
        }
    }

    private void buildReceiptFooter() {
        receiptBuilder.append(RECEIPT_FOOTER).append("\n");
    }

    private void appendLine(String label, String value) {
        receiptBuilder.append(label).append(": ").append(value).append("\n");
    }

    private boolean isPositiveAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private String getValue(String key, String modelValue) {
        String mapValue = receiptData.getOrDefault(key, "N/A");
        return !"N/A".equals(mapValue) ? mapValue : modelValue;
    }
}