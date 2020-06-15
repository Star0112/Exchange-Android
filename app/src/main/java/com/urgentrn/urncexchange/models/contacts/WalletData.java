package com.urgentrn.urncexchange.models.contacts;

import com.urgentrn.urncexchange.models.ImageData;

import java.util.List;

public class WalletData extends BaseContact {

    private List<WalletAddress> addresses;
    private List<ImageData> images;

    public List<WalletAddress> getAddresses() {
        return addresses;
    }

    public List<ImageData> getImages() {
        return images;
    }

    public ImageData getDefaultImage() {
        if (images != null) {
            for (ImageData image : images) {
                if (image.getType().equals("default")) {
                    return image;
                }
            }
        }
        return null;
    }
}
