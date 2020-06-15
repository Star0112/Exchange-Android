package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.Symbol;

import java.util.List;

public class SymbolResponse extends BaseResponse {

    private List<Symbol> data;

    public List<Symbol> getData() {
        return data;
    }
}
