package com.dmio.org.tut.core.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.dmio.org.tut.R;
import com.dmio.org.tut.application.ExApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期处理
 *
 * @author Mars.Wong
 * @date 2013-9-7 下午1:12:37
 * @copyright QH Technology Co.,LTD.
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

    private static Context mContext;

    private static final SimpleDateFormat formator = new SimpleDateFormat();
    private static final String FORMAT_DEFAULT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_DEFAULT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
    private static final String FORMAT_EN_DATE = "EEE MMM dd kk:mm:ss yyyy";

    public static final int DAY = 1000 * 60 * 60 * 24;
    public static final int HOUR = 1000 * 60 * 60;
    public static final int MINUTE = 1000 * 60;

    static {
        mContext = ExApplication.getInstance().getBaseContext();
    }

    public enum Type {
        message, conversation
    }

    private static String getString(int resId) {
        return mContext.getResources().getString(resId);
    }

    public static String getDateTimeCN(Date date) {
        String formatCN = getString(R.string.date_format_timestamp_cn);
        formator.applyPattern(formatCN);
        return formator.format(date);
    }

    public static String getDateTimeEN(Date date) {
        String formatEN = getString(R.string.date_format_timestamp_en);
        formator.applyPattern(formatEN);
        return formator.format(date);
    }

    public static String getDateEN(Date date) {
        formator.applyPattern(FORMAT_DEFAULT_DATE);
        return formator.format(date);
    }

    /**
     * 时间格式化
     *
     * @param dateTime Long类型的格式
     * @return 非今年 yy-MM-dd HH:mm
     * 今年非本月 MM-dd HH:mm
     * 今天 HH:mm
     */
    public static String timeFormat(Date dateTime) {
        if (dateTime == null) return null;
        // 获取传入日期
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        calendar.setTime(dateTime);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

        calendar.setTime(new Date());
        int todayYear = calendar.get(Calendar.YEAR);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayDate = calendar.get(Calendar.DATE);

        String dayType = "yy-MM-dd HH:mm";
        if (year == todayYear && month <= todayMonth) {
            dayType = "MM-dd HH:mm";
        }
        if (year == todayYear && month == todayMonth && date == todayDate) {
            dayType = "HH:mm";
        }

        String formatEN = getString(R.string.date_format_timestamp_en);
        formator.applyPattern(null == formatEN ? formatEN : dayType);
        return formator.format(dateTime);
    }

    public static String getNowCN() {
        return getDateTimeCN(new Date());
    }

    public static String getNowEN() {
        return getDateTimeEN(new Date());
    }

    public static String getDateNowEN() {
        return getDateEN(new Date());
    }

    /**
     * Date格式转换成特殊的String
     *
     * @param dateTime 日期
     * @return 如果是今天或昨天就返回今天 HH:mm 或 昨天 HH:mm，其它如果是本周则返回“星期x HH:mm”，如果不是则返回“yyyy-MM-dd HH:mm”
     */
    public static Date stringToDate(String dateTime) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * Date格式转换成特殊的String
     *
     * @param date 日期
     * @return 如果是今天或昨天就返回今天 HH:mm 或 昨天 HH:mm，其它如果是本周则返回“星期x HH:mm”，如果不是则返回“yyyy-MM-dd HH:mm”
     */
    public static String dateToString(Date date, Type type) {
        // 获取一周星期数字符串
        String weekBunch = getString(R.string.date_week_bunch);
        // 获取时区
        TimeZone timeZone = TimeZone.getDefault();

        // 获取传入日期
        final Calendar currentDay = Calendar.getInstance(timeZone);
        currentDay.setTime(date);
        int year = currentDay.get(Calendar.YEAR); // 获取年份
        int month = currentDay.get(Calendar.MONTH) + 1;// 获取月份
        int day = currentDay.get(Calendar.DAY_OF_MONTH);// 获取日期
        int hour = currentDay.get(Calendar.HOUR_OF_DAY);
        int minute = currentDay.get(Calendar.MINUTE);
        int currentWay = currentDay.get(Calendar.DAY_OF_WEEK);

        // 获取当前日期
        final Calendar todayDay = Calendar.getInstance(timeZone);
        todayDay.setTime(new Date());
        int nowYear = todayDay.get(Calendar.YEAR); // 获取当前年份
        int nowMonth = todayDay.get(Calendar.MONTH) + 1;// 获取当前月份
        int nowDay = todayDay.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
        int nowWay = todayDay.get(Calendar.DAY_OF_WEEK); // 一周内的第几天

        // 分隔一周星期串
        String[] days = weekBunch.split(",");
        // 当前星期
        // String weekDate = days[nowWay];
        String weekDate = days[currentWay];

        // 比较当前日期和传入日期差值，以便做出不同的显示方式
        int n = day - nowDay;
        // 当前年
        if (year == nowYear) {
            // 当前月
            if (month == nowMonth) {
                if (n == 0) {
                    // 当前天
                    if (Type.message.equals(type)) {
                        return String.format(getString(R.string.date_msg_today), (hour > 9 ? hour : "0" + hour), (minute > 9 ? minute : "0" + minute));
                    } else if (Type.conversation.equals(type)) {
                        return String.format(getString(R.string.date_hour_minute), (hour > 9 ? hour : "0" + hour), (minute > 9 ? minute : "0" + minute));
                    }
                } else if (n == -1) {
                    // 前一天
                    if (Type.message.equals(type)) {
                        return String.format(getString(R.string.date_msg_yesterday), (hour > 9 ? hour : "0" + hour), (minute > 9 ? minute : "0" + minute));
                    } else if (Type.conversation.equals(type)) {
                        return getString(R.string.date_yesterday);
                    }
                } else if ((n >= 1 - nowWay) && (n < 8 - nowWay)) {
                    // 同一个星期
                    if (Type.message.equals(type)) {
                        return String.format(getString(R.string.date_msg_week), weekDate, (hour > 9 ? hour : "0" + hour), (minute > 9 ? minute : "0" + minute));
                    } else if (Type.conversation.equals(type)) {
                        return String.format(getString(R.string.date_week), weekDate);
                    }
                } else {
                    // 当前月其他天
                    if (Type.message.equals(type)) {
                        return String.format(getString(R.string.date_month_day_hm), (month > 9 ? month : "0" + month), (day > 9 ? day : "0" + day), (hour > 9 ? hour : "0" + hour), (minute > 9 ? minute : "0" + minute));
                    } else if (Type.conversation.equals(type)) {
                        return String.format(getString(R.string.date_month_day), (month > 9 ? month : "0" + month), (day > 9 ? day : "0" + day));
                    }
                }
            } else {
                // 非当前月
                if (Type.message.equals(type)) {
                    return String.format(getString(R.string.date_month_day_hm), (month > 9 ? month : "0" + month), (day > 9 ? day : "0" + day), (hour > 9 ? hour : "0" + hour), (minute > 9 ? minute : "0" + minute));
                } else if (Type.conversation.equals(type)) {
                    return String.format(getString(R.string.date_month_day), (month > 9 ? month : "0" + month), (day > 9 ? day : "0" + day));
                }
            }
        }

        if (Type.conversation.equals(type)) {
            // formator.applyPattern(FORMAT_DEFAULT_DATE);
            return String.format(getString(R.string.date_year_month), String.valueOf(year).substring(2, 4), (month > 9 ? month : "0" + month), (day > 9 ? day : "0" + day));
        } else {
            // 其他显示默认格式
            formator.applyPattern(FORMAT_DEFAULT_TIMESTAMP);
            return formator.format(date);
        }

    }

    /**
     * 将指定格式串的日期串转换为对应的日期
     *
     * @param dateStr 仅限于格式为yyyy-MM-dd HH:mm:ss的日期串转换
     * @return
     */
    public static Date parseToDate(String dateStr) {
        if (null != dateStr) {
            try {
                int year = Integer.valueOf(dateStr.substring(0, dateStr.indexOf("-")));
                int month = Integer.valueOf(dateStr.substring(dateStr.indexOf("-") + 1, dateStr.lastIndexOf("-")));
                int date = Integer.valueOf(dateStr.substring(dateStr.lastIndexOf("-") + 1, dateStr.indexOf(" ")));

                int hourOfDay = Integer.valueOf(dateStr.substring(dateStr.indexOf(" ") + 1, dateStr.indexOf(":")));
                int minute = Integer.valueOf(dateStr.substring(dateStr.indexOf(":") + 1, dateStr.lastIndexOf(":")));
                int second = Integer.valueOf(dateStr.substring(dateStr.lastIndexOf(":") + 1, dateStr.length()));

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, date, hourOfDay, minute, second);
                return calendar.getTime();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取时间差（刚刚/xx小时以前/xx天以前）
     *
     * @param date
     * @return
     */
    public static String getDateStr(Date date) {
        if (date == null) {
            return "";
        }

        Date now = new Date();
        long diff = now.getTime() - date.getTime();

        if (diff < 0) {
            return getString(R.string.date_time_span_now);
        }

        long days = diff / DAY;
        if (days < 1) {
            long hours = diff / HOUR;
            if (hours < 1) {
                long minuts = diff / MINUTE;
                if (minuts < 1) {
                    return getString(R.string.date_time_span_now);
                } else {
                    return String.format(getString(R.string.date_time_span_minutes), minuts);
                }
            } else {
                return String.format(getString(R.string.date_time_span_hours), hours);
            }
        } else {
            return String.format(getString(R.string.date_time_span_days), days);
        }
    }

    /**
     * 转换日期格式串
     *
     * @param dateStr eg.2016-11-02
     * @return
     */
    public static Date parseDate(String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DEFAULT_DATE);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 转换英文日期时间格式
     *
     * @param dateStr eg.Mon Nov 14 20:28:24 2016
     * @return
     */
    public static Date parseENDateTime(String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_EN_DATE, Locale.ENGLISH);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取两个日期之间相差的天数
     *
     * @param m1 日期1毫秒数
     * @param m2 日期2毫秒数
     * @return
     */
    public static int getTwoDaysGap(long m1, long m2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(m1);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTimeInMillis(m2);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);
        return Math.abs(day2 - day1);
    }

}
