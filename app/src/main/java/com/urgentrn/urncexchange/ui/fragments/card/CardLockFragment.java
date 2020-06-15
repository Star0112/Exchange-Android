package com.urgentrn.urncexchange.ui.fragments.card;

import android.content.res.ColorStateList;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetCardResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

@EFragment(R.layout.fragment_card_lock)
public class CardLockFragment extends BaseFragment implements ApiCallback {

    @ViewById
    ImageView imgCheck;

    @ViewById
    TextView txtLock;

    @ViewById
    Switch switchLock;

    private Card card;

    @AfterViews
    protected void init() {
        if (getArguments() == null) return;
        card = (Card)getArguments().getSerializable("card");

        setBackgroundColor(getResources().getColor(R.color.colorBackground));
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);

        updateView();
        switchLock.setChecked(card.isLocked());
    }

    @Override
    public void updateView() {
        imgCheck.setImageTintList(ColorStateList.valueOf(getResources().getColor(card.isLocked() ? R.color.colorRed : R.color.colorGreen)));
        txtLock.setText(card.isLocked() ? R.string.locked : R.string.unlocked);
        txtLock.setTextColor(getResources().getColor(card.isLocked() ? R.color.colorRed : R.color.colorGreen));
    }

    @CheckedChange(R.id.switchLock)
    void onCheckedChange(boolean isChecked) {
        if (isChecked == card.isLocked()) return;
        final HashMap<String, Object> request = new HashMap<>();
        request.put("locked", isChecked);
        ApiClient.getInterface().lockCard(card.getReference(), request).enqueue(new AppCallback<>(getContext(), this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetCardResponse) {
            card.setLocked(switchLock.isChecked());
            updateView();
        }
    }

    @Override
    public void onFailure(String message) {
        switchLock.setChecked(!switchLock.isChecked());
    }
}
