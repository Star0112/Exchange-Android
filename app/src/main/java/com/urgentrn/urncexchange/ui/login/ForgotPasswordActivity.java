package com.urgentrn.urncexchange.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.UserRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.ForgotPasswordResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_forgot_password)
public class ForgotPasswordActivity extends BaseActivity implements ApiCallback {

    @ViewById
    EditText editEmail;

    private String username;

    @AfterViews
    protected void init() {
        setToolBar(false);
        editEmail.requestFocus();

        editEmail.setText(getIntent().getStringExtra("username"));
    }

    @EditorAction(R.id.editText)
    void onEditorActions(TextView v, int actionId) {
        onNext(null);
    }

    public void onNext(View v) {
        final String email = editEmail.getText().toString().trim();
        if (email.isEmpty()) {
            editEmail.requestFocus();
            editEmail.setError(getString(R.string.error_email_empty));
        } else if (!Utils.isEmailValid(email)) {
            editEmail.requestFocus();
            editEmail.setError(getString(R.string.error_email_invalid));
        } else {
            final UserRequest request = new UserRequest();
            request.setUsername(username);
            ApiClient.getInterface()
                    .forgotPassword(request)
                    .enqueue(new AppCallback<>(this, this));
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof ForgotPasswordResponse) {
            final String method = ((ForgotPasswordResponse)response).getCodeDeliveryMethod();
            if (method.equals("SMS")) {
                final Intent intent = new Intent(this, ForgotPasswordConfirmActivity_.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                // TODO: handle email confirmation
            }
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
