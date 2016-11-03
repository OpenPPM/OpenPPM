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
 * File: FundingsourceLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.CompanyDAO;
import es.sm2.openppm.core.dao.FundingsourceDAO;
import es.sm2.openppm.core.exceptions.FundingsourceNotFoundException;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Fundingsource
 * @see es.sm2.openppm.logic.Fundingsource
 * @author Hibernate Generator by Javier Hernandez
 */
public class FundingsourceLogic extends AbstractGenericLogic<Fundingsource, Integer>{
	
	/**
	 * Delete Fundingsource
	 * @param fundingsource
	 * @throws Exception 
	 */
	public void deleteFundingsource(Fundingsource fundingsource) throws Exception {
		if (fundingsource == null) {
			throw new FundingsourceNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			FundingsourceDAO fundingsourceDAO = new FundingsourceDAO(session);
			fundingsource = fundingsourceDAO.findById(fundingsource.getIdFundingSource(), false);
			
			if(!fundingsource.getProjectfundingsources().isEmpty()){
				throw new LogicException("msg.error.delete.this_has","maintenance.fundingsource","project");
			}
			else{
				fundingsourceDAO.makeTransient(fundingsource);
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
	 * 
	 * @param fundingsource
	 * @throws Exception
	 */
	public List<Fundingsource> findByCompany(Employee user) throws Exception {

		List<Fundingsource> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			FundingsourceDAO fundingsourceDAO = new FundingsourceDAO(session);
			CompanyDAO companyDAO = new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(user);
			
			list = fundingsourceDAO.findByRelation(Fundingsource.COMPANY, company, Fundingsource.NAME, Order.ASC);
			
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

