package com.urgentrn.urncexchange.ui.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.Symbol;
import com.urgentrn.urncexchange.models.contacts.BaseWalletAddress;
import com.urgentrn.urncexchange.models.contacts.WalletAddress;
import com.urgentrn.urncexchange.models.contacts.WalletData;
import com.urgentrn.urncexchange.models.request.ImageUploadRequest;
import com.urgentrn.urncexchange.models.request.WalletRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.CreateWalletResponse;
//import com.urgentrn.urncexchange.ui.QrCodeActivity_;
import com.urgentrn.urncexchange.ui.adapter.AddressAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.view.EditableItemView;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.ImageUtils;
import com.urgentrn.urncexchange.utils.WalletUtils;
import com.yalantis.ucrop.UCrop;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_address)
public class AddressActivity extends BaseActivity implements ApiCallback {

    @ViewById
    TextView txtTitle, txtAdd, txtEdit, txtDelete;

    @ViewById
    ImageView imgAvatar;

    @ViewById
    EditText editWalletName;

    @ViewById
    RecyclerView recyclerView;

    @ViewById
    EditableItemView itemAdd;

    private AddressAdapter adapter;

    private WalletData data;
    private List<WalletAddress> addresses = new ArrayList<>();
    private String imageData;
    private int selectedQRPosition;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressAdapter(position -> scanQRCode(position));
        recyclerView.setAdapter(adapter);

        data = (WalletData) getIntent().getSerializableExtra("data");
        if (data != null) {
            txtTitle.setText(R.string.edit_address);
            if (data.getDefaultImage() != null) {
                Glide.with(this).load(data.getDefaultImage().getPath()).into(imgAvatar);
                txtAdd.setVisibility(View.GONE);
                txtEdit.setVisibility(View.VISIBLE);
            } else {
                txtAdd.setVisibility(View.VISIBLE);
                txtEdit.setVisibility(View.GONE);
            }
            editWalletName.setText(data.getName());
            if (data.getAddresses() != null) addresses = new ArrayList<>(data.getAddresses());
            txtDelete.setVisibility(View.VISIBLE);
        } else {
            txtTitle.setText(R.string.new_address);
            txtAdd.setVisibility(View.VISIBLE);
            txtEdit.setVisibility(View.GONE);
            txtDelete.setVisibility(View.GONE);
        }
        adapter.setData(addresses);
        itemAdd.setOnClickListener(v -> onAdd());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.button_done).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default: // should be Done button click
                onDone();
                return true;
        }
    }

    public void onAvatarClicked(View v) {
        ImageUtils.pickFromGallery(this);
    }

    public void scanQRCode(int position) {
        selectedQRPosition = position;

//        final IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setCaptureActivity(QrCodeActivity_.class);
//        integrator.setOrientationLocked(false);
//        integrator.initiateScan();
    }

    public void onAdd() {
        final List<Integer> assetIds = new ArrayList<>();
        for (WalletAddress address : addresses) {
            assetIds.add(address.getAssetId());
        }

        final Intent intent = new Intent(this, SelectAssetsActivity_.class);
        intent.putExtra("data", (ArrayList)assetIds);
        startActivityForResult(intent, Constants.ActivityRequestCodes.SELECT_ASSETS);
    }

    private void onDone() {
        final String name = editWalletName.getText().toString().trim();
        if (name.isEmpty()) {
            editWalletName.setError(getString(R.string.error_field_empty));
            return;
        }

//        if (data == null && imageData == null) {
//            showAlert(R.string.error_empty_image);
//            return;
//        }

        final List<BaseWalletAddress> addresses = new ArrayList<>();
        for (WalletAddress walletAddress : this.addresses) {
            if (TextUtils.isEmpty(walletAddress.getAddress())) {
                showAlert(R.string.error_empty_wallet_address);
                return;
            }
            final Symbol symbol = WalletUtils.getSymbolData(walletAddress.getAssetId());
            if (symbol != null && symbol.getExtra() != null && symbol.getExtra().get("fieldName") != null && TextUtils.isEmpty(walletAddress.getExtra())) {
                showAlert(R.string.error_empty_memo);
                return;
            }
            if (walletAddress.getId() == 0) {
                addresses.add(walletAddress);
            }
        }
        if (data != null) {
            for (WalletAddress walletAddress : data.getAddresses()) {
                int assetId = 0;
                for (WalletAddress address : this.addresses) {
                    if (address.getAssetId() == walletAddress.getAssetId()) {
                        assetId = address.getAssetId();
                        break;
                    }
                }
                if (assetId > 0) { // update wallet address
                    ApiClient.getInterface().updateWalletAddress(data.getId(), walletAddress.getId(), walletAddress).enqueue(new AppCallback<>(this));
                } else { // delete wallet address
                    ApiClient.getInterface().deleteWalletAddress(data.getId(), walletAddress.getId()).enqueue(new AppCallback<>(this));
                }
            }
        }

        final WalletRequest request = new WalletRequest();
        request.setName(name);
        if (imageData != null) {
            request.setImage(new ImageUploadRequest(ImageUploadRequest.Type.DEFAULT, ImageUtils.convertToBase64(imageData)));
        }
        request.setAddresses(addresses);

        if (data == null) {
            ApiClient.getInterface().createWallet(request).enqueue(new AppCallback<>(this, this));
        } else {
            ApiClient.getInterface().updateWallet(data.getId(), request).enqueue(new AppCallback<>(this, this));
        }
    }

    public void onDelete(View v) {
        showAlert(R.string.delete_address_confirm, R.string.button_delete, R.string.button_cancel, ((dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                ApiClient.getInterface().deleteWallet(data.getId()).enqueue(new AppCallback<>(this, this));
            }
        }));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PermissionRequestCodes.STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageUtils.pickFromGallery(this);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == Constants.ActivityRequestCodes.SELECT_ASSETS && data != null) {
//                final List<Integer> assetIds = data.getIntegerArrayListExtra("data");
//                for (int assetId : assetIds) {
//                    final WalletAddress address = new WalletAddress();
//                    address.setAssetId(assetId);
//                    addresses.add(address);
//                }
//                adapter.notifyDataSetChanged();
//            } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
//                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//                if (result != null) {
//                    if (result.getContents() != null) { // null means cancelled
//                        adapter.getItem(selectedQRPosition).setAddress(result.getContents());
//                        adapter.notifyItemChanged(selectedQRPosition);
//                    }
//                }
//            } else if (requestCode == Constants.ActivityRequestCodes.SELECT_PICTURE) {
//                final Uri selectedUri = data.getData();
//                if (selectedUri != null) {
//                    final UCrop uCrop = UCrop.of(selectedUri, Uri.fromFile(new File(getCacheDir(), System.currentTimeMillis() + ".png")));
//                    uCrop.withAspectRatio(1, 1);
//                    uCrop.start(this);
//
//                } else {
//                    Toast.makeText(this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
//                }
//            } else if (requestCode == UCrop.REQUEST_CROP) {
//                final Uri resultUri = UCrop.getOutput(data);
//                if (resultUri != null) {
//                    Glide.with(this).load(new File(resultUri.getPath())).into(imgAvatar);
//                    imageData = ImageUtils.encodeImage(this, resultUri);
//                    txtAdd.setVisibility(View.GONE);
//                    txtEdit.setVisibility(View.VISIBLE);
//                } else {
//                    Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
//                }
//            }
//        } else if (resultCode == UCrop.RESULT_ERROR) {
//            if (data != null) {
//                final Throwable cropError = UCrop.getError(data);
//                if (cropError != null) {
//                    Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }
//            Toast.makeText(this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof CreateWalletResponse) { // create/update wallet success
            setResult(RESULT_OK);
            finish();
        } else { // delete wallet success
            setResult(RESULT_OK);
            finish();
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
