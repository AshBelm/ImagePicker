package com.mcmo.z.imagepicker.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ZhangWei on 2016/11/29.
 */

public class NativeCameraLauncher {
    /**
     * OnActivityResult(){
     *     Bundle bundle=data.getExtras();
     *     Bitmap bitmap=(Bitmap)bundle.get("data");
     *
     * }
     * @param context
     * @param questCode
     */
    public static final void startCameraForThumbnail(Activity context, int questCode) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivityForResult(pictureIntent, questCode);
        }
    }

    public static final String startCameraStoreInDCIM(Activity context, int questCode) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File tempFile = null;
            try {
                tempFile = getDCIMFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("NCLauncher", "NativeCameraLauncher startCameraForOriginalSize error : temp file create failed");
            }
            if (tempFile != null) {
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                context.startActivityForResult(pictureIntent, questCode);
                return tempFile.getAbsolutePath();
            }
        }
        return null;
    }

    /**
     * 获取原尺寸图片
     * fileprovider uthorities ="com.mcmo.z.imagepicker.cameralauncher
     * @param context
     * @param questCode
     * @param directory 图片存放目录 可以为null
     * @return 调用成功放回图片地址，调用失败返回null
     */
    public static final String startCameraForOriginalSize(Activity context, int questCode, String directory) {
        if (Build.VERSION.SDK_INT >= 24) {
            return startCameraForOriginalN(context, questCode, directory);
        } else {
            return startCameraForOriginal(context, questCode, directory);
        }
    }

    /**
     * {@link NativeCameraLauncher#startCameraForOriginalSize(Activity, int, String)}
     * @param context
     * @param questCode
     * @return
     */
    public static final String startCameraForOriginalSize(Activity context, int questCode) {
        return startCameraForOriginalSize(context, questCode,null);
    }

    private static final String startCameraForOriginal(Activity context, int questCode, String directory) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File tempFile = null;
            try {
                tempFile = getTempFile(context, directory);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("NCLauncher", "NativeCameraLauncher startCameraForOriginalSize error : temp file create failed");
            }
            if (tempFile != null) {
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                context.startActivityForResult(pictureIntent, questCode);
                return tempFile.getAbsolutePath();
            }
        }
        return null;
    }

    ///android:authorities="com.mcmo.z.imagepicker.cameralauncher"
    private static final String startCameraForOriginalN(Activity context, int questCode, String directory) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File tempFile = null;
            try {
                tempFile = getTempFile(context, directory);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("NCLauncher", "NativeCameraLauncher startCameraForOriginalSize error : temp file create failed");
            }
            if (tempFile != null) {
                Uri photoUri = FileProvider.getUriForFile(context, "com.mcmo.z.imagepicker.cameralauncher", tempFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                context.startActivityForResult(pictureIntent, questCode);
                return tempFile.getAbsolutePath();
            }
        }
        return null;
    }

    private static final File getTempFile(Activity context, String directory) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir;
        if (StringUtil.empty(directory)) {
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            storageDir = new File(directory);
        }
        if (!storageDir.exists() || !storageDir.isDirectory()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    private static final File getDCIMFile() throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Log.i("NCLauncher", "getDCIMFile: " + path.getAbsolutePath());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = File.createTempFile(imageFileName, ".jpg", path);
        return image;
    }

    public static void sendDCIMRefreshBroadCast(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
}
