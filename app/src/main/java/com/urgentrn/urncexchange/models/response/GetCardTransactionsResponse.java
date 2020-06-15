package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.CardTransactionsHistory;

public class GetCardTransactionsResponse extends BaseResponse {

    private CardTransactionsHistory data;

    public CardTransactionsHistory getData() {
        return data;
    }
}
