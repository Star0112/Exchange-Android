package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.fxn769.Numpad;
import com.mukesh.OtpView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.request.CodeRequest;
import com.urgentrn.urncexchange.models.request.UpdateUserRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
//import com.urgentrn.urncexchange.ui.VerifySuccessActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup_phone_verification)
public class PhoneVerificationActivity extends BaseActivity implements ApiCallback {

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

    public void onResend(View v) {
        final User user = ExchangeApplication.getApp().getUser();
        if (user == null) return;
        UpdateUserRequest request = new UpdateUserRequest();
        request.setCountry(user.getCountry()); // TODO: confirm if country should be changed as well
//        request.setPhone(user.getPhone());
        ApiClient.getInterface().updateUser(request).enqueue(new AppCallback<>(this, this));
    }

    public void onSubmit(View v) {
        final String code = otpView.getText().toString();
        if (code.length() != Constants.PHONE_VERIFICATION_CODE_LENGTH) return;
        CodeRequest request = new CodeRequest();
        request.setCode(code);
        ApiClient.getInterface().verifyPhone(request).enqueue(new AppCallback<>(this, this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetUserResponse) {
            final User user = ((GetUserResponse)response).getData();
            if (user != null) ExchangeApplication.getApp().setUser(user);

            Toast.makeText(this, R.string.resend_code_success, Toast.LENGTH_SHORT).show();
        } else {
            final User user = ExchangeApplication.getApp().getUser();
            if (user == null) return;
//            user.setPhoneVerified(true);
            ExchangeApplication.getApp().getPreferences().setUsername(user.getUsername());

//            Intent intent = new Intent(this, VerifySuccessActivity_.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("type", Constants.VerifyType.PHONE);
//            intent.putExtra("is_changing", getIntent().getBooleanExtra("is_changing", false));
//            startActivity(intent);
//            finish();
        }
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PhoneActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from_splash", Utils.isFromSplash(getIntent()));
        startActivity(intent);
        finish();
    }
}
