package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.card.GiftCardData;

public class GetGiftCardsResponse extends BaseResponse {

    private GiftCardData data;

    public GiftCardData getData() {
        return data;
    }
}
