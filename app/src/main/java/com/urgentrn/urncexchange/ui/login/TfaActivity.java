package com.urgentrn.urncexchange.ui.login;

import android.content.Intent;
import android.view.View;

import com.fxn769.Numpad;
import com.mukesh.OtpView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.TfaRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.LoginResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.signup.PINCreateActivity_;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login_2fa)
public class TfaActivity extends BaseActivity implements ApiCallback {

    @ViewById
    OtpView otpView;

    @ViewById
    Numpad padView;

    @AfterViews
    protected void init() {
        setToolBar(false);

        otpView.setItemCount(Constants.PHONE_VERIFICATION_CODE_LENGTH);
        otpView.setOtpCompletionListener(otp -> onSubmit(null));

        padView.setOnTextChangeListener((text, digits_remaining) -> otpView.setText(text));
    }

    public void onSubmit(View v) {
        final String code = otpView.getText().toString();
        if (code.length() != Constants.PHONE_VERIFICATION_CODE_LENGTH) return;

        final TfaRequest request = new TfaRequest();
        request.setCode(code);
        request.setUsername(getIntent().getStringExtra("username"));
        request.setSession(getIntent().getStringExtra("session"));
        request.setType(getIntent().getStringExtra("type"));
        ApiClient.getInterface().login(request).enqueue(new AppCallback<>(this, this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof LoginResponse) {
            final LoginResponse data = (LoginResponse)response;

            final boolean remember = getIntent().getBooleanExtra("remember", false);
            ExchangeApplication.getApp().getPreferences().clear();
            ExchangeApplication.getApp().getPreferences().setRefreshToken(data.getRefreshToken());
            ExchangeApplication.getApp().getPreferences().setUsername(remember ? data.getUser().getUsername() : null);
            ExchangeApplication.getApp().setToken(data.getAccessToken(), true);
            ExchangeApplication.getApp().setUser(data.getUser());

            // Assume phone number has been verified
            final Intent intent = new Intent(this, PINCreateActivity_.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
