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
 * File: GeographyLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.GeographyDAO;
import es.sm2.openppm.core.exceptions.GeographyNotFoundException;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class GeographyLogic extends AbstractGenericLogic<Geography, Integer> {
	
	/**
	 * Delete Geography
	 * @param idGeography
	 * @throws Exception 
	 */
	public void deleteGeography(Integer idGeography) throws Exception {
		if (idGeography == null || idGeography == -1) {
			throw new GeographyNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			GeographyDAO geographyDAO = new GeographyDAO(session);
			Geography geography = geographyDAO.findById(idGeography, false);
			
			if (geography != null) {
				if(!geography.getProjects().isEmpty()){
					throw new LogicException("msg.error.delete.this_has","geography","project");
				}
				else{
					geographyDAO.makeTransient(geography);
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
