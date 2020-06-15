package com.urgentrn.urncexchange.ui.dialogs;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.adapter.OnItemClickListener;
import com.urgentrn.urncexchange.ui.adapter.SelectWalletAdapter;
import com.urgentrn.urncexchange.ui.base.BaseDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.dialog_select_wallet)
public class SelectWalletDialog extends BaseDialog implements OnItemClickListener {

    @ViewById
    View btnBackSelect;

    @ViewById
    TextView txtDialogTitle, editSearch;

    @ViewById
    RecyclerView recyclerView;

    private SelectWalletAdapter adapter;
    private List<Wallet> availableWallets;
    private int basePosition, toPosition;

    @AfterViews
    protected void init() {
        btnBackSelect.setVisibility(View.GONE);

        final String title = getArguments().getString("title");
        if (title != null) txtDialogTitle.setText(title);
        final String type = getArguments().getString("type");
        basePosition = getArguments().getInt("base_position");
        toPosition = getArguments().getInt("other_position");

        if (getArguments().getSerializable("wallets") != null) {
            availableWallets = (List<Wallet>)getArguments().getSerializable("wallets");
        } else if (type == null) {
            availableWallets = AppData.getInstance().getWallets();
        } else {
            availableWallets = new ArrayList<>();
            for (Wallet wallet : AppData.getInstance().getWallets()) {
                if (AppData.getInstance().getExchangeTickers(type).get(wallet.getSymbol()) != null) {
                    availableWallets.add(wallet);
                }
            }
        }
        adapter = new SelectWalletAdapter(this, availableWallets);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_full));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        if (basePosition >= 0) adapter.setSelectedPosition(basePosition);
    }

    @TextChange(R.id.editSearch)
    void onKeywordTextChange(CharSequence s) {
        final List<Wallet> wallets = new ArrayList<>();
        for (Wallet wallet : availableWallets) {
            if (wallet.getSymbol().toLowerCase().contains(s.toString().toLowerCase()) || wallet.getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                wallets.add(wallet);
            }
        }
        adapter.setData(wallets);
    }

    @Override
    public void onItemClick(int filteredPosition) {
        for (Wallet wallet : availableWallets) {
            if (wallet.getSymbol().equals(adapter.getItem(filteredPosition).getSymbol())) {
                final int position = availableWallets.indexOf(wallet);
                if (position == basePosition) {
                    onClose();
                } else if (position == toPosition) {
                    showAlert(getString(R.string.error_choose_same_asset));
                } else {
                    basePosition = position;
                    if (mListener != null) {
                        mListener.onDismiss(true);
                    }
                    onClose();
                }
                return;
            }
        }
    }

    public int getSelectedPosition() {
        return basePosition;
    }

    @Click(R.id.btnCloseSelect)
    void onClose() {
        editSearch.setText(null);
        dismiss();
    }
}
