package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class BuyHistory implements Serializable {
    private String assetName, totalBalance, date;

    public BuyHistory(String assetsName, String totalBalance, String date) {
        this.assetName = assetsName;
        this.totalBalance = totalBalance;
        this.date = date;
    }

    public String getAssetName() { return assetName; }
    public String getTotalBalance() { return totalBalance; }
    public String getDate() { return date; }
}

