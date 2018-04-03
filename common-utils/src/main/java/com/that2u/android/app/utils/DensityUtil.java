package com.that2u.android.app.utils;

import android.content.Context;

/**
 * Created by MinhPA on 12/10/2016.
 */

public class DensityUtil {
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int dpFromPx(final Context context, int px){
        return (int)(px / context.getResources().getDisplayMetrics().density);
    }

    public static int pxFromDp(final Context context, int dp){
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }
}
