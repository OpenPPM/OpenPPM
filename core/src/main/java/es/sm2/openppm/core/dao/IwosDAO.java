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
 * File: IwosDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:44
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import es.sm2.openppm.core.model.impl.Iwo;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class IwosDAO extends AbstractGenericHibernateDAO<Iwo, Integer> {

	public IwosDAO(Session session) {
		super(session);
	}

	/**
	 * Return Project iwos
	 * @param proj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Iwo> findByProject(Project proj) {
		List<Iwo> iwos = null;
		if (proj != null) {
			Query query = getSession().createQuery(
					"select iwos " +
					"from Iwo as iwos " + 
					"join fetch iwos.projectcosts as costs " +
					"join costs.project as project " +
					"where project.idProject = :idProject");
			query.setInteger("idProject", proj.getIdProject());
			iwos = query.list();
		}
		return iwos;
	}
	
	/**
	 * Return total Iwos cost of project
	 * @param proj
	 * @return
	 */
	public double getTotal(Project proj) {
		double total = 0;
		if (proj != null) {
			Query query = getSession().createQuery(
					"select sum(iwos.planned) as total " +
					"from Iwo as iwos " + 
					"join iwos.projectcosts as costs " +
					"join costs.project as project " +
					"where project.idProject = :idProject");
			query.setInteger("idProject", proj.getIdProject());
			if (query.uniqueResult() != null) {
				total = (Double) query.uniqueResult(); 
			}
		}
		return  total;
	}
	
	
	/**
	 * Return actual total Iwos cost of project to control it
	 * @param proj
	 * @return
	 */
	public double getActualTotal(Project proj) {
		double total = 0;
		if (proj != null) {
			Query query = getSession().createQuery(
					"select sum(iwos.actual) as total " +
					"from Iwo as iwos " + 
					"join iwos.projectcosts as costs " +
					"join costs.project as project " +
					"where project.idProject = :idProject");
			query.setInteger("idProject", proj.getIdProject());
			if (query.uniqueResult() != null) {
				total = (Double) query.uniqueResult(); 
			}
		}
		return  total;
	}

}
