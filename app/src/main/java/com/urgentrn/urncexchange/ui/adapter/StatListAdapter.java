package com.urgentrn.urncexchange.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class StatListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Stat> stats = new ArrayList<>();
    private int color;

    public static class Stat {
        int title;
        double value;
        int icon;
        int description;

        public Stat(int titleResId, double value, int iconResId, int descriptionResId) {
            this.title = titleResId;
            this.value = value;
            this.icon = iconResId;
            this.description = descriptionResId;
        }
    }

    public StatListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return stats.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return stats.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return stats.get(groupPosition).description;
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_market_stat, null);
        }

        final TextView title = convertView.findViewById(R.id.title);
        final TextView value = convertView.findViewById(R.id.value);
        final ImageView icon = convertView.findViewById(R.id.icon);
        final ImageView arrow = convertView.findViewById(R.id.arrow);

        final Stat stat = (Stat) getGroup(groupPosition);
        title.setText(stat.title);
        value.setText(Utils.formatLargeNumber(stat.value));
        icon.setImageResource(stat.icon);
        icon.setImageTintList(ColorStateList.valueOf(color));
        arrow.setImageResource(isExpanded ? R.mipmap.ic_add : R.mipmap.ic_add);
        arrow.setImageTintList(ColorStateList.valueOf(color));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_market_stat, null);
        }

        final TextView description = convertView.findViewById(android.R.id.text1);
        description.setText((int)getChild(groupPosition, childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setData(List<Stat> stats, int color) {
        this.stats = stats;
        this.color = color;
        notifyDataSetChanged();
    }
}
