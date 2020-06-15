package com.urgentrn.urncexchange.ui.dialogs;

import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.Exchange;
import com.urgentrn.urncexchange.models.Transaction;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.card.CardDetail;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

@EFragment(R.layout.dialog_transaction)
public class TransactionDialog extends BaseDialog {

    @ViewById
    View llTransaction, llLock, llRate, llAmount, llReceived, llExchanged, llPaymentMethod, llSpent, llFee;

    @ViewById
    TextView txtTitle, txtAmount, txtRate, txtAmountTitle, txtUsdAmount, txtDate, txtStatus, txtTransactionId;

    // Sent, Exchanged
    @ViewById
    TextView txtReceived, txtExchanged;

    // Bought, Sold
    @ViewById
    TextView txtPaymentMethod, txtFee;

    // Spent
    @ViewById
    TextView txtVendor, txtSpentLabel, txtSpent, txtSpentRate, txtSpentCoin, txtSpentAmount;

    // Card Lock
    @ViewById
    TextView txtLockedAmount, txtReleaseDate, txtCardRewardsBack, txtWalletRewardsBack, txtReferralRewards;

    @ViewById
    ImageView imgCoin, imgStatus, imgSpendVip;

    private Wallet wallet;
    private Transaction transaction;
    private Card card;

    @AfterViews
    protected void init() {
        if (getArguments() == null) return;
        wallet = (Wallet) getArguments().getSerializable("wallet");
        transaction = (Transaction)getArguments().getSerializable("transaction");
        if (wallet == null || transaction == null) return;

        final String typeLowerCase = transaction.getType().toLowerCase();
        if (typeLowerCase.equals("reserveentry")) { // Card Lock
            llTransaction.setVisibility(View.GONE);
            llLock.setVisibility(View.VISIBLE);
            showLockView();
        } else {
            llTransaction.setVisibility(View.VISIBLE);
            llLock.setVisibility(View.GONE);
            llSpent.setVisibility(View.GONE);
            if (typeLowerCase.equals("send") ||typeLowerCase.equals("receive") ||
                    typeLowerCase.contains("deposit") || typeLowerCase.contains("withdraw") ||
                    typeLowerCase.contains("reward") || typeLowerCase.contains("release")) {
                showSentReceivedView();
            } else if (typeLowerCase.contains("exchange")) {
                if (transaction.getExchange() == null) { // TODO: when does this happen?

                } else {
                    final String exchangeType = WalletUtils.checkBuyOrSell(transaction.getSymbol(), transaction.getExchange().getSymbol());
                    if (exchangeType.isEmpty()) { // exchange
                        showExchangedView(typeLowerCase.contains("send"));
                    } else {
                        showBoughtSoldView(exchangeType.equals("buy"));
                    }
                }
            } else if (typeLowerCase.contains("purchase") || typeLowerCase.contains("refund")) { // spent
                showSpentView(typeLowerCase);
            }

            if (!typeLowerCase.contains("release")) {
//                imgCoin.setImageTintList(ColorStateList.valueOf(Color.parseColor(wallet.getColor())));
            }
        }

        txtDate.setText(Utils.formattedDateTime(transaction.getDateTime()));

        int colorRes;
        String status;
        switch (transaction.getStatus()) {
            case "completed":
                colorRes = R.color.colorComplete;
                status = getString(R.string.button_complete);
                break;
            case "pending":
                colorRes = R.color.colorPending;
                status = getString(R.string.pending);
                break;
            default:
                colorRes = R.color.colorRed;
                status = transaction.getStatus();
                break;
        }
        imgStatus.setImageTintList(ColorStateList.valueOf(getResources().getColor(colorRes)));
        txtStatus.setText(status);

        txtTransactionId.setText(String.format(Locale.US, "%s: %d", getString(R.string.transaction_id), transaction.getId()));
    }

    private void showExchangedView(boolean isSend) {
        llAmount.setVisibility(View.GONE);
        llPaymentMethod.setVisibility(View.GONE);
        llFee.setVisibility(View.GONE);

        final Exchange exchange = transaction.getExchange();
        txtTitle.setText(String.format("%s %s", getString(R.string.exchanged), wallet.getTitle()));
        imgCoin.setImageResource(R.mipmap.ic_exchanged);
        txtAmount.setText(String.format("%s %s", Utils.formattedNumber(Math.abs(transaction.getAmount())), transaction.getSymbol()));
        final String rateFormatted = exchange.getAmount() < 0 ? Utils.formattedNumber(1 / exchange.getRate()) : exchange.getRateFormatted();
        txtRate.setText(String.format("1 %s = %s %s", transaction.getSymbol(), rateFormatted, exchange.getSymbol()));
        final String baseAmountFormatted = Utils.formattedNumber(Math.abs(transaction.getAmount())) + " " + transaction.getSymbol();
        final String exchangedAmountFormatted = Utils.formattedNumber(exchange.getAmount()) + " " + exchange.getSymbol();
        txtReceived.setText(isSend ? exchangedAmountFormatted : baseAmountFormatted);
        txtExchanged.setText(isSend ? baseAmountFormatted : exchangedAmountFormatted);
    }

    private void showBoughtSoldView(boolean isBought) { // true: currency wallet, false: coin wallet
        llReceived.setVisibility(View.GONE);
        llExchanged.setVisibility(View.GONE);

        final Exchange exchange = transaction.getExchange();
        txtTitle.setText(transaction.getString());
        imgCoin.setImageResource(isBought ? R.mipmap.ic_bought : R.mipmap.ic_sold);
        txtAmount.setText(String.format("%s %s", isBought ? Utils.formattedNumber(exchange.getAmount()) : transaction.getAmountFormatted(),
                isBought ? exchange.getSymbol() : transaction.getSymbol()));
        txtRate.setText(transaction.getConversionRateAtTransactionFormatted());
        txtUsdAmount.setText(transaction.getUsdAmountAtTransactionFormatted());
        txtPaymentMethod.setText(transaction.getMethod());
        txtFee.setText(exchange.getFeeFormatted());
        if (transaction.getStatus().equals("pending") && transaction.getMethod().contains("****")) {
            // TODO
            transaction.getAvailability();
        }
    }

    private void showSentReceivedView() {
        llRate.setVisibility(View.GONE);
        llReceived.setVisibility(View.GONE);
        llExchanged.setVisibility(View.GONE);
        llPaymentMethod.setVisibility(View.GONE);
        llFee.setVisibility(View.GONE);

        String title, amountFormatted, usdAmountFormatted;
        if (wallet.getSymbolData().isCurrency()) {
            final String transactionString = ExchangeApplication.getApp().getConfig().getStrings().getTransactions().get(transaction.getType());
            if (transaction.getString() != null && (transaction.getString().contains("Sent") || transaction.getString().contains("Received"))) {
                if (transaction.getString().contains("Sent")) {
                    title = String.format("%s %s", getString(R.string.sent), wallet.getTitle());
                    txtAmountTitle.setText(R.string.to);
                    usdAmountFormatted = transaction.getSecondText() != null ? transaction.getSecondText().replace("To ", "") : null;
                } else {
                    title = String.format("%s %s", getString(R.string.received), wallet.getTitle());
                    txtAmountTitle.setText(R.string.from);
                    usdAmountFormatted = transaction.getSecondText() != null ? transaction.getSecondText().replace("From ", "") : null;
                }
            } else {
                title = transactionString != null && !transactionString.isEmpty() ? transactionString : transaction.getType();
                txtAmountTitle.setText(String.format("%s %s", title, getString(R.string.method)));
                usdAmountFormatted = transaction.getMethod();
            }
            imgCoin.setImageResource(transaction.getType().contains("Send") || transaction.getType().contains("Withdraw") ? R.mipmap.ic_sent : R.mipmap.ic_received);
            amountFormatted= transaction.getUsdAmountAtTransactionFormatted();
        } else {
            if (transaction.getType().contains("Release")) { // Lock Release
                title = String.format("%s %s", wallet.getSymbol(), getString(R.string.unlock));
                Glide.with(getContext()).load(wallet.getSymbolData().getColoredImage()).into(imgCoin);
                usdAmountFormatted = transaction.getUsdAmountNowFormatted();
            } else {
                switch (transaction.getType()) {
                    case "Send":
                        title = String.format("%s %s", getString(R.string.sent), wallet.getTitle());
                        imgCoin.setImageResource(R.mipmap.ic_sent);
                        break;
                    case "Receive":
                        title = String.format("%s %s", getString(R.string.received), wallet.getTitle());
                        imgCoin.setImageResource(R.mipmap.ic_received);
                        break;
                    case "reward":
                        title = String.format("%s %s", wallet.getTitle(), getString(R.string.reward));
                        imgCoin.setImageResource(R.mipmap.ic_reward);
                        break;
                    default:
                        title = null;
                        imgCoin.setImageResource(R.mipmap.ic_sent);
                        break;
                }
                usdAmountFormatted = transaction.getUsdAmountAtTransactionFormatted();
            }
            amountFormatted = String.format("%s %s", transaction.getAmountFormatted(), wallet.getSymbol());
        }
        txtTitle.setText(title);
        txtAmount.setText(amountFormatted);
        txtUsdAmount.setText(usdAmountFormatted);
    }

    private void showSpentView(String type) {
        llRate.setVisibility(View.GONE);
        llAmount.setVisibility(View.GONE);
        llReceived.setVisibility(View.GONE);
        llExchanged.setVisibility(View.GONE);
        llPaymentMethod.setVisibility(View.GONE);
        llSpent.setVisibility(View.VISIBLE);

        final String title;
        String typeText = null;
        if (ExchangeApplication.getApp().getConfig() != null) {
            typeText = ExchangeApplication.getApp().getConfig().getStrings().getTransactions().get(transaction.getType().toLowerCase());
        }
        if (TextUtils.isEmpty(transaction.getString())) {
            title = (typeText != null ? typeText : transaction.getType()) + " " + wallet.getTitle();
        } else {
            title = transaction.getString();
        }
        txtTitle.setText(title);

        imgCoin.setImageResource(type.contains("purchase") ? R.mipmap.ic_spent: R.mipmap.ic_refund);
        final String amountFormatted = String.format("%s %s", transaction.getAmountFormatted(), transaction.getSymbol());
        txtAmount.setText(amountFormatted);
        txtVendor.setText(transaction.getMerchant());
        txtSpentLabel.setText(typeText != null ? typeText : transaction.getType());
        txtSpent.setText(transaction.getUsdAmountAtTransactionFormatted());
        txtSpentRate.setText(transaction.getConversionRateAtTransactionFormatted());
        txtSpentCoin.setText(wallet.getTitle());
        txtSpentAmount.setText(amountFormatted);
        txtFee.setText(transaction.getFeeFormatted());
    }

    private void showLockView() {
        txtTitle.setText(String.format("%s %s", wallet.getSymbol(), getString(R.string.card_lock)));
        Glide.with(getContext()).load(wallet.getSymbolData().getColoredImage()).into(imgCoin);
        txtAmount.setText(transaction.getUsdAmountNowFormatted());
        txtLockedAmount.setText(String.format("%s %s", Utils.formattedNumber(transaction.getReserveEntry().getAmount()), transaction.getSymbol()));
        txtReleaseDate.setText(Utils.formattedDateTime(transaction.getReserveEntry().getEndDate()));

        if (card != null) {
            for (CardDetail detail : card.getCardInfo().getDetails()) {
                switch (detail.getDetail().toLowerCase()) {
                    case "card rewards back":
                        txtCardRewardsBack.setText(detail.getTitle());
                        break;
                    case "wallet reward back":
                        txtWalletRewardsBack.setText(detail.getTitle());
                        break;
                    case "referral invites":
                        txtReferralRewards.setText(detail.getTitle());
                        break;
                    case "vip benefits":
                        imgSpendVip.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void setActiveCard(Card card) {
        this.card = card;
    }

    @Click(R.id.btnUnlock)
    void onUnlock() {
        ApiClient.getInterface().releaseReserveEntry(transaction.getReserveEntry().getId()).enqueue(new AppCallback<>(getContext(), new ApiCallback() {
            @Override
            public void onResponse(BaseResponse response) {
                dismiss();
            }

            @Override
            public void onFailure(String message) {
                dismiss();
            }
        }));
    }

    @Click(R.id.btnLock)
    void onKeepLock() {
        onClose();
    }

    @Click(R.id.btnClose)
    void onClose() {
        dismiss();
    }
}
