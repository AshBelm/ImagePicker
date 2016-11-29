package com.mcmo.z.imagepicker.library.photogallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mcmo.z.imagepicker.library.R;
import com.mcmo.z.imagepicker.library.activity.CropActivity;
import com.mcmo.z.imagepicker.library.util.ImageLoaderUtil;
import com.mcmo.z.library.photoview.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.security.Key;
import java.util.ArrayList;


/**
 * Created by weizhang210142 on 2016/3/23.
 */
public class PhotoGalleryActivity extends AppCompatActivity {
    private HackyViewPager vp;
    private ArrayList<String> data;
    private GalleryAdapter adapter;
    private DisplayImageOptions options = ImageLoaderUtil.getDisplayOptions_ImgOriginal();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private final int REQUESTCODE=13;
    public static String KEY_PHOTOS = "photos";
    public static String KEY_POSITION = "position";
    public static String KEY_NEED_CROP = "needCrop";
    public static String KEY_CROP_SAVE_PATH = "savepath";
    private boolean needCrop;
    private String cropSavePath;

    public static Intent getIntent(Context context, ArrayList<String> urls, int startPosition) {
        return getIntent(context, urls, startPosition, false, "");
    }

    public static Intent getIntent(Context context, ArrayList<String> urls, int startPosition, boolean crop, String savePath) {
        Intent intent = new Intent(context, PhotoGalleryActivity.class);
        intent.putStringArrayListExtra(KEY_PHOTOS, urls);
        intent.putExtra(KEY_POSITION, startPosition);
        intent.putExtra(KEY_NEED_CROP, crop);
        intent.putExtra(KEY_CROP_SAVE_PATH, savePath);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photogallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        Intent intent = getIntent();
        data = intent.getStringArrayListExtra(KEY_PHOTOS);
        int index = intent.getIntExtra(KEY_POSITION, 0);
        needCrop = intent.getBooleanExtra(KEY_NEED_CROP, false);
        cropSavePath = intent.getStringExtra(KEY_CROP_SAVE_PATH);
        if (data == null) {
            Toast.makeText(this, R.string.toast_no_photo, Toast.LENGTH_SHORT).show();
            finish();
        }
        vp = (HackyViewPager) findViewById(R.id.vp_photo);
        vp.setPageMargin(getResources().getDimensionPixelSize(R.dimen.gap));
        vp.setOffscreenPageLimit(2);
        adapter = new GalleryAdapter();
        vp.setAdapter(adapter);
        if (index >= 0 && index < adapter.getCount()) {
            vp.setCurrentItem(index);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_gallery, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(needCrop ? true : false);
        return super.onPrepareOptionsMenu(menu);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.menu_crop) {
                Intent intent = CropActivity.getIntent(PhotoGalleryActivity.this, data.get(vp.getCurrentItem()), cropSavePath);
                startActivityForResult(intent,REQUESTCODE);
            } else if (i == R.id.menu_accept) {
                String filePath=data.get(vp.getCurrentItem());
                resultFinish(filePath);
            }
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            resultFinish(data.getData());
        }else{
            Intent intent=new Intent();
            setResult(RESULT_CANCELED,intent);
            finish();
        }
    }

    private void resultFinish(String filePath){
        Intent intent=new Intent();
        intent.setData(Uri.fromFile(new File(filePath)));
        setResult(RESULT_OK,intent);
        finish();
    }
    private void resultFinish(Uri uri){
        Intent intent=new Intent();
        intent.setData(uri);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void finishAndReturn() {
        finish();
    }


    public void onTitleBackClick(View v) {
        finishAndReturn();
    }

    @Override
    public void onBackPressed() {
        finishAndReturn();
    }


    private class GalleryAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = LayoutInflater.from(PhotoGalleryActivity.this).inflate(R.layout.item_photo_gallery, null);
            PhotoView pv = (PhotoView) v.findViewById(R.id.pv_photo);
            ImageLoader.getInstance().displayImage("file:///" + data.get(position), pv);
            container.addView(v);
            return v;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
