package com.urgentrn.urncexchange.ui.fragments.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.MarketInfo;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.ChartDataResponse;
import com.urgentrn.urncexchange.models.response.MarketInfoResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.view.ImageLineChartRenderer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.urgentrn.urncexchange.utils.Utils.addChar;

@EFragment(R.layout.fragment_dashboard)
public class DashboardFragment extends BaseFragment implements ApiCallback {

    @ViewById
    LinearLayout llRefresh;

    @ViewById
    ImageView imgRefresh;

    @ViewById
    TextView txtRefresh, txtPrice;

    @ViewById(R.id.selectCoin)
    Spinner spinner;

    @ViewById(R.id.chartView)
    LineChart chart;

    private List<String> symbolsName = new ArrayList<>();
    private List<String> displaySymbolsName = new ArrayList<>();
    private int selectedNum = 0;

    @AfterViews
    protected void init() {
        txtRefresh.setClickable(true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedNum = position;
                updatePriceView(symbolsName.get(selectedNum));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        loadMarketInfo();
        initGraph();
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
        chart.setNoDataText(getString(R.string.no_data));
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
    public void updateView(HashMap<String, ExchangeData> data) {

    }

    private void loadMarketInfo() {
        ApiClient.getInterface()
                .getMarketInfo()
                .enqueue(new AppCallback<MarketInfoResponse>(this));
    }

    private void updatePriceView(String symbol) {
        ApiClient.getInterface()
                .getChartData(symbol, "1", "15770190907847", 3600)
                .enqueue(new AppCallback<ChartDataResponse>(getContext(), this));
    }

    private void updateChartView(List<List<String>> chartData) {
        if (chartData == null) return;
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < chartData.size(); i++) {
            entries.add(new Entry(i, Float.parseFloat(chartData.get(i).get(2))));
        }
        final LineDataSet dataSet = new LineDataSet(entries, "");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setRenderer(new ImageLineChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler(), Color.parseColor("#FFFFFF")));
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircleHole(true);
        dataSet.setDrawCircles(false);
        dataSet.setHighlightEnabled(true);
        dataSet.setHighLightColor(Color.parseColor("#FFFFFF"));
        dataSet.setHighlightLineWidth(1);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setColor(Color.parseColor("#FFFFFF"));
        dataSet.setLineWidth(1);
        final int middleColor = Color.parseColor("#00FFFFFF");
        dataSet.setFillDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Color.parseColor("#FFFFFF"), middleColor}));

        chart.getData().setHighlightEnabled(false);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                txtPrice.setText(String.valueOf(e.getY()));
            }

            @Override
            public void onNothingSelected() {
            }
        });
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                chart.getData().setHighlightEnabled(true);
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                chart.getData().setHighlightEnabled(false);
                txtPrice.setText(String.valueOf(chartData.get(chartData.size() - 1).get(2)));
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
        txtPrice.setText(String.valueOf(chartData.get(chartData.size() - 1).get(2)));
        chart.setVisibility(View.VISIBLE);
        txtPrice.setVisibility(View.VISIBLE);
    }

    @Click(R.id.txtRefresh)
    void onRefresh() {
        updatePriceView(symbolsName.get(selectedNum));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {}
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if(response instanceof ChartDataResponse) {
            final List<List<String>> data = ((ChartDataResponse)response).getData();
            updateChartView(data);
        } else if(response instanceof MarketInfoResponse) {
            final List<MarketInfo> data = ((MarketInfoResponse)response).getData();
            if(data != null) {
                for (MarketInfo marketInfo : data) {
                    symbolsName.add(marketInfo.getName());
                    displaySymbolsName.add(addChar(marketInfo.getName(), '/', 4));
                }
            }
            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, displaySymbolsName);
            spinner.setAdapter(spinnerAdapter);
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
