package com.urgentrn.urncexchange.models.bank;

public class FlowAccount {

    private int id;
    private int _pid;
    private int _id;
    private int entityId;
    private int accountFormatId;
    private String name;
    private boolean status;

    public int getId() {
        return id;
    }

    public int get_pid() {
        return _pid;
    }

    public int get_id() {
        return _id;
    }

    public int getEntityId() {
        return entityId;
    }

    public int getAccountFormatId() {
        return accountFormatId;
    }

    public String getName() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }
}
