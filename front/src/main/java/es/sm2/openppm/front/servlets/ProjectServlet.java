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
 * File: ProjectServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.logic.charter.ProjectCharterTemplate;
import es.sm2.openppm.core.logic.charter.ProjectExecutiveTemplate;
import es.sm2.openppm.core.logic.impl.CategoryLogic;
import es.sm2.openppm.core.logic.impl.ClassificationlevelLogic;
import es.sm2.openppm.core.logic.impl.ClosurecheckprojectLogic;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.ContractTypeLogic;
import es.sm2.openppm.core.logic.impl.CustomerLogic;
import es.sm2.openppm.core.logic.impl.CustomertypeLogic;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.FundingsourceLogic;
import es.sm2.openppm.core.logic.impl.GeographyLogic;
import es.sm2.openppm.core.logic.impl.LabelLogic;
import es.sm2.openppm.core.logic.impl.LearnedLessonProjectLogic;
import es.sm2.openppm.core.logic.impl.LogprojectstatusLogic;
import es.sm2.openppm.core.logic.impl.MilestonecategoryLogic;
import es.sm2.openppm.core.logic.impl.MilestonetypeLogic;
import es.sm2.openppm.core.logic.impl.NotificationSpecificLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.logic.impl.ProjectCharterLogic;
import es.sm2.openppm.core.logic.impl.ProjectKpiLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.ResourceProfilesLogic;
import es.sm2.openppm.core.logic.impl.ResourcepoolLogic;
import es.sm2.openppm.core.logic.impl.RiskcategoryLogic;
import es.sm2.openppm.core.logic.impl.SellerLogic;
import es.sm2.openppm.core.logic.impl.StagegateLogic;
import es.sm2.openppm.core.logic.impl.TeamMemberLogic;
import es.sm2.openppm.core.logic.impl.TechnologyLogic;
import es.sm2.openppm.core.logic.impl.TimelineLogic;
import es.sm2.openppm.core.logic.setting.GeneralSetting;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Classificationlevel;
import es.sm2.openppm.core.model.impl.Closurecheckproject;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Customertype;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Documentproject.DocumentType;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.Label;
import es.sm2.openppm.core.model.impl.LearnedLessonProject;
import es.sm2.openppm.core.model.impl.Logprojectstatus;
import es.sm2.openppm.core.model.impl.Milestonecategory;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Projectkpi;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Riskcategory;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Stagegate;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Technology;
import es.sm2.openppm.core.model.impl.Timeline;
import es.sm2.openppm.core.model.search.ProjectSearch;
import es.sm2.openppm.core.model.wrap.ProjectWrap;
import es.sm2.openppm.core.reports.GenerateReportInterface;
import es.sm2.openppm.core.reports.ReportUtil;
import es.sm2.openppm.core.reports.annotations.ReportType;
import es.sm2.openppm.core.reports.beans.ReportFile;
import es.sm2.openppm.core.reports.beans.ReportParams;
import es.sm2.openppm.core.utils.DataUtil;
import es.sm2.openppm.core.utils.JSONModelUtil;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.utils.StringUtils;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.RequestUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.HtmlUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.CsvFile;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;
import es.sm2.openppm.utils.json.Exclusion;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Servlet implementation class ProjectServer
 */
public class ProjectServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(ProjectServlet.class);
	
	public final static String REFERENCE = "projects";
	
	/***************** Actions ****************/
	public final static String NEW_PROJECT					= "new-project";
	public final static String NEW_INVERTMENT				= "new-investment";
	public final static String CREATE_PROJECT				= "create-project";
	public final static String LIST_PROJECTS				= "list-projects";
	public final static String LIST_INVESTMENTS				= "list-investments";
	public final static String DELETE_PROJECT				= "delete-project";
	public final static String GENERATE_STATUS_REPORT		= "generate-status-report";
	public final static String GENERATE_EXECUTIVE_REPORT	= "generate-executive-report";
	public final static String DOWNLOAD_DOC					= "download-document";
	public final static String REPORT_PROJECT_RESOURCE_CSV			= "report-project-resource-csv";
	public final static String REPORT_PROJECT_MONTH_CSV				= "report-project-month-csv";
	public final static String REPORT_PROJECT_ACTIVITY_MONTH_CSV	= "report-project-activity-month-csv";
	public final static String REPORT_PROJECT_ACTIVITY_RESOURCE_CSV	= "report-project-activity-resource-csv";
	
	
	/************** Actions AJAX **************/
	public final static String JX_CHECK_CODE							= "ajax-check-code";
	public final static String JX_SAVE_CUSTOMER							= "ajax-save-customer";	
	public final static String JX_SAVE_DOCUMENT							= "save-document-jx";
	public final static String JX_CONS_PROJECT							= "cons-project-jx";
	public final static String JX_APPROVE_INVESTMENT					= "approve-investment-jx";
	public final static String JX_REJECT_INVESTMENT						= "reject-investment-jx";
	public final static String JX_CANCEL_INVESTMENT						= "cancel-investment-jx";
	public final static String JX_RESET_INVESTMENT						= "reset-investment-jx";
	public final static String JX_FILTER_TABLE							= "filter-table-projects-jx";
	public final static String JX_FILTER_TABLE_INVESTMENTS				= "filter-table-investments-jx";
	public final static String JX_UPLOAD_FILESYSTEM						= "upload-file-system-jx";	
	public final static String JX_DELETE_DOC							= "delete-document-jx";
	public final static String JX_RESOURCE_CAPACITY_RUNNING				= "resource_capacity_running-jx";
	public final static String JX_REPORT_PROJECT_MONTH					= "report_project_month";
	public final static String JX_REPORT_PROJECT_ACTIVITY_RESSOURCE 	= "report_project_activity_ressource";
	public final static String JX_REPORT_PROJECT_ACTIVITY_MONTH 		= "report_project_acitvity_month";
	public final static String JX_SHOW_AGGREGATE_KPIS					= "show-aggregate-kpis-jx";
	public final static String JX_UPDATE_PRIORITY_ADJUSTMENT			= "update_priority_adjustment_jx";
	
    
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");

		if (SecurityUtil.consUserRole(req) != -1) {
			if (ServletFileUpload.isMultipartContent(req) && StringPool.BLANK.equals(accion)) {
                accion = getMultipartField("accion");
			}		
			
			LOGGER.debug("Accion: " + accion);
			
			if (SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, LIST_PROJECTS))) {
				
				/***************** Actions ****************/
				if (ValidateUtil.isNull(accion) || LIST_PROJECTS.equals(accion) || LIST_INVESTMENTS.equals(accion)) {
					viewProjects(req, resp, accion);
				}
				else if (NEW_PROJECT.equals(accion) || NEW_INVERTMENT.equals(accion)) { viewCreateProjectForm(req, resp, accion); }
				else if (DELETE_PROJECT.equals(accion)) { deleteProject(req, resp); }
				else if (CREATE_PROJECT.equals(accion)) { createProject(req, resp); }
				else if (GENERATE_STATUS_REPORT.equals(accion)) { generateStatusReport(req, resp); }
				else if (GENERATE_EXECUTIVE_REPORT.equals(accion)) { generateExecutiveReport(req, resp); }
				else if (DOWNLOAD_DOC.equals(accion)) {downloadDocument(req, resp);}
				else if (REPORT_PROJECT_RESOURCE_CSV.equals(accion)) { reportProjectResourceCSV(req, resp); }
				else if (REPORT_PROJECT_MONTH_CSV.equals(accion)) { reportProjectMonthCSV(req, resp); }
				else if (REPORT_PROJECT_ACTIVITY_MONTH_CSV.equals(accion)) { reportProjectActivityMonthCSV(req, resp); }
				else if (REPORT_PROJECT_ACTIVITY_RESOURCE_CSV.equals(accion)) { reportProjectActivityResourceCSV(req, resp); }
	
				/*********** Actions AJAX ************/
				else if (JX_SAVE_DOCUMENT.equals(accion)) { saveDocumentJX(req,resp); }
				else if (JX_CHECK_CODE.equals(accion)) { checkCodeJX(req, resp); }
				else if (JX_SAVE_CUSTOMER.equals(accion)) { saveCustomerJX(req, resp); }			
				else if (JX_CONS_PROJECT.equals(accion)) { consProjectJX(req, resp); }
				else if (JX_APPROVE_INVESTMENT.equals(accion)) { approveInvestmentJX(req, resp); }
				else if (JX_REJECT_INVESTMENT.equals(accion)) { rejectInvestmentJX(req, resp); }
				else if (JX_CANCEL_INVESTMENT.equals(accion)) { cancelInvestmentJX(req, resp); }
				else if (JX_RESET_INVESTMENT.equals(accion)) { resetInvestmentJX(req, resp); }
				else if (JX_FILTER_TABLE.equals(accion)) { filterTableProjectsJX(req, resp); }
				else if (JX_FILTER_TABLE_INVESTMENTS.equals(accion)) { filterTableInvestmentsJX(req, resp); }
				else if (JX_UPLOAD_FILESYSTEM.equals(accion)) { uploadFileSystem(req, resp); }
				else if (JX_DELETE_DOC.equals(accion)) { deleteDocument(req, resp); }
				else if (JX_RESOURCE_CAPACITY_RUNNING.equals(accion)) { showResourceCapacityRuning(req, resp); }
				else if (JX_REPORT_PROJECT_MONTH.equals(accion)) {showReportProjectMonth(req, resp); }
				else if (JX_REPORT_PROJECT_ACTIVITY_RESSOURCE.equals(accion)) {showReportProjectActivityRessource(req, resp);}
				else if (JX_REPORT_PROJECT_ACTIVITY_MONTH.equals(accion)) {showReportProjectActivityMonth(req, resp);}
				else if (JX_SHOW_AGGREGATE_KPIS.equals(accion)) { showAggregateKpisJX(req, resp); }
				else if (JX_UPDATE_PRIORITY_ADJUSTMENT.equals(accion)) { updatePriorityAdjustmentJX(req, resp); }
				
			}
			else {
				forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
			}
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Update priority adjustment
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void updatePriorityAdjustmentJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

    	String idStr 	= ParamUtil.getString(req, "ids");
		Integer[] ids 	= StringUtil.splitStrToIntegers(idStr, null);
		
		PrintWriter out = resp.getWriter();
		
		try {
			ProjectLogic logic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			if (ValidateUtil.isNotNull(ids)) {
				
				for (Integer id : ids) {
					
					// Find project
					Project project = logic.findById(id);
					
					// Get data
					Integer riskRatingAdjustament 	= ParamUtil.getInteger(req, "totalRiskRating"+id, 0);
					Integer strategicAdjustament 	= ParamUtil.getInteger(req, "totalAdjustment"+id, 0);
					Boolean useRiskAdjust			= ParamUtil.getBoolean(req, "useRiskAdjust"+id);
					Boolean useStrategicAdjust		= ParamUtil.getBoolean(req, "useStrategicAdjust"+id);
					
					// Change data
					project.setRiskRatingAdjustament(useRiskAdjust ? riskRatingAdjustament : null);
					project.setUseRiskAdjust(useRiskAdjust);
					
					project.setStrategicAdjustament(useStrategicAdjust ? strategicAdjustament : null);
					project.setUseStrategicAdjust(useStrategicAdjust);
					
					// Save changes
					logic.save(project);
				}
			}
			out.print(infoUpdated(getResourceBundle(req), null, "priorityAdjustment"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Show aggregate kpis
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
	private void showAggregateKpisJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String idStr 	= ParamUtil.getString(req, "ids");
		Integer[] ids 	= StringUtil.splitStrToIntegers(idStr, null);
		
		PrintWriter out = resp.getWriter();
		
		try {
			ProjectKpiLogic projectKpiLogic = new ProjectKpiLogic();
			
			List<Projectkpi> projectkpis = null;
					
			if (ids == null) {
				projectkpis = new ArrayList<Projectkpi>();
			}
			else {
				projectkpis = projectKpiLogic.getAggregates(ids);
			}
			
			out.print(JsonUtil.toJSON(projectkpis, new Exclusion(Company.class)));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
		
	}

	/**
	 * Export report project activity resource to csv
	 * 
	 * @param req
	 * @param resp
	 */
	
	private void reportProjectActivityResourceCSV(HttpServletRequest req,
			HttpServletResponse resp) {

    	try {
			
			Integer[] ids			= StringUtil.splitStrToIntegers(ParamUtil.getString(req, "ids"), null);
			Date since				= ParamUtil.getDate(req, "since", getDateFormat(req), null);
			Date until				= ParamUtil.getDate(req, "until", getDateFormat(req), null);
			String showAll 			= ParamUtil.getString(req, "showAll", null);
			Integer idResourcePool	= ParamUtil.getInteger(req, "idResourcePool", null);
			
			String fileName = "Project x Activity x Recurso";
			CsvFile file	= new CsvFile(Constants.SEPARATOR_CSV);
			
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			Object[][] table = projectLogic.capacityRunningActivityResource(ids, since, until, getResourceBundle(req), idResourcePool, 
					getCompany(req), getUser(req), Boolean.parseBoolean(showAll));

			if (table != null) {
				
				for (int i = 0; i < table.length ; i++) {
					for (int j = 1; j < table[i].length; j++) {
						
						if (i > 0 && j > 2) {
							Double hours = (Double)table[i][j];
							if (table[i][j] == null) { hours = 0d;}
							file.addValue(ValidateUtil.toCurrency(hours));
						}
						else {
							file.addValue(table[i][j] == null?StringPool.BLANK:(table[i][j]).toString());
						}
					}
					file.newLine();
				}
			}
//			Object[][] table = projectLogic.capacityRunningActivityResource_(ids, since, until, getResourceBundle(req), idResourceManager, getCompany(req));
//
//			if (table != null) {
//				
//				for (int i = 0; i < table.length ; i++) {
//					for (int j = 0; j < table[i].length; j++) {
//						
//						if (i > 0 && j > 1) {
//							Double hours = (Double)table[i][j];
//							if (table[i][j] == null) { hours = 0d;}
//							file.addValue(ValidateUtil.toCurrency(hours));
//						}
//						else {
//							file.addValue(table[i][j] == null?StringPool.BLANK:(table[i][j]).toString());
//						}
//					}
//					file.newLine();
//				}
//			}

			sendFile(req, resp, file.getFileBytes(), fileName + ".csv");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	}
    
	/**
	 * Export report project activity month to csv
	 * 
	 * @param req
	 * @param resp
	 */
    private void reportProjectActivityMonthCSV(HttpServletRequest req,
			HttpServletResponse resp) {

    	try {
			
			Integer[] ids			= StringUtil.splitStrToIntegers(ParamUtil.getString(req, "ids"), null);
			Date since				= ParamUtil.getDate(req, "since", getDateFormat(req), null);
			Date until				= ParamUtil.getDate(req, "until", getDateFormat(req), null);
			String showAll 			= ParamUtil.getString(req, "showAll", null);
			Integer idResourcePool	= ParamUtil.getInteger(req, "idResourcePool", null);
			
			String fileName = "Project x Activity x Month";
			CsvFile file	= new CsvFile(Constants.SEPARATOR_CSV);
			
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			Object[][] table = projectLogic.capacityRunningActivityMonth(ids, since, until, getResourceBundle(req), idResourcePool, getCompany(req), 
					getUser(req), Boolean.parseBoolean(showAll));
			
			if (table != null) {
				
				for (int i = 0; i < table.length ; i++) {
					for (int j = 0; j < table[i].length; j++) {
						
						if (i > 0 && j > 1) {
							file.addValue(ValidateUtil.toCurrency(table[i][j]));
						}
						else {
							file.addValue(table[i][j] == null?StringPool.BLANK:(table[i][j]).toString());
						}
					}
					file.newLine();
				}
			}

			sendFile(req, resp, file.getFileBytes(), fileName + ".csv");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	}

    /**
     * Export report project month to csv
     * 
     * @param req
     * @param resp
     */
	private void reportProjectMonthCSV(HttpServletRequest req,
			HttpServletResponse resp) {
		
    	try {
			
			Integer[] ids			= StringUtil.splitStrToIntegers(ParamUtil.getString(req, "ids"), null);
			Date since				= ParamUtil.getDate(req, "since", getDateFormat(req), null);
			Date until				= ParamUtil.getDate(req, "until", getDateFormat(req), null);
			String showAll 			= ParamUtil.getString(req, "showAll", null);
			Integer idResourcePool	= ParamUtil.getInteger(req, "idResourcePool", null);
			
			String fileName = "Project x Month";
			CsvFile file	= new CsvFile(Constants.SEPARATOR_CSV);
			
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			Object[][] table = projectLogic.capacityRunningMonth(ids, since, until, getResourceBundle(req), idResourcePool, 
					getCompany(req), getUser(req), Boolean.parseBoolean(showAll));
			
			if (table != null) {
				
				for (int i = 0; i < table.length ; i++) {
					for (int j = 0; j < table[i].length; j++) {
						
						if (i > 0 && j > 0) {
							file.addValue(ValidateUtil.toCurrency(table[i][j]));
						}
						else {
							file.addValue(table[i][j] == null?StringPool.BLANK:(table[i][j]).toString());
						}
					}
					file.newLine();
				}
			}

			sendFile(req, resp, file.getFileBytes(), fileName + ".csv");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	}

	/**
     * Export Resource capacity running to csv
     * 
     * @param req
     * @param resp
     */
	private void reportProjectResourceCSV(HttpServletRequest req, HttpServletResponse resp) {

		try {
			
			Integer[] ids	= StringUtil.splitStrToIntegers(ParamUtil.getString(req, "ids"), null);
			Date since		= ParamUtil.getDate(req, "since", getDateFormat(req), null);
			Date until				= ParamUtil.getDate(req, "until", getDateFormat(req), null);
			String showAll 			= ParamUtil.getString(req, "showAll", null);
			Integer idResourcePool	= ParamUtil.getInteger(req, "idResourcePool", null);
			
			String fileName = "Project x Resource";
			CsvFile file	= new CsvFile(Constants.SEPARATOR_CSV);
			
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			Object[][] table = projectLogic.capacityRunningResource(ids, since, until, idResourcePool, getCompany(req), getUser(req), Boolean.parseBoolean(showAll));
			
			if (table != null) {
				
				for (int i = 0; i < table.length ; i++) {
					for (int j = 0; j < table[i].length; j++) {
						
						if (i > 0 && j > 0) {
							file.addValue(ValidateUtil.toCurrency(table[i][j]));
						}
						else {
							file.addValue(table[i][j] == null?StringPool.BLANK:(table[i][j]).toString());
						}
					}
					file.newLine();
				}
			}

			sendFile(req, resp, file.getFileBytes(), fileName + ".csv");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	}

	/**
	 * Show resource capacity running
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showResourceCapacityRuning(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		// Project x Resource
		
		Object[][] data = null;
		
		List<Object> proyectos = new ArrayList<Object>();
		List<Object> recursos = new ArrayList<Object>();

		try {
			
			Integer[] ids			= StringUtil.splitStrToIntegers(ParamUtil.getString(req, "ids"), null);
			Date since				= ParamUtil.getDate(req, "since", getDateFormat(req), null);
			Date until				= ParamUtil.getDate(req, "until", getDateFormat(req), null);
			Boolean showAll 		= ParamUtil.getBoolean(req, "showAll", true);
			Integer idResourcePool	= ParamUtil.getInteger(req, "idResourcePool", null);
			
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
		
			data = projectLogic.capacityRunningResource(ids, since, until, idResourcePool, getCompany(req), getUser(req), showAll);
			
			if (data != null) {
				
				for (int i = 0; i < data.length ; i++) {

					if (data[i][0] != null) {
						proyectos.add(data[i][0]);
					}

					for (int j = 0; j < data[i].length; j++) {
					
						if ( i == 0 && j > 0) {
							recursos.add(data[0][j]);
						}
					}
				}
				
				proyectos.add(proyectos.size(), "<b>"+getResourceBundle(req).getString("total")+"</b>");
				recursos.set(recursos.size()-1, getResourceBundle(req).getString("total"));
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		
		req.setAttribute("proyectos", proyectos);
		req.setAttribute("recursos", recursos);
		req.setAttribute("data", data);
		
		forward("/project/performance_reports/reports_project_resource.ajax.jsp", req, resp);
	}

	/**
	 * Show month project report
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showReportProjectMonth(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		// Project x Month
		
		Object[][] data = null;
		
		List<Object> projects = new ArrayList<Object>();
		List<Object> months = new ArrayList<Object>();
//		List<Object> datas = new ArrayList<Object>();

		try {
			
			Integer[] ids				= StringUtil.splitStrToIntegers(ParamUtil.getString(req, "ids"), null);
			Date since					= ParamUtil.getDate(req, "since", getDateFormat(req), null);
			Date until					= ParamUtil.getDate(req, "until", getDateFormat(req), null);
			Boolean showAll 			= ParamUtil.getBoolean(req, "showAll", true);
			Integer idResourcePool		= ParamUtil.getInteger(req, "idResourcePool", null);
			
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			data = projectLogic.capacityRunningMonth(ids, since, until, getResourceBundle(req), idResourcePool, getCompany(req), getUser(req), showAll);
			
			if (data != null) {
				
				for (int i = 0; i < data.length ; i++) {

					if (data[i][0] != null) {
						projects.add(data[i][0]);
					}

					for (int j = 0; j < data[i].length; j++) {
					
						if ( i == 0 && j > 0) {
							months.add(data[0][j]);
						}

						if (i > 0 && j > 0) {
//							datas.add(data[i][j]);
						}
					}
				}
				projects.add(projects.size(), "<b>"+getResourceBundle(req).getString("total")+"</b>");
				months.set(months.size()-1, getResourceBundle(req).getString("total"));
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("projects", projects);
		req.setAttribute("months", months);
		req.setAttribute("data", data);
		
		forward("/project/performance_reports/reports_project_month.ajax.jsp", req, resp);
	}
	
	/**
	 * Show resource project activity report 
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showReportProjectActivityRessource(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		// Project Activity X Ressource
		
		Object[][] data = null;
		
		List<Object> projects = new ArrayList<Object>();
		List<Object> activities = new ArrayList<Object>();
		List<Object> ressources = new ArrayList<Object>();
//		List<Object> datas = new ArrayList<Object>();
		
		try {
			
			Integer[] ids			= StringUtil.splitStrToIntegers(ParamUtil.getString(req, "ids"), null);
			Date since				= ParamUtil.getDate(req, "since", getDateFormat(req), null);
			Date until				= ParamUtil.getDate(req, "until", getDateFormat(req), null);
			Boolean showAll 		= ParamUtil.getBoolean(req, "showAll", true);
			Integer idResourcePool 	= ParamUtil.getInteger(req, "idResourcePool", null);
			
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			data = projectLogic.capacityRunningActivityResource(ids, since, until, getResourceBundle(req), idResourcePool, getCompany(req), getUser(req), showAll);
			
			if (data != null) {
				
				for (int i = 0; i < data.length ; i++) {

					if (data[i][0] != null) {
						projects.add(data[i][1]);
					}
					
					if (data[i][1] != null) {
						activities.add(data[i][2]);
					}

					for (int j = 0; j < data[i].length; j++) {
					
						if ( i == 0 && j > 2) {
							ressources.add(data[0][j]);
						}

						if (i > 0 && j > 2) {
//							datas.add(data[i][j]);
						}
					}
				}
				projects.add(projects.size(), "<b>"+getResourceBundle(req).getString("total")+"</b>");
				activities.add(activities.size(), "");
				ressources.set(ressources.size()-1, getResourceBundle(req).getString("total"));
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("projects", projects);
		req.setAttribute("ressources", ressources);
		req.setAttribute("activities", activities);
		req.setAttribute("data", data);
		
		forward("/project/performance_reports/reports_project_activity_ressource.ajax.jsp", req, resp);
	}
	
	/**
	 * Show month project activity report
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showReportProjectActivityMonth(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		// Project Activity X Month
		
		Object[][] data = null;
		
		List<Object> projects = new ArrayList<Object>();
		List<Object> activities = new ArrayList<Object>();
		List<Object> months = new ArrayList<Object>();
//		List<Object> datas = new ArrayList<Object>();
		
		try {
			
			Integer[] ids			= StringUtil.splitStrToIntegers(ParamUtil.getString(req, "ids"), null);
			Date since				= ParamUtil.getDate(req, "since", getDateFormat(req), null);
			Date until				= ParamUtil.getDate(req, "until", getDateFormat(req), null);
			Boolean showAll 		= ParamUtil.getBoolean(req, "showAll", true);
			Integer idResourcePool 	= ParamUtil.getInteger(req, "idResourcePool", null);
			
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			data = projectLogic.capacityRunningActivityMonth(ids, since, until, getResourceBundle(req), idResourcePool, getCompany(req), getUser(req), showAll);
			
			if (data != null) {
				
				for (int i = 0; i < data.length ; i++) {

					
					if (data[i][0] != null) {
						projects.add(data[i][0]);
					}
					
					if (data[i][1] != null) {
						activities.add(data[i][1]);
					}
					else
					{
						if (i != 0) 
						{
							activities.add("");
						}
					}

					for (int j = 0; j < data[i].length; j++) {
					
						if ( i == 0 && j > 1) {
							months.add(data[0][j]);
						}

						if (i > 0 && j > 1) {
//							datas.add(data[i][j]);
						}
					}
				}
				
				projects.add(projects.size(), "<b>"+getResourceBundle(req).getString("total")+"</b>");
				months.set(months.size()-1, getResourceBundle(req).getString("total"));
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("projects", projects);
		req.setAttribute("months", months);
		req.setAttribute("activities", activities);
		req.setAttribute("data", data);
		
		forward("/project/performance_reports/reports_project_activity_month.ajax.jsp", req, resp);
	}

	/**
	 * Reset investment (Ajax)
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void resetInvestmentJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		PrintWriter out = resp.getWriter();
		
		try {

            // Only PMO change status of project if setting is activated
            if (!SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)
                    && SettingUtil.getBoolean(req, GeneralSetting.ONLY_PMO_CHANGE_STATUS)) {

                throw new es.sm2.openppm.core.logic.exceptions.SecurityException("Only PMO change status of project");
            }

			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			int idProject = ParamUtil.getInteger(req, Project.IDPROJECT,-1);
			
			Project proj = projectLogic.consProject(idProject);
			
			proj.setStatus(Constants.STATUS_INITIATING);
			proj.setInvestmentStatus(Constants.INVESTMENT_IN_PROCESS);
			
			projectLogic.saveAndUpdateDataProject(proj);
			
			LogprojectstatusLogic logstatusLogic = new LogprojectstatusLogic();
			logstatusLogic.save(new Logprojectstatus(
					proj,getUser(req), Constants.STATUS_INITIATING, Constants.INVESTMENT_IN_PROCESS, new Date()));
			
			out.print(JsonUtil.infoTrue());
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
	 * Cancel investment (ajax)
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void cancelInvestmentJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		try {

            // Only PMO change status of project if setting is activated
            if (!SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)
                    && SettingUtil.getBoolean(req, GeneralSetting.ONLY_PMO_CHANGE_STATUS)) {

                throw new es.sm2.openppm.core.logic.exceptions.SecurityException("Only PMO change status of project");
            }

			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			int idProject 		= ParamUtil.getInteger(req, Project.IDPROJECT,-1);
			String comments		= ParamUtil.getString(req, Project.CLIENTCOMMENTS);
			
			Project proj = projectLogic.consProject(idProject);
			
			proj.setPriority(0);
			proj.setClientComments(comments);
			proj.setStatus(Constants.STATUS_ARCHIVED);
			proj.setInvestmentStatus(Constants.INVESTMENT_INACTIVATED);
			
			projectLogic.saveAndUpdateDataProject(proj);
			
			LogprojectstatusLogic logstatusLogic = new LogprojectstatusLogic();
			logstatusLogic.save(new Logprojectstatus(
					proj,getUser(req), Constants.STATUS_ARCHIVED, Constants.INVESTMENT_INACTIVATED, new Date()));
			
			out.print(JsonUtil.infoTrue());
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
	 * Reject investment (Ajax)
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void rejectInvestmentJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		try {
            // Only PMO change status of project if setting is activated
            if (!SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)
                    && SettingUtil.getBoolean(req, GeneralSetting.ONLY_PMO_CHANGE_STATUS)) {

                throw new es.sm2.openppm.core.logic.exceptions.SecurityException("Only PMO change status of project");
            }

			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			int idProject		= ParamUtil.getInteger(req, Project.IDPROJECT,-1);
			int finalPosition	= ParamUtil.getInteger(req, Project.FINALPOSITION,0);
			int numCompetitors	= ParamUtil.getInteger(req, Project.NUMCOMPETITORS,0);
			String clientComm	= ParamUtil.getString(req, Project.CLIENTCOMMENTS);
			
			Project proj = projectLogic.consProject(idProject);
			
			proj.setPriority(0);
			proj.setClientComments(clientComm);
			proj.setFinalPosition(finalPosition);
			proj.setNumCompetitors(numCompetitors);
			proj.setStatus(Constants.STATUS_ARCHIVED);
			proj.setInvestmentStatus(Constants.INVESTMENT_REJECTED);
			
			projectLogic.saveAndUpdateDataProject(proj);
			
			LogprojectstatusLogic logstatusLogic = new LogprojectstatusLogic();
			logstatusLogic.save(new Logprojectstatus(
					proj,getUser(req), Constants.STATUS_ARCHIVED,Constants.INVESTMENT_REJECTED, new Date()));
			
			out.print(JsonUtil.infoTrue());
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
	 * Approve investment (ajax)
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void approveInvestmentJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		JSONObject infoJSON = new JSONObject();
		
		try {
			int idProject		= ParamUtil.getInteger(req, Project.IDPROJECT,-1);
			int numCompetitors	= ParamUtil.getInteger(req, Project.NUMCOMPETITORS,0);
			String comments		= ParamUtil.getString(req, Project.CLIENTCOMMENTS);
			
			// Declare logics
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			// Logic
			infoJSON = projectLogic.approveInvestment(idProject, numCompetitors, comments, getUser(req));
			
			out.print(infoJSON.toString());
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Cons project (Ajax)
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void consProjectJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		List<Documentproject> docs = null;
		
		PrintWriter out = resp.getWriter();		
		
		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			int idProject = ParamUtil.getInteger(req, Project.IDPROJECT,-1);
			
			Project proj = projectLogic.consProject(idProject);
			
			String documentStorage = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_STORAGE, Settings.DEFAULT_PROJECT_DOCUMENT_STORAGE);

			req.setAttribute("documentStorage", documentStorage);
			
			out.print(JSONModelUtil.investmentToJSON(proj, docs, documentStorage, getResourceBundle(req)));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Filter table investments (ajax)
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void filterTableInvestmentsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		try {
			ProjectLogic projectLogic	= new ProjectLogic(getSettings(req), getResourceBundle(req));
			ProjectSearch projectSearch = new ProjectSearch(getUser(req), getSettings(req));
			
			// Load Filters
			loadFilters(projectSearch, req);
			
			// Find projects
			List<ProjectWrap> projects = projectLogic.findProjects(projectSearch);
			
			JSONArray projectsJSON	= new JSONArray();
			StringBuilder sb		= new StringBuilder();
			
			Double sumBudget 	= null;
			Double sumBac 		= null;
			
			for (ProjectWrap item : projects) {
				
				if (sb.toString().equals(StringPool.BLANK)) { sb.append(item.getIdProject()); }
				else { sb.append(","+item.getIdProject()); }
				
				JSONArray projectJSON = new JSONArray();
				projectJSON.add(item.getIdProject());
				projectJSON.add(getResourceBundle(req).getString("investments.status."+item.getInvestmentStatus()));
				projectJSON.add(HtmlUtil.escape(item.getAccountingCode()));
				projectJSON.add(HtmlUtil.escape(item.getProjectName()));
				projectJSON.add(HtmlUtil.escape(item.getChartLabel()));
				
				if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)) {
					projectJSON.add(HtmlUtil.escape(item.getPerformingOrg()));
				}
				projectJSON.add(HtmlUtil.escape(item.getProgram()));
				projectJSON.add(ValidateUtil.toCurrency(item.getTcv()));
				projectJSON.add(ValidateUtil.toCurrency(item.getBac()));
                projectJSON.add(item.getDuration());
				projectJSON.add(item.getPriority());
				projectJSON.add(item.getProbability());
				
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getPlannedInitDate()));
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getPlannedFinishDate()));
				
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getCalculatedPlanStartDate()));
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getCalculatedPlanFinishDate()));
				
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getStartDate()));
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getFinishDate()));
				
				projectJSON.add(item.getEffort());
				projectJSON.add(ValidateUtil.toCurrency(item.getExternalCost()));
                projectJSON.add(HtmlUtil.escape(item.getStagegate()));
				
				projectJSON.add(StringPool.BLANK);
				projectsJSON.add(projectJSON);
				
				// Summatories
				//
				if (item.getTcv() != null) {
					if (sumBudget == null) { sumBudget = 0d; }
					sumBudget += item.getTcv();
				}
				
				if (item.getBac() != null) {
					if (sumBac == null) { sumBac = 0d; }
					sumBac += item.getBac();
				}
			}
			
			JSONObject infoJSON = new JSONObject();
			infoJSON.put("ids", sb.toString());
			
			JSONObject data = new JSONObject();
			
			if (sumBudget != null) {
				data.put("sumBudget", sumBudget);
			}
			
			if (sumBac != null) {
				data.put("sumBac", sumBac);
			}
			
			data.put("aaData", projectsJSON);
			
			if (infoJSON != null) {
				data.put("info", infoJSON);
			}
			
			String jsonOutput = data.toString();
			byte[] byteArray = jsonOutput.getBytes("UTF8");
			
			out.print(new String(byteArray));
			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e);	}
		finally { out.close(); }
	}

	
	/**
	 * Filter table projectx (ajax)
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void filterTableProjectsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		try {
			
			ProjectLogic projectLogic	= new ProjectLogic(getSettings(req), getResourceBundle(req));
			ProjectSearch projectSearch = new ProjectSearch(getUser(req), getSettings(req));

			// Load Filters
			loadFilters(projectSearch, req);

			// Add filters by setting
			projectSearch.setShowInactivated(SettingUtil.getBoolean(getSettings(req), Settings.SETTING_SHOW_INACTIVATED, Settings.SETTING_SHOW_INACTIVATED_DEFAULT));
			
			// Find projects
			List<ProjectWrap> projects = projectLogic.findProjects(projectSearch);

			JSONArray projectsJSON	= new JSONArray();
			StringBuilder sb		= new StringBuilder();
			
			boolean showActualCost	= SettingUtil.getBoolean(getSettings(req), Settings.SETTING_PROJECT_COLUMN_ACTUAL_COST, Settings.DEFAULT_PROJECT_COLUMN_ACTUAL_COST);
			
			Double sumBudget 	= null;
			Double sumBac 		= null;
			
			for (ProjectWrap item : projects) {
				
				if (sb.toString().equals(StringPool.BLANK)) { sb.append(item.getIdProject()); }
				else { sb.append(","+item.getIdProject()); }
				
				String generalRisk = StringPool.BLANK;
				
				if (!ValidateUtil.isNull(item.getRag())) {
					if (item.getRag().charAt(0) == Constants.RISK_LOW) { generalRisk = "low_importance"; }
					else if (item.getRag().charAt(0) == Constants.RISK_MEDIUM) { generalRisk = "medium_importance"; }
					else if (item.getRag().charAt(0) == Constants.RISK_HIGH) { generalRisk = "high_importance"; }
					else if (item.getRag().charAt(0) == Constants.RISK_NORMAL) { generalRisk = "normal_importance"; }
					generalRisk = "<div class='"+generalRisk+"'>&nbsp;&nbsp;&nbsp;</div>";
				}
				
				JSONArray projectJSON = new JSONArray();
				projectJSON.add(item.getIdProject());
				projectJSON.add(item.getStatus());
				projectJSON.add(generalRisk);
				projectJSON.add("<div class=\""+ item.getKpiStatus() + " riskRating" + "\"></div>");
				projectJSON.add(getResourceBundle(req).getString("project_status."+item.getStatus()));
				projectJSON.add(HtmlUtil.escape(item.getAccountingCode()));
				projectJSON.add(HtmlUtil.escape(item.getProjectName()));
				projectJSON.add(HtmlUtil.escape(item.getChartLabel()));
				
				if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)) {
					projectJSON.add(HtmlUtil.escape(item.getPerformingOrg()));
				}
                projectJSON.add(HtmlUtil.escape(item.getProgram()));
				projectJSON.add(HtmlUtil.escape(item.getProjectManager()));
				projectJSON.add(ValidateUtil.toCurrency(item.getTcv()));
				projectJSON.add(ValidateUtil.toCurrency(item.getBac()));
                projectJSON.add(item.getDuration());
				projectJSON.add(item.getPriority());
				
				projectJSON.add(ValidateUtil.toPercent(item.getPoc()));
				
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getPlannedInitDate()));
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getPlannedFinishDate()));
				
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getCalculatedPlanStartDate()));
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getCalculatedPlanFinishDate()));
				
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getStartDate()));
				projectJSON.add(DateUtil.format(getResourceBundle(req), item.getFinishDate()));
				
				if (showActualCost) { projectJSON.add(ValidateUtil.toCurrency(item.getLastAc())); }
				else { projectJSON.add(StringPool.BLANK); }
				
				projectJSON.add(item.getEffort());
				projectJSON.add(ValidateUtil.toCurrency(item.getExternalCost()));

                projectJSON.add(HtmlUtil.escape(item.getCategory()));
                projectJSON.add(HtmlUtil.escape(item.getClassificationLevel()));
                projectJSON.add(HtmlUtil.escape(item.getStagegate()));

				projectJSON.add(StringPool.BLANK);
				
				projectsJSON.add(projectJSON);
				
				// Summatories
				//
				if (item.getTcv() != null) {
					if (sumBudget == null) { sumBudget = 0d; }
					sumBudget += item.getTcv();
				}
				
				if (item.getBac() != null) {
					if (sumBac == null) { sumBac = 0d; }
					sumBac += item.getBac();
				}
			}
			
			// Response 
			//
			JSONObject infoJSON = new JSONObject();
			infoJSON.put("ids", sb.toString());
			
			JSONObject data = new JSONObject();
			
			if (sumBudget != null) {
				data.put("sumBudget", sumBudget);
			}
			
			if (sumBac != null) {
				data.put("sumBac", sumBac);
			}
			
			data.put("aaData", projectsJSON);
			
			if (infoJSON != null) {
				data.put("info", infoJSON);
			}
			
			String jsonOutput = data.toString();
			byte[] byteArray = jsonOutput.getBytes("UTF8");
			
			out.print(new String(byteArray));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e);	}
		finally { out.close(); }
	}

	/**
	 * Load filters
	 * 
	 * @param projectSearch
	 * @param req
	 * @throws Exception 
	 */
	private void loadFilters(ProjectSearch projectSearch, HttpServletRequest req) throws Exception {
	
		// Filter by rol
		if (SecurityUtil.isUserInRole(req, Constants.ROLE_PMO) ||
                SecurityUtil.isUserInRole(req, Constants.ROLE_FM) ||
                SecurityUtil.isUserInRole(req, Constants.ROLE_LOGISTIC)) {	// PMO or Functional Manager or Logistic
			
			projectSearch.setPerformingorgs(DataUtil.toList(getUser(req).getPerformingorg().getIdPerfOrg()));
		}
		else if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)) {	// Porfolio Manager
			
			projectSearch.setCompany(getCompany(req));
		}
		else if (SecurityUtil.isUserInRole(req, Constants.ROLE_IM)) {	// Investment Manager
			
			projectSearch.setEmployeeByInvestmentManager(getUser(req));
		}
		else if (SecurityUtil.isUserInRole(req, Constants.ROLE_PM)) {	// Project Manager
			
			projectSearch.setEmployeeByProjectManagers(DataUtil.toList(getUser(req).getIdEmployee()));
		}
		else if (SecurityUtil.isUserInRole(req, Constants.ROLE_SPONSOR)) {	// Sponsor
			
			projectSearch.setEmployeeBySponsors(DataUtil.toList(getUser(req).getIdEmployee()));
		}
		else if (SecurityUtil.isUserInRole(req, Constants.ROLE_STAKEHOLDER)) {	// Stakeholder
			
			projectSearch.setStakeholder(getUser(req));
		}
		else if (SecurityUtil.isUserInRole(req, Constants.ROLE_PROGM)) {	// Program Manager
			
			projectSearch.setProgramManager(getUser(req));
		}
		
		// Load Filters
		projectSearch.setStatus(ParamUtil.getStringList(req, Project.STATUS, null));
		projectSearch.setInvestmentStatus(ParamUtil.getStringList(req, Project.INVESTMENTSTATUS, null));
		projectSearch.setSince(ParamUtil.getDate(req, Projectactivity.PLANINITDATE, getDateFormat(req),null));
		projectSearch.setUntil(ParamUtil.getDate(req, Projectactivity.PLANENDDATE, getDateFormat(req),null));
		projectSearch.setBudgetYear(ParamUtil.getInteger(req, Project.BUDGETYEAR, null));
		
		String internalProject = ParamUtil.getString(req, Project.INTERNALPROJECT, null);
		projectSearch.setInternalProject(ValidateUtil.isNull(internalProject) ? null : Boolean.parseBoolean(internalProject));
		
		String isGeoSelling = ParamUtil.getString(req, Project.ISGEOSELLING, null);
		projectSearch.setIsGeoSelling(ValidateUtil.isNull(isGeoSelling) ? null : Boolean.parseBoolean(isGeoSelling));
		
		projectSearch.setCustomertypes(ParamUtil.getIntegersSplit(req, "customertype.idCustomerType"));
		projectSearch.setCustomers(ParamUtil.getIntegersSplit(req, "customer.idCustomer"));
		projectSearch.setPrograms(ParamUtil.getIntegersSplit(req, "program.idProgram"));
		projectSearch.setSellers(ParamUtil.getIntegersSplit(req, "seller.idSeller"));
		projectSearch.setCategories(ParamUtil.getIntegersSplit(req, "category.idCategory"));
		projectSearch.setGeography(ParamUtil.getIntegersSplit(req, "geography.idGeography"));
		projectSearch.setProjects(ParamUtil.getIntegersSplit(req, Project.IDPROJECT));
		
		if (SettingUtil.getBoolean(req, Settings.SETTING_DISABLE_PROJECT, Settings.DEFAULT_DISABLE_PROJECT)) {
			projectSearch.setIncludeDisabled(false); //TODO put filter
		}
		
		if (ValidateUtil.isNull(projectSearch.getEmployeeByProjectManagers())) {
			projectSearch.setEmployeeByProjectManagers(ParamUtil.getIntegersSplit(req, "employeeByProjectManager.idEmployee"));
		}
		
		if (ValidateUtil.isNull(projectSearch.getEmployeeBySponsors())) {
			projectSearch.setEmployeeBySponsors(ParamUtil.getIntegersSplit(req, "employeeBySponsor.idEmployee"));
		}
		
		if (ValidateUtil.isNull(projectSearch.getPerformingorgs())) {
			projectSearch.setPerformingorgs(ParamUtil.getIntegersSplit(req, "performingorg.idPerfOrg"));
		}
		
		projectSearch.setIncludeRejected(JX_FILTER_TABLE.equals(ParamUtil.getString(req, "accion")) ?
				SettingUtil.getBoolean(getSettings(req), 
				Settings.SETTING_SHOW_REJECTED_INVESTMENT_IN_CLOSED_PROJECTS, 
				Settings.SETTING_SHOW_REJECTED_INVESTMENT_IN_CLOSED_PROJECTS_DEFAULT) :
					false);
		
		projectSearch.setProjectName(ParamUtil.getString(req, Project.PROJECTNAME));
		projectSearch.setRag(ParamUtil.getString(req, Project.RAG));
		projectSearch.setPriority(ParamUtil.getString(req, "filterPriority", null));
		projectSearch.setFirstPriority(ParamUtil.getInteger(req, "first", null));
		projectSearch.setLastPriority(ParamUtil.getInteger(req, "last", null));
		projectSearch.setLabels(ParamUtil.getIntegersSplit(req, "label.idLabel"));
		projectSearch.setStageGates(ParamUtil.getIntegersSplit(req, "stageGate.idStageGate"));
		
		// Filters by configuration
		//
		projectSearch.setContractTypes(ParamUtil.getIntegersSplit(req, Configurations.LIST_FILTERS_CONTRACT_TYPE));
		projectSearch.setEmployeeByFunctionalManagers(ParamUtil.getIntegersSplit(req, Configurations.LIST_FILTERS_FM));
		
		String isIndirectSeller = ParamUtil.getString(req, Configurations.BOOLEAN_FILTER_SELLERS, null);
		
		if (ValidateUtil.isNotNull(isIndirectSeller)) {
			projectSearch.setIsIndirectSeller(Boolean.parseBoolean(isIndirectSeller));
		}
		
		// Classification level
		projectSearch.setClassificationsLevel(ParamUtil.getIntegersSplit(req, Configurations.LIST_FILTERS_CLASSIFICATION_LEVEL));

        // Include classifications levels unclassified
        String classificationsLevel = ParamUtil.getString(req, Configurations.LIST_FILTERS_CLASSIFICATION_LEVEL, null);
        projectSearch.setClassificationsLevelUnclassified(classificationsLevel != null && classificationsLevel.contains(Constants.UNCLASSIFIED));

        // Founding source
        projectSearch.setFundingsources(ParamUtil.getIntegersSplit(req, Configurations.LIST_FILTERS_FOUNDING_SOURCE));

        // Include unclassified founding sources
        String fundingSource = ParamUtil.getString(req, Configurations.LIST_FILTERS_FOUNDING_SOURCE, null);

        projectSearch.setFundingSourceUnclassified(fundingSource != null && fundingSource.contains(Constants.UNCLASSIFIED));

        // Technologies
        projectSearch.setTechnologies(ParamUtil.getIntegersSplit(req, Configurations.LIST_FILTERS_TECHNOLOGY));

		// Including adjustament
		//
		String includingAdjustament = ParamUtil.getString(req, Configurations.BOOLEAN_FILTER_PRIORITY_ADJUSTMENT, null);
		
		if (ValidateUtil.isNotNull(includingAdjustament)) {
			projectSearch.setIncludingAdjustament(Boolean.parseBoolean(includingAdjustament));
		}
		
		// Risk rating
		String riskRating = ParamUtil.getString(req, Configurations.FILTER_RISK_RATING, null);
		
		if (ValidateUtil.isNotNull(riskRating)) {
			
			projectSearch.setRiskRating(riskRating);
			projectSearch.setFirstRiskRating(ParamUtil.getInteger(req, Configurations.FILTER_FIRST_RISK_RATING, null));
			projectSearch.setLastRiskRating(ParamUtil.getInteger(req, Configurations.FILTER_LAST_RISK_RATING, null));
		}
		
		// Find configurations
		HashMap<String, String> configurations = RequestUtil.getConfigurationValues(
				req,
				Configurations.LIST_FILTERS_CONTRACT_TYPE,
				Configurations.LIST_FILTERS_FM,
				Configurations.BOOLEAN_FILTER_SELLERS,
				Configurations.LIST_FILTERS_CLASSIFICATION_LEVEL,
				Configurations.BOOLEAN_FILTER_PRIORITY_ADJUSTMENT,
				Configurations.FILTER_RISK_RATING,
				Configurations.FILTER_FIRST_RISK_RATING,
				Configurations.FILTER_LAST_RISK_RATING,
                Configurations.LIST_FILTERS_FOUNDING_SOURCE,
                Configurations.LIST_FILTERS_TECHNOLOGY
			);
		
		// Save configuration
		ConfigurationLogic configurationLogic = new ConfigurationLogic();
		configurationLogic.saveConfigurations(getUser(req), configurations, Configurations.TYPE_LIST_FILTERS);
	}
	
	/**
     * Save customer (Ajax)
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveCustomerJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	String customerName	 = ParamUtil.getString(req, "customer_name", null);
    	int idCustomerType	 = ParamUtil.getInteger(req, "customer_type", -1);
    	
    	PrintWriter out = resp.getWriter();
		resp.setContentType("application/json");
		
		try {
			
			if (customerName == null || idCustomerType == -1) {
				throw new NoDataFoundException();
			}
			
			CustomerLogic customerLogic = new CustomerLogic();
			Customer customer 			= new Customer();
			customer.setName(customerName);
			customer.setCustomertype(new Customertype(idCustomerType));
			
			customer.setCompany(getCompany(req));
			
			customerLogic.save(customer);
			
			JSONObject customerJSON = new JSONObject();
			customerJSON.put("id", customer.getIdCustomer());
			
			out.print(customerJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}


	/**
     * Check duplicate code of project (ajax)
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void checkCodeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

//    	String code = ParamUtil.getString(req, "code", null);
		
		PrintWriter out = resp.getWriter();
		
		try {
			// TODO codigo sin uso
			//if (!ProjectLogic.checkCode(code)) {
			//	throw new ProjectCodeInUseException();
			//}
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}


	/**
     * Create new project
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void createProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    	// Collect information
    	Integer idPerfOrg		= ParamUtil.getInteger(req, Project.PERFORMINGORG, null);
    	Date plannedInitDate 	= ParamUtil.getDate(req, Project.PLANNEDINITDATE, getDateFormat(req), null);
    	Date plannedFinishDate	= ParamUtil.getDate(req, Project.PLANNEDFINISHDATE, getDateFormat(req), null);
    	String accountingCode 	= ParamUtil.getString(req, Project.ACCOUNTINGCODE, null);
    	String projectName		= ParamUtil.getString(req, Project.PROJECTNAME, null);
    	String chartLabel		= ParamUtil.getString(req, Project.CHARTLABEL, null);
    	Integer idProgram		= ParamUtil.getInteger(req, Project.PROGRAM, null);
    	Integer idCustomer		= ParamUtil.getInteger(req, Project.CUSTOMER, null);
    	Integer idCategory		= ParamUtil.getInteger(req, Project.CATEGORY, null);
    	int funcionalManager	= ParamUtil.getInteger(req, Project.EMPLOYEEBYFUNCTIONALMANAGER, -1);
    	int sponsor				= ParamUtil.getInteger(req, Project.EMPLOYEEBYSPONSOR, -1);
    	int projectManager		= ParamUtil.getInteger(req, Project.EMPLOYEEBYPROJECTMANAGER, -1);
    	int investmentManager	= ParamUtil.getInteger(req, Project.EMPLOYEEBYINVESTMENTMANAGER, -1);
    	double tcv				= ParamUtil.getCurrency(req, Project.TCV,0);
    	Integer duration		= ParamUtil.getInteger(req, Project.DURATION, null);
    	Integer priority		= ParamUtil.getInteger(req, Project.PRIORITY, 0);
    	Integer probability		= ParamUtil.getInteger(req, Project.PROBABILITY, 0);
    	Integer budgetYear		= ParamUtil.getInteger(req, Project.BUDGETYEAR,null);
    	
    	// Has permission
    	if (SecurityUtil.isUserInRole(req, Constants.ROLE_PMO) || SecurityUtil.isUserInRole(req, Constants.ROLE_IM)) {
			
    		// Create project
			Project proj = new Project();

			// Set data
			proj.setStatus(Constants.STATUS_INITIATING);
			proj.setInitDate(new Date());
			proj.setPlannedInitDate(plannedInitDate);
			proj.setPlannedFinishDate(plannedFinishDate);
			proj.setAccountingCode(accountingCode);
			proj.setProjectName(projectName);
			proj.setChartLabel(chartLabel);
			proj.setTcv(tcv);
			proj.setDuration(duration);
			proj.setPriority(priority);
			proj.setProbability(probability);
			proj.setBudgetYear(budgetYear);
			proj.setInvestmentStatus(Constants.INVESTMENT_IN_PROCESS);
			proj.setInternalProject(false);
			
			// Initialize calculate dates
			proj.setCalculatedPlanStartDate(plannedInitDate);
			proj.setCalculatedPlanFinishDate(plannedFinishDate);
			
			// Initialize adjusts
			proj.setUseRiskAdjust(false);
			proj.setUseStrategicAdjust(false);
			
			// Set relations
			//
			if (idPerfOrg != null) {
				proj.setPerformingorg(new Performingorg(idPerfOrg));
			}
			if (funcionalManager != -1) {
				proj.setEmployeeByFunctionalManager(new Employee(funcionalManager));
			}
			if (sponsor != -1) {
				proj.setEmployeeBySponsor(new Employee(sponsor));
			}
			if (projectManager != -1) {
				proj.setEmployeeByProjectManager(new Employee(projectManager));
			}
			if (investmentManager != -1) {
				proj.setEmployeeByInvestmentManager(new Employee(investmentManager));
			}
			if (idCategory != null) {
				proj.setCategory(new Category(idCategory));
			}
			if (idCustomer != null) {
				proj.setCustomer(new Customer(idCustomer));
			}
			if (idProgram != null) {
				proj.setProgram(new Program(idProgram));
			}
			
			try {

				// Declare logics
				ProjectLogic projectLogic 						= new ProjectLogic(getSettings(req), getResourceBundle(req));
				LogprojectstatusLogic logstatusLogic 			= new LogprojectstatusLogic();
				
				// Create project
				proj = projectLogic.createProject(proj, getCompany(req));
				
				// FIXME mirar porque falla al meter la notificacion dentro del projectLogic.createProject (no trae los contactos) son problemas de la sesion
                // Notifications
                //
                NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(getSettings(req));

                // Notification - The project has changed state
                if (SettingUtil.getBoolean(getSettings(req), NotificationType.CHANGE_PROJECT_STATUS)) {
                    notificationsLogic.changeProjectStatus(proj.getIdProject());
                }

                // Notification - Assignment project to PM
                if (SettingUtil.getBoolean(getSettings(req), NotificationType.ASSIGNMENT_PROJECT) &&
                        proj.getEmployeeByProjectManager() != null) {

                    notificationsLogic.assignedProject(proj.getIdProject());
                }

				// Create log
				logstatusLogic.save(new Logprojectstatus(
						proj,getUser(req), Constants.STATUS_INITIATING, Constants.INVESTMENT_IN_PROCESS, new Date()));
				
				// Send information
				if (!proj.getAccountingCode().equals(StringPool.BLANK) && projectLogic.accountingCodeInUse(proj, getUser(req))) {
					info(StringPool.INFORMATION, req, "msg.info.accounting_code_in_use", proj.getAccountingCode());
				}
				if (projectLogic.chartLabelInUse(proj, getUser(req))) {
					info(StringPool.INFORMATION, req, "msg.info.in_use", "project.chart_label", proj.getChartLabel());
				}
			}
			catch (Exception e) {
				ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
			}
			
			// Send data
			String type = ParamUtil.getString(req, "type");
			forwardServlet(ProjectInitServlet.REFERENCE+"?accion=&type="+type+"&id="+proj.getIdProject(), req, resp);
		}
		else {
			
			// Send data - not permission
			req.setAttribute(StringPool.ERROR, getResourceBundle(req).getString("msg.error.permission.create_project"));
			req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
			forward("/index.jsp", req, resp);
		}
	}


	/**
     * Save document project
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveDocumentJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
		JSONObject docJSON = new JSONObject();
		PrintWriter out = resp.getWriter();
		
		try {
			// Request
			int idDocumentProject		= ParamUtil.getInteger(req, Documentproject.IDDOCUMENTPROJECT, -1);
			int idProject				= ParamUtil.getInteger(req, Documentproject.PROJECT, -1);
			String type					= ParamUtil.getString(req, Documentproject.TYPE, null);
			String link					= ParamUtil.getString(req, Documentproject.LINK);
			String comment				= ParamUtil.getString(req, Documentproject.CONTENTCOMMENT, StringPool.BLANK);
			String creationContact 		= getUser(req).getContact().getFullName();
            Date creationDate 		    = DateUtil.getCalendar().getTime();

            // IDs
            int idTimeline              = ParamUtil.getInteger(req, Timeline.IDTIMELINE, -1);
            int idClosureCheckProject	= ParamUtil.getInteger(req, Closurecheckproject.IDCLOSURECHECKPROJECT, -1);

            // Date format
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_PATTERN);

			// Declare logic
			DocumentprojectLogic documentprojectLogic = new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
			
			// Logic
			Documentproject doc = documentprojectLogic.saveDocumentproject(
					idDocumentProject,
					idProject,
					type,
					link,
					comment,
					creationContact,
					creationDate
				);
			
			// Set document into closure check project 
			if (idClosureCheckProject != -1) {
				
				ClosurecheckprojectLogic closurecheckprojectLogic = new ClosurecheckprojectLogic();

                closurecheckprojectLogic.saveDocument(idClosureCheckProject, doc);

				docJSON.put(Closurecheckproject.IDCLOSURECHECKPROJECT, idClosureCheckProject);
			}
            // Set document into timeline
            else if (idTimeline != -1) {

                TimelineLogic timelineLogic = new TimelineLogic(getSettings(req), getResourceBundle(req));

                timelineLogic.saveDocument(idTimeline, doc);

                docJSON.put(Timeline.IDTIMELINE, idTimeline);
            }

			// Response
			//
			docJSON.put(Documentproject.IDDOCUMENTPROJECT, doc.getIdDocumentProject());
			docJSON.put(Documentproject.TYPE, getResourceBundle(req).getString("documentation." + doc.getType()));
			docJSON.put(Documentproject.LINK, doc.getLink());
			docJSON.put(Documentproject.CONTENTCOMMENT, doc.getContentComment());
			docJSON.put(Documentproject.CREATIONCONTACT, doc.getCreationContact());
			docJSON.put(Documentproject.CREATIONDATE, dateFormat.format( doc.getCreationDate()));
			docJSON.put("documentType", DocumentType.LINK.getName());

			if (idDocumentProject == -1) {
				infoCreated(getResourceBundle(req), docJSON, "document");
			}
			else {
				infoUpdated(getResourceBundle(req), docJSON, "document");
			}
			
			out.print(docJSON);
		}
		catch (Exception e) {
			ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e);
		}
		
		finally { out.close(); }
	}


	/**
     * Delete project in initiating
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
	private void deleteProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int idProject = ParamUtil.getInteger(req, "id",-1);
		
		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			if (SettingUtil.getBoolean(req, Settings.SETTING_DISABLE_PROJECT, Settings.DEFAULT_DISABLE_PROJECT)) {

                // Logic find project
				Project project = projectLogic.consProject(idProject);

                // Delete LLAA by project
                LearnedLessonProjectLogic learnedLessonProjectLogic = new LearnedLessonProjectLogic();

                List<LearnedLessonProject> learnedLessonProjects = learnedLessonProjectLogic.findByProject(project);

                if (ValidateUtil.isNotNull(learnedLessonProjects)) {

                    for (LearnedLessonProject learnedLessonProject : learnedLessonProjects) {

                        learnedLessonProjectLogic.delete(learnedLessonProject);
                    }
                }

                // Delete teammembers
                //
                TeamMemberLogic teamMemberLogic = new TeamMemberLogic();

                List<Teammember> teammembers = teamMemberLogic.consTeamMembers(project);

                if (ValidateUtil.isNotNull(teammembers)) {

                    for (Teammember teammember : teammembers) {
                        teamMemberLogic.delete(teammember);
                    }
                }

                // Disable project
                project.setDisable(true);
				projectLogic.save(project);
			}
			else  {

                // Logic get project
                Project project = projectLogic.findById(idProject);

                // Logic delete project
                projectLogic.deleteProject(projectLogic.findById(idProject));

                // Get folder
                //
                String docFolderSetting    = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_FOLDER, Settings.DEFAULT_PROJECT_DOCUMENT_FOLDER);
                String docFolder           = docFolderSetting.concat(File.separator + StringUtils.formatting(project.getChartLabel()) + StringPool.UNDERLINE + project.getIdProject());

                File dirDocs = new File(docFolder);

                // Delete documents folder
                if (dirDocs.isDirectory()) {

                    Logger.getLogger(getClass()).info("Documents project folder " + dirDocs.getAbsolutePath() + " delete");

                    try {
                        FileUtils.deleteDirectory(dirDocs);
                    }
                    catch (IOException e) {

                        Logger.getLogger(getClass()).error("Not delete folder: " + dirDocs.getAbsolutePath());

                        req.setAttribute(StringPool.InfoType.ERROR.getName(), "Not delete folder: " + dirDocs.getAbsolutePath());
                    }
                }
			}
			
		} 
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewProjects(req, resp, LIST_PROJECTS);
	}


	private void viewCreateProjectForm(HttpServletRequest req,
			HttpServletResponse resp, String accion) throws ServletException, IOException {
		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		
		if (SecurityUtil.isUserInRole(req, Constants.ROLE_PMO) ||
				SecurityUtil.isUserInRole(req, Constants.ROLE_IM)) {
			try {
				CategoryLogic categoryLogic 				= new CategoryLogic();
				ResourceProfilesLogic resourceProfilesLogic = new ResourceProfilesLogic();
				PerformingOrgLogic performingOrgLogic 		= new PerformingOrgLogic();
				CustomerLogic customerLogic 				= new CustomerLogic();
				CustomertypeLogic customertypeLogic			= new CustomertypeLogic();
				ProgramLogic programLogic 					= new ProgramLogic();
				
				req.setAttribute("customers", customerLogic.searchByCompany(getCompany(req)));
				req.setAttribute("customertypes", customertypeLogic.findByCompany(getUser(req)));
				req.setAttribute("profiles", resourceProfilesLogic.findAll());
				req.setAttribute("programs", programLogic.searchByPerfOrg(getUser(req).getPerformingorg()));
				req.setAttribute("categories", categoryLogic.findByCompanyAndEnabled(getCompany(req), null));
				req.setAttribute("perforgs", performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req), Performingorg.NAME, Order.ASC));
				
				
				if (NEW_PROJECT.equals(accion)) {
					req.setAttribute("title", getResourceBundle(req).getString("title.new_project"));
					req.setAttribute("type", Constants.TYPE_PROJECT);
				}
				else {
					req.setAttribute("title", getResourceBundle(req).getString("title.new_investment"));
					req.setAttribute("type", Constants.TYPE_INVESTMENT);
				}
			} 
			catch (Exception e) {
				ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
			}
			
			forward("/index.jsp?nextForm=project/new_project", req, resp);
		}
		else {
			req.setAttribute(StringPool.ERROR, getResourceBundle(req).getString("msg.error.permission.create_project"));
			forward("/index.jsp", req, resp);
		}
	}


	private void viewProjects(HttpServletRequest req, HttpServletResponse resp, String accion) throws ServletException, IOException {

		if ((SecurityUtil.isUserInRole(req, Constants.ROLE_PMO))            || 		// PMO
			(SecurityUtil.isUserInRole(req, Constants.ROLE_IM))             || 	    // Investment Manager
			(SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM))          || 		// Porfolio Manager
			(SecurityUtil.isUserInRole(req, Constants.ROLE_PM))             || 		// Project Manager
			(SecurityUtil.isUserInRole(req, Constants.ROLE_PROGM))          ||		// Program Manager
            (SecurityUtil.isUserInRole(req, Constants.ROLE_FM))             ||		// Functional Manager
			(SecurityUtil.isUserInRole(req, Constants.ROLE_STAKEHOLDER))    ||	    // Stakeholder
			(SecurityUtil.isUserInRole(req, Constants.ROLE_SPONSOR))        ||      // Sponsor
            (SecurityUtil.isUserInRole(req, Constants.ROLE_LOGISTIC))) {            // Logistic
			
			List<Customertype> cusType	 					= null;
			List<Customer> customers						= null;
			List<Category> categories	 					= null;
			List<Program> programs		 					= null;
			List<Employee> projtManagers 					= null;
			List<Seller> sellers		 					= null;
			List<Geography> geographys	 					= null;
			List<Employee> sponsors		 					= null;
			List<Fundingsource> fundingSources				= null;
			List<Performingorg> performingorgs				= null;
			List<Label> labels								= null;
            List<Technology> technologies					= null;
            List<Stagegate> stageGates						= null;
			List<Resourcepool> resourcepools 				= null;
			List<Milestonetype> milestoneTypes 				= null;
			List<Classificationlevel> classificationsLevel 	= null;
			List<Milestonecategory> milestoneCategories 	= null;
			
			try {

				// Declare logics
				//
				CategoryLogic categoryLogic 						= new CategoryLogic();
				GeographyLogic geoLogic 							= new GeographyLogic();
				CustomerLogic customerLogic 						= new CustomerLogic();
				CustomertypeLogic customertypeLogic 				= new CustomertypeLogic();
		    	EmployeeLogic employeeLogic 						= new EmployeeLogic();
		    	FundingsourceLogic fundingsourceLogic 				= new FundingsourceLogic();
		    	ProgramLogic programLogic 							= new ProgramLogic();
		    	SellerLogic sellerLogic 							= new SellerLogic();
		    	PerformingOrgLogic performingOrgLogic				= new PerformingOrgLogic();
		    	LabelLogic labelLogic 								= new LabelLogic();
                TechnologyLogic technologyLogic                     = new TechnologyLogic();
		    	StagegateLogic stagegateLogic						= new StagegateLogic();
				ConfigurationLogic configurationLogic				= new ConfigurationLogic();
		    	ContractTypeLogic contractTypeLogic					= new ContractTypeLogic();
		    	ResourcepoolLogic resourcepoolLogic 				= new ResourcepoolLogic();
		    	MilestonetypeLogic milestonetypeLogic				= new MilestonetypeLogic();
		    	RiskcategoryLogic riskcategoryLogic					= new RiskcategoryLogic();
		    	ClassificationlevelLogic classificationlevelLogic 	= new ClassificationlevelLogic();
		    	MilestonecategoryLogic milestonecategoryLogic		= new MilestonecategoryLogic();
		    	
		    	// Logics
		    	//
				if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)) {
					
					List<String> joins = new ArrayList<String>();
					joins.add(Program.EMPLOYEE);
					joins.add(Program.EMPLOYEE + "." + Employee.CONTACT);
					
					programs = programLogic.consByCompany(getUser(req), joins);
				}
				else if (SecurityUtil.isUserInRole(req, Constants.ROLE_PROGM)) {
					programs = programLogic.consByProgramManager(getUser(req));
				}
				else { programs	= programLogic.consByPO(getUser(req)); }
				
				cusType				= customertypeLogic.findByCompany(getUser(req));
				customers			= customerLogic.searchByCompany(getCompany(req));
				categories			= categoryLogic.findByRelation(Category.COMPANY, getCompany(req), Category.NAME, Order.ASC);
				projtManagers		= employeeLogic.findByPOAndRol(getUser(req).getPerformingorg(), Constants.ROLE_PM);
				sponsors			= employeeLogic.findByPOAndRol(getUser(req).getPerformingorg(), Constants.ROLE_SPONSOR);
				sellers				= sellerLogic.searchByCompany(getCompany(req));
				geographys	 		= geoLogic.findByRelation(Geography.COMPANY, getCompany(req), Geography.NAME, Order.ASC);
				fundingSources		= fundingsourceLogic.findByRelation(Fundingsource.COMPANY, getCompany(req), Fundingsource.NAME, Order.ASC);
				labels				= labelLogic.findEnableByCompany(getUser(req));
                technologies	    = technologyLogic.findByRelation(Technology.COMPANY, getCompany(req), Technology.NAME, Order.ASC);
				stageGates			= stagegateLogic.findByRelation(Stagegate.COMPANY, getCompany(req), Stagegate.NAME, Order.ASC);
				milestoneTypes		= milestonetypeLogic.findByRelation(Milestonetype.COMPANY, getCompany(req), Milestonetype.NAME, Order.ASC);
				milestoneCategories = milestonecategoryLogic.findByRelation(Milestonecategory.COMPANY, getCompany(req), Milestonecategory.NAME, Order.ASC);
				
				if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)) {
					performingorgs = performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req), Performingorg.NAME, Order.ASC);
				}
					
				if (!LIST_INVESTMENTS.equals(accion)) {
					resourcepools = resourcepoolLogic.findByRelation(Resourcepool.COMPANY, getCompany(req));
				}
				
				classificationsLevel = classificationlevelLogic.findByRelation(Classificationlevel.COMPANY, getCompany(req), Classificationlevel.NAME, Order.ASC);
				
				// Response
				//
				req.setAttribute("functionalManagers", employeeLogic.findByPOAndRol(getUser(req).getPerformingorg(), Constants.ROLE_FM));
				req.setAttribute("contractTypes", contractTypeLogic.findByRelation(Contracttype.COMPANY, getCompany(req), Contracttype.DESCRIPTION, Order.ASC));
				req.setAttribute("risksCatory", riskcategoryLogic.findByRelation(Riskcategory.COMPANY, getCompany(req), Riskcategory.NAME, Order.ASC));
				req.setAttribute("configurations", configurationLogic.findByTypes(getUser(req), Configurations.TYPE_LIST_FILTERS));
				
			}
			catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
			
			/* Message change project PO */
			String projectName 	= ParamUtil.getString(req, "project_name", StringPool.BLANK);
			String poName 		= ParamUtil.getString(req, "po_name", StringPool.BLANK);
			
			if (projectName != StringPool.BLANK && poName != StringPool.BLANK) { 
				info(StringPool.INFORMATION, req, "msg.change_po", projectName, poName); 
			}

			
			req.setAttribute("since", DateUtil.getFirstMonthDay(new Date()));
			req.setAttribute("until", DateUtil.getLastMonthDay(new Date()));
			req.setAttribute("cusType", cusType);
			req.setAttribute("customers", customers);
			req.setAttribute("categories", categories);
			req.setAttribute("programs", programs);
			req.setAttribute("projectManagers", projtManagers);
			req.setAttribute("sellers", sellers);
			req.setAttribute("geographys", geographys);
			req.setAttribute("sponsors", sponsors);
			req.setAttribute("fundingSources", fundingSources);
			req.setAttribute("resourcepools", resourcepools);
			req.setAttribute("labels", labels);
            req.setAttribute("technologies", technologies);
			req.setAttribute("stageGates", stageGates);
			req.setAttribute("milestoneTypes", milestoneTypes);
			req.setAttribute("classificationsLevel", classificationsLevel);
			req.setAttribute("milestoneCategories", milestoneCategories);

			if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)) {
				req.setAttribute("performingorgs", performingorgs);
			}
			
			req.setAttribute("documentStorage", SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_STORAGE, Settings.DEFAULT_PROJECT_DOCUMENT_STORAGE));
			
			req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
			
			if (LIST_INVESTMENTS.equals(accion)) {				
				req.setAttribute("title", getResourceBundle(req).getString("investments"));
				forward("/index.jsp?nextForm=project/list_investments", req, resp);
			}
			else {
				req.setAttribute("title", getResourceBundle(req).getString("projects"));
				forward("/index.jsp?nextForm=project/list_projects", req, resp);
			}
		}
		else {
			forwardServlet(ErrorServlet.REFERENCE + "?accion=403", req, resp);
		}
	}

	
	/**
	 * Generate executive report
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void generateExecutiveReport(HttpServletRequest req, HttpServletResponse resp) 
	throws ServletException, IOException {
		
		boolean error = false;
		byte[] zipBytes = null;

		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			ParamUtil.print(req);
			
			String strIds	= ParamUtil.getString(req, "ids");
			Date since = ParamUtil.getDate(req, "filter_start", getDateFormat(req), null);
			Date until = ParamUtil.getDate(req, "filter_finish", getDateFormat(req), null);

			Integer[] idsList = StringUtil.splitStrToIntegers(strIds, null);
			
			if (idsList != null) {
				List<File> chartersFiles = new ArrayList<File>(); 
				
				List<Project> lista = projectLogic.consList(idsList, null, null, null);				
				
				for (Project proj : lista) {
			
					ProjectExecutiveTemplate executiveTemplate = ProjectExecutiveTemplate.getTemplate(getSettings(req));
					
					File charterDocFile = executiveTemplate.generateDoc(getResourceBundle(req), getSettings(req), proj, since, until);
					
					if (charterDocFile != null) {
						chartersFiles.add(charterDocFile);
					}
				}
				
				if (!chartersFiles.isEmpty()) {
					zipBytes = DocumentUtils.zipFiles(chartersFiles);

                    for (File charterFile : chartersFiles) {

                        // delete temporal file
                        DocumentUtils.deleteFile(charterFile);
                    }
				}
			}
			else {
				info(StringPool.INFORMATION, req, "msg.error.not_for_showing", "projects","executive_report");
			}
		}
		catch (Exception e) {
			error = true;
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}
		
		if (zipBytes != null && !error) {
			sendFile(req, resp, zipBytes, "executive_reports.zip");
		}
		else {
			viewProjects (req, resp, "");
		}
	}
	
	
	/**
	 * Generate status report
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void generateStatusReport(HttpServletRequest req, HttpServletResponse resp) 
	throws ServletException, IOException {
		
		boolean error = false;
		byte[] zipBytes = null;

		try {
			ProjectCharterLogic projectCharterLogic = new ProjectCharterLogic();
			ProjectLogic projectLogic 				= new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			ParamUtil.print(req);
			
			String strIds	= ParamUtil.getString(req, "ids");

			Integer[] idsList = StringUtil.splitStrToIntegers(strIds, null);
			
			if (idsList != null) {

                List<Project> projects = projectLogic.consList(idsList, null, null, null);

                GenerateReportInterface generateReport = CacheStatic.getGenerateReport(ReportType.PROJECT_CHARTER);

                if (generateReport != null) {

                    // Report files
                    List<ReportFile> reportFiles = new ArrayList<ReportFile>();

                    for (Project project : projects) {
                        // Call to generate report file
                        ReportFile reportFile = generateReport.generate(new ReportParams(project, getResourceBundle(req), getSettings(req), getUser(req)));

                        if (reportFile == null) {
                            LogManager.getLog(this.getClass()).warn("Could not create report file for project: "+project.getIdProject()+" "+project.getProjectName());
                        } else {
                            // Add report file
                            reportFiles.add(reportFile);
                        }
                    }

                    // Create zip file
                    zipBytes = ReportUtil.getInstance().zipFiles(reportFiles);
                }
                else {

                    // TODO javier.hernandez - 21/03/2015 - deprecated code migrate to GenerateReportInterface

                    List<File> chartersFiles = new ArrayList<File>();

                    for (Project proj : projects) {

                        Projectcharter projCharter = projectCharterLogic.findByProject(proj);

                        ProjectCharterTemplate projCharterTemplate = ProjectCharterTemplate.getProjectChaterTemplate(getSettings(req));

                        File charterDocFile = projCharterTemplate.generateCharter(
                                getResourceBundle(req), proj, projCharter, getSettings(req), getUser(req).getContact().getFullName());

                        if (charterDocFile != null) {
                            chartersFiles.add(charterDocFile);
                        }
                    }

                    if (!chartersFiles.isEmpty()) {

                        // Create zip file
                        zipBytes = DocumentUtils.zipFiles(chartersFiles);

                        for (File charterFile : chartersFiles) {

                            // delete temporal file
                            DocumentUtils.deleteFile(charterFile);
                        }
                    }
                }
			}
			else {
				info(StringPool.INFORMATION, req, "msg.error.not_for_showing", "projects","executive_report");
			}
		}
		catch (Exception e) {
			error = true;
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}
		
		if (zipBytes != null && !error) {
			sendFile(req, resp, zipBytes, "executive_reports.zip");
		}
		else {
			viewProjects (req, resp, "");
		}
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void uploadFileSystem(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

        // Request
        //
		int idProject				    = Integer.parseInt(getMultipartField(Documentproject.PROJECT));
		int docId					    = Integer.parseInt(getMultipartField(Documentproject.IDDOCUMENTPROJECT));
		String docType				    = getMultipartField(Documentproject.TYPE);
		String oldName				    = StringPool.BLANK;
		String contentComment 		    = getMultipartField(Documentproject.CONTENTCOMMENT, StringPool.UTF8);
		String creationContact		    = getUser(req).getContact().getFullName();
		Date creationDate			    = DateUtil.getCalendar().getTime();
        Integer idClosureCheckProject   = ValidateUtil.isNotNull(getMultipartField(Closurecheckproject.IDCLOSURECHECKPROJECT)) ?
                Integer.parseInt(getMultipartField(Closurecheckproject.IDCLOSURECHECKPROJECT)) : null;
        Integer idTimeline              =  ValidateUtil.isNotNull(getMultipartField(Timeline.IDTIMELINE)) ?
                Integer.parseInt(getMultipartField(Timeline.IDTIMELINE)) : null;

        // Date format
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_PATTERN);

		JSONObject docJSON = new JSONObject();
        String errorFile = StringPool.BLANK;

		PrintWriter out = resp.getWriter();

		resp.setContentType("text/plain");
		
		Documentproject doc =  new Documentproject();
		
		// Declare logic
		DocumentprojectLogic documentprojectLogic   = new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
        ProjectLogic projectLogic                   = new ProjectLogic(getSettings(req));
		
		try {			
			FileItem dataFile = getMultipartFields().get("file");
			
			// Update	
			if(docId != -1) { 
				
				doc = documentprojectLogic.findById(docId, false);
				
				oldName = doc.getIdDocumentProject() + StringPool.UNDERLINE + (doc.getName().lastIndexOf("\\") != -1 ?
                        StringUtils.formatting(doc.getName().substring(doc.getName().lastIndexOf("\\") + 1)) :
                        StringUtils.formatting(doc.getName()));
			}
			
			// if new file set data
			if (dataFile != null && dataFile.getSize() > 0) {
				
				doc.setName(dataFile.getName().lastIndexOf("\\") != -1 ? dataFile.getName().substring(dataFile.getName().lastIndexOf("\\") + 1) : dataFile.getName());
				doc.setExtension(FilenameUtils.getExtension(dataFile.getName()));
				doc.setMime(dataFile.getContentType());
			}

            Project project = projectLogic.consProject(idProject);

			// Set another data
			doc.setProject(project);
			doc.setType(docType);
			doc.setContentComment(contentComment);
			doc.setCreationContact(creationContact);
			doc.setCreationDate(creationDate);

			// Logic
			doc = documentprojectLogic.save(doc);
			
			if (dataFile != null && dataFile.getSize() > 0) {
				
				String newName		= doc.getIdDocumentProject() + StringPool.UNDERLINE + (dataFile.getName().lastIndexOf("\\") != -1 ?
                        StringUtils.formatting(dataFile.getName().substring(dataFile.getName().lastIndexOf("\\") + 1)) :
                        StringUtils.formatting(dataFile.getName()));

                // TODO jordi.ripoll - 12/05/2015 - los archivos se guardaban en esta ruta - al migrar reestructurar codigo
				String dirBySetting = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_FOLDER, Settings.DEFAULT_PROJECT_DOCUMENT_FOLDER);

                // Create folder by project and status
                //
                String docFolder = dirBySetting.concat(File.separator + StringUtils.formatting(project.getChartLabel()) + StringPool.UNDERLINE + idProject);

                File folderProject = new File(docFolder);

                if (!folderProject.isDirectory()) {
                    // Create dir project
                    folderProject.mkdir();
                }

                // Update doc folder
                docFolder = docFolder.concat(File.separator + docType);

                File folderProjectStatus = new File(docFolder);

                if (!folderProjectStatus.isDirectory()) {
                    // Create dir status
                    folderProjectStatus.mkdir();
                }

				File file = new File(docFolder, newName);

                // Validate file and permissions
                errorFile = documentprojectLogic.validateFile(new File(docFolder));

				// Save file
				dataFile.write(file);
				
				if(!oldName.equals(newName)) {

                    file = new File(dirBySetting, oldName);

                    // Search in new path
                    if (!file.exists()) {
                        file = new File(docFolder, oldName);
                    }

                    // Delete old file
					file.delete();
				}
            }
			
			// Set document into closure check project
            if (idClosureCheckProject != null) {

				// Declare logic
				ClosurecheckprojectLogic closurecheckprojectLogic = new ClosurecheckprojectLogic();

                closurecheckprojectLogic.saveDocument(idClosureCheckProject, doc);
				
				// Response
				docJSON.put(Closurecheckproject.IDCLOSURECHECKPROJECT, idClosureCheckProject);
			}
            // Set document into timeline
            else if (idTimeline != null) {

                // Declare logic
                TimelineLogic timelineLogic = new TimelineLogic(getSettings(req), getResourceBundle(req));

                timelineLogic.saveDocument(idTimeline, doc);

                // Response
                docJSON.put(Timeline.IDTIMELINE, idTimeline);
            }
			
			// Response
			//
			docJSON.put(Documentproject.IDDOCUMENTPROJECT, doc.getIdDocumentProject());
			docJSON.put(Documentproject.TYPE, getResourceBundle(req).getString("documentation." + doc.getType()));
			docJSON.put(Documentproject.NAME, (doc.getName().lastIndexOf("\\") != -1 ? doc.getName().substring(doc.getName().lastIndexOf("\\") + 1) : doc.getName()));
			docJSON.put(Documentproject.CONTENTCOMMENT, doc.getContentComment());
			docJSON.put(Documentproject.CREATIONCONTACT, doc.getCreationContact());
            docJSON.put(Documentproject.CREATIONDATE, dateFormat.format( doc.getCreationDate()));
			docJSON.put("documentType", DocumentType.FILE_SYSTEM.getName()); 
			
			if (docId == -1) {
				infoCreated(getResourceBundle(req), docJSON, "document"); //TODO esto no funciona LA CULPA ES DEL METODO AJAX de subir documento que es diferente
			}
			else {
				infoUpdated(getResourceBundle(req), docJSON, "document");
			}

            // Add messages file error
            if (ValidateUtil.isNotNull(errorFile)) {
                docJSON.put(StringPool.InfoType.ERROR.getName(), errorFile);
            }

            out.print(docJSON);
		}		
		catch (Exception e) {
			
			try {				
				documentprojectLogic.delete(doc);
			} catch (Exception e1) {
				ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e1);	
			}
			
			ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e);			
		} 
		finally { out.close(); }	
	}
	
	/**
	 * Download Document 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void downloadDocument(HttpServletRequest req, HttpServletResponse resp) 
	throws ServletException, IOException {

		try {

            // Request
            Integer idDoc = ParamUtil.getInteger(req, "idDocument");

            File file;
            Documentproject doc;

            // Declare logics
			DocumentprojectLogic documentprojectLogic   = new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
            ProjectLogic projectLogic                   = new ProjectLogic(getSettings(req));

            // Get project document
			doc = documentprojectLogic.findById(idDoc, false);

            // Get doc folder
			String docFolder = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_FOLDER, Settings.DEFAULT_PROJECT_DOCUMENT_FOLDER);

			file = new File(docFolder +	File.separator + doc.getIdDocumentProject() + "_" + doc.getName());

            Project project = projectLogic.consProject(doc.getProject(), null);

            // Search in new path
            if (!file.exists() && doc.getProject() != null && ValidateUtil.isNotNull(doc.getType())) {

                // Dir
                docFolder = docFolder.concat(File.separator + StringUtils.formatting(project.getChartLabel()) + StringPool.UNDERLINE + project.getIdProject() + File.separator + doc.getType());

                // File name
                docFolder = docFolder.concat(File.separator + doc.getIdDocumentProject() + StringPool.UNDERLINE + StringUtils.formatting(doc.getName()));

                file = new File(docFolder);
            }

            if (file.exists()) {
                sendFile(req, resp, DocumentUtils.getBytesFromFile(file), doc.getName());
            }
            else {
                LogManager.getLog(getClass()).error("Not found file: " + docFolder);

                // Constructor message
                ParamResourceBundle messageError = new ParamResourceBundle("msg.error.file_not_found", docFolder);
				messageError.setBlack(true);

                String error = ParamUtil.getString(req, "error", messageError.getMessage(getResourceBundle(req)));

                // Set message error
                req.setAttribute("error", error);

                // Redirect actual servlet
                forwardServlet(Constants.DocumentProjectType.findByName(doc.getType()).getServlet() + "?accion=", req, resp);
            }
        }
		catch (Exception e) {
            ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
        }
	}
	
	/**
	 * Delete document
     *
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteDocument(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		PrintWriter out = resp.getWriter();

		try {

            // Request
            Integer idDoc = ParamUtil.getInteger(req, Documentproject.IDDOCUMENTPROJECT);

            // Declare logic
			DocumentprojectLogic documentprojectLogic = new DocumentprojectLogic(getSettings(req), getResourceBundle(req));

            // Logic
            documentprojectLogic.remove(new Documentproject(idDoc));

            // Response
		    out.print(infoDeleted(getResourceBundle(req), "document"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }		
		finally { out.close(); }
	}
}

