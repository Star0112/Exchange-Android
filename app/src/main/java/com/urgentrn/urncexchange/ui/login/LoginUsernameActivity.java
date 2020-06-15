package com.urgentrn.urncexchange.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.ui.HomeActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login_username)
public class LoginUsernameActivity extends BaseActivity {

    @ViewById
    EditText editText;

    @ViewById
    CheckBox checkBox;

    @ViewById
    TextView txtForgotUsername;

    @AfterViews
    protected void init() {
        setToolBar(false);
        editText.requestFocus();

        txtForgotUsername.setText(String.format("%s?", getString(R.string.forgot_username)));
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            editText.setText(username);
        } else {
            editText.setText(ExchangeApplication.getApp().getPreferences().getUsername());
        }
    }

    @EditorAction(R.id.editText)
    void onEditorActions(TextView v, int actionId) {
        onNext(null);
    }

    public void onForgot(View v) {
        final Intent intent = new Intent(this, ForgotUsernameActivity_.class);
        startActivity(intent);
    }

    public void onNext(View v) {
        final String username = editText.getText().toString().trim();
        if (username.isEmpty()) {
            editText.setError(getString(R.string.error_username_empty));
        } else if (Utils.isUsernameInvalid(username)) {
            editText.setError(getString(R.string.error_username_short));
        } else {
            final Intent intent = new Intent(this, LoginPasswordActivity_.class);
            intent.putExtra("username", username);
            intent.putExtra("remember", checkBox.isChecked());
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
