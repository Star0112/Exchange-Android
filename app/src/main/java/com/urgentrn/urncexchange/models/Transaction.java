package com.urgentrn.urncexchange.models;
import java.io.Serializable;

public class Transaction implements Serializable {

    private int id;
    private String symbol;
    private String txhash;
    private String to;
    private String title;
    private String status;
    private double amount;
    private String amountFormatted;
    private double conversionRateAtTransaction;
    private String conversionRateAtTransactionFormatted;
    private double usdAmountAtTransaction;
    private String usdAmountAtTransactionFormatted;
    private double conversionRateNow;
    private String conversionRateNowFormatted;
    private double usdAmountNow;
    private String usdAmountNowFormatted;
    private String dateTime;
    private String availability;
    private String type;
    private String icon;
    private Exchange exchange;
    private String service;
    private Object account; // TODO: not using object because some are empty array []
    private String string;
    private String secondText;
    private String method;
    private String merchant;
    private Reward reward;
    private ReserveEntry reserveEntry;
    private ReserveEntry releasingReserveEntry;
    private double fee;
    private String feeFormatted;

    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTo() {
        return to;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getString() {
        return string;
    }

    public double getFee() {
        return fee;
    }
}
