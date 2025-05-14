package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;

public class JSONMapper {
    public static void main(String[] args) {
        // Your JSON string
        JSONObject jsonObject = getJsonObjectWithRelatedTransactions();
        //JSONObject jsonObject = getJsonObject();

        System.out.println(jsonObject.toString(4));

        JSONObject data = jsonObject.getJSONObject("data");

        // Create a HashMap to store extracted values
        HashMap<String, String> mappedData = new HashMap<>();

        // Validate and extract keys safely
        mappedData.put("printed_on", data.has("printed_on")
                ? data.getString("printed_on")
                : "Not available");
        mappedData.put("merchant_address", data.has("merchant") && data.getJSONObject("merchant").has("address")
                ? data.getJSONObject("merchant").getString("address")
                : "Not available");
        mappedData.put("cardholder_verification_method", data.has("payment") && data.getJSONObject("payment").has("cardholder_verification_method")
                ? data.getJSONObject("payment").getString("cardholder_verification_method")
                : "Not available");
        mappedData.put("card_acceptor_location", data.has("payment") && data.getJSONObject("payment").has("card_acceptor_location")
                ? data.getJSONObject("payment").getString("card_acceptor_location")
                : "Not available");
        mappedData.put("card_entry_mode", data.has("payment") && data.getJSONObject("payment").has("card_entry_mode")
                ? data.getJSONObject("payment").getString("card_entry_mode")
                : "Not available");
        mappedData.put("order_transaction_id", data.has("order") && data.getJSONObject("order").has("transaction_id")
                ? data.getJSONObject("order").getString("transaction_id")
                : "Not available");
        mappedData.put("success", jsonObject.has("success") ? String.valueOf(jsonObject.getBoolean("success")) : "Not available");

        // Validate related_transactions safely
        // Validate related_transactions and extract ALL transaction IDs
        if (data.has("related_transactions") && !data.isNull("related_transactions")) {
            JSONArray relatedTransactions = data.getJSONArray("related_transactions");

            if (!relatedTransactions.isEmpty()) {
                StringBuilder transactionIds = new StringBuilder();

                for (int i = 0; i < relatedTransactions.length(); i++) {
                    long transactionId = relatedTransactions.getJSONObject(i).getLong("transaction_id");
                    transactionIds.append(transactionId).append(i < relatedTransactions.length() - 1 ? ", " : "");
                }

                mappedData.put("related_transaction_ids", transactionIds.toString());
            } else {
                mappedData.put("related_transaction_ids", "Not available (empty array)");
            }
        } else {
            mappedData.put("related_transaction_ids", "Not available (null)");
        }

        // Print mapped data
        System.out.println("Validated JSON Data:");
        mappedData.forEach((key, value) -> System.out.println(key + " : " + value));

        // Get specific values from the mappedData HashMap
        String card_acceptor_location = mappedData.getOrDefault("card_acceptor_location", "Not available");
        String merchantAddress = mappedData.getOrDefault("merchant_address", "Not available");
        String cardholderVerificationMethod = mappedData.getOrDefault("cardholder_verification_method", "Not available");


        System.out.println("\nIndividually extracted values:");
        System.out.println("Card Acceptor Location: " + card_acceptor_location);
        System.out.println("Merchant Address: " + merchantAddress);
        System.out.println("Cardholder Verification Method: " + cardholderVerificationMethod);
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

        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject;
    }
}


