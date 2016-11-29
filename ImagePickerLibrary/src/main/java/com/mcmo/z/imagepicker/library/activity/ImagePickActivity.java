package com.mcmo.z.imagepicker.library.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcmo.z.imagepicker.library.R;
import com.mcmo.z.imagepicker.library.fragment.AlbumDetailFragment;
import com.mcmo.z.imagepicker.library.fragment.AlbumListFragment;
import com.mcmo.z.imagepicker.library.info.BaseImagesInfo;
import com.mcmo.z.imagepicker.library.photogallery.PhotoGalleryActivity;
import com.mcmo.z.imagepicker.library.util.ImageLoaderUtil;
import com.mcmo.z.imagepicker.library.util.MediaProvider;
import com.mcmo.z.imagepicker.library.util.NativeCameraLauncher;
import com.mcmo.z.imagepicker.library.util.PixelUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by weizhang210142 on 2016/5/5.
 */
public class ImagePickActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    public Map<String, ArrayList<BaseImagesInfo>> data;
    private AlbumDetailFragment mDetailFrag;
    private AlbumListFragment mListFrag;
    private String mCurrFragTag = "";
    private final int REQUESTCODE = 12001;
    private final int REQUESTCODE_PICTURE = 12002;
    private static final String KEY_NEED_CROP = "needcrop";
    private static final String KEY_SAVE_PATH = "savepath";
    private boolean needCrop;
    private String savePath;

    private void setAlbumName(String name) {
        mToolbar.setTitle(name);
    }

    public static Intent getIntent(Context context, boolean crop, String savePath) {
        Intent intent = new Intent(context, ImagePickActivity.class);
        intent.putExtra(KEY_NEED_CROP, crop);
        intent.putExtra(KEY_SAVE_PATH, savePath);
        return intent;
    }

    public static Intent getIntent(Context context) {
        return getIntent(context, false, "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcmo_activity_image_pick);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(mNavigationListener);
        needCrop = getIntent().getBooleanExtra(KEY_NEED_CROP, false);
        savePath = getIntent().getStringExtra(KEY_SAVE_PATH);
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderUtil.initImageLoaderConfing(this);
        }
        changeToDetail();
        new GetImageTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imagepick, menu);
        return true;
    }

    private String picturePath;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_camera) {
            picturePath = NativeCameraLauncher.startCameraForOriginalSize(this, REQUESTCODE_PICTURE);
        }
        return true;
    }

    public void changeToDetail(String key) {
        changeToDetail();
        setAlbumName(getAlbumName(key));
        mDetailFrag.setImageInfo(data.get(key));
    }

    private void changeToDetail() {
        if (mCurrFragTag.equals(AlbumDetailFragment.TAG)) {
            return;
        }
        mCurrFragTag = AlbumDetailFragment.TAG;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mDetailFrag == null) {
            mDetailFrag = new AlbumDetailFragment();
            ft.add(R.id.flay, mDetailFrag, mDetailFrag.TAG);
        } else {
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            ft.show(mDetailFrag);
        }
        if (mListFrag != null) {
            ft.hide(mListFrag);
        }
        ft.commitAllowingStateLoss();
    }

    private void changeToList() {
        if (mCurrFragTag.equals(AlbumListFragment.TAG)) {
            return;
        }
        setAlbumName("");
        mCurrFragTag = AlbumListFragment.TAG;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        if (mListFrag == null) {
            mListFrag = new AlbumListFragment();
            ft.add(R.id.flay, mListFrag, mListFrag.TAG);
        } else {
            ft.show(mListFrag);
        }
        if (mDetailFrag != null) {
            ft.hide(mDetailFrag);
        }
        ft.commitAllowingStateLoss();
    }


    private View.OnClickListener mNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            back();
        }
    };

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (mCurrFragTag.equals(AlbumListFragment.TAG)) {
            finish();
        } else {
            changeToList();
        }
    }

    public String getAlbumName(String key) {
        if (key.equals("Camera")) {
            return "相机";
        } else if (key.equals("Screenshots")) {
            return "截屏";
        } else {
            return key;
        }
    }

    private class GetImageTask extends AsyncTask<Object, Object, Map<String, ArrayList<BaseImagesInfo>>> {
        @Override
        protected Map<String, ArrayList<BaseImagesInfo>> doInBackground(Object... params) {
            MediaProvider mediaProvider = new MediaProvider(ImagePickActivity.this);
            return mediaProvider.queryBaseImage();
        }

        @Override
        protected void onPostExecute(Map<String, ArrayList<BaseImagesInfo>> result) {
            super.onPostExecute(data);
            data = result;
            if (data.size() == 0) {
                return;
            }
            if (data.containsKey("Camera")) {
                setAlbumName(getAlbumName("Camera"));
                mDetailFrag.setImageInfo(data.get("Camera"));
            } else {
                String key = data.keySet().iterator().next();
                setAlbumName(getAlbumName(key));
                mDetailFrag.setImageInfo(data.get(key));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                intent.setData(uri);
                setResult(RESULT_OK, intent);
                this.finish();
            }
        } else if (requestCode == REQUESTCODE_PICTURE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> photo=new ArrayList<>();
                photo.add(picturePath);
                startGallery(photo,0);
            }
        }
    }

    public void startGallery(ArrayList<String> photos, int position) {
        Intent intent = PhotoGalleryActivity.getIntent(this, photos, position, needCrop, savePath);
        startActivityForResult(intent, REQUESTCODE);
    }

    public void resultFinish(String filePath) {
        Intent intent = new Intent();
        intent.setData(Uri.fromFile(new File(filePath)));
        setResult(RESULT_OK, intent);
        finish();
    }
}
