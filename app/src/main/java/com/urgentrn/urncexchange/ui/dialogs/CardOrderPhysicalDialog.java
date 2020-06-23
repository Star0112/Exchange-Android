package com.urgentrn.urncexchange.ui.dialogs;

import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.card.CardInfo;
import com.urgentrn.urncexchange.models.card.CardUpgrade;
import com.urgentrn.urncexchange.models.request.UpgradeCardRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
//import com.urgentrn.urncexchange.ui.CardSuccessActivity_;
import com.urgentrn.urncexchange.ui.adapter.SelectWalletAdapter;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Locale;

@EFragment(R.layout.dialog_card_order_physical)
public class CardOrderPhysicalDialog extends BaseDialog implements ApiCallback {

    @ViewById
    View llOrder;

    // Balance View
    @ViewById
    View llLowBalance, llLock;
    @ViewById
    TextView txtBalance, txtLockPeriod, txtLockAmount;

    @ViewById
    TextView txtFee, txtCoin, txtAmount, txtTerms;

    @ViewById
    ImageView imgCoin;

    @ViewById
    Button btnPassword, btnPin;

    // Select Wallet View
    @ViewById
    View llSelectWallet;

    @ViewById
    RecyclerView recyclerView;

    private CardInfo card;
    private int selectedWalletPosition;

    @AfterViews
    protected void init() {
        card = (CardInfo)getArguments().getSerializable("card");
        selectedWalletPosition = WalletUtils.getBestWalletIndex();

//        showBalanceView();

        txtFee.setText(String.format("-$%s", Utils.formattedNumber(25))); // TODO: why hardcoded?
        updateWalletView();

        final String termText = getString(R.string.cardholder_terms)
                .replace(getString(R.string.terms_conditions), "<a href=\"" + card.getTerms() + "\">" + getString(R.string.terms_conditions) + "</a>")
                .replace(getString(R.string.cardholder_agreement), "<a href=\"" + card.getPrivacyPolicy() + "\">" + getString(R.string.cardholder_agreement) + "</a>");
        txtTerms.setText(Html.fromHtml(termText));
        txtTerms.setMovementMethod(LinkMovementMethod.getInstance());
        onCheckedChange(null, false);

        // Select Wallet View
        initSelectWalletView();

        initPassView(Constants.SecurityType.CARD, null, isSuccess -> {
            if (isSuccess) onConfirm();
        });
    }

    private void showBalanceView() {
        for (Wallet wallet : AppData.getInstance().getWallets()) {
            if (wallet.getSymbol().equalsIgnoreCase("SXP")) {
                txtBalance.setText(Utils.formattedNumber(wallet.getBalance(), 0, 5));
                if (wallet.getBalance() < card.getReserveAmount()) {
                    llLowBalance.setVisibility(View.VISIBLE);
                } else {
                    llLowBalance.setVisibility(View.GONE);
                }
            }
        }
        if (card.getReservePeriodFactor() == 0) {
            llLock.setVisibility(View.GONE);
        } else {
            llLock.setVisibility(View.VISIBLE);
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 6);
            txtLockPeriod.setText(getString(R.string.month_lock, Utils.dateToString(cal, "MM/dd/yyyy")));
        }
        txtLockAmount.setText(String.format(Locale.US, "%s %s",
                card.getReserveAsset() != null ? Utils.formattedNumber(card.getReserveAmount()) : 0,
                card.getReserveAsset() != null ? card.getReserveAsset() : "SXP"
        ));
    }

    private void initSelectWalletView() {
        llSelectWallet.setVisibility(View.GONE);
        final SelectWalletAdapter adapter = new SelectWalletAdapter(position -> {
            selectedWalletPosition = position;
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
        final Wallet wallet = AppData.getInstance().getWallets().get(selectedWalletPosition);

        Glide.with(getContext()).load(wallet.getSymbolData().getColoredImage()).into(imgCoin);
        txtCoin.setText(wallet.getTitle());
        txtAmount.setText(wallet.getBalanceCurrencyFormatted());
    }

    @Click(R.id.llCoin)
    void onSelectCoin() {
        replaceView(llOrder, llSelectWallet, false);
    }

    @CheckedChange(R.id.checkBox)
    void onCheckedChange(CompoundButton v, boolean isChecked) {
        btnPassword.setEnabled(isChecked);
        btnPin.setEnabled(isChecked);
    }

    private void onConfirm() {
        final CardUpgrade upgrade = card.getConvertToPhysical();
        if (upgrade == null) {
            showAlert(getString(R.string.error_card_order_physical_not_available));
            return;
        }
        showProgressBar();
        final int assetId = AppData.getInstance().getWallets().get(selectedWalletPosition).getSymbolData().getId();
        ApiClient.getInterface()
                .upgradeCard(new UpgradeCardRequest(upgrade.getId(), false, assetId))
                .enqueue(new AppCallback<>(getContext(), this));
    }

    @Click(R.id.btnBackSelect)
    void onBack() {
        replaceView(llSelectWallet, llOrder, true);
    }

    @Click({R.id.btnClose, R.id.btnCloseSelect})
    void onClose() {
        dismiss();
    }

    @Override
    public void onResponse(BaseResponse response) {
        hideProgressBar();
//        final Intent intent = new Intent(getContext(), CardSuccessActivity_.class);
//        intent.putExtra("type", Constants.VerifyType.CARD_ORDER_PHYSICAL);
//        startActivity(intent);
//        dismiss();
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();
    }
}
