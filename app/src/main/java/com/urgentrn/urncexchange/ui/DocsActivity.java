package com.urgentrn.urncexchange.ui;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.ui.adapter.DocAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_docs)
public class DocsActivity extends BaseActivity {

    @ViewById
    RecyclerView recyclerView;

    @AfterViews
    protected void init() {
        if (ExchangeApplication.getApp().getConfig() == null) {
            finish();
            return;
        }

        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(new DocAdapter(ExchangeApplication.getApp().getConfig().getLinks()));
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
    }
}
