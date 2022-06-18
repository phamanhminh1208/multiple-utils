package com.that2u.android.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by minhpa on 26/03/2017.
 */

public final class BitmapUtils {
    private static final String DEFAULT_CACHE_IMAGE_FILE_NAME = "cache_img.png";

    public static String getAssetImageUri(String path){
        return "file:///android_asset/" + path;
    }

    public static Bitmap getBitmapFromAsset(Context context, String path){
        Bitmap bitmap = null;

        try{
            InputStream inputStream = context.getAssets().open(path);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }catch (Exception e){
            if(bitmap != null){
                bitmap.recycle();
            }
            bitmap = null;
            e.printStackTrace();
        }

        return bitmap;
    }

    public static File createCacheBitmapFile(Context context, Bitmap bitmap){
        return createCacheBitmapFile(context, bitmap, DEFAULT_CACHE_IMAGE_FILE_NAME, false);
    }

    public static File createCacheBitmapFile(Context context, Bitmap bitmap, String cacheFileName, Boolean needFillBackground){
        if(bitmap == null || bitmap.isRecycled()){
            return null;
        }

        if(TextUtils.isEmpty(cacheFileName)){
            cacheFileName = DEFAULT_CACHE_IMAGE_FILE_NAME;
        }

        FileOutputStream fOut = null;
        try{
            if(needFillBackground){
                bitmap = fillBackground(bitmap, Color.WHITE);
            }

            File file = new File(context.getCacheDir(), cacheFileName);
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            file.setReadable(true, false);
            return file;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fOut != null){
                try {
                    fOut.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static File createCacheBitmapFileFromAsset(Context context, String path){
        return createCacheBitmapFileFromAsset(context, path, DEFAULT_CACHE_IMAGE_FILE_NAME, false);
    }

    public static File createCacheBitmapFileFromAsset(Context context, String path, String cacheFileName, Boolean needFillBackground){
        if(TextUtils.isEmpty(path)){
            return null;
        }

        try{
            Bitmap tmpBitmap = getBitmapFromAsset(context, path);
            if(tmpBitmap != null && !tmpBitmap.isRecycled()){
                return createCacheBitmapFile(context, tmpBitmap, cacheFileName, needFillBackground);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap fillBackground(Bitmap image, int bgColor) {
        if(image == null || image.isRecycled()){
            return null;
        }

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
        if(image == null || image.isRecycled()){
            return false;
        }

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

    public static Bitmap resizeBitmapForFacebookShare(Bitmap bitmap){
        return resizeBitmap(bitmap, 400, 400);
    }

    public static Bitmap resizeBitmap(Bitmap source, int targetWidth, int targetHeight){
        if(source == null || source.isRecycled()){
            return source;
        }

        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        float scaleFactor = 1f;
        if(targetWidth > 0 || targetHeight > 0){
            scaleFactor = Math.min(targetWidth/sourceWidth, targetHeight/sourceHeight);
        }

        float newWidth = sourceWidth * scaleFactor;
        float newHeight = sourceHeight * scaleFactor;

        try{
            return Bitmap.createScaledBitmap(source, (int)newWidth, (int)newHeight, true);
        }catch (Exception e){
            e.printStackTrace();
        }

        return source;
    }
}
