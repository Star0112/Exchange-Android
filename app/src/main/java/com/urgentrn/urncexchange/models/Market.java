package com.urgentrn.urncexchange.models;

import java.util.List;

public class Market {

    private List<MarketCap> price;
    private List<MarketCap> marketCap;
    private List<MarketCap> volume;

    public List<MarketCap> getPrice() {
        return price;
    }

    public List<MarketCap> getMarketCap() {
        return marketCap;
    }

    public List<MarketCap> getVolume() {
        return volume;
    }
}
