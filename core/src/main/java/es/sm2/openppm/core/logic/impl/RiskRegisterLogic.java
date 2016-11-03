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
 * File: RiskRegisterLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rendersnake.HtmlAttributes;
import org.rendersnake.HtmlCanvas;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.HistoricriskDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.dao.RiskCategoriesDAO;
import es.sm2.openppm.core.dao.RiskReassessmentLogDAO;
import es.sm2.openppm.core.dao.RiskRegisterDAO;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Historicrisk;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Riskcategories;
import es.sm2.openppm.core.model.impl.Riskreassessmentlog;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class RiskRegisterLogic extends AbstractGenericLogic<Riskregister, Integer> {

	/**
	 * Save project risk
	 * @param kpi
	 * @param user 
	 * @param saveReassessmentLog 
	 * @throws Exception 
	 */
	public Riskregister saveProjectRisk(Riskregister risk, Employee user, ResourceBundle idioma, boolean saveReassessmentLog) throws Exception {
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			RiskRegisterDAO riskRegisterDAO = new RiskRegisterDAO(session);
			HistoricriskDAO historicriskDAO	= new HistoricriskDAO(session);
			
			// If update
			if (risk.getIdRisk() != null) { 
				// Save risk reassessments
		    	if (saveReassessmentLog == true) {
		    		RiskReassessmentLogDAO riskReassessmentLogDAO = new RiskReassessmentLogDAO(session);
		    		
					Riskregister riskPrevious  = riskRegisterDAO.findById(risk.getIdRisk());
		    		
		    		List<String> changes = changesInRisk(riskPrevious, risk, idioma, session);
		    		
		    		for (String change : changes) {
		    			Riskreassessmentlog riskreassessmentlog = new Riskreassessmentlog();
		    			
		    			riskreassessmentlog.setRiskregister(risk);
		    			riskreassessmentlog.setRiskDate(new Date());
		    			riskreassessmentlog.setRiskChange(change);
		    			riskreassessmentlog.setEmployee(user);
		    			
		    			riskReassessmentLogDAO.makePersistent(riskreassessmentlog);
		    		}
		    	}
			}
			
			// Save risk
			risk = (Riskregister) session.merge(risk);
			
			// Save historic
			Historicrisk historicriskLastDate = historicriskDAO.findbyIdRiskAndLastDate(risk);
			
			if (historicriskLastDate == null || 
				(!historicriskLastDate.getProbability().equals(risk.getProbability()) || !historicriskLastDate.getImpact().equals(risk.getImpact()))) {
				Historicrisk historicrisk = new Historicrisk();
				
				historicrisk.setRiskregister(risk);
				historicrisk.setProbability(risk.getProbability());
				historicrisk.setImpact(risk.getImpact());
				historicrisk.setEmployee(user);
				historicrisk.setActualDate(new Date());
				
				historicriskDAO.makePersistent(historicrisk);
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
		return risk;
	}

	/**
	 * Compare actual risk with previous risk and return changes
	 * @param idioma 
	 * @param risk
	 * @param session 
	 * @return
	 * @throws Exception 
	 */
	private List<String> changesInRisk(Riskregister riskPrevious, Riskregister riskActual, ResourceBundle idioma, Session session) throws Exception  {
		List <String> changes = new ArrayList<String>();
		
		if (riskPrevious.getStatus() != null && !riskPrevious.getStatus().equals(riskActual.getStatus())) {
			if (riskActual.getStatus().equals(Constants.CHAR_CLOSED)) {
				changes.add(idioma.getString("risk.status") + StringPool.COLON_SPACE + idioma.getString("risk.status.closed"));
			}
			else if (riskActual.getStatus().equals(Constants.CHAR_OPEN)) {
				changes.add(idioma.getString("risk.status") + StringPool.COLON_SPACE + idioma.getString("risk.status.open"));
			}
			
		}
		if (riskPrevious.getRiskType() != null && !riskPrevious.getRiskType().equals(riskActual.getRiskType())) {
			
			if (riskActual.getRiskType().equals(Constants.RISK_OPPORTUNITY)) {
				changes.add(idioma.getString("risk.type") + StringPool.COLON_SPACE + idioma.getString("risk.type.opportunity"));
			}
			else if (riskActual.getRiskType().equals(Constants.RISK_THREAT)) {
				changes.add(idioma.getString("risk.type") + StringPool.COLON_SPACE + idioma.getString("risk.type.threat"));
			}
			
		}
		if (riskPrevious.getRiskcategories() != null && !riskPrevious.getRiskcategories().equals(riskActual.getRiskcategories())) {
			RiskCategoriesDAO riskCategoriesDAO = new RiskCategoriesDAO(session);
			Riskcategories riskcategories 		= riskCategoriesDAO.findById(riskPrevious.getRiskcategories().getIdRiskCategory());
			
			changes.add(idioma.getString("risk.response") + StringPool.COLON_SPACE + riskcategories.getDescription());
		}
			
		return changes;
		
	}

	/**
	 * Find by project and open risk
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public List<Riskregister> findByProjectAndOpen(Project project) throws Exception {
		List<Riskregister> riskregisters = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			RiskRegisterDAO riskRegisterDAO = new RiskRegisterDAO(session);
			riskregisters = riskRegisterDAO.findByProjectAndOpen(project);
			
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
		
		return riskregisters;
	}

	/**
	 * Generate risk report
	 * 
	 * @param idsProjects
	 * @param resourceBundle 
	 * @return
	 * @throws Exception 
	 */
	public JSONArray generateRiskReport(Integer[] idsProjects, ResourceBundle resourceBundle) throws Exception {
		
		JSONArray returnJSON = new JSONArray();
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			// Declare DAO
			RiskRegisterDAO riskRegisterDAO = new RiskRegisterDAO(session);
			ProjectDAO projectDAO			= new ProjectDAO(session);
			
			// DAO get data projects
			List<Project> projects = projectDAO.consList(idsProjects, Project.PROJECTNAME, Order.ASC, null); 
			//TODO no se puede ordenar la tabla segun estos criterios por culpa de la ordenacion propia de la tabla js
			
			// Joins 
			List<String> joins = new ArrayList<String>();
			joins.add(Riskregister.RISKCATEGORIES);
			
			List<Riskregister> risks = null;
			
			if (ValidateUtil.isNotNull(projects)) {
				
				for (Project project : projects) {
					
					// DAO get data risk
					risks = riskRegisterDAO.findByRelation(Riskregister.PROJECT, project, joins);
					
					// Set data risk 
					//
					if (ValidateUtil.isNotNull(risks)) {
						
						for (Riskregister risk : risks) {
							
							JSONObject riskJSON = new JSONObject();
							
							riskJSON.put("projectName", ValidateUtil.isNotNull(project.getProjectName()) ? project.getProjectName() : StringPool.BLANK);
							riskJSON.put("projectID", project.getIdProject());
							riskJSON.put("riskCode", ValidateUtil.isNotNull(risk.getRiskCode()) ? risk.getRiskCode() : StringPool.BLANK);
							riskJSON.put("riskName", ValidateUtil.isNotNull(risk.getRiskName()) ? risk.getRiskName() : StringPool.BLANK);
							riskJSON.put("riskRaisedDate", risk.getDateRaised() != null ? DateUtil.format(Constants.DATE_PATTERN, risk.getDateRaised()) 
									: StringPool.BLANK);
							riskJSON.put("riskDueDate", risk.getDueDate()!= null ? DateUtil.format(Constants.DATE_PATTERN, risk.getDueDate()) 
									: StringPool.BLANK);
							
							String riskStatus = StringPool.BLANK;
							if (ValidateUtil.isNotNull(risk.getStatus())) {
								
								if (Constants.CHAR_CLOSED.equals(risk.getStatus())) {
									riskStatus = resourceBundle.getString("risk.status.closed");
								}
								else if (Constants.CHAR_OPEN.equals(risk.getStatus())) {
									riskStatus = resourceBundle.getString("risk.status.open");
								}
							}
							
							riskJSON.put("riskStatus", riskStatus);
							
							// Set riskrating
							//
							String classRiskRating = StringPool.BLANK;
							
							if (risk.getRiskRating() >= 1500) {
								classRiskRating = "risk_high";
							}
							else if (risk.getRiskRating() >= 500) {
								classRiskRating = "risk_medium";
							}
							else if (risk.getRiskRating() != 0) {
								classRiskRating = "risk_low";
							}
							
							HtmlAttributes attributesHiddenDiv = new HtmlAttributes();
							attributesHiddenDiv.class_("riskValue");
							attributesHiddenDiv.style("display:none;");
							
							HtmlAttributes attributesColorDiv = new HtmlAttributes();
							attributesColorDiv.class_("riskRating " + classRiskRating);
							attributesColorDiv.style("width: 25%; float: left; margin-left: 10px;");
							
							HtmlAttributes attributesValueDiv = new HtmlAttributes();
							attributesValueDiv.style("float: left; margin-left: 10px;");
							
							HtmlAttributes attributesContent = new HtmlAttributes();
							attributesContent.style("margin: 0px auto; width: 80px;");
							
							String riskRating =  risk.getRiskRating() != 0 ? 
									StringPool.OPEN_PARENTHESIS + risk.getRiskRating() + StringPool.CLOSE_PARENTHESIS : StringPool.BLANK;
							
							String contentDiv = new HtmlCanvas()
									.div(attributesContent)
										.div(attributesHiddenDiv)
											.content(String.valueOf(risk.getRiskRating()))
										.div(attributesColorDiv)
											.content(StringPool.SPACE)
										.div(attributesValueDiv)
											.content(riskRating)
			                    .toHtml();
							
							
							riskJSON.put("riskRating", contentDiv);
							
							riskJSON.put("riskDescription", risk.getRiskcategories() != null && 
									ValidateUtil.isNotNull(risk.getRiskcategories().getDescription()) ? 
											risk.getRiskcategories().getDescription() : StringPool.BLANK);
							
							// Set Button 
							//
							HtmlAttributes attributesImg = new HtmlAttributes();
							attributesImg.class_("link");
							attributesImg.src("images/view.png");
							attributesImg.title(resourceBundle.getString("view"));
							attributesImg.onClick("viewProjectTab" + StringPool.OPEN_PARENTHESIS + project.getIdProject() + 
									StringPool.COMMA + Constants.TAB_RISK + StringPool.CLOSE_PARENTHESIS + StringPool.SEMICOLON);
							
							String button = new HtmlCanvas()
									.img(attributesImg)
			                    .toHtml();
							
							riskJSON.put("buttons", button);
							
							returnJSON.add(riskJSON);
						}
					}
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
		
		return returnJSON;
	}
	
	/**
	 * Check for not closed risks
	 * 
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public boolean risksNotClosed(Project project) throws Exception {
		
		boolean notClosed = false;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
		
			tx = session.beginTransaction();
			
			RiskRegisterDAO dao = new RiskRegisterDAO(session);
			notClosed = dao.risksNotClosed(project);
			
			tx.commit();
		}
		catch (Exception e) { e.printStackTrace();if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return notClosed;
	}
}