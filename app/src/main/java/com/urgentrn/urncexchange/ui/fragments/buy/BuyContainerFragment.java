package com.urgentrn.urncexchange.ui.fragments.buy;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.fragments.setting.SettingFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_container)
public class BuyContainerFragment extends BaseFragment implements ApiCallback {
    @AfterViews
    protected void init() {
        getChildFragmentManager().beginTransaction()
                .add(R.id.container, new BuyFragment_())
                .commit();
    }

    @Override
    public void onResponse(BaseResponse response) {
    }

    @Override
    public void onFailure(String message) {
    }
}
