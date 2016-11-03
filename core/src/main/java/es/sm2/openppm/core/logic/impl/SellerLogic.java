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
 * File: SellerLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import es.sm2.openppm.core.listener.impl.project.DeleteSellerAbstractListener;
import es.sm2.openppm.core.listener.ProcessListener;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.dao.SellerDAO;
import es.sm2.openppm.core.exceptions.SellerNotFoundException;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Seller
 * @see es.sm2.openppm.logic.Seller
 * @author Hibernate Generator by Javier Hernandez
 */
public class SellerLogic extends AbstractGenericLogic<Seller, Integer>{

	
	/**
	 * Return all Seller ordered
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public List<Seller> findAllOrdered(Order... order) throws Exception {
		List<Seller> list = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			SellerDAO sellerDAO = new SellerDAO(session);
			list = sellerDAO.findAllOrdered(order);

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
		return list;
	}
	
	/**
	 * Return sellers with a Statement of Work
	 * 
	 * @param projects
	 * @return
	 * @throws Exception
	 */
	public List<Seller> findByProcurement(Project...projects) throws Exception {
		List<Seller> list = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			SellerDAO sellerDAO = new SellerDAO(session);
			list = sellerDAO.findByProcurement(projects);

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
		return list;
	}
	
	/**
	 * Delete Seller
	 * @param seller
	 * @throws Exception 
	 */
	public void deleteSeller(Seller seller) throws Exception {

		if (seller == null) {
			throw new SellerNotFoundException();
		}

		List<String> projectNames = null;

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

            // Declare DAOs
			ProjectDAO projectDAO   = new ProjectDAO(session);
            SellerDAO sellerDAO     = new SellerDAO(session);

			seller          = sellerDAO.findById(seller.getIdSeller(), false);
			projectNames    = projectDAO.findBySeller(seller);

            session.close();
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

        // Validations
        //
        if (projectNames.size() > 0) {

            //TODO jordi.ripoll - 04/02/2015 - esto no esta bien se tiene que volver a hacer, nada de html en java
            String projects = "<br /><ul><li>";

            for (String s : projectNames) {

                if(!projects.equals("<br /><ul><li>")) {
                    projects += "</li><li>";
                }

                projects += s;
            }

            projects += "</li></ul>";

            throw new LogicException("msg.error.delete.seller", seller.getName(), projects);
        }

		// Listener to control deleting a supplier in Rattings tab
		ProcessListener.getInstance().process(
				DeleteSellerAbstractListener.class,
				seller,
				seller,
				null);


        // Delete seller
        //
        session = SessionFactoryUtil.getInstance().getCurrentSession();

        // Declare DAOs
        SellerDAO sellerDAO = new SellerDAO(session);

        try {
            tx = session.beginTransaction();

            sellerDAO.makeTransient(seller);

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
	 * Search by company
	 * @param company
	 * @return
	 * @throws Exception 
	 */
	public List<Seller> searchByCompany(Company company) throws Exception {
		
		List<Seller> list = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			SellerDAO sellerDAO = new SellerDAO(session);
			list = sellerDAO.searchByCompany(company);

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
		return list;
	}

}

