package com.urgentrn.urncexchange.ui.fragments.setting;

import android.app.Activity;
import android.content.Intent;
import android.widget.Switch;
import android.widget.TextView;

import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.Constants;
//import com.urgentrn.urncexchange.ui.contacts.ManageContactsActivity_;
//import com.urgentrn.urncexchange.ui.fragments.profile.PhoneFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import me.aflak.libraries.dialog.FingerprintDialog;

@EFragment(R.layout.fragment_setting)
public class SettingFragment extends BaseFragment implements ApiCallback {

    @ViewById(R.id.newHeader)
    TextView newHeader;

    @ViewById
    Switch switchPasscode, switchBiomitrics;

    @AfterViews
    protected void init() {
        newHeader.setText(R.string.title_setting);
        switchPasscode.setChecked(ExchangeApplication.getApp().getPreferences().isPasscodeEnabled());
        switchBiomitrics.setChecked(ExchangeApplication.getApp().getPreferences().isFingerprintEnabled());

        initView();
        updateView();
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

    private void initView() {}

    @Override
    public void updateView() {

    }

    @Click(R.id.userProfile)
    void onUserProfileClicked() {
        ((BaseFragment)getParentFragment()).replaceFragment(new UserProfileFragment_(), false);
    }

    @Click(R.id.membership)
    void onUserMembershipClicked() {
        ((BaseFragment)getParentFragment()).replaceFragment(new MembershipFragment_(), false);
    }

    @Click(R.id.invite)
    void onUserInviteClicked() {
        ((BaseFragment)getParentFragment()).replaceFragment(new InviteFragment_(), false);
    }

    @CheckedChange(R.id.switchPasscode)
    void onPasscodeChecked(boolean isChecked) {
        if (getActivity() == null) return;
        if (isChecked == ExchangeApplication.getApp().getPreferences().isPasscodeEnabled()) return;
        if (isChecked) {
            ExchangeApplication.getApp().getPreferences().setPasscodeEnabled(true);
        } else {
            ((BaseActivity)getActivity()).showPasscodeDialog(Constants.SecurityType.SETTING, null, isSuccess -> {
                if (isSuccess) {
                    ExchangeApplication.getApp().getPreferences().setPasscodeEnabled(false);
                } else {
                    switchPasscode.setChecked(true);
                }
            });
        }
    }

    @CheckedChange(R.id.switchBiomitrics)
    void onBiomitricsChecked(boolean isChecked) {
        if (getActivity() == null) return;
        if (isChecked == ExchangeApplication.getApp().getPreferences().isFingerprintEnabled()) return;
        if (!FingerprintDialog.isAvailable(getContext())) {
            showAlert("Fingerprint is not available");
            ExchangeApplication.getApp().getPreferences().setFingerprintEnabled(false);
            switchBiomitrics.setChecked(false);
            return;
        }
        if (isChecked) {
            ExchangeApplication.getApp().getPreferences().setFingerprintEnabled(true);
        } else {
            ((BaseActivity)getActivity()).showFingerprintDialog(Constants.SecurityType.SETTING, isSuccess -> {
                if (isSuccess) {
                    ExchangeApplication.getApp().getPreferences().setFingerprintEnabled(true);
                } else {
                    switchBiomitrics.setChecked(true);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<Wallet> data) {
        updateView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTickersUpdated(HashMap<String, ExchangeData> data) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {}
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResponse(BaseResponse response) {

    }

    @Override
    public void onFailure(String message) {

    }
}
