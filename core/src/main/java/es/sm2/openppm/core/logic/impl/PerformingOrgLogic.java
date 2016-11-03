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
 * File: PerformingOrgLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.PerformingOrgDAO;
import es.sm2.openppm.core.exceptions.PerfOrgNotFoundException;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class PerformingOrgLogic extends AbstractGenericLogic<Performingorg, Integer> {

	/**
	 * Return perforg by id
	 * @param idPerforg
	 * @return
	 * @throws Exception 
	 */
	public Performingorg consPerforg (Integer idPerfOrg) throws Exception {
		
		Performingorg perforg = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			PerformingOrgDAO perforgDAO = new PerformingOrgDAO(session);
			if (idPerfOrg != -1) {
				perforg = perforgDAO.findById(idPerfOrg, false);
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
		return perforg;
	}
	
	/**
	 * Delete Performing Org
	 * @param idPerfOrg
	 * @throws Exception 
	 */
	public void deletePerfOrg(Integer idPerfOrg) throws Exception {
		if (idPerfOrg == null || idPerfOrg == -1) {
			throw new PerfOrgNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			PerformingOrgDAO perforgDAO = new PerformingOrgDAO(session);
			Performingorg perforg = perforgDAO.findById(idPerfOrg, false);
			
			if (!perforg.getEmployees().isEmpty()) {
				throw new LogicException("msg.error.delete.this_has","perf_organization","maintenance.resource");
			}
			else if (!perforg.getProjects().isEmpty()) {
				throw new LogicException("msg.error.delete.this_has","perf_organization","menu.projects");
			}else{
				perforgDAO.makeTransient(perforg);
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
	 * Search Performingorg by Contact
	 * @param contact
	 * @return
	 * @throws Exception 
	 */
	public List<Performingorg> consByContact(Contact contact) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		List<Performingorg> orgs = null;
		
		try{
			tx = session.beginTransaction();
			
			PerformingOrgDAO perforgDAO = new PerformingOrgDAO(session);
			orgs = perforgDAO.searchByContact(contact);
			
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
		return orgs;
	}

	/**
	 * Find other POs by company
	 * @param company
	 * @param performingorg
	 * @return
	 * @throws Exception 
	 */
	public List<Performingorg> findOtherPOs(Company company, Integer idPerfOrg) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		List<Performingorg> orgs = null;
		
		try{
			tx = session.beginTransaction();
			
			PerformingOrgDAO perforgDAO = new PerformingOrgDAO(session);
			orgs = perforgDAO.findOtherPOs(company, idPerfOrg);
			
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
		return orgs;
	}
}
