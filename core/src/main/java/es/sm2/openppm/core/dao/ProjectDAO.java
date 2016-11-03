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
 * File: ProjectDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.model.search.ProjectSearch;
import es.sm2.openppm.core.logic.persistance.MappingGenericDAO;
import es.sm2.openppm.core.logic.persistance.bean.NamedJoin;
import es.sm2.openppm.core.logic.persistance.bean.NamedOrder;
import es.sm2.openppm.core.logic.persistance.bean.NamedQuery;
import es.sm2.openppm.core.logic.persistance.bean.NamedRestriction;
import es.sm2.openppm.core.logic.setting.VisibilityProjectSetting;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Incomes;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectassociation;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.impl.Workingcosts;
import es.sm2.openppm.core.model.wrap.ProjectWrap;
import es.sm2.openppm.core.utils.FilterUtil;
import es.sm2.openppm.core.utils.IntegerUtil;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectDAO extends MappingGenericDAO<Project, Integer> {

	public ProjectDAO(Session session) {
		super(session);
	}

    public enum ProjectNamedRestriction implements NamedRestriction {
		BY_PM, BY_PROJECT;
	}
	
	public enum ProjectNamedJoin implements NamedJoin {
		BY_PM;
	}
	
	public enum ProjectNamedOrder implements NamedOrder {
		BY_NAME_DESC;
	}
	
	public enum ProjectNamedQuery implements NamedQuery {
		FIND_BY_ID;
	}
	
	/**
	 * Cons project for initiating
	 * @param proj
	 * @return
	 */
	public Project consInitiatingProject(Project proj) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		crit.setFetchMode("performingorg", FetchMode.JOIN);
		crit.setFetchMode("customer", FetchMode.JOIN);
		crit.setFetchMode(Project.GEOGRAPHY, FetchMode.JOIN);
		crit.setFetchMode(Project.CATEGORY, FetchMode.JOIN);
		crit.setFetchMode(Project.PROGRAM, FetchMode.JOIN);
		crit.setFetchMode("employeeByProjectManager", FetchMode.JOIN);
		crit.setFetchMode("employeeByProjectManager.contact", FetchMode.JOIN);
		crit.setFetchMode("employeeByInvestmentManager", FetchMode.JOIN);
		crit.setFetchMode("employeeByInvestmentManager.contact", FetchMode.JOIN);
		crit.setFetchMode("employeeByFunctionalManager", FetchMode.JOIN);
		crit.setFetchMode("employeeByFunctionalManager.contact", FetchMode.JOIN);
		crit.setFetchMode("employeeBySponsor", FetchMode.JOIN);
		crit.setFetchMode("employeeBySponsor.contact", FetchMode.JOIN);
		crit.setFetchMode("stakeholders", FetchMode.JOIN);
		crit.setFetchMode(Project.STAGEGATE, FetchMode.JOIN);
		crit.setFetchMode(Project.CLASSIFICATIONLEVEL, FetchMode.JOIN);
		crit.setFetchMode(Project.WORKINGCOSTSES, FetchMode.JOIN);
		crit.setFetchMode(Project.WORKINGCOSTSES+"."+Workingcosts.CURRENCY, FetchMode.JOIN);
		crit.setFetchMode(Project.PROJECTASSOCIATIONSFORDEPENDENT, FetchMode.JOIN);
		crit.setFetchMode(Project.PROJECTASSOCIATIONSFORDEPENDENT+"."+Projectassociation.PROJECTBYLEAD, FetchMode.JOIN);
		crit.setFetchMode(Project.PROJECTASSOCIATIONSFORLEAD, FetchMode.JOIN);
		crit.setFetchMode(Project.PROJECTASSOCIATIONSFORLEAD+"."+Projectassociation.PROJECTBYDEPENDENT, FetchMode.JOIN);
		crit.add(Restrictions.eq("idProject", proj.getIdProject()));
		
		
		return (Project) crit.uniqueResult();
	}
	
	/**
	 * Get Investments in process
     *
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> consInvestmentInProcess(Integer[] ids) {
		List<Project> list = null;

		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Project.INVESTMENTSTATUS, Constants.INVESTMENT_IN_PROCESS))
			.add(Restrictions.in(Project.IDPROJECT, ids));
		
		list = crit.list();

		return list;
	}
	

	/**
	 * Cons Project and extra info
	 * @param proj
	 * @param joins
	 * @return
	 */
	public Project consProject(Project proj, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());

        if (ValidateUtil.isNotNull(joins)) {

            for (String join : joins) {
                crit.setFetchMode(join, FetchMode.JOIN);
            }
        }

		crit.add(Restrictions.eq("idProject", proj.getIdProject()));
		
		return (Project) crit.uniqueResult();
	}
	
	/**
	 * Find projects where an Employee is team member in range of days
	 * @param employee
	 * @param sinceDate
	 * @param untilDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByResourceInProject(Employee employee, Date sinceDate, Date untilDate) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)			
			.createCriteria(Project.PROJECTACTIVITIES)
			.createCriteria(Projectactivity.TEAMMEMBERS)
			.add(Restrictions.eq(Teammember.EMPLOYEE, employee))
			.add(Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_ASSIGNED))
			.add(Restrictions.disjunction()
					.add(Restrictions.between(Teammember.DATEIN, sinceDate, untilDate))
					.add(Restrictions.between(Teammember.DATEOUT, sinceDate, untilDate))
					.add(Restrictions.and(
							Restrictions.le(Teammember.DATEIN, sinceDate),
							Restrictions.ge(Teammember.DATEOUT, untilDate)
						)
					)
				);
		
		return crit.list();
		
	}
	
	/**
	 * Check if Accounting code is in use
     *
	 * @param proj
	 * @param company 
	 * @return
	 */
	public boolean accountingCodeInUse(Project proj, Company company) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Project.ACCOUNTINGCODE, proj.getAccountingCode()))
			.add(Restrictions.ne(Project.IDPROJECT, proj.getIdProject()))
			.createCriteria(Project.PERFORMINGORG)
				.add(Restrictions.eq(Performingorg.COMPANY, company));
		
		return ((Integer)crit.uniqueResult() > 0);
	}
	
	/**
	 * DSOu = hoy – max(income.actualBillDate)
	 * @param project
	 * @return days
	 */
	public Integer calculateDSOUnbilled(Project project) {
		Integer days = 0;
		
		Query query = getSession().createQuery(
				"select current_date()-max(i.actualBillDate) " +
				"from Incomes as i " +
				"join i.project as p " +
				"where p.idProject=:idProject and i.actualPaymentDate is null");
		query.setInteger("idProject", project.getIdProject());
		
		if (query.uniqueResult() != null) {
			days = ((Double) query.uniqueResult()).intValue();
		}
		
		return days;
	}
	
	
	/**
	 * 
	 * @param project
	 * @return
	 */
	public Integer calculateDSOBilled(Project project) {
		Integer days = null;
		
		Query query = getSession().createQuery(
				"select max(i.actualPaymentDate-i.actualBillDate) " +
				"from Incomes as i " +
				"join i.project as p " +
				"where p.idProject=:idProject and i.actualPaymentDate is not null");
		query.setInteger("idProject", project.getIdProject());
		
		if (query.uniqueResult() != null) {
			days = ((Double) query.uniqueResult()).intValue();
		}
		
		return days;
	}
	
	
	/**
	 * Consult list of projects by ids
	 * @param ids
	 * @param property
	 * @param order
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> consList(Integer[] ids, String property, String order, List<String> joins) {
		
		List<Project> list = new ArrayList<Project>();
		
		if (ValidateUtil.isNotNull(ids)) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
					.add(Restrictions.in(Project.IDPROJECT, ids));
			
			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			addJoins(crit, joins);
			addOrder(crit, property, order);
			
			list =  crit.list();
		}
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Project> findByProgram(Program program, boolean investiment) {

		List<Project> list = new ArrayList<Project>();
		
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		crit.add(Restrictions.eq(Project.PROGRAM, program));
		
		if (investiment) {
			crit.add(Restrictions.ne(Project.INVESTMENTSTATUS, Constants.INVESTMENT_APPROVED));
			
			list.addAll(crit.list());
		}
		else {
			crit.add(Restrictions.eq(Project.INVESTMENTSTATUS, Constants.INVESTMENT_APPROVED));
			
			list.addAll(crit.list());
		}
		return list;
	}
	
	/**
	 * Consult list of projects by seller
	 * 
	 * @param seller
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findBySeller(Seller seller) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		crit.setProjection(Projections.property(Project.PROJECTNAME));
		
		crit.createCriteria(Project.PROJECTACTIVITIES)
			.createCriteria(Projectactivity.ACTIVITYSELLERS)		
			.add(Restrictions.eq(Activityseller.SELLER, seller));
		
		crit.addOrder(Order.asc(Project.PROJECTNAME));		

		return crit.list();
	}

	
	/**
	 * Check if chart label is in use
	 * @param proj
	 * @param company
	 * @return
	 */
	public boolean chartLabelInUse(Project proj, Company company) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Project.CHARTLABEL, proj.getChartLabel()))
			.add(Restrictions.ne(Project.IDPROJECT, proj.getIdProject()))
			.createCriteria(Project.PERFORMINGORG)
				.add(Restrictions.eq(Performingorg.COMPANY, company));
		
		return ((Integer)crit.uniqueResult() > 0);
	}

	@SuppressWarnings("unchecked")
	public List<Project> consListInControl(Integer[] ids, String property, String order) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())			
			.add(Restrictions.eq(Project.STATUS, Constants.STATUS_CONTROL))
			.add(Restrictions.in(Project.IDPROJECT, ids));
	
		addOrder(crit, property, order);
		
		return crit.list();
	}
	
	public boolean hasPermission(Project project, Employee user, Company company, int tab) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.idEq(project.getIdProject()));
		
		int rol = user.getResourceprofiles().getIdProfile();
		
		if (rol == Constants.ROLE_PMO || rol == Constants.ROLE_FM || rol == Constants.ROLE_LOGISTIC) {
			crit.add(Restrictions.eq(Project.PERFORMINGORG, user.getPerformingorg()));
		}
		else if (rol == Constants.ROLE_PM) {
			crit.add(Restrictions.eq(Project.EMPLOYEEBYPROJECTMANAGER, user));
		}
		else if (rol == Constants.ROLE_SPONSOR) {
			crit.add(Restrictions.eq(Project.EMPLOYEEBYSPONSOR, user));
		}
		else if (rol == Constants.ROLE_PORFM) {
			crit.createCriteria(Project.PERFORMINGORG)
				.add(Restrictions.eq(Performingorg.COMPANY, company));
		}
		else if (rol == Constants.ROLE_IM && tab == Constants.TAB_INITIATION) {
			crit.add(Restrictions.eq(Project.EMPLOYEEBYINVESTMENTMANAGER, user));
		}
		else if (rol == Constants.ROLE_PROGM) {
			crit.createCriteria(Project.PROGRAM).add(Restrictions.eq(Program.EMPLOYEE, user));
		}
		else if (rol == Constants.ROLE_STAKEHOLDER) {
			crit.createCriteria(Project.STAKEHOLDERS).add(Restrictions.eq(Stakeholder.EMPLOYEE, user));
		}
		else {
			return false;
		}
		
		return ((Integer)crit.uniqueResult() > 0);
	}

	/**
	 * Find project by income
	 * @param income
	 * @return
	 */
	public Project findByIncome(Incomes income) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.createCriteria(Project.INCOMESES)
			.add(Restrictions.idEq(income.getIdIncome()));
		
		return (Project) crit.uniqueResult();
	}

	/**
	 * Find by Project Followup
	 * @param projectfollowup
	 * @return
	 */
	public Project findByFollowup(Projectfollowup projectfollowup) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.createCriteria(Project.PROJECTFOLLOWUPS)
			.add(Restrictions.idEq(projectfollowup.getIdProjectFollowup()));
	
		return (Project) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Project> find(String search, Performingorg performingorg, Company company) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.disjunction()
					.add(Restrictions.ilike(Project.PROJECTNAME,"%"+search+"%"))
					.add(Restrictions.ilike(Project.CHARTLABEL,"%"+search+"%"))
					.add(Restrictions.ilike(Project.ACCOUNTINGCODE,"%"+search+"%"))
				);
		
			Criteria poCrit = crit.createCriteria(Project.PERFORMINGORG)
				.add(Restrictions.eq(Performingorg.COMPANY, company));
			
			if (performingorg != null) {
				poCrit.add(Restrictions.eq(Performingorg.IDPERFORG, performingorg.getIdPerfOrg()));
			}
		
		return crit.list();
	}
	
	/**
	 * 
	 * @param employee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByEmployee(Employee employee, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
			
		crit.createCriteria(Project.PROJECTACTIVITIES)
			.createCriteria(Projectactivity.TEAMMEMBERS)			
			.add(Restrictions.eq(Teammember.EMPLOYEE, employee));
		
		crit.addOrder(Order.asc(Project.PROJECTNAME));
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			
		return crit.list();
	}
	
	/**
	 * 
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByEmployee(Employee employee, Date initDate, Date endDate) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.createCriteria(Project.PROJECTACTIVITIES)
			.createCriteria(Projectactivity.TIMESHEETS)
			.add(Restrictions.eq(Timesheet.EMPLOYEE, employee))
			.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
			.add(Restrictions.ge(Timesheet.INITDATE, initDate))
			.add(Restrictions.le(Timesheet.ENDDATE, endDate));		
				
		crit.addOrder(Order.asc(Project.PROJECTNAME));
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			
		return crit.list();
	}
	
	/**
	 * 
	 * @param program
	 * @return
	 */
	public Double getBudgetBottomUp(Program program, boolean projectsDisabled) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.sum(Project.TCV))
			.add(Restrictions.eq(Project.PROGRAM, program));

		// Projects disabled
		if (projectsDisabled) {
			
			crit.add(Restrictions.disjunction()
					.add(Restrictions.ne(Project.DISABLE, Constants.SELECTED))
					.add(Restrictions.isNull(Project.DISABLE))
					);
		}

		Double budget = (Double)crit.uniqueResult();
		
		return (budget == null ? 0 : budget);
	}

	/**
	 * Find by PO and equals Planning or Execution
	 * @param performingorg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByPO(Performingorg performingorg) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Project.PERFORMINGORG, performingorg))
			.add(Restrictions.or(
					Restrictions.eq(Project.STATUS, Constants.STATUS_CONTROL),
					Restrictions.eq(Project.STATUS, Constants.STATUS_PLANNING))
				);
		
		return crit.list();
	}

	/**
	 * Check if project is in these status
	 * @param project
	 * @param status
	 * @return
	 */
	public boolean hasPermissionProject(Project project, String...status) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.idEq(project.getIdProject()));
		
		Disjunction disjuntions = Restrictions.disjunction();
		
		for (String stat : status) {
			disjuntions.add(Restrictions.eq(Project.STATUS, stat));
		}
		
		crit.add(disjuntions);
		return ((Integer)crit.uniqueResult() > 0);
	}

	@SuppressWarnings("unchecked")
	public List<Project> find(String search, Performingorg performingorg, List<String> joins) {
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.disjunction()
					.add(Restrictions.ilike(Project.PROJECTNAME,"%"+search+"%"))
					.add(Restrictions.ilike(Project.CHARTLABEL,"%"+search+"%"))
					.add(Restrictions.ilike(Project.ACCOUNTINGCODE,"%"+search+"%"))
				);
		if(performingorg != null){	
			crit.add(Restrictions.eq(Project.PERFORMINGORG, performingorg));
		}
		
		addJoins(crit, joins);
		
		return crit.list();
	}

	/**
	 * Sum priority by program
	 * @param program
	 * @return
	 */
	public Integer sumPriorityByProgram(Program program, boolean projectsDisabled) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
						.setProjection(Projections.sum(Project.PRIORITY))
						.add(Restrictions.eq(Project.PROGRAM, program))
						.add(Restrictions.disjunction()
							.add(Restrictions.eq(Project.INVESTMENTSTATUS, Constants.INVESTMENT_APPROVED))
							.add(Restrictions.eq(Project.INVESTMENTSTATUS, Constants.INVESTMENT_IN_PROCESS))
						);
		
		// Projects not disabled
		if (projectsDisabled) {
			
			crit.add(Restrictions.disjunction()
					.add(Restrictions.ne(Project.DISABLE, Constants.SELECTED))
					.add(Restrictions.isNull(Project.DISABLE))
			);
		}
		
		Integer priority = (Integer)crit.uniqueResult();
		
		return (priority == null ? 0 : priority);
	}

	/**
	 * Search projects with list investment status
	 * @param program
	 * @param investmentStatus 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByProgramAndInvestmentStatus(Program program, List<String> investmentStatus, boolean projectsDisabled) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
						.add(Restrictions.eq(Project.PROGRAM, program));
		
		Disjunction disjunction = Restrictions.disjunction();
		
		for (String status : investmentStatus) {
			disjunction.add(Restrictions.eq(Project.INVESTMENTSTATUS, status));
		}
		crit.add(disjunction);
		
		// Projects not disabled
		if (projectsDisabled) {
			
			crit.add(Restrictions.disjunction()
					.add(Restrictions.ne(Project.DISABLE, Constants.SELECTED))
					.add(Restrictions.isNull(Project.DISABLE))
					);
		}
		
		crit.addOrder(Order.asc(Project.TCV));
		
		return crit.list();
	}

	/**
	 * Filter projects
     *
	 * @param filter
     * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectWrap> findProjects(ProjectSearch filter) {
		
		String q = "SELECT DISTINCT NEW es.sm2.openppm.core.model.wrap.ProjectWrap("+
				"p.idProject, "+
				"p.status, "+
				"p.investmentStatus, "+
				"CASE WHEN p.rag IS NULL THEN '' ELSE p.rag END, "+
				"CASE WHEN p.kpiStatus IS NULL THEN '' ELSE p.kpiStatus END, "+
				"CASE WHEN p.accountingCode IS NULL THEN '' ELSE p.accountingCode END, "+
				"p.projectName, "+
				"p.chartLabel, "+
				"po.name, "+
				"CASE WHEN p.tcv IS NULL THEN 0d ELSE p.tcv END, "+
				"CASE WHEN p.priority IS NULL THEN 0 ELSE p.priority END, "+
				"CASE WHEN p.probability IS NULL THEN 0 ELSE p.probability END, "+
				"CASE WHEN p.poc IS NULL THEN 0d ELSE p.poc END, "+
				"p.plannedInitDate, "+
				"p.plannedFinishDate, "+
				"p.calculatedPlanStartDate, "+
				"p.calculatedPlanFinishDate, "+
				"p.startDate, "+
				"p.finishDate, "+
				"(SELECT CASE WHEN SUM(pf.ac) IS NULL THEN 0d ELSE SUM(pf.ac) END "+
					"FROM Projectfollowup pf "+
					"WHERE pf.project = p "+
					"AND pf.ac IS NOT NULL AND pf.followupDate = (SELECT MAX(pf2.followupDate) FROM Projectfollowup pf2 WHERE pf2.project = p AND pf2.ac IS NOT NULL)), "+
				"CASE WHEN p.effort IS NULL THEN 0 ELSE p.effort END, "+
				"(SELECT CASE WHEN SUM(ch.cost) IS NULL THEN 0d ELSE SUM(ch.cost) END FROM Chargescosts ch WHERE ch.project = p AND (ch.idChargeType = 1 OR ch.idChargeType = 2 OR ch.idChargeType = 3)), " +
				"CASE WHEN p.bac IS NULL THEN 0d ELSE p.bac END, "+
				"CASE WHEN pr IS NULL THEN '' ELSE pr.programName END, "+
				"CASE WHEN pmContact IS NULL THEN '' ELSE pmContact.fullName END, "+
                "CASE WHEN cat IS NULL THEN '' ELSE cat.name END, "+
                "CASE WHEN clalvl IS NULL THEN '' ELSE clalvl.name END, "+
                "CASE WHEN p.duration IS NULL THEN 0 ELSE p.duration END, "+
                "CASE WHEN st IS NULL THEN '' ELSE st.name END "+
				") "+
			"FROM Projectactivity pa "+
			"JOIN pa.project p " +
			"JOIN p.program pr "+
			"JOIN p.performingorg po " +
			"LEFT JOIN p.employeeByProjectManager pm " +
			"LEFT JOIN pm.contact pmContact " +
			"LEFT JOIN p.stakeholders stk " +
			"LEFT JOIN pa.activitysellers actSell " +
			"LEFT JOIN p.projectfundingsources fs " +
			"LEFT JOIN p.projectlabels pl " +
            "LEFT JOIN p.projecttechnologies pt " +
            "LEFT JOIN p.category cat " +
            "LEFT JOIN p.classificationlevel clalvl " +
            "LEFT JOIN p.stagegate st " +
			"JOIN pa.wbsnode w ";
		

		// QUERY
		return createQuery(q, filter).list();
	}

    /**
     * Create query
     *
     * @param queryFormat
     * @param filter
     * @return
     */
    private Query createQuery(String queryFormat, ProjectSearch filter) {

        Query query = getSession().createQuery(queryFormat + createWhere(filter));

        addParameters(query, filter);
        return query;
    }

    /**
     * Add parameters
     *
     * @param query
     * @param filter
     */
    private void addParameters(Query query, ProjectSearch filter) {

        // Add parameter for priority
        if (!ValidateUtil.isNull(filter.getPriority())) {

            if ((ProjectSearch.GREATHER_EQUAL.equals(filter.getPriority()) || ProjectSearch.LESS_EQUAL.equals(filter.getPriority())) && filter.getLastPriority() != null ) {

                query.setInteger("lastPriority", filter.getLastPriority());
            }
            else if (ProjectSearch.BETWEEN.equals(filter.getPriority()) && filter.getLastPriority() != null && filter.getFirstPriority() != null) {

                query.setInteger("firstPriority", filter.getFirstPriority());
                query.setInteger("lastPriority", filter.getLastPriority());
            }
        }

        // Add parameter for risk rating
        if (ValidateUtil.isNotNull(filter.getRiskRating())) {

            if ((ProjectSearch.GREATHER_EQUAL.equals(filter.getRiskRating()) || ProjectSearch.LESS_EQUAL.equals(filter.getRiskRating())) && filter.getLastRiskRating() != null ) {

                query.setInteger("lastRiskRating", filter.getLastRiskRating());
            }
            else if (ProjectSearch.BETWEEN.equals(filter.getRiskRating()) && filter.getLastRiskRating() != null && filter.getFirstRiskRating() != null) {

                query.setInteger("firstRiskRating", filter.getFirstRiskRating());
                query.setInteger("lastRiskRating", filter.getLastRiskRating());
            }
        }

        if (filter.getIncludeDisabled() != null && !filter.getIncludeDisabled()) {
            query.setBoolean("disable", Boolean.TRUE);
        }

        if (filter.getSince() != null) { query.setDate("since", filter.getSince()); }
        if (filter.getUntil() != null) { query.setDate("until", filter.getUntil()); }
        if (filter.getInternalProject() != null) { query.setBoolean("internalProject", filter.getInternalProject()); }
        if (filter.getIsGeoSelling() != null) { query.setBoolean("isGeoSelling", filter.getIsGeoSelling()); }
        if (filter.getBudgetYear() != null) { query.setInteger("budgetYear", filter.getBudgetYear()); }
        if (ValidateUtil.isNotNull(filter.getProjectName())) { query.setString("projectName", "%"+filter.getProjectName().toUpperCase()+"%"); }

        Integer[] idsProject = IntegerUtil.parseStringSequence(filter.getProjectName(), StringPool.COMMA);

        if (SettingUtil.getBoolean(filter.getSettings(), VisibilityProjectSetting.PROJECT_COLUMN_IDPROJECT) && idsProject != null) {

            query.setParameterList("projectNameID", idsProject);
        }

        if (ValidateUtil.isNotNull(filter.getRag())) { query.setCharacter("rag", filter.getRag().charAt(0)); }
        if (filter.getCompany() != null) { query.setEntity("company", filter.getCompany()); }
        if (filter.getEmployeeByInvestmentManager() != null) { query.setEntity("employeeByInvestmentManager", filter.getEmployeeByInvestmentManager()); }
        if (filter.getStakeholder() != null) { query.setEntity("stakeholder", filter.getStakeholder()); }
        if (filter.getProgramManager() != null) { query.setEntity("programManager", filter.getProgramManager()); }
        if (filter.getIsIndirectSeller() != null) { query.setBoolean("isIndirectSeller", filter.getIsIndirectSeller()); }

        if (ValidateUtil.isNotNull(filter.getPerformingorgs())) { query.setParameterList("performingorgs", filter.getPerformingorgs()); }
        if (ValidateUtil.isNotNull(filter.getEmployeeBySponsors())) { query.setParameterList("employeeBySponsors", filter.getEmployeeBySponsors()); }
        if (ValidateUtil.isNotNull(filter.getEmployeeByProjectManagers())) { query.setParameterList("employeeByProjectManagers", filter.getEmployeeByProjectManagers()); }
        if (ValidateUtil.isNotNull(filter.getCustomers())) { query.setParameterList("customers", filter.getCustomers()); }
        if (ValidateUtil.isNotNull(filter.getCustomertypes())) { query.setParameterList("customertypes", filter.getCustomertypes()); }
        if (ValidateUtil.isNotNull(filter.getPrograms())) { query.setParameterList("programs", filter.getPrograms()); }
        if (ValidateUtil.isNotNull(filter.getCategories())) { query.setParameterList("categories", filter.getCategories()); }
        if (ValidateUtil.isNotNull(filter.getSellers())) { query.setParameterList("sellers", filter.getSellers()); }
        if (ValidateUtil.isNotNull(filter.getGeography())) { query.setParameterList("geography", filter.getGeography()); }
        if (ValidateUtil.isNotNull(filter.getFundingsources())) { query.setParameterList("fundingsources", filter.getFundingsources()); }
        if (ValidateUtil.isNotNull(filter.getProjects())) { query.setParameterList("projects", filter.getProjects()); }
        if (ValidateUtil.isNotNull(filter.getStatus())) { query.setParameterList("status", filter.getStatus()); }
        if (ValidateUtil.isNotNull(filter.getInvestmentStatus())) { query.setParameterList("investmentStatus", filter.getInvestmentStatus()); }
        if (ValidateUtil.isNotNull(filter.getLabels())) { query.setParameterList("labels", filter.getLabels()); }
        if (ValidateUtil.isNotNull(filter.getTechnologies())) { query.setParameterList("technologies", filter.getTechnologies()); }
        if (ValidateUtil.isNotNull(filter.getStageGates())) { query.setParameterList("stagegate", filter.getStageGates()); }
        if (ValidateUtil.isNotNull(filter.getContractTypes())) { query.setParameterList("contractTypes", filter.getContractTypes()); }
        if (ValidateUtil.isNotNull(filter.getEmployeeByFunctionalManagers())) { query.setParameterList("employeeByFunctionalManagers", filter.getEmployeeByFunctionalManagers()); }
        if (ValidateUtil.isNotNull(filter.getClassificationsLevel())) { query.setParameterList("classificationsLevel", filter.getClassificationsLevel()); }

        if (filter.getShowInactivated() != null && !filter.getShowInactivated()) {

            query.setString("hideInactivated", Constants.INVESTMENT_INACTIVATED);
        }
    }

    /**
     * Create where for filter projects
     *
     * @param filter
     * @return
     */
    private String createWhere(ProjectSearch filter) {

        String where = "";

        // Create filter for priority
        if (!ValidateUtil.isNull(filter.getPriority())) {

            if (ProjectSearch.GREATHER_EQUAL.equals(filter.getPriority()) && filter.getLastPriority() != null) {

                where += FilterUtil.addFilterAnd(where, "p.priority >= :lastPriority ");
            }
            else if (ProjectSearch.LESS_EQUAL.equals(filter.getPriority()) && filter.getLastPriority() != null) {

                where += FilterUtil.addFilterAnd(where, "p.priority <= :lastPriority ");
            }
            else if (ProjectSearch.BETWEEN.equals(filter.getPriority()) && filter.getLastPriority() != null && filter.getFirstPriority() != null) {

                where += FilterUtil.addFilterAnd(where, "(p.priority BETWEEN :firstPriority AND :lastPriority) ");
            }
        }

        // Create filter for risk rating
        if (ValidateUtil.isNotNull(filter.getRiskRating())) {

            String subSelectRiskRating = "(SELECT SUM(rg.probability*rg.impact) FROM Riskregister rg WHERE rg.project = p)";

            if (ProjectSearch.GREATHER_EQUAL.equals(filter.getRiskRating()) && filter.getLastRiskRating() != null) {

                where += FilterUtil.addFilterAnd(where, subSelectRiskRating + " >= :lastRiskRating ");
            }
            else if (ProjectSearch.LESS_EQUAL.equals(filter.getRiskRating()) && filter.getLastRiskRating() != null) {

                where += FilterUtil.addFilterAnd(where, subSelectRiskRating + " <= :lastRiskRating ");
            }
            else if (ProjectSearch.BETWEEN.equals(filter.getRiskRating()) && filter.getLastRiskRating() != null && filter.getFirstRiskRating() != null) {

                where += FilterUtil.addFilterAnd(where, "(" + subSelectRiskRating + " BETWEEN :firstRiskRating AND :lastRiskRating) ");
            }
        }

        // Filter by since and until dates
        if (filter.getSince() != null && filter.getUntil() != null) {
            where += FilterUtil.addFilterAnd(where, "((p.startDate BETWEEN :since AND :until) OR (p.finishDate BETWEEN :since AND :until) OR (p.startDate <= :since AND p.finishDate >= :until))");
        }
        else if (filter.getSince() != null) {
            where += FilterUtil.addFilterAnd(where, "p.startDate >= :since ");
        }
        else if (filter.getUntil() != null) {
            where += FilterUtil.addFilterAnd(where, "p.finishDate <= :until ");
        }

        if (filter.getShowInactivated() != null && !filter.getShowInactivated()) {
            where += FilterUtil.addFilterAnd(where, "p.investmentStatus != :hideInactivated ");
        }

        if (filter.getIncludeDisabled() != null && !filter.getIncludeDisabled()) {
            where += FilterUtil.addFilterAnd(where, "(p.disable is null or p.disable != :disable) ");
        }

        where += FilterUtil.addFilterAnd(filter.getInternalProject(), where, "p.internalProject = :internalProject ");
        where += FilterUtil.addFilterAnd(filter.getIsGeoSelling(), where, "p.isGeoSelling = :isGeoSelling ");
        where += FilterUtil.addFilterAnd(filter.getBudgetYear(), where, "p.budgetYear = :budgetYear ");

        // Project name filter
        //
        String projectNameFilter = "(UPPER(p.projectName) LIKE :projectName OR UPPER(p.chartLabel) LIKE :projectName OR UPPER(p.accountingCode) LIKE :projectName ";

        Integer[] idsProject = IntegerUtil.parseStringSequence(filter.getProjectName(), StringPool.COMMA);

        if (SettingUtil.getBoolean(filter.getSettings(), VisibilityProjectSetting.PROJECT_COLUMN_IDPROJECT) && idsProject != null) {

            projectNameFilter += "OR p.idProject IN (:projectNameID) ";
        }

        projectNameFilter += ")";

        where += FilterUtil.addFilterAnd(filter.getProjectName(), where, projectNameFilter);
        where += FilterUtil.addFilterAnd(filter.getRag(), where, "p.rag = :rag ");
        where += FilterUtil.addFilterAnd(filter.getCompany(), where, "po.company = :company ");
        where += FilterUtil.addFilterAnd(filter.getEmployeeByInvestmentManager(), where, "p.employeeByInvestmentManager = :employeeByInvestmentManager ");
        where += FilterUtil.addFilterAnd(filter.getStakeholder(), where, "stk.employee = :stakeholder ");
        where += FilterUtil.addFilterAnd(filter.getProgramManager(), where, "pr.employee = :programManager ");
        where += FilterUtil.addFilterAnd(filter.getIsIndirectSeller(), where, "actSell.indirect = :isIndirectSeller ");

        if (ValidateUtil.isNotNull(filter.getPerformingorgs())) { where += FilterUtil.addFilterAnd(where, "po.idPerfOrg IN (:performingorgs) "); }
        if (ValidateUtil.isNotNull(filter.getEmployeeBySponsors())) { where += FilterUtil.addFilterAnd(where, "p.employeeBySponsor.idEmployee IN (:employeeBySponsors) "); }
        if (ValidateUtil.isNotNull(filter.getEmployeeByProjectManagers())) { where += FilterUtil.addFilterAnd(where, "p.employeeByProjectManager.idEmployee IN (:employeeByProjectManagers) "); }
        if (ValidateUtil.isNotNull(filter.getCustomers())) { where += FilterUtil.addFilterAnd(where, "p.customer.idCustomer IN (:customers) "); }
        if (ValidateUtil.isNotNull(filter.getCustomertypes())) { where += FilterUtil.addFilterAnd(where, "p.customer.customertype.idCustomerType IN (:customertypes) "); }
        if (ValidateUtil.isNotNull(filter.getPrograms())) { where += FilterUtil.addFilterAnd(where, "p.program.idProgram IN (:programs) "); }
        if (ValidateUtil.isNotNull(filter.getCategories())) { where += FilterUtil.addFilterAnd(where, "p.category.idCategory IN (:categories) "); }
        if (ValidateUtil.isNotNull(filter.getSellers())) { where += FilterUtil.addFilterAnd(where, "actSell.seller.idSeller IN (:sellers) "); }
        if (ValidateUtil.isNotNull(filter.getGeography())) { where += FilterUtil.addFilterAnd(where, "p.geography.idGeography IN (:geography) "); }
        if (ValidateUtil.isNotNull(filter.getProjects())) { where += FilterUtil.addFilterAnd(where, "p.idProject IN (:projects) "); }

        if (filter.getIncludeRejected() != null && filter.getIncludeRejected() && ValidateUtil.isNotNull(filter.getStatus())) {
            where += FilterUtil.addFilterAnd(where, "p.status IN (:status) AND p.investmentStatus NOT IN ('"+ Constants.INVESTMENT_REJECTED +"') ");
        }
        else if (ValidateUtil.isNotNull(filter.getStatus())) {
            where += FilterUtil.addFilterAnd(where, "p.status IN (:status) ");
        }
        if (filter.getIncludeRejected() != null && filter.getIncludeRejected()) {
            where += FilterUtil.addFilterAnd(where, "p.investmentStatus NOT IN ('"+ Constants.INVESTMENT_REJECTED +"') ");
        }

        // Priority adjustment
        //
        if (filter.getIncludingAdjustament() != null && filter.getIncludingAdjustament()) {
            where += FilterUtil.addFilterAnd(where, "((p.useRiskAdjust is not null and p.useRiskAdjust = true) or (p.useStrategicAdjust is not null and p.useStrategicAdjust = true)) ");
        }
        else if (filter.getIncludingAdjustament() != null) {
            where += FilterUtil.addFilterAnd(where, "(p.useRiskAdjust is null or p.useRiskAdjust = false) ");
            where += FilterUtil.addFilterAnd(where, "(p.useStrategicAdjust is null or p.useStrategicAdjust = false)");
        }

        if (ValidateUtil.isNotNull(filter.getInvestmentStatus())) { where += FilterUtil.addFilterAnd(where, "p.investmentStatus IN (:investmentStatus) "); }
        if (ValidateUtil.isNotNull(filter.getLabels())) { where += FilterUtil.addFilterAnd(where, "pl.label.idLabel IN (:labels) "); }
        if (ValidateUtil.isNotNull(filter.getTechnologies())) { where += FilterUtil.addFilterAnd(where, "pt.technology.idTechnology IN (:technologies) "); }
        if (ValidateUtil.isNotNull(filter.getStageGates())) { where += FilterUtil.addFilterAnd(where, "p.stagegate.idStageGate IN (:stagegate) "); }
        if (ValidateUtil.isNotNull(filter.getContractTypes())) { where += FilterUtil.addFilterAnd(where, "p.contracttype.idContractType IN (:contractTypes) "); }
        if (ValidateUtil.isNotNull(filter.getEmployeeByFunctionalManagers())) { where += FilterUtil.addFilterAnd(where, "p.employeeByFunctionalManager.idEmployee IN (:employeeByFunctionalManagers) "); }

        // Add classification level filter
        //
        String classificationLevel = StringPool.BLANK;
        if (ValidateUtil.isNotNull(filter.getClassificationsLevel())) {

            classificationLevel = " p.classificationlevel.idClassificationlevel IN (:classificationsLevel) ";
        }

        if (filter.isClassificationsLevelUnclassified()) {

            classificationLevel += (ValidateUtil.isNull(filter.getClassificationsLevel())?StringPool.BLANK:" OR ")+" p.classificationlevel is empty ";
        }

        if (ValidateUtil.isNotNull(classificationLevel)) {
            where += FilterUtil.addFilterAnd(where, StringPool.OPEN_PARENTHESIS+classificationLevel+StringPool.CLOSE_PARENTHESIS);
        }

        // Add classification level filter
        //
        String fundingSource = StringPool.BLANK;
        if (ValidateUtil.isNotNull(filter.getFundingsources())) {
            fundingSource = " fs.fundingsource.idFundingSource IN (:fundingsources) ";
        }

        if (filter.isFundingSourceUnclassified()) {
            fundingSource += (ValidateUtil.isNull(filter.getFundingsources()) ? StringPool.BLANK:" OR ")+" fs.fundingsource is empty ";
        }

        if (ValidateUtil.isNotNull(fundingSource)) {
            where += FilterUtil.addFilterAnd(where, StringPool.OPEN_PARENTHESIS+fundingSource+StringPool.CLOSE_PARENTHESIS);
        }

        return where;
    }

	/**
	 * Delete old data
	 * @param project
	 */
	public void prepareForImport(Project project) {

		// DELETE NODES
		Query query = getSession().createQuery(
				"delete from Wbsnode as w " +
				"where w.project.idProject = :idProject"
			);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
		
		// DELETE ACTIVITIES
		query = getSession().createQuery(
				"delete from Projectactivity as pa " +
				"where pa.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
		
		// DELETE MILESTONES
		query = getSession().createQuery(
				"delete from Milestones as m " +
				"where m.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
		
		// DELETE FOLLOWUPS
		query = getSession().createQuery(
				"delete from Projectfollowup as f " +
				"where f.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
		
		// DELETE INCOMES
		query = getSession().createQuery(
				"delete from Incomes as i " +
				"where i.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
		
		// DELETE STAKEHOLDERS
		query = getSession().createQuery(
				"delete from Stakeholder as s " +
				"where s.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
		
		// DELETE ISSUES LOG
		query = getSession().createQuery(
				"delete from Issuelog as il " +
				"where il.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();

		// DELETE ASSUMPTIONS
		query = getSession().createQuery(
				"delete from Assumptionregister as ar " +
				"where ar.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
		
		// DELETE RISKREGISTER
		query = getSession().createQuery(
				"delete from Riskregister as r " +
				"where r.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
		
		// DELETE CHARGE COSTS
		query = getSession().createQuery(
				"delete from Chargescosts as c " +
				"where c.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
		
		// DELETE CHARGE WORKING COSTS
		query = getSession().createQuery(
				"delete from Workingcosts as w " +
				"where w.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
	}

	/**
	 * All leads for the projects
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> leads(Integer[] ids) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.createCriteria(Project.PROJECTASSOCIATIONSFORLEAD)
				.createCriteria(Projectassociation.PROJECTBYDEPENDENT)
					.add(Restrictions.in(Project.IDPROJECT, ids));
		
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		crit.addOrder(Order.desc(Project.PROJECTNAME));
		
		return crit.list();
	} 
	
	/**
	 * All dependents for the projects
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> dependents(Integer[] ids) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.createCriteria(Project.PROJECTASSOCIATIONSFORDEPENDENT)
				.createCriteria(Projectassociation.PROJECTBYLEAD)
					.add(Restrictions.in(Project.IDPROJECT, ids));
		
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		crit.addOrder(Order.desc(Project.PROJECTNAME));
		
		return crit.list();
	}

	/**
	 * Consult project leads
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> consLeads(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.createCriteria(Project.PROJECTASSOCIATIONSFORLEAD)
				.createCriteria(Projectassociation.PROJECTBYDEPENDENT)
					.add(Restrictions.eq(Project.IDPROJECT, project.getIdProject()));
		
		crit.addOrder(Order.desc(Project.PROJECTNAME));
		
		return crit.list();
	}
	
	/**
	 * Consult project depedents
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> consDependents(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.createCriteria(Project.PROJECTASSOCIATIONSFORDEPENDENT)
				.createCriteria(Projectassociation.PROJECTBYLEAD)
					.add(Restrictions.eq(Project.IDPROJECT, project.getIdProject()));
		
		crit.addOrder(Order.desc(Project.PROJECTNAME));
		
		return crit.list();
	}

	/**
	 * 
	 * @param filter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectWrap> findProjectsForExecutiveReport(ProjectSearch filter) {
		
		String q = "SELECT DISTINCT NEW es.sm2.openppm.core.model.wrap.ProjectWrap("+
				"p, "+
				"(SELECT CASE WHEN SUM(ch.cost) IS NULL THEN 0d ELSE SUM(ch.cost) END FROM Chargescosts ch WHERE ch.project = p AND (ch.idChargeType = 1 OR ch.idChargeType = 2 OR ch.idChargeType = 3)) " +
				") "+
			"FROM Projectactivity pa "+
			"JOIN pa.project p " +
			"JOIN p.program pr "+
			"JOIN p.performingorg po " +
			"LEFT JOIN p.stakeholders stk " +
			"LEFT JOIN pa.activitysellers actSell " +
			"LEFT JOIN p.projectfundingsources fs " +
			"LEFT JOIN p.projectlabels pl " +
			"JOIN pa.wbsnode w ";
		
		String where = "";
		
		// Create filter for priority
		if (!ValidateUtil.isNull(filter.getPriority())) {
			
			if (ProjectSearch.GREATHER_EQUAL.equals(filter.getPriority()) && filter.getLastPriority() != null) {
				
				where += FilterUtil.addFilterAnd(where, "p.priority >= :lastPriority ");
			}
			else if (ProjectSearch.LESS_EQUAL.equals(filter.getPriority()) && filter.getLastPriority() != null) {
				
				where += FilterUtil.addFilterAnd(where, "p.priority <= :lastPriority ");
			}
			else if (ProjectSearch.BETWEEN.equals(filter.getPriority()) && filter.getLastPriority() != null && filter.getFirstPriority() != null) {
				
				where += FilterUtil.addFilterAnd(where, "(p.priority BETWEEN :firstPriority AND :lastPriority) ");
			}
		}
		
		// Filter by since and until dates
		if (filter.getSince() != null && filter.getUntil() != null) {
			where += FilterUtil.addFilterAnd(where, "((p.startDate BETWEEN :since AND :until) OR (p.finishDate BETWEEN :since AND :until) OR (p.startDate <= :since AND p.finishDate >= :until))");
		}
		else if (filter.getSince() != null) {
			where += FilterUtil.addFilterAnd(where, "p.startDate >= :since ");
		}
		else if (filter.getUntil() != null) {
			where += FilterUtil.addFilterAnd(where, "p.finishDate <= :since ");
		}

		where += FilterUtil.addFilterAnd(filter.getInternalProject(), where, "p.internalProject = :internalProject ");
		where += FilterUtil.addFilterAnd(filter.getBudgetYear(), where, "p.budgetYear = :budgetYear ");
		where += FilterUtil.addFilterAnd(filter.getProjectName(), where, "(UPPER(p.projectName) LIKE :projectName OR UPPER(p.chartLabel) LIKE :projectName OR UPPER(p.accountingCode) LIKE :projectName) ");
		where += FilterUtil.addFilterAnd(filter.getRag(), where, "p.rag = :rag ");
		where += FilterUtil.addFilterAnd(filter.getCompany(), where, "po.company = :company ");
		where += FilterUtil.addFilterAnd(filter.getEmployeeByInvestmentManager(), where, "p.employeeByInvestmentManager = :employeeByInvestmentManager ");
        if (ValidateUtil.isNotNull(filter.getEmployeeByFunctionalManagers())) { where += FilterUtil.addFilterAnd(where, "p.employeeByFunctionalManager.idEmployee IN (:employeeByFunctionalManagers) "); }
        where += FilterUtil.addFilterAnd(filter.getStakeholder(), where, "stk.employee = :stakeholder ");
		where += FilterUtil.addFilterAnd(filter.getProgramManager(), where, "pr.employee = :programManager ");
		

		if (ValidateUtil.isNotNull(filter.getPerformingorgs())) { where += FilterUtil.addFilterAnd(where, "po.idPerfOrg IN (:performingorgs) "); }
		if (ValidateUtil.isNotNull(filter.getEmployeeBySponsors())) { where += FilterUtil.addFilterAnd(where, "p.employeeBySponsor.idEmployee IN (:employeeBySponsors) "); }
		if (ValidateUtil.isNotNull(filter.getEmployeeByProjectManagers())) { where += FilterUtil.addFilterAnd(where, "p.employeeByProjectManager.idEmployee IN (:employeeByProjectManagers) "); }
		if (ValidateUtil.isNotNull(filter.getCustomers())) { where += FilterUtil.addFilterAnd(where, "p.customer.idCustomer IN (:customers) "); }
		if (ValidateUtil.isNotNull(filter.getCustomertypes())) { where += FilterUtil.addFilterAnd(where, "p.contracttype.idContractType IN (:customertypes) "); }
		if (ValidateUtil.isNotNull(filter.getPrograms())) { where += FilterUtil.addFilterAnd(where, "p.program.idProgram IN (:programs) "); }
		if (ValidateUtil.isNotNull(filter.getCategories())) { where += FilterUtil.addFilterAnd(where, "p.category.idCategory IN (:categories) "); }
		if (ValidateUtil.isNotNull(filter.getSellers())) { where += FilterUtil.addFilterAnd(where, "actSell.seller.idSeller IN (:sellers) "); }
		if (ValidateUtil.isNotNull(filter.getGeography())) { where += FilterUtil.addFilterAnd(where, "p.geography.idGeography IN (:geography) "); }
		if (ValidateUtil.isNotNull(filter.getFundingsources())) { where += FilterUtil.addFilterAnd(where, "fs.fundingsource.idFundingSource IN (:fundingsources) "); }
		if (ValidateUtil.isNotNull(filter.getProjects())) { where += FilterUtil.addFilterAnd(where, "p.idProject IN (:projects) "); }
		if (ValidateUtil.isNotNull(filter.getStatus())) { where += FilterUtil.addFilterAnd(where, "p.status IN (:status) "); }
		if (ValidateUtil.isNotNull(filter.getInvestmentStatus())) { where += FilterUtil.addFilterAnd(where, "p.investmentStatus IN (:investmentStatus) "); }
		if (ValidateUtil.isNotNull(filter.getLabels())) { where += FilterUtil.addFilterAnd(where, "pl.label.idLabel IN (:labels) "); }
		
		Query query = getSession().createQuery(q+where);
		
		// Add parameter for priority
		if (!ValidateUtil.isNull(filter.getPriority())) {
			
			if ((ProjectSearch.GREATHER_EQUAL.equals(filter.getPriority()) || ProjectSearch.LESS_EQUAL.equals(filter.getPriority())) && filter.getLastPriority() != null ) {
				
				query.setInteger("lastPriority", filter.getLastPriority());
			}
			else if (ProjectSearch.BETWEEN.equals(filter.getPriority()) && filter.getLastPriority() != null && filter.getFirstPriority() != null) {
				
				query.setInteger("firstPriority", filter.getFirstPriority());
				query.setInteger("lastPriority", filter.getLastPriority());
			}
		}
		
		if (filter.getSince() != null) { query.setDate("since", filter.getSince()); }
		if (filter.getUntil() != null) { query.setDate("until", filter.getUntil()); }
		if (filter.getInternalProject() != null) { query.setBoolean("internalProject", filter.getInternalProject()); }
		if (filter.getBudgetYear() != null) { query.setInteger("budgetYear", filter.getBudgetYear()); }
		if (ValidateUtil.isNotNull(filter.getProjectName())) { query.setString("projectName", "%" + filter.getProjectName().toUpperCase() + "%"); }
		if (ValidateUtil.isNotNull(filter.getRag())) { query.setCharacter("rag", filter.getRag().charAt(0)); }
		if (filter.getCompany() != null) { query.setEntity("company", filter.getCompany()); }
		if (filter.getEmployeeByInvestmentManager() != null) { query.setEntity("employeeByInvestmentManager", filter.getEmployeeByInvestmentManager()); }
        if (ValidateUtil.isNotNull(filter.getEmployeeByFunctionalManagers())) { query.setParameterList("employeeByFunctionalManagers", filter.getEmployeeByFunctionalManagers()); }
        if (filter.getStakeholder() != null) { query.setEntity("stakeholder", filter.getStakeholder()); }
		if (filter.getProgramManager() != null) { query.setEntity("programManager", filter.getProgramManager()); }
		
		if (ValidateUtil.isNotNull(filter.getPerformingorgs())) { query.setParameterList("performingorgs", filter.getPerformingorgs()); }
		if (ValidateUtil.isNotNull(filter.getEmployeeBySponsors())) { query.setParameterList("employeeBySponsors", filter.getEmployeeBySponsors()); }
		if (ValidateUtil.isNotNull(filter.getEmployeeByProjectManagers())) { query.setParameterList("employeeByProjectManagers", filter.getEmployeeByProjectManagers()); }
		if (ValidateUtil.isNotNull(filter.getCustomers())) { query.setParameterList("customers", filter.getCustomers()); }
		if (ValidateUtil.isNotNull(filter.getCustomertypes())) { query.setParameterList("customertypes", filter.getCustomertypes()); }
		if (ValidateUtil.isNotNull(filter.getPrograms())) { query.setParameterList("programs", filter.getPrograms()); }
		if (ValidateUtil.isNotNull(filter.getCategories())) { query.setParameterList("categories", filter.getCategories()); }
		if (ValidateUtil.isNotNull(filter.getSellers())) { query.setParameterList("sellers", filter.getSellers()); }
		if (ValidateUtil.isNotNull(filter.getGeography())) { query.setParameterList("geography", filter.getGeography()); }
		if (ValidateUtil.isNotNull(filter.getFundingsources())) { query.setParameterList("fundingsources", filter.getFundingsources()); }
		if (ValidateUtil.isNotNull(filter.getProjects())) { query.setParameterList("projects", filter.getProjects()); }
		if (ValidateUtil.isNotNull(filter.getStatus())) { query.setParameterList("status", filter.getStatus()); }
		if (ValidateUtil.isNotNull(filter.getInvestmentStatus())) { query.setParameterList("investmentStatus", filter.getInvestmentStatus()); }
		if (ValidateUtil.isNotNull(filter.getLabels())) { query.setParameterList("labels", filter.getLabels()); }

		return query.list();
	}

	/**
	 * Check if project is dependent of other project
	 * 
	 * @param project
	 * @return
	 */
	public boolean hasParent(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.setProjection(Projections.rowCount())
				.createCriteria(Project.PROJECTASSOCIATIONSFORDEPENDENT)
				.createCriteria(Projectassociation.PROJECTBYLEAD)
					.add(Restrictions.eq(Project.IDPROJECT, project.getIdProject()));
		
		
	
		return ((Integer)crit.uniqueResult() > 0);
	}

	/**
	 * Find projects by program and show or not show projects disabled
	 * 
	 * @param program
	 * @param projectsDisabled
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByProgramAndProjectsDisabled(Program program, boolean projectsDisabled) {
		
		List<Project> projects = null;
		
		if (program.getIdProgram() != -1) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass());
			
			crit.add(Restrictions.eq("program.idProgram", program.getIdProgram()));
			
			// Projects not disabled
			if (projectsDisabled) {
				
				crit.add(Restrictions.disjunction()
						.add(Restrictions.ne(Project.DISABLE, Constants.SELECTED))
						.add(Restrictions.isNull(Project.DISABLE))
						);
			}
			
			projects = crit.list();
		}
		
		return projects;
	}

	/**
	 * 
	 * @param performingorg
	 * @param projectsDisabled
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByPO(Performingorg performingorg, boolean projectsDisabled) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.eq(Project.PERFORMINGORG, performingorg))
				.add(Restrictions.or(
						Restrictions.eq(Project.STATUS, Constants.STATUS_CONTROL),
						Restrictions.eq(Project.STATUS, Constants.STATUS_PLANNING))
					);
		
		// Projects not disabled
		if (projectsDisabled) {
			
			crit.add(Restrictions.disjunction()
					.add(Restrictions.ne(Project.DISABLE, Constants.SELECTED))
					.add(Restrictions.isNull(Project.DISABLE))
					);
		}
		
		return crit.list();	
	}

	/**
	 * Select projects order by programs and finish date
	 * 
	 * @param idProjects
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> consListOrderByProgramsAndFinishDate(Integer[] idProjects) {

		List<Project> list = new ArrayList<Project>();
		
		if (ValidateUtil.isNotNull(idProjects)) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
					.add(Restrictions.in(Project.IDPROJECT, idProjects));
			
			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			crit.addOrder(Order.asc(Project.PROGRAM));
			
			crit.addOrder(Order.asc(Project.FINISHDATE));
			
			list =  crit.list();
		}
		
		return list;
	}

	/**
	 * Select projects order by categories and finish date
	 * 
	 * @param idProjects
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> consListOrderByCategorysAndFinishDate(Integer[] idProjects) {
		
		List<Project> list = new ArrayList<Project>();
		
		if (ValidateUtil.isNotNull(idProjects)) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
					.add(Restrictions.in(Project.IDPROJECT, idProjects));
			
			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			crit.addOrder(Order.desc(Project.CATEGORY));
			
			crit.addOrder(Order.asc(Project.FINISHDATE));
			
			list =  crit.list();
		}
		
		return list;
	}

	/**
	 * Find by user role and not Initiating project
	 * 
	 * @param user
	 * @param enableDisable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByUser(Employee user, Boolean enableDisable) {
		
		// Create criteria
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		// Not initiating projects
		crit.add(Restrictions.ne(Project.STATUS, Constants.STATUS_INITIATING));
		
		if (Constants.ROLE_PM == user.getResourceprofiles().getIdProfile()) {
			
			// By Project Manager
			crit.add(Restrictions.eq(Project.EMPLOYEEBYPROJECTMANAGER, user));
		}
		else if (Constants.ROLE_FM == user.getResourceprofiles().getIdProfile()) {
			
			// By Functional Manager
			crit.add(Restrictions.eq(Project.EMPLOYEEBYFUNCTIONALMANAGER, user));
		}
		else if (Constants.ROLE_IM == user.getResourceprofiles().getIdProfile()) {
			
			// By Investment Manager
			crit.add(Restrictions.eq(Project.EMPLOYEEBYINVESTMENTMANAGER, user));
		}
		else if (Constants.ROLE_SPONSOR == user.getResourceprofiles().getIdProfile()) {
			
			// By Sponsor
			crit.add(Restrictions.eq(Project.EMPLOYEEBYSPONSOR, user));
		}
		else {
			
			// Other case by PO
			crit.add(Restrictions.eq(Project.PERFORMINGORG, user.getPerformingorg()));
		}
		
		// If setting disabled is enabled restrict projects not disabled
		if (enableDisable) {
			
			crit.add(Restrictions.disjunction()
				.add(Restrictions.ne(Project.DISABLE, Boolean.TRUE))
				.add(Restrictions.isNull(Project.DISABLE))
			);
		}
		
		// order by project name
		crit.addOrder(Order.asc(Project.PROJECTNAME));

        List<String> joins = new ArrayList<String>();
        joins.add(Project.PERFORMINGORG);
        joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
        joins.add(Project.EMPLOYEEBYPROJECTMANAGER + "." + Employee.CONTACT);

        addJoins(crit, joins);
		
		return crit.list();
	}

    /**
     * Find PMs by employee and dates
     *
     * @param employee
     * @param dates
     * @return
     */
    public List<Project> findPMs(Employee employee, List<Date> dates) {

        // Create criteria
        Criteria crit = getSession().createCriteria(getPersistentClass());

        // Filter not null project managers
        crit.add(Restrictions.isNotNull(Project.EMPLOYEEBYPROJECTMANAGER));

        // Filters teammember by assigned and employee
        Criteria teammembers = crit.createCriteria(Project.PROJECTACTIVITIES)
                .createCriteria(Projectactivity.TEAMMEMBERS)
                .add(Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_ASSIGNED))
                .add(Restrictions.eq(Teammember.EMPLOYEE, employee));

        // Filter dates
        if (ValidateUtil.isNotNull(dates)) {

            for (Date date : dates) {

                teammembers.add(Restrictions.and(
                                Restrictions.le(Teammember.DATEIN, date),
                                Restrictions.ge(Teammember.DATEOUT, date)
                        )
                );
            }
        }

        return crit.list();
    }

	/**
	 * Search projects with list project status
	 *
	 * @param po
	 * @param projectStatus
	 * @param projectsDisabled
	 * @return
	 */
	public List<Project> findByPOAndProjectStatus(Performingorg po, List<String> projectStatus, boolean projectsDisabled) {

		// Create criteria
		Criteria crit = getSession().createCriteria(getPersistentClass());

		// Filter by PO
        if (po != null) {
            crit.add(Restrictions.eq(Project.PERFORMINGORG, po));
        }

		// Filter by proejct status
        if (ValidateUtil.isNotNull(projectStatus)) {
            crit.add(Restrictions.in(Project.STATUS, projectStatus));
        }

		// Projects not disabled
		if (projectsDisabled) {

			crit.add(Restrictions.disjunction()
							.add(Restrictions.ne(Project.DISABLE, Constants.SELECTED))
							.add(Restrictions.isNull(Project.DISABLE))
			);
		}

		// Filter investments rejected
		crit.add(Restrictions.ne(Project.INVESTMENTSTATUS, Constants.INVESTMENT_REJECTED));

		// Order
		crit.addOrder(Order.asc(Project.CHARTLABEL));

		return crit.list();
	}


    /**
     * Find investments in process and not disabled
     *
     * @return
     */
    public List<Project> findInvestmentsInProcess() {

        // Create criteria
        Criteria crit = getSession().createCriteria(getPersistentClass());


        // Filter Not disabled projects
        crit.add(Restrictions.disjunction()
                        .add(Restrictions.ne(Project.DISABLE, Constants.SELECTED))
                        .add(Restrictions.isNull(Project.DISABLE))
        );

        // Filter investments rejected
        crit.add(Restrictions.eq(Project.INVESTMENTSTATUS, Constants.INVESTMENT_IN_PROCESS));

        // Order
        crit.addOrder(Order.asc(Project.CHARTLABEL));

        return crit.list();
    }

    /**
     * Update Effort
     *
     * @param project
     */
    public synchronized void updateEffort(Project project) {

        Query query = getSession().createQuery(
                "update Project p " +
                        " set p.effort = :effort"+
                        " where p.idProject = :idProject"
        );

        query.setInteger("idProject", project.getIdProject());
        query.setInteger("effort", project.getEffort());
        query.executeUpdate();
    }

    /**
     * Update BAC
     *
     * @param project
     */
    public synchronized void updateBAC(Project project) {

        Query query = getSession().createQuery(
                "update Project p " +
                        " set p.bac = :bac"+
                        " where p.idProject = :idProject"
        );

        query.setInteger("idProject", project.getIdProject());
        query.setDouble("bac", project.getBac());
        query.executeUpdate();
    }

    /**
     * Find by search
     *
     * @param searchParams
     * @return
     */
    public List<Project> find(ProjectSearch searchParams) {

        String q = "SELECT DISTINCT p FROM Project p " +
                "JOIN FETCH p.program pr "+
                "LEFT JOIN FETCH pr.employee poe " +
                "LEFT JOIN FETCH poe.contact " +

                "JOIN FETCH p.performingorg po " +

                "LEFT JOIN FETCH p.employeeByProjectManager pm " +
                "LEFT JOIN FETCH pm.contact pmContact " +

                "LEFT JOIN FETCH p.employeeBySponsor sp " +
                "LEFT JOIN FETCH sp.contact " +

                "LEFT JOIN FETCH p.customer " +
                "LEFT JOIN FETCH p.geography " +
                "LEFT JOIN FETCH p.contracttype " +

                "LEFT JOIN p.stakeholders stk " +
                "LEFT JOIN p.projectactivities pa "+
                "LEFT JOIN pa.activitysellers actSell " +
                "LEFT JOIN p.projectfundingsources fs " +
                "LEFT JOIN p.projectlabels pl " +
                "LEFT JOIN p.projecttechnologies pt " +
                "LEFT JOIN p.category cat " +
                "LEFT JOIN p.classificationlevel clalvl " +
                "JOIN pa.wbsnode w ";

        return createQuery(q, searchParams).list();
    }
}