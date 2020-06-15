package com.urgentrn.urncexchange.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_qr_code)
public class QrCodeActivity extends BaseActivity implements DecoratedBarcodeView.TorchListener {

    @ViewById
    DecoratedBarcodeView barcodeScannerView;

    private CaptureManager capture;
    private boolean isTorchOn;

    @AfterViews
    protected void init() {
        setToolBar(true);

        barcodeScannerView.setTorchListener(this);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), null);
        capture.decode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public void onFlash(View v) {
        if (isTorchOn) {
            barcodeScannerView.setTorchOff();
        } else {
            barcodeScannerView.setTorchOn();
        }
    }

    public void onFlip(View v) {
        capture.onPause();

        final CameraSettings cameraSettings = barcodeScannerView.getBarcodeView().getCameraSettings();
        cameraSettings.setRequestedCameraId(cameraSettings.getRequestedCameraId() == 1 ? 0 : 1);
        barcodeScannerView.getBarcodeView().setCameraSettings(cameraSettings);

        capture.onResume();
    }

    @Override
    public void onTorchOn() {
        isTorchOn = true;
    }

    @Override
    public void onTorchOff() {
        isTorchOn = false;
    }
}
