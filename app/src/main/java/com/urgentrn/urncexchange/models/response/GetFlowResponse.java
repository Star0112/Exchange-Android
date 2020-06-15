package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.bank.FlowData;

public class GetFlowResponse extends BaseResponse {

    private FlowData data;

    public FlowData getData() {
        return data;
    }
}
