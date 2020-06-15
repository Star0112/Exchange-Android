package com.urgentrn.urncexchange.models.request;

import com.urgentrn.urncexchange.models.contacts.BaseWalletAddress;

import java.util.List;

public class WalletRequest {

    private String name;
    private ImageUploadRequest image;
    private List<BaseWalletAddress> addresses;

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(ImageUploadRequest image) {
        this.image = image;
    }

    public void setAddresses(List<BaseWalletAddress> addresses) {
        this.addresses = addresses;
    }
}
