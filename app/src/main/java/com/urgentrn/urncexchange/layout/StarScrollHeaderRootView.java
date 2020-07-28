package com.urgentrn.urncexchange.layout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.gauravbhola.viewpagerslidingheader.ScrollUtils;
import com.gauravbhola.viewpagerslidingheader.SlidingHeaderCallbacks;
import com.gauravbhola.viewpagerslidingheader.ViewPagerSlidingHeaderRootView;

import java.util.ArrayList;

public class StarScrollHeaderRootView extends RelativeLayout {

    private float prevY = 0;
    private float mTranslationYLowerBound;
    private float mTranslationYUpperBound;

    float mTouchDy;

    private View mToolbar;
    private View mTabLayout;
    private View mHeaderView;
    private View mPagerContainer;

    public static enum DrawerState {OPEN, CLOSED, CLOSING, OPENING};

    private DrawerState mDrawerState;

    public StarScrollHeaderRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initHeaderViewPager(View toolbar, View headerView, View tabView, View pager) {
        mDrawerState = DrawerState.OPEN;
        mToolbar = toolbar;
        mTabLayout = tabView;
        mHeaderView = headerView;
        mPagerContainer = pager;


        mTabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTranslationYLowerBound = -(mHeaderView.getHeight());
                mTranslationYUpperBound = 0;
            }
        });

        mPagerContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int viewPagerWidth = mPagerContainer.getWidth();

                if(viewPagerWidth != 0) {
                    LayoutParams layoutParams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    int height = mPagerContainer.getHeight() + mHeaderView.getHeight();
                    layoutParams.width = viewPagerWidth;
                    layoutParams.height = height;
                    layoutParams.addRule(ALIGN_PARENT_BOTTOM);
                    mPagerContainer.setLayoutParams(layoutParams);
                    mPagerContainer.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                    //mPager.measure(viewPagerWidth, height);
                    mPagerContainer.setTranslationY(getPagerDeviation());
                    mPagerContainer.requestLayout();
                }
            }
        });
    }

    private boolean mIntercepting;

    MyGestureListener mMyGestureListener = new MyGestureListener(getContext());

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mMyGestureListener.onTouch(this, event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener implements OnTouchListener {
        Context context;

        public MyGestureListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                float y = event.getY();
                mTouchDy = y - prevY;
                prevY = y;

                mIntercepting = shouldInterceptTouchEvents((int)mTouchDy);

                if (mIntercepting) {
                    moveContents(mTouchDy);
                    return true;
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                settleContents();
                return true;
                //return false;
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                prevY = event.getY();
                return true;
            }
            return true;
        }
    }

    public int getPagerDeviation() {
        return mHeaderView.getHeight();
    }


    public boolean moveContents(float dy) {
        //awesomeness :) ;)
        if (mDrawerState == DrawerState.OPEN
                || mDrawerState == DrawerState.OPENING
                || mDrawerState == DrawerState.CLOSING
                || mDrawerState == DrawerState.CLOSED
        ) {
            return moveHeader(dy);
        }
        return true;
    }

    public boolean moveHeader(float dy) {
        if (dy > 0) {
            mDrawerState = DrawerState.OPENING;
        } else if (dy < 0) {
            mDrawerState = DrawerState.CLOSING;
        }
        float translationY = (mTabLayout.getTranslationY() + dy);
        translationY = ScrollUtils.getFloat(translationY, mTranslationYLowerBound, mTranslationYUpperBound);

        mTabLayout.setTranslationY(translationY);
        mHeaderView.setTranslationY(-translationY/5);
        mPagerContainer.setTranslationY(getPagerDeviation() + translationY);

        if (translationY == mTranslationYLowerBound) {
            mDrawerState = DrawerState.CLOSED;
            return false;
        }
        if (translationY == mTranslationYUpperBound) {
            mDrawerState = DrawerState.OPEN;
            return false;
        }
        return true;
    }

    public void settleContents() {
        if (mDrawerState == DrawerState.OPENING
                || mDrawerState == DrawerState.CLOSING) {
            closeDrawer();
            return;
        }
    }

    public boolean shouldInterceptTouchEvents(int dy) {  //First touch
        if (dy <= 0) {
            //sliding up
            if (mDrawerState == DrawerState.OPEN || mDrawerState == DrawerState.CLOSED || mDrawerState == DrawerState.CLOSING || mDrawerState == DrawerState.OPENING) {
                //drawer is open or is in midway
                return true;
            }
        }
        if (dy > 0) {
            //sliding down
            if (mDrawerState == DrawerState.CLOSED || mDrawerState == DrawerState.CLOSING || mDrawerState == DrawerState.OPENING) {
                //drawer midway
                return true;
            }
        }
        return false;
    }

    private void closeDrawer() {
        long animationDuration = 200;
        float translationY = 0;
        DrawerState drawerState = DrawerState.OPEN;

        if (mDrawerState == DrawerState.CLOSING) {
            translationY = mTranslationYLowerBound;
            drawerState = DrawerState.CLOSED;
        }

        if (mDrawerState == DrawerState.OPENING) {
            translationY = mTranslationYUpperBound;
            drawerState = DrawerState.OPEN;
        }

        final DrawerState finalDrawerState = drawerState;

        ObjectAnimator animator = ObjectAnimator.ofFloat(mHeaderView,
                "translationY",
                mHeaderView.getTranslationY(), -translationY/5);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mTabLayout,
                "translationY",
                mTabLayout.getTranslationY(), translationY);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mPagerContainer,
                "translationY",
                mPagerContainer.getTranslationY(), getPagerDeviation() + translationY);


        ArrayList<ObjectAnimator> arrayListObjectAnimators = new ArrayList<ObjectAnimator>(); //ArrayList of ObjectAnimators
        arrayListObjectAnimators.add(0, animator);
        arrayListObjectAnimators.add(1, animator2);
        arrayListObjectAnimators.add(2, animator3);


        ObjectAnimator[] objectAnimators = arrayListObjectAnimators.toArray(new ObjectAnimator[arrayListObjectAnimators.size()]);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.setInterpolator(new DecelerateInterpolator());
        animSetXY.playTogether(objectAnimators);
        animSetXY.setDuration(animationDuration);//1sec
        animSetXY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mDrawerState = finalDrawerState;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animSetXY.start();
    }
}
