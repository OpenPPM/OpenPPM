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
 * File: CustomertypeLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.CompanyDAO;
import es.sm2.openppm.core.dao.CustomertypeDAO;
import es.sm2.openppm.core.exceptions.CustomertypeNotFoundException;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Customertype;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Customertype
 * @see es.sm2.openppm.logic.Customertype
 * @author Hibernate Generator by Javier Hernandez
 */
public class CustomertypeLogic extends AbstractGenericLogic<Customertype, Integer>{

	/**
	 * Delete Customertype
	 * @param customertype
	 * @throws Exception 
	 */
	public void deleteCustomertype(Customertype customertype) throws Exception {
		if (customertype == null) {
			throw new CustomertypeNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			CustomertypeDAO customertypeDAO = new CustomertypeDAO(session);
			customertype = customertypeDAO.findById(customertype.getIdCustomerType(), false);
			
			if (!customertype.getCustomers().isEmpty()) {
				
				throw new LogicException("msg.error.delete.this_has","customer_type","customer");
			}
			else{
				customertypeDAO.makeTransient(customertype);
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
	 * Find customer types by company of user
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Customertype> findByCompany(Employee user) throws Exception {
		List<Customertype> list = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			CustomertypeDAO customertypeDAO = new CustomertypeDAO(session);
			CompanyDAO companyDAO			= new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(user);
			
			list = customertypeDAO.findByCompany(company);

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
}

