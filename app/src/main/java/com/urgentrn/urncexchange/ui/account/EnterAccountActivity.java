package com.urgentrn.urncexchange.ui.account;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.bank.FlowFormatField;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.VerifySuccessActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EActivity(R.layout.activity_enter_account)
public class EnterAccountActivity extends BaseActivity implements ApiCallback {

    @ViewById
    LinearLayout llEdit;

    private List<EditText> inputs = new ArrayList<>();
    private List<FlowFormatField> fields = new ArrayList<>();

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);

        if (AppData.getInstance().getFlowData() == null) return;
        for (FlowFormatField field : AppData.getInstance().getFlowData().getFormat().getFields()) {
            if (field.getType().equals("string")) {
                fields.add(field);

                final EditText input = new EditText(this);
                input.setHint(field.getName());
                input.setSingleLine();
                input.setTextSize(14F);
                input.setTypeface(ResourcesCompat.getFont(this, R.font.codec_pro_regular));
                inputs.add(input);
                llEdit.addView(input);
            }
        }
    }

    public void onContinue(View v) {
        final HashMap<String, String> params = new HashMap<>();
        for (EditText input : inputs) {
            if (input.getText().toString().isEmpty()) {
                input.setError(getString(R.string.error_field_empty));
                return;
            } else {
                final FlowFormatField field = fields.get(inputs.indexOf(input));
                params.put(field.getField(), input.getText().toString());
            }
        }
        params.put("accountType", getIntent().getStringExtra("accountType"));
        ApiClient.getInterface()
                .addBankAccount(AppData.getInstance().getFlowData().getId(), params)
                .enqueue(new AppCallback<>(this, this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        final Intent intent = new Intent(this, VerifySuccessActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type", Constants.VerifyType.BANK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
    }
}
