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
 * File: ClosurecheckprojectLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.ClosurecheckprojectDAO;
import es.sm2.openppm.core.dao.DocumentprojectDAO;
import es.sm2.openppm.core.model.impl.Closurecheck;
import es.sm2.openppm.core.model.impl.Closurecheckproject;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;



/**
 * Logic object for domain model class Closurecheckproject
 * @see es.sm2.openppm.logic.Closurecheckproject
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class ClosurecheckprojectLogic extends AbstractGenericLogic<Closurecheckproject, Integer> {

	/**
	 * Save all closure check project
	 * 
	 * @param ids
	 * @param checks
	 * @throws Exception 
	 */
	public void saveAll(Integer[] ids, boolean[] checks) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			// Declare DAO
			ClosurecheckprojectDAO closurecheckprojectDAO = new ClosurecheckprojectDAO(session);
			
			for (int i=0 ; i < ids.length; i++) {
				
				Closurecheckproject closurecheckproject = new Closurecheckproject();
				
				// DAO get object
				closurecheckproject = closurecheckprojectDAO.findById(ids[i]);
				
				// Set data
				closurecheckproject.setRealized(checks[i]);
				
				// DAO update
				closurecheckprojectDAO.makePersistent(closurecheckproject);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

	/**
	 * There closure check for project
	 * 
	 * @param closurecheck
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public boolean hasClosureCheck(Closurecheck closurecheck, Project project) throws Exception {
		
		boolean exists = false;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			// Declare DAO
			ClosurecheckprojectDAO closurecheckprojectDAO = new ClosurecheckprojectDAO(session);
			
			// DAO
			exists = closurecheckprojectDAO.hasClosureCheck(closurecheck, project);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return exists;
	}

	/**
	 * Delete closure check project and document associated
	 * 
	 * @param closurecheckproject
	 * @throws Exception 
	 */
	public void deleteEntityAndDocument(Closurecheckproject closurecheckproject) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			if (closurecheckproject != null) {
				
				// Declare DAO
				ClosurecheckprojectDAO closurecheckprojectDAO 	= new ClosurecheckprojectDAO(session);
				DocumentprojectDAO documentprojectDAO 			= new DocumentprojectDAO(session);
				
				// DAOs
				//
				closurecheckprojectDAO.makeTransient(closurecheckprojectDAO.findById(closurecheckproject.getIdClosureCheckProject()));
				
				Documentproject documentproject = closurecheckproject.getDocumentproject();
				
				if (documentproject != null && documentproject.getIdDocumentProject() != -1) {
					documentprojectDAO.makeTransient(documentprojectDAO.findById(documentproject.getIdDocumentProject()));
				}
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

	/**
	 * Create news closurecheckproject
	 * 
	 * @param closureChecks
	 * @param project
	 * @throws Exception 
	 */
	public void createNews(List<Closurecheck> closureChecks, Project project) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			if (ValidateUtil.isNotNull(closureChecks) && project != null) {
				
				// Declare DAO
				ClosurecheckprojectDAO closurecheckprojectDAO = new ClosurecheckprojectDAO(session);
				
				for (Closurecheck closurecheck : closureChecks) {
					
					// if not has in closurechechproject then created
					if (!closurecheckprojectDAO.hasClosureCheck(closurecheck, project)) { 
						
						Closurecheckproject closurecheckproject = new Closurecheckproject();
						
						closurecheckproject.setProject(project);
						closurecheckproject.setClosurecheck(closurecheck);
						closurecheckproject.setRealized(false);
						
						// DAO save
						closurecheckprojectDAO.makePersistent(closurecheckproject);
					}
				}
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

    /**
     * Save document
     *
     * @param idClosureCheckProject
     * @param doc
     */
    public void saveDocument(int idClosureCheckProject, Documentproject doc) throws Exception {

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            // DAO
            ClosurecheckprojectDAO closurecheckprojectDAO = new ClosurecheckprojectDAO(session);

            // Find data
            Closurecheckproject closurecheckproject = closurecheckprojectDAO.findById(idClosureCheckProject);

            // Set document
            closurecheckproject.setDocumentproject(doc);

            // Update closure check project
            closurecheckprojectDAO.makePersistent(closurecheckproject);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }
}

