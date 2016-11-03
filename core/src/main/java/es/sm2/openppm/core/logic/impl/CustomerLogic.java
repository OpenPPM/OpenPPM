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
 * File: CustomerLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.CompanyDAO;
import es.sm2.openppm.core.dao.CustomerDAO;
import es.sm2.openppm.core.exceptions.CustomerNotFoundException;
import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class CustomerLogic extends AbstractGenericLogic<Customer, Integer> {

	/**
	 * Search Customer by Company
	 * @param company
	 * @return
	 * @throws Exception
	 */
	public List<Customer> searchByCompany(Company company) throws Exception {
		
		List<Customer> customers = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			CustomerDAO customerDAO = new CustomerDAO(session);
			customers = customerDAO.searchByCompany(company);
			
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
		return customers;
	}	
	
	/**
	 * Delete Customer
	 * @param customer
	 * @throws Exception
	 */
	public void deleteCustomer(Customer customer) throws Exception {
		if (customer == null) {
			throw new CustomerNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			CustomerDAO customerDAO = new CustomerDAO(session);
			customer = customerDAO.findById(customer.getIdCustomer(), false);
			
			if(!customer.getProjects().isEmpty()){
				throw new LogicException("msg.error.delete.this_has","customer","project");
			}
			else{
				customerDAO.makeTransient(customer);
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
	 * Find All and order by name
	 * @return
	 * @throws Exception 
	 */
	public List<Customer> findAllOrder() throws Exception {
		List<Customer> customers = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			CustomerDAO customerDAO = new CustomerDAO(session);
			customers = customerDAO.findAllOrder();
			
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
		return customers;
	}

	/**
	 * Find customer by company of user
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Customer> findByCompany(Employee user) throws Exception {
		List<Customer> list = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			CustomerDAO customerDAO = new CustomerDAO(session);
			CompanyDAO companyDAO	= new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(user);
			
			list = customerDAO.findByCompany(company);

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
		return list;
	}

	/**
	 * @param idCustomer
	 * @param lock
	 * @return
	 * @throws Exception
	 */
	public Customer findById(Integer idCustomer, boolean lock) throws Exception {
		if (idCustomer == null || idCustomer == -1) {
     		throw new NoDataFoundException();
		}
		Customer customer = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			CustomerDAO customerDAO = new CustomerDAO(session);
			customer = customerDAO.findById(idCustomer, lock);
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
		return customer;
	}
	
	
}
