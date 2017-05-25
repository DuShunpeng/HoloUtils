package com.meevii.holoutils;

import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DS on 12/12/16.
 */

public class DateUtil {
    public static String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    public static String FORMAT_MMdd = "MM/dd";

    public static String getCurrentString() {
        return getCurrentWithFormat(DEFAULT_DATE_FORMAT);
    }

    public static String getRandomString() {
        SimpleDateFormat ft = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        Date date = new Date(getRandomTimestamp());
        return getFormatDate(date, ft);
    }

    public static String getDefaultDateFormat(Calendar cal) {
        return getDateStringWithFormat(cal.getTime(), DEFAULT_DATE_FORMAT);
    }

    public static String getDateStringWithFormat(Date date, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.getDefault());
        return format.format(date);
    }

    public static int compare(String before, String after, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date a = sdf.parse(before);
        Date b = sdf.parse(after);
        return a.compareTo(b);
    }

    public static String getYYYYMMDDDateString(Calendar cal) {
        return getDateStringWithFormat(cal.getTime(), DEFAULT_DATE_FORMAT);
    }

    public static String getTodayWithDefaultFormat() {
        return getCurrentWithFormat(DEFAULT_DATE_FORMAT);
    }

    public static String getBeforeDayWithDefautFormat(String defaultFormateDay) {
        return getDayWithStep(defaultFormateDay, -1);
    }

    public static String getAfterDayWithDefautFormat(String defaultFormateDay) {
        return getDayWithStep(defaultFormateDay, 1);
    }

    public static boolean range(String startDate, String endDate, Date date) throws ParseException {
        return range(startDate, endDate, FORMAT_MMdd, date);
    }

    public static boolean range(String startDate, String endDate, String format, Date date) throws ParseException {
        String dateToday = getDayWithFormat(format, date);
        if (compare(dateToday, startDate, format) < 0) {
            return false;
        }
        if (compare(endDate, dateToday, format) < 0) {
            return false;
        }
        return true;
    }

    private static String getDayWithStep(String defaultFormateDay, int step) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(defaultFormateDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + step);
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(c.getTime());
    }

    public static String getCurrentWithFormat(String formatString) {
        SimpleDateFormat ft = new SimpleDateFormat(formatString, Locale.US);
        Date date = new Date(System.currentTimeMillis());
        return getFormatDate(date, ft);
    }

    private static String getDayWithFormat(String formatString, Date date) {
        SimpleDateFormat ft = new SimpleDateFormat(formatString, Locale.getDefault());
        return getFormatDate(date, ft);
    }

    private static String getFormatDate(Date d, java.text.DateFormat df) {
        return df.format(d);
    }

    private static long getRandomTimestamp() {
        long offset = Timestamp.valueOf("2016-07-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2017-06-30 00:00:00").getTime();
        long diff = end - offset + 1;
        Timestamp rand = new Timestamp(offset + (long) (Math.random() * diff));
        Log.i("rtime", "rand.getTime(): " + rand.getTime());
        return rand.getTime();
    }
}
