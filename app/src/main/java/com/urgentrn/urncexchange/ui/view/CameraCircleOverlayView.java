package com.urgentrn.urncexchange.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.urgentrn.urncexchange.R;
import org.jetbrains.annotations.NotNull;

public class CameraCircleOverlayView extends LinearLayout {

    private Bitmap windowFrame;
    private int leftValue;
    private int topValue;
    private int rightValue;
    private int bottomValue;

    public CameraCircleOverlayView(Context context) {
        super(context);
    }

    public CameraCircleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraCircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CameraCircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (windowFrame == null) {
            createWindowFrame();
        }
        if (windowFrame != null) {
            canvas.drawBitmap(windowFrame, 0f, 0f, null);
        }
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        windowFrame = null;
    }

    private void createWindowFrame() {
        int width = getWidth();
        int height = getHeight();
        windowFrame = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if (windowFrame != null) {
            Canvas canvas = drawOval(width, height);
            drawDashes(canvas);
        }
    }

    @NotNull
    private Canvas drawOval(int width, int height) {
        Canvas canvas = new Canvas(windowFrame);
        RectF outerRectangle = new RectF(0f, 0f, width, height);
        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorBlackTransparent));
        canvas.drawRect(outerRectangle, circlePaint);
        circlePaint.setColor(Color.TRANSPARENT);
        circlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        float centerX = (float) (width / 2);
        leftValue = (int) centerX - (width / 3);
        rightValue = (int) centerX + (width / 3);
        topValue = height / 12;
        bottomValue = height - (height / 12);
        canvas.drawOval(leftValue, topValue, rightValue, bottomValue, circlePaint);
        return canvas;
    }

    private void drawDashes(Canvas canvas) {
        float startAngle = 0;
        float endAngle = 360;
        int strokeWidth = 30;
        int dashWith = 2;
        int dashSpace = 90;
        float halfOffset = strokeWidth / 2f;

        Paint dashedCirclePaint = new Paint();
        dashedCirclePaint.setAntiAlias(true);
        dashedCirclePaint.setStrokeWidth(strokeWidth);
        dashedCirclePaint.setColor(Color.WHITE);
        dashedCirclePaint.setStyle(Paint.Style.STROKE);
        dashedCirclePaint.setPathEffect(new DashPathEffect(
                new float[]{dashWith, dashSpace},
                dashSpace
        ));
        canvas.drawArc(
                leftValue - halfOffset,
                topValue - halfOffset,
                rightValue + halfOffset,
                bottomValue + halfOffset,
                startAngle, endAngle,
                false,
                dashedCirclePaint);
    }

    public int getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(int leftValue) {
        this.leftValue = leftValue;
    }

    public int getTopValue() {
        return topValue;
    }

    public void setTopValue(int topValue) {
        this.topValue = topValue;
    }

    public int getRightValue() {
        return rightValue;
    }

    public void setRightValue(int rightValue) {
        this.rightValue = rightValue;
    }

    public int getBottomValue() {
        return bottomValue;
    }

    public void setBottomValue(int bottomValue) {
        this.bottomValue = bottomValue;
    }
}
