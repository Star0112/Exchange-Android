package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.DepositAddress;

public class GetDepositAddressResponse extends BaseResponse {

    private DepositAddress data;

    public DepositAddress getData() {
        return data;
    }
}
