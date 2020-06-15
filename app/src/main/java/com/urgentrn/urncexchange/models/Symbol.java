package com.urgentrn.urncexchange.models;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Symbol extends BaseSymbol {

    private Asset fromAsset;
    private String card;
    private String feeFormatted;
    private double feeCurrency;
    private String feeCurrencyFormatted;
    private LinkedHashMap<String, ConversionRate> conversionRate;
    private String explorerUrl;
    private HashMap<String, String> extra; // fieldName, fieldMessage
    private MarketData marketData;

    public Object getFromAsset() { return fromAsset; }

    public String getCard() {
        return card;
    }

    public String getFeeFormatted() {
        return feeFormatted;
    }

    public double getFeeCurrency() {
        return feeCurrency;
    }

    public String getFeeCurrencyFormatted() {
        return feeCurrencyFormatted;
    }

    public LinkedHashMap<String, ConversionRate> getConversionRate() {
        return conversionRate;
    }

    public String getExplorerUrl() {
        return explorerUrl;
    }

    public HashMap<String, String> getExtra() {
        return extra;
    }

    public MarketData getMarketData() {
        return marketData;
    }

    public class ConversionRate {

        private double price;
        private String priceFormatted;

        public double getPrice() {
            return price;
        }

        public String getPriceFormatted() {
            return priceFormatted;
        }
    }
}
