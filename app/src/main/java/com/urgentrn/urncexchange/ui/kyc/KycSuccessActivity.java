package com.urgentrn.urncexchange.ui.kyc;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetAvailableCardsResponse;
import com.urgentrn.urncexchange.ui.MainActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_kyc_success)
public class KycSuccessActivity extends BaseActivity implements ApiCallback {

    @ViewById
    TextView txtTitle, txtMessage, btnContinue;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.no_animation);

        txtTitle.setText(R.string.kyc_success_title);
        txtMessage.setText(R.string.kyc_success_message);
        btnContinue.setText(R.string.kyc_continue);

        ApiClient.getInterface().getAvailableCards().enqueue(new AppCallback<>(this));
    }

    public void onContinue(View v) {
        final Intent intent = new Intent(this, ScanActivity_.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_bottom);
    }

    public void onSkip(View v) {
        final Intent intent = new Intent(this, MainActivity_.class);
        intent.putExtra("verify_type", Constants.VerifyType.TIER1);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_bottom);
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetAvailableCardsResponse) {
            AppData.getInstance().setAvailableCards(((GetAvailableCardsResponse)response).getData());
        }
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onBackPressed() {

    }
}
