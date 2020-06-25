package com.urgentrn.urncexchange.ui.base;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.ui.MainActivity;
import com.urgentrn.urncexchange.ui.dialogs.PasscodeDialog;
import com.urgentrn.urncexchange.ui.dialogs.PasscodeDialog_;
import com.urgentrn.urncexchange.ui.dialogs.PasswordDialog;
import com.urgentrn.urncexchange.ui.dialogs.PasswordDialog_;
import com.urgentrn.urncexchange.ui.view.PermissionDialog;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import me.aflak.libraries.callback.FingerprintDialogCallback;
import me.aflak.libraries.dialog.FingerprintDialog;

public abstract class BaseActivity extends AppCompatActivity {

    private final BaseDialogHelper mDialogHelper = new BaseDialogHelper(this);
    private PasswordDialog passwordDialog;
    private PasscodeDialog passcodeDialog;

    protected void setToolBar(boolean isDark) {
        final Toolbar toolBar = findViewById(R.id.toolBar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setHomeAsUpIndicator(isDark ? R.mipmap.ic_back_white : R.mipmap.ic_back);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public BaseDialogHelper getDialogHelper() {
        return mDialogHelper;
    }

    public void showProgressBar() {
        Utils.hideKeyboard(this, getCurrentFocus());
        final View view = findViewById(R.id.progressBar);
        if (view != null) view.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        final View view = findViewById(R.id.progressBar);
        if (view != null) view.setVisibility(View.GONE);
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    public void requestPermission(final String[] permissions, String rationale, final int requestCode, boolean shouldFinishActivityOnDismiss) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
            PermissionDialog
                    .newInstance(rationale, permissions, requestCode, null)
                    .shouldFinishActivityOnDismiss(shouldFinishActivityOnDismiss)
                    .show(getSupportFragmentManager(), null);
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    public void showAlert(int message) {
        showAlert(message, true);
    }

    public void showAlert(int message, boolean shouldShowTitle) {
        showAlert(message > 0 ? getString(message) : null, shouldShowTitle);
    }

    public void showAlert(String message) {
        showAlert(message, true);
    }

    public void showAlert(String message, boolean shouldShowTitle) {
        showAlert(shouldShowTitle ? getString(R.string.app_name2) : null, message);
    }

    public void showAlert(String title, String message) {
        mDialogHelper.showAlert(title, message);
    }

    public void showAlert(int message, DialogInterface.OnClickListener listener) {
        showAlert(message > 0 ? getString(message) : null, listener);
    }

    public void showAlert(String message, DialogInterface.OnClickListener listener) {
        showAlert(getString(R.string.app_name2), message, null, listener, false);
    }

    public void showAlert(int message, int positiveButtonText, int negativeButtonText, DialogInterface.OnClickListener listener) {
        showAlert(getString(R.string.app_name2), message > 0 ? getString(message) : null, getString(positiveButtonText), getString(negativeButtonText), listener, false);
    }

    public void showAlert(int message, int positiveButtonText, int negativeButtonText, DialogInterface.OnClickListener listener, boolean cancelable) {
        showAlert(getString(R.string.app_name2), message > 0 ? getString(message) : null, getString(positiveButtonText), getString(negativeButtonText), listener, cancelable);
    }

    public void showAlert(String title, String message, String positiveButtonText, DialogInterface.OnClickListener listener, boolean cancelable) {
        showAlert(title, message, positiveButtonText, null, listener, cancelable);
    }

    public void showAlert(String title, String message, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener listener, boolean cancelable) {
        mDialogHelper.showAlert(title, message, positiveButtonText, negativeButtonText, listener, cancelable);
    }

    public void setStatusBarColor(int color) {
        setStatusBarColor(color, color);
    }

    public void setStatusBarColor(int statusBarColor, int navigationBarColor) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int flags = getWindow().getDecorView().getSystemUiVisibility();
            if (statusBarColor == getColor(R.color.colorWhite) ||
                    statusBarColor == getColor(R.color.colorBackground) ||
                    statusBarColor == getColor(R.color.colorYellow) ||
                    statusBarColor == getColor(R.color.colorCardBackground1) ||
                    statusBarColor == getColor(R.color.colorCardBackground2) ||
                    statusBarColor == getColor(R.color.colorCardBackground3)) {
                getWindow().getDecorView().setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else if (Color.alpha(statusBarColor) < 0xA0) {
                getWindow().getDecorView().setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(flags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        getWindow().setStatusBarColor(statusBarColor);
        if (navigationBarColor != 0 && !(this instanceof MainActivity)) getWindow().setNavigationBarColor(navigationBarColor);
    }

    public void showPassDialog(Constants.SecurityType type, BaseDialog.OnDialogDismissListener listener) {
         if (ExchangeApplication.getApp().getPreferences().isFingerprintEnabled()) {
            showFingerprintDialog(type, listener);
        } else if (ExchangeApplication.getApp().getPreferences().isPasscodeEnabled()) {
            showPasscodeDialog(type, null, listener);
        } else {
            showPasswordDialog(type, null, listener, null);
        }
    }

    public void showPasswordDialog(Constants.SecurityType type, String color, BaseDialog.OnDialogDismissListener listener, String prevAuth) {
        if (passwordDialog != null && passwordDialog.getDialog() != null && passwordDialog.getDialog().isShowing()) return;
        if (passcodeDialog != null && passcodeDialog.getDialog() != null && passcodeDialog.getDialog().isShowing()) return;

        passwordDialog = new PasswordDialog_();
        passwordDialog.setOnDialogDismissListener(listener);
        final Bundle args = new Bundle();
        args.putSerializable("type", type);
        args.putString("color", color);
        args.putString("prev_auth", prevAuth);
        passwordDialog.setArguments(args);
        passwordDialog.show(getSupportFragmentManager(), "PASSWORD");
    }

    public void showPasscodeDialog(Constants.SecurityType type, String color, BaseDialog.OnDialogDismissListener listener) {
        if (passwordDialog != null && passwordDialog.getDialog() != null && passwordDialog.getDialog().isShowing()) return;
        if (passcodeDialog != null && passcodeDialog.getDialog() != null && passcodeDialog.getDialog().isShowing()) return;

        passcodeDialog = new PasscodeDialog_();
        passcodeDialog.setOnDialogDismissListener(listener);
        final Bundle args = new Bundle();
        args.putSerializable("type", type);
        args.putString("color", color);
        passcodeDialog.setArguments(args);
        passcodeDialog.show(getSupportFragmentManager(), "PASSCODE");
    }

    public void showBiometrics(Constants.SecurityType type, BaseDialog.OnDialogDismissListener listener) {
        final boolean usePassword = type != Constants.SecurityType.SETTING;
        final BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {
            showAlert(R.string.biometrics_not_available);
            if (!usePassword) return;
            if (ExchangeApplication.getApp().getPreferences().isPasscodeEnabled()) {
                showPasscodeDialog(type, null, listener);
            } else {
                showPasswordDialog(type, null, listener, null);
            }
            return;
        }
        final int negativeButtonTextId;
        if (type == Constants.SecurityType.SETTING) {
            negativeButtonTextId = R.string.button_cancel;
        } else {
            if (ExchangeApplication.getApp().getPreferences().isPasscodeEnabled()) {
                negativeButtonTextId = R.string.use_passcode;
            } else {
                negativeButtonTextId = R.string.use_password;
            }
        }
        final BiometricPrompt biometricPrompt = new BiometricPrompt(this, ContextCompat.getMainExecutor(this), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                if (negativeButtonTextId == R.string.button_cancel) {
                    if (listener != null) listener.onDismiss(false);
                } else {
                    if (negativeButtonTextId == R.string.use_passcode) {
                        showPasscodeDialog(type, null, listener);
                    } else {
                        showPasswordDialog(type, null, listener, null);
                    }
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                if (listener != null) listener.onDismiss(true);
            }

            @Override
            public void onAuthenticationFailed() {

            }
        });
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.app_name2))
                .setDescription(getString(R.string.confirm_biometrics))
                .setNegativeButtonText(getString(negativeButtonTextId))
                .setConfirmationRequired(type == Constants.SecurityType.CARD || type == Constants.SecurityType.TRANSACTION)
                .setDeviceCredentialAllowed(false)
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    public void showFingerprintDialog(Constants.SecurityType type, BaseDialog.OnDialogDismissListener listener) {
        if (Constants.USE_BIOMETRICS) {
            showBiometrics(type, listener);
            return;
        }

        final boolean usePassword = type != Constants.SecurityType.SETTING;
        if (!FingerprintDialog.isAvailable(this)) {
            showAlert(R.string.fingerprint_not_available);
            if (!usePassword) return;
            if (ExchangeApplication.getApp().getPreferences().isPasscodeEnabled()) {
                showPasscodeDialog(type, null, listener);
            } else {
                showPasswordDialog(type, null, listener, null);
            }
            return;
        }
        FingerprintDialog.initialize(this)
                .title(R.string.app_name2)
                .message(R.string.confirm_fingerprint)
                .titleFont(R.font.codec_pro_bold)
                .messageFont(R.font.codec_pro_regular)
                .passwordText(ExchangeApplication.getApp().getPreferences().isPasscodeEnabled() ? getString(R.string.use_passcode) : null)
                .cancelText(type == Constants.SecurityType.DEFAULT ? R.string.sign_out : R.string.button_cancel)
                .usePasswordButton(!usePassword ? null : v -> {
                    if (ExchangeApplication.getApp().getPreferences().isPasscodeEnabled()) {
                        showPasscodeDialog(type, null, listener);
                    } else {
                        showPasswordDialog(type, null, listener, "fingerprint");
                    }
                })
                .cancelOnPressBack(type != Constants.SecurityType.DEFAULT && type != Constants.SecurityType.SETTING)
                .cancelOnTouchOutside(type != Constants.SecurityType.DEFAULT && type != Constants.SecurityType.SETTING)
                .callback(new FingerprintDialogCallback() {
                    @Override
                    public void onAuthenticationSucceeded() {
                        if (listener != null) listener.onDismiss(true);
                    }

                    @Override
                    public void onAuthenticationCancel() {
                        if (listener != null) listener.onDismiss(false);
                    }
                })
                .show();
    }

    public void onBack(View v) {
        onBackPressed();
    }
}
