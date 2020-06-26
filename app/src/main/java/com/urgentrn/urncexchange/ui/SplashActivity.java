package com.urgentrn.urncexchange.ui;

import android.content.Intent;
import android.content.IntentSender;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.scottyab.rootbeer.RootBeer;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.signup.PINCreateActivity_;
import com.urgentrn.urncexchange.ui.signup.TouchIDActivity_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity implements ApiCallback {

    @AfterViews
    protected void init() {
        if (new RootBeer(getApplicationContext()).isRooted()) {
            showAlert(getString(R.string.warning_rooted_device), ((dialog, which) -> finish()));
            return;
        }

        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        final Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, 0);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        if (ExchangeApplication.getApp().getUser() != null) {
            onNextStep();
        } else if (ExchangeApplication.getApp().getConfig() != null) {
            onUserStep();
        } else {
            ApiClient.getInterface();
            onUserStep();
        }
    }

    private void onUserStep() {
        if (ExchangeApplication.getApp().getToken() != null) {
            ApiClient.getInterface().getUser().enqueue(new AppCallback<>(this));
        } else {
            Utils.transferActivity(this, HomeActivity_.class);
        }
    }

    private void onNextStep() {
        Intent intent;
        if (ExchangeApplication.getApp().getPreferences().getPasscode() != null) {
            if (ExchangeApplication.getApp().getPreferences().isFingerprintEnabled()) {
                showPassDialog(Constants.SecurityType.DEFAULT, isSuccess -> {
                    if (isSuccess) {
                        startActivity(new Intent(this, MainActivity_.class));
                        finish();
                    } else {
                        ExchangeApplication.getApp().logout(this);
                    }
                });
                return;
            } else {
                intent = new Intent(this, TouchIDActivity_.class);
            }
        } else {
            intent = new Intent(this, PINCreateActivity_.class);
        }
        intent.putExtra("from_splash", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResponse(BaseResponse response) {

    }

    @Override
    public void onFailure(String message) {

    }
}
