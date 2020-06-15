package com.urgentrn.urncexchange.models;

public class SymbolData extends BaseSymbol {

    private double price;
    private double priceBtc;
    private double marketCap;
    private double totalSupply;
    private double circulatingSupply;
    private double maxSupply;
    private double volumeUsd24h;
    private double percentChange1h;
    private double percentChange24h;
    private double percentChange7d;
    private String info;

    public double getPrice() {
        return price;
    }

    public double getPriceBtc() {
        return priceBtc;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public double getVolumeUsd24h() {
        return volumeUsd24h;
    }

    public double getPercentChange1h() {
        return percentChange1h;
    }

    public double getPercentChange24h() {
        return percentChange24h;
    }

    public double getPercentChange7d() {
        return percentChange7d;
    }

    public String getInfo() {
        return info;
    }
}
