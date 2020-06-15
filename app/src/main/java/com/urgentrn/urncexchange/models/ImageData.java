package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class ImageData implements Serializable {

    private int id;
    private int parent_id;
    private String parent_type;
    private String type;
    private String path;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parent_id;
    }

    public String getParentType() {
        return parent_type;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }
}
