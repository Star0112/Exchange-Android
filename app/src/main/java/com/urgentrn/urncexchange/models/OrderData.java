package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class OrderData implements Serializable {
    private double bidAmount;
    private double bidPrice;
    private double askAmount;
    private double askPrice;

    public OrderData(double bidAmount, double bidPrice, double askAmount, double askPrice) {
        this.bidAmount = bidAmount;
        this.bidPrice = bidPrice;
        this.askAmount = askAmount;
        this.askPrice = askPrice;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public double getAskAmount() {
        return askAmount;
    }

    public double getAskPrice() {
        return askPrice;
    }
}
