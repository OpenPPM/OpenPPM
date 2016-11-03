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
 * Module: front
 * File: RiskTemplatesServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.RiskCategoriesLogic;
import es.sm2.openppm.core.logic.impl.RiskcategoryLogic;
import es.sm2.openppm.core.logic.impl.RiskregistertemplateLogic;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.model.impl.Riskcategories;
import es.sm2.openppm.core.model.impl.Riskcategory;
import es.sm2.openppm.core.model.impl.Riskregistertemplate;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;

/**
 * Servlet implementation class RiskTemplates
 */
public class RiskTemplatesServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(RiskTemplatesServlet.class);
	
	public final static String REFERENCE = "risktemplates";
	
	/***************** Actions ****************/
	public final static String VIEW_RISK_TEMPLATE 		= "view-risk-template";
	public final static String SAVE_RISK_TEMPLATE 		= "save-risk-template";
	
	/************** Actions AJAX **************/
	public final static String JX_DELETE_RISK_TEMPLATE 	= "ajax-delete-risk-template";
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
    	String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, REFERENCE))) {
			
			/***************** Actions ****************/
			if (accion == null || StringPool.BLANK.equals(accion)) { consRiskTemplates(req, resp); }
			else if (VIEW_RISK_TEMPLATE.equals(accion)) { viewRiskTemplate(req, resp, null); }
			else if (SAVE_RISK_TEMPLATE.equals(accion)) { saveRiskTemplate(req, resp); }
			
			/************** Actions AJAX **************/
			else if (JX_DELETE_RISK_TEMPLATE.equals(accion)) { deleteRiskTemplateJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE + "?accion=403", req, resp);
		}
	}

	/**
	 * Consult risk templates
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@SuppressWarnings("rawtypes")
	private void consRiskTemplates(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List list = null;
		
		try {
			
			RiskregistertemplateLogic riskregistertemplateLogic = new RiskregistertemplateLogic();
			List<String> joins 									= new ArrayList<String>();
			
			joins.add(Riskregistertemplate.RISKCATEGORIES);
			
			list = riskregistertemplateLogic.findByRelation(Riskregistertemplate.COMPANY, getCompany(req), joins);
		}
    	catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("list", list);
		req.setAttribute("idManten", Constants.MANT_RISK_TEMPLATES);
		req.setAttribute("title", getResourceBundle(req).getString("menu.maintenance"));
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		forward("/index.jsp?nextForm=maintenance/maintenance", req, resp);
	}

	/**
	 * Save risk template
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveRiskTemplate(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		Riskregistertemplate riskTemplate = new Riskregistertemplate();
		
    	try {
    		
    		RiskregistertemplateLogic riskregistertemplateLogic = new RiskregistertemplateLogic();
    		
    		riskTemplate = setRiskTemplateFromRequest(req);
    		
    		if (riskTemplate.getIdRisk() == null) {
    			infoCreated(req, "risk_template");
    		}
    		else {
    			infoUpdated(req, "risk_template");
    		}
    		
    		riskTemplate = riskregistertemplateLogic.save(riskTemplate);
		}
    	catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    	
    	viewRiskTemplate(req, resp, riskTemplate.getIdRisk());
	}

	/**
	 * Set risk template from request
	 * @param req
	 * @return
	 */
	private Riskregistertemplate setRiskTemplateFromRequest(HttpServletRequest req) {
		
    	Integer idRisk				= ParamUtil.getInteger(req, Riskregistertemplate.IDRISK, -1);
    	String code					= ParamUtil.getString(req, Riskregistertemplate.RISKCODE, null);
    	String name					= ParamUtil.getString(req, Riskregistertemplate.RISKNAME, null);
    	String owner				= ParamUtil.getString(req, Riskregistertemplate.OWNER, null);
    	String description			= ParamUtil.getString(req, Riskregistertemplate.DESCRIPTION, null);
    	int probability				= ParamUtil.getInteger(req, Riskregistertemplate.PROBABILITY, -1);
    	int impact					= ParamUtil.getInteger(req, Riskregistertemplate.IMPACT, -1);
    	String status				= ParamUtil.getString(req, Riskregistertemplate.STATUS, null);
    	double potentialCost		= ParamUtil.getCurrency(req, Riskregistertemplate.POTENTIALCOST, -1);
    	Integer potentialDelay		= ParamUtil.getInteger(req, Riskregistertemplate.POTENTIALDELAY, -1);
    	String riskTrigger			= ParamUtil.getString(req, Riskregistertemplate.RISKTRIGGER, null);
    	Integer riskType			= ParamUtil.getInteger(req, Riskregistertemplate.RISKTYPE, -1);
    	Integer response			= ParamUtil.getInteger(req, "response", -1);
    	String mitigationActions 	= ParamUtil.getString(req, Riskregistertemplate.MITIGATIONACTIONSREQUIRED, null);
    	double mitigationCost		= ParamUtil.getCurrency(req, Riskregistertemplate.PLANNEDMITIGATIONCOST, -1);
    	String contingencyActions 	= ParamUtil.getString(req, Riskregistertemplate.CONTINGENCYACTIONSREQUIRED, null);
    	double contingencyCost		= ParamUtil.getCurrency(req, Riskregistertemplate.PLANNEDCONTINGENCYCOST, -1);
    	String residualRisk 		= ParamUtil.getString(req, Riskregistertemplate.RESIDUALRISK, null);
    	double residualCost			= ParamUtil.getCurrency(req, Riskregistertemplate.RESIDUALCOST, -1);
    	boolean materialized		= ParamUtil.getBoolean(req, Riskregistertemplate.MATERIALIZED, false);
    	double actualCost			= ParamUtil.getCurrency(req, Riskregistertemplate.ACTUALMATERIALIZATIONCOST, -1);
    	Integer actualDelay			= ParamUtil.getInteger(req, Riskregistertemplate.ACTUALMATERIALIZATIONDELAY, -1);
    	String finalComments		= ParamUtil.getString(req, Riskregistertemplate.FINALCOMMENTS, null);
    	String responseDescription 	= ParamUtil.getString(req, Riskregistertemplate.RESPONSEDESCRIPTION, null);
    	Integer idRiskCategory		= ParamUtil.getInteger(req, Riskregistertemplate.RISKCATEGORY, -1);
    	    	
    	Riskregistertemplate riskTemplate 	= new Riskregistertemplate();
    	
    	if (idRisk > 0) {
    		riskTemplate.setIdRisk(idRisk);
    	}
    	riskTemplate.setCompany(getCompany(req));
    	riskTemplate.setRiskCode(code);
    	riskTemplate.setRiskName(name);
    	riskTemplate.setOwner(owner);
    	riskTemplate.setDescription(description);
    	riskTemplate.setResponseDescription(responseDescription);
    	if (probability != -1) {
    		riskTemplate.setProbability(probability);
    	}
    	else {
    		riskTemplate.setProbability(0);
    	}
    	if (impact != -1) {
    		riskTemplate.setImpact(impact);
    	}
    	else {
    		riskTemplate.setImpact(0);
    	}
    	if (potentialCost != -1) {
    		riskTemplate.setPotentialCost(potentialCost);
    	}
    	if (potentialDelay != -1) {
    		riskTemplate.setPotentialDelay(potentialDelay);
    	}
    	riskTemplate.setRiskTrigger(riskTrigger);
    	if (riskType != -1) {
    		riskTemplate.setRiskType(riskType);
    	}
    	if (response != -1) {
    		riskTemplate.setRiskcategories(new Riskcategories(response));
    	}
    	riskTemplate.setMitigationActionsRequired(mitigationActions);
    	if (mitigationCost != -1) {
    		riskTemplate.setPlannedMitigationCost(mitigationCost);
    	}
    	riskTemplate.setContingencyActionsRequired(contingencyActions);
    	if (contingencyCost != -1) {
    		riskTemplate.setPlannedContingencyCost(contingencyCost);
    	}
    	riskTemplate.setResidualRisk(residualRisk);
    	if (residualCost != -1) {
    		riskTemplate.setResidualCost(residualCost);
    	}
    	riskTemplate.setMaterialized(materialized);
    	if (actualCost != -1) {
    		riskTemplate.setActualMaterializationCost(actualCost);
    	}
    	if (actualDelay != -1) {
    		riskTemplate.setActualMaterializationDelay(actualDelay);
    	}
    	riskTemplate.setFinalComments(finalComments);
    	riskTemplate.setStatus(status);
    	if (idRiskCategory != -1) {
    		riskTemplate.setRiskcategory(new Riskcategory(idRiskCategory));
    	}
    	
		return riskTemplate;
	}

	/**
	 * Delete risk template
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void deleteRiskTemplateJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idRisk = ParamUtil.getInteger(req, Riskregistertemplate.IDRISK, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			
			JSONObject infoJSON	= null;
			
			if (idRisk != -1) {
				
				RiskregistertemplateLogic riskregistertemplateLogic = new RiskregistertemplateLogic();
				
				Riskregistertemplate riskregistertemplate = new Riskregistertemplate();
				
				riskregistertemplate = riskregistertemplateLogic.findById(idRisk);
				
				riskregistertemplateLogic.delete(riskregistertemplate);
				
				infoJSON = infoDeleted(getResourceBundle(req), "risk_template");
			}
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}
	
	/**
	 * View risk template
	 * @param req
	 * @param resp
	 */
	private void viewRiskTemplate(HttpServletRequest req, HttpServletResponse resp, Integer idRisk) throws ServletException, IOException {
		
		if (idRisk == null) {
			idRisk = ParamUtil.getInteger(req, Riskregistertemplate.IDRISK, null);
		}
		
		try {
			
			// Declare logics
			RiskCategoriesLogic riskCatLogic 	= new RiskCategoriesLogic();
			RiskcategoryLogic riskcategoryLogic = new RiskcategoryLogic();
			
			if (idRisk != null) {
				
				RiskregistertemplateLogic riskregistertemplateLogic = new RiskregistertemplateLogic();

				req.setAttribute("risk", riskregistertemplateLogic.findById(idRisk));
			}
			
			req.setAttribute("risksCategory", riskcategoryLogic.findByRelation(Riskcategory.COMPANY, getCompany(req), Riskcategory.NAME, Order.ASC));
			req.setAttribute("riskCategories", riskCatLogic.findAll());
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("title", getResourceBundle(req).getString("menu.maintenance"));
		forward("/index.jsp?nextForm=maintenance/risktemplates/risk_template", req, resp);
	}
   
}