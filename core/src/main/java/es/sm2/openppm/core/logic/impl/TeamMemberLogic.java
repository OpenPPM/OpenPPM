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
 * File: TeamMemberLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.ProjectcalendarDAO;
import es.sm2.openppm.core.dao.ResourcepoolDAO;
import es.sm2.openppm.core.dao.TeamMemberDAO;
import es.sm2.openppm.core.dao.TimesheetDAO;
import es.sm2.openppm.core.javabean.DatoColumna;
import es.sm2.openppm.core.javabean.FiltroTabla;
import es.sm2.openppm.core.javabean.Fte;
import es.sm2.openppm.core.javabean.PropertyRelation;
import es.sm2.openppm.core.javabean.TeamMembersFTEs;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectcalendar;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.utils.EntityComparator;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TeamMemberLogic extends AbstractGenericLogic<Teammember, Integer> {
	
	public final static Logger LOGGER = Logger.getLogger(TeamMemberLogic.class);

    private Map<String, String> settings;

	public TeamMemberLogic() {
		super();
	}
	
	public TeamMemberLogic(ResourceBundle bundle) {
		super(bundle);
	}

    public TeamMemberLogic(Map<String, String> settings, ResourceBundle bundle) {
        super(bundle);
        this.settings = settings;
    }

    /**
     * Return Team members
     *
     * @param project
     * @param since
     * @param until
     * @param showDisabled
     * @return
     * @throws Exception
     */
	public List<Teammember> consStaffinFtes(Project project, Date since, Date until, boolean showDisabled) throws Exception {

		List<Teammember> teammembers 	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			TeamMemberDAO memberDAO = new TeamMemberDAO(session);
			teammembers = memberDAO.consStaffinFtes(project, since, until, showDisabled);
		
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
		return teammembers;
	}
	
	/**
	 * Search Team Members by Project with actuals FTEs
	 * @param project
	 * @param since
	 * @param until
	 * @return
	 * @throws Exception
	 */
	public List<Teammember> consStaffinActualsFtes(Project project, Date since, Date until) throws Exception {

		List<Teammember> teammembers 	= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx	= null;
		try {
			tx = session.beginTransaction();
			
			TeamMemberDAO memberDAO = new TeamMemberDAO(session);
			teammembers = memberDAO.consStaffinActualsFtes(project, since, until);
		
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return teammembers;
	}

	/**
	 * Consult staff of a project
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public List<Teammember> consTeamMembers(Project project) throws Exception {
		List<Teammember> team = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			TeamMemberDAO teamDAO = new TeamMemberDAO(session);
			
			if (project.getIdProject() != -1) {
				team = teamDAO.consTeamMembers(project);
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
		return team;
	}


    /**
     * Approve team member
     *
     * @param settings
     * @param idTeamMember
     * @param commentsRm
     * @throws Exception
     */
	public void approveTeamMember(HashMap<String, String> settings, Integer idTeamMember, String commentsRm) throws Exception {
		
		Transaction tx = null;

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			approveTeamMember(session, settings, idTeamMember, commentsRm);

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
	 * Approve Teammember
	 *  @param session
	 * @param settings
     * @param idTeamMember
     * @param commentsRm
     */
	private void approveTeamMember(Session session, HashMap<String, String> settings, Integer idTeamMember, String commentsRm) {
		
		TeamMemberDAO teamDAO			= new TeamMemberDAO(session);
		TimesheetDAO timesheetDAO		= new TimesheetDAO(session);
		ProjectcalendarDAO projCalDAO	= new ProjectcalendarDAO(session);
		
		Teammember teammember = teamDAO.findById(idTeamMember, false);
		
		if (teammember != null) {
			
			teammember.setStatus(Constants.RESOURCE_ASSIGNED);
			teammember.setDateApproved(new Date());
			if(commentsRm != null) {
				teammember.setCommentsRm(commentsRm);	
			}				
			teamDAO.makePersistent(teammember);
			
			
			Calendar initDate = DateUtil.getCalendar();
			initDate.setTime(DateUtil.getFirstWeekDay(teammember.getDateIn()));
			
			Calendar endDate = DateUtil.getCalendar();
			endDate.setTime(DateUtil.getLastWeekDay(teammember.getDateOut()));
			
			// Hours for day
			double maxHours = Constants.DEFAULT_HOUR_DAY;
			
			Projectcalendar projCal	= teammember.getProjectactivity().getProject().getProjectcalendar();
			Calendarbase emplCal	= teammember.getEmployee().getCalendarbase();
			
			if (emplCal != null && emplCal.getHoursDay() != null) {
				
				maxHours = emplCal.getHoursDay();
			}
			else if (projCal != null && projCal.getHoursDay() != null) {
				
				maxHours = projCal.getHoursDay();
			}

            Double hourDay = 0.0;

            if (teammember.getFte() != null && teammember.getFte() != 0) {
                hourDay = maxHours /100 * new Double(teammember.getFte());
            }

			if (hourDay == 0) { hourDay = null; }
			
			while (endDate.after(initDate)) {
				
				Timesheet timesheet = timesheetDAO.findByCriteria(
						teammember.getEmployee(),
						teammember.getProjectactivity(),
						DateUtil.getFirstWeekDay(initDate.getTime()),
						DateUtil.getLastWeekDay(initDate.getTime()));
				
				if (timesheet == null) {
					timesheet = new Timesheet();
					timesheet.setProjectactivity(teammember.getProjectactivity());
					timesheet.setEmployee(teammember.getEmployee());
					timesheet.setInitDate(DateUtil.getFirstWeekDay(initDate.getTime()));
					timesheet.setEndDate(DateUtil.getLastWeekDay(initDate.getTime()));
					timesheet.setStatus(Constants.TIMESTATUS_APP0);
				}

				if (Constants.TIMESTATUS_APP0.equals(timesheet.getStatus()) && SettingUtil.getBoolean(settings, Settings.SettingType.PRELOAD_HOURS_WITH_WORKLOAD)) {

					if (timesheet.getHoursDay1() == null && setHours(timesheet, teammember, initDate, projCalDAO, projCal, emplCal, 0)) { timesheet.setHoursDay1(hourDay); }
					if (timesheet.getHoursDay2() == null && setHours(timesheet, teammember, initDate, projCalDAO, projCal, emplCal, 1)) { timesheet.setHoursDay2(hourDay); }
					if (timesheet.getHoursDay3() == null && setHours(timesheet, teammember, initDate, projCalDAO, projCal, emplCal, 2)) { timesheet.setHoursDay3(hourDay); }
					if (timesheet.getHoursDay4() == null && setHours(timesheet, teammember, initDate, projCalDAO, projCal, emplCal, 3)) { timesheet.setHoursDay4(hourDay); }
					if (timesheet.getHoursDay5() == null && setHours(timesheet, teammember, initDate, projCalDAO, projCal, emplCal, 4)) { timesheet.setHoursDay5(hourDay); }
					if (timesheet.getHoursDay6() == null && setHours(timesheet, teammember, initDate, projCalDAO, projCal, emplCal, 5)) { timesheet.setHoursDay6(hourDay); }
					if (timesheet.getHoursDay7() == null && setHours(timesheet, teammember, initDate, projCalDAO, projCal, emplCal, 6)) { timesheet.setHoursDay7(hourDay); }
				}
				
				timesheetDAO.makePersistent(timesheet);
				
				initDate.add(Calendar.DAY_OF_MONTH, 7);
			}
		}
	}
	
	/**
	 * 
	 * @param timesheet
	 * @param teammember
	 * @param initDate
	 * @param projCalDAO
	 * @param projCal
	 * @param calBase
	 * @param day
	 * @return
	 */
	private boolean setHours(Timesheet timesheet, Teammember teammember,
			Calendar initDate, ProjectcalendarDAO projCalDAO,
			Projectcalendar projCal, Calendarbase calBase, int day) {
		
		Calendar dayCal = DateUtil.getCalendar();
		dayCal.setTime(initDate.getTime());
		dayCal.add(Calendar.DAY_OF_WEEK, day);
		
		boolean validDate = (!DateUtil.isWeekend(dayCal)
				&& DateUtil.between(teammember.getDateIn(), teammember.getDateOut(), dayCal.getTime())
				&& !projCalDAO.isException(dayCal.getTime(), projCal, calBase));
		
		return validDate;
	}

    /**
     * Save Project Team member
     *
     * @param member
     * @return
     * @throws Exception
     */
	public Teammember saveResource(Teammember member) throws Exception {

		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			member = saveResource(session, member);
			
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
		return member;
	}

	/**
	 * Save and validate teammeber
	 * @param session
	 * @param member
	 * @return
	 * @throws LogicException
	 */
	private Teammember saveResource(Session session, Teammember member) throws LogicException {
		
		TeamMemberDAO memberDAO = new TeamMemberDAO(session);
		
		if (member.getIdTeamMember() == null && memberDAO.isAssigned(member)) {
			throw new LogicException("msg.error.resource_already_assigned");
		}
		
		member = memberDAO.makePersistent(member);
		
		return member;
	}
	
	/**
	 * Return Team Member by id
	 * @param idTeamMember
	 * @return
	 * @throws Exception 
	 */
	public Teammember consTeamMember(Integer idTeamMember) throws Exception {
		Teammember teamMember = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			TeamMemberDAO teamDAO = new TeamMemberDAO(session);
			if (idTeamMember != -1) {
				teamMember = teamDAO.findById(idTeamMember, false);
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
		return teamMember;
	}
	
	
	/**
	 * Return TeamMember with relations
	 * @param member
	 * @return
	 * @throws Exception 
	 */
	public Teammember consTeamMember(Teammember member) throws Exception {
		Teammember teamMember = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			TeamMemberDAO teamDAO = new TeamMemberDAO(session);
			
			teamMember = teamDAO.searchMember(member);

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
		return teamMember;
	}
	
	/**
	 * 
	 * @param filter
	 * @param joins
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Teammember> filter(FiltroTabla filter, ArrayList<String> joins, Employee user) throws Exception {
		
		List<Teammember> members	= null;
		Transaction tx				= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			TeamMemberDAO memberDAO 		= new TeamMemberDAO(session);
			ResourcepoolDAO resourcepoolDAO = new ResourcepoolDAO(session);
			
			List<Resourcepool> listResourcepool = resourcepoolDAO.findByResourceManager(user);
			
			members = memberDAO.filter(filter, joins, listResourcepool);
			
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
		return members;
	}

	/**
	 * 
	 * @param filtrosExtras
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int findTotal(List<DatoColumna> filtrosExtras, Employee user) throws Exception {
		int total = 0;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			TeamMemberDAO memberDAO 		= new TeamMemberDAO(session);
			ResourcepoolDAO resourcepoolDAO = new ResourcepoolDAO(session);
			
			List<Resourcepool> listResourcepool = resourcepoolDAO.findByResourceManager(user);
			
			total = memberDAO.findTotal(filtrosExtras, listResourcepool);

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
		return total;
	}

	/**
	 * 
	 * @param filtro
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int findTotalFiltered(FiltroTabla filtro, Employee user) throws Exception {
		int total = 0;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			TeamMemberDAO memberDAO = new TeamMemberDAO(session);
			ResourcepoolDAO resourcepoolDAO = new ResourcepoolDAO(session);
			
			List<Resourcepool> listResourcepool = resourcepoolDAO.findByResourceManager(user);
			
			total = memberDAO.findTotalFiltered(filtro, listResourcepool);

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
		return total;
	}

	/**
	 * Check if employee is assigned in these day on the project activity
	 * @param initDate
	 * @param dayOfWeek
	 * @param idActivity
	 * @param employee
	 * @return
	 * @throws Exception 
	 */
	public boolean isWorkDay(Date initDate, Integer dayOfWeek,
			Integer idActivity, Employee employee) throws Exception {

		boolean isWorkDay = false;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			TeamMemberDAO memberDAO = new TeamMemberDAO(session);
			
			Calendar exceptionDate = DateUtil.getCalendar();
			exceptionDate.setTime(initDate);
			exceptionDate.add(Calendar.DAY_OF_WEEK, dayOfWeek);
			
			isWorkDay = memberDAO.isWorkDay(exceptionDate.getTime(), new Projectactivity(idActivity), employee);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return isWorkDay;
	}
	
	/**
	 * 
	 * @param employee
	 * @param joins
	 * @return
	 * @throws Exception
	 */
	public List<Teammember> findByEmployeeOrderByProject(Employee employee, Date since, Date until, List<String> joins) throws Exception {
		
		List<Teammember> members	= null;
		Transaction tx				= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			TeamMemberDAO memberDAO = new TeamMemberDAO(session);
			members = memberDAO.findByEmployeeOrderByProject(employee, since, until, joins);
			
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
		return members;
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
	 * @throws Exception 
	 */
	public List<Teammember> consStaffinFtes(Integer[] idResourcePools, Integer[] idProjects,
			Integer[] idPMs, Integer[] idJobCategories, String[] statusList,
			String fullName, Date since, Date until, String nameOrder, String order, Integer idEmployee, Employee user, 
			Integer[] idSellers, Integer[] idCategories, HashMap<String, String> settings) throws Exception {
		
		List<Teammember> members = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			TeamMemberDAO memberDAO = new TeamMemberDAO(session);

			members = memberDAO.consStaffinFtes(idResourcePools, idProjects, idPMs, idJobCategories, statusList, fullName, since, until, 
					nameOrder, order, idEmployee, user, idSellers, idCategories, settings);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return members;
	}

    /**
     * Search team members by filters
     *
     * @param list
     * @param propertyOrder
     * @param typeOrder
     * @param joins
     * @return
     * @throws Exception
     */
	public List<Teammember> findByFilters(ArrayList<PropertyRelation> list, String propertyOrder, String typeOrder, List<String> joins) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		List<Teammember> teammembers = null;
		
		try {
			
			tx = session.beginTransaction();
		
			TeamMemberDAO teamMemberDAO = new TeamMemberDAO(session);
			teammembers = teamMemberDAO.findByFilters(list, propertyOrder, typeOrder, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return teammembers;
	}

	/**
	 * Check that there are no members assigned
	 * @param idProject
	 * @return
	 * @throws Exception 
	 */
	public boolean hasMembersAssigned(Integer idProject) throws Exception {

        if (idProject == null || idProject <= 0) {
            throw new LogicException("msg.error.project");
        }

		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		boolean assigned = false;
		
		try {
			
			tx = session.beginTransaction();
		
			TeamMemberDAO teamMemberDAO = new TeamMemberDAO(session);
			assigned = teamMemberDAO.hasMembersAssigned(new Project(idProject));
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return assigned;
	}

	/**
	 * Update Team Member
	 * 
	 *
     * @param settings
     * @param idTeamMember
     * @param dateIn
     * @param dateOut
     * @param fte
     * @return
	 * @throws Exception
	 */
	public Teammember updateTeamMember(HashMap<String, String> settings, Integer idTeamMember,
                                       Date dateIn, Date dateOut, Integer fte) throws Exception {

		Transaction tx		= null;
		Teammember member	= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			// Update team member
			member = updateTeamMember(session, settings, idTeamMember, dateIn, dateOut, fte);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return member;
	}

    /**
     * Update Team Member
     *
     * @param session
     * @param settings
     * @param idTeamMember
     * @param dateIn
     * @param dateOut
     * @param fte
     * @return
     * @throws LogicException
     */
	public Teammember updateTeamMember(Session session, HashMap<String, String> settings, Integer idTeamMember,
                                       Date dateIn, Date dateOut, Integer fte) throws LogicException {

		// Declare DAO
		TeamMemberDAO memberDAO		= new TeamMemberDAO(session);
		TimesheetDAO timesheetDAO	= new TimesheetDAO(session);
		
		// Find member
		Teammember member = memberDAO.findById(idTeamMember);

        // Update FTE
        if (!SettingUtil.getBoolean(settings, Settings.SettingType.PRELOAD_HOURS_WITH_WORKLOAD) && fte != null) {

            member.setFte(fte);
        }

		// Change Date in
		//
		if (dateIn != null && dateIn.after(member.getDateIn())) {
			
			LOGGER.debug("Change init date: "+DateUtil.format(Constants.DATE_PATTERN, dateIn));
			
			// find time sheet affected
			List<Timesheet> timesheets = timesheetDAO.timeSheetsForUpdateIn(member, dateIn);
			
			LOGGER.debug("\tNumber of time sheets for check: "+timesheets.size());
			
			for (Timesheet timesheet : timesheets) {
				
				LOGGER.debug("\t\tProcess time sheet for validate. In: "
						+ DateUtil.format(Constants.DATE_PATTERN, timesheet.getInitDate())+ " Out: "
						+ DateUtil.format(Constants.DATE_PATTERN, timesheet.getEndDate()));
				
				// Throwing error if there hours charged
				if (checkHoursIn(member, timesheet, dateIn,
						Constants.TIMESTATUS_APP1, Constants.TIMESTATUS_APP2, Constants.TIMESTATUS_APP3)) {

					throw new LogicException(
							new ParamResourceBundle("msg.error.hours_in_state",
									member.getEmployee().getContact().getFullName(),
									Constants.TIMESTATUS_APP1+", "+Constants.TIMESTATUS_APP2+", "+ Constants.TIMESTATUS_APP3,
									member.getProjectactivity().getActivityName(),
									DateUtil.format(getBundle(), member.getDateIn()),
									DateUtil.format(getBundle(), dateIn)).toString(getBundle()));
                }
            }
			
			LOGGER.debug("\tCheck for delete time sheets");
			
			// Check for Delete timeSheets
			for (Timesheet timesheet : timesheets) {
				
				LOGGER.debug("\t\tProcess time sheet for delete. In: "
						+ DateUtil.format(Constants.DATE_PATTERN, timesheet.getInitDate())+ " Out: "
						+ DateUtil.format(Constants.DATE_PATTERN, timesheet.getEndDate()));
				
				if (dateIn.after(timesheet.getEndDate())
						&& memberDAO.notAssignedToOtherMember(member, timesheet)) {
				
					// Delete Time sheet
					timesheetDAO.makeTransient(timesheet);
					LOGGER.debug("\t\t\tTime sheet DELETED");
				}
				else {
					LOGGER.debug("\t\t\tTime sheet NOT deleted");
				}
			}
			
			// Set new in
			member.setDateIn(dateIn);
			LOGGER.debug("\tDate in changed");
		}
		else if (dateIn != null) {
			
			// Set new in
			member.setDateIn(dateIn);
			LOGGER.debug("\tDate in changed");
		}
		
		// Change Date out
		//
		
		// If we reduce the date out check that there are no approved hours
		if (dateOut.before(member.getDateOut())) {

			LOGGER.debug("Change end date: "+DateUtil.format(Constants.DATE_PATTERN, dateOut));
			
			// find time sheet affected
			List<Timesheet> timesheets = timesheetDAO.timeSheetsForUpdateOut(member, dateOut);

			for (Timesheet timesheet : timesheets) {

				LOGGER.debug("\t\tProcess time sheet for validate. In: "
						+ DateUtil.format(Constants.DATE_PATTERN, timesheet.getInitDate())+ " Out: "
						+ DateUtil.format(Constants.DATE_PATTERN, timesheet.getEndDate()));

				// Throwing error if there hours charged
				if (checkHoursOut(member, timesheet, dateOut,
						Constants.TIMESTATUS_APP1, Constants.TIMESTATUS_APP2, Constants.TIMESTATUS_APP3)) {

					throw new LogicException(
							new ParamResourceBundle("msg.error.hours_in_state",
									member.getEmployee().getContact().getFullName(),
									Constants.TIMESTATUS_APP1+", "+Constants.TIMESTATUS_APP2+", "+ Constants.TIMESTATUS_APP3,
									member.getProjectactivity().getActivityName(),
									DateUtil.format(getBundle(), dateOut),
									DateUtil.format(getBundle(), member.getDateOut())).toString(getBundle()));
				}
			}
			
			LOGGER.debug("\tCheck for delete time sheets");
			
			// Check for Delete timeSheets
			for (Timesheet timesheet : timesheets) {
				
				LOGGER.debug("\t\tProcess time sheet for delete. In: "
						+ DateUtil.format(Constants.DATE_PATTERN, timesheet.getInitDate())+ " Out: "
						+ DateUtil.format(Constants.DATE_PATTERN, timesheet.getEndDate()));
				
				if (dateOut.before(timesheet.getInitDate())
						&& memberDAO.notAssignedToOtherMember(member, timesheet)) {
					
					// Delete Time sheet
					timesheetDAO.makeTransient(timesheet);
					LOGGER.debug("\t\t\tTime sheet DELETED");
				}
				else {
					LOGGER.debug("\t\t\tTime sheet NOT deleted");
				}
			}
		}
		// Control if new date in or out overlaps with a range of dates for the same member and activity
		if (dateIn != null && dateOut != null) {

			// find teammember affected
			List<Teammember> teammembers = memberDAO.findByActivityAndDates(member.getEmployee(), member.getProjectactivity(),
																			dateIn, dateOut, null);
			if (ValidateUtil.isNotNull(teammembers)){

				for (Teammember teammember : teammembers) {

					if(!teammember.equals(member)){

						LOGGER.debug("\t\tProcess teammember for validate. In: "
								+ DateUtil.format(Constants.DATE_PATTERN, teammember.getDateIn())+ " Out: "
								+ DateUtil.format(Constants.DATE_PATTERN, teammember.getDateOut()));

						// Throwing error if new date out overlaps other teammember date
						if (!Constants.RESOURCE_RELEASED.equals(teammember.getStatus()) &&
                                (DateUtil.between(teammember.getDateIn(), teammember.getDateOut(), dateOut) ||
								DateUtil.between(teammember.getDateIn(), teammember.getDateOut(), dateIn))) {

							throw new LogicException(
									new ParamResourceBundle("msg.error.tasks_in_state",
											member.getEmployee().getContact().getFullName(),
											member.getProjectactivity().getActivityName(),
											DateUtil.format(getBundle(), teammember.getDateIn()),
											DateUtil.format(getBundle(), teammember.getDateOut())).toString(getBundle()));
						}
					}
				}
			}
		}
		
		// Set new out date
		member.setDateOut(dateOut);
		
		// Save team member
		member = saveResource(session, member);
		
		// Call for update timeSheets
		approveTeamMember(session, settings ,member.getIdTeamMember(), null);
		
		// Search team member with data
		member = memberDAO.searchMember(member);
		return member;
	}

	/**
	 * Check hours in APP2 or APP1 for close project
	 * 
	 * @param session
	 * @param idTeamMember
	 * @param dateOut
	 * @param status
	 * @return
	 * @throws LogicException
	 */
	public String checkTeamMember(Session session, Integer idTeamMember, Date dateOut, String status) throws LogicException {

		String warning = null;
		
		TeamMemberDAO memberDAO		= new TeamMemberDAO(session);
		
		// Find member
		Teammember member = memberDAO.findById(idTeamMember);
		
		// If we reduce the date out check that there are no approved hours
		if (dateOut.before(member.getDateOut())) {

			TimesheetDAO timesheetDAO	= new TimesheetDAO(session);
			
			// find time sheet affected
			List<Timesheet> timesheets = timesheetDAO.timeSheetsForUpdateOut(member, dateOut);
			
			int i = 0;
			while (i < timesheets.size() && ValidateUtil.isNull(warning)) {
				
				Timesheet timesheet = timesheets.get(i++);
				
				// Add warning if there hours charged
				if (checkHoursOut(member, timesheet, dateOut, status)) {

					warning = new ParamResourceBundle("msg.warning.hours_in_state",
									member.getEmployee().getContact().getFullName(),
									status,
									member.getProjectactivity().getActivityName(),
									DateUtil.format(getBundle(), dateOut),
									DateUtil.format(getBundle(), member.getDateOut())).toString(getBundle());
				}
			}
			
		}
		
		return warning;
	}

	/**
	 * Check if there are hours in a state
	 * 
	 * @param member
	 * @param timesheet
	 * @param dateIn
	 * @param statusFilter
	 * @return
	 */
	private boolean checkHoursIn(Teammember member, Timesheet timesheet, Date dateIn, String...statusFilter) {
		
		boolean found = false;
		
		if (ValidateUtil.isNotNull(statusFilter)) {
			
			int i = 0;
			
			while (!found && i < statusFilter.length) {
				
				// Get current status
				String status = statusFilter[i++];
				
				// Check status
				found =   (checkHoursDay(timesheet, status, member.getDateIn(), dateIn, timesheet.getInitDate(), timesheet.getHoursDay1(), 0)
						|| checkHoursDay(timesheet, status, member.getDateIn(), dateIn, timesheet.getInitDate(), timesheet.getHoursDay2(), 1)
						|| checkHoursDay(timesheet, status, member.getDateIn(), dateIn, timesheet.getInitDate(), timesheet.getHoursDay3(), 2)
						|| checkHoursDay(timesheet, status, member.getDateIn(), dateIn, timesheet.getInitDate(), timesheet.getHoursDay4(), 3)
						|| checkHoursDay(timesheet, status, member.getDateIn(), dateIn, timesheet.getInitDate(), timesheet.getHoursDay5(), 4)
						|| checkHoursDay(timesheet, status, member.getDateIn(), dateIn, timesheet.getInitDate(), timesheet.getHoursDay6(), 5)
						|| checkHoursDay(timesheet, status, member.getDateIn(), dateIn, timesheet.getInitDate(), timesheet.getHoursDay7(), 6));
			}
		}
		return found;
	}
	
	/**
	 * Check if there are hours in a state
	 * 
	 * @param member
	 * @param timesheet
	 * @param dateOut
	 * @param statusFilter
	 * @return
	 */
	private boolean checkHoursOut(Teammember member, Timesheet timesheet, Date dateOut, String...statusFilter) {
		
		boolean found = false;
		
		if (ValidateUtil.isNotNull(statusFilter)) {
			
			int i = 0;
			
			while (!found && i < statusFilter.length) {
				
				// Get current status
				String status = statusFilter[i++];
				
				found = (checkHoursDay(timesheet, status, dateOut, member.getDateOut(), timesheet.getInitDate(), timesheet.getHoursDay1(), 0)
					|| checkHoursDay(timesheet, status, dateOut, member.getDateOut(), timesheet.getInitDate(), timesheet.getHoursDay2(), 1)
					|| checkHoursDay(timesheet, status, dateOut, member.getDateOut(), timesheet.getInitDate(), timesheet.getHoursDay3(), 2)
					|| checkHoursDay(timesheet, status, dateOut, member.getDateOut(), timesheet.getInitDate(), timesheet.getHoursDay4(), 3)
					|| checkHoursDay(timesheet, status, dateOut, member.getDateOut(), timesheet.getInitDate(), timesheet.getHoursDay5(), 4)
					|| checkHoursDay(timesheet, status, dateOut, member.getDateOut(), timesheet.getInitDate(), timesheet.getHoursDay6(), 5)
					|| checkHoursDay(timesheet, status, dateOut, member.getDateOut(), timesheet.getInitDate(), timesheet.getHoursDay7(), 6));
			}
		}
		return found;
	}
	
	/**
	 * Check if there are hours in a state in one day
	 * 
	 * @param timesheet
	 * @param status
	 * @param dateIn
	 * @param dateOut
	 * @param date
	 * @param hoursDay
	 * @param day
	 * @return
	 */
	private boolean checkHoursDay(Timesheet timesheet, String status,Date dateIn, Date dateOut, Date date, Double hoursDay, int day) {
		
		boolean found = false;
		
		if (hoursDay != null && hoursDay > 0 && status.equals(timesheet.getStatus())) {
			
			// Update date of day
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, day);
			
			// Date with hours in state and in the range
			found = DateUtil.between(dateIn, dateOut, calendar.getTime());
		}
			
		return found;
	}

    /**
     * Generate Team Members Ftes by Projects
     *
     * @param projects
     * @param resources
     * @param sinceDate
     * @param untilDate
     * @return
     * @throws Exception
     */
   	public List<TeamMembersFTEs> generateFTEsMembersByProjects(List<Project> projects, List<Employee> resources,
   			Date sinceDate, Date untilDate) throws Exception {
   		
   		List<TeamMembersFTEs> listMembers 	= new ArrayList<TeamMembersFTEs>();
   		Calendar sinceCal					= DateUtil.getCalendar();
   		Calendar untilCal					= DateUtil.getCalendar();
   		TeamMembersFTEs memberFtes			= null;
   		int[] listFTES						= null;
   		double[] listHours					= null;					
   		    
   		if (sinceDate != null || untilDate != null) {
   			
   			TimesheetLogic timesheetLogic = new TimesheetLogic();
   			
   			sinceCal.setTime(sinceDate);
   			untilCal.setTime(untilDate);
   			
   			int weeks = 0;
   			while (!sinceCal.after(untilCal)) {
   				weeks++;
   				sinceCal.add(Calendar.DAY_OF_MONTH, +7);
   			}
   			
   			for (Employee resource : resources) {
   					 
   				if (memberFtes != null) {
   					memberFtes.setFtes(listFTES);
   					memberFtes.setListHours(listHours);
   					listMembers.add(memberFtes);
   				}
   				
   				Teammember member = new Teammember();
   				member.setEmployee(resource);
   				
   				listFTES	= new int[weeks];
   				listHours	= new double[weeks];
   				memberFtes	= new TeamMembersFTEs(member);
   				
   				int i = 0;
   				sinceCal.setTime(sinceDate);
   				while (!sinceCal.after(untilCal)) {
   					
   					for (Project project : projects) {
   						
   						Fte fte = timesheetLogic.getFte(
   								project, resource,
   								DateUtil.getFirstWeekDay(sinceCal.getTime()),
   								DateUtil.getLastWeekDay(sinceCal.getTime()));
   						
   						listFTES[i]		+= (fte.getFte() == null?0:fte.getFte());
   						listHours[i]	+= (fte.getHours() == null?0:fte.getHours());
   					}
   					
   					i++;
   					sinceCal.add(Calendar.DAY_OF_MONTH, +7);
   				}
   			}
   			if (memberFtes != null) {
   				memberFtes.setFtes(listFTES);
   				memberFtes.setListHours(listHours);
   				listMembers.add(memberFtes);
   			}
   		}
   		return listMembers;
   	}
   	
   	/**
   	 * Generate Team Members Ftes by Project
   	 * 
   	 * @param teammembers
   	 * @param sinceDate
   	 * @param untilDate
   	 * @return
   	 * @throws Exception
   	 */
   	public List<TeamMembersFTEs> generateFTEsMembersByProject(List<Teammember> teammembers, Date sinceDate, Date untilDate) throws Exception {
		
		List<TeamMembersFTEs> listMembers 	= new ArrayList<TeamMembersFTEs>();
		Calendar sinceCal			= DateUtil.getCalendar();
		Calendar untilCal			= DateUtil.getCalendar();
		int idProject				= -1;
		TeamMembersFTEs memberFtes	= null;
		int[] listFTES				= null;
		
		if (sinceDate != null || untilDate != null) {
			sinceCal.setTime(sinceDate);
			untilCal.setTime(untilDate);
			
			int weeks = 0;
			while (!sinceCal.after(untilCal)) {
				weeks++;
				sinceCal.add(Calendar.DAY_OF_MONTH, +7);
			}
			
			for (Teammember member : teammembers) {
				
				if (memberFtes == null || !(idProject == member.getProjectactivity().getProject().getIdProject())) {
					 
					if (memberFtes != null) {
						memberFtes.setFtes(listFTES);
						listMembers.add(memberFtes);
					}
					
					listFTES	= new int[weeks];
					
					// Initialized
					for (int j = 0; j < listFTES.length; j++) {
						listFTES[j] = -1;
					}
					
					memberFtes	= new TeamMembersFTEs(member, member.getProjectactivity().getProject());
					idProject	= member.getProjectactivity().getProject().getIdProject();
				}
				
				int i = 0;
				sinceCal.setTime(sinceDate);

				Calendar dayCal		= null;
				Calendar weekCal	= null;
				
				while (!sinceCal.after(untilCal)) {
					
					dayCal	= DateUtil.getCalendar();
					dayCal.setTime(sinceCal.getTime());
					weekCal = DateUtil.getLastWeekDay(dayCal);
					
					int fte			= -1;
					int workDays	= 5;
					
					while (!dayCal.after(weekCal)) {
					
						if (isMemberIn(member, dayCal)) {
							
							if (fte == -1) {
								fte = 0;
							}
							
							fte += (member.getFte() == null ? 0 : member.getFte());
						}
						
						dayCal.add(Calendar.DAY_OF_MONTH, 1);
					}
					
					if (fte != -1) {
						
						if (listFTES[i] == -1) {
							listFTES[i] = fte/workDays;
						}
						else {
							listFTES[i] += fte/workDays;
						}
					}
					
					i++;
					sinceCal.add(Calendar.DAY_OF_MONTH, +7);
				}
			}
			if (memberFtes != null) {
				memberFtes.setFtes(listFTES);
				listMembers.add(memberFtes);
			}
		}
		return listMembers;
	}

	/**
	 * Generate Team Members Ftes by Job Category
	 * 
	 * @param teammembers
	 * @param sinceDate
	 * @param untilDate
	 * @return
	 * @throws Exception
	 */
	public List<TeamMembersFTEs> generateFTEsMembersByJob(List<Teammember> teammembers, Date sinceDate, Date untilDate) throws Exception {
		
		List<TeamMembersFTEs> listMembers 	= new ArrayList<TeamMembersFTEs>();
		Calendar sinceCal					= DateUtil.getCalendar();
		Calendar untilCal					= DateUtil.getCalendar();
		Integer idJobCategory				= -1;
		TeamMembersFTEs memberFtes			= null;
		int[] listFTES						= null;
		
		if (sinceDate != null || untilDate != null) {
			sinceCal.setTime(sinceDate);
			untilCal.setTime(untilDate);
			
			int weeks = 0;
			while (!sinceCal.after(untilCal)) {
				weeks++;
				sinceCal.add(Calendar.DAY_OF_MONTH, +7);
			}

			List<Project> projects = null;
			
			for (Teammember member : teammembers) {

				if (memberFtes == null || 
						(member.getJobcategory() == null && idJobCategory != null) || 
						(member.getJobcategory() != null && !member.getJobcategory().getIdJobCategory().equals(idJobCategory))) {
					 
					if (memberFtes != null) {
						memberFtes.setFtes(listFTES);
						memberFtes.setProjectNames(projects);
						listMembers.add(memberFtes);
					}
						
					listFTES		= new int[weeks];
					
					// Initialized
					for (int j = 0; j < listFTES.length; j++) {
						listFTES[j] = -1;
					}
					
					memberFtes		= new TeamMembersFTEs(member);
					projects		= new ArrayList<Project>();
					idJobCategory	= member.getJobcategory() == null?null:member.getJobcategory().getIdJobCategory();
				}
				
				if (!projects.contains(member.getProjectactivity().getProject())) {
					projects.add(member.getProjectactivity().getProject());
				}
				int i = 0;
				sinceCal.setTime(sinceDate);

				Calendar dayCal		= null;
				Calendar weekCal	= null;
				
				while (!sinceCal.after(untilCal)) {
					
					dayCal	= DateUtil.getCalendar();
					dayCal.setTime(sinceCal.getTime());
					weekCal = DateUtil.getLastWeekDay(dayCal);
					
					int fte			= -1;
					int workDays	= 5;
					
					while (!dayCal.after(weekCal)) {

						if (isMemberIn(member, dayCal)) {
							
							if (fte == -1) {
								fte = 0;
							}
							
							fte += (member.getFte() == null ? 0 : member.getFte());
						}
						
						dayCal.add(Calendar.DAY_OF_MONTH, 1);
					}
					
					if (fte != -1) {
						
						if (listFTES[i] == -1) {
							listFTES[i] = fte/workDays;
						}
						else {
							listFTES[i] += fte/workDays;
						}
					}
					
					i++;
					sinceCal.add(Calendar.DAY_OF_MONTH, +7);
				}
			}
			if (memberFtes != null) {
				memberFtes.setFtes(listFTES);
				memberFtes.setProjectNames(projects);
				listMembers.add(memberFtes);
			}
		}
		
		// Generate titles for filters
		//
		for (TeamMembersFTEs item : listMembers) {
			
			if (item.getMember().getJobcategory() == null) {
				
				item.setTitle(getBundle().getString("job_category.empty"));
			}
			else {
				
				item.setTitle(item.getMember().getJobcategory().getName());
			}
		}
		
		return listMembers;
	}

    /**
     * Is member in dates and status
     *
     * @param member
     * @param dayCal
     * @return
     */
    private boolean isMemberIn(Teammember member, Calendar dayCal) {

        return DateUtil.between(member.getDateIn(), member.getDateOut(), dayCal.getTime()) && !DateUtil.isWeekend(dayCal)
                && (
                (member.getProjectactivity().getProject().getEndDate() != null
                        && (Constants.STATUS_CLOSED.equals(member.getProjectactivity().getProject().getStatus())
                        || Constants.STATUS_ARCHIVED.equals(member.getProjectactivity().getProject().getStatus()))
                        && member.getProjectactivity().getProject().getEndDate().after(dayCal.getTime()))
                        || (!Constants.STATUS_CLOSED.equals(member.getProjectactivity().getProject().getStatus())
                        && !Constants.STATUS_ARCHIVED.equals(member.getProjectactivity().getProject().getStatus()))
                        || member.getProjectactivity().getProject().getEndDate() == null
                );
    }

    /**
	 * Generate Team Members Ftes
	 * 
	 * @param teammembers
	 * @param sinceDate
	 * @param untilDate
	 * @return
	 * @throws Exception
	 */
	public List<TeamMembersFTEs> generateFTEsMembers(List<Teammember> teammembers, Date sinceDate, Date untilDate) throws Exception {
		
		List<TeamMembersFTEs> listMembers 	= new ArrayList<TeamMembersFTEs>();
		Calendar sinceCal			= DateUtil.getCalendar();
		Calendar untilCal			= DateUtil.getCalendar();
		int idEmploye				= -1;
		TeamMembersFTEs memberFtes	= null;
		int[] listFTES				= null;
		
		if (sinceDate != null || untilDate != null) {
			sinceCal.setTime(sinceDate);
			untilCal.setTime(untilDate);
			
			int weeks = 0;
			while (!sinceCal.after(untilCal)) {
				weeks++;
				sinceCal.add(Calendar.DAY_OF_MONTH, +7);
			}
			
			for (Teammember member : teammembers) {
				
				if (memberFtes == null || !(idEmploye == member.getEmployee().getIdEmployee())) {
					 
					if (memberFtes != null) {
						memberFtes.setFtes(listFTES);
						listMembers.add(memberFtes);
					}
					
					listFTES	= new int[weeks];
					
					// Initialized
					for (int j = 0; j < listFTES.length; j++) {
						listFTES[j] = -1;
					}
					
					memberFtes	= new TeamMembersFTEs(member);
					idEmploye	= member.getEmployee().getIdEmployee();
				}

                if (member.getProjectactivity() != null) {

                    int i = 0;
                    sinceCal.setTime(sinceDate);

                    Calendar dayCal		= null;
                    Calendar weekCal	= null;

                    while (!sinceCal.after(untilCal)) {

                        dayCal	= DateUtil.getCalendar();
                        dayCal.setTime(sinceCal.getTime());
                        weekCal = DateUtil.getLastWeekDay(dayCal);

                        int fte			= -1;
                        int workDays	= 5;

                        while (!dayCal.after(weekCal)) {

                            if (isMemberIn(member, dayCal)) {

                                if (fte == -1) {
                                    fte = 0;
                                }

                                fte += (member.getFte() == null ? 0 : member.getFte());
                            }

                            dayCal.add(Calendar.DAY_OF_MONTH, 1);
                        }

                        if (fte != -1) {

                            if (listFTES[i] == -1) {
                                listFTES[i] = fte/workDays;
                            }
                            else {
                                listFTES[i] += fte/workDays;
                            }
                        }

                        i++;
                        sinceCal.add(Calendar.DAY_OF_MONTH, +7);
                    }
                }
			}
			
			if (memberFtes != null) {
				memberFtes.setFtes(listFTES);
				listMembers.add(memberFtes);
			}
		}
		return listMembers;
	}

	/**
	 * Change resource to released
	 * 
	 * @param idResource
	 * @param commentsPm
	 * @throws Exception 
	 */
	public void changeToReleased(int idResource, String commentsPm) throws Exception {
		
		// Create session and transaction
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			// Declare DAO
			TeamMemberDAO memberDAO		= new TeamMemberDAO(session);
			TimesheetDAO timesheetDAO	= new TimesheetDAO(session);
			
			// Find member
			Teammember member = memberDAO.findById(idResource);
			
			// Check APP1 and APP2
			if (timesheetDAO.hoursInState(member,
					Constants.TIMESTATUS_APP1,
					Constants.TIMESTATUS_APP2)) {
				
				throw new LogicException("msg.error.hours_pending");
			}
			
			
			// Change to released and add comments
			member.setStatus(Constants.RESOURCE_RELEASED);
    		member.setCommentsPm(commentsPm);
    		
    		// Save changes
    		memberDAO.makePersistent(member);
    		
    		// Delete hours in APP0 in dates
    		//
    		
    		// Find time sheets in APP0
    		List<Timesheet> timesheets = timesheetDAO.hoursInState(member, Constants.TIMESTATUS_APP0);
    		LOGGER.debug("Number of time sheets for check delete: "+timesheets.size());
    		
    		// Find team members for check
    		List<Teammember> members = memberDAO.membersForCheckHours(member);
    		LOGGER.debug("Number of members for check delete: "+members.size());
    		
    		if (ValidateUtil.isNotNull(timesheets)) {
    			
    			// Check if time sheet is used by other member
    			for (Timesheet sheet :timesheets) {
    				
    				LOGGER.debug("\t Proces time sheet: "+sheet.getIdTimeSheet() 
    						+ " InitDate: "+DateUtil.format(Constants.DATE_PATTERN, sheet.getInitDate())
    						+ " EndDate: "+DateUtil.format(Constants.DATE_PATTERN, sheet.getEndDate()));
    				
    				// Flag to delete time sheet
    				boolean delete = true;
    				
    				// If has members check dates
    				if (ValidateUtil.isNotNull(members)) {
    					
    					int i = 0;
    					while (delete && i < members.size()) {

    						// Get current member for check
    						Teammember currentMember = members.get(i++);
    						
    						LOGGER.debug("\t\t Check Member: "+currentMember.getIdTeamMember() 
    	    						+ " DateIn: "+DateUtil.format(Constants.DATE_PATTERN, currentMember.getDateIn())
    	    						+ " DateOut: "+DateUtil.format(Constants.DATE_PATTERN, currentMember.getDateOut()));
    						
    						// If time sheet is in dates of team member exclude delete sheet
    						if (DateUtil.between(sheet.getInitDate(), sheet.getEndDate(), currentMember.getDateIn(), currentMember.getDateOut())) {
    							
    							LOGGER.debug("\t\t\t EXCLUDE delete time sheet. Dates concide");
    							
    							delete = false;
    						}
    							
    					}
    				}
    				
    				// Delete time sheet
    				if (delete) {
    					
    					LOGGER.debug("\t\t\t Time Sheet DELETED");
    					timesheetDAO.makeTransient(sheet);
    				}
    			}
    		}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally {
			SessionFactoryUtil.getInstance().close();
		}
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
	 * @throws Exception 
	 */
	public List<Teammember> capacityPlanningResource(Integer idEmployee, Date since, Date until, String[] statusList,
                                                     List<String> joins, HashMap<String, String> settings, Employee user) throws Exception {
		
		List<Teammember> members = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			TeamMemberDAO memberDAO = new TeamMemberDAO(session);
			
			members = memberDAO.capacityPlanningResource(idEmployee, since, until, statusList, joins, settings, user);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return members;
	}

	/**
	 * Generate Team Members Ftes detail 
	 * detail is contact name and project name and activity name
	 * 
	 * @param teammembers
	 * @param sinceDate
	 * @param untilDate
	 * @return
	 */
	public List<TeamMembersFTEs> generateFTEsMembersDetail(List<Teammember> teammembers, Date sinceDate, Date untilDate) {
		
		List<TeamMembersFTEs> listMembers 	= new ArrayList<TeamMembersFTEs>();
		Calendar sinceCal			= DateUtil.getCalendar();
		Calendar untilCal			= DateUtil.getCalendar();
		TeamMembersFTEs memberFtes	= null;
		int[] listFTES				= null;
		
		if (sinceDate != null || untilDate != null) {
			
			sinceCal.setTime(sinceDate);
			untilCal.setTime(untilDate);
			
			// Count weeks
			int weeks = 0;
			while (!sinceCal.after(untilCal)) {
				weeks++;
				sinceCal.add(Calendar.DAY_OF_MONTH, +7);
			}
			
			for (Teammember member : teammembers) {
				
				// New teamMembersftes
				listFTES	= new int[weeks];
				memberFtes	= new TeamMembersFTEs(member, member.getProjectactivity().getProject());
			
				int i = 0;
				sinceCal.setTime(sinceDate);

				Calendar dayCal		= null;
				Calendar weekCal	= null;
				
				// Ftes by weeks
				//
				while (!sinceCal.after(untilCal)) {
					
					dayCal	= DateUtil.getCalendar();
					dayCal.setTime(sinceCal.getTime());
					weekCal = DateUtil.getLastWeekDay(dayCal);
					
					int fte			= 0;
					int workDays	= 5;
					
					// Ftes by week 
					//
					while (!dayCal.after(weekCal)) {
					
						if (isMemberIn(member, dayCal)) {
							fte += (member.getFte() == null?0:member.getFte());
						}
						
						dayCal.add(Calendar.DAY_OF_MONTH, 1);
					}
					
					listFTES[i] +=  (workDays > 0?fte/workDays:0);
					
					i++;
					sinceCal.add(Calendar.DAY_OF_MONTH, +7);
				}
				
				// Set teamMembersftes
				memberFtes.setFtes(listFTES);
				
				listMembers.add(memberFtes);
			}
		}
		
		return listMembers;
	}

    /**
     * Get all resources(assigned and unassigned) for RM
     *
     *
     *
     *
     * @param teammembers
     * @param since
     * @param fullName
     * @param order
     * @param resourceManager
     * @param idResourcePools
     * @param idJobCategories
     * @return
     */
    public List<Teammember> addUnassigneds(List<Teammember> teammembers, Date since, Date until, String fullName,
                                           String order, Employee resourceManager, Integer[] idResourcePools, Integer[] idJobCategories) throws Exception {

        if (resourceManager != null) {

            // Logic
            EmployeeLogic employeeLogic = new EmployeeLogic();

            // List resource pools
            //
            List<Resourcepool> listResourcepool;

            if (ValidateUtil.isNotNull(idResourcePools)) {

                listResourcepool = new ArrayList<Resourcepool>();

                for (Integer idResourcePool : idResourcePools) {
                    listResourcepool.add(new Resourcepool(idResourcePool));
                }
            }
            else {

                ResourcepoolLogic resourcepoolLogic = new ResourcepoolLogic();

                listResourcepool = resourcepoolLogic.findByResourceManager(resourceManager);
            }

            // Get employees
            List<Employee> employees = employeeLogic.findUnassigned(since, until, fullName, listResourcepool, idJobCategories);

            // Parse employees to teammembers
            //
            for (Employee employee : employees) {

                Teammember teammember = new Teammember();

                teammember.setEmployee(employee);

                teammembers.add(teammember);
            };

            // Sort teammembers
            Collections.sort(teammembers, new EntityComparator<Teammember>(new Order(Teammember.EMPLOYEE +"."+Employee.CONTACT +"."+ Contact.FULLNAME, order)));
        }

        return teammembers;
    }

    /**
     * Find team members filter by activity and status list
     *
     * @param oldInstance
     * @param statusList
     * @param joins
     * @return
     */
    public List<Teammember> findByActivityAndStatus(Projectactivity oldInstance, List<String> statusList, List<String> joins) throws Exception {

        List<Teammember> members = null;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            TeamMemberDAO memberDAO = new TeamMemberDAO(session);

            members = memberDAO.findByActivityAndStatus(oldInstance, statusList, joins);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return members;
    }


    /**
     * Find teammembers filter by datein equals baseline start and dateout equals baseline finish and status
     *
     * @param projectactivity
     * @param status
     * @param joins
     * @return
     * @throws Exception
     */
    public List<Teammember> findByEqualsDatesAndStatus(Projectactivity projectactivity, String status, List<String> joins) throws Exception {

        List<Teammember> members = null;

        if (projectactivity != null && projectactivity.getPlanInitDate() != null && projectactivity.getPlanEndDate() != null) {

            Transaction tx	= null;
            Session session = SessionFactoryUtil.getInstance().getCurrentSession();
            try {
                tx = session.beginTransaction();

                TeamMemberDAO memberDAO = new TeamMemberDAO(session);

                members = memberDAO.findByEqualsDatesAndStatus(projectactivity, status, joins);

                tx.commit();
            }
            catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
            finally { SessionFactoryUtil.getInstance().close(); }
        }

        return members;
    }

    /**
     *
     *
     * @param teammember
     * @param planInitDate
     * @param planEndDate
     * @return
     * @throws Exception
     */
    public void updateDates(Teammember teammember, Date planInitDate, Date planEndDate) throws Exception {

        Teammember updateTeammember = null;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            TeamMemberDAO memberDAO = new TeamMemberDAO(session);

            memberDAO.updateDates(teammember, planInitDate, planEndDate);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }
}
