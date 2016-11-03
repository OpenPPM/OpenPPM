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
 * File: ChangerequestwbsnodeLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.ChangerequestwbsnodeDAO;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Changerequestwbsnode;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Changerequestwbsnode
 * @see es.sm2.openppm.logic.Changerequestwbsnode
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class ChangerequestwbsnodeLogic extends AbstractGenericLogic<Changerequestwbsnode, Integer> {

	/**
	 * Find by change control and order by control account name
	 * 
	 * @param changecontrol
	 * @return
	 * @throws Exception 
	 */
	public List<Changerequestwbsnode> findByChangeControl(Changecontrol changecontrol) throws Exception {
		
		List<Changerequestwbsnode> changesRequestWBSNode = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ChangerequestwbsnodeDAO changerequestwbsnodeDAO = new ChangerequestwbsnodeDAO(session);
			
			changesRequestWBSNode = changerequestwbsnodeDAO.findByChangeControl(changecontrol);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return changesRequestWBSNode;
	}

	/**
	 * Find by changecontrol and wbsnode
	 * 
	 * @param changecontrol
	 * @param wbsnode
	 * @return
	 * @throws Exception 
	 */
	public List<Changerequestwbsnode> findByChangeControlAndWBSNode(Changecontrol changecontrol, Wbsnode wbsnode) throws Exception {
		
	List<Changerequestwbsnode> changesRequestWBSNode = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ChangerequestwbsnodeDAO changerequestwbsnodeDAO = new ChangerequestwbsnodeDAO(session);
			
			changesRequestWBSNode = changerequestwbsnodeDAO.findByChangeControlAndWBSNode(changecontrol, wbsnode);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return changesRequestWBSNode;
	}
}

