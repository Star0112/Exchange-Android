package com.urgentrn.urncexchange.models;

public class RewardData {

    private int id;
    private String description;
    private String schedule;
    private String um;
    private String amount;
    private String amountFormatted;
    private String amountPaid;
    private String amountPaidFormatted;
    private String amountReversedFormatted;
    private String reserveStartDate;
    private String reserveEndDate;
    private boolean eligible;

    public int getId() {
        return id;
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

    public String getAmountFormatted() {
        return amountFormatted;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public String getAmountPaidFormatted() {
        return amountPaidFormatted;
    }

    public String getAmountReversedFormatted() {
        return amountReversedFormatted;
    }

    public String getReserveStartDate() {
        return reserveStartDate;
    }

    public String getReserveEndDate() {
        return reserveEndDate;
    }

    public boolean isEligible() {
        return eligible;
    }
}
