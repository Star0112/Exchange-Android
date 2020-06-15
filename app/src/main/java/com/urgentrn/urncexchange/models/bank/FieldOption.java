package com.urgentrn.urncexchange.models.bank;

public class FieldOption {

    private String name;
    private String value;

    public FieldOption(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
