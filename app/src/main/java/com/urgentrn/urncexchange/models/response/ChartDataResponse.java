package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.ChartData;

import java.util.List;

public class ChartDataResponse extends BaseResponse {
    private List<ChartData> data;

    public List<ChartData> getData() { return data; }

}
