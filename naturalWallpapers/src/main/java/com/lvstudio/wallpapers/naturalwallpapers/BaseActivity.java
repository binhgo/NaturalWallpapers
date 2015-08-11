package com.lvstudio.wallpapers.naturalwallpapers;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.vlb.wallpaper.R;

public class BaseActivity extends ActionBarActivity
{

    public static final int PICK_FROM_GALLERY = 22;
    public static final int STICKER = 23;

    public int columnWidth = 90;
    public float padding = 0;
    public static final int NUM_OF_COLUMNS = 2;
    protected AdView adView;
    private InterstitialAd interstitial;
    protected AdRequest adRequest;
    protected int REQUEST_MULTIPLE_PHOTO = 1000;
    protected int REQUEST_FRAME_PHOTO = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);


        super.onCreate(savedInstanceState);

        Resources r = getResources();
        padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, r.getDisplayMetrics());
        columnWidth = (int) ((getScreenWidth() - ((NUM_OF_COLUMNS + 1) * padding)) / NUM_OF_COLUMNS);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#802c3136")));


        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.home1);

        actionBar.setDisplayShowTitleEnabled(false);


        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.view_action_tittle);

        //actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A2c3136")));

        //ActionBar bar = getSupportActionBar();
        //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c3136")));


    }

    @TargetApi(16)
    protected void setBackgroundV16Plus(View view, Bitmap bitmap)
    {
        view.setBackground(new BitmapDrawable(getResources(), bitmap));

    }

    @SuppressWarnings("deprecation")
    protected void setBackgroundV16Minus(View view, Bitmap bitmap)
    {
        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    public int getScreenWidth()
    {
        int columnWidth;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try
        {
            display.getSize(point);
        }
        catch (java.lang.NoSuchMethodError ignore)
        { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
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

    public void showInfo()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Thanks you for your using natural wallpapers app. We will always update and love to hear your feedback ^^. \nIf you see your photos and don't want it's show in app, please contact us we will make it's down. \nLvStudio \n\nThis product uses the Flickr API but is not endorsed or certified by Flickr.").setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();

    }

    protected Boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
        {
            return false;
        }
        else return true;
    }

    protected void pickImgfrmGal()
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            final Intent chooserIntent = Intent.createChooser(intent, "Select Source");
            startActivityForResult(chooserIntent, PICK_FROM_GALLERY);
        }
        catch (ActivityNotFoundException e)
        {
        }
    }

    public void initialAdmob()
    {

        adRequest = new AdRequest.Builder().build();
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-6956931160448072/9924001545");

        LinearLayout layout = (LinearLayout) findViewById(R.id.lnlAdmob);

        layout.addView(adView);
        adView.loadAd(adRequest);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-6956931160448072/2400734747");

        interstitial.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                // TODO Auto-generated method stub
                super.onAdLoaded();
                if (interstitial.isLoaded())
                {
                    interstitial.show();
                }
            }

            @Override
            public void onAdClosed()
            {
                // TODO Auto-generated method stub
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                // TODO Auto-generated method stub
                super.onAdFailedToLoad(errorCode);
            }

        });
    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();

    }

    protected void initialIntershial()
    {
        adRequest = new AdRequest.Builder().build();
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-2738380339931313/1997151585");

        interstitial.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                // TODO Auto-generated method stub
                super.onAdLoaded();
                if (interstitial.isLoaded())
                {
                    interstitial.show();
                }
            }

            @Override
            public void onAdClosed()
            {
                // TODO Auto-generated method stub
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                // TODO Auto-generated method stub
                super.onAdFailedToLoad(errorCode);
            }

        });
    }

    protected void showFullBannerEdit(boolean isTurn)
    {
        if (isTurn)
        {
            SharedPreferences prefs = getSharedPreferences("shareprefer", MODE_PRIVATE);
            int count = prefs.getInt("isTurneditCount", 0);
            SharedPreferences.Editor editor = prefs.edit();
            if (count == 0)
            {
                interstitial.loadAd(adRequest);
                editor.putBoolean("isTurnedit", false);
                editor.putInt("isTurneditCount", ++count);
                editor.commit();
            }
            else if (count == 6)
            {
                editor.putInt("isTurneditCount", 0);
                editor.commit();
            }
            else
            {
                editor.putInt("isTurneditCount", ++count);
                editor.putBoolean("isTurnedit", true);
                editor.commit();
            }
        }
        else
        {
            interstitial.loadAd(adRequest);
        }
    }

}
