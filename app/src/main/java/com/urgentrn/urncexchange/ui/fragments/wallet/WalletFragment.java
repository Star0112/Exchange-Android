package com.urgentrn.urncexchange.ui.fragments.wallet;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.urgentrn.urncexchange.BuildConfig;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.Transaction;
import com.urgentrn.urncexchange.models.TransactionsHistory;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.request.UpdateWalletRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetAccountsResponse;
import com.urgentrn.urncexchange.models.response.TransactionsResponse;
import com.urgentrn.urncexchange.models.response.UpdateWalletResponse;
import com.urgentrn.urncexchange.ui.adapter.TransactionAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.BuySellDialog;
import com.urgentrn.urncexchange.ui.dialogs.BuySellDialog_;
import com.urgentrn.urncexchange.ui.dialogs.DepositCurrencyDialog;
import com.urgentrn.urncexchange.ui.dialogs.DepositCurrencyDialog_;
import com.urgentrn.urncexchange.ui.dialogs.DepositDialog;
import com.urgentrn.urncexchange.ui.dialogs.DepositDialog_;
import com.urgentrn.urncexchange.ui.dialogs.TransactionDialog;
import com.urgentrn.urncexchange.ui.dialogs.TransactionDialog_;
import com.urgentrn.urncexchange.ui.transactions.SendInputActivity_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_wallet_content)
public class WalletFragment extends BaseFragment implements ApiCallback {

    @ViewById
    Toolbar toolBar;

    @ViewById
    View llGradient, btnDeposit, txtNoTransactions;

    // top card view

    @ViewById
    ImageView imgCoin;

    @ViewById
    Switch switchCard;

    @ViewById
    TextView txtCoin, txtBalanceUSD, txtBalance, btnBuy, btnSell;

    // transactions history

    @ViewById
    UltimateRecyclerView recyclerView;

    private TransactionAdapter adapterTransaction;
    private final DepositDialog depositDialog = new DepositDialog_();
    private final DepositCurrencyDialog depositCurrencyDialog = new DepositCurrencyDialog_();
    private final BuySellDialog dialog = new BuySellDialog_();
    private final TransactionDialog dialogTransaction = new TransactionDialog_();

    private int selectedPosition = 0;
    private Wallet wallet;
    private int page = 0, totalPages = 1;
    private Transaction transactionLimit;

    @AfterViews
    protected void init() {
        if (getArguments() == null || getContext() == null) return;
        selectedPosition = getArguments().getInt("position");

        setBackgroundColor(getResources().getColor(R.color.colorListBackground));
        setToolBar(false);

        adapterTransaction = new TransactionAdapter(position -> showTransactionDialog(adapterTransaction.getItem(position)));
        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapterTransaction));

        if (Constants.USE_NESTED_LIST_HEADER) {
            final TextView txtHeader = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_normal_header_transaction, null);
            txtHeader.setText(R.string.transaction_history);
            recyclerView.setNormalHeader(txtHeader);
        }

        recyclerView.setDefaultOnRefreshListener(() -> onRefresh());
        recyclerView.setLoadMoreView(R.layout.bottom_progressbar);
        recyclerView.setOnLoadMoreListener(((itemsCount, maxLastVisiblePosition) -> getTransactions(page)));
        recyclerView.setAdapter(adapterTransaction);

        updateView(null); // this should be first to set selected wallet

        if (wallet == null) {
            onBackPressed();
            return;
        }

        // moving to bottom here because of wallet color
        final int color = Utils.getMoreTransparentColor(wallet.getColor());
        setStatusBarColor(color);
        llGradient.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {color, getResources().getColor(R.color.colorListBackground)}));
        btnBuy.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(wallet.getColor())));
        btnSell.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(wallet.getColor())));

//        updateDepositMenu(null);
        if (wallet.getSymbolData().isCurrency()) {
            btnBuy.setText(R.string.deposit);
            btnSell.setText(R.string.withdraw);
        } else {
            btnBuy.setText(R.string.buy);
            btnSell.setText(R.string.sell);
        }

        recyclerView.setRefreshing(true);
        if (!Constants.USE_NESTED_LIST_HEADER) {
            onRefresh();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    public void updateView() {
        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgCoin);
        txtCoin.setText(wallet.getTitle());
        txtBalanceUSD.setText(wallet.getBalanceCurrencyFormatted());
        txtBalance.setText(String.format("%s %s", Utils.formattedNumber(wallet.getBalance(), 0, 5), wallet.getSymbol()));
        switchCard.setChecked(wallet.isDefault());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<Wallet> data) {
        if (getContext() == null || !isAdded()) return;
        if (AppData.getInstance().getWallets().size() <= selectedPosition) { // TODO: when does this happen?
            return;
        }
        wallet = AppData.getInstance().getWallets().get(selectedPosition);

        updateView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDepositMenu(GetAccountsResponse data) {
        btnDeposit.setVisibility(!wallet.getSymbolData().isCurrency() || (AppData.getInstance().getAccounts() != null && AppData.getInstance().getAccounts().size() > 0) ? View.VISIBLE : View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetActiveCard(Card card) {
        dialogTransaction.setActiveCard(card);
        if (wallet.isExchangeWallet()) {
            // TODO
        }
    }

    public void onRefresh() {
        txtNoTransactions.setVisibility(View.GONE);
        getTransactions(0);
    }

    private void getTransactions(int page) {
        if (page < totalPages) {
            ApiClient.getInterface()
                    .getTransactions(wallet.getSymbol(), page + 1, Constants.DEFAULT_PAGE_LIMIT)
                    .enqueue(new AppCallback<>(this));
        }
    }

    @Click(R.id.btnDeposit)
    void onDeposit() {
        if (wallet == null) return;
        if (WalletUtils.getAssetRestriction(wallet.getSymbolData().getType(), true) == WalletUtils.AssetRestriction.UPGRADE) {
            showVerifyDialog(null);
        } else {
            final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
            if (symbol != null) {
                if (!symbol.isReceive()) {
                    showAlert(getString(R.string.error_maintenance, wallet.getTitle()));
                    return;
                }
                final Bundle args = new Bundle();
                args.putSerializable("wallet", wallet);
                if (!symbol.isCurrency()) {
                    if (depositDialog.getDialog() != null && depositDialog.getDialog().isShowing()) return;
                    depositDialog.setArguments(args);
                    depositDialog.show(getChildFragmentManager(), WalletUtils.TransactionType.DEPOSIT.name());
                } else {
                    if (depositCurrencyDialog.getDialog() != null && depositCurrencyDialog.getDialog().isShowing()) return;
                    depositCurrencyDialog.setArguments(args);
                    depositCurrencyDialog.show(getChildFragmentManager(), WalletUtils.TransactionType.DEPOSIT.name());
                }
            }
        }
    }

    @Click(R.id.btnSend)
    void onSend() {
        if (wallet == null) return;
        if (WalletUtils.getAssetRestriction(wallet.getSymbolData().getType(), false) == WalletUtils.AssetRestriction.UPGRADE) {
            showVerifyDialog(null);
        } else {
            final List<String> options = new ArrayList<>();
            options.add(getString(R.string.send_option_username));
            options.add(getString(R.string.send_option_email));
            options.add(getString(R.string.send_option_mobile));
            if (!wallet.getSymbolData().isCurrency()) {
                final double fee;
                final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
                if (symbol != null) {
                    if (!symbol.isSend()) {
                        showAlert(getString(R.string.error_maintenance, wallet.getTitle()));
                        return;
                    }
                    fee = symbol.getFee();
                } else {
                    return;
                }
                options.add(getString(R.string.send_option_address, Utils.formattedNumber(fee), wallet.getSymbol()));
            }
            final String[] items = new String[options.size()];
            options.toArray(items);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.send_to, wallet.getSymbol()))
                    .setItems(items, (dialog, which) -> {
                        final Intent intent = new Intent(getContext(), SendInputActivity_.class);
                        intent.putExtra("wallet", wallet);
                        intent.putExtra("type", Constants.SendType.values()[which]);
                        startActivity(intent);
                    })
                    .show();
        }
    }

    @Click(R.id.btnBuy)
    void onBuy() {
        onBuySell(wallet.getSymbolData().isCurrency() ? WalletUtils.TransactionType.DEPOSIT.ordinal() : WalletUtils.TransactionType.BUY.ordinal());
    }

    @Click(R.id.btnSell)
    void onSell() {
        onBuySell(wallet.getSymbolData().isCurrency() ? WalletUtils.TransactionType.WITHDRAW.ordinal() : WalletUtils.TransactionType.SELL.ordinal());
    }

    @CheckedChange(R.id.switchCard)
    void onSwitchCard(boolean isChecked) {
        if (isChecked == wallet.isDefault()) return;
        if (!isChecked) {
            switchCard.setChecked(true);
            showAlert(getString(R.string.error_no_default_wallet));
            return;
        }
        ApiClient.getInterface()
                .updateDefaultWallet(wallet.getSymbol(), new UpdateWalletRequest(true))
                .enqueue(new AppCallback<>(getContext(), new ApiCallback() {
                    @Override
                    public void onResponse(BaseResponse response) {
                        if (response instanceof UpdateWalletResponse) {
                            if (((UpdateWalletResponse)response).isDefault()) {
                                for (Wallet wallet1 : AppData.getInstance().getWallets()) {
                                    wallet1.setDefault(wallet1.getSymbol().equals(wallet.getSymbol()));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        switchCard.setChecked(false);
                    }
                }));
    }

    private void showTransactionDialog(Transaction transaction) {
        if (transaction == null) return;
        if (dialogTransaction.getDialog() != null && dialogTransaction.getDialog().isShowing() || dialogTransaction.isAdded()) return;
        final Bundle args = new Bundle();
        args.putSerializable("wallet", wallet);
        args.putSerializable("transaction", transaction);
        dialogTransaction.setArguments(args);
        dialogTransaction.show(getChildFragmentManager(), "HISTORY");
    }

    private void onBuySell(int position) {
        if (WalletUtils.getAssetRestriction(wallet.getSymbolData().getType(), true) == WalletUtils.AssetRestriction.UPGRADE) {
            showVerifyDialog(null);
        } else {
            if (position == WalletUtils.TransactionType.BUY.ordinal() && AppData.getInstance().getFlowData() == null) return;
            if (position != WalletUtils.TransactionType.SELL.ordinal() && AppData.getInstance().getAccounts() == null) return;
            if (dialog.getDialog() != null && dialog.getDialog().isShowing()) return;
            final Bundle args = new Bundle();
            args.putSerializable("wallet", wallet);
            args.putInt("type", position);
            dialog.setArguments(args);
            dialog.show(getChildFragmentManager(), WalletUtils.TransactionType.values()[position].name());
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;
        if (response instanceof TransactionsResponse) {
            final TransactionsHistory data = ((TransactionsResponse)response).getData();
            page = data.getPage();
            totalPages = data.getTotalPages();
            if (page == totalPages) {
                if (recyclerView.isLoadMoreEnabled()) {
                    recyclerView.disableLoadmore();
                }
            } else if (page == 1) {
                recyclerView.reenableLoadmore();
            }
            recyclerView.setRefreshing(false);
            txtNoTransactions.setVisibility(data.getTotal() == 0 ? View.VISIBLE : View.GONE);

            adapterTransaction.setData(wallet, data.getTransactions(), data.getPage());

            if (false && !BuildConfig.DEBUG && transactionLimit == null && wallet.isExchangeWallet()) {
                for (Transaction transaction : data.getTransactions()) {
                    if (transaction.getType().equals("reserveEntry")) {
                        transactionLimit = transaction;
                        ExchangeApplication.getApp().getPreferences().setTransactionLimit(transaction);
                        return;
                    }
                }
                getTransactions(page);
            }

        }
    }

    @Override
    public void onFailure(String message) {
        if (isAdded() && recyclerView.mExchangeRefreshLayout.isRefreshing()) {
            recyclerView.setRefreshing(false);
        }
    }
}
