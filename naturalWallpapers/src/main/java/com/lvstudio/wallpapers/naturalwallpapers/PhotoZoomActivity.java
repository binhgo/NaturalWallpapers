package com.lvstudio.wallpapers.naturalwallpapers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.polites.android.GestureImageView;

public class PhotoZoomActivity extends Activity
{

    private GestureImageView imgV;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    ProgressDialog progressDialog;
    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photozoom);
        String uri = getIntent().getExtras().getString("URL");
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(false)
                .cacheOnDisk(true).showImageOnFail(R.drawable.load_fail).bitmapConfig(Bitmap.Config.ARGB_8888)
                .build();

        imgV = (GestureImageView) findViewById(R.id.imageV);

        imageLoader.loadImage(uri, options, new ImageLoadingListener()
        {

            @Override
            public void onLoadingStarted(String arg0, View arg1)
            {
                progressDialog = ProgressDialog.show(PhotoZoomActivity.this,
                        null, "Loading....");
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
            {
                progressDialog.dismiss();
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
            {
                progressDialog.dismiss();
                imgV.setImageBitmap(arg2);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1)
            {
                progressDialog.dismiss();
            }
        });

    }

}
