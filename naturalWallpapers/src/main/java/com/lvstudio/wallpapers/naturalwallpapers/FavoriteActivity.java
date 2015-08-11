package com.lvstudio.wallpapers.naturalwallpapers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.lvwallpaper.model.PhotoGallery;
import com.lvwallpapers.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;
import com.vlb.wallpaper.R;
public class FavoriteActivity extends BaseActivity
{

    private GridView grvFavorite;
    private List<PhotoGallery> lstPhotoGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initialData();
        initialView();
    }

    private void initialView()
    {
        grvFavorite = (GridView) findViewById(R.id.grvPhoto);
        grvFavorite.setAdapter(new ImageAdapter(this, columnWidth, lstPhotoGallery, 94));
        grvFavorite.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ArrayList<String> lstPhotoId = new ArrayList<String>();
                ArrayList<String> lstPhotoURL = new ArrayList<String>();
                ArrayList<String> lstPhotoURL_m = new ArrayList<String>();
                if (lstPhotoGallery != null && lstPhotoGallery.size() > 0)
                {

                    for (PhotoGallery pg : lstPhotoGallery)
                    {
                        lstPhotoId.add(pg.photoId);
                        lstPhotoURL.add(pg.url_l);
                        lstPhotoURL_m.add(pg.url_m);
                    }

                    Intent intent = new Intent(FavoriteActivity.this, ScreenSlideActivity.class);
                    intent.putStringArrayListExtra("lstPhoto", lstPhotoId);
                    intent.putStringArrayListExtra("lstPhotoURL", lstPhotoURL);
                    intent.putStringArrayListExtra("lstPhotoURL_m", lstPhotoURL_m);
                    intent.putExtra("position", position);
                    startActivity(intent);

                }

            }
        });

    }

    private void initialData()
    {
        try
        {
            lstPhotoGallery = PhotoGallery.listAll(PhotoGallery.class);
        }
        catch (Exception E)
        {
            lstPhotoGallery = new ArrayList<PhotoGallery>();
        }
    }

}
