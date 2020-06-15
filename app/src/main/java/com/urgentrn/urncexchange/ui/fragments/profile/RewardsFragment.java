package com.urgentrn.urncexchange.ui.fragments.profile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.RewardData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetReferralResponse;
import com.urgentrn.urncexchange.models.response.GetRewardsResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@EFragment(R.layout.fragment_rewards)
public class RewardsFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtCardRewardsPercent, txtWalletRewardsPercent, txtReferralCode, txtCommission, txtUnlocked, txtBonus, txtCardRewards, txtWalletRewards, txtRewardsDescription;

    @ViewById
    View progressCode;

    @ViewById
    ImageView imgLock;

    private static String referralCode, referralMessage;
    private static List<RewardData> rewards = new ArrayList<>();

    @AfterViews
    protected void init() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);

        txtRewardsDescription.setText(null);

        if (referralCode == null) {
            txtReferralCode.setVisibility(View.INVISIBLE);
            progressCode.setVisibility(View.VISIBLE);
        } else {
            txtReferralCode.setText(referralCode);
        }

        ApiClient.getInterface().getReferralCode().enqueue(new AppCallback<>(this));
        ApiClient.getInterface().getRewards().enqueue(new AppCallback<>(this));
        updateView();
    }

    @Override
    public void updateView() {
        for (RewardData data : rewards) {
            if (!data.isEligible()) continue;
            final String amountFormatted = data.getAmountFormatted();
            final String amountPaidFormatted = data.getAmountPaidFormatted() != null && !data.getAmountPaidFormatted().isEmpty() ? data.getAmountPaidFormatted() : "$0.00";
            final String amountReversedFormatted = data.getAmountReversedFormatted() != null && !data.getAmountReversedFormatted().isEmpty() ? data.getAmountReversedFormatted() : "$0.00";
            if (data.getSchedule().equals("cardPurchase")) {
                txtCardRewardsPercent.setText(amountFormatted);
                txtCardRewards.setText(amountPaidFormatted);
            } else if (data.getSchedule().equals("exchange")) {
                txtWalletRewardsPercent.setText(amountFormatted);
                txtWalletRewards.setText(amountPaidFormatted);
            } else if (data.getSchedule().equals("tierUpgrade")) {
                txtCommission.setText(amountPaidFormatted);
                txtBonus.setText(amountReversedFormatted);
                txtRewardsDescription.setText(getString(R.string.rewards_description, amountFormatted));
                if (data.getReserveEndDate() != null) {
                    final Date endDate = Utils.stringToDate(data.getReserveEndDate(), "yyyy-MM-dd");
                    if (endDate != null) {
                        long mills = endDate.getTime() - new Date().getTime();
                        long month = mills / 1000 / (3600 * 24 * 30);
                        long day = (mills / 1000 / (3600 * 24)) % 30;
                        imgLock.setImageResource(R.mipmap.ic_lock);
                        if (month > 0) {
                            txtUnlocked.setText(String.format(Locale.US, "%s %dmo, %dd", "Unlocked in", month, day));
                        } else {
                            txtUnlocked.setText(String.format(Locale.US, "%s %dd", "Unlocked in", day));
                        }
                    }
                } else {
                    imgLock.setImageResource(R.mipmap.ic_lock); // TODO: confirm if replaced with unlock icon
                    txtUnlocked.setText(R.string.unlocked);
                }
            }
        }
    }

    @Click(R.id.llCopy)
    void onCopy() {
        if (referralCode == null) return;
        final ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(getString(R.string.referral_code), referralCode);
        clipboard.setPrimaryClip(clip);
        showToast(getString(R.string.referral_code_copied), true);
    }

    @Click(R.id.btnInvite)
    void onInvite() {
        if (referralCode == null) return;
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, referralMessage != null ? referralMessage : referralCode);
        startActivity(Intent.createChooser(intent, "Share Referral Code"));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (!isAdded()) return;
        if (response instanceof GetReferralResponse) {
            referralCode = ((GetReferralResponse)response).getCode();
            referralMessage = ((GetReferralResponse)response).getMessage();

            txtReferralCode.setText(referralCode);
            txtReferralCode.setVisibility(View.VISIBLE);
            progressCode.setVisibility(View.GONE);
        } else if (response instanceof GetRewardsResponse) {
            final List<RewardData> data = ((GetRewardsResponse)response).getData();
            if (data != null) {
                rewards = data;
                updateView();
            }
        }
    }

    @Override
    public void onFailure(String message) {
        if (!isAdded()) return;
        progressCode.setVisibility(View.GONE);
    }
}
