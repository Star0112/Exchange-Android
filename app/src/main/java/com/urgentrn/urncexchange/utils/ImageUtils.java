package com.urgentrn.urncexchange.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;

import androidx.core.app.ActivityCompat;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageUtils {

    public static String encodeImage(Bitmap bmp) {
        if (bmp == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] b = stream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String encodeImage(Context context, Uri uri) {
        try {
            final InputStream imageStream = context.getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            return encodeImage(selectedImage);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static Bitmap decodeImage(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static byte[] compressImage(byte[] data, int maxSize, boolean front) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final Bitmap decodedImage = decodeImage(data);
        final Bitmap resizedImage = maxSize > 0 ? getResizedBitmap(decodedImage, maxSize) : decodedImage;
        if (resizedImage == decodedImage) { // no need to compress
            return data;
        }
        fixOrientation(resizedImage, front).compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] getCroppedImageData(byte[] data, boolean front, float width, float height, int left, int top, int right, int bottom) {
        try {
            Bitmap imageOriginal = decodeImage(data);
            float aspectRatio = width / height;
            imageOriginal = fixOrientation(imageOriginal, front);
            imageOriginal = cropToAspectRatio(imageOriginal, aspectRatio);

            Bitmap croppedBitmap = getCroppedBitmap(width, height, left, top, right, bottom, imageOriginal);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            croppedBitmap.recycle();
            return byteArray;
        } catch (Exception e) {
            return null;
        }
    }

    private static Bitmap getCroppedBitmap(float width, float height, int left, int top, int right, int bottom, Bitmap imageOriginal) {
        float factorX = (float) imageOriginal.getWidth() / width;
        float factorY = (float) imageOriginal.getHeight() / height;

        int cropStartX = Math.round(left * factorX);
        int cropStartY = Math.round(top * factorY);
        int cropWidthX = Math.round(right * factorX);
        int cropHeightY = Math.round(bottom * factorY);

        final Matrix matrix = new Matrix();
        matrix.postRotate(0);
        return Bitmap.createBitmap(imageOriginal, cropStartX, cropStartY, cropWidthX, cropHeightY, matrix, false);
    }

    private static Bitmap cropToAspectRatio(Bitmap image, float aspectRatio) {
        int width = image.getWidth();
        int height = image.getHeight();
        float imageAspectRatio = (float) width / (float) height;

        if (imageAspectRatio == aspectRatio) {
            return image;
        }

        int newWidth = (int) (height * aspectRatio);
        if (newWidth < width) {
            int margin = (width - newWidth) / 2;
            return Bitmap.createBitmap(image, margin, 0, newWidth, height);
        } else {
            int newHeight = (int) (width / aspectRatio);
            int margin = (height - newHeight) / 2;
            return Bitmap.createBitmap(image, 0, margin, width, newHeight);
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            if (width <= maxSize) {
                return image;
            }
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            if (height <= maxSize) {
                return image;
            }
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap fixOrientation(Bitmap bitmap, boolean front) {
        int rotation = bitmap.getWidth() > bitmap.getHeight() ? 90 * (front ? -1 : 1) : 0;
        if (rotation == 0) return bitmap;

        final Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        matrix.preScale(1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static String convertToBase64(String data) {
        return "data:image/jpeg;base64," + data.replace("\n", "");
    }

    public static void pickFromGallery(BaseActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, activity.getString(R.string.permission_read_storage_rationale), Constants.PermissionRequestCodes.STORAGE_READ_ACCESS_PERMISSION, false);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                    .setType("image/*")
                    .addCategory(Intent.CATEGORY_OPENABLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }

            activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.select_picture)), Constants.ActivityRequestCodes.SELECT_PICTURE);
        }
    }
}
