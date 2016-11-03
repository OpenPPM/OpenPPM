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
 * File: SecurityLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.SecurityDAO;
import es.sm2.openppm.core.exceptions.LoginMaxAttemptsException;
import es.sm2.openppm.core.exceptions.LoginUserExpiredException;
import es.sm2.openppm.core.exceptions.LoginUsernameException;
import es.sm2.openppm.core.utils.StringUtils;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class SecurityLogic extends AbstractGenericLogic<Security, Integer> {
	
	/**
	 * Return Username in use
	 * @param security
	 * @return
	 * @throws Exception 
	 */
	public boolean isUserName(Security security) throws Exception {
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		Security secure = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
		
			SecurityDAO securityDAO = new SecurityDAO(session);
			secure = securityDAO.getByUserName(security);
			
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
		return (secure != null);
	}

	
	/**
	 * Change password of employee
	 * @param idEmployee
	 * @return
	 * @throws Exception 
	 */
	public Security changePassword(Contact contact, String password) throws Exception {
		
		Security security = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
		
			SecurityDAO securityDAO = new SecurityDAO(session);
			
			security = securityDAO.getByContact(contact);
			if(!ValidateUtil.isNull(password)) {
				security.setPassword(StringUtils.md5(password));
			}
			else {
				security.setPassword(StringUtils.md5(security.getLogin()));	
			}			
			securityDAO.makePersistent(security);
			
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
		return security;
	}
	
	
	/**
	 * Get Security By Contact
	 * @param contact
	 * @return
	 * @throws Exception 
	 */
	public Security getByContact(Contact contact) throws Exception {
		
		Security security = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
		
			SecurityDAO securityDAO = new SecurityDAO(session);
			security = securityDAO.getByContact(contact);
			
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
		return security;
	}
	
	
	/**
	 * Get Security By Employee
	 * @param employee
	 * @return
	 * @throws Exception 
	 */
	public Security getByEmployee (Employee employee) throws Exception {
		
		Security security = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			SecurityDAO securityDAO = new SecurityDAO(session);
			security = securityDAO.getByEmployee(employee);

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
		return security;
	}


	/**
	 * Determine if an user can login
	 * @param security
	 * @return
	 * @throws Exception 
	 */
	public Security preLogin(Security security) throws Exception {
		Security sec = null;
		boolean maxAttempted = false;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
		
			SecurityDAO securityDAO = new SecurityDAO(session);
			sec = securityDAO.getByUserName(security);
			
			if (sec == null) {
				throw new LoginUsernameException();
			}
			else {
				
				SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_PATTERN, Constants.DEF_LOCALE_NUMBER);
				
				// User account is expired
				if (sec.getDateLapsed() != null) {
					throw new LoginUserExpiredException(dateFormat.format(sec.getDateLapsed()));
				}
				
				if (sec.getAttempts() != null && (sec.getAttempts()+1) >= Settings.MAX_LOGIN_ATTEMPS) {
					
					Calendar dateLapsed = Calendar.getInstance();
					dateLapsed.add(Calendar.MINUTE, Settings.BLOCK_WAIT_TIME);
					
					sec.setDateLapsed(dateLapsed.getTime());
					sec.setAttempts((sec.getAttempts() != null ? sec.getAttempts():0)+1);
					
					maxAttempted = true;
				}
				else {
					sec.setDateLapsed(null);
					sec.setAttempts((sec.getAttempts() != null ? sec.getAttempts():0)+1);
				}
				
				securityDAO.makePersistent(sec);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }

		if (maxAttempted) {
			throw new LoginMaxAttemptsException(Settings.BLOCK_WAIT_TIME);
		}
		
		return sec;
	}

	/**
	 * Update security
	 * @param security
	 * @throws Exception
	 */
	public void updateSecurity(Security security) throws Exception {
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			SecurityDAO securityDAO = new SecurityDAO(session);
			securityDAO.makePersistent(security);
			
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
	 * Check if user name is in use
	 * @param contact
	 * @param login
	 * @return
	 * @throws Exception 
	 */
	public boolean isUserName(Contact contact, String login) throws Exception {
		
		boolean inUse = false;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			SecurityDAO securityDAO = new SecurityDAO(session);
			inUse = securityDAO.isUserName(contact, login);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return inUse;
	}
}
