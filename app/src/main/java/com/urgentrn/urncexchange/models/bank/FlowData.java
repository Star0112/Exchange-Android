package com.urgentrn.urncexchange.models.bank;

import java.util.List;

public class FlowData {

    private int id;
    private String flow;
    private FlowFormat format;
    private List<FlowService> services;
    private FlowAccount account;

    private String errorMessage;

    public FlowData(String message) {
        this.errorMessage = message;
    }

    public int getId() {
        return id;
    }

    public String getFlow() {
        return flow;
    }

    public FlowFormat getFormat() {
        return format;
    }

    public List<FlowService> getServices() {
        return services;
    }

    public FlowAccount getAccount() {
        return account;
    }

    public String getMessage() {
        return errorMessage;
    }
}
