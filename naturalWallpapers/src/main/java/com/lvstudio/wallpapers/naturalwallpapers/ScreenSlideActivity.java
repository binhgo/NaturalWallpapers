package com.lvstudio.wallpapers.naturalwallpapers;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lvwallpaper.model.PhotoGallery;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.staticfunction.StaticFunction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScreenSlideActivity extends BaseActivity
{

    private static int NUM_PAGES = 0;
    private int selectItem;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Button btnSetWallpaper;

    FloatingActionButton btnSave, btnFavorite;
    private ArrayList<String> lstPhoto, lstPhotoURL_m, listPhotoName;

    private ArrayList<String>  listPhotoURL;
    private ArrayList<String>  listPhotoURL_Best;
    ProgressDialog progressDialog;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        Log.e("MEMORY","yyyÿyiiiiiiiiiiiiiiiiiijjjjjjjjjjjjjjj");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);


        initialAdmob();
        selectItem = getIntent().getExtras().getInt("position");
        lstPhoto = getIntent().getExtras().getStringArrayList("lstPhoto");
        listPhotoURL = getIntent().getExtras().getStringArrayList("lstPhotoURL");
        listPhotoURL_Best = getIntent().getExtras().getStringArrayList("lstPhotoURL_Best");

        lstPhotoURL_m = getIntent().getExtras().getStringArrayList("lstPhotoURL_m");
        listPhotoName = getIntent().getExtras().getStringArrayList("listPhotoName");
        NUM_PAGES = lstPhoto.size();


        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.txtTittle)).setTypeface(StaticFunction.getTypeface(this));
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.txtTittle)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.txtTittle)).setText(" " + listPhotoName.get(selectItem) + " ");

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(1);
        mPager.setCurrentItem(selectItem);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                selectItem = position;
                ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.txtTittle)).setText(" " + listPhotoName.get(selectItem) + " ");

            }
        });


        // Collections.sort(lstPhoto);
        btnSave = (FloatingActionButton) findViewById(R.id.btnDownload);
        btnSave.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int position = mPager.getCurrentItem();
                new SavePhotoOrginalDobackground().execute(listPhotoURL_Best.get(position));
            }
        });

        btnFavorite = (FloatingActionButton) findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                int position = mPager.getCurrentItem();
                setFavorite(position);
            }
        });

        btnSetWallpaper = (Button) findViewById(R.id.btnSetWallpaper);
        btnSetWallpaper.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                int position = mPager.getCurrentItem();
                setWallpaper(position);
            }
        });

        Toast.makeText(ScreenSlideActivity.this, "Click on Photo to Zoom and Swipe to next", Toast.LENGTH_LONG).show();

    }

    @Override
    public void setWallpaper(Bitmap bitmap) throws IOException {
        super.setWallpaper(bitmap);
    }

    private void setWallpaper(int pos)
    {

        imageLoader.loadImage(listPhotoURL_Best.get(pos), new ImageLoadingListener()
        {

            @Override
            public void onLoadingStarted(String arg0, View arg1)
            {
                progressDialog = ProgressDialog.show(ScreenSlideActivity.this, null, "Processing....");
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
            {
                progressDialog.dismiss();
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap)
            {
                // get the Image to as Bitmap
                progressDialog.dismiss();
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                // get the height and width of screen
                int height = metrics.heightPixels;
                int width = metrics.widthPixels;

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(ScreenSlideActivity.this);
                try
                {

                    wallpaperManager.setBitmap(bitmap);
                    wallpaperManager.suggestDesiredDimensions(width, height);
                    showFullBannerEdit(false);
                    Toast.makeText(ScreenSlideActivity.this, "Wallpaper Set", Toast.LENGTH_SHORT).show();

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1)
            {
                // TODO Auto-generated method stub
                progressDialog.dismiss();
            }
        });

    }

    private void setFavorite(int position)
    {

        try
        {
            List<PhotoGallery> lstPG = PhotoGallery.find(PhotoGallery.class, "photo_id =?", lstPhoto.get(position));

            if (lstPG != null && lstPG.size() > 0)
            {
                PhotoGallery photo = lstPG.get(0);
                photo.delete();
            }
            else
            {

                PhotoGallery photo = new PhotoGallery();
                photo.photoId = lstPhoto.get(position);
                photo.url_l = listPhotoURL.get(position);
                photo.url_m = lstPhotoURL_m.get(position);
                photo.save();

            }
        }
        catch (Exception e)
        {
            PhotoGallery photo = new PhotoGallery();
            photo.photoId = lstPhoto.get(position);
            photo.url_l = listPhotoURL.get(position);
            photo.url_m = lstPhotoURL_m.get(position);
            photo.save();
        }

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        Log.e("Select Item", selectItem + "   " + lstPhoto.get(selectItem));
        mPager.setCurrentItem(selectItem);

    }

    public class SavePhotoOrginalDobackground extends AsyncTask<String, String, String>
    {

        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            isSuccess = false;
            progressDialog = ProgressDialog.show(ScreenSlideActivity.this, null, "Saving...");
        }

        @Override
        protected String doInBackground(String... arg0)
        {

            String url = arg0[0];

            saveImage(url);

            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {

            progressDialog.dismiss();

            if (isSuccess)
            {
                showFullBannerEdit(false);
                Toast.makeText(ScreenSlideActivity.this, "Downloaded", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ScreenSlideActivity.this, "Can not Download. Please check your networks again", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }

        private void saveImage(String urlPath)
        {

            // create the new connection
            HttpURLConnection urlConnection;
            try
            {

                URL url = new URL(urlPath);
                urlConnection = (HttpURLConnection) url.openConnection();

                // set up some things on the connection
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/Beautiful Wallpaper");
                myDir.mkdirs();
                long time = System.currentTimeMillis();
                String fname = "Image-" + time + ".jpg";
                File file = new File(myDir, fname);

                if (file.exists()) file.delete();
                try
                {
                    FileOutputStream out = new FileOutputStream(file);
                    InputStream inputStream = urlConnection.getInputStream();

                    // create a buffer...
                    byte[] buffer = new byte[1024];
                    int bufferLength = 0; // used to store a temporary size of
                    // the
                    // buffer

                    // now, read through the input buffer and write the contents
                    // to
                    // the file
                    while ((bufferLength = inputStream.read(buffer)) > 0)
                    {
                        // add the data in the buffer to the file in the file
                        // output
                        // stream (the file on the sd card
                        out.write(buffer, 0, bufferLength);
                        // add up the size so we know how much is downloaded

                    }
                    // close the output stream when done
                    out.close();
                    isSuccess = true;
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + root + "/Beautiful Wallpaper/" + fname)));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter
    {

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount()
        {
            return lstPhoto.size();
        }

        private ScreenSlidePageFragment fragment;

        // Returns the fragment to display for that page
        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            fragment = ScreenSlidePageFragment.create(position, lstPhoto, listPhotoURL);
            return fragment;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_screenslide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection

        switch (item.getItemId())
        {
            case R.id.action_love:
                int position = mPager.getCurrentItem();
                setWallpaper(position);
                return true;
            case R.id.action_info:
                showInfo();
                return true;
            case R.id.action_rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName().toString())));
                return true;
            case R.id.action_otherapp:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=lvstudio&hl=en")));
                return true;
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
