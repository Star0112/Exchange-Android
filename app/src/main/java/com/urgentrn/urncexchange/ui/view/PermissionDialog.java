package com.urgentrn.urncexchange.ui.view;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.urgentrn.urncexchange.R;

public class PermissionDialog extends DialogFragment {

    private static final String ARG_MESSAGE = "message";
    private static final String ARG_PERMISSIONS = "permissions";
    private static final String ARG_REQUEST_CODE = "request_code";
    private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

    private boolean mShouldFinishActivityOnDismiss = false;

    public static PermissionDialog newInstance(String message, String[] permissions, int requestCode, String notGrantedMessage) {
        PermissionDialog fragment = new PermissionDialog();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        args.putStringArray(ARG_PERMISSIONS, permissions);
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putString(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.app_name)
                .setMessage(args.getString(ARG_MESSAGE))
                .setPositiveButton(R.string.button_ok, (dialog, which) -> {
                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                    if (permissions == null) throw new IllegalArgumentException();
                    ActivityCompat.requestPermissions(getActivity(), permissions, args.getInt(ARG_REQUEST_CODE));
                })
                .setNegativeButton(R.string.button_cancel, (dialog, which) -> {
                    final String message = args.getString(ARG_NOT_GRANTED_MESSAGE);
                    if (message != null) Toast.makeText(getActivity(), !message.isEmpty() ? message : "Permission denied", Toast.LENGTH_SHORT).show();
                    if (mShouldFinishActivityOnDismiss) getActivity().finish();
                })
                .setCancelable(false)
                .create();
    }

    public PermissionDialog shouldFinishActivityOnDismiss(boolean shouldFinishActivityOnDismiss) {
        this.mShouldFinishActivityOnDismiss = shouldFinishActivityOnDismiss;
        return this;
    }
}
