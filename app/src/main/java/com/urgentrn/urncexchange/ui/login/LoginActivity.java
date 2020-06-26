package com.urgentrn.urncexchange.ui.login;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.LoginRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.LoginResponse;
import com.urgentrn.urncexchange.ui.HomeActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.signup.PINCreateActivity_;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements ApiCallback {

    @ViewById
    EditText editEmail, editPassword;

    private String userName;

    @AfterViews
    protected void init() {
        editEmail.setText("fengliu93");
        editPassword.setText("Security123!@#");
        setToolBar(false);
    }

    @EditorAction(R.id.editPassword)
    void onEditorActions(TextView v, int actionId) {
        onLogin(null);
    }

    public void onLogin(View v) {
        userName = editEmail.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            editEmail.requestFocus();
            editEmail.setError(getString(R.string.error_name_empty));
        } else if (Utils.isUsernameInvalid(userName)) {
            editEmail.requestFocus();
            editEmail.setError(getString(R.string.error_name_invalid));
        } else if (password.isEmpty()) {
            editPassword.requestFocus();
            editPassword.setError(getString(R.string.error_password_empty));
        } else if (!Utils.isPasswordValid(password)) {
            editPassword.requestFocus();
            editPassword.setError(getString(R.string.error_password_short));
        } else {
            ApiClient.getInterface()
                    .login(new LoginRequest(userName, password))
                    .enqueue(new AppCallback<>(this, this));
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof LoginResponse) {
            final LoginResponse data = (LoginResponse)response;

            Log.v("login response", data.toString());
            Intent intent;
            ExchangeApplication.getApp().getPreferences().clear();
            ExchangeApplication.getApp().getPreferences().setRefreshToken(data.getRefreshToken());
            ExchangeApplication.getApp().setToken(data.getAccessToken(), true);
            ExchangeApplication.getApp().setUser(data.getUser());

            intent = new Intent(this, PINCreateActivity_.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
