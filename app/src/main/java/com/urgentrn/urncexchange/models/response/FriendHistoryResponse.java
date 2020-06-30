package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.InviteUser;

import java.util.List;

public class FriendHistoryResponse extends BaseResponse {
    private History data;

    public List<InviteUser> getData() {
        return data.referees;
    }

    class History {
        private int offset;
        private int limit;
        private int total;
        List<InviteUser> referees;
    }
}
