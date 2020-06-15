package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.StateData;

import java.util.List;

public class GetStatesResponse extends BaseResponse {

    private List<StateData> data;

    public List<StateData> getData() {
        return data;
    }
}
