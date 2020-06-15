package com.urgentrn.urncexchange.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.urgentrn.urncexchange.R;

public class BaseDialogHelper {

    private Context mContext;
    private ProgressBar mProgressBar;

    public BaseDialogHelper(Context context) {
        mContext = context;
    }

    public void showLoading() {
        if (mProgressBar == null)
            mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleSmall);

        ViewGroup parent = (ViewGroup)mProgressBar.getParent();
        if (parent != null)
            parent.removeView(mProgressBar);

        ((ViewGroup)((Activity)mContext).getCurrentFocus()).addView(mProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.bringToFront();
    }

    public void hideLoading() {
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
    }

    public void showAlert(int message) {
        showAlert(mContext.getString(message));
    }

    public void showAlert(String message) {
        showAlert(null, message);
    }

    public void showAlert(int title, int message) {
        showAlert(mContext.getString(title), message);
    }

    public void showAlert(String title, int message) {
        showAlert(title, mContext.getString(message));
    }

    public void showAlert(int title, String message) {
        showAlert(mContext.getString(title), message);
    }

    public void showAlert(String title, String message) {
        showAlert(title, message, null, null, null, false);
    }

    public void showAlert(String title, String message, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener listener, boolean cancelable) {
        if (((Activity)mContext).isFinishing()) return;
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveButtonText != null ? positiveButtonText : mContext.getString(R.string.button_ok), listener);
        if (listener != null && negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, listener);
        }
        builder.show();
    }
}
