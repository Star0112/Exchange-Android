package com.urgentrn.urncexchange.ui.fragments.card;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.CardTransactionsHistory;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.card.CardDetail;
import com.urgentrn.urncexchange.models.card.CardTransaction;
import com.urgentrn.urncexchange.models.card.token.CardToken;
import com.urgentrn.urncexchange.models.card.token.DigitalWalletToken;
import com.urgentrn.urncexchange.models.card.token.MarqetaCard;
import com.urgentrn.urncexchange.models.request.UpdateWalletRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetCardResponse;
import com.urgentrn.urncexchange.models.response.GetCardTransactionsResponse;
import com.urgentrn.urncexchange.models.response.UpdateWalletResponse;
import com.urgentrn.urncexchange.ui.adapter.CardTransactionAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.CardTransactionDialog;
import com.urgentrn.urncexchange.ui.dialogs.CardTransactionDialog_;
import com.urgentrn.urncexchange.ui.dialogs.LoadBalanceDialog;
import com.urgentrn.urncexchange.ui.dialogs.LoadBalanceDialog_;
import com.urgentrn.urncexchange.ui.dialogs.SelectWalletDialog;
import com.urgentrn.urncexchange.ui.dialogs.SelectWalletDialog_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.fragment_card)
public class CardFragment extends BaseFragment implements ApiCallback {

    @ViewById
    Toolbar toolBar;

    @ViewById
    TextView txtTapCardNumbers, txtCvv, txtCardNum, txtExp, txtName, txtFundingCoin, txtFundingBalance;

    // Daily Limit, Transactions
    @ViewById
    TextView txtDailyLimit, txtNoTransactions;

    // Load Balance
    @ViewById
    TextView txtBalance, txtDate, txtPending;

    @ViewById
    ImageView imgCard, imgGPay, imgFundingCoin;

    @ViewById
    View llCard, llDetail, llBalance, llBottomSheet;

    @ViewById
    ProgressBar progressCard;

    @ViewById
    UltimateRecyclerView recyclerView;

    private CardTransactionAdapter adapter;
    private CardTransactionDialog dialogTransaction = new CardTransactionDialog_();
    private final LoadBalanceDialog dialogLoad = new LoadBalanceDialog_();
    private final SelectWalletDialog dialog = new SelectWalletDialog_();

    private Card card, cardDetail;
    private List<CardTransaction> transactions = new ArrayList<>();
    private int page = 0, totalPages = 1;

    private MarqetaCard marqetaCard;
    private boolean isWalletInitialized, canBeProvisioned;

    @AfterViews
    protected void init() {
        if (getArguments() == null || getContext() == null) return;

        getView().setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Color.WHITE, Color.parseColor("#fff6f3")}));
        final MenuItem btnSetting = toolBar.getMenu()
                .add(R.string.card_settings)
                .setIcon(R.mipmap.ic_setting)
                .setOnMenuItemClickListener(item -> onSetting());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            btnSetting.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorSalmon)));
        } else {
            final Drawable drawable = getResources().getDrawable(R.mipmap.ic_setting);
            drawable.setTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorSalmon)));
            btnSetting.setIcon(drawable);
        }
        btnSetting.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        card = (Card)getArguments().getSerializable("card");
        if (card == null || getUser() == null) return;

        Glide.with(getContext()).load(card.getCardInfo().getFrontImage()).into(imgCard);
        txtName.setText(String.format("%s %s", getUser().getFirstName(), getUser().getLastName()));

        if (card.isPhysical()) {
            txtTapCardNumbers.setVisibility(View.GONE);
            txtCvv.setVisibility(View.INVISIBLE);
            txtCardNum.setText(String.format("•••• %s", card.getNumber().length() >= 4 ? card.getNumber().substring(card.getNumber().length() - 4) : card.getNumber()));
            txtExp.setVisibility(View.INVISIBLE);
        } else if (card.isVirtual()) {
            txtTapCardNumbers.setVisibility(View.VISIBLE);
            txtCvv.setVisibility(View.VISIBLE);
            txtExp.setVisibility(View.VISIBLE);
            updateDetailView(false);
        } else {
            // TODO: which case?
        }
        updateGPayView();
        updateWalletView(null);

        if (card.isLoadable()) {
            llCard.getLayoutParams().height = getResources().getDimensionPixelOffset(R.dimen.card_height_2x);
            txtCardNum.setVisibility(View.INVISIBLE);
            txtName.setVisibility(View.INVISIBLE);

            llDetail.setVisibility(View.GONE);
            llBalance.setVisibility(View.VISIBLE);
            showBalanceView();
        } else {
            txtCardNum.setVisibility(View.VISIBLE);
            txtName.setVisibility(View.VISIBLE);

            llDetail.setVisibility(View.VISIBLE);
            showDailyLimitView(false);
            initTransactionsView();
            llBalance.setVisibility(View.GONE);

            llBottomSheet.setVisibility(View.VISIBLE);
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
            bottomSheetBehavior.setPeekHeight(displayMetrics.heightPixels - (int)((card.isPhysical() ? 440 : 460) /* dpHeight of top view*/ * displayMetrics.density));
        }
        onRefresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(Card updatedCard) {
        if (updatedCard != null) card = updatedCard;
        if (card.isVirtual()) {
            updateDetailView(false);
        }
        if (card.isLoadable()) {
            showBalanceView();
        }
    }

    private void updateGPayView() {
        if (Constants.USE_GOOGLE_PAY && card.isVirtual()) {
            if (card.getCardInfo().isTokenizable()) {
                // To get network
                final HashMap<String, String> request = new HashMap<>();
                request.put("reference", card.getReference());
                ApiClient.getInterface().getNetwork(request).enqueue(new AppCallback<>(this));

                return;
            }
        }
        imgGPay.setVisibility(View.GONE);
    }

    private void showDailyLimitView(boolean isUnlocked) {
        String dailyLimitFormatted = null;
        int progress = 0;
        if (isUnlocked) {
            for (CardDetail detail : card.getCardInfo().getDetails()) {
                if (detail.getType().equals("dailyLimit")) {
                    final double dailyLimit = Double.valueOf(detail.getDetail());
                    final double availableLimit = cardDetail.getAvailableLimit();
                    dailyLimitFormatted= String.format("$%s/$%s", availableLimit, dailyLimit);
                    progress = ((int)(availableLimit / dailyLimit * 100));
                    break;
                }
            }
        } else {
            for (CardDetail detail : card.getCardInfo().getDetails()) {
                if (detail.getDetail().equals("Daily Limit")) {
                    dailyLimitFormatted = detail.getTitle();
                    break;
                }
            }
        }
        txtDailyLimit.setText(dailyLimitFormatted);
        progressCard.setProgress(progress);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateWalletView(List<Wallet> wallets) {
        for (Wallet wallet : AppData.getInstance().getWallets()) {
            if (wallet.isDefault()) {
                updateFundingSourceView(wallet);
                return;
            }
        }
    }

    private void updateFundingSourceView(Wallet wallet) {
        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgFundingCoin);
        txtFundingCoin.setText(wallet.getTitle());
        txtFundingBalance.setText(wallet.getBalanceCurrencyFormatted());
    }

    private void initTransactionsView() {
        adapter = new CardTransactionAdapter(position -> showTransactionDialog(adapter.getItem(position)));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setDefaultOnRefreshListener(() -> onRefresh());
        recyclerView.setLoadMoreView(R.layout.bottom_progressbar);
        recyclerView.setOnLoadMoreListener(((itemsCount, maxLastVisiblePosition) -> getTransactions(page)));
        recyclerView.setAdapter(adapter);
    }

    private void showBalanceView() {
        txtBalance.setText(String.format("$ %s", Utils.formattedNumber(card.getAvailableBalance())));
        txtDate.setText(card.getBalanceUpdatedAt());
        txtPending.setText(String.format("$ %s", Utils.formattedNumber(card.getPendingBalance())));
    }

    private void updateDetailView(boolean isUnlocked) {
        if (card.isLoadable()) return;
        showDailyLimitView(isUnlocked);
        txtCvv.setText(String.format("CVV: %s", isUnlocked && cardDetail.getCvv() != null && !cardDetail.getCvv().isEmpty() ?
                cardDetail.getCvv() : "•••"));
        if (isUnlocked) {
            txtCardNum.setText(cardDetail.getNumber() != null && !cardDetail.getNumber().isEmpty() ?
                    Utils.addSeparator(cardDetail.getNumber(), " ", 4) : "---- ---- ---- ----");
        } else {
            txtCardNum.setText(card.getNumber().replace("*", "•").replace(" ", "  "));
        }
        txtExp.setText(String.format("EXP: %s", Utils.addSeparator(isUnlocked && cardDetail.getExpiryDate() != null && !cardDetail.getExpiryDate().isEmpty() ?
                cardDetail.getExpiryDate() : "••••", "/", 2)));
    }

    @Click(R.id.imgCard)
    void onTapCard() {
        if (card.isPhysical()/* || cardDetail != null*/) return;

        if (getActivity() == null) return;
        ((BaseActivity)getActivity()).showPassDialog(Constants.SecurityType.CARD, isSuccess -> {
            if (isSuccess) {
                final HashMap<String, String> request = new HashMap<>();
                request.put("reference", card.getReference());
                ApiClient.getInterface().getCardInfo(request).enqueue(new AppCallback<>(this));
            }
        });
    }

    private void showTransactionDialog(CardTransaction transaction) {
        if (transaction == null) return;
        if (dialogTransaction.getDialog() != null && dialogTransaction.getDialog().isShowing()) return;
        final Bundle args = new Bundle();
        args.putSerializable("transaction", transaction);
        dialogTransaction.setArguments(args);
        dialogTransaction.show(getChildFragmentManager(), "CARD");
    }

    private boolean onSetting() {
        final Fragment fragment = new CardSettingFragment_();
        final Bundle args = new Bundle();
        args.putSerializable("card", card);
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
        return true;
    }

    @Click(R.id.llCardDetail)
    void onCardDetail() {
        final Fragment fragment = new CardDetailFragment_();
        final Bundle args = new Bundle();
        args.putSerializable("card", card);
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }

    @Click(R.id.btnLoad)
    void onLoadBalance() {
        if (card.getId() < 0) return;
        if (dialogLoad.getDialog() != null && dialogLoad.getDialog().isShowing()) return;
        final Bundle args = new Bundle();
        args.putSerializable("card_id", card.getId());
        dialogLoad.setArguments(args);
        dialogLoad.show(getChildFragmentManager(), WalletUtils.TransactionType.LOAD.name());
    }

    @Click(R.id.imgGPay)
    void onGooglePay() {
        if (canBeProvisioned) {
            if (!isWalletInitialized) return;
            if (cardDetail == null || cardDetail.getPlatformData() == null) {
                return;
            }
        } else {
            openGPayApp("com.google.android.apps.walletnfcrel");
        }
    }

    @Click(R.id.llFunding)
    void onFundingSource() {
        if (dialog.getDialog() != null && dialog.getDialog().isShowing()) return;
        final Bundle args = new Bundle();
        args.putString("title", getString(R.string.funding_source));
        args.putInt("base_position", -1);
        args.putInt("other_position", -1);
        final List<Wallet> wallets = new ArrayList<>();
        for (Wallet wallet : AppData.getInstance().getWallets()) {
            if (wallet.enableCardSpending()) {
                wallets.add(wallet);
            }
        }
        args.putSerializable("wallets", (Serializable)wallets);
        dialog.setArguments(args);
        dialog.setOnDialogDismissListener(isSuccess -> {
            final int position = dialog.getSelectedPosition();
            final Wallet wallet = wallets.get(position);
            if (!wallet.isDefault()) {
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
                                        updateFundingSourceView(wallet);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        }));
            }
        });
        dialog.show(getChildFragmentManager(), WalletUtils.TransactionType.EXCHANGE.name());
    }

    public void openGPayApp(String packageName) {
        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void onRefresh() {
        txtNoTransactions.setVisibility(View.GONE);
        getTransactions(0);
    }

    private void getTransactions(int page) { // Get Card Transactions
        if (!TextUtils.isEmpty(card.getReference())) {
            if (page < totalPages) {
                ApiClient.getInterface()
                        .getCardTransactions(card.getReference(), page + 1, Constants.DEFAULT_PAGE_LIMIT)
                        .enqueue(new AppCallback<>(this));
            }
        } else {
            recyclerView.setRefreshing(false);
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;
        if (response instanceof GetCardTransactionsResponse) {
            final CardTransactionsHistory data = ((GetCardTransactionsResponse)response).getData();
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

            if (page == 1) transactions.clear();
            transactions.addAll(data.getTransactions());
            if (adapter != null) adapter.setData(transactions);
            AppData.getInstance().setCardTransactions(transactions);
        } else if (response instanceof GetCardResponse) {
            cardDetail = ((GetCardResponse)response).getData();
            if (cardDetail.getPlatformData() != null) { // from getNetwork api
                marqetaCard.setNetwork(cardDetail.getPlatformData().getNetwork());
                imgGPay.setImageResource(R.mipmap.google_pay);
                if (card.getTokens() != null) {
                    final List<DigitalWalletToken> digitalWalletTokens = new ArrayList<>();
                    for (CardToken token : this.card.getTokens()) {
                        if (token.getType().equals("androidPay")) {
                            final DigitalWalletToken digitalWalletToken = new DigitalWalletToken();
                            digitalWalletToken.setTokenReferenceId(token.getToken());
                            digitalWalletTokens.add(digitalWalletToken);
                        }
                    }
                    marqetaCard.setDigitalWalletTokens(digitalWalletTokens);
                } else {
                    canBeProvisioned = true;
                    imgGPay.setVisibility(View.VISIBLE);
                }
            } else {
                updateDetailView(true); // from getCardInfo api
            }
        }
    }

    @Override
    public void onFailure(String message) {
        if (!isAdded()) return;
        if (message.contains("no transactions")) {
            recyclerView.setRefreshing(false);
            txtNoTransactions.setVisibility(View.VISIBLE);
        }
    }
}
