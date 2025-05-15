package receipt;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class JsonUtils {

    public static HashMap<String, String> convertToMap(JsonResponse jsonResponse) {
        //HashMap<String, String> mappedData = new HashMap<>();
        LinkedHashMap<String, String> mappedData = new LinkedHashMap<>();

        Data data = jsonResponse.getData();
        mappedData.put("printed_on", data.getPrintedOn());
        //mappedData.put("success", String.valueOf(jsonResponse.isSuccess()));

        // Merchant
        mappedData.put("merchant_address", data.getMerchant().getAddress());
        mappedData.put("merchant_name", data.getMerchant().getName());

        // Payment
        mappedData.put("payment_method", data.getPayment().getPaymentMethod());
        mappedData.put("card_entry_mode", data.getPayment().getCardEntryMode());
        mappedData.put("authorization_code", data.getPayment().getAuthorizationCode());
        mappedData.put("terminal_id", data.getPayment().getTerminalId());

        // Order
        mappedData.put("order_transaction_id", data.getOrder().getTransactionId());
        mappedData.put("order_amount", String.valueOf(data.getOrder().getAmount()));
        mappedData.put("order_currency", data.getOrder().getCurrency());
        mappedData.put("order_tip", String.valueOf(data.getOrder().getTip()));

        // Related Transactions
        List<RelatedTransaction> relatedTransactions = data.getRelatedTransactions();
        if (relatedTransactions != null && !relatedTransactions.isEmpty()) {
            StringBuilder transactionIds = new StringBuilder();
            for (RelatedTransaction transaction : relatedTransactions) {
                transactionIds.append(transaction.getTransactionId()).append(", ");
                mappedData.put("acquirer_ref_" + transaction.getTransactionId(), transaction.getPaymentDetails().getAcquirerReferenceNumber());
            }
            mappedData.put("related_transaction_ids", transactionIds.toString().replaceAll(", $", ""));
        } else {
            mappedData.put("related_transaction_ids", "None");
        }

        return mappedData;
    }
}
