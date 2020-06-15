package com.urgentrn.urncexchange.models.card;

import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.card.token.CardToken;
import com.urgentrn.urncexchange.models.card.token.PlatformData;

import java.io.Serializable;
import java.util.List;

public class Card implements Serializable {

    private int id;
    private int card_order_id;
    private int entity_id;
    private int card_id;
    private Integer from_entity_card_id;
    private String reference;
    private String number;
    private String cvv;
    private String expiry_date;
    private boolean physical;
    private boolean virtual;
    private boolean gift;
    private boolean locked;
    private boolean activated;
    private boolean pin_set;
    private String status;
    private int freeze;
    private User entity;
    private CardInfo card;
    private CardOrder card_order;
    private List<GiftMeta> metas; // used for gift card
    private List<CardToken> tokens; // used for apple / google pay

    private boolean loadable;
    private String reserve_date;
    private String reserve_release_date;

    private PlatformData platformData;
    private double availableLimit;
    private double availableBalance;
    private double pendingBalance;
    private String balanceUpdatedAt;

    private boolean isUpgrading;

    public int getId() {
        return id;
    }

    public int getCardOrderId() {
        return card_order_id;
    }

    public int getEntityId() {
        return entity_id;
    }

    public int getCardId() {
        return card_id;
    }

    public Integer getFromEntityCardId() {
        return from_entity_card_id;
    }

    public String getReference() {
        return reference;
    }

    public String getNumber() {
        return number != null ? number : "";
    }

    public String getCvv() {
        return cvv;
    }

    public String getExpiryDate() {
        return expiry_date;
    }

    public boolean isPhysical() {
        return physical;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public boolean isGift() {
        return gift;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isActivated() {
        return activated;
    }

    public boolean isPinSet() {
        return pin_set;
    }

    public String getStatus() {
        return status != null ? status : "";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFreeze() {
        return freeze;
    }

    public User getEntity() {
        return entity;
    }

    public CardInfo getCardInfo() {
        return card;
    }

    public CardOrder getCardOrder() {
        return card_order;
    }

    public List<GiftMeta> getMetas() {
        return metas;
    }

    public String getMeta(String type) {
        for (GiftMeta meta : metas) {
            if (meta.getType().equals(type)) {
                return meta.getValue();
            }
        }
        return null;
    }

    public List<CardToken> getTokens() {
        return tokens;
    }

    public boolean isLoadable() {
        return loadable;
    }

    public PlatformData getPlatformData() {
        return platformData;
    }

    public double getAvailableLimit() {
        return availableLimit;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public double getPendingBalance() {
        return pendingBalance;
    }

    public String getBalanceUpdatedAt() {
        return balanceUpdatedAt;
    }

    public boolean isUpgrading() {
        return isUpgrading;
    }

    public void setUpgrading(boolean upgrading) {
        isUpgrading = upgrading;
    }
}
