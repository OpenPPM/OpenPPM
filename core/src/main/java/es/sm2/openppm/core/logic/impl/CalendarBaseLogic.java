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
 * File: CalendarBaseLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.CalendarbaseDAO;
import es.sm2.openppm.core.exceptions.CalendarBaseNotFoundException;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class CalendarBaseLogic extends AbstractGenericLogic<Calendarbase, Integer> {

	/**
	 * Return Calendar Base with Exceptions
	 * @param calendarbase
	 * @return
	 * @throws Exception 
	 */
	public Calendarbase consCalendarBaseWithExceptions(Calendarbase calendarbase) throws Exception {
		
		if (calendarbase.getIdCalendarBase() == null) { 
			throw new CalendarBaseNotFoundException();
		}
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		Transaction tx 			= null;
		Calendarbase calendar 	= null;
		
		try {
			tx = session.beginTransaction();
			
			CalendarbaseDAO calendarbaseDAO = new CalendarbaseDAO(session);
			calendar = calendarbaseDAO.findByIdWithExceptions(calendarbase);
			
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
	 * Return Calendar Base
	 * @param calendarbase
	 * @return
	 * @throws Exception 
	 */
	public Calendarbase consCalendarBase(Calendarbase calendarbase) throws Exception {
		
		if (calendarbase.getIdCalendarBase() == null) { 
			throw new CalendarBaseNotFoundException();
		}
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		Transaction tx 			= null;
		Calendarbase calendar 	= null;
		
		try {
			tx = session.beginTransaction();
			
			CalendarbaseDAO calendarbaseDAO = new CalendarbaseDAO(session);
			calendar = calendarbaseDAO.findById(calendarbase.getIdCalendarBase(), false);
			
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

}
