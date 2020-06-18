package com.urgentrn.urncexchange.ui.adapter;

import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.ExchangeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Market;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.holder.ExchangableHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoinExchangeableAdapter extends ExchangeableUltimateViewAdapter<Wallet> {

    private OnItemClickListener mListener;
    public boolean isInEditMode = false;
    public List<Wallet> data; // for sort
    public List<String> symbols = new ArrayList<>(); // non-favorite list

    public CoinExchangeableAdapter(List<Wallet> data, OnItemClickListener listener, OnStartDragListener dragStartListener) {
        super(data);
        this.mListener = listener;
        this.mDragStartListener = dragStartListener;
        this.data = new ArrayList<>(data);
    }

    @Override
    public ExchangableHolder newFooterHolder(View view) {
        return new ExchangableHolder(view);
    }

    @Override
    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int pos) {
        holder.itemView.setOnClickListener(v -> {
            updateMode(true);
            mListener.onItemClick(-9999);
        });
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder holder, Wallet wallet, int position) {
        super.withBindHolder(holder, wallet, position);

        ((ExchangableHolder)holder).updateView(wallet, isInEditMode);

        final Market market = AppData.getInstance().getMarketCap(wallet.getSymbol());
        ((ExchangableHolder)holder).updateChart(market, wallet.getColor());

        holder.itemView.findViewById(R.id.llItem).setOnClickListener(v -> {
            if (isInEditMode) {
                if (symbols.contains(wallet.getSymbol())) {
                    symbols.remove(wallet.getSymbol());
                    ((ExchangableHolder)holder).updateChecked(true);
                } else {
                    symbols.add(wallet.getSymbol());
                    ((ExchangableHolder)holder).updateChecked(false);
                }
            } else {
                mListener.onItemClick(position);
            }
        });
        holder.itemView.findViewById(R.id.llFavorite).setOnClickListener(v -> {
            mListener.onItemClick(-position - 1);
            holder.exchangeLayout.close();
        });
        holder.itemView.findViewById(R.id.imgReorder).setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder);
            }
            return false;
        });
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        super.onItemMove(fromPosition, toPosition);
        Collections.swap(data, fromPosition, toPosition);
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.item_exchange_coin;
    }

    @Override
    protected ExchangableHolder newViewHolder(View view) {
        return new ExchangableHolder(view);
    }

    @Override
    public Wallet getItem(int position) {
        return source.get(position);
    }

    public List<Wallet> getData() {
        return source;
    }

    public void setData(List<Wallet> data) {
        this.source = data;
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public void updateMode(boolean editable) {
        isInEditMode = editable;
        symbols.clear();
        customLoadMoreItemView.findViewById(R.id.txtEdit).setVisibility(editable ? View.GONE : View.VISIBLE);
        notifyDataSetChanged();
    }
}
