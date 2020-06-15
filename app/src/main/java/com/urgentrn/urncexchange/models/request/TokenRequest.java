package com.urgentrn.urncexchange.models.request;

public class TokenRequest {

    private String reference;
    private String type;
    private ProvisionData provisionData;

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setProvisionData(String appVersion, String walletAccountId, String deviceId) {
        this.provisionData = new ProvisionData(appVersion, walletAccountId, deviceId);
    }

    private class ProvisionData {

        private String appVersion;

        // Apple Pay Specific
        private String[] certificates;
        private String nonce;
        private String nonceSignature;

        // Android Pay Specific
        private String walletAccountId;

        private String deviceId;

        private ProvisionData(String appVersion, String walletAccountId, String deviceId) {
            this.appVersion = appVersion;
            this.walletAccountId = walletAccountId;
            this.deviceId = deviceId;
        }
    }
}
