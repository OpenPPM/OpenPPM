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
 * File: TeamMemberDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings.SettingType;
import es.sm2.openppm.core.javabean.DatoColumna;
import es.sm2.openppm.core.javabean.FiltroTabla;
import es.sm2.openppm.core.javabean.PropertyRelation;
import es.sm2.openppm.core.logic.setting.GeneralSetting;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class TeamMemberDAO extends AbstractGenericHibernateDAO<Teammember, Integer> {

	private Criteria critProject;
	private Criteria critActivity;
	private Criteria critContact;
	private Criteria employeeCrit;
	
	public TeamMemberDAO(Session session) {
		super(session);
	}

	/**
	 * Search by member
	 * 
	 * @param member
	 * @return
	 */
	public Teammember searchMember(Teammember member) { 
		
		Query query = getSession().createQuery(
				"select t " +
				"from Teammember as t " + 
				"join fetch t.projectactivity as pa " +
				"join fetch t.employee as e " +
				"join fetch e.contact as c " +
				"join fetch e.resourceprofiles as rp " +
				"join fetch e.performingorg as po " +
				"left join fetch e.jobcatemployees as jobs " +
				"left join fetch jobs.jobcategory as jobcategory " +
				"join fetch e.resourcepool as rp " +
				"where t.idTeamMember = :idTeamMember ");
				
		query.setInteger("idTeamMember", member.getIdTeamMember());
		
		return (Teammember) query.uniqueResult();
				
	}
	
	/**
	 * Search Team Member by criteria
	 * 
	 * @param employee
	 * @param projectactivity
	 * @param datein
	 * @param dateout
	 * @return
	 */
	public Teammember searchByCriteria(Employee employee, Projectactivity projectactivity,Date datein,Date dateout) {
		
		Query query = getSession().createQuery(
				"select teammember " +
				"from Teammember as teammember " + 
				"join fetch teammember.employee as employee " +
				"join fetch employee.resourceprofiles as resourceprofiles " +
				"join teammember.projectactivity as projAc " +
				"where ((teammember.dateIn between :datein and :dateout) " +
					"or (teammember.dateOut between :datein and :dateout) " +
					"or (:datein between teammember.dateIn and teammember.dateOut) " +
					"or (:dateout between teammember.dateIn and teammember.dateOut)) " +
					"and employee.idEmployee = :idEmployee and projAc.idActivity = :idActivity ");
		query.setInteger("idEmployee", employee.getIdEmployee());
		query.setInteger("idActivity", projectactivity.getIdActivity());
		query.setDate("datein", datein);
		query.setDate("dateout", dateout);
		
		Teammember teammember = null;
		
		if (!query.list().isEmpty()) { teammember = (Teammember) query.list().get(0); }
		
		return teammember;
	}
	
	
	/**
	 * Search Team Members by Project...
	 * 
	 * @param project
	 * @param since
	 * @param until
	 * @param showDisabled
     * @return
	 */
	public List<Teammember> consStaffinFtes(Project project, Date since, Date until, boolean showDisabled) {
		
		// Create query and restrictions
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.add(Restrictions.or(
					Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_ASSIGNED),
					Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_RELEASED)))
			.add(Restrictions.disjunction()
				.add(Restrictions.between(Teammember.DATEIN, since, until))
				.add(Restrictions.between(Teammember.DATEOUT, since, until))
				.add(Restrictions.and(
						Restrictions.le(Teammember.DATEIN, since),
						Restrictions.ge(Teammember.DATEOUT, until)
					)
				)
			);
			
		// Restriction project
		crit.createCriteria(Teammember.PROJECTACTIVITY)
			.add(Restrictions.eq(Projectactivity.PROJECT, project));

        if (!showDisabled) {

            // Exclude employees and contacts disabled
            crit.createCriteria(Teammember.EMPLOYEE, "em")
                    .add(Restrictions.ne(Employee.DISABLE, true))
                    .setFetchMode(Teammember.EMPLOYEE+"."+Employee.RESOURCEPROFILES, FetchMode.JOIN);

            crit.createCriteria("em."+Employee.CONTACT, "contact")
                    .add(Restrictions.ne(Contact.DISABLE, true));
        }
        else {
            crit.setFetchMode(Teammember.EMPLOYEE, FetchMode.JOIN);
            crit.setFetchMode(Teammember.EMPLOYEE + "." + Employee.RESOURCEPROFILES, FetchMode.JOIN);
            crit.setFetchMode(Teammember.EMPLOYEE + "." + Employee.CONTACT, FetchMode.JOIN);

            // Create aliases for order
            crit.createAlias(Teammember.EMPLOYEE, "em").createAlias("em."+Employee.CONTACT, "contact");
        }

        // Data needed
        crit.setFetchMode(Teammember.JOBCATEGORY, FetchMode.JOIN);

		// Order by name
		crit.addOrder(Order.asc("contact.fullName"));
		
		return crit.list();
	}
	
	/**
	 * Search Team Members by Project with actuals FTEs
	 * 
	 * @param project
	 * @param since
	 * @param until
	 * @return
	 */
	public List<Teammember> consStaffinActualsFtes(Project project,Date since,Date until) {
		
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.add(Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_ASSIGNED))
			.add(Restrictions.disjunction()
				.add(Restrictions.between(Teammember.DATEIN, since, until))
				.add(Restrictions.between(Teammember.DATEOUT, since, until))
				.add(Restrictions.and(
						Restrictions.le(Teammember.DATEIN, since),
						Restrictions.ge(Teammember.DATEOUT, until)
					)
				)
			);
		
		crit.createCriteria(Teammember.PROJECTACTIVITY)
			.add(Restrictions.eq(Projectactivity.PROJECT, project));
		
		Criteria timesheetsCrit = crit.createCriteria(Teammember.EMPLOYEE)
			.createCriteria(Employee.TIMESHEETS)
			.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
			.add(Restrictions.disjunction()
				.add(Restrictions.between(Timesheet.INITDATE, since, until))
				.add(Restrictions.between(Timesheet.ENDDATE, since, until))
				.add(Restrictions.and(
						Restrictions.le(Timesheet.INITDATE, since),
						Restrictions.ge(Timesheet.ENDDATE, until)
					)
				)
			);
		
		timesheetsCrit.createCriteria(Timesheet.PROJECTACTIVITY)			
			.add(Restrictions.eq(Projectactivity.PROJECT, project));
		
		crit.setFetchMode(Teammember.JOBCATEGORY, FetchMode.JOIN);
		crit.setFetchMode(Teammember.EMPLOYEE, FetchMode.JOIN);
		crit.setFetchMode(Teammember.EMPLOYEE+"."+Employee.CALENDARBASE, FetchMode.JOIN);
		
		return crit.list();
	}
	
	/**
	 * Get Employees to a time sheet pending approve
	 * @param project
	 * @param initdate
	 * @param enddate
	 * @return
	 */
	public List<Employee> membersTracking(Project project,Date initdate, Date enddate) {
		
		List<Employee> employees = new ArrayList<Employee>();
		
		String queryString = "select distinct employee.idEmployee " +
			"from Teammember as teammember " + 
			"join teammember.employee as employee " +
			"join teammember.projectactivity as projAc " +
			"join projAc.project as project " +
			"right join employee.weektimesheets as weekTs " +
			"join weekTs.projecttimesheets as timesheet " +
			"where project.idProject = :idProject " +
				"and (timesheet.appLevel = :minAppLevel " +
					"or timesheet.appLevel = :maxAppLevel ) " +
				"and timesheet.timeSheetDate between :initdate and :enddate ";
		
		Query query = getSession().createQuery(queryString);
		query.setInteger("idProject", project.getIdProject());
		query.setDate("initdate", initdate);
		query.setDate("enddate", enddate);
		
		query.setCharacter("minAppLevel", Constants.APP1);
		query.setCharacter("maxAppLevel", Constants.APP2);
		
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
					"select employee " +
					"from Teammember as teammember " + 
					"join teammember.employee as employee " +
					"join fetch employee.contact as contact " +
					"join fetch employee.resourceprofiles as resourceprofiles " +
					"join fetch employee.employee as rm " +
					"where employee.idEmployee in ("+ids.toString()+")"
					);
			
			employees = query2.list();
		}
		
		return employees;
	}


	/**
	 * List of Team Members by Resource Manager
	 * 
	 * @param idEmployee
	 * @return
	 */
	public List<Teammember> consMemberOfResourceManager(Integer idEmployee) {
		
		Query query = getSession().createQuery(
				"select tm " +
				"from Employee as rm " +
				"join rm.employees as emples " +
				"join emples.teammembers as tm " +
				"join fetch tm.employee as empl " +
				"join fetch empl.contact as cont " +
				"join tm.projectactivity as projAc " +
				"join projAc.project as project " +
				"where rm.idEmployee = :idEmployee " +
				"order by tm.employee.contact.idContact, project.idProject");
		
		query.setInteger("idEmployee", idEmployee);
		
		return query.list();
	}
	
	
	/**
	 * List Team Members by program
	 * 
	 * @param idProgram
	 * @param initdate
	 * @param enddate
	 * @return
	 */
	public List<Teammember> membersByProgram(Integer idProgram,Date initdate, Date enddate) {
		
		String queryString = "select distinct teammember " +
			"from Teammember as teammember " +
			"join fetch teammember.employee as e " +
			"join fetch e.resourceprofiles as resProf " +
			"join teammember.projectactivity as projAc " +
			"join projAc.project as project " +
			"join project.program as program " +
			"where program.idProgram = :idProgram " +
			"and ((teammember.dateIn between :initdate and :enddate) " +
				"or (teammember.dateOut between :initdate and :enddate) " +
				"or (:initdate between teammember.dateIn and teammember.dateOut) " +
				"or (:enddate between teammember.dateIn and teammember.dateOut)) ";
		
		Query query = getSession().createQuery(queryString);
		query.setInteger("idProgram", idProgram);
		query.setDate("initdate", initdate);
		query.setDate("enddate", enddate);
		
		return query.list();
	}
	
	
	/**
	 * List Team Members by Project Activity
	 * 
	 * @param idProjectActivity
	 * @return
	 */
	public List<Teammember> membersByProjectActivity (Integer idProjectActivity) {
		
		String queryString = "select distinct teammember " +
			"from Teammember as teammember " +
			"join teammember.projectactivity as projAc " +
			"where projAc.idActivity = :idProjectActivity";
		
		Query query = getSession().createQuery(queryString);
		query.setInteger("idProjectActivity", idProjectActivity);
		
		return query.list();
	}

	
	/**
	 * Members dedicated at project
	 * 
	 * @param project
	 * @return
	 */
	public List<Teammember> consTeamMembers(Project project) {
		
		List<Teammember> members = new ArrayList<Teammember>();
		
		Query query = getSession().createQuery(
			"select t.idTeamMember " +
			"from Teammember as t " +
			"join t.projectactivity as projAc " +
			"join projAc.project as project " +
			"where project.idProject = :idProject " +
			"group by t");
		
		query.setInteger("idProject", project.getIdProject());
		
		List<Integer> idMembers = query.list();
		StringBuilder ids = new StringBuilder();
		
		for (Integer id : idMembers) {
			if (ids.toString().equals(StringPool.BLANK)) {
				ids.append(id.toString());
			}
			else {
				ids.append(StringPool.COMMA+id.toString());
			}
		}
		
		if (!ids.toString().equals(StringPool.BLANK)) {
			Query query2 = getSession().createQuery(
					"select distinct t " +
					"from Teammember as t " +
					"join fetch t.employee as e " +
					"join fetch e.performingorg as po " +
					"join fetch e.contact as c " +
					"join fetch e.resourceprofiles as rp " +
					"left join fetch e.skillsemployees as skilldb " +
					"left join fetch skilldb.skill as skill " +
					"join fetch t.projectactivity as projAc " +
					"where t.idTeamMember in ("+ids.toString()+")"
					);
			
			members = query2.list();
		}
		
		return members;
	}

	
	/**
	 * Get project by TeamMember
	 * 
	 * @param member
	 * @return
	 */
	public Project consProject(Teammember member) {
		
		Project proj = null;
		
		Query query = getSession().createQuery(
			"select distinct project " +
			"from Teammember as t " +
			"join t.projectactivity as projAc " +
			"join projAc.project as project " +
			"where t.idTeamMember = :idTeamMember ");
		
		query.setInteger("idTeamMember", member.getIdTeamMember());
		
		if (!query.list().isEmpty()) {
			proj = (Project) query.list().get(0);
		}
		return proj;
	}
	
	/**
	 * Check if resource is assigned in range of dates in activity 
	 * 
	 * @param member
	 * @return
	 */
	public boolean isAssigned(Teammember member) {
		
		Date since = member.getDateIn();
		Date until = member.getDateOut();
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Teammember.PROJECTACTIVITY, member.getProjectactivity()))
			.add(Restrictions.eq(Teammember.EMPLOYEE, member.getEmployee()))
			.add(Restrictions.ne(Teammember.STATUS, Constants.RESOURCE_RELEASED))
			.add(Restrictions.disjunction()
				.add(Restrictions.between(Teammember.DATEIN, since, until))
				.add(Restrictions.between(Teammember.DATEOUT, since, until))
				.add(Restrictions.and(
						Restrictions.le(Teammember.DATEIN, since),
						Restrictions.ge(Teammember.DATEOUT, until)
					)
				)
			);
		
		return ((Integer)crit.uniqueResult()) > 0;
	}
	
	/**
	 * Filter table utility
	 * 
	 * @param filter
	 * @param joins
	 * @param listResourcepool
	 * @return
	 * @throws ParseException
	 */
	public List<Teammember> filter(FiltroTabla filter, ArrayList<String> joins, List<Resourcepool> listResourcepool) throws ParseException {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.setFirstResult(filter.getDisplayStart())
			.setMaxResults(filter.getDisplayLength());
		
		// Apply filters
		applyFilters(crit, filter.getFiltro(), filter.getOrden(), listResourcepool);
		
		// Add data needed
		if (joins != null) {
			for (String join : joins) {
				crit.setFetchMode(join, FetchMode.JOIN);
			}
		}
		
		return crit.list();
	}

	/**
	 * Apply filters by table utility
	 * 
	 * @param crit
	 * @param filtrosExtras
	 * @param orders
	 * @param listResourcepool
	 * @throws ParseException
	 */
	private void applyFilters(Criteria crit, List<DatoColumna> filtrosExtras, List<DatoColumna> orders, List<Resourcepool> listResourcepool) throws ParseException {
		
		employeeCrit = crit.createCriteria(Teammember.EMPLOYEE);

        if (ValidateUtil.isNotNull(listResourcepool)) {
            employeeCrit.add(Restrictions.in(Employee.RESOURCEPOOL, listResourcepool));
        }

		critContact = employeeCrit.createCriteria(Employee.CONTACT);
		critActivity = crit.createCriteria(Teammember.PROJECTACTIVITY);
		critProject	 = critActivity.createCriteria(Projectactivity.PROJECT);
		
		for (DatoColumna filtroColumna : filtrosExtras) {
			
			if (filtroColumna.getTipo() == List.class) {
				
				// Since Until
				if (filtroColumna.getSubTipo() != null && filtroColumna.getSubTipo() == Date.class) {
					
					Date sinceDate = (Date) filtroColumna.getObjectList()[0];
					Date untilDate = (Date) filtroColumna.getObjectList()[1];

					String sinceName = (String) filtroColumna.getNombreList()[0];
					String untilName = (String) filtroColumna.getNombreList()[1];
					
					crit.add(Restrictions.disjunction()
							.add(Restrictions.between(sinceName, sinceDate, untilDate))
							.add(Restrictions.between(untilName, sinceDate, untilDate))
							.add(Restrictions.and(
									Restrictions.le(sinceName, sinceDate),
									Restrictions.ge(untilName, untilDate)
								)
							)
						);
				}
				else {
					// Tiene que estar en la lista
					crit.add(Restrictions.in(filtroColumna.getNombre(),filtroColumna.getValorList()));
				}
			}
			else if (filtroColumna.getTipo() == String.class) {
				
				if (filtroColumna.getSubTipo() == List.class) {
					
					if (!ValidateUtil.isNull(filtroColumna.getValor())) {
						String[] value = filtroColumna.getValor().split(",");
						List<Integer> ids = new ArrayList<Integer>();
						
						for (String id : value) { ids.add(Integer.parseInt(id)); }
						
						if (filtroColumna.getNombre().equals(Project.EMPLOYEEBYPROJECTMANAGER)) {
							
							critProject.add(Restrictions.in(filtroColumna.getNombre()+".idEmployee",ids));
						}
						else if (filtroColumna.getCriteria() != null) {
							Criteria crit2 = crit.createCriteria(filtroColumna.getCriteria());
							crit2.add(Restrictions.in(filtroColumna.getNombre(),ids));
						}
						else {
							crit.add(Restrictions.in(filtroColumna.getNombre(),ids));
						}
						
					}
				}
				else {
					// Add filters for fullname project name or charlabel
					if (filtroColumna.getNombre().equals(Contact.FULLNAME)) {

                        critContact.add(Restrictions.like(filtroColumna.getNombre(),"%"+filtroColumna.getValor()+"%"));
                    }
                    else if (filtroColumna.getNombre().equals(Configurations.PROJECT_NAME)) {
                        // Filter employees by project description OR project short name
                        critProject.add(Restrictions.disjunction()
                                        .add(Restrictions.like(Project.PROJECTNAME,StringPool.PERCENT +filtroColumna.getValor()+ StringPool.PERCENT))
                                        .add(Restrictions.like(Project.CHARTLABEL, StringPool.PERCENT +filtroColumna.getValor()+ StringPool.PERCENT))
                        );

                    }
					else {
						// Comparando texto
						crit.add(Restrictions.like(filtroColumna.getNombre(),"%"+filtroColumna.getValor()+"%"));
					}
				}
			}
			else if (filtroColumna.getTipo() == Integer.class) {
				// Filtrando Objectos
				
				Integer value = Integer.parseInt(filtroColumna.getValor());
				crit.add(Restrictions.eq(filtroColumna.getNombre(),value));
			}
			else if (filtroColumna.getTipo() != Date.class) {
				// Filtrando Objectos
				if (filtroColumna.getCriteria() != null) {
					
					crit.createCriteria(filtroColumna.getCriteria())
						.add(Restrictions.eq(filtroColumna.getNombre(),filtroColumna.getObject()));
				}
				else {
					
					crit.add(Restrictions.eq(filtroColumna.getNombre(),filtroColumna.getObject()));
				}
			}
		}
        if (orders != null && !orders.isEmpty()) {
			
			appplyOrders(crit, orders);
		}
	}
	
	/**
	 * Apply orders for table utility
	 * 
	 * @param crit
	 * @param orders
	 */
	private void appplyOrders(Criteria crit, List<DatoColumna> orders) {
	
		for (DatoColumna order : orders) {
			
			Criteria critTemp = null;
			if (order.getNombre().equals(Contact.FULLNAME)) { critTemp = critContact; }
			else if (order.getNombre().equals(Project.EMPLOYEEBYPROJECTMANAGER)
					|| order.getNombre().equals(Project.PROJECTNAME)) { critTemp = critProject; }
			else if (order.getNombre().equals(Projectactivity.ACTIVITYNAME)) { critTemp = critActivity; }
			else if (order.getNombre().equals(Resourcepool.NAME)) {
				
				critTemp = employeeCrit.createCriteria(Employee.RESOURCEPOOL);
			}
			else { critTemp = crit; }
			
			if (!Teammember.SELLRATE.equals(order.getNombre())) {
				critTemp.addOrder((order.getValor().equals("asc")?Order.asc(order.getNombre()):Order.desc(order.getNombre())));
			}
		}
	}
	
	/**
	 * Find totals for table utility
	 * 
	 * @param filtrosExtras
	 * @param listResourcepool
	 * @return
	 * @throws ParseException
	 */
	public int findTotal(List<DatoColumna> filtrosExtras, List<Resourcepool> listResourcepool) throws ParseException {

		Criteria crit = getSession().createCriteria(getPersistentClass()).setProjection(Projections.rowCount());
		
		applyFilters(crit, filtrosExtras, null, listResourcepool);
		
		return ((Integer)crit.list().get(0)).intValue();
	}

	/**
	 * Find totals filtered for table utility
	 * 
	 * @param filtro
	 * @param listResourcepool
	 * @return
	 * @throws ParseException
	 */
	public int findTotalFiltered(FiltroTabla filtro, List<Resourcepool> listResourcepool) throws ParseException {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());

		applyFilters(crit, filtro.getFiltro(), null, listResourcepool);
		
		crit.setProjection(Projections.rowCount());
			
		return ((Integer)crit.list().get(0)).intValue();
	}

	/**
	 * Check if employee is assigned in these day on the project activity
	 * 
	 * @param day
	 * @param projectactivity
	 * @param employee
	 * @return
	 */
	public boolean isWorkDay(Date day, Projectactivity projectactivity,
			Employee employee) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Teammember.PROJECTACTIVITY, projectactivity))
			.add(Restrictions.eq(Teammember.EMPLOYEE, employee))
			.add(Restrictions.le(Teammember.DATEIN, day))
			.add(Restrictions.ge(Teammember.DATEOUT, day));
			
		return ((Integer) crit.uniqueResult() > 0);
	}
	
	/**
	 * Find members assigned by project in dates 
	 * 
	 * @param employee
	 * @param joins
	 * @return
	 * @throws Exception
	 */
	public List<Teammember> findByEmployeeOrderByProject(Employee employee, Date since, Date until, List<String> joins) throws Exception {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		addJoins(crit, joins);
		crit.add(Restrictions.eq(Teammember.EMPLOYEE, employee))
			.add(Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_ASSIGNED));
		
		if (since != null && until != null) {
			
			crit.add(Restrictions.disjunction()
					.add(Restrictions.between(Teammember.DATEIN, since, until))
					.add(Restrictions.between(Teammember.DATEOUT, since, until))
					.add(Restrictions.and(
							Restrictions.le(Teammember.DATEIN, since),
							Restrictions.ge(Teammember.DATEOUT, until)
						)
					)
				);
		}
		else if (since != null) {
			crit.add(Restrictions.and(
					Restrictions.le(Teammember.DATEIN, since),
					Restrictions.ge(Teammember.DATEOUT, since)
				));
		}
		else if (until != null) {
			crit.add(Restrictions.and(
					Restrictions.le(Teammember.DATEIN, until),
					Restrictions.ge(Teammember.DATEOUT, until)
				));
		}
		
		crit.createCriteria(Teammember.PROJECTACTIVITY)
			.createCriteria(Projectactivity.PROJECT)
			.addOrder(Order.desc(Project.IDPROJECT));
		
		return crit.list();
	}

	/**
	 * Check if any member is assigned or preassigned in these project
	 * 
	 * @param proj
	 * @return
	 */
	public boolean isAssigned(Project proj) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.or(
					Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_ASSIGNED),
					Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_PRE_ASSIGNED))
				)
			.createCriteria(Teammember.PROJECTACTIVITY)
				.add(Restrictions.eq(Projectactivity.PROJECT, proj));
		
		Integer count = (Integer) crit.uniqueResult();
		return (count > 0);
	}

	/**
	 * Search Team Members by filter
	 * 
	 * @param idResourcePools 
	 * @param idProjects
	 * @param idPMs
	 * @param idJobCategories
	 * @param statusList
	 * @param fullName
	 * @param since
	 * @param until
	 * @param nameOrder 
	 * @param order
	 * @param idEmployee 
	 * @param user 
	 * @param idSellers 
	 * @param idCategories 
	 * @param settings 
	 * @return
	 */
	public List<Teammember> consStaffinFtes(Integer[] idResourcePools, Integer[] idProjects,
			Integer[] idPMs, Integer[] idJobCategories, String[] statusList,
			String fullName, Date since, Date until, String nameOrder, String order, Integer idEmployee, Employee user, 
			Integer[] idSellers, Integer[] idCategories, HashMap<String, String> settings) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass(), "t");
		
		if (nameOrder == null && ValidateUtil.isNull(idJobCategories)) {
			
			crit.add(Restrictions.isNull(Teammember.JOBCATEGORY));
		}

		Criteria projActCrit = crit.createCriteria(Teammember.PROJECTACTIVITY);
		
		// Filter by sellers
		if (ValidateUtil.isNotNull(idSellers)) {
			
			projActCrit.createCriteria(Projectactivity.ACTIVITYSELLERS)
							.createCriteria(Activityseller.SELLER)	
								.add(Restrictions.in(Seller.IDSELLER, idSellers));
		}
		
		Criteria projCrit = projActCrit.createCriteria(Projectactivity.PROJECT);
		
		// Filter by category
		if (ValidateUtil.isNotNull(idCategories)) {
			
			projCrit.createCriteria(Project.CATEGORY)
						.add(Restrictions.in(Category.IDCATEGORY, idCategories));
		}
		
		// Filter by Project
		if (ValidateUtil.isNotNull(idProjects)) { projCrit.add(Restrictions.in(Project.IDPROJECT, idProjects)); }
		
		// Remove information if project is closed
		if (!SettingUtil.getBoolean(settings, SettingType.CLOSED_PROJECTS_CAPACITY_PLANNING)) {
            projCrit.add(Restrictions.and(
                    Restrictions.ne(Project.STATUS, Constants.STATUS_CLOSED),
                    Restrictions.ne(Project.STATUS, Constants.STATUS_ARCHIVED)));
		}
		
		// Filter by Project Manager
        if (user.getResourceprofiles().getIdProfile() == Constants.ROLE_PM) {

            projCrit.createCriteria(Project.EMPLOYEEBYPROJECTMANAGER)
                        .add(Restrictions.eq(Employee.IDEMPLOYEE, user.getIdEmployee()));
        }
        else if (user.getResourceprofiles().getIdProfile() != Constants.ROLE_PM && ValidateUtil.isNotNull(idPMs)) {

            projCrit.createCriteria(Project.EMPLOYEEBYPROJECTMANAGER)
                        .add(Restrictions.in(Employee.IDEMPLOYEE, idPMs));
        }
		
		// Filter by Status
		if (ValidateUtil.isNotNull(statusList)) { crit.add(Restrictions.in(Teammember.STATUS, statusList)); }
		
		// Filter by Skill
		Criteria jobCrit = null;
		if (Jobcategory.IDJOBCATEGORY.equals(nameOrder)
				|| ValidateUtil.isNotNull(idJobCategories)) { jobCrit = crit.createCriteria(Teammember.JOBCATEGORY); }
		if (ValidateUtil.isNotNull(idJobCategories)) { jobCrit.add(Restrictions.in(Jobcategory.IDJOBCATEGORY, idJobCategories)); }
		
		// Filter by dates (since && until)
		if (since != null && until != null) {
			
			crit.add(Restrictions.disjunction()
					.add(Restrictions.between(Teammember.DATEIN, since, until))
					.add(Restrictions.between(Teammember.DATEOUT, since, until))
					.add(Restrictions.and(
							Restrictions.le(Teammember.DATEIN, since),
							Restrictions.ge(Teammember.DATEOUT, until)
						)
					)
				);
		}
		else if (since != null) {
			crit.add(Restrictions.and(
					Restrictions.le(Teammember.DATEIN, since),
					Restrictions.ge(Teammember.DATEOUT, since)
				));
		}
		else if (until != null) {
			crit.add(Restrictions.and(
					Restrictions.le(Teammember.DATEIN, until),
					Restrictions.ge(Teammember.DATEOUT, until)
				));
		}

		// Filter By Employee
		Criteria employeeCrit = crit.createCriteria(Teammember.EMPLOYEE);
		
		if (idEmployee != null) { employeeCrit.add(Restrictions.idEq(idEmployee)); }
		
		// Filter by resource pool
		if (ValidateUtil.isNotNull(idResourcePools)) {
			
			employeeCrit.createCriteria(Employee.RESOURCEPOOL)
							.add(Restrictions.in(Resourcepool.IDRESOURCEPOOL, idResourcePools));
		}

		// Filter by po
		if (user != null) {
			employeeCrit.add(Restrictions.eq(Employee.PERFORMINGORG, user.getPerformingorg()));
		}
		
		// Filter by Full Name
		Criteria contactCrit = employeeCrit.createCriteria(Employee.CONTACT);
		if (ValidateUtil.isNotNull(fullName)) {
			contactCrit.add(Restrictions.ilike(Contact.FULLNAME, "%"+fullName+"%"));
		}

		// Apply Order
		Criteria orderCrit = null;
		if (Project.IDPROJECT.equals(nameOrder)) { orderCrit = projCrit; }
		else if (Jobcategory.IDJOBCATEGORY.equals(nameOrder)) { orderCrit = jobCrit; }
		else { orderCrit = contactCrit; }
		
		if (nameOrder != null) {
			if (Constants.DESCENDENT.equals(order)) { orderCrit.addOrder(Order.desc(nameOrder)); }
			else { orderCrit.addOrder(Order.asc(nameOrder)); }
		}
		
		contactCrit.addOrder(Order.asc(Contact.IDCONTACT));
		
		List<String> joins = new ArrayList<String>();
		joins.add(Teammember.PROJECTACTIVITY);
		joins.add(Teammember.PROJECTACTIVITY+"."+Projectactivity.PROJECT);
		joins.add(Teammember.PROJECTACTIVITY+"."+Projectactivity.PROJECT+"."+Project.EMPLOYEEBYPROJECTMANAGER);
		joins.add(Teammember.PROJECTACTIVITY+"."+Projectactivity.PROJECT+"."+Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
		
		addJoins(crit, joins);
		
		return crit.list();
	}


    /**
     * Find team members filter by activity and status list
     *
     * @param activity
     * @param statusList
     * @param joins
     * @return
     */
	public List<Teammember> findByActivityAndStatus(Projectactivity activity, List<String> statusList, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		if (joins != null) {
			for (String join : joins) {
				crit.setFetchMode(join, FetchMode.JOIN);
			}
		}
		
		crit.add(Restrictions.eq(Teammember.PROJECTACTIVITY, activity))
            .add(Restrictions.in(Teammember.STATUS, statusList));

		
		return crit.list();
	}
	
	/**
	 * Find team members filter by activity, employee and dates
	 * 
	 * @param employee
	 * @param projectactivity
	 * @param dateIn
	 * @param dateOut
	 * @param joins
	 * @return
	 */
	public List<Teammember> findByActivityAndDates(Employee employee, Projectactivity projectactivity, Date dateIn, Date dateOut, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		if (joins != null) {
			for (String join : joins) {
				crit.setFetchMode(join, FetchMode.JOIN);
			}
		}
	 
		crit.add(Restrictions.eq(Teammember.EMPLOYEE, employee))
			.add(Restrictions.eq(Teammember.PROJECTACTIVITY, projectactivity));
		
		// Filter by dates (since && until)
		if (dateIn != null && dateOut != null) {
			
			crit.add(Restrictions.disjunction()
					.add(Restrictions.between(Teammember.DATEIN, dateIn, dateOut))
					.add(Restrictions.between(Teammember.DATEOUT, dateIn, dateOut))
					.add(Restrictions.and(
							Restrictions.le(Teammember.DATEIN, dateIn),
							Restrictions.ge(Teammember.DATEOUT, dateOut)
						)
					)
				);
		}
		else if (dateIn != null) {
			crit.add(Restrictions.and(
					Restrictions.le(Teammember.DATEIN, dateIn),
					Restrictions.ge(Teammember.DATEOUT, dateIn)
				));
		}
		else if (dateOut != null) {
			crit.add(Restrictions.and(
					Restrictions.le(Teammember.DATEIN, dateOut),
					Restrictions.ge(Teammember.DATEOUT, dateOut)
				));
		}
		
		return crit.list();
	}
	
	/**
	 * Search team members by filters
	 * 
	 * @param list
	 * @param propertyOrder
	 * @param typeOrder
	 * @param joins 
	 * @return
	 */
	public List<Teammember> findByFilters(ArrayList<PropertyRelation> list, String propertyOrder, String typeOrder, List<String> joins) {
		
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
	 * 
	 * @param propertyRelation
	 * @return
	 */
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
	 * Check that there are no members assigned
	 * 
	 * @param project
	 * @return
	 */
	public boolean hasMembersAssigned(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.createCriteria(Teammember.PROJECTACTIVITY)
				.add(Restrictions.eq(Projectactivity.PROJECT, project));
			
		Integer count = (Integer) crit.uniqueResult();
		return (count > 0);
	}
	
	/**
	 * Check that there are members
	 * 
	 * @param wbsnode
	 * @return
	 */
	public boolean hasMembers(Wbsnode wbsnode) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.createCriteria(Teammember.PROJECTACTIVITY)
				.add(Restrictions.eq(Projectactivity.WBSNODE, wbsnode));
			
		Integer count = (Integer) crit.uniqueResult();
		return (count > 0);
	}

	/**
	 * Time sheet not assigned to other team member
	 * 
	 * @param member
	 * @param timesheet
	 * @return
	 */
	public boolean notAssignedToOtherMember(Teammember member, Timesheet timesheet) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.setProjection(Projections.rowCount())
				.add(Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_ASSIGNED))
				.add(Restrictions.eq(Teammember.EMPLOYEE, member.getEmployee()))
				.add(Restrictions.ne(Teammember.IDTEAMMEMBER, member.getIdTeamMember()))
				.add(Restrictions.eq(Teammember.PROJECTACTIVITY, member.getProjectactivity()));
		
		crit.add(Restrictions.disjunction()
				.add(Restrictions.between(Teammember.DATEIN, timesheet.getInitDate(), timesheet.getEndDate()))
				.add(Restrictions.between(Teammember.DATEOUT, timesheet.getInitDate(), timesheet.getEndDate()))
				.add(Restrictions.and(
						Restrictions.le(Teammember.DATEIN, timesheet.getInitDate()),
						Restrictions.ge(Teammember.DATEOUT, timesheet.getEndDate())
					)
				)
			);
		
		
		Integer count = (Integer) crit.uniqueResult();
		return !(count > 0);
	}

	/**
	 * Find teammembers of project in date
	 * 
	 * @param project
	 * @param closeDate
	 * @return
	 */
	public List<Teammember> findByProjectInDate(Project project, Date closeDate) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.add(Restrictions.and(
				Restrictions.le(Teammember.DATEIN, closeDate),
				Restrictions.ge(Teammember.DATEOUT, closeDate)
			));
		
		crit.createCriteria(Teammember.PROJECTACTIVITY)
			.add(Restrictions.eq(Projectactivity.PROJECT, project));
		
		return crit.list();
	}

	/**
	 * Find members assigned of employee and exclude member
	 * 
	 * @param member
	 * @return
	 */
	public List<Teammember> membersForCheckHours(Teammember member) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.ne(Teammember.IDTEAMMEMBER, member.getIdTeamMember()))
				.add(Restrictions.eq(Teammember.PROJECTACTIVITY, member.getProjectactivity()))
				.add(Restrictions.eq(Teammember.EMPLOYEE, member.getEmployee()))
				.add(Restrictions.eq(Teammember.STATUS, Constants.RESOURCE_ASSIGNED));
				
		
		return crit.list();
	}

	/**
	 * Capacity planning resource 
	 * 
	 * @param idEmployee
	 * @param since
	 * @param until
	 * @param statusList
	 * @param joins
	 * @param settings
	 * @param user
     * @return
	 */
	public List<Teammember> capacityPlanningResource(Integer idEmployee, Date since, Date until,
                                                     String[] statusList, List<String> joins,
                                                     HashMap<String, String> settings, Employee user) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass(), "t");

		// Filter by dates (since && until)
		if (since != null && until != null) {
			
			crit.add(Restrictions.disjunction()
					.add(Restrictions.between(Teammember.DATEIN, since, until))
					.add(Restrictions.between(Teammember.DATEOUT, since, until))
					.add(Restrictions.and(
							Restrictions.le(Teammember.DATEIN, since),
							Restrictions.ge(Teammember.DATEOUT, until)
						)
					)
				);
		}
		else if (since != null) {
			crit.add(Restrictions.and(
					Restrictions.le(Teammember.DATEIN, since),
					Restrictions.ge(Teammember.DATEOUT, since)
				));
		}
		else if (until != null) {
			crit.add(Restrictions.and(
					Restrictions.le(Teammember.DATEIN, until),
					Restrictions.ge(Teammember.DATEOUT, until)
				));
		}

		if (ValidateUtil.isNotNull(statusList)) {
			crit.add(Restrictions.in(Teammember.STATUS, statusList));
		}
		
		// Filter By Employee
		Criteria employeeCrit = crit.createCriteria(Teammember.EMPLOYEE);
		
		if (idEmployee != null) { employeeCrit.add(Restrictions.idEq(idEmployee)); }
		
		// Joins
		//
		Criteria projActCrit = crit.createCriteria(Teammember.PROJECTACTIVITY);
		
		Criteria projCrit = projActCrit.createCriteria(Projectactivity.PROJECT);
		
		// Remove information if project is closed
        if (!SettingUtil.getBoolean(settings, SettingType.CLOSED_PROJECTS_CAPACITY_PLANNING)) {
            projCrit.add(Restrictions.and(
                    Restrictions.ne(Project.STATUS, Constants.STATUS_CLOSED),
                    Restrictions.ne(Project.STATUS, Constants.STATUS_ARCHIVED)));
		}
		
		addJoins(crit, joins);

        // PM Projects
        if (!SettingUtil.getBoolean(settings, GeneralSetting.PM_VIEW_OTHER_PROJECTS) &&
                user.getResourceprofiles().getIdProfile() == Constants.ROLE_PM) {

            projCrit.add(Restrictions.eq(Project.EMPLOYEEBYPROJECTMANAGER, user));
        }

		// Orders
		//
		employeeCrit.createCriteria(Employee.CONTACT)
						.addOrder(Order.asc(Contact.FULLNAME));
		
		projCrit.addOrder(Order.asc(Project.PROJECTNAME));
		
		projActCrit.addOrder(Order.asc(Projectactivity.ACTIVITYNAME));
		
		return crit.list();
	}

    /**
     * Find teammembers filter by datein equals baseline start and dateout equals baseline finish and status
     *
     * @param projectactivity
     * @param status
     * @param joins
     * @return
     */
    public List<Teammember> findByEqualsDatesAndStatus(Projectactivity projectactivity, String status, List<String> joins) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
                .add(Restrictions.eq(Teammember.PROJECTACTIVITY, projectactivity))
                .add(Restrictions.eq(Teammember.DATEIN, projectactivity.getPlanInitDate()))
                .add(Restrictions.eq(Teammember.DATEOUT, projectactivity.getPlanEndDate()))
                .add(Restrictions.eq(Teammember.STATUS, status));

        addJoins(crit, joins);

        return crit.list();
    }

    /**
     * Update dates teammember
     *
     * @param teammember
     * @param dateIn
     * @param dateOut
     */
    public void updateDates(Teammember teammember, Date dateIn, Date dateOut) {

        Query query = getSession().createQuery(
                "update Teammember t " +
                        " set t.dateIn = :dateIn , t.dateOut = :dateOut"+
                        " where t.idTeamMember = :idTeamMember"
        );

        query.setInteger("idTeamMember", teammember.getIdTeamMember());
        query.setDate("dateIn", dateIn);
        query.setDate("dateOut", dateOut);

        query.executeUpdate();
    }
}
