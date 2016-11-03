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
 * File: OperationaccountLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.OperationaccountDAO;
import es.sm2.openppm.core.exceptions.OperationaccountNotFoundException;
import es.sm2.openppm.core.model.impl.Operationaccount;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class OperationaccountLogic extends AbstractGenericLogic<Operationaccount, Integer> {
	
	/**
	 * Delete Operationaccount
	 * @param idOperationAccount
	 * @throws Exception 
	 */
	public void deleteOperationAccount(Integer idOperationAccount) throws Exception {
		if (idOperationAccount == null || idOperationAccount == -1) {
			throw new OperationaccountNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			OperationaccountDAO operationaccountDAO = new OperationaccountDAO(session);
			Operationaccount operationaccount = operationaccountDAO.findById(idOperationAccount, false);
			
			if (operationaccount != null) {
				if (!operationaccount.getOperations().isEmpty()) {
					throw new LogicException("msg.error.delete.this_has","maintenance.operation.op_account","operation");
				}
				else {
					operationaccountDAO.makeTransient(operationaccount);
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
}
