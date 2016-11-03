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
 * File: ProjectCostsLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.DirectCostsDAO;
import es.sm2.openppm.core.dao.ExpensesDAO;
import es.sm2.openppm.core.dao.ProjectCostsDAO;
import es.sm2.openppm.core.exceptions.CostNotFoundException;
import es.sm2.openppm.core.model.impl.Directcosts;
import es.sm2.openppm.core.model.impl.Expenses;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcosts;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class ProjectCostsLogic extends AbstractGenericLogic<Projectcosts, Integer> {

	/**
	 * Save project cost and return this
	 * @param costs
	 * @return
	 * @throws Exception 
	 */
	public Projectcosts saveCosts (Projectcosts costs) throws Exception {
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Projectcosts projCosts = null;
		try {
			tx = session.beginTransaction();
			
			ProjectCostsDAO costsDAO = new ProjectCostsDAO(session);
			projCosts = costsDAO.saveProjectCost(costs);
			
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
		return projCosts;
	}	
	
	
	/**
	 * Delete cost by id and type
	 * @param idCost
	 * @param costType
	 * @throws Exception 
	 */
	public void deleteCost (Integer idCost, Integer costType) throws Exception {
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			if (idCost == -1 || costType == -1) {
				throw new CostNotFoundException();
			}
			if (costType == Constants.COST_TYPE_DIRECT) {
				DirectCostsDAO directDAO = new DirectCostsDAO(session);
				Directcosts cost = directDAO.findById(idCost, false);
				if (cost != null) {
					directDAO.makeTransient(cost);
				}
			}
			else if (costType == Constants.COST_TYPE_EXPENSE) {
				ExpensesDAO expensesDAO = new ExpensesDAO(session);
				Expenses cost = expensesDAO.findById(idCost, false);
				if (cost != null) {
					expensesDAO.makeTransient(cost);
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
	 * List costs of the project
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public List<Projectcosts> consCosts (Project project) throws Exception {
		
		List<Projectcosts> costs = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ProjectCostsDAO costsDAO = new ProjectCostsDAO(session);
			
			if (project.getIdProject() != -1) {
				costs = costsDAO.findByProject(project);
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
		return costs;
	}
}
