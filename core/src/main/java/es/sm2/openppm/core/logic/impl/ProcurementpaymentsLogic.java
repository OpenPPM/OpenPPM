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
 * File: ProcurementpaymentsLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.ProcurementpaymentsDAO;
import es.sm2.openppm.core.exceptions.ProjectNotFoundException;
import es.sm2.openppm.core.javabean.ProcurementBudget;
import es.sm2.openppm.core.model.impl.Procurementpayments;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.wrap.RangeDateWrap;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Procurementpayments
 * @see es.sm2.openppm.logic.Procurementpayments
 * @author Hibernate Generator by Javier Hernandez
 */
public class ProcurementpaymentsLogic extends AbstractGenericLogic<Procurementpayments, Integer>{

	
	/**
	 * Get Procurement Payments from Project
	 * 
	 * @param proj
	 * @param joins
	 * @return
	 * @throws Exception
	 */
	public List<Procurementpayments> consProcurementPaymentsByProject(Project proj, List<String> joins) throws Exception {
		if (proj == null) {
			throw new ProjectNotFoundException();
		}
		
		List<Procurementpayments> listProcPay = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProcurementpaymentsDAO procPayDAO = new ProcurementpaymentsDAO(session);
			listProcPay = procPayDAO.consProcurementPaymentsByProject(proj, joins);
			
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
		return listProcPay;
	}	
	
	/**
	 * Get Procurement Budgets from Project
	 * 
	 * @param proj
	 * @param joins
	 * @return
	 * @throws Exception
	 */
	public List<ProcurementBudget> consProcurementBudgetsByProject(Project proj, List<String> joins) throws Exception {
		if (proj == null) {
			throw new ProjectNotFoundException();
		}
		
		List<ProcurementBudget> lista = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProcurementpaymentsDAO procPayDAO = new ProcurementpaymentsDAO(session);
			lista = procPayDAO.consProcurementBudgetsByProject(proj, joins);
			
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
		return lista;
	}

	/**
	 * Find min and max actual dates of projects
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public RangeDateWrap findMinAndMaxActualDatesByProjects(Integer[] ids) throws Exception {
		
		RangeDateWrap range = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			// Declare dao
			ProcurementpaymentsDAO procPayDAO = new ProcurementpaymentsDAO(session);
			
			// Parse projects
			List<Project> projects = new ArrayList<Project>();
			
			for (Integer id : ids) {
				projects.add(new Project(id));
			}
			
			// Dao
			range = procPayDAO.findMinAndMaxActualDatesByProjects(projects);
			
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
		
		return range;
	}	
}

