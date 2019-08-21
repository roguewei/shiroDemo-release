package com.winston.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtile {

    public static String getTimeFormat(long millions){
        try{
            Date date = new Date(millions);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            String format1 = format.format(date);
            return format1;
        }catch (Exception e){
            return null;
        }
    }


    public static String getTimeFormatDate(long millions){
        try{
            Date date = new Date(millions);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            String format1 = format.format(date);
            return format1;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 将指定时间格式转换为时间戳
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Long getTimeFormatMill(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");//excel默认格式,
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date parse = format.parse(dateStr);
        return parse.getTime();
    }
}
