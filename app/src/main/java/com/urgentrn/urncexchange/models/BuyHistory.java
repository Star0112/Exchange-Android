package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class BuyHistory implements Serializable {
    private int id;
    private String token;
    private String money;
    private String price;
    private String buyAmount;
    private String createdAt;
    private String updatedAt;
    private int userId;
    private user username;

    private class user {
        private String username;
    }
}
