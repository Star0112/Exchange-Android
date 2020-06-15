package com.urgentrn.urncexchange.models;

public class Fund {

    private int id;
    private int asset_id;
    private int entity_id;
    private double total;
    private double pending;
    private String reserved;
    private double available;
    private boolean status;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public int getId() {
        return id;
    }

    public int getAssetId() {
        return asset_id;
    }

    public int getEntityId() {
        return entity_id;
    }

    public double getTotal() {
        return total;
    }

    public double getPending() {
        return pending;
    }

    public String getReserved() {
        return reserved;
    }

    public double getAvailable() {
        return available;
    }

    public boolean isStatus() {
        return status;
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
