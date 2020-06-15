package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.TransactionsHistory;

public class TransactionsResponse extends BaseResponse {

    private TransactionsHistory data;

    public TransactionsHistory getData() {
        return data;
    }
}
