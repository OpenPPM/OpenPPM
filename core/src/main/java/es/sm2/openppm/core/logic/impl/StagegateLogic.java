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
 * File: StagegateLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.StagegateDAO;
import es.sm2.openppm.core.exceptions.StagegateNotFoundException;
import es.sm2.openppm.core.model.impl.Stagegate;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Stagegate
 * @see es.sm2.openppm.core.model.impl.Stagegate
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class StagegateLogic extends AbstractGenericLogic<Stagegate, Integer> {

	/**
	 * Delete stage gate
	 * 
	 * @param stagegate
	 * @throws Exception 
	 */
	public void deleteStageGate(Stagegate stagegate) throws Exception {
		
		if (stagegate == null) {
			throw new StagegateNotFoundException();
		}
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			StagegateDAO stagegateDAO = new StagegateDAO(session);
			stagegate = stagegateDAO.findById(stagegate.getIdStageGate(), false);
			
			if (stagegate != null) {
				if(!stagegate.getProjects().isEmpty()){
					throw new LogicException("msg.error.delete.this_has","stage_gate","project");
				}
				else{
					stagegateDAO.makeTransient(stagegate);
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

	/**
	 * Search stage gates for projects according to order stage gate name
	 * 
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	public List<Stagegate> findByProjects(Integer[] ids) throws Exception {
		
		List<Stagegate> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			StagegateDAO stagegateDAO = new StagegateDAO(session);
			
			list = stagegateDAO.findByProjects(ids);
			
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

}

