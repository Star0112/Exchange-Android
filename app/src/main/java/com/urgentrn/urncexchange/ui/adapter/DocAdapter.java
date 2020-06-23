package com.urgentrn.urncexchange.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Link;
//import com.urgentrn.urncexchange.ui.TermActivity_;
import com.urgentrn.urncexchange.ui.holder.DocHolder;

import java.util.List;

public class DocAdapter extends RecyclerView.Adapter<DocHolder> {

    private List<Link> data;

    public DocAdapter(List<Link> data) {
        this.data = data;
    }

    @Override
    @NonNull
    public DocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_doc, parent, false);

        return new DocHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DocHolder viewHolder, int position) {
        final Link link = data.get(position);
        viewHolder.setTitle(link.getTitle());
        viewHolder.itemView.setOnClickListener(v -> {
            final Context context = viewHolder.itemView.getContext();
//            final Intent intent = new Intent(context, TermActivity_.class);
//            intent.putExtra("title", link.getTitle());
//            intent.putExtra("url", link.getUrl());
//            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
