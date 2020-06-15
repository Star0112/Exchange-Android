package com.urgentrn.urncexchange.ui.contacts;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.ui.adapter.SelectWalletAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_select_assets)
public class SelectAssetsActivity extends BaseActivity {

    @ViewById
    RecyclerView recyclerView;

    private SelectWalletAdapter adapter;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);

        final List<Integer> assetIds = (ArrayList)getIntent().getSerializableExtra("data");

        final List<Wallet> wallets = new ArrayList<>();
        for (Wallet wallet : AppData.getInstance().getWallets()) {
            if (!wallet.getSymbolData().isCurrency() && !assetIds.contains(wallet.getSymbolData().getId())) {
                wallets.add(wallet);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        adapter = new SelectWalletAdapter(null, wallets);
        adapter.setMultiSelectable(true);
        adapter.showBalance(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.button_done).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default: // should be Done button click
                onDone();
                return true;
        }
    }

    public void onDone() {
        setResult(RESULT_OK, new Intent().putExtra("data", (ArrayList)adapter.getSelectedIds()));
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
    }
}
