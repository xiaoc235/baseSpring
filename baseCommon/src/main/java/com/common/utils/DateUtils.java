package com.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by conor on 2017/3/13.
 */
public class DateUtils {

    /**
     * 计算两个日期的时间差
     * @param maxTime
     * @param minTime
     * @return
     */
    public static String getTimeDifference(Timestamp maxTime, Timestamp minTime) {
        if(maxTime==null||minTime==null){
            return "";
        }
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long t1 = 0L;
        long t2 = 0L;
        try {
            t1 = timeformat.parse(getTimeStampNumberFormat(maxTime)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            t2 = timeformat.parse(getTimeStampNumberFormat(minTime)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //因为t1-t2得到的是毫秒级,所以要初3600000得出小时.算天数或秒同理
        int days = (int)((t1-t2)/(3600000*24));
        int hours=(int) ((t1 - t2)/3600000-days*24);
        int minutes=(int) (((t1 - t2)/1000-(hours+days*24)*3600)/60);
        int second=(int) ((t1 - t2)/1000-(hours+days*24)*3600-minutes*60);
        return ""+days+"天"+hours+"小时"+minutes+"分"+second+"秒";
    }


    /**
     * 计算两个日期的时间差,
     * 如果时间差>1天，只显示天数；如果1天>时间差>1小时，只显示小时；如果1小时>时间差>1分钟，只显示分钟；如果1分钟>时间差>1秒，只显示秒；
     * @param maxTime
     * @param minTime
     * @return
     */
    public static String getShowTime(Timestamp maxTime, Timestamp minTime) {
        if(maxTime==null||minTime==null){
            return "";
        }
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long t1 = 0L;
        long t2 = 0L;
        try {
            t1 = timeformat.parse(getTimeStampNumberFormat(maxTime)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            t2 = timeformat.parse(getTimeStampNumberFormat(minTime)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //因为t1-t2得到的是毫秒级,所以要初3600000得出小时.算天数或秒同理
        int days = (int)((t1-t2)/(3600000*24));
        int hours=(int) ((t1 - t2)/3600000-days*24);
        int minutes=(int) (((t1 - t2)/1000-(hours+days*24)*3600)/60);
        int second=(int) ((t1 - t2)/1000-(hours+days*24)*3600-minutes*60);
        String showTime = "";
        if(days>=1){
            showTime = days+"天";
        }else if(hours>=1){
            showTime = hours+"小时";
        }else if(minutes>=1){
            showTime = minutes+"分";
        }else if(second>=1){
            showTime = second+"秒";
        }
        return showTime;
    }


    /**
     * 格式化时间
     * Locale是设置语言敏感操作
     * @param formatTime
     * @return
     */
    public static String getTimeStampNumberFormat(Timestamp formatTime) {
        if(formatTime==null){
            return "";
        }
        SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh", "cn"));
        return m_format.format(formatTime);
    }

    /**
     * 格式化时间,只获取时间
     * Locale是设置语言敏感操作
     * @param formatTime
     * @return
     */
    public static String getTime(Timestamp formatTime) {
        if(formatTime==null){
            return "";
        }
        SimpleDateFormat m_format = new SimpleDateFormat("HH:mm:ss", new Locale("zh", "cn"));
        return m_format.format(formatTime);
    }

    /**
     * 格式化时间,只获取日期
     * Locale是设置语言敏感操作
     * @param formatTime
     * @return
     */
    public static String getDate(Timestamp formatTime) {
        if(formatTime==null){
            return "";
        }
        SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "cn"));
        return m_format.format(formatTime);
    }
}
