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
 * File: ExpensesDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Budgetaccounts;
import es.sm2.openppm.core.model.impl.Expenses;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcosts;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class ExpensesDAO extends AbstractGenericHibernateDAO<Expenses, Integer> {

	public ExpensesDAO(Session session) {
		super(session);
	}

	/**
	 * Return Total expenses of project
	 * @param proj
	 * @return
	 */
	public double getTotal(Project proj) {
		double total = 0;
		if (proj != null) {
			Query query = getSession().createQuery(
					"select sum(expenses.planned) " +
					"from Expenses as expenses " + 
					"join expenses.projectcosts as costs " +
					"join costs.project as project " +
					"where project.idProject = :idProject");
			query.setInteger("idProject", proj.getIdProject());
			if (query.uniqueResult() != null) {
				total = (Double) query.uniqueResult(); 
			}
		}
		return total;
	}

	
	/**
	 * Return Total actual expenses of project to control it
	 * @param proj
	 * @return
	 */
	public double getActualTotal(Project proj) {
		double total = 0;
		if (proj != null) {
			Query query = getSession().createQuery(
					"select sum(expenses.actual) " +
					"from Expenses as expenses " + 
					"join expenses.projectcosts as costs " +
					"join costs.project as project " +
					"where project.idProject = :idProject");
			query.setInteger("idProject", proj.getIdProject());
			if (query.uniqueResult() != null) {
				total = (Double) query.uniqueResult(); 
			}
		}
		return total;
	}
	
	/**
	 * Check if budget account is in use in project and expense
	 * @param project
	 * @param expenses
	 * @param budgetaccounts
	 * @return
	 */
	public boolean isBudgetInUse(Project project, Expenses expenses,
			Budgetaccounts budgetaccounts) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Expenses.BUDGETACCOUNTS, budgetaccounts));
		
		if (expenses.getIdExpense() != -1) {
			crit.add(Restrictions.ne(Expenses.IDEXPENSE, expenses.getIdExpense()));
		}
		
		crit.createCriteria(Expenses.PROJECTCOSTS)
			.add(Restrictions.eq(Projectcosts.PROJECT, project));
		
		Integer count = (Integer) crit.uniqueResult();
		
		return (count != null && count > 0);
	}

	/**
	 * Find by project
	 * 
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Expenses> findByProject(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		// Filter project
		crit.createCriteria(Expenses.PROJECTCOSTS)
				.add(Restrictions.eq(Projectcosts.PROJECT, project));
		
		// Order
		crit.createCriteria(Expenses.BUDGETACCOUNTS)
				.addOrder(Order.asc(Budgetaccounts.DESCRIPTION));
		
		return crit.list();
	}
}
