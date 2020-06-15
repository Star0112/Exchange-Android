package com.urgentrn.urncexchange.models.contacts;

import java.util.List;

public class ContactsData {

    private int current_page;
    private List<Contact> data;
    private String first_page_url;
    private int from;
    private int last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private int per_page;
    private String prev_page_url;
    private int to;
    private int total;

    public int getCurrentPage() {
        return current_page;
    }

    public List<Contact> getData() {
        return data;
    }

    public String getFirstPageUrl() {
        return first_page_url;
    }

    public int getFrom() {
        return from;
    }

    public int getLastPage() {
        return last_page;
    }

    public String getLastPageUrl() {
        return last_page_url;
    }

    public String getNextPageUrl() {
        return next_page_url;
    }

    public String getPath() {
        return path;
    }

    public int getPerPage() {
        return per_page;
    }

    public String getPrevPageUrl() {
        return prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public int getTotal() {
        return total;
    }
}
