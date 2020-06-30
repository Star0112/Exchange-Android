package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class SendHistory implements Serializable {
    private String amount;
    private String fee;
    private String createdAt;
    private String updatedAt;
    private Receiver receiver;
    private Asset asset;

    public String getAsset() {
        return this.asset.name;
    }
    public String getDate() {
        return this.updatedAt;
    }
    public String getReceiver() {
        return this.receiver.email;
    }
    public String getAmount() {
        return this.amount;
    }

    class Receiver implements Serializable {
        private String firstname;
        private String lastname;
        private String email;
    }

    class Asset implements Serializable {
        private String name;
    }
}
