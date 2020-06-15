package com.urgentrn.urncexchange.ui.fragments.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.card.AvailablePrice;
import com.urgentrn.urncexchange.models.card.GiftCard;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetAvailableCardsResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.BuyGiftCardDialog;
import com.urgentrn.urncexchange.ui.dialogs.BuyGiftCardDialog_;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_buy_gift_card)
public class BuyGiftCardFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtTitle, txtPrice, txtPurchaseAmounts, txtDescription;

    @ViewById
    CardView cardView;

    @ViewById
    ImageView imgCard;

    @ViewById(R.id.txtPriceRange)
    TabLayout tabLayout;

    @ViewById
    Button btnContinue;

    private final BuyGiftCardDialog dialog = new BuyGiftCardDialog_();

    private GiftCard card;
    private double fee;

    @AfterViews
    protected void init() {
        if (getArguments() == null || getContext() == null) return;

        setToolBar(false);

        card = (GiftCard)getArguments().getSerializable("card");
        fee = getArguments().getDouble("fee");

        txtTitle.setText(getString(R.string.buy_gift_card, card.getName()));
        if (card.getColor() != null) {
            cardView.setCardBackgroundColor(Color.parseColor(card.getColor()));
        }
        Glide.with(getContext()).load(card.getImage()).into(imgCard);
        if (card.getPriceType().equals("fixed")) {
            txtPurchaseAmounts.setText(String.format("%s:", getString(R.string.purchase_amounts)));
            for (AvailablePrice price : card.getAvailablePrices()) {
                tabLayout.addTab(tabLayout.newTab().setText(String.format("$%s", Utils.formattedNumber(price.getPrice(), 0, 2))));
            }
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    txtPrice.setText(tab.getText());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    txtPrice.setText(tab.getText());
                }
            });
            if (tabLayout.getTabCount() > 0) {
                tabLayout.getTabAt(0).select();
            }
            btnContinue.setText(R.string.button_continue);
        } else {
            txtPrice.setText(String.format("$%s - $%s", Utils.formattedNumber(card.getMinPrice()), Utils.formattedNumber(card.getMaxPrice())));
            txtPurchaseAmounts.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            btnContinue.setText(R.string.enter_amount);
        }
        txtDescription.setText(card.getDescription());

        if (AppData.getInstance().getAvailableCards().size() == 0) {
            ApiClient.getInterface().getAvailableCards().enqueue(new AppCallback<>(this));
        }
    }

    @Click(R.id.btnContinue)
    void onContinue() {
        if (dialog.getDialog() != null && dialog.getDialog().isShowing()) return;
        final Bundle args = new Bundle();
        args.putSerializable("card", card);
        args.putDouble("fee", fee);
        if (card.getPriceType().equals("fixed")) {
            args.putDouble("price", card.getAvailablePrices().get(tabLayout.getSelectedTabPosition()).getPrice());
        }
        dialog.setArguments(args);
        dialog.show(getChildFragmentManager(), WalletUtils.TransactionType.GIFT.name());
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetAvailableCardsResponse) {
            AppData.getInstance().setAvailableCards(((GetAvailableCardsResponse)response).getData());
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
