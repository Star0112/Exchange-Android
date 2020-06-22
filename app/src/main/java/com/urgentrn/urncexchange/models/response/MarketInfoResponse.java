package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.MarketInfo;

import java.util.List;

public class MarketInfoResponse extends BaseResponse {
    private List<MarketInfo> data;

    public List<MarketInfo> getData() {
        return data;
    }
}
