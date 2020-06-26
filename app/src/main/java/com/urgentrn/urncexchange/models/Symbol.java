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
