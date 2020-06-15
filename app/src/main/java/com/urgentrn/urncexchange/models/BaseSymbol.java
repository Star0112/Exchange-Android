package com.urgentrn.urncexchange.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseSymbol implements Serializable {

    private int id;
    private String title;
    private String image;
    private Object images; // TODO: not using array because some are json object {}
    private String symbol;
    private String type;
    private boolean status;
    private boolean base;
    private boolean receive;
    private boolean send;
    private boolean cardSpendable;
    private int order;
    private double fee;
    private String color;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDefaultImage() {
        return getImage("default");
    }

    public String getColoredImage() {
        return getImage("colored");
    }

    private String getImage(String type) { // type: default, colored, bordered
        if (images instanceof List) {
            if (false) {
                final List<ImageData> imageList = (ArrayList)images;
                for (int i = 0; i < imageList.size(); i++) {
                    final ImageData data = imageList.get(i);
                    if (data.getType().equalsIgnoreCase(type)){
                        return data.getType();
                    }
                }
            } else {
                final List<Map> imageList = (ArrayList)images;
                for (int i = 0; i < imageList.size(); i++) {
                    final Map image = imageList.get(i);
                    final String imageType = (String)image.get("type");
                    if (imageType != null && imageType.equalsIgnoreCase(type)) {
                        return (String)image.get("path");
                    }
                }
            }
        }
        return null;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return type;
    }

    public boolean isBlockChain() {
        return type.equalsIgnoreCase("blockchain");
    }

    public boolean isCurrency() {
        return type.equalsIgnoreCase("currency");
    }

    public boolean isStableCoin() {
        return type.equalsIgnoreCase("stablecoin");
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isBase() {
        return base;
    }

    public boolean isReceive() {
        return receive;
    }

    public boolean isSend() {
        return send;
    }

    public boolean isCardSpendable() {
        return cardSpendable;
    }

    public int getOrder() {
        return order;
    }

    public double getFee() {
        return fee;
    }

    public String getColor() {
        return color;
    }
}
