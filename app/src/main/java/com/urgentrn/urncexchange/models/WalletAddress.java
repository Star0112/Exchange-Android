package com.urgentrn.urncexchange.models;

import java.util.HashMap;

public class WalletAddress {

    private String address;
    private String symbol;
    private String share;
    private HashMap<String, String> extra; // fieldName, fieldValue, fieldMessage

    public String getAddress() {
        return address;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getShare() {
        return share;
    }

    public HashMap<String, String> getExtra() {
        return extra;
    }
}
