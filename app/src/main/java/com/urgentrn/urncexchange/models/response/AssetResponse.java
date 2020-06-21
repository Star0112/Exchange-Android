package com.urgentrn.urncexchange.models.response;


import com.urgentrn.urncexchange.models.AssetBalance;

import java.util.List;

public class AssetResponse extends BaseResponse {
    private List<AssetBalance> data;

    public List<AssetBalance> getData() {
        return data;
    }
}
