package receipt;

import java.util.HashMap;
import java.util.Map;

public enum KeyMappings {
    PRINTED_ON("printed_on", "Printed On"),
    SUCCESS("success", "Success"),
    APPLICATION_ID("application_id", "Application ID"),
    CARDHOLDER_VERIFICATION_METHOD("cardholder_verification_method", "Cardholder Verification Method"),
    CARD_ACCEPTOR_LOCATION("card_acceptor_location", "Card Acceptor Location"),
    CARD_EXPIRY_DATE("card_expiry_date", "Card Expiry Date"),
    ISSUER_BIN("issuer_bin", "Issuer BIN"),
    ISSUER_COUNTRY_CODE("issuer_country_code", "Issuer Country Code"),
    LAST4("last4", "Card Last 4 Digits"),
    RESPONSE_CODE("response_code", "Response Code"),
    PAYMENT_DETAILS("payment_details", "Payment Details"),
    AMOUNT("amount", "Amount"),
    TIP("tip", "Tip"),
    ORDER_ID("order_id", "Order ID"),
    NAME("name", "Name"),
    ADDRESS("address", "Address"),
    CURRENCY("currency", "Currency"),
    TRANSACTION_ID("transaction_id", "Transaction ID"),
    REFERENCE_TRANSACTION_ID("reference_transaction_id", "Reference ID"),
    RELATED_TRANSACTIONS("related_transactions", "Related Transactions"),
    UNIT_PRICE("unit_price", "Unit Price"),
    MERCHANT("merchant", "Merchant"),
    AMOUNT_REFUNDED("amountrefunded", "Amount Refunded"),
    MERCHANT_ADDRESS("merchant_address", "Merchant Address"),
    MERCHANT_NAME("merchant_name", "Merchant Name"),
    PAYMENT_METHOD("payment_method", "Payment Method"),
    CARD_ENTRY_MODE("card_entry_mode", "Card Entry Mode"),
    AUTHORIZATION_CODE("authorization_code", "Authorization Code"),
    TERMINAL_ID("terminal_id", "Terminal ID"),
    ORDER_TRANSACTION_ID("order_transaction_id", "Order Transaction ID"),
    ORDER_AMOUNT("order_amount", "Order Amount"),
    ORDER_CURRENCY("order_currency", "Order Currency"),
    ORDER_TIP("order_tip", "Tip Amount"),
    RELATED_TRANSACTION_IDS("related_transaction_ids", "Related Transaction IDs");

    private final String key;
    private final String displayText;

    private static final Map<String, KeyMappings> lookup = new HashMap<>();

    static {
        for (KeyMappings mapping : KeyMappings.values()) {
            lookup.put(mapping.key, mapping);
        }
    }

    KeyMappings(String key, String displayText) {
        this.key = key;
        this.displayText = displayText;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayText() {
        return displayText;
    }

    public static String getDisplayTextForKey(String key) {
        KeyMappings mapping = lookup.get(key);
        return mapping != null ? mapping.getDisplayText() : key; // Default to key if not mapped
    }
}

