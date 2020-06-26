package com.urgentrn.urncexchange.models.request;

public class OrderRequest {
    private String market;
    private int side;
    private int amount;
    private float price;
    private String type;

    public OrderRequest(String market, int side, int amount, float price, String type) {
        this.market = market;
        this.side = side;
        this.amount = amount;
        this.price = price;
        this.type = type;
    }
}
