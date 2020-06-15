package com.marshalchen.ultimaterecyclerview.exchange;


import androidx.recyclerview.widget.RecyclerView;


import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SwipeItemMangerImpl is a helper class to help maintain swipe status
 */
public class ExchangeItemManagerImpl implements ExchangeItemManagerInterface {
    public final int INVALID_POSITION = -1;

    private Mode mode = Mode.Single;

    protected int mOpenPosition = INVALID_POSITION;

    protected Set<Integer> mOpenPositions = new HashSet<>();
    protected Set<ExchangeLayout> mShownLayouts = new HashSet<>();

    protected RecyclerView.Adapter mAdapter;

    public ExchangeItemManagerImpl(RecyclerView.Adapter adapter) {
        if (adapter == null)
            throw new IllegalArgumentException("Adapter can not be null");

        if (!(adapter instanceof ExchangeItemManagerInterface))
            throw new IllegalArgumentException("adapter should implement the SwipeAdapterInterface");

        this.mAdapter = adapter;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        mOpenPositions.clear();
        mShownLayouts.clear();
        mOpenPosition = INVALID_POSITION;
    }


    private void initialize(UltimateRecyclerviewViewHolder targetViewHolder, int position) {
        targetViewHolder.onLayoutListener = new OnLayoutListener(position);
        targetViewHolder.exchangeMemory = new ExchangeMemory(position);
        targetViewHolder.position = position;

        targetViewHolder.exchangeLayout.addExchangeListener(targetViewHolder.exchangeMemory);
        targetViewHolder.exchangeLayout.addOnLayoutListener(targetViewHolder.onLayoutListener);
    }


    public void updateConvertView(UltimateRecyclerviewViewHolder targetViewHolder, int position) {
        if (targetViewHolder.onLayoutListener == null) {
            initialize(targetViewHolder, position);
        }

        ExchangeLayout exchangeLayout = targetViewHolder.exchangeLayout;
        if (exchangeLayout == null)
            throw new IllegalStateException("can not find SwipeLayout in target view");

        mShownLayouts.add(exchangeLayout);

        ((ExchangeMemory) targetViewHolder.exchangeMemory).setPosition(position);
        ((OnLayoutListener) targetViewHolder.onLayoutListener).setPosition(position);
        targetViewHolder.position = position;
    }

    @Override
    public void openItem(int position) {
        if (mode == Mode.Multiple) {
            if (!mOpenPositions.contains(position))
                mOpenPositions.add(position);
        } else {
            mOpenPosition = position;
        }
    }

    @Override
    public void closeItem(int position) {
        if (mode == Mode.Multiple) {
            mOpenPositions.remove(position);
        } else {
            if (mOpenPosition == position)
                mOpenPosition = INVALID_POSITION;
        }
    }

    @Override
    public void closeAllExcept(ExchangeLayout layout) {
        for (ExchangeLayout s : mShownLayouts) {
            if (s != layout)
                s.close();
        }
    }

    @Override
    public void removeShownLayouts(ExchangeLayout layout) {
        mShownLayouts.remove(layout);
    }

    @Override
    public List<Integer> getOpenItems() {
        if (mode == ExchangeItemManagerInterface.Mode.Multiple) {
            return new ArrayList<>(mOpenPositions);
        } else {
            return Arrays.asList(mOpenPosition);
        }
    }

    @Override
    public List<ExchangeLayout> getOpenLayouts() {
        return new ArrayList<>(mShownLayouts);
    }

    @Override
    public boolean isOpen(int position) {
        if (mode == Mode.Multiple) {
            return mOpenPositions.contains(position);
        } else {
            return mOpenPosition == position;
        }
    }

    private class OnLayoutListener implements ExchangeLayout.OnLayout {

        private int position;

        OnLayoutListener(int position) {
            this.position = position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onLayout(ExchangeLayout v) {
            if (isOpen(position)) {
                v.open(false, false);
            } else {
                v.close(false, false);
            }
        }

    }

    private class ExchangeMemory extends SimpleExchangeListener {

        private int position;

        ExchangeMemory(int position) {
            this.position = position;
        }

        @Override
        public void onClose(ExchangeLayout layout) {
            if (mode == Mode.Multiple) {
                mOpenPositions.remove(position);
            } else {
                mOpenPosition = INVALID_POSITION;
            }
        }

        @Override
        public void onStartOpen(ExchangeLayout layout) {
            if (mode == Mode.Single) {
                closeAllExcept(layout);
            }
        }

        @Override
        public void onOpen(ExchangeLayout layout) {
            if (mode == ExchangeItemManagerInterface.Mode.Multiple)
                mOpenPositions.add(position);
            else {
                closeAllExcept(layout);
                mOpenPosition = position;
            }
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

}


