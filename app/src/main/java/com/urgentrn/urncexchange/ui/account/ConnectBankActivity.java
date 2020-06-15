package com.urgentrn.urncexchange.ui.account;

import android.content.Intent;
import android.view.View;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_connect_bank)
public class ConnectBankActivity extends BaseActivity {

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);
    }

    public void onNext(View v) {
        final Intent intent = new Intent(this, ConnectAccountActivity_.class);
        startActivity(intent);
    }

    public void onSkip(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
    }
}
