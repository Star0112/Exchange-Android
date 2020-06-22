package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.DepositHistory;

import java.util.List;

public class DepositHistoryResponse extends BaseResponse {
    private History data;


    public List<DepositHistory> getData() {
        return data.data;
    }
    private class History {
        int offset;
        int limit;
        int total;
        private List<DepositHistory> data;
    }
}
