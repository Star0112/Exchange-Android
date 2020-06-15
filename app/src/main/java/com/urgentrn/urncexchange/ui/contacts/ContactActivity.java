package com.urgentrn.urncexchange.ui.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.contacts.Contact;
import com.urgentrn.urncexchange.models.request.ContactRequest;
import com.urgentrn.urncexchange.models.request.ImageUploadRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.ContactResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.view.EditableItemView;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.ImageUtils;
import com.yalantis.ucrop.UCrop;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EActivity(R.layout.activity_contact)
public class ContactActivity extends BaseActivity implements ApiCallback {

    @ViewById
    TextView txtTitle, txtAdd, txtEdit, txtDelete;

    @ViewById
    ImageView imgAvatar;

    @ViewById
    EditText editFirstName, editLastName;

    @ViewById
    EditableItemView itemUsername, itemPhoneNumber, itemEmail;

    private Contact contact;
    private String imageData;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);

        contact = (Contact) getIntent().getSerializableExtra("data");
        if (contact != null) {
            txtTitle.setText(R.string.edit_contact);
            if (contact.getImage() != null) {
                Glide.with(this).load(contact.getImage().getPath()).into(imgAvatar);
                txtAdd.setVisibility(View.GONE);
                txtEdit.setVisibility(View.VISIBLE);
            } else {
                txtAdd.setVisibility(View.VISIBLE);
                txtEdit.setVisibility(View.GONE);
            }
            if (contact.getName() != null) {
                final String[] name = contact.getName().split(" ");
                editFirstName.setText(name.length > 0 ? name[0] : null);
                editLastName.setText(name.length > 1 ? name[1] : null);
            }
            itemUsername.setValue(contact.getUsername());
            itemPhoneNumber.setValue(contact.getPhone());
            itemEmail.setValue(contact.getEmail());
            txtDelete.setVisibility(View.VISIBLE);
        } else {
            txtTitle.setText(R.string.new_contact);
            txtAdd.setVisibility(View.VISIBLE);
            txtEdit.setVisibility(View.GONE);
            txtDelete.setVisibility(View.GONE);
        }

        itemPhoneNumber.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        itemEmail.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
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

    private void onDone() {
        final String firstName = editFirstName.getText().toString().trim();
        if (firstName.isEmpty()) {
            editFirstName.setError(getString(R.string.error_field_empty));
            return;
        }

        final String lastName = editLastName.getText().toString().trim();
        if (lastName.isEmpty()) {
            editLastName.setError(getString(R.string.error_field_empty));
            return;
        }

        final String username = itemUsername.getValue();
        final String phoneNumber = itemPhoneNumber.getValue();
        final String email = itemEmail.getValue();
        if (username.isEmpty() && phoneNumber.isEmpty() && email.isEmpty()) {
            showAlert(R.string.error_empty_contact);
            return;
        }

        final ContactRequest request = new ContactRequest();
        if (imageData != null) {
            request.setImage(new ImageUploadRequest(ImageUploadRequest.Type.DEFAULT, ImageUtils.convertToBase64(imageData)));
        }
        request.setName(firstName + " " + lastName);
        if (!username.isEmpty()) {
            if (contact == null || contact.getUsername() == null || !contact.getUsername().equals(username)) {
                request.setUsername(username);
            }
        }
        if (!phoneNumber.isEmpty()) {
            if (contact == null || contact.getPhone() == null || !contact.getPhone().equals(phoneNumber)) {
                request.setPhone(phoneNumber);
            }
        }
        if (!email.isEmpty()) {
            if (contact == null || contact.getEmail() == null || !contact.getEmail().equals(email)) {
                request.setEmail(email);
            }
        }

        if (contact == null) {
            ApiClient.getInterface().createContact(request).enqueue(new AppCallback<>(this, this));
        } else {
            ApiClient.getInterface().updateContact(contact.getId(), request).enqueue(new AppCallback<>(this, this));
        }
    }

    public void onDelete(View v) {
        showAlert(R.string.delete_contact_confirm, R.string.delete_contact, R.string.button_cancel, ((dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                ApiClient.getInterface().deleteContact(contact.getId()).enqueue(new AppCallback<>(this, this));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.ActivityRequestCodes.SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    final UCrop uCrop = UCrop.of(selectedUri, Uri.fromFile(new File(getCacheDir(), System.currentTimeMillis() + ".png")));
                    uCrop.withAspectRatio(1, 1);
                    uCrop.start(this);

                } else {
                    Toast.makeText(this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    Glide.with(this).load(new File(resultUri.getPath())).into(imgAvatar);
                    imageData = ImageUtils.encodeImage(this, resultUri);
                    txtAdd.setVisibility(View.GONE);
                    txtEdit.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            if (data != null) {
                final Throwable cropError = UCrop.getError(data);
                if (cropError != null) {
                    Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof ContactResponse) {
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
