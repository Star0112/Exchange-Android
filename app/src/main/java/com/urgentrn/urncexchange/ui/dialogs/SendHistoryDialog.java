package com.urgentrn.urncexchange.ui.dialogs;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.BuyHistory;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.BuyHistoryResponse;
import com.urgentrn.urncexchange.ui.adapter.BuyHistoryAdapter;
import com.urgentrn.urncexchange.ui.base.BaseDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.dialog_send_history)
public class SendHistoryDialog extends BaseDialog implements ApiCallback {

    private int limit = 20;
    private int offset = 0;
    private String coinName;
    private List<BuyHistory> histories = new ArrayList<>();
    private BuyHistoryAdapter adapter;

    @ViewById
    RecyclerView recyclerBuyHistory;




    @AfterViews
    protected void init() {
        coinName = getArguments().getString("coin");
        recyclerBuyHistory.setHasFixedSize(true);
        recyclerBuyHistory.setLayoutManager(new LinearLayoutManager((getContext())));
        showProgressBar();
        setupDrawer(offset, limit);
    }

    private void setupDrawer(int offset, int limit) {
        ApiClient.getInterface()
                .getBuyHistory(coinName, offset, limit)
                .enqueue(new AppCallback<BuyHistoryResponse>(this));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onResponse(BaseResponse response) {
        hideProgressBar();
        if(response instanceof BuyHistoryResponse) {
            final List<BuyHistory> data = ((BuyHistoryResponse)response).getData();
            if(data != null) {
                for (BuyHistory buyHistory : data) {
                    histories.add(buyHistory);
                }
                if(data.size() != 0) {
                    offset += (limit * 2);
                    setupDrawer(offset, limit);
                }
            }
            if(offset == 0 || data.size() == 0) {
                adapter = new BuyHistoryAdapter(histories);
                recyclerBuyHistory.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
