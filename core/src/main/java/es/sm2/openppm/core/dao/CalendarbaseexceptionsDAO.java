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
 * File: CalendarbaseexceptionsDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Calendarbaseexceptions;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class CalendarbaseexceptionsDAO extends AbstractGenericHibernateDAO<Calendarbaseexceptions, Integer> {

	public CalendarbaseexceptionsDAO(Session session) {
		super(session);
	}
	
	/**
	 * Delete all Exceptions of Calendar Base
	 * @param calendarbase
	 */
	public void deleteExceptions(Calendarbase calendarbase) {
		
		Query query = getSession().createQuery(
				"delete from Calendarbaseexceptions where calendarbase.idCalendarBase = :idCalendar");

		query.setInteger("idCalendar", calendarbase.getIdCalendarBase());
		
		query.executeUpdate();
		
	}

	/**
	 * Get Base exceptions by employee
	 * @param idEmployee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Calendarbaseexceptions> findByEmployee(Integer idEmployee) {		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.createCriteria(Calendarbaseexceptions.CALENDARBASE)
			.createCriteria(Calendarbase.EMPLOYEES)
			.add(Restrictions.idEq(idEmployee));
		
		return crit.list();
	}
	
	/**
	 * 	
	 * @param base
	 * @param idEmployee
	 * @param initDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Calendarbaseexceptions> findByCalendarBase(Calendarbase base, Integer idEmployee, Date initDate, Date endDate) {
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Calendarbaseexceptions.CALENDARBASE, base))
			.add(Restrictions.ge(Calendarbaseexceptions.STARTDATE, initDate))
			.add(Restrictions.le(Calendarbaseexceptions.FINISHDATE, endDate))
			.createCriteria(Calendarbaseexceptions.CALENDARBASE)
			.createCriteria(Calendarbase.EMPLOYEES)
			.add(Restrictions.idEq(idEmployee));			
		
		return crit.list();
	}

}
