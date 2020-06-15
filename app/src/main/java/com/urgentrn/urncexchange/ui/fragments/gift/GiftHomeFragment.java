package com.urgentrn.urncexchange.ui.fragments.gift;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.card.GiftCard;
import com.urgentrn.urncexchange.models.card.GiftCardData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetGiftCardsResponse;
import com.urgentrn.urncexchange.ui.adapter.GiftCardAvailableAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.fragments.profile.BuyGiftCardFragment;
import com.urgentrn.urncexchange.ui.fragments.profile.BuyGiftCardFragment_;
import com.urgentrn.urncexchange.ui.fragments.profile.GiftCardFragment_;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.fragment_gift_cards)
public class GiftHomeFragment extends BaseFragment implements ApiCallback {

    @ViewById
    Toolbar toolBar;

    @ViewById
    EditText editSearch;

    @ViewById
    View llNormal, progressSearch, imgCloseSearch;

    @ViewById
    TabLayout tabLayout;

    @ViewById
    TextView txtHeader;

    @ViewById
    RecyclerView recyclerView, recyclerSearch;

    @ViewById
    UltimateRecyclerView recyclerAll;

    private GiftCardAvailableAdapter adapterRef, adapterAll, adapterSearch;

    private List<GiftCard> availableCards, allCards = new ArrayList<>();
    private int page = 0, totalPages = 1;
    private static double fee;
    private static final int spanCount = 2;
    private static final boolean useFeaturedBrands = false;
    private static final boolean useSameCards = false;

    @AfterViews
    protected void init() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        toolBar.getMenu()
                .add(R.string.your_cards)
                .setIcon(R.mipmap.ic_menu_card)
                .setOnMenuItemClickListener(item -> onCardClicked())
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        if (useFeaturedBrands) {
            txtHeader.setVisibility(View.GONE);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerAll.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        recyclerAll.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            adapterRef = new GiftCardAvailableAdapter(position -> onBuyDetail(adapterRef.getItem(position)));
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
            recyclerView.setAdapter(adapterRef);
        } else {
            tabLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            recyclerAll.setVisibility(View.VISIBLE);
        }

        adapterAll = new GiftCardAvailableAdapter(position -> onBuyDetail(adapterAll.getItem(position)));
        adapterAll.setData(allCards);

        if (Constants.USE_NESTED_LIST_HEADER) {
            final TextView txtHeader = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_normal_header_transaction, null);
            txtHeader.setText(R.string.gift_cards);
            recyclerAll.setNormalHeader(txtHeader);
        }

        recyclerAll.mRecyclerView.setClipToPadding(false);
        recyclerAll.mRecyclerView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.default_padding), 0, getResources().getDimensionPixelSize(R.dimen.default_padding));
        recyclerAll.setLayoutManager(new BasicGridLayoutManager(getContext(), spanCount, adapterAll));
        if (!useSameCards) {
            recyclerAll.setDefaultOnRefreshListener(() -> getPagedGiftCards(1));
            recyclerAll.setLoadMoreView(R.layout.bottom_progressbar);
            recyclerAll.setOnLoadMoreListener(((itemsCount, maxLastVisiblePosition) -> getPagedGiftCards(page + 1)));
        }
        recyclerAll.setAdapter(adapterAll);

        recyclerSearch.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        adapterSearch = new GiftCardAvailableAdapter(position -> onBuyDetail(adapterSearch.getItem(position)));
        recyclerSearch.setAdapter(adapterSearch);

        if (useSameCards) {
            availableCards = AppData.getInstance().getAvailableGiftCards();
            if (availableCards == null || availableCards.size() == 0) {
                final HashMap<String, Object> options = new HashMap<>();
                ApiClient.getInterface().getAvailableGiftCards(options).enqueue(new AppCallback<>(getContext(), this));
            }
        } else {
            if (useFeaturedBrands) {
                final HashMap<String, Object> options = new HashMap<>();
                options.put("featured", true);
                ApiClient.getInterface().getAvailableGiftCards(options).enqueue(new AppCallback<>(getContext(), this));
            }

            getPagedGiftCards(page + 1);
        }

        updateView();
    }

    @Override
    public void updateView() {
        if (availableCards != null && availableCards.size() > 0) {
            if (useFeaturedBrands) {
                adapterRef.setData(availableCards);
            }
            if (useSameCards) {
                adapterAll.setData(availableCards);
            }
        }
    }

    private boolean onCardClicked() {
        if (AppData.getInstance().getMyCards() == null) {
            showToast(getString(R.string.error_no_gift_cards), false);
            return true;
        }
        ((BaseFragment)getParentFragment()).replaceFragment(new GiftCardFragment_(), false);

        return true;
    }

    private void getPagedGiftCards(int page) {
        if (page > totalPages) return;
        final HashMap<String, Object> options = new HashMap<>();
        options.put("page", page);
        options.put("limit", Constants.DEFAULT_LIST_LIMIT_SMALL);
        ApiClient.getInterface().getAvailableGiftCards(options).enqueue(new AppCallback<>(new ApiCallback() {
            @Override
            public void onResponse(BaseResponse response) {
                if (!isAdded()) return;
                if (response instanceof GetGiftCardsResponse) {
                    final GiftCardData data = ((GetGiftCardsResponse)response).getData();
                    GiftHomeFragment.this.page = data.getPage();
                    totalPages = data.getTotalPages();
                    if (data.getPage() == 1) {
                        recyclerAll.reenableLoadmore();
                        recyclerAll.setRefreshing(false);
                        allCards.clear();
                    }
                    if (data.getPage() == totalPages) {
                        if (recyclerAll.isLoadMoreEnabled()) {
                            recyclerAll.disableLoadmore();
                        }
                    }
                    final List<GiftCard> newCards = data.getRefs();
                    allCards.addAll(newCards);
                    if (data.getPage() == 1) {
                        adapterAll.notifyDataSetChanged();
                    } else {
                        adapterAll.notifyItemRangeChanged(allCards.size() - newCards.size() + 1, newCards.size());
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                if (isAdded() && recyclerAll.mExchangeRefreshLayout.isRefreshing()) {
                    recyclerAll.setRefreshing(false);
                }
            }
        }));
    }

    private void onBuyDetail(GiftCard card) {
        final BuyGiftCardFragment fragment = new BuyGiftCardFragment_();
        final Bundle args = new Bundle();
        args.putSerializable("card", card);
        args.putDouble("fee", fee);
        fragment.setArguments(args);
        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }

    @EditorAction(R.id.editSearch)
    boolean onSearchDone(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            Utils.hideKeyboard(getContext(), editSearch);

            final String keyword = editSearch.getText().toString();
            if (!keyword.isEmpty()) {
                progressSearch.setVisibility(View.VISIBLE);
                imgCloseSearch.setVisibility(View.GONE);
                final HashMap<String, Object> options = new HashMap<>();
                options.put("search", keyword);
                ApiClient.getInterface().getAvailableGiftCards(options).enqueue(new AppCallback<>(new ApiCallback() {
                    @Override
                    public void onResponse(BaseResponse response) {
                        if (!isAdded()) return;
                        if (response instanceof GetGiftCardsResponse) {
                            final GiftCardData data = ((GetGiftCardsResponse)response).getData();
                            final List<GiftCard> searchedCards = data.getRefs();
                            adapterSearch.setData(searchedCards);

                            llNormal.setVisibility(View.GONE);
                            recyclerSearch.setVisibility(View.VISIBLE);
                        }
                        progressSearch.setVisibility(View.GONE);
                        imgCloseSearch.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(String message) {
                        progressSearch.setVisibility(View.GONE);
                    }
                }));
            }

            return true;
        }
        return false;
    }

    @Click(R.id.imgCloseSearch)
    void onCloseSearch() {
        llNormal.setVisibility(View.VISIBLE);
        recyclerSearch.setVisibility(View.GONE);
        imgCloseSearch.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetGiftCardsResponse) {
            final GiftCardData data = ((GetGiftCardsResponse)response).getData();
            availableCards = data.getRefs();
            fee = data.getFee();
            if (useSameCards) {
                AppData.getInstance().setAvailableGiftCards(availableCards);
            }
            updateView();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
