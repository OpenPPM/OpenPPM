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
 * Module: core
 * File: DataUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import es.sm2.openppm.core.common.Constants;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public final class DataUtil {

	public static final String LIST_BY_SIZE			= "listBySize"; // Default Option
	public static final String LIST_BY_NOT_EMPTY	= "listByNotEmpty";
	
	/**
	 * Create Propertie Map
	 * @param key
	 * @param value
	 * @return
	 */
	public static final <E> HashMap<String, E> toMap(String key, E value) {
		
		HashMap<String, E> map = new HashMap<String, E>();
		map.put(key, value);
		
		return map;
	}
	
	/**
	 * Create Propertie Map
	 * @param key
	 * @param value
	 * @return
	 */
	public static final HashMap<String, Object> toMapObj(String key, Object value) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(key, value);
		
		return map;
	}
	
	/**
	 * Create propertie Map
	 * @param key
	 * @param value
	 * @return
	 */
	public static final HashMap<String, Class<?>> toMapClass(String key, Class<?> value) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put(key, value);
		
		return map;
	}

	/**
	 * Create List
	 * @param values
	 * @return
	 */
	public static final <E> List<E> toList(E...values) {
		
		List<E> list = null;
		
		if (values != null && values.length > 0) {
			
			list = new ArrayList<E>(values.length);
			
			for (E obj : values) { list.add(obj); }
		}
		return list;
	}
	
	/**
	 * Short element
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static int sortFunction(Object value1 , Object value2) {
		
		return sortFunction(value1, value2, null);
	}

	/**
	 * List is empty
	 * @param list
	 * @return
	 */
	public static boolean listEmpty(List<?> list) {
		return (list == null || list.isEmpty());
	}
	
	/**
	 * List not empty
	 * @param list
	 * @return
	 */
	public static boolean listNotEmpty(List<?> list) {
		return !listEmpty(list);
	}
	
	/**
	 * Short element
	 * @param value1
	 * @param value2
	 * @param order
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int sortFunction(Object value1 , Object value2, Order order) {
		
		try {
        	
            int value;
            
            if (value1 != null) {
            	if (value1 instanceof List) { value1 = ((List)value1).size(); }
            	else if (order != null && order.forceClass() == Date.class) {
            	
            		SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_PATTERN);
            		value1 = df.parse((String) value1);
            	}
            }
            	
            if (value2 != null) {
	            if (value2 instanceof List) { value2 = ((List)value2).size(); }
	            else if (order != null && order.forceClass() == Date.class) {
	            	SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_PATTERN);
	            	value2 = df.parse((String) value2);
	            }
            }
            
            if (value1 != null && value2 != null) {
	            if (value1 instanceof String && value2 instanceof String) {
	            	
	            	value = ((String)value1).compareToIgnoreCase((String)value2);
	            }
	            else {
	            	value = ((Comparable)value1).compareTo(value2);
	            }
            }
            else if (value1 == null) { value = -1;  }
            else { value = +1;  }
            
            return (order == null? value :(Order.ASC.equals(order.getTypeOrder()) ? value : -1 * value));
        }
        catch(Exception e) {
        	e.printStackTrace();
            throw new RuntimeException();
        }
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getFieldValue(Object  item, String field, HashMap<String, String> typeList) {
		
		String[] fields = field.split("\\.");
		Object value	= null; 
		
		int i = 0 ;
		for (String itemField : fields) {

			if (i++ == 0) { value =  fieldValue(item, itemField); }
			else if (value != null && value instanceof List) {
				
				String val = "";
				
				for (Object itemList : (List<Object>)value) {
					
					val += (String) getFieldValue(itemList, itemField, typeList);
				}
				
				value = val;
			}
			else if (value != null) { value =  fieldValue(value, itemField); }
			
			if (value != null && value instanceof List && i == fields.length) {
				
				if (typeList != null && typeList.containsKey(itemField) && LIST_BY_NOT_EMPTY.equals(typeList.get(itemField))) {
					value = !((List)value).isEmpty();
				}
				else {
					value = ((List)value).size();
				}
			}
		}
		return value;
	}
	
	private static <T> Object fieldValue(T  item, String field) {
		
		Object value = null; 
		
		try {
			Method getter = item.getClass().getMethod("get" +
                String.valueOf(field.charAt(0)).toUpperCase() +
                field.substring(1));

			value = getter.invoke(item, new Object[0]);
		} catch (Exception e) {
			if (!"baja".equals(field)) { e.printStackTrace(); }
		}
		return value;
	}
}
