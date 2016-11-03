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
 * File: EmployeeoperationdateDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Employeeoperationdate;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Employeeoperationdate
 * @see es.sm2.openppm.core.dao.Employeeoperationdate
 * @author Hibernate Generator by Javier Hernandez
 */
public class EmployeeoperationdateDAO extends AbstractGenericHibernateDAO<Employeeoperationdate, Integer> {


	public EmployeeoperationdateDAO(Session session) {
		super(session);
	}

	/**
	 * Find for generate calendar
	 * @param employee
	 * @param operation
	 * @param since
	 * @param until
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Employeeoperationdate> findForCalendar(Employee employee,
			Operation operation, Date since, Date until) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Employeeoperationdate.EMPLOYEE, employee))
			.add(Restrictions.eq(Employeeoperationdate.OPERATION, operation))
			.add(Restrictions.between(Employeeoperationdate.DATEFOROPERATION, since, until));
		
		return crit.list();
	}

}

