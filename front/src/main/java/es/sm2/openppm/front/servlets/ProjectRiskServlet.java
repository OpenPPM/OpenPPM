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
 * File: ProjectRiskServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.charts.ChartJQPLOT;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.AssumptionLogLogic;
import es.sm2.openppm.core.logic.impl.AssumptionRegisterLogic;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.HistoricriskLogic;
import es.sm2.openppm.core.logic.impl.IssuelogLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.RiskCategoriesLogic;
import es.sm2.openppm.core.logic.impl.RiskReassessmentLogLogic;
import es.sm2.openppm.core.logic.impl.RiskRegisterLogic;
import es.sm2.openppm.core.logic.impl.RiskcategoryLogic;
import es.sm2.openppm.core.logic.impl.RiskregistertemplateLogic;
import es.sm2.openppm.core.model.impl.Assumptionreassessmentlog;
import es.sm2.openppm.core.model.impl.Assumptionregister;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Historicrisk;
import es.sm2.openppm.core.model.impl.Issuelog;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Riskcategories;
import es.sm2.openppm.core.model.impl.Riskcategory;
import es.sm2.openppm.core.model.impl.Riskreassessmentlog;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.impl.Riskregistertemplate;
import es.sm2.openppm.core.utils.JSONModelUtil;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Servlet implementation class ProjectManageRisksServlet
 */
public class ProjectRiskServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
    
	public final static Logger LOGGER = Logger.getLogger(ProjectRiskServlet.class);
	
	public final static String REFERENCE 			= "projectrisk";
	
	/***************** Actions ****************/
	public final static String DELETE_ASSUMPTION		= "delete-assumption";
	public final static String DELETE_RISK				= "delete-risk";
	public final static String DELETE_ISSUE				= "delete-issue";
	public final static String DELETE_LOG_ASSUMPTION	= "delete-log-assumption";
	public final static String ADD_RISK					= "add-risk";
	public final static String EDIT_RISK				= "edit-risk";	
	public final static String SAVE_RISK				= "save-risk";
	public final static String IMPORT_RISKS				= "import-risks";
	public final static String IMPORT_RISK_TEMPLATES	= "import-risk-templates";
	
	/************** Actions AJAX **************/
	public final static String JX_SAVE_ISSUE				= "ajax-save-issue";	
	public final static String JX_SAVE_ASSUMPTION			= "ajax-save-assumption";
	public final static String JX_CONS_LOG_ASSUMPTIONS		= "ajax-cons-log-assumptions";
	public final static String JX_SAVE_ASSUMPTION_LOG		= "ajax-save-assumption-log";
	public final static String JX_SAVE_REASSESSMENT_LOG		= "ajax-save-reassessment-log";
	public final static String JX_CONS_LOG_REASSESSMENT		= "ajax-cons-log-reassessment";
	public final static String JX_DELETE_LOG_REASSESSMENT	= "ajax-delete-log-reassessment";
	public final static String JX_SHOW_HISTORIC_RISK		= "ajax-show-historic-risk";
	public final static String JX_RISK_RATING_CHART			= "ajax-chart-risk-rating";
	public final static String JX_EXPORT_RISK				= "ajax-export-risk";
	public final static String JX_CONS_RISK_TEMPLATES		= "ajax-consult-risk-templates";

	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1) {
			
			/***************** Actions ****************/
			if (accion == null || "".equals(accion)) { viewManageRisk(req, resp); }
			if (DELETE_ASSUMPTION.equals(accion)) { deleteAssumption(req, resp); }
			else if (SAVE_RISK.equals(accion)) { saveRisk(req, resp); }
			else if (ADD_RISK.equals(accion)) { addRisk(req, resp); }
			else if (EDIT_RISK.equals(accion)) { editRisk(req, resp); }
			else if(DELETE_RISK.equals(accion)) { deleteRisk(req, resp); }
			else if (DELETE_ISSUE.equals(accion)) { deleteIssue(req, resp); }
			else if (DELETE_LOG_ASSUMPTION.equals(accion)) { deleteLogAssumption(req, resp); }
			else if (IMPORT_RISKS.equals(accion)) { importRisks(req, resp); }
			else if (IMPORT_RISK_TEMPLATES.equals(accion)) { importRiskTemplates(req, resp); }
			
			/************** Actions AJAX **************/			
			else if (JX_DELETE_LOG_REASSESSMENT.equals(accion)) { deleteLogReassessmentJX(req, resp); }
			else if (JX_SAVE_ASSUMPTION.equals(accion)) { saveAssumptionJX(req, resp); }
			else if (JX_SAVE_ISSUE.equals(accion)) { saveIssueJX(req, resp); }
			else if (JX_CONS_LOG_ASSUMPTIONS.equals(accion)) { consultLogAssumptionJX(req, resp); }			
			else if (JX_SAVE_ASSUMPTION_LOG.equals(accion)) { saveAssumptionLogJX(req, resp); }			
			else if (JX_CONS_LOG_REASSESSMENT.equals(accion)) { consultLogRessessmentJX(req, resp); }
			else if (JX_SAVE_REASSESSMENT_LOG.equals(accion)) { saveLogRessessmentJX(req, resp); }
			else if (JX_SHOW_HISTORIC_RISK.equals(accion)) { showHistoricRiskJX(req, resp); }
			else if (JX_RISK_RATING_CHART.equals(accion)) { riskRatingChartJX(req, resp); }
			else if (JX_EXPORT_RISK.equals(accion)) { exportRiskJX(req, resp); }
			else if (JX_CONS_RISK_TEMPLATES.equals(accion)) { consultRiskTemplatesJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
    }
    
    /**
     * Import templates risk
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void importRiskTemplates(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
    	Integer idProject		= ParamUtil.getInteger(req, "id", null);
    	String idImportTemplates = ParamUtil.getString(req, "idImportRiskTemplates", null);
    
    	Integer[] idImportTemplatesArray	= StringUtil.splitStrToIntegers(idImportTemplates, null);
    	
    	try {
    		if (idProject != null) {
    			
    			// Declare logics
	    		ProjectLogic projectLogic 							= new ProjectLogic(getSettings(req), getResourceBundle(req));
	    		RiskregistertemplateLogic riskregistertemplateLogic = new RiskregistertemplateLogic();
	    		RiskRegisterLogic riskRegisterLogic					= new RiskRegisterLogic();
    		
    			Project actualProject = projectLogic.findById(idProject);
    			
    			if (idImportTemplatesArray != null) {
    				
    				for (int id : idImportTemplatesArray) {
    					
    					// Logic
    					Riskregistertemplate template 	= riskregistertemplateLogic.findById(id);
    					
    					Riskregister riskregister 		= new Riskregister();
    					
    					// Set data 
						//
    					riskregister.setProject(actualProject);
    					riskregister.setDateRaised(new Date());
    					riskregister.setActualMaterializationDelay(template.getActualMaterializationDelay());
    					riskregister.setClosed(template.getClosed());
    					riskregister.setContingencyActionsRequired(template.getContingencyActionsRequired());
    					riskregister.setDescription(template.getDescription());
    					riskregister.setFinalComments(template.getFinalComments());
    					riskregister.setImpact(template.getImpact());
    					riskregister.setMaterialized(template.getMaterialized());
    					riskregister.setMitigationActionsRequired(template.getMitigationActionsRequired());
    					riskregister.setOwner(template.getOwner());
    					riskregister.setPlannedContingencyCost(template.getPlannedContingencyCost());
    					riskregister.setPlannedMitigationCost(template.getPlannedMitigationCost());
    					riskregister.setPotentialCost(template.getPotentialCost());
    					riskregister.setPotentialDelay(template.getPotentialDelay());
    					riskregister.setProbability(template.getProbability());
    					riskregister.setResidualCost(template.getResidualCost());
    					riskregister.setResidualRisk(template.getResidualRisk());
    					riskregister.setResponseDescription(template.getResponseDescription());
    					riskregister.setRiskcategories(template.getRiskcategories());
    					riskregister.setRiskCode(template.getRiskCode());
    					riskregister.setRiskDoc(template.getRiskDoc());
    					riskregister.setRiskName(template.getRiskName());
    					riskregister.setRiskTrigger(template.getRiskTrigger());
    					riskregister.setRiskType(template.getRiskType());
    					riskregister.setStatus(template.getStatus());
    					riskregister.setRiskcategory(template.getRiskcategory());
    					
    					// Logic
    					riskRegisterLogic.saveProjectRisk(riskregister, getUser(req), getResourceBundle(req), false);
    				}
    			}
    		}
    		
    	}
    	catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    	viewManageRisk(req, resp);
	}

	/**
     * Consult risk templates
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void consultRiskTemplatesJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	String search = ParamUtil.getString(req, Riskregistertemplate.RISKNAME, null);
		
		PrintWriter out = resp.getWriter();
		try {
			RiskregistertemplateLogic riskregistertemplateLogic = new RiskregistertemplateLogic();
			
			List<Riskregistertemplate> riskregistertemplates= riskregistertemplateLogic.find(search, getCompany(req));
			
			JSONArray listJSON = new JSONArray();
			
			for (Riskregistertemplate riskregistertemplate : riskregistertemplates) {
				JSONObject itemJSON = new JSONObject();
				
				itemJSON.put(Riskregistertemplate.IDRISK, riskregistertemplate.getIdRisk());
				itemJSON.put(Riskregistertemplate.RISKCODE, riskregistertemplate.getRiskCode());
				itemJSON.put(Riskregistertemplate.RISKNAME, riskregistertemplate.getRiskName());
				itemJSON.put(Riskregistertemplate.RISKTYPE, riskregistertemplate.getRiskType());
				itemJSON.put(Riskregistertemplate.DESCRIPTION, riskregistertemplate.getDescription());
				
				listJSON.add(itemJSON);
			}
			
			out.print(listJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
     * Export risk as template
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void exportRiskJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	Integer idRisk = ParamUtil.getInteger(req, "idRisk", null);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject updateJSON 								= new JSONObject();
			RiskregistertemplateLogic riskregistertemplateLogic = new RiskregistertemplateLogic();
			
			if (idRisk != null) {
				riskregistertemplateLogic.saveRisk(idRisk, getCompany(req));
			}
			
			updateJSON.put("INFO", "Risk template saved");
			
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
     * Import projects risks
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void importRisks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	Integer idProject		= ParamUtil.getInteger(req, "id",null);
    	String idImportProjects = ParamUtil.getString(req, "idImportProjects",null);
    
    	Integer[] idImportProjectsArray	= StringUtil.splitStrToIntegers(idImportProjects, null);
    	
    	try {
    		List<Riskregister> riskregisters = null;
    		
    		ProjectLogic projectLogic 			= new ProjectLogic(getSettings(req), getResourceBundle(req));
    		RiskRegisterLogic riskRegisterLogic = new RiskRegisterLogic();
    		
    		Project actualProject = projectLogic.findById(idProject);
    		
    		if (idImportProjectsArray != null) {
	    		for (int id : idImportProjectsArray) {
	    			riskregisters = riskRegisterLogic.findByRelation(Riskregister.PROJECT, new Project(id));
	    			
	    			for (Riskregister riskregister : riskregisters) {
	    				riskregister.setIdRisk(null);
	    				riskregister.setProject(actualProject);
	    				
	    				riskRegisterLogic.saveProjectRisk(riskregister, getUser(req), getResourceBundle(req), false);	
	    			}
	    		}
    		}
    		
    	}
    	catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    	viewManageRisk(req, resp);
	}

	/**
     * Draw risk rating chart
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void riskRatingChartJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	String idsBySorting 					= ParamUtil.getString(req, "idsBySorting");
    	
    	PrintWriter out 						= resp.getWriter();
    	
    	try {
    		Date minDate 						= null;
    		Date maxDate 						= null;
        	SimpleDateFormat date 				= new SimpleDateFormat("MM/dd/yyyy");
        	SimpleDateFormat tempDate 			= new SimpleDateFormat("MM/dd/yyyy");
        	Random random 						= new Random();
        	Float r 							= null;
    		Float g 							= null;
    		Float b 							= null;
    		String[] defaultColors 				= new String[]{ "#4bb2c5", "#c5b47f", "#EAA228", "#579575", "#839557", "#958c12",
    		        											"#953579", "#4b5de4", "#d8b83f", "#ff5800", "#0085cc"};
    		List<Riskregister> risks 			= new ArrayList<Riskregister>();		
    		Boolean insufficientDates			= true;
    		
    		JSONArray riskRatingValuesJSON 		= new JSONArray();
    		JSONArray riskRatingNamesJSON 		= new JSONArray();
    		JSONArray riskRatingColorsJSON		= new JSONArray();
    		JSONObject updateJSON 				= new JSONObject();

    		RiskRegisterLogic riskRegLogic 		= new RiskRegisterLogic();
    		HistoricriskLogic historicriskLogic	= new HistoricriskLogic();
    		
    		Integer[] idsBySortingList 			= StringUtil.splitStrToIntegers(idsBySorting, null);
			
			if (idsBySortingList != null) {
				for (Integer id : idsBySortingList) {
					risks.add(riskRegLogic.findById(id));
				}
			}
    		
    		//colors
    		for(String defaultColor : defaultColors){
    			riskRatingColorsJSON.add(defaultColor);
    		}
    		
			int varianceColors = risks.size() >  defaultColors.length ? risks.size() - defaultColors.length : 0;
			
			for (int i=0; i < varianceColors; i++) {
				random.setSeed(new Date().getTime());
        		r = random.nextFloat();
        		g = random.nextFloat();
        		b = random.nextFloat();
        		Color randomColor = new Color(r, g, b);
        		String rgb = Integer.toHexString(randomColor.getRGB());
        		rgb = rgb.substring(2, rgb.length());
        		rgb = StringPool.POUND.concat(rgb);
        		
        		riskRatingColorsJSON.add(rgb);
			}
    		
    		
    		for (Riskregister risk: risks) {
    			JSONArray riskJSON = new JSONArray();
    			
    			List<Historicrisk> historicrisks = historicriskLogic.findByRelation(Historicrisk.RISKREGISTER, risk, Historicrisk.ACTUALDATE, Constants.ASCENDENT);
    			
    			if (!historicrisks.isEmpty()) {
    				
	    			String tempDateStr = tempDate.format(historicrisks.get(0).getActualDate());
	    			
	    			for (Historicrisk historicrisk: historicrisks) {
	    				
	    				JSONArray historicriskJSON = new JSONArray();
	    				
	    				historicriskJSON.add(date.format(historicrisk.getActualDate()));
	    				historicriskJSON.add(historicrisk.getProbability() * historicrisk.getImpact());
	    				
	    				if (!tempDateStr.equals(date.format(historicrisk.getActualDate()))) {
	    					insufficientDates = false;
	    				}
	    				
	    				riskJSON.add(historicriskJSON);
	    			}
    			}
    			
    			riskRatingValuesJSON.add(riskJSON);
				riskRatingNamesJSON.add(risk.getRiskCode());
				
				/* min and max dates*/
				if (!insufficientDates) {
					Date tempMinDate = historicrisks.get(0).getActualDate();
					Date tempMaxDate = historicrisks.get(historicrisks.size() - 1).getActualDate();
					
					if (minDate == null && maxDate == null) {
						minDate = tempMinDate;
						maxDate = tempMaxDate;
					}
					else {
						if (tempMinDate.compareTo(minDate) < 0) {
							minDate = tempMinDate;
						}
						if (tempMaxDate.compareTo(maxDate) > 0) {
							maxDate = tempMaxDate;
						}
					}
				}
    		}
    		
    		ChartJQPLOT chart = new ChartJQPLOT(minDate, maxDate);
			updateJSON.put("tickInterval", chart.getTickInterval());
			updateJSON.put("minDate", chart.addDate(minDate, -1));
			updateJSON.put("maxDate", chart.addDate(maxDate, 1));
			
    		updateJSON.put("riskRatingColors", 	riskRatingColorsJSON);
    		updateJSON.put("riskRatingNames", 	riskRatingNamesJSON);
    		updateJSON.put("riskRatingValues", 	riskRatingValuesJSON);
    		updateJSON.put("insufficientDates", insufficientDates);
    		
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
		
	}

	/**
     * Show historic risk
     * @param req
     * @param resp
     * @throws IOException 
     */
	private void showHistoricRiskJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer idRisk = ParamUtil.getInteger(req, "idRisk", null);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject updateJSON 				= new JSONObject();
			HistoricriskLogic historicriskLogic = new HistoricriskLogic();
			RiskRegisterLogic riskRegisterLogic = new RiskRegisterLogic();
			List<String> joins 					= new ArrayList<String>();
			SimpleDateFormat date 				= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			JSONArray historicrisksJSON 		= new JSONArray();
			
			joins.add(Historicrisk.EMPLOYEE);
			joins.add(Historicrisk.EMPLOYEE + "." + Employee.CONTACT);
			
			List<Historicrisk> historicrisks = historicriskLogic.findByRelation(Historicrisk.RISKREGISTER, riskRegisterLogic.findById(idRisk), joins);
			
			for (Historicrisk historicrisk : historicrisks) {
				JSONObject historicriskJSON = new JSONObject();
				
				historicriskJSON.put(Historicrisk.IDHISTORICRISK, historicrisk.getIdHistoricrisk());
				historicriskJSON.put(Historicrisk.PROBABILITY, historicrisk.getProbability());
				historicriskJSON.put(Historicrisk.IMPACT, historicrisk.getImpact());
				historicriskJSON.put(Historicrisk.EMPLOYEE, historicrisk.getEmployee().getContact().getFullName());
				historicriskJSON.put(Historicrisk.ACTUALDATE, date.format(historicrisk.getActualDate()));
				
				historicrisksJSON.add(historicriskJSON);
			}
			
			updateJSON.put("historicrisks", historicrisksJSON);
			
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	private void viewManageRisk(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Integer idProject = ParamUtil.getInteger(req, "id", null);
		
		Project project							= null;
		List<Assumptionregister> assumptions	= null;
		List<Riskregister> risks				= null;
		List<Riskcategories> riskCategories		= null;
		List<Issuelog> issues					= null;
		List<Performingorg> perfOrgs			= null;
		List<Documentproject> docs				= new ArrayList<Documentproject>();
		
		if (idProject == null) {
			idProject = ParamUtil.getInteger(req, "id", (Integer)req.getSession().getAttribute("idProject"));
		}
		
		boolean hasPermission = false;
		
		try {

			hasPermission = SecurityUtil.hasPermission(req, new Project(idProject), Constants.TAB_RISK);
			
			if (hasPermission) {
				
				AssumptionRegisterLogic assumptionRegisterLogic = new AssumptionRegisterLogic();
				DocumentprojectLogic documentprojectLogic 		= new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
				IssuelogLogic issuelogLogic						= new IssuelogLogic();
				ProjectLogic projectLogic 						= new ProjectLogic(getSettings(req), getResourceBundle(req));
				PerformingOrgLogic performingOrgLogic			= new PerformingOrgLogic();
				RiskcategoryLogic riskcategoryLogic = new RiskcategoryLogic();
				
				List<String> joins = new ArrayList<String>();
				joins.add(Project.PROGRAM);
                joins.add(Project.PROGRAM+"."+ Program.EMPLOYEE);
				joins.add(Project.PROGRAM + "." + Program.EMPLOYEE + "." + Employee.CONTACT);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
				
				project = projectLogic.consProject(new Project(idProject), joins);
				
				assumptions = assumptionRegisterLogic.consAssumptions(project);
				
				RiskRegisterLogic riskRegLogic = new RiskRegisterLogic();
				joins = new ArrayList<String>();
				joins.add(Riskregister.RISKCATEGORIES);
				risks = riskRegLogic.findByRelation(Riskregister.PROJECT, project, joins);
				
				RiskCategoriesLogic riskCatLogic = new RiskCategoriesLogic();
				riskCategories = riskCatLogic.findAll();
				
				issues = issuelogLogic.consIssues(project);
				
				perfOrgs  = performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req), Performingorg.NAME, Constants.ASCENDENT);
				
				String documentStorage = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_STORAGE, Settings.DEFAULT_PROJECT_DOCUMENT_STORAGE);
				
				// Get documents
				docs = documentprojectLogic.getDocuments(getSettings(req), project, Constants.DOCUMENT_RISK);
				
				req.setAttribute("risksCategory", riskcategoryLogic.findByRelation(Riskcategory.COMPANY, getCompany(req), Riskcategory.NAME, Order.ASC));
				req.setAttribute("docs", docs);
				req.setAttribute("documentStorage", documentStorage);
			}
		}
		catch (Exception e) {
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}
		
		if (hasPermission) {
			req.setAttribute("project", project);
			req.setAttribute("assumptions", assumptions);
			req.setAttribute("risks", risks);
			req.setAttribute("riskCategories", riskCategories);
			req.setAttribute("issues", issues);
			req.setAttribute("title", ValidateUtil.isNotNull(project.getChartLabel()) ? 
					project.getProjectName() + " ("+project.getChartLabel()+")" : project.getProjectName());
			req.setAttribute("perforgs", perfOrgs);	
			req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
			req.setAttribute("tab", Constants.TAB_RISK);
			forward("/index.jsp?nextForm=project/risk/manage_risks_project", req, resp);
		}
		else {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}
	
	/**
	 * Delete assumption
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteAssumption(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer idAssumption = ParamUtil.get(req, "assumption_id", -1);
		
		try {
			AssumptionRegisterLogic assumptionRegisterLogic = new AssumptionRegisterLogic();
			assumptionRegisterLogic.delete(new Assumptionregister(idAssumption));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewManageRisk(req, resp);
	}
	
	/**
	 * Save planning risk
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveRisk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		boolean saveReassessmentLog	= ParamUtil.getBoolean(req, "saveReassessmentLog", false);
		
		try {
			
			RiskRegisterLogic riskRegisterLogic = new RiskRegisterLogic();
			
			Riskregister risk = setRiskFromRequest(req);
			
			risk = riskRegisterLogic.saveProjectRisk(risk, getUser(req), getResourceBundle(req), saveReassessmentLog);
			
			req.setAttribute("risk", risk);
			req.setAttribute("showRisk", true);	
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		viewManageRisk(req, resp);
	}
	
	/**
	 * Add planning risk
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addRisk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			req.setAttribute("showRisk", true);
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		viewManageRisk(req, resp);
	}
	
	/**
	 * Edit planning risk
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void editRisk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer idRisk		= ParamUtil.getInteger(req, "risk_id", -1);
		
		try {		
			if(idRisk != -1) {
				
				RiskRegisterLogic riskRegisterLogic = new RiskRegisterLogic();
				
				req.setAttribute("risk", riskRegisterLogic.findById(idRisk));
				req.setAttribute("showRisk", true);	
			}				
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewManageRisk(req, resp);
	}
	
	/**
	 * Delete Risk
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteRisk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer idRisk = ParamUtil.get(req, "risk_id", -1);
		
		try {
			RiskRegisterLogic riskRegisterLogic = new RiskRegisterLogic();
			
			riskRegisterLogic.delete(riskRegisterLogic.findById(idRisk));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewManageRisk(req, resp);
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteIssue(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer idIssue = ParamUtil.get(req, "issue_id", -1);
		
		try {
			IssuelogLogic issuelogLogic = new IssuelogLogic();
			
			issuelogLogic.delete(new Issuelog(idIssue));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewManageRisk(req, resp);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteLogAssumption(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer idLog = ParamUtil.get(req, "assumption_log_id", -1);
		
		try {
			AssumptionLogLogic assumptionLogLogic = new AssumptionLogLogic();
			assumptionLogLogic.delete(new Assumptionreassessmentlog(idLog));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewManageRisk(req, resp);
	}
	
	/**
	 * Delete log reassessment
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteLogReassessmentJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		int idLog = ParamUtil.get(req, "reassessment_log_id", -1);
		
		try {	
			RiskReassessmentLogLogic riskReassessmentLogLogic = new RiskReassessmentLogLogic();
			riskReassessmentLogLogic.delete(riskReassessmentLogLogic.findById(idLog));			
			out.print(infoDeleted(getResourceBundle(req), "reassessment log"));
		} 
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}
	
	/**
	 * Save assumption register
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveAssumptionJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		
		try {
			AssumptionRegisterLogic assumptionRegisterLogic = new AssumptionRegisterLogic();
			Assumptionregister assumption = setAssumptionFromRequest(req);
			assumptionRegisterLogic.save(assumption);
			JSONObject assumptionJSON = JSONModelUtil.assumptionToJSON(assumption);
			out.print(assumptionJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * Save issue log
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveIssueJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		
		try {
			IssuelogLogic issuelogLogic = new IssuelogLogic();
			
			Issuelog issue = setIssueFromRequest(req);
			issuelogLogic.save(issue);
			JSONObject issueJSON = JSONModelUtil.issueToJSON(getResourceBundle(req), issue);
			out.print(issueJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * Consult log assumptions of an assumption
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void consultLogAssumptionJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		try {
			AssumptionLogLogic assumptionLogLogic = new AssumptionLogLogic();
			Integer idAssumption = ParamUtil.getInteger(req, "assumption_id");
			List<Assumptionreassessmentlog> assumptionLogs = assumptionLogLogic.consAssumptions(new Assumptionregister(idAssumption));
			
			JSONArray data = new JSONArray();
			for (Assumptionreassessmentlog log : assumptionLogs) {
				data.add(JSONModelUtil.assumptionLogToJSON(getResourceBundle(req), log));
			}
			
			out.print(data);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * Save assumption log
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveAssumptionLogJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		
		try {
			AssumptionLogLogic assumptionLogLogic = new AssumptionLogLogic();
			Assumptionreassessmentlog assumptionLog = setAssumptionLogFromRequest(req);
			assumptionLogLogic.save(assumptionLog);
			JSONObject assumptionLogJSON = JSONModelUtil.assumptionLogToJSON(getResourceBundle(req), assumptionLog);
			out.print(assumptionLogJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * Consult Log ressessment
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void consultLogRessessmentJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer idRisk = ParamUtil.getInteger(req, "risk_id");
		PrintWriter out = resp.getWriter();
		
		try {
			RiskReassessmentLogLogic riskReassessmentLogLogic 	= new RiskReassessmentLogLogic();
			EmployeeLogic employeeLogic 						= new EmployeeLogic();
			
			List<String> joins = new ArrayList<String>();
			joins.add(Employee.CONTACT);
			Employee employee = null;
			
			List<Riskreassessmentlog> reassessmentLogs = riskReassessmentLogLogic.consReassessments(new Riskregister(idRisk));
			
			JSONArray data = new JSONArray();
			
			for (Riskreassessmentlog log : reassessmentLogs) {
				
				if (log.getEmployee() != null) {
					employee = employeeLogic.findById(log.getEmployee().getIdEmployee(), joins);
					data.add(JSONModelUtil.reassessmentLogToJSON(getResourceBundle(req), log, employee.getContact().getFullName()));
				}
				else {
					employee = null;
					data.add(JSONModelUtil.reassessmentLogToJSON(getResourceBundle(req), log, null));
				}
				
			}
			
			out.print(data);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * Save Log ressessment
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveLogRessessmentJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		
		try {
			RiskReassessmentLogLogic riskReassessmentLogLogic 	= new RiskReassessmentLogLogic();
			EmployeeLogic employeeLogic 						= new EmployeeLogic();
			
			
			Riskreassessmentlog riskLog = setRiskReassessmentLogFromRequest(req);
			riskReassessmentLogLogic.save(riskLog);
			
			List<String> joins = new ArrayList<String>();
			joins.add(Employee.CONTACT);
			Contact contact =  employeeLogic.findById(getUser(req).getIdEmployee(), joins).getContact();
			
			JSONObject assumptionLogJSON = JSONModelUtil.reassessmentLogToJSON(getResourceBundle(req), riskLog, contact.getFullName());
			out.print(assumptionLogJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	
    /**
     * Set risk register from request
     * @param req
     * @return
     */
    private Riskregister setRiskFromRequest(HttpServletRequest req) {
    	
		Integer idProject 			= ParamUtil.getInteger(req, "id");
    	Integer idRisk				= ParamUtil.getInteger(req, "risk_id", -1);
    	String code					= ParamUtil.getString(req, "risk_code", null);
    	String name					= ParamUtil.getString(req, "risk_name", null);
    	String owner				= ParamUtil.getString(req, "owner", null);
    	Date dateRaised				= ParamUtil.getDate(req, "date_raised", getDateFormat(req), null);
    	Date dueDate				= ParamUtil.getDate(req, "due_date", getDateFormat(req), null);
    	String description			= ParamUtil.getString(req, "description", null);
    	int probability				= ParamUtil.getInteger(req, "probability", -1);
    	int impact					= ParamUtil.getInteger(req, "impact", -1);
    	String status				= ParamUtil.getString(req, "status", null);
    	double potentialCost		= ParamUtil.getCurrency(req, "potential_cost", -1);
    	Integer potentialDelay		= ParamUtil.getInteger(req, "potential_delay", -1);
    	String riskTrigger			= ParamUtil.getString(req, "risk_trigger", null);
    	Integer riskType			= ParamUtil.getInteger(req, "risk_type", -1);
    	Integer response			= ParamUtil.getInteger(req, "response", -1);
    	String mitigationActions 	= ParamUtil.getString(req, "mitigation_actions", null);
    	double mitigationCost		= ParamUtil.getCurrency(req, "mitigation_cost", -1);
    	String contingencyActions 	= ParamUtil.getString(req, "contingency_actions", null);
    	double contingencyCost		= ParamUtil.getCurrency(req, "contingency_cost", -1);
    	String residualRisk 		= ParamUtil.getString(req, "residual_risk", null);
    	double residualCost			= ParamUtil.getCurrency(req, "residual_cost", -1);
    	boolean materialized		= ParamUtil.getBoolean(req, "materialized", false);
    	Date dateMaterialized 		= ParamUtil.getDate(req, "date_materialized", getDateFormat(req), null);
    	double actualCost			= ParamUtil.getCurrency(req, "actual_cost", -1);
    	Integer actualDelay			= ParamUtil.getInteger(req, "actual_delay", -1);
    	String finalComments		= ParamUtil.getString(req, "final_comments", null);
    	String responseDescription 	= ParamUtil.getString(req, "response_description", null);
    	Integer idRiskcategory		= ParamUtil.getInteger(req, Riskregister.RISKCATEGORY, -1);
    	
    	Riskregister risk = new Riskregister();
    	
    	if (idRisk > 0) {
    		risk.setIdRisk(idRisk);
    	}
    	risk.setProject(new Project(idProject));
    	risk.setRiskCode(code);
    	risk.setRiskName(name);
    	risk.setOwner(owner);
    	risk.setDateRaised(dateRaised);
    	risk.setDueDate(dueDate);
    	risk.setDescription(description);
    	risk.setResponseDescription(responseDescription);
    	if (probability != -1) {
    		risk.setProbability(probability);
    	}
    	else {
    		risk.setProbability(0);
    	}
    	if (impact != -1) {
    		risk.setImpact(impact);
    	}
    	else {
    		risk.setImpact(0);
    	}
    	if (potentialCost != -1) {
    		risk.setPotentialCost(potentialCost);
    	}
    	if (potentialDelay != -1) {
    		risk.setPotentialDelay(potentialDelay);
    	}
    	risk.setRiskTrigger(riskTrigger);
    	if (riskType != -1) {
    		risk.setRiskType(riskType);
    	}
    	if (response != -1) {
    		risk.setRiskcategories(new Riskcategories(response));
    	}
    	risk.setMitigationActionsRequired(mitigationActions);
    	if (mitigationCost != -1) {
    		risk.setPlannedMitigationCost(mitigationCost);
    	}
    	risk.setContingencyActionsRequired(contingencyActions);
    	if (contingencyCost != -1) {
    		risk.setPlannedContingencyCost(contingencyCost);
    	}
    	risk.setResidualRisk(residualRisk);
    	if (residualCost != -1) {
    		risk.setResidualCost(residualCost);
    	}
    	risk.setMaterialized(materialized);
    	risk.setDateMaterialization(dateMaterialized);
    	if (actualCost != -1) {
    		risk.setActualMaterializationCost(actualCost);
    	}
    	if (actualDelay != -1) {
    		risk.setActualMaterializationDelay(actualDelay);
    	}
    	
    	Riskcategory riskcategory = null;
    	if (idRiskcategory != -1) {
    		riskcategory = new Riskcategory(idRiskcategory);
    	}

    	risk.setRiskcategory(riskcategory);
    	risk.setFinalComments(finalComments);
    	risk.setStatus(status);
    	
		return risk;
	}

    
    /**
     * Set risk reassessment log from AJAX Request
     * @param req
     * @return
     */
    private Riskreassessmentlog setRiskReassessmentLogFromRequest(HttpServletRequest req) {
    	Riskreassessmentlog riskLog = new Riskreassessmentlog();
    	
    	Integer idLog 	= ParamUtil.getInteger(req, "reassessment_log_id", -1);
    	Integer idRisk 	= ParamUtil.getInteger(req, "risk_id", -1);
    	Date date 		= ParamUtil.getDate(req, "date", getDateFormat(req), new Date());
    	String change 	= ParamUtil.getString(req, "change", null);
    	
    	if (idLog > 0) {
    		riskLog.setIdLog(idLog);
    	}
    	riskLog.setRiskregister(new Riskregister(idRisk));
    	riskLog.setRiskChange(change);
    	riskLog.setRiskDate(date);
    	riskLog.setEmployee(getUser(req));
    	
		return riskLog;
	}
    

	/**
     * Set assumption log from AJAX Request
     * @param req
     * @return
     */
    private Assumptionreassessmentlog setAssumptionLogFromRequest(
			HttpServletRequest req) {
    	Assumptionreassessmentlog assumptionLog = new Assumptionreassessmentlog();
    	
    	Integer idLog = ParamUtil.getInteger(req, "assumption_log_id", -1);
    	Integer idAssumption = ParamUtil.getInteger(req, "assumption_id", -1);
    	Date date = ParamUtil.getDate(req, "date", getDateFormat(req), null);
    	String change = ParamUtil.getString(req, "change", null);
    	
    	if (idLog > 0) {
    		assumptionLog.setIdLog(idLog);
    	}
    	assumptionLog.setAssumptionregister(new Assumptionregister(idAssumption));
    	assumptionLog.setAssumptionChange(change);
    	assumptionLog.setAssumptionDate(date);
    	
		return assumptionLog;
	}


	/**
     * Set Issue log from AJAX Request
     * @param req
     * @return
     */
	private Issuelog setIssueFromRequest(HttpServletRequest req) {
		Issuelog issue = new Issuelog();
		
		Integer idIssue = ParamUtil.getInteger(req, "issue_id", -1);
		Integer idProject = ParamUtil.getInteger(req, "id", -1);
		String sPriority = ParamUtil.getString(req, "priority", null);
		Date dateLogged = ParamUtil.getDate(req, "date_logged", getDateFormat(req), null);
		Date targetDate = ParamUtil.getDate(req, "target_date", getDateFormat(req), null);
		String assignedTo = ParamUtil.getString(req, "assigned_to", null);
		String description = ParamUtil.getString(req, "description", null);
		String resolution = ParamUtil.getString(req, "resolution", null);
		Date dateClosed = ParamUtil.getDate(req, "date_closed", getDateFormat(req), null);
        String owner = ParamUtil.getString(req, "owner", null);
		
		if (idIssue > -1) {
			issue.setIdIssue(idIssue);
		}
		if (sPriority != null && sPriority.length() > 0) {
			issue.setPriority(sPriority.charAt(0));
		}
		issue.setProject(new Project(idProject));
		issue.setDateLogged(dateLogged);
		issue.setTargetDate(targetDate);
		issue.setAssignedTo(assignedTo);
		issue.setDescription(description);
		issue.setResolution(resolution);
		issue.setDateClosed(dateClosed);
        issue.setOwner(owner);
		
		return issue;
	}

	
	/**
     * Set assumption register from request
     * @param req
     * @return
     */
    private Assumptionregister setAssumptionFromRequest(HttpServletRequest req) {
    	Assumptionregister assumption = new Assumptionregister();
		
		Integer idAssumption = ParamUtil.getInteger(req, "assumption_id", -1);
		Integer idProject = ParamUtil.getInteger(req, "id", -1);
		String code = ParamUtil.getString(req, "code", null);
		String name = ParamUtil.getString(req, "name", null);
		String originator = ParamUtil.getString(req, "originator", null);
		String description = ParamUtil.getString(req, "description", null);
		String documentation = ParamUtil.getString(req, "doc", null);
		
		if (idAssumption > -1) {
			assumption.setIdAssumption(idAssumption);
		}
		assumption.setProject(new Project(idProject));
		assumption.setDescription(description);
		assumption.setAssumptionCode(code);
		assumption.setAssumptionName(name);
		assumption.setOriginator(originator);
		assumption.setAssumptionDoc(documentation);
		
		return assumption;
	}
}
