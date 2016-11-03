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
 * File: ProjectCharterLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.ProjectCharterDAO;
import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class ProjectCharterLogic extends AbstractGenericLogic<Projectcharter, Integer>{

	/**
	 * Get Project Charter by Id
	 * @param idProjectCharter
	 * @return
	 * @throws Exception 
	 */
	public Projectcharter consProjectCharter (Integer idProjectCharter) throws Exception {
		if (idProjectCharter == -1) {
			throw new NoDataFoundException();
		}
		Projectcharter projCharter = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectCharterDAO charterDAO = new ProjectCharterDAO(session);
			projCharter = charterDAO.findById(idProjectCharter, false);
			
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
		return projCharter;
	}
	

	/**
	 * Get project charter by project
	 * @param proj
	 * @return
	 * @throws Exception
	 */
	public Projectcharter findByProject(Project proj) throws Exception {
		
		Projectcharter projCharter = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectCharterDAO charterDAO = new ProjectCharterDAO(session);
			projCharter = charterDAO.findByProject(proj);
			
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
		return projCharter;
	}
}
