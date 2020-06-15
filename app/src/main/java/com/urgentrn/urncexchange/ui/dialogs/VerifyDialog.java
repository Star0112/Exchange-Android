package com.urgentrn.urncexchange.ui.dialogs;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.ui.base.BaseDialog;
import com.urgentrn.urncexchange.ui.kyc.DocUploadActivity_;
import com.urgentrn.urncexchange.ui.kyc.VerifyAccountActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_verify)
public class VerifyDialog extends BaseDialog {

    @ViewById
    TextView txtTitle, txtSubtitle;

    @ViewById
    View btnVerify, llPending;

    private int level;

    @AfterViews
    protected void init() {
        if (getArguments() != null) {
            final String title = getArguments().getString("title");
            if (!TextUtils.isEmpty(title)) {
                txtTitle.setText(title);
            } else {
                txtSubtitle.setVisibility(View.GONE);
            }
        } else {
            txtSubtitle.setVisibility(View.GONE);
        }

        final User user = ExchangeApplication.getApp().getUser();
        if (user == null) { // TODO: when does this happen?
            onClose();
            return;
        }

        level = user.getTierLevel();
        final boolean isPending = user.getTierLevel() == 2 && user.isTierPending();

        btnVerify.setVisibility(isPending ? View.INVISIBLE : View.VISIBLE);
        llPending.setVisibility(isPending ? View.VISIBLE : View.INVISIBLE);
    }

    @Click(R.id.btnVerify)
    void onVerify() {
        Intent intent;
        if (level == 0) {
            intent = new Intent(getContext(), VerifyAccountActivity_.class);
        } else {
            intent = new Intent(getContext(), DocUploadActivity_.class);
        }
        startActivity(intent);
        dismiss();
    }

    @Click(R.id.btnClose)
    void onClose() {
        dismiss();
    }
}
