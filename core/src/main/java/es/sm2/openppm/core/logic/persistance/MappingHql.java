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
 * File: MappingHql.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */
package es.sm2.openppm.core.logic.persistance;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import es.sm2.openppm.core.logic.persistance.bean.HQuery;
import es.sm2.openppm.core.logic.persistance.bean.HRestriction;
import es.sm2.openppm.core.logic.persistance.bean.HqlContainer;
import es.sm2.openppm.core.logic.persistance.bean.NamedJoin;
import es.sm2.openppm.core.logic.persistance.bean.NamedOrder;
import es.sm2.openppm.core.logic.persistance.bean.NamedQuery;
import es.sm2.openppm.core.logic.persistance.bean.NamedRestriction;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;

/**
 * @author javier.hernandez
 *
 */
public class MappingHql {

    private final static String PATH_HQL		= "/es/sm2/openppm/core/logic/persistance/hql/";
	private final static String EXTENSION_HQL	= ".hql.xml";
	private final static String AND				= "AND";
	private final static String WHERE			= " WHERE ";
	private final static String ORDER_BY		= " ORDER BY ";
	private final static String KEY				= "key";
	private final static String UTF_8			= "UTF-8";
    public static final String HIBERNATE_QUERY_LANGUAGE = "HibernateQueryLanguage";

    private HqlContainer container;
	
	private NamedQuery namedQuery			    = null;
	private List<NamedJoin> joins				= new ArrayList<NamedJoin>();
	private List<NamedRestriction> restrictions	= new ArrayList<NamedRestriction>();
	private List<NamedOrder> orders			    = new ArrayList<NamedOrder>();
    private HashMap<String, String> subSelects  = new HashMap<String, String>();

	public MappingHql(String name) {
		
		// Format for name
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		
		// Create load xml
		XStream xstream = new XStream(new DomDriver(UTF_8));
		xstream.alias(KEY, String.class);
		xstream.alias(HIBERNATE_QUERY_LANGUAGE, HqlContainer.class);
		xstream.autodetectAnnotations(true);
		
		InputStream stream = MappingHql.class.getResourceAsStream(PATH_HQL+name+EXTENSION_HQL);
		
		container = (HqlContainer) xstream.fromXML(stream, new HqlContainer());
	}
	
	/**
	 * Add joins for generate statement
	 * 
	 * @param joins
	 * @return
	 */
	public MappingHql putJoins(NamedJoin...joins) {

        if (ValidateUtil.isNotNull(joins)) {
            this.joins.addAll(Arrays.asList(joins));
        }
		
		return this;
	}
	
	/**
	 * Add restrictions for generate statement 
	 * 
	 * @param restrictions
	 * @return
	 */
	public MappingHql putRestrictions(NamedRestriction... restrictions) {

        if (ValidateUtil.isNotNull(restrictions)) {
            this.restrictions.addAll(Arrays.asList(restrictions));
        }

		return this;
	}

    /**
     * Add sub select replacing KEY
     *
     * @param replaceName - KEY for replacing in original select
     * @param subSelect - sub select value
     * @return instance of MappingHql
     */
    public MappingHql putSubSelect(String replaceName, String subSelect) {

        // Add sub select for replace key
        if (ValidateUtil.isNotNull(replaceName) && ValidateUtil.isNotNull(subSelect)) {
            this.subSelects.put(replaceName, subSelect);
        }

        return this;
    }
	/**
	 * Add orders for generate statement
	 * 
	 * @param orders
	 * @return
	 */
	public MappingHql putOrders(NamedOrder... orders) {

        if (ValidateUtil.isNotNull(orders)) {
            this.orders.addAll(Arrays.asList(orders));
        }

		return this;
	}
	
	/**
	 * Set named query for generate statement
	 * 
	 * @param namedQuery
	 * @return
	 */
	public MappingHql setNamedQuery(NamedQuery namedQuery) {

		this.namedQuery = namedQuery;

		return this;
	}
	
	
	/**
	 * Get Statement
	 *
	 * @return
	 */
	public String create() {

		// HQuery
		HQuery queryFormat = container.getHQuery(namedQuery);
		
		// Statement
		StringBuilder query = new StringBuilder(queryFormat.getStatement());
		
		if (ValidateUtil.isNotNull(joins)) {
			// Add joins
			for (NamedJoin item : joins) {
				
				if (queryFormat.getJoinsMap().containsKey(item.name())) {
					query.append(StringPool.SPACE).append(queryFormat.getJoinsMap().get(item.name()).getStatement());
				}
			}
		}
		
		// Create where and add joins
		StringBuilder where = new StringBuilder();
		
		if (ValidateUtil.isNotNull(restrictions)) {
			for (NamedRestriction item : restrictions) {
	
				// Add joins
				if (queryFormat.getJoinsMap() != null && queryFormat.getJoinsMap().containsKey(item.name())) {
					query.append(StringPool.SPACE).append(queryFormat.getJoinsMap().get(item.name()).getStatement());
				}
				
				// Create where
				if (queryFormat.getRestrictionsMap().containsKey(item.name())) {
					
					HRestriction restriction = queryFormat.getRestrictionsMap().get(item.name());
					
					if (ValidateUtil.isNotNull(where.toString())) {
						where.append(StringPool.SPACE).append(ValidateUtil.isNull(restriction.getType()) ? AND : restriction.getType());
					}
					
					where.append(StringPool.SPACE).append(queryFormat.getRestrictionsMap().get(item.name()).getStatement());
				}
			}
		}

		// Create orders
		StringBuilder ordersQuery = new StringBuilder();
		if (ValidateUtil.isNotNull(orders)) {
			for (NamedOrder item : orders) {
	
				if (queryFormat.getOrdersMap().containsKey(item.name())) {
					ordersQuery.append(StringPool.SPACE).append(queryFormat.getOrdersMap().get(item.name()).getStatement());
				}
			}
		}
		
		// Add where
		if (ValidateUtil.isNotNull(where.toString())) {

			query.append(WHERE).append(where.toString());
		}

		// Add orders
		if (ValidateUtil.isNotNull(ordersQuery.toString())) {
			
			query.append(ORDER_BY).append(ordersQuery.toString());
		}

        // Parse to string for replaces
        String resultQuery = query.toString();

        // Add sub selects
        if (!this.subSelects.isEmpty()) {

            for (String key : this.subSelects.keySet()) {

                resultQuery = Pattern.compile(key).matcher(resultQuery).replaceAll(this.subSelects.get(key));
            }
        }

		return resultQuery;
	}
}
