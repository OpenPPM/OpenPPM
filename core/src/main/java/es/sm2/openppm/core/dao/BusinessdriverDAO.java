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
 * File: BusinessdriverDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Businessdriver;
import es.sm2.openppm.core.model.impl.Businessdriverset;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Businessdriver
 * @see es.sm2.openppm.core.dao.Businessdriver
 * @author Hibernate Generator by Javier Hernandez
 */
public class BusinessdriverDAO extends AbstractGenericHibernateDAO<Businessdriver, Integer> {


	public BusinessdriverDAO(Session session) {
		super(session);
	}

	/**
	 * get sum relative priorization
	 * @param businessdriverset
	 * @return
	 */
	public double getTotalPriority(Businessdriverset businessdriverset) {
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.setProjection(Projections.sum(Businessdriver.RELATIVEPRIORIZATION))
				.add(Restrictions.eq(Businessdriver.BUSINESSDRIVERSET, businessdriverset));
			
		Double total = (Double)crit.uniqueResult();
		return (total == null ? 0.0 : total);
	}

}

