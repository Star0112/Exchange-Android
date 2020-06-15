package com.urgentrn.urncexchange.ui.fragments.card;

import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.card.CardTransaction;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.adapter.CardTransactionAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.fragment_card_report_submit)
public class CardReportSubmitFragment extends BaseFragment implements ApiCallback {

    @ViewById
    UltimateRecyclerView recyclerView;

    @ViewById
    Spinner spinnerView;

    @ViewById
    EditText editMessage;

    private List<CardTransaction> data = new ArrayList<>();

    @AfterViews
    protected void init() {
        if (getArguments() == null || getContext() == null) return;

        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);

        data = (List<CardTransaction>)getArguments().getSerializable("data");
        initTransactionsView();

        final String[] spinnerData = {getString(R.string.fraud), getString(R.string.lost)};
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, spinnerData);
        spinnerView.setAdapter(spinnerAdapter);
    }

    private void initTransactionsView() {
        final CardTransactionAdapter adapter = new CardTransactionAdapter(null);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        recyclerView.setAdapter(adapter);
        if (data != null && data.size() > 0) {
            adapter.setData(new ArrayList<>(data));
        }
    }

    @Click(R.id.btnSubmit)
    void onSubmit() {
        final String message = editMessage.getText().toString();
        if (message.isEmpty()) {
            editMessage.setError(getString(R.string.error_field_empty));
            return;
        }

        final int id;
        final String event;
        if (spinnerView.getSelectedItemPosition() == 0) { // Fraud
            if (data == null || data.size() == 0) {
                ((BaseActivity)getActivity()).showAlert(R.string.error_no_transaction_for_fraud, R.string.button_ok, R.string.button_cancel, ((dialog, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        onBackPressed();
                    }
                }));
                return;
            }
            id = data.get(0).getId();
            event = "fraud";
        } else { // Lost
            id = getArguments().getInt("card_id");
            event = "lost";
        }

        final HashMap<String, String> request = new HashMap<>();
        request.put("event", event);
        request.put("message", message);
        ApiClient.getInterface().reportCard(id, request).enqueue(new AppCallback<>(getContext(), this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (getActivity() == null) return;
        ((BaseActivity)getActivity()).showAlert(R.string.report_success, ((dialog, which) -> onBackPressed()));
    }

    @Override
    public void onFailure(String message) {

    }
}
