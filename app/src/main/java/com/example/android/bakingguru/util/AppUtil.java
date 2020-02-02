package com.example.android.bakingguru.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.example.android.bakingguru.R;

public class AppUtil {

    public static boolean isTabletView(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static boolean isLandscapeView() {
        return Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isTabletOrLandscapeView(Context context) {
        return isTabletView(context) || isLandscapeView();
    }
}
