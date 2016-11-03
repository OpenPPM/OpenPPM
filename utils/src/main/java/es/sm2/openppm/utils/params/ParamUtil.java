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
 * File: ParamUtil.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.params;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import es.sm2.openppm.utils.functions.ValidateUtil;


public final class ParamUtil {
	
	private ParamUtil() {
		super();
	}
	
	private static Logger logger = Logger.getLogger(ParamUtil.class);

	public static boolean getBoolean(HttpServletRequest request, String param) {
		return GetterUtil.getBoolean(request.getParameter(param));
	}

	public static boolean getBoolean(
		HttpServletRequest request, String param, boolean defaultValue) {

		return get(request, param, defaultValue);
	}

	public static boolean[] getBooleanValues(
		HttpServletRequest request, String param) {

		return getBooleanValues(request, param, new boolean[0]);
	}

	public static boolean[] getBooleanValues(
		HttpServletRequest request, String param, boolean[] defaultValue) {

		return GetterUtil.getBooleanValues(
			request.getParameterValues(param), defaultValue);
	}

	public static Date getDate(
		HttpServletRequest request, String param, DateFormat df) {

		return GetterUtil.getDate(request.getParameter(param), df);
	}

	public static Date getDate(
		HttpServletRequest request, String param, DateFormat df,
		Date defaultValue) {

		return get(request, param, df, defaultValue);
	}

	public static double getDouble(HttpServletRequest request, String param) {
		return GetterUtil.getDouble(request.getParameter(param));
	}

	public static Double getDouble(
		HttpServletRequest request, String param, Double defaultValue) {

		return get(request, param, defaultValue);
	}

	public static double[] getDoubleValues(
		HttpServletRequest request, String param) {

		return getDoubleValues(request, param, new double[0]);
	}

	public static double[] getDoubleValues(
		HttpServletRequest request, String param, double[] defaultValue) {

		return GetterUtil.getDoubleValues(
			request.getParameterValues(param), defaultValue);
	}

	public static float getFloat(HttpServletRequest request, String param) {
		return GetterUtil.getFloat(request.getParameter(param));
	}

	public static float getFloat(
		HttpServletRequest request, String param, float defaultValue) {

		return get(request, param, defaultValue);
	}

	public static float[] getFloatValues(
		HttpServletRequest request, String param) {

		return getFloatValues(request, param, new float[0]);
	}

	public static float[] getFloatValues(
		HttpServletRequest request, String param, float[] defaultValue) {

		return GetterUtil.getFloatValues(
			request.getParameterValues(param), defaultValue);
	}

	public static int getInteger(HttpServletRequest request, String param) {
		return GetterUtil.getInteger(request.getParameter(param));
	}

	public static int getInteger(
		HttpServletRequest request, String param, int defaultValue) {

		return get(request, param, defaultValue);
	}
	
	public static Integer getInteger(
			HttpServletRequest request, String param, Integer defaultValue) {

			return get(request, param, defaultValue);
		}

	public static int[] getIntegerValues(
		HttpServletRequest request, String param) {

		return getIntegerValues(request, param, new int[0]);
	}

	public static int[] getIntegerValues(
		HttpServletRequest request, String param, int[] defaultValue) {

		return GetterUtil.getIntegerValues(
			request.getParameterValues(param), defaultValue);
	}

	public static long getLong(HttpServletRequest request, String param) {
		return GetterUtil.getLong(request.getParameter(param));
	}

	public static long getLong(
		HttpServletRequest request, String param, long defaultValue) {

		return get(request, param, defaultValue);
	}

	public static long[] getLongValues(
		HttpServletRequest request, String param) {

		return getLongValues(request, param, new long[0]);
	}

	public static long[] getLongValues(
		HttpServletRequest request, String param, long[] defaultValue) {

		return GetterUtil.getLongValues(
			request.getParameterValues(param), defaultValue);
	}

	public static short getShort(HttpServletRequest request, String param) {
		return GetterUtil.getShort(request.getParameter(param));
	}

	public static short getShort(
		HttpServletRequest request, String param, short defaultValue) {

		return get(request, param, defaultValue);
	}

	public static short[] getShortValues(
		HttpServletRequest request, String param) {

		return getShortValues(request, param, new short[0]);
	}

	public static short[] getShortValues(
		HttpServletRequest request, String param, short[] defaultValue) {

		return GetterUtil.getShortValues(
			request.getParameterValues(param), defaultValue);
	}

	public static Character getCharacter(HttpServletRequest req, String param) {
		return GetterUtil.getCharacter(req.getParameter(param));
	}

	public static Character getCharacter(
		HttpServletRequest req, String param, Character defaultValue) {

		return get(req, param, defaultValue);
	}

	public static Character[] getCharValues(
		HttpServletRequest req, String param) {

		return getCharValues(req, param, new Character[0]);
	}

	public static Character[] getCharValues(
		HttpServletRequest req, String param, Character[] defaultValue) {

		return GetterUtil.getCharValues(
			req.getParameterValues(param), defaultValue);
	}
	
	public static double getCurrency (HttpServletRequest req, String param) {
		return GetterUtil.getCurrency(req.getParameter(param));
	}

	public static Double getCurrency(
			HttpServletRequest req, String param, Double defaultValue) {
		
		return GetterUtil.getCurrency(req.getParameter(param), defaultValue);
	}
	
	public static double getCurrency(
		HttpServletRequest req, String param, double defaultValue) {

		return GetterUtil.getCurrency(req.getParameter(param), defaultValue);
	}
	
	public static Character get(
		HttpServletRequest req, String param, Character defaultValue) {

		return GetterUtil.get(req.getParameter(param), defaultValue);
	}
	
	public static String getString(HttpServletRequest req, String param) {
		return GetterUtil.getString(req.getParameter(param));
	}

    /**
     * Create enum from request
     *
     * @param request - data from servlet
     * @param param - name of enum type
     * @param enumClass - Type of enum class
     * @param <T> - type of enum
     * @return - Enum created by name of param and enum type
     */
    public static <T extends Enum<T>> T getEnum(HttpServletRequest request, String param, Class<T> enumClass) {

        return getEnum(request, param, enumClass, null);
    }

    /**
     * Create enum from request
     *
     * @param request - data from servlet
     * @param param - name of enum type
     * @param enumClass - Type of enum class
     * @param defaultValue - default value if enum is null
     * @param <T> - type of enum
     * @return - Enum created by name of param and enum type
     */
    public static <T extends Enum<T>> T getEnum(HttpServletRequest request, String param, Class<T> enumClass, T defaultValue) {

        // Get value string from servlet request
        String value = getString(request, param);

        // Default value for enum
        T enumValue = defaultValue;

        try {

            if (ValidateUtil.isNotNull(value)) {

                // Instance type of enum
                enumValue = Enum.valueOf(enumClass, value);
            }
        }
        catch (IllegalArgumentException e) {

            // If enum constant type not exists
            logger.warn("Parse enum param", e);
        }

        return enumValue;
    }

	public static String getString(
		HttpServletRequest req, String param, String defaultValue) {

		return get(req, param, defaultValue);
	}
	
	public static String[] getStringValues(
		HttpServletRequest request, String param, String[] defaultValue) {

		return GetterUtil.getStringValues(
			request.getParameterValues(param), defaultValue);
	}
	
	public static List<String> getStringList(
			HttpServletRequest request, String param, List<String> defaultValue) {

		String[] items = GetterUtil.getStringValues(request.getParameterValues(param), null);

		if (items != null) {
			return Arrays.asList(items);
		}
		return defaultValue;
	}

	public static boolean get(
		HttpServletRequest request, String param, boolean defaultValue) {

		return GetterUtil.get(request.getParameter(param), defaultValue);
	}

	public static Date get(
		HttpServletRequest request, String param, DateFormat df,
		Date defaultValue) {

		return GetterUtil.get(request.getParameter(param), df, defaultValue);
	}

	public static Double get(
		HttpServletRequest request, String param, Double defaultValue) {

		return GetterUtil.get(request.getParameter(param), defaultValue);
	}

	public static float get(
		HttpServletRequest request, String param, float defaultValue) {

		return GetterUtil.get(request.getParameter(param), defaultValue);
	}
	
	public static int get(
			HttpServletRequest request, String param, int defaultValue) {
		
		return GetterUtil.get(request.getParameter(param), defaultValue);
	}

	public static Integer get(
		HttpServletRequest request, String param, Integer defaultValue) {

		return GetterUtil.get(request.getParameter(param), defaultValue);
	}

	public static long get(
		HttpServletRequest request, String param, long defaultValue) {

		return GetterUtil.get(request.getParameter(param), defaultValue);
	}

	public static short get(
		HttpServletRequest request, String param, short defaultValue) {

		return GetterUtil.get(request.getParameter(param), defaultValue);
	}

	public static String get(
		HttpServletRequest request, String param, String defaultValue) {

		String returnValue =
			GetterUtil.get(request.getParameter(param), defaultValue);

		if (returnValue != null) {
			return returnValue.trim();
		}

		return null;
	}
	
	/**
	 * List values from format 1,2,3
	 * @param req
	 * @param param
	 * @return
	 */
	public static List<Integer> getIntegersSplit(HttpServletRequest req, String param) {

		String value = getString(req, param, null);

		if (ValidateUtil.isNotNull(value)) {
			
			String[] items = value.split(",");
			
			List<Integer> ids = null;
			
			if (items != null && items.length > 0) {
				ids = new ArrayList<Integer>();
				for (String id : items) {
					
					try {
						if (ValidateUtil.isNotNull(id)) { ids.add(Integer.parseInt(id)); }
					}
					catch (Exception e) {
						// ID is not Integer value, discarted value
					}
				}
			}
			
			return ids;
		}
		
		
		return null;
	}
	
	public static List<Integer> getIntegers(
			HttpServletRequest req, String param, List<Integer> defaultValue) {

		Integer[] items = GetterUtil.getIntegerList(
				req.getParameterValues(param), null);

		if (items != null) {
			return Arrays.asList(items);
		}
		return defaultValue;
	}
	
	public static Integer[] getIntegerList(
		HttpServletRequest req, String param) {

		return getIntegerList(req, param, new Integer[0]);
	}

	public static Integer[] getIntegerList(
		HttpServletRequest req, String param, Integer[] defaultValue) {

		return GetterUtil.getIntegerList(
			req.getParameterValues(param), defaultValue);
	}
	
	@SuppressWarnings("unchecked")
	public static void print(HttpServletRequest request) {
		Enumeration<String> enu = request.getParameterNames();

		while (enu.hasMoreElements()) {
			String param = enu.nextElement();

			String[] values = request.getParameterValues(param);

			for (int i = 0; i < values.length; i++) {
				logger.debug(param + "[" + i + "] = " + values[i]);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getAllParameters(HttpServletRequest request) {
		Map<String, String> parameters = new HashMap<String, String>();
		Enumeration<String> enu = request.getParameterNames();

		while (enu.hasMoreElements()) {
			String param = enu.nextElement();

			String[] values = request.getParameterValues(param);

			for (int i = 0; i < values.length; i++) {
				parameters.put(param, values[i]);
			}
		}
		
		return parameters;
	}

}
