package receipt;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ReceiptPrinter {
    private static final String RECEIPT_HEADER = "--------- Receipt ---------";
    private static final String RECEIPT_FOOTER = "---------------------------";
    private static final String NONE = "None";
    private static final String CURRENCY_PREFIX = "CURRENCY: ";
    private static final String AMOUNT_PREFIX = "AMOUNT: ";
    private static final String TIP_PREFIX = "TIP: ";
    private static final String AMOUNT_REFUNDED = "AMOUNT REFUNDED: ";

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
        appendItems(); // 👈 Add this line
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
        appendLine("Printed On", getValue(KeyMappings.PRINTED_ON.getKey(), data.getPrintedOn()));
        appendLine("Merchant Name", getValue(KeyMappings.MERCHANT_NAME.getKey(), data.getMerchant().getName()));
        appendLine("Merchant Address", getValue(KeyMappings.MERCHANT_ADDRESS.getKey(), data.getMerchant().getAddress()));
        appendLine("Payment Method", getValue(KeyMappings.PAYMENT_METHOD.getKey(), data.getPayment().getPaymentMethod()));
        appendLine("Transaction ID", getValue(KeyMappings.ORDER_TRANSACTION_ID.getKey(), data.getOrder().getTransactionId()));
    }

    private void appendMonetaryInfo() {
        Order order = jsonResponse.getData().getOrder();
        appendMoneyAmount(AMOUNT_PREFIX, order.getAmount());
        appendMoneyAmount(TIP_PREFIX, order.getTip());
        appendMoneyAmount(AMOUNT_REFUNDED, order.getAmountRefunded());
        receiptBuilder.append(CURRENCY_PREFIX)
                .append(getValue(KeyMappings.ORDER_CURRENCY.getKey(), order.getCurrency()))
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

    private void appendItems() {
        List<Item> items = jsonResponse.getData().getOrder().getItems();
        if (items == null || items.isEmpty()) {
            receiptBuilder.append("Items: None\n");
            return;
        }

        receiptBuilder.append("Items:\n");
        for (Item item : items) {
            String line = String.format("- %s x%d @ %.2f %s",
                    item.getName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getCurrency());
            receiptBuilder.append(line).append("\n");
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