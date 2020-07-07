package com.urgentrn.urncexchange.ui.kyc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

@EActivity(R.layout.activity_verify_account)
public class VerifyAccountActivity extends BaseActivity {

    @ViewById
    EditText editFirstName, editLastName, editSSN;

    @ViewById
    TextView txtBirth, txtNinType, txtNin, txtExpiryDate;

    @ViewById
    View llExpiryDate;

    private final Calendar calendar = Calendar.getInstance();
    private final Calendar calendar2 = Calendar.getInstance();
//    private Constants.NinType ninType = Constants.NinType.NIN;

    final DatePickerDialog.OnDateSetListener date = ((view, year, month, dayOfMonth) -> {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        txtBirth.setError(null);
        txtBirth.setText(Utils.dateToString(calendar, null));
    });

    final DatePickerDialog.OnDateSetListener date2 = ((view, year, month, dayOfMonth) -> {
        calendar2.set(Calendar.YEAR, year);
        calendar2.set(Calendar.MONTH, month);
        calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        txtExpiryDate.setError(null);
        txtExpiryDate.setText(Utils.dateToString(calendar2, null));
    });

    @AfterViews
    protected void init() {
        setToolBar(true);

        updateNinType();
    }

    @Click(R.id.txtBirth)
    void onBirthClicked() {
        new DatePickerDialog(this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Click(R.id.txtExpiryDate)
    void onExpiryClicked() {
        new DatePickerDialog(this, date2, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Click(R.id.txtNinType)
    void onNinTypeClicked() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_type)
                .setItems(R.array.nin_types, (dialog, which) -> {
//                    ninType = Constants.NinType.values()[which];
                    updateNinType();
                })
                .show();
    }

    private void updateNinType() {
//        final String nin = getResources().getStringArray(R.array.nin_types)[ninType.ordinal()];
//        txtNinType.setText(nin);
//        txtNin.setText(nin);
//        if (ninType.equals(Constants.NinType.PASSPORT)) {
//            llExpiryDate.setVisibility(View.VISIBLE);
//        } else {
//            llExpiryDate.setVisibility(View.GONE);
//        }
    }

    public void onNext(View v) {
        final String firstName = editFirstName.getText().toString().trim();
        final String lastName = editLastName.getText().toString().trim();
        final String birthdate = txtBirth.getText().toString().trim();
        final String nin = editSSN.getText().toString().trim();
        final String expiry_date = txtExpiryDate.getText().toString().trim();
        if (firstName.isEmpty()) {
            editFirstName.setError(getString(R.string.error_field_empty));
        } else if (lastName.isEmpty()) {
            editLastName.setError(getString(R.string.error_field_empty));
        } else if (birthdate.isEmpty()) {
            txtBirth.setError(getString(R.string.error_field_empty));
        } else if (!isBirthdayValid(calendar)) {
            txtBirth.setError(getString(R.string.error_invalid_birth));
            Toast.makeText(this, R.string.error_invalid_birth, Toast.LENGTH_SHORT).show();
        } else if (nin.isEmpty()) {
            editSSN.setError(getString(R.string.error_field_empty));
//        } else if (false && nin.length() < Constants.MINIMUM_LENGTH_SSN) {
//            editSSN.setError(getString(R.string.error_ssn_short));
//        } else if (ninType.equals(Constants.NinType.PASSPORT) && (expiry_date.isEmpty() || calendar2.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis())) {
//            txtExpiryDate.setError(getString(expiry_date.isEmpty() ? R.string.error_field_empty : R.string.error_field_invalid));
        } else {
            Intent intent = new Intent(this, VerifyAccount2Activity_.class);
            intent.putExtra("info", new String[]{
                    firstName,
                    lastName,
                    birthdate,
                    nin,
//                    ninType.name().toLowerCase(),
//                    ninType.equals(Constants.NinType.PASSPORT) ? expiry_date : null,
            });
            startActivity(intent);
        }
    }

    private boolean isBirthdayValid(Calendar calendar) {
        final Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        newCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        newCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 18);
        return newCalendar.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis();
    }
}
