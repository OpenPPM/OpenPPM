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
 * File: PluginfileDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Plugin;
import es.sm2.openppm.core.model.impl.Pluginfile;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Pluginfile
 * @see es.sm2.openppm.core.dao.Pluginfile
 * @author Hibernate Generator by Javier Hernandez
 */
public class PluginfileDAO extends AbstractGenericHibernateDAO<Pluginfile, Integer> {


	public PluginfileDAO(Session session) {
		super(session);
	}

	/**
	 * Find plugin file by company
	 * 
	 * @param company
	 * @param pluginName
	 * @param typefile
	 * @return
	 */
	public Pluginfile findByCompany(Company company, String pluginName, String typefile) {
		
		Pluginfile pluginFile = null;
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.eq(Pluginfile.TYPEFILE, typefile))
			.createCriteria(Pluginfile.PLUGIN)
				.add(Restrictions.eq(Plugin.NAME, pluginName))
				.add(Restrictions.eq(Plugin.COMPANY, company));

		pluginFile = (Pluginfile) crit.uniqueResult();
		
		return pluginFile;
	}

}

