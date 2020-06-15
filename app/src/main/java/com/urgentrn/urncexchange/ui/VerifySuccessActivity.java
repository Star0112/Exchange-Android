package com.urgentrn.urncexchange.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.login.LoginActivity_;
import com.urgentrn.urncexchange.ui.login.LoginUsernameActivity_;
import com.urgentrn.urncexchange.ui.signup.PINCreateActivity_;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_verification_success)
public class VerifySuccessActivity extends BaseActivity {

    @ViewById
    TextView txtTitle, txtDescription;

    private Constants.VerifyType type;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.no_animation);

        type = (Constants.VerifyType) getIntent().getSerializableExtra("type");
        if (type == null) return;
        switch (type) {
            case PHONE:
                txtDescription.setText(R.string.phone_verify_success);
                break;
            case PASSWORD:
                txtTitle.setText(R.string.password_change_success_title);
                txtDescription.setText(R.string.password_change_success_description);
                break;
            case TIER1:
                txtDescription.setText(R.string.tier1_verify_success);
                break;
            case TIER2:
                txtTitle.setText(R.string.app_name2);
                txtDescription.setText(R.string.tier2_verify_pending);
                break;
            case BANK:
                txtTitle.setText(R.string.activated);
                txtDescription.setText(R.string.add_account_success);
                break;
            case CARD_ORDER:
                txtTitle.setText(R.string.congrats);
                txtDescription.setText(R.string.card_order_success);
                break;
            case CARD_UPGRADE:
                txtTitle.setText(R.string.congrats);
                txtDescription.setText(R.string.card_upgrade_success);
                break;
            case CARD_ORDER_PHYSICAL:
                txtTitle.setText(R.string.congrats);
                txtDescription.setText(R.string.card_order_physical_success);
                break;
            case CARD_ACTIVATE:
                txtTitle.setText(R.string.activated);
                txtDescription.setText(R.string.card_activate_success);
                break;
            case CARD_CREATE_PIN:
                txtTitle.setText(R.string.pin_set);
                txtDescription.setText(R.string.create_pin_success);
                break;
            case CARD_UPDATE_PIN:
                txtTitle.setText(R.string.pin_set);
                txtDescription.setText(R.string.change_pin_success);
                break;
            default:
                txtTitle.setText(R.string.thank_you);
                txtDescription.setText(null);
                break;
        }
    }

    public void onNext(View v) {
        final Intent intent = new Intent();
        Class cls;
        switch (type) {
            case PHONE:
                if (!getIntent().getBooleanExtra("is_changing", false)) {
                    cls = PINCreateActivity_.class;
                } else {
                    cls = MainActivity_.class;
                }
                break;
            case PASSWORD:
                if (Constants.USE_COMBINED_LOGIN) {
                    cls = LoginActivity_.class;
                    intent.putExtra("username", getIntent().getStringExtra("username"));
                    intent.putExtra("password", getIntent().getStringExtra("password"));
                } else {
                    cls = LoginUsernameActivity_.class;
                    intent.putExtra("username", getIntent().getStringExtra("username"));
                }
                break;
            case CARD_CREATE_PIN:
            case CARD_UPDATE_PIN:
                finish();
                return;
            default:
                cls = MainActivity_.class;
                intent.putExtra("verify_type", type);
                if (type == Constants.VerifyType.CARD_ACTIVATE) {
                    intent.putExtra("reference", getIntent().getStringExtra("reference"));
                    intent.putExtra("pin_type", getIntent().getStringExtra("pin_type"));
                }
                break;
        }
        intent.setClass(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_bottom);
    }

    @Override
    public void onBackPressed() {

    }
}
