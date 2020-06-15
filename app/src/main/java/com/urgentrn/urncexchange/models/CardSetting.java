package com.urgentrn.urncexchange.models;

public class CardSetting {

    private int imgRes;
    private int titleRes;

    public CardSetting(int imgRes, int titleRes) {
        this.imgRes = imgRes;
        this.titleRes = titleRes;
    }

    public int getImgRes() {
        return imgRes;
    }

    public int getTitleRes() {
        return titleRes;
    }
}
