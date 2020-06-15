package com.urgentrn.urncexchange.ui;

import android.content.Intent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.adapter.TutorialPagerAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.login.LoginActivity_;
import com.urgentrn.urncexchange.ui.login.LoginUsernameActivity_;
import com.urgentrn.urncexchange.ui.signup.EmailActivity_;
import com.urgentrn.urncexchange.ui.signup.ReferralActivity_;
import com.urgentrn.urncexchange.ui.signup.SignupActivity_;
import com.urgentrn.urncexchange.ui.signup.UsernameActivity_;
import com.urgentrn.urncexchange.ui.view.CircleIndicator;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(Constants.USE_ONBOARDING ? R.layout.activity_onboarding : R.layout.activity_home)
public class HomeActivity extends BaseActivity {

    @ViewById
    ViewPager viewPager;

    @ViewById
    CircleIndicator indicator;

    @AfterViews
    protected void init() {
        if (Constants.USE_ONBOARDING) {
            viewPager.setAdapter(new TutorialPagerAdapter());
            indicator.setViewPager(viewPager);
        }
    }

    public void onLogin(View v) {
        if (!checkAvailability()) return;
        transferActivity(Constants.USE_COMBINED_LOGIN ? LoginActivity_.class : LoginUsernameActivity_.class);
    }

    public void onSignUp(View v) {
        if (!checkAvailability()) return;
        if (Constants.ENABLE_US_SIGN_UP) {
            transferActivity(EmailActivity_.class);
            return;
        }
        transferActivity(Constants.USE_COMBINED_SIGNUP ? SignupActivity_.class : UsernameActivity_.class);
    }

    private boolean checkAvailability() {
        final String message = getIntent().getStringExtra("message");
        if (message != null) {
            showAlert(message);
            return false;
        }
        return true;
    }

    private void transferActivity(Class<?> cls) {
        final Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
