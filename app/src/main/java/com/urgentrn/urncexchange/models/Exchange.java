package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class Exchange implements Serializable {

    private double amount;
    private double rate;
    private String rateFormatted;
    private String symbol;
    private double fee;
    private String feeFormatted;

    public double getAmount() {
        return amount;
    }

    public double getRate() {
        return rate;
    }

    public String getRateFormatted() {
        return rateFormatted;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getFee() {
        return fee;
    }

    public String getFeeFormatted() {
        return feeFormatted;
    }
}
