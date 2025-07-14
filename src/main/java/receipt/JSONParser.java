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
            if (data.has("merchant")) {
                JSONObject merchantJson = data.optJSONObject(KeyMappings.MERCHANT.getKey()); // Use `optJSONObject`
                if (merchantJson != null) {
                    merchant.setAddress(merchantJson.optString(KeyMappings.ADDRESS.getKey(), "Not available"));
                    merchant.setName(merchantJson.optString(KeyMappings.NAME.getKey(), "Not available"));
                }
            }

            // Map values to Payment object
            Payment payment = new Payment();
            if (data.has("payment")) {
                JSONObject paymentJson = data.optJSONObject("payment");
                if (paymentJson != null) {
                    payment.setApplicationId(paymentJson.optString(KeyMappings.APPLICATION_ID.getKey(), "Not available"));
                    payment.setAuthorizationCode(paymentJson.optString(KeyMappings.AUTHORIZATION_CODE.getKey(), "Not available"));
                    payment.setCardholderVerificationMethod(paymentJson.optString(KeyMappings.CARDHOLDER_VERIFICATION_METHOD.getKey(), "Not available"));
                    payment.setCardAcceptorLocation(paymentJson.optString(KeyMappings.CARD_ACCEPTOR_LOCATION.getKey(), "Not available"));
                    payment.setCardExpiryDate(paymentJson.optString(KeyMappings.CARD_EXPIRY_DATE.getKey(), "Not available"));
                    payment.setCardEntryMode(paymentJson.optString(KeyMappings.CARD_ENTRY_MODE.getKey(), "Not available"));
                    payment.setIssuerBin(paymentJson.optString(KeyMappings.ISSUER_BIN.getKey(), "Not available"));
                    payment.setIssuerCountryCode(paymentJson.optString(KeyMappings.ISSUER_COUNTRY_CODE.getKey(), "Not available"));
                    payment.setLast4(paymentJson.optString(KeyMappings.LAST4.getKey(), "Not available"));
                    payment.setPaymentMethod(paymentJson.optString(KeyMappings.PAYMENT_METHOD.getKey(), "Not available"));
                    payment.setResponseCode(paymentJson.optString(KeyMappings.RESPONSE_CODE.getKey(), "Not available"));
                    payment.setTerminalId(paymentJson.optString(KeyMappings.TERMINAL_ID.getKey(), "Not available"));
                }
            }

            // Map values to Order object
            Order order = new Order();
            if (data.has("order")) {
                JSONObject orderJson = data.optJSONObject("order");
                if (orderJson != null) {
                    order.setTransactionId(orderJson.optString(KeyMappings.TRANSACTION_ID.getKey(), "Not available"));

                    //amount:
                    int amountInt = orderJson.optInt(KeyMappings.AMOUNT.getKey(), 0); // Defaults to 0 if not present
                    BigDecimal amount = BigDecimal.valueOf(amountInt)
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                    order.setAmount(amount);

                    //tip:
                    BigDecimal tip = BigDecimal.ZERO;
                    if (orderJson.has(KeyMappings.TIP.getKey()) && !orderJson.isNull(KeyMappings.TIP.getKey())) {
                        JSONObject tipJson = orderJson.optJSONObject(KeyMappings.TIP.getKey());
                        if (tipJson != null) {
                            int tipInt = tipJson.optInt(KeyMappings.AMOUNT.getKey(), 0);
                            tip = BigDecimal.valueOf(tipInt)
                                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                        }
                    } else {
                        System.out.println("Warning: 'tip' is missing or null in the JSON!");
                    }
                    order.setTip(tip);

                    //amountRefunded:
                    if (!orderJson.has(KeyMappings.AMOUNT_REFUNDED.getKey()) || orderJson.isNull(KeyMappings.AMOUNT_REFUNDED.getKey())) {
                        System.out.println("Warning: 'amountrefunded' is missing or null in the JSON!");
                    }
                    int amountRefundedInt = orderJson.optInt(KeyMappings.AMOUNT_REFUNDED.getKey(), 0);
                    BigDecimal amountRefunded = BigDecimal.valueOf(amountRefundedInt)
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    order.setAmountRefunded(amountRefunded);

                    order.setCurrency(orderJson.optString(KeyMappings.CURRENCY.getKey(), "Not available"));
                    order.setCompleted(orderJson.optString("completed", "Not available"));

                    //Check if items exist
                    JSONObject itemsContainer = orderJson.optJSONObject("items");
                    if (itemsContainer != null) {
                        JSONArray itemsArray = itemsContainer.optJSONArray("items");
                        if (itemsArray != null) {
                            List<Item> items = new ArrayList<>();

                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject itemJson = itemsArray.optJSONObject(i);
                                if (itemJson != null) {
                                    Item item = new Item();

                                    // Parse basic fields
                                    item.setName(itemJson.optString(KeyMappings.NAME.getKey(), "Unnamed"));
                                    item.setCurrency(itemJson.optString(KeyMappings.CURRENCY.getKey(), "EUR"));
                                    item.setQuantity(itemJson.optInt("quantity", 1));

                                    // Parse unit price (in euros)
                                    String unitPriceStr = itemJson.optString(KeyMappings.UNIT_PRICE.getKey(), "0");
                                    BigDecimal unitPrice = unitPriceStr.equals("null") || unitPriceStr.isEmpty()
                                            ? BigDecimal.ZERO
                                            : new BigDecimal(unitPriceStr);
                                    item.setUnitPrice(unitPrice);

                                    // Calculate total: unit_price × quantity
                                    BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                                    item.setItemPrice(totalPrice);

                                    // Optional log for debugging
                                    System.out.println("SunmiPrinting: " + item.getName() +
                                            ", Qty: " + item.getQuantity() +
                                            ", Unit: " + item.getUnitPrice() +
                                            ", Total: " + item.getItemPrice());

                                    items.add(item);
                                }
                            }

                            order.setItems(items);
                        }
                    }
                }
            }

            // Extract the related transactions list safely
            List<RelatedTransaction> relatedTransactions = new ArrayList<>();
            JSONArray relatedTransactionsArray = data.optJSONArray(KeyMappings.RELATED_TRANSACTIONS.getKey());
            if (relatedTransactionsArray != null) {
                for (int i = 0; i < relatedTransactionsArray.length(); i++) {
                    JSONObject transactionJson = relatedTransactionsArray.optJSONObject(i);
                    if (transactionJson != null) {
                        RelatedTransaction transaction = new RelatedTransaction();
                        transaction.setTransactionId(transactionJson.optLong(KeyMappings.TRANSACTION_ID.getKey(), 0));

                        String amountStr = transactionJson.optString(KeyMappings.AMOUNT.getKey(), "0");

                        BigDecimal amount = amountStr.equalsIgnoreCase("null")
                                || amountStr.trim().isEmpty()
                                ? BigDecimal.ZERO
                                : new BigDecimal(amountStr).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                        order.setAmount(amount);

                        transaction.setReferenceTransactionId(transactionJson.optLong(KeyMappings.REFERENCE_TRANSACTION_ID.getKey(), 0));
                        transaction.setOrderId(transactionJson.optString(KeyMappings.ORDER_ID.getKey(), "Not available"));
                        transaction.setCurrency(transactionJson.optString(KeyMappings.CURRENCY.getKey(), "Not available"));

                        JSONObject paymentDetailsJson = transactionJson.optJSONObject(KeyMappings.PAYMENT_DETAILS.getKey());
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

        JSONObject jsonObject = getJsonObjectWithRelatedTransactionsAndItems();
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


    private static JSONObject getJsonObjectWithRelatedTransactionsAndItems() {
        String jsonString = "{"
                + "\"data\": {"
                + "\"merchant\": {"
                + "\"address\": \"Rue de la Gares test1 Gößweinstein\","
                + "\"name\": \"MultiSafepay B.V.\""
                + "},"
                + "\"order\": {"
                + "\"amount\": 740,"
                + "\"amountrefunded\": 0,"
                + "\"completed\": \"2025-07-10T11:58:42\","
                + "\"currency\": \"EUR\","
                + "\"items\": {"
                + "\"items\": ["
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 4,"
                + "\"name\": \"Caffè Latte\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 3.99"
                + "},"
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 0,"
                + "\"name\": \"Caffè Americano\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 0.1"
                + "},"
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 0,"
                + "\"name\": \"Caffè Mocha\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 0.11"
                + "},"
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 3,"
                + "\"name\": \"Cortado\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 3.2"
                + "}"
                + "]"
                + "},"
                + "\"tip\": null,"
                + "\"transaction_id\": \"1752141520108726\""
                + "},"
                + "\"payment\": {"
                + "\"application_id\": \"a0000000041010\","
                + "\"authorization_code\": \"006459\","
                + "\"card_acceptor_location\": \"Gößweinstein\","
                + "\"card_entry_mode\": \"ICC_CONTACTLESS\","
                + "\"card_expiry_date\": \"3109\","
                + "\"cardholder_verification_method\": \"ENCIPHERED_PIN_ONLINE\","
                + "\"issuer_bin\": \"522205\","
                + "\"issuer_country_code\": \"ES\","
                + "\"last4\": \"5665\","
                + "\"payment_method\": \"MASTERCARD\","
                + "\"response_code\": \"00\","
                + "\"terminal_id\": \"00000OTA\""
                + "},"
                + "\"printed_on\": \"2025-07-14T09:44:54\","
                + "\"related_transactions\": null"
                + "},"
                + "\"success\": true"
                + "}";

        return new JSONObject(jsonString);
    }


    private static JSONObject getJsonObjectWithRelatedTransactionsAndItemsAndReferenceTransactions() {
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
                + "\"items\": {"
                + "\"items\": ["
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 4,"
                + "\"name\": \"Caffè Latte\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 3.99"
                + "},"
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 0,"
                + "\"name\": \"Caffè Americano\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 0.1"
                + "},"
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 0,"
                + "\"name\": \"Caffè Mocha\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 0.11"
                + "},"
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 3,"
                + "\"name\": \"Cortado\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 3.2"
                + "}"
                + "]"
                + "},"
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

        return new JSONObject(jsonString);
    }

   /* private static JSONObject getJsonObjectWithRelatedTransactions() {
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
    }*/

    private static String getJsonObjectWithRelatedTransactions() {
        return "{"
                + "\"data\": {"
                + "\"merchant\": {"
                + "\"address\": \"Rue de la Gares test1 Gößweinstein\","
                + "\"name\": \"MultiSafepay B.V.\""
                + "},"
                + "\"order\": {"
                + "\"amount\": 740,"
                + "\"amountrefunded\": 0,"
                + "\"completed\": \"2025-07-10T11:58:42\","
                + "\"currency\": \"EUR\","
                + "\"items\": {"
                + "\"items\": ["
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 4,"
                + "\"name\": \"Caffè Latte\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 3.99"
                + "},"
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 0,"
                + "\"name\": \"Caffè Americano\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 0.1"
                + "},"
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 0,"
                + "\"name\": \"Caffè Mocha\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 0.11"
                + "},"
                + "{"
                + "\"currency\": \"EUR\","
                + "\"item_price\": 3,"
                + "\"name\": \"Cortado\","
                + "\"quantity\": 1,"
                + "\"unit_price\": 3.2"
                + "}"
                + "]"
                + "},"
                + "\"tip\": null,"
                + "\"transaction_id\": \"1752141520108726\""
                + "},"
                + "\"payment\": {"
                + "\"application_id\": \"a0000000041010\","
                + "\"authorization_code\": \"006459\","
                + "\"card_acceptor_location\": \"Gößweinstein\","
                + "\"card_entry_mode\": \"ICC_CONTACTLESS\","
                + "\"card_expiry_date\": \"3109\","
                + "\"cardholder_verification_method\": \"ENCIPHERED_PIN_ONLINE\","
                + "\"issuer_bin\": \"522205\","
                + "\"issuer_country_code\": \"ES\","
                + "\"last4\": \"5665\","
                + "\"payment_method\": \"MASTERCARD\","
                + "\"response_code\": \"00\","
                + "\"terminal_id\": \"00000OTA\""
                + "},"
                + "\"printed_on\": \"2025-07-14T09:44:54\","
                + "\"related_transactions\": null"
                + "},"
                + "\"success\": true"
                + "}";
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
