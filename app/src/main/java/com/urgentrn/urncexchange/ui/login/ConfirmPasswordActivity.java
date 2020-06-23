package com.urgentrn.urncexchange.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.PasswordConfirmRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
//import com.urgentrn.urncexchange.ui.VerifySuccessActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup_password)
public class ConfirmPasswordActivity extends BaseActivity implements ApiCallback {

    @ViewById
    TextView txtTitle, txtLabel;

    @ViewById
    EditText editText;

    @ViewById
    ImageView imgEye;

    private String username, password;
    private boolean isPasswordVisible;

    @AfterViews
    protected void init() {
        setToolBar(false);
        password = getIntent().getStringExtra("password");

        txtTitle.setText(R.string.forgot_password);
        txtLabel.setText(R.string.confirm_new_password_input_label);
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
        final String password = editText.getText().toString();
        if (password.isEmpty()) {
            editText.setError(getString(R.string.error_password_empty));
        } else if (!Utils.isPasswordValid(password)) {
            editText.setError(getString(R.string.error_password_short));
        } else if (!password.equals(this.password)) {
            editText.setError(getString(R.string.error_password_not_match));
        } else {
            username = getIntent().getStringExtra("username");

            final PasswordConfirmRequest request = new PasswordConfirmRequest();
            request.setUsername(username);
            request.setPassword(password);
            request.setCode(getIntent().getStringExtra("code"));
            ApiClient.getInterface()
                    .forgotPasswordConfirm(request)
                    .enqueue(new AppCallback<>(this, this));
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
//        final Intent intent = new Intent(this, VerifySuccessActivity_.class);
//        intent.putExtra("type", Constants.VerifyType.PASSWORD);
//        intent.putExtra("username", username);
//        intent.putExtra("password", password);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onFailure(String message) {

    }
}
