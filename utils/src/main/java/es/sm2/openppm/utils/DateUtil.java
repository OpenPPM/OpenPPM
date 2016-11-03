/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: utils
 * File: DateUtil.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public final class DateUtil {

	public static Locale DEF_LOCALE = new Locale("en", "US");
	
	private DateUtil() {
		super();
	}
	
	/**
	 * Get Default Locale Calendar (default: Locale("en", "US"))
	 * @return
	 */
	public static Calendar getCalendar() {
		return getCalendar(DEF_LOCALE);
	}
	
	/**
	 * Get Default Locale Calendar
	 * @param locale
	 * @return
	 */
	public static Calendar getCalendar(Locale locale) {
		Calendar cal = Calendar.getInstance(locale);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		
		return cal;
	}
	
	@Deprecated
	public static String format(ResourceBundle idioma, Date date) {
		String str = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(getDatePattern(idioma));
			str = df.format(date);
		}
		return str;
	}
	
	
	public static String format(String pattern, Date date) {
		String str = "";
		if (pattern != null && date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			str = df.format(date);
		}
		return str;
	}

	
	/**
	 * Return the first day of a given week date
	 * @param date
	 * @return
	 */
	public static Date getFirstWeekDay(Date date) {
		Calendar cal = DateUtil.getCalendar();
		cal.setTime(date);
		
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		return cal.getTime();
	}
	
	
	/**
	 * Return the last day of a given week date
	 * @param date
	 * @return
	 */
	public static Date getLastWeekDay(Date date) {
		Calendar cal = DateUtil.getCalendar();
		cal.setTime(date);
		
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return cal.getTime();
	}
	
	
	/**
	 * Return the last day of a given week date
	 * @param date
	 * @return
	 */
	public static Calendar getLastWeekDay(Calendar calendar) {
		Calendar cal = (Calendar)calendar.clone();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return cal;
	}
	
	
	/**
	 * Return the last day of month
	 * @param date
	 * @return
	 */
	public static Date getLastMonthDay(Date date) {
		Calendar cal = DateUtil.getCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	
	/**
	 * Return the first day of month
	 * @param date
	 * @return
	 */
	public static Date getFirstMonthDay(Date date) {
		Calendar cal = DateUtil.getCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Return the first day of month
	 * @param year 
	 * @return
	 */
	public static Calendar getFirstYearDay(int year) {
		Calendar cal = DateUtil.getCalendar();
		cal.setTime(new Date());
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.YEAR, year);
		return cal;
	}
	
	/**
	 * Return the last day of month
	 * @param year 
	 * @return
	 */
	public static Calendar getLastYearDay(int year) {
		Calendar cal = DateUtil.getCalendar();
		cal.setTime(new Date());
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal;
	}
	
	
	/**
	 * Return Date Pattern for given idioma
	 * @param idioma
	 * @return
	 */
	@Deprecated
	public static String getDatePattern(ResourceBundle idioma) {

		return "dd/MM/yyyy";
	}

	
	/**
	 * Return Date Picker Pattern for given idioma
	 * @param idioma
	 * @return
	 */
	@Deprecated
	public static String getDatePickerPattern(ResourceBundle idioma) {

		return "dd/mm/yy";
	}

	
	/**
	 * Return months between two dates
	 * @param firstDate
	 * @param secondDate
	 * @return
	 */
	public static Integer monthsBetween(Date firstDate, Date secondDate) {
		Calendar first = DateUtil.getCalendar();
		first.setTime(firstDate);
		Calendar second = DateUtil.getCalendar();
		second.setTime(secondDate);
		
		Integer months = 0;
		
		while (first.before(second)) {
			months ++;
			first.add(Calendar.MONTH, 1);
		}
		
		return months;
	}
	
	/**
	 * Return days between two dates
	 * @param firstDate
	 * @param secondDate
	 * @return
	 */
	public static int daysBetween(Date firstDate, Date secondDate) {
		Calendar first = DateUtil.getCalendar();
		first.setTime(firstDate);
		Calendar second = DateUtil.getCalendar();
		second.setTime(secondDate);
		
		int days = 0;
		
		if (first.before(second)) {
			while (first.before(second)) {
				days ++;
				first.add(Calendar.DAY_OF_WEEK, 1);
			}
		}
		else {
			while (second.before(first)) {
				days ++;
				second.add(Calendar.DAY_OF_WEEK, 1);
			}
			days = days * -1;
		}
		
		return days;
	}

	/**
	 * Equals two dates, compare day to day, month to month and year to year
	 * @param calendar1
	 * @param calendar2
	 * @return
	 */
	public static boolean equals(Date date1, Date date2) {
		
		Calendar calendar1 = getCalendar();
		calendar1.setTime(date1);
		
		Calendar calendar2 = getCalendar();
		calendar2.setTime(date2);
		
		return equals(calendar1, calendar2);
	}
	
	/**
	 * Equals two calendar, compare day to day, month to month and year to year
	 * @param calendar1
	 * @param calendar2
	 * @return
	 */
	public static boolean equals(Calendar calendar1, Calendar calendar2) {
		
		int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
		int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
		
		int month1 = calendar1.get(Calendar.MONTH);
		int month2 = calendar2.get(Calendar.MONTH);
		
		int year1 = calendar1.get(Calendar.YEAR);
		int year2 = calendar2.get(Calendar.YEAR);
		
		return (day1 == day2 && month1 == month2 && year1 == year2);
	}

	public static boolean betweenWeek(Date since, Date until, Date day) {
		
		Date initWeek	= getFirstWeekDay(day);
		Date endWeek	= getLastWeekDay(day);
		
		return between(since, until, initWeek, endWeek);
	}
	
	public static boolean between(Date since, Date until, Date dateIn, Date dateOut) {
		
		return between(dateIn, dateOut, since) || between(dateIn, dateOut, until) || (since.before(dateIn) && until.after(dateOut));
	}
	
	public static boolean between(Date since, Date until, Date date) {
		
		return date != null && (!since.after(date) && !until.before(date));
	}

	public static boolean isWeekend(Calendar dateCal) {
		
		return (dateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || dateCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}

    /**
     * Return date list dates by since and until
     *
     * @param since
     * @param until
     * @return
     */
    public static List<Date> lisDates(Date since, Date until) {

        List<Date> listDates = new ArrayList<Date>();

        Calendar sinceCal		= DateUtil.getCalendar();
        Calendar untilCal		= DateUtil.getCalendar();

        sinceCal.setTime(since);
        untilCal.setTime(until);

        while (!sinceCal.after(untilCal)) {

            listDates.add(sinceCal.getTime());

            sinceCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return listDates;
    }
}
