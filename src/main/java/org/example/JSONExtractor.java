package org.example;
import org.json.JSONObject;

// Add this dependency to your build.gradle file:
// implementation 'org.json:json:20231013'
//
// Or if using Maven, add to pom.xml:
// <dependency>
//     <groupId>org.json</groupId>
//     <artifactId>json</artifactId>
//     <version>20231013</version>
// </dependency>

public class JSONExtractor {
    public static void main(String[] args) {
        JSONObject jsonObject = getJsonObject();
        // Print raw JSON string
        //System.out.println("Raw JSON:\n" + jsonString);
        // Pretty-print JSON structure
        System.out.println(jsonObject.toString(4));

        //data
        // |--merchant
        // |--order
        // |--payment

        JSONObject data = jsonObject.getJSONObject("data");

        // Extract values
        String address = data.getJSONObject("merchant").getString("address");
        String completed = data.getJSONObject("order").getString("completed");

        // Check if the key exists before accessing
        JSONObject paymentData = data.optJSONObject("payment");
        String cardEntryMode = paymentData != null && paymentData.has("card_entry_mode")
                ? paymentData.getString("card_entry_mode")
                : "Not available";

        String applicationId = data.getJSONObject("payment").getString("application_id");
        String terminalId = data.getJSONObject("payment").getString("terminal_id");

        String relatedTransactions = data.isNull("related_transactions") ? "null"
                                   : data.getString("related_transactions");

        boolean success = jsonObject.getBoolean("success");

        // Simulating setting values to TextViews
        System.out.println("Address: " + address);
        System.out.println("Completed: " + completed);
        System.out.println("cardEntryMode: " + cardEntryMode);
        System.out.println("Application ID: " + applicationId);
        System.out.println("Terminal ID: " + terminalId);
        System.out.println("Related Transactions: " + relatedTransactions);
        System.out.println("Success: " + success);
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
