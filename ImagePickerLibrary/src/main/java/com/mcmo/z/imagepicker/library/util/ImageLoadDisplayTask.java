package com.mcmo.z.imagepicker.library.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by weizhang210142 on 2016/5/5.
 */
public class ImageLoadDisplayTask implements Runnable {
    private static final String TAG = "ImageLoadDisplayTask";
    private Reference<ImageView> ivRef;
    private String path;
    private int viewWidth;
    private int viewHeight;
    private LruCache<String, Bitmap> mCache;
    private int failedImageRes= View.NO_ID;

    public ImageLoadDisplayTask(String path, ImageView imageView, LruCache<String, Bitmap> cache,int failedRes) {
        this.path = path;
        this.mCache = cache;
        this.failedImageRes=failedRes;
        this.viewWidth = imageView.getMeasuredWidth();
        this.viewHeight = imageView.getMeasuredHeight();
        ivRef = new WeakReference<ImageView>(imageView);
    }

    @Override
    public void run() {
        Bitmap bitmap = null;
        if (mCache != null) {
            bitmap = mCache.get(path);
        }
        if (bitmap == null || bitmap.isRecycled()) {
            File file=new File(path);
            if(file.exists()&&file.isFile()){
                bitmap = decodeThumbBitmapForFile(path, viewWidth, viewHeight);
                if (mCache != null) {
                    mCache.put(path, bitmap);
                }
            }
        }
        final ImageView iv = ivRef.get();
        if (iv != null) {
            if(bitmap!=null){
                iv.post(new ShowBitmapTask(iv, bitmap));
            }else if(failedImageRes!=View.NO_ID){
                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageResource(failedImageRes);
                    }
                });
            }
        }
    }

    /**
     * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
     *
     * @param path
     * @param viewWidth
     * @param viewHeight
     * @return
     */
    private Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap信息，不加载bitmap资源
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //设置缩放比例
        options.inSampleSize = computeScale(options.outWidth, options.outHeight, viewWidth, viewHeight);
        Log.e(TAG, "decodeThumbBitmapForFile inSampleSize=" + options.inSampleSize+" "+options.outWidth+" "+options.outHeight+" "+viewWidth+" "+viewHeight);
        //设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 计算图片缩放比例
     *
     * @param originalW 原始宽
     * @param originalH 原始高
     * @param newW      目标宽
     * @param newH      目标高
     * @return
     */
    private int computeScale(int originalW, int originalH, int newW, int newH) {
        int inSampleSize = 1;
        if (newW <= 0 || newH <= 0) {
            return inSampleSize;
        }
        //假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if (originalW > newW || originalH > newH) {
            int widthScale = originalW / newW;
            int heightScale = originalH / newH;

            //为了保证图片不缩放变形，我们取宽高比例最小的那个
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;
        }
        return inSampleSize;
    }

    private class ShowBitmapTask implements Runnable {
        private ImageView iv;
        private Bitmap bitmap;

        public ShowBitmapTask(ImageView iv, Bitmap bitmap) {
            this.iv = iv;
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            iv.setImageBitmap(bitmap);
        }
    }
}
