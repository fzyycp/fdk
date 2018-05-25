/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.anotation.NonNull;
import cn.faury.fdk.common.anotation.Nullable;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 */
public class DateUtil {

    /**
     * 年月日格式化
     */
    public static String FORMAT_DATE = "yyyy-MM-dd";

    /**
     * 年月日时分秒格式化
     */
    public static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串格式化为日期
     *
     * @param dateStr 日期字符串
     * @param format  格式化模式
     * @return 日期对象
     */
    @Nullable
    public static Date parse(@NonNull String dateStr, @NonNull String format) {
        if (StringUtil.isEmpty(dateStr)) {
            return null;
        }
        Date date = null;
        try {
            date = new SimpleDateFormat(StringUtil.emptyReplace(format, FORMAT_DATE)).parse(dateStr);
        } catch (RuntimeException | ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串格式化为日期
     *
     * @param dateStr 日期字符串
     * @return 日期对象
     */
    @Nullable
    public static Date parse(@NonNull String dateStr) {
        return parse(dateStr, FORMAT_DATE);
    }

    /**
     * 格式化日期字符串
     *
     * @param date   日期
     * @param format 格式化模式
     * @return 字符串
     */
    @Nullable
    public static String format(@NonNull Date date, @NonNull String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(StringUtil.emptyReplace(format, FORMAT_DATE)).format(date);
    }

    /**
     * 格式化日期字符串
     *
     * @param date 日期
     * @return 格式化字符串
     */
    @Nullable
    public static String formatDate(@NonNull Date date) {
        return format(date, FORMAT_DATE);
    }

    /**
     * 格式化日期字符串
     *
     * @param date 日期
     * @return 格式化字符串
     */
    @Nullable
    public static String formatDateTime(@NonNull Date date) {
        return format(date, FORMAT_DATE_TIME);
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    @NonNull
    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 获取当前日期字符串
     *
     * @return 当前日期字符串
     */
    @Nullable
    public static String getCurrentDateStr(@NonNull String format) {
        return format(getCurrentDate(), format);
    }

    /**
     * 获取当前日期字符串
     *
     * @return 当前日期字符串
     */
    @Nullable
    public static String getCurrentDateStr() {
        return formatDate(getCurrentDate());
    }

    /**
     * 获取当前日期时间字符串
     *
     * @return 当前日期时间字符串
     */
    @Nullable
    public static String getCurrentDateTimeStr() {
        return formatDateTime(getCurrentDate());
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳
     */
    @NonNull
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 将使用的毫秒数转化为可读的字符串, 如1天1小时1分1秒.
     *
     * @param millisecond 使用的毫秒数.
     * @return 可读的字符串, 如1天1小时1分1秒.
     */
    @Nullable
    public static String descDateTime(long millisecond) {
        // 用移位运算提高性能.
        if (millisecond < 0) {
            return String.valueOf(millisecond);
        }
        if (millisecond < 1000) {
            return String.valueOf(millisecond) + "毫秒";
        }
        // 长于1秒的过程，毫秒不计
        millisecond /= 1000;
        if (millisecond < 60) {
            return String.valueOf(millisecond) + "秒";
        }
        if (millisecond < 3600) {
            long nMinute = millisecond / 60;
            long nSecond = millisecond % 60;
            return String.valueOf(nMinute) + "分" + String.valueOf(nSecond) + "秒";
        }
        // 3600 * 24 = 86400
        if (millisecond < 86400) {
            long nHour = millisecond / 3600;
            long nMinute = (millisecond - nHour * 3600) / 60;
            long nSecond = (millisecond - nHour * 3600) % 60;
            return String.valueOf(nHour) + "小时" + String.valueOf(nMinute) + "分" + String.valueOf(nSecond) + "秒";
        }

        long nDay = millisecond / 86400;
        long nHour = (millisecond - nDay * 86400) / 3600;
        long nMinute = (millisecond - nDay * 86400 - nHour * 3600) / 60;
        long nSecond = (millisecond - nDay * 86400 - nHour * 3600) % 60;
        return String.valueOf(nDay) + "天" + String.valueOf(nHour) + "小时" + String.valueOf(nMinute) + "分"
                + String.valueOf(nSecond) + "秒";
    }

    /**
     * 从Date对象得到Calendar对象. <BR>
     * JDK提供了Calendar.getTime()方法, 可从Calendar对象得到Date对象,
     * 但没有提供从Date对象得到Calendar对象的方法.
     *
     * @param date 给定的Date对象
     * @return 得到的Calendar对象. 如果date参数为null, 则得到表示当前时间的Calendar对象.
     */
    @Nullable
    public static Calendar toCalendar(@NonNull Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        return cal;
    }

    /**
     * 检查一个时间是否接近于一个时间。
     *
     * @param date     要比较的时间
     * @param baseDate 基础时间
     * @param seconds  秒数
     * @return 如果 date 在 baseDate前后 seonds 秒数，则返回true，否则返回false。
     */
    public static boolean isDateClose(Date date, Date baseDate, int seconds) {
        long m_time = date.getTime();
        long b_time = baseDate.getTime();
        long ms = seconds * 1000L;

        return Math.abs(m_time - b_time) < ms;
    }
}
