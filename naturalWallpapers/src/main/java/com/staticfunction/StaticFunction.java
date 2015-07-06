package com.staticfunction;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

/**
 * Created by user on 6/24/2015.
 */
public class StaticFunction
{

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
}
