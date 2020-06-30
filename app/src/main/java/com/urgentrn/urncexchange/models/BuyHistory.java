package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class BuyHistory implements Serializable {
    private int id;
    private String token;
    private String money;
    private String price;
    private int buyCount;
    private String buyAmount;
    private String createdAt;
    private String updatedAt;

    public String getAsset() {
        return this.token;
    }
    public int getBuyCount() { return this.buyCount; }
    public String getAmount() {
        return this.buyAmount;
    }
    public String getBaseAsset() {
        return this.money;
    }
    public String getPrice() {
        return this.price;
    }
    public String getDate() {
        return this.updatedAt;
    }
}
