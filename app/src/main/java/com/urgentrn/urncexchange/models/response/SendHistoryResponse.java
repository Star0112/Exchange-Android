package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.SendHistory;

import java.util.List;

public class SendHistoryResponse extends BaseResponse {
    private History data;

    public List<SendHistory> getData() { return data.users; }

    private class History {
        private int offset;
        private int limit;
        private int total;
        private List<SendHistory> users;
    }
}
