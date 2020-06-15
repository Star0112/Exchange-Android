package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.ExchangeData;

import java.util.HashMap;

public class GetExchangeTickersResponse extends BaseResponse {

    private HashMap<String, ExchangeData> data;

    public HashMap<String, ExchangeData> getData() {
        return data;
    }
}
