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
 * File: TimelineDAO.java
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
import es.sm2.openppm.core.model.impl.Timeline;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Timeline
 * @see es.sm2.openppm.core.dao.Timeline
 * @author Hibernate Generator by Javier Hernandez
 */
public class TimelineDAO extends AbstractGenericHibernateDAO<Timeline, Integer> {


	public TimelineDAO(Session session) {
		super(session);
	}
	
	/**
	 * Find by project
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Timeline> findByProject(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.addOrder(Order.asc(Timeline.IDTIMELINE))			
			.add(Restrictions.eq(Timeline.PROJECT, project));
		
		return crit.list();
	}

}

