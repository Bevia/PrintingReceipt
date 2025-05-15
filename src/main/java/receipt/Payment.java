package receipt;

public class Payment {
    private String applicationId;
    private String authorizationCode;
    private String cardholderVerificationMethod;
    private String cardEntryMode;
    private String issuerBin;
    private String issuerCountryCode;
    private String last4;
    private String paymentMethod;
    private String responseCode;
    private String terminalId;

    // Getters and Setters
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getAuthorizationCode() { return authorizationCode; }
    public void setAuthorizationCode(String authorizationCode) { this.authorizationCode = authorizationCode; }

    public String getCardholderVerificationMethod() { return cardholderVerificationMethod; }
    public void setCardholderVerificationMethod(String cardholderVerificationMethod) { this.cardholderVerificationMethod = cardholderVerificationMethod; }

    public String getCardEntryMode() { return cardEntryMode; }
    public void setCardEntryMode(String cardEntryMode) { this.cardEntryMode = cardEntryMode; }

    public String getIssuerBin() { return issuerBin; }
    public void setIssuerBin(String issuerBin) { this.issuerBin = issuerBin; }

    public String getIssuerCountryCode() { return issuerCountryCode; }
    public void setIssuerCountryCode(String issuerCountryCode) { this.issuerCountryCode = issuerCountryCode; }

    public String getLast4() { return last4; }
    public void setLast4(String last4) { this.last4 = last4; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getResponseCode() { return responseCode; }
    public void setResponseCode(String responseCode) { this.responseCode = responseCode; }

    public String getTerminalId() { return terminalId; }
    public void setTerminalId(String terminalId) { this.terminalId = terminalId; }
}
