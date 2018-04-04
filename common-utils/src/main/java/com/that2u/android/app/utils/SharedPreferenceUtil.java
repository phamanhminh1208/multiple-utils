package com.that2u.android.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by phama on 3/25/2018.
 */

public class SharedPreferenceUtil {
    private static final String PREFERENCE_NAME = "shared_preferences";

    private static synchronized SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        try {
            return getSharedPreference(context).getBoolean(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static synchronized boolean getBooleanValue(Context context, String key) {
        return getBooleanValue(context, key, false);
    }

    public static synchronized boolean setBooleanValue(Context context, String key, boolean value) {
        try {
            getSharedPreference(context)
                    .edit()
                    .putBoolean(key, value)
                    .apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
