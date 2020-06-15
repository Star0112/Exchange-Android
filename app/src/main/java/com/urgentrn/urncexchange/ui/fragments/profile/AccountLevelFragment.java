package com.urgentrn.urncexchange.ui.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Limit;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.card.CardDetail;
import com.urgentrn.urncexchange.ui.MainActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.kyc.DocUploadActivity_;
import com.urgentrn.urncexchange.ui.kyc.VerifyAccountActivity_;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_account_level)
public class AccountLevelFragment extends BaseFragment {

    @ViewById
    TextView txtTierLevel, txtCurrentTier, txtCryptoWithdraw, txtCryptoDeposit, txtBankWithdraw, txtBank, txtCard, txtWire, txtReceive, txtSend, txtOrder, txtType, txtCardPurchases, txtATMWithdraw, txtLevel0, txtLevel1, txtLevel2, txtLevel3;

    @ViewById
    ImageView imgLevel0, imgLevel1, imgLevel2, imgLevel3;

    @ViewById
    View llPending, llTier, llCard, llNoCard;

    @AfterViews
    protected void init() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);

        final User user = getUser();
        if (user == null) return;
        if (user.isTierPending() && user.getTierLevel() >= 2) {
            llPending.setVisibility(View.VISIBLE);
            txtTierLevel.setText(getString(R.string.tier_verification, user.getTierLevel()));
            llTier.setVisibility(View.GONE);
        } else {
            llPending.setVisibility(View.GONE);
            llTier.setVisibility(View.VISIBLE);
            txtCurrentTier.setText(String.valueOf(user.getTierLevel()));
        }

        final Limit cryptoWithdrawLimit = user.getLimits().getWallet().get("withdraw");
        if (cryptoWithdrawLimit != null) {
            txtCryptoWithdraw.setText(String.format("%s/%s", cryptoWithdrawLimit.getAmountFormatted(), cryptoWithdrawLimit.getPeriod()));
        }

        final Limit cryptoDepositLimit = user.getLimits().getWallet().get("deposit");
        if (cryptoDepositLimit != null) {
            txtCryptoDeposit.setText(String.format("%s/%s", cryptoDepositLimit.getAmountFormatted(), cryptoDepositLimit.getPeriod()));
        }

        final Limit bankWithdrawLimit = user.getLimits().getBank().get("withdraw");
        if (bankWithdrawLimit != null) {
            txtBankWithdraw.setText(String.format("%s/%s", bankWithdrawLimit.getAmountFormatted(), bankWithdrawLimit.getPeriod()));
        }

        final Limit bankDepositLimit = user.getLimits().getBank().get("deposit");
        if (bankDepositLimit != null) {
            txtBank.setText(String.format("%s/%s", bankDepositLimit.getAmountFormatted(), bankDepositLimit.getPeriod()));
        }

        txtCard.setText(String.format("$%s/%s", Utils.formattedNumber(user.getLimits().getCard().getAmount()), user.getLimits().getCard().getPeriod()));
        txtWire.setText(String.format("$%s/%s", Utils.formattedNumber(user.getLimits().getWire().getAmount()), user.getLimits().getWire().getPeriod()));
        txtReceive.setText(user.getTierLevel() > 0 ? R.string.enabled : R.string.disabled);
        txtSend.setText(user.getTierLevel() > 0 ? R.string.enabled : R.string.disabled);
        txtOrder.setText(user.getTierLevel() > 2 || (user.getTierLevel() == 2 && user.isTierCompleted()) ? R.string.enabled : R.string.disabled);

        txtLevel0.setText(String.format("%s 0", getString(R.string.tier_level)));
        txtLevel1.setText(String.format("%s 1", getString(R.string.tier_level)));
        txtLevel2.setText(String.format("%s 2", getString(R.string.tier_level)));
        txtLevel3.setText(String.format("%s 3", getString(R.string.tier_level)));

        imgLevel0.setImageTintList(getResources().getColorStateList(R.color.colorGreen));
        imgLevel1.setImageTintList(getResources().getColorStateList(user.getTierLevel() < 1 ? R.color.colorRed : R.color.colorGreen));
        imgLevel2.setImageTintList(getResources().getColorStateList(user.getTierLevel() < 2 ? R.color.colorRed : user.getTierLevel() == 2 && user.isTierPending() ? R.color.colorPendingLight : R.color.colorGreen));
        imgLevel3.setImageTintList(getResources().getColorStateList(user.getTierLevel() < 3 ? R.color.colorRed : user.getTierLevel() == 3 && user.isTierPending() ? R.color.colorPendingLight : R.color.colorGreen));

        if (AppData.getInstance().getMyCards() != null) {
            for (Card card : AppData.getInstance().getMyCards()) {
                if (!card.isGift()) {
                    llCard.setVisibility(View.VISIBLE);
                    llNoCard.setVisibility(View.GONE);
                    txtType.setText(card.getCardInfo().getTitle());
                    for (CardDetail detail : card.getCardInfo().getDetails()) {
                        if (detail.getDetail().equals("Daily Limit")) {
                            txtCardPurchases.setText(String.format("%s/day", detail.getTitle()));
                        } else if (detail.getDetail().equals("Daily ATM Limit")) {
                            txtATMWithdraw.setText(String.format("%s/day", detail.getTitle()));
                        }
                    }
                    return;
                }
            }
        }
        // no cards available
        llCard.setVisibility(View.GONE);
        llNoCard.setVisibility(View.VISIBLE);
    }

    @Click(R.id.btnUpgrade)
    void onUpgrade() {
        if (getUser() == null) return;
        Intent intent;
        if (getUser().getTierLevel() == 0) {
            intent = new Intent(getContext(), VerifyAccountActivity_.class);
            startActivity(intent);
        } else if (getUser().getTierLevel() == 1) {
            intent = new Intent(getContext(), DocUploadActivity_.class);
            startActivity(intent);
        } else if (getUser().getTierLevel() == 2) {
            showAlert(getString(R.string.error_tier3_upgrade));
        }
    }

    @Click(R.id.btnOrder)
    void onOrder() {
        ((MainActivity)getActivity()).setActiveTab(R.id.navigation_card, null);
        onBackPressed();
    }

    @Click(R.id.llLevel0)
    void onLevel0() {
        onTierDetail(0);
    }

    @Click(R.id.llLevel1)
    void onLevel1() {
        onTierDetail(1);
    }

    @Click(R.id.llLevel2)
    void onLevel2() {
        onTierDetail(2);
    }

    @Click(R.id.llLevel3)
    void onLevel3() {
        onTierDetail(3);
    }

    private void onTierDetail(int level) {
        TierLevelFragment fragment = new TierLevelFragment_();
        final Bundle args = new Bundle();
        args.putSerializable("level", level);
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }
}
