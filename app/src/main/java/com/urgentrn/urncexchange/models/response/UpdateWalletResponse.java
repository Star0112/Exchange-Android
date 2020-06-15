package com.urgentrn.urncexchange.models.response;

public class UpdateWalletResponse extends BaseResponse {

    UpdateWalletData data;

    public boolean isDefault() {
        return data.isDefault;
    }

    private class UpdateWalletData {

        private boolean isDefault;
    }
}
