package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class AssetBalance implements Serializable {
    private int isFiat;
    private String name;
    private String displayName;
    private String image;
    private String address;
    private String available;
    private String freeze;

    public AssetBalance(int isFiat, String name, String displayName, String address, String available, String free, String image) {
        this.isFiat = isFiat;
        this.name = name;
        this.displayName = displayName;
        this.displayName = displayName;
        this.address = address;
        this.available = available;
        this.freeze = free;
        this.image = image;
    }

    public int getType() { return isFiat; }
    public String getCoin() { return name; }
    public String getAddress() { return address; }
    public String getAvailable() { return available; }
    public String getFreeze() { return freeze; }
    public String getImage() { return image; }
}
