package receipt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class JSONParser {
    public static JsonResponse parseJson(JSONObject jsonObject) {
        try {
            JSONObject data = jsonObject.optJSONObject("data"); // Use optJSONObject safely

            if (data == null) {
                System.err.println("JSONParser: Missing 'data' object in JSON.");
                return null;
            }

            // Map values to Merchant object
            Merchant merchant = new Merchant();
            JSONObject merchantJson = data.optJSONObject("merchant");
            if (merchantJson != null) {
                merchant.setAddress(merchantJson.optString("address", "Not available"));
                merchant.setName(merchantJson.optString("name", "Not available"));
            }

            // Map values to Payment object
            Payment payment = new Payment();
            JSONObject paymentJson = data.optJSONObject("payment");
            if (paymentJson != null) {
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
            JSONObject orderJson = data.optJSONObject("order");
            if (orderJson != null) {
                order.setTransactionId(orderJson.optString("transaction_id", "Not available"));

                String amountStr = orderJson.optString("amount", "0");
                BigDecimal amount = amountStr.equals("null") || amountStr.isEmpty()
                        ? BigDecimal.ZERO
                        : new BigDecimal(amountStr).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                order.setAmount(amount);

                String tipStr = orderJson.optString("tip", "0");
                BigDecimal tip = tipStr.equals("null") || tipStr.isEmpty()
                        ? BigDecimal.ZERO
                        : new BigDecimal(tipStr).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                order.setTip(tip);

                String amountRefundedStr = orderJson.optString("amountrefunded", "0");
                BigDecimal amountRefunded = amountRefundedStr.equals("null") || amountRefundedStr.isEmpty()
                        ? BigDecimal.ZERO
                        : new BigDecimal(amountRefundedStr).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                order.setAmountRefunded(amountRefunded);

                order.setCurrency(orderJson.optString("currency", "Not available"));
                order.setCompleted(orderJson.optString("completed", "Not available"));
            }

            // Extract the related transactions list safely
            List<RelatedTransaction> relatedTransactions = new ArrayList<>();
            JSONArray relatedTransactionsArray = data.optJSONArray("related_transactions");
            if (relatedTransactionsArray != null) {
                for (int i = 0; i < relatedTransactionsArray.length(); i++) {
                    JSONObject transactionJson = relatedTransactionsArray.optJSONObject(i);
                    if (transactionJson != null) {
                        RelatedTransaction transaction = new RelatedTransaction();
                        transaction.setTransactionId(transactionJson.optLong("transaction_id", 0));

                        String amountStr = transactionJson.optString("amount", "0");
                        BigDecimal amount = amountStr.equals("null") || amountStr.isEmpty()
                                ? BigDecimal.ZERO
                                : new BigDecimal(amountStr).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                        order.setAmount(amount);

                        transaction.setReferenceTransactionId(transactionJson.optLong("reference_transaction_id", 0));
                        transaction.setOrderId(transactionJson.optString("order_id", "Not available"));
                        transaction.setCurrency(transactionJson.optString("currency", "Not available"));

                        JSONObject paymentDetailsJson = transactionJson.optJSONObject("payment_details");
                        if (paymentDetailsJson != null) {
                            PaymentDetails paymentDetails = new PaymentDetails();
                            paymentDetails.setAcquirerReferenceNumber(paymentDetailsJson.optString("acquirer_reference_number", "Not available"));
                            transaction.setPaymentDetails(paymentDetails);
                        }

                        relatedTransactions.add(transaction);
                    }
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

        } catch (Exception e) {
            System.err.println("JSONParser: Error parsing JSON: " + e.getMessage());// Better logging
            return null; // Return null to indicate failure (handle this in calling code)
        }
    }

    public static void main(String[] args) {
        // Example JSON
        //String jsonString = getStringWithRelatedTransaction();
        //String jsonString = getString();

        JSONObject jsonObject = getJsonObjectWithRelatedTransactions();
        //JSONObject jsonObject = getJsonObject();

        //JSONObject jsonObject = getJsonObject();
        System.out.println(jsonObject.toString(4));

        // Parse JSON into JsonResponse object
        JsonResponse response = JSONParser.parseJson(jsonObject);

        // Convert JsonResponse to HashMap
        HashMap<String, String> receiptDataMap = JsonUtils.convertToMap(Objects.requireNonNull(response)); // Convert JsonResponse to HashMap

        ReceiptPrinter printer = new ReceiptPrinter(response, receiptDataMap);
        printer.printReceipt();

    }

    private static JSONObject getJsonObjectWithRelatedTransactions() {
        String jsonString = "{"
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
                + "}";

        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject;
    }

    private static JSONObject getJsonObject() {

        String jsonString = "{"
                + "\"data\": {"
                + "\"merchant\": {"
                + "\"address\": \"Vijzelstraat Amsterdam\","
                + "\"name\": \"MultiSafepay B.V.\""
                + "},"
                + "\"order\": {"
                + "\"amount\": 200,"
                + "\"amountrefunded\": 0,"
                + "\"completed\": \"2025-05-13T12:10:56\","
                + "\"currency\": \"EUR\","
                + "\"items\": null,"
                + "\"tip\": 423,"
                + "\"transaction_id\": \"1747131054104206\""
                + "},"
                + "\"payment\": {"
                + "\"application_id\": \"a0000000041010\","
                + "\"authorization_code\": \"426045\","
                + "\"card_acceptor_location\": \"Amsterdam\","
                + "\"card_entry_mode\": \"ICC_CONTACTLESS\","
                + "\"card_expiry_date\": \"2908\","
                + "\"cardholder_verification_method\": \"PLAINTEXT_PIN_OFFLINE\","
                + "\"issuer_bin\": \"545366\","
                + "\"issuer_country_code\": \"ES\","
                + "\"last4\": \"2473\","
                + "\"payment_method\": \"MASTERCARD\","
                + "\"response_code\": \"00\","
                + "\"terminal_id\": \"000006DT\""
                + "},"
                + "\"printed_on\": \"2025-05-14T14:28:40\","
                + "\"related_transactions\": null"
                + "},"
                + "\"success\": true"
                + "}";

        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject;
    }

   /* private static String getStringWithRelatedTransaction() {
        // Insert your JSON here
        return "{"
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
                + "}";
    }

    private static String getString() {
        // Insert your JSON here
        return "{"
                + "\"data\": {"
                + "\"merchant\": {"
                + "\"address\": \"Vijzelstraat Amsterdam\","
                + "\"name\": \"MultiSafepay B.V.\""
                + "},"
                + "\"order\": {"
                + "\"amount\": 1,"
                + "\"amountrefunded\": 0,"
                + "\"completed\": \"2025-05-13T12:10:56\","
                + "\"currency\": \"EUR\","
                + "\"items\": null,"
                + "\"tip\": null,"
                + "\"transaction_id\": \"1747131054104206\""
                + "},"
                + "\"payment\": {"
                + "\"application_id\": \"a0000000041010\","
                + "\"authorization_code\": \"426045\","
                + "\"card_acceptor_location\": \"Amsterdam\","
                + "\"card_entry_mode\": \"ICC_CONTACTLESS\","
                + "\"card_expiry_date\": \"2908\","
                + "\"cardholder_verification_method\": \"PLAINTEXT_PIN_OFFLINE\","
                + "\"issuer_bin\": \"545366\","
                + "\"issuer_country_code\": \"ES\","
                + "\"last4\": \"2473\","
                + "\"payment_method\": \"MASTERCARD\","
                + "\"response_code\": \"00\","
                + "\"terminal_id\": \"000006DT\""
                + "},"
                + "\"printed_on\": \"2025-05-14T14:28:40\","
                + "\"related_transactions\": null"
                + "},"
                + "\"success\": true"
                + "}";

    }*/
}
