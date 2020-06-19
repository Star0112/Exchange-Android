package com.urgentrn.urncexchange.ui.fragments.card;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.BuildConfig;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.card.CardDetail;
import com.urgentrn.urncexchange.models.card.CardInfo;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetAvailableCardsResponse;
import com.urgentrn.urncexchange.ui.adapter.CardDetailAdapter;
import com.urgentrn.urncexchange.ui.adapter.CardDetailGroupAdapter;
import com.urgentrn.urncexchange.ui.adapter.CardPagerAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.CardOrderDialog;
import com.urgentrn.urncexchange.ui.dialogs.CardOrderDialog_;
import com.urgentrn.urncexchange.ui.view.CircleIndicator;
import com.urgentrn.urncexchange.ui.view.ZoomOutPageTransformer;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.ListViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_card_order)
public class CardOrderFragment extends BaseFragment implements ViewTreeObserver.OnScrollChangedListener, ApiCallback {

    @ViewById
    NestedScrollView scrollView;

    @ViewById
    TextView txtTitle, txtTitle1, txtTitle2;

    @ViewById
    CardView cardView1, cardView2;

    @ViewById
    ViewPager viewPager;

    @ViewById
    CircleIndicator indicator;

    @ViewById
    RecyclerView recyclerView;

    @ViewById
    View llUnavailable, llOrder, toolBar, imgBackground, llDetail;

    @ViewById
    ImageView imgCardSmall, imgArrow;

    @ViewById
    ExpandableListView listView;

    @ViewById
    Button btnOrder;

    private CardPagerAdapter cardAdapter;
    private CardDetailAdapter detailAdapter;
    private CardDetailGroupAdapter groupAdapter;
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            if (cardAdapter.getCount() == 0) { // when no available cards except gift cards
                showUnavailableView();
                return;
            }
            selectedCard = cardAdapter.getItem(i);

            txtTitle.setText(selectedCard.getTitle());
            Glide.with(getContext())
                    .load(selectedCard.getFrontImage())
                    .into(imgCardSmall);
            cardView1.setCardBackgroundColor(getResources().getColor(i == 0 ? R.color.colorWhite : R.color.colorBackground));
            cardView2.setCardBackgroundColor(getResources().getColor(i == 1 ? R.color.colorWhite : R.color.colorBackground));
            cardView1.setCardElevation(1 - i);
            cardView2.setCardElevation(i);
            txtTitle1.setAlpha(0.4f + 0.6f * (1 - i));
            txtTitle2.setAlpha(0.4f + 0.6f * i);

            updateDetailView();

            final int color = getResources().getColor(i == 0 ? R.color.colorCardBackground1 : R.color.colorCardBackground2);
            setStatusBarColor(color);
            toolBar.setBackgroundColor(color);
            imgBackground.setBackgroundTintList(ColorStateList.valueOf(color));
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
    private CardOrderDialog dialog = new CardOrderDialog_();
    private CardInfo selectedCard;

    private boolean isUpgrade;
    private boolean isExpanded = true;

    @AfterViews
    protected void init() {
        if (getArguments() != null) {
            isUpgrade = getArguments().getBoolean("is_upgrade");
        }

        if (isUpgrade) {
            setToolBar(false);
        }

        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(getResources().getDimensionPixelSize(R.dimen.card_width), 0, getResources().getDimensionPixelSize(R.dimen.card_width), 0);
        viewPager.setPageMargin(0);

        cardAdapter = new CardPagerAdapter(isUpgrade);
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
        viewPager.setAdapter(cardAdapter);
        indicator.setViewPager(viewPager);

        detailAdapter = new CardDetailAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(detailAdapter);

        groupAdapter = new CardDetailGroupAdapter();
        listView.setAdapter(groupAdapter);
        listView.setOnGroupClickListener(((parent, v, groupPosition, id) -> true));

//        onViewDetail();

        btnOrder.setText(isUpgrade ? R.string.button_upgrade : R.string.order_now);

        if (isUpgrade) {
            updateCardsView((List<CardInfo>)getArguments().getSerializable("cards"));
        } else {
            if (AppData.getInstance().getAvailableCards().size() == 0) {
                ApiClient.getInterface().getAvailableCards().enqueue(new AppCallback<>(this));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (AppData.getInstance().getAvailableCards().size() > 0) {
            updateView();
        }
    }

    @Override
    public void updateView() {
        updateCardsView(AppData.getInstance().getAvailableCards());
    }

    private void updateCardsView(final List<CardInfo> data) {
        if (data.size() == 0) {
            showUnavailableView();
            return;
        }

        llOrder.setVisibility(View.VISIBLE);
        cardAdapter.setData(data);
        indicator.getDataSetObserver().onChanged();
        pageChangeListener.onPageSelected(0);

        if (cardAdapter.getCount() > 1) {
            cardView1.setVisibility(View.VISIBLE);
            cardView2.setVisibility(View.VISIBLE);
            txtTitle1.setText(cardAdapter.getItem(0).getTitle().replace("Exchange ", ""));
            txtTitle2.setText(cardAdapter.getItem(1).getTitle().replace("Exchange ", ""));
        } else {
            cardView1.setVisibility(View.GONE);
            cardView2.setVisibility(View.GONE);
        }
    }

    private void showUnavailableView() {
        setStatusBarColor(getResources().getColor(R.color.colorCardBackground3));
        llUnavailable.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {getResources().getColor(R.color.colorCardBackground3), getResources().getColor(R.color.colorCardBackground4)}));
        llUnavailable.setVisibility(View.VISIBLE);

        llOrder.setVisibility(View.GONE);
        llDetail.setVisibility(View.GONE);
        btnOrder.setVisibility(View.GONE);
    }

    private void updateDetailView() {
        final List<CardDetail> features = new ArrayList<>();
        final List<CardDetail> groups = new ArrayList<>();
        for (CardDetail detail : selectedCard.getDetails()) {
            if (detail.getType().equals("feature")) {
                features.add(detail);
            } else if (detail.getType().equals("group")) {
                groups.add(detail);
            }
        }
        detailAdapter.setData(features);
        groupAdapter.setData(groups);
        for (int i = 0; i < groups.size(); i ++) {
            listView.expandGroup(i);
        }
        ListViewUtils.setListViewHeightBasedOnChildren(listView);
    }

    @Click(R.id.cardView1)
    void onCard1Clicked() {
        viewPager.setCurrentItem(0);
    }

    @Click(R.id.cardView2)
    void onCard2Clicked() {
        viewPager.setCurrentItem(1);
    }

    @Click(R.id.llDetail)
    void onViewDetail() {
        isExpanded = !isExpanded;
        imgArrow.setImageResource(isExpanded ? R.mipmap.ic_add : R.mipmap.ic_add);
        listView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Click(R.id.btnOrder)
    void onOrderClicked() {
        if (selectedCard == null) return;

        if (isUpgrade) {
            onOrder(getArguments().getBoolean("is_virtual"));
        } else {
            final List<String> cardTypes = new ArrayList<>();
            cardTypes.add(getString(R.string.physical_card));
            if (selectedCard.isVirtual()) {
                cardTypes.add(getString(R.string.virtual_card));
            }
            final String[] items = new String[cardTypes.size()];
            cardTypes.toArray(items);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.select_card_type)
                    .setItems(items, (dialog, which) -> {
                        onOrder(which == 1);
                    })
                    .show();
        }
    }

    private void onOrder(boolean isVirtual) {
        if (getUser() == null) return;
        if (getUser().getTierLevel() < 2) {
            showVerifyDialog(null);
            return;
        }
        if (dialog.getDialog() != null && dialog.getDialog().isShowing()) return;
        if (AppData.getInstance().getWallets().size() == 0) return;
        final Bundle args = new Bundle();
        args.putSerializable("card", selectedCard);
        args.putBoolean("virtual", isVirtual);
        args.putBoolean("upgrade", isUpgrade);
        dialog.setArguments(args);
        dialog.show(getChildFragmentManager(), isUpgrade ? Constants.VerifyType.CARD_UPGRADE.name() : Constants.VerifyType.CARD_ORDER.name());
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
        if (BuildConfig.DEBUG) showToast(message, false);
        updateView();
    }

    @Override
    public void onScrollChanged() {
        final int offset = scrollView.getScrollY();
        final int visibleOffset = 200;
        toolBar.setAlpha(offset < visibleOffset ? 0 : (offset - visibleOffset) / 100f);
    }

    @Override
    public void onBackPressed() {
        scrollView.getViewTreeObserver().removeOnScrollChangedListener(this);
        super.onBackPressed();
    }
}
