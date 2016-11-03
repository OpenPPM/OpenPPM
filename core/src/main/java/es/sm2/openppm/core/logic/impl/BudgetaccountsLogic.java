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
 * File: BudgetaccountsLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.BudgetaccountsDAO;
import es.sm2.openppm.core.exceptions.BudgetAccountNotFoundException;
import es.sm2.openppm.core.model.impl.Budgetaccounts;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class BudgetaccountsLogic extends AbstractGenericLogic<Budgetaccounts, Integer> {
	
	/**
	 * Delete Budget Account
	 * @param idBudgetAccount
	 * @throws Exception
	 */
	public void deleteBudgetAccounts(Integer idBudgetAccount) throws Exception {
		if (idBudgetAccount == null || idBudgetAccount == -1) {
			throw new BudgetAccountNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			BudgetaccountsDAO budgetaccountsDAO = new BudgetaccountsDAO(session);
			Budgetaccounts budgetaccounts = budgetaccountsDAO.findById(idBudgetAccount, false);
			
			if (budgetaccounts != null) {
				if(!budgetaccounts.getExpenseses().isEmpty()){
					throw new LogicException("msg.error.delete.this_has","maintenance.budgetaccounts","expenses");
				}
				else if(!budgetaccounts.getDirectcostses().isEmpty()){
					throw new LogicException("msg.error.delete.this_has","maintenance.budgetaccounts","direct_costs");
				}				
				else{
					budgetaccountsDAO.makeTransient(budgetaccounts);
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
	}
	

	/**
	 * Return list of all budget accounts
	 * @return
	 * @throws Exception
	 */
	public List<Budgetaccounts> consBudgetaccounts () throws Exception {
		
		List<Budgetaccounts> budgetaccounts = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			BudgetaccountsDAO budgetaccountsDAO = new BudgetaccountsDAO(session);
			budgetaccounts = budgetaccountsDAO.findAll();
			
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
		return budgetaccounts;
	}
}
