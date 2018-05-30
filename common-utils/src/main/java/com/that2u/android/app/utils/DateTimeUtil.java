package com.that2u.android.app.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sev_user on 3/14/2017.
 */

public final class DateTimeUtil {
    private static final SimpleDateFormat sDateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static Calendar getDateStringOnlyFromMillisecond(long millisecond){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Date getDateOnlyFromMillisecond(long millisecond){
        Calendar calendar = getDateStringOnlyFromMillisecond(millisecond);
        try {
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isInSameDay(long millisecond1, long millisecond2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(millisecond1);
        cal2.setTimeInMillis(millisecond2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
