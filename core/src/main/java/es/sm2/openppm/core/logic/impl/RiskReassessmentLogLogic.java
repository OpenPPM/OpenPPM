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
 * File: RiskReassessmentLogLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.RiskReassessmentLogDAO;
import es.sm2.openppm.core.exceptions.RiskNotFoundException;
import es.sm2.openppm.core.model.impl.Riskreassessmentlog;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class RiskReassessmentLogLogic extends AbstractGenericLogic<Riskreassessmentlog, Integer> {
	
	
	/**
	 * Cons risk reassessments logs for a risk register
	 * @param project
	 * @return
	 * @throws RiskNotFoundException 
	 */
	public List<Riskreassessmentlog> consReassessments(Riskregister risk) throws Exception {
		if (risk == null) {
			throw new RiskNotFoundException();
		}
		List<Riskreassessmentlog> riskLogs = null;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			Riskreassessmentlog riskLog = new Riskreassessmentlog();
			riskLog.setRiskregister(risk);
			
			RiskReassessmentLogDAO riskLogDAO = new RiskReassessmentLogDAO(session);
			riskLogs = riskLogDAO.searchByExample(riskLog);
			
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
		
		return riskLogs;
	}
	
}
