package com.lvstudio.wallpapers.naturalwallpapers;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orm.SugarApp;

public class BeautifulWallpaperAplication extends SugarApp
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        //initImageLoader(this);


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).memoryCache(new WeakMemoryCache()).tasksProcessingOrder(QueueProcessingType.LIFO)
                //.imageDecoder(new NutraBaseImageDecoder(true))
                // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    public static void initImageLoader(Context context)
    {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache()).tasksProcessingOrder(QueueProcessingType.LIFO)
                // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

}
