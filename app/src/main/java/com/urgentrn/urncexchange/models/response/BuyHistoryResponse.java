package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.BuyHistory;

import java.util.List;

public class BuyHistoryResponse extends BaseResponse {
    private History data;

    public List<BuyHistory> getData() { return data.users; }

    private class History {
        private int offset;
        private int limit;
        private int total;
        private List<BuyHistory> users;
    }
}
