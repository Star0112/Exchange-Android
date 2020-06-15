package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.WalletAddress;

public class GetAddressResponse extends BaseResponse {

    private WalletAddress data;

    public WalletAddress getData() {
        return data;
    }
}
