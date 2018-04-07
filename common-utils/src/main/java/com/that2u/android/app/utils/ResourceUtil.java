package com.that2u.android.app.utils;

import android.content.Context;
import android.net.Uri;

/**
 * Created by minhpa on 6/30/2016.
 */

public class ResourceUtil {

    public static final String TYPE_STRING = "string";
    public static final String TYPE_DRAWABLE = "drawable";

    public static int getResourceId(Context context, String name, String type) {
        return context.getResources().getIdentifier(name, type,
                context.getPackageName());
    }

    public static Uri getAssetFileUri(String path){
        return Uri.parse("file:///android_asset/"+path);
    }
}
