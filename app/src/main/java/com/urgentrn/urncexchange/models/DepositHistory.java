package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class DepositHistory implements Serializable {
    private String id, symbol,  address, amount, confirmPercent, txhash, createdAt, status;

    public DepositHistory(String id, String symbol, String address, String amount, String confirmPercent, String txhash, String createdAt, String status) {
        this.id = id;
        this.symbol = symbol;
        this.address = address;
        this.amount = amount;
        this.confirmPercent = confirmPercent;
        this.txhash = txhash;
        this.createdAt = createdAt;
        this.status = status;
    }

    public String getSymbol() { return symbol; }
    public String getAmount() { return amount; }
    public String getDate() { return createdAt; }
}

