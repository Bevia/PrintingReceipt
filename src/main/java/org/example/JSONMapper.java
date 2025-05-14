package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;

public class JSONMapper {
    public static void main(String[] args) {
        // Your JSON string
        //JSONObject jsonObject = getJsonObjectWithRelatedTransactions();
        JSONObject jsonObject = getJsonObject();

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
    }

    private static JSONObject getJsonObjectWithRelatedTransactions() {
        String jsonString = "{"
                + "\"data\": {"
                + "\"merchant\": {"
                + "\"address\": \"Rue de la Gares test1 fdsas\","
                + "\"name\": \"MultiSafepay B.V.\""
                + "},"
                + "\"order\": {"
                + "\"amount\": 150,"
                + "\"amountrefunded\": 75,"
                + "\"completed\": \"2024-09-10T10:03:50\","
                + "\"currency\": \"EUR\","
                + "\"items\": null,"
                + "\"tip\": {"
                + "\"amount\": 50"
                + "},"
                + "\"transaction_id\": \"1725955429106316\""
                + "},"
                + "\"payment\": {"
                + "\"application_id\": \"a0000000041010\","
                + "\"authorization_code\": \"006459\","
                + "\"card_acceptor_location\": \"fdsas\","
                + "\"card_entry_mode\": \"ICC_CONTACTLESS\","
                + "\"card_expiry_date\": \"2709\","
                + "\"cardholder_verification_method\": \"PLAINTEXT_PIN_OFFLINE\","
                + "\"issuer_bin\": \"516788\","
                + "\"issuer_country_code\": \"ES\","
                + "\"last4\": \"5477\","
                + "\"payment_method\": \"MASTERCARD\","
                + "\"response_code\": \"00\","
                + "\"terminal_id\": \"0000037E\""
                + "},"
                + "\"printed_on\": \"2024-12-03T12:18:50\","
                + "\"related_transactions\": ["
                + "{"
                + "\"amount\": 25,"
                + "\"created\": \"2024-09-10T10:05:06\","
                + "\"currency\": \"EUR\","
                + "\"description\": \"Refund order 310355912 for Alipay+POS\","
                + "\"items\": null,"
                + "\"modified\": \"2024-09-10T10:05:06\","
                + "\"order_id\": \"310355912\","
                + "\"payment_details\": {"
                + "\"acquirer_reference_number\": \"02692424254000001108772\""
                + "},"
                + "\"reference_transaction_id\": 1725955429106316,"
                + "\"status\": \"completed\","
                + "\"transaction_id\": 1725955506106330,"
                + "\"type\": \"refund\""
                + "},"
                + "{"
                + "\"amount\": 50,"
                + "\"created\": \"2024-09-10T10:05:29\","
                + "\"currency\": \"EUR\","
                + "\"description\": \"Refund order 310355912 for Alipay+POS\","
                + "\"items\": null,"
                + "\"modified\": \"2024-09-10T10:05:29\","
                + "\"order_id\": \"310355912\","
                + "\"payment_details\": {"
                + "\"acquirer_reference_number\": \"02692424254000001108780\""
                + "},"
                + "\"reference_transaction_id\": 1725955429106316,"
                + "\"status\": \"completed\","
                + "\"transaction_id\": 1725955529106353,"
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
                + "\"address\": \"Rue de la Gares test1 fdsas\","
                + "\"name\": \"MultiSafepay B.V.\""
                + "},"
                + "\"order\": {"
                + "\"amount\": 100,"
                + "\"amountrefunded\": 0,"
                + "\"completed\": \"2024-12-19T14:16:13\","
                + "\"currency\": \"EUR\","
                + "\"items\": null,"
                + "\"tip\": null,"
                + "\"transaction_id\": \"1734614172108670\""
                + "},"
                + "\"payment\": {"
                + "\"application_id\": \"a0000000043060\","
                + "\"authorization_code\": \"006459\","
                + "\"card_acceptor_location\": \"fdsas\","
                + "\"card_entry_mode\": \"ICC_CONTACTLESS\","
                + "\"card_expiry_date\": \"2512\","
                + "\"cardholder_verification_method\": \"ENCIPHERED_PIN_ONLINE\","
                + "\"issuer_bin\": \"679999\","
                + "\"issuer_country_code\": null,"
                + "\"last4\": \"2010\","
                + "\"payment_method\": \"MAESTRO\","
                + "\"response_code\": \"00\","
                + "\"terminal_id\": \"00000DKJ\""
                + "},"
                + "\"printed_on\": \"2025-05-12T09:38:26\","
                + "\"related_transactions\": null"
                + "},"
                + "\"success\": true"
                + "}";

        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject;
    }
}


