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
 * File: EmployeeDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.persistance.MappingGenericDAO;
import es.sm2.openppm.core.logic.persistance.MappingHql;
import es.sm2.openppm.core.logic.persistance.bean.NamedRestriction;
import es.sm2.openppm.core.logic.persistance.hql.employee.EmployeeNamedQuery;
import es.sm2.openppm.core.logic.persistance.hql.employee.EmployeeNamedRestriction;
import es.sm2.openppm.core.logic.persistance.hql.generic.GenericNamedOrder;
import es.sm2.openppm.core.logic.persistance.hql.generic.NamedParams;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Expensesheet;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Jobcatemployee;
import es.sm2.openppm.core.model.impl.Managepool;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Profile;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Skill;
import es.sm2.openppm.core.model.impl.Skillsemployee;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.search.EmployeeSearch;
import es.sm2.openppm.core.model.search.TimeSheetOperationSearch;
import es.sm2.openppm.core.model.wrap.ApprovalWrap;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class EmployeeDAO extends MappingGenericDAO<Employee, Integer> {

	public EmployeeDAO(Session session) {
		super(session);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Employee> searchByExample(Employee exampleInstance,
			Class... joins) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		if (exampleInstance.getPerformingorg() != null) {
			crit.add(Restrictions.eq("performingorg.idPerfOrg", exampleInstance.getPerformingorg().getIdPerfOrg()));
		}
		if (exampleInstance.getResourceprofiles() != null) { 
			crit.add(Restrictions.eq("resourceprofiles.idProfile", exampleInstance.getResourceprofiles().getIdProfile()));
		}
		crit.setFetchMode("contact", FetchMode.JOIN);
		for (Class c : joins) {
			if (c.equals(Employee.class)) {
				crit.setFetchMode("employee", FetchMode.JOIN);
				crit.setFetchMode("employee.contact", FetchMode.JOIN);
			} else if (c.equals(Performingorg.class)) {
				crit.setFetchMode("performingorg", FetchMode.JOIN);
			} else if (c.equals(Resourceprofiles.class)) {
				crit.setFetchMode("resourceprofiles", FetchMode.JOIN);
			}
		}
		return crit.list();
	}
	

	/**
	 * Find by id with Contact
	 * @param emp
	 * @return
	 */
	public Employee findByIdWithContact(Employee emp) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		crit.setFetchMode("contact", FetchMode.JOIN);
		crit.setFetchMode("contact.company", FetchMode.JOIN);
		crit.add(Restrictions.eq("idEmployee", emp.getIdEmployee()));
		return (Employee) crit.uniqueResult();
	}


    /**
     * Search employees by criteria
     *
     * @param idProfile
     * @param idJobCategories
     * @param org
     * @param company
     * @param idResourcePool
     * @param idSkills
     * @param idSeller
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Employee> searchByFilter(Integer idProfile, String idJobCategories,
			Performingorg org, Company company, Integer idResourcePool, String idSkills, Integer idSeller) {
		
		List<Employee> employees = new ArrayList<Employee>();
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(
				"select distinct employee.idEmployee " +
				"from Employee as employee " +
				"join employee.contact as c " +
				"join employee.performingorg as perfOrg " +
				"join perfOrg.company as company " +
				"left join employee.jobcatemployees as jobs " +
				"left join jobs.jobcategory as jobcategory " +
				"left join employee.skillsemployees as skillsemployee " +
				"left join skillsemployee.skill as skill " +
				"left join employee.seller as seller " +
				"join employee.resourceprofiles as profiles " +
				"where company.idCompany = :idCompany " +
					"and (employee.disable is null or employee.disable != true) " +
					"and (c.disable is null or c.disable != true)"); 
		
		//filter by PO
		if (org.getIdPerfOrg() != -1) {
			queryString.append("and perfOrg.idPerfOrg = :idPerfOrg ");
		}
		
		//filter by profile
		if (idProfile != -1) {
			queryString.append("and profiles.idProfile = :idProfile ");
		}
		
		if (idResourcePool != null && idResourcePool != -1) {
			queryString.append("and employee.resourcepool.idResourcepool = :idResourcePool ");
		}
		
		if (!ValidateUtil.isNull(idJobCategories)) { 
			queryString.append("and jobcategory.idJobCategory in ("+idJobCategories+") ");
		}
		
		if (!ValidateUtil.isNull(idSkills)) { 
			queryString.append("and skill.idSkill in ("+idSkills+") ");
		}
		
		if (idSeller != null && idSeller != -1) {
			queryString.append("and seller.idSeller = :idSeller ");
		}
		
		queryString.append("group by employee");
		
		Query query = getSession().createQuery(queryString.toString());
		
		//Set the attributes of the filters
		query.setInteger("idCompany", company.getIdCompany());
		
		if (org.getIdPerfOrg() != -1) {
			query.setInteger("idPerfOrg", org.getIdPerfOrg());
		}
		
		if (idProfile != -1) {
			query.setInteger("idProfile", idProfile);
		}
		
		if (idResourcePool != null && idResourcePool != -1) {
			query.setInteger("idResourcePool", idResourcePool);
		}
		
		if (idSeller != null && idSeller != -1) {
			query.setInteger("idSeller", idSeller);
		}

		List<Integer> idEmployees = query.list();
		StringBuilder ids = new StringBuilder();
		
		for (Integer id : idEmployees) {
			if (ids.toString().equals(StringPool.BLANK)) {
				ids.append(id.toString());
			}
			else {
				ids.append(StringPool.COMMA+id.toString());
			}
		}
		
		if (!ids.toString().equals(StringPool.BLANK)) {
			Query query2 = getSession().createQuery(
					"select distinct employee " +
					"from Employee as employee " +
					"left join fetch employee.teammembers as teammembers " +
					"join fetch employee.contact as c " +
					"join fetch employee.resourcepool as rm " +
					"join fetch employee.performingorg as perfOrg " +
					"left join fetch employee.jobcatemployees as jobs " +
					"left join fetch jobs.jobcategory as jobcategory " +
					"left join fetch employee.skillsemployees as skillsemployee " +
					"left join fetch skillsemployee.skill as skill " +
					"left join fetch employee.seller as seller " +
					"join fetch employee.resourceprofiles as profiles " +
					"where employee.idEmployee in ("+ids.toString()+")"
					);
			employees = query2.list();
		}
		
		return employees;
	}


	/**
	 * Return employee with data
	 * @param employee
	 * @return
	 */
	public Employee findByIdEmployee(Employee employee) {
		
		Employee empl = null;
		
		if (employee.getIdEmployee() != null) {
			Criteria crit = getSession().createCriteria(getPersistentClass());
			crit.setFetchMode(Employee.CONTACT, FetchMode.JOIN);
			crit.setFetchMode(Employee.CONTACT+"."+ Contact.COMPANY, FetchMode.JOIN);
			crit.setFetchMode(Employee.PERFORMINGORG, FetchMode.JOIN);
			crit.setFetchMode(Employee.RESOURCEPROFILES, FetchMode.JOIN);
			crit.add(Restrictions.eq("idEmployee", employee.getIdEmployee()));
			empl = (Employee) crit.uniqueResult();
		}
		
		return empl;
	}

	
	/**
	 * Search Employee By filter
	 * @param name
	 * @param jobTitle
	 * @param idProfile
	 * @param idPerfOrg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> searchByFilter(String name, String jobTitle,
			Integer idProfile, Integer idPerfOrg, Company company) {
		
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.or(
				Restrictions.isNull(Employee.DISABLE),
				Restrictions.ne(Employee.DISABLE, true))
			);
		
		crit.createCriteria(Employee.CONTACT)
			.add(Restrictions.ilike(Contact.FULLNAME, "%"+name+"%"))
			.add(Restrictions.ilike(Contact.JOBTITLE, "%"+jobTitle+"%"))
			.add(Restrictions.eq(Contact.COMPANY, company))
			.add(Restrictions.or(
				Restrictions.isNull(Contact.DISABLE),
				Restrictions.ne(Contact.DISABLE, true))
			)
			.createCriteria(Contact.COMPANY)
				.add(Restrictions.or(
					Restrictions.isNull(Company.DISABLE),
					Restrictions.ne(Company.DISABLE, true))
				);
		
		if (idProfile != -1) {
			crit.add(Restrictions.eq(Employee.RESOURCEPROFILES, new Resourceprofiles(idProfile)));
		}
		if (idPerfOrg != -1) {
			crit.add(Restrictions.eq(Employee.PERFORMINGORG, new Performingorg(idPerfOrg)));
		}
		
		crit.setFetchMode(Employee.CONTACT, FetchMode.JOIN);
		crit.setFetchMode(Employee.CONTACT+"."+Contact.COMPANY, FetchMode.JOIN);
		crit.setFetchMode(Employee.PERFORMINGORG, FetchMode.JOIN);
		
		return crit.list();
	}
	
	
	/**
	 * Calculate FTE of Team Members for these employee
	 * @param employee
	 * @param dateOut 
	 * @param dateIn 
	 * @return
	 */
	public double calculateFTE(Employee employee, Date dateIn, Date dateOut) {
		
		double fte = 0;
		if (employee.getIdEmployee() != null) {
			Query query = getSession().createQuery(
					"select sum(t.fte) " +
					"from Teammember as t " +
					"where ((t.dateIn between :datein and :dateout) " +
							"or (t.dateOut between :datein and :dateout) " +
							"or (:datein between t.dateIn and t.dateOut) " +
							"or (:dateout between t.dateIn and t.dateOut)) " +
						"and t.employee.idEmployee = :idEmployee");
			query.setInteger("idEmployee", employee.getIdEmployee());
			query.setDate("datein", dateIn);
			query.setDate("dateout", dateOut);
			if (query.uniqueResult() != null) {
				fte = (Long)query.uniqueResult();
			}
		}
		return fte;
	}

	@SuppressWarnings("unchecked")
	public List<Employee> consEmployeesByUser(Contact contact) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.or(
					Restrictions.isNull(Employee.DISABLE),
					Restrictions.ne(Employee.DISABLE, true))
				);
		
		crit.createCriteria(Employee.RESOURCEPROFILES)
			.addOrder(Order.asc(Resourceprofiles.PROFILENAME));
		
		crit.setFetchMode(Employee.RESOURCEPROFILES, FetchMode.JOIN)
			.setFetchMode(Employee.PERFORMINGORG, FetchMode.JOIN);
		
		crit.createCriteria(Employee.CONTACT)
			.add(Restrictions.idEq(contact.getIdContact()))
			.add(Restrictions.or(
					Restrictions.isNull(Contact.DISABLE),
					Restrictions.ne(Contact.DISABLE, true))
				)
			.createCriteria(Contact.COMPANY)
				.add(Restrictions.or(
					Restrictions.isNull(Company.DISABLE),
					Restrictions.ne(Company.DISABLE, true))
				);
		
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public Employee findResourceByPM(Employee pm) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Employee.CONTACT, pm.getContact()))
			.add(Restrictions.eq(Employee.RESOURCEPROFILES, new Resourceprofiles(Constants.ROLE_RESOURCE)))
			.add(Restrictions.eq(Employee.PERFORMINGORG, pm.getPerformingorg()));
		
		List<Employee> list = crit.list();
		
		return (list != null && !list.isEmpty()?list.get(0):null);
	}

	/**
	 * Find employees by PO and Rol
	 * @param performingorg
	 * @param role
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> findByPOAndRol(Performingorg performingorg, int role) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		if (performingorg != null) {
			crit.add(Restrictions.eq(Employee.PERFORMINGORG, performingorg));
		}
		
		crit.add(Restrictions.eq(Employee.RESOURCEPROFILES, new Resourceprofiles(role)))
			.setFetchMode(Employee.CONTACT, FetchMode.JOIN)
			.createCriteria(Employee.CONTACT)
			.addOrder(Order.asc(Contact.FULLNAME));
		
		crit.setFetchMode(Employee.PERFORMINGORG, FetchMode.JOIN);
		
		return crit.list();
	}
	
	/**
	 * Search employee by contact and PO and rol
	 * @param idContact
	 * @param performingorg
	 * @param idProfile
	 * @return
	 */
	public Employee findByContactAndPOAndRol(int idContact, Performingorg performingorg, int idProfile) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		crit.add(Restrictions.eq(Employee.CONTACT, new Contact(idContact)));
		
		if (performingorg != null) {
			crit.add(Restrictions.eq(Employee.PERFORMINGORG, performingorg));
		}
		
		crit.add(Restrictions.eq(Employee.RESOURCEPROFILES, new Resourceprofiles(idProfile)))
			.setFetchMode(Employee.CONTACT, FetchMode.JOIN)
			.createCriteria(Employee.CONTACT)
			.addOrder(Order.asc(Contact.FULLNAME));
		
		return (Employee) crit.uniqueResult();
	}

	/**
	 * Search employee by rol an company
	 * @param profile
	 * @param company
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> searchEmployees(Resourceprofiles profile, Company company, Performingorg performingorg,
			List<String> joins) {
		
//		Criteria crit = getSession().createCriteria(getPersistentClass())
//				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		addJoins(crit, joins);

		crit.add(Restrictions.eq(Employee.PERFORMINGORG, performingorg));

		crit.add(Restrictions.eq(Employee.RESOURCEPROFILES, profile))
			.createCriteria(Employee.CONTACT)
			.add(Restrictions.eq(Contact.COMPANY, company))
			.addOrder(Order.asc(Contact.FULLNAME));

		//crit.setProjection(Projections.distinct((Projections.property("id"))));

		
		return crit.list();
	}


	/**
	 * 
	 * @param idProjectActivity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> searchEmployeesAssignedToProjectActivity (int idProjectActivity) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.createCriteria(Employee.TEAMMEMBERS)
			.add(Restrictions.eq(Teammember.PROJECTACTIVITY, new Projectactivity(idProjectActivity)))
			.add(Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_ASSIGNED));

		return crit.list();
	}

	/**
	 * Find Approvals TimeSheet
	 * object[0] == idEmployee
	 * object[1] == Name employee
	 * object[2] == idProject
	 * object[3] == Project Name 
	 * @param pm
	 * @param initDate
	 * @param endDate
	 * @param minStatus
	 * @param maxStatus
	 * @param statusResource
	 * @param includeClosed
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findApprovalsTimeByPM(Employee pm, Date initDate,
			Date endDate, String minStatus, String maxStatus, String statusResource,
			boolean includeClosed) {
		
		String orts2StatusApp2 	= "";
		String ortsStatusApp2 	= "";
		
		if (minStatus != null && maxStatus != null) {
			
			ortsStatusApp2 = "and (ts.status = :minStatus or ts.status = :maxStatus ";
			orts2StatusApp2 = "and (ts2.status = :minStatus or ts2.status = :maxStatus ";
			
			if (Constants.TIMESTATUS_APP0.equals(minStatus) && (
					Constants.TIMESTATUS_APP2.equals(maxStatus) ||
					Constants.TIMESTATUS_APP3.equals(maxStatus))) {
			
				ortsStatusApp2 += "or ts.status = :app1 ";
				orts2StatusApp2 += "or ts2.status = :app1 ";
			}
			
			if (Constants.TIMESTATUS_APP3.equals(maxStatus) && (
					Constants.TIMESTATUS_APP0.equals(minStatus) ||
					Constants.TIMESTATUS_APP1.equals(minStatus))) {
				
				ortsStatusApp2 += "or ts.status = :app2 ";
				orts2StatusApp2 += "or ts2.status = :app2 ";
			}
			
			ortsStatusApp2 += ") ";
			orts2StatusApp2 += ") ";
		}

		Query query = getSession().createQuery(
				"select distinct e.idEmployee,c.fullName ,p.idProject, p.projectName " +
				"from Employee as e " +
				"join e.timesheets as ts " +
				"join e.contact as c " +
				"join ts.projectactivity as pa " +
				"join pa.teammembers tm " +
				"join pa.project as p " +
				"where " +
					"tm.employee = e and " +
					(ValidateUtil.isNotNull(statusResource)?"tm.status = :statusResource and ":"") +
					"(p.employeeByProjectManager = :pm or " +
						"(ts.projectactivity is null " +
						"and e.performingorg = :po " +
						"and e.idEmployee not in " +
							 "(select distinct e2.idEmployee " +
								 "from Employee as e2 " +
								 "join e2.timesheets as ts2 " +
								 "join ts2.projectactivity as pa2 " +
								 "join pa2.project as p2 " +
								 "where p2.employeeByProjectManager = :pm " +
								 	"and ts2.initDate = :initDate " +
								 	"and ts2.endDate = :endDate " +
								 	orts2StatusApp2+
							 ")" +
						")" +
						 "and e.idEmployee in " +
							 "(select distinct e3.idEmployee " +
							 	"from Employee as e3 " +
								 "join e3.timesheets as ts3 " +
								 "join ts3.projectactivity as pa3 " +
								 "join pa3.project as p3 " +
								 "where p3.employeeByProjectManager = :pm " +
								 	"and ts3.initDate = :initDate " +
								 	"and ts3.endDate = :endDate " +
								 	"and ts3.status = :app0" +
							")" +
					") " +
					"and ts.initDate = :initDate " +
					"and ts.endDate = :endDate " +
					ortsStatusApp2+
					(!includeClosed?"and p.status != :statusClosed and p.status != :statusArchived ":""));
		
		query.setInteger("pm", pm.getIdEmployee());
		query.setDate("initDate", initDate);
		query.setDate("endDate", endDate);
		query.setEntity("po", pm.getPerformingorg());
		query.setString("app0", Constants.TIMESTATUS_APP0);
		
		if (!includeClosed) {
			query.setString("statusClosed", Constants.STATUS_CLOSED);
            query.setString("statusArchived", Constants.STATUS_ARCHIVED);
		}
		
		if (ValidateUtil.isNotNull(statusResource)) {
			query.setString("statusResource", statusResource);
		}
		
		if (minStatus != null && maxStatus != null) {
			
			query.setString("maxStatus", maxStatus);
			query.setString("minStatus", minStatus);
			
			
			if (Constants.TIMESTATUS_APP0.equals(minStatus) && (
					Constants.TIMESTATUS_APP2.equals(maxStatus) ||
					Constants.TIMESTATUS_APP3.equals(maxStatus))) {
			
				query.setString("app1", Constants.TIMESTATUS_APP1);
			}
			
			if (Constants.TIMESTATUS_APP3.equals(maxStatus) && (
					Constants.TIMESTATUS_APP0.equals(minStatus) ||
					Constants.TIMESTATUS_APP1.equals(minStatus))) {
				
				query.setString("app2", Constants.TIMESTATUS_APP2);
			}
		}
		
		return query.list();
	}

	/**
	 * Find Approvals Expense Sheet
	 * object[0] == idEmployee
	 * object[1] == Name employee
	 * object[2] == idProject
	 * object[3] == Project Name 
	 * @param pm
	 * @param initDate
	 * @param endDate
	 * @param maxStatus 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findApprovalsExpenseByPM(Employee pm, Date initDate, Date endDate, String maxStatus) {
		
		Query query = getSession().createQuery(
				"select distinct e.idEmployee,c.fullName ,p.idProject, p.projectName " +
				"from Employee as e " +
				"join e.expensesheets as ex " +
				"join ex.project as p " +
				"join e.contact as c " +
				"where p.employeeByProjectManager = :pm " +
					"and (ex.expenseDate between :initDate and :endDate) " +
					"and (ex.status = :app1 or ex.status = :maxStatus)");
		
		query.setInteger("pm", pm.getIdEmployee());
		query.setDate("initDate", initDate);
		query.setDate("endDate", endDate);
		query.setString("app1", Constants.EXPENSE_STATUS_APP1);
		query.setString("maxStatus", maxStatus);
		
		return query.list();
	}

	/**
	 * Find Approvals Time Sheets by Functional Manager
	 * 
	 * @param user
	 * @param initDate
	 * @param endDate
	 * @param status
	 * @param statusResource
	 * @param includeClosed
	 * @param onlyOperations 
	 * @param employees - not equals
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> findApprovals(Employee user, Date initDate,
			Date endDate, String status, String statusResource,
			boolean includeClosed, List<Employee> employees,
			boolean onlyOperations) {
		
		Resourceprofiles profile = user.getResourceprofiles();
		
		Criteria crit = getSession().createCriteria(getPersistentClass(),"e")
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.setFetchMode(Employee.CONTACT, FetchMode.JOIN);
		
		if (ValidateUtil.isNotNull(employees)) {
			
			List<Integer> ids = new ArrayList<Integer>();
			for (Employee item : employees) { ids.add(item.getIdEmployee()); }
			
			crit.add(Restrictions.not(Restrictions.in("e."+Employee.IDEMPLOYEE, ids)));
		}
		
		crit.createCriteria(Employee.TIMESHEETS,"ts")
				.add(Restrictions.eq(Timesheet.INITDATE, initDate))
				.add(Restrictions.eq(Timesheet.ENDDATE, endDate))
				.add(Restrictions.eq(Timesheet.STATUS, status))
				.createAlias(Timesheet.PROJECTACTIVITY,"pa", Criteria.LEFT_JOIN)
				.createAlias("pa."+Projectactivity.PROJECT,"p", Criteria.LEFT_JOIN);
		
		if (ValidateUtil.isNotNull(statusResource)) {
			crit.createCriteria("pa."+Projectactivity.TEAMMEMBERS)
				.add(Restrictions.eq(Teammember.STATUS, statusResource))
				.createCriteria(Teammember.EMPLOYEE)
					.add(Restrictions.eqProperty(Employee.IDEMPLOYEE, "e."+Employee.IDEMPLOYEE));
		}
		else if (onlyOperations) {
			crit.add(Restrictions.isNotNull("ts."+Timesheet.OPERATION));
		}
		
		// Exluce projects closed
		if (!includeClosed) {
			crit.add(Restrictions.and(
                    Restrictions.ne("p."+Project.STATUS, Constants.STATUS_CLOSED),
                    Restrictions.ne("p."+Project.STATUS, Constants.STATUS_ARCHIVED)));
		}
		
		// Filter by User. Settings by company for last approval. 
		if (profile.getIdProfile() == Constants.ROLE_FM) {
			
			crit.add(Restrictions.or(
					Restrictions.eq("p."+Project.EMPLOYEEBYFUNCTIONALMANAGER, user),
					Restrictions.and(
							Restrictions.isNull("ts."+Timesheet.PROJECTACTIVITY),
							Restrictions.eq("e."+Employee.PERFORMINGORG, user.getPerformingorg())
						)
				));
		}
		else if (profile.getIdProfile() == Constants.ROLE_PMO) {
			
			crit.add(Restrictions.or(
					Restrictions.eq("p."+Project.PERFORMINGORG, user.getPerformingorg()),
					Restrictions.and(
							Restrictions.isNull("ts."+Timesheet.PROJECTACTIVITY),
							Restrictions.eq("e."+Employee.PERFORMINGORG, user.getPerformingorg())
						)
				));
		}
		
		return crit.list();
	}

	/**
	 * Find Approvals Expenses by Functional Manager
	 * @param user
	 * @param since
	 * @param until
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> findApprovalsExpense(Employee user,
			Date since, Date until) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.setFetchMode(Employee.CONTACT, FetchMode.JOIN);
		
		crit.createCriteria(Employee.EXPENSESHEETS)
			.add(Restrictions.between(Expensesheet.EXPENSEDATE, since, until))
			.add(Restrictions.or(
					Restrictions.eq(Expensesheet.STATUS, Constants.TIMESTATUS_APP2),
					Restrictions.eq(Expensesheet.STATUS, Constants.TIMESTATUS_APP3)
				));
		
		crit.add(Restrictions.eq(Employee.PERFORMINGORG, user.getPerformingorg()));
		
		return crit.list();
	}

	/**
	 * Find Employees where inputed hours is approval
	 * 
	 * @param project
	 * @param since
	 * @param until
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> findInputedInProject(Project project, Date since,
			Date until) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		Criteria members = crit.createCriteria(Employee.TEAMMEMBERS)
				.add(Restrictions.or(
						Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_ASSIGNED), 
						Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_RELEASED)));
		
		
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
		members.createCriteria(Teammember.PROJECTACTIVITY)
			.add(Restrictions.eq(Projectactivity.PROJECT, project));
		
		Criteria sheets = crit.createCriteria(Employee.TIMESHEETS)
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
		sheets.createCriteria(Timesheet.PROJECTACTIVITY)
			.add(Restrictions.eq(Projectactivity.PROJECT, project));
		
		crit.createCriteria(Employee.CONTACT).addOrder(Order.asc(Contact.FULLNAME));
		
		crit.setFetchMode(Employee.CALENDARBASE, FetchMode.JOIN);
		
		return crit.list();
	}
	

	/**
	 * List employees by company
	 * @param company
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> findByCompany(Company company, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		crit.createCriteria(Employee.CONTACT)
			.add(Restrictions.eq(Contact.COMPANY, company));
		
		addJoins(crit, joins);
		
		return crit.list();
	}

	/**
	 * Check if token is in use
	 * @param token
	 * @return
	 */
	public boolean tokenInUse(String token) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Employee.TOKEN, token));
			
		return ((Integer) crit.uniqueResult() > 0);
	}

	/**
	 * Get employee for generate token
	 * @param contact
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> findForToken(Contact contact) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Employee.CONTACT, contact))
			.add(Restrictions.or(
				Restrictions.eq(Employee.RESOURCEPROFILES, new Resourceprofiles(Constants.ROLE_PMO)),
				Restrictions.eq(Employee.RESOURCEPROFILES, new Resourceprofiles(Constants.ROLE_PM))));
		
		List<String> joins = new ArrayList<String>();
		joins.add(Employee.PERFORMINGORG);
		joins.add(Employee.RESOURCEPROFILES);
		
		addJoins(crit, joins);
		
		crit.addOrder(Order.asc(Employee.PERFORMINGORG));
		crit.addOrder(Order.asc(Employee.RESOURCEPROFILES));
		
		return crit.list();
	}

	/**
	 * Find employee by token
	 * @param token
	 * @return
	 */
	public Employee findByToken(String token) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Employee.TOKEN, token));
		
		List<String> joins = new ArrayList<String>();
		joins.add(Employee.PERFORMINGORG);
		joins.add(Employee.RESOURCEPROFILES);
		
		addJoins(crit, joins);
		
		return (Employee) crit.uniqueResult();
	}

	/**
	 * Find by Resource Manager
	 * @param listResourcepool
	 * @param fullName
	 * @param joins
	 * @param typeOrder
     * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> findByManager(List<Resourcepool> listResourcepool, String fullName,
                                        List<String> joins, String typeOrder) {
	
		Criteria crit = getSession().createCriteria(getPersistentClass());

        if (ValidateUtil.isNotNull(listResourcepool)) {
            crit.add(Restrictions.in(Employee.RESOURCEPOOL, listResourcepool));
        }

        Criteria critContact = crit.createCriteria(Employee.CONTACT)
                .add(Restrictions.ilike(Contact.FULLNAME, "%"+fullName+"%"));

        // Order by contact
        addOrder(critContact, Contact.FULLNAME, typeOrder);

		addJoins(crit, joins);
		
		return crit.list();
	}

    /**
     * Find imputed in projects
     *
     * @param projects
     * @param since
     * @param until
     * @param idResourcePool
     * @param activities
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Employee> findInputedInProjects(List<Project> projects,
			Date since, Date until, Integer idResourcePool, List<Projectactivity> activities) {
		
		List<Employee> employees = null;
		
		if (ValidateUtil.isNotNull(projects) || ValidateUtil.isNotNull(activities)) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			if (idResourcePool != null) {
				crit.add(Restrictions.eq(Employee.RESOURCEPOOL, new Resourcepool(idResourcePool)));
			}
			
			// Timesheet filter by status app3 and dates and projects
			//
			Criteria sheets = crit.createCriteria(Employee.TIMESHEETS)
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
			
			if (projects != null) {
                Criteria activitiesCrit = sheets.createCriteria(Timesheet.PROJECTACTIVITY)
                        .add(Restrictions.in(Projectactivity.PROJECT, projects));

                // Select employees whose activity is control account
                Criteria wbsnodeCrit = activitiesCrit.createCriteria(Projectactivity.WBSNODE)
                        .add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true));

            }
			else if (activities != null) {
				sheets.add(Restrictions.in(Timesheet.PROJECTACTIVITY, activities));
			}
			
			crit.createCriteria(Employee.CONTACT)
			.addOrder(Order.asc(Contact.FULLNAME));
			
			employees = crit.list();
		}
		
		return employees;
	}
	
	@SuppressWarnings("unchecked")
	public List<Employee> findInputedInOperations(List<Employee> employees,
			Date since, Date until, Integer idResourcePool) {
		
		List<Employee> returnEmployees = null;
		
		if (ValidateUtil.isNotNull(employees)) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			List<Integer> ids = new ArrayList<Integer>();
			for (Employee item : employees) { ids.add(item.getIdEmployee()); }
			
			crit.add(Restrictions.not(Restrictions.in(Employee.IDEMPLOYEE, ids)));
			
			if (idResourcePool != null) {
				crit.add(Restrictions.eq(Employee.RESOURCEPOOL, new Resourcepool(idResourcePool)));
			}
			
			Criteria sheets = crit.createCriteria(Employee.TIMESHEETS)
					.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
					.add(Restrictions.isNotNull(Timesheet.OPERATION));
			
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
			
			crit.createCriteria(Employee.CONTACT)
				.addOrder(Order.asc(Contact.FULLNAME));
			
			returnEmployees = crit.list();
		}

		return returnEmployees;
	}

	/**
	 * Cons employees for login and profile
	 * 
	 * @param contact
	 * @param profile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> consEmployeesByUserAndRol(Contact contact, int profile) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.or(
						Restrictions.isNull(Employee.DISABLE),
						Restrictions.ne(Employee.DISABLE, true))
					);
			
			crit.createCriteria(Employee.RESOURCEPROFILES)
				.add(Restrictions.eq(Resourceprofiles.IDPROFILE, profile));
			
			crit.setFetchMode(Employee.RESOURCEPROFILES, FetchMode.JOIN)
				.setFetchMode(Employee.PERFORMINGORG, FetchMode.JOIN);
			
			crit.createCriteria(Employee.CONTACT)
				.add(Restrictions.idEq(contact.getIdContact()))
				.add(Restrictions.or(
						Restrictions.isNull(Contact.DISABLE),
						Restrictions.ne(Contact.DISABLE, true))
					)
				.createCriteria(Contact.COMPANY)
					.add(Restrictions.or(
						Restrictions.isNull(Company.DISABLE),
						Restrictions.ne(Company.DISABLE, true))
					);
			
			return crit.list();
	}

	/**
	 * Employees by profile and company
	 * 
	 * @param profile
	 * @param company 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> consEmployeesByRol(int profile, Company company) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.or(
						Restrictions.isNull(Employee.DISABLE),
						Restrictions.ne(Employee.DISABLE, true))
					);
			
			crit.createCriteria(Employee.RESOURCEPROFILES)
				.add(Restrictions.eq(Resourceprofiles.IDPROFILE, profile));
			
			crit.setFetchMode(Employee.RESOURCEPROFILES, FetchMode.JOIN)
				.setFetchMode(Employee.PERFORMINGORG, FetchMode.JOIN);
			
			Criteria critContact = crit.createCriteria(Employee.CONTACT);
				
			critContact.createCriteria(Contact.COMPANY)
				.add(Restrictions.and(
						Restrictions.eq(Company.IDCOMPANY, company.getIdCompany()),
						Restrictions.or(
								Restrictions.isNull(Company.DISABLE),
								Restrictions.ne(Company.DISABLE, true)))
					);
			
			return crit.list();
	}

	/**
	 * Consult resource managers by employee
	 * 
	 * @param employee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> consResourceManagers(Employee employee) {
		
		List<Employee> listEmployee = null;
		
		if (employee != null && employee.getResourcepool() != null) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass());
			
			crit.createCriteria(Employee.MANAGEPOOLS)
					.add(Restrictions.eq(Managepool.RESOURCEPOOL, employee.getResourcepool()));
			
			Criteria critContact = crit.createCriteria(Employee.CONTACT);
			
			critContact.createCriteria(Contact.COMPANY)
							.add(Restrictions.or(
								Restrictions.isNull(Company.DISABLE),
								Restrictions.ne(Company.DISABLE, true))
							);
			
			listEmployee = crit.list();
		}
		
		return listEmployee;
	}

	/**
	 * Find Employees where inputed hours is approval
	 * 
	 * @param idResourcePools 
	 * @param projects
	 * @param fullName 
	 * @param idJobCategories 
	 * @param idPMs 
	 * @param idSellers 
	 * @param idCategories 
	 * @param since
	 * @param until
	 * @param order 
	 * @param nameOrder 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> findInputedInProjects(Integer[] idResourcePools, List<Project> projects, Integer[] idPMs, Integer[] idJobCategories, 
			Integer[] idSellers, Integer[] idCategories, String fullName, Date since, Date until, String nameOrder, String order, Employee user) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		// Filter by user
		if (user != null) {
			crit.add(Restrictions.eq(Employee.PERFORMINGORG, user.getPerformingorg()));
		}
		
		// Aliases 
		//
		crit.createAlias(Employee.TEAMMEMBERS, "tm", CriteriaSpecification.INNER_JOIN)
			.createAlias(Employee.TIMESHEETS, "ts", CriteriaSpecification.INNER_JOIN)
			.createAlias("tm."+Teammember.PROJECTACTIVITY, "pa", CriteriaSpecification.INNER_JOIN)
			.createAlias("pa."+Projectactivity.PROJECT, "p", CriteriaSpecification.LEFT_JOIN)
			.createAlias("p."+ Project.EMPLOYEEBYPROJECTMANAGER, "pm", CriteriaSpecification.LEFT_JOIN)
			.createAlias(Employee.RESOURCEPOOL, "rp", CriteriaSpecification.LEFT_JOIN)
			.createAlias(Employee.SELLER, "s", CriteriaSpecification.LEFT_JOIN)
			.createAlias("tm."+Teammember.JOBCATEGORY, "jc", CriteriaSpecification.LEFT_JOIN)
			.createAlias("p."+Project.CATEGORY, "c", CriteriaSpecification.LEFT_JOIN)
			;
		
		// Teammembers 
		//
		crit.add(Restrictions.eq("tm."+Teammember.STATUS, Constants.RESOURCE_ASSIGNED));
		
		if (since != null && until != null) {
			
			crit.add(Restrictions.disjunction()
				.add(Restrictions.between("tm."+Teammember.DATEIN, since, until))
				.add(Restrictions.between("tm."+ Teammember.DATEOUT, since, until))
				.add(Restrictions.and(
						Restrictions.le("tm."+Teammember.DATEIN, since),
						Restrictions.ge("tm."+Teammember.DATEOUT, until)
					)
				)
			);
		}
		
		// Timesheets 
		//
		crit.add(Restrictions.eq("ts."+Timesheet.STATUS, Constants.TIMESTATUS_APP3));

		if (since != null && until != null) {
			
			crit.add(Restrictions.disjunction()
				.add(Restrictions.between("ts."+Timesheet.INITDATE, since, until))
				.add(Restrictions.between("ts."+Timesheet.ENDDATE, since, until))
				.add(Restrictions.and(
						Restrictions.le("ts."+Timesheet.INITDATE, since),
						Restrictions.ge("ts."+Timesheet.ENDDATE, until)
					)
				)
			);
		}
		
		// Filters
		//
		Conjunction conjunction = Restrictions.conjunction();
		
		// Filter by projects
		if (ValidateUtil.isNotNull(projects)) {
			conjunction.add(Restrictions.in("pa."+ Projectactivity.PROJECT, projects));
		}
		// Filter by project managers
		if (ValidateUtil.isNotNull(idPMs)) {
			conjunction.add(Restrictions.in("pm."+Employee.IDEMPLOYEE, idPMs));
		}
		// Filter by resourcepools
		if (ValidateUtil.isNotNull(idResourcePools)) {
			conjunction.add(Restrictions.in("rp." + Resourcepool.IDRESOURCEPOOL, idResourcePools));
		}
		// Filter by sellers
		if (ValidateUtil.isNotNull(idSellers)) {
			conjunction.add(Restrictions.in("s." + Seller.IDSELLER, idSellers));
		}
		// Filter by jobcategories
		if (ValidateUtil.isNotNull(idJobCategories)) {
			conjunction.add(Restrictions.in("jc." + Jobcategory.IDJOBCATEGORY, idJobCategories));
		}
		// Filter by categories
		if (ValidateUtil.isNotNull(idCategories)) {
			conjunction.add(Restrictions.in("c." + Category.IDCATEGORY, idCategories));
		}
		
		crit.add(conjunction);
		
		// Filter by Full Name
		Criteria contactCrit = crit.createCriteria(Employee.CONTACT);

		if (ValidateUtil.isNotNull(fullName)) {
			contactCrit.add(Restrictions.ilike(Contact.FULLNAME, "%"+fullName+"%"));
		}

		// Apply Order
		Criteria orderCrit = null;

		String alias = StringPool.BLANK;


		if (Project.IDPROJECT.equals(nameOrder)) {
			
			alias = "pm.";
			orderCrit = crit; 
		}
		else if (Jobcategory.IDJOBCATEGORY.equals(nameOrder)) { 
			alias = "jc.";
			orderCrit = crit; 
		}
		else {
            orderCrit = contactCrit;
        }
		
		if (nameOrder != null) {
			
			if (Constants.DESCENDENT.equals(order)) { 
				orderCrit.addOrder(Order.desc(alias+nameOrder)); 
			}
			else {
				orderCrit.addOrder(Order.asc(alias+nameOrder));
			}
		}
		
		// Calendar base 
		crit.setFetchMode(Employee.CALENDARBASE, FetchMode.JOIN);
		
		return crit.list();
	}

	/**
	* Filter employees an contacts having timesheets suggest to reject.
	* 
	* @param initDate
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<ApprovalWrap> findSuggestRejectInAprovals(Date initDate) {
	
        // Construct query.
        String q = "SELECT DISTINCT NEW es.sm2.openppm.core.model.wrap.ApprovalWrap( " +
                 "e, " +
                 "(SELECT count(ts.suggestReject) " +
                    " FROM Timesheet ts " +
                    " WHERE ts.initDate = :initDate AND ts.suggestReject is true and ts.employee = e))" +
                 "FROM Timesheet t " +
                 "JOIN t.employee e " +
                 "WHERE t.initDate = :initDate " ;

        Query query = getSession().createQuery(q);

        // Add initDate  for filter.
        query.setDate("initDate", initDate);

        return query.list();
	}

    /**
     * Find unassigned employees by filters
     *
     *
     * @param since
     * @param until
     * @param fullName
     * @param listResourcepool
     * @param idJobCategories
     */
    public List<Employee> findUnassigned(Date since, Date until, String fullName, List<Resourcepool> listResourcepool, Integer[] idJobCategories) {

        // Add Restrictions
        //
        List<NamedRestriction> restrictions = new ArrayList<NamedRestriction>();

        MappingHql mappingHql = this.getMappingHql().setNamedQuery(EmployeeNamedQuery.FIND_UNSIGNED)
                .putRestrictions(EmployeeNamedRestriction.EXCLUDE_IN_DATES);

        if (ValidateUtil.isNotNull(fullName)) {
            mappingHql.putRestrictions(EmployeeNamedRestriction.LIKE_NAME);
        }

        if (ValidateUtil.isNotNull(listResourcepool)) {
            mappingHql.putRestrictions(EmployeeNamedRestriction.IN_POOLS);
        }

        if (ValidateUtil.isNotNull(idJobCategories)) {
            mappingHql.putRestrictions(EmployeeNamedRestriction.IN_CATEGORIES);
        }

        // Create query
        String statement = mappingHql.putOrders(GenericNamedOrder.BY_CONTACT_NAME).create();

        Query query = getSession().createQuery(statement);

        // Set parameters
        //
        if (ValidateUtil.isNotNull(listResourcepool)) {
            query.setParameterList(NamedParams.RESOURCEPOOL.name(), listResourcepool);
        }

        if (ValidateUtil.isNotNull(fullName)) {
            query.setString(NamedParams.FULLNAME.name(), StringPool.PERCENT+fullName.toLowerCase()+StringPool.PERCENT);
        }

        if (ValidateUtil.isNotNull(idJobCategories)) {
            query.setParameterList(NamedParams.CATEGORIES.name(), idJobCategories);
        }

        query.setDate(NamedParams.SINCE.name(), since);
        query.setDate(NamedParams.UNTIL.name(), until);
        query.setString(NamedParams.RESOURCE_ASSIGNED.name(), Constants.RESOURCE_ASSIGNED);

        return query.list();
    }

    /**
     * Find Management operations
     *
     * @param search
     * @param user
     * @param resourcePools
     * @return
     */
    public List<Employee> find(TimeSheetOperationSearch search, Employee user, List<Resourcepool> resourcePools) {

        Criteria timeSheetCrit = getSession().createCriteria(Timesheet.class);

        // Operation
        //
        timeSheetCrit.createCriteria(Timesheet.OPERATION)
                .add(Restrictions.eq(Operation.IDOPERATION, search.getCodeOperation()))
                .add(Restrictions.eq(Operation.AVAILABLEFORMANAGER, Boolean.TRUE));

        if (search.getSince() != null && search.getUntil() != null) {

            timeSheetCrit.add(Restrictions.disjunction()
                            .add(Restrictions.between(Timesheet.INITDATE, search.getSince(), search.getUntil()))
                            .add(Restrictions.between(Timesheet.ENDDATE, search.getSince(), search.getUntil()))
                            .add(Restrictions.and(
                                            Restrictions.le(Timesheet.INITDATE, search.getSince()),
                                            Restrictions.ge(Timesheet.ENDDATE, search.getUntil())
                                    )
                            )
            );
        }

        // Filter by NOT Resource
        if (user != null && user.getResourceprofiles() != null &&
                !Resourceprofiles.Profile.RESOURCE.equals(user.getResourceprofiles().getProfile())) {

            List<String> statusList = new ArrayList<String>();
            statusList.add(Constants.TIMESTATUS_APP1);
            statusList.add(Constants.TIMESTATUS_APP2);
            statusList.add(Constants.TIMESTATUS_APP3);

            timeSheetCrit.add(Restrictions.in(Timesheet.STATUS, statusList));
        }

        // Employee
        //
        Criteria employeeCriteria = timeSheetCrit.createCriteria(Timesheet.EMPLOYEE);

        if (ValidateUtil.isNotNull(resourcePools)) {
            employeeCriteria.add(Restrictions.in(Employee.RESOURCEPOOL, resourcePools));
        }

        if (search.getCodePool() != null) {
            employeeCriteria.add(Restrictions.eq(Employee.RESOURCEPOOL, new Resourcepool(search.getCodePool())));
        }

        if (ValidateUtil.isNotNull(search.getCodeJobCategoryList())) {
            employeeCriteria.createCriteria(Employee.JOBCATEMPLOYEES)
                    .createCriteria(Jobcatemployee.JOBCATEGORY)
                        .add(Restrictions.in(Jobcategory.IDJOBCATEGORY, search.getCodeJobCategoryList()));
        }

        if (ValidateUtil.isNotNull(search.getCodeSkillList())) {
            employeeCriteria.createCriteria(Employee.SKILLSEMPLOYEES)
                    .createCriteria(Skillsemployee.SKILL)
                        .add(Restrictions.in(Skill.IDSKILL, search.getCodeSkillList()));
        }

        // Contact
        //
        Criteria critContact = employeeCriteria.createCriteria(Employee.CONTACT);

        if (ValidateUtil.isNotNull(search.getCodeContactList())) {
            critContact.add(Restrictions.in(Contact.IDCONTACT, search.getCodeContactList()));
        }

        critContact.createCriteria(Contact.COMPANY)
                .add(Restrictions.eq(Company.IDCOMPANY, search.getCompany().getIdCompany()))
                .add(Restrictions.or(
                                Restrictions.isNull(Company.DISABLE),
                                Restrictions.ne(Company.DISABLE, true))
                );


        timeSheetCrit.setFetchMode(Timesheet.EMPLOYEE, FetchMode.JOIN);
        timeSheetCrit.setFetchMode(Timesheet.EMPLOYEE + StringPool.PERIOD + Employee.CONTACT, FetchMode.JOIN);
        timeSheetCrit.setFetchMode(Timesheet.EMPLOYEE + StringPool.PERIOD + Employee.RESOURCEPOOL, FetchMode.JOIN);
        timeSheetCrit.setFetchMode(Timesheet.EMPLOYEE + StringPool.PERIOD + Employee.SELLER, FetchMode.JOIN);

        HashMap<Integer, Employee> groupingTime = new HashMap<Integer, Employee>();
        List<Employee> employees = new ArrayList<Employee>();

        for (Timesheet timesheet : (List<Timesheet>)timeSheetCrit.list()) {

            if (groupingTime.containsKey(timesheet.getEmployee().getIdEmployee())) {

                Employee employee = groupingTime.get(timesheet.getEmployee().getIdEmployee());
                employee.getTimesheets().add(timesheet);
            }
            else {

                Employee employee = timesheet.getEmployee();
                employee.setTimesheets(new HashSet<Timesheet>());
                employee.getTimesheets().add(timesheet);

                groupingTime.put(timesheet.getEmployee().getIdEmployee(), employee);
                employees.add(employee);
            }
        }

        return employees;
    }

    /**
     * Find by search
     *
     * @param search
     * @return
     */
    public List<Employee> find(EmployeeSearch search) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        crit.createCriteria(Employee.CONTACT)
                .addOrder(Order.asc(Contact.FULLNAME));

        // PO
        Criteria critPO = crit.createCriteria(Employee.PERFORMINGORG);

        if (search.getCodePerformingOrganization() != null) {
            critPO.add(Restrictions.eq(Performingorg.IDPERFORG, search.getCodePerformingOrganization()));
        }

        // Resource profile
        Criteria critProfile = crit.createCriteria(Employee.RESOURCEPROFILES);

        if (search.getCodeProfile() != null) {
            critProfile.add(Restrictions.eq(Resourceprofiles.IDPROFILE, search.getCodeProfile()));
        }

        return crit.list();
    }
}