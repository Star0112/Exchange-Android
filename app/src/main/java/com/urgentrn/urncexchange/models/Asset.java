package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class Asset implements Serializable {

    private int id;
    private int from_asset_id; // only used for pegged assets
    private String name;
    private String symbol;
    private String color;
    private boolean base;
    private boolean receive;
    private boolean send;
    private int order;
    private String type;
    private String platform;
    private boolean status;

    public int getId() {
        return id;
    }

    public int getFromAssetId() {
        return from_asset_id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }

    public boolean isBase() {
        return base;
    }

    public boolean isReceive() {
        return receive;
    }

    public boolean isSend() {
        return send;
    }

    public int getOrder() {
        return order;
    }

    public String getType() {
        return type;
    }

    public String getPlatform() {
        return platform;
    }

    public boolean isStatus() {
        return status;
    }
}
