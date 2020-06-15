package com.urgentrn.urncexchange.ui.fragments.bank;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.DepositAddress;
import com.urgentrn.urncexchange.models.bank.FieldOption;
import com.urgentrn.urncexchange.ui.DDFormActivity_;
import com.urgentrn.urncexchange.ui.adapter.BankAccountAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_direct_deposit)
public class DirectDepositFragment extends BaseFragment {

    @ViewById
    RecyclerView recyclerView;

    private BankAccountAdapter adapter;

    @AfterViews
    protected void init() {
        getView().setBackgroundResource(R.color.colorYellow);

        adapter = new BankAccountAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        updateBankView();
    }

    private void updateBankView() {
        final DepositAddress address = AppData.getInstance().getDepositAddress();
        final List<FieldOption> data = new ArrayList<>();
        data.add(new FieldOption(address.getStrings().get("routingNumber"), address.getData().get("routingNumber")));
        data.add(new FieldOption(address.getStrings().get("accountNumber"), address.getData().get("accountNumber")));
        adapter.setData(data);
    }

    @Click(R.id.btnDirectDeposit)
    void onDirectDeposit() {
        startActivity(new Intent(getContext(), DDFormActivity_.class));
    }
}
