package com.urgentrn.urncexchange.ui.fragments.card;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.CardTransactionsHistory;
import com.urgentrn.urncexchange.models.card.CardTransaction;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetCardTransactionsResponse;
import com.urgentrn.urncexchange.ui.adapter.CardTransactionAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.CardTransactionDialog;
import com.urgentrn.urncexchange.ui.dialogs.CardTransactionDialog_;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_card_history)
public class CardHistoryFragment extends BaseFragment implements ApiCallback {

    @ViewById
    UltimateRecyclerView recyclerView;

    @ViewById
    View txtNoTransactions;

    private CardTransactionAdapter adapter;
    private CardTransactionDialog dialogTransaction = new CardTransactionDialog_();

    private List<CardTransaction> transactions;
    private int page = 0, totalPages = 1;

    @AfterViews
    protected void init() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);

        adapter = new CardTransactionAdapter(position -> showTransactionDialog(adapter.getItem(position)));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setDefaultOnRefreshListener(() -> onRefresh());
        recyclerView.setLoadMoreView(R.layout.bottom_progressbar);
        recyclerView.setOnLoadMoreListener(((itemsCount, maxLastVisiblePosition) -> getTransactions(page)));
        recyclerView.setAdapter(adapter);

        transactions = AppData.getInstance().getCardTransactions();
        if (transactions.size() > 0) {
            adapter.setData(transactions);
        } else {
            recyclerView.setRefreshing(true);
            onRefresh();
        }
    }

    private void onRefresh() {
        txtNoTransactions.setVisibility(View.GONE);
        getTransactions(0);
    }

    private void getTransactions(int page) { // Get Card Transactions
        if (page < totalPages) {
            final String reference = getArguments().getString("reference");
            ApiClient.getInterface()
                    .getCardTransactions(reference, page + 1, Constants.DEFAULT_PAGE_LIMIT)
                    .enqueue(new AppCallback<>(this));
        }
    }

    private void showTransactionDialog(CardTransaction transaction) {
        if (transaction == null) return;
        if (dialogTransaction.getDialog() != null && dialogTransaction.getDialog().isShowing()) return;
        final Bundle args = new Bundle();
        args.putSerializable("transaction", transaction);
        dialogTransaction.setArguments(args);
        dialogTransaction.show(getChildFragmentManager(), "CARD");
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetCardTransactionsResponse) {
            final CardTransactionsHistory data = ((GetCardTransactionsResponse)response).getData();
            page = data.getPage();
            totalPages = data.getTotalPages();
            if (page == totalPages) {
                if (recyclerView.isLoadMoreEnabled()) {
                    recyclerView.disableLoadmore();
                }
            } else if (page == 1) {
                recyclerView.reenableLoadmore();
            }
            recyclerView.setRefreshing(false);
            txtNoTransactions.setVisibility(data.getTotal() == 0 ? View.VISIBLE : View.GONE);

            if (page == 1) transactions.clear();
            transactions.addAll(data.getTransactions());
            adapter.setData(transactions);
            AppData.getInstance().setCardTransactions(transactions);
        }
    }

    @Override
    public void onFailure(String message) {
        if (message.contains("no transactions")) {
            recyclerView.setRefreshing(false);
            txtNoTransactions.setVisibility(View.VISIBLE);
        }
    }
}
