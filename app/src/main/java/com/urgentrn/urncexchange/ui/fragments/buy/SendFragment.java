package com.urgentrn.urncexchange.ui.fragments.buy;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_send)
public class SendFragment extends BaseFragment implements ApiCallback {
    @Override
    public void onResponse(BaseResponse response) {

    }

    @Override
    public void onFailure(String message) {

    }
}
