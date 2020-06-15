package com.urgentrn.urncexchange.models.bank;

import java.util.List;

public class FlowFormatField {

    private String name;
    private String field;
    private boolean required;
    private String type;
    private List<FieldOption> options;

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }

    public boolean isRequired() {
        return required;
    }

    public String getType() {
        return type;
    }

    public List<FieldOption> getOptions() {
        return options;
    }
}
