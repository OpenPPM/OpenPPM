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
 * File: TimesheetDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.*;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.setting.GeneralSetting;
import es.sm2.openppm.core.model.wrap.ImputationWrap;
import es.sm2.openppm.core.utils.SettingUtil;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.javabean.PropertyRelation;
import es.sm2.openppm.core.utils.FilterUtil;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.wrap.ApprovalWrap;
import es.sm2.openppm.core.model.wrap.ResourceTimeWrap;
import es.sm2.openppm.core.model.wrap.TimesheetWrap;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Timesheet
 * @see es.sm2.openppm.core.dao.Timesheet
 * @author Hibernate Generator by Javier Hernandez
 */
public class TimesheetDAO extends AbstractGenericHibernateDAO<Timesheet, Integer> {

	public TimesheetDAO(Session session) {
		super(session);
	}

	/**
	 * Find time Sheet of the resource by criteria 
	 * @param employee
	 * @param projectactivity
	 * @param initDate
	 * @param endDate
	 * @return
	 */
	public Timesheet findByCriteria(Employee employee,
			Projectactivity projectactivity, Date initDate, Date endDate) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Timesheet.PROJECTACTIVITY, projectactivity))
			.add(Restrictions.eq(Timesheet.EMPLOYEE, employee))
			.add(Restrictions.eq(Timesheet.INITDATE, initDate))
			.add(Restrictions.eq(Timesheet.ENDDATE, endDate));
			
		
		return (Timesheet) crit.uniqueResult();
	}

	/**
	 * Find time sheets of the resource
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @param joins
	 * @param project
	 * @param minStatus
	 * @param maxStatus
	 * @param filterUser
	 * @param settings
     * @return
	 */
	public List<Timesheet> findByCriteria(Employee employee, Date initDate,
                                          Date endDate, List<String> joins, Project project, String minStatus,
                                          String maxStatus, Employee filterUser, boolean includeClosed, HashMap<String, String> settings) {
		
		return findByCriteria(employee, initDate, endDate, joins, project,
				minStatus, maxStatus, filterUser, Constants.RESOURCE_ASSIGNED, includeClosed, settings);
	}
	
	/**
	 * Find time sheets of the resource
	 * 
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @param joins
	 * @param project
	 * @param minStatus
	 * @param maxStatus
	 * @param filterUser
	 * @param statusResource
     * @param settings
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> findByCriteria(Employee employee, Date initDate,
			Date endDate, List<String> joins, Project project, String minStatus,
			String maxStatus, Employee filterUser, String statusResource,
			boolean includeClosed, HashMap<String, String> settings) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.add(Restrictions.eq(Timesheet.EMPLOYEE, employee))
			.add(Restrictions.eq(Timesheet.INITDATE, initDate))
			.add(Restrictions.eq(Timesheet.ENDDATE, endDate));
		
		if (minStatus != null && maxStatus != null) {
			
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.eq(Timesheet.STATUS, minStatus));
			disjunction.add(Restrictions.eq(Timesheet.STATUS, maxStatus));
			
			if (Constants.TIMESTATUS_APP0.equals(minStatus) && (
					Constants.TIMESTATUS_APP2.equals(maxStatus) ||
					Constants.TIMESTATUS_APP3.equals(maxStatus))) {
				
				disjunction.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP1));
			}
			
			if (Constants.TIMESTATUS_APP3.equals(maxStatus) && (
					Constants.TIMESTATUS_APP0.equals(minStatus) ||
					Constants.TIMESTATUS_APP1.equals(minStatus))) {
				
				disjunction.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP2));
			}

            //TODO 21/04/2015 - jordi.ripoll- tarea 1095 -pendiente de confirmacion
            // Last level be approved hours app1 by setting
            //
//            String lastLevelForApprove = SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET,
//                    Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET);
//
//            if ((filterUser != null && lastLevelForApprove.equals(String.valueOf(filterUser.getResourceprofiles().getIdProfile()))) &&
//                    (settings != null && SettingUtil.getBoolean(settings, GeneralSetting.LAST_LEVEL_APPROVED_APP1))) {
//
//                disjunction.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP1));
//            }

			crit.add(disjunction);
		}

		Criteria critEmployee =	crit.createCriteria(Timesheet.EMPLOYEE);
		critEmployee.createCriteria(Employee.CONTACT);

		Criteria critActivity = crit.createCriteria(Timesheet.PROJECTACTIVITY);

		Criteria critMembers = critActivity.createCriteria(Projectactivity.TEAMMEMBERS)
			.add(Restrictions.eq(Teammember.EMPLOYEE, employee));
		
		if (ValidateUtil.isNotNull(statusResource)) {
			critMembers.add(Restrictions.eq(Teammember.STATUS, statusResource));
		}
		
		Criteria critProject = critActivity.createCriteria(Projectactivity.PROJECT);
		
		if (!includeClosed) {
            critProject.add(Restrictions.and(
                    Restrictions.ne(Project.STATUS, Constants.STATUS_CLOSED),
                    Restrictions.ne(Project.STATUS, Constants.STATUS_ARCHIVED)));
		}
		
		if (project != null) {		
			critProject.add(Restrictions.eq(Project.IDPROJECT, project.getIdProject()));
		}
		else if (filterUser != null) {
			
			// Filter by User. Settings by company for last approval.
			Resourceprofiles profile = filterUser.getResourceprofiles();
			
			if (profile.getIdProfile() == Constants.ROLE_FM) {
				
				critProject.add(Restrictions.eq(Project.EMPLOYEEBYFUNCTIONALMANAGER, filterUser));
			}
			else if (profile.getIdProfile() == Constants.ROLE_PMO) {
				
				critProject.add(Restrictions.eq(Project.PERFORMINGORG, filterUser.getPerformingorg()));
			}
		}
		
		addJoins(crit, joins);
		
		return crit.list();
	}
	
	/**
	 * Hours assigned
	 * 
	 * @param teammember
	 * @param minStatus
	 * @param maxStatus
	 * @return
	 */
	public boolean hoursInState(Teammember teammember, String minStatus, String maxStatus) {
		
		String q = "SELECT COUNT(ts) FROM Timesheet ts " +
				"JOIN ts.employee e " +
				"JOIN e.teammembers tem " +
				"WHERE " +
					"ts.projectactivity = :projectactivity " +
					"AND ((ts.initDate between :since and :until) " +
						"OR (ts.endDate between :since and :until) " +
						"OR (:since between ts.initDate and ts.endDate) " +
						"OR (:until between ts.initDate and ts.endDate)) " +
					"AND tem = :teammember " +
					"AND (ts.status = :minStatus OR ts.status = :maxStatus)";
		
		
		Query query = getSession().createQuery(q);
		
		query.setEntity("teammember", teammember);
		query.setEntity("projectactivity", teammember.getProjectactivity());
		query.setDate("since", DateUtil.getFirstWeekDay(teammember.getDateIn()));
		query.setDate("until", DateUtil.getFirstWeekDay(teammember.getDateOut()));
		query.setString("minStatus", minStatus);
		query.setString("maxStatus", maxStatus);
		
		Long count = (Long) query.uniqueResult();
		
		return (count != null && count > 0);
	}
	
	/**
	 * Time Sheets in state by member
	 * 
	 * @param teammember
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> hoursInState(Teammember teammember, String status) {
		
		String q = "SELECT ts FROM Timesheet ts " +
				"JOIN ts.employee e " +
				"JOIN e.teammembers tem " +
				"WHERE " +
					"ts.projectactivity = :projectactivity " +
					"AND ts.status = :status " +
					"AND tem = :teammember " +
					"AND ((ts.initDate between :since and :until) " +
						"OR (ts.endDate between :since and :until) " +
						"OR (:since between ts.initDate and ts.endDate) " +
						"OR (:until between ts.initDate and ts.endDate)) ";
		
		Query query = getSession().createQuery(q);
		
		query.setEntity("teammember", teammember);
		query.setEntity("projectactivity", teammember.getProjectactivity());
		query.setDate("since", DateUtil.getFirstWeekDay(teammember.getDateIn()));
		query.setDate("until", DateUtil.getFirstWeekDay(teammember.getDateOut()));
		query.setString("status", status);
		
		return query.list();
	}
	
	/**
	 * 
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> findWithActivityByResource(Employee employee, Date initDate, Date endDate, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())		
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.add(Restrictions.eq(Timesheet.EMPLOYEE, employee))
			.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
			.add(Restrictions.ge(Timesheet.INITDATE, initDate))
			.add(Restrictions.le(Timesheet.ENDDATE, endDate))
			.createAlias(Timesheet.PROJECTACTIVITY, "pActivity")
			.addOrder(Order.asc("pActivity." + Projectactivity.PROJECT))
			.addOrder(Order.asc(Timesheet.INITDATE));

		addJoins(crit, joins);
		
		return crit.list();
	}
	
	/**
	 * 
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> findWithOperationByResource(Employee employee, Date initDate, Date endDate, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())		
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.add(Restrictions.isNull(Timesheet.PROJECTACTIVITY))
			.add(Restrictions.eq(Timesheet.EMPLOYEE, employee))
			.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
			.add(Restrictions.ge(Timesheet.INITDATE, initDate))
			.add(Restrictions.le(Timesheet.ENDDATE, endDate))
			.addOrder(Order.asc(Timesheet.OPERATION))
			.addOrder(Order.asc(Timesheet.INITDATE));

		addJoins(crit, joins);
		
		return crit.list();
	}

	/**
	 * Find unique time sheet operation
	 * @param operation
	 * @param consUser
	 * @param initDate
	 * @param endDate
	 * @return
	 */
	public Timesheet findByOperation(Operation operation, Employee employee,
			Date initDate, Date endDate) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Timesheet.EMPLOYEE, employee))
			.add(Restrictions.eq(Timesheet.OPERATION, operation))
			.add(Restrictions.eq(Timesheet.INITDATE, initDate))
			.add(Restrictions.eq(Timesheet.ENDDATE, endDate));
		
		return (Timesheet) crit.uniqueResult();
	}

	/**
	 * Find Time Sheets operations
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> findByOperation(Employee employee, Date initDate,
			Date endDate, List<String> joins, String minStatus, String maxStatus) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Timesheet.EMPLOYEE, employee))
			.add(Restrictions.isNotNull(Timesheet.OPERATION))
			.add(Restrictions.eq(Timesheet.INITDATE, initDate))
			.add(Restrictions.eq(Timesheet.ENDDATE, endDate));
	
		if (minStatus != null && maxStatus != null) {
			
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.eq(Timesheet.STATUS, minStatus));
			disjunction.add(Restrictions.eq(Timesheet.STATUS, maxStatus));
			
			if (Constants.TIMESTATUS_APP0.equals(minStatus) && (
					Constants.TIMESTATUS_APP2.equals(maxStatus) ||
					Constants.TIMESTATUS_APP3.equals(maxStatus))) {
				
				disjunction.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP1));
			}
			
			if (Constants.TIMESTATUS_APP3.equals(maxStatus) && (
					Constants.TIMESTATUS_APP0.equals(minStatus) ||
					Constants.TIMESTATUS_APP1.equals(minStatus))) {
				
				disjunction.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP2));
			}
			
			crit.add(disjunction);
		}

		addJoins(crit, joins);
		
		return crit.list();
	}

	
	/**
	 * Is in status
	 * @param idEmployee
	 * @param id
	 * @param initDate
	 * @param endDate
	 * @param status
	 * @param filterUser 
	 * @return
	 */
	public boolean isStatusResource(Integer idEmployee, Integer id, Date initDate, Date endDate, String status, Employee filterUser) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Timesheet.STATUS, status))
			.add(Restrictions.eq(Timesheet.INITDATE, initDate))
			.add(Restrictions.eq(Timesheet.ENDDATE, endDate))
			.add(Restrictions.eq(Timesheet.EMPLOYEE, new Employee(idEmployee)));
		
		if (Constants.TIMESTATUS_APP1.equals(status)) {
			
			if (id.equals(0)) {
				crit.add(Restrictions.isNull(Timesheet.PROJECTACTIVITY));
			}
			else {
				crit.createCriteria(Timesheet.PROJECTACTIVITY)
					.add(Restrictions.eq(Projectactivity.PROJECT, new Project(id)));
			}
		}
		else if (Constants.TIMESTATUS_APP2.equals(status)) {
			Criteria critFilter = crit.createCriteria(Timesheet.PROJECTACTIVITY)
				.createCriteria(Projectactivity.PROJECT);
			
			// Filter by User. Settings by company for last approval.
			
			Resourceprofiles profile = filterUser.getResourceprofiles();
			
			if (profile.getIdProfile() == Constants.ROLE_FM) {
				
				critFilter.add(Restrictions.eq(Project.EMPLOYEEBYFUNCTIONALMANAGER, filterUser));
			}
			else if (profile.getIdProfile() == Constants.ROLE_PM) {
				
				critFilter.add(Restrictions.eq(Project.EMPLOYEEBYPROJECTMANAGER, filterUser));
			}
			else {
				
				critFilter.add(Restrictions.eq(Project.PERFORMINGORG, filterUser.getPerformingorg()));
			}
		}
		
		Integer count = (Integer) crit.uniqueResult();
		
		return (count != null && count > 0);
	}
	
	/**
	 * Is time status
	 * @param idEmployee
	 * @param initDate
	 * @param endDate
	 * @param status
	 * @param filterUser
	 * @param inOperation
	 * @return
	 */
	public boolean isTimeStatus(Integer idEmployee, Date initDate,
			Date endDate, String status, Employee filterUser, boolean inOperation) {
		
		boolean isStatus = false;
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Timesheet.STATUS, status))
			.add(Restrictions.eq(Timesheet.INITDATE, initDate))
			.add(Restrictions.eq(Timesheet.ENDDATE, endDate))
			.add(Restrictions.eq(Timesheet.EMPLOYEE, new Employee(idEmployee)));
		
		if (inOperation) {
			crit.add(Restrictions.isNotNull(Timesheet.OPERATION));
		}
		else {
			Resourceprofiles profile = filterUser.getResourceprofiles();
			
			Criterion restriction = null;
			
			if (profile.getIdProfile() == Constants.ROLE_FM) {
				restriction = Restrictions.eq(Project.EMPLOYEEBYFUNCTIONALMANAGER, filterUser);
			}
			else if (profile.getIdProfile() == Constants.ROLE_PM) {
				restriction = Restrictions.eq(Project.EMPLOYEEBYPROJECTMANAGER, filterUser);
			}
			else {
				restriction = Restrictions.eq(Project.PERFORMINGORG, filterUser.getPerformingorg());
			}
			
			Criteria critFilter = crit.createCriteria(Timesheet.PROJECTACTIVITY)
				.createCriteria(Projectactivity.PROJECT);
			critFilter.add(restriction);
		}
		
		isStatus = (Integer)crit.uniqueResult() > 0;

		if (!isStatus && !inOperation) { isStatus = isTimeStatus(idEmployee, initDate, endDate, status, filterUser, true); }
		
		return isStatus;
	}
	
	
	/**
	 * Get Hours resource
	 * @param idEmployee
	 * @param id
	 * @param initDate
	 * @param endDate
	 * @param minStatus
	 * @param maxStatus
	 * @return
	 */
	public double getHoursResource(Integer idEmployee, Integer id,
			Date initDate, Date endDate, String minStatus, Employee filterUser) {
		
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.sum(Timesheet.HOURSDAY1));
		proList.add(Projections.sum(Timesheet.HOURSDAY2));
		proList.add(Projections.sum(Timesheet.HOURSDAY3));
		proList.add(Projections.sum(Timesheet.HOURSDAY4));
		proList.add(Projections.sum(Timesheet.HOURSDAY5));
		proList.add(Projections.sum(Timesheet.HOURSDAY6));
		proList.add(Projections.sum(Timesheet.HOURSDAY7));
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(proList)
			.add(Restrictions.disjunction()
					.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP1))
					.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP2))
					.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
				)
			.add(Restrictions.eq(Timesheet.INITDATE, initDate))
			.add(Restrictions.eq(Timesheet.ENDDATE, endDate))
			.add(Restrictions.eq(Timesheet.EMPLOYEE, new Employee(idEmployee)));
		
		if (Constants.TIMESTATUS_APP1.equals(minStatus)) {
			crit.createCriteria(Timesheet.PROJECTACTIVITY)
					.add(Restrictions.eq(Projectactivity.PROJECT, new Project(id)));
		}
		else if (Constants.TIMESTATUS_APP2.equals(minStatus)) {
			
			// Filter by User. Settings by company for last approval.
			
			Criteria critFilter = crit.createCriteria(Timesheet.PROJECTACTIVITY)
				.createCriteria(Projectactivity.PROJECT);
			
			Resourceprofiles profile = filterUser.getResourceprofiles();
			
			if (profile.getIdProfile() == Constants.ROLE_FM) {
				
				critFilter.add(Restrictions.eq(Project.EMPLOYEEBYFUNCTIONALMANAGER, filterUser));
			}
			else if (profile.getIdProfile() == Constants.ROLE_PMO) {
				
				critFilter.add(Restrictions.eq(Project.PERFORMINGORG, filterUser.getPerformingorg()));
			}
		}
		Object[] hoursList = (Object[]) crit.uniqueResult();
		
		double hours = 0;
		
		if (hoursList != null) {
			hours += (hoursList[0] == null?0:(Double)hoursList[0]);
			hours += (hoursList[1] == null?0:(Double)hoursList[1]);
			hours += (hoursList[2] == null?0:(Double)hoursList[2]);
			hours += (hoursList[3] == null?0:(Double)hoursList[3]);
			hours += (hoursList[4] == null?0:(Double)hoursList[4]);
			hours += (hoursList[5] == null?0:(Double)hoursList[5]);
			hours += (hoursList[6] == null?0:(Double)hoursList[6]);
		}

		return hours;
	}
	
	/**
	 * Get Hours resource by operation
	 * @param idEmployee
	 * @param initDate
	 * @param endDate
	 * @return
	 */
	public double getHoursResourceOpeartion(Integer idEmployee,
			Date initDate, Date endDate) {
		
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.sum(Timesheet.HOURSDAY1));
		proList.add(Projections.sum(Timesheet.HOURSDAY2));
		proList.add(Projections.sum(Timesheet.HOURSDAY3));
		proList.add(Projections.sum(Timesheet.HOURSDAY4));
		proList.add(Projections.sum(Timesheet.HOURSDAY5));
		proList.add(Projections.sum(Timesheet.HOURSDAY6));
		proList.add(Projections.sum(Timesheet.HOURSDAY7));
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(proList)
			.add(Restrictions.disjunction()
					.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP1))
					.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP2))
					.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
				)
			.add(Restrictions.eq(Timesheet.INITDATE, initDate))
			.add(Restrictions.eq(Timesheet.ENDDATE, endDate))
			.add(Restrictions.eq(Timesheet.EMPLOYEE, new Employee(idEmployee)))
			.add(Restrictions.isNotNull(Timesheet.OPERATION));
		
		Object[] hoursList = (Object[]) crit.uniqueResult();
		
		double hours = 0;
		
		if (hoursList != null) {
			hours += (hoursList[0] == null?0:(Double)hoursList[0]);
			hours += (hoursList[1] == null?0:(Double)hoursList[1]);
			hours += (hoursList[2] == null?0:(Double)hoursList[2]);
			hours += (hoursList[3] == null?0:(Double)hoursList[3]);
			hours += (hoursList[4] == null?0:(Double)hoursList[4]);
			hours += (hoursList[5] == null?0:(Double)hoursList[5]);
			hours += (hoursList[6] == null?0:(Double)hoursList[6]);
		}

		return hours;
	}

	/**
	 * Calculate Fte for inputed hours in project
	 * 
	 * @param project
	 * @param member
	 * @param firstWeekDay
	 * @param lastWeekDay
	 * @return
	 */
	public double getHoursResource(Project project, Employee member, Date firstWeekDay,
			Date lastWeekDay) {
		
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.sum(Timesheet.HOURSDAY1));
		proList.add(Projections.sum(Timesheet.HOURSDAY2));
		proList.add(Projections.sum(Timesheet.HOURSDAY3));
		proList.add(Projections.sum(Timesheet.HOURSDAY4));
		proList.add(Projections.sum(Timesheet.HOURSDAY5));
		proList.add(Projections.sum(Timesheet.HOURSDAY6));
		proList.add(Projections.sum(Timesheet.HOURSDAY7));
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(proList)
			.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
			.add(Restrictions.eq(Timesheet.EMPLOYEE, member));
		
		if (firstWeekDay != null && lastWeekDay != null) {
		
			crit.add(Restrictions.eq(Timesheet.INITDATE, firstWeekDay))
			.add(Restrictions.eq(Timesheet.ENDDATE, lastWeekDay));
		}
		
		// Aliases 
		//
		crit.createAlias(Timesheet.PROJECTACTIVITY, "pa", CriteriaSpecification.LEFT_JOIN);
		
		// Projects or operations
		crit.add(Restrictions.disjunction()
				.add(Restrictions.isNotNull(Timesheet.OPERATION))
				.add(Restrictions.eq( "pa."+Projectactivity.PROJECT, project)));

		Object[] hoursList = (Object[]) crit.uniqueResult();
		
		double hours = 0;
		
		if (hoursList != null) {
			hours += (hoursList[0] == null?0:(Double)hoursList[0]);
			hours += (hoursList[1] == null?0:(Double)hoursList[1]);
			hours += (hoursList[2] == null?0:(Double)hoursList[2]);
			hours += (hoursList[3] == null?0:(Double)hoursList[3]);
			hours += (hoursList[4] == null?0:(Double)hoursList[4]);
			hours += (hoursList[5] == null?0:(Double)hoursList[5]);
			hours += (hoursList[6] == null?0:(Double)hoursList[6]);
		}
		
		return hours;
	}
	
	public double getHoursResourceInDates(Project project, Employee member, Date since, Date until,
			Integer idResourcePool, Operation operation, Projectactivity activity) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		Criteria employeeCrit = null;
		
		if (member != null || idResourcePool != null) {
			employeeCrit = crit.createCriteria(Timesheet.EMPLOYEE);
		}
		
		if (idResourcePool != null) {
			employeeCrit.add(Restrictions.eq(Employee.RESOURCEPOOL, new Resourcepool(idResourcePool)));
		}
		
		if (member != null) { employeeCrit.add(Restrictions.idEq(member.getIdEmployee())); }
		
		if (since != null && until != null) {
		
			crit.add(Restrictions.disjunction()
				.add(Restrictions.between(Timesheet.INITDATE, since, until))
				.add(Restrictions.between(Timesheet.ENDDATE, since, until))
				.add(Restrictions.and(
						Restrictions.le(Timesheet.INITDATE, since),
						Restrictions.ge(Timesheet.ENDDATE, until)
					)
				)
			);
		}
		
		if (project != null) {

            crit.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3));

            Criteria activitiesCrit = crit.createCriteria(Timesheet.PROJECTACTIVITY)
                                                .add(Restrictions.eq(Projectactivity.PROJECT, project));

            // Select only timesheet whose activity is control account
            Criteria wbsnodeCrit = activitiesCrit.createCriteria(Projectactivity.WBSNODE)
                    .add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true));
		}
		else if (operation != null) {

			crit.add(Restrictions.or(
						Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP2),
						Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3)))
				.add(Restrictions.eq(Timesheet.OPERATION, operation));
		}
		else if (activity != null) {
			
			
			crit.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
				.add(Restrictions.eq(Timesheet.PROJECTACTIVITY, activity));
		}
		
		return calcHours(crit.list(), since, until);
	}

    /**
     * Get hours in status of project
     *
     * @param project
     * @param status
     * @return
     */
    public double getHoursResource(Project project, String status) {

		Criteria crit = getSession().createCriteria(getPersistentClass());

        crit.add(Restrictions.eq(Timesheet.STATUS, status == null ? Constants.TIMESTATUS_APP3 : status));

        Criteria activitiesCrit = crit.createCriteria(Timesheet.PROJECTACTIVITY)
                                            .add(Restrictions.eq(Projectactivity.PROJECT, project));

        // Select only timesheet whose activity is control account
        Criteria wbsnodeCrit = activitiesCrit.createCriteria(Projectactivity.WBSNODE)
                .add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true));

		return calcHours(crit.list(), null, null);
	}
	
	private double calcHours(List<Timesheet> timeSheets, Date since, Date until) {
		
		double hours = 0;
		
		for (Timesheet item : timeSheets) {
			
			Calendar tempCal = DateUtil.getCalendar();
			tempCal.setTime(item.getInitDate());
			
			if ((since == null || until == null) || DateUtil.between(since, until, tempCal.getTime())) {
				hours += (item.getHoursDay1() == null?0:item.getHoursDay1());
			}
			tempCal.add(Calendar.DAY_OF_MONTH, 1);
			
			if ((since == null || until == null) || DateUtil.between(since, until, tempCal.getTime())) {
				hours += (item.getHoursDay2() == null?0:item.getHoursDay2());
			}
			tempCal.add(Calendar.DAY_OF_MONTH, 1);

			if ((since == null || until == null) || DateUtil.between(since, until, tempCal.getTime())) {
				hours += (item.getHoursDay3() == null?0:item.getHoursDay3());
			}
			tempCal.add(Calendar.DAY_OF_MONTH, 1);
			if ((since == null || until == null) || DateUtil.between(since, until, tempCal.getTime())) {
				hours += (item.getHoursDay4() == null?0:item.getHoursDay4());
			}
			tempCal.add(Calendar.DAY_OF_MONTH, 1);
			if ((since == null || until == null) || DateUtil.between(since, until, tempCal.getTime())) {
				hours += (item.getHoursDay5() == null?0:item.getHoursDay5());
			}
			tempCal.add(Calendar.DAY_OF_MONTH, 1);
			if ((since == null || until == null) || DateUtil.between(since, until, tempCal.getTime())) {
				hours += (item.getHoursDay6() == null?0:item.getHoursDay6());
			}
			tempCal.add(Calendar.DAY_OF_MONTH, 1);
			if ((since == null || until == null) || DateUtil.between(since, until, tempCal.getTime())) {
				hours += (item.getHoursDay7() == null?0:item.getHoursDay7());
			}
		}
		return hours;
	}

	@SuppressWarnings("unchecked")
	public double getHoursInProject(Project project, Date since, Date until) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3));
		
		crit.createCriteria(Timesheet.PROJECTACTIVITY)
			.add(Restrictions.eq(Projectactivity.PROJECT, project));

		return calcHours(crit.list(), since, until);
	}

	@SuppressWarnings("unchecked")
	public double getHoursInActivity(Projectactivity activity, Date since, Date until) {
		
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3));
		
		crit.add(Restrictions.eq(Timesheet.PROJECTACTIVITY, activity));

		return calcHours(crit.list(), since, until);
	}
	
	/**
	 * Calculate Fte for inputed hours in project
	 * @param project
	 * @param member
	 * @param firstWeekDay
	 * @param lastWeekDay
	 * @return
	 */
	public double getHoursResource(Project project, Teammember member, Date firstWeekDay,
			Date lastWeekDay) {
		
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.sum(Timesheet.HOURSDAY1));
		proList.add(Projections.sum(Timesheet.HOURSDAY2));
		proList.add(Projections.sum(Timesheet.HOURSDAY3));
		proList.add(Projections.sum(Timesheet.HOURSDAY4));
		proList.add(Projections.sum(Timesheet.HOURSDAY5));
		proList.add(Projections.sum(Timesheet.HOURSDAY6));
		proList.add(Projections.sum(Timesheet.HOURSDAY7));
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(proList)
			.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3));
		
		crit.createCriteria(Timesheet.EMPLOYEE)
			.createCriteria(Employee.TEAMMEMBERS)
			.add(Restrictions.idEq(member.getIdTeamMember()));
		
		if (firstWeekDay != null && lastWeekDay != null) {
		
			crit.add(Restrictions.eq(Timesheet.INITDATE, firstWeekDay))
				.add(Restrictions.eq(Timesheet.ENDDATE, lastWeekDay));
		}
		
		if (project == null) {
			crit.add(Restrictions.eq(Timesheet.PROJECTACTIVITY, member.getProjectactivity()));
		}
		else {
			
			crit.createCriteria(Timesheet.PROJECTACTIVITY)
				.add(Restrictions.eq(Projectactivity.PROJECT, project));
		}
		
		Object[] hoursList = (Object[]) crit.uniqueResult();
		
		double hours = 0;
		
		if (hoursList != null) {
			hours += (hoursList[0] == null?0:(Double)hoursList[0]);
			hours += (hoursList[1] == null?0:(Double)hoursList[1]);
			hours += (hoursList[2] == null?0:(Double)hoursList[2]);
			hours += (hoursList[3] == null?0:(Double)hoursList[3]);
			hours += (hoursList[4] == null?0:(Double)hoursList[4]);
			hours += (hoursList[5] == null?0:(Double)hoursList[5]);
			hours += (hoursList[6] == null?0:(Double)hoursList[6]);
		}
		
		return hours;
	}

	/**
	 * Find time sheet
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @param activity
	 * @param status
	 * @return
	 */
	public Timesheet findByResource(Employee employee, Date initDate,
			Date endDate, Projectactivity activity, String status) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Timesheet.PROJECTACTIVITY, activity))
			.add(Restrictions.eq(Timesheet.EMPLOYEE, employee))
			.add(Restrictions.eq(Timesheet.INITDATE, initDate))
			.add(Restrictions.eq(Timesheet.ENDDATE, endDate))
			.add(Restrictions.eq(Timesheet.STATUS, status));
			

			return (Timesheet) crit.uniqueResult();
	}
	
	/**
	 * 
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> findByProject(Employee employee, Project project, Date initDate, Date endDate, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())		
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)			
			.add(Restrictions.eq(Timesheet.EMPLOYEE, employee))
			.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
			.add(Restrictions.ge(Timesheet.INITDATE, initDate))
			.add(Restrictions.le(Timesheet.ENDDATE, endDate))			
			.addOrder(Order.asc(Timesheet.INITDATE));
		
		crit.createCriteria(Timesheet.PROJECTACTIVITY)
			.add(Restrictions.eq(Projectactivity.PROJECT, project))
			.addOrder(Order.asc(Projectactivity.PROJECT));		
		
		if(joins != null) {
			addJoins(crit, joins);	
		}
		
		return crit.list();
	}

	/**
	 * Hours by resource in dates in status APP3
	 * 
	 * @param since
	 * @param until
	 * @param activities
	 * @param employee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getHoursResourceInDates(Date since, Date until, List<Projectactivity> activities, Employee employee) {
		
		// Declare response
		List<Object[]> result = null;
		
		if (ValidateUtil.isNotNull(activities)) {
			
			Query query = getSession().createQuery(
				"select project.idProject, activity.idActivity,  " +
					"SUM(" +
						"(case when (timesheet.hoursDay1 is not null and TO_DAYS(timesheet.initDate) >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) <= TO_DAYS(:until)) THEN timesheet.hoursDay1 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay2 is not null and TO_DAYS(timesheet.initDate) + 1 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 1 <= TO_DAYS(:until)) THEN timesheet.hoursDay2 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay3 is not null and TO_DAYS(timesheet.initDate) + 2 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 2 <= TO_DAYS(:until)) THEN timesheet.hoursDay3 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay4 is not null and TO_DAYS(timesheet.initDate) + 3 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 3 <= TO_DAYS(:until)) THEN timesheet.hoursDay4 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay5 is not null and TO_DAYS(timesheet.initDate) + 4 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 4 <= TO_DAYS(:until)) THEN timesheet.hoursDay5 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay6 is not null and TO_DAYS(timesheet.initDate) + 5 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 5 <= TO_DAYS(:until)) THEN timesheet.hoursDay6 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay7 is not null and TO_DAYS(timesheet.initDate) + 6 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 6 <= TO_DAYS(:until)) THEN timesheet.hoursDay7 ELSE 0 end )" +
				    ") " +
			    "from Projectactivity activity " +
			    	"join activity.project as project "+
		    		"join activity.wbsnode as wbsnode " +
		    		"left join activity.timesheets as timesheet "+
		    		"left join timesheet.employee as employee "+
			    	"left join employee.contact as contact "+
				"where activity in(:activities) " +
						"and employee.idEmployee = :idEmployee " +
						"and timesheet.status = :app3 " +
				"group by project.idProject, activity.idActivity " +
				"order by project.accountingCode asc, activity.idActivity asc"
			);
			
			query.setParameterList("activities", activities);
			query.setInteger("idEmployee", employee.getIdEmployee());
			query.setDate("since", since);
			query.setDate("until", until);
			query.setString("app3", Constants.TIMESTATUS_APP3);
			
			result = query.list();
		}
		else {
			
			// Initialize list
			result = new ArrayList<Object[]>();
		}
		
		return result;
	}

	/**
	 * Hours by resource in dates in status APP2 or APP3
	 * 
	 * @param since
	 * @param until
	 * @param operations
	 * @param employee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getHoursResourceInDatesOperation(Date since, Date until,
			List<Operation> operations, Employee employee) {
		
		// Declare response
		List<Object[]> result = null;
				
		if (ValidateUtil.isNotNull(operations)) {
			
			Query query = getSession().createQuery(
				"select operation.idOperation,  " +
					"SUM(" +
						"(case when (timesheet.hoursDay1 is not null and TO_DAYS(timesheet.initDate) >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) <= TO_DAYS(:until)) THEN timesheet.hoursDay1 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay2 is not null and TO_DAYS(timesheet.initDate) + 1 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 1 <= TO_DAYS(:until)) THEN timesheet.hoursDay2 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay3 is not null and TO_DAYS(timesheet.initDate) + 2 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 2 <= TO_DAYS(:until)) THEN timesheet.hoursDay3 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay4 is not null and TO_DAYS(timesheet.initDate) + 3 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 3 <= TO_DAYS(:until)) THEN timesheet.hoursDay4 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay5 is not null and TO_DAYS(timesheet.initDate) + 4 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 4 <= TO_DAYS(:until)) THEN timesheet.hoursDay5 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay6 is not null and TO_DAYS(timesheet.initDate) + 5 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 5 <= TO_DAYS(:until)) THEN timesheet.hoursDay6 ELSE 0 end ) + " +
						"(case when (timesheet.hoursDay7 is not null and TO_DAYS(timesheet.initDate) + 6 >= TO_DAYS(:since) and TO_DAYS(timesheet.initDate) + 6 <= TO_DAYS(:until)) THEN timesheet.hoursDay7 ELSE 0 end )" +
				    ") " +
			    "from Timesheet timesheet " +
			    	"join timesheet.operation as operation "+
		    		"join timesheet.employee as employee "+
			    	"join employee.contact as contact "+
				"where operation in(:operations) " +
						"and employee.idEmployee = :idEmployee " +
						"and (timesheet.status = :app2 or timesheet.status = :app3) " +
				"group by operation.idOperation " +
				"order by operation.operationName asc, operation.idOperation asc"
			);
			
			query.setParameterList("operations", operations);
			query.setInteger("idEmployee", employee.getIdEmployee());
			query.setDate("since", since);
			query.setDate("until", until);
			query.setString("app2", Constants.TIMESTATUS_APP2);
			query.setString("app3", Constants.TIMESTATUS_APP3);
			
			result = query.list();
		}
		else {
			
			// Initialize list
			result = new ArrayList<Object[]>();
		}
		
		return result;
	}

	/**
	 * The node has hours in APP1, APP2 or APP3
	 * 
	 * @param wbsNode
	 * @return
	 */
	public boolean isInputedApprovedHours(Wbsnode wbsNode) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.disjunction() 
				.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP1))
				.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP2))
				.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3)))
			.createCriteria(Timesheet.PROJECTACTIVITY)
				.add(Restrictions.eq(Projectactivity.WBSNODE, wbsNode));
		
		return (Integer)crit.uniqueResult() > 0;
	}
	
	/**
	 * The node has hours in APP0
	 * 
	 * @param wbsNode
	 * @return
	 */
	public boolean isInputedHours(Wbsnode wbsNode) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP0))
			.createCriteria(Timesheet.PROJECTACTIVITY)
				.add(Restrictions.eq(Projectactivity.WBSNODE, wbsNode));
		
		return (Integer)crit.uniqueResult() > 0;
	}

	/**
	 * Calc hours to the activiy for status
	 * @param idActivity
	 * @param timestatus
	 * @return
	 */
	public double calcHoursActivityForStatus(Integer idActivity, String timestatus) {
		
		Double hours 	= 0.0;
		String hoursDay = Timesheet.HOURSDAY1;

		hoursDay = hoursDay.substring(0, hoursDay.length() - 1);
		
		for (int i = 1; i < 8; i++) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
					.setProjection(Projections.sum(hoursDay + i))
					.add(Restrictions.eq(Timesheet.STATUS, timestatus))
					.add(Restrictions.eq(Timesheet.PROJECTACTIVITY, new Projectactivity(idActivity)));

			hours += (Double)crit.uniqueResult() == null ? 0.0 : (Double)crit.uniqueResult();
		}
		
		return hours;
	}
	
	/**
	 * Search timesheet by filters
	 * @param list
	 * @param properyOrder
	 * @param typeOrder
	 * @param joins 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> findByFilters(ArrayList<PropertyRelation> list, String propertyOrder, String typeOrder, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		if (joins != null) {
			for (String join : joins) {
				crit.setFetchMode(join, FetchMode.JOIN);
			}
		}
		
		for(PropertyRelation propertyRelation : list){
			crit.add(findRestriction(propertyRelation));
		}
		
		addOrder(crit, propertyOrder, typeOrder);
		
		return crit.list();
	}

	/**
	 * Find restriction type
	 * @param propertyRelation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Criterion findRestriction(PropertyRelation propertyRelation) {
		
		Criterion criterion = null;
		
		if(propertyRelation.getRestriction() == Constants.EQUAL_RESTRICTION){
			criterion = Restrictions.eq(propertyRelation.getProperty(), propertyRelation.getRelation());
		}
		else if(propertyRelation.getRestriction() == Constants.NOT_EQUAL_RESTRICTION){
			criterion = Restrictions.ne(propertyRelation.getProperty(), propertyRelation.getRelation());
		}
		else if(propertyRelation.getRestriction() == Constants.GREATER_OR_EQUAL_RESTRICTION){
			criterion = Restrictions.ge(propertyRelation.getProperty(), propertyRelation.getRelation());
		}
		else if(propertyRelation.getRestriction() == Constants.LESS_OR_EQUAL_RESTRICTION){
			criterion = Restrictions.le(propertyRelation.getProperty(), propertyRelation.getRelation());
		}
		else if(propertyRelation.getRestriction() == Constants.ILIKE_RESTRICTION){
			criterion = Restrictions.ilike(propertyRelation.getProperty(), propertyRelation.getRelation());
		}
		else if(propertyRelation.getRestriction() == Constants.DISJUNCTION){
			ArrayList<PropertyRelation> listDisjunctions = (ArrayList<PropertyRelation>) propertyRelation.getRelation();
			
			Disjunction d = Restrictions.disjunction();
			
			for(PropertyRelation propertyRelationDis : listDisjunctions){
				d.add(findRestriction(propertyRelationDis));
			}
			
			criterion = d;
		}
		
		return criterion;
	}


	/**
	 * Find time sheet affected for update date out
	 * 
	 * @param teammember
	 * @param newDateOut
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> timeSheetsForUpdateOut(Teammember teammember, Date newDateOut) {

		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Timesheet.PROJECTACTIVITY, teammember.getProjectactivity()))
			.add(Restrictions.eq(Timesheet.EMPLOYEE, teammember.getEmployee()))
			.add(Restrictions.conjunction()
					.add(Restrictions.le(Timesheet.INITDATE, DateUtil.getFirstWeekDay(teammember.getDateOut())))
					.add(Restrictions.ge(Timesheet.ENDDATE, DateUtil.getLastWeekDay(newDateOut))));
			

		return crit.list();
	}
	
	/**
	 * Find time sheet affected for update date in
	 * 
	 * @param teammember
	 * @param newDateOut
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Timesheet> timeSheetsForUpdateIn(Teammember teammember, Date newDateIn) {

		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Timesheet.PROJECTACTIVITY, teammember.getProjectactivity()))
			.add(Restrictions.eq(Timesheet.EMPLOYEE, teammember.getEmployee()))
			.add(Restrictions.conjunction()
					.add(Restrictions.ge(Timesheet.INITDATE, DateUtil.getFirstWeekDay(teammember.getDateIn())))
					.add(Restrictions.le(Timesheet.ENDDATE, DateUtil.getLastWeekDay(newDateIn))));
			

		return crit.list();
	}

	/**
	 * Get timesheets in app3 by resources and dates
	 * 
	 * @param resources
	 * @param since
	 * @param until
	 * @param showOperations
	 * @param idProjects
	 * @param user
     * @param idPMs
     * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceTimeWrap> capacityRunning(List<Employee> resources, Date since, Date until,
                                                  Boolean showOperations, Integer[] idProjects,
                                                  Employee user, Integer[] idPMs) {

        List<ResourceTimeWrap> operations = new ArrayList<ResourceTimeWrap>();

        String where 	= "";
		String groupBy 	= "group by ";
		String orderBy 	= "order by ";
		
		String q = "SELECT NEW es.sm2.openppm.core.model.wrap.ResourceTimeWrap("+
				"e.idEmployee, "+
				"c.fullName, "+
				"ts.initDate, "+
				"ts.endDate, "+
				"SUM(coalesce(ts.hoursDay1, 0) + coalesce(ts.hoursDay2, 0) + coalesce(ts.hoursDay3, 0) + coalesce(ts.hoursDay4, 0) + "+
					"coalesce(ts.hoursDay5, 0) + coalesce(ts.hoursDay6, 0) + coalesce(ts.hoursDay7, 0))" +
				") "+
			"FROM Timesheet ts " +
			"LEFT JOIN ts.projectactivity pa " +
			"LEFT JOIN pa.project p " +
			"JOIN ts.employee e "+
			"JOIN e.contact c "+
            "LEFT JOIN p.employeeByProjectManager pm ";
		
		// Names where
		//
		where += FilterUtil.addFilterAnd(where, "ts.status = :status ");

        where += FilterUtil.addFilterAnd(where, "ts.projectactivity is not null ");

        if (ValidateUtil.isNotNull(idProjects)) {
            where += FilterUtil.addFilterAnd(where, "p.idProject IN (:idProjects) ");
        }
		if (ValidateUtil.isNotNull(resources)) {
			where += FilterUtil.addFilterAnd(where, "ts.employee IN (:resources) ");
		}
		if (since != null) {
			where += FilterUtil.addFilterAnd(where, "ts.initDate >= :initDate ");
		}
		if (until != null) {
			where += FilterUtil.addFilterAnd(where, "ts.initDate <= :endDate ");
		}

        // PM Projects
        if (user.getResourceprofiles().getIdProfile() == Constants.ROLE_PM) {
            where += FilterUtil.addFilterAnd(where, "pm.idEmployee = :idPM ");
        }
        //TODO no se si el filtro esta bien o deberia ser asi, lo comento para dejarlo como estaba
//        else if (ValidateUtil.isNotNull(idPMs)) {
//            where += FilterUtil.addFilterAnd(where, "pm.idEmployee IN (:idPMs) ");
//        }

		// Group by
		groupBy += "e.idEmployee, c.fullName, ts.initDate, ts.endDate ";
		
		// Order by
		orderBy += "c.fullName, ts.initDate";
		
		// Query
		Query query = getSession().createQuery(q+where+groupBy+orderBy);
		
		// Values where
		//
		query.setString("status", Constants.TIMESTATUS_APP3);
		
		if (ValidateUtil.isNotNull(resources)) {
			query.setParameterList("resources", resources);
		}
		
		if (since != null) { query.setDate("initDate", since); }
		if (until != null) { query.setDate("endDate", until); }
		
		if (ValidateUtil.isNotNull(idProjects)) {
			query.setParameterList("idProjects", idProjects);
		}

        // PM Projects
        if (user.getResourceprofiles().getIdProfile() == Constants.ROLE_PM) {
            query.setParameter("idPM", user.getIdEmployee());
        }
        //TODO si hay un recurso que tiene proyectos con dos PMs saldran los datos de los dos proyectos aunque se filtre por uno
//        else if (ValidateUtil.isNotNull(idPMs)) {
//            query.setParameterList("idPMs", idPMs);
//        }

        List<ResourceTimeWrap> activities = query.list();

        // Show operations
        if (showOperations != null && showOperations) {

            // Select for operations
            //
            where 		= "";
            groupBy 	= "group by ";
            orderBy 	= "order by ";

            q = "SELECT NEW es.sm2.openppm.core.model.wrap.ResourceTimeWrap("+
                    "op.operationName, "+
                    "ts.initDate, "+
                    "ts.endDate, "+
                    "e.idEmployee, "+
                    "SUM(coalesce(ts.hoursDay1, 0) + coalesce(ts.hoursDay2, 0) + coalesce(ts.hoursDay3, 0) + coalesce(ts.hoursDay4, 0) + "+
                    "coalesce(ts.hoursDay5, 0) + coalesce(ts.hoursDay6, 0) + coalesce(ts.hoursDay7, 0))" +
                    ") "+
                    "FROM Timesheet ts " +
                    "JOIN ts.operation op "+
                    "JOIN ts.employee e ";

            // Names where
            where += FilterUtil.addFilterAnd(where, "ts.status = :status ");

            if (ValidateUtil.isNotNull(resources)) {
                where += FilterUtil.addFilterAnd(where, "ts.employee IN (:resources) ");
            }
            if (since != null) {
                where += FilterUtil.addFilterAnd(where, "ts.initDate >= :initDate ");
            }
            if (until != null) {
                where += FilterUtil.addFilterAnd(where, "ts.initDate <= :endDate ");
            }

            // Group by
            groupBy += "ts.initDate, ts.endDate, op.operationName, e.idEmployee ";

            // Order by
            orderBy += "ts.initDate";

            // Query
            query = getSession().createQuery(q+where+groupBy+orderBy);

            // Values where
            query.setString("status", Constants.TIMESTATUS_APP3);

            if (ValidateUtil.isNotNull(resources)) {
                query.setParameterList("resources", resources);
            }
            if (since != null) { query.setDate("initDate", since); }
            if (until != null) { query.setDate("endDate", until); }

            operations = query.list();
        }

        // Concatenate selects
        //
        List<ResourceTimeWrap> activitiesAndOperations = new ArrayList<ResourceTimeWrap>();

        activitiesAndOperations.addAll(activities);
        activitiesAndOperations.addAll(operations);

        return activitiesAndOperations;
	}

	/**
	 * Select capacity running resource by project
	 * 
	 * @param employee
	 * @param since
	 * @param until
	 * @param user
     * @param settings
     * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceTimeWrap> capacityRunningResourceByProject(Employee employee, Date since, Date until,
                                                                   Employee user, HashMap<String, String> settings) {

        // Select for activities
        //
		String where 	= "";
		String groupBy 	= "group by ";
		String orderBy 	= "order by ";
		
		String q = "SELECT NEW es.sm2.openppm.core.model.wrap.ResourceTimeWrap("+
				"e.idEmployee, "+
				"p.projectName, "+
				"pmContact.fullName, "+
				"ts.initDate, "+
				"ts.endDate, "+
				"SUM(coalesce(ts.hoursDay1, 0) + coalesce(ts.hoursDay2, 0) + coalesce(ts.hoursDay3, 0) + coalesce(ts.hoursDay4, 0) + "+
					"coalesce(ts.hoursDay5, 0) + coalesce(ts.hoursDay6, 0) + coalesce(ts.hoursDay7, 0))" +
				") "+
			"FROM Timesheet ts " +
			"JOIN ts.projectactivity pa " +
			"JOIN pa.project p " +
			"JOIN ts.employee e "+
			"LEFT JOIN p.employeeByProjectManager pm " +
			"LEFT JOIN pm.contact pmContact ";

		// Names where
		where += FilterUtil.addFilterAnd(where, "ts.status = :status ");
		
		if (employee != null) {
			where += FilterUtil.addFilterAnd(where, "ts.employee = :employee ");
		}
		if (since != null) {
			where += FilterUtil.addFilterAnd(where, "ts.initDate >= :initDate ");
		}
		if (until != null) {
			where += FilterUtil.addFilterAnd(where, "ts.initDate <= :endDate ");
		}

        // PM Projects
        if (!SettingUtil.getBoolean(settings, GeneralSetting.PM_VIEW_OTHER_PROJECTS) &&
                user.getResourceprofiles().getIdProfile() == Constants.ROLE_PM) {

            where += FilterUtil.addFilterAnd(where, "pm.idEmployee = :idPM ");
        }

		// Group by
		groupBy += "e.idEmployee, p.projectName, pmContact.fullName, ts.initDate, ts.endDate ";
		
		// Order by
		orderBy += "p.projectName, ts.initDate";
		
		// Query
		Query query = getSession().createQuery(q+where+groupBy+orderBy);

		// Values where
		query.setString("status", Constants.TIMESTATUS_APP3);
		if (employee != null) {
			query.setParameter("employee", employee);
		}
		if (since != null) { query.setDate("initDate", since); }
		if (until != null) { query.setDate("endDate", until); }

        // PM Projects
        if (!SettingUtil.getBoolean(settings, GeneralSetting.PM_VIEW_OTHER_PROJECTS) &&
                user.getResourceprofiles().getIdProfile() == Constants.ROLE_PM) {

            query.setParameter("idPM", user.getIdEmployee());
        }

        List<ResourceTimeWrap> activities = query.list();

        // Select for operations
        //
        where 		= "";
        groupBy 	= "group by ";
        orderBy 	= "order by ";

        q = "SELECT NEW es.sm2.openppm.core.model.wrap.ResourceTimeWrap("+
                "op.operationName, "+
                "ts.initDate, "+
                "ts.endDate, "+
                "SUM(coalesce(ts.hoursDay1, 0) + coalesce(ts.hoursDay2, 0) + coalesce(ts.hoursDay3, 0) + coalesce(ts.hoursDay4, 0) + "+
                "coalesce(ts.hoursDay5, 0) + coalesce(ts.hoursDay6, 0) + coalesce(ts.hoursDay7, 0))" +
                ") "+
                "FROM Timesheet ts " +
                "JOIN ts.operation op ";

        // Names where
        where += FilterUtil.addFilterAnd(where, "ts.status = :status ");

        if (employee != null) {
            where += FilterUtil.addFilterAnd(where, "ts.employee = :employee ");
        }
        if (since != null) {
            where += FilterUtil.addFilterAnd(where, "ts.initDate >= :initDate ");
        }
        if (until != null) {
            where += FilterUtil.addFilterAnd(where, "ts.initDate <= :endDate ");
        }

        // Group by
        groupBy += "ts.initDate, ts.endDate, op.operationName ";

        // Order by
        orderBy += "ts.initDate";

        // Query
        query = getSession().createQuery(q+where+groupBy+orderBy);

        // Values where
        query.setString("status", Constants.TIMESTATUS_APP3);

        if (employee != null) {
            query.setParameter("employee", employee);
        }
        if (since != null) { query.setDate("initDate", since); }
        if (until != null) { query.setDate("endDate", until); }

        List<ResourceTimeWrap> operations = query.list();

        // Concatenate selects
        //
        List<ResourceTimeWrap> activitiesAndOperations = new ArrayList<ResourceTimeWrap>();

        activitiesAndOperations.addAll(activities);
        activitiesAndOperations.addAll(operations);

        return activitiesAndOperations;
	}
	
	/**
	 * Select capacity running resource by job category
	 * 
	 * @param employee
	 * @param since
	 * @param until
	 * @param user
     * @param settings
     * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResourceTimeWrap> capacityRunningResourceByJobCategory(Employee employee, Date since, Date until,
                                                                       Employee user, HashMap<String, String> settings) {
		
		// Select for activities 
		//
		String where 	= "";
		String groupBy 	= "group by ";
		String orderBy 	= "order by ";
		
		String q = "SELECT NEW es.sm2.openppm.core.model.wrap.ResourceTimeWrap("+
				"jc.name, "+
				"p.projectName, "+
				"ts.initDate, "+
				"ts.endDate, "+
				"SUM(coalesce(ts.hoursDay1, 0) + coalesce(ts.hoursDay2, 0) + coalesce(ts.hoursDay3, 0) + coalesce(ts.hoursDay4, 0) + "+
					"coalesce(ts.hoursDay5, 0) + coalesce(ts.hoursDay6, 0) + coalesce(ts.hoursDay7, 0))" +
				") "+
			"FROM Timesheet ts " +
			"JOIN ts.projectactivity pa " +
			"JOIN pa.project p " +
            "LEFT JOIN p.employeeByProjectManager pm " +
			"JOIN ts.employee e "+
			"JOIN e.teammembers tm " +
			"JOIN tm.projectactivity patm " +
			"JOIN tm.jobcategory jc ";

		// Names where
		where += FilterUtil.addFilterAnd(where, "ts.status = :status ");
		
		where += FilterUtil.addFilterAnd(where, "pa.idActivity = patm.idActivity ");
		
		if (employee != null) {
			where += FilterUtil.addFilterAnd(where, "ts.employee = :employee ");
		}
		
		if (since != null) {
			
			where += FilterUtil.addFilterAnd(where, "ts.initDate >= :initDate ");
			
			where += FilterUtil.addFilterAnd(where, "tm.dateIn <= :endDate "); 
		}
		
		if (until != null) {
			
			where += FilterUtil.addFilterAnd(where, "ts.initDate <= :endDate ");
			
			where += FilterUtil.addFilterAnd(where, "tm.dateOut >= :initDate ");
		}

        // PM Projects
        if (!SettingUtil.getBoolean(settings, GeneralSetting.PM_VIEW_OTHER_PROJECTS) &&
                user.getResourceprofiles().getIdProfile() == Constants.ROLE_PM) {

            where += FilterUtil.addFilterAnd(where, "pm.idEmployee = :idPM ");
        }

		// Group by
		groupBy += "jc.name, p.projectName, ts.initDate, ts.endDate ";
		
		// Order by
		orderBy += "jc.name, ts.initDate";
		
		// Query
		Query query = getSession().createQuery(q+where+groupBy+orderBy);
		
		// Values where
		query.setString("status", Constants.TIMESTATUS_APP3);
		
		if (employee != null) {
			query.setParameter("employee", employee);
		}
		if (since != null) { query.setDate("initDate", since); }
		if (until != null) { query.setDate("endDate", until); }

        // PM Projects
        if (!SettingUtil.getBoolean(settings, GeneralSetting.PM_VIEW_OTHER_PROJECTS) &&
                user.getResourceprofiles().getIdProfile() == Constants.ROLE_PM) {

            query.setParameter("idPM", user.getIdEmployee());
        }

		List<ResourceTimeWrap> activities = query.list();		
		
		// Select for operations 
		//
		where 		= "";
		groupBy 	= "group by ";
		orderBy 	= "order by ";
		
		q = "SELECT NEW es.sm2.openppm.core.model.wrap.ResourceTimeWrap("+
				"op.operationName, "+
				"ts.initDate, "+
				"ts.endDate, "+
				"SUM(coalesce(ts.hoursDay1, 0) + coalesce(ts.hoursDay2, 0) + coalesce(ts.hoursDay3, 0) + coalesce(ts.hoursDay4, 0) + "+
					"coalesce(ts.hoursDay5, 0) + coalesce(ts.hoursDay6, 0) + coalesce(ts.hoursDay7, 0))" +
				") "+
			"FROM Timesheet ts " +
			"JOIN ts.operation op ";

		// Names where
		where += FilterUtil.addFilterAnd(where, "ts.status = :status ");
		
		if (employee != null) {
			where += FilterUtil.addFilterAnd(where, "ts.employee = :employee ");
		}
		if (since != null) {
			where += FilterUtil.addFilterAnd(where, "ts.initDate >= :initDate ");
		}
		if (until != null) {
			where += FilterUtil.addFilterAnd(where, "ts.initDate <= :endDate ");
		}
		
		// Group by
		groupBy += "ts.initDate, ts.endDate, op.operationName ";
		
		// Order by
		orderBy += "ts.initDate";
		
		// Query
		query = getSession().createQuery(q+where+groupBy+orderBy);
		
		// Values where
		query.setString("status", Constants.TIMESTATUS_APP3);
		
		if (employee != null) {
			query.setParameter("employee", employee);
		}
		if (since != null) { query.setDate("initDate", since); }
		if (until != null) { query.setDate("endDate", until); }
		
		List<ResourceTimeWrap> operations = query.list();		
		
		// Concatenate selects 
		//
		List<ResourceTimeWrap> activitiesAndOperations = new ArrayList<ResourceTimeWrap>();
		
		activitiesAndOperations.addAll(activities);
		activitiesAndOperations.addAll(operations);
		
		return activitiesAndOperations;
	}

	/**
	 * Check for hours pending approval - APP1 or APP2
	 * 
	 * @param project
	 * @return
	 */
	public boolean pendingApproval(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.or(
					Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP1),
					Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP2)
				));

		// Project restriction
		crit.createCriteria(Timesheet.PROJECTACTIVITY)
			.add(Restrictions.eq(Projectactivity.PROJECT, project));
			
		Integer count = (Integer) crit.uniqueResult();
		
		return (count != null && count > 0);
	}
	
	/**
	 * Returns employee with all level (app) hours.
	 * 
	 * @param initDate
	 * @param endDate
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ApprovalWrap> findTimesheetsAllApp(Date initDate, Date endDate, Employee user) {
		
		String activity = "(SELECT coalesce(SUM(" +
					"coalesce(tse.hoursDay1, 0D) + " +
					"coalesce(tse.hoursDay2, 0D) + " +
					"coalesce(tse.hoursDay3, 0D) + " +
					"coalesce(tse.hoursDay4, 0D) + "+
					"coalesce(tse.hoursDay5, 0D) + " +
					"coalesce(tse.hoursDay6, 0D) + " +
					"coalesce(tse.hoursDay7, 0D)), 0D) " +
				"FROM Timesheet tse " +
					"JOIN tse.projectactivity paSub " +
					"JOIN paSub.project pSub " +
				"WHERE tse.employee = e " +
					"AND tse.projectactivity is not null " +
					"AND tse.initDate = :initDate " +
					"AND tse.endDate= :endDate " +
					"AND ((pSub.status != :statusClosed AND pSub.status != :statusArchived) " +
                            "OR ((pSub.status = :statusClosed OR pSub.status = :statusArchived) AND tse.status != :app0))) ";
		
		String operation = "(SELECT coalesce(SUM(" +
					"coalesce(tseo.hoursDay1, 0D) + " +
					"coalesce(tseo.hoursDay2, 0D) + " +
					"coalesce(tseo.hoursDay3, 0D) + " +
					"coalesce(tseo.hoursDay4, 0D) + "+
					"coalesce(tseo.hoursDay5, 0D) + " +
					"coalesce(tseo.hoursDay6, 0D) + " +
					"coalesce(tseo.hoursDay7, 0D)), 0D) " +
				"FROM Timesheet tseo " +
				"WHERE tseo.employee = e " +
					"AND tseo.operation is not null "+
					"AND tseo.initDate = :initDate " +
					"AND tseo.endDate = :endDate) ";
		
		String suggestReject = "(SELECT count(tss.suggestReject) " +
				 	" FROM Timesheet tss " +
				 	" WHERE tss.initDate = :initDate " +
					 	" AND tss.suggestReject IS TRUE " +
					 	" AND tss.employee = e) ";
		
		String q = "SELECT NEW es.sm2.openppm.core.model.wrap.ApprovalWrap( " +
						"e.idEmployee, " +
						"c.fullName," +
						"rp.name," +
						"coalesce(s.name,'')," +
						getHours(Constants.TIMESTATUS_APP0) +", " +
						getHours(Constants.TIMESTATUS_APP1) +", " +
						getHours(Constants.TIMESTATUS_APP2) +", " +
						getHours(Constants.TIMESTATUS_APP3) +", " +
						activity + ", " +
						operation + ", " +
						suggestReject + " ) " +
					"FROM Employee e " +
						"JOIN e.timesheets ts " +
						"JOIN e.contact c " +
						"JOIN e.resourcepool rp " +
						"LEFT JOIN e.seller s " +
					"WHERE ts.initDate = :initDate " +
						"AND ts.endDate = :endDate " +
						"AND e.performingorg = :performingOrg " +
					"GROUP BY e.idEmployee ,c.fullName ";
		
		Query query = getSession().createQuery(q);
		
		query.setString(Constants.TIMESTATUS_APP0, Constants.TIMESTATUS_APP0);
		query.setString(Constants.TIMESTATUS_APP1, Constants.TIMESTATUS_APP1);
		query.setString(Constants.TIMESTATUS_APP2, Constants.TIMESTATUS_APP2);
		query.setString(Constants.TIMESTATUS_APP3, Constants.TIMESTATUS_APP3);
		
		// Add filters.
		query.setDate("initDate", initDate);
		query.setDate("endDate", endDate);
		query.setEntity("performingOrg", user.getPerformingorg());
		query.setString("statusClosed", Constants.STATUS_CLOSED);
        query.setString("statusArchived", Constants.STATUS_ARCHIVED);
		
		return query.list();
	}

	/**
	 * Returns the sum of all the hours for an employee by level (app0 ... etc)
	 * 
	 * @param appLevel
	 * @return
	 */
	private String getHours(String appLevel) {
		
		String query = "(SELECT coalesce(SUM(" +
					"coalesce(tseh1.hoursDay1, 0D) + " +
					"coalesce(tseh1.hoursDay2, 0D) + " +
					"coalesce(tseh1.hoursDay3, 0D) + " +
					"coalesce(tseh1.hoursDay4, 0D) + "+
					"coalesce(tseh1.hoursDay5, 0D) + " +
					"coalesce(tseh1.hoursDay6, 0D) + " +
					"coalesce(tseh1.hoursDay7, 0D)), 0D) " +
				"FROM Timesheet tseh1 " +
					"LEFT JOIN tseh1.projectactivity paSub1 " +
					"LEFT JOIN paSub1.project pSub1 " +
					"LEFT JOIN tseh1.operation ope1 " +
				"WHERE tseh1.employee = e " +
					"AND tseh1.status = :"+appLevel + " " + 
					"AND tseh1.initDate = :initDate " +
					"AND tseh1.endDate = :endDate ";
		
		if (Constants.TIMESTATUS_APP0.equals(appLevel)) {
			
			query += "AND ((pSub1.status != :statusClosed AND pSub1.status != :statusArchived) OR ope1 is not null) ";
		}

		query += ")";
		
		return query;
	}
	
	/**
	 * Find teemsheets for an employee
	 * 
	 * @param initWeek
	 * @param endWeek
	 * @param employee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TimesheetWrap> findTimesheetsForEmployee(Date initWeek,
			Date endWeek, Employee employee) {
		
		String q = "SELECT DISTINCT NEW es.sm2.openppm.core.model.wrap.TimesheetWrap( " +
						"ts, " +
						"p.status," +
						"case when pa is null then 0 else pa.idActivity end, " +
						"case when pa is null then '' else pa.activityName end, " +
						"case when o is null then '' else o.operationName end, " +
						"case when p is null then 0 else p.idProject end, " +
						"case when p is null then '' else p.projectName end, " +
						"case when p is null then '' else p.chartLabel end, " +
						"case when p is null then '' else p.status end, " +
						"case when pm is null then 0 else pm.idEmployee end, " +
						"case when c is null then '' else c.fullName end) " +
					"FROM Timesheet ts " +
						"LEFT JOIN ts.employee e " +
						"LEFT JOIN ts.projectactivity pa " +
						"LEFT JOIN pa.project p " +
						"LEFT JOIN p.employeeByProjectManager pm " +
						"LEFT JOIN pm.contact c " +
						"LEFT JOIN ts.operation o " +
					"WHERE ts.employee = :employee " +
						"AND ts.initDate = :initWeek " +
						"AND ts.endDate = :endWeek " +
						"AND (((p.status = :statusClosed OR p.status = :statusArchived) AND ts.status = :app3) " +
							"OR (p.status != :statusClosed AND p.status != :statusArchived) " +
							"OR ts.operation is not null)";
					
		Query query = getSession().createQuery(q);
		
		query.setDate("initWeek", initWeek);
		query.setDate("endWeek", endWeek);
		query.setEntity("employee", employee);
		query.setString("statusClosed", Constants.STATUS_CLOSED);
        query.setString("statusArchived", Constants.STATUS_ARCHIVED);
		query.setString("app3", Constants.TIMESTATUS_APP3);
		
		
		return query.list();
	}
	
	/**
	 * Returns employee with all level (app) hours.
	 * 
	 * @param initDate
	 * @param endDate
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ApprovalWrap> findTimesheetsAllAppByProject(Date initDate, Date endDate, Employee user) {
		
		String suggestReject = "(SELECT count(tss.suggestReject) " +
				 	"FROM Timesheet tss " +
				 		"LEFT JOIN tss.projectactivity pjas " +
				 		"LEFT JOIN pjas.project prs " +
				 	"WHERE tss.initDate = :initDate " +
				 		"AND tss.suggestReject IS TRUE " +
				 		"AND tss.employee = e " +
				 		"AND prs = p) ";
		
		String q = "SELECT NEW es.sm2.openppm.core.model.wrap.ApprovalWrap( " +
						"e.idEmployee, " +
						"c.fullName, " +
						"p.idProject, " +
						"p.projectName, " +
						getHoursByProject(Constants.TIMESTATUS_APP0) +", " +
						getHoursByProject(Constants.TIMESTATUS_APP1) +", " +
						getHoursByProject(Constants.TIMESTATUS_APP2) +", " +
						getHoursByProject(Constants.TIMESTATUS_APP3) +", " +
						suggestReject + " ) " +
					"FROM Employee e " +
						"JOIN e.timesheets ts " +
						"JOIN e.contact c " +
						"JOIN ts.projectactivity pa " +
						"JOIN pa.project p " +
					"WHERE ts.initDate = :initDate " +
						"AND ts.endDate = :endDate " +
						"AND p.employeeByProjectManager = :pm " +
					"GROUP BY e.idEmployee , c.fullName, p.idProject, p.projectName ";
		
		Query query = getSession().createQuery(q);
		
		query.setString(Constants.TIMESTATUS_APP0, Constants.TIMESTATUS_APP0);
		query.setString(Constants.TIMESTATUS_APP1, Constants.TIMESTATUS_APP1);
		query.setString(Constants.TIMESTATUS_APP2, Constants.TIMESTATUS_APP2);
		query.setString(Constants.TIMESTATUS_APP3, Constants.TIMESTATUS_APP3);
		
		// Add filters.
		query.setDate("initDate", initDate);
		query.setDate("endDate", endDate);
		query.setEntity("pm", user);
		query.setString("statusClosed", Constants.STATUS_CLOSED);
        query.setString("statusArchived", Constants.STATUS_ARCHIVED);
		
		return query.list();
	}
	
	/**
	 * Returns the sum of all the hours for an employee by level (app0 ... ect) and project
	 * 
	 * @param appLevel
	 * @return
	 */
	private String getHoursByProject(String appLevel) {
		
		 String query = "(SELECT coalesce(SUM(" +
					"coalesce(tseh.hoursDay1, 0D) + " +
					"coalesce(tseh.hoursDay2, 0D) + " +
					"coalesce(tseh.hoursDay3, 0D) + " +
					"coalesce(tseh.hoursDay4, 0D) + "+
					"coalesce(tseh.hoursDay5, 0D) + " +
					"coalesce(tseh.hoursDay6, 0D) + " +
					"coalesce(tseh.hoursDay7, 0D)), 0D) " +
				"FROM Timesheet tseh " +
					"LEFT JOIN tseh.projectactivity pjah " +
					"LEFT JOIN pjah.project prh " +
				"WHERE tseh.employee = e " +
					"AND prh = p " +
					"AND tseh.status = :"+appLevel + " " + 
					"AND tseh.initDate = :initDate " +
					"AND tseh.endDate = :endDate ";
		
		if (Constants.TIMESTATUS_APP0.equals(appLevel)) {
			
			query += "AND prh.status != :statusClosed AND prh.status != :statusArchived ";
		}
		
		query += ")";
		
		return query;
	}
	
	/**
	 * Find teemsheets for an employee by project
	 * 
	 * @param initWeek
	 * @param endWeek
	 * @param employee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TimesheetWrap> findTimesheetsForEmployeeByProject(Date initWeek,
			Date endWeek, Employee employee, Project project) {

		String q = "SELECT DISTINCT NEW es.sm2.openppm.core.model.wrap.TimesheetWrap( " +
						"ts, " +
						"p.status," +
						"case when pa is null then 0 else pa.idActivity end, " +
						"case when pa is null then '' else pa.activityName end, " +
						"case when o is null then '' else o.operationName end, " +
						"case when p is null then 0 else p.idProject end, " +
						"case when p is null then '' else p.projectName end, " +
						"case when p is null then '' else p.chartLabel end, " +
						"case when p is null then '' else p.status end, " +
						"case when pm is null then 0 else pm.idEmployee end, " +
						"case when c is null then '' else c.fullName end) " +
					"FROM Timesheet ts " +
						"LEFT JOIN ts.employee e " +
						"LEFT JOIN ts.projectactivity pa " +
						"LEFT JOIN pa.project p " +
						"LEFT JOIN ts.operation o " +
						"LEFT JOIN p.employeeByProjectManager pm " +
						"LEFT JOIN pm.contact c " +
					"WHERE ts.employee = :employee " +
						"AND ts.initDate = :initWeek " +
						"AND ts.endDate = :endWeek " +
						"AND (p = :project OR ts.operation is not null ) " +
						"AND ((p.status = :statusClosed OR p.status = :statusArchived) " +
							"AND ts.status = :appLevel " +
							"OR (p.status != :statusClosed AND p.status != :statusArchived)" +
							"OR ts.operation is not null) ";
					
		Query query = getSession().createQuery(q);
		
		query.setDate("initWeek", initWeek);
		query.setDate("endWeek", endWeek);
		query.setEntity("employee", employee);
		query.setString("statusClosed", Constants.STATUS_CLOSED);
        query.setString("statusArchived", Constants.STATUS_ARCHIVED);
		query.setString("appLevel", Constants.TIMESTATUS_APP3);
		query.setEntity("project", project);
		
		return query.list();
	}

    /**
     * Find imputations hours APP3
     *
     * @param employee
     * @param initDate
     * @param endDate
     * @return
     */
    public List<ImputationWrap> findImputations(Employee employee, Date initDate, Date endDate) {

        List<ImputationWrap> imputations = new ArrayList<ImputationWrap>();

        // Query project activities
        //
        String qAct = "SELECT DISTINCT NEW es.sm2.openppm.core.model.wrap.ImputationWrap( " +
                "p.projectName, " +
                "p.chartLabel, " +
                "coalesce(SUM(" +
                    "(case when (ts.hoursDay1 is not null and TO_DAYS(ts.initDate) >= TO_DAYS(:since) and TO_DAYS(ts.initDate) <= TO_DAYS(:until)) THEN ts.hoursDay1 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay2 is not null and TO_DAYS(ts.initDate) + 1 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 1 <= TO_DAYS(:until)) THEN ts.hoursDay2 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay3 is not null and TO_DAYS(ts.initDate) + 2 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 2 <= TO_DAYS(:until)) THEN ts.hoursDay3 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay4 is not null and TO_DAYS(ts.initDate) + 3 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 3 <= TO_DAYS(:until)) THEN ts.hoursDay4 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay5 is not null and TO_DAYS(ts.initDate) + 4 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 4 <= TO_DAYS(:until)) THEN ts.hoursDay5 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay6 is not null and TO_DAYS(ts.initDate) + 5 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 5 <= TO_DAYS(:until)) THEN ts.hoursDay6 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay7 is not null and TO_DAYS(ts.initDate) + 6 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 6 <= TO_DAYS(:until)) THEN ts.hoursDay7 ELSE 0 end )" +
                    "), 0D) "  +
                ") " +
                "FROM Timesheet ts " +
                    "JOIN ts.employee e " +
                    "JOIN ts.projectactivity pa " +
                    "JOIN pa.project p " +
                "WHERE e.idEmployee = :idEmployee " +
                    "AND ts.initDate >= :initDate " +
                    "AND ts.endDate <= :endDate " +
                    "AND ts.status = :status " +
                "GROUP BY p.projectName, p.chartLabel " +
                "ORDER BY p.projectName";

        Query query = getSession().createQuery(qAct);

        query.setString("status", Constants.TIMESTATUS_APP3);
        query.setInteger("idEmployee", employee.getIdEmployee());
        query.setDate("initDate",  DateUtil.getFirstWeekDay(initDate));
        query.setDate("endDate",  DateUtil.getLastWeekDay(endDate));
        query.setDate("since",  initDate);
        query.setDate("until",  endDate);

        List<ImputationWrap> imputationsActivity = query.list();

        // Add elements
        if (ValidateUtil.isNotNull(imputationsActivity)) {

            for (ImputationWrap imputationActivity : imputationsActivity) {

                if (imputationActivity.getHoursAPP3() != null && imputationActivity.getHoursAPP3() != 0.0) {
                    imputations.add(imputationActivity);
                }
            }
        }

        // Query operations
        //
        String qOp = "SELECT DISTINCT NEW es.sm2.openppm.core.model.wrap.ImputationWrap( " +
                "op.operationName, " +
                "coalesce(SUM(" +
                    "(case when (ts.hoursDay1 is not null and TO_DAYS(ts.initDate) >= TO_DAYS(:since) and TO_DAYS(ts.initDate) <= TO_DAYS(:until)) THEN ts.hoursDay1 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay2 is not null and TO_DAYS(ts.initDate) + 1 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 1 <= TO_DAYS(:until)) THEN ts.hoursDay2 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay3 is not null and TO_DAYS(ts.initDate) + 2 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 2 <= TO_DAYS(:until)) THEN ts.hoursDay3 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay4 is not null and TO_DAYS(ts.initDate) + 3 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 3 <= TO_DAYS(:until)) THEN ts.hoursDay4 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay5 is not null and TO_DAYS(ts.initDate) + 4 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 4 <= TO_DAYS(:until)) THEN ts.hoursDay5 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay6 is not null and TO_DAYS(ts.initDate) + 5 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 5 <= TO_DAYS(:until)) THEN ts.hoursDay6 ELSE 0 end ) + " +
                    "(case when (ts.hoursDay7 is not null and TO_DAYS(ts.initDate) + 6 >= TO_DAYS(:since) and TO_DAYS(ts.initDate) + 6 <= TO_DAYS(:until)) THEN ts.hoursDay7 ELSE 0 end )" +
                    "), 0D) "  +
                ") " +
                "FROM Timesheet ts " +
                "JOIN ts.employee e " +
                "JOIN ts.operation op " +
                "WHERE e.idEmployee = :idEmployee " +
                "AND ts.initDate >= :initDate " +
                "AND ts.endDate <= :endDate " +
                "AND ts.status = :status " +
                "GROUP BY op.operationName " +
                "ORDER BY op.operationName";

        query = getSession().createQuery(qOp);

        query.setString("status", Constants.TIMESTATUS_APP3);
        query.setInteger("idEmployee", employee.getIdEmployee());
        query.setDate("initDate",  DateUtil.getFirstWeekDay(initDate));
        query.setDate("endDate",  DateUtil.getLastWeekDay(endDate));
        query.setDate("since",  initDate);
        query.setDate("until",  endDate);

        List<ImputationWrap> imputationsOperation = query.list();

        // Add elements
        if (ValidateUtil.isNotNull(imputationsOperation)) {

            for (ImputationWrap imputationOperation : imputationsOperation) {

                if (imputationOperation.getHoursAPP3() != null && imputationOperation.getHoursAPP3() != 0.0) {
                    imputations.add(imputationOperation);
                }
            }
        }

        return imputations;
    }
}