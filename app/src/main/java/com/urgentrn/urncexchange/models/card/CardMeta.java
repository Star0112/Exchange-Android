package com.urgentrn.urncexchange.models.card;

import java.io.Serializable;

public class CardMeta implements Serializable {

    private int id;
    private int card_detail_id;
    private String name;
    private String value;
    private String image;
    private String url;
    private String order;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getCardDetailId() {
        return card_detail_id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public String getOrder() {
        return order;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }
}
