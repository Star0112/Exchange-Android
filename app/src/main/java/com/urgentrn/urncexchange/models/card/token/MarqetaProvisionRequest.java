package com.urgentrn.urncexchange.models.card.token;

public class MarqetaProvisionRequest {

    private String cardToken;
    private String deviceType;
    private String provisioningAppVersion;
    private String walletAccountId;
    private String deviceId;

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setProvisioningAppVersion(String provisioningAppVersion) {
        this.provisioningAppVersion = provisioningAppVersion;
    }

    public String getProvisioningAppVersion() {
        return provisioningAppVersion;
    }

    public void setWalletAccountId(String walletAccountId) {
        this.walletAccountId = walletAccountId;
    }

    public String getWalletAccountId() {
        return walletAccountId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
