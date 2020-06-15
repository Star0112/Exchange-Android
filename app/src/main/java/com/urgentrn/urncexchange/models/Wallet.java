package com.urgentrn.urncexchange.models;

import java.io.Serializable;
import java.util.HashMap;

public class Wallet implements Serializable {

    private String title;
    private String image;
    private String color;
    private String symbol;
    private double balance;
    private String balanceFormatted;
    private double balanceCurrency;
    private String balanceCurrencyFormatted;
    private double reservedBalance;
    private boolean isActive;
    private boolean isDefault;
    private boolean isFavorite;
    private boolean hasBanking;
    private boolean bankingCardProgram;
    private SymbolData symbolData;
    private HashMap<String, Integer> parameters;

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getColor() {
        return color;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isExchangeWallet() {
        return symbol.equals("SXP");
    }

    public double getBalance() {
        return balance;
    }

    public String getBalanceFormatted() {
        return balanceFormatted;
    }

    public double getBalanceCurrency() {
        return balanceCurrency;
    }

    public String getBalanceCurrencyFormatted() {
        return balanceCurrencyFormatted;
    }

    public char getCurrencySymbol() {
        return balanceCurrencyFormatted.charAt(0);
    }

    public double getReservedBalance() {
        return reservedBalance;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean enableCardSpending() {
        return parameters != null && parameters.get("enableCardSpending") == 1;
    }

    public boolean enableReceive() {
        return parameters != null && parameters.get("enableReceive") == 1;
    }

    public boolean enableSend() {
        return parameters != null && parameters.get("enableSend") == 1;
    }

    public SymbolData getSymbolData() {
        return symbolData;
    }
}
