package com.urgentrn.urncexchange.models.card;

import java.io.Serializable;
import java.util.List;

public class CardInfo implements Serializable {

    private int id;
    private int asset_id;
    private String title;
    private String platform;
    private String physical;
    private String loadable;
    private String virtual;

    private String gift;
    private String tokenizable;

    private boolean status;

    private int order;
    private String terms;
    private String privacy_policy;

    private List<Image> images;
    private List<CardDetail> details;
    private double fee;
    private int reserve_asset_id;
    private String reserve_asset;
    private double reserve_amount;
    private String reserve_period;
    private int reserve_period_factor;

    private String pin_type;

    private List<CardUpgrade> upgrades;
    private CardUpgrade convert_to_physical;

    private int upgradeId;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPlatform() {
        return platform;
    }

    public boolean isPhysical() {
        return "1".equals(physical) || "true".equals(physical);
    }

    public boolean isVirtual() {
        return "1".equals(virtual) || "true".equals(virtual);
    }

    public boolean isGift() {
        return "1".equals(gift) || "true".equals(gift);
    }

    public boolean isTokenizable() {
        return "1".equals(tokenizable) || "true".equals(tokenizable);
    }

    public boolean isStatus() {
        return status;
    }

    public int getOrder() {
        return order;
    }

    public String getTerms() {
        return terms;
    }

    public String getPrivacyPolicy() {
        return privacy_policy;
    }

    public List<Image> getImages() {
        return images;
    }

    private String getImage(String type) {
        for (Image image : images) {
            if (image.getType().equals(type)) {
                return image.getImage();
            }
        }
        return null;
    }

    public String getBaseImage() {
        return getImage("base");
    }

    public String getFrontImage() {
        return getImage("front");
    }

    public String getBackImage() {
        return getImage("back");
    }

    public List<CardDetail> getDetails() {
        return details;
    }

    public double getFee() {
        return fee;
    }

    public String getReserveAsset() {
        return reserve_asset;
    }

    public double getReserveAmount() {
        return reserve_amount;
    }

    public String getReservePeriod() {
        return reserve_period;
    }

    public int getReservePeriodFactor() {
        return reserve_period_factor;
    }

    public String getPinType() {
        return pin_type;
    }

    public List<CardUpgrade> getUpgrades() {
        return upgrades;
    }

    public CardUpgrade getConvertToPhysical() {
        return convert_to_physical;
    }

    public class Image implements Serializable {

        private String type;
        private String image;

        private String path; // from card info api response

        public String getType() {
            return type;
        }

        public String getImage() {
            return image;
        }
    }

    public int getUpgradeId() {
        return upgradeId;
    }

    public void setUpgradeId(int upgradeId) {
        this.upgradeId = upgradeId;
    }
}
