package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class MarketInfo implements Serializable {
    private int id;
    private String name;
    private String base;
    private String pair;
    private int minAmount;
    private float price = 50;

    public MarketInfo(int id, String name, String base, String pair, int minAmount ) {
        this.id = id;
        this.name = name;
        this.base = base;
        this.pair = pair;
        this.minAmount = minAmount;
    }

    public int getId() { return id; }
    public String getName() {
        return this.name;
    }
    public float getPrice() { return 50.0f; }
    public String getBase() {return this.base; }
    public String getPair() { return this.pair; }
 }
