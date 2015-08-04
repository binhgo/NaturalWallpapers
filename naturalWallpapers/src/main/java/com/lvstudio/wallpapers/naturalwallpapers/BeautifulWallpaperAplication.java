package com.lvstudio.wallpapers.naturalwallpapers;

import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orm.SugarApp;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class BeautifulWallpaperAplication extends SugarApp
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        initImageLoader(this);

        Parse.initialize(this, "LLbkTtbCo2D9l7nj95zxFI1kXnGLtJMEUQbW0nAv", "UPNEvStvyIG60QpZLE93AQqmF1qanaxgI8woY0ex");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //initParse(this);


//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).memoryCache(new WeakMemoryCache()).tasksProcessingOrder(QueueProcessingType.LIFO)
//                //.imageDecoder(new NutraBaseImageDecoder(true))
//                // Remove for release app
//                .build();
//        // Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(config);

    }

    public static void initImageLoader(Context context)
    {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache()).tasksProcessingOrder(QueueProcessingType.LIFO)
                // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }


    /*public static void initParse(Context context)
    {
        Parse.initialize(this, "rk4vOVDanOfh3SHqe8pK9fTMEy9fhPYSPE0OIcce", "xXYFvY8Hvj99JojJgtpNE4B8lvsYe1a9rZHb04W9");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }*/

}
