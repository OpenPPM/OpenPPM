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
 * File: ProblemcheckLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.ProblemcheckDAO;
import es.sm2.openppm.core.dao.ProblemcheckprojectDAO;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Problemcheck;
import es.sm2.openppm.core.model.impl.Problemcheckproject;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Problemcheck
 * @see es.sm2.openppm.logic.Problemcheck
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class ProblemcheckLogic extends AbstractGenericLogic<Problemcheck, Integer> {

	/**
	 * Find the remaining and active checks
	 * 
	 * @param project
	 * @param company 
	 * @return
	 * @throws Exception 
	 */
	public List<Problemcheck> findbyRemainAndActive(Project project, Company company) throws Exception {
		
		List<Problemcheck> problemChecks = new ArrayList<Problemcheck>();
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			// Declare DAO
			ProblemcheckDAO problemcheckDAO 				= new ProblemcheckDAO(session);
			
			// DAO find by project
			List<Problemcheck> problemchecksExclude = problemcheckDAO.findByProject(project);
			
			// Parse to array exclude ids 
			//
			Integer[] idsExclude = null;
			
			if (ValidateUtil.isNotNull(problemchecksExclude)) {
				
				idsExclude = new Integer[problemchecksExclude.size()];
				
				int i = 0;
				for (Problemcheck problemcheck : problemchecksExclude) {
					
					idsExclude[i] = problemcheck.getIdProblemCheck();
					
					i++;
				}
			}
			
			// DAO remain problem checks
			problemChecks = problemcheckDAO.findbyRemainAndActive(idsExclude, company);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return problemChecks;
	}

	/**
	 * Delete problem check
	 * 
	 * @param problemcheck
	 * @throws Exception 
	 */
	public void deleteProblemCheck(Problemcheck problemcheck) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			// Declare DAOs
			ProblemcheckDAO problemcheckDAO 				= new ProblemcheckDAO(session);
			ProblemcheckprojectDAO problemcheckprojectDAO 	= new ProblemcheckprojectDAO(session);
			
			// Foreign keys control
			List<Problemcheckproject> closurecheckprojectList = problemcheckprojectDAO.findByRelation(Problemcheckproject.PROBLEMCHECK, 
					problemcheck, Problemcheckproject.IDPROBLEMCHECKPROJECT, Constants.ASCENDENT);
			
			if (ValidateUtil.isNotNull(closurecheckprojectList)) {
				throw new LogicException("msg.error.delete.this_has","problem_check","problem_check_list");
			}
			
			// Save
			problemcheckDAO.makeTransient(problemcheck);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}
}

