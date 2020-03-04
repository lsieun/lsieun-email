package lsieun.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static lsieun.utils.LogUtils.*;

public class DateUtils {
    private static final DateFormat STANDARD_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat FILE_FORMAT = new SimpleDateFormat("yyyy_MM_dd.HH_mm_ss");

    public static String getTimestamp() {
        Date now = new Date();
        return FILE_FORMAT.format(now);
    }

    public static Date getToday(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND, 0);
        Date today = cal.getTime();
        return today;
    }

    public static Date getNextDay(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND, 0);
        Date today = cal.getTime();
        return today;
    }

    public static boolean betweenTime(int startHour, int startMinute, int endHour, int endMinute) {
        Date now = new Date();
        Date startTime = DateUtils.getToday(startHour, startMinute);
        Date endTime = DateUtils.getToday(endHour, endMinute);
        if (startTime.before(now) && endTime.after(now)) {
            return true;
        }
        return false;
    }

    public static String toReadable(long delta) {
        long currentTimeMillis = System.currentTimeMillis();
        long timestamp = currentTimeMillis + delta;
        Date futureTime = new Date(timestamp);
        return STANDARD_FORMAT.format(futureTime);
    }
}
