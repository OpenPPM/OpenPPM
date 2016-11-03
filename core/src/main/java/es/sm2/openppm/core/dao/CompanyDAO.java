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
 * File: CompanyDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:44
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class CompanyDAO extends AbstractGenericHibernateDAO<Company, Integer> {

	public CompanyDAO(Session session) {
		super(session);
	}

	/**
	 * Search company of employee
	 * @param user
	 * @return
	 */
	public Company searchByEmployee(Employee user) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
			.createCriteria(Company.CONTACTS)
			.createCriteria(Contact.EMPLOYEES)
			.add(Restrictions.idEq(user.getIdEmployee()));
		
		return (Company) crit.uniqueResult();
	}

	/**
	 * Find active companies
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Company> findActives() {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.disjunction()
					.add(Restrictions.eq(Company.DISABLE, Boolean.FALSE))
					.add(Restrictions.isNull(Company.DISABLE))
					);
				
		return crit.list();
	}

}
