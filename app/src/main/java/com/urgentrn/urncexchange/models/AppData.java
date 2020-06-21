package com.urgentrn.urncexchange.models;

import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.models.bank.Account;
import com.urgentrn.urncexchange.models.bank.FlowData;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.card.CardInfo;
import com.urgentrn.urncexchange.models.card.CardTransaction;
import com.urgentrn.urncexchange.models.card.GiftCard;
import com.urgentrn.urncexchange.models.contacts.Contact;
import com.urgentrn.urncexchange.models.contacts.WalletData;
import com.urgentrn.urncexchange.utils.WalletUtils;

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
    private HashMap<String, Market> marketCaps = new HashMap<>();
    private List<PortfolioData> portfolioData = new ArrayList<>();
    private List<Account> accounts;
    private FlowData flowData;
    private HashMap<String, ExchangeData> blockChainTickers = new HashMap<>();
    private HashMap<String, ExchangeData> currencyTickers = new HashMap<>();
    private HashMap<String, ExchangeData> stableCoinTickers = new HashMap<>();
    private HashMap<String, ExchangeData> exchangeTickers = new HashMap<>();
    private HashMap<String, WalletAddress> walletAddresses = new HashMap<>();
    private DepositAddress depositAddress;

    private List<CardInfo> availableCards = new ArrayList<>();
    private List<GiftCard> availableGiftCards;
    private List<Card> myCards;
    private List<CardTransaction> cardTransactions = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();
    private List<WalletData> walletData = new ArrayList<>();

    private List<CountryData> countries = new ArrayList<>();
    private HashMap<String, List<StateData>> states = new HashMap<>();
    private PlaidApi plaidApi;



    private List<AssetBalance> assetBalance = new ArrayList<>();

    public List<Wallet> getWallets() {
        return ExchangeApplication.getApp().getUser() != null && ExchangeApplication.getApp().getUser().getFavoriteFunds() == 1 ? favoriteWallets : wallets;
    }

    public List<Wallet> getMainWallets() {
        return wallets;
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

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
        EventBus.getDefault().post(wallets);
    }

    public List<Wallet> getFavoriteWallets() {
        return favoriteWallets;
    }

    public void setFavoriteWallets(List<Wallet> wallets) {
        this.favoriteWallets = wallets;
        EventBus.getDefault().post(wallets);
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<Symbol> symbols) {
        this.symbols = symbols;
        WalletUtils.setDefaultCurrencySymbol();
    }

    public Market getMarketCap(String symbol) {
        return marketCaps.get(symbol);
    }

    public void addMarketCap(String symbol, Market market) {
        marketCaps.put(symbol, market);
        EventBus.getDefault().post(symbol);
    }

    public List<PortfolioData> getPortfolioData() {
        return portfolioData;
    }

    public void setPortfolioData(List<PortfolioData> portfolioData) {
        this.portfolioData = portfolioData;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public FlowData getFlowData() {
        return flowData;
    }

    public void setFlowData(FlowData flowData) {
        this.flowData = flowData;
        EventBus.getDefault().post(flowData);
    }

    public void setBlockChainTickers(HashMap<String, ExchangeData> blockChainTickers) {
        blockChainTickers.remove("USD");
        this.blockChainTickers = blockChainTickers;
        EventBus.getDefault().post(exchangeTickers); // used for exchange menu on Wallet screen
    }

    public void setCurrencyTickers(HashMap<String, ExchangeData> currencyTickers) {
        this.currencyTickers = currencyTickers;
        EventBus.getDefault().post(exchangeTickers);
    }

    public void setStableCoinTickers(HashMap<String, ExchangeData> stableCoinTickers) {
        stableCoinTickers.remove("USD");
        this.stableCoinTickers = stableCoinTickers;
        EventBus.getDefault().post(exchangeTickers);
    }

    public HashMap<String, ExchangeData> getExchangeTickers(String type) {
        switch (type.toLowerCase()) {
            case "blockchain":
                return blockChainTickers;
            case "currency":
                return currencyTickers;
            case "stablecoin":
                return stableCoinTickers;
            default:
                return exchangeTickers;
        }
    }

    public HashMap<String, ExchangeData> getExchangeTickers() {
        return getExchangeTickers("blockChain");
    }

    public void setExchangeTickers(HashMap<String, ExchangeData> exchangeTickers) {
        this.exchangeTickers = exchangeTickers;
    }

    public WalletAddress getWalletAddress(String symbol) {
        return walletAddresses.get(symbol);
    }

    public void addWalletAddress(WalletAddress walletAddress) {
        walletAddresses.put(walletAddress.getSymbol(), walletAddress);
    }

    public DepositAddress getDepositAddress() {
        return depositAddress;
    }

    public void setDepositAddress(DepositAddress depositAddress) {
        this.depositAddress = depositAddress;
    }

    public List<CardInfo> getAvailableCards() {
        return availableCards;
    }

    public void setAvailableCards(List<CardInfo> availableCards) {
        this.availableCards = availableCards;
    }

    public List<GiftCard> getAvailableGiftCards() {
        return availableGiftCards;
    }

    public void setAvailableGiftCards(List<GiftCard> availableGiftCards) {
        this.availableGiftCards = availableGiftCards;
    }

    public List<Card> getMyCards() {
        return myCards;
    }

    public void setMyCards(List<Card> myCards) {
        this.myCards = myCards;
    }

    public List<CardTransaction> getCardTransactions() {
        return cardTransactions;
    }

    public void setCardTransactions(List<CardTransaction> cardTransactions) {
        this.cardTransactions = cardTransactions;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts != null ? contacts : new ArrayList<>();
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
    }

    public List<WalletData> getWalletData() {
        return walletData;
    }

    public void setWalletData(List<WalletData> data) {
        this.walletData = data != null ? data : new ArrayList<>();
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

    public PlaidApi getPlaidApi() {
        return plaidApi;
    }

    public void setPlaidApi(PlaidApi plaidApi) {
        this.plaidApi = plaidApi;
    }


    // For URNC
    public void setAssetBalanceData(List<AssetBalance> assetBalance) {
        this.assetBalance = assetBalance;
    }

    public List<AssetBalance> getAssetBalanceData() {
        return this.assetBalance;
    }

    public void clearData() {
        wallets.clear();
        favoriteWallets.clear();
//        symbols.clear();
//        marketCaps.clear();
        portfolioData.clear();
        accounts = null;
        flowData = null;
        exchangeTickers.clear();
        walletAddresses.clear();
        depositAddress = null;
        availableCards.clear();
        myCards = null;
        cardTransactions.clear();
        contacts.clear();
        walletData.clear();


        assetBalance.clear();
    }

    /**
     * Event Bus Models
     *
     * @see User
     * @see Card
     * @see com.urgentrn.urncexchange.models.response.GetAccountsResponse
     * @see List<Wallet>
     * @see com.urgentrn.urncexchange.models.response.SymbolResponse
     * @see String symbol
     * @see HashMap<String, ExchangeData>
     * @see FlowData
     */
}
