package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class Reward implements Serializable {

    private int id;
    private int fund_id;
    private String description;
    private String schedule;
    private String um;
    private String amount;
    private int status;
    private String reserve_period;
    private String reserve_period_factor;
    private String reserve_amount;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public int getId() {
        return id;
    }

    public int getFundId() {
        return fund_id;
    }

    public String getDescription() {
        return description;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getUm() {
        return um;
    }

    public String getAmount() {
        return amount;
    }

    public int getStatus() {
        return status;
    }

    public String getReservePeriod() {
        return reserve_period;
    }

    public String getReservePeriodFactor() {
        return reserve_period_factor;
    }

    public String getReserveAmount() {
        return reserve_amount;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public String getDeletedAt() {
        return deleted_at;
    }
}
