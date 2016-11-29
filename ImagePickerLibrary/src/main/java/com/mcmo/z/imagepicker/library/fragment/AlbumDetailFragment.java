package com.mcmo.z.imagepicker.library.fragment;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mcmo.z.imagepicker.library.R;
import com.mcmo.z.imagepicker.library.activity.ImagePickActivity;
import com.mcmo.z.imagepicker.library.info.BaseImagesInfo;
import com.mcmo.z.imagepicker.library.photogallery.PhotoGalleryActivity;
import com.mcmo.z.imagepicker.library.util.MediaProvider;
import com.mcmo.z.imagepicker.library.util.PixelUtil;
import com.mcmo.z.imagepicker.library.view.ImagePickAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ZhangWei on 2016/11/24.
 */

public class AlbumDetailFragment extends Fragment implements ImagePickAdapter.GetThumbnailCallBack{
    public static final String TAG = "AlbumDetailFragment";
    private GridView gv;
    private ImagePickActivity mActivity;
    private ImagePickAdapter mAdapter;
    private ArrayList<BaseImagesInfo> datas;
    private ContentResolver cr;
    private Cursor c;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ImagePickActivity)
            mActivity = (ImagePickActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(datas==null){
            mAdapter = new ImagePickAdapter(mActivity);
        }else{
            mAdapter = new ImagePickAdapter(mActivity,datas);
        }
        mAdapter.setGetThumbnailCallBack(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mcmo_activity_album_detail, container, false);
        gv = (GridView) view.findViewById(R.id.mcmo_gv_ip);
        gv.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);
        gv.setAdapter(mAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mActivity.startGallery(getPhotoArray(),position);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setImageInfo(ArrayList<BaseImagesInfo> datas) {
        this.datas = datas;
        if(mAdapter!=null){
            mAdapter.setData(this.datas);
        }
    }

    private boolean onLayoutFinish;
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (onLayoutFinish) return;
            ;
            int width = gv.getMeasuredWidth();
            int spacing = 0;
            if (Build.VERSION.SDK_INT >= 16) {
                spacing = gv.getHorizontalSpacing();
            } else {
                spacing = PixelUtil.dip2px(mActivity, 2);
            }
            int column = 4;
            if (Build.VERSION.SDK_INT >= 11) {
                column = gv.getNumColumns();
            }
            int itemWidth = (width - (spacing * (column + 1))) / column;
            Log.e("AlbumDetail", "onGlobalLayout: itemWidth=" + itemWidth);
            if (mAdapter != null) {
                mAdapter.setItemWidth(itemWidth);
            }
            onLayoutFinish = true;
            if (Build.VERSION.SDK_INT > 16)
                gv.getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutListener);
        }
    };
    private final String[] projection={MediaStore.Images.Thumbnails.DATA};
    @Override
    public String getThumbnail(int position) {
        if(cr==null) cr=mActivity.getContentResolver();
        c=cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,projection, MediaStore.Images.Thumbnails.IMAGE_ID+"=?",new String[]{datas.get(position).get_id()+""},null);
        String filePath=null;
        if(c.moveToFirst()){
            filePath=c.getString(0);
            datas.get(position).setThumbnail(filePath);
        }else{
            datas.get(position).setQueryOver(true);
        }
        c.close();
        return filePath;
    }
    private ArrayList<String> getPhotoArray(){
        ArrayList<String> urls=new ArrayList<>(datas.size());
        for (int i = 0; i < datas.size(); i++) {
            urls.add(datas.get(i).getData());
        }
        return urls;
    }
}
