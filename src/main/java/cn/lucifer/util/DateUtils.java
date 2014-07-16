package cn.lucifer.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间工具类
 * 
 * @author Lucifer
 * 
 */
public class DateUtils {

	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	static DateFormat dateFormatNoYear = new SimpleDateFormat("MM-dd");
	static DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static DateFormat hourFormat = new SimpleDateFormat("HH:mm");

	/**
	 * 获取最后时间
	 * 
	 * @param date
	 * @return
	 */
	public static String parseLastTime(Date date) {
		String result = null;

		long times = date.getTime() / 1000;
		long now = System.currentTimeMillis() / 1000;
		long tn = now - times;
		if (tn < 60) {
			result = "刚刚";
		} else if (tn <= 3600) {
			result = tn / 60 + "分钟前";
		} else if (tn <= 86400) {
			result = tn / 3600 + "小时前";
		} else if (tn <= 172800) {
			result = "昨天 " + hourFormat.format(date);
		} else if (tn <= 259200) {
			result = "前天 " + hourFormat.format(date);
		} else {
			result = timeFormat.format(date);
		}

		return result;
	}

	/**
	 * 解释yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	public static Date getTime(String time) {
		time = org.apache.commons.lang.StringUtils.trimToNull(time);
		if (time == null) {
			return null;
		}
		try {
			return timeFormat.parse(time);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 返回标准日期格式
	 * 
	 * @param date
	 * @return format: yyyy-MM-dd
	 */
	public static String getString(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * 返回省略了年份的日期格式
	 * 
	 * @param date
	 * @return format: MM-dd
	 */
	public static String getStringNoYear(Date date) {
		return dateFormatNoYear.format(date);
	}

	/**
	 * 获取当前日期, 省略时分秒
	 * 
	 * @return
	 */
	public static Date getNoTimeDate() {
		return getNoTimeDate(null);
	}

	private static final int zero = 0;

	/**
	 * 处理掉时分秒(毫秒)
	 * 
	 * @param time
	 * @return
	 */
	public static Date getNoTimeDate(Date time) {
		Calendar calendar = Calendar.getInstance();
		if (time != null) {
			calendar.setTime(time);
		}
		calendar.set(Calendar.HOUR_OF_DAY, zero);
		calendar.set(Calendar.MINUTE, zero);
		calendar.set(Calendar.SECOND, zero);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取最后时间
	 * 
	 * @param date
	 * @return 就是获取该天最后时间
	 */
	public static Date getLastTime(Date date) {
		date = getNoTimeDate(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}

	/**
	 * 获取减七天的时间
	 * 
	 * @return
	 */
	public static Date getSub7Date() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		return calendar.getTime();
	}

	/**
	 * 获取加七天的时间
	 * 
	 * @return
	 */
	public static Date getAdd7Date() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 7);
		return calendar.getTime();
	}

	/**
	 * 获取指定日期加七天的时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getAdd7Date(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 7);
		return calendar.getTime();
	}

	/**
	 * 获取某月的天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysOfMonth(String year, String month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得某月的的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1);// 日，设为一号
		c.add(Calendar.MONTH, 1);// 月份加一，得到下个月的一号
		c.add(Calendar.DATE, -1);// 下一个月减一为本月最后一天
		return c.getTime();// 获得月末是几号
	}

	/**
	 * 取得某天所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		return c.getTime();
	}

	/**
	 * 取得某天所在周的最后第二天(周六的)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastTwoDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 5);
		return c.getTime();
	}

	/**
	 * 取得某天所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
		return c.getTime();
	}

	public static String getDayOfWeek(Date date) {
		String result = "周";
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			result += "日";
			break;
		case 2:
			result += "一";
			break;
		case 3:
			result += "二";
			break;
		case 4:
			result += "三";
			break;
		case 5:
			result += "四";
			break;
		case 6:
			result += "五";
			break;
		case 7:
			result += "六";
			break;
		}
		return result;
	}

	/**
	 * 对当前日期进行修正
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, day);
		return c.getTime();
	}

	/**
	 * 对当前时间进行修正
	 * 
	 * @param date
	 * @param hour
	 * @param min
	 * @return
	 */
	public static Date addTime(Date date, int hour, int min) {
		return addTime(date, hour, min, 0);
	}

	/**
	 * 对当前时间进行修正
	 * 
	 * @param date
	 * @param hour
	 * @param min
	 * @param second
	 * @return
	 */
	public static Date addTime(Date date, int hour, int min, int second) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, hour);
		c.add(Calendar.MINUTE, min);
		c.add(Calendar.SECOND, second);
		return c.getTime();
	}

}
