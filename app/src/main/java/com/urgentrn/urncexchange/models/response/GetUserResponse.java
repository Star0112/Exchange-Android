package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.User;

public class GetUserResponse extends BaseResponse {

    private User data;

    public User getData() {
        return data;
    }
}
