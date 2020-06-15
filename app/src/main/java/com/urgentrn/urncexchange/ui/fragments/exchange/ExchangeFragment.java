package com.urgentrn.urncexchange.ui.fragments.exchange;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.ExchangeQuote;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.request.ExchangeQuoteRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.ExchangeQuoteResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.ExchangeConfirmDialog;
import com.urgentrn.urncexchange.ui.dialogs.ExchangeConfirmDialog_;
import com.urgentrn.urncexchange.ui.dialogs.SelectWalletDialog;
import com.urgentrn.urncexchange.ui.dialogs.SelectWalletDialog_;
import com.urgentrn.urncexchange.ui.transactions.ExchangeInputActivity_;
import com.urgentrn.urncexchange.ui.transactions.ExchangeSuccessActivity_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.fragment_exchange)
public class ExchangeFragment extends BaseFragment implements ApiCallback {

//    @ViewById
//    View llBackground, btnConfirm, fabBorder;
//
//    @ViewById
//    ImageView imgCoinFrom, imgCoinTo;
//
//    @ViewById
//    TextView txtCoinFrom1, txtCoinFrom2, txtBalance, txtPercent1, txtPercent2, txtPercent3, txtAmountFrom, txtAmountTo, txtCoinTo2, txtCoinTo1, txtAmount, txtExchange, txtExchangeSymbol, txtReceive, txtReceiveSymbol, txtTerms;
//
//    private final SelectWalletDialog dialog = new SelectWalletDialog_();
//    private final ExchangeConfirmDialog confirmDialog = new ExchangeConfirmDialog_();
//
//    private List<Wallet> availableWallets = new ArrayList<>();
//    private int fromPosition = -1, toPosition = -1;
//    private Wallet fromWallet, toWallet;
//    private ExchangeData fromExchange, toExchange;
//    private double fromAmount, toAmount;
//    private boolean isFromAssetPegged;
//    private double[] percents = {0.25, 0.5, 1};

    @AfterViews
    protected void init() {
        initView();
        updateView();

        final String termText = getString(R.string.exchange_accept_terms)
                .replace(getString(R.string.exchange_terms_conditions), "<a href=\"" + Utils.getTermLink("EXCHANGE_TERMS_AND_CONDITIONS") + "\">" + getString(R.string.exchange_terms_conditions) + "</a>");
//        txtTerms.setText(Html.fromHtml(termText));
//        txtTerms.setMovementMethod(LinkMovementMethod.getInstance());
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

    private void initView() {
//        txtCoinFrom1.setText(null);
//        txtCoinFrom2.setText(null);
//        txtBalance.setText(null);
//        txtAmountFrom.setText(null);
//        txtAmountTo.setText(null);
//        txtCoinTo2.setText(null);
//        txtCoinTo1.setText(null);
//        txtAmount.setText(null);
//        txtExchangeSymbol.setText(null);
//        txtReceiveSymbol.setText(null);
    }

    @Override
    public void updateView() {
        if (getUser() == null || AppData.getInstance().getWallets().size() == 0 || AppData.getInstance().getExchangeTickers().size() == 0)
            return;

//        String baseType = "blockChain";
//        if (fromPosition >= 0) {
//            baseType = availableWallets.get(fromPosition).getSymbolData().getType();
//        }
//
//        availableWallets.clear();
//        for (Wallet wallet : AppData.getInstance().getWallets()) {
//            if (AppData.getInstance().getExchangeTickers(baseType).get(wallet.getSymbol()) != null) {
//                availableWallets.add(wallet);
//                if (fromPosition < 0) {
//                    fromPosition = availableWallets.size() - 1;
//                }
//            }
//        }
//
//        onFromCoinUpdated(fromPosition);
//        onToCoinUpdated(toPosition >= 0 ? toPosition : fromPosition == 0 ? 1 : 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<Wallet> data) {
        updateView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTickersUpdated(HashMap<String, ExchangeData> data) {
//        if (fromWallet == null) {
//            updateView();
//        }
    }

    @Click(R.id.llCoinFrom)
    void onSwitchCoinFrom() {
//        showSelectDialog(fromPosition, toPosition);
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
//        fromAmount = fromWallet.getBalance() * percents[index];
        updateAmountValues();
    }

    @Click(R.id.llCoinTo)
    public void onSwitchCoinTo() {
//        showSelectDialog(toPosition, fromPosition);
    }

    private void showSelectDialog(int basePosition, int otherPosition) {
//        if (dialog.getDialog() != null && dialog.getDialog().isShowing()) return;
//        if (confirmDialog.getDialog() != null && confirmDialog.getDialog().isShowing()) return;
//        if (fromWallet == null || toWallet == null) return;
//        final Bundle args = new Bundle();
//        args.putString("type", fromWallet.getSymbolData().getType());
//        args.putInt("base_position", basePosition);
//        args.putInt("other_position", otherPosition);
//        dialog.setArguments(args);
//        dialog.setOnDialogDismissListener(isSuccess -> {
//            final int position = dialog.getSelectedPosition();
//            if (basePosition == fromPosition) {
//                onFromCoinUpdated(position);
//            } else {
//                onToCoinUpdated(position);
//            }
//        });
//        dialog.show(getChildFragmentManager(), WalletUtils.TransactionType.EXCHANGE.name());
    }

    @Click(R.id.fab)
    public void onSwap() {
//        fromAmount = toAmount;
//
//        final int fromPosition = this.fromPosition;
//        onFromCoinUpdated(toPosition);
//        onToCoinUpdated(fromPosition);
    }

    @Click(R.id.llAmountFrom)
    void onClickAmountFrom() {
//        if (fromWallet == null) return;
//        final Intent intent = new Intent(getContext(), ExchangeInputActivity_.class);
//        intent.putExtra("wallet", fromWallet);
//        intent.putExtra("amount", fromAmount);
//        startActivityForResult(intent, Constants.ActivityRequestCodes.AMOUNT_INPUT_FROM);
    }

    @Click(R.id.llAmountTo)
    void onClickAmountTo() {
//        if (toWallet == null) return;
//        final Intent intent = new Intent(getContext(), ExchangeInputActivity_.class);
//        intent.putExtra("wallet", toWallet);
//        intent.putExtra("amount", toAmount);
//        startActivityForResult(intent, Constants.ActivityRequestCodes.AMOUNT_INPUT_TO);
    }

    @CheckedChange(R.id.checkBox)
    void onCheckedChange(boolean isChecked) {
//        btnConfirm.setEnabled(isChecked);
    }

    @Click(R.id.btnConfirm)
    void onNext() {
//        if (fromAmount == 0) {
//            showAlert(getString(R.string.error_amount_empty));
//            return;
//        }
//        if (dialog.getDialog() != null && dialog.getDialog().isShowing()) return;
//        if (confirmDialog.getDialog() != null && confirmDialog.getDialog().isShowing()) return;
//
//        onQuote();
    }

    private void onFromCoinUpdated(int position) {
//        if (position < 0 || availableWallets.size() == 0) return;
//        fromPosition = position;
//        if (availableWallets.size() > position) {
//            fromWallet = availableWallets.get(position);
//        } else {
//            fromWallet = availableWallets.get(0);
//        }
//
//        setStatusBarColor(Utils.getTransparentColor(fromWallet.getColor()));
//        Glide.with(getContext())
//                .load(fromWallet.getSymbolData().getColoredImage())
//                .into(imgCoinFrom);
//        txtCoinFrom1.setText(fromWallet.getTitle());
//        txtCoinFrom2.setText(fromWallet.getSymbol());
//        txtBalance.setText(String.format("%s: %s %s", getString(R.string.balance), Utils.formattedNumber(fromWallet.getBalance(), 0, 5), fromWallet.getSymbol()));
//        txtPercent1.setTextColor(Color.parseColor(fromWallet.getColor()));
//        txtPercent2.setTextColor(Color.parseColor(fromWallet.getColor()));
//        txtPercent3.setTextColor(Color.parseColor(fromWallet.getColor()));
//
//        updateCommonValues();
    }

    private void onToCoinUpdated(int position) {
//        if (position < 0 || availableWallets.size() == 0) return;
//        toPosition = position;
//        if (availableWallets.size() > position) {
//            toWallet = availableWallets.get(position);
//        } else {
//            toWallet = availableWallets.get(0);
//        }
//
//        Glide.with(getContext())
//                .load(toWallet.getSymbolData().getColoredImage())
//                .into(imgCoinTo);
//        txtCoinTo1.setText(toWallet.getTitle());
//        txtCoinTo2.setText(toWallet.getSymbol());
//
//        updateCommonValues();
    }

    private void updateCommonValues() {
//        if (fromWallet == null || toWallet == null) return;
//
//        fromExchange = AppData.getInstance().getExchangeTickers().get(fromWallet.getSymbol());
//        toExchange = AppData.getInstance().getExchangeTickers().get(toWallet.getSymbol());
//
//        int startColor = Utils.getTransparentColor(fromWallet.getColor());
//        int endColor = Utils.getTransparentColor(toWallet.getColor());
//        int centerColor = (Integer) new ArgbEvaluator().evaluate(0.6f, startColor, endColor);
//
//        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{startColor, centerColor, endColor});
//        llBackground.setBackground(gradientDrawable);
//        fabBorder.getBackground().setColorFilter(centerColor, PorterDuff.Mode.SRC);
//
//        if (toWallet.getSymbolData().getType().equals("peggedAsset") && fromWallet.getSymbol().equals("SXP")) {
//            isFromAssetPegged = true;
//        }
//
//        if (fromExchange == null || toExchange == null) return;
//
//        if (true || !isFromAssetPegged) {
//            txtAmount.setText(String.format("1 %s = %s %s", fromWallet.getSymbol(), Utils.formattedNumber(toExchange.getPrice() != 0 ? fromExchange.getPrice() / toExchange.getPrice() * (1 - fromExchange.getFee()) : 0), toWallet.getSymbol()));
//        } else {
//            txtAmount.setText(String.format("1 %s = 1 %s", fromWallet.getSymbol(), toWallet.getSymbol()));
//        }
//        txtExchangeSymbol.setText(fromWallet.getSymbol());
//        txtReceiveSymbol.setText(toWallet.getSymbol());
//        updateAmountValues();
    }

    private void updateAmountValues() {
//        if (fromExchange == null || toExchange == null) return;
//
//        if (fromAmount > fromWallet.getBalance()) fromAmount = fromWallet.getBalance();
//        toAmount = fromAmount * fromExchange.getPrice() / toExchange.getPrice() * (1 - fromExchange.getFee());
//
//        txtAmountFrom.setText(Utils.formattedNumber(fromAmount));
//        txtAmountTo.setText(Utils.formattedNumber(toAmount));
//        txtExchange.setText(Utils.formattedNumber(fromAmount));
//        txtReceive.setText(Utils.formattedNumber(toAmount));
    }

    public void resetAmount() {
//        fromAmount = 0;
//        updateAmountValues();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
//            double amount = data.getDoubleExtra("amount", 0);
//            if (requestCode == Constants.ActivityRequestCodes.AMOUNT_INPUT_FROM) {
//
//            } else if (requestCode == Constants.ActivityRequestCodes.AMOUNT_INPUT_TO) {
//                amount = amount * toExchange.getPrice() / fromExchange.getPrice() / (1 - fromExchange.getFee());
//            } else {
//                return;
//            }
//            fromAmount = Math.min(amount, fromWallet.getBalance());
//            updateAmountValues();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onQuote() {
//        final ExchangeQuoteRequest request = new ExchangeQuoteRequest();
//        request.setFromExchangeId(fromExchange.getExchangeId());
//        request.setToExchangeId(toExchange.getExchangeId());
//        request.setFromQuantity(fromAmount);

//        ApiClient.getInterface().exchangeQuote(request).enqueue(new AppCallback<>(getContext(), this));
    }

    private void onConfirm(ExchangeQuote quote) {
        final Bundle args = new Bundle();
//        final String[] args1 = new String[]{
//                fromWallet.getColor(), fromWallet.getSymbolData().getColoredImage(), fromWallet.getSymbol(),
//                toWallet.getColor(), toWallet.getSymbolData().getColoredImage(), toWallet.getSymbol(),
//                quote.getFee().getAmountFormatted()
//        };
//        final double[] args2 = new double[]{quote.getFromQuantity(), quote.getToQuantity()};
//        args.putStringArray("args1", args1);
//        args.putDoubleArray("args2", args2);
//        args.putInt("quote_id", quote.getId());
//        confirmDialog.setArguments(args);
//        confirmDialog.setOnDialogDismissListener(isSuccess -> {
//            Intent intent = new Intent(getContext(), ExchangeSuccessActivity_.class);
//            intent.putExtra("args1", args1);
//            intent.putExtra("args2", args2);
//            startActivity(intent);
//        });
//        confirmDialog.show(getChildFragmentManager(), WalletUtils.TransactionType.EXCHANGE.name());
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof ExchangeQuoteResponse) {
            onConfirm(((ExchangeQuoteResponse) response).getData());
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
