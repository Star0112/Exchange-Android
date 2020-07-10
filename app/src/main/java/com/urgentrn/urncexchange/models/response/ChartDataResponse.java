package com.urgentrn.urncexchange.models.response;

import java.util.List;

public class ChartDataResponse extends BaseResponse {
    private List<List<String>> data;

    public List<List<String>> getData() { return data; }

}
