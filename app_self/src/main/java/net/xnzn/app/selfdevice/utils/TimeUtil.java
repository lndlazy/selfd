package net.xnzn.app.selfdevice.utils;

import android.text.TextUtils;
import android.util.Log;

import net.xnzn.lib_utils.ByteUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TimeUtil {



  /**
   * "1"、"2"、"3"、"4" 、5、6、7
   *
   * @return
   */
  public static String getWeek() {
    Calendar cal = Calendar.getInstance();
    return (cal.get(Calendar.DAY_OF_WEEK)) + "";
  }


  /**
   * 判断时间是否在时间段内
   *
   * @param date         当前时间 yyyy-MM-dd HH:mm:ss
   * @param strDateBegin 开始时间 00:00:00
   * @param strDateEnd   结束时间 00:05:00
   * @return
   */
  public static boolean isInDate(Date date, String strDateBegin,
                                 String strDateEnd) {

    if (TextUtils.isEmpty(strDateBegin) || TextUtils.isEmpty(strDateEnd)) {
      return false;
    }

    if (TextUtils.equals("null", strDateBegin) || TextUtils.equals("null", strDateEnd)) {
      return false;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strDate = sdf.format(date);
    // 截取当前时间时分秒
    int strDateH = Integer.parseInt(strDate.substring(11, 13));
    int strDateM = Integer.parseInt(strDate.substring(14, 16));
    int strDateS = Integer.parseInt(strDate.substring(17, 19));
    // 截取开始时间时分秒
    int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
    int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
    int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));

    // 截取结束时间时分秒
    int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
    int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
    int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));


//    LogUtils.i("当前时间时分秒：" + strDateH + " " + strDateM + " " + strDateS);
//    LogUtils.i("开始时间时分秒：" + strDateBeginH + " " + strDateBeginM + " " + strDateBeginS);
//    LogUtils.i("结束时间时分秒：" + strDateEndH + " " + strDateEndM + " " + strDateEndS);


    long longStart = time2long(strDateBegin);
    long longEnd = time2long(strDateEnd);
    long longNow = new Date().getTime();


//    LogUtils.i("=============================");
//    LogUtils.i("longNow " + longNow + " longStart " + longStart + " longEnd" + longEnd);
//    LogUtils.i("longNow " + long2Str(longNow) + " longStart " + long2Str(longStart) + " longEnd " + long2Str(longEnd));


    if (longStart <= longNow && longNow <= longEnd) {
      return true;
    }
    return false;


  }


  static SimpleDateFormat simpleDateFormatSSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  static SimpleDateFormat simpleDateFormatT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  static SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat("yyMMddHHmmss");
  static SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("yyyyMMddHHmmss");
  static SimpleDateFormat simpleDateFormat7 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
  static SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("yyyyMMdd");
  static SimpleDateFormat simpleDateFormat8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  static SimpleDateFormat simpleDateFormat9 = new SimpleDateFormat("yyyy-MM-dd");
  static SimpleDateFormat simpleDateFormat10 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  static SimpleDateFormat simpleDateFormat11 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
  static SimpleDateFormat simpleDateFormat12 = new SimpleDateFormat("HH:mm:ss");
  static SimpleDateFormat simpleDateFormat13 = new SimpleDateFormat("MM");

  public static String getCurrentYmd11() {
    return simpleDateFormat11.format(new Date());
  }

  public static String getCurrentYmd10() {
    return simpleDateFormat10.format(new Date());
  }

  public static String getNowDateQucan() {
    return simpleDateFormat12.format(new Date());
  }

  public static String getCurrentMon() {
    return simpleDateFormat13.format(new Date());
  }


  public static long getTimeLong2(String time) {
    try {
      Date date = simpleDateFormatT.parse(time);
      return date.getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return 0;

  }

  public static long getTimeLong(String time) {
    try {
      Date date = simpleDateFormat.parse(time);
      return date.getTime();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return 0;

  }

  /**
   * @param datetime
   * @return String  返回类型
   * @throws
   * @Title: dateToWeek
   * @Description: 根据日期获取 星期 （2019-05-06 ——> 星期一）
   */
  public static String dateToWeek(String datetime) {

    if (TextUtils.isEmpty(datetime)) {
      return "时间未返回";
    }
    SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    Calendar cal = Calendar.getInstance();
    Date date;
    try {
      date = f.parse(datetime);
      cal.setTime(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    // 一周的第几天
    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if (w < 0)
      w = 0;
    return weekDays[w];
  }


  /**
   * @param datetime
   * @return String  返回类型
   * @throws
   * @Title: dateToWeek
   * @Description: 根据日期获取 星期 （2019-05-06 ——> 星期一）
   */
  public static String dateToWeek2(String datetime) {

    if (TextUtils.isEmpty(datetime)) {
      return "时间未返回";
    }
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    Calendar cal = Calendar.getInstance();
    Date date;
    try {
      date = f.parse(datetime);
      cal.setTime(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    // 一周的第几天
    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if (w < 0)
      w = 0;
    return weekDays[w];
  }

  public static String dateToWeek3(String datetime) {

    if (TextUtils.isEmpty(datetime)) {
      return "时间未返回";
    }
    SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
    String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    Calendar cal = Calendar.getInstance();
    Date date;
    try {
      date = f.parse(datetime);
      cal.setTime(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    // 一周的第几天
    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if (w < 0)
      w = 0;
    return weekDays[w];
  }


  public static String getCurrentLogTime() {
    return simpleDateFormatSSS.format(new Date()) + " ";
  }

  public static String getLog(long mis) {
    return simpleDateFormatSSS.format(new Date(mis));
  }


  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  public static String getCurrentYmdhmsSSS(long time) {
    return simpleDateFormat7.format(new Date(time));
  }

  public static String getCurrentYmdhmsSSS() {
    return simpleDateFormat7.format(new Date());
  }

  public static String getCurrentYmdhms() {
    return simpleDateFormat8.format(new Date());
  }

  public static String getCurrentYmdhms2(long time) {
    return simpleDateFormat8.format(new Date(time));
  }

  public static String getCurrentYmd2() {
    return simpleDateFormat9.format(new Date());
  }

  public static String getCurrentYmd() {
    return sdf.format(new Date());
  }

  public static String getNextDateYmd() {
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, +1); //今天的时间加一天
    date = calendar.getTime();
    return sdf.format(date);
  }

  public static String getCurrentYmdhms(long time) {


    return simpleDateFormat5.format(new Date(time));
  }

  public static String getPayTime() {
    return simpleDateFormatSSS.format(new Date());

  }

  /**
   * HH：MM：：SS转时间戳
   *
   * @param s
   * @return
   */
  public static long time2long(String s) {

    Date nowDate = new Date();
    String yearMouthDay = sdf.format(nowDate);

    s = yearMouthDay + " " + s;

    Log.e("time", "ssss= " + s);

    Date date = null;
    try {
      date = simpleDateFormat.parse(s);
    } catch (ParseException e) {
      e.printStackTrace();
    }


    long ts = date.getTime();


    return ts;
  }


  public static long getLongTime(String time) {
    try {
      if (time == null) {
        return 0;
      }
      return simpleDateFormat.parse(time.replace("T", " ")).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return 0;
  }

  /**
   * 最新刷卡时间是否在当前时间
   *
   * @param time
   * @return
   */
  public static boolean isToday(String time) {

    String timeStr = time.substring(0, 8);

    String temp = simpleDateFormat4.format(new Date());

//    LogUtils.i("最新刷卡时间是 " + time + " 当前时间 " + temp);


    if (TextUtils.equals(timeStr, temp)) {
      return true;
    }

    return false;

  }

  public static String getTodayHMD() {
    String temp = simpleDateFormat4.format(new Date());
    return temp;
  }


  /**
   * 最新刷卡时间是否在当前时段
   *
   * @param time
   * @return
   */
  public static boolean isCurrentTimeInterval(String time, String s, String e) {


    try {

      if (isEffectiveDate(simpleDateFormat5.parse(time), s, e)) {
//        LogUtils.i("最新刷卡时间 在 当前时段 " + time + " " + s + "-" + e);

        return true;
      }

    } catch (ParseException e1) {
      e1.printStackTrace();

    }

//    LogUtils.i("最新刷卡时间 不在 当前时段 " + time + " " + s + "-" + e);
    return false;
  }


  public static String long2Str(long time) {
    return simpleDateFormat.format(new Date(time));
  }


  /**
   * yyMMddHHmmss
   *
   * @return
   */
  public static String getCurrentyyMMddHHmmss() {
    return simpleDateFormat6.format(new Date());
  }


  public static String long2Time(long time) {
    return sdf.format(time);
  }


  public static String long2Time2(long time) {
    return simpleDateFormat.format(time);
  }

  public static String getTimeStr() {
    return simpleDateFormat.format(new Date());
  }


  /**
   * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
   *
   * @param nowTime   当前时间
   * @param startTimeStr 开始时间
   * @param endTimeStr   结束时间
   * @return
   * @author jqlin
   */
  static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");

  public static boolean isEffectiveDate(Date nowTime, String startTimeStr, String endTimeStr) {


    Date startTime = null;
    Date endTime = null;


    try {
      if (!TextUtils.isEmpty(startTimeStr)) {
        startTimeStr = startTimeStr.substring(0, 2) + ":" + startTimeStr.substring(2, 4);
      }
      if (!TextUtils.isEmpty(endTimeStr)) {
        endTimeStr = endTimeStr.substring(0, 2) + ":" + endTimeStr.substring(2, 4);
      }
      startTime = simpleDateFormat2.parse(startTimeStr);
      endTime = simpleDateFormat2.parse(endTimeStr);

    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }


    if (nowTime.getTime() == startTime.getTime()
            || nowTime.getTime() == endTime.getTime()) {
      return true;
    }

    Calendar date = Calendar.getInstance();
    date.setTime(nowTime);

    Calendar begin = Calendar.getInstance();
    begin.setTime(startTime);

    Calendar end = Calendar.getInstance();
    end.setTime(endTime);


    date.set(Calendar.YEAR, begin.get(Calendar.YEAR));
    date.set(Calendar.MONDAY, begin.get(Calendar.MONDAY));
    date.set(Calendar.DAY_OF_MONTH, begin.get(Calendar.DAY_OF_MONTH));


    if (date.after(begin) && date.before(end)) {
      return true;
    } else {
      return false;
    }
  }


  public static String getOrderIdByUUIdNew() {

    long time = System.currentTimeMillis();
    int hashCodeV = UUID.randomUUID().toString().hashCode();
    if (hashCodeV < 0) {//有可能是负数
      hashCodeV = -hashCodeV;
    }
//         0 代表前面补充0     
//         4 代表长度为4     
//         d 代表参数为正数型

    return TimeUtil.getCurrentYmdhmsSSS(time) + String.format("%010d", hashCodeV);
  }


  static SimpleDateFormat bindPanSdf = new SimpleDateFormat("MMddHHmm");

  public static byte[] getMDHMByte() {
    String temp = bindPanSdf.format(new Date());
    byte[] data = ByteUtil.hexStr2bytes(temp);


//        return new byte[]{
//                (byte) (now.get(Calendar.MONTH) + 1),
//                (byte) (now.get(Calendar.DAY_OF_MONTH)),
//                (byte) (now.get(Calendar.HOUR_OF_DAY)),
//                (byte) ((now.get(Calendar.MINUTE)))
//        };


    return data;
  }

} 