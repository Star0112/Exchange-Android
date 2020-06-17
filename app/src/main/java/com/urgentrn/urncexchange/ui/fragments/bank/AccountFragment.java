package com.urgentrn.urncexchange.ui.fragments.bank;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.DepositAddress;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.bank.FieldOption;
import com.urgentrn.urncexchange.ui.MainActivity;
import com.urgentrn.urncexchange.ui.adapter.BankAccountAdapter;
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

@EFragment(R.layout.fragment_account)
public class AccountFragment extends BaseFragment {

    @ViewById
    TextView txtBalance, txtCoin;

    @ViewById
    ImageView imgCoin;

    @ViewById
    RecyclerView recyclerView;

    private BankAccountAdapter adapter;
    private final BuySellDialog dialog = new BuySellDialog_();

    private Wallet wallet;

    @AfterViews
    protected void init() {
        getView().setBackgroundResource(R.color.colorYellow);

        adapter = new BankAccountAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        updateView(null);
        updateBankView();
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
        final List<FieldOption> data = new ArrayList<>();
        data.add(new FieldOption(address.getStrings().get("routingNumber"), address.getData().get("routingNumber")));
        data.add(new FieldOption(address.getStrings().get("accountNumber"), address.getData().get("accountNumber")));
        data.add(new FieldOption(getString(R.string.bank_information), address.getStrings().get("bankInformation")));
        adapter.setData(data);
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
        ((MainActivity)getActivity()).setActiveTab(R.id.navigation_dash, wallet);
    }
}
