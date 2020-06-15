package com.urgentrn.urncexchange.ui.fragments.price;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Market;
import com.urgentrn.urncexchange.models.MarketCap;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.TransactionsHistory;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetAccountsResponse;
import com.urgentrn.urncexchange.models.response.MarketResponse;
import com.urgentrn.urncexchange.models.response.TransactionsResponse;
import com.urgentrn.urncexchange.ui.adapter.AssetAdapter;
import com.urgentrn.urncexchange.ui.adapter.StatListAdapter;
import com.urgentrn.urncexchange.ui.adapter.TransactionAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.dialogs.BuySellDialog;
import com.urgentrn.urncexchange.ui.dialogs.BuySellDialog_;
import com.urgentrn.urncexchange.ui.dialogs.DepositCurrencyDialog;
import com.urgentrn.urncexchange.ui.dialogs.DepositCurrencyDialog_;
import com.urgentrn.urncexchange.ui.dialogs.DepositDialog;
import com.urgentrn.urncexchange.ui.dialogs.DepositDialog_;
import com.urgentrn.urncexchange.ui.fragments.wallet.WalletFragment;
import com.urgentrn.urncexchange.ui.fragments.wallet.WalletFragment_;
import com.urgentrn.urncexchange.ui.transactions.SendInputActivity_;
import com.urgentrn.urncexchange.ui.view.ImageLineChartRenderer;
import com.urgentrn.urncexchange.ui.view.LockableScrollView;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.GraphUtils;
import com.urgentrn.urncexchange.utils.ListViewUtils;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@EFragment(R.layout.fragment_price_detail)
public class PriceDetailFragment extends BaseFragment implements ViewTreeObserver.OnScrollChangedListener, ApiCallback {

    @ViewById
    Toolbar toolBar;

    private MenuItem btnDeposit, btnSend;

    @ViewById
    View llHeader, llGradient, llPriceChange, llMarketStats, llBuySell;

    @ViewById
    LockableScrollView lockableScrollView;

    @ViewById
    TextView txtHeaderPrice, txtCoin, txtPriceSymbol, txtPriceInteger, txtPriceDecimal, txtPriceChange, txtDate, txtSelectedDate, txtWallet, txtWalletUSD, txtWalletPrice, txtMoreAssets;

    @ViewById
    ImageView imgHeaderCoin, imgCoin, imgPriceChange, imgWallet, imgArrow;

    @ViewById(R.id.chartView)
    LineChart chart;

    @ViewById(R.id.txtPeriod)
    TabLayout tabLayout;

    @ViewById
    Button btnBuy, btnSell;

    @ViewById
    ExpandableListView listView;

    @ViewById
    RecyclerView recyclerView, recyclerTransactions;

    private StatListAdapter statsAdapter;
    private AssetAdapter adapter;
    private TransactionAdapter adapterTransaction;
    private final DepositDialog depositDialog = new DepositDialog_();
    private final DepositCurrencyDialog depositCurrencyDialog = new DepositCurrencyDialog_();
    private final BuySellDialog dialog = new BuySellDialog_();

    private int selectedPeriod = 2;
    private Wallet wallet;
    private List<Wallet> otherWallets = new ArrayList<>();

    @AfterViews
    protected void init() {
        toolBar.setNavigationOnClickListener(v -> onBackPressed());
        btnDeposit = toolBar.getMenu()
                .add(R.string.deposit)
                .setIcon(R.mipmap.ic_qr_code)
                .setOnMenuItemClickListener(item -> onDeposit());
        btnSend = toolBar.getMenu()
                .add(R.string.title_send)
                .setIcon(R.mipmap.ic_send)
                .setOnMenuItemClickListener(item -> onSend());

        lockableScrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        int position = 0;
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
        final List<Wallet> wallets = AppData.getInstance().getWallets();
        if (position < 0 || position >= wallets.size()) { // TODO: when does this happen?
            onBackPressed();
            return;
        }
        wallet = wallets.get(position);

        initGraph();
        setupTab();

        listView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            ListViewUtils.setListViewHeight(parent, groupPosition);
            return false;
        });
        // To set height when first loaded
        new Handler().postDelayed(() -> {
            if (listView == null) return;
            listView.performItemClick(null, 0, 0);
            listView.performItemClick(null, 0, 0);
        }, 100);

        adapter = new AssetAdapter(pos -> updateWallet(otherWallets.get(pos)));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        statsAdapter = new StatListAdapter(getContext());
        listView.setAdapter(statsAdapter);

        llBuySell.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Color.parseColor("#00ffffff"), Color.parseColor("#fefefe")}));

        updateView();

        adapterTransaction = new TransactionAdapter(null);
        recyclerTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerTransactions.setAdapter(adapterTransaction);
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

    private boolean onDeposit() {
        if (WalletUtils.getAssetRestriction(wallet.getSymbolData().getType(), true) == WalletUtils.AssetRestriction.UPGRADE) {
            showVerifyDialog(null);
        } else {
            final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
            if (symbol != null) {
                if (!symbol.isReceive()) {
                    showAlert(getString(R.string.error_maintenance, wallet.getTitle()));
                    return true;
                }
                final Bundle args = new Bundle();
                args.putSerializable("wallet", wallet);
                if (!symbol.isCurrency()) {
                    if (depositDialog.getDialog() != null && depositDialog.getDialog().isShowing()) return true;
                    depositDialog.setArguments(args);
                    depositDialog.show(getChildFragmentManager(), WalletUtils.TransactionType.DEPOSIT.name());
                } else {
                    if (depositCurrencyDialog.getDialog() != null && depositCurrencyDialog.getDialog().isShowing()) return true;
                    depositCurrencyDialog.setArguments(args);
                    depositCurrencyDialog.show(getChildFragmentManager(), WalletUtils.TransactionType.DEPOSIT.name());
                }
            }
        }
        return true;
    }

    private boolean onSend() {
        if (WalletUtils.getAssetRestriction(wallet.getSymbolData().getType(), false) == WalletUtils.AssetRestriction.UPGRADE) {
            showVerifyDialog(null);
        } else {
            final List<String> options = new ArrayList<>();
            options.add(getString(R.string.send_option_username));
            options.add(getString(R.string.send_option_email));
            options.add(getString(R.string.send_option_mobile));
            if (!wallet.getSymbolData().isCurrency()) {
                final double fee;
                final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
                if (symbol != null) {
                    if (!symbol.isSend()) {
                        showAlert(getString(R.string.error_maintenance, wallet.getTitle()));
                        return true;
                    }
                    fee = symbol.getFee();
                } else {
                    return true;
                }
                options.add(getString(R.string.send_option_address, Utils.formattedNumber(fee), wallet.getSymbol()));
            }
            final String[] items = new String[options.size()];
            options.toArray(items);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.send_to, wallet.getSymbol()))
                    .setItems(items, (dialog, which) -> {
                        final Intent intent = new Intent(getContext(), SendInputActivity_.class);
                        intent.putExtra("wallet", wallet);
                        intent.putExtra("type", Constants.SendType.values()[which]);
                        startActivity(intent);
                    })
                    .show();
        }
        return true;
    }

    @Click(R.id.llWallet)
    void onWallet() {
        final WalletFragment fragment = new WalletFragment_();
        final Bundle args = new Bundle();
        args.putInt("position", WalletUtils.getWalletIndex(wallet));
        fragment.setArguments(args);

        ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
    }

    @Click(R.id.cardViewHistory)
    void onViewHistory() {
        onWallet();
    }

    @Click(R.id.btnBuy)
    void onBuy() {
        onBuySell(WalletUtils.TransactionType.BUY.ordinal());
    }

    @Click(R.id.btnSell)
    void onSell() {
        onBuySell(WalletUtils.TransactionType.SELL.ordinal());
    }

    private void onBuySell(int position) {
        if (WalletUtils.getAssetRestriction(wallet.getSymbolData().getType(), true) == WalletUtils.AssetRestriction.UPGRADE) {
            showVerifyDialog(null);
        } else {
            if (position == WalletUtils.TransactionType.BUY.ordinal() && AppData.getInstance().getFlowData() == null) return;
            if (position != WalletUtils.TransactionType.SELL.ordinal() && AppData.getInstance().getAccounts() == null) return;
            if (dialog.getDialog() != null && dialog.getDialog().isShowing()) return;
            final Bundle args = new Bundle();
            args.putSerializable("wallet", wallet);
            args.putInt("type", position);
            dialog.setArguments(args);
            dialog.show(getChildFragmentManager(), WalletUtils.TransactionType.values()[position].name());
        }
    }

    private void setupTab() {
        tabLayout.addTab(tabLayout.newTab().setText("ALL"));
        tabLayout.addTab(tabLayout.newTab().setText("1M"));
        tabLayout.addTab(tabLayout.newTab().setText("1W"));
        tabLayout.addTab(tabLayout.newTab().setText("1D"));
        tabLayout.addTab(tabLayout.newTab().setText("1H"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedPeriod = tab.getPosition();
                getMarketData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                getMarketData();
            }
        });

        for(int i = 0; i < tabLayout.getTabCount(); i++) {
            final View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(getResources().getDimensionPixelSize(R.dimen.default_padding_small), 0, getResources().getDimensionPixelSize(R.dimen.default_padding_small), 0);
            tab.requestLayout();
        }
    }

    public void initGraph() {
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
    }

    private void getMarketData() {
        ApiClient.getInterface()
                .getGraphMarketData(wallet.getSymbol(), "minutes", GraphUtils.getPeriodFactor(4 - selectedPeriod), GraphUtils.getInterval(4 - selectedPeriod))
                .enqueue(new AppCallback<>(this));
    }

    public void updateWallet(Wallet wallet) {
        if (wallet != this.wallet) {
            this.wallet = wallet;
            updateView();
        }
    }

    @Override
    public void updateView() {
        updateDepositMenu(null);
        updateView(null);

        final Drawable drawable = getResources().getDrawable(R.mipmap.ic_back);
        drawable.setTintList(ColorStateList.valueOf(Utils.getTransparentColor((wallet.getColor()))));
        toolBar.setNavigationIcon(drawable);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            btnDeposit.setIconTintList(ColorStateList.valueOf(Utils.getTransparentColor(wallet.getColor())));
            btnSend.setIconTintList(ColorStateList.valueOf(Utils.getTransparentColor((wallet.getColor()))));
        } else {
            final Drawable drawable1 = getResources().getDrawable(R.mipmap.ic_qr_code);
            final Drawable drawable2 = getResources().getDrawable(R.mipmap.ic_send);
            drawable1.setTintList(ColorStateList.valueOf(Utils.getTransparentColor((wallet.getColor()))));
            drawable2.setTintList(ColorStateList.valueOf(Utils.getTransparentColor((wallet.getColor()))));
            btnDeposit.setIcon(drawable1);
            btnSend.setIcon(drawable2);
        }
        btnDeposit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        btnSend.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        final int color = Color.parseColor(wallet.getColor());
        btnBuy.setBackgroundTintList(ColorStateList.valueOf(color));
        btnSell.setBackgroundTintList(ColorStateList.valueOf(color));
        if (wallet.getSymbolData().isCurrency()) {
            btnBuy.setText(R.string.deposit);
            btnSell.setText(R.string.withdraw);
        } else {
            btnBuy.setText(R.string.buy);
            btnSell.setText(R.string.sell);
        }
        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgHeaderCoin);
        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgCoin);
        txtCoin.setText(wallet.getTitle());

        if (wallet.getSymbolData().isCurrency()) {
            llGradient.setBackgroundResource(R.color.colorTransparent);
            llPriceChange.setVisibility(View.GONE);
            chart.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
            if (selectedPeriod == 3 && AppData.getInstance().getMarketCap(wallet.getSymbol()) != null) {
                updatePriceView(AppData.getInstance().getMarketCap(wallet.getSymbol()).getPrice());
            } else {
                tabLayout.getTabAt(selectedPeriod).select();
            }
        }

        Glide.with(getContext())
                .load(wallet.getSymbolData().getColoredImage())
                .into(imgWallet);
        txtWallet.setText(String.format("Available %s: ", wallet.getSymbol().toUpperCase()));
        txtWalletPrice.setTextColor(color);
        imgArrow.setImageTintList(ColorStateList.valueOf(color));

        ApiClient.getInterface()
                .getTransactions(wallet.getSymbol(), 1, 2)
                .enqueue(new AppCallback<>(this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(List<Wallet> data) {
        txtMoreAssets.setText(wallet.getSymbolData().isBlockChain() ? R.string.more_assets : R.string.more_fiats);
        otherWallets.clear();
        for (Wallet wallet : this.wallet.getSymbolData().isBlockChain() ? AppData.getInstance().getCryptoWallets(false) : AppData.getInstance().getCurrencyWallets(true)) {
            if (wallet.getSymbol().equals(this.wallet.getSymbol())) {
                this.wallet = wallet;
            } else {
                otherWallets.add(wallet);
            }
        }
        adapter.setData(otherWallets);

        updateWalletValues();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDepositMenu(GetAccountsResponse data) {
        btnDeposit.setVisible(!wallet.getSymbolData().isCurrency() || false /* hide for all currency assets */ && (AppData.getInstance().getAccounts() != null && AppData.getInstance().getAccounts().size() > 0));
    }

    private void updateWalletValues() {
        final String price = Utils.formattedNumber(wallet.getSymbolData().getPrice());
        txtHeaderPrice.setText(String.format("%c%s", wallet.getCurrencySymbol(), price));
        txtPriceSymbol.setText(String.valueOf(wallet.getCurrencySymbol()));
        final int index = price.indexOf(".");
        txtPriceInteger.setText(index < 0 ? price : price.substring(0, index));
        txtPriceDecimal.setText(index < 0 ? null : price.substring(index));

        txtWalletUSD.setText(wallet.getBalanceCurrencyFormatted());
        txtWalletPrice.setText(Utils.formattedNumber(wallet.getBalance(), 0, 6));

        if (wallet.getSymbolData().isCurrency()) {
            llMarketStats.setVisibility(View.GONE);
        } else {
            llMarketStats.setVisibility(View.VISIBLE);
            final List<StatListAdapter.Stat> stats = new ArrayList<>();
            stats.add(new StatListAdapter.Stat(R.string.market_cap, wallet.getSymbolData().getMarketCap(), R.mipmap.ic_market_cap, R.string.market_cap_description));
            stats.add(new StatListAdapter.Stat(R.string.volume, wallet.getSymbolData().getVolumeUsd24h(), R.mipmap.ic_volume, R.string.volume_description));
            stats.add(new StatListAdapter.Stat(R.string.circulating_supply, wallet.getSymbolData().getCirculatingSupply(), R.mipmap.ic_supply, R.string.circulating_supply_description));
            statsAdapter.setData(stats, Color.parseColor(wallet.getColor()));
        }
    }

    private void updatePriceView(List<MarketCap> data) {
        if (data == null || data.size() == 0 || wallet.getSymbolData().isCurrency()) return;

        String periodText;
        switch (selectedPeriod) {
            case 4:
                periodText = "this hour";
                break;
            case 3:
                periodText = "this day";
                break;
            case 2:
                periodText = "this week";
                break;
            case 1:
                periodText = "this month";
                break;
            case 0:
                periodText = "this year";
                break;
            default:
                periodText = "";
                break;
        }
        final double priceChange = data.get(0).getAmount() - data.get(data.size() - 1).getAmount();
        imgPriceChange.setImageResource(priceChange < 0 ? R.mipmap.arrow_down : R.mipmap.arrow_up);
        txtPriceChange.setText(String.format(Locale.US, "%c%s (%s%%)",
                wallet.getCurrencySymbol(),
                Utils.formattedNumber(Math.abs(priceChange), 0, 2),
                Utils.formattedNumber(data.get(0).getAmount() == 0 ? 0 : 100 * priceChange / data.get(0).getAmount(), 0, 2)
        ));
        txtPriceChange.setTextColor(getResources().getColor(priceChange < 0 ? R.color.colorRed : R.color.colorGreen));
        txtDate.setText(periodText);

        updateChartView(data);
    }

    private void updateChartView(List<MarketCap> data) {
        if (data.size() == 0) return;

        final List<MarketCap> chartData = new ArrayList<>(data);
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
            entries.add(new Entry(i, (float) chartData.get(i).getAmount()));
        }
        final LineDataSet dataSet = new LineDataSet(entries, "");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setRenderer(new ImageLineChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler(), Color.parseColor(wallet.getColor())));

        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircleHole(true);
        dataSet.setDrawCircles(false);
        dataSet.setHighlightEnabled(true);
        dataSet.setHighLightColor(Color.parseColor(wallet.getColor()));
        dataSet.setHighlightLineWidth(1);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setColor(Color.parseColor(wallet.getColor()));
        dataSet.setLineWidth(1);
        final int middleColor = Color.parseColor(wallet.getColor().replace("#", "#3f"));
        dataSet.setFillDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Color.parseColor(wallet.getColor()), middleColor}));
        llGradient.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {middleColor, Color.TRANSPARENT}));
        llPriceChange.setVisibility(View.VISIBLE);
        chart.setVisibility(View.VISIBLE);

        chart.getData().setHighlightEnabled(false);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                final String price = Utils.formattedNumber(entries.get((int) e.getX()).getY());
                txtHeaderPrice.setText(String.format("%c%s", wallet.getCurrencySymbol(), price));
                txtPriceSymbol.setText(String.valueOf(wallet.getCurrencySymbol()));
                final int index = price.indexOf(".");
                txtPriceInteger.setText(index < 0 ? price : price.substring(0, index));
                txtPriceDecimal.setText(index < 0 ? null : price.substring(index));

                final String currentDateStr = chartData.get((int) e.getX()).getDate();
                txtSelectedDate.setText(Utils.dateToString(Utils.stringToDate(currentDateStr, null, "UTC"), null));
            }

            @Override
            public void onNothingSelected() {
                Log.e("log2", "onNothingSelected: ");
            }
        });
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                if (lockableScrollView == null) return;
                lockableScrollView.setScrollingEnabled(false);

                chart.getData().setHighlightEnabled(true);
                llPriceChange.setVisibility(View.INVISIBLE);
                txtSelectedDate.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                if (lockableScrollView == null) return;
                lockableScrollView.setScrollingEnabled(true);

                chart.getData().setHighlightEnabled(false);
                llPriceChange.setVisibility(View.VISIBLE);
                txtSelectedDate.setVisibility(View.INVISIBLE);
                final String price = Utils.formattedNumber(wallet.getSymbolData().getPrice());
                txtHeaderPrice.setText(String.format("%c%s", wallet.getCurrencySymbol(), price));
                txtPriceSymbol.setText(String.valueOf(wallet.getCurrencySymbol()));
                final int index = price.indexOf(".");
                txtPriceInteger.setText(index < 0 ? price : price.substring(0, index));
                txtPriceDecimal.setText(index < 0 ? null : price.substring(index));
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
        if (!isAdded()) return;
        if (response instanceof MarketResponse) {
            final Market data = ((MarketResponse)response).getData();
            updatePriceView(data.getPrice());
        } else if (response instanceof TransactionsResponse) {
            final TransactionsHistory data = ((TransactionsResponse)response).getData();
            recyclerTransactions.setVisibility(data.getTotal() == 0 ? View.GONE : View.VISIBLE);
            adapterTransaction.setData(wallet, data.getTransactions(), data.getPage());
        }
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onScrollChanged() {
        final int offset = lockableScrollView.getScrollY();
        final int visibleOffset = 200;
        llHeader.setAlpha(offset < visibleOffset ? 0 : (offset - visibleOffset) / 100f);
    }

    @Override
    public void onBackPressed() {
        lockableScrollView.getViewTreeObserver().removeOnScrollChangedListener(this);
        super.onBackPressed();
    }
}
