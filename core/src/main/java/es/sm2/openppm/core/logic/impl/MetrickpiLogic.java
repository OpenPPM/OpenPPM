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
 * File: MetrickpiLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.CompanyDAO;
import es.sm2.openppm.core.dao.MetrickpiDAO;
import es.sm2.openppm.core.exceptions.MetrickpiNotFoundException;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Metrickpi;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Metrickpi
 * @see es.sm2.openppm.logic.Metrickpi
 * @author Hibernate Generator by Javier Hernandez
 */
public class MetrickpiLogic extends AbstractGenericLogic<Metrickpi, Integer>{
	
	/**
	 * Delete Metrickpi
	 * @param metrickpi
	 * @throws Exception 
	 */
	public void deleteMetrickpi(Metrickpi metrickpi) throws Exception {
		if (metrickpi == null) {
			throw new MetrickpiNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			MetrickpiDAO metrickpiDAO = new MetrickpiDAO(session);
			metrickpi = metrickpiDAO.findById(metrickpi.getIdMetricKpi(), false);
			
			if (!metrickpi.getProjectkpis().isEmpty()) {
				throw new LogicException("msg.error.delete.this_has","metric","kpi_project");
			}else{
				metrickpiDAO.makeTransient(metrickpi);
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
	 * Find by company of user
	 * @param user
	 * @param joins 
	 * @return
	 * @throws Exception 
	 */
	public List<Metrickpi> findByCompany(Employee user, List<String> joins) throws Exception {

		List<Metrickpi> list = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			MetrickpiDAO metrickpiDAO	= new MetrickpiDAO(session);
			CompanyDAO companyDAO		= new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(user);
			list = metrickpiDAO.findByCompany(company, joins);

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
     * Search metrickpis
     *
     * @param name
     * @param idBSCDimension
     * @param type
     * @param project
     * @param company
     * @param joins
     * @return
     * @throws Exception
     */
	public List<Metrickpi> searchMetricKpis(String name, Integer idBSCDimension, String type, Project project, Company company, List<String> joins) throws Exception {
		
		List<Metrickpi> metricKpis = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			MetrickpiDAO metrickpiDAO = new MetrickpiDAO(session);
			metricKpis = metrickpiDAO.searchByFilter(name, idBSCDimension, type, project, company, joins);
			
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
		return metricKpis;
	}
}

