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
 * File: TimesheetLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.*;
import es.sm2.openppm.core.javabean.Fte;
import es.sm2.openppm.core.javabean.OperationFte;
import es.sm2.openppm.core.javabean.PropertyRelation;
import es.sm2.openppm.core.listener.ProcessListener;
import es.sm2.openppm.core.listener.impl.timesheet.TimesheetAbstractListener;
import es.sm2.openppm.core.logic.exceptions.NotFoundSellersException;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.model.impl.*;
import es.sm2.openppm.core.model.wrap.ApprovalWrap;
import es.sm2.openppm.core.model.wrap.ImputationWrap;
import es.sm2.openppm.core.model.wrap.ResourceTimeWrap;
import es.sm2.openppm.core.model.wrap.TimesheetWrap;
import es.sm2.openppm.core.utils.EntityComparator;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;


/**
 * Logic object for domain model class Timesheet
 * @see TimesheetLogic
 * @author Hibernate Generator by Javier Hernandez
 */
public class TimesheetLogic extends AbstractGenericLogic<Timesheet, Integer>{

	private static final Logger LOGGER = Logger.getLogger(TimesheetLogic.class);

	/**
	 * Find time sheets of the resource
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @param joins
	 * @param project
	 * @param minStatus
	 * @param maxStatus
	 * @return
	 * @throws Exception
	 */
	public List<Timesheet> findByResource(Employee employee, Date initDate, Date endDate,
			List<String> joins, Project project, String minStatus, String maxStatus,
			Employee filterUser, Boolean includeClosed, HashMap<String, String> settings) throws Exception {
		
		List<Timesheet> list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			list = timesheetDAO.findByCriteria(employee, initDate, endDate, joins, project, minStatus, maxStatus, filterUser, includeClosed, settings);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
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
	 * @param statusResource
	 * @return
	 * @throws Exception
	 */
	public List<Timesheet> findByResource(Employee employee, Date initDate, Date endDate,
			List<String> joins, Project project, String minStatus, String maxStatus,
			Employee filterUser, String statusResource, boolean includeClosed, HashMap<String, String> settings) throws Exception {
		
		List<Timesheet> list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			list = timesheetDAO.findByCriteria(employee, initDate, endDate, joins, project, minStatus, maxStatus, filterUser, statusResource, includeClosed, settings);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}
	
	/**
	 * Find time sheet
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @param activity
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public Timesheet findByResource(Employee employee, Date initDate, Date endDate,
			Projectactivity activity, String status) throws Exception {
		
		Timesheet item = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			item = timesheetDAO.findByResource(employee, initDate, endDate, activity, status);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return item;
	}

	/**
	 * Find unique time sheet operation
	 * @param operation
	 * @param initDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public Timesheet findByOperation(Operation operation, Employee employee,
			Date initDate, Date endDate) throws Exception {
		
		Timesheet item	= null;
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			item = timesheetDAO.findByOperation(operation, employee, initDate, endDate);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return item;
	}

	/**
	 * Find Time Sheets operations
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @param joins
	 * @param minStatus
	 * @param maxStatus
	 * @return
	 * @throws Exception
	 */
	public List<Timesheet> findByResourceOperation(Employee employee, Date initDate,
			Date endDate, List<String> joins, String minStatus, String maxStatus) throws Exception {
		
		List<Timesheet> list	= null;
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			list = timesheetDAO.findByOperation(employee, initDate, endDate, joins, minStatus, maxStatus);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * Get status actual level
	 * @param idEmployee
	 * @param id
	 * @param initDate
	 * @param endDate
	 * @param status
	 * @param approveLevel 
	 * @param filterUser 
	 * @return
	 * @throws Exception
	 */
	public String getStatusResource(Integer idEmployee, Integer id,
			Date initDate, Date endDate, String status, String approveLevel, Employee filterUser) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			if (Constants.TIMESTATUS_APP1.equals(status)) {
				
				boolean isStatus = timesheetDAO.isStatusResource(idEmployee, id, initDate, endDate, status, filterUser);
				
				if (!isStatus) { 
					status = approveLevel; 
				}
			}
			else {
				
				boolean isStatus = false;
				status			 = Constants.TIMESTATUS_APP1;
				
				isStatus = timesheetDAO.isTimeStatus(idEmployee, initDate, endDate, status, filterUser, false);
				
				if (!isStatus) {
					status = Constants.TIMESTATUS_APP2;
					isStatus = timesheetDAO.isTimeStatus(idEmployee, initDate, endDate, Constants.TIMESTATUS_APP2, filterUser, false);
				}
				if (!isStatus) { status = Constants.TIMESTATUS_APP3; }
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return status;
	}

	/**
	 * Get Hours resource
	 * @param idEmployee
	 * @param id
	 * @param initDate
	 * @param endDate
	 * @param filterUser
	 * @param approveRol
	 * @return
	 * @throws Exception
	 */
	public double getHoursResource(Integer idEmployee, Integer id,
			Date initDate, Date endDate, Employee filterUser, int approveRol) throws Exception {
		
		double hours = 0;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			int rol = filterUser.getResourceprofiles().getIdProfile();
			
			String minStatus = (rol == Constants.ROLE_PM? Constants.TIMESTATUS_APP1: Constants.TIMESTATUS_APP2);
			
			hours = timesheetDAO.getHoursResource(idEmployee, id, initDate, endDate, minStatus, filterUser);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return hours;
	}
	
	/**
	 * Get Hours resource by operation
	 * @param idEmployee
	 * @param initDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public double getHoursResourceOperation(Integer idEmployee,
			Date initDate, Date endDate) throws Exception {
		
		double hours = 0;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			hours = timesheetDAO.getHoursResourceOpeartion(idEmployee, initDate, endDate);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return hours;
	}

	/**
	 * Calculate hours for inputed hours in project
	 * @param project 
	 * @param member
	 * @param firstWeekDay
	 * @param lastWeekDay
	 * @return
	 * @throws Exception 
	 */
	public Fte getFte(Project project, Employee member, Date firstWeekDay, Date lastWeekDay) throws Exception {
		
		Fte fte = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			ProjectcalendarDAO projectcalendarDAO = new ProjectcalendarDAO(session);
			
			Projectcalendar projCal = projectcalendarDAO.findByProject(project);
			Calendarbase emplCal	= member.getCalendarbase();
			
			double hours = timesheetDAO.getHoursResource(project, member, firstWeekDay, lastWeekDay); 
			
			// Hours by week
			double hourWeek = Constants.DEFAULT_HOUR_WEEK;
			if (emplCal != null && emplCal.getHoursWeek() != null) { hourWeek = emplCal.getHoursWeek(); }
			else if (projCal != null && projCal.getHoursWeek() != null) { hourWeek = projCal.getHoursWeek(); }
			
			// Check exception days
			Calendar tempCal = DateUtil.getCalendar();
			tempCal.setTime(firstWeekDay);
			
			int exceptionDay = 0;
			while (!tempCal.getTime().after(lastWeekDay)) {
				
				if (projectcalendarDAO.isException(tempCal.getTime(), projCal, emplCal)) { exceptionDay++; }
				tempCal.add(Calendar.DAY_OF_WEEK, 1);
			}
			
			if (exceptionDay > 0) {
				
				// Hours by day
				double hoursDay = Constants.DEFAULT_HOUR_DAY;
				if (emplCal != null && emplCal.getHoursDay() != null) { hoursDay = emplCal.getHoursDay(); }
				else if (projCal != null && projCal.getHoursDay() != null) { hoursDay = projCal.getHoursDay(); }

				hourWeek -= hoursDay*exceptionDay;
			}
			fte = new Fte((hours * 100)/hourWeek, hours);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return fte;
	}
	
	/**
	 * Calculate Fte for inputed hours in project
	 * @param project 
	 * @param member
	 * @param firstWeekDay
	 * @param lastWeekDay
	 * @return
	 * @throws Exception 
	 */
	public double getFte(Project project, Teammember member, Date firstWeekDay, Date lastWeekDay) throws Exception {
		
		double fte = 0;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			ProjectcalendarDAO projectcalendarDAO = new ProjectcalendarDAO(session);
			
			Projectcalendar projCal = projectcalendarDAO.findByProject(project);
			Calendarbase emplCal	= member.getEmployee().getCalendarbase();
			
			double hours = timesheetDAO.getHoursResource(null, member, firstWeekDay, lastWeekDay); 
			
			// Hours by week
			double hourWeek = Constants.DEFAULT_HOUR_WEEK;
			if (emplCal != null && emplCal.getHoursWeek() != null) { hourWeek = emplCal.getHoursWeek(); }
			else if (projCal != null && projCal.getHoursWeek() != null) { hourWeek = projCal.getHoursWeek(); }
			
			// Check exception days
			Calendar tempCal = DateUtil.getCalendar();
			tempCal.setTime(firstWeekDay);
			
			int exceptionDay = 0;
			while (!tempCal.getTime().after(lastWeekDay)) {
				
				if (projectcalendarDAO.isException(tempCal.getTime(), projCal, emplCal)) { exceptionDay++; }
				tempCal.add(Calendar.DAY_OF_WEEK, 1);
			}
			
			if (exceptionDay > 0) {
				
				// Hours by day
				double hoursDay = Constants.DEFAULT_HOUR_DAY;
				if (emplCal != null && emplCal.getHoursDay() != null) { hoursDay = emplCal.getHoursDay(); }
				else if (projCal != null && projCal.getHoursDay() != null) { hoursDay = projCal.getHoursDay(); }
				
				hourWeek -= hoursDay*exceptionDay;
			}
			
			fte = (hours * 100)/hourWeek;
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return fte;
	}
	

	//FIXME esta funcion no se utiliza
	/**
	 * Calc Time Sheet AC
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public Double calcTimeSheetAC(Project project) throws Exception {

		double ac = 0;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			EmployeeDAO employeeDAO		= new EmployeeDAO(session);
			TimesheetDAO timesheetDAO	= new TimesheetDAO(session);
			
			List<Employee> employees = employeeDAO.findInputedInProject(project, null, null);
			
			for (Employee employee : employees) {
				
				Double cost = (employee.getCostRate() == null?0D:employee.getCostRate());
				Double hours = timesheetDAO.getHoursResource(project, employee, null, null);
				
				ac += new Double(hours==null?0:hours)*cost;
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return ac;
	}

	/**
	 * Calc hours to the activiy for status
	 * @param idActivity
	 * @param timestatus
	 * @return
	 * @throws Exception 
	 */
	public double calcHoursActivityForStatus(Integer idActivity, String timestatus) throws Exception {
		
		double hours = 0.0;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			hours =	timesheetDAO.calcHoursActivityForStatus(idActivity, timestatus);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return hours;
	}

	/**
	 * Calculation AC from activity (cost / hour * hours)
	 * 
	 * @param idActivity
	 * @param timestatus
	 * @param controlDate
	 * @param exludeWithProvider
	 * @return
	 * @throws Exception
	 */
	public double calcACforActivityAndStatus(Integer idActivity, String timestatus, Date controlDate, boolean exludeWithProvider) throws Exception {
		
		Double ac = 0.0;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			// Calculation AC from activity (cost / hour * hours)
			//
			ac = calcACforActivityAndStatus(session, idActivity, timestatus, controlDate, exludeWithProvider, null);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return ac;
	}

    /**
     * Get hours in status of project
     *
     * @param project
     * @param status
     * @return
     * @throws Exception
     */
    public double getHoursResource(Project project, String status) throws Exception {

        Double hours = 0.0;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {
            tx = session.beginTransaction();

            TimesheetDAO timesheetDAO 				= new TimesheetDAO(session);
            // Get hours in status of project
            hours = timesheetDAO.getHoursResource(project, status);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return hours;
    }

	/**
	 * Calculation AC from activity (cost / hour * hours)
	 * 
	 * @param session
	 * @param idActivity
	 * @param timestatus
	 * @param controlDate
	 * @param exludeWithProvider
	 * @param sellers 
	 * @return
	 * @throws NotFoundSellersException 
	 */
	public double calcACforActivityAndStatus(Session session, Integer idActivity, String timestatus,
			Date controlDate, boolean exludeWithProvider, List<Activityseller> sellers) throws NotFoundSellersException {
		
		// Default value
		double ac = 0.0;
		
		// Instance DAOs
		TimesheetDAO timesheetDAO 				= new TimesheetDAO(session);
		TeamMemberDAO teamMemberDAO				= new TeamMemberDAO(session);

		// Create filters for find time sheets
		ArrayList<PropertyRelation> listFilter 	= new ArrayList<PropertyRelation>();
		listFilter.add(new PropertyRelation(Constants.EQUAL_RESTRICTION, Timesheet.PROJECTACTIVITY, new Projectactivity(idActivity)));
		listFilter.add(new PropertyRelation(Constants.EQUAL_RESTRICTION, Timesheet.STATUS, timestatus));
		
		List<Timesheet> timesheets = timesheetDAO.findByFilters(listFilter, null, null, null);
		
		LOGGER.debug("\t\t Number of timesheets: "+timesheets.size());
		
		boolean hasSeller = false;
		
		// Calcule AC
		for (Timesheet timesheet : timesheets) {
			
			LOGGER.debug("\t\t\t Timesheet: init "
					+ DateUtil.format("dd/MM/yyyy", timesheet.getInitDate())
					+ " end "
					+ DateUtil.format("dd/MM/yyyy", timesheet.getEndDate()));
			
			// Find teammembers for timesheet
			List<Teammember> teammembers = teamMemberDAO.findByActivityAndDates(
					timesheet.getEmployee(),
					new Projectactivity(idActivity),
					timesheet.getInitDate(), timesheet.getEndDate(),
					null);
			
			if (ValidateUtil.isNotNull(teammembers)) {
				// Calc ac for one day
				ac += calcAC(teammembers, timesheet.getInitDate(), timesheet.getHoursDay1(), 0, controlDate, exludeWithProvider, sellers, hasSeller);
				ac += calcAC(teammembers, timesheet.getInitDate(), timesheet.getHoursDay2(), 1, controlDate, exludeWithProvider, sellers, hasSeller);
				ac += calcAC(teammembers, timesheet.getInitDate(), timesheet.getHoursDay3(), 2, controlDate, exludeWithProvider, sellers, hasSeller);
				ac += calcAC(teammembers, timesheet.getInitDate(), timesheet.getHoursDay4(), 3, controlDate, exludeWithProvider, sellers, hasSeller);
				ac += calcAC(teammembers, timesheet.getInitDate(), timesheet.getHoursDay5(), 4, controlDate, exludeWithProvider, sellers, hasSeller);
				ac += calcAC(teammembers, timesheet.getInitDate(), timesheet.getHoursDay6(), 5, controlDate, exludeWithProvider, sellers, hasSeller);
				ac += calcAC(teammembers, timesheet.getInitDate(), timesheet.getHoursDay7(), 6, controlDate, exludeWithProvider, sellers, hasSeller);
			}
			else {
				LOGGER.debug("\t\t\t Not found teammembers for timesheet");
			}
		}
		
		if (ValidateUtil.isNotNull(sellers) && !hasSeller) {
			// No hours earned for the same resource provider node
			throw new NotFoundSellersException();
		}
		
		return ac;
	}

	/**
	 * Calculation AC
	 * 
	 * @param teammembers
	 * @param date
	 * @param hoursDay
	 * @param day
	 * @param controlDate 
	 * @param exludeWithProvider 
	 * @param sellers 
	 * @param hasSeller 
	 * @return
	 */
	private Double calcAC(List<Teammember> teammembers, Date date,
			Double hoursDay, int day, Date controlDate, boolean exludeWithProvider,
			List<Activityseller> sellers, boolean hasSeller) {
		
		double ac 		= 0;
		boolean done 	= false;
		
		// Update date of day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		
		if (controlDate == null) {
			done = true;
		}
		else {
			
			Calendar calendarControl = Calendar.getInstance();
			calendarControl.setTime(controlDate);
			
			// Hours in control date
			if (calendar.before(calendarControl) || calendar.equals(calendarControl)) {
				done = true;
			}
			else {
				LOGGER.debug("\t\t\t\t Exclude time sheet day ("
						+ DateUtil.format("dd/MM/yyyy", calendar.getTime())
						+ ") after control date ("
						+ DateUtil.format("dd/MM/yyyy", controlDate) +")");
			}
		}
		
		if (done) {
			
			if (hoursDay != null && hoursDay > 0) {
				
				Teammember teammember = null;
				
				int i = 0;
				
				// Find member between date
				while (teammember == null && teammembers.size() > i) {
					
					// Current member
					Teammember currentMember = teammembers.get(i++);
					if (DateUtil.between(currentMember.getDateIn(), currentMember.getDateOut(), calendar.getTime())) {
						teammember = currentMember;
					}
				}
				
				// Calculation AC
				if (teammember != null && (!exludeWithProvider || (exludeWithProvider && teammember.getEmployee().getSeller() == null))) {
					
					// Seller of employee
					Seller seller = teammember.getEmployee().getSeller();
					
					// Check if seller of employee its in sellers of activity 
					if (ValidateUtil.isNotNull(sellers) && !hasSeller && seller != null && sellers.contains(seller)) {

						// Flag for exclude hours
						hasSeller = true;
					}
					
					double cost = 1;
					
					// Set cost for calculate hours value
					//
					if (teammember.getSellRate() != null && teammember.getSellRate() > 0) {
						cost = teammember.getSellRate();
					}
					else if (teammember.getEmployee().getCostRate() != null && teammember.getEmployee().getCostRate() > 0) {
						cost = teammember.getEmployee().getCostRate();
					}
					
					// Calculate AC
					ac = cost * hoursDay;
				}
			}
		}
		
		return ac;
	}

	/**
	 * Hours assigned
	 * @param teammember
	 * @param minStatus
	 * @param maxStatus
	 * @return
	 * @throws Exception
	 */
	public boolean hoursInState(Teammember teammember, String minStatus, String maxStatus) throws Exception {
		
		boolean assigend = true;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			assigend = timesheetDAO.hoursInState(teammember, minStatus, maxStatus);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return assigend;
	}

	/**
	 * Find hours for resource
	 * @param user
	 * @param date
	 * @return
	 * @throws Exception 
	 */
	public List<Timesheet> findHoursForResource(Employee user, Date date, HashMap<String, String> settings) throws Exception {
		
		List<Timesheet> list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			// Dates of week
			Date initWeek	= DateUtil.getFirstWeekDay(date);
        	Date endWeek	= DateUtil.getLastWeekDay(date);
			        
        	// Joins
        	List<String> joins = new ArrayList<String>();
			joins.add(Timesheet.PROJECTACTIVITY);
			joins.add(Timesheet.PROJECTACTIVITY + "." + Projectactivity.PROJECT);
			joins.add(Timesheet.PROJECTACTIVITY + "." + Projectactivity.PROJECT+ "." + Project.EMPLOYEEBYPROJECTMANAGER);
			joins.add(Timesheet.PROJECTACTIVITY + "." + Projectactivity.PROJECT+ "." + Project.EMPLOYEEBYPROJECTMANAGER+ "." + Employee.CONTACT);
			joins.add(Timesheet.OPERATION);
			
			// Add hours not finally approved
			list = timesheetDAO.findByCriteria(user, initWeek, endWeek, joins, null, Constants.TIMESTATUS_APP0, Constants.TIMESTATUS_APP2, null, false, settings);
			
			// Add hours approved 
			list.addAll(timesheetDAO.findByCriteria(user, initWeek, endWeek, joins, null, Constants.TIMESTATUS_APP3, Constants.TIMESTATUS_APP3, null, null, true, settings));
			
			// Add all operations
			list.addAll(timesheetDAO.findByOperation(user, initWeek, endWeek, joins, null, null));
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}
	
	/**
	 * Change state hours
	 * 
	 * @param user
	 * @param idEmployees
	 * @param idProjects
	 * @param date
	 * @param comments
	 * @param approve
	 * @param settings
	 * @param extraData
	 *@param resourceBundle  @throws Exception
	 */
	public void changeHours(Employee user, Integer[] idEmployees, Integer[] idProjects,
							Date date, String comments, boolean approve, HashMap<String, String> settings, Map<String, Object> extraData, ResourceBundle resourceBundle) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
		
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			TimesheetcommentDAO commentDAO = new TimesheetcommentDAO(session);
			
			if (ValidateUtil.isNotNull(idEmployees)) {
				
				// Dates of week
				Date initDate	= DateUtil.getFirstWeekDay(date);
				Date endDate	= DateUtil.getLastWeekDay(date);
				
				// Approval rol
				int approveRol = Integer.parseInt(SettingUtil.getSetting(
						session,
						user.getContact().getCompany(),
						Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET,
						Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET));
				
				// Status for rol

				String previousStatus;
				String nextStatus;

				Employee filterUser = (approveRol == user.getResourceprofiles().getIdProfile())?user:null;
				
				if (approve) {

                    // Three levels
					if (approveRol == Constants.ROLE_PMO || approveRol == Constants.ROLE_FM) {

                        if (Constants.ROLE_PM == user.getResourceprofiles().getIdProfile()) {
                            previousStatus  = Constants.TIMESTATUS_APP1;
                            nextStatus      = Constants.TIMESTATUS_APP2;
                        }
                        else {
                            previousStatus  = Constants.TIMESTATUS_APP2;
                            nextStatus      = Constants.TIMESTATUS_APP3;
                        }
					}
                    else if (approveRol == Constants.ROLE_PM) {

                        previousStatus  = Constants.TIMESTATUS_APP1;
                        nextStatus      = Constants.TIMESTATUS_APP3;
                    }
					else {
                        previousStatus  = Constants.TIMESTATUS_APP2;
                        nextStatus      = Constants.TIMESTATUS_APP3;
					}
				}
				else { // Rejected
					
					nextStatus      = Constants.TIMESTATUS_APP0;
                    previousStatus  = Constants.TIMESTATUS_APP3;

                    if ((approveRol == Constants.ROLE_PMO || approveRol == Constants.ROLE_FM) && Constants.ROLE_PM == user.getResourceprofiles().getIdProfile()) {
                        previousStatus  = Constants.TIMESTATUS_APP2;
                    }
				}

                String prevStatusRej = previousStatus;
				
				HashMap<Integer, Integer> employeesRejected = new HashMap<Integer, Integer>();

                // Put session
                extraData.put(ProcessListener.EXTRA_DATA_SESSION, session);

				int i = 0;
				for (int idEmployee : idEmployees) {
					
					Project proj = ((Constants.ROLE_PM == user.getResourceprofiles().getIdProfile()) && ValidateUtil.isNotNull(idProjects))
							? new Project(idProjects[i]) : null;
							
		    		// Get Time Sheet for change
					List<Timesheet> timeSheets = timesheetDAO.findByCriteria(new Employee(idEmployee), initDate, endDate, null,
							proj, previousStatus, prevStatusRej, filterUser, false, settings);
					
					timeSheets.addAll(timesheetDAO.findByOperation(new Employee(idEmployee), initDate,
							endDate, null, previousStatus, prevStatusRej));

					// Change time Sheets
					for (Timesheet timesheet : timeSheets) {

						Timesheetcomment timesheetcomment = new Timesheetcomment();
						timesheetcomment.setContentComment(comments);
						timesheetcomment.setActualStatus(nextStatus);
						timesheetcomment.setPreviousStatus(timesheet.getStatus());
						timesheetcomment.setTimesheet(timesheet);
						timesheetcomment.setCommentDate(new Date());
						commentDAO.makePersistent(timesheetcomment);
						
						timesheet.setStatus(nextStatus);
						timesheet.setSuggestReject(false);

						Timesheet previousTimesheet = new Timesheet(timesheet.getIdTimeSheet());

						previousTimesheet.setStatus(previousStatus);

                        // Call to listener
                        ProcessListener.getInstance().process(
                                TimesheetAbstractListener.class,
                                previousTimesheet,
                                timesheet,
                                extraData);


                        // DAO save timesheet
	    	    		timesheetDAO.makePersistent(timesheet);
	    	    		
	    	    		// Add employee rejected
	    	    		employeesRejected.put(idEmployee, idEmployee);
					}

					i++;
				}

				// Notification - Rejected hours
				if (!approve && !employeesRejected.isEmpty() && SettingUtil.getBoolean(settings, NotificationType.REJECTED_HOURS)) {
					
					NotificationSpecificLogic notificationsLogic 	= new NotificationSpecificLogic(settings);
					
					// Declare DAO
					EmployeeDAO employeeDAO = new EmployeeDAO(session);
					
					// Joins
					List<String> joins = new ArrayList<String>();
					joins.add(Employee.CONTACT);
						
					// List employees
					List<Employee> employees = employeeDAO.isInList(Employee.IDEMPLOYEE, employeesRejected.keySet().toArray(), joins);
					
					for (Employee employee : employees) {
						
						// Create notification
						notificationsLogic.rejectedHours(session, employee, user, initDate, endDate, comments);
					}
				}
			}
		
			tx.commit();
		}
		catch (Exception e) { e.printStackTrace();if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}
	
	/**
	 * Change state hours
	 * 
	 * @param user
	 * @param idTimeSheets
	 * @param comments
	 * @param approve
	 * @param resourceBundle 
	 * @param settings 
	 * @throws Exception
	 */
	public void changeHours(Employee user, Integer[] idTimeSheets, String comments, boolean approve, 
			HashMap<String, String> settings, Map<String, Object> extraData, ResourceBundle resourceBundle) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
		
			tx = session.beginTransaction();
			
			if (ValidateUtil.isNotNull(idTimeSheets)) {
				
				TimesheetDAO timesheetDAO = new TimesheetDAO(session);
				TimesheetcommentDAO commentDAO = new TimesheetcommentDAO(session);
			
				// Approval rol
				int approveRol = Integer.parseInt(SettingUtil.getSetting(
						session,
						user.getContact().getCompany(),
						Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET,
						Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET));

				// Next status for reject or approve
				String nextStatus = getNextTimeSheetStatus(user, approve, approveRol);

    			// List rejected
    			List<Timesheet> timesheetsRejected = new ArrayList<Timesheet>();

				// Put session
				extraData.put(ProcessListener.EXTRA_DATA_SESSION, session);

    			for (int idTimeSheet : idTimeSheets) {
    				
    	    		Timesheet timesheet = timesheetDAO.findById(idTimeSheet);

    	    		if (timesheet != null
    	    				&& (timesheet.getProjectactivity() == null
                            || (!Constants.STATUS_CLOSED.equals(timesheet.getProjectactivity().getProject().getStatus())
                                && !Constants.STATUS_ARCHIVED.equals(timesheet.getProjectactivity().getProject().getStatus())))
    	    				&& permittedStatusTransition(user, timesheet, approveRol, approve)) {

						// Previous time sheet for listener
						Timesheet previousTimesheet = new Timesheet(timesheet.getIdTimeSheet());
						previousTimesheet.setStatus(timesheet.getStatus());

						// Comment for change
    					Timesheetcomment timesheetcomment = new Timesheetcomment();
    					timesheetcomment.setContentComment(comments);
    					timesheetcomment.setActualStatus(nextStatus);
    					timesheetcomment.setPreviousStatus(timesheet.getStatus());
    					timesheetcomment.setTimesheet(timesheet);
    					timesheetcomment.setCommentDate(new Date());
    					commentDAO.makePersistent(timesheetcomment);

						// Update time sheet
    					timesheet.setStatus(nextStatus);
    					timesheet.setSuggestReject(false);

						// Call to listener
						ProcessListener.getInstance().process(
								TimesheetAbstractListener.class,
                                previousTimesheet,
								timesheet,
								extraData);

    					timesheetDAO.makePersistent(timesheet);
    					
    					// Add timesheet rejected
    					timesheetsRejected.add(timesheet);
    	    		}
    			}
    			
    			// Notification - Rejected hours
    			//
				if (!approve && !timesheetsRejected.isEmpty() && SettingUtil.getBoolean(settings, NotificationType.REJECTED_HOURS)) {
					
					NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(settings);
					
					// Create notification - only one notification
					notificationsLogic.rejectedHours(session, timesheetsRejected.get(0).getEmployee(), user, 
							timesheetsRejected.get(0).getInitDate(), timesheetsRejected.get(0).getEndDate(), comments);
				}
    		}
		
			tx.commit();
		}
		catch (Exception e) { e.printStackTrace();if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

	/**
	 * Calculate next status for approve or reject time sheet
	 *
	 * @param user
	 * @param approve
	 * @param approveRol
	 * @return
	 */
	private String getNextTimeSheetStatus(Employee user, boolean approve, int approveRol) {

		String nextStatus = Constants.TIMESTATUS_APP0;

		// Transition for approve
		if (approve) {

            // Approve level for RM
            if (Resourceprofiles.Profile.RESOURCE_MANAGER.equals(user.getResourceprofiles().getProfile())) {
                nextStatus = Constants.TIMESTATUS_APP2;
            }
            // Approve level for PM
            else if (Constants.ROLE_PM == user.getResourceprofiles().getIdProfile() && approveRol > 0) {
                nextStatus = Constants.TIMESTATUS_APP2;
            }
            // Approve level for Others
            else {
                nextStatus = Constants.TIMESTATUS_APP3;
            }
        }

		return nextStatus;
	}

	/**
	 * Permitted transition for user and time sheet
	 *
	 * @param user
	 * @param timesheet
	 * @return
	 */
	private boolean permittedStatusTransition(Employee user, Timesheet timesheet, int approveRol, boolean approve) {

		// Approve permissions
		if (approve) {

			// Transition level for RM
			if (Resourceprofiles.Profile.RESOURCE_MANAGER.equals(user.getResourceprofiles().getProfile()) &&
					Constants.TIMESTATUS_APP1.equals(timesheet.getStatus())) {
				return true;
			}
			// Transition level for PM
			else if (Constants.ROLE_PM == user.getResourceprofiles().getIdProfile() && approveRol > 0 &&
					Constants.TIMESTATUS_APP1.equals(timesheet.getStatus())) {
				return true;
			}
			// Transition level for Others
			else if (Constants.TIMESTATUS_APP2.equals(timesheet.getStatus())) {
				return true;
			}
		}
		// Reject permissions
		else {

			// Transition level for RM
			if (Resourceprofiles.Profile.RESOURCE_MANAGER.equals(user.getResourceprofiles().getProfile()) &&
					(Constants.TIMESTATUS_APP2.equals(timesheet.getStatus()) || Constants.TIMESTATUS_APP1.equals(timesheet.getStatus()))) {
				return true;
			}
			// Transition level for PM
			else if (Constants.ROLE_PM == user.getResourceprofiles().getIdProfile() && approveRol > 0 && Constants.TIMESTATUS_APP1.equals(timesheet.getStatus())) {
				return true;
			}
			// Transition level for Others
			else if (Constants.TIMESTATUS_APP2.equals(timesheet.getStatus()) ||
					Constants.TIMESTATUS_APP3.equals(timesheet.getStatus())) {
				return true;
			}
		}

		return false;
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
	 * @throws Exception
	 */
	public List<ResourceTimeWrap> capacityRunning(List<Employee> resources, Date since, Date until,
                                                  Boolean showOperations, Integer[] idProjects, Employee user,
                                                  Integer[] idPMs) throws Exception {
		
		List<ResourceTimeWrap> list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			list = timesheetDAO.capacityRunning(resources, since, until, showOperations, idProjects, user, idPMs);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * Get timesheets in app3 by resource and dates by project
	 * 
	 * @param resource
	 * @param since
	 * @param until
	 * @param user
     * @param settings
     * @return
	 * @throws Exception 
	 */
	public List<ResourceTimeWrap> capacityRunningResourceByProject(Employee resource, Date since, Date until,
                                                                   Employee user, HashMap<String, String> settings) throws Exception {
		
		List<ResourceTimeWrap> list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			list = timesheetDAO.capacityRunningResourceByProject(resource, since, until, user, settings);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}
	
	/**
	 * Capacity running resource by job category
	 * 
	 * @param resource
	 * @param since
	 * @param until
	 * @param user
     * @param settings
     * @return
	 * @throws Exception 
	 */
	public List<ResourceTimeWrap> capacityRunningResourceByJobCategory(Employee resource, Date since, Date until,
                                                                       Employee user, HashMap<String, String> settings) throws Exception {
		
		List<ResourceTimeWrap> list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			list = timesheetDAO.capacityRunningResourceByJobCategory(resource, since, until, user, settings);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * Check for hours pending approval
	 * 
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public boolean pendingApproval(Project project) throws Exception {
		
		boolean pending = false;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
		
			tx = session.beginTransaction();
			
			TimesheetDAO dao = new TimesheetDAO(session);
			pending = dao.pendingApproval(project);
			
			tx.commit();
		}
		catch (Exception e) { e.printStackTrace();if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return pending;
	}
	
	/**
	 * Calculate AC of Project
	 * 
	 * @param session
	 * @param project
	 * @param statusDate
	 * @param calculateEVMSetting
	 * @return
	 * @throws Exception
	 */
	public double calcProjectAC(Session session, Project project, Date statusDate, String calculateEVMSetting) throws Exception {
		
		// Default value for AC
		double	acApp3 = 0.0;
		
		// Instance DAOs
		ProjectActivityDAO activityDAO		= new ProjectActivityDAO(session);
		ActivitysellerDAO activitysellerDAO = new ActivitysellerDAO(session);
		
		// Find activities of project
		List<Projectactivity> activities = activityDAO.findByProject(project, null);
		
		LOGGER.debug("* Calculate Project AC: "+project.getIdProject());
		
		for (Projectactivity activity : activities) {

			// Find sellers of activity
			List<Activityseller> sellers = activitysellerDAO.findByRelation(Activityseller.PROJECTACTIVITY, activity);
			
			// Activity has sellers associated
			if (Constants.CALCULATE_EVM_EXTERNAL_TM.equals(calculateEVMSetting)
					&& ValidateUtil.isNotNull(sellers)) {
				
				try {
					
					// Calculate AC hours - (Sell rate or cost rate) * hours
					double ac = calcACforActivityAndStatus(session, activity.getIdActivity(), Constants.TIMESTATUS_APP3, statusDate, false, sellers);
					acApp3 += ac;
					
					LOGGER.debug("\t Has Sellers, Calc AC for Activity And Status: "+ac+ " Activity: "+activity.getActivityName());
				}
				catch (NotFoundSellersException e) {
					
					// Add EV
					acApp3 += activity.getEv() == null?0:activity.getEv();
					
					LOGGER.debug("\t Has Sellers, Add EV: "+activity.getEv()+ " Activity: "+activity.getActivityName());
				}
				
			}
			else {
				
				// (Sell rate or cost rate) * hours
				//
				double ac = calcACforActivityAndStatus(session, activity.getIdActivity(), Constants.TIMESTATUS_APP3, statusDate, false, null);
				acApp3 += ac;
				
				LOGGER.debug("\t Calc AC for Activity And Status: "+ac+ " Activity: "+activity.getActivityName());
			}
		}
		
		return acApp3;
	}
	
	/**
	 * Calculate AC of Project
	 * 
	 * @param project
	 * @param statusDate
	 * @param calculateEVMSetting
	 * @return
	 * @throws Exception
	 */
	public double calcProjectAC(Project project, Date statusDate, String calculateEVMSetting) throws Exception {
		
		// Default value for AC
		double	acApp3 = 0.0;
		
		// Session and transaction
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
		
			// Start transaction
			tx = session.beginTransaction();
			
			acApp3 = calcProjectAC(session, project, statusDate, calculateEVMSetting);
			
			tx.commit();
		}
		catch (Exception e) { e.printStackTrace();if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return acApp3;
	}
	
	/**
	 * Returns timesheets for an employee by dates
	 * 
	 * @param initDate
	 * @param endDate
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalWrap> findAllApprovals(Date initDate, Date endDate, Employee user) throws Exception {
		
		List<ApprovalWrap> listApp = null;
	
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx	= null;
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			// Activities time for approve with timesheet suggest reject
			listApp = timesheetDAO.findTimesheetsAllApp(initDate, endDate, user);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return listApp;
	}
	
	/**
	 *  Returns sum timesheets for an employee by dates
	 *  
	 * @param employee
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<TimesheetWrap> findTimesheets(Employee employee, Date date) throws Exception {
		
		List<TimesheetWrap> times = new ArrayList<TimesheetWrap>();
	
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			// Dates of week
			Date initWeek	= DateUtil.getFirstWeekDay(date);
        	Date endWeek	= DateUtil.getLastWeekDay(date);
        	
        	times = timesheetDAO.findTimesheetsForEmployee(initWeek, endWeek, employee); ;
		}
	
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	
		return times;
	}
	
	/**
	 * Returns total timesheets for an employee by dates for a PM
	 * 
	 * @param initDate
	 * @param endDate
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<ApprovalWrap> findAllTimesheetsByProject(Date initDate, Date endDate, Employee user) throws Exception {
		
		List<ApprovalWrap> listApp = null;
	
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx	= null;
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			// Activities time for approve with timesheet suggest reject
			listApp = timesheetDAO.findTimesheetsAllAppByProject(initDate, endDate, user);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return listApp;
	}

	/**
	 * Returns total hours for an employee by project
	 * 
	 * @param employee
	 * @param date
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public List<TimesheetWrap> findHoursDetailByProject(Employee employee, Date date, Project project) throws Exception {
		
		List<TimesheetWrap> times = new ArrayList<TimesheetWrap>();
	
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			// Dates of week
			Date initWeek	= DateUtil.getFirstWeekDay(date);
        	Date endWeek	= DateUtil.getLastWeekDay(date);
        	
        	times = timesheetDAO.findTimesheetsForEmployeeByProject(initWeek, endWeek, employee, project); ;
		}
	
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	
		return times;
	}

	/**
	 * Update time sheet suggest
	 * 
	 * @param idTimeSheet
	 * @param suggestRejectComment
	 * @param settings 
	 * @param resourceBundle 
	 * @throws Exception 
	 */
	public void suggestRejectTimeSheet(int idTimeSheet, String suggestRejectComment, 
			HashMap<String, String> settings, ResourceBundle resourceBundle) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			// Declare DAO
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			
			Timesheet timesheet = timesheetDAO.findById(idTimeSheet);
			
			// Set data
			timesheet.setSuggestRejectComment(suggestRejectComment);
			timesheet.setSuggestReject(true);
			
			// Update
			timesheetDAO.makePersistent(timesheet);
			
			// Notification - Request rejected hours
            if (SettingUtil.getBoolean(settings, NotificationType.REQUEST_REJECTED_HOURS)) {
				
                NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(settings);
                
                notificationsLogic.requestRejectedHours(session, timesheet, settings);
            }
            
            tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

    /**
     * Update timesheet
     *
     * @param timesheet
     * @param user
     * @param resourceBundle
     * @param settings
     * @param approve
     * @param comments
     * @return
     * @throws Exception
     *
     */
    public JSONObject updateTimeSheet(Employee user, ResourceBundle resourceBundle, HashMap<String, String> settings,
									  Map<String, Object> extraData, boolean approve, Timesheet timesheet, String comments) throws Exception {

        JSONObject returnJSON = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			// Declare DAO
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);

			// Put session
			extraData.put(ProcessListener.EXTRA_DATA_SESSION, session);

			if ((approve && timesheet.sumHours() == null) || (approve && timesheet.sumHours() > 0)) {

				String status = (timesheet.isOperationForApprove() || timesheet.isActivityTime() ? Constants.TIMESTATUS_APP1 : Constants.TIMESTATUS_APP2);

				// Declare DAO
				TimesheetcommentDAO timesheetcommentDAO = new TimesheetcommentDAO(session);
				
				Timesheetcomment timesheetcomment = new Timesheetcomment();
				timesheetcomment.setContentComment(comments);
				timesheetcomment.setActualStatus(status);
				timesheetcomment.setPreviousStatus(timesheet.getStatus());
				timesheetcomment.setTimesheet(timesheet);
				timesheetcomment.setCommentDate(new Date());

				String previousStatus = timesheet.getStatus();

				// DAO save
				timesheetcommentDAO.makePersistent(timesheetcomment);
				timesheet.setStatus(status);

                // Send notifications APP1->PM, APP2->PMO with a listener
                // Call to listener
				Timesheet previousTimesheet = new Timesheet(timesheet.getIdTimeSheet());

				previousTimesheet.setStatus(previousStatus);

				// Call to listener
				ProcessListener.getInstance().process(
						TimesheetAbstractListener.class,
                        previousTimesheet,
                        timesheet,
						extraData);


				returnJSON = info(resourceBundle, StringPool.SUCCESS, "msg.info.status", returnJSON, "timesheet", "applevel."+status);
				returnJSON.put("status", resourceBundle.getString("applevel."+ status));
			}
			else { 
				returnJSON = infoUpdated(resourceBundle, returnJSON, "timesheet"); 
			}
			
			timesheet.setSuggestReject(false);

			// DAO
            timesheet = timesheetDAO.makePersistent(timesheet);
			
			// Operation
			//
			if (timesheet.isOperationForManager()) {
				
				// Declare DAO
				EmployeeoperationdateDAO employeeoperationdateDAO = new EmployeeoperationdateDAO(session);
				
	    		Calendar tempDate = DateUtil.getCalendar();
	    		tempDate.setTime(timesheet.getInitDate());
	    		
	    		List<Employeeoperationdate> emplDates = null;
	    		
	    		// Hours
	    		List<Double> hours = new ArrayList<Double>();
	    		
	    		hours.add(timesheet.getHoursDay1() != null ? timesheet.getHoursDay1() : 0D);
	    		hours.add(timesheet.getHoursDay2() != null ? timesheet.getHoursDay2() : 0D);
	    		hours.add(timesheet.getHoursDay3() != null ? timesheet.getHoursDay3() : 0D);
	    		hours.add(timesheet.getHoursDay4() != null ? timesheet.getHoursDay4() : 0D);
	    		hours.add(timesheet.getHoursDay5() != null ? timesheet.getHoursDay5() : 0D);
	    		hours.add(timesheet.getHoursDay6() != null ? timesheet.getHoursDay6() : 0D);
	    		hours.add(timesheet.getHoursDay7() != null ? timesheet.getHoursDay7() : 0D);
	    		
	    		for (int i = 0; i < 7; i++) {
	    			
	    			// DAO find
	    			emplDates = employeeoperationdateDAO.findForCalendar(user, timesheet.getOperation(), tempDate.getTime(), tempDate.getTime());
	    			
	    			if (hours.get(i) > 0 && emplDates.isEmpty()) {
	    				
	    				Employeeoperationdate emplDate = new Employeeoperationdate();
	    				emplDate.setDateForOperation(tempDate.getTime());
	    				emplDate.setEmployee(user);
	    				emplDate.setOperation(timesheet.getOperation());
	    				
	    				// DAO save
	    				employeeoperationdateDAO.makePersistent(emplDate);
	    			}
	    			else if (hours.get(i) == 0 && !emplDates.isEmpty()) {
	    				
	    				for (Employeeoperationdate emplDate :emplDates) {
	    					
	    					// DAO delete
	    					employeeoperationdateDAO.makeTransient(emplDate);
	    				}
	    			}
	    			
	    			tempDate.add(Calendar.DAY_OF_MONTH, 1);
	    		}

                // Notification - Approve operation
                //
                if (((approve && timesheet.sumHours() == null) || (approve && timesheet.sumHours() > 0)) &&
                        SettingUtil.getBoolean(settings, NotificationType.APPROVE_OPERATION)) {

                    NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(settings);

                    notificationsLogic.approveOperation(session, timesheet, settings);
                }

			}

            tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return returnJSON;
	}

	/**
	 * Change state of time Sheet
	 * 
	 * @param user
	 * @param settings
	 * @param resourceBundle 
	 * @param timesheet
	 * @param comments 
	 * @param approve 
	 * @return
	 * @throws Exception 
	 */
	public JSONObject changeTimeSheet(Employee user, HashMap<String, String> settings, ResourceBundle resourceBundle, 
			Timesheet timesheet, String comments, Map<String, Object> extraData, boolean approve) throws Exception {
		
		JSONObject returnJSON = new JSONObject();
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			// Declare DAO
			TimesheetDAO timesheetDAO 				= new TimesheetDAO(session);
			TimesheetcommentDAO timesheetcommentDAO = new TimesheetcommentDAO(session);

			// Put session
			extraData.put(ProcessListener.EXTRA_DATA_SESSION, session);
			
			String nextStatus;
			
			if (approve) {
				
				nextStatus = (user.getResourceprofiles().getIdProfile() == Constants.ROLE_PM && 
						Integer.parseInt(SettingUtil.getSetting(
								session,
								user.getContact().getCompany(),
								Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET,
								Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET)) > 0 ? Constants.TIMESTATUS_APP2 : Constants.TIMESTATUS_APP3);
			}
			else { 
				nextStatus = Constants.TIMESTATUS_APP0; 
			}

			// Set timesheet comment
			Timesheetcomment timesheetcomment = new Timesheetcomment();
			timesheetcomment.setContentComment(comments);
			timesheetcomment.setActualStatus(nextStatus);
			timesheetcomment.setPreviousStatus(timesheet.getStatus());
			timesheetcomment.setTimesheet(timesheet);
			timesheetcomment.setCommentDate(new Date());
			
			// DAO save
			timesheetcommentDAO.makePersistent(timesheetcomment);

			String previousStatus = timesheet.getStatus();

			timesheet.setStatus(nextStatus);
			timesheet.setSuggestReject(false);

			// DAO save
			timesheetDAO.makePersistent(timesheet);

			// Send notifications APP1->PM, APP2->PMO with a listener
            //
			Timesheet previousTimesheet = new Timesheet(timesheet.getIdTimeSheet());

			previousTimesheet.setStatus(previousStatus);

			// Call to listener
			ProcessListener.getInstance().process(
					TimesheetAbstractListener.class,
                    previousTimesheet,
                    timesheet,
					extraData);
			
			// Response
			returnJSON = info(resourceBundle, StringPool.SUCCESS, "msg.info.status", new JSONObject(), "timesheet", "applevel."+nextStatus);
            
			// Notification - Rejected hours
			//
			if (!approve && SettingUtil.getBoolean(settings, NotificationType.REJECTED_HOURS)) {
				
				NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(settings);
				
				// Create notification
				notificationsLogic.rejectedHours(session, timesheet.getEmployee(), user, timesheet.getInitDate(), timesheet.getEndDate(), comments);
			}
			
            tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return returnJSON;
	}

    /**
     * Consult operations
     *
     * @param employee
     * @param initDate
     * @param endDate
     * @param joins
     * @return
     */
    public List<OperationFte> consultOperations(Employee employee, Date initDate, Date endDate, List<String> joins) throws Exception {

        List<OperationFte> operationsFte = null;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {
            tx = session.beginTransaction();

            // Declare DAO
            TimesheetDAO timesheetDAO = new TimesheetDAO(session);

            // DAO
            List<Timesheet> timesheets = timesheetDAO.findWithOperationByResource(employee, initDate, endDate, joins);

            if (ValidateUtil.isNotNull(timesheets)) {

                // Set dates
                List<Date> dates = DateUtil.lisDates(initDate, endDate);

                HashMap<Operation, HashMap<Date, Double>> operations = new HashMap<Operation, HashMap<Date, Double>>();

                Double hoursDay1;
                Double hoursDay2;
                Double hoursDay3;
                Double hoursDay4;
                Double hoursDay5;
                Double hoursDay6;
                Double hoursDay7;

                for (Timesheet timesheet : timesheets) {

                    // Get hours timesheet
                    hoursDay1 = timesheet.getHoursDay1();
                    hoursDay2 = timesheet.getHoursDay2();
                    hoursDay3 = timesheet.getHoursDay3();
                    hoursDay4 = timesheet.getHoursDay4();
                    hoursDay5 = timesheet.getHoursDay5();
                    hoursDay6 = timesheet.getHoursDay6();
                    hoursDay7 = timesheet.getHoursDay7();

                    // Get days
                    HashMap<Date, Double> days = operations.get(timesheet.getOperation()) == null ?
                            new HashMap<Date, Double>() : operations.get(timesheet.getOperation());

                    // Update days with hours
                    Calendar calendar = DateUtil.getCalendar();
                    calendar.setTime(timesheet.getInitDate());

                    days.put(calendar.getTime(), hoursDay1);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    days.put(calendar.getTime(), hoursDay2);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    days.put(calendar.getTime(), hoursDay3);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    days.put(calendar.getTime(), hoursDay4);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    days.put(calendar.getTime(), hoursDay5);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    days.put(calendar.getTime(), hoursDay6);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    days.put(calendar.getTime(), hoursDay7);

                    operations.put(timesheet.getOperation(), days);
                }

                // Build response
                //
                if (ValidateUtil.isNotNull(dates)) {

                    List<Double> hours;
                    operationsFte      = new ArrayList<OperationFte>();

                    // Sort operations
                    List<Operation> operationsSort = new ArrayList<Operation>(operations.keySet());

                    Collections.sort(operationsSort, new EntityComparator<Operation>(new Order(Operation.OPERATIONNAME, Order.ASC)));

                    // Add operation and hours
                    for (Operation operation : operationsSort) {

                        OperationFte operationFte = new OperationFte();

                        operationFte.setOperation(operation);

                        hours = new ArrayList<Double>();

                        for (Date date : dates) {
                            hours.add(operations.get(operation).get(date));
                        }

                        operationFte.setHours(hours);

                        operationsFte.add(operationFte);
                    }
                }
            }
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return operationsFte;
    }

    /**
     * Find imputations
     *
     * @param employee
     * @param initDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public List<ImputationWrap> findImputations(Employee employee, Date initDate, Date endDate) throws Exception {

        List<ImputationWrap> imputations = null;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {
            tx = session.beginTransaction();

            // Declare DAO
            TimesheetDAO timesheetDAO = new TimesheetDAO(session);

            // DAO select
            imputations = timesheetDAO.findImputations(employee, initDate, endDate);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return imputations;
    }
}