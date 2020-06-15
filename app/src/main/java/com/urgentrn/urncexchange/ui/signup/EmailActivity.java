package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup_email)
public class EmailActivity extends BaseActivity {

    @ViewById
    EditText editText;

    @AfterViews
    protected void init() {
        setToolBar(false);
        editText.requestFocus();
    }

    @EditorAction(R.id.editText)
    void onEditorActions(TextView v, int actionId) {
        onNext(null);
    }

    public void onNext(View v) {
        final String email = editText.getText().toString().trim();
        if (email.isEmpty()) {
            editText.setError(getString(R.string.error_email_empty));
        } else if (!Utils.isEmailValid(email)) {
            editText.setError(getString(R.string.error_email_invalid));
        } else {
            final Intent intent = new Intent(this, PasswordActivity_.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            intent.putExtra("email", email);
            intent.putExtra("referralCode", getIntent().getStringExtra("referralCode"));
            startActivity(intent);
        }
    }
}
