package com.urgentrn.urncexchange.ui.fragments.profile;

import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.SymbolResponse;
import com.urgentrn.urncexchange.ui.MainActivity;
import com.urgentrn.urncexchange.ui.adapter.SelectFiatCurrencyAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_currency)
public class CurrencyFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtTitle, txtDescription;

    @ViewById
    RecyclerView recyclerView;

    private SelectFiatCurrencyAdapter adapter;

    private static List<Symbol> data;
    private boolean isDefault;
    private List<String> defaultSelectedSymbols = new ArrayList<>();

    @AfterViews
    protected void init() {
        if (getArguments() == null || getContext() == null) return;

        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);

        isDefault = getArguments().getBoolean("is_default");

        txtTitle.setText(isDefault ? R.string.default_currency : R.string.currencies);
        txtDescription.setText(isDefault ? R.string.select_default_currency : R.string.activate_fiat_currency);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        adapter = new SelectFiatCurrencyAdapter(null, !isDefault);
        recyclerView.setAdapter(adapter);

        if (data == null) {
            ApiClient.getInterface().getSymbols("currency").enqueue(new AppCallback<>(getContext(), this));
        } else {
            updateView();
        }
    }

    @Override
    public void updateView() {
        if (isDefault) {
            final List<Symbol> data = new ArrayList<>();
            if (true) {
                for (Wallet wallet : AppData.getInstance().getCurrencyWallets()) {
                    for (Symbol symbol : CurrencyFragment.data) {
                        if (symbol.getSymbol().equals(wallet.getSymbol())) {
                            data.add(symbol);
                            break;
                        }
                    }
                }
            } else { // not using this because symbol data is not synced with currency symbols
                for (Wallet wallet : AppData.getInstance().getCurrencyWallets()) {
                    data.add(WalletUtils.getSymbolData(wallet.getSymbol()));
                }
            }
            adapter.setData(data);
        } else {
            defaultSelectedSymbols.clear();
            adapter.setData(data);
            for (int i = 0; i < data.size(); i ++) {
                for (Wallet wallet : AppData.getInstance().getCurrencyWallets()) {
                    if (wallet.getSymbol().equals(data.get(i).getSymbol())) {
                        defaultSelectedSymbols.add(wallet.getSymbol());
                        adapter.setSelectedPosition(i);
                        break;
                    }
                }
            }
        }
    }

    @Click(R.id.btnDone)
    void onDone() {
        if (isDefault) {
            final Symbol selectedSymbol = adapter.getSelectedItem();
            if (selectedSymbol != null) {
                if (selectedSymbol.getId() != getUser().getCurrencyAssetId()) {
                    ApiClient.getInterface().setDefaultCurrency(selectedSymbol.getSymbol()).enqueue(new AppCallback<>(getContext(), this));
                } else {
                    onBackPressed();
                }
            }
        } else {
            for (Symbol symbol : data) {
                if (defaultSelectedSymbols.contains(symbol.getSymbol())) {
                    if (!adapter.getSelectedSymbols().contains(symbol.getSymbol())) {
                        ApiClient.getInterface().removeCurrencyWallet(symbol.getSymbol()).enqueue(new AppCallback<>(getContext(), this));
                    }
                } else {
                    if (adapter.getSelectedSymbols().contains(symbol.getSymbol())) {
                        ApiClient.getInterface().addCurrencyWallet(symbol.getSymbol()).enqueue(new AppCallback<>(getContext(), this));
                    }
                }
            }
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof SymbolResponse) {
            data = ((SymbolResponse)response).getData();
            updateView();
        } else {
            if (isDefault) {
                getUser().setCurrencyAssetId(adapter.getSelectedItem().getId());
            }
            ((MainActivity)getActivity()).getData(true);
            onBackPressed();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
