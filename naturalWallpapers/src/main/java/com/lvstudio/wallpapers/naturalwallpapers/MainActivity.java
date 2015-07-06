package com.lvstudio.wallpapers.naturalwallpapers;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lvwallpaper.model.GalleryM;
import com.lvwallpaper.model.PhotoGallery;
import com.lvwallpaper.model.UserGallery;
import com.lvwallpapers.adapter.ImageAdapter;
import com.lvwallpapers.utils.SwipeRefreshLayoutBottom;
import com.lvwallpapers.utils.WebServiceUtils;
import com.staticfunction.StaticFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity implements SwipeRefreshLayoutBottom.OnRefreshListener
{

    private GridView gridBackground;
    private ImageAdapter adapter;
    private GalleryM galleryM;

    ProgressBar progressBar;
    //private ProgressDialog progressDialog;
    private SwipeRefreshLayoutBottom mSwipeRefreshLayout;
    private boolean isFirst;
    private List<UserGallery> lstUserGallery;
    private int currentGallery = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String categoryName = "";
        categoryName = this.getIntent().getStringExtra("CategoryName").toString();

        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.txtTittle)).setTypeface(StaticFunction.getTypeface(this));
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.txtTittle)).setText(" " + categoryName + " ");

        initialView();

        initialData(categoryName);
        initialAdmob();
        showFullBannerEdit(false);
    }

    private void initialView()
    {

        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
        gridBackground = (GridView) findViewById(R.id.grvPhoto);
        mSwipeRefreshLayout = (SwipeRefreshLayoutBottom) findViewById(R.id.mainRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        gridBackground.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ArrayList<String> lstPhotoId = new ArrayList<String>();
                ArrayList<String> lstPhotoURL = new ArrayList<String>();
                ArrayList<String> lstPhotoURL_m = new ArrayList<String>();
                ArrayList<String> listPhotoName = new ArrayList<String>();
                ArrayList<String> listUrl_Best = new ArrayList<String>();

                if (galleryM.lstPhotoGallery != null && galleryM.lstPhotoGallery.size() > 0)
                {

                    for (PhotoGallery pg : galleryM.lstPhotoGallery)
                    {
                        lstPhotoId.add(pg.photoId);
                        lstPhotoURL.add(pg.url_l);
                        lstPhotoURL_m.add(pg.url_m);
                        listPhotoName.add(pg.photoName);
                        listUrl_Best.add(pg.getBestURL());
                    }

                    Intent intent = new Intent(MainActivity.this, ScreenSlideActivity.class);
                    intent.putStringArrayListExtra("lstPhoto", lstPhotoId);
                    intent.putStringArrayListExtra("lstPhotoURL", lstPhotoURL);
                    intent.putStringArrayListExtra("lstPhotoURL_Best", listUrl_Best);
                    intent.putStringArrayListExtra("lstPhotoURL_m", lstPhotoURL_m);
                    intent.putStringArrayListExtra("listPhotoName", listPhotoName);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });

    }

    private int getActionBarHeight()
    {
        int actionBarHeight = getSupportActionBar().getHeight();
        if (actionBarHeight != 0) return actionBarHeight;
        final TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;


    }

    private void initialData(String cateName)
    {
        galleryM = new GalleryM();

        if (isNetworkConnected())
        {
            LoadGalleryDobackground loadGalleryDobackground = new LoadGalleryDobackground();
            loadGalleryDobackground.categoryName = cateName;
            loadGalleryDobackground.execute();
        }
        else
        {
            Toast.makeText(this, "No internet connect.Please check again", Toast.LENGTH_LONG).show();
        }
    }

    public class LoadGalleryDobackground extends AsyncTask<String, String, String>
    {

        public String categoryName = "";


        @Override
        protected void onPreExecute()
        {

            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(String... arg0)
        {

            lstUserGallery = WebServiceUtils.getGalleries(categoryName);
            Collections.sort(lstUserGallery);

            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            progressBar.setVisibility(View.GONE);

            if (lstUserGallery != null && lstUserGallery.size() > 0)
            {
                currentGallery = 0;
                new LoadDataDobackground(lstUserGallery.get(0).getId(), false).execute();
            }

        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            progressBar.setVisibility(View.GONE);
        }
    }


    public class LoadDataDobackground extends AsyncTask<String, String, String>
    {


        String galleryId;
        GalleryM galleryTmp;

        boolean isFirst;

        public LoadDataDobackground(String galleryId, boolean isFirst)
        {
            this.galleryId = galleryId;
            this.isFirst = isFirst;
        }

        @Override
        protected void onPreExecute()
        {
            if (isFirst)
            {
                progressBar.setVisibility(View.VISIBLE);
            }
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... arg0)
        {

            if (isFirst)
            {

                GalleryM galleryFirst = WebServiceUtils.getGallery(galleryId);
                if (galleryFirst.pages == 1)
                {
                    galleryTmp = galleryFirst;
                }
                else
                {
                    galleryTmp = WebServiceUtils.getGallery(galleryId);
                }
            }
            else
            {
                galleryTmp = WebServiceUtils.getGallery(galleryId);
            }


            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (isFirst)
            {
                progressBar.setVisibility(View.GONE);
            }

            if (galleryM.lstPhotoGallery != null && galleryM.lstPhotoGallery.size() > 0)
            {

                galleryM.page = galleryTmp.page;
                galleryM.pages = galleryTmp.pages;
                galleryM.lstPhotoGallery.addAll(galleryTmp.lstPhotoGallery);

            }
            else
            {
                galleryM.page = galleryTmp.page;
                galleryM.pages = galleryTmp.pages;
                galleryM.lstPhotoGallery = galleryTmp.lstPhotoGallery;
            }

            if (adapter != null)
            {
                adapter.setNotify(galleryM.lstPhotoGallery);
            }
            else
            {
                adapter = new ImageAdapter(MainActivity.this, columnWidth, galleryM.lstPhotoGallery, getActionBarHeight());
                gridBackground.setAdapter(adapter);
            }

            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh()
    {

        if (currentGallery < lstUserGallery.size() - 1)
        {
            currentGallery = currentGallery + 1;
            if (isNetworkConnected())
            {
                new LoadDataDobackground(lstUserGallery.get(currentGallery).getId(), false).execute();
            }
            else
            {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
        else
        {
            mSwipeRefreshLayout.setRefreshing(false);
        }

       /* if (galleryM.page > 1) {

            if (isNetworkConnected()) {
                new LoadDataDobackground(galleryM.page - 1, false).execute();
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
            }

        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_love:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName().toString())));
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
