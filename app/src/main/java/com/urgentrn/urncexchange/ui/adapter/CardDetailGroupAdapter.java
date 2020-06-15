package com.urgentrn.urncexchange.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.card.CardDetail;
import com.urgentrn.urncexchange.models.card.CardMeta;

import java.util.ArrayList;
import java.util.List;

public class CardDetailGroupAdapter extends BaseExpandableListAdapter {

    private List<CardDetail> data = new ArrayList<>();

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getMetas().size();
    }

    @Override
    public CardDetail getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public CardMeta getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getMetas().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_header, null);
        }

        final CardDetail detail = getGroup(groupPosition);
        final TextView txtTitle = convertView.findViewById(R.id.txtHeader);
        if (txtTitle != null) {
            final int paddingVertical = TextUtils.isEmpty(detail.getTitle()) ? 0 : convertView.getContext().getResources().getDimensionPixelSize(R.dimen.default_padding);
            txtTitle.setPadding(0, paddingVertical, 0, paddingVertical);
            txtTitle.setText(detail.getTitle());
            txtTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_card_group, null);
        }

        final CardMeta meta = getChild(groupPosition, childPosition);
        final TextView txtName = convertView.findViewById(R.id.txtName);
        final TextView txtValue = convertView.findViewById(R.id.txtValue);
        final ImageView imgCheck = convertView.findViewById(R.id.imgCheck);
        if (txtName != null) txtName.setText(meta.getName());
        if (txtValue != null) txtValue.setText(meta.getValue());
        if (imgCheck != null) {
            if (meta.getImage() != null) {
                Glide.with(convertView).load(meta.getImage()).into(imgCheck);
                imgCheck.setVisibility(View.VISIBLE);
            } else {
                imgCheck.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setData(List<CardDetail> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
