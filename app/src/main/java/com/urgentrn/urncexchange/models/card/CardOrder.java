package com.urgentrn.urncexchange.models.card;

import com.urgentrn.urncexchange.models.ReserveEntry;

import java.io.Serializable;

public class CardOrder implements Serializable {

    private int id;
    private int entity_id;
    private int card_id;
    private Integer asset_id;
    private Integer entity_card_id;
    private Integer card_upgrade_id;
    private Integer exchange_quote_id;
    private Integer reserve_entry_id;
    private double amount;
    private String reference;
    private String type;
    private String status;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private ReserveEntry reserve_entry;

    public int getId() {
        return id;
    }

    public int getEntityId() {
        return entity_id;
    }

    public int getCardId() {
        return card_id;
    }

    public Integer getAssetId() {
        return asset_id;
    }

    public Integer getEntityCardId() {
        return entity_card_id;
    }

    public Integer getCardUpgradeId() {
        return card_upgrade_id;
    }

    public Integer getExchangeQuoteId() {
        return exchange_quote_id;
    }

    public Integer getReserveEntryId() {
        return reserve_entry_id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getReference() {
        return reference;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
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

    public ReserveEntry getReserveEntry() {
        return reserve_entry;
    }
}
