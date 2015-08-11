package com.lvwallpapers.GCM;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.vlb.wallpaper.R;
import com.staticfunction.StaticFunction;

public class GcmMessageHandler extends IntentService {

//    String mes;
//    private Handler handler;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        String title = extras.getString("title");
        String subtitle = extras.getString("subtitle");

        StaticFunction.sendNotification(GcmMessageHandler.this, title, subtitle, 1010, R.drawable.ic_launcher);

//        mes = extras.getString("title");
        //mes = extras.toString();
//        showToast();

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

//    public void showToast() {
//        handler.post(new Runnable() {
//            public void run() {
//                Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
//            }
//        });
//    }

}
