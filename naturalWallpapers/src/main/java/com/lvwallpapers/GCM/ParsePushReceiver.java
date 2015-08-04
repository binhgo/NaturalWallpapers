package com.lvwallpapers.GCM;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParsePushBroadcastReceiver;
import com.staticfunction.StaticFunction;

import org.json.JSONObject;

/**
 * Created by USER on 8/4/2015.
 */
public class ParsePushReceiver extends ParsePushBroadcastReceiver
{

    @Override
    protected void onPushReceive(Context context, Intent intent)
    {
        super.onPushReceive(context, intent);

        Bundle extras = intent.getExtras();
        String jsonData = extras.getString("com.parse.Data");


        String imageUrl = "";
        String title = "New pictures, check it out!";
        String sub = "Hello, we have updated a lot of new pictures, enjoy!!!";

        try
        {
            JSONObject jsonObject = new JSONObject(jsonData);
            imageUrl = jsonObject.getString("image");
            title = jsonObject.getString("title");
            sub = jsonObject.getString("sub");

        }
        catch (Exception ex)
        {
            imageUrl = "";
        }
        finally
        {
            StaticFunction.sendNotificationBig(context, 101, title, sub, title, imageUrl);
        }


    }

    @Override
    protected Notification getNotification(Context context, Intent intent)
    {
        return null;
    }
}
