package com.that2u.android.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sev_user on 3/14/2017.
 */

public final class DateTimeUtil {
    private static final SimpleDateFormat sDateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static String getDateStringOnlyFromMillisecond(long millisecond){
        Date date = new Date(millisecond);
        return sDateOnlyFormat.format(date);
    }

    public static Date getDateOnlyFromMillisecond(long millisecond){
        String dateStr = getDateStringOnlyFromMillisecond(millisecond);
        try {

            return sDateOnlyFormat.parse(dateStr);
        } catch (ParseException e) {
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
