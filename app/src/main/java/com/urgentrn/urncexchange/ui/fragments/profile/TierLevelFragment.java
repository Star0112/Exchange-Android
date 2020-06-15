package com.urgentrn.urncexchange.ui.fragments.profile;

import android.widget.ExpandableListView;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.TierData;
import com.urgentrn.urncexchange.models.TierLevel;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetTiersResponse;
import com.urgentrn.urncexchange.ui.adapter.TierAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@EFragment(R.layout.fragment_tier_level)
public class TierLevelFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtTitle;

    @ViewById
    ExpandableListView listView;

    private static TierData tierData;
    private int level;

    @AfterViews
    protected void init() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);

        level = getArguments().getInt("level");
        txtTitle.setText(String.format(Locale.US, "%s %d", getString(R.string.tier_level), level));
        if (tierData == null) {
            ApiClient.getInterface().getTiers().enqueue(new AppCallback<>(getContext(), this));
        } else {
            updateView();
        }
    }

    @Override
    public void updateView() {
        HashMap<String, TierLevel.Period> periods = new HashMap<>();
        for (TierLevel tier : tierData.getTiers()) {
            if (tier.getTitle().contains(String.valueOf(level))) {
                periods = tier.getPeriods();
                break;
            }
        }

        final List<String> keys = new ArrayList<String>(){{
            add("daily");
            add("monthly");
            add("annual");
            add("requirements");
        }};
        final TierAdapter adapter = new TierAdapter(getContext(), keys, periods, tierData.getRequirements());
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);
        listView.setChildIndicator(null);
        listView.setDividerHeight(0);
        for (int i = 0; i < keys.size(); i ++) {
            listView.expandGroup(i);
        }
        listView.setOnGroupClickListener((parent, v, groupPosition, id) -> true);
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetTiersResponse) {
            tierData = ((GetTiersResponse)response).getData();
            updateView();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
