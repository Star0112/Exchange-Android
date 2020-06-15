package com.urgentrn.urncexchange.ui.dialogs;

import android.os.Handler;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

import at.grabner.circleprogress.CircleProgressView;

@EFragment(R.layout.dialog_pin_view)
public class PINViewDialog extends BaseDialog {

    @ViewById
    TextView txtTitle, txtDescription, txtProgress;

    @ViewById
    CircleProgressView progressView;

    private int progress;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progress += 1;
            txtProgress.setText(String.format(Locale.US, "%d", 10 - progress));
            if (progress == 10) {
                dismiss();
                return;
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    @AfterViews
    protected void init() {
        if (getArguments() == null) return;

        txtTitle.setText(getArguments().getString("title"));
        txtDescription.setText(getArguments().getString("description"));

        progressView.setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.codec_pro_bold));
        progressView.setUnitTextTypeface(ResourcesCompat.getFont(getContext(), R.font.codec_pro_bold));
    }

    @Override
    public void onStart() {
        super.onStart();

        progressView.setValueAnimated(100, 10000);
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onStop() {
        super.onStop();

        handler.removeCallbacks(runnable);
    }
}
