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
 * File: ResourcepoolLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.EmployeeDAO;
import es.sm2.openppm.core.dao.ManagepoolDAO;
import es.sm2.openppm.core.dao.ResourcepoolDAO;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Managepool;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;



/**
 * Logic object for domain model class Resourcepool
 * @see ResourcepoolLogic
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class ResourcepoolLogic extends AbstractGenericLogic<Resourcepool, Integer> {

	/**
	 * Save resourcepool and create managepools
	 * 
	 * @param resourcepool
	 * @param idsResourceManagers
	 * @return
	 * @throws Exception
	 */
	public Resourcepool save(Resourcepool resourcepool, String[] idsResourceManagers) throws Exception {

		// Validates 
		//
		if (resourcepool == null) {
			throw new LogicException("data_not_found_param", "maintenance.employee.resource_pool", StringPool.BLANK);
		}
		
		if (!ValidateUtil.isNotNull(idsResourceManagers)) {
			throw new LogicException("data_not_found_param", "maintenance.employee.resource_manager", StringPool.BLANK);
		}
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ResourcepoolDAO resourcepoolDAO = new ResourcepoolDAO(session);
			ManagepoolDAO managepoolDAO 	= new ManagepoolDAO(session);
			
			Integer idResourcePool = resourcepool.getIdResourcepool();
			
			// Save resourcepool 
			//
			resourcepool = resourcepoolDAO.makePersistent(resourcepool);
			
			// Only for update 
			//
			if (idResourcePool != null) {
				
				// Delete managepool associated to resourcepool 
				//
				List<Managepool> listManagepool = managepoolDAO.findByRelation(Managepool.RESOURCEPOOL, resourcepool);
				
				for (Managepool managepool : listManagepool) {
					managepoolDAO.makeTransient(managepool);
				}
			}
			
			// Create managepool for each resource manager
			for (String idResourceManager : idsResourceManagers) {
				
				Managepool managepool = new Managepool();
				
				managepool.setResourcepool(resourcepool);
				managepool.setEmployee(new Employee(new Integer(idResourceManager)));
				
				managepoolDAO.makePersistent(managepool);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return resourcepool;
	}

	/**
	 * Delete resourcepool and managepool associated
	 * 
	 * @param resourcepool
	 * @throws Exception 
	 */
	public void deleteResourcePool(Resourcepool resourcepool) throws Exception {
		
		// Validates 
		//
		if (resourcepool == null) {
			throw new LogicException("data_not_found_param", "maintenance.employee.resource_pool", StringPool.BLANK);
		}
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ResourcepoolDAO resourcepoolDAO = new ResourcepoolDAO(session);
			ManagepoolDAO managepoolDAO 	= new ManagepoolDAO(session);
			EmployeeDAO employeeDAO 		= new EmployeeDAO(session);
			
			// Verify that you have an employee associated 
			//
			if (ValidateUtil.isNotNull(employeeDAO.findByRelation(Employee.RESOURCEPOOL, resourcepool))) {
				throw new LogicException("msg.error.delete.this_has","maintenance.employee.resource_pool","maintenance.employee");
			}
			
			// Delete managepools 
			//
			List<Managepool> listManagepool = managepoolDAO.findByRelation(Managepool.RESOURCEPOOL, resourcepool);
			
			if (ValidateUtil.isNotNull(listManagepool)) {
				
				for (Managepool managepool : listManagepool) {
					managepoolDAO.makeTransient(managepool);
				}
			}
			
			// Delete resourcepool 
			//
			resourcepoolDAO.makeTransient(resourcepool);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

	/**
	 * Resource pools by resource manager
	 * 
	 * @param resourceManager
	 * @return
	 * @throws Exception 
	 */
	public List<Resourcepool> findByResourceManager(Employee resourceManager) throws Exception {
		
		List<Resourcepool> resourcepools = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			if (resourceManager != null) {
				
				ResourcepoolDAO resourcepoolDAO = new ResourcepoolDAO(session);
				
				resourcepools = resourcepoolDAO.findByResourceManager(resourceManager);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return resourcepools;
	}

	/**
	 * Resource pools by resource managers
	 * 
	 * @param resourceManagers
	 * @return
	 * @throws Exception 
	 */
	public List<Resourcepool> findByResourceManagers(List<Employee> resourceManagers) throws Exception {
		
		List<Resourcepool> resourcepools = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			if (ValidateUtil.isNotNull(resourceManagers)) {
				
				ResourcepoolDAO resourcepoolDAO = new ResourcepoolDAO(session);
				
				resourcepools = resourcepoolDAO.findByResourceManagers(resourceManagers);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return resourcepools;
	}

	/**
	 * Resource pools by project manager
	 * 
	 * @param projectManager
	 * @return
	 * @throws Exception 
	 */
	public List<Resourcepool> findByProjectManager(Employee projectManager) throws Exception {
		
		List<Resourcepool> resourcepools = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			if (projectManager != null) {
				
				ResourcepoolDAO resourcepoolDAO = new ResourcepoolDAO(session);
				
				resourcepools = resourcepoolDAO.findByProjectManager(projectManager);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return resourcepools;
	}

    /**
     * Find by role
     *
     * @param user
     * @return
     */
    public List<Resourcepool> findByRole(Employee user) throws Exception {

        List<Resourcepool> resourcepools = null;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            resourcepools = findByRole(session, user);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return resourcepools;
    }

    /**
     * Find by role
     *
     * @param session
     * @param user
     * @return
     */
    public List<Resourcepool> findByRole(Session session, Employee user) {

        List<Resourcepool> resourcepools = null;

        ResourcepoolDAO resourcepoolDAO = new ResourcepoolDAO(session);

        if (user != null && user.getResourceprofiles() != null &&
                Resourceprofiles.Profile.RESOURCE_MANAGER.equals(user.getResourceprofiles().getProfile())) {

            resourcepools = resourcepoolDAO.findByResourceManager(user);
        }
        else if (user != null && user.getResourceprofiles() != null &&
                Resourceprofiles.Profile.PROJECT_MANAGER.equals(user.getResourceprofiles().getProfile())) {

            resourcepools = resourcepoolDAO.findByProjectManager(user);
        }
        else if (user != null && user.getContact() != null && user.getContact().getCompany() != null) {
            resourcepools = resourcepoolDAO.findByRelation(Resourcepool.COMPANY, user.getContact().getCompany());
        }


        return resourcepools;
    }
}

