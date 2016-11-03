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
 * File: OperationLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.CompanyDAO;
import es.sm2.openppm.core.dao.OperationDAO;
import es.sm2.openppm.core.exceptions.OperationNotFoundException;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.search.OperationSearch;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OperationLogic extends AbstractGenericLogic<Operation, Integer> {
	
	/**
	 * Delete Operation
	 * @param idOperation
	 * @throws Exception 
	 */
	public void deleteOperation(Integer idOperation) throws Exception {
		if (idOperation == null || idOperation == -1) {
			throw new OperationNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			OperationDAO operationDAO = new OperationDAO(session);
			Operation operation = operationDAO.findById(idOperation, false);
			
			if (operation != null) {
				if (!operation.getExpensesheets().isEmpty()) {
					throw new LogicException("msg.error.delete.this_has","maintenance.operation","expenses.sheet");
				}
				else if(!operation.getTimesheets().isEmpty()){
					throw new LogicException("msg.error.delete.this_has","maintenance.operation","timesheet");
				}
				else {
					operationDAO.makeTransient(operation);
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
	 * Return operation by id
	 * @param idOperation
	 * @return
	 * @throws Exception 
	 */
	public Operation consOperation(Integer idOperation) throws Exception {
		Operation operation = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {	
			tx = session.beginTransaction();
			
			OperationDAO operationDAO = new OperationDAO(session);
			if (idOperation != -1) {
				operation = operationDAO.findByIdOperation(new Operation(idOperation));
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
		return operation;
	}
	
	/**
	 * Find Operations by Company
	 * @param company
	 * @return
	 * @throws Exception 
	 */
	public List<Operation> findByAllCompany(Company company) throws Exception {
		
		List<Operation> list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			OperationDAO operationDAO = new OperationDAO(session);
			list = operationDAO.findAllByCompany(company, false);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * Find operations available for Resource Manager
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Operation> findForRM(Employee user) throws Exception {
		
		List<Operation> operations = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			OperationDAO operationDAO	= new OperationDAO(session);
			CompanyDAO companyDAO		= new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(user);
			
			operations = operationDAO.findForRM(company);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return operations;
	}

    /**
     * Find by company and not exclude seller resources
     *
     * @param company
     * @param user
     * @return
     * @throws Exception
     */
    public List<Operation> findByCompanyAndUser(Company company, Employee user) throws Exception {

        List<Operation> list = null;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {
            tx = session.beginTransaction();

            boolean isSeller = false;

            // Check resource is a seller
            //
            if (user != null && user.getResourceprofiles() != null &&
                    Constants.ROLE_RESOURCE == user.getResourceprofiles().getIdProfile() &&
                    user.getSeller() != null) {

                isSeller = true;
            }

            // Declare DAO
            OperationDAO operationDAO = new OperationDAO(session);

            // DAO
            list = operationDAO.findAllByCompany(company, isSeller);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return list;
    }

    /**
     * Find by search
     *
     * @param search
     * @return
     */
    public List<Operation> find(OperationSearch search) throws Exception {

        List<Operation> list = null;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {
            tx = session.beginTransaction();

            // Declare DAO
            OperationDAO operationDAO = new OperationDAO(session);

            // DAO
            list = operationDAO.find(search);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return list;
    }
}
