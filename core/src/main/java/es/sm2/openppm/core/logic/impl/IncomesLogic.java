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
 * File: IncomesLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.IncomesDAO;
import es.sm2.openppm.core.model.impl.Incomes;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class IncomesLogic extends AbstractGenericLogic<Incomes, Integer> {
	
	/**
	 * Save income
	 * @param idFinancePlan
	 * @param income
	 * @throws Exception 
	 */
	public void saveIncome (Integer idProject, Incomes income) throws Exception {
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			income.setProject(new Project(idProject));
			
			IncomesDAO incomesDAO = new IncomesDAO(session);
			incomesDAO.makePersistent(income);
			
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
	 * Get income by id
	 * @param idIncome
	 * @return
	 * @throws Exception 
	 */
	public Incomes consIncome (Integer idIncome) throws Exception {
		
		Incomes income = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			IncomesDAO incomesDAO = new IncomesDAO(session);

			if (idIncome != -1) {
				income =  incomesDAO.findById(idIncome, false);
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
		return income;
	}
	
	
	/**
	 * List incomes by project
	 * @param idProject
	 * @param order 
	 * @return
	 * @throws Exception 
	 */
	public List<Incomes> consIncomes (Integer idProject, String order) throws Exception {
		
		List<Incomes> incomes = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			IncomesDAO incomesDAO = new IncomesDAO(session);

			if (idProject != -1) {
				incomes = incomesDAO.findByProject(new Project(idProject), order);
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
		return incomes;
	}
}
