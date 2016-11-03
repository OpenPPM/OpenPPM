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
 * File: HQuery.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */
package es.sm2.openppm.core.logic.persistance.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import es.sm2.openppm.utils.functions.ValidateUtil;



/**
 * @author javier.hernandez
 *
 */
@XStreamAlias("query")
public class HQuery extends HStatement {

	private List<HJoin> joins = new ArrayList<HJoin>();
	private List<HRestriction> restrictions = new ArrayList<HRestriction>();
	private List<HOrder> orders = new ArrayList<HOrder>();
	
	@XStreamOmitField
	private Map<String, HJoin> joinsMap = null;
	@XStreamOmitField
	private Map<String, HRestriction> restrictionsMap = null;
	@XStreamOmitField
	private Map<String, HOrder> ordersMap = null;
	
	/**
	 * @return the joins
	 */
	public List<HJoin> getJoins() {
		return joins;
	}
	/**
	 * @param joins the joins to set
	 */
	public void setJoins(List<HJoin> joins) {
		this.joins = joins;
	}
	/**
	 * @return the restrictions
	 */
	public List<HRestriction> getRestrictions() {
		return restrictions;
	}
	/**
	 * @param restrictions the restrictions to set
	 */
	public void setRestrictions(List<HRestriction> restrictions) {
		this.restrictions = restrictions;
	}
	/**
	 * @return the orders
	 */
	public List<HOrder> getOrders() {
		return orders;
	}
	/**
	 * @param orders the orders to set
	 */
	public void setOrders(List<HOrder> orders) {
		this.orders = orders;
	}
	
	/**
	 * @return the joinsMap
	 */
	public Map<String, HJoin> getJoinsMap() {
		
		if (ValidateUtil.isNotNull(joins) && joinsMap == null) {
			
			joinsMap = new HashMap<String, HJoin>();
			
			for (HJoin item : joins) {
				joinsMap.put(item.getKey(), item);
			}
		}
		
		return joinsMap;
	}

	/**
	 * @return the restrictionsMap
	 */
	public Map<String, HRestriction> getRestrictionsMap() {
		
		if (ValidateUtil.isNotNull(restrictions) && restrictionsMap == null) {
			
			restrictionsMap = new HashMap<String, HRestriction>();
			
			for (HRestriction item : restrictions) {
				restrictionsMap.put(item.getKey(), item);
			}
		}

		return restrictionsMap;
	}

	/**
	 * @return the ordersMap
	 */
	public Map<String, HOrder> getOrdersMap() {
		
		if (ValidateUtil.isNotNull(orders) && ordersMap == null) {
			
			ordersMap = new HashMap<String, HOrder>();
			
			for (HOrder item : orders) {
				ordersMap.put(item.getKey(), item);
			}
		}

		return ordersMap;
	}
}
