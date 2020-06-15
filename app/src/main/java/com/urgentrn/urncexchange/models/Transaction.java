package com.urgentrn.urncexchange.models;

import com.google.gson.internal.LinkedTreeMap;
import com.urgentrn.urncexchange.models.bank.Field;
import com.urgentrn.urncexchange.utils.Utils;

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

    public String getTxHash() {
        return txhash;
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

    public String getAmountFormatted() {
        return Utils.formattedNumber(amountFormatted);
    }

    public double getConversionRateAtTransaction() {
        return conversionRateAtTransaction;
    }

    public String getConversionRateAtTransactionFormatted() {
        return conversionRateAtTransactionFormatted;
    }

    public double getUsdAmountAtTransaction() {
        return usdAmountAtTransaction;
    }

    public String getUsdAmountAtTransactionFormatted() {
        return usdAmountAtTransactionFormatted;
    }

    public double getConversionRateNow() {
        return conversionRateNow;
    }

    public String getConversionRateNowFormatted() {
        return conversionRateNowFormatted;
    }

    public double getUsdAmountNow() {
        return usdAmountNow;
    }

    public String getUsdAmountNowFormatted() {
        return usdAmountNowFormatted;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getAvailability() {
        return availability;
    }

    public String getType() {
        return type;
    }

    public String getIcon() {
        return icon;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public String getService() {
        return service;
    }

    public Field getAccount() {
        if (this.account instanceof LinkedTreeMap) {
            final LinkedTreeMap account = (LinkedTreeMap)this.account;
            final Field field = new Field();
            field.setBankName((String)account.get("bankName"));
            field.setAccountType((String)account.get("accountType"));
            field.setRoutingNumber((String)account.get("routingNumber"));
            field.setAccountNumber((String)account.get("accountNumber"));
            field.setName((String)account.get("name"));
            field.setBalance((double)account.get("balance"));
            field.setAccountId((String)account.get("account_id"));
            return field;
        }
        return null;
    }

    public String getString() {
        return string;
    }

    public String getSecondText() {
        return secondText;
    }

    public String getMethod() {
        return method;
    }

    public String getMerchant() {
        return merchant;
    }

    public Reward getReward() {
        return reward;
    }

    public ReserveEntry getReserveEntry() {
        return reserveEntry;
    }

    public ReserveEntry getReleasingReserveEntry() {
        return releasingReserveEntry;
    }

    public double getFee() {
        return fee;
    }

    public String getFeeFormatted() {
        return feeFormatted;
    }
}
