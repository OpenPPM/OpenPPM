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
 * File: EmployeeoperationdateLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import es.sm2.openppm.core.dao.*;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Employeeoperationdate;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Employeeoperationdate
 * @see EmployeeoperationdateLogic
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public class EmployeeoperationdateLogic extends AbstractGenericLogic<Employeeoperationdate, Integer> {

	/**
	 * Find for generate calendar
	 * @param employee
	 * @param operation
	 * @param since
	 * @param until
	 * @return
	 * @throws Exception
	 */
	public List<Employeeoperationdate> findForCalendar(Employee employee,
			Operation operation, Date since, Date until) throws Exception {
		
		List<Employeeoperationdate> dates = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			EmployeeoperationdateDAO dao = new EmployeeoperationdateDAO(session);
			
			dates = dao.findForCalendar(employee, operation, since, until);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return dates;
	}

    /**
     * Update dates
     * @param employee
     * @param operation
     * @param emplDates
     * @param dates
     * @param dateFormat
     * @param settings
     *@param resourceBundle @throws Exception
     */
    public void updateDates(Employee employee, Operation operation, List<Employeeoperationdate> emplDates,
                            String[] dates, SimpleDateFormat dateFormat, HashMap<String, String> settings,
                            ResourceBundle resourceBundle) throws Exception {

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            // Declare DAOs
            EmployeeoperationdateDAO dateDao    = new EmployeeoperationdateDAO(session);
            TimesheetDAO timesheetDAO	        = new TimesheetDAO(session);
            EmployeeDAO employeeDAO		        = new EmployeeDAO(session);
            OperationDAO operationDAO           = new OperationDAO(session);

            // DAO find employee
            employee = employeeDAO.findById(employee.getIdEmployee());

            // DAO find operation
            operation = operationDAO.findById(operation.getIdOperation());

            String setting = SettingUtil.getString(settings, Settings.SETTING_RESOURCE_MANAGER_APPROVE_OPERATION,
                    Settings.DEFAULT_RESOURCE_MANAGER_APPROVE_OPERATION);

            Timesheet timeSheet = null;
            Employeeoperationdate newEmplDate;
            List<Date> datesForNotify = new ArrayList<Date>();

            for (String dateStr : dates) {

                boolean found = false;
                Date date = dateFormat.parse(dateStr);

                for (Employeeoperationdate emplDate : emplDates) {

                    if (DateUtil.equals(date, emplDate.getDateForOperation())) {
                        found = true;
                        emplDates.remove(emplDate);
                        break;
                    }
                }

                timeSheet = createDate(employee, operation, timeSheet, date, timesheetDAO, setting);

                // New operation
                if (!found) {

                    newEmplDate = new Employeeoperationdate();
                    newEmplDate.setEmployee(employee);
                    newEmplDate.setOperation(operation);
                    newEmplDate.setDateForOperation(date);

                    // DAO save
                    dateDao.makePersistent(newEmplDate);

                    // Add date in list
                    datesForNotify.add(date);
                }
            }

            // Notification - Assigned operation
            //
            if (SettingUtil.getBoolean(settings, NotificationType.ASSIGNED_OPERATION) && ValidateUtil.isNotNull(datesForNotify)) {

                NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(settings);

                notificationsLogic.assignedOperation(session, settings, employee, operation, datesForNotify);
            }

            // Delete all not valid
            deleteDates(employee, operation, emplDates, timesheetDAO, dateDao, setting);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }

	/**
	 * Delete Dates
	 * @param employee
	 * @param operation
	 * @param emplDates
	 * @throws Exception 
	 */
	public void deleteDates(Employee employee, Operation operation,
			List<Employeeoperationdate> emplDates) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			EmployeeoperationdateDAO dateDao = new EmployeeoperationdateDAO(session);
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			CompanyDAO companyDAO = new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(employee);
			
			String setting = SettingUtil.getSetting(
					session, company,
					Settings.SETTING_RESOURCE_MANAGER_APPROVE_OPERATION,
					Settings.DEFAULT_RESOURCE_MANAGER_APPROVE_OPERATION);
			
			deleteDates(employee, operation, emplDates, timesheetDAO, dateDao, setting);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}


	/**
	 * Create Dates
	 * @param employee
	 * @param operation
	 * @param dates
	 * @param dateFormat
	 * @throws Exception 
	 */
	public void createDates(Employee employee, Operation operation,
			String[] dates, SimpleDateFormat dateFormat) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			EmployeeoperationdateDAO dateDao = new EmployeeoperationdateDAO(session);
			TimesheetDAO timesheetDAO	= new TimesheetDAO(session);
			EmployeeDAO employeeDAO		= new EmployeeDAO(session);
			
			employee = employeeDAO.findById(employee.getIdEmployee());
			
			String setting = SettingUtil.getSetting(
					session, employee.getContact().getCompany(),
					Settings.SETTING_RESOURCE_MANAGER_APPROVE_OPERATION,
					Settings.DEFAULT_RESOURCE_MANAGER_APPROVE_OPERATION);
			
			Employeeoperationdate emplDate = null;
			Timesheet timeSheet = null;
			
			for (String dateStr : dates) {
				
				Date date = dateFormat.parse(dateStr);
				
				timeSheet = createDate(employee, operation, timeSheet, date, timesheetDAO, setting);
				
				emplDate = new Employeeoperationdate();
				emplDate.setEmployee(employee);
				emplDate.setOperation(operation);
				emplDate.setDateForOperation(date);
				
				dateDao.makePersistent(emplDate);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

	/**
	 * Create date
	 * @param employee
	 * @param operation
	 * @param timeSheet
	 * @param date
	 * @param timesheetDAO
	 * @return
	 */
	private Timesheet createDate(Employee employee, Operation operation, Timesheet timeSheet, Date date,
			TimesheetDAO timesheetDAO, String setting) {
		
		Date initDate	= DateUtil.getFirstWeekDay(date);
		Date endDate	= DateUtil.getLastWeekDay(date);
		
		if (timeSheet == null || !DateUtil.equals(timeSheet.getInitDate(),initDate)) {
			
			timeSheet = timesheetDAO.findByOperation(operation, employee, initDate, endDate);
		}
		
		if (timeSheet == null) {
			timeSheet = new Timesheet();
			timeSheet.setEmployee(employee);
			timeSheet.setOperation(operation);
			timeSheet.setInitDate(initDate);
			timeSheet.setEndDate(endDate);
		}
		
		if (Constants.TIMESTATUS_APP3.equals(setting)) {
			timeSheet.setStatus(Constants.TIMESTATUS_APP3);
		}
		else if (timeSheet.getStatus() == null) {
			timeSheet.setStatus(Constants.TIMESTATUS_APP0);
		}
		
		double hoursDay = (employee.getCalendarbase() != null && employee.getCalendarbase().getHoursDay() != null
				?employee.getCalendarbase().getHoursDay()
				:Constants.DEFAULT_HOUR_DAY);
		
		timeSheet = changeHours(timeSheet, date, hoursDay);
		
		return timesheetDAO.makePersistent(timeSheet);
	}
	
	/**
	 * Delete Dates
	 * @param employee
	 * @param operation
	 * @param emplDates
	 * @param timesheetDAO
	 * @param dateDao
	 */
	private void deleteDates(Employee employee, Operation operation, List<Employeeoperationdate> emplDates,
			TimesheetDAO timesheetDAO, EmployeeoperationdateDAO dateDao, String setting) {
		
		
		Timesheet timeSheet = null;
		
		for (Employeeoperationdate emplDate : emplDates) {
			
			Date initDate	= DateUtil.getFirstWeekDay(emplDate.getDateForOperation());
			Date endDate	= DateUtil.getLastWeekDay(emplDate.getDateForOperation());
			
			if (timeSheet == null || !DateUtil.equals(timeSheet.getInitDate(),initDate)) {
				
				timeSheet = timesheetDAO.findByOperation(operation, employee, initDate, endDate);
			}
			
			if (timeSheet != null) {
				
				if (Constants.TIMESTATUS_APP3.equals(setting)) {
					timeSheet.setStatus(Constants.TIMESTATUS_APP3);
				}
				
				timeSheet = changeHours(timeSheet, emplDate.getDateForOperation(), null);
				
				
				if (totalHours(timeSheet) > 0) { timeSheet = timesheetDAO.makePersistent(timeSheet); }
				else { timesheetDAO.makeTransient(timeSheet); timeSheet = null; }
			}
			
			dateDao.makeTransient(emplDate);
		}
	}
	
	/**
	 * Sum hour of time sheet
	 * @param timeSheet
	 * @return
	 */
	private double totalHours(Timesheet timeSheet) {
		
		double total = 0;
		
		if (timeSheet.getHoursDay1() != null) { total += timeSheet.getHoursDay1(); }
		if (timeSheet.getHoursDay2() != null) { total += timeSheet.getHoursDay2(); }
		if (timeSheet.getHoursDay3() != null) { total += timeSheet.getHoursDay3(); }
		if (timeSheet.getHoursDay4() != null) { total += timeSheet.getHoursDay4(); }
		if (timeSheet.getHoursDay5() != null) { total += timeSheet.getHoursDay5(); }
		if (timeSheet.getHoursDay6() != null) { total += timeSheet.getHoursDay6(); }
		if (timeSheet.getHoursDay7() != null) { total += timeSheet.getHoursDay7(); }
		
		return total;
	}
	
	/**
	 * Change hours in one day
	 * @param timeSheet
	 * @param date
	 * @param hoursDay
	 * @return
	 */
	private Timesheet changeHours(Timesheet timeSheet,
			Date date, Double hoursDay) {
		
		Calendar dayCal = DateUtil.getCalendar();
		dayCal.setTime(date);
		
		int day = dayCal.get(Calendar.DAY_OF_WEEK);
		
		if (day == Calendar.MONDAY) { timeSheet.setHoursDay1(hoursDay); }
		else if (day == Calendar.TUESDAY) { timeSheet.setHoursDay2(hoursDay); }
		else if (day == Calendar.WEDNESDAY) { timeSheet.setHoursDay3(hoursDay); }
		else if (day == Calendar.THURSDAY) { timeSheet.setHoursDay4(hoursDay); }
		else if (day == Calendar.FRIDAY) { timeSheet.setHoursDay5(hoursDay); }
		else if (day == Calendar.SATURDAY) { timeSheet.setHoursDay6(hoursDay); }
		else if (day == Calendar.SUNDAY) { timeSheet.setHoursDay7(hoursDay); }
		
		return timeSheet;
	}
}

