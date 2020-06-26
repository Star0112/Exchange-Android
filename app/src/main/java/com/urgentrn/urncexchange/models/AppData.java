package com.urgentrn.urncexchange.models;

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
    private List<DepositHistory> depositHistories = new ArrayList<>();
    private List<MarketInfo> marketInfo = new ArrayList<>();


    // For URNC
    public void setAssetBalanceData(List<AssetBalance> assetBalance) {
        this.assetBalance = assetBalance;
    }

    public void setDepositHistoryData(List<DepositHistory> depositHistories) {
        this.depositHistories = depositHistories;
    }

    public void setMarketInfoData(List<MarketInfo> marketInfo) {
        this.marketInfo = marketInfo;
    }

    public List<AssetBalance> getAssetBalanceData() {
        return this.assetBalance;
    }

    public List<DepositHistory> getDepositHistoryData() {
        return this.depositHistories;
    }

    public List<MarketInfo> getMarketInfoData() { return this.marketInfo; }



    public void clearData() {
        marketInfo.clear();
        depositHistories.clear();
        assetBalance.clear();
    }

}
