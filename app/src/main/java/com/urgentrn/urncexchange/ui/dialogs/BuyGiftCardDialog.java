package com.urgentrn.urncexchange.ui.dialogs;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
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
import com.urgentrn.urncexchange.models.card.CardInfo;
import com.urgentrn.urncexchange.models.card.GiftCard;
import com.urgentrn.urncexchange.models.request.IssueCardRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.adapter.SelectWalletAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
//import com.urgentrn.urncexchange.ui.transactions.BuySellSuccessActivity_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_buy_gift_card)
public class BuyGiftCardDialog extends BaseDialog implements ApiCallback {

    @ViewById
    View llBuySell, llWallet, llAccount, llSelectCoin, imgSwitch, llPercent;

    @ViewById
    TextView txtTitle, txtPrice, txtAmount, txtSelectAmount, txtSelectCoin, txtBalance, btnNext, txtTerms;

    @ViewById
    ImageView imgSelectCoin;

    @ViewById
    Numpad padView;

    @ViewById
    Button btnPassword, btnPin;

    // Confirm View
    @ViewById
    View llConfirm;

    // Select Wallet View
    @ViewById
    View llSelectWallet;

    @ViewById
    RecyclerView recyclerView;

    private GiftCard card;
    private double price/*USD*/, amount/*Coin*/, priceFee, amountFee;
    private Wallet wallet;

    @AfterViews
    protected void init() {
        card = (GiftCard)getArguments().getSerializable("card");

        price = amount = 0;
        if (card.getPriceType().equals("fixed")) {
            price = getArguments().getDouble("price");
        }

        final int bestWalletIndex = WalletUtils.getBestWalletIndex();
        wallet = AppData.getInstance().getWallets().get(bestWalletIndex);

        txtTitle.setText(R.string.amount);

        txtPrice.setText(String.format("$%s", Utils.formattedNumber(price)));
        txtPrice.setTextColor(getResources().getColor(R.color.textColorPrimary));
        txtAmount.setVisibility(View.INVISIBLE);
        txtSelectAmount.setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));

        padView.setDecimal(false);
        padView.setOnTextChangeListener((text, digits_remaining) -> {
            price = padView.getValue();
            final double marketPrice;
            final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
            if (symbol != null && symbol.getMarketData() != null) {
                marketPrice = symbol.getMarketData().getPrice();
            } else {
                marketPrice = wallet.getSymbolData().getPrice();
            }
            amount = price / marketPrice;
            txtPrice.setText(String.format("$%s", text));
            txtSelectAmount.setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
        });

        final String termText = getString(R.string.cardholder_accept_terms)
                .replace(getString(R.string.terms_conditions), "<a href=\"" + Utils.getTermLink("TERMS_OF_USE") + "\">" + getString(R.string.terms_conditions) + "</a>");
        txtTerms.setText(Html.fromHtml(termText));
        txtTerms.setMovementMethod(LinkMovementMethod.getInstance());
        onCheckedChange(null, false);

        btnNext.setText(R.string.buy);

        // Select Wallet View
        initSelectWalletView(bestWalletIndex);

        updateWalletView();

        // Confirm View
        if (card.getPriceType().equals("fixed")) {
            showConfirmView();
            llBuySell.setVisibility(View.GONE);
        } else {
            llConfirm.setVisibility(View.GONE);
        }

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
        llWallet.setVisibility(View.GONE);
        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgSelectCoin);
        txtSelectCoin.setText(String.format("%s %s", wallet.getSymbol(), getString(R.string.title_wallet)));
        txtBalance.setText(wallet.getBalanceCurrencyFormatted());

        txtSelectAmount.setVisibility(wallet.getSymbolData().isCurrency() ? View.GONE : View.VISIBLE);
        imgSwitch.setVisibility(View.GONE);
        llPercent.setVisibility(View.GONE);

        padView.setValue(price);

        updateConfirmValues();
    }

    private void showConfirmView() {
        Glide.with(getContext()).load(card.getImage()).into(((ImageView)llConfirm.findViewById(R.id.imgCard)));
        final TextView txtDisclaimer = llConfirm.findViewById(R.id.txtDisclaimer);
        if (true || TextUtils.isEmpty(card.getDisclaimer())) {
            txtDisclaimer.setVisibility(View.GONE);
        } else {
            txtDisclaimer.setOnClickListener(v -> ((BaseActivity)getActivity()).showAlert(card.getName(), card.getDisclaimer()));
            txtDisclaimer.setText(card.getDisclaimer());
        }

        updateConfirmValues();

        if (!card.getPriceType().equals("fixed")) {
            replaceView(llBuySell, llConfirm, false);
        }
    }

    private void updateConfirmValues() {
        final double fee = getArguments().getDouble("fee");
        priceFee = price * fee;
        amountFee = amount * fee;
        if (card.getColor() != null) {
            ((CardView)llConfirm.findViewById(R.id.cardView)).setCardBackgroundColor(Color.parseColor(card.getColor()));
        }
        ((TextView)llConfirm.findViewById(R.id.txtConfirmPrice)).setText(String.format("$%s", Utils.formattedNumber(price)));
        ((TextView)llConfirm.findViewById(R.id.txtPaymentMethod)).setText(String.format("%s Wallet", wallet.getSymbol().toUpperCase()));
        ((TextView)llConfirm.findViewById(R.id.txtConfirmAmount)).setText(String.format("%s %s", Utils.formattedNumber(amount), wallet.getSymbol()));
        ((TextView)llConfirm.findViewById(R.id.txtFee)).setText(String.format("$%s", Utils.formattedNumber(priceFee)));
        ((TextView)llConfirm.findViewById(R.id.txtTotal)).setText(String.format("$%s", Utils.formattedNumber(price + priceFee)));
    }

    @Click(R.id.llSelectCoin)
    void onWalletClicked() {
        replaceView(llConfirm, llSelectWallet, false);
    }

    @CheckedChange(R.id.checkBox)
    void onCheckedChange(CompoundButton v, boolean isChecked) {
        btnPassword.setEnabled(isChecked);
        btnPin.setEnabled(isChecked);
    }

    @Click({R.id.btnBackSelect, R.id.btnBackConfirm})
    void onBack() {
        if (llSelectWallet.getVisibility() == View.VISIBLE) {
            replaceView(llSelectWallet, llConfirm, true);
        } else {
            if (card.getPriceType().equals("fixed")) {
                onClose();
            } else {
                replaceView(llConfirm, llBuySell, true);
            }
        }
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
        if (!card.getPriceType().equals("fixed")) {
            if (price < card.getMinPrice()) {
                showAlert("The amount is out of range.");
                return;
            } else if (price > card.getMaxPrice()) {
                showAlert("The amount is out of range.");
                return;
            }
        }

        showConfirmView();
    }

    private void onConfirm() {
        if (price + priceFee > wallet.getBalanceCurrency()) {
            showAlert(getString(R.string.error_larger_amount_transaction, getString(R.string.buy).toLowerCase()));
            return;
        }

        if (AppData.getInstance().getAvailableCards().size() == 0) return;
        showProgressBar();

        int cardId = 0;
        for (CardInfo card : AppData.getInstance().getAvailableCards()) {
            if (card.isGift()) {
                cardId = card.getId();
            }
        }
        if (cardId == 0) {
            showAlert("No gift card available");
            return;
        }
        final int assetId = wallet.getSymbolData().getId();
        ApiClient.getInterface()
                .issueCard(new IssueCardRequest(cardId, assetId, card.getRef(), price))
                .enqueue(new AppCallback<>(getContext(), this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;
        hideProgressBar();
//
//        final Intent intent = new Intent(getContext(), BuySellSuccessActivity_.class);
//        intent.putExtra("type", WalletUtils.TransactionType.GIFT);
//        intent.putExtra("name", card.getName());
//        intent.putExtra("amount", amount);
//        startActivity(intent);
//        dismiss();
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();
    }
}
