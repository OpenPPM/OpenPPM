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
 * File: RiskregistertemplateLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.impl;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.RiskRegisterDAO;
import es.sm2.openppm.core.dao.RiskregistertemplateDAO;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.impl.Riskregistertemplate;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Riskregistertemplate
 * @see es.sm2.openppm.logic.Riskregistertemplate
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class RiskregistertemplateLogic extends AbstractGenericLogic<Riskregistertemplate, Integer> {

	public Riskregistertemplate saveRisk(Integer idRisk, Company company) throws Exception {
		Riskregistertemplate riskregistertemplate = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			RiskRegisterDAO riskRegisterDAO 				= new RiskRegisterDAO(session);
			RiskregistertemplateDAO riskregistertemplateDAO = new RiskregistertemplateDAO(session);
			
			Riskregister risk = riskRegisterDAO.findById(idRisk);
			
			Riskregistertemplate template = new Riskregistertemplate();
			
			template.setActualMaterializationCost(risk.getActualMaterializationCost());
			template.setActualMaterializationDelay(risk.getActualMaterializationDelay());
			template.setClosed(risk.getClosed());
			template.setCompany(company);
			template.setContingencyActionsRequired(risk.getContingencyActionsRequired());
			template.setDescription(risk.getDescription());
			template.setFinalComments(risk.getFinalComments());
			template.setImpact(risk.getImpact());
			template.setMaterialized(risk.getMaterialized());
			template.setMitigationActionsRequired(risk.getMitigationActionsRequired());
			template.setOwner(risk.getOwner());
			template.setPlannedContingencyCost(risk.getPlannedContingencyCost());
			template.setPlannedMitigationCost(risk.getPlannedMitigationCost());
			template.setPotentialCost(risk.getPotentialCost());
			template.setPotentialDelay(risk.getPotentialDelay());
			template.setProbability(risk.getProbability());
			template.setResidualCost(risk.getResidualCost());
			template.setResidualRisk(risk.getResidualRisk());
			template.setResponseDescription(risk.getResponseDescription());
			template.setRiskcategories(risk.getRiskcategories());
			template.setRiskCode(risk.getRiskCode());
			template.setRiskDoc(risk.getRiskDoc());
			template.setRiskName(risk.getRiskName());
			template.setRiskTrigger(risk.getRiskTrigger());
			template.setRiskType(risk.getRiskType());
			template.setStatus(risk.getStatus());
			
			riskregistertemplateDAO.makePersistent(template);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return riskregistertemplate;
	}

	/**
	 * Consult risk templates
	 * @param search
	 * @param company
	 * @return
	 * @throws Exception 
	 */
	public List<Riskregistertemplate> find(String search, Company company) throws Exception {
		List<Riskregistertemplate> riskregistertemplates = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			RiskregistertemplateDAO riskregistertemplateDAO = new RiskregistertemplateDAO(session);
			riskregistertemplates = riskregistertemplateDAO.find(search, company);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return riskregistertemplates;
	}

}

