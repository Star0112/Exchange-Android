package com.urgentrn.urncexchange.models;

public class Banner {

    private int id;
    private String title;
    private String link;
    private int order;
    private int status;
    private String created_at;
    private String updated_at;
    private ImageData image;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public int getOrder() {
        return order;
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

    public ImageData getImage() {
        return image;
    }
}
