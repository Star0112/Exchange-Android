package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.Banner;

import java.util.List;

public class GetBannersResponse extends BaseResponse {

    private List<Banner> data;

    public List<Banner> getData() {
        return data;
    }
}
