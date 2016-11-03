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
 * File: MilestonetypeLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.MilestoneDAO;
import es.sm2.openppm.core.dao.MilestonetypeDAO;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Milestonetype
 * @see es.sm2.openppm.logic.Milestonetype
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class MilestonetypeLogic extends AbstractGenericLogic<Milestonetype, Integer> {

	/**
	 * Delete milestone type
	 * 
	 * @param milestonetype
	 * @throws Exception 
	 */
	public void deleteMilestoneType(Milestonetype milestonetype) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			// Declare DAOs
			MilestonetypeDAO milestonetypeDAO 	= new MilestonetypeDAO(session);
			MilestoneDAO milestoneDAO 			= new MilestoneDAO(session);
			
			// Foreign keys control
			List<Milestones> milestones = milestoneDAO.findByRelation(Milestones.MILESTONETYPE, milestonetype, Milestones.IDMILESTONE, Constants.ASCENDENT);
			
			if (!milestones.isEmpty()) {
				throw new LogicException("msg.error.delete.this_has","milestone_type","milestones");
			}
			
			// Save
			milestonetypeDAO.makeTransient(milestonetype);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}
}

