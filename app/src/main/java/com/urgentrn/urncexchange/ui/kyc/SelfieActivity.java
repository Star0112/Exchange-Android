package com.urgentrn.urncexchange.ui.kyc;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_selfie)
public class SelfieActivity extends BaseActivity {

    @ViewById
    TextView txtTitle, txtStep, txtHint;

    @ViewById
    ImageView imgDocument;

    private int step;

    @AfterViews
    protected void init() {
        setToolBar(true);

        step = getIntent().getIntExtra("step", 0);
        txtStep.setText(getString(R.string.step_of, step, 4));
        switch (step) {
            case 3:
                txtTitle.setText(R.string.take_selfie);
                imgDocument.setImageResource(R.mipmap.kyc2);
                txtHint.setText(R.string.scan_hint4);
                break;
            case 4:
                txtTitle.setText(R.string.proof_of_address);
                imgDocument.setImageResource(R.mipmap.kyc3);
                txtHint.setText(R.string.scan_hint7);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onNext(View v) {
        Intent intent = new Intent(this, ScanActivity_.class);
        intent.putExtra("step", step);
        startActivity(intent);
    }
}
