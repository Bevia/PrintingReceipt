package org.example;
import org.json.JSONArray;
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

public class JSONExtractorWithRelatedTx {
    public static void main(String[] args) {
        JSONObject jsonObject = getJsonObjectWithRelatedTransactions();

        System.out.println(jsonObject.toString(4));

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

        // Validate related_transactions: Check if it exists and is NOT null
        JSONArray relatedTransactions = data.has("related_transactions") && !data.isNull("related_transactions")
                ? data.getJSONArray("related_transactions")
                : null; // Assign null if missing or explicitly set to null

        if (relatedTransactions != null) {
            System.out.println("Related Transactions:");
            for (int i = 0; i < relatedTransactions.length(); i++) {
                JSONObject transaction = relatedTransactions.getJSONObject(i);

                // Extract transaction_id
                long transactionId = transaction.getLong("transaction_id");

                // Extract payment_details safely
                JSONObject paymentDetails = transaction.has("payment_details") && !transaction.isNull("payment_details")
                        ? transaction.getJSONObject("payment_details")
                        : null;

                // Extract acquirer_reference_number safely
                String acquirerReferenceNumber = paymentDetails != null && paymentDetails.has("acquirer_reference_number")
                        ? paymentDetails.getString("acquirer_reference_number")
                        : "Not available";

                System.out.println("  Transaction " + (i + 1) + ":");
                System.out.println("    Transaction ID: " + transactionId);
                System.out.println("    Acquirer Reference Number: " + acquirerReferenceNumber);

                if (paymentDetails != null) {
                    System.out.println("    Payment Details: " + paymentDetails.toString(4));
                } else {
                    System.out.println("    Payment Details: Not available");
                }
            }
        } else {
            System.out.println("Related Transactions: Not available (null)");
        }

        boolean success = jsonObject.getBoolean("success");

        // Simulating setting values to TextViews
        System.out.println("Address: " + address);
        System.out.println("Completed: " + completed);
        System.out.println("Card Entry Mode: " + cardEntryMode);
        System.out.println("Application ID: " + applicationId);
        System.out.println("Terminal ID: " + terminalId);
        System.out.println("Related Transactions: " + relatedTransactions);
        System.out.println("Success: " + success);
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
}
