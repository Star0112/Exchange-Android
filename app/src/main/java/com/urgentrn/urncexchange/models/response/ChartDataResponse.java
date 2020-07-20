package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.KlineData;

import java.util.List;

public class ChartDataResponse extends BaseResponse {
    private List<KlineData> data;

    public List<KlineData> getData() { return data; }

}
