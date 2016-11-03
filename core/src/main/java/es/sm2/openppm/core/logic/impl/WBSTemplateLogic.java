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
 * File: WBSTemplateLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.WbstemplateDAO;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.impl.Wbstemplate;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Wbstemplate
 * @see es.sm2.openppm.logic.Wbstemplate
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class WBSTemplateLogic extends AbstractGenericLogic<Wbstemplate, Integer> {

	
	/**
	 * Save WBS template
	 * @param idWBSNode
	 * @param company 
	 * @throws Exception
	 */
	public void saveWBSTemplate(Integer idWbsnode, Company company) throws Exception {
			
		preorderTraversal(idWbsnode, null, idWbsnode, company, 0);
		
	}


	/**
	 * Tree traversal and save nodes
	 * @param idWBSNode
	 * @param company
	 * @param level
	 * @throws Exception
	 */
	private void preorderTraversal(Integer root, Wbstemplate wbstemplateParent, Integer idWbsnode, Company company, Integer level) throws Exception {
		
			Wbstemplate wbstemplate = null;
		
			WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();
			
			List<String> joins = new ArrayList<String>();
			joins.add(Wbsnode.WBSNODES);
			
			Wbsnode wbsnode = wbsNodeLogic.findById(idWbsnode, joins);
			
			//Save parent
			if(level == 0){
				wbstemplate = saveWBSnode(null, wbstemplateParent, wbsnode, company);
				
				root = wbstemplate.getIdWbsnode();
			}
			else {
				wbstemplate = saveWBSnode(root, wbstemplateParent, wbsnode, company);
			}
			
			//Save childs
			if(!wbsnode.getWbsnodes().isEmpty()){
				for(Wbsnode child : wbsnode.getWbsnodes()){
					preorderTraversal(root, wbstemplate, child.getIdWbsnode(), company, level + 1);
				}
			}
	}



	/**
	 * Save wbsnode
	 * @param wbsNode
	 * @param idProject
	 * @return activity
	 * @throws Exception 
	 */
	public Wbstemplate saveWBSnode (Integer root, Wbstemplate wbsParent, Wbsnode wbsNode, Company company) throws Exception {

		Wbstemplate wbstemplate = new Wbstemplate();
		
		Transaction tx = null;		 
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			WbstemplateDAO wbstemplateDAO = new WbstemplateDAO(session);
			
			wbstemplate.setCode(wbsNode.getCode());
			wbstemplate.setDescription(wbsNode.getDescription());
			wbstemplate.setCompany(company);
			wbstemplate.setIsControlAccount(wbsNode.getIsControlAccount());
			wbstemplate.setName(wbsNode.getName());
			wbstemplate.setWbstemplate(wbsParent);
			wbstemplate.setRoot(root);
			
			wbstemplate = wbstemplateDAO.makePersistent(wbstemplate);
			
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
		return wbstemplate;
	}


	/**
	 * Find by company and property equal to null
	 * @param company
	 * @param property
	 * @return
	 * @throws Exception
	 */
	public List<Wbstemplate> findByCompanyAndRelationNull(Company company, String property) throws Exception {
		List<Wbstemplate> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WbstemplateDAO wbstemplateDAO = new WbstemplateDAO(session);
			list = wbstemplateDAO.findByCompanyAndRelationNull(company, property);
			
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
	 * Return cascade childs of WBStemplate
	 * @param wbstemplate
	 * @return
	 * @throws Exception
	 */
	public List<Wbstemplate> findAllChildsWbsTemplate(Wbstemplate wbstemplate, List<String> joins) throws Exception {
		
		List<Wbstemplate> childs = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WbstemplateDAO wbstemplateDAO = new WbstemplateDAO(session);
			childs = wbstemplateDAO.findAllChilds(wbstemplate, joins);
			
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
	 * Return inmediate childs of WBStemplate node
	 * @param wbstemplate
	 * @return
	 * @throws Exception
	 */
	public List<Wbstemplate> findChildsWbsnode(Wbstemplate wbstemplate) throws Exception {
		List<Wbstemplate> childs = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WbstemplateDAO wbstemplateDAO = new WbstemplateDAO(session);
			childs = wbstemplateDAO.findChilds(wbstemplate);
			
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
	 * Return tree of WBSTemplate
	 * @param root
	 * @return
	 * @throws Exception 
	 */
	public List<Wbstemplate> findWbsTemplate(Wbstemplate wbstemplate) throws Exception {
		List<Wbstemplate> wbstemplates = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			WbstemplateDAO wbstemplateDAO = new WbstemplateDAO(session);
			wbstemplates = wbstemplateDAO.findWbsTemplate(wbstemplate);
			
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
		
		return wbstemplates;
	}

	
/**
 * Save WBS template node
 * @param wbstemplate
 * @throws Exception 
 */
	public void saveWBSTemplateNode(Wbstemplate wbstemplate) throws Exception {
			
		WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();
		
		/*
		if(wbstemplate.getWbstemplate() != null && wbstemplate.getWbstemplate().getIsControlAccount()){ // The parent node is control account.
			throw new LogicException("msg.error.parent_wbs");
		}
		else if(wbstemplate.getIsControlAccount() && !wbstemplate.getWbstemplates().isEmpty()){ // A control account node can not have child nodes.
			throw new LogicException("msg.error.childs_wbs");
		}
		*/
		
		// The parent node and this is control account
		if(wbstemplate.getWbstemplate() != null && wbstemplate.getIsControlAccount() && wbstemplate.getWbstemplate().getIsControlAccount()){ 
			throw new LogicException("msg.error.parent_wbs");
		}
		
		wbsTemplateLogic.save(wbstemplate);
	}

}

