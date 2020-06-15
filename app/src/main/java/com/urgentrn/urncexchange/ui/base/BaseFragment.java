package com.urgentrn.urncexchange.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.ui.MainActivity;
import com.urgentrn.urncexchange.ui.dialogs.VerifyDialog;
import com.urgentrn.urncexchange.ui.dialogs.VerifyDialog_;
import com.urgentrn.urncexchange.ui.fragments.OnFragmentInteractionListener;

public abstract class BaseFragment extends Fragment {

    protected OnFragmentInteractionListener mListener;
    private int statusBarColor;
    private boolean isLoading;
    private final VerifyDialog verifyDialog = new VerifyDialog_();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        updateStatusBarColor();

        view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        final View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected User getUser() {
        return ExchangeApplication.getApp().getUser();
    }

    protected void setBackgroundColor(int color) {
        if (getView() == null) return;
        getView().setBackgroundColor(color);
        setStatusBarColor(color);
    }

    protected int getStatusBarColor() {
        return statusBarColor;
    }

    protected void setStatusBarColor(int color) {
        statusBarColor = color;
        updateStatusBarColor();
    }

    public void updateStatusBarColor() {
        if (!isAdded()) return;
        if (getParentFragment() == null) { // check if container fragment
            if (getActivity() instanceof MainActivity && !((MainActivity)getActivity()).isActiveFragment(this)) {
                return;
            }
            final int backStackEntryCount = getChildFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount < getChildFragmentManager().getFragments().size()) { // fit status bar color to child screen
                final Fragment topChildFragment = getChildFragmentManager().getFragments().get(backStackEntryCount);
                if (topChildFragment instanceof BaseFragment) {
                    ((BaseFragment)topChildFragment).updateStatusBarColor();
                    return;
                }
            }
        } else {
            if (getActivity() instanceof MainActivity && !((MainActivity)getActivity()).isActiveFragment((BaseFragment)getParentFragment())) {
                return;
            }
        }
        if (getActivity() != null) { // fit status bar color to this screen
            ((BaseActivity)getActivity()).setStatusBarColor(statusBarColor != 0 ? statusBarColor : getResources().getColor(R.color.colorWhite));
        }
    }

    protected void setToolBar(boolean isDark) {
        final Toolbar toolBar = getView().findViewById(R.id.toolBar);
        if (toolBar != null) {
            toolBar.setNavigationIcon(isDark ? R.mipmap.ic_back_white : R.mipmap.ic_back);
            toolBar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    protected void showAlert(String message) {
        if (getActivity() != null) {
            ((BaseActivity)getActivity()).showAlert(message);
        }
    }

    protected void showToast(String message, boolean styled) {
        if (!styled || getActivity() == null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            return;
        }
        final Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, 0, 0);
        toast.setView(getActivity().getLayoutInflater().inflate(R.layout.toast, null));
        ((TextView)toast.getView().findViewById(R.id.message)).setText(message);
        toast.show();
    }

    public void updateView() {

    }

    protected void showVerifyDialog(String title) {
        if (verifyDialog.getDialog() != null && verifyDialog.getDialog().isShowing()) return;
        final Bundle args = new Bundle();
        args.putString("title", title);
        verifyDialog.setArguments(args);
        verifyDialog.show(getChildFragmentManager(), "VERIFY");
    }

    public void replaceFragment(Fragment fragment, boolean shouldReplace) {
        final String tag = fragment.getClass().getSimpleName();
        final FragmentManager fragmentManager = getChildFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            final String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            if (fragmentTag != null && fragmentTag.equals(tag)) {
                return;
            }
        }
        if (shouldReplace) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                    .replace(R.id.container, fragment, tag)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                    .add(R.id.container, fragment, tag)
//                    .replace(R.id.container, fragment)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    public void onBackPressed() {
        if (!isLoading && getParentFragment() != null) {
            final FragmentManager fragmentManager = getParentFragment().getChildFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                ((BaseFragment)fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount() - 1)).updateStatusBarColor();
            } else {
                // TODO: when does this happen?
            }
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                    .remove(this)
                    .commitAllowingStateLoss();
            fragmentManager.popBackStack();
        }
    }
}
