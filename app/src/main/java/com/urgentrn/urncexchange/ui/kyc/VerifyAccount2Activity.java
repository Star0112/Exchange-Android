package com.urgentrn.urncexchange.ui.kyc;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.hbb20.CountryCodePicker;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EActivity(R.layout.activity_verify_account2)
public class VerifyAccount2Activity extends BaseActivity implements ApiCallback {

    @ViewById
    CountryCodePicker ccp;

    @ViewById
    TextView txtState, txtAddress, txtCity, txtZipCode;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 123;
    private String stateCode;

    @AfterViews
    protected void init() {
        setToolBar(true);

        setCountries();

        ccp.setOnCountryChangeListener(() -> setStates());
        ccp.setTypeFace(ResourcesCompat.getFont(this, R.font.codec_pro_regular));

        Places.initialize(getApplicationContext(), "AIzaSyD" + "_" + "b7QrtQTbexblsapCYPK0v5tE4jYhdjU");
    }

    public void onGoogleAddress(View v) {
        final List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG);
        final Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry(ccp.getSelectedCountryNameCode())
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                final Place place = Autocomplete.getPlaceFromIntent(data);
                String locality = "", subLocalityLevel1 = "", areaName = "", postalCode = "";
                for (AddressComponent component : place.getAddressComponents().asList()) {
                    if (component.getTypes().get(0).equals("locality")) {
                        locality = component.getName();
                    } else if (component.getTypes().get(0).equals("sublocality_level_1")) {
                        subLocalityLevel1 = component.getName();
                    } else if (component.getTypes().get(0).equals("administrative_area_level_1")) {
//                        final List<StateData> states = AppData.getInstance().getStates().get(ccp.getSelectedCountryNameCode());
//                        if (states != null) {
//                            for (StateData state : states) {
//                                if (state.getCode().equalsIgnoreCase(component.getShortName())) {
//                                    stateCode = component.getShortName();
//                                    txtState.setText(component.getName());
//                                    break;
//                                }
//                            }
//                        }
                    } else if (component.getTypes().get(0).equals("postal_code")) {
                        postalCode = component.getName();
                    }
                }
                txtCity.setText(!TextUtils.isEmpty(locality) ? locality : subLocalityLevel1);
                txtZipCode.setText(postalCode);
                txtAddress.setText(place.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                final Status status = Autocomplete.getStatusFromIntent(data);
                showAlert(status.getStatusMessage());
            }
        }
    }

    @Click(R.id.txtState)
    void onStateClicked() {
//        final List<StateData> data = AppData.getInstance().getStates().get(ccp.getSelectedCountryNameCode());
//        if (data == null) return;
//
//        final String[] states = new String[data.size()];
//        for (int i = 0; i < data.size(); i ++) {
//            states[i] = data.get(i).getName();
//        }
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select State")
//                .setItems(states, (dialog, which) -> {
//                    txtState.setError(null);
//                    txtState.setText(states[which]);
//                    stateCode = data.get(which).getCode();
//                })
//                .show();
    }

    private void setCountries() {
//        final List<String> codes = new ArrayList<>();
//        for (CountryData country : AppData.getInstance().getCountries()) {
//            if (!country.getCode().equalsIgnoreCase("us")) {
//                codes.add(country.getCode());
//            }
//        }
//        ccp.setCustomMasterCountries(TextUtils.join(",", codes));
//
//        setStates();
    }

    private void setStates() {
        final String countryNameCode = ccp.getSelectedCountryNameCode();
        if (countryNameCode.equals("CA")) {
            txtState.setHint(R.string.province);
            txtZipCode.setHint(R.string.postal_code);
//            editSSN.setHint(R.string.driver_license_password);
        } else {
            txtState.setHint(R.string.state);
            txtZipCode.setHint(R.string.zipcode);
//            editSSN.setHint(R.string.ssn_nin);
        }
//        if (AppData.getInstance().getStates().get(countryNameCode) == null) {
//            ApiClient.getInterface()
//                    .getStates(ccp.getSelectedCountryNameCode())
//                    .enqueue(new AppCallback<>(new ApiCallback() {
//                        @Override
//                        public void onResponse(BaseResponse response) {
//                            if (response instanceof GetStatesResponse) {
//                                final List<StateData> data = ((GetStatesResponse)response).getData();
//                                AppData.getInstance().getStates().put(countryNameCode, data);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(String message) {
//
//                        }
//                    }));
//        }

        txtAddress.setText(null);
        txtCity.setText(null);
        txtState.setText(null);
        txtZipCode.setText(null);
        stateCode = null;
    }

    public void onNext(View v) {
        final String country = ccp.getSelectedCountryNameCode();
        final String address = txtAddress.getText().toString().trim();
        final String city = txtCity.getText().toString().trim();
        final String state = txtState.getText().toString().trim();
        final String zipcode = txtZipCode.getText().toString().trim();
        if (address.isEmpty()) {
            txtAddress.setError(getString(R.string.error_field_empty));
        } else if (city.isEmpty()) {
            txtCity.setError(getString(R.string.error_field_empty));
        } else if (false && state.isEmpty()) {
            txtState.setError(getString(R.string.error_field_empty));
        } else if (zipcode.isEmpty()) {
            txtZipCode.setError(getString(R.string.error_field_empty));
        } else {
//            final UpdateUserRequest request = new UpdateUserRequest();
//            final String[] info = getIntent().getStringArrayExtra("info");
//
//            request.setFirstName(info[0]);
//            request.setLastName(info[1]);
//            request.setBirthdate(info[2]);
//            request.setNin(info[3]);
//            request.setNinType(info[4]);
//            if (info[5] != null) request.setExpiryDate(info[5]);
//            request.setCountry(country);
//            request.setAddress(address);
//            request.setCity(city);
//            request.setState(stateCode);
//            request.setZipcode(zipcode);
//
//            ApiClient.getInterface().updateUser(request).enqueue(new AppCallback<>(this, this));
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
//        if (response instanceof GetUserResponse) {
//            final Intent intent = new Intent(this, KycSuccessActivity_.class);
//            intent.putExtra("type", Constants.VerifyType.TIER1);
//            startActivity(intent);
//        }
    }

    @Override
    public void onFailure(String message) {

    }
}
