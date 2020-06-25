package com.urgentrn.urncexchange.ui.fragments.deposit;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.scottyab.rootbeer.Const;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.fragment_card)
public class CardFragment extends BaseFragment implements ApiCallback {

    @ViewById
    Toolbar toolbar;

    @ViewById
    CardInputWidget cardInputWidget;

    @ViewById
    EditText price;


    private Stripe stripe;

    @AfterViews
    protected void init() {
        setToolBar(true);
        price.setText("0");

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


    @Override
    public void updateView() {

    }

    @Click(R.id.btnPay)
    void onPay() {
        final String stripePrice = price.getText().toString();
        if (stripePrice.isEmpty()) {
            price.requestFocus();
            price.setError(getString((R.string.error_price_empty)));
        } else if (Float.parseFloat(stripePrice) <= 0) {
            price.requestFocus();
            price.setError(getString(R.string.error_price_invalid));
        } else {

            if (Constants.USE_STRIPE_TOKEN) {
                if (cardInputWidget.getCard() != null) {
                    stripe = new Stripe(getContext(), "pk_test_kKU5jpvzvvF4qmNlBcgWuq760023NITiJJ");
                    stripe.createToken(cardInputWidget.getCard(), new ApiResultCallback<Token>() {
                        @Override
                        public void onSuccess(Token token) {
                            Toast.makeText(getContext(), token.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@NotNull Exception e) {

                        }
                    });
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(HashMap<String, ExchangeData> data) {

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
