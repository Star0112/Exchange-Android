package com.urgentrn.urncexchange.ui.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.utils.Constants;

public abstract class BaseDialog extends BottomSheetDialogFragment {

    protected OnDialogDismissListener mListener;
    private boolean hideable = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(d -> {
            final FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            final BottomSheetBehavior behaviour = BottomSheetBehavior.from(bottomSheet);
            behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
            if (!hideable) behaviour.setHideable(false);
        });

        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        // TODO:
//        this.mDismissed = false;
//        this.mShownByMe = true;
        manager.beginTransaction().add(this, tag).commitAllowingStateLoss();
    }

    public void setHideable(boolean hideable) {
        this.hideable = hideable;
    }

    protected void showAlert(String message) {
        if (getActivity() != null) {
            ((BaseActivity)getActivity()).showAlert(message);
        }
    }

    protected void showAlert(String message, DialogInterface.OnClickListener callback) {
        if (getActivity() != null) {
            ((BaseActivity)getActivity()).showAlert(message, callback);
        }
    }

    protected void showProgressBar() {
        if (getView() != null) {
            final View view = getView().findViewById(R.id.progressBar);
            if (view != null) view.setVisibility(View.VISIBLE);
        }
    }

    protected void hideProgressBar() {
        if (getView() != null) {
            final View view = getView().findViewById(R.id.progressBar);
            if (view != null) view.setVisibility(View.GONE);
        }
    }

    protected void showPasswordDialog(Constants.SecurityType type, String color, OnDialogDismissListener listener, String prevAuth) {
        if (getActivity() == null) return;
        ((BaseActivity)getActivity()).showPasswordDialog(type, color, listener, prevAuth);
    }

    protected void showPasscodeDialog(Constants.SecurityType type, String color, OnDialogDismissListener listener) {
        if (getActivity() == null) return;
        ((BaseActivity)getActivity()).showPasscodeDialog(type, color, listener);
    }

    protected void showFingerprintDialog(Constants.SecurityType type, OnDialogDismissListener listener) {
        if (getActivity() == null) return;
        ((BaseActivity)getActivity()).showFingerprintDialog(type, listener);
    }

    public void setOnDialogDismissListener(OnDialogDismissListener listener) {
        this.mListener = listener;
    }

    protected void replaceView(View endingView, View startingView, boolean back) {
        replaceView(endingView, startingView, back, false);
    }

    protected void replaceView(View endingView, View startingView, boolean back, boolean isSameHeight) {
        final Animation inAnimation = AnimationUtils.loadAnimation(getContext(), back ? R.anim.slide_in_right : R.anim.slide_in_left);
        final Animation outAnimation = AnimationUtils.loadAnimation(getContext(), back ? R.anim.slide_out_right : R.anim.slide_out_left);
        inAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                startingView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                endingView.setVisibility(isSameHeight ? View.INVISIBLE : View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startingView.startAnimation(inAnimation);
        endingView.startAnimation(outAnimation);
    }

    public interface OnDialogDismissListener {
        void onDismiss(boolean isSuccess);
    }
}
