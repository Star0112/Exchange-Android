package com.urgentrn.urncexchange.models;

import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.models.contacts.Contact;
import com.urgentrn.urncexchange.models.contacts.WalletData;

import org.greenrobot.eventbus.EventBus;

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

    private List<Wallet> wallets = new ArrayList<>();
    private List<Wallet> favoriteWallets = new ArrayList<>();
    private List<Symbol> symbols = new ArrayList<>();
    private List<PortfolioData> portfolioData = new ArrayList<>();

    private HashMap<String, ExchangeData> exchangeTickers = new HashMap<>();
    private HashMap<String, WalletAddress> walletAddresses = new HashMap<>();
    private DepositAddress depositAddress;

    private List<Contact> contacts = new ArrayList<>();
    private List<WalletData> walletData = new ArrayList<>();

    private List<CountryData> countries = new ArrayList<>();
    private HashMap<String, List<StateData>> states = new HashMap<>();



    private List<AssetBalance> assetBalance = new ArrayList<>();
    private List<DepositHistory> depositHistories = new ArrayList<>();
    private List<MarketInfo> marketInfo = new ArrayList<>();

    public List<Wallet> getWallets() {
        return ExchangeApplication.getApp().getUser() != null ? favoriteWallets : wallets;
    }

    public List<Wallet> getCurrencyWallets() {
        return getCurrencyWallets(false);
    }

    public List<Wallet> getCurrencyWallets(boolean hasStableCoin) {
        final List<Wallet> currencyWallets = new ArrayList<>();
        for (Wallet wallet : getWallets()) {
            if (hasStableCoin && !wallet.getSymbolData().isBlockChain() || !hasStableCoin && wallet.getSymbolData().isCurrency()) {
                currencyWallets.add(wallet);
            }
        }
        return currencyWallets;
    }

    public List<Wallet> getCryptoWallets() {
        return getCryptoWallets(true);
    }

    public List<Wallet> getCryptoWallets(boolean hasStableCoin) {
        final List<Wallet> cryptoWallets = new ArrayList<>();
        for (Wallet wallet : getWallets()) {
            if (hasStableCoin && !wallet.getSymbolData().isCurrency() || !hasStableCoin && wallet.getSymbolData().isBlockChain()) {
                cryptoWallets.add(wallet);
            }
        }
        return cryptoWallets;
    }


    public List<Symbol> getSymbols() {
        return symbols;
    }


    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts != null ? contacts : new ArrayList<>();
    }

    public List<CountryData> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryData> countries) {
        this.countries = countries;
    }

    public HashMap<String, List<StateData>> getStates() {
        return states;
    }



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
        wallets.clear();
        favoriteWallets.clear();
        portfolioData.clear();
        exchangeTickers.clear();
        walletAddresses.clear();
        depositAddress = null;
        contacts.clear();
        walletData.clear();


        assetBalance.clear();
    }

}
