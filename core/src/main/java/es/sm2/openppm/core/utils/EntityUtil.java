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
 * File: EntityUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import es.sm2.openppm.utils.LogManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EntityUtil {
	
	private boolean removeLocal;
	private boolean override;
	private boolean recursive;

    /**
     * Copy fields to entity target with same name
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     * @return
     */
	public <S, T> T copyEntity(S source, T target) {

        // Add class fields
		Field[] thisFields = source.getClass().getDeclaredFields();
        addData(source, target, thisFields);

        // Add super class fields
        Field[] superFields = source.getClass().getSuperclass().getDeclaredFields();
        addData(source, target, superFields);

        return target;
	}

    /**
     * Add data to object
     *
     * @param source
     * @param target
     * @param thisFields
     * @param <S>
     * @param <T>
     */
    private <S, T> void addData(S source, T target, Field[] thisFields) {

        for (Field field : thisFields) {

            String nameField = null;

            if (isRemoveLocal() && field.getName().contains("local")) {

                nameField = field.getName().substring(5, field.getName().length());
                nameField = String.valueOf(nameField.charAt(0)).toLowerCase() + nameField.substring(1);
            }
            else { nameField = field.getName(); }

            try {
                if (hasField(target, nameField)) {

                    Object value = getFieldValue(source, nameField);

                    if (isOverride()) {
                        setFieldValue(target, nameField, value, field.getType());
                    }
                    else {
                        Object targetValue = getFieldValue(target, nameField);

                        if (targetValue == null) {
                            setFieldValue(target, nameField, value, field.getType());
                        }
                    }
                }
            } catch (Exception e) { e.printStackTrace();}

        }
    }

    /**
     * Check if field exist
     *
     * @param target
     * @param nameField
     * @param <T>
     * @return
     */
    private <T> boolean hasField(T target, String nameField) {

        boolean found = true;

        try {
            found = target.getClass().getDeclaredField(nameField) != null;
        }
        catch (NoSuchFieldException e) {
            found = false;
        }

        if (!found) {
            try {
                found = target.getClass().getSuperclass().getDeclaredField(nameField) != null;
            }
            catch (NoSuchFieldException e) {
                found = false;
            }
        }

        return !"serialVersionUID".equals(nameField) && found;
    }

    public Object getFieldValue(Object source, String field) {
		
		Object value = null; 
		
		try {
			Method getter = getMethod(source, field);

			value = getter.invoke(source, new Object[0]);
		} catch (Exception e) { }
		return value;
	}

    /**
     * Get Method for field
     *
     * @param object
     * @param field
     * @return
     */
    private Method getMethod(Object object, String field) {

        Method method = null;

        try {

            method = object.getClass().getMethod("get" +
                    String.valueOf(field.charAt(0)).toUpperCase() +
                    field.substring(1));
        }
        catch (NoSuchMethodException e) {

            try {
                method = object.getClass().getSuperclass().getMethod("get" +
                        String.valueOf(field.charAt(0)).toUpperCase() +
                        field.substring(1));
            }
            catch (NoSuchMethodException ex) {
            }
        }

        return method;
    }


    /**
     * Get set Method for field
     *
     * @param object
     * @param field
     * @param typeArg
     * @return
     */
    private Method getMethodSet(Object object, String field, Class<?> typeArg) {

        Method method = null;

        try {

            method = object.getClass().getMethod("set" +
                    String.valueOf(field.charAt(0)).toUpperCase() +
                    field.substring(1), typeArg);
        }
        catch (NoSuchMethodException e) {

            try {
                method = object.getClass().getSuperclass().getMethod("set" +
                        String.valueOf(field.charAt(0)).toUpperCase() +
                        field.substring(1), typeArg);
            }
            catch (NoSuchMethodException ex) {
            }
        }

        return method;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void setFieldValue(Object target, String field, Object arg, Class<?> typeArg) {
		
		try {

            Method setter = getMethodSet(target,field, typeArg);

			if (isRecursive() && arg instanceof List) {
				
				List list = new ArrayList();
				
				for (Object item : ((List)arg)) {
					
					Object newItem = createContents(item.getClass());
					
					newItem = copyEntity(item, newItem);
					
					list.add(newItem);
				}
				
				setter.invoke(target, list);
			}
			else if (setter != null) {
				
				setter.invoke(target, arg);
			}
			
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private <R> R createContents(Class<R> typeArg) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		
        return typeArg.getConstructor().newInstance();
    }

	/**
	 * @return the removeLocal
	 */
	public boolean isRemoveLocal() {
		return removeLocal;
	}

	/**
	 * @param removeLocal the removeLocal to set
	 */
	public void setRemoveLocal(boolean removeLocal) {
		this.removeLocal = removeLocal;
	}

	/**
	 * @return the override
	 */
	public boolean isOverride() {
		return override;
	}

	/**
	 * @param override the override to set
	 */
	public void setOverride(boolean override) {
		this.override = override;
	}

	/**
	 * @return the recursive
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * @param recursive the recursive to set
	 */
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}
}
