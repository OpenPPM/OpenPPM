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
 * File: ProjectcalendarLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.EmployeeDAO;
import es.sm2.openppm.core.dao.ProjectActivityDAO;
import es.sm2.openppm.core.dao.ProjectcalendarDAO;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectcalendar;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class ProjectcalendarLogic extends AbstractGenericLogic<Projectcalendar, Integer> {

	/**
	 * Return ProjectCalendar By Project
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public Projectcalendar consCalendarByProject(Project project) throws Exception {
		
		Projectcalendar calendar = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ProjectcalendarDAO projectcalendarDAO = new ProjectcalendarDAO(session);
			calendar = projectcalendarDAO.findByProject(project);
			
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
		return calendar;
	}

	/**
	 * Check if is Exception day in project
	 * @param initDate
	 * @param dayOfWeek
	 * @param idActivity
	 * @return
	 * @throws Exception
	 */
	public Boolean isException(Date initDate, Integer dayOfWeek, Integer idActivity, Integer idEmployee) throws Exception {
		
		boolean isException = false;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			Calendar exceptionDate = DateUtil.getCalendar();
			exceptionDate.setTime(initDate);
			exceptionDate.add(Calendar.DAY_OF_WEEK, dayOfWeek);
			
			ProjectcalendarDAO projectcalendarDAO = new ProjectcalendarDAO(session);
			ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			
			Projectactivity activity = activityDAO.findById(idActivity);
			Project project = activity.getProject();
			Employee employee = employeeDAO.findById(idEmployee);
			
			isException = projectcalendarDAO.isException(exceptionDate.getTime(), project.getProjectcalendar(), employee.getCalendarbase());
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return isException;
	}
}
