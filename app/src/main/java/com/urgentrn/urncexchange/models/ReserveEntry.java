package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class ReserveEntry implements Serializable {

    private int id;
    private int fund_id;
    private int opposing_reserve_entry_id;
    private String description;
    private double amount;
    private int user_releasable;
    private int auto_release;
    private String start_date;
    private String end_date;
    private String created_at;
    private String updated_at;
    private ReserveEntry opposing_reserve_entry;

    public int getId() {
        return id;
    }

    public int getFundId() {
        return fund_id;
    }

    public int getOpposingReserveEntryId() {
        return opposing_reserve_entry_id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public int getUserReleasable() {
        return user_releasable;
    }

    public int getAutoRelease() {
        return auto_release;
    }

    public String getStartDate() {
        return start_date;
    }

    public String getEndDate() {
        return end_date;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public ReserveEntry getOpposingReserveEntry() {
        return opposing_reserve_entry;
    }
}
