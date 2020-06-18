package com.urgentrn.urncexchange.ui.fragments.buy;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.CoinBalance;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.adapter.CoinBalanceAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.signup.PINCreateActivity_;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.fragment_buy)
public class BuyFragment extends BaseFragment implements ApiCallback {
    @ViewById
    View llBackground, btnBuy;

    @ViewById(R.id.newHeader)
    TextView newHeader;

    @ViewById(R.id.selectCoin)
    Spinner selectCoin;

    @ViewById
    EditText buyPrice, buyAmount;

    @ViewById
    RecyclerView coinBalance;

    String[] symbols = { "URNC/BTC", "URNC/ETH", "URNC/USD"};
    private CoinBalanceAdapter adapterCoin;
    private ArrayList<CoinBalance> tempCoins = new ArrayList<>();

    @AfterViews
    protected void init() {
        newHeader.setText(R.string.title_buy);

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, symbols);
        selectCoin.setAdapter(spinnerAdapter);

        tempCoins.add(new CoinBalance("cypto","URNC","0x01029dko4", "51000","24453"));
        tempCoins.add(new CoinBalance("cypto","BTC","0x324dfds54", "25000","68253"));
        tempCoins.add(new CoinBalance("cypto","ETH","0xf102a5ko4", "30300","27423"));
        tempCoins.add(new CoinBalance("currency","USD","0xh1i2cdko4", "60200","26451"));
        tempCoins.add(new CoinBalance("cypto","COIN","0xu10d9dko4", "70400","18253"));


        setupDrawer();
        updateView();
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

    private void setupDrawer() {

        coinBalance.setHasFixedSize(true);
        coinBalance.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterCoin = new CoinBalanceAdapter(pos -> updateCoin(tempCoins.get(pos)));
        adapterCoin.setData(tempCoins);
        coinBalance.setAdapter(adapterCoin);
    }

    public void updateCoin(CoinBalance coin){

    }

    @Override
    public void updateView() {
    }

    @Click(R.id.btnBuy)
    void onBuy() {
        final String price = buyPrice.getText().toString();
        final String amount = buyAmount.getText().toString();

        if (price.isEmpty()) {
            buyPrice.requestFocus();
            buyPrice.setError(getString(R.string.error_price_empty));
        } else if (Integer.parseInt(price)<=0) {
            buyPrice.requestFocus();
            buyPrice.setError(getString(R.string.error_price_invalid));
        } else if (amount.isEmpty()) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_empty));
        } else if (!Utils.isPasswordValid(amount)) {
            buyAmount.requestFocus();
            buyAmount.setError(getString(R.string.error_amount_invalid));
        } else {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<Wallet> data) {
        updateView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTickersUpdated(HashMap<String, ExchangeData> data) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {}
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResponse(BaseResponse response) {

    }

    @Override
    public void onFailure(String message) {

    }
}
