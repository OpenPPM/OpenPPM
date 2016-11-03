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
 * File: Order.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import java.io.Serializable;
import java.util.HashMap;

public class Order implements Serializable {
	
	private static final long serialVersionUID = 7095784482492712613L;
	
	public static String ASC = "asc";
	public static String DESC = "desc";
	
	private String property;
	private String typeOrder;
	private HashMap<String, Class<?>> forceType; 
	
	public Order(String property) {
		this.property = property;
	}
	
	public Order(String property, String typeOrder) {
		this.property = property;
		this.typeOrder = typeOrder;
	}
	
	public Order(String property, String typeOrder, HashMap<String, Class<?>> forceType) {
		this.property = property;
		this.typeOrder = typeOrder;
		this.forceType = forceType;
	}
	
	public Order() {}

	public void toggle() {
		if (typeOrder == null) { typeOrder = ASC; }
		else if (ASC.equals(typeOrder)) { typeOrder = DESC; }
		else { typeOrder = ASC; }
	}
	
	public Class<?> forceClass() {
		
		return (getForceType() != null && getForceType().containsKey(property) ? getForceType().get(property):String.class);
	}
	
	/**
	 * Add order ascendent
	 * @param property
	 * @return
	 */
	public static Order asc(String property) {
		
		return new Order(property, ASC);
	}
	
	/**
	 * Add order descendent
	 * @param property
	 * @return
	 */
	public static Order desc(String property) {
		
		return new Order(property, DESC);
	}
	
	/**
	 * Add order
	 * @param order
	 * @param query
	 */
	public static String add(Order order, String query) {
		
		if (order != null && order.getProperty() != null) {
			
			if (order.getProperty().indexOf(".") == -1) {
				query += " order by e."+order.getProperty()+ " ";
			}
			else {
				query += " order by "+order.getProperty()+ " ";
			}
			if (order.getTypeOrder() != null) { query += order.getTypeOrder()+" "; }
		}
		
		return query;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * @param typeOrder the typeOrder to set
	 */
	public void setTypeOrder(String typeOrder) {
		this.typeOrder = typeOrder;
	}
	/**
	 * @return the typeOrder
	 */
	public String getTypeOrder() {
		return typeOrder;
	}

	/**
	 * @return the forceType
	 */
	public HashMap<String, Class<?>> getForceType() {
		return forceType;
	}

	/**
	 * @param forceType the forceType to set
	 */
	public void setForceType(HashMap<String, Class<?>> forceType) {
		this.forceType = forceType;
	}

}
