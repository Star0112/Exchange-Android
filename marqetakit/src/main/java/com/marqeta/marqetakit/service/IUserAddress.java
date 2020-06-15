package com.marqeta.marqetakit.service;

/**
 * Created by Jon Weissenburger on 9/28/17.
 */

public interface IUserAddress {

    void setAddress1(String address1);
    String getAddress1();

    void setAddress2(String address2);
    String getAddress2();

    void setName(String name);
    String getName();

    void setCity(String city);
    String getCity();

    void setState(String state);
    String getState();

    void setZip(String zip);
    String getZip();

    void setCountry(String country);
    String getCountry();

    void setPhone(String phone);
    String getPhone();

}
