package com.urgentrn.urncexchange.ui.view;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ImageLineChartRenderer extends LineChartRenderer {
    private final LineChart lineChart;
    private int color;

    public ImageLineChartRenderer(LineChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler, int color) {
        super(chart, animator, viewPortHandler);
        this.lineChart = chart;
        this.color = color;
    }

    @Override
    public void drawExtras(Canvas c) {
        super.drawExtras(c);

        Highlight[] highlighted = lineChart.getHighlighted();
        if (highlighted == null) return;

        float phaseY = mAnimator.getPhaseY();

        float[] imageBuffer = new float[2];
        imageBuffer[0] = 0;
        imageBuffer[1] = 0;
        LineData lineData = mChart.getLineData();

        for (Highlight high : highlighted) {
            int dataSetIndex = high.getDataSetIndex();
            ILineDataSet set = lineData.getDataSetByIndex(dataSetIndex);

            if (set == null || !set.isHighlightEnabled())
                continue;

            Transformer trans = lineChart.getTransformer(set.getAxisDependency());

            Entry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            imageBuffer[0] = e.getX();
            imageBuffer[1] = e.getY() * phaseY;
            trans.pointValuesToPixel(imageBuffer);

            Paint outlineCircle = new Paint();
            outlineCircle.setColor(color);
            c.drawCircle(imageBuffer[0], imageBuffer[1], 14, outlineCircle);

//            Paint paint = new Paint();
//            paint.setColor(Color.WHITE);
//            c.drawCircle(imageBuffer[0], imageBuffer[1], 12, paint);

            Paint linePaint = new Paint();
            linePaint.setStrokeWidth(6);
            linePaint.setColor(color);
            c.drawLine(imageBuffer[0], imageBuffer[1], imageBuffer[0], lineChart.getHeight(), linePaint);
        }
    }
}
