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
 * File: WBSNodeLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import es.sm2.openppm.core.dao.*;
import es.sm2.openppm.core.model.impl.Changerequestwbsnode;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.impl.Wbstemplate;
import es.sm2.openppm.utils.functions.ValidateUtil;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.exceptions.WbsnodeCaException;
import es.sm2.openppm.core.exceptions.WbsnodeResumException;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;

public class WBSNodeLogic extends AbstractGenericLogic<Wbsnode, Integer> {

	public final static Logger LOGGER = Logger.getLogger(WBSNodeLogic.class);
	
	/**
	 * Get the first Wbsnode of the project
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public Wbsnode findFirstWBSnode(Project project) throws Exception {
				Wbsnode wbsnode = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WBSNodeDAO wbsNodeDAO = new WBSNodeDAO(session);
			wbsnode = wbsNodeDAO.findFirstNode(project);
			
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
		
		return wbsnode;
	}	
	
	
	/**
	 * Return Childs of Wbsnode
	 * @param wbsnode
	 * @return
	 * @throws Exception 
	 */
	public List<Wbsnode> findChildsWbsnode(Wbsnode wbsnode) throws Exception {
		
		List<Wbsnode> childs = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WBSNodeDAO wbsNodeDAO = new WBSNodeDAO(session);
			childs = wbsNodeDAO.findChilds(wbsnode);
			
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
		
		return childs;
	}	
	
	
	/** 
	 * Return all WBSNodes for a scope project
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public List<Wbsnode> consWBSNodes(Project project) throws Exception {
		List<Wbsnode> list = null;
		
		if (project.getIdProject() == null) {
			throw new NoDataFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WBSNodeDAO wbsNodeDAO = new WBSNodeDAO(session);
			list = wbsNodeDAO.findByProject(project);
			
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
	 * Save wbsnode
	 * 
	 * @param wbsNode
	 * @param idProject
	 * @return activity
	 * @throws Exception 
	 */
	public boolean saveWBSnode (Wbsnode wbsNode, Project project, boolean isCA) throws Exception {

		Transaction tx = null;		 
		boolean changeCA = false;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO		= new TimesheetDAO(session);
			ProjectActivityDAO activityDAO	= new ProjectActivityDAO(session);
			
			// The activity has hours in APP1, APP2 or APP3
			if (isCA && !wbsNode.getIsControlAccount() && timesheetDAO.isInputedApprovedHours(wbsNode)) {
				throw new LogicException("msg.error.updateWBS");
			}
			// The parent node and this is control account
			else if (wbsNode.getWbsnode() != null && wbsNode.getIsControlAccount() && wbsNode.getWbsnode().getIsControlAccount()) {
				throw new LogicException("msg.error.parent_child_wbs");
			}
			
			
			WBSNodeDAO wbsNodeDAO = new WBSNodeDAO(session);
			/* Saving a child node */
			if(wbsNode.getWbsnode() != null && wbsNode.getIsControlAccount()) {
				Wbsnode parent = wbsNodeDAO.findFirstNode(project);
				if(parent.getIsControlAccount()) {
					parent.setIsControlAccount(false);
					wbsNodeDAO.saveWBSnode(parent, project);
					changeCA = true;	
				}
			}	
			/* Saving the first node */
			if(wbsNode.getWbsnode() == null && wbsNode.getIsControlAccount()) {
				List<Wbsnode> nodes = wbsNodeDAO.findChildsByProject(project);
				if(!nodes.isEmpty()) {
					for(Wbsnode node : nodes) {
						if(node.getIsControlAccount()) {
							wbsNode.setIsControlAccount(false);
							changeCA = true;
							break;
						}
					}
				}
			}
			
			wbsNodeDAO.saveWBSnode(wbsNode, project);
			
			//TODO no se deberia actualizar el presupuesto de la actividad root?
			
			// Update dates of root activity
			if (wbsNode.getWbsnode() != null) {
				Projectactivity rootActivity = activityDAO.consRootActivity(project);
				activityDAO.updatePlannedDates(project, rootActivity);
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
		return changeCA;
	}
	
	/**
	 * Validate WBS node
	 * 
	 * @param wbsNode
	 * @param isCA
	 * @param resourceBundle 
	 * @return
	 * @throws Exception
	 */
	public JSONObject validateWBSnode(Wbsnode wbsNode, boolean isCA, ResourceBundle resourceBundle) throws Exception {

		Transaction tx = null;		 
		
		JSONObject responseJSON = new JSONObject();
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();

            // Declare DAOs
			TimesheetDAO timesheetDAO	                    = new TimesheetDAO(session);
			WBSNodeDAO wbsNodeDAO		                    = new WBSNodeDAO(session);
			TeamMemberDAO memberDAO		                    = new TeamMemberDAO(session);
            ChangerequestwbsnodeDAO changerequestwbsnodeDAO = new ChangerequestwbsnodeDAO(session);
			
			// Load WBS node
			Integer wbsNodeId = wbsNode.getIdWbsnode();
			wbsNode = wbsNodeDAO.findById(wbsNodeId);
			
			if (wbsNode == null) {
				LOGGER.warn("Not found WBSNode with ID: "+wbsNodeId);
			}
			// The activity has hours in APP0 and not in APP1, APP2 or APP3
			boolean confirm = false;
			
			if (!isCA
                    && (wbsNode != null && wbsNode.getIsControlAccount() != null && wbsNode.getIsControlAccount())
                    && !timesheetDAO.isInputedApprovedHours(wbsNode)
                    && timesheetDAO.isInputedHours(wbsNode)) {

                confirm = true;
				responseJSON.put("confirmMsg", resourceBundle.getString("msg.confirm.remove_ca_app0"));
			}
            // Has team members asociated
			else if (!isCA
					&& (wbsNode != null && wbsNode.getIsControlAccount() != null && wbsNode.getIsControlAccount())
					&& memberDAO.hasMembers(wbsNode)) {
				
				confirm = true;
				responseJSON.put("confirmMsg", resourceBundle.getString("msg.confirm.remove_ca_members"));
			}
            // Has change control list
            else if (!isCA
                    && (wbsNode != null && wbsNode.getIsControlAccount() != null && wbsNode.getIsControlAccount())
                    && ValidateUtil.isNotNull(changerequestwbsnodeDAO.findByRelation(Changerequestwbsnode.WBSNODE, wbsNode))) {

                throw new LogicException("msg.error.wbsnode_ca_disabled_change_list");
            }

			responseJSON.put("confirm", confirm);
			
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
		
		return responseJSON;
	}
	
	/**
	 * Detele wbs node
	 * @param idWbsnode
	 * @param settings
     * @throws Exception
	 */
	public void deleteWBSNode(Integer idWbsnode, HashMap<String, String> settings) throws Exception {
		Wbsnode wbsNode = null;
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectLogic projectLogic = new ProjectLogic(settings, getBundle());
			TimesheetDAO timesheetDAO = new TimesheetDAO(session);
			ProjectDAO projectDAO 		= new ProjectDAO(session);
			
			if (timesheetDAO.isInputedApprovedHours(wbsNode)) {
				throw new LogicException("msg.error.deleteWBS");
			}
		
			WBSNodeDAO wbsNodeDAO = new WBSNodeDAO(session);
			if (idWbsnode == -1) {
				throw new NoDataFoundException();
			}
			
			wbsNode = wbsNodeDAO.findById(idWbsnode, false);
			
			
			if (wbsNode == null) {
				throw new NoDataFoundException();
			}
			
			Project project = wbsNode.getProject();
			
			if (wbsNode.getIsControlAccount() != null && wbsNode.getIsControlAccount()) {
				throw new WbsnodeCaException();
			}
			if (wbsNode.getWbsnode() == null) {
				throw new WbsnodeResumException();
			}
			
			wbsNodeDAO.makeTransient(wbsNode);
			
			// Update dates of project
			project.setStartDate(projectLogic.getStartDate(project, session));
			project.setFinishDate(projectLogic.getFinishDate(project, session));
			project.setPoc(projectLogic.calcPoc(project, session, settings));
			
			projectDAO.makePersistent(project);
			
			// Update projects leads
			ProjectLogic logic = new ProjectLogic(settings, getBundle());
			logic.updateDatesLeads(session, project);
						
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
	 * Check WBS Errors
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public List<ParamResourceBundle> checkErrorsWBS(Project project) throws Exception {
		
		List<ParamResourceBundle> errors	= new ArrayList<ParamResourceBundle>();
		Transaction tx		= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			WBSNodeDAO wbsNodeDAO = new WBSNodeDAO(session);
			Wbsnode firstNode = wbsNodeDAO.findFirstNode(project);
			
			if (firstNode == null) {
				
				errors.add(new ParamResourceBundle("msg.error.not_found_any","wbs_node","project"));
			}
			else {
				findErrosWBS(firstNode, errors,false);
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
		return errors;
	}


	/**
	 * Find Errors for WBS
	 * @param wbsnode
	 * @param errors
	 * @param isca
	 */
	private void findErrosWBS(Wbsnode wbsnode, List<ParamResourceBundle> errors,boolean isca) {
		if (wbsnode.getWbsnode() != null && wbsnode.getWbsnodes().size() == 0 && !wbsnode.getWbsnode().getIsControlAccount() &&
				!wbsnode.getIsControlAccount() && !isca) {
			
			ParamResourceBundle temp = new ParamResourceBundle("msg.info.not_ca",wbsnode.getCode(),wbsnode.getName());
			errors.add(temp);
		}
		if (wbsnode.getWbsnodes().size() > 0) {
			for (Wbsnode child : wbsnode.getWbsnodes()) {
				if (isca && child.getIsControlAccount()) {
					ParamResourceBundle temp = new ParamResourceBundle("msg.info.already_ca",child.getCode(),child.getName());
					errors.add(temp);
				}
				if (isca) {
					findErrosWBS(child, errors, true);
				}
				else {
					findErrosWBS(child, errors, child.getIsControlAccount());
				}
			}
		}
	}

	/**
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Double consTotalBudget(Project project) throws Exception {
		
		Double total = null;
		
		Transaction tx		= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			WBSNodeDAO wbsNodeDAO = new WBSNodeDAO(session);
			total = wbsNodeDAO.consTotalBudget(project);
			
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
		return total;
	}

	/**
	 * Find by project
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public List<Wbsnode> findByProject(Project project) throws Exception {
		List<Wbsnode> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WBSNodeDAO wbsNodeDAO = new WBSNodeDAO(session);
			list = wbsNodeDAO.findByProject(project);
			
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
	 * 
	 * @param idWBSParent
	 * @param idWBSTemplate
	 * @param project
	 * @throws Exception 
	 */
	public void saveWBSTemplate(Wbsnode wbsnodeParent, Integer idWBSTemplate, Project project) throws Exception {
		preorderTraversal(wbsnodeParent, idWBSTemplate, project);
	}

	/**
	 * 
	 * @param idWBSParent
	 * @param idWBSTemplate
	 * @param project
	 * @param level
	 * @throws Exception 
	 */
	private void preorderTraversal(Wbsnode wbsnodeParent, Integer idWBSTemplate, Project project) throws Exception {
		
		WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();
		
		List<String> joins = new ArrayList<String>();
		joins.add(Wbstemplate.WBSTEMPLATE);
		joins.add(Wbstemplate.WBSTEMPLATES);
		
		Wbstemplate wbstemplate = wbsTemplateLogic.findById(idWBSTemplate, joins);
		
		Wbsnode wbsNode = new Wbsnode();
		
		wbsNode.setCode(wbstemplate.getCode());
		wbsNode.setName(wbstemplate.getName());
		wbsNode.setDescription(wbstemplate.getDescription());
		wbsNode.setIsControlAccount(wbstemplate.getIsControlAccount());
		wbsNode.setBudget(null);
		wbsNode.setWbsnode(wbsnodeParent);
		wbsNode.setProject(project);
		
		//Save parent
		saveWBSnode(wbsNode, project, wbsNode.getIsControlAccount());

		for(Wbstemplate child : wbstemplate.getWbstemplates()){
			preorderTraversal(wbsNode, child.getIdWbsnode(), project);
		}
		
	}

	/**
	 * Cascade delete wbsnode and associated activities
	 * @param idWbsnode
	 * @param settings
     * @throws Exception
	 */
	public void deleteWBSNodeAndActivities(int idWbsnode, HashMap<String, String> settings) throws Exception {
		
		WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();
		
		deleteActivitiesAssociated(idWbsnode);
		
		wbsNodeLogic.deleteWBSNode(idWbsnode, settings);
	}

	/**
	 * Recursive delete activities
	 * @param idWbsnode
	 * @throws Exception
	 */
	private void deleteActivitiesAssociated(int idWbsnode) throws Exception {
		
		WBSNodeLogic wbsNodeLogic 					= new WBSNodeLogic();
		ProjectActivityLogic projectActivityLogic 	= new ProjectActivityLogic(null, getBundle());
		
		List<String> joins = new ArrayList<String>();
		joins.add(Wbsnode.WBSNODES);
		joins.add(Wbsnode.PROJECTACTIVITIES);
		
		Wbsnode wbsnode = wbsNodeLogic.findById(idWbsnode, joins);
		
		//Delete activities wbsnode
		for(Projectactivity projectactivity : wbsnode.getProjectactivities()) {
			projectActivityLogic.delete(projectactivity);
		}

		//Delete child activities
		for(Wbsnode child : wbsnode.getWbsnodes()){
			deleteActivitiesAssociated(child.getIdWbsnode());
		}
	}


	
	/**
	 * Return Childs of Obsnode
	 * @param wbsnode
	 * @return
	 * @throws Exception 
	 */
	public List<Wbsnode> findChildsObsnode(Wbsnode wbsnode) throws Exception {
			
		List<Wbsnode> childs = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WBSNodeDAO wbsNodeDAO = new WBSNodeDAO(session);
			childs = wbsNodeDAO.findChilds(wbsnode);
			
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
		
		return childs;
	}
	
}
