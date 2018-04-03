package com.that2u.android.app.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by phama on 2017-03-12.
 */

public class VibrateUtil {
    private static final int LIGHT_LENGTH = 100;
    private static final int NORMAL_LENGTH = 300;
    private static final int STRONG_LENGTH = 1000;

    public static void vibrate(Context context, int length){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(length);
    }

    public static void lightVibrate(Context context){
        vibrate(context, LIGHT_LENGTH);
    }

    public static void normalVibrate(Context context){
        vibrate(context, NORMAL_LENGTH);
    }

    public static void strongVibrate(Context context){
        vibrate(context, STRONG_LENGTH);
    }
}
