package com.urgentrn.urncexchange.models.card;

import java.io.Serializable;
import java.util.List;

public class GiftCard implements Serializable {

    private String ref;
    private String name;
    private String link;
    private String color;
    private String description;
    private String disclaimer;
    private String priceType;
    private double minPrice;
    private double maxPrice;
    private boolean image;
    private List<CardInfo.Image> images;
    private List<AvailablePrice> availablePrices;

    public String getRef() {
        return ref;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public String getPriceType() {
        return priceType;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public boolean hasImage() {
        return image;
    }

    public List<CardInfo.Image> getImages() {
        return images;
    }

    private String getImage(String type) {
        for (CardInfo.Image image : images) {
            if (image.getType().equals(type)) {
                return image.getImage();
            }
        }
        return null;
    }

    public String getImage() {
        return getImage("base");
    }

    public List<AvailablePrice> getAvailablePrices() {
        return availablePrices;
    }
}
