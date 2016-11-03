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
 * File: ActivitysellerLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.ActivitysellerDAO;
import es.sm2.openppm.core.exceptions.ProjectNotFoundException;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.utils.Info;
import es.sm2.openppm.utils.StringPool.InfoType;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Activityseller
 * @see es.sm2.openppm.core.model.impl.Activityseller
 * @author Hibernate Generator by Javier Hernandez
 */
public class ActivitysellerLogic extends AbstractGenericLogic<Activityseller, Integer> {

	public ActivitysellerLogic () {
		super();
	}
	
	public ActivitysellerLogic (ResourceBundle bundle) {
		super(bundle);
	}
	
	public final static Logger LOGGER = Logger.getLogger(ActivitysellerLogic.class);
	
	/**
	 * Get Activity sellers from Project
	 *
     * @param joins
     * @param projects
	 * @return
	 * @throws Exception
	 */
	public List<Activityseller> consActivitySellerByProject(List<String> joins, Project...projects) throws Exception {
		if (projects == null) {
			throw new ProjectNotFoundException();
		}
		
		List<Activityseller> listActSeller = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ActivitysellerDAO activitysellerDAO = new ActivitysellerDAO(session);
			listActSeller = activitysellerDAO.consActivitySellerByProject(joins, projects);
			
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
		return listActSeller;
	}

	/**
	 * ActivitySeller has associated projects
	 * @param activityseller
	 * @return
	 * @throws Exception 
	 */
	public boolean hasAssociatedProjects(Activityseller activityseller) throws Exception {
		boolean hasAssociated = false;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ActivitysellerDAO activitysellerDAO = new ActivitysellerDAO(session);
			hasAssociated = activitysellerDAO.hasAssociatedProjects(activityseller);
			
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
		return hasAssociated;
	}
	
	/**
	 * Has activity
	 * @param projectactivity
	 * @return
	 * @throws Exception
	 */
	public boolean hasActivity(Projectactivity projectactivity) throws Exception {
		Integer count = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ActivitysellerDAO activitysellerDAO = new ActivitysellerDAO(session);
			count = activitysellerDAO.rowCountEq(Activityseller.PROJECTACTIVITY, projectactivity);
			
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
		return (count != null && count > 0);
	}

	/**
	 * Find seller associated project
	 * @param projectactivity
	 * @param joins
	 * @return
	 * @throws Exception 
	 */
	public List<Activityseller> findSellerAssociatedProject(Projectactivity projectactivity, List<String> joins) throws Exception {
		List<Activityseller> listActSeller = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ActivitysellerDAO activitysellerDAO = new ActivitysellerDAO(session);
			listActSeller = activitysellerDAO.findSellerAssociatedProject(projectactivity, joins);
			
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
		return listActSeller;
	}

	/**
	 * Find by seller and project
	 * 
	 * @param seller
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public List<Activityseller> findBySellerAndProject(Seller seller, Project project) throws Exception {
		
		List<Activityseller> listActSeller = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			if (seller != null && project != null) {
				
				// Declare DAO
				ActivitysellerDAO activitysellerDAO = new ActivitysellerDAO(session);
				
				// DAO
				listActSeller = activitysellerDAO.findBySellerAndProject(seller, project);
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
		return listActSeller;
	}

	/**
	 * Update Type Activities Seller
	 * 
	 * @param seller
	 * @param indirect
	 * @param project 
	 * @throws Exception 
	 */
	public void updateTypeActivitiesSeller(Seller seller, Boolean indirect, Project project) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			if (seller != null && project != null) {
				
				// Declare DAO
				ActivitysellerDAO activitysellerDAO = new ActivitysellerDAO(session);
				
				// DAO
				activitysellerDAO.updateTypeActivitiesSeller(seller, indirect, project);
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
	 * Check same type of provider
	 * 
	 * @param seller
	 * @param project
	 * @param indirect
	 * @param created 
	 * @return
	 * @throws Exception 
	 */
	public Boolean sameTypeOfProvider(Seller seller, Project project, Boolean indirect, Boolean created) throws Exception {
		
		Boolean sameTypeOfProvider = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			if (seller != null && project != null && indirect != null && created != null) {
				
				// Declare DAO
				ActivitysellerDAO activitysellerDAO = new ActivitysellerDAO(session);
				
				// DAO
				List<Activityseller> activitysellers = activitysellerDAO.findBySellerAndProject(seller, project);
				
				// Set boolean
				if ((ValidateUtil.isNotNull(activitysellers) && created && activitysellers.size() > 0)
						|| (ValidateUtil.isNotNull(activitysellers) && !created && activitysellers.size() > 1)) {

					if (!activitysellers.get(0).getIndirect().equals(indirect)) {
						sameTypeOfProvider = false;
					}
					else {
						sameTypeOfProvider = true;
					}
				}
				else {
					sameTypeOfProvider = true;
				}
			}
			else {
				throw new Exception(getBundle().getString("msg.error.data"));
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
		
		return sameTypeOfProvider;
	}

	/**
	 * Save sow
	 * 
	 * @param activityseller
	 * @param activitysellerOld 
	 * @param updateAll
	 * @throws Exception 
	 */
	public List<Info> saveSow(Activityseller activityseller, Activityseller activitysellerOld, Boolean updateAll) throws Exception {
		
		List<Info> infos = new ArrayList<Info>();
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			if (activityseller != null && updateAll != null) {
				
				// Declare DAO
				ActivitysellerDAO activitysellerDAO = new ActivitysellerDAO(session);
				
				// Update
				//
				if (activityseller.getIdActivitySeller() != null) {
					
					// Control error has associated projects
					//
					// If change activity
					if (activityseller.getProject() != null && !activitysellerOld.getProjectactivity().getIdActivity().equals(activityseller.getProjectactivity().getIdActivity()) 
							&& activitysellerDAO.hasAssociatedProjects(activitysellerOld)) {
						
						infos.add(new Info(InfoType.INFORMATION, "msg.error.activity_seller"));
					}
					// If associate project to activity
					else if (activityseller.getProject() != null && activitysellerOld.getProject() == null && activitysellerDAO.hasAssociatedProjects(activitysellerOld)) {
						infos.add(new Info(InfoType.INFORMATION, "msg.error.activity_seller"));
					}
					
					infos.add(new Info(InfoType.SUCCESS, "msg.info.updated", "procurement.sow_selection.sow"));
				}
				// Created 
				//
				else {
					
					// Control error has associated projects
					if (activityseller.getProject() != null && activitysellerDAO.hasAssociatedProjects(activityseller)) {
						infos.add(new Info(InfoType.INFORMATION, "msg.error.activity_seller"));
					}
					
					infos.add(new Info(InfoType.SUCCESS, "msg.info.created", "procurement.sow_selection.sow"));
				}
				
				// DAO save activity seller
				activityseller = activitysellerDAO.makePersistent(activityseller);
				
				LOGGER.info("Save activity seller");
				
				if (updateAll) {
					
					// DAO update all type Activities Seller
					activitysellerDAO.updateTypeActivitiesSeller(activityseller.getSeller(), activityseller.getIndirect(), 
							activityseller.getProjectactivity().getProject());
					
					infos.add(new Info(InfoType.INFORMATION, "msg.info.updated_type_actsellers", activityseller.getSeller().getName()));
					LOGGER.info("Update all type activities seller");
				}
			}
			else {
				throw new Exception(getBundle().getString("msg.error.data"));
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
		
		return infos;
	}

}

