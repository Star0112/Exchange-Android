package com.urgentrn.urncexchange.ui.fragments.card;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.card.CardInfo;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.CvvDialog;
import com.urgentrn.urncexchange.ui.dialogs.CvvDialog_;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EFragment(R.layout.fragment_card_status)
public class CardStatusFragment extends BaseFragment implements ApiCallback {

    @ViewById
    Toolbar toolBar;

    @ViewById
    TextView txtTitle, txtName, txtOrderStatus, txtKycStatus, txtCardStatus;

    @ViewById
    ImageView imgCard;

    @ViewById
    View llLock, txtHelp, btnActivate;

    private CvvDialog dialog = new CvvDialog_();

    private boolean isUpgrading; // from Card Setting
    private Card card; // isUpgrading ? Upgrading Card : Current Card

    @AfterViews
    protected void init() {
        if (getArguments() == null || getContext() == null) return;

        isUpgrading = getArguments().getBoolean("is_upgrading");
        card = (Card)getArguments().getSerializable("card");
        if (card == null) return;
        final CardInfo cardInfo = card.getCardInfo();

        setBackgroundColor(getResources().getColor(R.color.colorBackground));
        if (isUpgrading) {
            setToolBar(false);
        }

        txtTitle.setText(cardInfo.getTitle());
        txtOrderStatus.setText(String.format("%s %s", cardInfo.getTitle(), getString(R.string.order_status)));
        if (cardInfo.getImages().size() > 0) {
            Glide.with(getContext()).load(cardInfo.getImages().get(cardInfo.getImages().size() == 1 ? 0 : 1).getImage()).into(imgCard);
        }
        if (getUser() != null) {
            txtName.setText(String.format("%s %s", getUser().getFirstName(), getUser().getLastName()));
            txtName.setVisibility(View.VISIBLE);
        }

        updateView(null);
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
    public void updateView(Card updatedCard) {
        if (updatedCard != null) {
            if (isUpgrading) {
                card = AppData.getInstance().getMyCards().get(0);
            } else {
                card = updatedCard;
            }
        }

        llLock.setVisibility(card.getCardInfo().getReserveAsset() != null && card.getCardInfo().getReserveAmount() > 0 ? View.VISIBLE : View.GONE);

        txtHelp.setVisibility(card.isPhysical() || isUpgrading ? View.VISIBLE : View.GONE);
        if (!card.getEntity().isTierCompleted() || !card.isPhysical()/* || isUpgrading*/) { // TODO: why is isUpgrading here?
            btnActivate.setVisibility(View.GONE);
        } else {
            btnActivate.setVisibility(View.VISIBLE);
        }

        if (card.getCardOrder() != null && !TextUtils.isEmpty(card.getCardOrder().getStatus())) {
            switch (card.getCardOrder().getStatus()) {
                case "processed":
                case "verified":
                    txtKycStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreenTransparent)));
                    txtKycStatus.setTextColor(getResources().getColor(R.color.colorGreen));
                    break;
                case "processing":
                    txtKycStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPendingTransparent)));
                    txtKycStatus.setTextColor(getResources().getColor(R.color.colorPending));
                    btnActivate.setEnabled(false);
                    break;
                default:
                    txtKycStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRedTransparent)));
                    txtKycStatus.setTextColor(getResources().getColor(R.color.colorRed));
                    txtHelp.setVisibility(View.GONE);
                    btnActivate.setVisibility(View.GONE);
                    break;
            }
            txtKycStatus.setText(String.format("%s%s", card.getCardOrder().getStatus().substring(0, 1).toUpperCase(), card.getCardOrder().getStatus().substring(1)));
        } else { // consider as failed
            txtKycStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRedTransparent)));
            txtKycStatus.setTextColor(getResources().getColor(R.color.colorRed));
            txtHelp.setVisibility(View.GONE);
            btnActivate.setVisibility(View.GONE);
            txtKycStatus.setText(R.string.failed);
        }

        switch (card.getStatus()) {
            case "shipped":
            case "processed":
                txtCardStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreenTransparent)));
                txtCardStatus.setTextColor(getResources().getColor(R.color.colorGreen));
                break;
            case "processing":
                txtCardStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPendingTransparent)));
                txtCardStatus.setTextColor(getResources().getColor(R.color.colorPending));
                btnActivate.setEnabled(false);
                break;
            default:
                txtCardStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRedTransparent)));
                txtCardStatus.setTextColor(getResources().getColor(R.color.colorRed));
                btnActivate.setEnabled(false);
                break;
        }
        txtCardStatus.setText(String.format("%s%s", card.getStatus().substring(0, 1).toUpperCase(), card.getStatus().substring(1)));
    }

    @Click(R.id.btnActivate)
    void onActivate() {
        if (dialog.getDialog() != null && dialog.getDialog().isShowing()) return;

        final Bundle args = new Bundle();
        args.putString("reference", card.getReference());
        args.putString("terms", card.getCardInfo().getTerms());
        args.putString("pin_type", card.getCardInfo().getPinType());
        dialog.setArguments(args);
        dialog.setHideable(false);
        dialog.show(getChildFragmentManager(), Constants.VerifyType.CARD_ACTIVATE.name());
    }

    @Override
    public void onResponse(BaseResponse response) {

    }

    @Override
    public void onFailure(String message) {

    }
}
