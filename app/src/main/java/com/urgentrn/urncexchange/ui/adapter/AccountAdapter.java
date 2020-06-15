package com.urgentrn.urncexchange.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.bank.Account;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.holder.AccountHolder;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountHolder> {

    private List<Account> data = new ArrayList<>();

    @Override
    @NonNull
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_account, parent, false);

        return new AccountHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder viewHolder, int position) {
        final Account account = data.get(position);
        viewHolder.updateView(account);

        final Context context = viewHolder.itemView.getContext();
        viewHolder.llRemove.setOnClickListener(v -> {
            ((BaseActivity)context).showAlert(R.string.remove_account_confirm, R.string.remove, R.string.button_cancel, ((dialog, which) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    ApiClient.getInterface()
                            .removeBankAccount(data.get(viewHolder.getAdapterPosition()).getId())
                            .enqueue(new AppCallback<>(context, new ApiCallback() {
                                @Override
                                public void onResponse(BaseResponse response) {
                                    data.remove(viewHolder.getAdapterPosition());
                                    notifyItemRemoved(viewHolder.getAdapterPosition());
                                }

                                @Override
                                public void onFailure(String message) {

                                }
                            }));
                }
            }));
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Account> data) {
        if (data == null) return; // TODO: when does this happen?
        this.data = data;
        notifyDataSetChanged();
    }
}
