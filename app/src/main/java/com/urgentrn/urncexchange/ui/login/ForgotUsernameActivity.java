package com.urgentrn.urncexchange.ui.login;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fxn769.Numpad;
import com.hbb20.CountryCodePicker;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.PhoneRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup_phone)
public class ForgotUsernameActivity extends BaseActivity implements ApiCallback {

    @ViewById
    CountryCodePicker ccp;

    @ViewById
    EditText editText;

    @ViewById
    Numpad padView;

    private String phone;

    @AfterViews
    protected void init() {
        setToolBar(false);

        ccp.registerCarrierNumberEditText(editText);

        if (false) {
            String countryCode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                countryCode = getResources().getConfiguration().getLocales().get(0).getCountry();
            } else {
                countryCode = getResources().getConfiguration().locale.getCountry();
            }
            ccp.setDefaultCountryUsingNameCode(countryCode == null ? "ca" : countryCode);
            ccp.resetToDefaultCountry();
        }

        padView.setTextLengthLimit(15);
        padView.setOnTextChangeListener((text, digits_remaining) -> editText.setText(text));
    }

    @EditorAction(R.id.editText)
    void onEditorActions(TextView v, int actionId) {
        onNext(null);
    }

    public void onNext(View v) {
        if (!ccp.isValidFullNumber()) {
            editText.setError(getString(R.string.error_phone_invalid));
        } else {
            phone = ccp.getFullNumberWithPlus();
            ApiClient.getInterface()
                    .forgotUsername(new PhoneRequest(phone))
                    .enqueue(new AppCallback<>(this, this));
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        final Intent intent = new Intent(this, ForgotUsernameConfirmActivity_.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }

    @Override
    public void onFailure(String message) {

    }
}
