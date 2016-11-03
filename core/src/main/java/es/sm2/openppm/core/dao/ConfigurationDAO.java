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
 * File: ConfigurationDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Configuration;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Configuration
 * @see es.sm2.openppm.core.dao.Configuration
 * @author Hibernate Generator by Javier Hernandez
 */
public class ConfigurationDAO extends AbstractGenericHibernateDAO<Configuration, Integer> {


	public ConfigurationDAO(Session session) {
		super(session);
	}

	/**
	 * Find configurations by types
	 * 
	 * @param user
	 * @param types
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Configuration> findByTypes(Employee user, String[] types) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.in(Configuration.TYPE, types))
				.add(Restrictions.eq(Configuration.CONTACT, user.getContact()));
		
		
		return crit.list();
	}

	/**
	 * Find by name
	 * 
	 * @param user
	 * @param name
	 * @param type
	 * @return
	 */
	public Configuration findByName(Employee user, String name, String type) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.eq(Configuration.TYPE, type))
				.add(Restrictions.eq(Configuration.NAME, name))
				.add(Restrictions.eq(Configuration.CONTACT, user.getContact()));
		
		return (Configuration) crit.uniqueResult();
	}

}

