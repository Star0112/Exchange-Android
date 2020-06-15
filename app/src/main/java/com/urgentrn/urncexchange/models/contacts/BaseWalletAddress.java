package com.urgentrn.urncexchange.models.contacts;

import java.io.Serializable;

public class BaseWalletAddress implements Serializable {

    private int asset_id;
    private String address;
    private String extra;

    public int getAssetId() {
        return asset_id;
    }

    public void setAssetId(int assetId) {
        this.asset_id = assetId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
