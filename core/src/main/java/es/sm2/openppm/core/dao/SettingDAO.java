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
 * File: SettingDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import es.sm2.openppm.utils.functions.ValidateUtil;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Setting
 * @author Hibernate Generator by Javier Hernandez
 */
public class SettingDAO extends AbstractGenericHibernateDAO<Setting, Integer> {


	public SettingDAO(Session session) {
		super(session);
	}
	
	/**
	 * Find setting
	 * @param company
	 * @param name
	 * @return
	 */
	public Setting findSetting(Company company, String name) {		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setFetchMode(Setting.COMPANY, FetchMode.JOIN)
			.add(Restrictions.eq(Setting.COMPANY, company))
			.add(Restrictions.eq(Setting.NAME, name));
		
		return (Setting)crit.uniqueResult();
	}
	
	/**
	 * Find setting by name
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Setting> findSetting(String name) {		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Setting.NAME, name));
		
		return crit.list();
	}

	/**
	 * Find all Settings for company
	 * @param company
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Setting> findByCompany(Company company, Collection<String> values) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Setting.COMPANY, company));

		if (ValidateUtil.isNotNull(values)) {
			crit.add(Restrictions.in(Setting.NAME, values));
		}

		return crit.list();
	}
}

