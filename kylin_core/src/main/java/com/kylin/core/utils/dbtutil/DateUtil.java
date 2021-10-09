package com.kylin.core.utils.dbtutil;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期相关工具类
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    // 缺省的日期显示格式： yyyyMMdd
    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

    // 缺省的日期时间显示格式：yyyyMMdd HH:mm:ss
    public static final String DEFAULT_DATETIME_FORMAT = "yyyyMMdd HH:mm:ss";

    // 精简日期格式
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * 私有化构造函数
     */
    private DateUtil() {

    }

    /**
     * 格式化日期
     *
     * @param source 被格式化的日期
     * @param format 日期格式,默认：yyyyMMddhhmmss
     * @return
     */
    public static String formatDate(Date source, String format) {

        String result = null;
        if (CheckUtil.isBlankOrNull(format)) {
            format = YYYYMMDDHHMMSS;
        }
        if (source != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(source);
            } catch (Exception e) {
                Log.e("DateUtil.formatDate", "格式化日期错误" + format);
            }
        }
        return result;
    }

    /**
     * 获取source所属月的第一天
     *
     * @param source 日期
     * @param format 日期格式
     * @return
     */
    public static String getMonthBegin(Date source, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(source);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return formatDate(calendar.getTime(), format);
    }

    /**
     * 获取source所属月的最后一天
     *
     * @param source 日期
     * @param format 日期格式
     * @return
     */
    public static String getMonthEnd(Date source, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(source);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return formatDate(calendar.getTime(), format);
    }

    /**
     * 获取source所在周的最后一天
     *
     * @param source 日期
     * @param format 日期格式
     * @return
     */
    public static String getWeekBegin(Date source, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(source);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return formatDate(calendar.getTime(), format);
    }

    /**
     * 获取source所在周的最后一天
     *
     * @param source 日期
     * @param format 日期格式
     * @return
     */
    public static String getWeekEnd(Date source, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(source);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return formatDate(calendar.getTime(), format);
    }

    /**
     * 根据数据库服务器时间得到当前日历对象
     *
     * @return 当前日期对应的日历对象
     */
    public static Date getNow() {
        new DateUtil();
        return DateUtil.getDateTimeDte(1);
    }

    /**
     * 得到系统当前日期时间
     *
     * @return 当前日期时间
     */
    public static String getNowStr() {
        new DateUtil();
        return DateUtil.getDateTimeStr(1);
    }

    /**
     * 得到用指定方式格式化的系统日期
     *
     * @param iType 返回日期格式
     *              ====================================================
     *              Type       格式
     *              0        20110411(yyyyMMdd)
     *              1        20110411 22:52:08(yyyyMMdd hh:mm:ss)
     *              ====================================================
     * @return 解析后的日期
     */
    public static Date getDateTimeDte(int iType) {
        Date date = null;

        // 格式化标准
        String strPattern = DEFAULT_DATE_FORMAT;
        if (iType == 0)
            strPattern = DEFAULT_DATE_FORMAT;
        if (iType == 1)
            strPattern = DEFAULT_DATETIME_FORMAT;

        // 格式化
        try {
            SimpleDateFormat datetmp = new SimpleDateFormat("yyyyMMdd");//设置日期格式
            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");//设置日期格式
            Object[] obj = new Object[]{datetmp.format(new Date()), time.format(new Date())};//atabasetool.getDBNowTime();
            String strDate = obj[0].toString();
            if (iType == 0)
                strDate = obj[0].
                        toString().replace("-", "").trim();
            if (iType == 1)
                strDate = obj[0].toString().
                        replace("-", "").trim() + " " + obj[1].toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat(strPattern);

            // strDate 数据库时间
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 得到用指定方式格式化的系统日期
     *
     * @param iType 返回日期格式
     *              ====================================================
     *              Type       格式
     *              0        20110411(yyyyMMdd)
     *              1        20110411 22:52:08(yyyyMMdd hh:mm:ss)
     *              2		20110411 225208(yyyyMMdd hhmmss)
     *              3        2011.04.11 22:52:08(yyyy.MM.dd hh:mm:ss)
     *              4        2011.04.11(yyyy.MM.dd)
     *              5        22:52:08(hh:mm:ss)
     *              6		2011-04-11  22:52:08(yyyy-MM-dd  hh:mm:ss)
     *              7		2011-04-11 (yyyy-MM-dd)
     *              8		2011-04-11 22:52:08(yyyy-MM-dd HH:mm:ss)
     *              9		20110411225208(yyyyMMddhhmmss)
     *              10		22:52(hh:mm)
     *              11		12月25日 10:50:34
     *              12		HH
     *              ====================================================
     * @return 日期时间字符串
     */
    public static String getDateTimeStr(int iType) {
        String strResult = "";

        // 实例化数据库交互接口
        /*IDatabaseToolBS atabasetool = 
                    (IDatabaseToolBS)new DateTools().getBean("IDatabaseToolBS");*/

        // 取得数据库时间
        try {
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");//设置日期格式
            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");//设置日期格式
            SimpleDateFormat date1 = new SimpleDateFormat("yyyy.MM.dd");//设置日期格式
            SimpleDateFormat date2 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            SimpleDateFormat date3 = new SimpleDateFormat("MM月dd日");//设置日期格式
            Object[] obj = new Object[]{date.format(new Date()), time.format(new Date()), date1.format(new Date()), date2.format(new Date()), date3.format(new Date())};//atabasetool.getDBNowTime();
            if (iType == 0)
                strResult = obj[0].toString().replaceAll("-", "").trim();
            if (iType == 1)
                strResult = obj[0].toString().replaceAll("-", "").trim() + " " + obj[1].toString();
            if (iType == 2)
                strResult = obj[0].toString().replaceAll("-", "").trim() + obj[1].toString().replaceAll(":", "");
            if (iType == 3)
                strResult = obj[2].toString() + "  " + obj[1].toString();
            if (iType == 4)
                strResult = obj[2].toString();
            if (iType == 5)
                strResult = obj[1].toString();
            if (iType == 6)
                strResult = obj[3].toString() + "  " + obj[1].toString();// yyyy-MM-dd HH:mm:ss
            if (iType == 7)
                strResult = obj[3].toString();
            if (iType == 8)
                strResult = obj[3].toString() + " " + obj[1].toString();// yyyy-MM-dd HH:mm:ss
            if (iType == 9)
                strResult = obj[0].toString().replaceAll("-", "").trim() + obj[1].toString().replaceAll(":", "").trim();
            if (iType == 10)
                strResult = obj[1].toString().substring(0, 5);
            if (iType == 11)
                strResult = obj[4].toString() + " " + obj[1].toString();// yyyy-MM-dd HH:mm:ss
            if (iType == 12)
                strResult = obj[1].toString().substring(0,2);// HH

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return strResult;
    }

    /**
     * 得到当前年份
     *
     * @return 当前年份
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static String getStrNowYear() {
        return String.valueOf(getCurrentYear());
    }

    /**
     * 得到当前月份
     *
     * @return 当前月份
     */
    public static int getCurrentMonth() {
        //用get得到的月份数比实际的小1，需要加上
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 得到月的周份
     *
     * @param type 0:数字、1:汉字，如第一周
     * @return
     */
    public static String getCurrentWeekOfMonth(int type) {

        return getCurrentWeekOfMonth(new Date(), type);
    }

    public static String getCurrentWeekOfMonth(Date date, int type) {

        String currWeek = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        if (type == 1) {
            switch (week) {
                case 1:
                    currWeek = "第一周";
                    break;

                case 2:
                    currWeek = "第二周";
                    break;

                case 3:
                    currWeek = "第三周";
                    break;

                case 4:
                    currWeek = "第四周";
                    break;

                case 5:
                    currWeek = "第五周";
                    break;

                case 6:
                    currWeek = "第六周";
                    break;

                default:
                    break;
            }

        } else {
            currWeek = String.valueOf(week);
        }

        return currWeek;
    }

    /**
     * 得到当前日
     *
     * @return 当前日
     */
    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
     *
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(int days) {
        return add(getNow(), days, Calendar.DATE);
    }

    // add by duanzengfu 2012/08/17 start

    /**
     * 当前系统日期加天数
     *
     * @param days 增加的日期数
     * @return 增加以后的日期字符串
     */
    public static String addDaysToStr(int days, String pattern) {

        // 缺省为“yyyyMMdd”
        if (null == pattern || "".equals(pattern)) {
            pattern = "yyyyMMdd";
        }
        Date date = null;
        String strDate = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            date = add(getNow(), days, Calendar.DATE);
            strDate = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * 当前系统日期加天数
     *
     * @param days 增加的日期数
     * @return 增加以后的日期字符串
     */
    public static String addDaysToStr(int days) {
        return addDaysToStr(days, "yyyyMMdd");
    }
    // add by duanzengfu 2012/08/17 end

    /**
     * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
     *
     * @param date 基准日期
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(Date date, int days) {
        return add(date, days, Calendar.DATE);
    }

    /**
     * 取得指定日期以后若干秒的日期。如果要得到以前的日期，参数用负数。
     *
     * @param date   基准日期
     * @param second 增加的日期数
     * @return 增加以后的日期
     */
    public static Date addSeconds(Date date, int second) {
        return add(date, second, Calendar.SECOND);
    }

    /**
     * 取得指定日期以后若干分钟的日期。如果要得到以前的日期，参数用负数。
     *
     * @param date    基准日期
     * @param minutes 增加的分钟数
     * @return 增加以后的日期
     */
    public static Date addMinutes(Date date, int minutes) {
        return add(date, minutes, Calendar.MINUTE);
    }

    /**
     * 取得指定日期以后若干月的日期。如果要得到以前的日期，参数用负数。
     *
     * @param date   基准日期
     * @param months 增加的月数
     * @return 增加以后的日期
     */
    public static Date addMonths(Date date, int months) {
        return add(date, months, Calendar.MONTH);
    }

    /**
     * 取得指定日期以后若干年的日期。如果要得到以前的日期，参数用负数。
     *
     * @param date  基准日期
     * @param years 增加的年数
     * @return 增加以后的日期
     */
    public static Date addYears(Date date, int years) {
        return add(date, years, Calendar.YEAR);
    }

    /**
     * 内部方法。为指定日期增加相应的天数或月数
     *
     * @param date   基准日期
     * @param amount 增加的数量
     * @param field  增加的单位，年，月或者日
     * @return 增加以后的日期
     */
    private static Date add(Date date, int amount, int field) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(field, amount);

        return calendar.getTime();
    }

    /**
     * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差天数
     */
    public static long diffDays(Date one, Date two) {
        return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差月份数
     */
    public static int diffMonths(Date one, Date two) {

        Calendar calendar = Calendar.getInstance();

        //得到第一个日期的年分和月份数
        calendar.setTime(one);
        int yearOne = calendar.get(Calendar.YEAR);
        int monthOne = calendar.get(Calendar.MONDAY);

        //得到第二个日期的年份和月份
        calendar.setTime(two);
        int yearTwo = calendar.get(Calendar.YEAR);
        int monthTwo = calendar.get(Calendar.MONDAY);

        return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
    }

    /**
     * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差年数
     */
    private static int diffYear(Date one, Date two) {

        Calendar calendar = Calendar.getInstance();

        //得到第一个日期的年分和月份数
        calendar.setTime(one);
        int yearOne = calendar.get(Calendar.YEAR);

        //得到第二个日期的年份和月份
        calendar.setTime(two);
        int yearTwo = calendar.get(Calendar.YEAR);

        return (yearOne - yearTwo);
    }

    /**
     * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数<br>
     * 前后日期格式必须为yyyyMMdd
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差年数
     */
    public static int diffYear(String one, String two) {
        return diffYear(parse(one, ""), parse(two, ""));
    }

    /**
     * 将一个字符串用给定的格式转换为日期类型。 <br>
     * 注意：如果返回null，则表示解析失败
     *
     * @param datestr 需要解析的日期字符串
     * @param pattern 日期字符串的格式，默认为“yyyy-MM-dd”的形式
     * @return 解析后的日期
     */
    public static Date parse(String datestr, String pattern) {
        Date date = null;

        if (!CheckUtil.isBlankOrNull(datestr)) {
            if (null == pattern || "".equals(pattern)) {
                pattern = DEFAULT_DATE_FORMAT;
            }

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                date = dateFormat.parse(datestr);
            } catch (ParseException e) {
                //
            }
        }

        return date;
    }

    /**
     * yyyy-MM-dd HH:mm字符转日期
     *
     * @param datestr 源字符
     * @return 目标日期
     */
    public static Date parse(String datestr) {
        return parse(datestr, "yyyy-MM-dd HH:mm");
    }

    /**
     * 返回本月的最后一天
     *
     * @return 本月最后一天的日期
     */
    public static Date getMonthLastDay() {
        return getMonthLastDay(getNow());
    }

    /**
     * 返回给定日期中的月份中的最后一天
     *
     * @param date 基准日期
     * @return 该月最后一天的日期
     */
    public static Date getMonthLastDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //将日期设置为下一月第一天
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);

        //减去1天，得到的即本月的最后一天
        calendar.add(Calendar.DATE, -1);

        return calendar.getTime();
    }

    /**
     * 判断是否在有效期
     *
     * @param strStartDate
     * @param strEndDate
     * @return 当在有效期时返回True, 否则False
     */
    public static boolean isValidDate(String strStartDate, String strEndDate) {
        boolean bResult = false;
        String strSysDate = getDateTimeStr(0);

        // 开始或结束日期为空表示无此限制
        if (null == strStartDate || "".equals(strStartDate))
            strStartDate = "19700101";
        if (null == strEndDate || "".equals(strEndDate))
            strEndDate = "99999999";

        // 判断日期是否在有效期
        bResult = (strStartDate.compareTo(strSysDate) <= 0
                && strEndDate.compareTo(strSysDate) >= 0);

        // 返回值
        return bResult;
    }


    /**
     * 取得系统时间前后(+ -)月数
     *
     * @param strMonthConunt 月数
     * @return 计算后年月
     */
    public String getAroundDate(String strMonthConunt) {
        String strResult = "";
        if (!CheckUtil.isNumeric(strMonthConunt))
            return strResult;
        // 取得相应年月
        strResult = this.aroundDate(getDateTimeDte(1), strMonthConunt);
        return strResult;
    }

    /**
     * 加/减X个月
     *
     * @param date           日期基数
     * @param strMonthConunt 月数      正数表示加,负数表示减
     * @return 计算后日期
     */
    private String aroundDate(Date date, String strMonthConunt) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();

        // 设置传入时间
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)
                + Integer.valueOf(strMonthConunt));
        return df.format(calendar.getTime());
    }

    /**
     * 加/减X个月
     *
     * @param strDate       日期基数
     * @param strMonthCount 月数      正数表示加,负数表示减
     * @return 计算后日期
     */
    public String getAroundDate(String strDate, String strMonthCount) {

        // 定义日期
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        try {

            // 字符类型转换为日期类型
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 设置传入时间
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)
                + Integer.valueOf(strMonthCount));
        return sdf.format(calendar.getTime());

    }
    // add by daiguoming, 2011/7/18, end

    /**
     * 返回要求格式日期
     *
     * @param iType   类型
     *                ====================================================
     *                Type       格式
     *                0        yyyyMMddHHmmss TO yyyy-MM-dd HH:mm
     *                1        yyyyMMdd TO yyyy-MM-dd
     *                2        yyyyMMdd HH:mm:ss TO yyyy-MM-dd HH
     *                3        yyyyMMdd TO yyyy-MM
     *                4        yyyyMMdd HH:mm TO yyyy-MM-dd HH:mm
     *                5        yyyy-MM-dd  TO yyyy年MM月dd日
     *                6        yyyy-MM-dd HH:mm:ss TO TO yyyy年MM月dd日
     *                7        yyyy-MM-dd HH:mm:ss TO TO HH:mm:ss
     *                ====================================================
     * @param strDate 原值
     * @return 目的值
     */
    public static String formatDate(int iType, String strDate) {
        String strResult = "";
        if (strDate == null || "".equals(strDate.trim()))
            return strResult;

        // 源格式
        SimpleDateFormat oriDateFormat = new SimpleDateFormat();

        // 目的格式
        SimpleDateFormat tarDateFormat = new SimpleDateFormat();

        switch (iType) {
            case 0:
                oriDateFormat.applyPattern("yyyyMMddHHmmss");
                tarDateFormat.applyPattern("yyyy-MM-dd HH:mm");
                break;
            case 1:
                oriDateFormat.applyPattern("yyyyMMdd");
                tarDateFormat.applyPattern("yyyy-MM-dd");
                break;
            case 2:
                oriDateFormat.applyPattern("yyyyMMdd HH:mm:ss");
                tarDateFormat.applyPattern("yyyy-MM-dd HH");
                break;
            case 3:
                oriDateFormat.applyPattern("yyyyMMdd");
                tarDateFormat.applyPattern("yyyy-MM");
                break;
            case 4:
                oriDateFormat.applyPattern("yyyyMMdd HH:mm");
                tarDateFormat.applyPattern("yyyy-MM-dd HH:mm");
                break;
            case 5:
                oriDateFormat.applyPattern("yyyy-MM-dd");
                tarDateFormat.applyPattern("yyyy年MM月dd日");
                break;
            case 6:
                oriDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
                tarDateFormat.applyPattern("yyyy年MM月dd日");
                break;
            case 7:
                oriDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
                tarDateFormat.applyPattern("HH:mm:ss");
                break;
            default:
                oriDateFormat.applyPattern("yyyyMMdd");
                tarDateFormat.applyPattern("yyyy-MM-dd");
        }

        try {

            // 格式转换
            strResult = tarDateFormat.format(oriDateFormat.parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strResult;
    }

    /**
     * 返回要求格式日期
     *
     * @param iType     类型
     * ====================================================
     *   Type       格式
     *     0        yyyyMMddHHmmss TO yyyy-MM-dd HH:mm
     *     1        yyyyMMdd TO yyyy-MM-dd
     *     2        yyyyMMdd HH:mm:ss TO yyyy-MM-dd HH
     *     3        yyyyMMdd TO yyyy-MM
     *     4        yyyyMMdd HH:mm TO yyyy-MM-dd HH:mm
     * ====================================================
     * @param strDate   原值
     * @return 目的值
     */
    //    public static String formatDate(int iType, String strDate) {
    //        String strResult = "";
    //        if (strDate == null || "".equals(strDate.trim())) return strResult;
    //
    //        // 源格式
    //        SimpleDateFormat oriDateFormat = new SimpleDateFormat();
    //
    //        // 目的格式
    //        SimpleDateFormat tarDateFormat = new SimpleDateFormat();
    //
    //
    //        switch (iType) {
    //            case 0:
    //                oriDateFormat.applyPattern("yyyyMMddHHmmss");
    //                tarDateFormat.applyPattern("yyyy-MM-dd HH:mm");
    //                break;
    //            case 1:
    //                oriDateFormat.applyPattern("yyyyMMdd");
    //                tarDateFormat.applyPattern("yyyy-MM-dd");
    //                break;
    //            case 2:
    //                oriDateFormat.applyPattern("yyyyMMdd HH:mm:ss");
    //                tarDateFormat.applyPattern("yyyy-MM-dd HH");
    //                break;
    //            case 3:
    //                oriDateFormat.applyPattern("yyyyMMdd");
    //                tarDateFormat.applyPattern("yyyy-MM");
    //                break;
    //            case 4:
    //                oriDateFormat.applyPattern("yyyyMMdd HH:mm");
    //                tarDateFormat.applyPattern("yyyy-MM-dd HH:mm");
    //                break;
    //            default:
    //                oriDateFormat.applyPattern("yyyyMMdd");
    //                tarDateFormat.applyPattern("yyyy-MM-dd");
    //        }
    //
    //        try {
    //
    //            // 格式转换
    //            strResult = tarDateFormat.format(oriDateFormat.parse(strDate));
    //        } catch (ParseException e) {
    //            e.printStackTrace();
    //        }
    //
    //        return strResult;
    //    }
    //
    //    // add by duanzengfu 2012/09/04 start
    //    /**
    //     * 将yyyymmdd转换为yyyy-mm-dd
    //     *
    //     * @param strDate 日期原值
    //     * @return 转换后的日期字符串
    //     */
    //    public static String parseDateFmt(String strDate) {
    //        return formatDate(1, strDate);
    //    }

    /**
     * 将yyyyMMddHHmmss转换为yyyy-MM-dd HH:mm
     *
     * @param strDate 源值
     * @return 格式化后值
     */
    public static String paresDateLongFmt(String strDate) {
        return formatDate(0, strDate);
    }

    /**
     * 将yyyyMMdd HH:mm转换为yyyy-MM-dd HH:mm
     *
     * @param strDate 源值
     * @return 格式化后值
     */
    public static String paresDateLongToLong(String strDate) {
        return formatDate(4, strDate);
    }

    /**
     * 判断输入值是否为指定格式的合法日期
     *
     * @param strDate 8位日期字符串
     * @return 合法则返回true, 否则返回false
     */
    public Boolean checkDate(String strDate) {

        try {
            // 如果输入日期不是8位的,判定为false.
            if (null == strDate || "".equals(strDate)
                    || !strDate.matches("[0-9]{8}")) {
                return false;
            }
            int year = Integer.parseInt(strDate.substring(0, 4));
            int month = Integer.parseInt(strDate.substring(4, 6)) - 1;
            int day = Integer.parseInt(strDate.substring(6));
            Calendar calendar = Calendar.getInstance();

            // 当 Calendar 处于 non-lenient 模式时，
            // 如果其日历字段中存在任何不一致性，它都会抛出一个异常。
            calendar.setLenient(false);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DATE, day);

            // 如果日期错误,执行该语句,必定抛出异常.
            calendar.get(Calendar.YEAR);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * 根据format格式要求，把date转换为相应的字符串
     *
     * @param format 要求格式
     * @param date   待转换对象
     * @return 按照要求转换后的字符串
     */
    public static String getDateToStr(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 将日期转换为长字符串（包含：年-月-日 时:分）
     *
     * @param date 日期
     * @return 返回型如：yyyy-MM-dd HH:mm 的字符串
     */
    public static String getDateToLStr(Date date) {
        if (null == date)
            return "";
        return getDateToStr("yyyy-MM-dd HH:mm", date);
    }

    /**
     * 将日期转换为字符串（包含：年-月-日 ）
     *
     * @param date 日期
     * @return 返回型如：yyyy-MM-dd的字符串
     */
    public static String getDateToTagStr(Date date) {
        if (null == date)
            return "";
        return getDateToStr("yyyy-MM-dd", date);
    }

    /**
     * 20181223111223 -> 11:12:23
     *
     * @param str
     * @return
     */
    public static String dividetime(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        return str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14);
    }


    /**
     * 20181223111223 -> 2018年12月23日 11:12:23
     *
     * @param str
     * @return
     */
    public static String divideNewtime(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        return str.substring(4, 6) + "月" + str.substring(6, 8) + "日 " + str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14);
    }


    /**
     * 20181223111223 -> 2018-12-23 11:12:23
     *
     * @param str
     * @return
     */
    public static String dividetimetype(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }

        return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8) + " " + str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14);
    }

    /**
     * 20181223111223 -> 2018-12-23 11:12:23
     *
     * @param str
     * @return
     */
    public static String divideMonthtype(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }

        return str.substring(4, 6) + "月" + str.substring(6, 8) + "日 " + str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14);
    }

    /**
     * 20181223111223 -> 2018-12-23
     *
     * @param str
     * @return
     */
    public static String divideHengtime(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        return str.substring(0, 4) + "-" + str.substring(4, 6) + "- " + str.substring(6, 8);
    }


    /**
     * 2018-12-23 -> 23/12
     *
     * @param str
     * @return
     */
    public static String divideXieXianTime(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        return str.substring(8, 10) + "/" + str.substring(5, 7);
    }

    /**
     * 20181223 -> 12.23
     *
     * @param str
     * @return
     */
    public static String divideMonthDay(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        return str.substring(4, 6) + "." + str.substring(6, 8);
    }

    /**
     * 20181223 -> 2018.12
     *
     * @param str
     * @return
     */
    public static String divideYearMonth(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        return str.substring(0, 4) + "." + str.substring(4, 6);
    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param strDate 需要判断的时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static boolean getTimeInDate(String strDate) {
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        String strDate = sdf.format(date);*/

        // 截取当前时间时分
        int strDateH = Integer.parseInt(strDate.substring(11, 13));
        int strDateM = Integer.parseInt(strDate.substring(14, 16));
        String curTime = strDateH + ":" + strDateM;
        System.out.println(curTime);

        String sourceTime = "17:00-24:00";
        boolean inTime = isInTime(sourceTime, curTime);
        System.out.println(inTime);
        return inTime;

    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param strDate 需要判断的时间 yyyy-MM-dd HH:mm:ss
     * @param start   开始时间 08:00       8点
     * @param end     结束时间 24:00       24点
     * @return
     */
    public static boolean getBooleanTimeInDate(String strDate, String start, String end) {
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        String strDate = sdf.format(date);*/

        // 时
        int strDateH = Integer.parseInt(strDate.substring(11, 13));
        // 分
        int strDateM = Integer.parseInt(strDate.substring(14, 16));


        String curTime = strDateH + ":" + strDateM;
        System.out.println(curTime);

        // String sourceTime = "17:00-24:00";
        String sourceTime = start+ "-" + end;
        boolean inTime = isInTime(sourceTime, curTime);
        System.out.println(inTime);
        return inTime;

    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param strDate 需要判断的时间09:12  9点12分
     * @param start   开始时间08:00       8点
     * @param end     结束时间24:00       24点
     * @return
     */
    public static boolean getBooleanWeather(String strDate, String start, String end) {
        // String sourceTime = "17:00-24:00";
        String sourceTime = start + "-" + end;
        boolean inTime = isInTime(sourceTime, strDate);
        System.out.println(inTime);
        return inTime;

    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param curTime    需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            long now = sdf.parse(curTime).getTime();// 当前时间
            long start = sdf.parse(args[0]).getTime();// 区间开始时间
            long end = sdf.parse(args[1]).getTime();// 区间结束时间
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }

    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param curTime    需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInSourceTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            long now = sdf.parse(curTime).getTime();// 当前时间
            long start = sdf.parse(args[0]).getTime();// 区间开始时间
            long end = sdf.parse(args[1]).getTime();// 区间结束时间
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }

    }


    /**
     * 获取本周的第一天日期和最后一天日期（按中国周）
     *
     * @param todayTime :"2017-03-15"
     * @return arr[0] 第一天日期 ；arr[1]最后一天日期
     * @throws ParseException
     */
    public static String[] getWeekStartandEndDate(String todayTime) {
        try {
            String[] arr = new String[2];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(todayTime));
            int d = 0;
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                d = -6;
            } else {
                d = 2 - cal.get(Calendar.DAY_OF_WEEK);
            }
            cal.add(Calendar.DAY_OF_WEEK, d);
            //所在周开始日期
            arr[0] = sdf.format(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, 6);
            //所在周结束日期
            arr[1] = sdf.format(cal.getTime());
            return arr;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + todayTime);
        }
    }

    /**
     * 获取指定月的第一天日期和最后一天日期
     *
     * @param todayTime:"2017-03-15"
     * @return arr[0] 第一天日期 ；arr[1]最后一天日期
     * @throws ParseException
     */
    public static String[] getMonthStartAndEndDate(String todayTime) throws ParseException {
        String[] arr = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(todayTime));
        c.set(Calendar.DAY_OF_MONTH, 1);
        arr[0] = sdf.format(c.getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        arr[1] = sdf.format(c.getTime());
        return arr;

    }

    /**
     * 获取指定年的第一天日期和最后一天日期
     *
     * @param todayTime :"2017-03-15"
     * @return arr[0] 第一天日期 ；arr[1]最后一天日期
     * @throws ParseException
     */
    public static String[] getYearStartAndEndDate(String todayTime) throws ParseException {
        String[] arr = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(todayTime));
        c.set(Calendar.DAY_OF_YEAR, 1);
        arr[0] = sdf.format(c.getTime());
        c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
        arr[1] = sdf.format(c.getTime());
        return arr;

    }

    /**
     * 根据指定周获取开始，结束日期
     *
     * @param week “2017-24”
     * @return
     */
    public static String[] getStartEndByWeek(String week) {
        String[] weekArr = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] arr = week.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(arr[0]));
        c.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(arr[1]));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
        c.add(Calendar.DATE, -dayOfWeek); // 得到本周的第一天
        weekArr[0] = sdf.format(c.getTime());
        c.add(Calendar.DATE, 6); // 得到本周的最后一天
        weekArr[1] = sdf.format(c.getTime());
        return weekArr;
    }


    /**
     * 两个日期相关天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDateDiff(String startDate, String endDate) {
        long diff = 0;
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            diff = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) > 0 ? (date1.getTime() - date2.getTime())
                    / (24 * 60 * 60 * 1000)
                    : (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
        }
        return diff;
    }

    /**
     * 两个日期相关天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDiffDay(String startDate, String endDate) {
        long diff = 0;
        try {
            Date date1 = new SimpleDateFormat("yyyyMMddhhmmss").parse(startDate);
            Date date2 = new SimpleDateFormat("yyyyMMddhhmmss").parse(endDate);
            diff = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) > 0 ? (date1.getTime() - date2.getTime())
                    / (24 * 60 * 60 * 1000)
                    : (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
        }
        return diff;
    }

    /**
     * 两个日期相关天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDiffAgree(String startDate, String endDate) {
        long diff = 0;
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endDate);
            diff = (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
        }
        return diff;
    }

    public static String getWeekName(String day) {
        String weekname = "星期一";
        switch (day) {
            case "1":
                weekname = "星期一";
                break;
            case "2":
                weekname = "星期二";
                break;
            case "3":
                weekname = "星期三";
                break;
            case "4":
                weekname = "星期四";
                break;
            case "5":
                weekname = "星期五";
                break;
            case "6":
                weekname = "星期六";
                break;
            case "7":
                weekname = "星期日";
                break;
        }

        return weekname;
    }




    //获取2个日期间所有的日期
    public static List<String> getDays(String startTime, String endTime) throws Exception {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date start = dateFormat.parse(startTime);
        Date end = dateFormat.parse(endTime);

        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
        while (tempStart.before(tempEnd)) {
            days.add(dateFormat.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }

        return days;
    }

    //获取今天是周几
    public static String getTodayweek(String day) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(day));

        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if(week == 1){
            return "7";
        }else {
            return (week -1) + "";
        }
    }

    //获取今天是周几
    public static String _getTodayweek(String day) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(day));

        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if(week == 1){
            return "周日";
        }else if(week == 2){
            return "周一";
        }else if(week == 3){
            return "周二";
        }else if(week == 4){
            return "周三";
        }else if(week == 5){
            return "周四";
        }else if(week == 6){
            return "周五";
        }else if(week == 7){
            return "周六";
        }else {
            return "";
        }
    }
}