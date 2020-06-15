package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class Limit implements Serializable {

    private double amount;
    private String amountFormatted;
    private String period;

    public double getAmount() {
        return amount;
    }

    public String getAmountFormatted() {
        return amountFormatted;
    }

    public String getPeriod() {
        return period;
    }
}
