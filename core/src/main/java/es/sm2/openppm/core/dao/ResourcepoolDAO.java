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
 * File: ResourcepoolDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Managepool;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Resourcepool
 * @see es.sm2.openppm.core.dao.Resourcepool
 * @author Hibernate Generator by Javier Hernandez
 */
public class ResourcepoolDAO extends AbstractGenericHibernateDAO<Resourcepool, Integer> {


	public ResourcepoolDAO(Session session) {
		super(session);
	}

	/**
	 * Resource pools by resource manager
	 * 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Resourcepool> findByResourceManager(Employee user) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		crit.createCriteria(Resourcepool.MANAGEPOOLS)
			.add(Restrictions.eq(Managepool.EMPLOYEE, user));
		
		crit.addOrder(Order.asc(Resourcepool.NAME));
			
		return crit.list();
	}

	/**
	 * Resource pools by resource managers
	 * 
	 * @param resourceManagers
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Resourcepool> findByResourceManagers(List<Employee> resourceManagers) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		crit.createCriteria(Resourcepool.MANAGEPOOLS)
			.add(Restrictions.in(Managepool.EMPLOYEE, resourceManagers));
		
		crit.addOrder(Order.asc(Resourcepool.NAME));
			
		return crit.list();
	}

	/**
	 * Find resouce pools by project manager
	 * 
	 * @param projectManager
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Resourcepool> findByProjectManager(Employee projectManager) {
		
		String queryString =	"SELECT distinct rp " +
								"FROM Resourcepool rp " +
								"JOIN rp.employees erp "+
								"WHERE erp.idEmployee in ( " +
									"SELECT e.idEmployee "+
									"FROM erp.teammembers t "+
									"JOIN t.employee e "+
									"JOIN t.projectactivity pa "+
									"JOIN pa.project pt "+
									"WHERE pt.idProject in ( "+
										"SELECT p.idProject "+
										"FROM pa.project p "+
										"WHERE p.employeeByProjectManager = :idProjectManager "+
									") "+
									"GROUP BY e.idEmployee "+
								")";
						
		Query query = getSession().createQuery(queryString);
		
		query.setInteger("idProjectManager", projectManager.getIdEmployee());
			
		return query.list();
	}
}

