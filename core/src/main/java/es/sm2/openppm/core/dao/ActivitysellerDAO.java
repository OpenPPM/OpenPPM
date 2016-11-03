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
 * File: ActivitysellerDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

/**
 * DAO object for domain model class Activityseller
 * @see es.sm2.openppm.core.dao.Activityseller
 * @author Hibernate Generator by Javier Hernandez
 */
public class ActivitysellerDAO extends AbstractGenericHibernateDAO<Activityseller, Integer> {


	public ActivitysellerDAO(Session session) {
		super(session);
	}

	/**
	 * Get Activity sellers from Project
	 *
     * @param joins
     * @param projects
	 * @return
	 */
	public List<Activityseller> consActivitySellerByProject(List<String> joins, Project...projects) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		for (String join : joins) {			
			crit.setFetchMode(join, FetchMode.JOIN);
		}
		
		crit.addOrder(Order.asc(Activityseller.IDACTIVITYSELLER))
			.createCriteria(Activityseller.PROJECTACTIVITY)			
			.add(Restrictions.in(Projectactivity.PROJECT, projects));
		
		return crit.list();
	}

	/**
	 * ActivitySeller has associated projects
	 * @param activityseller
	 * @return
	 */
	public boolean hasAssociatedProjects(Activityseller activityseller) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		crit.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Activityseller.PROJECTACTIVITY, activityseller.getProjectactivity()))
			.add(Restrictions.isNotNull(Activityseller.PROJECT));
		
		Integer count = (Integer) crit.uniqueResult();
		
		return (count != null && count > 0);
	}

	/**
	 * Find seller associated project
	 * @param projectactivity
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Activityseller> findSellerAssociatedProject(Projectactivity projectactivity, List<String> joins) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		for (String join : joins) {			
			crit.setFetchMode(join, FetchMode.JOIN);
		}
		
		crit.add(Restrictions.eq(Activityseller.PROJECTACTIVITY, projectactivity))
			.add(Restrictions.isNotNull(Activityseller.PROJECT));
		
		return crit.list();
	}

	/**
	 * Find by seller and project
	 * 
	 * @param seller
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Activityseller> findBySellerAndProject(Seller seller, Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		// Filter by seller
		crit.add(Restrictions.eq(Activityseller.SELLER, seller))
			.add(Restrictions.isNotNull(Activityseller.INDIRECT));
		
		Criteria critProjectActivity = crit.createCriteria(Activityseller.PROJECTACTIVITY);
		
		// Filter by project
		critProjectActivity.add(Restrictions.eq(Projectactivity.PROJECT, project));
		
		return crit.list();
	}

	/**
	 * Update Type Activities Seller
	 * 
	 * @param seller
	 * @param indirect
	 * @param project 
	 */
	public void updateTypeActivitiesSeller(Seller seller, Boolean indirect, Project project) {
		
		Query query = getSession().createQuery(
				"update Activityseller actsPrimary "+
				"set actsPrimary.indirect = :indirect "+
				"where actsPrimary.seller = :seller and actsPrimary.projectactivity.idActivity in ( "+		
					    "select pa.idActivity "+
					    "from Projectactivity pa "+
					    "where pa.project = :project) "
				
			);
		
		query.setParameter("seller", seller);
		query.setParameter("project", project);
		query.setInteger("indirect", Boolean.TRUE.equals(indirect) ? 1 : 0);
		
		query.executeUpdate();
	}
}

