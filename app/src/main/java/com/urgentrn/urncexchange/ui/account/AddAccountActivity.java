package com.urgentrn.urncexchange.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.PlaidApi;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.PlaidApiResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.dialogs.VerifyDialog;
import com.urgentrn.urncexchange.ui.dialogs.VerifyDialog_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_add_account)
public class AddAccountActivity extends BaseActivity implements ApiCallback {

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);

        if (AppData.getInstance().getFlowData() == null) {
            onBackPressed();
        } else if (AppData.getInstance().getFlowData().getMessage() != null) {
            showAlert(AppData.getInstance().getFlowData().getMessage(), (dialog, which) -> onBackPressed());
        }
    }

    public void onBankAccount(View v) {
        final User user = ExchangeApplication.getApp().getUser();
        if (user == null) return; // TODO: when does this happen?
        if (true){//user.getTierLevel() < 2 || (user.getTierLevel() == 2 && user.isTierPending())) {
            final VerifyDialog verifyDialog = new VerifyDialog_();
            final Bundle args = new Bundle();
            args.putString("title", getString(R.string.add_bank_account));
            verifyDialog.setArguments(args);
            verifyDialog.show(getSupportFragmentManager(), "VERIFY");
        } else {
            final String flow = AppData.getInstance().getFlowData().getFlow();
            if (flow.equals("plaid")) {
                if (AppData.getInstance().getPlaidApi() == null) {
                    if (ExchangeApplication.getApp().getConfig() == null) return;
                    final String url = ExchangeApplication.getApp().getConfig().getIntegrations().get("plaid");
                    ApiClient.getInterface().getPlaidApi(url).enqueue(new AppCallback<>(this, true, this));
                } else {
                    handlePlaidFlow();
                }
            } else if (flow.equals("stripes")) {
                showAlert(R.string.error_bank);
            } else if (flow.equals("default")) {
                showAlert("Coming soon!");
            } else {
                showAlert("You have no payment method to add");
            }
        }
    }

    private void handlePlaidFlow() {
        final User user = ExchangeApplication.getApp().getUser();
        if (user == null) return;
        if (AppData.getInstance().getPlaidApi().getCountryCodes().contains(user.getCountry().toUpperCase())) {
            showPlaidView();
        } else {
            final Intent intent = new Intent(this, ConnectBankActivity_.class);
            startActivity(intent);
        }
    }

    private void showPlaidView() {
        final PlaidApi plaidApi = AppData.getInstance().getPlaidApi();
        final Intent intent = new Intent(this, PlaidActivity_.class);
        intent.putExtra("data", plaidApi);
        startActivity(intent);
    }

    public void onDebitCard(View v) {
        final User user = ExchangeApplication.getApp().getUser();
        if (user == null) return;
        if (true){//user.getTierLevel() < 2 || (user.getTierLevel() == 2 && user.isTierPending())) {
            final VerifyDialog verifyDialog = new VerifyDialog_();
            final Bundle args = new Bundle();
            args.putString("title", getString(R.string.add_bank_account));
            verifyDialog.setArguments(args);
            verifyDialog.show(getSupportFragmentManager(), "VERIFY");
        } else {
            if (!AppData.getInstance().getFlowData().getFlow().equals("stripes")) {
                showAlert(R.string.error_debit_card);
                return;
            }
            final Intent intent = new Intent(this, AddCardActivity_.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof PlaidApiResponse) {
            AppData.getInstance().setPlaidApi(((PlaidApiResponse)response).getData());
            handlePlaidFlow();
        }
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
    }
}
