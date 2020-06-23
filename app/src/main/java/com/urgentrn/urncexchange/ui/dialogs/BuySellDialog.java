package com.urgentrn.urncexchange.ui.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn769.Numpad;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.StripeIntent;
import com.urgentrn.urncexchange.BuildConfig;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.ExchangeQuote;
import com.urgentrn.urncexchange.models.Limit;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.bank.Account;
import com.urgentrn.urncexchange.models.bank.FlowData;
import com.urgentrn.urncexchange.models.bank.FlowService;
import com.urgentrn.urncexchange.models.request.DisbursementRequest;
import com.urgentrn.urncexchange.models.request.ExchangeQuoteRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.ExchangeConfirmResponse;
import com.urgentrn.urncexchange.models.response.ExchangeQuoteResponse;
import com.urgentrn.urncexchange.models.response.GetExchangeTickersResponse;
//import com.urgentrn.urncexchange.ui.account.AddAccountActivity_;
import com.urgentrn.urncexchange.ui.adapter.LinkedAccountAdapter;
import com.urgentrn.urncexchange.ui.adapter.SelectCurrencyAdapter;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
//import com.urgentrn.urncexchange.ui.transactions.BuySellSuccessActivity_;
import com.urgentrn.urncexchange.utils.CardUtils;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.urgentrn.urncexchange.utils.WalletUtils.TransactionType;

@EFragment(R.layout.dialog_buy_sell)
public class BuySellDialog extends BaseDialog implements ApiCallback {

    @ViewById
    View llBuySell, llWallet, llAccount, imgSwitch, llPercent;

    @ViewById
    TextView txtTitle, txtPriceAvailable, txtTitleSelect, txtPrice, txtAmount, txtPercent1, txtPercent2, txtPercent3, txtCoin, txtLimit, txtBalance, txtAddPayment, txtAccount, txtWallet, btnNext, txtTotal;

    @ViewById
    ImageView imgCoin, imgCard;

    @ViewById
    Numpad padView;

    // Confirm View
    @ViewById
    View llConfirm;

    // Select Wallet View
    @ViewById
    View llSelectWallet, llAccounts, llAddPayment, paddingView;

    @ViewById
    RecyclerView recyclerAccounts, recyclerView;

    private LinkedAccountAdapter adapterAccounts, adapterWireAccounts;
    private SelectCurrencyAdapter adapterCurrency;

    private Wallet wallet, currencyWallet;
    private Account selectedAccount;
    private boolean isAccountSelected;
    private TransactionType type;
    private int accountId/*selected Bank Account ID*/;
    private ExchangeQuote quote;
    private Stripe stripe;

    private char currencySymbol;
    private double price/*USD*/, amount/*Coin*/, fee/*used to show amount in deposit/withdraw success screen*/;
    private double[] percents = {0.25, 0.5, 1};
    private boolean isCoin = false;

    @AfterViews
    protected void init() {
        wallet = (Wallet)(getArguments().getSerializable("wallet"));
        currencySymbol = wallet.getCurrencySymbol();
        type = TransactionType.values()[getArguments().getInt("type")];
        final int color = Color.parseColor(wallet.getColor());

        price = amount = 0;
        isCoin = false;

        int titleRes;
        switch (type) {
            case BUY:
                titleRes = R.string.buy;
                txtPriceAvailable.setVisibility(View.GONE);
                break;
            case SELL:
                titleRes = R.string.sell;
                txtPriceAvailable.setText(String.format("%s %s %s", Utils.formattedNumber(wallet.getBalance(), 0, 5), wallet.getSymbol(), getString(R.string.available)));
                txtPriceAvailable.setVisibility(View.VISIBLE);
                break;
            case DEPOSIT:
                titleRes = R.string.deposit;
                txtPriceAvailable.setVisibility(View.GONE);
                break;
            case WITHDRAW:
                titleRes = R.string.withdraw;
                txtPriceAvailable.setVisibility(View.GONE);
                break;
            default:
                titleRes = 0;
        }
        txtTitle.setText(getString(R.string.title_dialog_buy_sell, getString(titleRes), wallet.getTitle()));

        if (isCoin) {
            txtAmount.setText(String.format("%c%s", currencySymbol, Utils.formattedNumber(price)));
            txtPrice.setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
        } else {
            txtPrice.setText(String.format("%c%s", currencySymbol, Utils.formattedNumber(price)));
            txtAmount.setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
        }
        txtPrice.setTextColor(color);
        txtTotal.setTextColor(color);
        txtAmount.setTextColor(getResources().getColor(R.color.textColorDefault));
        if (wallet.getSymbolData().isCurrency()) {
            txtAmount.setVisibility(View.GONE);
            imgSwitch.setVisibility(View.GONE);
        }

        llPercent.setVisibility(type == TransactionType.BUY ? View.GONE : View.VISIBLE);

        padView.setDecimal(true);
        padView.setOnTextChangeListener((text, digits_remaining) -> {
            final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
            double marketPrice;
            if (symbol != null && symbol.getMarketData() != null) {
                marketPrice = symbol.getMarketData().getPrice();
            } else {
                marketPrice = wallet.getSymbolData().getPrice();
            }
            if (isCoin) {
                amount = padView.getValue();
                price = amount * marketPrice;
                txtAmount.setText(String.format("%c%s", currencySymbol, Utils.formattedNumber(price)));
                txtPrice.setText(String.format("%s %s", text, wallet.getSymbol()));
            } else {
                price = padView.getValue();
                amount = price / marketPrice;
                txtPrice.setText(String.format("%c%s", currencySymbol, text));
                txtAmount.setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
            }
        });

        // Select Wallet View
        llSelectWallet.setVisibility(View.GONE);
        txtTitleSelect.setText(getString(R.string.title_dialog_buy_sell, getString(titleRes), wallet.getTitle()));
        initSelectAccountView();
        initSelectWalletView();

        updateWalletView();

        // Confirm View
        llConfirm.setVisibility(View.GONE);

        initPassView(Constants.SecurityType.TRANSACTION, null, isSuccess -> {
            if (isSuccess) onConfirm();
        });
    }

    private void initSelectAccountView() { // Electronic Transfer Accounts view for DEPOSIT / WITHDRAW
        if (type == TransactionType.SELL) {
            llAccounts.setVisibility(View.GONE);
            paddingView.setVisibility(View.VISIBLE);

            isAccountSelected = false;
        } else {
            llAccounts.setVisibility(View.VISIBLE);
            adapterAccounts = new LinkedAccountAdapter(position -> {
                final Account account = adapterAccounts.getItem(position);
                if (account.getStatus().equalsIgnoreCase("pending") || account.getStatus().equalsIgnoreCase("failed")) {
                    showAlert(getString(R.string.error_account_pending, account.getStatus()));
                } else {
                    if (type == TransactionType.BUY) {
                        adapterCurrency.setSelectedPosition(-1);
                    } else {
                        adapterWireAccounts.setSelectedPosition(-1);
                    }
                    adapterAccounts.setSelectedPosition(position);
                    isAccountSelected = true;
                    selectedAccount = adapterAccounts.getItem(position);
                    updateWalletView();
                    onBack();
                }
            }, true);
            final LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
            recyclerAccounts.setLayoutManager(layoutManager1);
            recyclerAccounts.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager1.getOrientation()));
            recyclerAccounts.setAdapter(adapterAccounts);
            llAddPayment.setOnClickListener(v -> {
                onAddPayment();
            });
            if (type == TransactionType.BUY) {
                txtAddPayment.setText(R.string.add_payment_method);
                txtAccount.setText(R.string.payment_accounts);
                txtWallet.setText(R.string.wallets);
            } else { // DEPOSIT / WITHDRAW
                txtAddPayment.setText(R.string.add_bank_account);
                txtAccount.setText(R.string.electronic_transfer_accounts);
                txtWallet.setText(R.string.wire_transfer_accounts);
            }
            paddingView.setVisibility(View.GONE);

            List<Account> accounts;
            if (type == TransactionType.BUY) {
                accounts = AppData.getInstance().getAccounts();
            } else {
                accounts = new ArrayList<>();
                if (AppData.getInstance().getAccounts() != null) {
                    for (Account account : AppData.getInstance().getAccounts()) {
                        for (FlowService service : account.getServices()) {
                            if (service.getName().equalsIgnoreCase("ach")) {
                                accounts.add(account);
                                break;
                            }
                        }
                    }
                }
            }
            adapterAccounts.setData(accounts);

            if (accounts != null && accounts.size() > 0) {
                selectedAccount = accounts.get(0);
                adapterAccounts.setSelectedPosition(0);
            } else {
                selectedAccount = null;
            }

            // to set account selected as default for deposit / withdraw
            isAccountSelected = accounts != null && accounts.size() > 0 && type != TransactionType.BUY;
        }
    }

    private void initSelectWalletView() { // Wire Transfer Accounts view for DEPOSIT / WITHDRAW
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        if (type == TransactionType.BUY || type == TransactionType.SELL) {
            adapterCurrency = new SelectCurrencyAdapter(position -> {
                if (type == TransactionType.BUY) adapterAccounts.setSelectedPosition(-1);
                adapterCurrency.setSelectedPosition(position);
                isAccountSelected = false;
                currencyWallet = adapterCurrency.getItem(position);
                updateWalletView();
                onBack();
            });
            recyclerView.setAdapter(adapterCurrency);

            final List<Wallet> currencyWallets = AppData.getInstance().getCurrencyWallets(!wallet.getSymbolData().isStableCoin());
            adapterCurrency.setData(currencyWallets);

            if (currencyWallets.size() > 0) {
                currencyWallet = currencyWallets.get(0);
                if (type == TransactionType.BUY) adapterAccounts.setSelectedPosition(-1);
                adapterCurrency.setSelectedPosition(0);
            }
        } else {
            adapterWireAccounts = new LinkedAccountAdapter(position -> {
                final Account account = adapterWireAccounts.getItem(position);
                if (account.getStatus().equalsIgnoreCase("pending") || account.getStatus().equalsIgnoreCase("failed")) {
                    showAlert(getString(R.string.error_account_pending, account.getStatus()));
                } else {
                    adapterAccounts.setSelectedPosition(-1);
                    adapterWireAccounts.setSelectedPosition(position);
                    isAccountSelected = false;
                    selectedAccount = adapterWireAccounts.getItem(position);
                    updateWalletView();
                    onBack();
                }
            }, true);
            recyclerView.setAdapter(adapterWireAccounts);

            final List<Account> accounts = new ArrayList<>();
            if (AppData.getInstance().getAccounts() != null) {
                for (Account account : AppData.getInstance().getAccounts()) {
                    for (FlowService service : account.getServices()) {
                        if (service.getName().equalsIgnoreCase("wire")) {
                            accounts.add(account);
                            break;
                        }
                    }
                }
            }
            adapterWireAccounts.setData(accounts);

            if (accounts.size() > 0 && selectedAccount == null) {
                selectedAccount = accounts.get(0);
                adapterWireAccounts.setSelectedPosition(0);
            }
        }
    }

    private void updateWalletView() {
        if (isAccountSelected || type == TransactionType.DEPOSIT || type == TransactionType.WITHDRAW) {
            llAccount.setVisibility(View.VISIBLE);
            llWallet.setVisibility(View.GONE);
            if (selectedAccount == null) {
                llAccount.setVisibility(View.GONE);
                return;
            }
            llAccount.setVisibility(View.VISIBLE);

            final boolean isCreditCard = selectedAccount.getFlow().equals("stripes");

            if (isCreditCard) {
                ((TextView)llAccount.findViewById(R.id.txtName)).setText(CardUtils.getBrandName(selectedAccount.getField().getBrand()));
                imgCard.setImageResource(CardUtils.getBrandImage(selectedAccount.getField().getBrand()));
//                final Limit limit = ExchangeApplication.getApp().getUser().getLimits().getCard();
//                ((TextView)llAccount.findViewById(R.id.txtAccount)).setText(String.format("%s: $%s %s", getString(R.string.limit), Utils.formattedNumber(limit.getAmount()), selectedAccount.getAsset().getSymbol()));
            } else {
                ((TextView)llAccount.findViewById(R.id.txtName)).setText(selectedAccount.getField().getBankName());
                ((TextView)llAccount.findViewById(R.id.txtAccount)).setText(selectedAccount.getField().getAccountNumber());
            }
            int colorRes;
            String status;
            switch (selectedAccount.getStatus().toLowerCase()) {
                case "pending":
                    colorRes = R.color.colorPendingLight;
                    status = getResources().getString(R.string.pending);
                    break;
                case "failed":
                    colorRes = R.color.colorRed;
                    status = getResources().getString(R.string.failed);
                    if (isCreditCard) ((TextView)llAccount.findViewById(R.id.txtName)).setText(R.string.card_failed);
                    break;
                default:
                    colorRes = R.color.colorWhite;
                    if (isCreditCard) {
                        status = String.format("•••• %s (%s)", selectedAccount.getField().getLast4(), selectedAccount.getAsset().getSymbol());
                    } else {
                        status = selectedAccount.getAsset().getSymbol();
                    }
                    break;
            }
            ((ImageView)llAccount.findViewById(R.id.imgStatus)).setImageTintList(ColorStateList.valueOf(getResources().getColor(colorRes)));
            ((TextView)llAccount.findViewById(R.id.txtStatus)).setText(status);
        } else {
            llAccount.setVisibility(View.GONE);
            if (currencyWallet == null) {
                llWallet.setVisibility(View.GONE);
                return;
            }
            llWallet.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(currencyWallet.getSymbolData().getColoredImage())
                    .into(imgCoin);
            txtCoin.setText(String.format("%s %s", currencyWallet.getSymbol(), getString(R.string.title_wallet)));
            txtBalance.setText(currencyWallet.getBalanceCurrencyFormatted());

            if (type == TransactionType.BUY) {
//                final Limit limit = ExchangeApplication.getApp().getUser().getLimits().getWallet().get(currencyWallet.getSymbol());
//                if (limit != null) {
//                    txtLimit.setText(String.format("%s: %s", getString(R.string.limit), limit.getAmountFormatted()));
//                    txtLimit.setVisibility(View.VISIBLE);
//                }
            }
        }
    }

    private void showConfirmView() {
        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(((ImageView)llConfirm.findViewById(R.id.imgConfirmCoin)));
        if (type == TransactionType.BUY || type == TransactionType.SELL) {
            ((TextView)llConfirm.findViewById(R.id.txtRate)).setText(String.format("1 %s = %s %s",
                    type == TransactionType.BUY ? currencyWallet.getSymbol() : wallet.getSymbol(),
                    quote.getRateFormatted(),
                    type == TransactionType.BUY ? wallet.getSymbol() : currencyWallet.getSymbol()
            ));
            ((TextView)llConfirm.findViewById(R.id.txtConfirmCoin)).setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
            ((TextView)llConfirm.findViewById(R.id.txtConfirmPrice)).setText(String.format("%c%s", currencySymbol, Utils.formattedNumber(type == TransactionType.BUY ? quote.getToAmount() : quote.getFromAmount())));
            if (true) {
                ((TextView)llConfirm.findViewById(R.id.txtFee)).setText(quote.getFee().getAmountFormatted());
            } else {
                ((TextView)llConfirm.findViewById(R.id.txtFee)).setText(String.format("%s%c%s", type == TransactionType.BUY ? "" : "-", currencySymbol, Utils.formattedNumber(quote.getFee().getAmount()))); // not using getAmountFormatted() because its max fraction digits count is 2
            }
            txtTotal.setText(String.format("%c%s", currencySymbol, Utils.formattedNumber(type == TransactionType.BUY ? quote.getFromAmount() : quote.getToAmount())));
            if (type == TransactionType.BUY) {
                ((TextView)llConfirm.findViewById(R.id.txtPaymentMethodLabel)).setText(R.string.payment_method);
                final String paymentMethod;
                if (accountId > 0) {
                    if (selectedAccount.getFlow().equals("stripes")) {
                        paymentMethod = CardUtils.getBrandName(selectedAccount.getField().getBrand());
                    } else {
                        paymentMethod =  selectedAccount.getField().getBankName();
                    }
                } else {
                    paymentMethod = currencyWallet.getTitle();
                }
                ((TextView)llConfirm.findViewById(R.id.txtPaymentMethod)).setText(paymentMethod);
                ((TextView)llConfirm.findViewById(R.id.txtAvailable)).setText(quote.getAvailability());

            } else {
                ((TextView)llConfirm.findViewById(R.id.txtPaymentMethodLabel)).setText(R.string.deposit_to);
                ((TextView)llConfirm.findViewById(R.id.txtPaymentMethod)).setText(String.format("%s %s", currencyWallet.getTitle(), getString(R.string.title_wallet)));
                llConfirm.findViewById(R.id.llAvailable).setVisibility(View.GONE);
            }
        } else { // Deposit / Withdraw
            llConfirm.findViewById(R.id.llRate).setVisibility(View.GONE);
            ((TextView)llConfirm.findViewById(R.id.txtConfirmCoin)).setText(String.format("%c%s", currencySymbol, Utils.formattedNumber(amount)));
            ((TextView)llConfirm.findViewById(R.id.txtPaymentMethod)).setText(selectedAccount != null ? selectedAccount.getField().getBankName() : null);
            ((TextView)llConfirm.findViewById(R.id.txtAvailableLabel)).setText(R.string.funds_arrive);
            ((TextView)llConfirm.findViewById(R.id.txtAvailable)).setText("5 days*");
            final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
            if (symbol != null) {
                fee = symbol.getFee();
            } else {
                fee = wallet.getSymbolData().getFee();
            }
            ((TextView)llConfirm.findViewById(R.id.txtFee)).setText(String.format("%c%s", currencySymbol, Utils.formattedNumber(fee)));
            txtTotal.setText(String.format("%c%s", currencySymbol, Utils.formattedNumber(amount - fee)));
            llConfirm.findViewById(R.id.llConfirmPrice).setVisibility(View.GONE);
            if (type == TransactionType.DEPOSIT) {
                llConfirm.findViewById(R.id.llDepositTo).setVisibility(View.VISIBLE);
                ((TextView)llConfirm.findViewById(R.id.txtDepositTo)).setText(String.format("%s %s", wallet.getSymbol(), getString(R.string.title_wallet)));
                ((TextView)llConfirm.findViewById(R.id.txtPaymentMethodLabel)).setText(R.string.from);
            } else {
                ((TextView)llConfirm.findViewById(R.id.txtPaymentMethodLabel)).setText(R.string.withdraw_to);
            }
        }
        replaceView(llBuySell, llConfirm, false);
    }

    // for BUY
    private int getServiceId() {
        final FlowData flowData = AppData.getInstance().getFlowData();
        if (flowData == null) return -1;
        if (flowData.getMessage() != null) {
            new Handler().postDelayed(() -> showAlert(flowData.getMessage(), (dialog, which) -> dismiss()), 100);
            return -1;
        }
        for (FlowService service : flowData.getServices()) {
            if (!service.getName().equals("wire")) {
                return service.getId();
            }
        }
        return -1;
    }

    @Click(R.id.imgSwitch)
    void onSwitch() {
        isCoin = !isCoin;
        padView.setValue(isCoin ? amount : price);
    }

    @Click(R.id.txtPercent1)
    void onPercent1() {
        onPercentClicked(0);
    }

    @Click(R.id.txtPercent2)
    void onPercent2() {
        onPercentClicked(1);
    }

    @Click(R.id.txtPercent3)
    void onPercent3() {
        onPercentClicked(2);
    }

    private void onPercentClicked(int index) {
        final double balance;
        switch (type) {
            case BUY:
            case DEPOSIT:
                final double marketPrice;
                if (isCoin) {
                    final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
                    if (symbol != null && symbol.getMarketData() != null) {
                        marketPrice = symbol.getMarketData().getPrice();
                    } else {
                        marketPrice = wallet.getSymbolData().getPrice();
                    }
                } else {
                    marketPrice = 1;
                }
                if (type == TransactionType.DEPOSIT || isAccountSelected) {
                    if (selectedAccount == null) return; // when DEPOSIT from no bank
                    balance = selectedAccount.getField().getBalance() / marketPrice;
                } else {
                    if (currencyWallet == null) return; // TODO: when does this happen?
                    balance = currencyWallet.getBalance() / marketPrice;
                }
                break;
            default: // SELL or WITHDRAW
                balance = isCoin ? wallet.getBalance() : wallet.getBalanceCurrency();
                break;
        }
        padView.setValue(balance * percents[index]);
    }

    @Click(R.id.llAccount)
    void onAccountClicked() {
        replaceView(llBuySell, llSelectWallet, false);
    }

    @Click(R.id.llWallet)
    void onWalletClicked() {
        replaceView(llBuySell, llSelectWallet, false);
    }

    @Click({R.id.btnBackConfirm, R.id.btnBackSelect})
    void onBack() {
        replaceView(llSelectWallet.getVisibility() == View.VISIBLE ? llSelectWallet : llConfirm, llBuySell, true);
    }

    @Click({R.id.btnClose, R.id.btnCloseConfirm, R.id.btnCloseSelect})
    void onClose() {
        dismiss();
    }

    @Click(R.id.btnNext)
    void onNext() {
        if (price == 0) {
            showAlert(getString(R.string.error_empty_amount));
            return;
        }

        if (type == TransactionType.SELL || type == TransactionType.WITHDRAW) {
            if (price > wallet.getBalanceCurrency()) {
                showAlert(getString(R.string.error_larger_amount_transaction, type.name().toLowerCase()));
                return;
            }
            if (type == TransactionType.WITHDRAW && selectedAccount == null) {
                showAlert(getString(R.string.error_no_payment_method), (dialogInterface, i) -> onAddPayment());
                return;
            }
        } else {
            double balance = -1;
            accountId = 0;
            if (type == TransactionType.BUY) {
                if (isAccountSelected) {
                    balance = selectedAccount.getField().getBalance();
                    accountId = selectedAccount.getId();
                } else if (currencyWallet != null) {
                    balance = currencyWallet.getBalanceCurrency();
                }
            } else { // DEPOSIT
                if (selectedAccount != null) {
                    balance = selectedAccount.getField().getBalance();
                    accountId = selectedAccount.getId();
                }
            }
            if (balance < 0) {
                showAlert(getString(R.string.error_no_payment_method), (dialogInterface, i) -> onAddPayment());
                return;
            } else if ((!isAccountSelected || !selectedAccount.getFlow().equals("stripes")) && price > balance) {
                showAlert(getString(R.string.error_not_enough_funds));
                return;
            }
        }

        if (type == TransactionType.DEPOSIT || type == TransactionType.WITHDRAW) {
            showConfirmView();
        } else { // type: BUY / SELL
            showProgressBar();
            ApiClient.getInterface().getExchangeTickers().enqueue(new AppCallback<>(getContext(), this));
        }
    }

    private void onAddPayment() {
        if (AppData.getInstance().getFlowData() == null) return;
//        final Intent intent = new Intent(getContext(), AddAccountActivity_.class);
//        startActivity(intent);
//        onClose();
    }

    private void onQuote(HashMap<String, ExchangeData> data) {
        if (wallet == null) { // TODO: when does this happen?
            hideProgressBar();
            return;
        }
        final ExchangeData exchangeWallet = data.get(wallet.getSymbol());
        final ExchangeData exchangeCurrency = data.get(type == TransactionType.BUY && isAccountSelected ? WalletUtils.defaultCurrencySymbol.getSymbol() : currencyWallet.getSymbol());
        if (exchangeWallet == null || exchangeCurrency == null) {
            hideProgressBar();
            showAlert("No tickers data available");
            return;
        }

        final ExchangeQuoteRequest request = new ExchangeQuoteRequest();
        if (type == TransactionType.BUY) {
            request.setFromExchangeId(exchangeCurrency.getExchangeId());
            request.setToExchangeId(exchangeWallet.getExchangeId());
            if (accountId > 0) { // Bank Account selected
                request.setAccountId(accountId);
                final int serviceId = getServiceId();
                if (serviceId < 0) return;
                request.setServiceId(serviceId);
            } else { // TODO: confirm if accountId is needed when currency wallet is selected
//                request.setAccountId(AppData.getInstance().getFlowData().getAccount().getId());
            }
            if (isCoin) {
                request.setToQuantity(amount);
            } else {
                request.setFromQuantity(price);
            }
        } else {
            request.setFromExchangeId(exchangeWallet.getExchangeId());
            request.setToExchangeId(exchangeCurrency.getExchangeId());
            if (isCoin) {
                request.setFromQuantity(amount);
            } else {
                request.setToQuantity(price);
            }
        }
        ApiClient.getInterface().exchangeQuote(request).enqueue(new AppCallback<>(getContext(), this));
    }

    private void onConfirm() {
        showProgressBar();
        if (type == TransactionType.BUY || type == TransactionType.SELL) {
            ApiClient.getInterface().exchangeConfirm(quote.getId()).enqueue(new AppCallback<>(getContext(), this));
        } else {
            accountId = selectedAccount.getId();
            int serviceId = 0;
            for (FlowService service : selectedAccount.getServices()) {
                if (isAccountSelected) { // ach
                    if (service.getName().equalsIgnoreCase("ach")) {
                        serviceId = service.getId();
                        break;
                    }
                } else { // wire
                    if (service.getName().equalsIgnoreCase("wire")) {
                        serviceId = service.getId();
                        break;
                    }
                }
            }
            if (serviceId == 0) {
                showAlert(AppData.getInstance().getFlowData().getMessage());
                return;
            }

            final DisbursementRequest request = new DisbursementRequest();
            request.setAssetId(wallet.getSymbolData().getId());
            request.setAmount(amount);
            request.setAccountId(accountId);
            request.setServiceId(serviceId);

            if (type == TransactionType.DEPOSIT) {
                ApiClient.getInterface().disbursementDeposit(request).enqueue(new AppCallback<>(getContext(), this));
            } else { // withdraw
                ApiClient.getInterface().disbursementWithdraw(request).enqueue(new AppCallback<>(getContext(), this));
            }
        }
    }

    private void showSuccessScreen() {
//        final Intent intent = new Intent(getContext(), BuySellSuccessActivity_.class);
//        intent.putExtra("wallet", wallet);
//        intent.putExtra("type", type);
//        intent.putExtra("amount", type == TransactionType.BUY || type == TransactionType.SELL ? amount : amount - fee);
//        startActivity(intent);
//        dismissAllowingStateLoss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            stripe.onPaymentResult(requestCode, data, new ApiResultCallback<PaymentIntentResult>() {
                @Override
                public void onSuccess(PaymentIntentResult paymentIntentResult) {
                    hideProgressBar();
                    final PaymentIntent.Status status = paymentIntentResult.getIntent().getStatus();
                    if (status == StripeIntent.Status.Succeeded) {
                        showSuccessScreen();
                    } else if (status != null) {
                        if (status != StripeIntent.Status.Canceled && status != StripeIntent.Status.RequiresAction) {
                            dismissAllowingStateLoss();
                        }
                        Toast.makeText(getContext(), status.name(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(@NotNull Exception e) {
                    if (BuildConfig.DEBUG) e.printStackTrace();
                    hideProgressBar();
                    if (e.getLocalizedMessage().contains("already succeeded")) {
                        showSuccessScreen();
                    } else {
                        showAlert(e.getLocalizedMessage());
                    }
                }
            });
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;
        if (response instanceof GetExchangeTickersResponse) {
            final HashMap<String, ExchangeData> data = ((GetExchangeTickersResponse)response).getData();
            onQuote(data);
            return;
        } else if (response instanceof ExchangeQuoteResponse) {
            quote = ((ExchangeQuoteResponse)response).getData();
            showConfirmView();
        } else if (response instanceof ExchangeConfirmResponse) { // Transaction Success
            final String publishableKey = ((ExchangeConfirmResponse)response).getPublishableKey();
            if (publishableKey != null && selectedAccount != null && selectedAccount.getFlow().equals("stripes")) {
                if (stripe == null) {
                    stripe = new Stripe(getContext(), publishableKey);
                }
                stripe.confirmPayment(this, ConfirmPaymentIntentParams.create(((ExchangeConfirmResponse)response).getClientSecret()));
                return;
            } else {
                showSuccessScreen();
            }
        }
        hideProgressBar();
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();
    }
}
