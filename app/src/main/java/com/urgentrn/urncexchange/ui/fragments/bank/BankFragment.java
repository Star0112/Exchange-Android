package com.urgentrn.urncexchange.ui.fragments.bank;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.CardSetting;
import com.urgentrn.urncexchange.models.DepositAddress;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetDepositAddressResponse;
import com.urgentrn.urncexchange.ui.MainActivity;
import com.urgentrn.urncexchange.ui.adapter.CardSettingAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.BuySellDialog;
import com.urgentrn.urncexchange.ui.dialogs.BuySellDialog_;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_bank)
public class BankFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtBalance, txtCoin, txtDisclosure;

    @ViewById
    ImageView imgCoin;

    @ViewById
    RecyclerView recyclerView;

    private CardSettingAdapter adapter;
    private final BuySellDialog dialog = new BuySellDialog_();

    private Wallet wallet;

    @AfterViews
    protected void init() {
        getView().setBackgroundResource(R.color.colorYellow);

        adapter = new CardSettingAdapter(position -> onItemClick(position));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        final List<CardSetting> data = new ArrayList<>();
        data.add(new CardSetting(R.mipmap.ic_bank_account, R.string.view_account));
        if (false) { // TODO: from api
            data.add(new CardSetting(R.mipmap.ic_bank_bill, R.string.bill_pay));
        }
        data.add(new CardSetting(R.mipmap.ic_bank_deposit, R.string.direct_deposit));
        if (false) { // TODO: from api
            data.add(new CardSetting(R.mipmap.ic_bank_card, R.string.exchange_cash));
        }
        adapter.setData(data);

        updateView(null);

        if (AppData.getInstance().getDepositAddress() == null) {
            ApiClient.getInterface().getDepositAddress(wallet.getSymbol()).enqueue(new AppCallback<>(getContext(), this));
        } else {
            updateBankView();
        }
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
    public void updateView(List<Wallet> data) {
        if (getContext() == null || !isAdded()) return;
        for (Wallet wallet : AppData.getInstance().getWallets()) {
            if (wallet.getSymbol().equals("USD")) {
                this.wallet = wallet;
                txtBalance.setText(wallet.getBalanceCurrencyFormatted());
                Glide.with(getContext()).load(wallet.getSymbolData().getColoredImage()).into(imgCoin);
                txtCoin.setText(String.format("%s %s", wallet.getTitle(), getString(R.string.title_wallet)));
                break;
            }
        }
    }

    private void updateBankView() {
        final DepositAddress address = AppData.getInstance().getDepositAddress();
        if (address == null) return;
        txtDisclosure.setText(address.getStrings().get("bankDisclaimer"));
    }

    @Click(R.id.btnWithdraw)
    void onWithdraw() {
        onDepositWithdraw(WalletUtils.TransactionType.WITHDRAW.ordinal());
    }

    @Click(R.id.btnDeposit)
    void onDeposit() {
        onDepositWithdraw(WalletUtils.TransactionType.DEPOSIT.ordinal());
    }

    private void onDepositWithdraw(int position) {
        if (wallet == null) return;
        if (WalletUtils.getAssetRestriction(wallet.getSymbolData().getType(), true) == WalletUtils.AssetRestriction.UPGRADE) {
            showVerifyDialog(null);
        } else {
            if (dialog.getDialog() != null && dialog.getDialog().isShowing()) return;
            final Bundle args = new Bundle();
            args.putSerializable("wallet", wallet);
            args.putInt("type", position);
            dialog.setArguments(args);
            dialog.show(getChildFragmentManager(), WalletUtils.TransactionType.values()[position].name());
        }
    }

    @Click(R.id.llViewWallet)
    void onViewWallet() {
        if (wallet == null || getActivity() == null) return;
        ((MainActivity)getActivity()).setActiveTab(R.id.navigation_wallet, wallet);
    }

    private void onItemClick(int position) {
        final int titleId = adapter.getItem(position).getTitleRes();
        switch (titleId) {
            case R.string.view_account:
                onViewAccount();
                break;
            case R.string.bill_pay:
                onBillPay();
                break;
            case R.string.direct_deposit:
                onDirectDeposit();
                break;
            case R.string.exchange_cash:
                onVisaCard();
                break;
            default:
                showAlert("Coming Soon!");
                break;
        }
    }

    private void onViewAccount() {
        if (AppData.getInstance().getDepositAddress() == null) return;
        ((BaseFragment)getParentFragment()).replaceFragment(new AccountFragment_(), false);
    }

    private void onBillPay() {

    }

    private void onDirectDeposit() {
        if (AppData.getInstance().getDepositAddress() == null) return;
        ((BaseFragment)getParentFragment()).replaceFragment(new DirectDepositFragment_(), false);
    }

    private void onVisaCard() {

    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;
        if (response instanceof GetDepositAddressResponse) {
            final DepositAddress depositAddress = ((GetDepositAddressResponse)response).getData();
            AppData.getInstance().setDepositAddress(depositAddress);
            updateBankView();
        }
    }

    @Override
    public void onFailure(String message) {
        if (!isAdded()) return;
        showToast(message, false);
    }
}
