package com.urgentrn.urncexchange.models;

public class PlaidAccount {

    private String id;
    private String name;
    private String type;
    private String subtype;
    private String mask;

    public PlaidAccount(String id, String name, String type, String subtype, String mask) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.mask = mask;
    }
}
