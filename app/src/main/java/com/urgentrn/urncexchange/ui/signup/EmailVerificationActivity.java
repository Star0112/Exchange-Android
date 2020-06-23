package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.request.UserRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.ui.HomeActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.login.LoginActivity_;
import com.urgentrn.urncexchange.ui.login.LoginUsernameActivity_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup_email_verification)
public class EmailVerificationActivity extends BaseActivity implements ApiCallback {

    @ViewById
    TextView txtEmail;

    private String username;

    private boolean isFirstTime;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getUser();
            handler.postDelayed(this, Constants.API_REQUEST_INTERVAL_SHORT);
        }
    };

    @AfterViews
    protected void init() {
        setToolBar(true);

        if (ExchangeApplication.getApp().getUser() == null) return;
        txtEmail.setText(ExchangeApplication.getApp().getUser().getEmail());
        username = ExchangeApplication.getApp().getUser().getUsername();

        isFirstTime = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        handler.postDelayed(runnable, isFirstTime ? Constants.API_REQUEST_INTERVAL_SHORT : 3);
        isFirstTime = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        handler.removeCallbacks(runnable);
    }

    private void getUser() {
        ApiClient.getInterface().getUser().enqueue(new AppCallback<>(EmailVerificationActivity.this));
    }

    public void onOpenEmail(View v) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        startActivity(Intent.createChooser(intent, "Select the mailbox"));
    }

    public void onResend(View v) {
        if (username == null) {
            username = getIntent().getStringExtra("username");
            if (username == null) return;
        }
        final UserRequest request = new UserRequest();
        request.setUsername(username);
        ApiClient.getInterface().resend(request).enqueue(new AppCallback<>(this, this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetUserResponse) {
            final User user = ((GetUserResponse)response).getData();
//            if (user.isEmailVerified()) {
                ExchangeApplication.getApp().setUser(user, false);
                startActivity(new Intent(this, PhoneActivity_.class));
                finish();
//            }
        } else {
            Toast.makeText(this, R.string.resend_email_success, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        if (!Constants.DEFAULT_BACK_BEHAVIOR) {
            Intent intent = new Intent(this, HomeActivity_.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (Utils.isFromSplash(getIntent())) {
            Intent intent = new Intent(this, Constants.USE_COMBINED_LOGIN ? LoginActivity_.class : LoginUsernameActivity_.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }
}
