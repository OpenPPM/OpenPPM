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
 * File: OperationDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.Date;
import java.util.List;

import es.sm2.openppm.core.model.search.OperationSearch;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Operationaccount;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class OperationDAO extends AbstractGenericHibernateDAO<Operation, Integer> {

	public OperationDAO(Session session) {
		super(session);
	}
	
	/**
	 * Return Operations with operation account
	 * @param operation
	 * @return
	 */
	public Operation findByIdOperation(Operation operation) {
		
		Operation ope = null;
		
		if (operation.getIdOperation() != null) {
			Criteria crit = getSession().createCriteria(getPersistentClass());
			crit.setFetchMode("operationaccount", FetchMode.JOIN);
			crit.add(Restrictions.eq("idOperation", operation.getIdOperation()));
			ope = (Operation) crit.uniqueResult();
		}
		return ope;
	}

	/**
	 * Find all of operations by company
     *
	 * @param company
	 * @param isSeller
     * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Operation> findAllByCompany(Company company, boolean isSeller) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.addOrder(Order.asc(Operation.OPERATIONNAME))
			.addOrder(Order.asc(Operation.IDOPERATION));

        crit.createCriteria(Operation.OPERATIONACCOUNT)
            .add(Restrictions.eq(Operationaccount.COMPANY, company));

        //Exclude externals operations if user is seller
        if (isSeller) {
            crit.add(Restrictions.eq(Operation.EXCLUDEEXTERNALS, false));
        }

		return crit.list();
	}
	
	/**
	 * Find operations available for Resource Manager
	 * @param company
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Operation> findForRM(Company company) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Operation.AVAILABLEFORMANAGER, true))
			.addOrder(Order.asc(Operation.OPERATIONNAME))
			.createCriteria(Operation.OPERATIONACCOUNT)
				.add(Restrictions.eq(Operationaccount.COMPANY, company));
		
		return crit.list();
	}

	/**
	 * Find operations by employees and dates and status app3
	 * 
	 * @param employees
	 * @param since
	 * @param until
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Operation> findByEmployeesAndDatesAndApproved(
			List<Employee> employees, Date since, Date until, Company company) {
		
		List<Operation> operations = null;
		
		if (ValidateUtil.isNotNull(employees)) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			// Operation 
			//
			crit.addOrder(Order.asc(Operation.OPERATIONNAME))
			.createCriteria(Operation.OPERATIONACCOUNT)
			.add(Restrictions.eq(Operationaccount.COMPANY, company));
			
			// Timesheet 
			//
			Criteria sheets = crit.createCriteria(Employee.TIMESHEETS)
					.add(Restrictions.eq(Timesheet.STATUS, Constants.TIMESTATUS_APP3))
					.add(Restrictions.isNotNull(Timesheet.OPERATION));
			
			if (!employees.isEmpty()) {
				sheets.add(Restrictions.in(Timesheet.EMPLOYEE, employees));
			}
			
			if (since != null && until != null) {
				sheets.add(Restrictions.disjunction()
						.add(Restrictions.between(Timesheet.INITDATE, since, until))
						.add(Restrictions.between(Timesheet.ENDDATE, since, until))
						.add(Restrictions.and(
								Restrictions.le(Timesheet.INITDATE, since),
								Restrictions.ge(Timesheet.ENDDATE, until)
								)
								)
						);
			}
			
			operations = crit.list();
		}
		
		return operations;
	}

    /**
     * Find by search
     *
     * @param search
     * @return
     */
    public List<Operation> find(OperationSearch search) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
                .addOrder(Order.asc(Operation.OPERATIONNAME))
                .addOrder(Order.asc(Operation.IDOPERATION));

        crit.createCriteria(Operation.OPERATIONACCOUNT)
                .add(Restrictions.eq(Operationaccount.COMPANY, search.getCompany()));

        if (search.isAvailableForManager()) {
            crit.add(Restrictions.eq(Operation.AVAILABLEFORMANAGER, Boolean.TRUE));
        }

		if (search.isAvailableForApprove()) {
            crit.add(Restrictions.eq(Operation.AVAILABLE_FOR_APPROVE, Boolean.TRUE));
        }

        return crit.list();
    }
}
