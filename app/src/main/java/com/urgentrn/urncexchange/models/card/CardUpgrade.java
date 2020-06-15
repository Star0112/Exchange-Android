package com.urgentrn.urncexchange.models.card;

import java.io.Serializable;

public class CardUpgrade implements Serializable {

    private int id;
    private int from_card_id;
    private int to_card_id;
    private int status;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private CardInfo to_card;
    private CardInfo from_card;
    private double fee;

    public int getId() {
        return id;
    }

    public int getFromCardId() {
        return from_card_id;
    }

    public int getToCardId() {
        return to_card_id;
    }

    public int getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public String getDeletedAt() {
        return deleted_at;
    }

    public CardInfo getToCard() {
        return to_card;
    }

    public CardInfo getFromCard() {
        return from_card;
    }

    public double getFee() {
        return fee;
    }
}
