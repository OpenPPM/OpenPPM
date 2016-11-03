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
 * File: BusinessdriversetLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.BusinessdriverDAO;
import es.sm2.openppm.core.dao.BusinessdriversetDAO;
import es.sm2.openppm.core.model.impl.Businessdriver;
import es.sm2.openppm.core.model.impl.Businessdriverset;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Businessdriverset
 * @see es.sm2.openppm.logic.Businessdriverset
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class BusinessdriversetLogic extends AbstractGenericLogic<Businessdriverset, Integer> {

	public void deleteBusinessDriverSet(Businessdriverset businessdriverset) throws Exception {
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			if (businessdriverset != null) {
				BusinessdriversetDAO businessdriversetDAO 	= new BusinessdriversetDAO(session);
				BusinessdriverDAO businessdriverDAO 		= new BusinessdriverDAO(session);
				
				List<Businessdriver> businessDrivers = businessdriverDAO.findByRelation(Businessdriver.BUSINESSDRIVERSET, businessdriverset);
				
				if (!businessDrivers.isEmpty()) {
					throw new LogicException("msg.error.delete.this_has","maintenance.business_driver_set","business_driver");
				}
				else {
					businessdriversetDAO.makeTransient(businessdriverset);
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

}

