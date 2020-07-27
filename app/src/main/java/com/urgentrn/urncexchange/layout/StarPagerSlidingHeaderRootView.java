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

public class StarPagerSlidingHeaderRootView extends RelativeLayout {
    private SlidingHeaderCallbacks mCallbacks;
    private HeaderSlideListener mHeaderListener;

    private float prevY = 0;
    private float mTranslationYLowerBound;
    private float mTranslationYUpperBound;

    int mPrevYintercepted;
    int mPrevXintercepted;

    float mTouchDy;

    boolean mScrollMode = false;
    private int mTouchSlop;

    private View mToolbar;
    private View mSlidingTabLayout;
    private View mHeaderView;
    private View mPager;

    public static enum DrawerState {OPEN, CLOSED, CLOSING, OPENING};

    private DrawerState mDrawerState;


    public static abstract class HeaderSlideListener {
        //goes from 100 to 0 when the header closes or is in midway
        private int openPercent;
        private View mSlidingTabLayout;

        private int getOpenPercent() {
            return openPercent;
        }

        public void setOpenPercent(int openPercent) {
            this.openPercent = openPercent;
            onOpenPercentChanged(openPercent, (mSlidingTabLayout == null) ? 0 : mSlidingTabLayout.getTranslationY());
        }

        public abstract void onOpenPercentChanged(int openPercent, float slidingTabTranslation);
    }
    public void registerCallbacks(SlidingHeaderCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    public StarPagerSlidingHeaderRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void initHeaderViewPager(View toolbar, View headerView, View tabView, View pager) {
        mDrawerState = DrawerState.OPEN;
        mToolbar = toolbar;
        mSlidingTabLayout = tabView;
        mHeaderView = headerView;
        mPager = pager;

        if (mHeaderListener != null) {
            mHeaderListener.mSlidingTabLayout = mSlidingTabLayout;
        }

        mSlidingTabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTranslationYLowerBound = -(mHeaderView.getHeight() + mToolbar.getHeight());
                mTranslationYUpperBound = 0;
            }
        });

        mPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                LayoutParams layoutParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                int height = 0;
                if (mToolbar == null) {
                    height = mPager.getHeight() + mHeaderView.getHeight();
                } else {
                    height = getHeight() - mSlidingTabLayout.getHeight();
                }

                int viewPagerWidth = mPager.getWidth();

                if(height != 0 && viewPagerWidth != 0) {
                    layoutParams.width = viewPagerWidth;
                    layoutParams.height = height;
                    layoutParams.addRule(ALIGN_PARENT_BOTTOM);
                    mPager.setLayoutParams(layoutParams);
                    mPager.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                    //mPager.measure(viewPagerWidth, height);
                    mPager.setTranslationY(getPagerDeviation());
                    mPager.requestLayout();
                }
            }
        });
    }

    private boolean mIntercepting;
    private MotionEvent mPendingDownMotionEvent;

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
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // if we dont have a lower bound on dy, the list flings unnecessarily
            if (Math.abs(mTouchDy) < 5) {
                velocityY = velocityY/5;
            } else if (Math.abs(mTouchDy) < 7) {
                velocityY = velocityY/3;
            } else if (Math.abs(mTouchDy) < 10) {
                velocityY = velocityY/2;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
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

    private MotionEvent obtainMotionEvent(MotionEvent base, int action) {
        MotionEvent ev = MotionEvent.obtainNoHistory(base);
        ev.setAction(action);
        return ev;
    }

    private void duplicateTouchEventForChildren(MotionEvent ev, MotionEvent... pendingEvents) {
        if (ev == null) {
            return;
        }
        for (int i = getChildCount() - 1; 0 <= i; i--) {
            View childView = getChildAt(i);
            //only duplicating the events if the child is a ViewPager
            if (childView != null && childView == mPager) {
                MotionEvent event = MotionEvent.obtainNoHistory(ev);
                float offsetX = -childView.getLeft();
                float offsetY = -childView.getTop();
                boolean consumed = false;
                if (pendingEvents != null) {
                    for (MotionEvent pe : pendingEvents) {
                        if (pe != null) {
                            MotionEvent peAdjusted = MotionEvent.obtainNoHistory(pe);
                            peAdjusted.offsetLocation(offsetX, offsetY);
                            try {
                                consumed |= childView.dispatchTouchEvent(peAdjusted);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                event.offsetLocation(offsetX, offsetY);
                try {
                    consumed |= childView.dispatchTouchEvent(event);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                if (consumed) {
                    break;
                }
            }
        }
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

    public int getPagerDeviation() {
        if (mToolbar == null) {
            return mHeaderView.getHeight();
        } else {
            return getActionBarHeight() + mHeaderView.getHeight();
        }
    }

    private int getActionBarHeight() {
        if (mToolbar != null) {
            return mToolbar.getHeight();
        }
        return 0;
    }

    public boolean moveHeader(float dy) {
        if (dy > 0) {
            mDrawerState = DrawerState.OPENING;
        } else if (dy < 0) {
            mDrawerState = DrawerState.CLOSING;
        }
        float translationY = (mSlidingTabLayout.getTranslationY() + dy);
        translationY = ScrollUtils.getFloat(translationY, mTranslationYLowerBound, mTranslationYUpperBound);

        mToolbar.setTranslationY(translationY);
        mSlidingTabLayout.setTranslationY(translationY);
        mHeaderView.setTranslationY(translationY);
        mPager.setTranslationY(getPagerDeviation() + translationY);

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
//        if (mActionBarState == ActionBarState.OPENING ||
//                mActionBarState == ActionBarState.CLOSING) {
//            closeActionBar();
//            return;
//        }
    }

    public boolean shouldInterceptTouchEvents(int dy) {  //First touch
        if (dy <= 0) {
            //sliding up

            if (mDrawerState == DrawerState.OPEN || mDrawerState == DrawerState.CLOSING || mDrawerState == DrawerState.OPENING) {
                //drawer is open or is in midway
                return true;
            }
            //drawer closed
            if (mDrawerState == DrawerState.CLOSED) {
                //drawer closed
                return true;
            }

            return false;
        }
        if (dy > 0) {
            //sliding down

            if (mDrawerState == DrawerState.CLOSED) {
                //drawer closed
                return true;
            }

            if (mDrawerState == DrawerState.CLOSING || mDrawerState == DrawerState.OPENING) {
                //drawer midway
                return true;
            }

            //drawer open
            return false;
        }
        return false;
    }

    private void closeDrawer() {
        long animationDuration = 200;
        float translationY = 0;
        int pagerNewHeight = mPager.getHeight();
        DrawerState drawerState = DrawerState.OPEN;
        float alpha = 0f;

        if (mDrawerState == DrawerState.CLOSING) {
            translationY = mTranslationYLowerBound;
            drawerState = DrawerState.CLOSED;
            alpha = 0f;
        }

        if (mDrawerState == DrawerState.OPENING) {
            translationY = mTranslationYUpperBound;
            drawerState = DrawerState.OPEN;
            alpha = 1f;
        }

        final DrawerState finalDrawerState = drawerState;

        ObjectAnimator animator = ObjectAnimator.ofFloat(mHeaderView,
                "translationY",
                mHeaderView.getTranslationY(), translationY);

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mToolbar,
                "translationY",
                mToolbar.getTranslationY(), translationY);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mSlidingTabLayout,
                "translationY",
                mSlidingTabLayout.getTranslationY(), translationY);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mPager,
                "translationY",
                mPager.getTranslationY(), getPagerDeviation() + translationY);


        ArrayList<ObjectAnimator> arrayListObjectAnimators = new ArrayList<ObjectAnimator>(); //ArrayList of ObjectAnimators
        arrayListObjectAnimators.add(0, animator);
        arrayListObjectAnimators.add(1, animator1);
        arrayListObjectAnimators.add(2, animator2);
        arrayListObjectAnimators.add(3, animator3);

        if (mHeaderListener != null) {
            ObjectAnimator animator5 = ObjectAnimator.ofInt(mHeaderListener,
                    "openPercent",
                    mHeaderListener.getOpenPercent(), (int)(alpha)*100);
            arrayListObjectAnimators.add(2, animator5);
        }

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
