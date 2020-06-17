package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class DepositCoin implements Serializable {
    private String type;
    private String coin;
    private String address;
    private String total;
    private String free;

    public DepositCoin(String type, String coin, String address, String total, String free) {
        this.type = type;
        this.coin = coin;
        this.address = address;
        this.total = total;
        this.free = free;
    }

    public String getType() { return type; }
    public String getCoin() { return coin; }
    public String getAddress() { return address; }
    public String getTotal() { return total; }
    public String getFree() { return free; }
}
