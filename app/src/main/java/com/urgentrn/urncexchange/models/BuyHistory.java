package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class BuyHistory implements Serializable {
    private int id;
    private String token;
    private String money;
    private String price;
    private String buyAmount;
    private String createdAt;
    private String updatedAt;
    private int userId;
    private UserName user;

    private class UserName {
        private String username;
    }

    public String getAsset() {
        return this.token;
    }
    public String getAmount() {
        return this.money;
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
