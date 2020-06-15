package com.urgentrn.urncexchange.models.card;

import com.urgentrn.urncexchange.models.Asset;

import java.io.Serializable;

public class CardTransaction implements Serializable {

    private int id;
    private int from_card_transaction_id;
    private int entity_card_id;
    private int transaction_id;
    private String merchant;
    private String reference;
    private String authorization;
    private String type;
    private String status;
    private String currency;
    private double approved;
    private double total;
    private String created_at;
    private String updated_at;
    private Transaction transaction;
    private CardExchangeQuote exchange_quote;
    private double conversion_rate;
    private double amount;

    private boolean selected;

    public int getId() {
        return id;
    }

    public int getFromCardTransactionId() {
        return from_card_transaction_id;
    }

    public int getEntityCardId() {
        return entity_card_id;
    }

    public int getTransactionId() {
        return transaction_id;
    }

    public String getMerchant() {
        return merchant;
    }

    public String getReference() {
        return reference;
    }

    public String getAuthorization() {
        return authorization;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrency() {
        return currency;
    }

    public double getApproved() {
        return approved;
    }

    public double getTotal() {
        return total;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public CardExchangeQuote getExchangeQuote() {
        return exchange_quote;
    }

    public double getConversionRate() {
        return conversion_rate;
    }

    public double getAmount() {
        return amount;
    }

    public class Transaction implements Serializable {

        private int id;
        private int asset_id;
        private int entity_id;
        private String hash;
        private String type;
        private String status;
        private double amount;
        private String created_at;
        private String updated_at;
        private String deleted_at;
        private Asset asset;

        public int getId() {
            return id;
        }

        public int getAssetId() {
            return asset_id;
        }

        public int getEntityId() {
            return entity_id;
        }

        public String getHash() {
            return hash;
        }

        public String getType() {
            return type;
        }

        public String getStatus() {
            return status;
        }

        public double getAmount() {
            return amount;
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

        public Asset getAsset() {
            return asset;
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
