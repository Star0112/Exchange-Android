package com.urgentrn.urncexchange.ui.contacts;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.contacts.BaseContact;
import com.urgentrn.urncexchange.models.contacts.Contact;
import com.urgentrn.urncexchange.models.contacts.WalletData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetContactsResponse;
import com.urgentrn.urncexchange.models.response.GetWalletsResponse;
import com.urgentrn.urncexchange.ui.adapter.ContactAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_manage_contacts)
public class ManageContactsActivity extends BaseActivity implements ApiCallback {

    @ViewById
    EditText editSearch;

    @ViewById
    TextView txtContacts, txtAddress;

    @ViewById
    UltimateRecyclerView recyclerContact, recyclerAddress;

    private MenuItem menuItem;
    private ContactAdapter contactAdapter, addressAdapter;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);

        contactAdapter = new ContactAdapter(position -> onContactDetail(contactAdapter.getItem(position)));
        addressAdapter = new ContactAdapter(position -> onContactDetail(addressAdapter.getItem(position)));
        recyclerContact.setLayoutManager(new LinearLayoutManager(this));
        recyclerAddress.setLayoutManager(new LinearLayoutManager(this));
        recyclerContact.addItemDecoration(new StickyRecyclerHeadersDecoration(contactAdapter));
        recyclerAddress.addItemDecoration(new StickyRecyclerHeadersDecoration(addressAdapter));
        final DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerContact.addItemDecoration(decoration);
        recyclerAddress.addItemDecoration(decoration);
        recyclerContact.setAdapter(contactAdapter);
        recyclerAddress.setAdapter(addressAdapter);

        onContactsClicked(null);
        editSearch.setText(null);

        getContacts();
        getWallets();
    }

    private void getContacts() {
        ApiClient.getInterface().getContacts().enqueue(new AppCallback<>(this));
    }

    private void getWallets() {
        ApiClient.getInterface().getWallets().enqueue(new AppCallback<>(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuItem = menu.add(R.string.add_new_contact);
        menuItem.setIcon(R.mipmap.ic_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default: // should be Add button click
                onAdd();
                return true;
        }
    }

    @TextChange(R.id.editSearch)
    void onKeywordTextChange(CharSequence s) {
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
        if (menuItem != null) menuItem.setTitle(R.string.add_new_contact);
        txtContacts.setAlpha(1f);
        txtContacts.setTypeface(ResourcesCompat.getFont(this, R.font.codec_pro_bold));
        txtAddress.setAlpha(0.5f);
        txtAddress.setTypeface(ResourcesCompat.getFont(this, R.font.codec_pro_regular));
        editSearch.setHint(R.string.search_contacts);
        recyclerContact.setVisibility(View.VISIBLE);
        recyclerAddress.setVisibility(View.GONE);
    }

    public void onAddressClicked(View v) {
        if (menuItem != null) menuItem.setTitle(R.string.add_new_wallet_address);
        txtContacts.setAlpha(0.5f);
        txtContacts.setTypeface(ResourcesCompat.getFont(this, R.font.codec_pro_regular));
        txtAddress.setAlpha(1f);
        txtAddress.setTypeface(ResourcesCompat.getFont(this, R.font.codec_pro_bold));
        editSearch.setHint(R.string.search_wallet_address);
        recyclerContact.setVisibility(View.GONE);
        recyclerAddress.setVisibility(View.VISIBLE);
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
        final List<BaseContact> filteredWallets = new ArrayList<>();
        for (WalletData data : AppData.getInstance().getWalletData()) {
            if (data.getName().toLowerCase().contains(editSearch.getText().toString().toLowerCase())) {
                filteredWallets.add(data);
            }
        }
        addressAdapter.setData(filteredWallets);
    }

    private void onAdd() {
        onContactDetail(null);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.ActivityRequestCodes.CONTACT) {
                getContacts();
            } else if (requestCode == Constants.ActivityRequestCodes.WALLET_ADDRESS) {
                getWallets();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetContactsResponse) {
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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
    }
}
