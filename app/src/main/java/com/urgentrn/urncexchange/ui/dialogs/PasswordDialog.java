package com.urgentrn.urncexchange.ui.dialogs;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.LoginRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_password)
public class PasswordDialog extends BaseDialog implements ApiCallback {

    @ViewById
    View btnBack, btnClose, btnSignOut;

    @ViewById
    EditText editPassword;

    @ViewById
    TextView btnSubmit;

    private Constants.SecurityType type;
    private String prevAuth;
    private String colorString;

    @AfterViews
    protected void init() {
        if (getArguments() == null) return;

        type = (Constants.SecurityType)getArguments().getSerializable("type");
        prevAuth = getArguments().getString("prev_auth");

        setCancelable(type != Constants.SecurityType.DEFAULT && type != Constants.SecurityType.SETTING);
        btnBack.setVisibility(prevAuth == null ? View.GONE : View.VISIBLE);
        if (type == Constants.SecurityType.DEFAULT) {
            btnClose.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            btnClose.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }

        colorString = getArguments().getString("color");
        if (colorString != null) {
            final int color = Color.parseColor(colorString);
            btnSubmit.setBackgroundTintList(ColorStateList.valueOf(color));
        }

        UIUtil.showKeyboardInDialog(getDialog(), editPassword);
    }

    @Click(R.id.btnBack)
    void onBack() {
        dismissAllowingStateLoss();
        if ("fingerprint".equals(prevAuth)) {
            showFingerprintDialog(type, mListener);
        } else if ("passcode".equals(prevAuth)) {
            showPasscodeDialog(type, colorString, mListener);
        }
    }

    @Click({R.id.btnClose, R.id.btnSignOut})
    void onClose() {
        if (mListener != null) mListener.onDismiss(false);
        dismissAllowingStateLoss();
    }

    @Click(R.id.btnSubmit)
    void onSubmit() {
        final String password = editPassword.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.error_password_empty), Toast.LENGTH_SHORT).show();
        } else if (!Utils.isPasswordValid(password)) {
            Toast.makeText(getContext(), getString(R.string.error_password_short), Toast.LENGTH_SHORT).show();
        } else {
            UIUtil.hideKeyboard(getContext(), editPassword);
            if (ExchangeApplication.getApp().getUser() != null) {
                showProgressBar();
                ApiClient.getInterface()
                        .login(new LoginRequest(ExchangeApplication.getApp().getUser().getUsername(), password))
                        .enqueue(new AppCallback<>(getContext(), this));
            } else {
                onClose();
            }
        }
    }

    @EditorAction(R.id.editPassword)
    void onEditorActions(TextView v, int actionId) {
        onSubmit();
    }

    @Override
    public void onResponse(BaseResponse response) {
        hideProgressBar();
        if (mListener != null) mListener.onDismiss(true);
        dismissAllowingStateLoss();
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();
    }
}
