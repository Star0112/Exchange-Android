package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

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
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup)
public class SignupActivity extends BaseActivity implements ApiCallback {

    @ViewById
    EditText editUsername, editFullname, editEmail, editPassword, editPasswordConfirm, editPhone;

    private String email, password;

    @AfterViews
    protected void init() {
    }

    public void onSignUp(View v) {
        final String username = editUsername.getText().toString().trim();
        final String fullname = editFullname.getText().toString().trim();
        email = editEmail.getText().toString().trim();
        password = editPassword.getText().toString();
        final String passwordConfirm = editPasswordConfirm.getText().toString();
        final String phone = editPhone.getText().toString();
        if (username.isEmpty()) {
            editUsername.requestFocus();
            editUsername.setError(getString(R.string.error_username_empty));
        } else if (fullname.isEmpty()) {
            editFullname.requestFocus();
            editFullname.setError(getString(R.string.error_fullname_empty));
        } else if (email.isEmpty()) {
            editEmail.requestFocus();
            editEmail.setError(getString(R.string.error_name_empty));
        } else if (!Utils.isEmailValid(email)) {
            editEmail.requestFocus();
            editEmail.setError(getString(R.string.error_name_invalid));
        } else if (password.isEmpty()) {
            editPassword.requestFocus();
            editPassword.setError(getString(R.string.error_password_empty));
        } else if (!Utils.isPasswordValid(password)) {
            editPassword.requestFocus();
            editPassword.setError(getString(R.string.error_password_short));
        } else if (!passwordConfirm.equals(password)) {
            editPasswordConfirm.requestFocus();
            editPasswordConfirm.setError(getString(R.string.error_password_not_match));
        } else if (email.isEmpty()) {
            editPhone.requestFocus();
            editPhone.setError(getString(R.string.error_phone_empty));
        } else {
            final SignupRequest request = new SignupRequest();
            request.setUsername(username);
            request.setUsername(fullname);
            request.setEmail(email);
            request.setPassword(password);
            request.setEmail(phone);

            ApiClient.getInterface()
                    .register(request)
                    .enqueue(new AppCallback<>(this, this));
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof SignupResponse) {
            ApiClient.getInterface()
                    .login(new LoginRequest(email, password))
                    .enqueue(new AppCallback<>(this, this));
        } else if (response instanceof LoginResponse) {
            final LoginResponse data = (LoginResponse)response;
            Log.v("Signup Process", data.toString());
//            ExchangeApplication.getApp().getPreferences().setRefreshToken(data.getRefreshToken());
//            ExchangeApplication.getApp().setToken(data.getAccessToken(), true);
//            ExchangeApplication.getApp().setUser(data.getUser());

            final Intent intent = new Intent(this, PINCreateActivity_.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
