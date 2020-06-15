package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.Wallet;

import java.util.List;

public class WalletResponse extends BaseResponse {

    private List<Wallet> data;

    public List<Wallet> getData() {
        return data;
    }
}
