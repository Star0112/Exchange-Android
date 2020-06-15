package com.urgentrn.urncexchange.ui.holder;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Market;
import com.urgentrn.urncexchange.models.MarketCap;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExchangableHolder extends UltimateRecyclerviewViewHolder {

    private ImageView imgCoin, imgCheck, imgFavorite;
    private TextView txtCoin, txtBalance, txtFavorite;
    private View llFavorite, imgReorder;
    private LineChart chart;

    public ExchangableHolder(View itemView) {
        super(itemView);

        imgCoin = itemView.findViewById(R.id.imgCoin);
        txtCoin = itemView.findViewById(R.id.txtCoin);
        txtBalance = itemView.findViewById(R.id.txtBalance);
        imgCheck = itemView.findViewById(R.id.imgCheck);
        llFavorite = itemView.findViewById(R.id.llFavorite);
        imgFavorite = itemView.findViewById(R.id.imgFavorite);
        txtFavorite = itemView.findViewById(R.id.txtFavorite);
        imgReorder = itemView.findViewById(R.id.imgReorder);
        chart = itemView.findViewById(R.id.chartView1);

        if (chart != null) { // because of footer
            chart.setVisibility(View.INVISIBLE);
            initGraph();
        }
    }

    public void updateView(Wallet data, boolean editable) {
        Glide.with(itemView).load(data.getSymbolData().getColoredImage()).into(imgCoin);
        txtCoin.setText(data.getTitle());
        txtBalance.setText(data.getBalanceCurrencyFormatted());
        updateFavoriteView(data.isFavorite());
        updateMode(editable, data.isFavorite());
    }

    private void updateFavoriteView(boolean isFavorite) {
        llFavorite.setBackgroundColor(Color.parseColor(isFavorite ? "#f06a6a" : "#a2d466"));
        imgFavorite.setImageResource(isFavorite ? R.mipmap.ic_favorite : R.mipmap.ic_favorite_filled);
        txtFavorite.setText(isFavorite ? "Remove from Favorites" : "Add to Favorites");
    }

    private void updateMode(boolean editable, boolean isFavorite) {
        if (editable) {
            imgCheck.setImageResource(R.mipmap.ic_check);
            imgCheck.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            txtBalance.setVisibility(View.GONE);
            imgReorder.setVisibility(View.VISIBLE);
        } else if (Constants.USE_FAVORITES) {
            imgCheck.setImageResource(isFavorite ? R.mipmap.ic_favorite_filled : R.mipmap.ic_favorite);
            imgCheck.setImageTintList(null);
            txtBalance.setVisibility(View.VISIBLE);
            imgReorder.setVisibility(View.GONE);
        } else {
            imgCheck.setVisibility(View.GONE);
        }
        exchangeLayout.setExchangeEnabled(Constants.USE_FAVORITES && !editable);
    }

    public void updateChecked(boolean checked) {
        imgCheck.setVisibility(checked ? View.VISIBLE : View.INVISIBLE);
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
        chart.setTouchEnabled(false);
    }

    public void updateChart(Market market, String color) {
        if (market == null) {
            chart.setVisibility(View.INVISIBLE);
            return;
        }

        List<MarketCap> marketCaps = market.getPrice();
        if (marketCaps == null || marketCaps.size() == 0) {
            chart.setVisibility(View.INVISIBLE);
            return;
        }

        chart.setVisibility(View.VISIBLE);

        final List<MarketCap> chartData = new ArrayList<>(marketCaps);
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
            entries.add(new Entry(i, (float)chartData.get(i).getAmount()));
        }
        LineDataSet dataSet = new LineDataSet(entries, "");
        LineData data = new LineData(dataSet);
        chart.setData(data);

        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(255);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircleHole(true);
        dataSet.setDrawCircles(false);
        dataSet.setHighlightEnabled(true);
        dataSet.setHighLightColor(Color.TRANSPARENT);
        dataSet.setHighlightLineWidth(0);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setColor(Color.parseColor(color));
        dataSet.setLineWidth(2);
        dataSet.setFillDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Color.parseColor(color), Color.WHITE}));

        chart.getData().setHighlightEnabled(false);
        chart.invalidate();
    }
}
