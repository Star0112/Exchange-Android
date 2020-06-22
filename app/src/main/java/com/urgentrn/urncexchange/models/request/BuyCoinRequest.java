package com.urgentrn.urncexchange.models.request;

public class BuyCoinRequest {
    private String type;
    private String symbol;
    private int amount;

    public BuyCoinRequest(String type, String symbol, int amount) {
        this.type = type;
        this.symbol = symbol;
        this.amount = amount;
    }
}
