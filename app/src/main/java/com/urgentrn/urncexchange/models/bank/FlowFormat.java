package com.urgentrn.urncexchange.models.bank;

import java.util.List;

public class FlowFormat {

    private int _id;
    private int _pid;
    private List<FlowFormatField> fields;

    public List<FlowFormatField> getFields() {
        return fields;
    }
}
