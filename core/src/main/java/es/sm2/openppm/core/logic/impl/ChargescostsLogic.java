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
 * File: ChargescostsLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.ChargescostsDAO;
import es.sm2.openppm.core.exceptions.ProjectNotFoundException;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Chargescosts
 * @see es.sm2.openppm.logic.Chargescosts
 * @author Hibernate Generator by Javier Hernandez
 */
public class ChargescostsLogic extends AbstractGenericLogic<Chargescosts, Integer>{
	
	/**
	 * Cons Charge Costs by project and type
	 * @param proj
	 * @param type
	 * @throws Exception
	 */
	public List<Chargescosts> consChargescostsByProject(Project proj, int type) 
	throws Exception {
		
		List<Chargescosts> list = null;
		if (proj == null) {
			throw new ProjectNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ChargescostsDAO chargescostsDAO = new ChargescostsDAO(session);
			list = chargescostsDAO.consChargescostsByProject(proj, type);
			
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
	 * 
	 * @param proj
	 * @return
	 * @throws Exception
	 */
	public double consExternalCostByProject(Project proj) 
	throws Exception {
			
		double result = 0;
		List<Chargescosts> list = null;
		if (proj == null) {
			throw new ProjectNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ChargescostsDAO chargescostsDAO = new ChargescostsDAO(session);
			list = new ArrayList<Chargescosts>();
			list.addAll(chargescostsDAO.consChargescostsByProject(proj, Constants.SELLER_CHARGE_COST));
			list.addAll(chargescostsDAO.consChargescostsByProject(proj, Constants.INFRASTRUCTURE_CHARGE_COST));
			list.addAll(chargescostsDAO.consChargescostsByProject(proj, Constants.LICENSE_CHARGE_COST));
			
			if(!list.isEmpty()) {
				for(Chargescosts cost : list) {
					result += cost.getCost();
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
		return result;
	}
}

