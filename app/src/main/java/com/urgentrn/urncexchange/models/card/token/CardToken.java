package com.urgentrn.urncexchange.models.card.token;

import java.io.Serializable;

public class CardToken implements Serializable {

    private int id;
    private int entity_card_id;
    private String type;
    private String device;
    private String reference;
    private String token;
    private TokenData data;
    private String status;
    private int freeze;
    private String deleted_at;
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

    public String getDevice() {
        return device;
    }

    public String getReference() {
        return reference;
    }

    public String getToken() {
        return token;
    }

    public TokenData getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public int getFreeze() {
        return freeze;
    }

    public String getDeletedAt() {
        return deleted_at;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }
}
