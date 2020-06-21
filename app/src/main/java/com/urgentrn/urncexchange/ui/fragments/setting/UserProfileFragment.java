package com.urgentrn.urncexchange.ui.fragments.setting;

import android.app.Activity;
import android.content.Intent;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.fragment_user_profile)
public class UserProfileFragment extends BaseFragment implements ApiCallback {

    @AfterViews
    protected void init() {
        initView();
        updateView();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);
    }

    @Override
    public void updateView() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<Wallet> data) {
        updateView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTickersUpdated(HashMap<String, ExchangeData> data) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {}
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResponse(BaseResponse response) {

    }

    @Override
    public void onFailure(String message) {

    }
}