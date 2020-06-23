package com.urgentrn.urncexchange.ui.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.RecentWalletAddress;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.Wallet;
import com.urgentrn.urncexchange.models.contacts.BaseContact;
import com.urgentrn.urncexchange.models.contacts.Contact;
import com.urgentrn.urncexchange.models.contacts.WalletAddress;
import com.urgentrn.urncexchange.models.contacts.WalletData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetContactsResponse;
import com.urgentrn.urncexchange.models.response.GetLastUsedAddressesResponse;
import com.urgentrn.urncexchange.models.response.GetWalletsResponse;
//import com.urgentrn.urncexchange.ui.QrCodeActivity_;
import com.urgentrn.urncexchange.ui.adapter.ContactAdapter;
import com.urgentrn.urncexchange.ui.adapter.WalletAddressAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.contacts.AddressActivity_;
import com.urgentrn.urncexchange.ui.contacts.ContactActivity_;
import com.urgentrn.urncexchange.ui.dialogs.SendConfirmDialog;
import com.urgentrn.urncexchange.ui.dialogs.SendConfirmDialog_;
import com.urgentrn.urncexchange.ui.view.EditableItemView;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_send)
public class SendActivity extends BaseActivity implements ApiCallback {

    @ViewById
    View llExtra, llRecent, llContact;

    @ViewById
    TextView txtTitle, txtExtra, txtContacts, txtAddress, txtHeader;

    @ViewById
    EditText editAddress, editExtra, editSearch;

    @ViewById
    EditableItemView itemAdd;

    @ViewById
    RecyclerView recyclerView;

    @ViewById
    RecyclerView recyclerContact, recyclerAddress;

    private WalletAddressAdapter recentAdapter;
    private ContactAdapter contactAdapter, addressAdapter;
    private SendConfirmDialog confirmDialog = new SendConfirmDialog_();

    private Wallet wallet;
    private Constants.SendType type;
    private double amount, price;
    private List<RecentWalletAddress> recentAddresses = new ArrayList<>();

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        wallet = (Wallet)getIntent().getSerializableExtra("wallet");
        type = (Constants.SendType)getIntent().getSerializableExtra("type");
        amount = getIntent().getDoubleExtra("amount", 0);
        price = getIntent().getDoubleExtra("price", 0);

        if (wallet == null) {
            onBackPressed();
            return;
        }

        txtTitle.setText(String.format("%s %s %s", getString(R.string.title_send), Utils.formattedNumber(amount), wallet.getSymbol()));

        final String addressHint;
        switch (type) {
            case USERNAME:
                addressHint = getString(R.string.username);
                break;
            case EMAIL:
                addressHint = getString(R.string.email);
                editAddress.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case MOBILE:
                addressHint = getString(R.string.phone_number);
                editAddress.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case ADDRESS:
                addressHint = String.format("%s %s", wallet.getSymbol(), getString(R.string.address));
                break;
            default:
                addressHint = null;
                break;
        }
        editAddress.setHint(addressHint);

        llExtra.setVisibility(View.GONE);
        final Symbol symbol = WalletUtils.getSymbolData(wallet.getSymbol());
        if (symbol != null && symbol.getExtra() != null) {
            final String fieldName = symbol.getExtra().get("fieldName");
            if (fieldName != null && !fieldName.isEmpty()) {
                llExtra.setVisibility(View.VISIBLE);
                txtExtra.setText(String.format("%s:", fieldName));
                editExtra.setHint(getString(R.string.enter_if_required, fieldName));
                onExtraTextChange("");
            }
        }

        itemAdd.setOnClickListener(v -> onContactDetail(null));

        recentAdapter = new WalletAddressAdapter(position -> {
            final RecentWalletAddress walletAddress = recentAdapter.getItem(position);

            if (confirmDialog.getDialog() != null && confirmDialog.getDialog().isShowing()) return;
            final Bundle args = new Bundle();
            if (llExtra.getVisibility() == View.VISIBLE) {
                final String extraName = txtExtra.getText().toString().replace(":", "");
                args.putString("extra_name", extraName);
                args.putString("extra_value", walletAddress.getExtra());
            }
            args.putSerializable("wallet", wallet);
            args.putString("address", walletAddress.getAddress());
            args.putDouble("amount", amount);
            args.putDouble("price", price);
            confirmDialog.setArguments(args);
            confirmDialog.show(getSupportFragmentManager(), WalletUtils.TransactionType.SEND.name());
        }, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(recentAdapter);

        contactAdapter = new ContactAdapter(position -> {
            if (position >= 0) { // Send
                final Contact contact = (Contact)contactAdapter.getItem(position);
                String address;
                if (contact.getUsername() != null && !contact.getUsername().isEmpty()) {
                    address = contact.getUsername();
                } else if (contact.getPhone() != null && !contact.getPhone().isEmpty()) {
                    address = contact.getPhone();
                } else if (contact.getEmail() != null && !contact.getEmail().isEmpty()) {
                    address = contact.getEmail();
                } else {
                    showAlert(R.string.wallet_address_required);
                    return;
                }
                onNext(address, null, false, null);
            } else { // Edit Contact
                onContactDetail(contactAdapter.getItem(-position - 1));
            }
        }, false);
        recyclerContact.setLayoutManager(new LinearLayoutManager(this));
        recyclerContact.addItemDecoration(decoration);
        recyclerContact.setAdapter(contactAdapter);

        if (!wallet.getSymbolData().isCurrency()) {
            addressAdapter = new ContactAdapter(position -> {
                if (position >= 0) { // Send
                    final WalletData walletData = (WalletData)addressAdapter.getItem(position);
                    for (WalletAddress address : walletData.getAddresses()) {
                        if (address.getAssetId() == wallet.getSymbolData().getId()) {
                            onNext(address.getAddress(), address.getExtra(), false, Constants.SendType.ADDRESS);
                            return;
                        }
                    }
                    showAlert(wallet.getTitle() + " " + getString(R.string.wallet_address_required));
                } else { // Edit Wallet Address
                    onContactDetail(addressAdapter.getItem(-position - 1));
                }
            }, false);
            recyclerAddress.setLayoutManager(new LinearLayoutManager(this));
            recyclerAddress.addItemDecoration(decoration);
            recyclerAddress.setAdapter(addressAdapter);

            onAddressClicked(null);

            getWallets();
        } else {
            txtAddress.setVisibility(View.INVISIBLE);

            onContactsClicked(null);
        }

        onAddressTextChange("");
        editSearch.setText(null);

        getRecentAddresses();
        getContacts();
    }

    private void getContacts() {
        ApiClient.getInterface().getContacts().enqueue(new AppCallback<>(this));
    }

    private void getRecentAddresses() {
        ApiClient.getInterface().getLastUsedAddresses(wallet.getSymbol(), 5).enqueue(new AppCallback<>(this));
    }

    private void getWallets() {
        ApiClient.getInterface().getWallets().enqueue(new AppCallback<>(this));
    }

    public void onSend(View v) {
        onNext(editAddress.getText().toString(), editExtra.getText().toString(), true, type);
    }

    @TextChange(R.id.editAddress)
    void onAddressTextChange(CharSequence s) {
        if (s != null && s.length() > 0) {
            editAddress.setTextSize(type.equals(Constants.SendType.ADDRESS) ? 12 : 16);
        } else {
            editAddress.setTextSize(12);
        }
    }

    @TextChange(R.id.editExtra)
    void onExtraTextChange(CharSequence s) {
        if (s != null && s.length() > 0) {
            editExtra.setTextSize(16);
        } else {
            editExtra.setTextSize(12);
        }
    }

    @TextChange(R.id.editSearch)
    void onKeywordTextChange(CharSequence s) {
        updateRecentView();
        updateContactsView();
        updateWalletsView();
    }

    @EditorAction(R.id.editSearch)
    boolean onSearchDone(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            Utils.hideKeyboard(this, editSearch);
            return true;
        }
        return false;
    }

    public void onContactsClicked(View v) {
        txtContacts.setAlpha(1f);
        txtContacts.setTypeface(ResourcesCompat.getFont(this, R.font.codec_pro_bold));
        txtAddress.setAlpha(0.5f);
        txtAddress.setTypeface(ResourcesCompat.getFont(this, R.font.codec_pro_regular));
        itemAdd.setTitle(getString(R.string.add_new_contact));
        txtHeader.setText(R.string.contacts);
        recyclerContact.setVisibility(View.VISIBLE);
        recyclerAddress.setVisibility(View.GONE);
    }

    public void onAddressClicked(View v) {
        txtContacts.setAlpha(0.5f);
        txtContacts.setTypeface(ResourcesCompat.getFont(this, R.font.codec_pro_regular));
        txtAddress.setAlpha(1f);
        txtAddress.setTypeface(ResourcesCompat.getFont(this, R.font.codec_pro_bold));
        itemAdd.setTitle(getString(R.string.add_new_wallet_address));
        txtHeader.setText(R.string.wallet_addresses);
        recyclerContact.setVisibility(View.GONE);
        recyclerAddress.setVisibility(View.VISIBLE);
    }

    private void updateRecentView() {
        final List<RecentWalletAddress> filteredAddresses = new ArrayList<>();
        for (RecentWalletAddress address : recentAddresses) {
            if (address.getAddress().toLowerCase().contains(editSearch.getText().toString().toLowerCase())) {
                filteredAddresses.add(address);
            }
        }
        recentAdapter.setData(filteredAddresses);
    }

    private void updateContactsView() {
        final List<BaseContact> filteredContacts = new ArrayList<>();
        for (Contact contact : AppData.getInstance().getContacts()) {
            if (contact.getName().toLowerCase().contains(editSearch.getText().toString().toLowerCase())) {
                filteredContacts.add(contact);
            }
        }
        contactAdapter.setData(filteredContacts);
    }

    private void updateWalletsView() {
        if (addressAdapter == null) return;
        final List<BaseContact> filteredWallets = new ArrayList<>();
        for (WalletData data : AppData.getInstance().getWalletData()) {
            if (data.getName().toLowerCase().contains(editSearch.getText().toString().toLowerCase())) {
                filteredWallets.add(data);
            }
        }
        addressAdapter.setData(filteredWallets);
    }

    public void scanQRCode(View v) {
        final IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setCaptureActivity(QrCodeActivity_.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    private void onContactDetail(BaseContact baseContact) {
        final Intent intent = new Intent();
        intent.putExtra("data", baseContact);
        if (recyclerContact.getVisibility() == View.VISIBLE) {
            intent.setClass(this, ContactActivity_.class);
            startActivityForResult(intent, Constants.ActivityRequestCodes.CONTACT);
        } else {
            intent.setClass(this, AddressActivity_.class);
            startActivityForResult(intent, Constants.ActivityRequestCodes.WALLET_ADDRESS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.ActivityRequestCodes.CONTACT) {
                getContacts();
            } else if (requestCode == Constants.ActivityRequestCodes.WALLET_ADDRESS) {
                getWallets();
            } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() != null) { // null means cancelled
                        editAddress.setText(result.getContents());
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onNext(String address, String extra, boolean isManual, Constants.SendType type) {
        if (confirmDialog.getDialog() != null && confirmDialog.getDialog().isShowing()) return;
        final Bundle args = new Bundle();
        if (address.isEmpty()) {
            showAlert(R.string.wallet_address_required);
            return;
        } else if (isManual) {
            if (type != null && type.equals(Constants.SendType.EMAIL) && !Utils.isEmailValid(address)) {
                showAlert(R.string.error_email_invalid);
                return;
            }
        }

        if (llExtra.getVisibility() == View.VISIBLE && type != null) { // memo required
            final String extraName = txtExtra.getText().toString().replace(":", "");
            if (TextUtils.isEmpty(extra)) {
                final boolean requireMemo = false;
                if (requireMemo) {
                    showAlert(String.format("%s required", extraName));
                    return;
                }
            } else {
                args.putString("extra_name", extraName);
                args.putString("extra_value", extra);
            }
        }

        args.putSerializable("wallet", wallet);
        args.putSerializable("type", type);
        args.putString("address", address);
        args.putDouble("amount", amount);
        args.putDouble("price", price);
        confirmDialog.setArguments(args);
        confirmDialog.show(getSupportFragmentManager(), WalletUtils.TransactionType.SEND.name());
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetLastUsedAddressesResponse) {
            recentAddresses = ((GetLastUsedAddressesResponse)response).getData();
            if (recentAddresses.size() > 0) {
                llRecent.setVisibility(View.VISIBLE);
            } else {
                llRecent.setVisibility(View.GONE);
            }
            llContact.setVisibility(View.VISIBLE);
            updateRecentView();
        } else if (response instanceof GetContactsResponse) {
            AppData.getInstance().setContacts(((GetContactsResponse)response).getData().getData());
            updateContactsView();
        } else if (response instanceof GetWalletsResponse) {
            AppData.getInstance().setWalletData(((GetWalletsResponse)response).getData());
            updateWalletsView();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
