package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Utils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

@EActivity(R.layout.activity_signup_username)
public class UsernameActivity extends BaseActivity implements ApiCallback {

    @ViewById
    ScrollView scrollView;

    @ViewById
    EditText editText;

    @ViewById
    ImageView imgValidation;

    @ViewById
    View llValidation, progressValidation;

    @ViewById
    TextView txtValidation, btnNext;

    private boolean isValid, isValidating;

    @AfterViews
    protected void init() {
        setToolBar(false);
        editText.requestFocus();
        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            if (isOpen) {
                new Handler().postDelayed(() -> scrollView.fullScroll(View.FOCUS_DOWN), 50);
            }
        });
    }

    private void updateValidationView(String message) {
        if (Utils.isUsernameInvalid(editText.getText().toString().trim())) {
            isValid = false; // when clear text before getting api response
            llValidation.setVisibility(View.INVISIBLE);
        } else {
            llValidation.setVisibility(View.VISIBLE);
            progressValidation.setVisibility(isValidating ? View.VISIBLE : View.INVISIBLE);
            imgValidation.setVisibility(isValidating ? View.INVISIBLE : View.VISIBLE);
            imgValidation.setImageResource(isValid ? R.mipmap.ic_add : R.mipmap.ic_add);
            if (message != null) {
                txtValidation.setText(message);
            } else {
                txtValidation.setText(isValidating ? R.string.username_validating : isValid ? R.string.username_valid : R.string.username_invalid);
            }
        }
        btnNext.setEnabled(isValid);
    }

    @TextChange(R.id.editText)
    void onTextChange(CharSequence s) {
        final String username = s.toString().trim();
        isValid = false;
        if (Utils.isUsernameInvalid(username)) {
            isValidating = false;
        } else {
            isValidating = true;
            final HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            ApiClient.getInterface().validateUsername(params).enqueue(new AppCallback<>(this));
        }
        updateValidationView(null);
    }

    @EditorAction(R.id.editText)
    void onEditorActions(TextView v, int actionId) {
        if (isValid) onNext(null);
    }

    public void onNext(View v) {
        final String username = editText.getText().toString().trim();
        if (username.isEmpty()) {
            editText.setError(getString(R.string.error_username_empty));
        } else if (Utils.isUsernameInvalid(username)) {
            editText.setError(getString(R.string.error_username_short));
        } else {
            final Intent intent = new Intent(this, EmailActivity_.class);
            intent.putExtra("username", username);
            intent.putExtra("referralCode", getIntent().getStringExtra("referralCode"));
            startActivity(intent);
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        isValid = true;
        isValidating = false;
        updateValidationView(null);
    }

    @Override
    public void onFailure(String message) {
        isValid = false;
        isValidating = false;
        updateValidationView(message);
    }
}
