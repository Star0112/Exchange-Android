package com.urgentrn.urncexchange.ui;

import android.content.Intent;
import android.content.IntentSender;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.scottyab.rootbeer.RootBeer;
import com.urgentrn.urncexchange.BuildConfig;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppConfig;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetApiResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.signup.PINCreateActivity_;
import com.urgentrn.urncexchange.ui.signup.TouchIDActivity_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity implements ApiCallback {

    private int step;

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
        step = 1;

//        if (ExchangeApplication.getApp().getToken() != null) {
//            ApiClient.getInterface().getUser().enqueue(new AppCallback<>(this));
//        } else {
//            Utils.transferActivity(this, HomeActivity_.class);
//        }
    }

    private void onNextStep() {
        final User user = ExchangeApplication.getApp().getUser();
        step = 2;

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
        if (isFinishing()) return;
        if (response instanceof GetApiResponse) {
            final GetApiResponse data = (GetApiResponse)response;
            Constants.API_URL = (BuildConfig.PRODUCTION && data.isProduction() ? data.getProduction() : data.getSandbox()) + "/api/";
            Constants.COUNTRY_NAME = data.getCountry();
            ApiClient.setInterface(null);

        }  else if (response instanceof GetUserResponse) {
            ExchangeApplication.getApp().setUser(((GetUserResponse)response).getData());
            onNextStep();
        } else { // from verify email api
            onNextStep();
        }
    }

    @Override
    public void onFailure(String message) {
        if (isFinishing()) return;
        if (message != null && message.contains("location")) {
            final Intent intent = new Intent(this, HomeActivity_.class);
            intent.putExtra("message", message);
            startActivity(intent);
            finish();
            return;
        }
        if (step == 1) {
            if (BuildConfig.DEBUG) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            if (ExchangeApplication.getApp().getConfig() == null) {
                showAlert(message != null && message.equals(getString(R.string.no_internet_connection)) ? message : "Server Error", ((dialog, which) -> finish()));
                return;
            }

            ExchangeApplication.getApp().setToken(null, true);
            ExchangeApplication.getApp().getPreferences().clear();
        }

        onUserStep();
    }
}
