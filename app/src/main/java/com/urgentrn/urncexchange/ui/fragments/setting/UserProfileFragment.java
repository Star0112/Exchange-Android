package com.urgentrn.urncexchange.ui.fragments.setting;

import android.app.Activity;
import android.content.Intent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.android.gms.common.api.Api;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.request.ProfileUpdateRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.LoginResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

@EFragment(R.layout.fragment_user_profile)
public class UserProfileFragment extends BaseFragment implements ApiCallback {

    @ViewById
    Toolbar toolbar;

    @ViewById
    EditText userName, email, firstName, lastName, phoneNumber;

    private User user;

    @AfterViews
    protected void init() {
        setToolBar(true);
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
       user = getUser();
       userName.setText(user.getUsername());
       email.setText(user.getEmail());
       firstName.setText(user.getFirstname());
       lastName.setText(user.getLastname());
       phoneNumber.setText(user.getPhonenumber());
    }

    @Override
    public void updateView() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(HashMap<String, ExchangeData> data) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {}
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @EditorAction(R.id.phoneNumber)
    @Click(R.id.btnSave)
    void onSave() {
        final String fName = firstName.getText().toString();
        final String lName = lastName.getText().toString();
        final String phNumber = phoneNumber.getText().toString();

        ApiClient.getInterface()
                .updateProfile(new ProfileUpdateRequest(fName, lName, phNumber))
                .enqueue(new AppCallback<LoginResponse>(this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if(response instanceof LoginResponse) {
            final LoginResponse data = (LoginResponse)response;
            ExchangeApplication.getApp().setUser(data.getUser());
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
