package com.urgentrn.urncexchange.ui.dialogs;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn769.Numpad;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.request.LoadCardRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.adapter.SelectWalletAdapter;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
//import com.urgentrn.urncexchange.ui.transactions.BuySellSuccessActivity_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_load_balance)
public class LoadBalanceDialog extends BaseDialog implements ApiCallback {

    @ViewById
    View llBuySell, llWallet, llAccount, imgSwitch;

    @ViewById
    TextView txtTitle, txtPrice, txtAmount, txtCoin, txtBalance, btnNext;

    @ViewById
    ImageView imgCoin;

    @ViewById
    Numpad padView;

    // Confirm View
    @ViewById
    View llConfirm;

    // Select Wallet View
    @ViewById
    View llSelectWallet;

    @ViewById
    RecyclerView recyclerView;

    private double price/*USD*/, amount/*Coin*/, priceFee, amountFee;
    private double[] percents = {0.25, 0.5, 1};
    private boolean isCoin = false;
    private Wallet wallet;

    @AfterViews
    protected void init() {
        price = amount = 0;
        isCoin = false;
        final int bestWalletIndex = WalletUtils.getBestWalletIndex();
        wallet = AppData.getInstance().getWallets().get(bestWalletIndex);

        txtTitle.setText(R.string.load_balance);

        if (isCoin) {
            txtAmount.setText(String.format("$%s", Utils.formattedNumber(price)));
            txtPrice.setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
        } else {
            txtPrice.setText(String.format("$%s", Utils.formattedNumber(price)));
            txtAmount.setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
        }
        txtPrice.setTextColor(getResources().getColor(R.color.textColorPrimary));
        txtAmount.setTextColor(getResources().getColor(R.color.textColorDefault));

        
        padView.setDecimal(true);
        padView.setOnTextChangeListener((text, digits_remaining) -> {
            if (isCoin) {
                amount = padView.getValue();
                price = amount * WalletUtils.getSymbolData(wallet.getSymbol()).getMarketData().getPrice();
                txtAmount.setText(String.format("$%s", Utils.formattedNumber(price)));
                txtPrice.setText(String.format("%s %s", text, wallet.getSymbol()));
            } else {
                price = padView.getValue();
                amount = price / WalletUtils.getSymbolData(wallet.getSymbol()).getMarketData().getPrice();
                txtPrice.setText(String.format("$%s", text));
                txtAmount.setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
            }
        });

        // Select Wallet View
        initSelectWalletView(bestWalletIndex);

        updateWalletView();

        // Confirm View
        llConfirm.setVisibility(View.GONE);

        initPassView(Constants.SecurityType.CARD, null, isSuccess -> {
            if (isSuccess) onConfirm();
        });
    }

    private void initSelectWalletView(int selectedWalletPosition) {
        llSelectWallet.setVisibility(View.GONE);
        final SelectWalletAdapter adapter = new SelectWalletAdapter(position -> {
            wallet = AppData.getInstance().getWallets().get(position);
            updateWalletView();
            onBack();
        }, AppData.getInstance().getWallets(), true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        recyclerView.setAdapter(adapter);

        adapter.setSelectedPosition(selectedWalletPosition);
    }

    private void updateWalletView() {
        llAccount.setVisibility(View.GONE);
        llWallet.setVisibility(View.VISIBLE);
        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgCoin);
        txtCoin.setText(String.format("%s %s", wallet.getSymbol(), getString(R.string.title_wallet)));
        txtBalance.setText(wallet.getBalanceCurrencyFormatted());

        txtAmount.setVisibility(wallet.getSymbolData().isCurrency() ? View.GONE : View.VISIBLE);
        imgSwitch.setVisibility(wallet.getSymbolData().isCurrency() ? View.GONE : View.VISIBLE);

        padView.setValue(isCoin ? amount : price);
    }

    private void showConfirmView() {
        final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
        final double fee;
        if (symbol != null) {
            fee = symbol.getFee();
        } else {
            fee = wallet.getSymbolData().getFee();
        }
        priceFee = price * fee / 100;
        amountFee = amount * fee / 100;
        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(((ImageView)llConfirm.findViewById(R.id.imgConfirmCoin)));
        ((TextView)llConfirm.findViewById(R.id.txtConfirmCoin)).setText(String.format("%s %s", Utils.formattedNumber(amount - amountFee), wallet.getSymbol()));
        ((TextView)llConfirm.findViewById(R.id.txtFee)).setText(String.format("$%s", Utils.formattedNumber(priceFee)));
        ((TextView)llConfirm.findViewById(R.id.txtTotal)).setText(String.format("$%s", Utils.formattedNumber(price - priceFee)));

        replaceView(llBuySell, llConfirm, false);
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
        final double balance = isCoin ? wallet.getBalance() : wallet.getBalanceCurrency();
        padView.setValue(balance * percents[index]);
    }

    @Click(R.id.llWallet)
    void onWalletClicked() {
        replaceView(llBuySell, llSelectWallet, false);
    }

    @Click({R.id.btnBackSelect, R.id.btnBackConfirm})
    void onBack() {
        replaceView(llSelectWallet.getVisibility() == View.VISIBLE ? llSelectWallet : llConfirm, llBuySell, true);
    }

    @Click({R.id.btnClose, R.id.btnCloseSelect, R.id.btnCloseConfirm})
    void onClose() {
        dismiss();
    }

    @Click(R.id.btnNext)
    void onNext() {
        if (price == 0) {
            showAlert(getString(R.string.error_empty_amount));
            return;
        }

        if (price > wallet.getBalanceCurrency()) {
            showAlert(getString(R.string.error_larger_amount_transaction, getString(R.string.load_balance).toLowerCase()));
            return;
        }

        showConfirmView();
    }

    private void onConfirm() {
        showProgressBar();

        final int cardId = getArguments().getInt("card_id");
        final int assetId = wallet.getSymbolData().getId();
        ApiClient.getInterface()
                .loadCard(new LoadCardRequest(cardId, assetId, price - priceFee))
                .enqueue(new AppCallback<>(getContext(), this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;
        hideProgressBar();
//        if (true) {
//            Intent intent = new Intent(getContext(), BuySellSuccessActivity_.class);
//            intent.putExtra("type", WalletUtils.TransactionType.LOAD);
//            intent.putExtra("amount", amount - amountFee);
//            startActivity(intent);
//            dismiss();
//        }
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();
    }
}
