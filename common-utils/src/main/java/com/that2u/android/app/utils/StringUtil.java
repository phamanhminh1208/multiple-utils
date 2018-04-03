package com.that2u.android.app.utils;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by phama on 2016-07-14.
 */
public final class StringUtil {
    public static String getCapitalizeString(String str){
        if(!TextUtils.isEmpty(str)){
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
        return "";
    }

    public static String convertArrayList2String(ArrayList<Character> characters){
        StringBuilder result = new StringBuilder(characters.size());
        for (Character c : characters) {
            result.append(c);
        }
        return result.toString();
    }
}
