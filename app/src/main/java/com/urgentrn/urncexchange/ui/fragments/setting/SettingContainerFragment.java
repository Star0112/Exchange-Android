package com.urgentrn.urncexchange.ui.fragments.setting;

import com.urgentrn.urncexchange.BuildConfig;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

@EFragment(R.layout.fragment_container)
public class SettingContainerFragment extends BaseFragment implements ApiCallback {
    @AfterViews
    protected void init() {
        getChildFragmentManager().beginTransaction()
                .add(R.id.container, new SettingFragment_())
                .commit();
    }

    @Override
    public void onResponse(BaseResponse response) {
    }

    @Override
    public void onFailure(String message) {
    }
}
