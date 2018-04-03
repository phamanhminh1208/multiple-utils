package com.that2u.android.app.utils;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;

/**
 * Created by minhpa on 26/03/2017.
 */

public final class StorageUtils {
    public static String getInternalFilePath(Context context, String dirName, String fileName){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());

        File directory = cw.getDir(dirName, Context.MODE_PRIVATE);

        if (!directory.exists()) {
            directory.mkdir();
        }

        File mypath = new File(directory, fileName);

        return mypath.getAbsolutePath();
    }


}
