package com.urgentrn.urncexchange.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class User implements Serializable {

    private int id;
    private String username;
    private String recordId;
    private String password;
    private boolean phoneVerified;
    private boolean emailVerified;
    private boolean mfa;
    private boolean softwareMfa;
    private List<String> availableMfa;
    private Limits limits;
    private Restrictions restrictions;
    private String tierVerification;
    private int fund_id;
    private int currency_asset_id;
    private int referral_code_id;
    private String firstName;
    private String lastName;
    private String country;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String nin;
    private String ninType;
    private String expiry_date;
    private String email;
    private String birthdate;
    private String phone;
    private int tierLevel;
    private String tierStatus;
    private int vcLimit;
    private String cardType;
    private String last_login_ip;
    private String last_login_at;
    private int activate_sxp;
    private int sxp_status;
    private int bank_acceptance;
    private int discount;
    private int inactive;
    private int freeze;
    private int terms_acceptance;

    private int favoriteFunds;
    private int favoriteAssets;
    // used for add favorite only
    private boolean isDefault;
    private boolean favorite;

    private String avatar;

    private String code;
    private VerificationCode verificationCode;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getPassword() {
        return password;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isMfa() {
        return mfa;
    }

    public void setMfa(boolean mfa) {
        this.mfa = mfa;
    }

    public boolean isSoftwareMfa() {
        return softwareMfa;
    }

    public List<String> getAvailableMfa() {
        return availableMfa;
    }

    public Limits getLimits() {
        return limits;
    }

    public String getTierVerification() {
        return tierVerification;
    }

    public int getFundId() {
        return fund_id;
    }

    public int getCurrencyAssetId() {
        return currency_asset_id;
    }

    public void setCurrencyAssetId(int currencyAssetIdr) {
        this.currency_asset_id = currencyAssetIdr;
    }

    public int getReferralCodeId() {
        return referral_code_id;
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipcode;
    }

    public String getNin() {
        return nin;
    }

    public String getNinType() {
        return ninType;
    }

    public String getExpiryDate() {
        return expiry_date;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public int getTierLevel() {
        return tierLevel;
    }

    public String getTierStatus() {
        return tierStatus != null ? tierStatus : "";
    }

    public boolean isTierPending() {
        return getTierStatus().equalsIgnoreCase("pending");
    }

    public boolean isTierCompleted() {
        return getTierStatus().equalsIgnoreCase("completed");
    }

    public int getVcLimit() {
        return vcLimit;
    }

    public String getCardType() {
        return cardType;
    }

    public String getLastLoginIp() {
        return last_login_ip;
    }

    public String getLastLoginAt() {
        return last_login_at;
    }

    public int getActivateSXP() {
        return activate_sxp;
    }

    public void setActivateSXP(int activateSXP) {
        this.activate_sxp = activateSXP;
    }

    public int getSXPStatus() {
        return sxp_status;
    }

    public void setSXPStatus(int sxpStatus) {
        this.sxp_status = sxpStatus;
    }

    public int getBankAcceptance() {
        return bank_acceptance;
    }

    public void setBankAcceptance(int bankAcceptance) {
        this.bank_acceptance = bankAcceptance;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getFreeze() {
        return freeze;
    }

    public boolean termsAccepted() {
        return terms_acceptance == 1;
    }

    public void acceptTerms(int termsAcceptance) {
        terms_acceptance = termsAcceptance;
    }

    public int getFavoriteFunds() {
        return favoriteFunds;
    }

    public void setFavoriteFunds(int favoriteFunds) {
        this.favoriteFunds = favoriteFunds;
    }

    public int getFavoriteAssets() {
        return favoriteAssets;
    }

    public void setFavoriteAssets(int favoriteAssets) {
        this.favoriteAssets = favoriteAssets;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCode() {
        return code;
    }

    public VerificationCode getVerificationCode() {
        return verificationCode;
    }

    public class Limits implements Serializable {

        private HashMap<String, Limit> wallet;
        private HashMap<String, Limit> bank;
        private Limit card;
        private Limit wire;

        public HashMap<String, Limit> getWallet() {
            return wallet != null ? wallet : new HashMap<>();
        }

        public HashMap<String, Limit> getBank() {
            return bank != null ? bank : new HashMap<>();
        }

        public Limit getCard() {
            return card;
        }

        public Limit getWire() {
            return wire;
        }
    }

    private class Restrictions implements Serializable {

        private boolean receive;
        private boolean send;
        private boolean card;

        public boolean isReceive() {
            return receive;
        }

        public boolean isSend() {
            return send;
        }

        public boolean isCard() {
            return card;
        }
    }

    private class VerificationCode implements Serializable {

        private String Destination, DeliveryMedium, AttributeName;

        public String getDestination() {
            return Destination;
        }

        public String getDeliveryMedium() {
            return DeliveryMedium;
        }

        public String getAttributeName() {
            return AttributeName;
        }
    }
}
