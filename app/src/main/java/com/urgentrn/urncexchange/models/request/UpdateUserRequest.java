package com.urgentrn.urncexchange.models.request;

public class UpdateUserRequest {

    private Boolean mfa;
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
    private String birthdate;
    private String phone;
    private Boolean terms_acceptance;

    private Integer favoriteAssets;
    private Integer favoriteFunds;

    public void setMfa(boolean mfa) {
        this.mfa = mfa;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public void setNinType(String ninType) {
        this.ninType = ninType;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiry_date = expiryDate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void acceptTerms(boolean termsAcceptance) {
        this.terms_acceptance = termsAcceptance;
    }

    public void setFavoriteAssets(int favoriteAssets) {
        this.favoriteAssets = favoriteAssets;
    }

    public void setFavoriteFunds(int favoriteFunds) {
        this.favoriteFunds = favoriteFunds;
    }
}
