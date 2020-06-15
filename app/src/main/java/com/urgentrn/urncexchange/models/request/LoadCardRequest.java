package com.urgentrn.urncexchange.models.request;

public class LoadCardRequest {

    private int entityCardId;
    private int assetId;
    private double amount;

    public LoadCardRequest(int entityCardId, int assetId, double amount) {
        this.entityCardId = entityCardId;
        this.assetId = assetId;
        this.amount = amount;
    }
}
