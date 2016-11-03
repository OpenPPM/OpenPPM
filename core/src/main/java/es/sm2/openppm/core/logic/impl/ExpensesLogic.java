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
 * File: ExpensesLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.ExpensesDAO;
import es.sm2.openppm.core.dao.ExpensesheetDAO;
import es.sm2.openppm.core.dao.ProjectCostsDAO;
import es.sm2.openppm.core.model.impl.Budgetaccounts;
import es.sm2.openppm.core.model.impl.Expenses;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcosts;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class ExpensesLogic extends AbstractGenericLogic<Expenses, Integer> {

	/**
	 * Get expense
	 * @param idCost
	 * @return
	 * @throws Exception 
	 */
	public Expenses consExpenses (Integer idCost) throws Exception {
		
		Expenses cost = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ExpensesDAO costDAO = new ExpensesDAO(session);
			if (idCost != -1) {
				cost = costDAO.findById(idCost, false);
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
		return cost;
	}
	
	
	/**
	 * Save expenses
	 * @param cost
	 * @param costDate 
	 * @throws Exception 
	 */
	public void saveExpenses (Expenses cost, Date costDate) throws Exception {
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ExpensesDAO costDAO = new ExpensesDAO(session);
			cost = costDAO.makePersistent(cost);
			
			if (costDate != null) {
				
				ProjectCostsDAO projectCostsDAO = new ProjectCostsDAO(session);
				
				Projectcosts projCost = cost.getProjectcosts();
				projCost.setCostDate(costDate);
				
				projectCostsDAO.makePersistent(projCost);
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
	}

	/**
	 * Check if budget account is in use in project and expense
	 * @param project
	 * @param expenses
	 * @param budgetaccounts
	 * @return
	 * @throws Exception 
	 */
	public boolean isBudgetInUse(Project project, Expenses expenses,
			Budgetaccounts budgetaccounts) throws Exception {
		
		boolean inUse = true;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			ExpensesDAO expensesDAO = new ExpensesDAO(session);
			inUse = expensesDAO.isBudgetInUse(project, expenses, budgetaccounts);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return inUse;
	}
	
	/**
	 * 
	 * @param expense
	 * @return
	 * @throws Exception
	 */
	public Double getSumExpenseSheet(Expenses expense) throws Exception {
		
		Double cost = null;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ExpensesheetDAO costDAO = new ExpensesheetDAO(session);
			cost = costDAO.getSumExpenseSheet(expense);		

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
		
		return cost;
	}	
}
