package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;
import android.view.View;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.ui.MainActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_touch_id)
public class TouchIDActivity extends BaseActivity {

    @AfterViews
    protected void init() {
        setToolBar(true);
    }

    public void onEnable(View v) {
        showFingerprintDialog(Constants.SecurityType.SETTING, isSuccess -> {
            if (isSuccess) {
                ExchangeApplication.getApp().getPreferences().setFingerprintEnabled(true);
                onNext(null);
            }
        });
    }

    public void onNext(View v) {
        if (v != null) {
            ExchangeApplication.getApp().getPreferences().setFingerprintEnabled(false);
        }
        final Intent intent;
//        if (ExchangeApplication.getApp().getUser().termsAccepted()) {
            intent = new Intent(this, MainActivity_.class);
            startActivity(intent);
        finish();
//        } else {
//            intent = new Intent(this, TermsActivity_.class);
//            startActivity(intent);
//        }
    }

    @Override
    public void onBackPressed() {
        if (Utils.isFromSplash(getIntent())) {
            Intent intent = new Intent(this, PINCreateActivity_.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }
}
