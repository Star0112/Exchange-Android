package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.TierData;

public class GetTiersResponse extends BaseResponse {

    private TierData data;

    public TierData getData() {
        return data;
    }
}
