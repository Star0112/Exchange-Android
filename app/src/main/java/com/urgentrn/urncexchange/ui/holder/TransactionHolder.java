package com.urgentrn.urncexchange.ui.holder;

import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.models.Transaction;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import java.util.Locale;

public class TransactionHolder extends RecyclerView.ViewHolder {

    private ImageView imgType, imgLock, imgStatus;
    private TextView txtType, txtWhere, txtAmount, txtPrice;

    public TransactionHolder(View itemView) {
        super(itemView);

        imgType = itemView.findViewById(R.id.imgType);
        imgLock = itemView.findViewById(R.id.imgLock);
        imgStatus = itemView.findViewById(R.id.imgStatus);
        txtType = itemView.findViewById(R.id.txtType);
        txtWhere = itemView.findViewById(R.id.txtWhere);
        txtAmount = itemView.findViewById(R.id.txtAmount);
        txtPrice = itemView.findViewById(R.id.txtPrice);
    }

    public void updateView(Wallet wallet, Transaction transaction) {
        int imgRes = 0, amountTextColor, priceTextColor;
        String typeText = null, amountText, priceText;

        if (transaction.getType() == null) return;
        final String typeLowerCase = transaction.getType().toLowerCase();
        if (typeLowerCase.equals("send") || typeLowerCase.contains("withdraw")) {
            imgRes = R.mipmap.ic_sent;
        } else if (typeLowerCase.equals("receive") || typeLowerCase.contains("deposit")) {
            imgRes = R.mipmap.ic_received;
        } else if (typeLowerCase.contains("exchange")) {
            if (transaction.getExchange() != null) {
                final String type = WalletUtils.checkBuyOrSell(transaction.getSymbol(), transaction.getExchange().getSymbol());
                if (type.equals("buy")) {
                    imgRes = R.mipmap.ic_bought;
                } else if (type.equals("sell")) {
                    imgRes = R.mipmap.ic_sold;
                } else {
                    imgRes = R.mipmap.ic_exchanged;
                }
            } else {
                // TODO: when does this happen?
            }
        } else if (typeLowerCase.contains("purchase")) {
            imgRes = R.mipmap.ic_spent;
        } else if (typeLowerCase.contains("refund")) {
            imgRes = R.mipmap.ic_refund;
        } else if (typeLowerCase.contains("reward")) {
            imgRes = R.mipmap.ic_reward;
        } else if (typeLowerCase.contains("reserveentry")) {
            if (typeLowerCase.equals("reserveentryrelease")) {
                imgRes = R.mipmap.ic_unlock_outline;
            } else {
                imgRes = R.mipmap.ic_lock_outline;
            }
        }

        if (TextUtils.isEmpty(transaction.getString())) {
            if (transaction.getType().equals("reserveEntry")) {
                typeText = String.format("%s Card Lock", wallet.getSymbol());
            } else if (transaction.getType().equals("reserveEntryRelease")) {
                typeText = String.format("%s Card Unlock", wallet.getSymbol());
            } else {
                String type = null;
                if (ExchangeApplication.getApp().getConfig() != null) {
                    type = ExchangeApplication.getApp().getConfig().getStrings().getTransactions().get(transaction.getType().toLowerCase());
                    if (type == null) {
                        type = ExchangeApplication.getApp().getConfig().getStrings().getTransactions().get(transaction.getType());
                        if (type != null) typeText = type; // for combined strings like "exchangeReceive"
                    }
                }
                if (typeText == null) typeText = (type != null ? type : transaction.getType()) + " " + wallet.getTitle();
            }
        } else {
            typeText = transaction.getString();
        }

        imgLock.setVisibility(View.GONE);
        if (wallet.getSymbolData().isCurrency()) {
            if (transaction.getType().contains("exchange")) {
                if (transaction.getExchange() != null) {
                    amountText = String.format(Locale.US, "%s %s", Utils.formattedNumber(transaction.getExchange().getAmount()), transaction.getExchange().getSymbol());
                } else {
                    // TODO: when does this happen?
                    amountText = String.format("%s %s", transaction.getAmountFormatted(), transaction.getSymbol());
                }
            } else {
                amountText = String.format("%s %s", transaction.getAmountFormatted(), transaction.getSymbol());
            }
        } else {
            amountText = String.format("%s %s", transaction.getAmountFormatted(), transaction.getSymbol());
            if (transaction.getType().equals("reserveEntry")) {
                imgLock.setVisibility(View.VISIBLE);
            }
        }

        if (transaction.getStatus().equals("completed")) {
            priceText = transaction.getUsdAmountAtTransactionFormatted();
            imgStatus.setVisibility(View.GONE);
        } else {
            priceText = transaction.getStatus().substring(0, 1).toUpperCase() + transaction.getStatus().substring(1);
            imgStatus.setImageTintList(ColorStateList.valueOf(itemView.getResources().getColor(transaction.getStatus().equals("failed") ? R.color.colorRed : R.color.colorPending)));
            imgStatus.setVisibility(View.VISIBLE);
        }

        if (typeLowerCase.contains("withdraw") || typeLowerCase.contains("deposit")) {
            amountText = String.format("%s %s", transaction.getUsdAmountNowFormatted(), transaction.getSymbol());
            if (typeLowerCase.contains("withdraw")) {
                amountText = "-" + amountText;
            }
            if (transaction.getStatus().equals("completed")) {
                priceText = "";
            }
        }

        final boolean isMoreTransparent;
        if (wallet.getSymbolData().isCurrency() && transaction.getType().contains("exchange")) {
            if (transaction.getAmountFormatted().contains("-")) {
                amountTextColor = itemView.getResources().getColor(R.color.colorComplete);
                priceTextColor = itemView.getResources().getColor(R.color.textColorDefault);
                isMoreTransparent = true;
            } else {
                amountTextColor = itemView.getResources().getColor(R.color.textColorDefault);
                priceTextColor = itemView.getResources().getColor(R.color.colorComplete);
                isMoreTransparent = false;
            }
        } else {
            if (transaction.getAmountFormatted().contains("-")) {
                amountTextColor = itemView.getResources().getColor(R.color.textColorDefault);
                priceTextColor = itemView.getResources().getColor(R.color.colorComplete);
                isMoreTransparent = false;
            } else {
                amountTextColor = itemView.getResources().getColor(R.color.colorComplete);
                priceTextColor = itemView.getResources().getColor(R.color.textColorDefault);
                isMoreTransparent = true;
            }
        }

        imgType.setImageResource(imgRes);
        txtType.setText(typeText);
        txtWhere.setText(transaction.getSecondText());
        txtAmount.setText(amountText);
        txtAmount.setTextColor(amountTextColor);
        txtPrice.setAlpha(isMoreTransparent ? 0.3f : 0.56f);
        txtPrice.setText(priceText);
        txtPrice.setTextColor(priceTextColor);
    }
}
