package com.urgentrn.urncexchange.models;

public class MarketData {

    private double price;
    private String priceFormatted;
    private double priceBtc;
    private String priceBtcFormatted;
    private double marketCap;
    private String marketCapFormatted;
    private double totalSupply;
    private String totalSupplyFormatted;
    private double circulatingSupply;
    private String circulatingFormatted;
    private double maxSupply;
    private String maxSupplyFormatted;
    private double volumeUsd24h;
    private String volumeUsd24hFormatted;
    private double percentChange1h;
    private String percentChange1hFormatted;
    private double percentChange24h;
    private String percentChange24hFormatted;
    private double percentChange7d;
    private String percentChange7dFormatted;
    private char decimal;
    private char thousand;

    public double getPrice() {
        return price;
    }

    public String getPriceFormatted() {
        return priceFormatted;
    }

    public double getPriceBtc() {
        return priceBtc;
    }

    public String getPriceBtcFormatted() {
        return priceBtcFormatted;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public String getMarketCapFormatted() {
        return marketCapFormatted;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public String getTotalSupplyFormatted() {
        return totalSupplyFormatted;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public String getCirculatingFormatted() {
        return circulatingFormatted;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public String getMaxSupplyFormatted() {
        return maxSupplyFormatted;
    }

    public double getVolumeUsd24h() {
        return volumeUsd24h;
    }

    public String getVolumeUsd24hFormatted() {
        return volumeUsd24hFormatted;
    }

    public double getPercentChange1h() {
        return percentChange1h;
    }

    public String getPercentChange1hFormatted() {
        return percentChange1hFormatted;
    }

    public double getPercentChange24h() {
        return percentChange24h;
    }

    public String getPercentChange24hFormatted() {
        return percentChange24hFormatted;
    }

    public double getPercentChange7d() {
        return percentChange7d;
    }

    public String getPercentChange7dFormatted() {
        return percentChange7dFormatted;
    }

    public char getDecimal() {
        return decimal;
    }

    public char getThousand() {
        return thousand;
    }
}
