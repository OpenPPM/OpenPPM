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
 * File: CategoryLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.CategoryDAO;
import es.sm2.openppm.core.exceptions.CategoryNotFoundException;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;


/**
 * Logic object for domain model class Category
 * @see es.sm2.openppm.logic.Category
 * @author Hibernate Generator by Javier Hernandez
 */
public class CategoryLogic extends AbstractGenericLogic<Category, Integer>{

	/**
	 * Delete Category
	 * @param category
	 * @throws Exception 
	 */
	public void deleteCategory(Category category) throws Exception {
		if (category == null) {
			throw new CategoryNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			CategoryDAO categoryDAO = new CategoryDAO(session);
			category = categoryDAO.findById(category.getIdCategory(), false);
			
			if (category != null) {
				if(!category.getProjects().isEmpty()){
					throw new LogicException("msg.error.delete.this_has","category","project");
				}
				else{
					categoryDAO.makeTransient(category);
				}
			}
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
	}


    /**
     * Find categories by company and enabled, include actual category
     *
     * @param company
     * @param category
     * @return
     */
    public List<Category> findByCompanyAndEnabled(Company company, Category category) throws Exception {

        List<Category> categories = new ArrayList<Category>();

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            CategoryDAO categoryDAO = new CategoryDAO(session);

            categories = categoryDAO.findByCompanyAndEnabled(company, category);

            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        finally {
            SessionFactoryUtil.getInstance().close();
        }

        return categories;
    }
}

