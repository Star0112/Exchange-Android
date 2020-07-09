package com.urgentrn.urncexchange.models;

import java.io.Serializable;
import java.util.List;

public class ChartData implements Serializable {
    private List<String> data;

    public String getValue() {
        if (data.size() != 0) {
            return data.get(1);
        } else {
            return "";
        }
    }
}
