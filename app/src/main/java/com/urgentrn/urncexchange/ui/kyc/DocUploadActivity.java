package com.urgentrn.urncexchange.ui.kyc;

import android.content.Intent;
import android.view.View;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_doc_upload)
public class DocUploadActivity extends BaseActivity implements ApiCallback {

    @ViewById
    View llTier1;

    @AfterViews
    protected void init() {
        setToolBar(true);

        if (ExchangeApplication.getApp().getUser().getTierLevel() >= 1) {
            llTier1.setVisibility(View.GONE);
        }
//        if (AppData.getInstance().getCountries().size() == 0) {
//            ApiClient.getInterface().getCountries().enqueue(new AppCallback<>(this));
//        }
    }

    public void onStart(View v) {
//        if (AppData.getInstance().getCountries().size() == 0) return;

        final Intent intent;
        final User user = ExchangeApplication.getApp().getUser();
        if (user == null) return;
        if (user.getTierLevel() == 0) {
            intent = new Intent(this, VerifyAccountActivity_.class);
        } else {
            intent = new Intent(this, ScanActivity_.class);
        }
        startActivity(intent);
    }

    @Override
    public void onResponse(BaseResponse response) {
//        if (response instanceof GetCountriesResponse) {
//            final List<CountryData> data = ((GetCountriesResponse)response).getData();
//            AppData.getInstance().setCountries(data);
//        }
    }

    @Override
    public void onFailure(String message) {

    }
}
