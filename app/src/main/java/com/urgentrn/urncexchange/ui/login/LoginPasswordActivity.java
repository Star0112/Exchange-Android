package com.urgentrn.urncexchange.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.LoginRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.LoginResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.signup.EmailVerificationActivity_;
import com.urgentrn.urncexchange.ui.signup.PINCreateActivity_;
import com.urgentrn.urncexchange.ui.signup.PhoneActivity_;
import com.urgentrn.urncexchange.ui.signup.PhoneVerificationActivity_;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login_password)
public class LoginPasswordActivity extends BaseActivity implements ApiCallback {

    @ViewById
    EditText editText;

    @ViewById
    ImageView imgEye;

    @ViewById
    TextView txtForgotPassword;

    private String username;
    private boolean isPasswordVisible;

    @AfterViews
    protected void init() {
        setToolBar(false);
        editText.requestFocus();

        username = getIntent().getStringExtra("username");
        txtForgotPassword.setText(String.format("%s?", getString(R.string.forgot_password)));
    }

    @EditorAction(R.id.editText)
    void onEditorActions(TextView v, int actionId) {
        onNext(null);
    }

    public void onShowPassword(View v) {
        isPasswordVisible = !isPasswordVisible;
        editText.setInputType(editText.getInputType() ^ 16);
        imgEye.setImageResource(isPasswordVisible ? R.mipmap.ic_eye : R.mipmap.ic_eye_off);
    }

    public void onForgot(View v) {
        final Intent intent = new Intent(this, ForgotPasswordActivity_.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onNext(View v) {
        String password;
        password = editText.getText().toString();
        if (password.isEmpty()) {
            editText.setError(getString(R.string.error_password_empty));
        } else if (!Utils.isPasswordValid(password)) {
            editText.setError(getString(R.string.error_password_short));
        } else {
            ApiClient.getInterface()
                    .login(new LoginRequest(username, password))
                    .enqueue(new AppCallback<>(this, this));
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof LoginResponse) {
            final LoginResponse data = (LoginResponse)response;

            Intent intent;
            final boolean remember = getIntent().getBooleanExtra("remember", false);
            if (data.hasTwoFactorAuth()) {
                intent = new Intent(this, TfaActivity_.class);
                intent.putExtra("username", data.getTwoFactorData().getUserId());
                intent.putExtra("type", data.getTwoFactorData().getType());
                intent.putExtra("session", data.getSession());
                intent.putExtra("remember", remember);
            } else {
                ExchangeApplication.getApp().getPreferences().clear();
                ExchangeApplication.getApp().getPreferences().setRefreshToken(data.getRefreshToken());
                ExchangeApplication.getApp().getPreferences().setUsername(remember ? data.getUser().getUsername() : null);
                ExchangeApplication.getApp().setToken(data.getAccessToken(), true);
                ExchangeApplication.getApp().setUser(data.getUser());

                if (data.getUser() == null) {
                    intent = new Intent(this, EmailVerificationActivity_.class);
                    intent.putExtra("username", username);
                } else if (data.getUser().isPhoneVerified()) {
                    intent = new Intent(this, PINCreateActivity_.class);
                } else if (data.getUser().getPhone() != null) {
                    intent = new Intent(this, PhoneVerificationActivity_.class);
                } else if (data.getUser().isEmailVerified()) {
                    intent = new Intent(this, PhoneActivity_.class);
                } else {
                    intent = new Intent(this, EmailVerificationActivity_.class);
                }
            }
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
