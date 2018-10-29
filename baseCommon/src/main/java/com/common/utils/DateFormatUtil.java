package com.common.utils;


import com.sun.istack.internal.Nullable;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateFormatUtil {
    public static final String      YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String      YYYY_MM_DD          = "yyyy-MM-dd";


    private static String defaultPattern(String pattern){
       return  (pattern == null || ("").equals(pattern)) ? YYYY_MM_DD_HH_MM_SS : pattern;
    }

    public static String date2Str(LocalDateTime date, String pattern) {
        return DateTimeFormatter.ofPattern(defaultPattern(pattern)).format(date);
    }

    public static String date2Str(LocalDateTime date) {
        return date2Str(date,null);
    }

    public static LocalDateTime str2date(String dateString){
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    public static String timestamp2Str(Timestamp timestamp, @Nullable String pattern) {
        return date2Str(timestamp.toLocalDateTime(), pattern);
    }

    public static Timestamp date2timestamp(LocalDateTime date) {
        return Timestamp.valueOf(date);
    }

    public static Timestamp date2timestamp(Instant date) {
        return Timestamp.from(date);
    }

    public static Timestamp str2timestamp(String dateStr) {
        return Timestamp.valueOf(dateStr);
    }

    /**
     * 得到本周周一
     */
    public static LocalDateTime getMondayOfThisWeek() {
        return LocalDateTime.now().plusWeeks(0).minusDays(LocalDateTime.now().getDayOfWeek().getValue() - 1L);
    }

    /**
     * 得到本周周日
     */
    public static LocalDateTime getSundayOfThisWeek() {
        return LocalDateTime.now().plusWeeks(1).minusDays(LocalDateTime.now().getDayOfWeek().getValue());
    }

    /**
     * 获取当前时间: yyyy-MM-dd HH:MM:SS
     */
    public static String getCurrentTime() {
        return date2Str(LocalDateTime.now(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取当前日期: yyyy-MM-dd
     */
    public static String getCurrentDate() {
        return date2Str(LocalDateTime.now(), YYYY_MM_DD);
    }

    /**
     * 两个时间间隔
     * @param start
     * @param end
     * @return
     */
    public static int getBetweenTwoDays(LocalDateTime start, LocalDateTime end) {
        return start.compareTo(end);
    }

    public static int getBetweenTwoDays(String start, String end) {
        return getBetweenTwoDays(str2date(start), str2date(end));
    }


    public static void main(String[] args) {
        System.out.println(getBetweenTwoDays(str2date("2018-09-10"), str2date("2018-09-10")));
    }
}
