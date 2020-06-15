package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.RewardData;

import java.util.List;

public class GetRewardsResponse extends BaseResponse {

    private List<RewardData> data;

    public List<RewardData> getData() {
        return data;
    }
}
