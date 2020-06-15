package com.urgentrn.urncexchange.models.card;

import java.io.Serializable;

public class GiftMeta implements Serializable {

    private int id;
    private int entity_card_id;
    private String type;
    private String value;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getEntityCardId() {
        return entity_card_id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
