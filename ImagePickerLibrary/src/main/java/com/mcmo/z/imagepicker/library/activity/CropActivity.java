package com.mcmo.z.imagepicker.library.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mcmo.z.imagepicker.library.R;
import com.mcmo.z.imagepicker.library.cropview.CropImageView;
import com.mcmo.z.imagepicker.library.cropview.callback.CropCallback;
import com.mcmo.z.imagepicker.library.cropview.callback.LoadCallback;
import com.mcmo.z.imagepicker.library.cropview.callback.SaveCallback;

import java.io.File;

/**
 * Created by ZhangWei on 2016/11/28.
 */

public class CropActivity extends AppCompatActivity implements View.OnClickListener{
    private CropImageView civ;
    private static final String KEY_IMAGE="imagepath";
    private static final String KEY_SAVE="savepath";
    private ProgressDialog mDialog;
    private String mFile;
    private String mSavePath;

    public static Intent getIntent(Context context,String filePath,String savePath){
        Intent intent= new Intent(context,CropActivity.class);
        intent.putExtra(KEY_IMAGE,filePath);
        intent.putExtra(KEY_SAVE,savePath);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(mNavigationListener);

        civ= (CropImageView) findViewById(R.id.cropImageView);
        mFile=getIntent().getStringExtra(KEY_IMAGE);
        mSavePath=getIntent().getStringExtra(KEY_SAVE);
        if(mSavePath==null||mSavePath.trim().length()==0){
            mSavePath=getCacheDir()+File.separator+"crop_"+mFile;
        }
        civ.startLoad(Uri.fromFile(new File(mFile)), new LoadCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
        findViewById(R.id.btn_free).setOnClickListener(this);
        findViewById(R.id.btn_original).setOnClickListener(this);
        mDialog=new ProgressDialog(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_gallery, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_accept){
            mDialog.show();
            civ.startCrop(Uri.fromFile(new File(mSavePath)), new CropCallback() {
                @Override
                public void onSuccess(Bitmap cropped) {

                }

                @Override
                public void onError() {
                    mDialog.dismiss();
                    Toast.makeText(CropActivity.this, "crop failed", Toast.LENGTH_SHORT).show();
                }
            }, new SaveCallback() {
                @Override
                public void onSuccess(Uri outputUri) {
                    mDialog.dismiss();
                    Intent intent=new Intent();
                    intent.setData(outputUri);
                    setResult(RESULT_OK,intent);
                    finish();
                }

                @Override
                public void onError() {
                    mDialog.dismiss();
                    Toast.makeText(CropActivity.this, "save failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_free) {
            civ.setCropMode(CropImageView.CropMode.FREE);
        } else if (i == R.id.btn_original) {
            civ.setCropMode(CropImageView.CropMode.FIT_IMAGE);
        }
    }
    private void back(){
        Intent intent=new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        back();
    }
    private View.OnClickListener mNavigationListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            back();
        }
    };
}
