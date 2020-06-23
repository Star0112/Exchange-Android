package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.fxn769.Numpad;
import com.hbb20.CountryCodePicker;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.CountryData;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.request.UpdateUserRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetCountriesResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.ui.HomeActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.login.LoginActivity_;
import com.urgentrn.urncexchange.ui.login.LoginUsernameActivity_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_signup_phone)
public class PhoneActivity extends BaseActivity implements ApiCallback {

    @ViewById
    CountryCodePicker ccp;

    @ViewById
    EditText editText;

    @ViewById
    Numpad padView;

    private boolean isChanging;
    private String phone;

    @AfterViews
    protected void init() {
        setToolBar(false);

        isChanging = getIntent().getBooleanExtra("is_changing", false);

        if (AppData.getInstance().getCountries().size() > 0) {
            setCountries();
        } else {
            ApiClient.getInterface().getCountries().enqueue(new AppCallback<>(this, this));
        }

        ccp.registerCarrierNumberEditText(editText);
        ccp.setTypeFace(ResourcesCompat.getFont(this, R.font.codec_pro_regular));

        final User user = ExchangeApplication.getApp().getUser();
//        if (false && user != null && user.getPhone() != null) { // disabling for now because of same country code for some countries
//            ccp.setFullNumber(user.getPhone());
//            return;
//        }
        if (false) {
            String countryCode = null;
            if (user != null) {
                if (user.getCountry() != null) {
                    countryCode = user.getCountry();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    countryCode = getResources().getConfiguration().getLocales().get(0).getCountry();
                } else {
                    countryCode = getResources().getConfiguration().locale.getCountry();
                }
            }
            ccp.setDefaultCountryUsingNameCode(countryCode == null ? "ca" : countryCode);
            ccp.resetToDefaultCountry();
        }

        padView.setTextLengthLimit(15);
        padView.setOnTextChangeListener((text, digits_remaining) -> editText.setText(text));
    }

    private void setCountries() {
        final List<String> codes = new ArrayList<>();
        for (CountryData country : AppData.getInstance().getCountries()) {
            if (!country.getCode().equalsIgnoreCase("us")) {
                codes.add(country.getCode());
                if (country.getName().contains(Constants.COUNTRY_NAME)) {
                    ccp.setCountryForNameCode(country.getCode());
                }
            }
        }
        ccp.setCustomMasterCountries(TextUtils.join(",", codes));
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

            UpdateUserRequest request = new UpdateUserRequest();
            request.setCountry(ccp.getSelectedCountryNameCode());
            request.setPhone(phone);
            ApiClient.getInterface().updateUser(request).enqueue(new AppCallback<>(this, this));
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetCountriesResponse) {
            final List<CountryData> data = ((GetCountriesResponse)response).getData();
            AppData.getInstance().setCountries(data);
            setCountries();
        } else if (response instanceof GetUserResponse) {
            final User user = ((GetUserResponse)response).getData();
            if (user != null) ExchangeApplication.getApp().setUser(user);

            final Intent intent = new Intent(this, PhoneVerificationActivity_.class);
            intent.putExtra("is_changing", isChanging);
            intent.putExtra("phone", phone);
            startActivity(intent);
        }
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        if (!isChanging && !Constants.DEFAULT_BACK_BEHAVIOR) {
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
