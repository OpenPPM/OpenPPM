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
 * File: DocumentprojectDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Project;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Documentproject
 * @see es.sm2.openppm.core.dao.Documentproject
 * @author Hibernate Generator by Javier Hernandez
 */
public class DocumentprojectDAO extends AbstractGenericHibernateDAO<Documentproject, Integer> {


	public DocumentprojectDAO(Session session) {
		super(session);
	}

	/**
	 * Find by type in the project
	 * @param project
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Documentproject findByType(Project project, String type) {
		
		List<Documentproject> list;
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Documentproject.PROJECT, project))
			.add(Restrictions.eq(Documentproject.TYPE, type));

		list = crit.list();
		return (list != null && !list.isEmpty()?list.get(0):null);
	}
	
	/**
	 * Find documents by type in the project
	 * @param project
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Documentproject> findListByType(Project project, String type) {
		
		List<Documentproject> list = null;
		
		// Filter by project
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Documentproject.PROJECT, project));	
		
		// Filter by type
		if(type != null) {
			crit.add(Restrictions.eq(Documentproject.TYPE, type));
		}
		else {
			
			crit.add(Restrictions.disjunction()
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_INITIATING))
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_RISK))
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_PLANNING))	
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_CONTROL))	
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_PROCUREMENT))	
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_CLOSURE))	
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_INVESTMENT))	
				);
		}
		
		// Order
		crit.addOrder(Order.asc(Documentproject.TYPE));

		list = crit.list();
		return list;
	}
	
	/**
	 * Find documents by type in the project
	 * @param project
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Documentproject> findListByType(Project project, String type, Boolean isFile) {
		
		List<Documentproject> list = null;
		
		// Filter by project
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Documentproject.PROJECT, project));
		
		if (isFile) {
			crit.add(Restrictions.isNotNull(Documentproject.MIME));
		}
		else {
			crit.add(Restrictions.isNull(Documentproject.MIME));
		}
		
		// Filter by type
		if(type != null) {
			crit.add(Restrictions.eq(Documentproject.TYPE, type));
		}
		else {
			
			crit.add(Restrictions.disjunction()
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_INITIATING))
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_RISK))
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_PLANNING))	
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_CONTROL))	
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_PROCUREMENT))	
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_CLOSURE))	
					.add(Restrictions.eq(Documentproject.TYPE, Constants.DOCUMENT_INVESTMENT))	
				);
		}
		
		// Order
		crit.addOrder(Order.asc(Documentproject.TYPE));

		list = crit.list();
		return list;
	}
}

