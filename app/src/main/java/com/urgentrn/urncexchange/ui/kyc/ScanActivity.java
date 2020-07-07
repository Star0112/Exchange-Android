package com.urgentrn.urncexchange.ui.kyc;

import android.content.Intent;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_scan)
public class ScanActivity extends BaseActivity implements ApiCallback {

    @ViewById
    TextView txtTitle, txtSubtitle, txtDescription, txtHint1, txtHint2;

    @ViewById
    CameraView cameraView;

    @ViewById
    View llPreview1, llPreview2, llPreview3, llBottomView1, llBottomView2, imgSwitch;

    @ViewById
    ImageView imgFlash;

    private int step;
    private int titleId;
    private int type = 1; // set Driving License as default
    private int mCurrentFlash;
    private byte[] mData;
    private int previewLeft;
    private int previewTop;
    private int previewRight;
    private int previewBottom;

    @AfterViews
    protected void init() {
        setToolBar(true);
        cameraView.setLifecycleOwner(this);
        step = getIntent().getIntExtra("step", 1);
        type = getIntent().getIntExtra("type", type);
        switch (step) {
            case 1:
                titleId = R.string.scan_driver_license_front;
                txtSubtitle.setText(R.string.select_document);
                txtDescription.setText(getResources().getStringArray(R.array.document_types)[type]);
                llPreview1.setVisibility(View.VISIBLE);
                llPreview2.setVisibility(View.GONE);
                llPreview3.setVisibility(View.GONE);
                txtHint1.setText(R.string.scan_hint1);
                txtHint2.setText(R.string.scan_hint2);
                imgSwitch.setVisibility(View.VISIBLE);
                break;
            case 2:
                titleId = R.string.scan_driver_license_back;
                txtSubtitle.setText(R.string.document);
                txtDescription.setText(getResources().getStringArray(R.array.document_types)[type]);
                llPreview1.setVisibility(View.VISIBLE);
                llPreview2.setVisibility(View.GONE);
                llPreview3.setVisibility(View.GONE);
                txtHint1.setText(R.string.scan_hint1);
                txtHint2.setText(R.string.scan_hint2);
                break;
            case 3:
                titleId = R.string.take_selfie;
                txtSubtitle.setText(R.string.selfie_photo);
                txtDescription.setVisibility(View.GONE);
                llPreview1.setVisibility(View.GONE);
                llPreview2.setVisibility(View.GONE);
                llPreview3.setVisibility(View.VISIBLE);
                txtHint1.setText(R.string.scan_hint5);
                txtHint2.setText(R.string.scan_hint6);
                cameraView.setFacing(Facing.FRONT);
                break;
            case 4:
                titleId = R.string.proof_of_address;
                txtSubtitle.setText(R.string.proof_of_address);
                txtDescription.setVisibility(View.GONE);
                llPreview1.setVisibility(View.GONE);
                llPreview2.setVisibility(View.VISIBLE);
                llPreview3.setVisibility(View.GONE);
                txtHint1.setText(R.string.scan_hint8);
                txtHint2.setText(R.string.scan_hint9);
                break;
            default:
                return;
        }
        txtTitle.setText(titleId);
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                cameraView.close();
                mData = result.getData();
                updateView(true);
                hideProgressBar();
            }

            @Override
            public void onVideoTaken(VideoResult result) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
    }

    private void updateView(boolean isTaken) {
        txtTitle.setText(isTaken ? R.string.check_readability : titleId);
        txtSubtitle.setVisibility(step != 4 || isTaken ? View.VISIBLE : View.GONE);
        llBottomView1.setVisibility(isTaken ? View.GONE : View.VISIBLE);
        llBottomView2.setVisibility(isTaken ? View.VISIBLE : View.GONE);
    }

    public void onSwitch(View v) {
        final String[] items = getResources().getStringArray(R.array.document_types);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_document)
                .setItems(items, (dialog, which) -> {
                    type = which;
                    txtDescription.setText(items[type]);
                })
                .show();
    }

    public void onCapture(View v) {
        showProgressBar();
        cameraView.takePictureSnapshot();
    }

    public void onFlash(View v) {
        cameraView.setFlash(cameraView.getFlash() == Flash.TORCH ? Flash.OFF : Flash.TORCH);
        imgFlash.setImageResource(cameraView.getFlash() == Flash.TORCH ? R.mipmap.ic_flash : R.mipmap.ic_flash_off);
    }

    public void onFlip(View v) {
        cameraView.toggleFacing();
    }

    public void onRetake(View v) {
        cameraView.open();
        mData = null;
        updateView(false);
    }

    public void onSubmit(View v) {
        if (mData != null) {
//            ImageUploadRequest.Type type;
//            initPreviewData(step);
//            switch (step) {
//                case 1:
//                    type = this.type == 0 ? ImageUploadRequest.Type.PASSPORT_FRONT
//                            : this.type == 1 ? ImageUploadRequest.Type.DRIVING_LICENSE_FRONT
//                            : this.type == 2 ? ImageUploadRequest.Type.NIN_DOCUMENT_FRONT
//                            : this.type == 3 ? ImageUploadRequest.Type.VOTER_ID_FRONT
//                            : ImageUploadRequest.Type.WORK_PERMIT_FRONT;
//                    break;
//                case 2:
//                    type = this.type == 0 ? ImageUploadRequest.Type.PASSPORT_BACK
//                            : this.type == 1 ? ImageUploadRequest.Type.DRIVING_LICENSE_BACK
//                            : this.type == 2 ? ImageUploadRequest.Type.NIN_DOCUMENT_BACK
//                            : this.type == 3 ? ImageUploadRequest.Type.VOTER_ID_BACK
//                            : ImageUploadRequest.Type.WORK_PERMIT_BACK;
//                    break;
//                case 3:
//                    type = ImageUploadRequest.Type.NIN_PHOTO;
//                    break;
//                case 4:
//                    type = ImageUploadRequest.Type.EXTRA_DOC;
//                    break;
//                default:
//                    return;
//            }
//            String data;
//            mData = ImageUtils.getCroppedImageData(mData, cameraView.getFacing() == Facing.FRONT, cameraView.getWidth(), cameraView.getHeight(), previewLeft, previewTop, previewRight, previewBottom);
//            try {
//                data = Base64.encodeToString(mData, Base64.DEFAULT);
//            } catch (OutOfMemoryError error) {
//                showAlert(error.getLocalizedMessage());
//                return;
//            }
//
//            ApiClient.getInterface()
//                    .uploadDocument(new ImageUploadRequest(type, ImageUtils.convertToBase64(data)))
//                    .enqueue(new AppCallback<>(this, this));
        }
    }

    private void initPreviewData(int step) {
        int horizontalMargin = (int) getResources().getDimension(R.dimen.default_margin_2x);
        if (step == 1 || step == 2) {
            previewLeft = llPreview1.getLeft() + horizontalMargin;
            previewTop = llPreview1.getTop();
            previewRight = llPreview1.getWidth() - horizontalMargin;
            previewBottom = llPreview1.getHeight();
        } else if (step == 3) {
            previewLeft = llPreview3.getLeft() + horizontalMargin;
            previewTop = llPreview3.getTop();
            previewRight = llPreview3.getWidth() - horizontalMargin;
            previewBottom = llPreview3.getHeight();
        } else if (step == 4) {
            previewLeft = llPreview2.getLeft() + horizontalMargin;
            previewTop = llPreview2.getTop();
            previewRight = llPreview2.getWidth() - horizontalMargin;
            previewBottom = llPreview2.getHeight();
        }
    }

    @Override
    public void onResponse(BaseResponse response) {
        Intent intent;
        if (step == 1) {
            if (type == 2) { // only front card for National Identity Card in some countries
//                for (CountryData country : AppData.getInstance().getCountries()) {
//                    if (country.getCode().equals(ExchangeApplication.getApp().getUser().getCountry()) && country.getNinCount() == 1) {
//                        intent = new Intent(this, ScanActivity_.class);
//                        intent.putExtra("type", type);
//                        intent.putExtra("step", 2);
//                        return;
//                    }
//                }
            }
            intent = new Intent(this, SelfieActivity_.class);
            intent.putExtra("step", 3);
            startActivity(intent);
//        } else if (step == 4) {
//            intent = new Intent(this, VerifySuccessActivity_.class);
//            intent.putExtra("type", Constants.VerifyType.TIER2);
        } else {
            intent = new Intent(this, SelfieActivity_.class);
            intent.putExtra("step", step + 1);
        }
        startActivity(intent);
    }

    @Override
    public void onFailure(String message) {

    }
}
