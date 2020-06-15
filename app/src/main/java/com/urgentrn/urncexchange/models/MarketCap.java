package com.urgentrn.urncexchange.models;

public class MarketCap {

    private String symbol;
    private double amount;
    private String amountFormatted;
    private String date;

    public String getSymbol() {
        return symbol;
    }

    public double getAmount() {
        return amount;
    }

    public String getAmountFormatted() {
        return amountFormatted;
    }

    public String getDate() {
        return date;
    }
}
