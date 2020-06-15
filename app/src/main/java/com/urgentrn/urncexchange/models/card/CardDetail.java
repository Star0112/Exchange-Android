package com.urgentrn.urncexchange.models.card;

import java.io.Serializable;
import java.util.List;

public class CardDetail implements Serializable {

    private int id;
    private int card_id;
    private String type;
    private String title;
    private String detail;
    private String image;
    private String url;
    private int order;
    private String created_at;
    private String updated_at;
    private List<CardMeta> metas;

    public int getId() {
        return id;
    }

    public int getCardId() {
        return card_id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public int getOrder() {
        return order;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public List<CardMeta> getMetas() {
        return metas;
    }
}
