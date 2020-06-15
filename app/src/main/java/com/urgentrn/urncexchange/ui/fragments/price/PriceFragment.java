package com.urgentrn.urncexchange.ui.fragments.price;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.tabs.TabLayout;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.PortfolioData;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.request.UpdateUserRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.models.response.PortfolioResponse;
import com.urgentrn.urncexchange.models.response.SymbolResponse;
import com.urgentrn.urncexchange.ui.MainActivity;
import com.urgentrn.urncexchange.ui.adapter.CoinExchangeableAdapter;
import com.urgentrn.urncexchange.ui.adapter.WalletAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.fragments.wallet.WalletFragment;
import com.urgentrn.urncexchange.ui.fragments.wallet.WalletFragment_;
import com.urgentrn.urncexchange.ui.view.ImageLineChartRenderer;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.GraphUtils;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@EFragment(R.layout.fragment_price)
public class PriceFragment extends BaseFragment implements ApiCallback {

    // drawer

    @ViewById(R.id.drawerLayout)
    DrawerLayout drawer;

    @ViewById
    Toolbar toolBar, toolBarDrawer;

    @ViewById
    UltimateRecyclerView recyclerCoins;

    @ViewById
    EditText editSearch;

    @ViewById
    View btnDone;

    @ViewById
    ImageView btnFavorite;

    @ViewById
    TextView txtCrypto, txtFiat, txtWalletsCount, txtTotalBalance;

    private boolean isCrypto = true;
    private CoinExchangeableAdapter adapterCoin;
    private ItemTouchHelper itemTouchHelper;
    private int unfavoriteCount;

    // main

    @ViewById
    TextView txtBalanceUSD, txtSymbol, txtBalanceChange, txtSelectedDate;

    @ViewById
    ImageView imgBalanceChange;

    @ViewById(R.id.txtPeriod)
    TabLayout tabLayout;

    @ViewById(R.id.chartView)
    LineChart chart;

    @ViewById
    RecyclerView recyclerView;

    @ViewById
    View llBalanceChange, llGradient, spacer, txtPeriod, progress;

    private WalletAdapter adapter;

    private int selectedPeriod = 2;

    @AfterViews
    protected void init() {
        adapter = new WalletAdapter(getParentFragment());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        setupDrawer();
        setupTab();
        initGraph();

        updateView(null);
        updateBalanceView();
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
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolBar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                Utils.hideKeyboard(getContext(), getView());
                editSearch.setText(null);
            }
        };
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(view -> {
            drawer.openDrawer(GravityCompat.START);
        });
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolBarDrawer.setNavigationOnClickListener(v -> onCloseDrawer());

        adapterCoin = new CoinExchangeableAdapter(new ArrayList<>(), position -> {
            if (position == -9999) { // when Edit clicked
                btnFavorite.setVisibility(View.GONE);
                btnDone.setVisibility(View.VISIBLE);
                itemTouchHelper.attachToRecyclerView(recyclerCoins.mRecyclerView);
            } else if (position >= 0) {
                drawer.closeDrawer(GravityCompat.START);
                final Wallet selectedWallet = adapterCoin.getItem(position);
                if (false && selectedWallet.getSymbolData().isCurrency()) {
                    final WalletFragment fragment = new WalletFragment_();
                    final Bundle args = new Bundle();
                    args.putInt("position", position);
                    fragment.setArguments(args);

                    ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
                } else {
                    ((PriceContainerFragment)getParentFragment()).updateWalletPosition(selectedWallet);
                }
            } else { // when exchange area clicked
                final Wallet selectedWallet = adapterCoin.getItem(-position - 1);
                final HashMap<String, Boolean> request = new HashMap<>();
                request.put("favorite", !selectedWallet.isFavorite());
                ApiClient.getInterface()
                        .addFavoriteWallet(selectedWallet.getSymbol(), request)
                        .enqueue(new AppCallback<>(new ApiCallback() {
                            @Override
                            public void onResponse(BaseResponse response) {
                                if (response instanceof GetUserResponse) {
                                    if (ExchangeApplication.getApp().getUser().getFavoriteFunds() == 1) {
                                        ((MainActivity)getActivity()).getMyWallets(true);
                                    } else {
                                        selectedWallet.setFavorite(!selectedWallet.isFavorite());
                                        adapterCoin.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        }));
            }
        }, viewHolder -> itemTouchHelper.startDrag(viewHolder));
        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        recyclerCoins.setLayoutManager(layoutManager1);
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerCoins.addItemDecoration(decoration);
        recyclerCoins.setLoadMoreView(R.layout.item_coin_footer);
        recyclerCoins.setOnLoadMoreListener((itemsCount, maxLastVisiblePosition) -> {});
        recyclerCoins.setAdapter(adapterCoin);

        itemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(adapterCoin));

        updateFavoriteView();
    }

    @TextChange(R.id.editSearch)
    void onKeywordTextChange(CharSequence s) {
        updateCoinView();
    }

    @EditorAction(R.id.editSearch)
    boolean onSearchDone(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            Utils.hideKeyboard(getContext(), editSearch);
            return true;
        }
        return false;
    }

    @Click(R.id.btnFavorite)
    void onFavorite() {
        if (getUser() == null) return;
        final int isFavoriteEnabled = 1 - getUser().getFavoriteFunds();
        if (isFavoriteEnabled == 1) { // to prevent favorite enabled when no favorite wallets
            boolean hasFavorite = false;
            for (Wallet wallet : AppData.getInstance().getWallets()) {
                if (wallet.isFavorite()) {
                    hasFavorite = true;
                    break;
                }
            }
            if (!hasFavorite) {
                showAlert(getString(R.string.no_favorite_wallets));
                return;
            }
        }
        final UpdateUserRequest request = new UpdateUserRequest();
        request.setFavoriteFunds(isFavoriteEnabled);
        ApiClient.getInterface().updateUser(request).enqueue(new AppCallback<>(getContext(), new ApiCallback() {
            @Override
            public void onResponse(BaseResponse response) {
                getUser().setFavoriteFunds(isFavoriteEnabled);
                updateFavoriteView();
                ((MainActivity)getActivity()).getMyWallets(true);
            }

            @Override
            public void onFailure(String message) {

            }
        }));
    }

    @Click(R.id.btnDone)
    void onDone() {
        final List<String> sortedSymbols = new ArrayList<>();
        for (Wallet wallet : adapterCoin.data) {
            sortedSymbols.add(wallet.getSymbol());
        }
        ExchangeApplication.getApp().getPreferences().setFavoriteSymbols(sortedSymbols);
        adapterCoin.setData(adapterCoin.data);

        unfavoriteCount = adapterCoin.symbols.size();
        if (unfavoriteCount == 0) {
            ((MainActivity)getActivity()).getMyWallets(true);
        } else {
            final HashMap<String, Boolean> request = new HashMap<>();
            request.put("favorite", false);
            for (int i = 0; i < adapterCoin.symbols.size(); i ++) {
                ApiClient.getInterface()
                        .addFavoriteWallet(adapterCoin.symbols.get(i), request)
                        .enqueue(new AppCallback<>(getContext(), new ApiCallback() {
                            @Override
                            public void onResponse(BaseResponse response) {
                                if (response instanceof GetUserResponse) {
                                    if (-- unfavoriteCount == 0) {
                                        ((MainActivity)getActivity()).getMyWallets(true);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        }));
            }
        }

        btnFavorite.setVisibility(View.VISIBLE);
        btnDone.setVisibility(View.GONE);
        adapterCoin.updateMode(false);
        itemTouchHelper.attachToRecyclerView(null);
    }

    @Click(R.id.txtCrypto)
    void onSelectCrypto() {
        txtCrypto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        txtFiat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPaleGrey)));
        isCrypto = true;
        updateCoinView();
    }

    @Click(R.id.txtFiat)
    void onSelectFiat() {
        txtCrypto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPaleGrey)));
        txtFiat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        isCrypto = false;
        updateCoinView();
    }

    private void updateCoinView() {
        final List<Wallet> filteredWallets = new ArrayList<>();
        for (Wallet wallet : isCrypto ? AppData.getInstance().getCryptoWallets(false) : AppData.getInstance().getCurrencyWallets(true)) {
            final String keyword = editSearch.getText().toString().toLowerCase();
            if (wallet.getSymbol().toLowerCase().contains(keyword) || wallet.getTitle().toLowerCase().contains(keyword)) {
                filteredWallets.add(wallet);
            }
        }
        adapterCoin.setData(filteredWallets);
        txtWalletsCount.setText(String.format(Locale.US, "(%d)", filteredWallets.size()));
    }

    private void updateFavoriteView() {
        if (getUser() == null) return;
        if (Constants.USE_FAVORITES) {
            if (getUser().getFavoriteFunds() == 1) {
                btnFavorite.setImageResource(R.mipmap.ic_favorite_filled);
            } else {
                btnFavorite.setImageResource(R.mipmap.ic_favorite);
            }
        } else {
            btnFavorite.setVisibility(View.GONE);
        }
        adapterCoin.getCustomLoadMoreView().findViewById(R.id.txtEdit).setVisibility(!Constants.USE_FAVORITES || getUser().getFavoriteFunds() == 0 ? View.GONE : View.VISIBLE);
    }

    public void onCloseDrawer() {
        if (adapterCoin.isInEditMode) {
            adapterCoin.data = new ArrayList<>(adapterCoin.getData());

            btnFavorite.setVisibility(View.VISIBLE);
            btnDone.setVisibility(View.GONE);
            adapterCoin.updateMode(false);
            itemTouchHelper.attachToRecyclerView(null);
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void setupTab() {
        tabLayout.addTab(tabLayout.newTab().setText("1H"));
        tabLayout.addTab(tabLayout.newTab().setText("1D"));
        tabLayout.addTab(tabLayout.newTab().setText("1W"));
        tabLayout.addTab(tabLayout.newTab().setText("1M"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedPeriod = tab.getPosition();
                getPortfolioData(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                getPortfolioData(true);
            }
        });
        tabLayout.getTabAt(selectedPeriod).select();
    }

    private void initGraph() {
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getXAxis().setDrawLimitLinesBehindData(false);
        chart.getAxisLeft().setDrawTopYLabelEntry(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisLeft().setDrawLimitLinesBehindData(false);
        chart.getAxisRight().setDrawTopYLabelEntry(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawLimitLinesBehindData(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setScaleEnabled(false);
        chart.setNoDataText(null);
    }

    private void getPortfolioData(boolean showProgress) {
        if (showProgress) progress.setVisibility(View.VISIBLE);
        ApiClient.getInterface()
                .getPortfolioData("minutes", GraphUtils.getPeriodFactor(selectedPeriod), GraphUtils.getInterval(selectedPeriod))
                .enqueue(new AppCallback<>(new ApiCallback() {
                    @Override
                    public void onResponse(BaseResponse response) {
                        if (response instanceof PortfolioResponse) {
                            final List<PortfolioData> data = ((PortfolioResponse)response).getData();
                            AppData.getInstance().setPortfolioData(data);

                            if (!isAdded()) return;

                            updateBalanceView();
                            progress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        if (!isAdded()) return;
                        progress.setVisibility(View.GONE);
                    }
                }));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<Wallet> data) {
        adapter.setData(AppData.getInstance().getCryptoWallets(false));

        if (data != null) {
            if (data.size() == 0) { // assume favorite enabled and no favorites
                getUser().setFavoriteFunds(1);
                onFavorite();
                return;
            }

            final boolean showRealTime = true; // add portfolio data to real time values
            final Wallet wallet = WalletUtils.getDefaultCurrencyWallet();
            if (showRealTime || wallet != null && !wallet.getSymbol().equals(txtSymbol.getText().toString())) {
                getPortfolioData(false);
            }
        }

        updateCoinView(); // drawer
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSymbolData(SymbolResponse data) {
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateChartView(String symbol) {
        for (int i = 0; i < adapter.getItemCount(); i ++) {
            if (adapter.getItem(i).getSymbol().equalsIgnoreCase(symbol)) {
                adapter.notifyItemChanged(i);
                adapterCoin.notifyItemChanged(i);
                return;
            }
        }
    }

    private void updateBalanceView() {
        final List<PortfolioData> data = AppData.getInstance().getPortfolioData();
        if (data == null || data.size() == 0) return;

        if (selectedPeriod == 2) {
            if (Constants.USE_PORTFOLIO_BALANCE) {
                txtBalanceUSD.setText(data.get(0).getBalanceFormatted());
                txtTotalBalance.setText(data.get(0).getBalanceFormatted());
            }
            final Wallet wallet = WalletUtils.getDefaultCurrencyWallet();
            if (wallet == null || wallet.getCurrencySymbol() == data.get(0).getBalanceFormatted().charAt(0)) {
                txtSymbol.setText(wallet != null ? wallet.getSymbol() : "USD");
            }
        }
        if (!Constants.USE_PORTFOLIO_BALANCE) {
            final String balance = String.format("%c%s", data.get(0).getBalanceFormatted().charAt(0), Utils.formattedNumber(WalletUtils.getTotalBalance(), 0, 2));
            txtBalanceUSD.setText(balance);
            txtTotalBalance.setText(balance);
        }

        final double oldBalance = data.get(data.size() - 1).getBalance();
        final double balanceChange = data.get(0).getBalance() - oldBalance;
        imgBalanceChange.setImageResource(balanceChange < 0 ? R.mipmap.arrow_down : R.mipmap.arrow_up);
        txtBalanceChange.setText(String.format(Locale.US, "%c%s (%s%%)",
                data.get(0).getBalanceFormatted().charAt(0),
                Utils.formattedNumber(Math.abs(balanceChange), 0, 2),
                Utils.formattedNumber(oldBalance == 0 ? 0 : balanceChange / oldBalance * 100, 0, 2)
        ));
        txtBalanceChange.setTextColor(getResources().getColor(balanceChange < 0 ? R.color.colorAccent : R.color.colorGreen));

        updateBalanceChartView(data);
    }

    private void updateBalanceChartView(List<PortfolioData> data) {
        if (data.size() == 0) return;

        final List<PortfolioData> chartData = new ArrayList<>(data);
        Collections.reverse(chartData);

        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                try {
                    return chartData.get((int) value).getDate();
                } catch (Exception e) {
                    return null;
                }
            }
        });
        List<Entry> entries = new ArrayList<>();
        for (int i = 0 ; i < chartData.size() ; i ++) {
            entries.add(new Entry(i, (float) chartData.get(i).getBalance()));
        }
        final LineDataSet dataSet = new LineDataSet(entries, "");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setRenderer(new ImageLineChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler(), getResources().getColor(R.color.colorAccent)));

        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(255);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircleHole(true);
        dataSet.setDrawCircles(false);
        dataSet.setHighlightEnabled(true);
        dataSet.setHighLightColor(getResources().getColor(R.color.colorAccent));
        dataSet.setHighlightLineWidth(1);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setColor(Color.rgb(246, 205, 199));
        dataSet.setLineWidth(1);
        dataSet.setFillDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Color.rgb(255, 226, 222), Color.rgb(255, 242, 238)}));
        llGradient.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Color.rgb(255, 242, 238), Color.rgb(255, 251, 245)}));

        chart.getData().setHighlightEnabled(false);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                txtBalanceUSD.setText(String.format("%c%s", data.get(0).getBalanceFormatted().charAt(0), Utils.formattedNumber(entries.get((int) e.getX()).getY(), 2, 2)));
                final String currentDateStr = chartData.get((int) e.getX()).getDate();
                txtSelectedDate.setText(Utils.dateToString(Utils.stringToDate(currentDateStr, null, "UTC"), null));
            }

            @Override
            public void onNothingSelected() {

            }
        });
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                chart.getData().setHighlightEnabled(true);
                llBalanceChange.setVisibility(View.INVISIBLE);
                txtSelectedDate.setVisibility(View.VISIBLE);

                spacer.setVisibility(View.VISIBLE);
                txtPeriod.setVisibility(View.GONE);
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                chart.getData().setHighlightEnabled(false);
                llBalanceChange.setVisibility(View.VISIBLE);
                txtSelectedDate.setVisibility(View.INVISIBLE);
                if (Constants.USE_PORTFOLIO_BALANCE) {
                    txtBalanceUSD.setText(data.get(0).getBalanceFormatted());
                } else {
                    txtBalanceUSD.setText(String.format("%c%s", data.get(0).getBalanceFormatted().charAt(0), Utils.formattedNumber(WalletUtils.getTotalBalance(), 0, 2)));
                }

                spacer.setVisibility(View.GONE);
                txtPeriod.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
        chart.invalidate();
    }

    @Override
    public void onResponse(BaseResponse response) {

    }

    @Override
    public void onFailure(String message) {

    }
}
