package com.urgentrn.urncexchange.ui.account;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.bank.FieldOption;
import com.urgentrn.urncexchange.models.bank.FlowFormatField;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_connect_account)
public class ConnectAccountActivity extends BaseActivity {

    @ViewById
    TextView txtType;

    private List<FieldOption> options = new ArrayList<>();
    private int selectedIndex;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);

        if (AppData.getInstance().getFlowData() != null) {
            for (FlowFormatField field : AppData.getInstance().getFlowData().getFormat().getFields()) {
                if (field.getField().equals("accountType")) {
                    options = field.getOptions();
                }
            }
        }

        updateView();
    }

    private void updateView() {
        if (options.size() == 0) return;
        txtType.setText(options.get(selectedIndex).getName());
    }

    @Click(R.id.llSelectType)
    void onSelect() {
        final String[] options = new String[this.options.size()];
        for (int i = 0; i < this.options.size(); i ++) {
            options[i] = this.options.get(i).getName();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, (dialog, which) -> {
            selectedIndex = which;
            updateView();
        });
        builder.show();
    }

    public void onContinue(View v) {
        final Intent intent = new Intent(this, EnterAccountActivity_.class);
        intent.putExtra("accountType", options.get(selectedIndex).getValue());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
    }
}
