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
 * File: WBSNodeDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class WBSNodeDAO extends AbstractGenericHibernateDAO<Wbsnode, Integer> {

	public WBSNodeDAO(Session session) {
		super(session);
	}

	/**
	 * Return WBS family Parent
	 * @param proj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Wbsnode findFirstNode(Project proj) {
		
		Wbsnode wbsNode = null;
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.isNull(Wbsnode.WBSNODE))
			.addOrder(Order.asc(Wbsnode.CODE))
			.add(Restrictions.eq(Wbsnode.PROJECT, proj));		
		
		List<Wbsnode> list = crit.list();
		if (!list.isEmpty()) {
			wbsNode = list.get(0);
		}
		return wbsNode;
	}
	
	
	/**
	 *	List of Childs for WBSNode 
	 * @param wbsnode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Wbsnode> findChilds(Wbsnode wbsnode) {
		
		Query query = getSession().createQuery(
				"select wbsnode " +
				"from Wbsnode as wbsnode " + 
				"left join fetch wbsnode.projectactivities as pA " +
				"where wbsnode.wbsnode = :parent " +
				"order by wbsnode.code");
		query.setInteger("parent", wbsnode.getIdWbsnode());
		
		return query.list();
	}

	public double consTotalBudget(Project project) {
		
		double budget = 0;
		Query query = getSession().createQuery(
				"select sum(wbsnode.budget) " +
				"from Wbsnode as wbsnode " + 
				"join wbsnode.project as project " +
				"where project.idProject = :idProject " +
				"and wbsnode.isControlAccount is true");
		
		query.setInteger("idProject", project.getIdProject());
		
		if (query.uniqueResult() != null) {
			budget = (Double)query.uniqueResult();
		}
		return budget;
	}

	/**
	 * 
	 * @param wbsNode
	 * @param idProject
	 * @param activity
	 */
	public Wbsnode saveWBSnode (Wbsnode wbsNode, Project project) {
		
		wbsNode = makePersistent(wbsNode);
		
		// Create associated project activity
		ProjectActivityDAO activityDAO = new ProjectActivityDAO(getSession());
		Projectactivity activity = activityDAO.findByWBSnode(project, wbsNode.getIdWbsnode());
		
		// Activities created only for CA nodes
		if (wbsNode.getIsControlAccount() || wbsNode.getWbsnode() == null) {
			
			if (activity == null) {
				activity = new Projectactivity();
				activity.setProject(project);
				activity.setWbsnode(wbsNode);
				activity.setWbsdictionary(StringPool.BLANK);
			}
		
			if (wbsNode.getCode() != null && !StringPool.BLANK.equals(wbsNode.getCode())) {
				activity.setActivityName(wbsNode.getCode()+". "+wbsNode.getName());
			}
			else {
				activity.setActivityName(wbsNode.getName());
			}
			
			activityDAO.makePersistent(activity);
		}
		else if (!wbsNode.getIsControlAccount() && activity != null && wbsNode.getWbsnode() != null) {
			activityDAO.makeTransient(activity);
		}
		
		return wbsNode;
	}

	public void restoreNodes(Wbsnode wbsParent, Project project, Projectactivity activity) {
		
		// Delete nodes
		Query query = getSession().createQuery(
				"delete from Wbsnode as w " +
				"where w.idWbsnode != :idWbsnode " +
					"and w.project.idProject = :idProject"
			);
		query.setInteger("idProject", project.getIdProject());
		query.setInteger("idWbsnode", wbsParent.getIdWbsnode());
		query.executeUpdate();
		
		// Delete Activities
		query = getSession().createQuery(
				"delete from Projectactivity as pa " +
				"where pa.project.idProject = :idProject " +
					"and pa.idActivity != :idActivity"
				);
		query.setInteger("idProject", project.getIdProject());
		query.setInteger("idActivity", activity.getIdActivity());
		query.executeUpdate();
		
		// Delete Milestones
		query = getSession().createQuery(
				"delete from Milestones as m " +
				"where m.project.idProject = :idProject "
				);
		query.setInteger("idProject", project.getIdProject());
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Wbsnode> searchByCode(Wbsnode node) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		crit.setFetchMode(Wbsnode.PROJECTACTIVITIES, FetchMode.JOIN);
		crit.add(Example.create(node).excludeZeroes())
			.add(Restrictions.eq(Wbsnode.PROJECT, node.getProject()));
		
		return crit.list();
	}

	public double totalBudget(Project project) {
		
		double total = 0;
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.sum(Wbsnode.BUDGET))
			.add(Restrictions.eq(Wbsnode.PROJECT, project));
		
		if (crit.uniqueResult() != null) {
			total = (Double) crit.uniqueResult();
		}
		
		return total;
	}

	/**
	 * Find by project
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Wbsnode> findByProject(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.addOrder(Order.asc(Wbsnode.CODE))			
			.add(Restrictions.eq(Wbsnode.PROJECT, project));
		
		return crit.list();
	}
	
	/**
	 * Retrun all WBSNodes of a project except the parent node
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Wbsnode> findChildsByProject(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.addOrder(Order.asc(Wbsnode.NAME))
			.add(Restrictions.isNotNull(Wbsnode.WBSNODE))
			.add(Restrictions.eq(Wbsnode.PROJECT, project));
		
		return crit.list();
	}

	/**
	 * Sum budget
	 * @param project
	 * @return
	 */
	public double getSumBudget(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true))
			.add(Restrictions.eq(Wbsnode.PROJECT, project))
			.setProjection(Projections.sum(Wbsnode.BUDGET));
		
		Double value = (Double) crit.uniqueResult(); 
		return (value == null?0:value);
	}

	/**
	 * Check if one or more wbsnodes have budget null or 0
	 * 
	 * @param project
	 * @return
	 */
	public boolean isBudgetEmpty(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Wbsnode.ISCONTROLACCOUNT, true))
			.add(Restrictions.eq(Wbsnode.PROJECT, project))
			.add(Restrictions.or(
					Restrictions.isNull(Wbsnode.BUDGET),
					Restrictions.eq(Wbsnode.BUDGET, new Double(0))
					)
				);
		
		//TODO http://192.168.65.102/redmine/issues/1688 .add(Restrictions.isNull(Wbsnode.BUDGET));
		
		Integer rows = (Integer)crit.uniqueResult();
		
		return (rows != null && rows > 0);
	}
}
