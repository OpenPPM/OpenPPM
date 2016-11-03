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
 * File: GetterUtil.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.params;

import java.text.DateFormat;
import java.util.Date;

import es.sm2.openppm.utils.CharPool;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;


public class GetterUtil {

	public static final boolean DEFAULT_BOOLEAN = false;

	public static final boolean[] DEFAULT_BOOLEAN_VALUES = new boolean[0];

	public static final double DEFAULT_DOUBLE = 0.0;

	public static final double[] DEFAULT_DOUBLE_VALUES = new double[0];

	public static final float DEFAULT_FLOAT = 0;

	public static final float[] DEFAULT_FLOAT_VALUES = new float[0];

	public static final int DEFAULT_INTEGER = 0;

	public static final int[] DEFAULT_INTEGER_VALUES = new int[0];

	public static final long DEFAULT_LONG = 0;

	public static final long[] DEFAULT_LONG_VALUES = new long[0];

	public static final short DEFAULT_SHORT = 0;

	public static final short[] DEFAULT_SHORT_VALUES = new short[0];
	
	public static final Character DEFAULT_CHAR = null;

	public static final Character[] DEFAULT_CHAR_VALUES = new Character[0];

	public static final String DEFAULT_STRING = StringPool.BLANK;

	public static String[] BOOLEANS = {"true", "t", "y", "on", "1"};

	public static boolean getBoolean(String value) {
		return getBoolean(value, DEFAULT_BOOLEAN);
	}

	public static boolean getBoolean(String value, boolean defaultValue) {
		return get(value, defaultValue);
	}

	public static boolean[] getBooleanValues(String[] values) {
		return getBooleanValues(values, DEFAULT_BOOLEAN_VALUES);
	}

	public static boolean[] getBooleanValues(
		String[] values, boolean[] defaultValue) {

		if (values == null) {
			return defaultValue;
		}

		boolean[] booleanValues = new boolean[values.length];

		for (int i = 0; i < values.length; i++) {
			booleanValues[i] = getBoolean(values[i]);
		}

		return booleanValues;
	}

	public static Date getDate(String value, DateFormat df) {
		return getDate(value, df, new Date());
	}

	public static Date getDate(String value, DateFormat df, Date defaultValue) {
		return get(value, df, defaultValue);
	}

	public static double getDouble(String value) {
		return getDouble(value, DEFAULT_DOUBLE);
	}

	public static Double getDouble(String value, Double defaultValue) {
		return get(value, defaultValue);
	}

	public static double[] getDoubleValues(String[] values) {
		return getDoubleValues(values, DEFAULT_DOUBLE_VALUES);
	}

	public static double[] getDoubleValues(
		String[] values, double[] defaultValue) {

		if (values == null) {
			return defaultValue;
		}

		double[] doubleValues = new double[values.length];

		for (int i = 0; i < values.length; i++) {
			doubleValues[i] = getDouble(values[i]);
		}

		return doubleValues;
	}

	public static float getFloat(String value) {
		return getFloat(value, DEFAULT_FLOAT);
	}

	public static float getFloat(String value, float defaultValue) {
		return get(value, defaultValue);
	}

	public static float[] getFloatValues(String[] values) {
		return getFloatValues(values, DEFAULT_FLOAT_VALUES);
	}

	public static float[] getFloatValues(
		String[] values, float[] defaultValue) {

		if (values == null) {
			return defaultValue;
		}

		float[] floatValues = new float[values.length];

		for (int i = 0; i < values.length; i++) {
			floatValues[i] = getFloat(values[i]);
		}

		return floatValues;
	}

	public static int getInteger(String value) {
		return getInteger(value, DEFAULT_INTEGER);
	}

	public static int getInteger(String value, int defaultValue) {
		return get(value, defaultValue);
	}

	public static int[] getIntegerValues(String[] values) {
		return getIntegerValues(values, DEFAULT_INTEGER_VALUES);
	}

	public static int[] getIntegerValues(String[] values, int[] defaultValue) {
		if (values == null) {
			return defaultValue;
		}

		int[] intValues = new int[values.length];

		for (int i = 0; i < values.length; i++) {
			intValues[i] = getInteger(values[i]);
		}

		return intValues;
	}

	public static Integer[] getIntegerList(String[] values, Integer[] defaultValue) {
		if (values == null) {
			return defaultValue;
		}

		Integer[] intValues = new Integer[values.length];

		for (int i = 0; i < values.length; i++) {
			intValues[i] = getInteger(values[i]);
		}

		return intValues;
	}

	public static long getLong(String value) {
		return getLong(value, DEFAULT_LONG);
	}

	public static long getLong(String value, long defaultValue) {
		return get(value, defaultValue);
	}

	public static long[] getLongValues(String[] values) {
		return getLongValues(values, DEFAULT_LONG_VALUES);
	}

	public static long[] getLongValues(String[] values, long[] defaultValue) {
		if (values == null) {
			return defaultValue;
		}

		long[] longValues = new long[values.length];

		for (int i = 0; i < values.length; i++) {
			longValues[i] = getLong(values[i]);
		}

		return longValues;
	}

	public static short getShort(String value) {
		return getShort(value, DEFAULT_SHORT);
	}

	public static short getShort(String value, short defaultValue) {
		return get(value, defaultValue);
	}

	public static short[] getShortValues(String[] values) {
		return getShortValues(values, DEFAULT_SHORT_VALUES);
	}

	public static short[] getShortValues(
		String[] values, short[] defaultValue) {

		if (values == null) {
			return defaultValue;
		}

		short[] shortValues = new short[values.length];

		for (int i = 0; i < values.length; i++) {
			shortValues[i] = getShort(values[i]);
		}

		return shortValues;
	}

	public static Character get(String value, Character defaultValue) {
		try {
			return value.charAt(0);
		}
		catch (Exception e) {
		}

		return defaultValue;
	}
	
	public static Character getCharacter(String value) {
		return getCharacter(value, DEFAULT_CHAR);
	}

	public static Character getCharacter(String value, Character defaultValue) {
		return get(value, defaultValue);
	}

	public static Character[] getCharValues(String[] values) {
		return getCharValues(values, DEFAULT_CHAR_VALUES);
	}

	public static Character[] getCharValues(
		String[] values, Character[] defaultValue) {

		if (values == null) {
			return defaultValue;
		}

		Character[] shortValues = new Character[values.length];

		for (int i = 0; i < values.length; i++) {
			shortValues[i] = getCharacter(values[i]);
		}

		return shortValues;
	}

	public static String getString(String value) {
		return getString(value, DEFAULT_STRING);
	}

	public static String getString(String value, String defaultValue) {
		return get(value, defaultValue);
	}
	
	public static String[] getStringValues(
		String[] values, String[] defaultValue) {

		if (values == null) {
			return defaultValue;
		}

		String[] strValues = new String[values.length];

		for (int i = 0; i < values.length; i++) {
			strValues[i] = getString(values[i]);
		}

		return strValues;
	}

	public static double getCurrency(String value) {
		
		try {
			if (value.indexOf(StringPool.COMMA) != -1) {
				value = StringUtil.replace (value, StringPool.POINT, StringPool.BLANK);
				value = value.replace (CharPool.COMMA, CharPool.POINT);
			}
		}
		catch (Exception ex) { ex.printStackTrace(); }
		
		return getDouble(value, DEFAULT_DOUBLE);
	}
	
	
	public static Double getCurrency(String value, Double defaultValue) {
		
		try {
			if (value.indexOf(StringPool.COMMA) != -1) {
				value = StringUtil.replace (value, StringPool.POINT, StringPool.BLANK);
				value = value.replace (CharPool.COMMA, CharPool.POINT);
			}
		}
		catch (Exception ex) {}
		
		return getDouble(value, defaultValue);
	}

	public static boolean get(String value, boolean defaultValue) {
		if (value != null) {
			try {
				value = value.trim();

				if (value.equalsIgnoreCase(BOOLEANS[0]) ||
					value.equalsIgnoreCase(BOOLEANS[1]) ||
					value.equalsIgnoreCase(BOOLEANS[2]) ||
					value.equalsIgnoreCase(BOOLEANS[3]) ||
					value.equalsIgnoreCase(BOOLEANS[4])) {

					return true;
				}
				else {
					return false;
				}
			}
			catch (Exception e) {
			}
		}

		return defaultValue;
	}

	public static Date get(String value, DateFormat df, Date defaultValue) {
		try {
			Date date = df.parse(value.trim());

			if (date != null) {
				return date;
			}
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static Double get(String value, Double defaultValue) {
		try {
			return Double.parseDouble(_trim(value));
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static float get(String value, float defaultValue) {
		try {
			return Float.parseFloat(_trim(value));
		}
		catch (Exception e) {
		}

		return defaultValue;
	}
	
	public static int get(String value, int defaultValue) {
		try {
			return Integer.parseInt(_trim(value));
		}
		catch (Exception e) {
		}
		
		return defaultValue;
	}

	public static Integer get(String value, Integer defaultValue) {
		try {
			return Integer.parseInt(_trim(value));
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static long get(String value, long defaultValue) {
		try {
			return Long.parseLong(_trim(value));
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static short get(String value, short defaultValue) {
		try {
			return Short.parseShort(_trim(value));
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static String get(String value, String defaultValue) {
		if (value != null) {
			value = value.trim();
			value = StringUtil.replace(
				value, StringPool.RETURN_NEW_LINE, StringPool.NEW_LINE);

			return value;
		}

		return defaultValue;
	}

	private static String _trim(String value) {
		if (value != null) {
			value = value.trim();

			StringBuilder sb = new StringBuilder();

			char[] charArray = value.toCharArray();

			for (int i = 0; i < charArray.length; i++) {
				if ((Character.isDigit(charArray[i])) ||
					((charArray[i] == CharPool.DASH) && (i == 0)) ||
					(charArray[i] == CharPool.PERIOD) ||
					(charArray[i] == CharPool.UPPER_CASE_E) ||
					(charArray[i] == CharPool.LOWER_CASE_E)) {

					sb.append(charArray[i]);
				}
			}

			value = sb.toString();
		}

		return value;
	}
}
