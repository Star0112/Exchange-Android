package com.urgentrn.urncexchange.ui.fragments.profile;

import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.CountryData;
import com.urgentrn.urncexchange.models.StateData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetCountriesResponse;
import com.urgentrn.urncexchange.models.response.GetStatesResponse;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_profile_address)
public class AddressFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtAddress, txtCity, txtState, txtZipCode, txtCountry;

    @AfterViews
    protected void init() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setToolBar(true);

        if (getUser() == null) return;
        txtAddress.setText(getUser().getAddress());
        txtCity.setText(getUser().getCity());
        if (getUser().getCountry().equals("CA")) {
            txtState.setHint(R.string.province);
            txtZipCode.setHint(R.string.postal_code);
        }
        txtZipCode.setText(getUser().getZipCode());

        if (AppData.getInstance().getCountries().size() > 0) {
            setCountry();
        } else {
            ApiClient.getInterface().getCountries().enqueue(new AppCallback<>(this));
        }
        if (AppData.getInstance().getStates().get(getUser().getCountry()) != null) {
            setState();
        } else {
            ApiClient.getInterface().getStates(getUser().getCountry()).enqueue(new AppCallback<>(this));
        }
    }

    private void setCountry() {
        for (CountryData country : AppData.getInstance().getCountries()) {
            if (country.getCode().equals(getUser().getCountry())) {
                txtCountry.setText(country.getName());
                return;
            }
        }
    }

    private void setState() {
        final List<StateData> states = AppData.getInstance().getStates().get(getUser().getCountry());
        for (StateData state : states) {
            if (state.getCode().equals(getUser().getState()) || state.getName().equals(getUser().getState())) {
                txtState.setText(state.getName());
                return;
            }
        }
    }

    @Click(R.id.btnContact)
    void onContact() {
        Utils.sendMail(getActivity());
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetCountriesResponse) {
            final List<CountryData> data = ((GetCountriesResponse)response).getData();
            AppData.getInstance().setCountries(data);
            setCountry();
        } else if (response instanceof GetStatesResponse) {
            final List<StateData> data = ((GetStatesResponse)response).getData();
            AppData.getInstance().getStates().put(getUser().getCountry(), data);
            setState();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
