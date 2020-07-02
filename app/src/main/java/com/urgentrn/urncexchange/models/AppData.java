package com.urgentrn.urncexchange.models;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppData {

    private static AppData INSTANCE = null;

    public static synchronized AppData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppData();
        }
        return INSTANCE;
    }

    private List<AssetBalance> assetBalance = new ArrayList<>();
    private List<MarketInfo> marketInfo = new ArrayList<>();


    // For URNC
    public void setAssetBalanceData(List<AssetBalance> assetBalance) {
        this.assetBalance = assetBalance;
        EventBus.getDefault().post(assetBalance);
    }

    public void setMarketInfoData(List<MarketInfo> marketInfo) {
        this.marketInfo = marketInfo;
    }

    public List<AssetBalance> getAssetBalanceData() {
        return this.assetBalance;
    }

    public List<MarketInfo> getMarketInfoData() { return this.marketInfo; }




    public void clearData() {
        marketInfo.clear();
        assetBalance.clear();
    }

}
