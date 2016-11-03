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
 * File: ProjectActivityDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import es.sm2.openppm.core.logic.setting.GeneralSetting;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.utils.SettingUtil;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class ProjectActivityDAO extends AbstractGenericHibernateDAO<Projectactivity, Integer> {
	
	/**
	 * Constructor
	 */
	public ProjectActivityDAO(Session session) {
		super(session);
	}

	/**
	 * List of activities by project
	 * 
	 * @param proj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Projectactivity> findByProject(Project proj, List<String> joins) {
		List<Projectactivity> list = null;
		if (proj != null) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.eq(Projectactivity.PROJECT, proj));
			
			if (joins != null) {
				
				for (String join : joins) {
			
					crit.setFetchMode(join, FetchMode.JOIN);
				}
			}
			
			// Order
			crit.addOrder(Order.asc(Projectactivity.ACTIVITYNAME));
		
			list = crit.list();
		}
		return list;
	}
	
	
	/**
	 * Find by WBSnode
	 * 
	 * @param proj
	 * @param idWBSnode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Projectactivity findByWBSnode(Project proj,Integer idWBSnode) {
		
		List<Projectactivity> listActivity = null;
		Projectactivity projAct = null;
		
		if (proj != null) {
			Query query = getSession().createQuery(
					"select activity " +
					"from Projectactivity as activity " + 
					"join activity.project as project " +
					"where project.idProject = :idProject and activity.wbsnode.idWbsnode = :idWBSnode");
			query.setInteger("idProject", proj.getIdProject());
			query.setInteger("idWBSnode", idWBSnode);
			
			listActivity = query.list();
			
			if (!listActivity.isEmpty()) {
				projAct = listActivity.get(0);
			}
		}
		return projAct;
	}

	
	/**
	 * Checks whether an activity with a start date is imputed hours than
	 * 
	 * @param activity
	 * @return
	 */
	public boolean checkAfterActivityInputed(Projectactivity activity) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.idEq(activity.getIdActivity()))
			.createCriteria(Projectactivity.TIMESHEETS)
			.add(Restrictions.disjunction()
					.add(Restrictions.ne(Timesheet.HOURSDAY1, 0D))
					.add(Restrictions.ne(Timesheet.HOURSDAY2, 0D))
					.add(Restrictions.ne(Timesheet.HOURSDAY3, 0D))
					.add(Restrictions.ne(Timesheet.HOURSDAY4, 0D))
					.add(Restrictions.ne(Timesheet.HOURSDAY5, 0D))
					.add(Restrictions.ne(Timesheet.HOURSDAY6, 0D))
					.add(Restrictions.ne(Timesheet.HOURSDAY7, 0D))
					)
			.add(Restrictions.le(Timesheet.ENDDATE, activity.getPlanInitDate()));
		
		return ((Integer)crit.uniqueResult() > 0);
	}

	
	/**
	 * Get total EV from project current/realized activities
	 * 
	 * @param idProject
	 * @return EV
	 */
	public double consCurrentProjectEV(Integer idProject) {
		
		double ev = 0;
		
		Query query = getSession().createQuery(
				"select sum(a.ev) " +
				"from Projectactivity as a " +
				"join a.project as p " +
				"where p.idProject=:idProject and a.actualInitDate<=current_date()");
		query.setInteger("idProject", idProject);
		
		if (query.uniqueResult() != null) {
			ev = (Double) query.uniqueResult();
		}
		return ev;
	}
	
	/**
	 * Get root activity of project
	 * 
	 * @param proj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Projectactivity consRootActivity(Project proj) {
		
		List<Projectactivity> activities = null;
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Projectactivity.PROJECT, proj));
		
		crit.createCriteria(Projectactivity.WBSNODE)
			.add(Restrictions.isNull(Wbsnode.WBSNODE));
		
		activities = crit.list();
		
		Projectactivity rootActivity = (activities != null && !activities.isEmpty()?activities.get(0):null);
		
		// Create Root Activity if not exist
		if (rootActivity == null) {
			
			Wbsnode wbsRoot = null;
			
			crit = getSession().createCriteria(Wbsnode.class)
				.add(Restrictions.isNull(Wbsnode.WBSNODE))
				.addOrder(Order.asc(Wbsnode.CODE))
				.add(Restrictions.eq(Wbsnode.PROJECT, proj));	
			
			List<Wbsnode> list = crit.list();
			if (!list.isEmpty()) { wbsRoot = list.get(0); }
			
			if (wbsRoot == null) {
				
				crit = getSession().createCriteria(Wbsnode.class)
					.setProjection(Projections.rowCount())
					.add(Restrictions.eq(Wbsnode.PROJECT, proj));
				
				if ((Integer)crit.uniqueResult() == 0) {
					
					WBSNodeDAO nodeDAO = new WBSNodeDAO(getSession());
					wbsRoot = new Wbsnode();
					wbsRoot.setProject(proj);
					wbsRoot.setIsControlAccount(true);
					wbsRoot.setName(proj.getProjectName());
					
					wbsRoot = nodeDAO.makePersistent(wbsRoot);
				}
			}
			
			if (wbsRoot != null) {
				
				rootActivity = new Projectactivity();
				rootActivity.setProject(proj);
				rootActivity.setWbsnode(wbsRoot);
				rootActivity.setWbsdictionary(StringPool.BLANK);
			
				if (wbsRoot.getCode() != null && !StringPool.BLANK.equals(wbsRoot.getCode())) {
					rootActivity.setActivityName(wbsRoot.getCode()+". "+wbsRoot.getName());
				}
				else {
					rootActivity.setActivityName(wbsRoot.getName());
				}
				rootActivity = makePersistent(rootActivity);
				
				updatePlannedDates(proj, rootActivity);
				updateActualDates(proj, rootActivity);
				
				rootActivity = findById(rootActivity.getIdActivity());
			}
		}
		
		return rootActivity;
	}
	
	/**
	 * Filter by project and since until over planned dates
	 * 
	 * @param project
	 * @param since
	 * @param until
	 * @param typeOrder 
	 * @param propertyOrder 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Projectactivity> findByProject(Project project, Date since,
			Date until, String propertyOrder, String typeOrder) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.disjunction()
						.add(Restrictions.between(Projectactivity.PLANINITDATE, since, until))
						.add(Restrictions.between(Projectactivity.PLANENDDATE, since, until))
						.add(Restrictions.and(
								Restrictions.le(Projectactivity.PLANINITDATE, since),
								Restrictions.ge(Projectactivity.PLANENDDATE, until)
							)
						)
				)
			.add(Restrictions.eq(Projectactivity.PROJECT, project));
		
		addOrder(crit, propertyOrder, typeOrder);
		
		return crit.list();
	}
	

	/**
	 * Update Planned Dates of root activity
	 * 
	 * @param project
	 * @param rootActivity
	 */
	public void updatePlannedDates(Project project, Projectactivity rootActivity) {
		
		// Num of activities of project
		int numActivities = rowCountEq(Projectactivity.PROJECT, project);
		
		// Get max and min project activity dates
		Criteria crit = getSession().createCriteria(getPersistentClass())
		.setProjection(Projections.projectionList()
				.add(Projections.min(Projectactivity.PLANINITDATE))
				.add(Projections.max(Projectactivity.PLANENDDATE)))
		.add(Restrictions.eq(Projectactivity.PROJECT, project));

		// Exclude root activities
		if (numActivities > 1) {
			crit.add(Restrictions.ne(Projectactivity.IDACTIVITY, rootActivity.getIdActivity()));
		}
		
		Object[] row = (Object[]) crit.uniqueResult();
		
		// Init calculated plan dates
		Date calculatedPlanStartDate = null;
		Date calculatedPlanFinishDate = null;
		
		// If there is result set root activity
		if (row != null && row.length > 0) {
			
			Date planInitDate = (Date)row[0];
			Date planEndDate = (Date)row[1];
			
			rootActivity.setPlanInitDate(planInitDate);
			rootActivity.setPlanEndDate(planEndDate);
			rootActivity = makePersistent(rootActivity);
			
			// Set calculated plan dates 
			calculatedPlanStartDate 	= planInitDate == null 	? project.getPlannedInitDate() 		: planInitDate;
			calculatedPlanFinishDate 	= planEndDate == null 	? project.getPlannedFinishDate() 	: planEndDate;
		}
		else {
			
			// Set calculated plan dates 
			calculatedPlanStartDate = project.getPlannedInitDate();
			calculatedPlanFinishDate = project.getPlannedFinishDate();
		}
		
		// Update calculated planning dates
		Query query = getSession().createQuery(
				"update Project p " +
				" set p.calculatedPlanStartDate = :calculatedPlanStartDate,"+
				" p.calculatedPlanFinishDate = :calculatedPlanFinishDate"+
				" where p.idProject = :idProject"
			);
		query.setInteger("idProject", project.getIdProject());
		query.setDate("calculatedPlanStartDate", calculatedPlanStartDate);
		query.setDate("calculatedPlanFinishDate", calculatedPlanFinishDate);
		query.executeUpdate();
	}
	
	/**
	 * Update Actual Dates of root activity
	 * 
	 * @param project
	 * @param rootActivity
	 */
	@SuppressWarnings("unchecked")
	public void updateActualDates(Project project, Projectactivity rootActivity) {
		
		// Find activities by project, exclude root activity
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.eq(Projectactivity.PROJECT, project))
				.add(Restrictions.ne(Projectactivity.IDACTIVITY, rootActivity.getIdActivity()));
		
		List<Projectactivity> activities = crit.list();
		
		Date initDate	= null;
		Date endDate	= null;
		
		for (Projectactivity activity : activities) {
			
			// Update init date
			//
			if (activity.getActualInitDate() != null && (initDate == null || initDate.after(activity.getActualInitDate()))) {
				
				initDate = activity.getActualInitDate();
			}
			else if (activity.getActualInitDate() == null && activity.getPlanInitDate() != null
					&& (initDate == null || initDate.after(activity.getPlanInitDate()))) {
				
				initDate = activity.getPlanInitDate();
			}
			
			// Update end Date
			//
			if (activity.getActualEndDate() != null && (endDate == null || endDate.before(activity.getActualEndDate()))) {
				
				endDate = activity.getActualEndDate();
			}
			else if (activity.getActualEndDate() == null && activity.getPlanEndDate() != null
					&& (endDate == null || endDate.before(activity.getPlanEndDate()))) {
				
				endDate = activity.getPlanEndDate();
			}
		}
		
		// Update dates with project dates if not has been assigned in activities
		if (initDate == null) { initDate = project.getPlannedInitDate(); }
		if (endDate == null) { endDate = project.getPlannedFinishDate(); }
		
		// Set dates in root activity
		rootActivity.setActualInitDate(initDate);
		rootActivity.setActualEndDate(endDate);
		
		// Save changes
		makePersistent(rootActivity);
	}
	
	/**
	 * List not assigned activities to seller
	 * 
	 * @param seller
	 * @param project
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> consNoAssignedActivities(Seller seller, Project project, Order... order) {
			
		Criteria usedCrit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.property(Projectactivity.IDACTIVITY))			
			.add(Restrictions.eq(Projectactivity.PROJECT, project));
		
		usedCrit.createCriteria(Projectactivity.ACTIVITYSELLERS)
			.add(Restrictions.eq(Activityseller.SELLER, seller));
		
		List<Projectactivity> lista = usedCrit.list();
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.projectionList()
				.add(Projections.property(Projectactivity.IDACTIVITY))
				.add(Projections.property(Projectactivity.ACTIVITYNAME)))
			.add(Restrictions.eq(Projectactivity.PROJECT, project));
		
		crit.createCriteria(Projectactivity.WBSNODE)
			.add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true));
		
		if (!lista.isEmpty()) {
			crit.add(Restrictions.not(Restrictions.in(Projectactivity.IDACTIVITY, lista)));	
		}

		for (Order o : order) {
			crit.addOrder(o);
		}
		
		return crit.list();
	}
	
	/**
	 * Sum EV
	 * 
	 * @param proj
	 * @return
	 */
	public double sumEV(Project proj) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.sum(Projectactivity.EV))
			.add(Restrictions.eq(Projectactivity.PROJECT, proj));

        crit.createCriteria(Projectactivity.WBSNODE)
                .add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true));

        Double value = (Double) crit.uniqueResult();

		return (value == null?0:value);
	}
	
	public double clacPocBySum(Project project) {

		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Projectactivity.PROJECT, project))
			.setProjection(Projections.projectionList()
					.add(Projections.sum(Projectactivity.POC))
					.add(Projections.rowCount()));
		
		Criteria critWBSNode = crit.createCriteria(Projectactivity.WBSNODE)
			.add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true));

		Object[] value = (Object[]) crit.uniqueResult();
		
		double poc = 0;
		
		if (value != null && value.length == 2) {
			double sumPoc	= (Double)value[0] == null?0:(Double)value[0];
			int rows		= (Integer)value[1];
			
			if (rows > 0) { poc = sumPoc / new Double(rows); }
		}
		
		return poc;
	}

	public Double calcPoc(Projectactivity projectactivity, Map<String, String> settings) {

        // Declare DAO
		WBSNodeDAO wbsNodeDAO = new WBSNodeDAO(getSession());

        // Get project
		Project project = projectactivity.getProject();

		Double poc = null;

        boolean thereAreBudget = wbsNodeDAO.getSumBudget(project) > 0;

        if (!wbsNodeDAO.isBudgetEmpty(project) || (settings != null && SettingUtil.getBoolean(settings, GeneralSetting.POC_EXCLUDE_CA_NO_BUDGET) && thereAreBudget)) {

            double ev		= sumEV(project);
            double budget	= wbsNodeDAO.getSumBudget(project);

            if (budget > 0) { poc = ev/budget; }
        }
		else {
			poc = clacPocBySum(project)/100;
		}
		
		return poc;
	}

	@SuppressWarnings("unchecked")
	public Projectactivity findByRedMine(String accountingCode, String wbsnodeCode) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		crit.createCriteria(Projectactivity.PROJECT)
			.add(Restrictions.eq(Project.ACCOUNTINGCODE, accountingCode));
		
		crit.createCriteria(Projectactivity.WBSNODE)
			.add(Restrictions.eq(Wbsnode.CODE, wbsnodeCode));
		
		List<Projectactivity> list = crit.list();
		
		return (list.isEmpty()?null:list.get(0));
	}

	@SuppressWarnings("unchecked")
	public List<Projectactivity> consByProjectList(Integer[] ids) {
		
		List<Projectactivity> activities = null;
		
		if (ValidateUtil.isNotNull(ids)) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass());
			
			crit.createCriteria(Projectactivity.PROJECT)
			.add(Restrictions.in(Project.IDPROJECT, ids))
			.addOrder(Order.asc(Project.ACCOUNTINGCODE));
			
			crit.createCriteria(Projectactivity.WBSNODE)
			.add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true));
			
			crit.addOrder(Order.asc(Projectactivity.IDACTIVITY));
			
			activities = crit.list();
		}
		else {
			activities = new ArrayList<Projectactivity>();
		}
		
		return activities;
	}

    /**
     * Find activities inputed
     *
     * @param ids
     * @param since
     * @param until
     * @param idResourcePool
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Projectactivity> findActivitiesInputedByProjectsAndEmployees(Integer[] ids, Date since, Date until, Integer idResourcePool) {
		
		List<Projectactivity> activities = null;
		
		if (ValidateUtil.isNotNull(ids)) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
							.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			// All activities by projects
			//
			crit.createCriteria(Projectactivity.PROJECT)
			.add(Restrictions.in(Project.IDPROJECT, ids))
			.addOrder(Order.asc(Project.ACCOUNTINGCODE));
			
			crit.createCriteria(Projectactivity.WBSNODE)
			.add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true));
			
			crit.addOrder(Order.asc(Projectactivity.IDACTIVITY));
			
			// Teammmembers
			//
			Criteria members = crit.createCriteria(Projectactivity.TEAMMEMBERS);
			
			if (since != null && until != null) {
				
				members.add(Restrictions.disjunction()
						.add(Restrictions.between(Teammember.DATEIN, since, until))
						.add(Restrictions.between(Teammember.DATEOUT, since, until))
						.add(Restrictions.and(
								Restrictions.le(Teammember.DATEIN, since),
								Restrictions.ge(Teammember.DATEOUT, until)
								)
								)
						);
			}
			
			// Employees 
			//
			Criteria employees = members.createCriteria(Teammember.EMPLOYEE);
			
			if (idResourcePool != null) {
				employees.add(Restrictions.eq(Employee.RESOURCEPOOL, new Resourcepool(idResourcePool)));
			}
			
			Criteria sheets = employees.createCriteria(Employee.TIMESHEETS)
					.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3));
			
			if (since != null && until != null) {
				sheets.add(Restrictions.disjunction()
						.add(Restrictions.between(Timesheet.INITDATE, since, until))
						.add(Restrictions.between(Timesheet.ENDDATE, since, until))
						.add(Restrictions.and(
								Restrictions.le(Timesheet.INITDATE, since),
								Restrictions.ge(Timesheet.ENDDATE, until)
								)
								)
						);
			}
			
			activities = crit.list();
		}
		else {
			activities = new ArrayList<Projectactivity>();
		}
		
		return activities;
	}

	/**
	 * Find control account activities by project
	 * 
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Projectactivity> findControlAccountsByProject(Project project) {
		
		List<Projectactivity> list = null;
		
		if (project != null) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.eq(Projectactivity.PROJECT, project));
			
			crit.createCriteria(Projectactivity.WBSNODE)
					.add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true));
		
			addOrder(crit, Projectactivity.ACTIVITYNAME, Constants.ASCENDENT);
			
			list = crit.list();
		}
		else {
			list = new ArrayList<Projectactivity>();
		}
		
		return list;
	}

	/**
	 *
	 * Find activity with greater date
	 * The activity can not be the same activity nor the parent node
     *
	 * @param project
	 * @param projectactivity
	 * @param rootActivity
	 * @return
	 */
	public Projectactivity findActivityMaxEndDate(Project project, Projectactivity projectactivity, Projectactivity rootActivity){

		List<Projectactivity> list = null;

		if (project != null && projectactivity != null && rootActivity != null) {
			// Control activity is not the same you are updating and is not the parent activity
			Criteria crit = getSession().createCriteria(getPersistentClass())
					.add(Restrictions.eq(Projectactivity.PROJECT, project))
					.add(Restrictions.ne(Projectactivity.IDACTIVITY, projectactivity.getIdActivity()))
					.add(Restrictions.ne(Projectactivity.IDACTIVITY, rootActivity.getIdActivity()))
							.addOrder(Order.desc(Projectactivity.PLANENDDATE));

			list = crit.list();
		}

		return (list.isEmpty()?null:list.get(0));
	}
}
