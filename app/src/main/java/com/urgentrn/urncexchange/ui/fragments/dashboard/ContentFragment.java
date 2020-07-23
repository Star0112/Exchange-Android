package com.urgentrn.urncexchange.ui.fragments.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.Nullable;

import com.gauravbhola.viewpagerslidingheader.ViewPagerSlidingHeaderFragment;
import com.urgentrn.urncexchange.R;

public class ContentFragment extends ViewPagerSlidingHeaderFragment {
    public static final String TAG = ContentFragment.class.getName();
    ScrollView mScrollView;

    public static ContentFragment newInstance(int position) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View getScrollableView() {
        return mScrollView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  LayoutInflater.from(getActivity()).inflate(R.layout.fragment_content, null);
        mScrollView = (ScrollView) v.findViewById(R.id.scrollview_observable);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCallbacks.updateFragmentInstance(this, getArguments().getInt(PARAM_POSITINON));
    }

}