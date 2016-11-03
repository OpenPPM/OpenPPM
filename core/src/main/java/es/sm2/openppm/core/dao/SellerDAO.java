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
 * File: SellerDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:44
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Seller
 * @see es.sm2.openppm.core.dao.Seller
 * @author Hibernate Generator by Javier Hernandez
 */
public class SellerDAO extends AbstractGenericHibernateDAO<Seller, Integer> {


	public SellerDAO(Session session) {
		super(session);
	}
	
	/**
	 * Return all Seller ordered
	 * 
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Seller> findAllOrdered(Order... order) {		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Order o : order) {
			crit.addOrder(o);
		}		
		return crit.list();
	}
	
	/**
	 *  Sellers by projects
     *
	 * @param projects
	 * @return
	 */
	public List<Seller> findByProcurement(Project...projects) {

		Criteria crit = getSession().createCriteria(getPersistentClass());

		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		crit.createCriteria(Seller.ACTIVITYSELLERS)
				.createCriteria(Activityseller.PROJECTACTIVITY)
					.add(Restrictions.in(Projectactivity.PROJECT, projects));
		
        crit.addOrder(Order.asc(Seller.NAME));

		return crit.list();
	}

	/**
	 * Search by company
	 * @param company
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Seller> searchByCompany(Company company) {

		Criteria crit = getSession().createCriteria(getPersistentClass())
			.addOrder(Order.asc(Seller.NAME))
			.add(Restrictions.eq(Seller.COMPANY, company));
		
		return crit.list();
	}

}

