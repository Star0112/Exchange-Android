package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.bank.Account;

import java.util.List;

public class GetAccountsResponse extends BaseResponse {

    private List<Account> data;

    public List<Account> getData() {
        return data;
    }
}
