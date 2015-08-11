package com.staticfunction;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.DisplayMetrics;
import android.view.View;

import com.lvstudio.wallpapers.naturalwallpapers.CategoryActivity;
import com.vlb.wallpaper.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by user on 6/24/2015.
 */
public class StaticFunction
{

    public static final String PROJECT_NUMBER = "68082673664";

    public static float getDensity(Activity activity)
    {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    public static Typeface getTypeface(Activity activity)
    {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Qualit_Deluxe_Platinium.ttf");
        return typeface;
    }

    public static void sendNotificationBigNoImage(final Context ctx, final int notifyId, final String title, final String subTitle, final String bigTitleforNewAPI)
    {

        Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.galaxy);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx).setAutoCancel(true).setContentTitle(title).setSmallIcon(R.drawable.ic_launcher).setContentText(subTitle);

        NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
        bigPicStyle.bigPicture(bitmap);
        bigPicStyle.setBigContentTitle(bigTitleforNewAPI);
        bigPicStyle.setSummaryText(subTitle);
        mBuilder.setStyle(bigPicStyle);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ctx, CategoryActivity.class);

        // The stack builder object will contain an artificial back stack
        // for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out
        // of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        // Adds the back stack for the Intent (but not the Intent itself)
        //stackBuilder.addParentStack(testActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    public static void sendNotificationBigWithImage(final Context ctx, final int notifyId, final String title, final String subTitle, final String bigTitleforNewAPI, Bitmap bitmap)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx).setAutoCancel(true).setContentTitle(title).setSmallIcon(R.drawable.ic_launcher).setContentText(subTitle);

        NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
        bigPicStyle.bigPicture(bitmap);
        bigPicStyle.setBigContentTitle(bigTitleforNewAPI);
        bigPicStyle.setSummaryText(subTitle);
        mBuilder.setStyle(bigPicStyle);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ctx, CategoryActivity.class);

        // The stack builder object will contain an artificial back stack
        // for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out
        // of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        // Adds the back stack for the Intent (but not the Intent itself)
        //stackBuilder.addParentStack(testActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    public static void sendNotificationBig(final Context ctx, final int notifyId, final String title, final String subTitle, final String bigTitleforNewAPI, String imageURL)
    {
        if (imageURL.isEmpty())
        {
            sendNotificationBigNoImage(ctx, notifyId, title, subTitle, bigTitleforNewAPI);
        }
        else
        {
            ImageLoader.getInstance().loadImage(imageURL, new ImageLoadingListener()
            {
                @Override
                public void onLoadingStarted(String s, View view)
                {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason)
                {
                    sendNotificationBigNoImage(ctx, notifyId, title, subTitle, bigTitleforNewAPI);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap)
                {
                    sendNotificationBigWithImage(ctx, notifyId, title, subTitle, bigTitleforNewAPI, bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view)
                {
                    sendNotificationBigNoImage(ctx, notifyId, title, subTitle, bigTitleforNewAPI);
                }
            });
        }


    }

    public static void sendNotification(Context ctx, String notificationDetails, String sub, int notifyId, int iconResource)
    {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(ctx.getApplicationContext(), CategoryActivity.class);

        // Construct a task stack.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        // Add the main Activity to the task stack as the parent.
//        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
//        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
//        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(ctx.getApplicationContext(), notifyId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);

        // Define the notification settings.
        builder.setSmallIcon(iconResource)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                //.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_launcher)).
                .setContentTitle(notificationDetails).setContentText(sub).setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);


        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(notifyId, builder.build());
    }
}
