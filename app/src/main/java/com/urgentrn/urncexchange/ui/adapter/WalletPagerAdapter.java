package com.urgentrn.urncexchange.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.request.UpdateWalletRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.UpdateWalletResponse;
import com.urgentrn.urncexchange.ui.MainActivity;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import java.util.ArrayList;
import java.util.List;

public class WalletPagerAdapter extends PagerAdapter {

    private OnItemClickListener mListener;
    private List<Wallet> data = new ArrayList<>();

    public WalletPagerAdapter(OnItemClickListener listener) {
        this.mListener = listener;
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
                .inflate(R.layout.pager_wallet, container, false);

        final Wallet wallet = data.get(position);
        final int color = Color.parseColor(wallet.getColor());

        view.findViewById(R.id.bgWallet).setBackgroundColor(color);
        ImageView imgCoin = view.findViewById(R.id.imgCoin);
        Glide.with(container.getContext())
                .load(wallet.getSymbolData().getDefaultImage())
                .into(imgCoin);
        imgCoin.setOnClickListener(v -> {
            if (!wallet.getSymbolData().isCurrency()) {
                ((MainActivity)container.getContext()).setActiveTab(R.id.navigation_wallet, wallet);
            }
        });
        ((TextView)view.findViewById(R.id.txtCoin)).setText(wallet.getTitle().toUpperCase());
        ((TextView)view.findViewById(R.id.txtBalanceUSD)).setText(wallet.getBalanceCurrencyFormatted());
        ((TextView)view.findViewById(R.id.txtBalance)).setText(String.format("%s %s", Utils.formattedNumber(wallet.getBalance(), 0, 5), wallet.getSymbol()));

        final Switch switchCard = view.findViewById(R.id.switchCard);
        final Button btnBuy = view.findViewById(R.id.btnBuy);
        final Button btnSell = view.findViewById(R.id.btnSell);
        final View llLock = view.findViewById(R.id.llLock);

        llLock.setVisibility(View.GONE);
        if (wallet.isExchangeWallet()) {
            final double reserveAmount = wallet.getReservedBalance();
            if (reserveAmount > 0) {
                ((TextView)view.findViewById(R.id.txtLock)).setText(String.format("%s %s", Utils.formattedNumber(reserveAmount), wallet.getSymbol()));
                llLock.setVisibility(View.VISIBLE);
                llLock.setOnClickListener(v -> mListener.onItemClick(-1));
            }
        }

        switchCard.setChecked(wallet.isDefault());
        if (wallet.getSymbolData().isCurrency()) {
            btnBuy.setText(R.string.deposit);
            btnSell.setText(R.string.withdraw);
        } else {
            btnBuy.setText(R.string.buy);
            btnSell.setText(R.string.sell);
        }

        btnBuy.setOnClickListener(v -> mListener.onItemClick(wallet.getSymbolData().isCurrency() ? WalletUtils.TransactionType.DEPOSIT.ordinal() : WalletUtils.TransactionType.BUY.ordinal()));
        btnSell.setOnClickListener(v -> mListener.onItemClick(wallet.getSymbolData().isCurrency() ? WalletUtils.TransactionType.WITHDRAW.ordinal() : WalletUtils.TransactionType.SELL.ordinal()));
        switchCard.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                switchCard.setChecked(true);
                ((BaseActivity)container.getContext()).showAlert(R.string.error_no_default_wallet);
                return;
            }
            ApiClient.getInterface()
                    .updateDefaultWallet(wallet.getSymbol(), new UpdateWalletRequest(true))
                    .enqueue(new AppCallback<>(container.getContext(), new ApiCallback() {
                        @Override
                        public void onResponse(BaseResponse response) {
                            if (response instanceof UpdateWalletResponse) {
                                if (((UpdateWalletResponse)response).isDefault()) {
                                    for (Wallet wallet1 : data) {
                                        wallet1.setDefault(wallet1.getSymbol().equals(wallet.getSymbol()));
                                    }
                                    notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onFailure(String message) {

                        }
                    }));
        });

        container.addView(view);
        return view;
    }

    public Wallet getItem(int position) {
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

    public void setData(List<Wallet> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
