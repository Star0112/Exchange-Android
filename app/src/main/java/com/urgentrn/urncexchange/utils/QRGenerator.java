package com.urgentrn.urncexchange.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRGenerator extends AsyncTask<String, Void, Bitmap> {
    private int mImageWidth, mImageHeight;
    private QRGeneratorListener mListener;

    public QRGenerator(int imageWidth, int imageHeight, QRGeneratorListener listener) {
        this.mImageWidth = imageWidth;
        this.mImageHeight = imageHeight;
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mListener != null) {
            mListener.qrGenerationStarted();
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return encodeToQrCode(params[0], mImageWidth, mImageHeight);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (mListener != null) {
            mListener.qrGenerated(bitmap);
        }
    }

    private Bitmap encodeToQrCode(String text, int width, int height){
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException ex) {
            ex.printStackTrace();
            return null;
        }

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    public interface QRGeneratorListener {
        void qrGenerationStarted();

        void qrGenerated(Bitmap bitmap);
    }
}
