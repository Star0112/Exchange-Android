package com.urgentrn.urncexchange.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.TierLevel;

import java.util.HashMap;
import java.util.List;

public class TierAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> keys;
    private HashMap<String, TierLevel.Period> periods;
    private List<String> requirements;

    public TierAdapter(Context context, List<String> keys, HashMap<String, TierLevel.Period> periods, List<String> requirements) {
        this.context = context;
        this.keys = keys;
        this.periods = periods;
        this.requirements = requirements;
    }

    @Override
    public int getGroupCount() {
        return keys.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (getGroup(groupPosition).equals("requirements")) {
            return requirements.size();
        } else {
            final TierLevel.Period period = periods.get(getGroup(groupPosition));
            return period == null ? 0 : period.getLimits().size() == 0 ? 1 : period.getLimits().size();
        }
    }

    @Override
    public String getGroup(int groupPosition) {
        return keys.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (getGroup(groupPosition).equals("requirements")) {
            return requirements.get(childPosition);
        } else {
            final TierLevel.Period period = periods.get(getGroup(groupPosition));
            return period != null && period.getLimits().size() > 0 ? period.getLimits().get(childPosition) : "N/A";
        }
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
            convertView = inflater.inflate(R.layout.group_tier, null);
        }

        final TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        if (getGroup(groupPosition).equals("requirements")) {
            txtTitle.setText(context.getString(R.string.requirements));
        } else {
            final TierLevel.Period period = periods.get(getGroup(groupPosition));
            if (period != null) {
                txtTitle.setText(period.getTitle());
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_default, null);
        }

        final TextView txtName = convertView.findViewById(R.id.txtName);
        final TextView txtValue = convertView.findViewById(R.id.txtValue);
        if (getChild(groupPosition, childPosition) instanceof String) {
            txtName.setText((String)getChild(groupPosition, childPosition));
            txtValue.setVisibility(View.GONE);
        } else {
            final TierLevel.Period.Limit limit = (TierLevel.Period.Limit)getChild(groupPosition, childPosition);
            txtName.setText(limit.getTitle());
            txtValue.setText(limit.isNoLimit() ? context.getString(R.string.no_limit) : limit.getAmountFormatted());
            txtValue.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
