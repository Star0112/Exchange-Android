package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class MarketInfo implements Serializable {
    private int id;
    private String name;
    private String base;
    private String pair;
    private int minAmount;
    private double price;

    public MarketInfo(int id, String name, String base, String pair, int minAmount, double price) {
        this.id = id;
        this.name = name;
        this.base = base;
        this.pair = pair;
        this.minAmount = minAmount;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() {
        return this.name;
    }
    public Double getPrice() { return this.price; }
    public String getBase() {return this.base; }
    public String getPair() { return this.pair; }
 }
