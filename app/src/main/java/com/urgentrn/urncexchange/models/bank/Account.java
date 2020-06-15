package com.urgentrn.urncexchange.models.bank;

import com.urgentrn.urncexchange.models.Asset;

import java.util.List;

public class Account {

    private int id;
    private String flow;
    private String status;
    private List<FlowService> services;
    private Field field;
    private Asset asset;

    public int getId() {
        return id;
    }

    public String getFlow() {
        return flow;
    }

    public String getStatus() {
        return status;
    }

    public List<FlowService> getServices() {
        return services;
    }

    public Field getField() {
        return field != null ? field : new Field();
    }

    public Asset getAsset() {
        return asset;
    }
}
