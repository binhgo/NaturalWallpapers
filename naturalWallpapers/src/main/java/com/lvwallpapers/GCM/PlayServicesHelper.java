package com.lvwallpapers.GCM;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.staticfunction.StaticFunction;

import java.io.IOException;

public class PlayServicesHelper {

    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_IS_UPLOAD_TO_LOCAL_SERVER = "ISuPLOADtOlOCALsERVER";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String TAG = "PlayServicesHelper";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private GoogleCloudMessaging googleCloudMessaging;
    private Activity activity;
    private String regId;

    public PlayServicesHelper(Activity activity) {
        this.activity = activity;
        checkPlayService();
    }

    private void checkPlayService() {
        // Check device for Play Services APK. If check succeeds, proceed with
        // GCM registration.
        if (checkPlayServices()) {
            googleCloudMessaging = GoogleCloudMessaging.getInstance(activity);
            regId = getRegistrationId();

            if (regId.isEmpty()) {
                registerInBackground();
            } else {
                boolean isUpload = getIsUploadToLocalServer();

                if (isUpload == false) {
                    API api = new API();
                    String result = api.uploadGCMRegisterIdToServer(regId);
                    boolean isUploadRegIdToLocalServer = false;
                    if (result.equalsIgnoreCase("")) {
                        isUploadRegIdToLocalServer = true;

                    } else {
                        isUploadRegIdToLocalServer = false;
                    }

                    // Persist the regID - no need to register again.
                    storeRegistrationId(regId, isUploadRegIdToLocalServer);
                }
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId() {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }


    private boolean getIsUploadToLocalServer() {
        final SharedPreferences prefs = getGCMPreferences();
        boolean isUpload = prefs.getBoolean(PROPERTY_IS_UPLOAD_TO_LOCAL_SERVER, false);
        return isUpload;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (googleCloudMessaging == null) {
                        googleCloudMessaging = GoogleCloudMessaging.getInstance(activity);
                    }
                    regId = googleCloudMessaging.register(StaticFunction.PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regId;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.


                    API api = new API();
                    String result = api.uploadGCMRegisterIdToServer(regId);
                    boolean isUploadRegIdToLocalServer = false;
                    if (result.equalsIgnoreCase("")) {
                        isUploadRegIdToLocalServer = true;

                    } else {
                        isUploadRegIdToLocalServer = false;
                    }

                    // Persist the regID - no need to register again.
                    storeRegistrationId(regId, isUploadRegIdToLocalServer);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg + "\n");
            }
        }.execute(null, null, null);
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences() {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
    }

    /**
     * Subscribe to Push Notifications
     *
     * @param regId registration ID
     */
    private void subscribeToPushNotifications(String regId) {
        Log.d(TAG, "subscribing...");
        /*String deviceId;
        final TelephonyManager mTelephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null)
        {
            deviceId = mTelephony.getDeviceId(); /*//*** use for mobiles
         } else
         {
         deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID); /*//*** use for tablets
         }*/

        //call API and send the reg id to server
        API api = new API();
        api.uploadGCMRegisterIdToServer(regId);
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param regId registration ID
     */
    private void storeRegistrationId(String regId, boolean isUploadToLocalServer) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion();
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.putBoolean(PROPERTY_IS_UPLOAD_TO_LOCAL_SERVER, isUploadToLocalServer);
        editor.commit();
    }

    public int getAppVersion() {
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}