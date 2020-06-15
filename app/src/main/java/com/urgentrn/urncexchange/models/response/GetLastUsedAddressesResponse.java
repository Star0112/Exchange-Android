package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.RecentWalletAddress;

import java.util.List;

public class GetLastUsedAddressesResponse extends BaseResponse {

    private List<RecentWalletAddress> data;

    public List<RecentWalletAddress> getData() {
        return data;
    }
}
