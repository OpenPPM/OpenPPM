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
 * File: EmployeeLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.sm2.openppm.core.model.search.EmployeeSearch;
import es.sm2.openppm.core.model.search.TimeSheetOperationSearch;
import es.sm2.openppm.core.utils.DataUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.CompanyDAO;
import es.sm2.openppm.core.dao.EmployeeDAO;
import es.sm2.openppm.core.dao.JobcatemployeeDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.dao.ResourcepoolDAO;
import es.sm2.openppm.core.dao.SkillsemployeeDAO;
import es.sm2.openppm.core.exceptions.EmployeeNotFoundException;
import es.sm2.openppm.core.utils.StringUtils;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Jobcatemployee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Skill;
import es.sm2.openppm.core.model.impl.Skillsemployee;
import es.sm2.openppm.core.model.wrap.ApprovalWrap;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class EmployeeLogic extends AbstractGenericLogic<Employee, Integer> {

	/**
	 * Save employee
	 * @param employee
	 * @param jobcategories 
	 * @throws Exception 
	 */
	public Employee saveEmployee (Employee employee, List<Skill> skills, List<Jobcategory> jobcategories) throws Exception {
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		Employee emple = null;
		
		try {
			tx = session.beginTransaction();
			
			EmployeeDAO employeeDAO 			= new EmployeeDAO(session);
			SkillsemployeeDAO skillsDAO			= new SkillsemployeeDAO(session);
			JobcatemployeeDAO jobcatemployeeDAO = new JobcatemployeeDAO(session);
			
			emple = employeeDAO.makePersistent(employee);
			
			// Skills
			List<Skillsemployee> skillsDB = null;
			Skillsemployee skill = new Skillsemployee();
			skill.setEmployee(emple);
			skillsDB = skillsDAO.findByEmployee(emple);
			
			if(skillsDB != null) {
				for (Skillsemployee s : skillsDB) {
					skillsDAO.makeTransient(s);
				}
			}
			
			if(skills != null) {
				Skillsemployee skillData;			
				
				for(Skill s : skills) {
					skillData = new Skillsemployee();					
					skillData.setEmployee(emple);
					skillData.setSkill(s);
					skillsDAO.makePersistent(skillData);					
				}
			}
			
			// Job Category
			List<Jobcatemployee> jobcatDB = null;
			Jobcatemployee jobcat = new Jobcatemployee();
			jobcat.setEmployee(emple);
			jobcatDB = jobcatemployeeDAO.findByRelation(Jobcatemployee.EMPLOYEE, emple);
			
			if(jobcatDB != null) {
				for (Jobcatemployee j : jobcatDB) {
					jobcatemployeeDAO.makeTransient(j);
				}
			}
			
			if(jobcategories != null) {
				Jobcatemployee jocCatData;			
				
				for(Jobcategory j : jobcategories) {
					jocCatData = new Jobcatemployee();					
					jocCatData.setEmployee(emple);
					jocCatData.setJobcategory(j);
					jobcatemployeeDAO.makePersistent(jocCatData);					
				}
			}
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return emple;
	}		
	
	
	/**
	 * Returns employees list with data
	 * @return
	 * @throws Exception 
	 */
	public List<Employee> findAllEmployeesWithData() throws Exception {
		List<Employee> employees = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {	
			tx = session.beginTransaction();
			
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			Employee exampleInstance = new Employee();
			employees = employeeDAO.searchByExample(exampleInstance, Employee.class, Performingorg.class, Resourceprofiles.class);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employees;
	}
	
	
	/**
	 * Return employee by id
	 * @param idEmployee
	 * @return
	 * @throws Exception 
	 */
	public Employee consEmployee(Integer idEmployee) throws Exception {
		Employee employee = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {	
			tx = session.beginTransaction();
			
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			if (idEmployee != -1) {
				employee = employeeDAO.findByIdEmployee(new Employee(idEmployee));
			}
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employee;
	}
	
	
	/**
	 * Delete employee by id
	 * @param idEmployee
	 * @throws Exception 
	 */
	public void deleteEmployee(Integer idEmployee) throws Exception {
		if (idEmployee == null || idEmployee == -1) {
			throw new EmployeeNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			JobcatemployeeDAO jobcatemployeeDAO = new JobcatemployeeDAO(session);
			SkillsemployeeDAO skillsemployeeDAO = new SkillsemployeeDAO(session);
			EmployeeDAO employeeDAO				= new EmployeeDAO(session);
			Employee employee					= employeeDAO.findById(idEmployee, false);
			
			if (employee != null) {
				
				if (!employee.getPerformingorgs().isEmpty()) {
					throw new LogicException("msg.error.delete.this_has","maintenance.employee","perf_organization");
				}
				else if (!employee.getPrograms().isEmpty()) {
					throw new LogicException("msg.error.delete.this_has","maintenance.employee","program");
				}
				else if (!employee.getTeammembers().isEmpty()) {
					throw new LogicException("msg.error.delete.seller","maintenance.employee","activity");
				}
				else if (!employee.getProjectsForFunctionalManager().isEmpty()) {
					throw new LogicException("msg.error.this_is_a_in_a","maintenance.employee","business_manager","project");
				}
				else if (!employee.getProjectsForInvestmentManager().isEmpty()) {
					throw new LogicException("msg.error.this_is_a_in_a","maintenance.employee","investment_manager","project");
				}
				else if (!employee.getProjectsForProjectManager().isEmpty()) {
					throw new LogicException("msg.error.this_is_a_in_a","maintenance.employee","project_manager","project");
				}
				else if (!employee.getProjectsForSponsor().isEmpty()) {
					throw new LogicException("msg.error.this_is_a_in_a","maintenance.employee","sponsor","project");
				}
				else if (!employee.getManagepools().isEmpty()) {
					throw new LogicException("msg.error.delete.this_has","maintenance.employee.resource_pool","maintenance.employee");
				}
				else if (!employee.getStakeholders().isEmpty()) {
					throw new LogicException("msg.error.delete.this_has","maintenance.employee","stakeholder");
				}
				else {
					for (Skillsemployee skillsemployee : employee.getSkillsemployees()) {
						skillsemployeeDAO.makeTransient(skillsemployee);
					}
					
					for (Jobcatemployee jobcatemployee : employee.getJobcatemployees()) {
						jobcatemployeeDAO.makeTransient(jobcatemployee);
					}
					
					employeeDAO.makeTransient(employee);
				}
			}
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
	}


    /**
     * Search employee by filter
     *
     * @param idProfile
     * @param idJobCategories
     * @param org
     * @param company
     * @param idResourcePool
     * @param idSkills
     * @param idSeller
     * @return
     * @throws Exception
     */
	public List<Employee> searchEmployees(Integer idProfile, String idJobCategories, Performingorg org, Company company,
                                          Integer idResourcePool, String idSkills, Integer idSeller) throws Exception {
		
		List<Employee> employees = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();

			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			
			employees = employeeDAO.searchByFilter(idProfile, idJobCategories, org, company, idResourcePool, idSkills, idSeller);

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employees;
	}
	
	
	/**
	 * Find employee by id with Contact
	 * @param idEmployee
	 * @return
	 * @throws Exception 
	 */
	public Employee consEmployeeWithContact(Employee emp) throws Exception {
		Employee employee = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			if (emp.getIdEmployee() != -1) {
				employee = employeeDAO.findByIdWithContact(emp);
			}

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employee;
	}


	/**
	 * Search Employees by filter
	 * @param name
	 * @param jobTitle
	 * @param idProfile
	 * @return
	 * @throws Exception 
	 */
	public List<Employee> searchEmployees(String name, String jobTitle,
			Integer idProfile, Integer idPerfOrg, Employee user) throws Exception {
		
		List<Employee> employees = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO	= new EmployeeDAO(session);
			CompanyDAO companyDAO	= new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(user);
			employees = employeeDAO.searchByFilter(name, jobTitle, idProfile, idPerfOrg, company);

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employees;
	}


	/**
	 * Calculate FTE of Team Members for these employee
	 * @param employee
	 * @param dateOut 
	 * @param dateIn 
	 * @return
	 * @throws Exception 
	 */
	public double calculateFTE(Employee employee, Date dateIn, Date dateOut) throws Exception {
		
		double fte = 0;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			fte = employeeDAO.calculateFTE(employee, dateIn, dateOut);

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return fte;
	}

	/**
	 * Cons employees for login
	 * 
	 * @param contact
	 * @return
	 * @throws Exception
	 */
	public List<Employee> consEmployeesByUser(Contact contact) throws Exception {
		List<Employee> employees = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			employees = employeeDAO.consEmployeesByUser(contact);

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employees;
	}

	/**
	 * Find employees by PO and Rol
	 * @param performingorg
	 * @param role
	 * @return
	 * @throws Exception 
	 */
	public List<Employee> findByPOAndRol(Performingorg performingorg, int role) throws Exception {
		List<Employee> employees = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			employees = employeeDAO.findByPOAndRol(performingorg, role);

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employees;
	}

	/**
	 * Search employee by role an company
	 * @param role
	 * @param company
	 * @param joins
	 * @return
	 * @throws Exception
	 */
	public List<Employee> searchEmployees(Resourceprofiles profile, Company company, Performingorg performingorg,
			List<String> joins) throws Exception {
		
		List<Employee> employees = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			employees = employeeDAO.searchEmployees(profile, company, performingorg, joins);

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employees;
	}
	

	/**
	 * 
	 * @param idProjectActivity
	 * @return
	 * @throws Exception
	 */
	public List<Employee> searchEmployeesAssignedToProjectActivity (int idProjectActivity) 
	throws Exception {
	
		List<Employee> employees = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			employees = employeeDAO.searchEmployeesAssignedToProjectActivity(idProjectActivity);

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employees;
	}


	/**
	 * Find Approvals Time Sheet
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
	 * @throws Exception
	 */
	public List<Object[]> findApprovalsTimeByPM(Employee pm, Date initDate,
			Date endDate, String minStatus, String maxStatus, String statusResource, boolean includeClosed) throws Exception {
		
		List<Object[]>  list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			list = employeeDAO.findApprovalsTimeByPM(pm, initDate, endDate, minStatus, maxStatus, statusResource, includeClosed);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}
	
	/**
	 * Find Approvals Expense Sheet
	 * object[0] == idEmployee
	 * object[1] == Name employee
	 * object[2] == idProject
	 * object[3] == Project Name 
	 * @param maxStatus 
	 * @param consUser
	 * @throws Exception 
	 */
	public List<Object[]> findApprovalsExpenseByPM(Employee pm, Date initDate, Date endDate, String maxStatus) throws Exception {
		
		List<Object[]>  list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			list = employeeDAO.findApprovalsExpenseByPM(pm, initDate, endDate, maxStatus);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}
	
	/**
	 * Find Approvals Time Sheets by Functional Manager
	 * @param user
	 * @param initDate
	 * @param endDate
	 * @param status
	 * @param statusResource
	 * @param includeClosed
	 * @param employees - not equals
	 * @param onlyOperations 
	 * @return
	 * @throws Exception
	 */
	public List<Employee> findApprovals(Employee user, Date initDate, Date endDate,
			String status, String statusResource, boolean includeClosed,
			List<Employee> employees, boolean onlyOperations) throws Exception {
		
		List<Employee>  list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			list = employeeDAO.findApprovals(user, initDate, endDate, status,
					statusResource, includeClosed, employees, onlyOperations);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * Find Approvals Expenses by Functional Manager
	 * @param user
	 * @param firstMonthDay
	 * @param lastMonthDay
	 * @return
	 * @throws Exception
	 */
	public List<Employee> findApprovalsExpense(Employee user,
			Date firstMonthDay, Date lastMonthDay) throws Exception {
		
		List<Employee>  list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			list = employeeDAO.findApprovalsExpense(user, firstMonthDay, lastMonthDay);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * Find Employees where inputed hours is approval
	 * @param project
	 * @param since
	 * @param until
	 * @return
	 * @throws Exception 
	 */
	public List<Employee> findInputedInProject(Project project, Date since,
			Date until) throws Exception {
		
		List<Employee>  list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			list = employeeDAO.findInputedInProject(project, since, until);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * List employees by company
	 * @param company
	 * @param joins
	 * @return
	 * @throws Exception
	 */
	public List<Employee> findByCompany(Company company, List<String> joins) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		List<Employee> list = null;
		
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			list = employeeDAO.findByCompany(company, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return list;
	}


	/**
	 * Add token for employee
	 * @param employee
	 * @return
	 * @throws Exception 
	 */
	public String addToken(Employee employee) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		String token = null;
		
		try {
			tx = session.beginTransaction();
			
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			employee = employeeDAO.findById(employee.getIdEmployee());
			
			token = StringUtils.getRandomString(30);
			
			while (employeeDAO.tokenInUse(token)) {
				token = StringUtils.getRandomString(30);
			}

			employee.setToken(token);
			
			employeeDAO.makePersistent(employee);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return token;
	}

	/**
	 * Get employee for generate token
	 * @param contact
	 * @return
	 * @throws Exception
	 */
	public List<Employee> findForToken(Contact contact) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		List<Employee> list = null;
		
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			list = employeeDAO.findForToken(contact);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * Find employee by token
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public Employee findByToken(String token) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		Employee employee = null;
		
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			employee = employeeDAO.findByToken(token);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return employee;
	}

	/**
	 * Find by Resource Manager
	 * @param user
	 * @param fullName
	 * @param joins
	 * @param typeOrder
     * @param listResourcepool
     * @return
	 * @throws Exception 
	 */
	public List<Employee> findByManager(Employee user, String fullName,
                                        List<String> joins, String typeOrder, List<Resourcepool> listResourcepool) throws Exception {
		
		List<Employee> employees = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			EmployeeDAO employeeDAO 		= new EmployeeDAO(session);
			ResourcepoolDAO resourcepoolDAO = new ResourcepoolDAO(session);

            if (ValidateUtil.isNull(listResourcepool)) {
                listResourcepool = resourcepoolDAO.findByResourceManager(user);
            }

			employees = employeeDAO.findByManager(listResourcepool, fullName, joins, typeOrder);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return employees;
	}


	/**
	 * Search employee by contact and PO and rol
	 * @param idContact
	 * @param performingorg
	 * @param idProfile
	 * @return
	 * @throws Exception
	 */
	public Employee findByContactAndPOAndRol(Integer idContact, Performingorg performingorg, int idProfile) throws Exception {
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		Employee employee = null;
		
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			employee = employeeDAO.findByContactAndPOAndRol(idContact, performingorg, idProfile);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return employee;
	}
	
	/**
	 * Find hours for approve
	 * 
	 * @param user
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<Employee> findHoursForApprove(Employee user, Date date) throws Exception {
		
		List<Employee> list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			
			// Dates of week
			Date initWeek	= DateUtil.getFirstWeekDay(date);
        	Date endWeek	= DateUtil.getLastWeekDay(date);
			
        	// Activities time for approve
			list = employeeDAO.findApprovals(user, initWeek, endWeek, Constants.TIMESTATUS_APP2, Constants.RESOURCE_ASSIGNED, false, null, false);
			
			// Operations time for approve
			list.addAll(employeeDAO.findApprovals(user, initWeek, endWeek, Constants.TIMESTATUS_APP2, null, true, list, true));
			
			// All Approved time for show
			list.addAll(employeeDAO.findApprovals(user, initWeek, endWeek, Constants.TIMESTATUS_APP3, null, true, list, false));
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return list;
	}
	
	/**
	 * Find Approve time sheets from a date.
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalWrap> findApprovals(Date date) throws Exception {
		
		List<ApprovalWrap> listApp = null;
	
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx	= null;
		
		try {
			tx = session.beginTransaction();
			
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
        	
			// Activities time for approve with timesheet suggest reject
			listApp = employeeDAO.findSuggestRejectInAprovals(date);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return listApp;
	}

	/**
	 * Cons employees for login and profile
	 * 
	 * @param contactFM
	 * @throws Exception 
	 */
	public List<Employee> consEmployeesByUserAndRole(Contact contact, int profile) throws Exception {
		
		List<Employee> employees = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			employees = employeeDAO.consEmployeesByUserAndRol(contact, profile);

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employees;
	}

	/**
	 * 
	 * @param profile
	 * @param company
	 * @return
	 * @throws Exception
	 */
	public List<Employee> consEmployeesByRole(int profile, Company company) throws Exception {
		
		List<Employee> employees = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			
			employees = employeeDAO.consEmployeesByRol(profile, company);

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employees;
	}

	/**
	 * Consult resource managers by employee
	 * 
	 * @param employee
	 * @return
	 * @throws Exception 
	 */
	public List<Employee> consResourceManagers(Employee employee) throws Exception {
		
		List<Employee> employees = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			
			employees = employeeDAO.consResourceManagers(employee);

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return employees;
	}

	/**
	 * Find Employees where inputed hours is approval
	 * 
	 * @param user
	 * @param idProjects
	 * @param idPMs
	 * @param idJobCategories
	 * @param idSellers 
	 * @param idResourcePools 
	 * @param idCategories 
	 * @param fullName
	 * @param since
	 * @param until
	 * @param nameOrder
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public List<Employee> findInputedInProjects(Employee user, Integer[] idProjects, Integer[] idPMs, Integer[] idJobCategories, 
			Integer[] idResourcePools, Integer[] idSellers, Integer[] idCategories, String fullName,
			Date since, Date until, String nameOrder, String order) throws Exception {
		
		List<Employee>  list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			
			List<Project> projects = new ArrayList<Project>();
			
			// Parse projects
			if (ValidateUtil.isNotNull(idProjects)) {
				
				for (Integer idProject : idProjects) {
					projects.add(new Project(idProject));
				}
			}
			
			list = employeeDAO.findInputedInProjects(idResourcePools, projects, idPMs, idJobCategories, idSellers,
                    idCategories, fullName, since, until, nameOrder, order, user);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}


	/**
	 * Check if resource is PM of project
	 * 
	 * @param employeeResource
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public boolean checkResourceIsPM(Employee employeeResource, Project project) throws Exception {
		
		boolean isPM = false;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			
			tx = session.beginTransaction();
		
			ProjectDAO projectDAO = new ProjectDAO(session);
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			
			// Find project data
			project = projectDAO.findById(project.getIdProject());
			
			// Find resource employee data
			employeeResource = employeeDAO.findById(employeeResource.getIdEmployee());
			
			// Check if Resource is PM
			if (employeeResource != null && project != null && project.getEmployeeByProjectManager() != null) {
				isPM = (project.getEmployeeByProjectManager().getContact().equals(employeeResource.getContact()));
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return isPM;
	}

    /**
     * Find unassigned employees by filters
     *
     *
     *  @param since
     * @param until
     * @param fullName
     * @param listResourcepool
     * @param idJobCategories
     * @throws Exception
     */
    public List<Employee> findUnassigned(Date since, Date until, String fullName,
                                         List<Resourcepool> listResourcepool, Integer[] idJobCategories) throws Exception {

        List<Employee> employees;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            EmployeeDAO employeeDAO 		= new EmployeeDAO(session);

            employees = employeeDAO.findUnassigned(since, until, fullName, listResourcepool, idJobCategories);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return employees;
    }

    /**
     * Find Management operations
     *
     * @param search
     * @param user
     * @return
     */
    public List<Employee> find(TimeSheetOperationSearch search, Employee user) throws Exception {

        List<Employee> employees;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            List<Resourcepool> resourcePools = null;

            // Declare DAO
            EmployeeDAO employeeDAO = new EmployeeDAO(session);

            if (user != null) {

                // Declare logic
                ResourcepoolLogic resourcepoolLogic = new ResourcepoolLogic();

                // Find resourcepools
               resourcePools = resourcepoolLogic.findByRole(session, user);
            }

            // DAO
            employees = employeeDAO.find(search, user, resourcePools);

            tx.commit();
        } catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return employees;
    }

    /**
     * Find by search
     *
     * @param search
     * @return
     */
    public List<Employee> find(EmployeeSearch search) throws Exception {

        // Declare return list
        List<Employee> employees = null;

        // Open connection
        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {
            // Begin transaction
            tx = session.beginTransaction();

            // Declare DAOs
            EmployeeDAO employeeDAO = new EmployeeDAO(session);

            // Find
            employees = employeeDAO.find(search);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            SessionFactoryUtil.getInstance().close(); }

        return employees;
    }
}
