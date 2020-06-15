package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.ReferralCodeRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup_referral)
public class ReferralActivity extends BaseActivity implements ApiCallback {

    @ViewById
    EditText editText;

    private String referralCode;

    @AfterViews
    protected void init() {
        setToolBar(false);
        editText.requestFocus();
    }

    @EditorAction(R.id.editText)
    void onEditorActions(TextView v, int actionId) {
        onNext(null);
    }

    public void onNext(View v) {
        referralCode = editText.getText().toString().trim();
        if (referralCode.isEmpty()) {
            editText.setError(getString(R.string.error_code_empty));
        } else {
            ApiClient.getInterface()
                    .referralCodeConfirm(new ReferralCodeRequest(referralCode))
                    .enqueue(new AppCallback<>(this, this));
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        final Intent intent = new Intent(this, Constants.USE_COMBINED_SIGNUP ? SignupActivity_.class : UsernameActivity_.class);
        intent.putExtra("referralCode", referralCode);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(String message) {

    }
}
