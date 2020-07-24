package com.urgentrn.urncexchange.ui.fragments.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gauravbhola.viewpagerslidingheader.ViewPagerSlidingHeaderFragment;
import com.urgentrn.urncexchange.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_statistics)
public class StatisticsFragment extends ViewPagerSlidingHeaderFragment {

    @ViewById
    ScrollView scrStatistics;

    public static StatisticsFragment newInstance() {
        StatisticsFragment_ fragment = new StatisticsFragment_();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View getScrollableView() {
        return scrStatistics;
    }

    protected void init() {
    }
}