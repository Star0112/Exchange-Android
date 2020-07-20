package com.urgentrn.urncexchange.ui.fragments.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SeekBarTouchStart;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.urgentrn.urncexchange.utils.Utils.addChar;
import static com.urgentrn.urncexchange.utils.Utils.getCurrentTime;
import static com.urgentrn.urncexchange.utils.Utils.timestampToDateString;

@EFragment(R.layout.fragment_dashboard)
public class DashboardFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtPrice, txtDate, txtPriceChange;

    @ViewById
    ImageView imgPriceChange;

    @ViewById
    Button btnRefresh;

    @ViewById
    View viewTimeline1d, viewTimeline1w, viewTimeline1m, viewTimeline3m, viewTimeline6m, viewTimeline1y;

    @ViewById
    LinearLayout llPriceChange;

    @ViewById(R.id.selectCoin)
    Spinner spinner;

    @ViewById(R.id.chartView)
    LineChart chart;

    private List<String> symbolsName = new ArrayList<>();
    private List<String> displaySymbolsName = new ArrayList<>();
    private int selectedSymbol = 0;
    private int selectedTimeline = 0;

    @AfterViews
    protected void init() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedSymbol = position;
                updatePriceView(symbolsName.get(selectedSymbol), selectedTimeline);
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

    private void updateChartView(List<List<String>> chartData) {
        if (chartData == null) return;
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < chartData.size(); i++) {
            entries.add(new Entry(i, Float.parseFloat(chartData.get(i).get(1))));
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
                txtDate.setText(timestampToDateString(chartData.get(entries.indexOf(e)).get(0)));
            }

            @Override
            public void onNothingSelected() {
            }
        });
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                chart.getData().setHighlightEnabled(true);
                llPriceChange.setVisibility(View.GONE);
                txtDate.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                chart.getData().setHighlightEnabled(false);
                txtPrice.setText(String.valueOf(chartData.get(chartData.size() - 1).get(1)));
                double changedPrice = Double.parseDouble(chartData.get(chartData.size() - 1).get(1)) - Double.parseDouble(chartData.get(0).get(1));
                double changedPricePercent = 100*changedPrice/Double.parseDouble(chartData.get(0).get(1));
                imgPriceChange.setImageResource(changedPrice > 0? R.mipmap.arrow_up: R.mipmap.arrow_down);
                txtPriceChange.setText(String.format(Locale.US, " %s (%s%%)",
                        Utils.formattedNumber(Math.abs(changedPrice), 0, 4),
                        Utils.formattedNumber(Math.abs(changedPricePercent), 0, 2)
                ));
                txtDate.setVisibility(View.GONE);
                txtPriceChange.setTextColor(getResources().getColor(changedPrice > 0? R.color.colorGreen: R.color.colorRed));
                llPriceChange.setVisibility(View.VISIBLE);
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
        spinner.setVisibility(View.VISIBLE);
        txtPrice.setText(String.valueOf(chartData.get(chartData.size() - 1).get(1)));
        double changedPrice = Double.parseDouble(chartData.get(chartData.size() - 1).get(1)) - Double.parseDouble(chartData.get(0).get(1));
        double changedPricePercent = 100*changedPrice/Double.parseDouble(chartData.get(0).get(1));
        imgPriceChange.setImageResource(changedPrice > 0? R.mipmap.arrow_up: R.mipmap.arrow_down);
        txtPriceChange.setText(String.format(Locale.US, " %s (%s%%)",
                Utils.formattedNumber(Math.abs(changedPrice), 0, 4),
                Utils.formattedNumber(Math.abs(changedPricePercent), 0, 2)
        ));
        txtPriceChange.setTextColor(getResources().getColor(changedPrice > 0? R.color.colorGreen: R.color.colorRed));
        chart.setVisibility(View.VISIBLE);
        txtPrice.setVisibility(View.VISIBLE);
        llPriceChange.setVisibility(View.VISIBLE);
        btnRefresh.setText(getCurrentTime());
        btnRefresh.setVisibility(View.VISIBLE);
    }

    private void updatePriceView(String symbol, int selectedTimeline) {
        long currentTimestamp = Calendar.getInstance().getTime().getTime()/1000L;
        long startTimestamp = 0L;
        int interval = 0;
        if (selectedTimeline == 0) {
            startTimestamp = currentTimestamp - 3600 * 24;
            interval = 1800;
        } else if ( selectedTimeline == 1) {
            startTimestamp = currentTimestamp - 3600 * 24 * 7;
            interval = 3600;
        } else if ( selectedTimeline == 2) {
            startTimestamp = currentTimestamp - 3600 * 24 * 30;
            interval = 3600 * 12;
        } else if ( selectedTimeline == 3) {
            startTimestamp = currentTimestamp - 3600 * 24 * 90;
            interval = 3600 * 24;
        } else if ( selectedTimeline == 4) {
            startTimestamp = currentTimestamp - 3600 * 24 * 180;
            interval = 3600 * 24;
        } else {
            startTimestamp = currentTimestamp - 3600 * 24 * 365;
            interval = 3600 * 24;
        }
        ApiClient.getInterface()
                .getChartData(symbol, String.valueOf(startTimestamp), String.valueOf(currentTimestamp), interval)
                .enqueue(new AppCallback<ChartDataResponse>(getContext(), this));
    }

    @Click(R.id.btnRefresh)
    void onRefresh() {
        updatePriceView(symbolsName.get(selectedSymbol), selectedTimeline);
    }

    @Click(R.id.llTimeline1d)
    void onChangeTimeline1d() {
        viewTimeline1d.setVisibility(View.VISIBLE);
        viewTimeline1w.setVisibility(View.INVISIBLE);
        viewTimeline1m.setVisibility(View.INVISIBLE);
        viewTimeline3m.setVisibility(View.INVISIBLE);
        viewTimeline6m.setVisibility(View.INVISIBLE);
        viewTimeline1y.setVisibility(View.INVISIBLE);
        selectedTimeline = 0;
        updatePriceView(symbolsName.get(selectedSymbol), selectedTimeline);
    }

    @Click(R.id.llTimeline1w)
    void onChangeTimeline1w() {
        viewTimeline1d.setVisibility(View.INVISIBLE);
        viewTimeline1w.setVisibility(View.VISIBLE);
        viewTimeline1m.setVisibility(View.INVISIBLE);
        viewTimeline3m.setVisibility(View.INVISIBLE);
        viewTimeline6m.setVisibility(View.INVISIBLE);
        viewTimeline1y.setVisibility(View.INVISIBLE);
        selectedTimeline = 1;
        updatePriceView(symbolsName.get(selectedSymbol), selectedTimeline);
    }

    @Click(R.id.llTimeline1m)
    void onChangeTimeline1m() {
        viewTimeline1d.setVisibility(View.INVISIBLE);
        viewTimeline1w.setVisibility(View.INVISIBLE);
        viewTimeline1m.setVisibility(View.VISIBLE);
        viewTimeline3m.setVisibility(View.INVISIBLE);
        viewTimeline6m.setVisibility(View.INVISIBLE);
        viewTimeline1y.setVisibility(View.INVISIBLE);
        selectedTimeline = 2;
        updatePriceView(symbolsName.get(selectedSymbol), selectedTimeline);
    }

    @Click(R.id.llTimeline3m)
    void onChangeTimeline3m() {
        viewTimeline1d.setVisibility(View.INVISIBLE);
        viewTimeline1w.setVisibility(View.INVISIBLE);
        viewTimeline1m.setVisibility(View.INVISIBLE);
        viewTimeline3m.setVisibility(View.VISIBLE);
        viewTimeline6m.setVisibility(View.INVISIBLE);
        viewTimeline1y.setVisibility(View.INVISIBLE);
        selectedTimeline = 3;
        updatePriceView(symbolsName.get(selectedSymbol), selectedTimeline);
    }

    @Click(R.id.llTimeline6m)
    void onChangeTimeline6m() {
        viewTimeline1d.setVisibility(View.INVISIBLE);
        viewTimeline1w.setVisibility(View.INVISIBLE);
        viewTimeline1m.setVisibility(View.INVISIBLE);
        viewTimeline3m.setVisibility(View.INVISIBLE);
        viewTimeline6m.setVisibility(View.VISIBLE);
        viewTimeline1y.setVisibility(View.INVISIBLE);
        selectedTimeline = 4;
        updatePriceView(symbolsName.get(selectedSymbol), selectedTimeline);
    }

    @Click(R.id.llTimeline1y)
    void onChangeTimeline1y() {
        viewTimeline1d.setVisibility(View.INVISIBLE);
        viewTimeline1w.setVisibility(View.INVISIBLE);
        viewTimeline1m.setVisibility(View.INVISIBLE);
        viewTimeline3m.setVisibility(View.INVISIBLE);
        viewTimeline6m.setVisibility(View.INVISIBLE);
        viewTimeline1y.setVisibility(View.VISIBLE);
        selectedTimeline = 5;
        updatePriceView(symbolsName.get(selectedSymbol), selectedTimeline);
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
