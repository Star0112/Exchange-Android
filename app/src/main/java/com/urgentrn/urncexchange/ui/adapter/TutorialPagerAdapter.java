package com.urgentrn.urncexchange.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.urgentrn.urncexchange.R;

public class TutorialPagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, int position) {
        final Context context = container.getContext();
        View view;
        if (position == 0) {
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.tutorial_first, container, false);
        } else {
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.tutorial_second, container, false);
        }

        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
