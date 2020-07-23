package com.urgentrn.urncexchange.ui.fragments.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gauravbhola.viewpagerslidingheader.SlidingHeaderActivityCallbacks;
import com.gauravbhola.viewpagerslidingheader.SlidingHeaderCallbacks;
import com.gauravbhola.viewpagerslidingheader.ViewPagerSlidingHeaderRootView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.layout.ContentFragment;
import com.urgentrn.urncexchange.layout.RecyclerViewFragment;
import com.urgentrn.urncexchange.models.ExchangeData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.layout.StarSlidingTabLayout;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

@EFragment(R.layout.activity_test)
public class TestFragment extends BaseFragment implements SlidingHeaderCallbacks, SlidingHeaderActivityCallbacks {

    @ViewById
    Toolbar mToolBar;

    @ViewById
    ViewPager mPager;

    @ViewById
    FrameLayout mPagerContainer;

    @ViewById
    StarSlidingTabLayout mSlidingTabLayout;

    @ViewById
    LinearLayout llHeader;

    @ViewById
    ViewPagerSlidingHeaderRootView mRootView;

    FragmentStatePagerAdapter mAdapter;

    @AfterViews
    protected void init() {
        mAdapter = new MyPagerAdapter(getChildFragmentManager());

        mPager.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        mRootView.registerCallbacks(this);
        setupViewPager();
        prepareViewPagerSlidingHeader();
    }

    public void setupViewPager() {
        mPager.setAdapter(mAdapter);
        mSlidingTabLayout.setCustomTabView(R.layout.layout_tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#ffffff"));
        mSlidingTabLayout.setViewPager(mPager);
    }

    void prepareViewPagerSlidingHeader() {
        mRootView.initHeaderViewPager(mToolBar, llHeader, mSlidingTabLayout, mPagerContainer);
        mRootView.setParallaxFactor(4);
        mRootView.registerHeaderListener(new ViewPagerSlidingHeaderRootView.HeaderSlideListener() {
            @Override
            public void onOpenPercentChanged(int openPercent, float translationY) {
//                L.d("openPercent = " + openPercent);
//                L.d("translation = " + translationY);
            }
        });
    }

    @Override
    public ViewPagerSlidingHeaderRootView getRootView() {
        return null;
    }

    @Override
    public boolean shouldDrawerMove() {
        return false;
    }

    @Override
    public void dispatchFling(MotionEvent ev1, MotionEvent ev2, float velx, float vely) {

    }

    public static class MyPagerAdapter extends FragmentStatePagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FirstFragment.newInstance(0, "A");
                case 1:
                    return FirstFragment.newInstance(0, "B");
                case 2:
                    return FirstFragment.newInstance(0, "C");
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
}
