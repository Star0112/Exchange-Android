package com.urgentrn.urncexchange.ui;

import android.content.Intent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.adapter.TutorialPagerAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.login.LoginActivity_;
import com.urgentrn.urncexchange.ui.login.LoginUsernameActivity_;
import com.urgentrn.urncexchange.ui.signup.ReferralActivity_;
import com.urgentrn.urncexchange.ui.signup.SignupActivity_;
import com.urgentrn.urncexchange.ui.signup.UsernameActivity_;
import com.urgentrn.urncexchange.ui.view.CircleIndicator;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_onboarding)
public class OnboardingActivity extends BaseActivity {

    @ViewById
    ViewPager viewPager;

    @ViewById
    CircleIndicator indicator;

    @AfterViews
    protected void init() {
        viewPager.setAdapter(new TutorialPagerAdapter());
        indicator.setViewPager(viewPager);
    }

    public void onReferral(View v) {
        transferActivity(ReferralActivity_.class);
    }

    public void onCreateAccount(View v) {
        transferActivity(Constants.USE_COMBINED_SIGNUP ? SignupActivity_.class : UsernameActivity_.class);
    }

    public void onSignIn(View v) {
        transferActivity(Constants.USE_COMBINED_LOGIN ? LoginActivity_.class : LoginUsernameActivity_.class);
    }

    private void transferActivity(Class<?> cls) {
        final Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
