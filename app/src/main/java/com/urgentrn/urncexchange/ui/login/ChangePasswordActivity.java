package com.urgentrn.urncexchange.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup_password)
public class ChangePasswordActivity extends BaseActivity {

    @ViewById
    TextView txtTitle, txtLabel;

    @ViewById
    EditText editText;

    @ViewById
    ImageView imgEye;

    private boolean isPasswordVisible;

    @AfterViews
    protected void init() {
        setToolBar(false);

        txtTitle.setText(R.string.forgot_password);
        txtLabel.setText(R.string.new_password_input_hint);
    }

    @EditorAction(R.id.editText)
    void onEditorActions(TextView v, int actionId) {
        onNext(null);
    }

    public void onShowPassword(View v) {
        isPasswordVisible = !isPasswordVisible;
        editText.setInputType(editText.getInputType() ^ 16);
        imgEye.setImageResource(isPasswordVisible ? R.mipmap.ic_eye : R.mipmap.ic_eye_off);
    }

    public void onNext(View v) {
        final String password = editText.getText().toString();
        if (password.isEmpty()) {
            editText.setError(getString(R.string.error_password_empty));
        } else if (!Utils.isPasswordValid(password)) {
            editText.setError(getString(R.string.error_password_short));
        } else {
            Intent intent = new Intent(this, ConfirmPasswordActivity_.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            intent.putExtra("code", getIntent().getStringExtra("code"));
            intent.putExtra("password", password);
            startActivity(intent);
        }
    }
}
