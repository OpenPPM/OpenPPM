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
 * File: StakeholderDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Stakeholderclassification;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class StakeholderDAO extends AbstractGenericHibernateDAO<Stakeholder, Integer> {


	public StakeholderDAO(Session session) {
		super(session);
	}

	/**
	 * Delete all stakeholder by project
	 * 
	 * @param project
	 */
	public void deleteByProject(Project project) {
		
		// Set query
		Query q = getSession().createQuery(
				"delete from Stakeholder as st "+ 
				"where st.project.idProject = :idProject"
			);
		
		// Set id
		q.setInteger("idProject", project.getIdProject());
		
		// Execute query
		q.executeUpdate();
	}
	
	/**
	 * Find stakeholder by project
	 * 
	 * @param proj
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Stakeholder> findByProject(Project proj, Order... order) {
		
		// Add project to criteria
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Stakeholder.PROJECT, proj))
			.setFetchMode(Stakeholder.EMPLOYEE, FetchMode.JOIN)
			.setFetchMode(Stakeholder.EMPLOYEE+"."+Employee.CONTACT, FetchMode.JOIN);
		
		// Add orders
		for (Order o : order) {
			addOrder(crit, o.getProperty(), o.getTypeOrder());
		}		
		
		// Return result
		return crit.list();
	}
	
	/**
	 * Find stakeholders by project and classification
	 * 
	 * @param proj
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Stakeholder> findByProjectAndClassification(
			Project proj, Stakeholderclassification classif, Order... order) {
		
		// Add classification and project to criteria
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Stakeholder.PROJECT, proj))
			.add(Restrictions.eq(Stakeholder.STAKEHOLDERCLASSIFICATION, classif))
			.setFetchMode(Stakeholder.EMPLOYEE, FetchMode.JOIN)
			.setFetchMode(Stakeholder.EMPLOYEE+"."+Employee.CONTACT, FetchMode.JOIN)
			.setFetchMode(Stakeholder.STAKEHOLDERCLASSIFICATION, FetchMode.JOIN);
		
		// Add orders
		for (Order o : order) {
			addOrder(crit, o.getProperty(), o.getTypeOrder());
		}		
		
		// Return results
		return crit.list();
	}

}
