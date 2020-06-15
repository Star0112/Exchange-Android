package com.urgentrn.urncexchange.ui.fragments.profile;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.card.Card;
import com.urgentrn.urncexchange.ui.adapter.GiftCardAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_gift_card)
public class GiftCardFragment extends BaseFragment {

    @ViewById
    Toolbar toolBar;

    @ViewById
    EditText editSearch;

    @ViewById
    RecyclerView recyclerView;

    private GiftCardAdapter adapter;

    @AfterViews
    protected void init() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);
        toolBar.setNavigationIcon(R.mipmap.ic_gift);

        editSearch.setVisibility(View.GONE);

        adapter = new GiftCardAdapter(position -> onDetail(position));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        updateView();
    }

    @Override
    public void updateView() {
        final List<Card> myCards = new ArrayList<>();
        final String keyword = editSearch.getText().toString().toLowerCase();
        for (Card card : AppData.getInstance().getMyCards()) {
            if (card.isGift() && !card.getStatus().equals("terminated")) {
                if (keyword.isEmpty() || card.getMeta("giftCardName").toLowerCase().contains(keyword)) {
                    myCards.add(card);
                }
            }
        }
        adapter.setData(myCards);
    }

    private void onDetail(int position) {
        final Card card = adapter.getItem(position);

        if (card.getStatus().equals("processing")) {
            showAlert(getString(R.string.gift_still_processing));
            return;
        }

        final GiftCardDetailFragment fragment = new GiftCardDetailFragment_();
        final Bundle args = new Bundle();
        args.putSerializable("card", card);
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }

    @TextChange(R.id.editSearch)
    void onKeywordTextChange(CharSequence s) {
        updateView();
    }

    @EditorAction(R.id.editSearch)
    boolean onSearchDone(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            Utils.hideKeyboard(getContext(), editSearch);
            return true;
        }
        return false;
    }
}
