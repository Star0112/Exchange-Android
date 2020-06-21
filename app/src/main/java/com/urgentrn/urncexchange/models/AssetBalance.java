package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class AssetBalance implements Serializable {
    private String type;
    private String coin;
    private String image;
    private String address;
    private String available;
    private String freeze;

    public AssetBalance(String type, String coin, String address, String available, String free, String image) {
        this.type = type;
        this.coin = coin;
        this.address = address;
        this.available = available;
        this.freeze = free;
        this.image = image;
    }

    public String getType() { return type; }
    public String getCoin() { return coin; }
    public String getAddress() { return address; }
    public String getAvailable() { return available; }
    public String getFreeze() { return freeze; }
    public String getImage() { return image; }
}
