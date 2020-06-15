package com.urgentrn.urncexchange.models.card;

import java.io.Serializable;

public class CardExchangeQuote implements Serializable {

    private int id;
    private int entity_id;
    private int from_exchange_id;
    private double from_amount;
    private double from_quantity;
    private int to_exchange_id;
    private double to_amount;
    private double to_quantity;
    private int card_transaction_id;
    private int card_order_id;
    private int account_id;
    private int account_service_id;
    private int disbursement_id;
    private int reserve_entry_id;
    private int fee_asset_id;
    private double fee_amount;
    private String status;
    private String expire_at;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public int getId() {
        return id;
    }

    public int getEntityId() {
        return entity_id;
    }

    public int getFromExchangeId() {
        return from_exchange_id;
    }

    public double getFromAmount() {
        return from_amount;
    }

    public double getFromQuantity() {
        return from_quantity;
    }

    public int getToExchangeId() {
        return to_exchange_id;
    }

    public double getToAmount() {
        return to_amount;
    }

    public double getToQuantity() {
        return to_quantity;
    }

    public int getCardTransactionId() {
        return card_transaction_id;
    }

    public int getCardOrderId() {
        return card_order_id;
    }

    public int getAccountId() {
        return account_id;
    }

    public int getAccountServiceId() {
        return account_service_id;
    }

    public int getDisbursementId() {
        return disbursement_id;
    }

    public int getReserveEntryId() {
        return reserve_entry_id;
    }

    public int getFeeAssetId() {
        return fee_asset_id;
    }

    public double getFeeAmount() {
        return fee_amount;
    }

    public String getStatus() {
        return status;
    }

    public String getExpireAt() {
        return expire_at;
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
