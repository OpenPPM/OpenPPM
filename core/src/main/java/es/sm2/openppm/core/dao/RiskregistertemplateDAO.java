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
 * File: RiskregistertemplateDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Riskregistertemplate;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Riskregistertemplate
 * @see es.sm2.openppm.core.dao.Riskregistertemplate
 * @author Hibernate Generator by Javier Hernandez
 */
public class RiskregistertemplateDAO extends AbstractGenericHibernateDAO<Riskregistertemplate, Integer> {


	public RiskregistertemplateDAO(Session session) {
		super(session);
	}

	@SuppressWarnings("unchecked")
	public List<Riskregistertemplate> find(String search, Company company) {
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.disjunction()
						.add(Restrictions.ilike(Riskregistertemplate.RISKNAME,"%"+search+"%"))
						.add(Restrictions.ilike(Riskregistertemplate.RISKCODE,"%"+search+"%"))
					)
				.add(Restrictions.eq(Performingorg.COMPANY, company));	
			
			return crit.list();
	}

}

