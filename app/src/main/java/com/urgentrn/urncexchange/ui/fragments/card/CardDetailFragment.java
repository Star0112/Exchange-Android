package com.urgentrn.urncexchange.ui.fragments.card;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.models.card.CardDetail;
import com.urgentrn.urncexchange.models.card.CardInfo;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetAvailableCardsResponse;
import com.urgentrn.urncexchange.ui.adapter.CardDetailGroupAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.ListViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_card_detail)
public class CardDetailFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtTitle, txtTapCardNumbers, txtCvv, txtCardNum, txtExp, txtName;

    @ViewById
    ImageView imgCard;
    
    @ViewById
    View llCard;

    @ViewById
    ExpandableListView listView;

    private CardDetailGroupAdapter groupAdapter;

    private Card card;

    @AfterViews
    protected void init() {
        if (getArguments() == null || getContext() == null) return;

        card = (Card)getArguments().getSerializable("card");
        if (card == null) return;

        setToolBar(false);

        txtTitle.setText(card.getCardInfo().getTitle());
        Glide.with(getContext()).load(card.getCardInfo().getFrontImage()).into(imgCard);
        if (true || card.isPhysical()) { // setting true for now as no need to see card number on this screen
            txtTapCardNumbers.setVisibility(View.GONE);
        } else {
            txtTapCardNumbers.setVisibility(View.INVISIBLE);
        }
        txtCvv.setVisibility(View.INVISIBLE);
        txtCardNum.setVisibility(View.INVISIBLE);
        txtExp.setVisibility(View.INVISIBLE);        
        if (card.isLoadable()) {
            llCard.getLayoutParams().height = getResources().getDimensionPixelOffset(R.dimen.card_height_2x);
            txtName.setVisibility(View.INVISIBLE);
        } else {
            txtName.setText(getUser() != null ? String.format("%s %s", getUser().getFirstName(), getUser().getLastName()) : null);
            txtName.setVisibility(View.VISIBLE);
        }

        groupAdapter = new CardDetailGroupAdapter();
        listView.setAdapter(groupAdapter);
        listView.setOnGroupClickListener(((parent, v, groupPosition, id) -> true));

        if (AppData.getInstance().getAvailableCards().size() > 0) {
            updateView();
        } else {
            ApiClient.getInterface().getAvailableCards().enqueue(new AppCallback<>(this));
        }
    }

    @Override
    public void updateView() {
        for (CardInfo cardInfo : AppData.getInstance().getAvailableCards()) {
            if (cardInfo.getId() == card.getCardInfo().getId() || cardInfo.getTitle().equals(card.getCardInfo().getTitle())) {
                updateDetailView(cardInfo);
                return;
            }
        }
    }

    private void updateDetailView(CardInfo cardInfo) {
        final List<CardDetail> groups = new ArrayList<>();
        for (CardDetail detail : cardInfo.getDetails()) {
            if (detail.getType().equals("group")) {
                groups.add(detail);
            }
        }
        groupAdapter.setData(groups);
        for (int i = 0; i < groups.size(); i ++) {
            listView.expandGroup(i);
        }
        ListViewUtils.setListViewHeightBasedOnChildren(listView);
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetAvailableCardsResponse) {
            AppData.getInstance().setAvailableCards(((GetAvailableCardsResponse)response).getData());
            updateView();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
