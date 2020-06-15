package com.urgentrn.urncexchange.models.contacts;

import java.io.Serializable;

public class BaseContact implements Serializable {

    private int id;
    private int entity_id;
    private String name;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getEntityId() {
        return entity_id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }
}
