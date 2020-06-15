package com.urgentrn.urncexchange.models.contacts;

import com.urgentrn.urncexchange.models.ImageData;
import com.urgentrn.urncexchange.models.User;

public class Contact extends BaseContact {

    private String phone;
    private String email;
    private String username;
    private ImageData image;

    private User entity;

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public ImageData getImage() {
        return image;
    }
}
