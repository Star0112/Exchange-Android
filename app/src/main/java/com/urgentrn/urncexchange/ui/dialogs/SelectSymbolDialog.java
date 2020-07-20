package com.urgentrn.urncexchange.ui.dialogs;

import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.MarketInfo;
import com.urgentrn.urncexchange.ui.adapter.OnItemClickListener;
import com.urgentrn.urncexchange.ui.adapter.SelectSymbolAdapter;
import com.urgentrn.urncexchange.ui.base.BaseDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.dialog_select_symbol)
public class SelectSymbolDialog extends BaseDialog implements OnItemClickListener {

    @ViewById
    RecyclerView recyclerSymbol;

    private SelectSymbolAdapter selectSymbolAdapter;
    private int selectedPosition;
    List<MarketInfo> marketInfos = new ArrayList<>();
    List<String> symbols = new ArrayList<>();

    @AfterViews
    protected void init() {
        symbols.clear();
        marketInfos = AppData.getInstance().getMarketInfoData();
        if (marketInfos == null) return;
        for (MarketInfo marketInfo : marketInfos) {
            symbols.add(marketInfo.getName());
        }
        selectSymbolAdapter = new SelectSymbolAdapter(this,symbols);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerSymbol.setLayoutManager(linearLayoutManager);
        recyclerSymbol.setAdapter(selectSymbolAdapter);
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
