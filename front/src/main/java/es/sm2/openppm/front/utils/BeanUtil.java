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
 * Module: front
 * File: BeanUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;

public class BeanUtil {

	
	/**
	 * Create instance of bean and load from request
	 * 
	 * @param request
	 * @param clazz
	 * @param simpleDateFormat
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> T loadBean(HttpServletRequest request, Class<T> clazz, SimpleDateFormat simpleDateFormat)
			throws InstantiationException, IllegalAccessException {
		
		T bean = clazz.newInstance();
		
		return loadBean(request, bean, simpleDateFormat, bean.getClass());
	}

    /**
     * Load bean from request
     *
     * @param request
     * @param bean
     * @param simpleDateFormat
     * @param clazz
     * @param <T>
     * @return
     */
	public static <T> T loadBean(HttpServletRequest request, T bean,
			SimpleDateFormat simpleDateFormat, Class<?> clazz) {
		
		// Search value for each fields 
		for (Field field : clazz.getDeclaredFields()) {
		
			// Name of field
			String nameField = field.getName();
			
			try {

                // Exclude 'serialVersionUID' field
                if (!"serialVersionUID".equals(nameField)) {

                    String value = request.getParameter(nameField);

                    // If value is informed from request
                    if (value != null) {

                        setFieldValue(bean, nameField, value, field.getType(), simpleDateFormat);

                    }
                }
            }
			catch (Exception e) {
                LogManager.getConsoleLog(BeanUtil.class).warn("Reflect load bean", e);
			}
		}
		
		// Recursive condition
		if (clazz.getSuperclass() != Object.class) {
			
			bean = loadBean(request, bean, simpleDateFormat, clazz.getSuperclass());
		}
		
		return bean;
	}
	
	/**
	 * Set field value
	 * 
	 * @param bean
	 * @param nameField
	 * @param value
	 * @param typeValue
	 * @param simpleDateFormat
	 */
	private static void setFieldValue(Object bean, String nameField, String value, Class<?> typeValue,
			SimpleDateFormat simpleDateFormat) {
		
		try {
			
			// Declare setter for set value
			Method setter = bean.getClass().getMethod("set" +
                String.valueOf(nameField.charAt(0)).toUpperCase() +
                nameField.substring(1), typeValue);
			
			
			if (typeValue == String.class) {
				
				setter.invoke(bean, value);
			}
			else if (typeValue == Date.class) {
			
				Object parseValue = null;
				
				try {
					parseValue = simpleDateFormat.format(value);
				}
				catch(Exception e) {}
				
				setter.invoke(bean, parseValue);
			}
			else if (typeValue == Double.class) {
	
				Object parseValue = null;
				
				try {
					parseValue = Double.valueOf(value);
				}
				catch(Exception e) {}
				
				setter.invoke(bean, parseValue);
			}
			else if (typeValue == Integer.class) {
	
				Object parseValue = null;
				
				try {
					parseValue = Integer.valueOf(value);
				}
				catch(Exception e) {}
				
				setter.invoke(bean, parseValue);
			}
			else if (typeValue == Boolean.class) {
	
				Object parseValue = null;
				
				if (ValidateUtil.isNotNull(value)) {
					try {
						parseValue = Boolean.valueOf(value);
					}
					catch(Exception e) {}
				}
				
				setter.invoke(bean, parseValue);
			}
			
			
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Generate toString from bean
	 * 
	 * @param bean
	 * @return
	 */
	public static String toString(Object bean) {
		
		StringBuilder beanValue = new StringBuilder();
		beanValue.append("***********************\n");
		beanValue.append("TO STRING -> "+bean.getClass().getCanonicalName()+"\n");
		beanValue.append("-----------------------\n");
		
		// Declared fields of beans
		toStringFields(bean, beanValue, bean.getClass().getDeclaredFields());
		toStringFields(bean, beanValue, bean.getClass().getSuperclass().getDeclaredFields());
		
		beanValue.append("***********************\n");
		return beanValue.toString();
	}

	private static void toStringFields(Object bean, StringBuilder beanValue, Field[] fields) {
		
		// Search value for each fields 
		for (Field field : fields) {
		
			try {
				
				// Name of field
				String nameField = field.getName();
				Object value = getFieldValue(bean, nameField);
				
				beanValue.append("\t* "+nameField+": "+(value== null?"null":value.toString())+"\n");
			}
			catch (Exception e) {
				// If not found method ignore
			}
			
		}
	}
	
	/**
	 * Value from field
	 * 
	 * @param bean
	 * @param nameField
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static Object getFieldValue(Object bean, String nameField)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		Object value = null; 
		
		Method getter = bean.getClass().getMethod("get" +
            String.valueOf(nameField.charAt(0)).toUpperCase() +
            nameField.substring(1));

		value = getter.invoke(bean, new Object[0]);
		
		return value;
	}
}
