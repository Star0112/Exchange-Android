package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.CountryData;

import java.util.List;

public class GetCountriesResponse extends BaseResponse {

    private List<CountryData> data;

    public List<CountryData> getData() {
        return data;
    }
}
