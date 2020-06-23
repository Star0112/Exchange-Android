package com.urgentrn.urncexchange.utils;

import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.models.AppConfig;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.Wallet;

public class WalletUtils {

    public enum AssetRestriction {
        LOCKED,
        ZERO,
        UPGRADE,
        UNLOCKED,
    }

    public enum TransactionType {
        BUY,
        SELL,
        DEPOSIT,
        WITHDRAW,
        EXCHANGE,
        SEND,
        RECEIVE,
        LOAD,
        GIFT,
        NETWORK,
    }

    public static Symbol getSymbolData(String symbol) {
        for (Symbol data : AppData.getInstance().getSymbols()) {
            if (data.getSymbol().equals(symbol)) {
                return data;
            }
        }
        return null;
    }

    public static Symbol getSymbolData(int id) {
        for (Symbol data : AppData.getInstance().getSymbols()) {
            if (data.getId() == id) {
                return data;
            }
        }
        return null;
    }

    public static Symbol defaultCurrencySymbol;

    public static void setDefaultCurrencySymbol() {
        if (ExchangeApplication.getApp().getUser() == null) return;
        for (Symbol data : AppData.getInstance().getSymbols()) {
//            if (data.getId() == ExchangeApplication.getApp().getUser().getCurrencyAssetId()) {
//                defaultCurrencySymbol = data;
//                return;
//            }
        }
    }

    public static Wallet getDefaultCurrencyWallet() {
        if (ExchangeApplication.getApp().getUser() == null) return null;
        for (Wallet data : AppData.getInstance().getCurrencyWallets()) {
//            if (data.getSymbolData().getId() == ExchangeApplication.getApp().getUser().getCurrencyAssetId()) {
//                return data;
//            }
        }
        return null;
    }

    public static String checkBuyOrSell(String symbol1, String symbol2) {
        for (Wallet wallet : AppData.getInstance().getWallets()) {
            if (wallet.getSymbolData().getType().equals("currency")) {
                if (wallet.getSymbol().equals(symbol1)) {
                    return "buy";
                }
                if (wallet.getSymbol().equals(symbol2)) {
                    return "sell";
                }
            }
        }
        return "";
    }

    public static double getTotalBalance() {
        double balance = 0;
        for (Wallet wallet : AppData.getInstance().getWallets()) {
            balance += wallet.getBalanceCurrency();
        }
        return balance;
    }

    public static AssetRestriction getAssetRestriction(String type, boolean skipPending) {
        if (ExchangeApplication.getApp().getConfig() == null || ExchangeApplication.getApp().getUser() == null) return null; // TODO: when does this happen?
        final AppConfig.Restrictions.TierLevel tierLevel = ExchangeApplication.getApp().getConfig().getRestrictions().getAssets().get(type);
        if (tierLevel != null) {
//            final int assetLevel = tierLevel.getTier();
//            final int userLevel = ExchangeApplication.getApp().getUser().getTierLevel();
//            if (assetLevel < userLevel || (assetLevel == userLevel && (skipPending || ExchangeApplication.getApp().getUser().isTierCompleted()))) {
//                return AssetRestriction.UNLOCKED;
//            } else {
//                if (userLevel == 0) {
//                    return AssetRestriction.UPGRADE;
//                } else {
                    return AssetRestriction.UPGRADE;
//                }
//            }
        } else {
            return AssetRestriction.LOCKED;
        }
    }

    public static int getWalletIndex(Wallet wallet) {
        for (int i = 0; i < AppData.getInstance().getWallets().size(); i ++) {
            if (wallet.getSymbol().equals(AppData.getInstance().getWallets().get(i).getSymbol())) {
                return i;
            }
        }
        return -1;
    }

    public static int getCryptoWalletIndex(Wallet wallet) {
        for (int i = 0; i < AppData.getInstance().getCryptoWallets().size(); i ++) {
            if (wallet.getSymbol().equals(AppData.getInstance().getCryptoWallets().get(i).getSymbol())) {
                return i;
            }
        }
        return -1;
    }

    public static int getBestWalletIndex() {
        int index = -1;
        double maxBalance = -1;
        for (int i = 0; i < AppData.getInstance().getWallets().size(); i ++) {
            final Wallet wallet = AppData.getInstance().getWallets().get(i);
            if (!wallet.isExchangeWallet() && wallet.getBalanceCurrency() > maxBalance) {
                maxBalance = wallet.getBalanceCurrency();
                index = i;
            }
        }
        return index >= 0 ? index : 0/* TODO: when does this happen? */;
    }
}
