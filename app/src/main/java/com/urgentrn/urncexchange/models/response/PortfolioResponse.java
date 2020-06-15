package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.PortfolioData;

import java.util.List;

public class PortfolioResponse extends BaseResponse {

    private List<PortfolioData> data;

    public List<PortfolioData> getData() {
        return data;
    }
}
