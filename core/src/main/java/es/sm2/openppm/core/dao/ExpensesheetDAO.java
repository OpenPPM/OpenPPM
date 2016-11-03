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
 * File: ExpensesheetDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Expenses;
import es.sm2.openppm.core.model.impl.Expensesheet;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Expensesheet
 * @see es.sm2.openppm.core.dao.Expensesheet
 * @author Hibernate Generator by Javier Hernandez
 */
public class ExpensesheetDAO extends AbstractGenericHibernateDAO<Expensesheet, Integer> {


	public ExpensesheetDAO(Session session) {
		super(session);
	}

	/**
	 * Find Expense Sheet by resource in month
	 * @param user
	 * @param firstMonthDay
	 * @param lastMonthDay
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Expensesheet> findByResource(Employee user, Date firstMonthDay,
			Date lastMonthDay, List<String> joins, String staus) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.add(Restrictions.eq(Expensesheet.EMPLOYEE, user))
			.add(Restrictions.between(Expensesheet.EXPENSEDATE, firstMonthDay, lastMonthDay));
		
		if (staus != null) {
			crit.add(Restrictions.eq(Expensesheet.STATUS, Constants.EXPENSE_STATUS_APP3));
		}
		
		addJoins(crit, joins);
		
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Expensesheet> findByResource(Employee employee,
			Date since, Date until, String minStatus,
			String maxStatus, Employee user, Project project,
			List<String> joins) {
		
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.add(Restrictions.between(Expensesheet.EXPENSEDATE, since, until));
		
		Criteria employeeCrit = crit.createCriteria(Expensesheet.EMPLOYEE)
			.add(Restrictions.idEq(employee.getIdEmployee()));
		
		if (minStatus != null && maxStatus != null) {
			
			crit.add(Restrictions.or(
					Restrictions.eq(Expensesheet.STATUS, minStatus),
					Restrictions.eq(Expensesheet.STATUS, maxStatus)
				));
		}
		
		if (project != null) {			
			crit.add(Restrictions.eq(Expensesheet.PROJECT, project));
		}
		else if (user != null) {
			
			employeeCrit.add(Restrictions.eq(Employee.PERFORMINGORG, user.getPerformingorg()));
		}
		
		addJoins(crit, joins);
		return crit.list();
	}

	public double getCostResource(Integer idEmployee, Integer id,
			Date since, Date until, String minStatus,
			String maxStatus, Employee user) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.sum(Expensesheet.COST))
			.add(Restrictions.or(
					Restrictions.eq(Expensesheet.STATUS, minStatus),
					Restrictions.eq(Expensesheet.STATUS, maxStatus)
					))
			.add(Restrictions.between(Expensesheet.EXPENSEDATE, since, until));
			
		Criteria employeeCrit = crit.createCriteria(Expensesheet.EMPLOYEE)
			.add(Restrictions.idEq(idEmployee));
		
		if (Constants.EXPENSE_STATUS_APP1.equals(minStatus)) {
			crit.add(Restrictions.eq(Expensesheet.PROJECT, new Project(id)));
		}
		else if (Constants.EXPENSE_STATUS_APP2.equals(minStatus)) {
			
			employeeCrit.add(Restrictions.eq(Employee.PERFORMINGORG, user.getPerformingorg()));
		}
		double cost = (Double) (crit.uniqueResult() == null?0D:crit.uniqueResult());
		
		return cost;
	}

	/**
	 * Is in status
	 * @param idEmployee
	 * @param id
	 * @param since
	 * @param until
	 * @param status
	 * @param user 
	 * @return
	 */
	public boolean isStatusResource(Integer idEmployee, Integer id,
			Date since, Date until, String status, Employee user) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Expensesheet.STATUS, status))
			.add(Restrictions.between(Expensesheet.EXPENSEDATE, since, until));
		
		Criteria employeeCrit = crit.createCriteria(Expensesheet.EMPLOYEE)
			.add(Restrictions.idEq(idEmployee));
		
		if (Constants.EXPENSE_STATUS_APP1.equals(status)) {
			crit.add(Restrictions.eq(Expensesheet.PROJECT, new Project(id)));
		}
		else if (Constants.EXPENSE_STATUS_APP2.equals(status)) {
			
			employeeCrit.add(Restrictions.eq(Employee.PERFORMINGORG, user.getPerformingorg()));
		}
		
		Integer count = (Integer) crit.uniqueResult();
		return (count != null && count > 0);
	}
	
	public double getSumExpenseSheet(Expenses expense) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.sum(Expensesheet.COST))
			.add(Restrictions.eq(Expensesheet.STATUS, Constants.EXPENSE_STATUS_APP3))
			.add(Restrictions.eq(Expensesheet.EXPENSES, expense));
		
		double cost = (Double) (crit.uniqueResult() == null ? 0D : crit.uniqueResult());
		
		return cost;
	}
}

