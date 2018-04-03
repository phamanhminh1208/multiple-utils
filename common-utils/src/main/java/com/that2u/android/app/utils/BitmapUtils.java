package com.that2u.android.app.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by minhpa on 26/03/2017.
 */

public final class BitmapUtils {

    public static Bitmap fillBackground(Bitmap image, int bgColor) {
        Bitmap newBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(bgColor);
        canvas.drawBitmap(image, 0, 0, null);

        if (newBitmap != image) {
            image.recycle();
        }

        return newBitmap;
    }

    public static boolean storeAsImage(Bitmap image, Bitmap.CompressFormat format, String filePath) {
        File pictureFile = new File(filePath);

        if (pictureFile.exists()) {
            pictureFile.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(format, 90, fos);
            fos.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
