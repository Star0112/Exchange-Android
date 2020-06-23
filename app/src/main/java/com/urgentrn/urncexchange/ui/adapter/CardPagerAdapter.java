package com.urgentrn.urncexchange.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.card.CardInfo;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter {

    private boolean isUpgrade;
    private List<CardInfo> data = new ArrayList<>();

    public CardPagerAdapter(boolean isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater
                .from(container.getContext())
                .inflate(R.layout.pager_card, container, false);

        final CardInfo card = data.get(position);

        Glide.with(container.getContext())
                .load(card.getBaseImage())
                .into((ImageView)view.findViewById(R.id.imgCard));
        final TextView txtName = view.findViewById(R.id.txtName);
        if (false && isUpgrade) {
            final User user = ExchangeApplication.getApp().getUser();
//            txtName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            txtName.setVisibility(View.VISIBLE);
        } else {
            txtName.setVisibility(View.INVISIBLE);
        }

        container.addView(view);
        return view;
    }

    public CardInfo getItem(int position) {
        return data.get(position);
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

    public void setData(List<CardInfo> data) {
        this.data.clear();
        for (CardInfo card : data) {
            if (!card.isGift()) {
                this.data.add(card);
            }
        }
        notifyDataSetChanged();
    }
}
