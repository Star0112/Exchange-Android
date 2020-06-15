package com.urgentrn.urncexchange.models.card;

import java.io.Serializable;

public class AvailablePrice implements Serializable {

    private double price;
    private String priceFormatted;

    public double getPrice() {
        return price;
    }

    public String getPriceFormatted() {
        return priceFormatted;
    }
}
