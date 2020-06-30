package com.urgentrn.urncexchange.models.request;

public class SendAssetRequest {
    private int assetId;
    private int amount;
    private String[] emails;

    public SendAssetRequest(int assetId, int amount, String[] emails) {
        this.assetId = assetId;
        this.amount = amount;
        this.emails = emails;
    }
}
