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
 * File: ProblemcheckDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Problemcheck;
import es.sm2.openppm.core.model.impl.Problemcheckproject;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Problemcheck
 * @see es.sm2.openppm.core.dao.Problemcheck
 * @author Hibernate Generator by Javier Hernandez
 */
public class ProblemcheckDAO extends AbstractGenericHibernateDAO<Problemcheck, Integer> {


	public ProblemcheckDAO(Session session) {
		super(session);
	}

	/**
	 * Find the remaining and active checks
	 * 
	 * @param idsExclude 
	 * @param company 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Problemcheck> findbyRemainAndActive(Integer[] idsExclude, Company company) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.eq(Problemcheck.COMPANY, company))
				.add(Restrictions.eq(Problemcheck.SHOWCHECK, true));
		
		// Exclude
		if (ValidateUtil.isNotNull(idsExclude)) {
			crit.add(Restrictions.not(Restrictions.in(Problemcheck.IDPROBLEMCHECK, idsExclude)));
		}
			
		// Order
		crit.addOrder(Order.asc(Problemcheck.NAME));
		
		return crit.list();
	}

	/**
	 * Find by project
	 * 
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Problemcheck> findByProject(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.eq(Problemcheck.SHOWCHECK, true));		
		
		crit.createCriteria(Problemcheck.PROBLEMCHECKPROJECTS)
			.add(Restrictions.eq(Problemcheckproject.PROJECT, project));	
					
		return crit.list();
	}

}

