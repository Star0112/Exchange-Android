package com.urgentrn.urncexchange.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Banner;
import com.urgentrn.urncexchange.ui.TermActivity_;

import java.util.List;

public class BannerPagerAdapter extends PagerAdapter {

    private List<Banner> banners;

    public BannerPagerAdapter(List<Banner> banners) {
        this.banners = banners;
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater
                .from(container.getContext())
                .inflate(R.layout.pager_banner, container, false);

        final Banner banner = banners.get(position);

        final ImageView image = view.findViewById(R.id.image);
        final TextView text = view.findViewById(R.id.text);
        if (banner.getImage() == null) {
            image.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            text.setText(banner.getTitle());
        } else {
            image.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            Glide.with(container.getContext())
                    .load(banner.getImage().getPath())
                    .into(image);
        }
        if (banner.getLink() != null) {
            view.setOnClickListener(v -> {
                final Context context = view.getContext();
                final Intent intent = new Intent(context, TermActivity_.class);
                intent.putExtra("title", banner.getTitle());
                intent.putExtra("url", banner.getLink());
                context.startActivity(intent);
            });
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
