package com.urgentrn.urncexchange.ui.account;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.urgentrn.urncexchange.BuildConfig;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.bank.FlowFormatField;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.VerifySuccessActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.CardUtils;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.StringEncryptUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

@EActivity(R.layout.activity_add_card)
public class AddCardActivity extends BaseActivity implements ApiCallback {

    @ViewById
    EditText editFullName, editCardNumber, editDate, editCvv, editZipCode;

    @ViewById
    CardInputWidget cardInputWidget;

    private Stripe stripe;

    private List<FlowFormatField> fields = new ArrayList<>();
    private String cardNumber, fullName, date, cvc, zipCode;

    private final boolean requireManualInput = false;

    @AfterViews
    protected void init() {
        setToolBar(true);

        for (FlowFormatField field : AppData.getInstance().getFlowData().getFormat().getFields()) {
            if (field.isRequired()) {
                fields.add(field);
            }
        }

        MaskedTextChangedListener.Companion.installOn(
                editCardNumber,
                "[0000] [0000] [0000] [0000]",
                new ArrayList<>(),
                AffinityCalculationStrategy.WHOLE_STRING,
                (maskFilled, extractedValue, formattedText) -> {
                    cardNumber = maskFilled ? extractedValue : null;
                }
        );
        MaskedTextChangedListener.Companion.installOn(
                editDate,
                "[00]{/}[00]",
                new ArrayList<>(),
                AffinityCalculationStrategy.WHOLE_STRING,
                (maskFilled, extractedValue, formattedText) -> {
                    date = maskFilled ? extractedValue : null;
                }
        );
    }

    @TextChange(R.id.editFullName)
    void onFullNameChange(CharSequence s) {
        fullName = null;
    }

    @TextChange(R.id.editCvv)
    void onCvvChange(CharSequence s) {
        cvc = null;
    }

    @TextChange(R.id.editZipCode)
    void onZipCodeChange(CharSequence s) {
        zipCode = null;
    }

    public void onScan(View v) {
        final Intent scanIntent = new Intent(this, CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, getResources().getColor(R.color.colorPrimary));
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, requireManualInput);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, requireManualInput);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, requireManualInput);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, requireManualInput);
        scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, requireManualInput);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, !requireManualInput);
        startActivityForResult(scanIntent, Constants.ActivityRequestCodes.SCAN_CODE);
    }

    public void onContinue(View v) {
        if (Constants.USE_STRIPE_TOKEN) {
            if (cardInputWidget.getCard() != null) {
                stripe = new Stripe(this, BuildConfig.PRODUCTION ? "pk_live_fRfc3lYXTZ52MWIeYxY9w7Mk00gWAo4faK" : "pk_test_7cujkScaevjdT80eI6M98zM100i7C9afcS");
                stripe.createToken(cardInputWidget.getCard(), new ApiResultCallback<Token>() {
                    @Override
                    public void onSuccess(Token token) {
                        onAddAccount(token, StringEncryptUtils.md5(cardInputWidget.getCard().getNumber()
                                + String.format(Locale.US, "%02d%02d", cardInputWidget.getCard().getExpMonth(), cardInputWidget.getCard().getExpYear() - 2000)
                                + cardInputWidget.getCard().getCvc()));
                    }

                    @Override
                    public void onError(@NotNull Exception e) {

                    }
                });
            }
        } else {
            onAddAccount(null, null);
        }
    }

    private void onAddAccount(Token token, String hashString) {
        final HashMap<String, String> params = new HashMap<>();

        for (FlowFormatField field : fields) {
            if (field.getField().equals("token")) {
                params.put(field.getField(), token.getId());
            } else if (field.getField().equals("identifier")) {
                params.put(field.getField(), hashString);
            } else if (field.getField().equals("fullName")) {
                if (fullName == null) fullName = editFullName.getText().toString().trim();
                if (fullName.isEmpty()) {
                    editFullName.setError(getString(R.string.error_field_empty));
                    editFullName.requestFocus();
                    return;
                }
                params.put(field.getField(), fullName);
            } else if (field.getField().equals("cardNumber")) {
                if (cardNumber == null) {
                    editCardNumber.setError(getString(R.string.error_field_empty));
                    editCardNumber.requestFocus();
                    return;
                }
                if (!CardUtils.isValid(cardNumber)) {
                    editCardNumber.setError(getString(R.string.error_field_invalid));
                    editCardNumber.requestFocus();
                    return;
                }
                params.put(field.getField(), cardNumber);
            } else if (field.getField().equals("expMonth") || field.getField().equals("expYear")) {
                if (date == null) {
                    editDate.setError(getString(R.string.error_field_invalid));
                    editDate.requestFocus();
                    return;
                }
                try {
                    final String expMonth = date.substring(0, 2);
                    final String expYear = date.substring(3, 5);
                    if (Integer.parseInt(expMonth) < 1 || Integer.parseInt(expMonth) > 12) {
                        editDate.setError(getString(R.string.error_field_invalid));
                        editDate.requestFocus();
                        return;
                    }
                    if (Integer.parseInt(expYear) < 0) {
                        editDate.setError(getString(R.string.error_field_invalid));
                        editDate.requestFocus();
                        return;
                    }
                    params.put(field.getField(), field.getField().equals("expMonth") ? expMonth : expYear);
                } catch (NumberFormatException e) {
                    editDate.setError(getString(R.string.error_field_invalid));
                    editDate.requestFocus();
                    return;
                }
            } else if (field.getField().equals("cvc")) {
                if (cvc == null) cvc = editCvv.getText().toString();
                if (cvc.isEmpty()) {
                    editCvv.setError(getString(R.string.error_field_empty));
                    editCvv.requestFocus();
                    return;
                }
                if (CardUtils.isAmex(cardNumber)) {
                    if (cvc.length() != 4) {
                        editCvv.setError(getString(R.string.error_field_invalid));
                        editCvv.requestFocus();
                        return;
                    }
                } else if (cvc.length() != 3) {
                    editCvv.setError(getString(R.string.error_field_invalid));
                    editCvv.requestFocus();
                    return;
                }
                params.put(field.getField(), cvc);
            } else if (field.getField().equals("zipcode")) {
                zipCode = editZipCode.getText().toString().trim();
                if (zipCode.isEmpty()) {
                    editZipCode.setError(getString(R.string.error_field_empty));
                    editZipCode.requestFocus();
                    return;
                }
                params.put(field.getField(), zipCode);
            } else {
                showAlert(field.getName() + " is missing");
                return;
            }
        }
        ApiClient.getInterface()
                .addBankAccount(AppData.getInstance().getFlowData().getId(), params)
                .enqueue(new AppCallback<>(this, this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ActivityRequestCodes.SCAN_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                final CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }
                if (scanResult.cvv != null) {
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }
                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }

                cardNumber = scanResult.cardNumber;
                editCardNumber.setText(scanResult.getFormattedCardNumber());
                if (requireManualInput) {
                    fullName = scanResult.cardholderName;
                    editFullName.setText(fullName);
                    date = String.format(Locale.US, "%02d/%02d", scanResult.expiryMonth, scanResult.expiryYear);
                    editDate.setText(date);
                    cvc = scanResult.cvv;
                    editCvv.setText(cvc);
                    zipCode = scanResult.postalCode;
                    editZipCode.setText(zipCode);
                }
            } else {
                resultDisplayStr = "Scan was canceled.";
            }
            if (BuildConfig.DEBUG) Log.d(getClass().getSimpleName(), resultDisplayStr);
        }
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
}
