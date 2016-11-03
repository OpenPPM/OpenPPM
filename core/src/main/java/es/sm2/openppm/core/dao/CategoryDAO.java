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
 * File: CategoryDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import es.sm2.openppm.core.model.impl.Checklist;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Wbsnode;
import org.hibernate.Criteria;
import org.hibernate.Session;

import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;


/**
 * DAO object for domain model class Category
 * @see es.sm2.openppm.core.dao.Category
 * @author Hibernate Generator by Javier Hernandez
 */
public class CategoryDAO extends AbstractGenericHibernateDAO<Category, Integer> {


	public CategoryDAO(Session session) {
		super(session);
	}

    /**
     * Find categories by company and enabled, include actual category
     *
     * @param company
     * @param category
     * @return
     */
    public List<Category> findByCompanyAndEnabled(Company company, Category category) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
                // Filter by company
                .add(Restrictions.eq(Category.COMPANY, company));

        if (category != null && category.getIdCategory() != null) {

            // Filter by enable or this category
            crit.add(Restrictions.disjunction()
                    .add(Restrictions.eq(Category.ENABLE, true))
                    .add(Restrictions.eq(Category.IDCATEGORY, category.getIdCategory()))
            );
        }
        else {
            // Filter by enable
            crit.add(Restrictions.eq(Category.ENABLE, true));
        }

        // Order
        crit.addOrder(Order.asc(Category.NAME));

        return crit.list();
    }
}

