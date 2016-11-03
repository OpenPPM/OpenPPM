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
 * File: ClosurecheckLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.ClosurecheckDAO;
import es.sm2.openppm.core.dao.ClosurecheckprojectDAO;
import es.sm2.openppm.core.model.impl.Closurecheck;
import es.sm2.openppm.core.model.impl.Closurecheckproject;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Closurecheck
 * @see es.sm2.openppm.logic.Closurecheck
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class ClosurecheckLogic extends AbstractGenericLogic<Closurecheck, Integer> {

	/**
	 * Delete closure check
	 * 
	 * @param closurecheck
	 * @throws Exception 
	 */
	public void deleteClosureCheck(Closurecheck closurecheck) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			// Declare DAOs
			ClosurecheckDAO closurecheckDAO 				= new ClosurecheckDAO(session);
			ClosurecheckprojectDAO closurecheckprojectDAO 	= new ClosurecheckprojectDAO(session);
			
			// Foreign keys control
			List<Closurecheckproject> closurecheckprojectList = closurecheckprojectDAO.findByRelation(Closurecheckproject.CLOSURECHECK, closurecheck, Closurecheckproject.IDCLOSURECHECKPROJECT, Constants.ASCENDENT);
			
			if (ValidateUtil.isNotNull(closurecheckprojectList)) {
				throw new LogicException("msg.error.delete.this_has","closure_check","closure_check_list");
			}
			
			// Save
			closurecheckDAO.makeTransient(closurecheck);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

	/**
	 * Find by company and active show
	 * 
	 * @param company
	 * @return
	 * @throws Exception 
	 */
	public List<Closurecheck> findByCompanyAndActive(Company company) throws Exception {
		
		List<Closurecheck> closureChecks = new ArrayList<Closurecheck>();
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ClosurecheckDAO closurecheckDAO = new ClosurecheckDAO(session);
			closureChecks = closurecheckDAO.findByCompanyAndActive(company);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return closureChecks;
	}
}
