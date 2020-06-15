package com.urgentrn.urncexchange.ui;

import com.github.barteksc.pdfviewer.PDFView;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_dd_form)
public class DDFormActivity extends BaseActivity {

    @ViewById
    PDFView pdfView;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);

        pdfView.fromAsset("directdepositform.pdf")
                .load();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
    }
}
