package receipt;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {
    public static JsonResponse parseJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject data = jsonObject.getJSONObject("data");

        // Map values to Merchant object
        Merchant merchant = new Merchant();
        if (data.has("merchant")) {
            JSONObject merchantJson = data.getJSONObject("merchant");
            merchant.setAddress(merchantJson.optString("address", "Not available"));
            merchant.setName(merchantJson.optString("name", "Not available"));
        }

        // Map values to Payment object
        Payment payment = new Payment();
        if (data.has("payment")) {
            JSONObject paymentJson = data.getJSONObject("payment");
            payment.setApplicationId(paymentJson.optString("application_id", "Not available"));
            payment.setAuthorizationCode(paymentJson.optString("authorization_code", "Not available"));
            payment.setCardholderVerificationMethod(paymentJson.optString("cardholder_verification_method", "Not available"));
            payment.setCardEntryMode(paymentJson.optString("card_entry_mode", "Not available"));
            payment.setIssuerBin(paymentJson.optString("issuer_bin", "Not available"));
            payment.setIssuerCountryCode(paymentJson.optString("issuer_country_code", "Not available"));
            payment.setLast4(paymentJson.optString("last4", "Not available"));
            payment.setPaymentMethod(paymentJson.optString("payment_method", "Not available"));
            payment.setResponseCode(paymentJson.optString("response_code", "Not available"));
            payment.setTerminalId(paymentJson.optString("terminal_id", "Not available"));
        }

        // Map values to Order object
        Order order = new Order();
        if (data.has("order")) {
            JSONObject orderJson = data.getJSONObject("order");
            order.setTransactionId(orderJson.optString("transaction_id", "Not available"));
            order.setAmountRefunded(orderJson.optInt("amountrefunded", 0));
            order.setAmount(orderJson.optInt("amount", 0));
            order.setCurrency(orderJson.optString("currency", "Not available"));
            order.setCompleted(orderJson.optString("completed", "Not available"));
        }

        // Extract related transactions list
        List<RelatedTransaction> relatedTransactions = new ArrayList<>();
        if (data.has("related_transactions") && !data.isNull("related_transactions")) {
            JSONArray relatedTransactionsArray = data.getJSONArray("related_transactions");
            for (int i = 0; i < relatedTransactionsArray.length(); i++) {
                JSONObject transactionJson = relatedTransactionsArray.getJSONObject(i);
                RelatedTransaction transaction = new RelatedTransaction();
                transaction.setTransactionId(transactionJson.optLong("transaction_id", 0));
                transaction.setAmount(transactionJson.optInt("amount", 0));
                transaction.setReferenceTransactionId(transactionJson.optLong("reference_transaction_id", 0));
                transaction.setOrderId(transactionJson.optString("order_id", "Not available"));
                transaction.setCurrency(transactionJson.optString("currency", "Not available"));

                if (transactionJson.has("payment_details")) {
                    JSONObject paymentDetailsJson = transactionJson.getJSONObject("payment_details");
                    PaymentDetails paymentDetails = new PaymentDetails();
                    paymentDetails.setAcquirerReferenceNumber(paymentDetailsJson.optString("acquirer_reference_number", "Not available"));
                    transaction.setPaymentDetails(paymentDetails);
                }

                relatedTransactions.add(transaction);
            }
        }

        // Create Data object
        Data parsedData = new Data();
        parsedData.setPrintedOn(data.optString("printed_on", "Not available"));
        parsedData.setMerchant(merchant);
        parsedData.setPayment(payment);
        parsedData.setOrder(order);
        parsedData.setRelatedTransactions(relatedTransactions);

        // Create JsonResponse object
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setData(parsedData);
        jsonResponse.setSuccess(jsonObject.optBoolean("success", false));

        return jsonResponse;
    }

    public static void main(String[] args) {
        // Example JSON
        String jsonString =  "{"
                + "\"data\": {"
                + "\"merchant\": {"
                + "\"address\": \"Vijzelstraat Amsterdam\","
                + "\"name\": \"MultiSafepay B.V.\""
                + "},"
                + "\"order\": {"
                + "\"amount\": 15,"
                + "\"amountrefunded\": 15,"
                + "\"completed\": \"2025-05-14T14:57:17\","
                + "\"currency\": \"EUR\","
                + "\"items\": null,"
                + "\"tip\": null,"
                + "\"transaction_id\": \"1747227436104103\""
                + "},"
                + "\"payment\": {"
                + "\"application_id\": \"a0000000031010\","
                + "\"authorization_code\": \"UU1YSN\","
                + "\"card_acceptor_location\": \"Amsterdam\","
                + "\"card_entry_mode\": \"ICC_CONTACTLESS\","
                + "\"card_expiry_date\": \"3112\","
                + "\"cardholder_verification_method\": \"FAILED\","
                + "\"issuer_bin\": \"416598\","
                + "\"issuer_country_code\": \"LT\","
                + "\"last4\": \"6777\","
                + "\"payment_method\": \"VISA\","
                + "\"response_code\": \"00\","
                + "\"terminal_id\": \"000006F3\""
                + "},"
                + "\"printed_on\": \"2025-05-14T14:59:02\","
                + "\"related_transactions\": ["
                + "{"
                + "\"amount\": 10,"
                + "\"created\": \"2025-05-14T14:57:41\","
                + "\"currency\": \"EUR\","
                + "\"description\": \"Refund order 86231067 for Alipay+POS\","
                + "\"items\": null,"
                + "\"modified\": \"2025-05-14T14:57:41\","
                + "\"order_id\": \"86231067\","
                + "\"payment_details\": {"
                + "\"acquirer_reference_number\": \"74347495134000462532670\""
                + "},"
                + "\"reference_transaction_id\": 1747227436104103,"
                + "\"status\": \"completed\","
                + "\"transaction_id\": 1747227461107399,"
                + "\"type\": \"refund\""
                + "},"
                + "{"
                + "\"amount\": 5,"
                + "\"created\": \"2025-05-14T14:57:53\","
                + "\"currency\": \"EUR\","
                + "\"description\": \"Refund order 86231067 for Alipay+POS\","
                + "\"items\": null,"
                + "\"modified\": \"2025-05-14T14:57:53\","
                + "\"order_id\": \"86231067\","
                + "\"payment_details\": {"
                + "\"acquirer_reference_number\": \"74347495134000462532886\""
                + "},"
                + "\"reference_transaction_id\": 1747227436104103,"
                + "\"status\": \"completed\","
                + "\"transaction_id\": 1747227473109273,"
                + "\"type\": \"refund\""
                + "}"
                + "]"
                + "},"
                + "\"success\": true"
                + "}"; // Insert your JSON here

        JsonResponse response = parseJson(jsonString);

        // Example Output
        System.out.println("Merchant Address: " + response.getData().getMerchant().getAddress());
        System.out.println("Payment Method: " + response.getData().getPayment().getPaymentMethod());
        System.out.println("Order Transaction ID: " + response.getData().getOrder().getTransactionId());

        // Print related transactions
        System.out.println("Related Transactions:");
        for (RelatedTransaction transaction : response.getData().getRelatedTransactions()) {
            System.out.println("  Transaction ID: " + transaction.getTransactionId());
            System.out.println("  Acquirer Reference Number: " + transaction.getPaymentDetails().getAcquirerReferenceNumber());
        }

        System.out.println("Success: " + response.isSuccess());

    }
}
