package com.mcmo.z.imagepicker;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mcmo.z.imagepicker.library.activity.ImagePickActivity;
import com.mcmo.z.imagepicker.library.util.MediaProvider;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv= (ImageView) findViewById(R.id.iv);

    }
    public void onButtonClick(View v){
        Intent intent=ImagePickActivity.getIntent(this,true, Environment.getExternalStorageDirectory().getPath().toString()+ File.separator+"122.jpg");
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri uri=data.getData();
            File file=new File(uri.getPath());
            ImageLoader.getInstance().displayImage("file:///"+file.getAbsolutePath(),iv);
            Log.e("abc", "onActivityResult "+uri.getPath()+" "+file.exists());
        }
    }
}
