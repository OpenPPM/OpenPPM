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
 * File: AssumptionRegisterDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Assumptionregister;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class AssumptionRegisterDAO extends AbstractGenericHibernateDAO<Assumptionregister, Integer> {

	
	public AssumptionRegisterDAO(Session session) {
		super(session);
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Assumptionregister> searchByExample(Assumptionregister exampleInstance,
			Class... joins) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		if (exampleInstance.getProject() != null) { 
			crit.add(Restrictions.eq("project", exampleInstance.getProject()));
		}
		return crit.list();
	}
}
