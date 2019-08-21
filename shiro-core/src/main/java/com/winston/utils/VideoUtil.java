package com.winston.utils;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;

/**
 * @author Winston
 * @title: VideoUtil
 * @projectName hq-pms
 * @description: 处理视频
 * @date 2019/4/2316:48
 */
public class VideoUtil {
    /**
     * 获取视频时长(时分秒)
     *
     * @param ms
     * @return
     */
    public static String ReadVideoTime(Long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
        if (strHour.equals("00")) {
            return strMinute + ":" + strSecond;
        } else {
            return strHour + ":" + strMinute + ":" + strSecond;
        }
    }

    /**
     * 获取视频时长(毫秒)
     *
     * @param file
     * @return
     */
    public static Long ReadVideoTimeMs(File file){

        // 获取视频时长
        Encoder encoder = new Encoder();
        Long length = 0l;

        MultimediaInfo m = null;
        try {
            m = encoder.getInfo(file);
            Long ls = m.getDuration();
            length = ls;
        } catch (EncoderException e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 删除
     *
     * @param files
     */
    private static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
