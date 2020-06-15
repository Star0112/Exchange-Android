package com.urgentrn.urncexchange.models.response;

public class ExchangeConfirmResponse extends BaseResponse {

    private ExchangeConfirm data;

    public String getClientSecret() {
        return data == null ? null : data.client_secret;
    }

    public String getPaymentIntent() {
        return data == null ? null : data.intent;
    }

    public String getPublishableKey() {
        return data == null ? null : data.pubkey;
    }

    private class ExchangeConfirm {

        private String transaction_id;

        private String client_secret;
        private String intent;
        private String pubkey;
    }
}
