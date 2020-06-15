package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.card.Card;

public class GetCardResponse extends BaseResponse {

    private Card data;

    public Card getData() {
        return data;
    }
}
