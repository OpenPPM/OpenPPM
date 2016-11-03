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
 * File: ValidateUtil.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.functions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import org.apache.log4j.Logger;

import es.sm2.openppm.utils.DateUtil;


public class ValidateUtil {

	public final static Logger LOGGER		= Logger.getLogger(ValidateUtil.class);
	public static Locale DEF_LOCALE_NUMBER 	= new Locale("es", "ES");
	
	/**
	 * Singletom class
	 */
	private ValidateUtil() {}
	
	/**
	 * Format in percent value
	 * @param doubleValue
	 * @return
	 */
	public static String toPercent(Double doubleValue) {
		
		DecimalFormat df = new DecimalFormat("###.##%");
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		
		String formatDouble		 = "";
		
		if (doubleValue != null) {
			formatDouble = df.format(doubleValue);
		}
		return formatDouble;
	}
	
	/**
	 * Value is null, equals empty or "null" return True
	 * @param value
	 * @return
	 */
	public static boolean isNull(String value) {

		boolean isNull = true;
		
		value = (value==null?value:value.trim());
		
		if (value != null && !"".equals(value)
				&& !"NULL".equals(value.toUpperCase())) { isNull = false; }
		
		return isNull;
	}
	
	/**
	 * String is not null and not empty
	 * @param value
	 * @return
	 */
	public static boolean isNotNull(String value) {
		return !isNull(value);
	}
	
	/**
	 * List is not empty
	 * @param list
	 * @return
	 */
	public static boolean isNotNull(Collection<?> list) {
		
		return !isNull(list);
	}
	
	/**
	 * List is empty
	 * @param list
	 * @return
	 */
	public static <T> boolean isNull(Collection<?> list) {
		
		return list == null || list.isEmpty();
	}
	
	/**
	 * Array is not empty
	 * @param array
	 * @return
	 */
	public static <T> boolean isNotNull(T array[]) {
		
		return !isNull(array);
	}
	
	/**
	 * Array is empty
	 * @param array
	 * @return
	 */
	public static <T> boolean isNull(T array[]) {
		
		return array == null || array.length == 0;
	}
	
	/**
	 * Is not null return first value otherwise second value
	 * @param value
	 * @param other
	 * @return
	 */
	public static String isNullCh(String value, String other) {
		
		return (isNull(value)?other:value);
	}
	
	/**
	 * Default false
	 * @param value
	 * @param other
	 * @return
	 */
	public static boolean defBoolean(Boolean value) {
		
		return (value==null?false:value);
	}
	
	/**
	 * Parse double value to currency format (default Locale("es", "ES"))
	 * @param objectValue
	 * @return
	 */
	public static String toCurrency(Object objectValue) {
		
		return toCurrency(objectValue, DEF_LOCALE_NUMBER);
	}
	
	/**
	 * Parse double value to currency format
	 * @param objectValue
	 * @return
	 */
	public static String toCurrency(Object objectValue, Locale locale) {
		
		String value ="";
		double doubleValue = 0;
		
		if (objectValue instanceof Double) {
			doubleValue = (Double)objectValue;
		}
		else if (objectValue instanceof String) {
			doubleValue = Double.parseDouble((String)objectValue);
		}
		
		if (objectValue == null) {
			value = "";
		}
		else {
			NumberFormat numberFormat 	= NumberFormat.getNumberInstance(locale);
			numberFormat.setMinimumFractionDigits(2);
			numberFormat.setMaximumFractionDigits(2);
			
			value = numberFormat.format(doubleValue);
		}
		
		return value;
	}
	
	/**
	 * Parse double value to currency format (default Locale("es", "ES"))
	 * @param doubleValue
	 * @return
	 */
	public static String toCurrencyWord(Double doubleValue) {
	
		return toCurrencyWord(doubleValue, DEF_LOCALE_NUMBER);
	}
	
	/**
	 * Parse double value to currency format
	 * @param doubleValue
	 * @param locale
	 * @return
	 */
	public static String toCurrencyWord(Double doubleValue, Locale locale) {
		
		String value;
		NumberFormat numberFormat 	= NumberFormat.getNumberInstance(locale);
		numberFormat.setMinimumFractionDigits(0);
		numberFormat.setMaximumFractionDigits(0);
		
		if (doubleValue == null) {
			value = numberFormat.format(new Double(0));
		}
		else {
			value = numberFormat.format(doubleValue);
		}
		
		Currency currency = Currency.getInstance(locale);
		
		return value + " "+currency.getSymbol();
	}
	
	/**
	 * Count work days from initDate to endDate.
	 * @param initDate
	 * @param endDate
	 * @return
	 */
	public static Integer calculateWorkDays(Date initDate, Date endDate, List<Date> exceptions) {
		
		Integer cont = null;
		
		if (initDate != null && endDate != null) {
			
			Calendar fecIni = DateUtil.getCalendar();
			fecIni.setTime(initDate);
			Calendar fecFin = DateUtil.getCalendar();
			fecFin.setTime(endDate);
			fecFin.add(Calendar.DAY_OF_MONTH, 1);
			
			cont = 0;
			
			while (fecIni.before(fecFin)) {
				int diaSemIni = fecIni.get(Calendar.DAY_OF_WEEK);
				
				if (diaSemIni != Calendar.SATURDAY && diaSemIni != Calendar.SUNDAY && !exceptions.contains(fecIni.getTime())) {
					cont ++;
				}

				fecIni.add(Calendar.DAY_OF_MONTH, 1);
			}
//			return (cont == 0?1:cont);
			return cont;
		}
		return cont;
	}
	
	/**
	 * Remove decimals
	 * @param value
	 * @return
	 */
	public static String toNumber(Double value) {
		
		return toNumber(value, null);
	}
	
	/**
	 * Remove decimals
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static String toNumber(Double value, Double defaultValue) {
		
		String number = "";
		
		if (value == null) { value = defaultValue; }
		
		if (value != null) {
			NumberFormat nf =  NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(0);
			
			number = nf.format(value);
		}
		
		return number;
	}

    /**
     * Compare two string in lower case if sequence is in data
     *
     * @param data
     * @param sequence
     * @return
     */
    public static boolean containsIgnoreCase(String data, String sequence) {

        return data.toLowerCase().contains(sequence.toLowerCase());
    }
}
