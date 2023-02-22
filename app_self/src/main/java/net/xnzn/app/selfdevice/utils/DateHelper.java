package net.xnzn.app.selfdevice.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {


    static SimpleDateFormat simpleDateFormat7 = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat simpleDateFormat8 = new SimpleDateFormat("MM-dd");


    public static String getNowHm() {
        return simpleDateFormat7.format(new Date());
    }

    public static String getHmByTime(long time) {
        return simpleDateFormat7.format(time);
    }

    public static String getNowMd() {
        return simpleDateFormat8.format(new Date());
    }

    public static String getMdByTime(long time) {
        return simpleDateFormat8.format(time);
    }

    public static String getCurrentWeek() {

        Calendar c = Calendar.getInstance();

        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }

        return "星期" + mWay;
    }

}
