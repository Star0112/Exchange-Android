package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.contacts.WalletData;

import java.util.List;

public class GetWalletsResponse extends BaseResponse {

    private List<WalletData> data;

    public List<WalletData> getData() {
        return data;
    }
}
