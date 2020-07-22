package com.urgentrn.urncexchange.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gauravbhola.viewpagerslidingheader.SlidingHeaderActivityCallbacks;
import com.gauravbhola.viewpagerslidingheader.SlidingHeaderCallbacks;
import com.gauravbhola.viewpagerslidingheader.ViewPagerSlidingHeaderRootView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.layout.ContentFragment;
import com.urgentrn.urncexchange.layout.RecyclerViewFragment;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.layout.StarSlidingTabLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_test)
public class TestActivity extends BaseActivity implements SlidingHeaderCallbacks, SlidingHeaderActivityCallbacks {


    @ViewById
    Toolbar mToolbar;

    @ViewById
    ViewPager mPager;

    @ViewById
    FrameLayout mPagerContainer;

    @ViewById
    StarSlidingTabLayout mSlidingTabLayout;

    @ViewById
    ImageView mImageView;

    @ViewById
    ViewPagerSlidingHeaderRootView mRootView;
    MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());

    @AfterViews
    protected void init() {
        mPager.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        mRootView.registerCallbacks(this);

        setupActionBar();
        setupViewPager();
        prepareViewPagerSlidingHeader();
    }


    void prepareViewPagerSlidingHeader() {
        mRootView.initHeaderViewPager(mToolbar, mImageView, mSlidingTabLayout, mPagerContainer);
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
    protected void onResume() {
        super.onResume();
    }

    public void setupViewPager() {
        mPager.setAdapter(mAdapter);
        mSlidingTabLayout.setCustomTabView(R.layout.layout_tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#ffffff"));
        mSlidingTabLayout.setViewPager(mPager);
    }

    public class MyAdapter extends FragmentStatePagerAdapter {
        public String[] mTitles = {"scrollView", "recyclerView"};
        Fragment[] mFragments = new Fragment[mTitles.length];

        public MyAdapter(FragmentManager fm) {
            super(fm);
            mFragments[0] = ContentFragment.newInstance(0);
            mFragments[1] = RecyclerViewFragment.newInstance();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments[i];
        }

        public void updateInstance(Fragment fragment, int position) {
            mFragments[position] = fragment;
        }

        public boolean shouldDrawerMove() {
            return ((SlidingHeaderCallbacks)mFragments[mPager.getCurrentItem()]).shouldDrawerMove();
        }

        public void dispatchFling(MotionEvent ev1, MotionEvent ev2, float velx, float vely) {
            ((SlidingHeaderCallbacks)mFragments[mPager.getCurrentItem()]).dispatchFling(ev1, ev2, velx, vely);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }


    }

    @Override
    public boolean shouldDrawerMove() {
        return mAdapter.shouldDrawerMove();
    }

    @Override
    public void dispatchFling(MotionEvent ev1, MotionEvent ev2, float velx, float vely) {
        mAdapter.dispatchFling(ev1, ev2, velx, vely);
    }

    private void setupActionBar() {
        if (mToolbar == null) {
            return;
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void updateFragmentInstance(ContentFragment fragment, int position) {
        mAdapter.updateInstance(fragment, position);
    }

    @Override
    public ViewPagerSlidingHeaderRootView getRootView() {
        return mRootView;
    }
}
