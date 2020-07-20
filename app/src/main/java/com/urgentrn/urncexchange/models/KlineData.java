package com.urgentrn.urncexchange.models;

public class KlineData {

    private long timestamp;
    private String open;
    private String close;
    private String high;
    private String low;
    private String volume;
    private String deal;

    public long getTimestamp() {
        return timestamp;
    }

    public String getOpen() { return open; }
    public String getClose() {
        return close;
    }
}
