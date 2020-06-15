package com.urgentrn.urncexchange.ui.fragments.card;

import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.Link;
import com.urgentrn.urncexchange.ui.adapter.DocAdapter;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.activity_docs)
public class CardholderFragment extends BaseFragment {

    @ViewById
    TextView txtTitle;

    @ViewById
    RecyclerView recyclerView;

    @AfterViews
    protected void init() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);
        getView().setClickable(true);

        txtTitle.setText(R.string.cardholder_docs);

        final List<Link> links = new ArrayList<>();
        links.add(new Link("terms", getString(R.string.cardholder_agreement), getArguments().getString("terms")));
        links.add(new Link("privacy_policy", getString(R.string.privacy_policy), getArguments().getString("privacy_policy")));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(new DocAdapter(links));
    }
}
