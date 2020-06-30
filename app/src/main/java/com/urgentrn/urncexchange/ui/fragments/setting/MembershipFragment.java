package com.urgentrn.urncexchange.ui.fragments.setting;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.common.api.Api;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.request.MembershipRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.MembershipResponse;
import com.urgentrn.urncexchange.models.response.PurchaseStatusResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import static com.urgentrn.urncexchange.utils.Utils.formattedDateTime;
import static com.urgentrn.urncexchange.utils.Utils.stringToDate;

@EFragment(R.layout.fragment_membership)
public class MembershipFragment extends BaseFragment implements ApiCallback {

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtPurchase, datePurchase;

    @ViewById
    Button btnPurchase;

    @AfterViews
    protected void init() {
        setToolBar(true);
        setupDrawer();
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

    private void setupDrawer() {
        ApiClient.getInterface()
                .getPurchaseStatus()
                .enqueue(new AppCallback<PurchaseStatusResponse>(this));
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

    @Click(R.id.btnPurchase)
    void onPurchase() {
        ApiClient.getInterface()
                .purchase(new MembershipRequest(1))
                .enqueue(new AppCallback<MembershipResponse>(this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        String date = null;
        if(response instanceof PurchaseStatusResponse) {
            final PurchaseStatusResponse data = (PurchaseStatusResponse)response;
            date = data.getData();
        } else if(response instanceof MembershipResponse) {
            final MembershipResponse data = (MembershipResponse)response;
            date = data.getMessage();
        }
        if(date != null) {
            btnPurchase.setVisibility(View.GONE);
            txtPurchase.setVisibility(View.VISIBLE);
            datePurchase.setVisibility(View.VISIBLE);
            datePurchase.setText(getResources().getString(R.string.purchase_date) + formattedDateTime(date));
        }
    }

    @Override
    public void onFailure(String message) {
    }
}
