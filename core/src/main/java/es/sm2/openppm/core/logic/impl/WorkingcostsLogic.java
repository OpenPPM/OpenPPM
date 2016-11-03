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
 * File: WorkingcostsLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.WorkingcostsDAO;
import es.sm2.openppm.core.exceptions.ProjectNotFoundException;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Workingcosts;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Workingcosts
 * @see es.sm2.openppm.logic.Workingcosts
 * @author Hibernate Generator by Javier Hernandez
 */
public class WorkingcostsLogic extends AbstractGenericLogic<Workingcosts, Integer>{

	
	/**
	 * 
	 * @param proj
	 * @return
	 * @throws Exception
	 */
	public int consInternalCostByProject(Project proj) 
	throws Exception {
		
		int result = 0;
		List<Workingcosts> list = null;
		if (proj == null) {
			throw new ProjectNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WorkingcostsDAO workingDAO = new WorkingcostsDAO(session);
			list = workingDAO.consByProject(proj);
			
			if(!list.isEmpty()) {
				for (Workingcosts w : list) {
					result += w.getEffort();
				}
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
		return result;
	}

	/**
	 * 
	 * @param proj
	 * @param dept
	 * @return
	 * @throws Exception
	 */
	public int getCostByDeptAndProject(Project proj, String dept) 
	throws Exception {
		if (proj == null) {
			throw new ProjectNotFoundException();
		}
		int result = 0;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WorkingcostsDAO workingDAO = new WorkingcostsDAO(session);
			result = workingDAO.getCostsByDept(proj, dept);			
			
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
		return result;
	}

}

