package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.card.Card;

import java.util.HashMap;
import java.util.List;

public class GetCardsResponse extends BaseResponse {

    private HashMap<String, List<Card>> data;

    public List<Card> getData() {
        return data.get("cards");
    }
}
