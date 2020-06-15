package com.urgentrn.urncexchange.models.contacts;

import com.urgentrn.urncexchange.models.Asset;

public class WalletAddress extends BaseWalletAddress {

    private int id;
    private int wallet_id;
    private String created_at;
    private String updated_at;
    private Asset asset;

    public int getId() {
        return id;
    }

    public int getWalletId() {
        return wallet_id;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public Asset getAsset() {
        return asset;
    }
}
