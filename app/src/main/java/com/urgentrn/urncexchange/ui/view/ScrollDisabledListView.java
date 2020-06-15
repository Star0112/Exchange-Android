package com.urgentrn.urncexchange.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ExpandableListView;

public class ScrollDisabledListView extends ExpandableListView {

    private int mPosition;
    private boolean mScrollable = false;

    public ScrollDisabledListView(Context context) {
        super(context);
    }

    public ScrollDisabledListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollDisabledListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollingEnabled(boolean scrollable) {
        this.mScrollable = scrollable;
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isScrollable()) {
            final int actionMasked = ev.getActionMasked() & MotionEvent.ACTION_MASK;

            if (actionMasked == MotionEvent.ACTION_DOWN) {
                // Record the position the list the touch landed on
                mPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                return super.dispatchTouchEvent(ev);
            }

            if (actionMasked == MotionEvent.ACTION_MOVE) {
                // Ignore move events
                return true;
            }

            if (actionMasked == MotionEvent.ACTION_UP) {
                // Check if we are still within the same view
                if (pointToPosition((int) ev.getX(), (int) ev.getY()) == mPosition) {
                    super.dispatchTouchEvent(ev);
                } else {
                    // Clear pressed state, cancel the action
                    setPressed(false);
                    invalidate();
                    return true;
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}
