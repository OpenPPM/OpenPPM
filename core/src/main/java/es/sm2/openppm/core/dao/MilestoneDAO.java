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
 * File: MilestoneDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.sm2.openppm.core.common.Constants;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Milestonecategory;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Milestones.MilestonePending;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class MilestoneDAO extends AbstractGenericHibernateDAO<Milestones, Integer> {

	public MilestoneDAO(Session session) {
		super(session);
	}

	/**
	 * Return all milestones by Project
	 * @param proj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Milestones> findByProject(Project proj) {
		List<Milestones> list = null;
		if (proj != null) {
			Query query = getSession().createQuery(
					"select milestones " +
					"from Milestones as milestones " + 
					"left join fetch milestones.projectactivity as activity " +
					"join milestones.project as project " +
					"where project.idProject = :idProject");
			query.setInteger("idProject", proj.getIdProject());
			list = query.list();
		}
		return list;
	}

	
	/**
	 * Find Milestone with Project Activity
	 * @param milestone
	 * @return
	 */
	public Milestones findMilestone(Milestones milestone) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		crit.setFetchMode("projectactivity", FetchMode.JOIN);
		crit.add(Restrictions.eq("idMilestone", milestone.getIdMilestone()));
		
		return (Milestones) crit.uniqueResult();
	}

	/**
	 * Finds a milestone by its name and project
	 * @param name
	 * @param project
	 * @return
	 */
	public Milestones findByNameAndProject(String name, Project project) {

		Criteria crit = getSession().createCriteria(getPersistentClass());
		crit.add(Restrictions.eq(Milestones.NAME, name));
		crit.add(Restrictions.eq(Milestones.PROJECT, project));

		return (Milestones) crit.uniqueResult();
	}


	/**
	 * Return Milestones without activity
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Milestones> findMilestoneWithoutActivity(Project project) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		crit.add(Restrictions.isNull("projectactivity.idActivity"));
		crit.add(Restrictions.eq("project.idProject", project.getIdProject()));
		return crit.list();
	}

	/**
	 * Delete all milestones by project
	 * @param project
	 */
	public void deleteByProject(Project project) {

		Query q = getSession().createQuery(
				"delete from Milestones as m "+ 
				"where m.project.idProject = :idProject"
			);
		
		q.setInteger("idProject", project.getIdProject());
		q.executeUpdate();
	}

	/**
	 * Find milestones for notify
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Milestones> findForNotify() {
		
		// FIXME Calcular la fecha segun los dias que esta configurado en el milestone. Cambiar la select
		
		Calendar now = DateUtil.getCalendar();
		now.set(Calendar.HOUR, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);

		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Milestones.NOTIFY, true))
			.add(Restrictions.eq(Milestones.NOTIFYDATE, now.getTime()));

        crit.createCriteria(Milestones.PROJECT)
            .add(Restrictions.eq(Project.STATUS, Constants.STATUS_CONTROL))
            // PO
			.setFetchMode(Project.PERFORMINGORG, FetchMode.JOIN)
			// PM
			.setFetchMode(Project.EMPLOYEEBYPROJECTMANAGER, FetchMode.JOIN)
			.setFetchMode(Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT, FetchMode.JOIN)
			// PMO
			.setFetchMode(Project.PERFORMINGORG, FetchMode.JOIN)
			.setFetchMode(Project.PERFORMINGORG+"."+Performingorg.EMPLOYEE, FetchMode.JOIN)
			.setFetchMode(Project.PERFORMINGORG+"."+Performingorg.EMPLOYEE+"."+Employee.CONTACT, FetchMode.JOIN);

		return crit.list();
	}

	/**
	 * Find milestones filter by projects and milestone type and dates
	 * 
	 * @param projects
	 * @param milestonetype 
	 * @param milestonecategory 
	 * @param until 
	 * @param since 
	 * @param milestonePending
	 * @param property
	 * @param order
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Milestones> filter(List<Project> projects, 
			Milestonetype milestonetype, Milestonecategory milestonecategory,
			Date since, Date until, String milestonePending, String property, String order, List<String> joins) {
		
		List<Milestones> list = new ArrayList<Milestones>();
		
		if (ValidateUtil.isNotNull(projects)) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass());
			
			// Filter dates
			//
			if (since != null & until != null) {
				
				crit.add(Restrictions.disjunction()
							.add(Restrictions.conjunction()
								.add(Restrictions.isNotNull(Milestones.ACHIEVED))
								.add(Restrictions.between(Milestones.ACHIEVED, since, until)))
							.add(Restrictions.conjunction()
								.add(Restrictions.isNotNull(Milestones.ESTIMATEDDATE))
								.add(Restrictions.between(Milestones.ESTIMATEDDATE, since, until)))
							.add(Restrictions.conjunction()
								.add(Restrictions.isNotNull(Milestones.PLANNED))
								.add(Restrictions.between(Milestones.PLANNED, since, until)))
						);
			}
			else if (since != null) {
				
				crit.add(Restrictions.disjunction()
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.ACHIEVED))
							.add(Restrictions.ge(Milestones.ACHIEVED, since)))
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.ESTIMATEDDATE))
							.add(Restrictions.ge(Milestones.ESTIMATEDDATE, since)))
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.PLANNED))
							.add(Restrictions.ge(Milestones.PLANNED, since)))
					);
			}
			else if (until != null) {
				
				crit.add(Restrictions.disjunction()
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.ACHIEVED))
							.add(Restrictions.le(Milestones.ACHIEVED, until)))
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.ESTIMATEDDATE))
							.add(Restrictions.le(Milestones.ESTIMATEDDATE, until)))
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.PLANNED))
							.add(Restrictions.le(Milestones.PLANNED, until)))
					);
			}
			
			// Filter by projects
			//
			crit.add(Restrictions.in(Milestones.PROJECT, projects));
			
			// Filter by milestone type
			//
			if (milestonetype != null) {
				crit.add(Restrictions.eq(Milestones.MILESTONETYPE, milestonetype));
			}
			
			// Filter by milestone category
			//
			if (milestonecategory != null) {
				crit.add(Restrictions.eq(Milestones.MILESTONECATEGORY, milestonecategory));
			}
			
			// Filter by pendings 
			//
			if (MilestonePending.YES.name().equals(milestonePending)) {
				crit.add(Restrictions.isNull(Milestones.ACHIEVED));
			}
			else if (MilestonePending.NO.name().equals(milestonePending)) {
				crit.add(Restrictions.isNotNull(Milestones.ACHIEVED));
			}
			
			// Left join milestone type for null relation
			crit.createCriteria(Milestones.MILESTONETYPE, CriteriaSpecification.LEFT_JOIN);
			
			// Joins
			addJoins(crit, joins);
			
			// Orders
			addOrder(crit, property, order);
			
			list =  crit.list();
		}
				
		return list;
	}

	/**
	 * Find milestones filter by project and activity and dates
	 * 
	 * @param project
	 * @param projectactivity
	 * @param since
	 * @param until
	 * @param hasCategory
	 * @param property
	 * @param order
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Milestones> filter(Project project, Projectactivity projectactivity, Date since, Date until, boolean hasCategory,String property, String order, List<String> joins) {
		
		List<Milestones> list = new ArrayList<Milestones>();
		
		if (project != null) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass());
			
			// Filter dates
			//
			if (since != null & until != null) {
				
				crit.add(Restrictions.disjunction()
							.add(Restrictions.conjunction()
								.add(Restrictions.isNotNull(Milestones.ACHIEVED))
								.add(Restrictions.between(Milestones.ACHIEVED, since, until)))
							.add(Restrictions.conjunction()
								.add(Restrictions.isNotNull(Milestones.ESTIMATEDDATE))
								.add(Restrictions.between(Milestones.ESTIMATEDDATE, since, until)))
							.add(Restrictions.conjunction()
								.add(Restrictions.isNotNull(Milestones.PLANNED))
								.add(Restrictions.between(Milestones.PLANNED, since, until)))
						);
			}
			else if (since != null) {
				
				crit.add(Restrictions.disjunction()
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.ACHIEVED))
							.add(Restrictions.ge(Milestones.ACHIEVED, since)))
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.ESTIMATEDDATE))
							.add(Restrictions.ge(Milestones.ESTIMATEDDATE, since)))
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.PLANNED))
							.add(Restrictions.ge(Milestones.PLANNED, since)))
					);
			}
			else if (until != null) {
				
				crit.add(Restrictions.disjunction()
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.ACHIEVED))
							.add(Restrictions.le(Milestones.ACHIEVED, until)))
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.ESTIMATEDDATE))
							.add(Restrictions.le(Milestones.ESTIMATEDDATE, until)))
						.add(Restrictions.conjunction()
							.add(Restrictions.isNotNull(Milestones.PLANNED))
							.add(Restrictions.le(Milestones.PLANNED, until)))
					);
			}
			
			// Filter by project
			//
			if (project != null) {
				crit.add(Restrictions.eq(Milestones.PROJECT, project));
			}
			
			// Filter by activity 
			//
			if (projectactivity != null) {
				crit.add(Restrictions.eq(Milestones.PROJECTACTIVITY, projectactivity));
			}
			
			// Filter by has category 
			//
			if (hasCategory) {
				crit.add(Restrictions.isNotNull(Milestones.MILESTONECATEGORY));
			}
			
			// Joins
			addJoins(crit, joins);
			
			// Orders
			addOrder(crit, property, order);
			
			list =  crit.list();
		}
				
		return list;
	}
	
}
