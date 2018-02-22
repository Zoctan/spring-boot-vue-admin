package com.zoctan.api.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */
public class DateUtil {
    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Timestamp getNowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        final Date currentTime = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String dateString = formatter.format(currentTime);
        final ParsePosition pos = new ParsePosition(8);
        return formatter.parse(dateString, pos);
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间格式 yyyy-MM-dd
     */
    public static Date getNowDateShort() {
        final Date currentTime = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String dateString = formatter.format(currentTime);
        final ParsePosition pos = new ParsePosition(8);
        return formatter.parse(dateString, pos);
    }

    /**
     * 获取现在时间
     *
     * @return 返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        final Date currentTime = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(currentTime);
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        final Date currentTime = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(currentTime);
    }

    /**
     * 获取时间 HH:mm:ss
     *
     * @retur
     */
    public static String getTimeShort() {
        final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        final Date currentTime = new Date();
        return formatter.format(currentTime);
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(final String strDate) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(final java.util.Date dateDate) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(dateDate);
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(final java.util.Date dateDate) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(dateDate);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(final String strDate) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }

    /**
     * 得到现在时间
     *
     * @return
     */
    public static Date getNow() {
        return new Date();
    }

    /**
     * 提取一个月中的最后一天
     *
     * @param day
     * @return
     */
    public static Date getLastDate(final long day) {
        final Date date = new Date();
        final long date_3_hm = date.getTime() - 3600000 * 34 * day;
        return new Date(date_3_hm);
    }

    /**
     * 得到现在时间
     *
     * @return 字符串 yyyyMMdd HHmmss
     */
    public static String getStringToday() {
        final Date currentTime = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
        return formatter.format(currentTime);
    }

    /**
     * 得到现在小时
     */
    public static String getHour() {
        final Date currentTime = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String dateString = formatter.format(currentTime);
        final String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    /**
     * 得到现在分钟
     *
     * @return
     */
    public static String getTime() {
        final Date currentTime = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String dateString = formatter.format(currentTime);
        final String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
     *
     * @param sformat yyyyMMddhhmmss
     * @return
     */
    public static String getUserDate(final String sformat) {
        final Date currentTime = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        final String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
     */
    public static String getTwoHour(final String st1, final String st2) {
        String[] kk = null;
        String[] jj = null;
        kk = st1.split(":");
        jj = st2.split(":");
        if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0])) {
            return "0";
        } else {
            final double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60;
            final double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60;
            if ((y - u) > 0) {
                return y - u + "";
            } else {
                return "0";
            }
        }
    }

    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(final String sj1, final String sj2) {
        final SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        final long day;
        try {
            final java.util.Date date = myFormatter.parse(sj1);
            final java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (final Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 时间前推或后推分钟,其中JJ表示分钟.
     */
    public static String getPreTime(final String sj1, final String jj) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mydate1 = "";
        try {
            final Date date1 = format.parse(sj1);
            final long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
            date1.setTime(Time * 1000);
            mydate1 = format.format(date1);
        } catch (final Exception e) {
        }
        return mydate1;
    }

    /**
     * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
     */
    public static String getNextDay(final String nowdate, final String delay) {
        try {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            final String mdate;
            final Date d = strToDate(nowdate);
            final long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = format.format(d);
            return mdate;
        } catch (final Exception e) {
            return "";
        }
    }

    /**
     * 判断是否润年
     *
     * @param ddate
     * @return
     */
    public static boolean isLeapYear(final String ddate) {

        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        final Date d = strToDate(ddate);
        final GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        final int year = gc.get(Calendar.YEAR);
        return (year % 400) == 0 || (year % 4) == 0 && (year % 100) != 0;
    }

    /**
     * 返回美国时间格式 26 Apr 2006
     *
     * @param str
     * @return
     */
    public static String getEDate(final String str) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final ParsePosition pos = new ParsePosition(0);
        final Date strtodate = formatter.parse(str, pos);
        final String j = strtodate.toString();
        final String[] k = j.split(" ");
        return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
    }

    /**
     * 获取一个月的最后一天
     *
     * @param dat
     * @return
     */
    public static String getEndDateOfMonth(final String dat) {// yyyy-MM-dd
        String str = dat.substring(0, 8);
        final String month = dat.substring(5, 7);
        final int mon = Integer.parseInt(month);
        if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
            str += "31";
        } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
            str += "30";
        } else {
            if (isLeapYear(dat)) {
                str += "29";
            } else {
                str += "28";
            }
        }
        return str;
    }

    /**
     * 判断二个时间是否在同一个周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekDates(final Date date1, final Date date2) {
        final Calendar cal1 = Calendar.getInstance();
        final Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        final int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 产生周序列,即得到当前时间所在的年度是第几周
     *
     * @return
     */
    public static String getSeqWeek() {
        final Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1) {
            week = "0" + week;
        }
        final String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }

    /**
     * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
     *
     * @param sdate
     * @param num
     * @return
     */
    public static String getWeek(final String sdate, final String num) {
        // 再转换为时间
        final Date dd = DateUtil.strToDate(sdate);
        final Calendar c = Calendar.getInstance();
        c.setTime(dd);
        if (num.equals("1")) // 返回星期一所在的日期
        {
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else if (num.equals("2")) // 返回星期二所在的日期
        {
            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        } else if (num.equals("3")) // 返回星期三所在的日期
        {
            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        } else if (num.equals("4")) // 返回星期四所在的日期
        {
            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        } else if (num.equals("5")) // 返回星期五所在的日期
        {
            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        } else if (num.equals("6")) // 返回星期六所在的日期
        {
            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        } else if (num.equals("0")) // 返回星期日所在的日期
        {
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(final String sdate) {
        // 再转换为时间
        final Date date = DateUtil.strToDate(sdate);
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    public static String getWeekStr(final String sdate) {
        String str;
        str = DateUtil.getWeek(sdate);
        if ("1".equals(str)) {
            str = "星期日";
        } else if ("2".equals(str)) {
            str = "星期一";
        } else if ("3".equals(str)) {
            str = "星期二";
        } else if ("4".equals(str)) {
            str = "星期三";
        } else if ("5".equals(str)) {
            str = "星期四";
        } else if ("6".equals(str)) {
            str = "星期五";
        } else if ("7".equals(str)) {
            str = "星期六";
        }
        return str;
    }

    /**
     * 两个时间之间的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(final String date1, final String date2) {
        if (date1 == null || date1.equals("")) {
            return 0;
        }
        if (date2 == null || date2.equals("")) {
            return 0;
        }
        // 转换为标准时间
        final SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        final java.util.Date date;
        final java.util.Date mydate;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
            return (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return Long.MIN_VALUE;
    }

    /**
     * 形成如下的日历 ， 根据传入的一个时间返回一个结构 星期日 星期一 星期二 星期三 星期四 星期五 星期六 下面是当月的各个时间
     * 此函数返回该日历第一行星期日所在的日期
     *
     * @param sdate
     * @return
     */
    public static String getNowMonth(String sdate) {
        // 取该时间所在月的一号
        sdate = sdate.substring(0, 8) + "01";

        // 得到这个月的1号是星期几
        final Date date = DateUtil.strToDate(sdate);
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        final int u = c.get(Calendar.DAY_OF_WEEK);
        return DateUtil.getNextDay(sdate, (1 - u) + "");
    }

    /**
     * 取得数据库主键 生成格式为yyyymmddhhmmss+k位随机数
     *
     * @param k 表示是取几位随机数，可以自己定
     */

    public static String getNo(final int k) {
        return getUserDate("yyyyMMddhhmmss") + getRandom(k);
    }

    /**
     * 返回一个随机数
     *
     * @param i
     * @return
     */
    public static String getRandom(final int i) {
        final Random jjj = new Random();
        // int suiJiShu = jjj.nextInt(9);
        if (i == 0) {
            return "";
        }
        final StringBuilder jj = new StringBuilder();
        for (int k = 0; k < i; k++) {
            jj.append(jjj.nextInt(9));
        }
        return jj.toString();
    }

    public static boolean vaildeDate(final String date) {
        final SimpleDateFormat sdf;
        if (date == null) {
            return false;
        }
        if (date.length() > 10) {
            sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        try {
            sdf.parse(date);
            return true;
        } catch (final ParseException pe) {
            return false;
        }
    }
}