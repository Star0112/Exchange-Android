package com.urgentrn.urncexchange.ui.dialogs;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.ui.adapter.OnItemClickListener;
import com.urgentrn.urncexchange.ui.adapter.SelectAssetAdapter;
import com.urgentrn.urncexchange.ui.base.BaseDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.dialog_select_asset)
public class SelectAssetDialog extends BaseDialog implements OnItemClickListener {

    @ViewById
    RecyclerView recyclerAsset;

    private SelectAssetAdapter selectAssetAdapter;
    private int selectedPosition;
    List<AssetBalance> assetBalances = new ArrayList<>();

    @AfterViews
    protected void init() {
        assetBalances = AppData.getInstance().getAssetBalanceData();
        if (assetBalances == null) return;

        selectAssetAdapter = new SelectAssetAdapter(this, assetBalances);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerAsset.setLayoutManager(linearLayoutManager);
        recyclerAsset.setAdapter(selectAssetAdapter);
    }

    @Override
    public void onItemClick(int position) {
        selectedPosition = position;

        if (mListener != null) {
            mListener.onDismiss(true);
        }
        dismiss();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
