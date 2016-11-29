package com.mcmo.z.imagepicker.library.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by weizhang210142 on 2016/5/5.
 */
public class NativeImageLoader {
    private static final String TAG = "NativeImageLoader";
    private LruCache<String ,Bitmap> mCache;
    private ExecutorService mExecutorService;
    private int mFailedImageRes;

    public NativeImageLoader() {
        Runtime runtime=Runtime.getRuntime();
        int cpuNum=runtime.availableProcessors();//获取当前可用cup数
        int maxThread=Math.max(1,cpuNum==1?5:cpuNum*3);
        mExecutorService= Executors.newFixedThreadPool(maxThread);
        long maxMemory= runtime.maxMemory();
        int maxCache= (int) (maxMemory/4);
        mCache=new LruCache<String,Bitmap>(maxCache){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB_MR1){
                    return value.getByteCount();
                }else{
                    return value.getRowBytes() * value.getHeight() / 1024;
                }
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if(evicted && oldValue!=null){
                    oldValue.recycle();
                    oldValue=null;
                }
            }
        };
    }
    public void release(){
        mExecutorService.shutdownNow();
        mCache.evictAll();
    }
    public void displayImage(String path, ImageView iv){
        mExecutorService.execute(new ImageLoadDisplayTask(path,iv,mCache,mFailedImageRes));
    }
}
