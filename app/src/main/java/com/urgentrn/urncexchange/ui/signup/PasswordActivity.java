package com.urgentrn.urncexchange.ui.signup;

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
import com.urgentrn.urncexchange.models.request.SignupRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.LoginResponse;
import com.urgentrn.urncexchange.models.response.SignupResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup_password)
public class PasswordActivity extends BaseActivity implements ApiCallback {

    @ViewById
    EditText editText;

    @ViewById
    ImageView imgEye;

    private String username, password;
    private boolean isPasswordVisible;

    @AfterViews
    protected void init() {
        setToolBar(false);
        editText.requestFocus();
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

    public void onNext(View v) {
        password = editText.getText().toString();
        if (password.isEmpty()) {
            editText.setError(getString(R.string.error_password_empty));
        } else if (!Utils.isPasswordValid(password)) {
            editText.setError(getString(R.string.error_password_short));
        } else {
            username = getIntent().getStringExtra("username");

            if (username == null) { // US sign up
                // TODO
                return;
            }

            final SignupRequest request = new SignupRequest();
            request.setUsername(username);
            request.setPassword(password);
            request.setEmail(getIntent().getStringExtra("email"));
            request.setReferralCode(getIntent().getStringExtra("referralCode"));

            ApiClient.getInterface()
                    .register(request)
                    .enqueue(new AppCallback<>(this, this));
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof SignupResponse) {
            ApiClient.getInterface()
                    .login(new LoginRequest(username, password))
                    .enqueue(new AppCallback<>(this, this));
        } else if (response instanceof LoginResponse) {
            final LoginResponse data = (LoginResponse)response;
            ExchangeApplication.getApp().getPreferences().setRefreshToken(data.getRefreshToken());
            ExchangeApplication.getApp().getPreferences().setUsername(data.getUser().getUsername());
            ExchangeApplication.getApp().setToken(data.getAccessToken(), true);
            ExchangeApplication.getApp().setUser(data.getUser());

            final Intent intent = new Intent(this, EmailVerificationActivity_.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
