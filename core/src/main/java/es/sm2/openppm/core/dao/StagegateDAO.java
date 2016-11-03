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
 * File: StagegateDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Stagegate;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Stagegate
 * @see es.sm2.openppm.core.dao.Stagegate
 * @author Hibernate Generator by Javier Hernandez
 */
public class StagegateDAO extends AbstractGenericHibernateDAO<Stagegate, Integer> {


	public StagegateDAO(Session session) {
		super(session);
	}

	/**
	 * Search stage gates for projects according to order stage gate name
	 * 
	 * @param projects
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Stagegate> findByProjects(Integer[] ids) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.addOrder(Order.asc(Stagegate.NAME))
				.createCriteria(Stagegate.PROJECTS)
					.add(Restrictions.in(Project.IDPROJECT, ids));
		
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return crit.list();
	}

}

