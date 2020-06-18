package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class CoinBalance implements Serializable {
    private String type;
    private String coinName;
    private String address;
    private String total;
    private String free;

    public CoinBalance(String type, String coin, String address, String total, String free) {
        this.type = type;
        this.coinName = coin;
        this.address = address;
        this.total = total;
        this.free = free;
    }

    public String getType() { return type; }
    public String getCoinName() { return coinName; }
    public String getAddress() { return address; }
    public String getTotal() { return total; }
    public String getFree() { return free; }
}
