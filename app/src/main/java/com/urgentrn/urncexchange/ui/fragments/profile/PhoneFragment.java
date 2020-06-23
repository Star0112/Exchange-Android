package com.urgentrn.urncexchange.ui.fragments.profile;

import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.signup.PhoneActivity_;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_phone)
public class PhoneFragment extends BaseFragment {

    @ViewById
    TextView txtPhone;

    @AfterViews
    protected void init() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);
    }

    @Override
    public void onResume() {
        super.onResume();

//        final String phone = ExchangeApplication.getApp().getUser().getPhone();
//        if (phone != null) {
//            final String formattedPhone = PhoneNumberUtils.formatNumber(phone, "US");
//            txtPhone.setText(Utils.maskedPhoneNumber(formattedPhone));
//        }
    }

    @Click(R.id.llChange)
    void onChange() {
        final Intent intent = new Intent(getContext(), PhoneActivity_.class);
        intent.putExtra("is_changing", true);
        startActivity(intent);
    }
}
