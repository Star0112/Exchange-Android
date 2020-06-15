package com.urgentrn.urncexchange.ui.fragments.card;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.card.CardTransaction;
import com.urgentrn.urncexchange.ui.adapter.CardTransactionAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_card_report)
public class CardReportFragment extends BaseFragment {

    @ViewById
    UltimateRecyclerView recyclerView;

    @ViewById
    View txtNoTransactions;

    private CardTransactionAdapter adapter;

    @AfterViews
    protected void init() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);

        initTransactionsView();
    }

    private void initTransactionsView() {
        adapter = new CardTransactionAdapter(null, true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        final List<CardTransaction> data = AppData.getInstance().getCardTransactions();
        if (data.size() > 0) {
            adapter.setData(new ArrayList<>(data));
            txtNoTransactions.setVisibility(View.GONE);
        } else {
            txtNoTransactions.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.btnNext)
    void onNext() {
        final List<CardTransaction> filteredData = new ArrayList<>();
        for (int i = 0; i < adapter.getAdapterItemCount(); i ++) {
            final CardTransaction transaction = adapter.getItem(i);
            if (transaction.isSelected()) {
                filteredData.add(transaction);
            }
        }

        final Fragment fragment = new CardReportSubmitFragment_();
        final Bundle args = new Bundle();
        args.putInt("card_id", getArguments().getInt("card_id"));
        args.putSerializable("data", (Serializable)filteredData);
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }
}
