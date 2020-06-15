package com.urgentrn.urncexchange.models.request;

public class ContactRequest {

    private String name;
    private String email;
    private String username;
    private String phone;
    private ImageUploadRequest image;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage(ImageUploadRequest image) {
        this.image = image;
    }
}
