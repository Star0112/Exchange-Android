package com.urgentrn.urncexchange.ui.fragments.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.card.GiftMeta;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetCardResponse;
import com.urgentrn.urncexchange.ui.TermActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Locale;

@EFragment(R.layout.fragment_gift_card_detail)
public class GiftCardDetailFragment extends BaseFragment implements ApiCallback {

    @ViewById
    Toolbar toolBar;

    @ViewById
    TextView txtTitle, txtPrice, txtCode, txtTime;

    @ViewById
    CardView cardView;

    @ViewById
    ImageView imgCard;

    @ViewById
    Button btnLink, btnLock;

    private Card card;
    private String cardLink;

    @AfterViews
    protected void init() {
        if (getArguments() == null || getContext() == null) return;

        setBackgroundColor(getResources().getColor(R.color.colorBackground));
        setToolBar(true);

        card = (Card)getArguments().getSerializable("card");
        String color = null;

        for (GiftMeta meta : card.getMetas()) {
            switch (meta.getType()) {
                case "giftCardColor":
                    color = meta.getValue();
                    cardView.setCardBackgroundColor(Color.parseColor(color));
                    break;
                case "giftCardImage":
                    Glide.with(getContext()).load(meta.getValue()).into(imgCard);
                    break;
                case "giftCardAmount":
                    txtPrice.setText(String.format("$%s", Utils.formattedNumber(meta.getValue())));
                    break;
                case "giftCardName":
                    txtTitle.setText(meta.getValue());
                    break;
                case "giftCardNumber":
                    txtCode.setText(meta.getValue());
                    break;
                case "giftCardLink":
                    cardLink = meta.getValue();
                    break;
                default:
                    break;
            }
        }

        setStatusBarColor(color != null ? Color.parseColor(color) : getResources().getColor(R.color.colorBlue));
        toolBar.setBackgroundColor(color != null ? Color.parseColor(color) : getResources().getColor(R.color.colorBlue));

        final int days = Utils.daysFromNow(card.getCardOrder().getCreatedAt());
        String text;
        if (days == 0) {
            text = "today";
        } else if (days == 1) {
            text = "yesterday";
        } else if (days > 0) {
            text = String.format(Locale.US, "%d %s", days, "days ago");
        } else {
            text = String.format(Locale.US, "%s %d %s", "In", -days, "days");
        }
        txtTime.setText(String.format("%s %s", "Created", text));

        if (TextUtils.isEmpty(cardLink)) {
            btnLink.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorGray)));
            btnLink.setTextColor(getResources().getColor(R.color.textColorGray));
            btnLink.setEnabled(false);
        }

        if (card.getStatus().equals("processing")) {
            btnLock.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorGray)));
            btnLock.setTextColor(getResources().getColor(R.color.textColorGray));
            btnLock.setEnabled(false);
        }
    }

    @Click(R.id.btnLink)
    void onLink() {
        final Intent intent = new Intent(getContext(), TermActivity_.class);
        intent.putExtra("title", getString(R.string.claim_code));
        intent.putExtra("url", cardLink);
        startActivity(intent);
    }

    @Click(R.id.btnLock)
    void onLock() {
        ((BaseActivity)getActivity()).showAlert(R.string.delete_card_confirm, R.string.button_yes, R.string.button_cancel, ((dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                final HashMap<String, Object> request = new HashMap<>();
                request.put("status", "terminated");
                ApiClient.getInterface().lockCard(card.getReference(), request).enqueue(new AppCallback<>(getContext(), this));
            }
        }));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetCardResponse) { // gift card removed successfully
            final Card updatedCard = ((GetCardResponse)response).getData();
            for (Card card : AppData.getInstance().getMyCards()) {
                if (card.getReference().equals(updatedCard.getReference())) {
                    card.setStatus(updatedCard.getStatus());
                    break;
                }
            }

            if (getParentFragment() != null) {
                final GiftCardFragment fragment = (GiftCardFragment)getParentFragment().getChildFragmentManager().findFragmentByTag("GiftCardFragment_");
                if (fragment != null) fragment.updateView();
            }

            onBackPressed();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
