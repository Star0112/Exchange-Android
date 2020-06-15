package com.urgentrn.urncexchange.ui.holder;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Market;
import com.urgentrn.urncexchange.models.MarketCap;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class WalletHolder extends RecyclerView.ViewHolder {

    private ImageView imgCoin, imgArrow;
    private TextView txtCoin, txtBalanceUSD, txtBalance, txtPrice, txtPercentChange;
    private LineChart chart;

    public WalletHolder(View itemView) {
        super(itemView);

        imgCoin = itemView.findViewById(R.id.imgCoin);
        imgArrow = itemView.findViewById(R.id.imgArrow);
        txtCoin = itemView.findViewById(R.id.txtCoin);
        txtBalanceUSD = itemView.findViewById(R.id.txtBalanceUSD);
        txtBalance = itemView.findViewById(R.id.txtBalance);
        txtPrice = itemView.findViewById(R.id.txtPrice);
        txtPercentChange = itemView.findViewById(R.id.txtPercentChange);
        chart = itemView.findViewById(R.id.chartView);

        chart.setVisibility(View.INVISIBLE);
        initGraph();
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

    public void updateView(Wallet data) {
        String priceFormatted, percentChange24hFormatted, image;
        double percentChange24h;
        if (Constants.USE_SYMBOL_DATA && WalletUtils.getSymbolData(data.getSymbol()) != null) {
            final Symbol symbolData = WalletUtils.getSymbolData(data.getSymbol());
            priceFormatted = symbolData.getMarketData().getPriceFormatted();
            percentChange24h = symbolData.getMarketData().getPercentChange24h();
            percentChange24hFormatted = symbolData.getMarketData().getPercentChange24hFormatted();
            image = symbolData.getColoredImage();
        } else {
            priceFormatted = String.format("%c%s", data.getCurrencySymbol(), Utils.formattedNumber(data.getSymbolData().getPrice(), 2, 4)); // due to narrow width
            percentChange24h = data.getSymbolData().getPercentChange24h();
            percentChange24hFormatted = String.format(Locale.US, "%s%%", Utils.formattedNumber(percentChange24h, 0, 2));
            image = data.getSymbolData().getColoredImage();
        }
        Glide.with(itemView)
                .load(image)
                .into(imgCoin);
        txtCoin.setText(data.getTitle());
        txtBalanceUSD.setText(data.getBalanceCurrencyFormatted());
        txtBalance.setText(String.format("%s %s", Utils.formattedNumber(data.getBalance(), 0, 5), data.getSymbol()));
        txtPrice.setText(priceFormatted);
        txtPercentChange.setText(String.format("%s%s", percentChange24h > 0 ? "+" : "", percentChange24hFormatted));
        imgArrow.setImageResource(percentChange24h > 0 ? R.mipmap.arrow_up : R.mipmap.arrow_down);

        final int color = itemView.getResources().getColor(percentChange24h > 0 ? R.color.colorGreen : R.color.colorRed);
        txtPercentChange.setTextColor(color);
    }

    public void updateChart(Market market, String color) {
        if (market == null) return;

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
