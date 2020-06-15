package com.marshalchen.ultimaterecyclerview;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.quickAdapter.easyRegularAdapter;
import com.marshalchen.ultimaterecyclerview.exchange.ExchangeItemManagerImpl;
import com.marshalchen.ultimaterecyclerview.exchange.ExchangeItemManagerInterface;
import com.marshalchen.ultimaterecyclerview.exchange.ExchangeLayout;

import java.util.List;

/**
 * An abstract adapter which can be extended for Recyclerview
 */
public abstract class ExchangeableUltimateViewAdapter<T>
        extends easyRegularAdapter<T, UltimateRecyclerviewViewHolder>
        implements ExchangeItemManagerInterface {

    public ExchangeableUltimateViewAdapter(List<T> list) {
        super(list);
    }


    protected ExchangeItemManagerImpl mItemManger = new ExchangeItemManagerImpl(this);

    /**
     * binding normal view holder
     *
     * @param holder   holder class
     * @param data     data
     * @param position position
     */
    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder holder, T data, int position) {
        mItemManger.updateConvertView(holder, position);
    }

    @Override
    protected void onBindAdViewHolder(RecyclerView.ViewHolder holder, int pos) {
        mItemManger.updateConvertView((UltimateRecyclerviewViewHolder) holder, pos);
    }

    @Override
    protected void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int pos) {
        mItemManger.updateConvertView((UltimateRecyclerviewViewHolder) holder, pos);
    }

    @Override
    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int pos) {
      //  mItemManger.updateConvertView((UltimateRecyclerviewViewHolder) holder, pos);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int pos) {
       // mItemManger.updateConvertView((UltimateRecyclerviewViewHolder) holder, pos);
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(ExchangeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<ExchangeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(ExchangeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public ExchangeItemManagerImpl.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(ExchangeItemManagerImpl.Mode mode) {
        mItemManger.setMode(mode);
    }

    public static class BaseSwipeableViewHolder extends RecyclerView.ViewHolder {

        public ExchangeLayout exchangeLayout = null;
        public ExchangeLayout.OnLayout onLayoutListener = null;
        public ExchangeLayout.ExchangeListener swipeMemory = null;
        public int position = -1;

        public BaseSwipeableViewHolder(View itemView) {
            super(itemView);

            exchangeLayout = (ExchangeLayout) itemView.findViewById(R.id.recyclerview_swipe);
        }
    }


    @Override
    public void insert(List<T> new_data) {
        super.insert(new_data);
        closeAllExcept(null);
    }


    @Override
    public void removeAll() {
        super.removeAll();
    }
}
