package com.urgentrn.urncexchange.models.request;

public class BuyCoinRequest {
    private String type;
    private int marketId;
    private int amount;

    public BuyCoinRequest(String type, int marketId, int amount) {
        this.type = type;
        this.marketId = marketId;
        this.amount = amount;
    }
}
