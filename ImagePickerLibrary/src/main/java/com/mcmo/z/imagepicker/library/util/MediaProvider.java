package com.mcmo.z.imagepicker.library.util;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.mcmo.z.imagepicker.library.info.BaseImagesInfo;
import com.mcmo.z.imagepicker.library.info.ImagesInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by weizhang210142 on 2016/5/4.
 */
public class MediaProvider {
    private static final String TAG = "MediaProvider";
    private Context mContext;

    public MediaProvider(Context context) {
        this.mContext=context;
    }
    private ContentResolver getContentResolver(){
        return mContext.getContentResolver();
    }

    /**
     *
     * @return map<文件夹名,图片信息>
     */
    public Map<String, ArrayList<BaseImagesInfo>> queryBaseImage(){
        ContentResolver contentResolver=getContentResolver();
        HashMap<String ,ArrayList<BaseImagesInfo>> map=new HashMap<>();
        String[] projection={MediaStore.Images.Media._ID,//
                MediaStore.Images.Media.BUCKET_ID,//
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,//
                MediaStore.Images.Media.DATA,//
                MediaStore.Images.Media.DATE_ADDED,//
                MediaStore.Images.Media.MIME_TYPE,//
                MediaStore.Images.Media.DISPLAY_NAME,//
        };
        Cursor c=contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,null,null, MediaStore.Images.Media.BUCKET_ID+","+ MediaStore.Images.Media.DATE_ADDED+" DESC");
        while (c.moveToNext()){
            BaseImagesInfo info=new BaseImagesInfo();
            info.set_id(c.getInt(0));
            info.setBucketId(c.getString(1));
            info.setBucketDisplayName(c.getString(2));
            info.setData(c.getString(3));
            info.setDateAdded(c.getInt(4));
            info.setMine(c.getString(5));
            info.setDisplayName(c.getString(6));
//            Log.e(TAG, "queryBaseImage "+info.toString());
            if(!info.getData().endsWith(".gif")){
                ArrayList<BaseImagesInfo> bucket=map.get(info.getBucketDisplayName());
                if(bucket==null){
                    bucket=new ArrayList<>();
                    map.put(info.getBucketDisplayName(),bucket);
                }
                bucket.add(info);
            }
        }
        c.close();
        c=null;
        return map;
    }
    //    public ArrayList<ImagesInfo> queryImage(){
//        ContentResolver contentResolver=getContentResolver();
//        ArrayList<ImagesInfo> result=new ArrayList<>();
//        String[] projection={MediaStore.Images.Media._ID,//
//                MediaStore.Images.Media.BUCKET_ID,//
//                MediaStore.Images.Media.DESCRIPTION,//
//                MediaStore.Images.Media.PICASA_ID,//
//                MediaStore.Images.Media.IS_PRIVATE,//
//                MediaStore.Images.Media.LATITUDE,//
//                MediaStore.Images.Media.LONGITUDE,//
//                MediaStore.Images.Media.DATE_TAKEN,//
//                MediaStore.Images.Media.ORIENTATION,//
//                MediaStore.Images.Media.MINI_THUMB_MAGIC,//
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,//
//                MediaStore.Images.Media.DATA,//
//                MediaStore.Images.Media.DATE_ADDED,//
//                MediaStore.Images.Media.TITLE,//
//                MediaStore.Images.Media.MIME_TYPE,//
//                MediaStore.Images.Media.DISPLAY_NAME,//
//                 };
//        Cursor c=contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,null,null,null);
//        while (c.moveToNext()){
//            ImagesInfo info=new ImagesInfo();
//            info.set_id(c.getInt(0));
//            info.setBucketId(c.getString(1));
//            info.setDescription(c.getString(2));
//            info.setPicasaId(c.getString(3));
//            info.setIsPrivate(c.getInt(4));
//            info.setLatitude(c.getDouble(5));
//            info.setLongitude(c.getDouble(6));
//            info.setDateTaken(c.getInt(7));
//            info.setOrientation(c.getInt(8));
//            info.setMiniThumbMagic(c.getInt(9));
//            info.setBucketDisplayName(c.getString(10));
//            info.setData(c.getString(11));
//            info.setDateAdded(c.getInt(12));
//            info.setTitle(c.getString(13));
//            info.setMine(c.getString(14));
//            info.setDisplayName(c.getString(15));
//            Log.e(TAG, "queryImage: "+info.toString());
//            result.add(info);
//        }
//        return result;
//    }
    public static void getThumbnail(Context context,ArrayList<BaseImagesInfo> images){
        ContentResolver contentResolver=context.getContentResolver();
        String[] projection={MediaStore.Images.Thumbnails.DATA};
        Cursor c=null;
        for (int i = 0; i < images.size(); i++) {
            c=contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,projection, MediaStore.Images.Thumbnails.IMAGE_ID+"=?",new String[]{images.get(i).get_id()+""},null);
            c.moveToFirst();
            String filePath=c.getString(0);
            Log.e(TAG, "getThumbnail "+filePath);
            images.get(i).setThumbnail(filePath);
            c.close();
        }

    }

}
