package com.urgentrn.urncexchange.models;

public class Link {

    private String type;
    private String title;
    private String url;

    public Link(String type, String title, String url) {
        this.type = type;
        this.title = title;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
