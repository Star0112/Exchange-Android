package com.urgentrn.urncexchange.ui.fragments.buy;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.AssetBalance;
import com.urgentrn.urncexchange.models.MarketInfo;
import com.urgentrn.urncexchange.models.request.BuyCoinRequest;
import com.urgentrn.urncexchange.models.response.AssetResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;
import com.urgentrn.urncexchange.ui.adapter.CoinBalanceAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.SelectSymbolDialog;
import com.urgentrn.urncexchange.ui.dialogs.SelectSymbolDialog_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.urgentrn.urncexchange.utils.Utils.addChar;
import static com.urgentrn.urncexchange.utils.Utils.formattedNumber;

@EFragment(R.layout.fragment_buy)
public class BuyFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtQuoteAsset, txtBaseAsset, txtQuoteAssetName, txtBaseAssetName, txtQuoteAssetBalance, txtBaseAssetBalance, txtBuyPrice;

    @ViewById
    EditText buyPrice, buyAmount;

    @ViewById
    Button btnSelectSymbol;

    @ViewById
    RecyclerView recyclerAssetBalance;

    private final SelectSymbolDialog symbolDialog = new SelectSymbolDialog_();

    private CoinBalanceAdapter adapterCoin;
    private ArrayList<AssetBalance> assetBalanceData = new ArrayList<>();
    private List<MarketInfo> marketInfoData = new ArrayList<>();
    private MarketInfo selectedAsset;
    private int selectedSymbol = 0;

    @AfterViews
    protected void init() {
        buyAmount.setText("1");

        recyclerAssetBalance.setHasFixedSize(true);
        recyclerAssetBalance.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getMargetData();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<AssetBalance> data) {
        if(data != null) {
            assetBalanceData.clear();
            assetBalanceData.addAll(data);
        }
        adapterCoin = new CoinBalanceAdapter(getChildFragmentManager(), pos ->assetBalanceData.get(pos));
        adapterCoin.setData(assetBalanceData);
        recyclerAssetBalance.setAdapter(adapterCoin);
        updateUI();
    }

    private void getAssetBalanceData() {
        ApiClient.getInterface()
                .getAssetBalance()
                .enqueue(new AppCallback<AssetResponse>(this));
    }

    private void getMargetData() {
        ApiClient.getInterface()
                .getMarketInfo()
                .enqueue(new AppCallback<MarketInfoResponse>(this));
    }

    @TextChange(R.id.buyAmount)
    void onBuyChange(CharSequence s) {
        int amount = !buyAmount.getText().toString().isEmpty()? Integer.parseInt(buyAmount.getText().toString()) : 0;
        double price = selectedAsset == null? 0 : selectedAsset.getPrice();
        buyPrice.setText(formattedNumber( price * amount));
    }

    @EditorAction(R.id.buyAmount)
    @Click(R.id.btnBuy)
    void onBuy() {
        final String amount = buyAmount.getText().toString();
        if (amount.length() != 0) {
            buyAmount.setText(String.valueOf(Integer.parseInt(amount)));
        }

        if (amount.isEmpty()) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_empty));
        } else if (Integer.parseInt(amount)<=0) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_invalid));
        } else if(Double.parseDouble(amount) * selectedAsset.getPrice() > getMyAssetBalance(selectedAsset.getBase()) ) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_limited));
        } else {
            ApiClient.getInterface()
                    .buyCoin(new BuyCoinRequest(this.selectedAsset.getPair(), this.selectedAsset.getId(), Integer.parseInt(amount)))
                    .enqueue(new AppCallback<BaseResponse>(getContext(), new ApiCallback() {
                        @Override
                        public void onResponse(BaseResponse response) {
                            getAssetBalanceData();
                            ((BaseActivity)getActivity()).showAlert(R.string.buy_success);
                        }
                        @Override
                        public void onFailure(String message) {
                        }
                    }));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {}
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Click(R.id.btnSend)
    void onSend() {
        ((BaseFragment)getParentFragment()).replaceFragment(new SendFragment_(), false);
    }

    @Click(R.id.btnSelectSymbol)
    void onShowSelectSymbol() {
        showSelectSymbol();
    }

    private void showSelectSymbol() {
        if (symbolDialog.getDialog() != null && symbolDialog.getDialog().isShowing()) return;
        symbolDialog.setOnDialogDismissListener(isSuccess -> {
            if (selectedSymbol != symbolDialog.getSelectedPosition()) {
                selectedSymbol = symbolDialog.getSelectedPosition();
                updateUI();
            }
        });
        symbolDialog.show(getChildFragmentManager(), "selSymbol");
    }

    private void updateUI() {
        selectedAsset = marketInfoData.get(selectedSymbol);
        txtBaseAsset.setText(selectedAsset.getBase());
        txtQuoteAsset.setText(selectedAsset.getPair());
        txtBaseAssetName.setText(selectedAsset.getBase());
        txtQuoteAssetName.setText(selectedAsset.getPair());
        txtBuyPrice.setText(String.format(Locale.US, "1%s = %s%s",
                selectedAsset.getPair(),
                selectedAsset.getPrice(),
                selectedAsset.getBase()
        ));
        btnSelectSymbol.setText(addChar(selectedAsset.getName(),'/',4));
        txtBaseAssetBalance.setText(formattedNumber(getMyAssetBalance(selectedAsset.getBase())));
        txtQuoteAssetBalance.setText(formattedNumber(getMyAssetBalance(selectedAsset.getPair())));

        onBuyChange("");
    }

    private double getMyAssetBalance(String asset) {
        for (AssetBalance assetBalance : assetBalanceData) {
            if(asset.equals(assetBalance.getCoin())) {
                return Double.parseDouble(assetBalance.getAvailable());
            }
        }
        return 0;
    }

    @Override
    public void onResponse(BaseResponse response) {
        if(response instanceof MarketInfoResponse) {
            final List<MarketInfo> data = ((MarketInfoResponse)response).getData();
            if(data != null) {
                for (MarketInfo marketInfo : data) {
                    marketInfoData.add(marketInfo);
                }
                updateUI();
            }
        } else if(response instanceof AssetResponse) {
            final List<AssetBalance> data = ((AssetResponse)response).getData();
            AppData.getInstance().setAssetBalanceData(data);
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
