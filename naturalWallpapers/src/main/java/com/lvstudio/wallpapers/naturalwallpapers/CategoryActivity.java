package com.lvstudio.wallpapers.naturalwallpapers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.lvwallpaper.model.Category;
import com.lvwallpapers.GCM.PlayServicesHelper;
import com.lvwallpapers.adapter.CategoryAdapter;
import com.lvwallpapers.utils.WebServiceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 6/30/2015.
 */
public class CategoryActivity extends Activity
{

    private ListView lvCategory;
    private List<Category> listImage;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);


        lvCategory = (ListView) findViewById(R.id.lvCategory);
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_listview, null, false);
        lvCategory.addFooterView(footerView);


        listImage = new ArrayList<Category>();


        LoadGalleryDobackground loadGalleryDobackground = new LoadGalleryDobackground();
        loadGalleryDobackground.execute();

        PlayServicesHelper playServicesHelper = new PlayServicesHelper(CategoryActivity.this);
    }

    public int getScreenHeight()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        return height;
    }



   /* @Override
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
            case R.id.action_favorite:
                Intent intent = new Intent(CategoryActivity.this, FavoriteActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    public class LoadGalleryDobackground extends AsyncTask<String, String, String>
    {


        @Override
        protected void onPreExecute()
        {


        }

        @Override
        protected String doInBackground(String... arg0)
        {

            listImage = WebServiceUtils.getAllCategory();
            //Collections.sort(lstUserGallery);

            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {


            if (listImage != null && listImage.size() > 0)
            {

                adapter = new CategoryAdapter(CategoryActivity.this, listImage, getScreenHeight() / 3);
                lvCategory.setAdapter(adapter);

                //currentGallery = 0;
                //new LoadDataDobackground(lstUserGallery.get(0).getId(), false).execute();
            }

        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            //if (MainActivity.this.progressDialog != null)
            // MainActivity.this.progressDialog.dismiss();
        }
    }


}

