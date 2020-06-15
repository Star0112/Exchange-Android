package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.card.CardInfo;

import java.util.List;

public class GetAvailableCardsResponse extends BaseResponse {

    private List<CardInfo> data;

    public List<CardInfo> getData() {
        return data;
    }
}
