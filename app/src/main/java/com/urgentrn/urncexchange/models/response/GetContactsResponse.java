package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.contacts.ContactsData;

public class GetContactsResponse extends BaseResponse {

    private ContactsData data;

    public ContactsData getData() {
        return data;
    }
}
