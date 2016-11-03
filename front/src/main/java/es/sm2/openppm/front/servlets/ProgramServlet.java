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
 * File: ProgramServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.charts.ChartArea2D;
import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.javabean.FiltroTabla;
import es.sm2.openppm.core.javabean.ProjectExecutivereport;
import es.sm2.openppm.core.javabean.PropertyRelation;
import es.sm2.openppm.core.logic.impl.ActivitysellerLogic;
import es.sm2.openppm.core.logic.impl.CategoryLogic;
import es.sm2.openppm.core.logic.impl.ChangeControlLogic;
import es.sm2.openppm.core.logic.impl.ChargescostsLogic;
import es.sm2.openppm.core.logic.impl.ChartLogic;
import es.sm2.openppm.core.logic.impl.ClassificationlevelLogic;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.ContractTypeLogic;
import es.sm2.openppm.core.logic.impl.CustomerLogic;
import es.sm2.openppm.core.logic.impl.CustomertypeLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.ExecutivereportLogic;
import es.sm2.openppm.core.logic.impl.FundingsourceLogic;
import es.sm2.openppm.core.logic.impl.GeographyLogic;
import es.sm2.openppm.core.logic.impl.LabelLogic;
import es.sm2.openppm.core.logic.impl.MilestoneLogic;
import es.sm2.openppm.core.logic.impl.MilestonecategoryLogic;
import es.sm2.openppm.core.logic.impl.MilestonetypeLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProcurementpaymentsLogic;
import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.logic.impl.ProjectActivityLogic;
import es.sm2.openppm.core.logic.impl.ProjectFollowupLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.RiskRegisterLogic;
import es.sm2.openppm.core.logic.impl.RiskcategoryLogic;
import es.sm2.openppm.core.logic.impl.SellerLogic;
import es.sm2.openppm.core.logic.impl.StagegateLogic;
import es.sm2.openppm.core.logic.impl.TechnologyLogic;
import es.sm2.openppm.core.logic.impl.TimesheetLogic;
import es.sm2.openppm.core.logic.impl.WorkingcostsLogic;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Changerequestwbsnode;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Classificationlevel;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Customertype;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Executivereport;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.Label;
import es.sm2.openppm.core.model.impl.Milestonecategory;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Procurementpayments;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Projectfundingsource;
import es.sm2.openppm.core.model.impl.Projectlabel;
import es.sm2.openppm.core.model.impl.Riskcategory;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Stagegate;
import es.sm2.openppm.core.model.impl.Technology;
import es.sm2.openppm.core.model.impl.Workingcosts;
import es.sm2.openppm.core.model.search.ChangeControlSearch;
import es.sm2.openppm.core.model.wrap.RangeDateWrap;
import es.sm2.openppm.core.utils.JSONModelUtil;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.front.render.HtmlAttributesRender;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.CsvFile;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.rendersnake.HtmlAttributes;
import org.rendersnake.HtmlCanvas;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Servlet implementation class ProgramServlet
 */
public class ProgramServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	private static final  Logger LOGGER = Logger.getLogger(ProgramServlet.class);
	
	public final static String REFERENCE 	= "program";
	
	/***************** Actions ****************/
	public final static String DEL_PROGRAM				= "delete-program";
	public final static String SAVE_PROGRAM 			= "save-program";
	public final static String EXPORT_STATUS_REPORT_CSV	= "export-status-report-csv";
	public final static String EXPORT_PROJECTS_CSV		= "export-projects-csv";
	public final static String EXPORT_INVESTMENTS_CSV	= "export-investments-csv";
	public final static String EXPORT_MILESTONES_CSV 	= "export-milestones-csv";
	
	/************** Actions AJAX **************/
	public final static String JX_CONS_SALES_FORECAST	= "ajax-cons-sales-forecast";
	public final static String JX_STATUS_REPORT			= "ajax-status-report";
	public final static String JX_RISK_REPORT			= "ajax-risk-report";
	public final static String JX_CONS_GANTT 			= "ajax-cons-gantt";
	public final static String JX_SEARCH_PROGRAMS		= "ajax-search-programs";
	public final static String CHART_BUBBLE				= "chart-bubble";
	public final static String JX_CONS_MILESTONES 		= "ajax-cons-milestones";
    public final static String JX_SET_DEFAULT_DATES		= "ajax-set-default-dates";
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.service(req, resp);
		
		String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		if (SecurityUtil.consUserRole(req) != -1) {
			
			/***************** Actions ****************/
			if (accion == null || "".equals(accion)) { viewPrograms(req, resp); }
			else if (DEL_PROGRAM.equals(accion)) { deleteProgram(req, resp); }
			else if (SAVE_PROGRAM.equals(accion)) { saveProgram(req, resp); }
			else if (EXPORT_STATUS_REPORT_CSV.equals(accion)) { exportSRToCSV(req, resp); }
			else if (EXPORT_PROJECTS_CSV.equals(accion)) { exportProjectsToCSV(req, resp); }
			else if (EXPORT_INVESTMENTS_CSV.equals(accion)) { exportInvestmentsToCSV(req, resp); }
			else if (EXPORT_MILESTONES_CSV.equals(accion)) { exportMilestonesToCSV(req, resp); }
			
			/************** Actions AJAX **************/
			else if (JX_CONS_SALES_FORECAST.equals(accion)) { consSalesForecastJX(req, resp); }
			else if (JX_STATUS_REPORT.equals(accion)) { generateStatusReportJX(req, resp); }
			else if (JX_RISK_REPORT.equals(accion)) { generateRiskReportJX(req, resp); }
			else if (JX_CONS_GANTT.equals(accion)) { consGantt(req, resp); }
			else if (JX_SEARCH_PROGRAMS.equals(accion)) { searchProgramsJX(req, resp); }
			else if (CHART_BUBBLE.equals(accion)) { chartBubbleJX(req, resp); }
			else if (JX_CONS_MILESTONES.equals(accion)) { consMilestonesJX(req, resp); }
            else if (JX_SET_DEFAULT_DATES.equals(accion)) { setDefaultDatesJX(req, resp); }
			
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Generate the default dates:
     * sinceDate=current date
     * untilDate=sinceDate + 1 MONTH
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void setDefaultDatesJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();
        JSONObject datesJSON = new JSONObject();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DATE_PATTERN);

        // Calculate default dates
        Date sinceCalc				= null;
        Date untilCalc 				= null;
        String formatSinceCalc      = null;
        String formatUntilCalc      = null;

        try {
            // Get calendars
            Calendar sinceCal = DateUtil.getCalendar();
            Calendar untilCal = DateUtil.getCalendar();

            // Until date = sinceDate + 1 MONTH
            untilCal.add(Calendar.MONTH, +1);

            // Get current date
            sinceCalc = sinceCal.getTime();
            untilCalc = untilCal.getTime();

            // Format dates
            formatSinceCalc = dt.format(sinceCalc);
            formatUntilCalc = dt.format(untilCalc);

            datesJSON.put("milestoneDateSince",formatSinceCalc);
            datesJSON.put("milestoneDateUntil",formatUntilCalc);

            out.print(datesJSON);
        }
        catch(Exception e){
            ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e);
        }
        finally{
            out.close();
        }
     }


    /**
	 * Generate risk report
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void generateRiskReportJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out 		= resp.getWriter();
		
		try {
			
			// Find parameters
			String idStr			= ParamUtil.getString(req, "ids");
			Integer[] idsProjects 	= StringUtil.splitStrToIntegers(idStr);
			
			// Declare logic
			RiskRegisterLogic riskRegLogic = new RiskRegisterLogic();
			
			// Logic
			out.print(riskRegLogic.generateRiskReport(idsProjects, getResourceBundle(req)));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Export milestones to CSV
	 * 
	 * @param req
	 * @param resp
	 */
	private void exportMilestonesToCSV(HttpServletRequest req, HttpServletResponse resp) {
		
		try {
			// Request
			String idStr 				= ParamUtil.getString(req, "ids");
			Date since	 				= ParamUtil.getDate(req, "milestoneDateSince", getDateFormat(req), null);
			Date until	 				= ParamUtil.getDate(req, "milestoneDateUntil", getDateFormat(req), null);
			Integer idMilestoneType 	= ParamUtil.getInteger(req, Milestones.MILESTONETYPE, -1);
			Integer idMilestoneCategory = ParamUtil.getInteger(req, Milestones.MILESTONECATEGORY, -1);
			String milestonePending 	= ParamUtil.getString(req, "milestonePending");
			Integer[] ids 				= StringUtil.splitStrToIntegers(idStr);
			
			String fileName;
			CsvFile file;
			Milestonetype milestonetype 		= null;
			Milestonecategory milestonecategory = null;
			
			// Declare logic
			MilestoneLogic milestoneLogic = new MilestoneLogic(getSettings(req), getResourceBundle(req));
			
			// If there are  projects export to CSV
			if (ids != null) {

				fileName = getResourceBundle(req).getString("milestones");
				
				// Get milestone type
				if (idMilestoneType != -1) {
					milestonetype = new Milestonetype(idMilestoneType);
				}
				
				// Get milestone category
				if (idMilestoneCategory != -1) {
					milestonecategory = new Milestonecategory(idMilestoneCategory);
				}
				
				// Logic
				file = milestoneLogic.exportToCSV(ids, milestonetype, milestonecategory, since, until, milestonePending);
				
				if (file != null) {
					// Return the file
					sendFile(req, resp, file.getFileBytes(), fileName + ".csv", Constants.FILE_CONTENT_TYPE_CSV);
				}
			}										
		}
		catch (Exception e) { 
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); 
		}	
	}

	/**
	 * Consult milestones by filters
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void consMilestonesJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		PrintWriter out 		= resp.getWriter();
		JSONObject returnJSON 	= new JSONObject();
		
		JSONArray milestonesesJSON = new JSONArray();
		
		try {
			// Request 
			//
			String idStr 				= ParamUtil.getString(req, "ids");
			Date since	 				= ParamUtil.getDate(req, "milestoneDateSince", getDateFormat(req), null);
			Date until	 				= ParamUtil.getDate(req, "milestoneDateUntil", getDateFormat(req), null);
			Integer idMilestoneType 	= ParamUtil.getInteger(req, Milestones.MILESTONETYPE, -1);
			Integer idMilestoneCategory = ParamUtil.getInteger(req, Milestones.MILESTONECATEGORY, -1);
			String milestonePending 	= ParamUtil.getString(req, "milestonePending");
			
			Integer[] ids 				= StringUtil.splitStrToIntegers(idStr);
			
			List<Project> projects 				= null;
			Milestonetype milestonetype 		= null;
			Milestonecategory milestonecategory = null;
			
			// Get projects
			if (ids != null) {
				ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
				projects = projectLogic.consList(ids, null, null, null);   
			}
			
			// Get milestone type
			if (idMilestoneType != -1) {
				milestonetype = new Milestonetype(idMilestoneType);
			}
			
			// Get milestone category
			if (idMilestoneCategory != -1) {
				milestonecategory = new Milestonecategory(idMilestoneCategory);
			}
			
			if (projects == null || projects.isEmpty()) {
				returnJSON = info(getResourceBundle(req), StringPool.INFORMATION, "data_not_found", returnJSON);
			}
			else {
				
				// Declare logic
				MilestoneLogic milestoneLogic = new MilestoneLogic();
				
				// Get milestones
				//
				List<String> joins = new ArrayList<String>();
				joins.add(Milestones.PROJECT);
				joins.add(Milestones.MILESTONETYPE);
				joins.add(Milestones.MILESTONECATEGORY);
				
				// Logic
				List<Milestones> milestones = milestoneLogic.filter(projects, milestonetype, milestonecategory, since, until, milestonePending, Milestones.PLANNED, Constants.ASCENDENT, joins);

				// Response
				//
				if (ValidateUtil.isNotNull(milestones)) {
					
					for (Milestones milestone : milestones) {
						
						JSONObject milestoneJSON = new JSONObject();
						
						// Set data
						milestoneJSON.put("projectId", milestone.getProject().getIdProject());
						
						milestoneJSON.put("projectName", ValidateUtil.isNotNull(milestone.getProject().getProjectName()) ? 
								milestone.getProject().getProjectName() : StringPool.BLANK);
							
						milestoneJSON.put("projectShortName", ValidateUtil.isNotNull(milestone.getProject().getChartLabel()) ? 
							milestone.getProject().getChartLabel() : StringPool.BLANK);
						
						if (milestone.getMilestonetype() != null && ValidateUtil.isNotNull(milestone.getMilestonetype().getName())) {
							milestoneJSON.put("milestoneTypeName", milestone.getMilestonetype().getName());
						}
						else {
							milestoneJSON.put("milestoneTypeName", StringPool.BLANK);
						}
						
						if (milestone.getMilestonecategory() != null && ValidateUtil.isNotNull(milestone.getMilestonecategory().getName())) {
							milestoneJSON.put("milestoneCategoryName", milestone.getMilestonecategory().getName());
						}
						else {
							milestoneJSON.put("milestoneCategoryName", StringPool.BLANK);
						}
						
						milestoneJSON.put("name", ValidateUtil.isNotNull(milestone.getName()) ? 
								milestone.getName() : StringPool.BLANK);
						
						milestoneJSON.put(Milestones.DESCRIPTION, ValidateUtil.isNotNull(milestone.getDescription()) ? 
								milestone.getDescription() : StringPool.BLANK);
						
						if (milestone.getPlanned() != null) {
							milestoneJSON.put("planned", DateUtil.format(getResourceBundle(req), milestone.getPlanned()));
						}
						else {
							milestoneJSON.put("planned", StringPool.BLANK);
						}
						
						if (milestone.getEstimatedDate() != null) {
							milestoneJSON.put("estimated", DateUtil.format(getResourceBundle(req), milestone.getEstimatedDate()));
						}
						else {
							milestoneJSON.put("estimated", StringPool.BLANK);
						}
						
						if (milestone.getAchieved() != null) {
							milestoneJSON.put("actual", DateUtil.format(getResourceBundle(req), milestone.getAchieved()));
						}
						else {
							milestoneJSON.put("actual", StringPool.BLANK);
						}
						
						milestonesesJSON.add(milestoneJSON);
					}
					
					returnJSON.put("milestones", milestonesesJSON);
				}
			}
			
			out.print(returnJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    /**
     * Search programs by filters
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void searchProgramsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String programName 				= ParamUtil.getString(req, Program.PROGRAMNAME, StringPool.BLANK);
		String programBudget 			= ParamUtil.getString(req, "programBudget", StringPool.BLANK);
		String programActualCost 		= ParamUtil.getString(req, "programActualCost", StringPool.BLANK);
		String programBudgetBottomUp 	= ParamUtil.getString(req, "programBudgetBottomUp", StringPool.BLANK);
		String initBudgetYear 			= ParamUtil.getString(req, Program.INITBUDGETYEAR, StringPool.BLANK);
		String finishBudgetYear 		= ParamUtil.getString(req, Program.FINISHBUDGETYEAR, StringPool.BLANK);
		String idStrProgramManager 		= ParamUtil.getString(req, "idsProgramManager", StringPool.BLANK);
		String idStrPerfOrg 			= ParamUtil.getString(req, "idsPerfOrg", StringPool.BLANK);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject returnJSON 					= new JSONObject();
			JSONArray programsJSON 					= new JSONArray();
			ProgramLogic programLogic 				= new ProgramLogic();
			ArrayList<PropertyRelation> list 		= new ArrayList<PropertyRelation>();
			List<Program> programs 					= new ArrayList<Program>();
			List<String> joins 						= new ArrayList<String>();
			PerformingOrgLogic performingOrgLogic	= new PerformingOrgLogic();
			
			// Filter by PO
			if (!idStrPerfOrg.equals( StringPool.BLANK)) {
				
				Integer idsPerfOrg[] = StringUtil.splitStrToIntegers(idStrPerfOrg);
				
				ArrayList<PropertyRelation> listPO = new ArrayList<PropertyRelation>();
				
				for(Integer idPerfOrg : idsPerfOrg){
					listPO.add(new PropertyRelation(Constants.EQUAL_RESTRICTION, Program.PERFORMINGORG, performingOrgLogic.findById(idPerfOrg)));
				}
				
				list.add(new PropertyRelation(Constants.DISJUNCTION, null, listPO));
			}
			
			// Filter by program name, code or description
			if (!programName.equals(StringPool.BLANK)) {
				
				ArrayList<PropertyRelation> listSearch = new ArrayList<PropertyRelation>();
				
				listSearch.add(new PropertyRelation(Constants.ILIKE_RESTRICTION, Program.PROGRAMNAME, "%"+programName+"%"));
				listSearch.add(new PropertyRelation(Constants.ILIKE_RESTRICTION, Program.PROGRAMCODE, "%"+programName+"%"));
				listSearch.add(new PropertyRelation(Constants.ILIKE_RESTRICTION, Program.DESCRIPTION, "%"+programName+"%"));
				
				list.add(new PropertyRelation(Constants.DISJUNCTION, null, listSearch));
			}
			
			// Filter by budget
			String programBudgetArray[] = programBudget.split(StringPool.COMMA);
		
			if (programBudgetArray.length > 0) {
				FiltroTabla filtroBudget = new FiltroTabla(programBudgetArray);
				
				if (filtroBudget.getFilterPriority().equals(FiltroTabla.GREATHER_EQUAL) && filtroBudget.getLast() != null) {
					list.add(new PropertyRelation(Constants.GREATER_OR_EQUAL_RESTRICTION, Program.BUDGET, Double.valueOf(filtroBudget.getLast())));
				}
				else if (filtroBudget.getFilterPriority().equals(FiltroTabla.LESS_EQUAL) && filtroBudget.getLast() != null) {
					list.add(new PropertyRelation(Constants.LESS_OR_EQUAL_RESTRICTION, Program.BUDGET, Double.valueOf(filtroBudget.getLast())));
				}
				else if(filtroBudget.getFilterPriority().equals(FiltroTabla.BETWEEN) && filtroBudget.getFirst() != null && filtroBudget.getLast() != null){
					list.add(new PropertyRelation(Constants.GREATER_OR_EQUAL_RESTRICTION, Program.BUDGET, Double.valueOf(filtroBudget.getFirst())));
					list.add(new PropertyRelation(Constants.LESS_OR_EQUAL_RESTRICTION, Program.BUDGET, Double.valueOf(filtroBudget.getLast())));
				}
			}
			
			// Filter by budget year
			if (!initBudgetYear.equals(StringPool.BLANK)) {
				list.add(new PropertyRelation(Constants.GREATER_OR_EQUAL_RESTRICTION, Program.INITBUDGETYEAR, initBudgetYear));
			}
			
			if (!finishBudgetYear.equals(StringPool.BLANK)) {
				list.add(new PropertyRelation(Constants.LESS_OR_EQUAL_RESTRICTION, Program.FINISHBUDGETYEAR, finishBudgetYear));
			}
			
			// Filter by program manager
			if (SecurityUtil.isUserInRole(req, Constants.ROLE_PROGM)) {
				
				list.add(new PropertyRelation(Constants.EQUAL_RESTRICTION, Program.EMPLOYEE, getUser(req)));
			}
			else {
				if (!idStrProgramManager.equals(StringPool.BLANK)) {
					EmployeeLogic employeeLogic = new EmployeeLogic();
					
					Integer idsProgramManager[] = StringUtil.splitStrToIntegers(idStrProgramManager);
					
					ArrayList<PropertyRelation> listPM = new ArrayList<PropertyRelation>();
					
					for(Integer idProgramManager : idsProgramManager){
						listPM.add(new PropertyRelation(Constants.EQUAL_RESTRICTION, Program.EMPLOYEE, employeeLogic.findById(idProgramManager)));
					}
					
					list.add(new PropertyRelation(Constants.DISJUNCTION, null, listPM));
				}
			}
			
			joins.add(Program.EMPLOYEE);
			joins.add(Program.EMPLOYEE+"."+Employee.CONTACT);
			joins.add(Program.PERFORMINGORG);
			
			if (!list.isEmpty()) {
				if (getUser(req).getResourceprofiles().getIdProfile() != Constants.ROLE_PORFM) {
					list.add(new PropertyRelation(Constants.EQUAL_RESTRICTION, Program.PERFORMINGORG,  getUser(req).getPerformingorg()));
				}
				
				programs = programLogic.findByFilters(list, Program.PROGRAMNAME, Constants.ASCENDENT, joins);
			}
			else {
				if (getUser(req).getResourceprofiles().getIdProfile() == Constants.ROLE_PORFM) {
					programs = programLogic.findAll(joins);
				}
				else {
					programs = programLogic.findByRelation(Program.PERFORMINGORG, getUser(req).getPerformingorg(), Program.PROGRAMNAME, Constants.ASCENDENT, joins);
				}
			}
			
			
			// Filter by calculated values
			List<Program> tempPrograms = null;
			
			// Filter by actual cost
			String programActualCostArray[]	= programActualCost.split(StringPool.COMMA);
			
			if (programActualCostArray.length > 0) {
				FiltroTabla filtroActualCost = new FiltroTabla(programActualCostArray);
				
				tempPrograms = new ArrayList<Program>();
				
				for (Program program : programs) {
					if (filtroActualCost.getFilterPriority().equals(FiltroTabla.GREATHER_EQUAL) &&  filtroActualCost.getLast() != null) {
						if (program.getSumActualCost() >= Double.valueOf(filtroActualCost.getLast())) {
							tempPrograms.add(program);
						}
					}
					else if (filtroActualCost.getFilterPriority().equals(FiltroTabla.LESS_EQUAL) &&  filtroActualCost.getLast() != null) {
						if (program.getSumActualCost() <= Double.valueOf(filtroActualCost.getLast())) {
							tempPrograms.add(program);
						}
					}
					else if (filtroActualCost.getFilterPriority().equals(FiltroTabla.BETWEEN) &&  filtroActualCost.getFirst() != null && filtroActualCost.getLast() != null) {
						if (program.getSumActualCost() >= Double.valueOf(filtroActualCost.getFirst()) && program.getSumActualCost() <= Double.valueOf(filtroActualCost.getLast())) {
							tempPrograms.add(program);
						}
					}
				}
				
				if(!tempPrograms.isEmpty()){
					programs = tempPrograms;
				}
			}
			
			// Filter by butget bottom-up
			String programBudgetBottomUpArray[] = programBudgetBottomUp.split(StringPool.COMMA);
			
			if (programBudgetBottomUpArray.length > 0) {
				FiltroTabla filtroBudgetBottomUp = new FiltroTabla(programBudgetBottomUpArray);
				
				tempPrograms = new ArrayList<Program>();
				
				for (Program program : programs) {
					if (filtroBudgetBottomUp.getFilterPriority().equals(FiltroTabla.GREATHER_EQUAL) &&  filtroBudgetBottomUp.getLast() != null) {
						if (program.getBudgetBottomUp() >= Double.valueOf(filtroBudgetBottomUp.getLast())) {
							tempPrograms.add(program);
						}
					}
					else if (filtroBudgetBottomUp.getFilterPriority().equals(FiltroTabla.LESS_EQUAL) &&  filtroBudgetBottomUp.getLast() != null) {
						if (program.getBudgetBottomUp() <= Double.valueOf(filtroBudgetBottomUp.getLast())) {
							tempPrograms.add(program);
						}
					}
					else if (filtroBudgetBottomUp.getFilterPriority().equals(FiltroTabla.BETWEEN) &&  filtroBudgetBottomUp.getFirst() != null && filtroBudgetBottomUp.getLast() != null) {
						if (program.getBudgetBottomUp() >= Double.valueOf(filtroBudgetBottomUp.getFirst()) && program.getBudgetBottomUp() <= Double.valueOf(filtroBudgetBottomUp.getLast())) {
							tempPrograms.add(program);
						}
					}
				}
				
				if(!tempPrograms.isEmpty()){
					programs = tempPrograms;
				}
			}
			
			// return
			for(Program program : programs){
				
				JSONObject programJSON = new JSONObject();
				
				programJSON.put("idProgram", program.getIdProgram());
				programJSON.put("programManagerFullName", program.getEmployee().getContact().getFullName());
				programJSON.put("programCode", program.getProgramCode());
				programJSON.put("programName", program.getProgramName());
				programJSON.put("description", program.getDescription());
				programJSON.put("perfOrg", program.getPerformingorg().getName());
				programJSON.put("initBudgetYear", program.getInitBudgetYear());
				programJSON.put("finishBudgetYear", program.getFinishBudgetYear());
				programJSON.put("budget", program.getBudget());
				programJSON.put("sumActualCost", program.getSumActualCost());
				programJSON.put("budgetBottomUp", program.getBudgetBottomUp());
				programJSON.put("programTitle", program.getProgramTitle());
				programJSON.put("programDoc", program.getProgramDoc());
				programJSON.put("priorization", program.getPriorization());
				programJSON.put("idEmployee", program.getEmployee().getIdEmployee());
				
				programsJSON.add(programJSON);
			}
			
			returnJSON.put("list_programs", programsJSON);
			
			out.print(returnJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
		
	}

	/**
	 * Cons chart gantt
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void consGantt(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		try {
			
			String idStr 		    = ParamUtil.getString(req, "ids");
			String property 	    = ParamUtil.getString(req, "propiedad");
			String order 		    = ParamUtil.getString(req, "orden");
			Date since	 		    = ParamUtil.getDate(req, "since", getDateFormat(req), null);
			Date until	 		    = ParamUtil.getDate(req, "until", getDateFormat(req), null);
			Integer[] ids 		    = StringUtil.splitStrToIntegers(idStr);
            boolean showMilestones	= ParamUtil.getBoolean(req, "showMilestones", true);

			List<Project> projects = null;
			
			if (ids != null) {
				ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
				
				// FIXME list of project from projectwrap
				if ("baselineFinishDate".equals(property) || "baselineInitDate".equals(property)) {
					property = Project.PROJECTNAME;
				}
				
				projects = projectLogic.consList(ids, property, order, null);   
			}
			
			JSONObject returnJSON = new JSONObject();
			
			if (projects == null || projects.isEmpty()) {
				returnJSON = info(getResourceBundle(req), StringPool.INFORMATION, "data_not_found", returnJSON);
				returnJSON.put("noData",true);
			}
			else {
				returnJSON = ChartLogic.consChartGantt(getResourceBundle(req), projects, since, until, showMilestones, getSettings(req));
				
				if (returnJSON == null) {
					
					returnJSON = new JSONObject();
					returnJSON.put("noData",true);
				}
				else {
					returnJSON.put("noData",false);
				}
			}
			out.print(returnJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Generate Status Report
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void generateStatusReportJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {
		
		// Declare response
		List<ProjectExecutivereport> listProjectExecutivereport = new ArrayList<ProjectExecutivereport>();
		
		try {
			
			// Find parameters
			String idStr	= ParamUtil.getString(req, "ids");
			String property = ParamUtil.getString(req, "property",Project.IDPROJECT);
			String order	= ParamUtil.getString(req, "order",Constants.DESCENDENT);
			String page 	= ParamUtil.getString(req, "page", "");
			
			// Depending of the page, set the status 
			String status = "";
			if (page.equals(Constants.PAGE_INVESTMENT)) {
				
				status = Constants.STATUS_INITIATING;
			}
			else if (page.equals(Constants.PAGE_PROGRAM) || page.equals(Constants.PAGE_PROJECT)){
				
				status = Constants.STATUS_CONTROL;
			}

			// Declare logic
			ProjectFollowupLogic projectFollowupLogic	= new ProjectFollowupLogic();
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));
			ExecutivereportLogic executivereportLogic	= new ExecutivereportLogic();
			
			Integer[] ids = StringUtil.splitStrToIntegers(idStr, null);
			
			
			if (ids != null) {
				
				// FIXME list of project from projectwrap
				if ("baselineFinishDate".equals(property) || "baselineInitDate".equals(property)) {
					property = Project.PROJECTNAME;
				}
				
				List<Project> projects = projectLogic.consList(ids, property, order, null);
				
				
				// For each project find followup and executive report
				for (Project project : projects) {
					
					Projectfollowup followup = projectFollowupLogic.findLastWithDataByProject(project);
					Executivereport executivereport = null;
					
					if (Boolean.parseBoolean(SettingUtil.getString(getSettings(req),
							Settings.SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT,
							Settings.SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT_DEFAULT))){
						
						executivereport = executivereportLogic.findByProject(project);
					}
					
					ProjectExecutivereport projectExecutivereport = new ProjectExecutivereport();
					projectExecutivereport.setProjectfollowup(followup);
					projectExecutivereport.setExecutivereport(executivereport);
					projectExecutivereport.setProject(project);
					projectExecutivereport.setStatus(status);
					
					listProjectExecutivereport.add(projectExecutivereport);
				}
				
				// Set the data
				req.setAttribute("projects", listProjectExecutivereport);
			}

		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);  }
 
		forward("/project/common/status_report_table.ajax.jsp", req, resp);
	}

	/**
	 * Create Sales Forecast Chart
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void consSalesForecastJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		String idStr = ParamUtil.getString(req, "ids");
		
		PrintWriter out = resp.getWriter();
		
		try {
			Integer[] ids = StringUtil.splitStrToIntegers(idStr, null);
			
			JSONObject returnJSON = new JSONObject();
			
			if (ids != null) {
					ChartArea2D chartSales 	= ChartLogic.consChartSales(getResourceBundle(req), ids);
				
				if (chartSales != null) {
					returnJSON.put("xml", chartSales.generateXML());
				}
				else {
					returnJSON = info(getResourceBundle(req), StringPool.INFORMATION, "msg.error.data", returnJSON);
				}
			}
			else {
				returnJSON = info(getResourceBundle(req), StringPool.INFORMATION, "data_not_found", returnJSON);
			}
			
			out.print(returnJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
	 * View programs
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewPrograms(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<Program> programs		= null;
		List<Employee> progManagers	= null;
		List<Customer> customers	= null;
		List<Category> categories	= null;
		List<Employee> projManagers = null;
		List<Seller> sellers		= null;
		List<Geography> geographys	= null;
		List<Employee> sponsors		= null;
		List<Customertype> cusType	= null;
		List<Fundingsource> fundingSources				= null;
		List<Performingorg> performingorgs				= null;
		List<Label> labels								= null;
        List<Technology> technologies					= null;
		List<Stagegate> stageGates						= null;
		List<Milestonetype> milestoneTypes 				= null;
		List<Classificationlevel> classificationsLevel 	= null;
		List<Milestonecategory> milestoneCategories 	= null;
		
		try {
			ProgramLogic programLogic = new ProgramLogic();
			
			List<String> joins = new ArrayList<String>();
			
			if (SecurityUtil.isUserInRole(req, Constants.ROLE_PROGM)) {
				programs = programLogic.consByProgramManager(getUser(req));
			}
			else if (SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)
					|| SecurityUtil.isUserInRole(req, Constants.ROLE_FM)) {
				
				programs = programLogic.consByPO(getUser(req));	
			}
			else if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)) {
				joins.add(Program.EMPLOYEE);
				joins.add(Program.EMPLOYEE+"."+Employee.CONTACT);
				joins.add(Program.PERFORMINGORG);
				
				programs = programLogic.consByCompany(getUser(req), joins);
			}

			// Declare logics
			CategoryLogic categoryLogic 						= new CategoryLogic();
			GeographyLogic geoLogic 							= new GeographyLogic();
			CustomerLogic customerLogic 						= new CustomerLogic();
			CustomertypeLogic customertypeLogic 				= new CustomertypeLogic();
	    	EmployeeLogic employeeLogic 						= new EmployeeLogic();
	    	FundingsourceLogic fundingsourceLogic 				= new FundingsourceLogic();
	    	SellerLogic sellerLogic 							= new SellerLogic();
	    	PerformingOrgLogic performingOrgLogic				= new PerformingOrgLogic();
	    	LabelLogic labelLogic 								= new LabelLogic();
            TechnologyLogic technologyLogic                     = new TechnologyLogic();
	    	StagegateLogic stagegateLogic						= new StagegateLogic();
	    	ConfigurationLogic configurationLogic				= new ConfigurationLogic();
	    	ContractTypeLogic contractTypeLogic					= new ContractTypeLogic();
	    	MilestonetypeLogic milestonetypeLogic				= new MilestonetypeLogic();
	    	RiskcategoryLogic riskcategoryLogic					= new RiskcategoryLogic();
	    	ClassificationlevelLogic classificationlevelLogic 	= new ClassificationlevelLogic();
	    	MilestonecategoryLogic milestonecategoryLogic		= new MilestonecategoryLogic();
	    	
	    	// Logics 
			//
	    	if (!SecurityUtil.isUserInRole(req, Constants.ROLE_PROGM)) {
	    		progManagers 	= employeeLogic.findByPOAndRol(getUser(req).getPerformingorg(), Constants.ROLE_PROGM);
	    	}
			cusType		 		= customertypeLogic.findByCompany(getUser(req));
			customers	 		= customerLogic.searchByCompany(getCompany(req));
			sponsors	 		= employeeLogic.findByPOAndRol(getUser(req).getPerformingorg(), Constants.ROLE_SPONSOR);
			categories			= categoryLogic.findByRelation(Category.COMPANY, getCompany(req), Category.NAME, Order.ASC);
			geographys	 		= geoLogic.findByRelation(Geography.COMPANY, getCompany(req), Geography.NAME, Order.ASC);
			sellers		 		= sellerLogic.searchByCompany(getCompany(req));
			projManagers 		= employeeLogic.findByPOAndRol(getUser(req).getPerformingorg(), Constants.ROLE_PM);
			fundingSources		= fundingsourceLogic.findByRelation(Fundingsource.COMPANY, getCompany(req), Fundingsource.NAME, Order.ASC);
			labels				= labelLogic.findEnableByCompany(getUser(req));
            technologies	    = technologyLogic.findByRelation(Technology.COMPANY, getCompany(req), Technology.NAME, Order.ASC);
			stageGates			= stagegateLogic.findByRelation(Stagegate.COMPANY, getCompany(req), Stagegate.NAME, Order.ASC);
			milestoneTypes		= milestonetypeLogic.findByRelation(Milestonetype.COMPANY, getCompany(req), Milestonetype.NAME, Order.ASC);
			milestoneCategories = milestonecategoryLogic.findByRelation(Milestonecategory.COMPANY, getCompany(req), Milestonecategory.NAME, Order.ASC);
			
			if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)) {
				performingorgs = performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req), Performingorg.NAME, Order.ASC);
			}
			
			classificationsLevel = classificationlevelLogic.findByRelation(Classificationlevel.COMPANY, getCompany(req), Classificationlevel.NAME, Order.ASC);
			
			// Response 
			//
			req.setAttribute("risksCatory", riskcategoryLogic.findByRelation(Riskcategory.COMPANY, getCompany(req), Riskcategory.NAME, Order.ASC));
			req.setAttribute("functionalManagers", employeeLogic.findByPOAndRol(getUser(req).getPerformingorg(), Constants.ROLE_FM));
			req.setAttribute("contractTypes", contractTypeLogic.findByRelation(Contracttype.COMPANY, getCompany(req), Contracttype.DESCRIPTION, Order.ASC));
			req.setAttribute("configurations", configurationLogic.findByTypes(getUser(req), Configurations.TYPE_LIST_FILTERS));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("title", getResourceBundle(req).getString("title.programs"));
		req.setAttribute("list_programs", programs);
		req.setAttribute("profile", getUser(req).getResourceprofiles().getIdProfile());
		req.setAttribute("list_employees", progManagers);
		req.setAttribute("cusType", cusType);
		req.setAttribute("customers", customers);
		req.setAttribute("projectManagers", projManagers);
		req.setAttribute("categories", categories);
		req.setAttribute("geographys", geographys);
		req.setAttribute("sellers", sellers);
		req.setAttribute("sponsors", sponsors);
		req.setAttribute("fundingSources", fundingSources);
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("labels", labels);
        req.setAttribute("technologies", technologies);
		req.setAttribute("stageGates", stageGates);
		req.setAttribute("milestoneTypes", milestoneTypes);
		req.setAttribute("classificationsLevel", classificationsLevel);
		req.setAttribute("milestoneCategories", milestoneCategories);
		
		if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)) {
			req.setAttribute("performingorgs", performingorgs);
		}
		
		forward("/index.jsp?nextForm=program/list_programs", req, resp);
	}	

	
	/**
	 * Create bubble chart
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void chartBubbleJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		PrintWriter out = resp.getWriter();
		
		try {
			String idStr				= ParamUtil.getString(req, "ids");
			String axesXType			= ParamUtil.getString(req, "axesXType");
			String idRiskCategoryStr	= ParamUtil.getString(req, Riskcategory.ENTITY);
			boolean adjustmentApply 	= ParamUtil.getBoolean(req, "priorityAdjustmentApply",false);
			
			Integer[] ids 				= StringUtil.splitStrToIntegers(idStr, null);
			Integer[] idRiskCategories 	= StringUtil.splitStrToIntegers(idRiskCategoryStr, null);
			
			JSONArray chartJSON 		= new JSONArray();
			JSONArray projectsJSON 		= new JSONArray();
			
			// Declare logic
			ProjectLogic projectLogic 	= new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			if (ids != null && ids.length > 0) {
				
				// Joins 
				//
				List<String> joins = new ArrayList<String>();
				
				joins.add(Project.CUSTOMER);
				joins.add(Project.CUSTOMER + "." + Customer.CUSTOMERTYPE);
				joins.add(Project.PROGRAM);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER + "." + Employee.CONTACT);
				joins.add(Project.EMPLOYEEBYSPONSOR);
				joins.add(Project.EMPLOYEEBYSPONSOR + "." + Employee.CONTACT);
				joins.add(Project.CATEGORY);
				joins.add(Project.GEOGRAPHY);
				
				// Logic
				List<Project> projList = projectLogic.consList(ids, null, null, joins);
				
				chartJSON = ChartLogic.createBubbleChart(getResourceBundle(req), projList, Project.TCV, axesXType, adjustmentApply, idRiskCategories);

				// Solution for padding jqplot chart
				if (axesXType.equals(Project.INITDATE) || axesXType.equals(Project.ENDDATE)) {
					Date lessStart			= null;
					Date greaterStart		= null;
					Date start 				= null;
					Date lessFinish			= null;
					Date greaterFinish		= null;
					Date finish				= null;
					Calendar calendar 		= DateUtil.getCalendar();
					SimpleDateFormat date 	= new SimpleDateFormat("MM/dd/yyyy");
					
					// Search less and greater date
					for (Project project : projList) {
						
						// Create information chart
						if (axesXType.equals(Project.INITDATE)) {
							start = project.getStartDate();
							
							if (lessStart == null || start.before(lessStart)) {
								lessStart = start;
							}
							
							if (greaterStart == null || start.after(greaterStart)) {
								greaterStart = start;
							}
						}
						else if (axesXType.equals(Project.ENDDATE)) {
							finish = project.getFinishDate();
							
							if (lessFinish == null || finish.before(lessFinish)) {
								lessFinish = finish;
							}
							
							if (greaterFinish == null || finish.after(greaterFinish)) {
								greaterFinish = finish;
							}
						}
					}
					
					// Add bubble after and before
					if (lessStart != null && greaterStart != null) {
						calendar.setTime(lessStart);
						calendar.add(Calendar.DAY_OF_MONTH, -7);
						chartJSON.add(JSONModelUtil.createBubbleProject(date.format(calendar.getTime()), 50, 0, "", "#ffffff", null, null, null));
						
						calendar.setTime(greaterStart);
						calendar.add(Calendar.DAY_OF_MONTH, +7);
						chartJSON.add(JSONModelUtil.createBubbleProject(date.format(calendar.getTime()), 50, 0, "", "#ffffff", null, null, null));
						
					}
					else if (lessFinish != null && greaterFinish != null) {
						calendar.setTime(lessFinish);
						calendar.add(Calendar.DAY_OF_MONTH, -7);
						chartJSON.add(JSONModelUtil.createBubbleProject(date.format(calendar.getTime()), 50, 0, "", "#ffffff", null, null, null));
						
						calendar.setTime(greaterFinish);
						calendar.add(Calendar.DAY_OF_MONTH, +7);
						chartJSON.add(JSONModelUtil.createBubbleProject(date.format(calendar.getTime()), 50, 0, "", "#ffffff", null, null, null));
					}
					
				}
				else {
					chartJSON.add(JSONModelUtil.createBubbleProject(50, 50, 0, "", "#ffffff", null, null, null));
				}
			
				// Create information for show in adjustment table
				for (Project project : projList) {
					
					
					int priority = project.getPriority() == null ?0:project.getPriority();
					int strategicAdjustment = project.getStrategicAdjustament() == null?0:project.getStrategicAdjustament();
					int riskRatingAdjustament = project.getRiskRatingAdjustament() == null?0:project.getRiskRatingAdjustament();
					int actualRiskRating = ChartLogic.calculateRiskRatingActual(project, idRiskCategories);
					
					JSONObject projectJSON = new JSONObject();
					projectJSON.put(Project.IDPROJECT, project.getIdProject());
					projectJSON.put(Project.PROJECTNAME, project.getProjectName());
					
					HtmlAttributes attributes = new HtmlAttributes();
					attributes.class_("actualRiskRating");
					projectJSON.put("actualRiskRating", new HtmlCanvas()
		                   	.span(attributes)
		                   	.content(String.valueOf(actualRiskRating))
		                    .toHtml());
					
					attributes = new HtmlAttributes();
					attributes.class_("actualStrategic");
					projectJSON.put(Project.PRIORITY, new HtmlCanvas()
		                   	.span(attributes)
		                   	.content(String.valueOf(priority))
		                    .toHtml());
					
					attributes = new HtmlAttributes();
					attributes.class_("riskAdjust");
					projectJSON.put(Project.RISKRATINGADJUSTAMENT, new HtmlCanvas()
		                   	.span(attributes)
		                   	.content(project.getUseRiskAdjust() ? String.valueOf(riskRatingAdjustament - actualRiskRating) : StringPool.BLANK)
		                    .toHtml());
					
					attributes = new HtmlAttributes();
					attributes.class_("strategicAdjust");
					projectJSON.put(Project.STRATEGICADJUSTAMENT, new HtmlCanvas()
		                   	.span(attributes)
	                   	 	.content(project.getUseStrategicAdjust() ? String.valueOf(strategicAdjustment - priority) : StringPool.BLANK)
		                    .toHtml());
					
                    // Create html buttons
					//
					
					// Risk rating
                    attributes = new HtmlAttributes();
                    attributes.type(HtmlAttributesRender.TYPE_HIDDEN.getTag());
                    attributes.name("actualRiskRating"+project.getIdProject());
                    attributes.value(actualRiskRating);

                    HtmlAttributes attributesTotal = new HtmlAttributes();
                    attributesTotal.name("totalRiskRating"+project.getIdProject());
                    attributesTotal.class_("campo totalRiskRating " + (project.getUseRiskAdjust()?StringPool.BLANK:"disabled"));
                    attributesTotal.style("width:100px");
                    attributesTotal.add(HtmlAttributesRender.NUMBER.getTag(), Boolean.TRUE.toString());
                    attributesTotal.value(project.getUseRiskAdjust()? riskRatingAdjustament+"" : null);
                    if (!project.getUseRiskAdjust()) {
                    	attributesTotal.add(HtmlAttributesRender.READONLY.getTag(), "readonly");
                    }
                    
                    HtmlAttributes attributesCheckbox = new HtmlAttributes();
                    attributesCheckbox.type(HtmlAttributesRender.TYPE_CHECKBOX.getTag());
                    attributesCheckbox.name("useRiskAdjust"+project.getIdProject());
                    attributesCheckbox.style("width:20px");
                    attributesCheckbox.class_("useRiskAdjust");
                    
                    if (!SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)) {
                    	attributesCheckbox.add(HtmlAttributesRender.DISABLED.getTag(), HtmlAttributesRender.DISABLED.getTag());
                    }
                    
                    if (project.getUseRiskAdjust()) {
                    	attributesCheckbox.add(HtmlAttributesRender.CHECKED.getTag(), StringPool.BLANK);
                    }

                    projectJSON.put("totalRiskRatingHTML", new HtmlCanvas()
                    		.input(attributesTotal)
                            .input(attributes)
                            .input(attributesCheckbox)
                            .toHtml());

                    // Strategic alignment
                    attributesTotal = new HtmlAttributes();
                    attributesTotal.name("totalAdjustment"+project.getIdProject());
                    attributesTotal.class_("campo percent totalAdjustment " + (project.getUseStrategicAdjust()?StringPool.BLANK:"disabled"));
                    attributesTotal.style("width:100px");
                    attributesTotal.value(project.getUseStrategicAdjust() ? strategicAdjustment : null);
                    attributesTotal.add(HtmlAttributesRender.RANGE.getTag(), "0,100");
                    if (!project.getUseStrategicAdjust()) {
                    	attributesTotal.add(HtmlAttributesRender.READONLY.getTag(), "readonly");
                    }
                    
                    attributesCheckbox = new HtmlAttributes();
                    attributesCheckbox.type(HtmlAttributesRender.TYPE_CHECKBOX.getTag());
                    attributesCheckbox.name("useStrategicAdjust"+project.getIdProject());
                    attributesCheckbox.style("width:20px");
                    attributesCheckbox.class_("useStrategicAdjust");
                    
                    if (!SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)) {
                    	attributesCheckbox.add(HtmlAttributesRender.DISABLED.getTag(), HtmlAttributesRender.DISABLED.getTag());
                    }
                    
                    if (project.getUseStrategicAdjust()) {
                    	attributesCheckbox.add(HtmlAttributesRender.CHECKED.getTag(), StringPool.BLANK);
                    }
                    
                    projectJSON.put("totalAdjustmentHTML", new HtmlCanvas()
                    		.input(attributesTotal)
                    		.input(attributesCheckbox)
                    		.toHtml());

                    // Add project for edit
                    projectsJSON.add(projectJSON);
                }

			}
			
			JSONObject data = new JSONObject();
			data.put("chart", chartJSON);
			data.put("projects", projectsJSON);
			
			out.print(data);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}
	

	/**
	 * Delete program
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteProgram(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Integer idProgram = ParamUtil.getInteger(req, "id", -1);
		
		try {
			ProgramLogic programLogic = new ProgramLogic();
			
			programLogic.deleteProgram(idProgram);
			infoDeleted(req, "program");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewPrograms(req, resp);
	}
	
	/**
	 * Save program
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveProgram(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idProgram			= ParamUtil.getInteger(req, "program_id", -1);
		int idProgManager		= ParamUtil.getInteger(req, "program_manager");
		String programCode		= ParamUtil.getString(req, "program_code", "");
		String programName		= ParamUtil.getString(req, "program_name", "");	
		String programTitle		= ParamUtil.getString(req, "program_title", "");	
		String description		= ParamUtil.getString(req, "program_description", "");
		String programDoc		= ParamUtil.getString(req, "program_doc", "");
		Double budget			= ParamUtil.getCurrency(req, "budget", 0);
		String initBudgetYear	= ParamUtil.getString(req, "initBudgetYear", "");
		String finishBudgetYear	= ParamUtil.getString(req, "finishBudgetYear", "");
		
		try {		
			Program program				= null;
			ProgramLogic programLogic 	= new ProgramLogic();
			
			if (idProgram == -1){
				program = new Program();
				
				program.setPerformingorg(getUser(req).getPerformingorg());
				infoCreated(req, "program");
			}
			else {
				program = programLogic.consProgram(idProgram);
				infoUpdated(req, "program");
			}
			
			if (SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)) {
				
				program.setEmployee(new Employee(idProgManager));
				program.setProgramCode(programCode);
				program.setProgramName(programName);
				program.setProgramTitle(programTitle);
				program.setProgramDoc(programDoc);
				program.setBudget(budget);
				program.setInitBudgetYear(initBudgetYear);
				program.setFinishBudgetYear(finishBudgetYear);
			}
			
			program.setDescription(description);
			
			programLogic.save(program);
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewPrograms(req, resp);
	}
	
	/**
	 * Export status report to csv
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void exportSRToCSV (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			// Get parameters
			String idStr = ParamUtil.getString(req, "ids");
			String fileName = "";
			CsvFile file = null;
		
			// Declare logic
			ExecutivereportLogic executivereportLogic	= new ExecutivereportLogic(getResourceBundle(req));
			
			// Get 'show_report' setting 
			Boolean showExecutivereport = Boolean.parseBoolean(SettingUtil.getString(getSettings(req),
					Settings.SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT,
					Settings.SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT_DEFAULT));
			
			Integer[] ids = StringUtil.splitStrToIntegers(idStr, null);
			
			// If there are  projects, export to csv
			if (ids != null) {

				fileName = getResourceBundle(req).getString("status_report.csv_file_name");
				file = executivereportLogic.exportSRToCSV(ids, showExecutivereport);
				
				// Return the file
				sendFile(req, resp, file.getFileBytes(), fileName + ".csv", Constants.FILE_CONTENT_TYPE_CSV);
			}										
		}
		catch (Exception e) { 
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); 
		}	
	}
	
	/**
	 * Export projects to csv
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void exportProjectsToCSV (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String idStr = ParamUtil.getString(req, "ids");
		String fileName = "";
		CsvFile file = null;

        ResourceBundle bundle = getResourceBundle(req);
        try {
			
			Integer[] ids = StringUtil.splitStrToIntegers(idStr, null);

			if (ids != null) {
				
				// Declare logic
				WorkingcostsLogic workingcostsLogic 				= new WorkingcostsLogic();
				ProjectLogic projectLogic 							= new ProjectLogic(getSettings(req), bundle);
				ProcurementpaymentsLogic procurementpaymentsLogic 	= new ProcurementpaymentsLogic();
                SellerLogic sellerLogic                             = new SellerLogic();
                TimesheetLogic timesheetLogic                       = new TimesheetLogic();

                SimpleDateFormat date = new SimpleDateFormat(Constants.DATE_YEAR_PATTERN);
				
				fileName = "Projects";
				file = new CsvFile(Constants.SEPARATOR_CSV);
				
				boolean showIdproject	= SettingUtil.getBoolean(getSettings(req), Settings.SETTING_CSV_COLUMN_IDPROJECT, Settings.DEFAULT_CSV_COLUMN_IDPROJECT);
				boolean exportFollowup	= SettingUtil.getBoolean(getSettings(req), Settings.SETTING_FOLLOWUP_INFORMATION_CSV, Settings.DEFAULT_FOLLOWUP_INFORMATION_CSV);
				
				if (showIdproject) { file.addValue("id"); }
				file.addValue(bundle.getString("project.chart_label"));
				file.addValue(bundle.getString("project"));
				file.addValue(bundle.getString("project.budget_year"));
				file.addValue(bundle.getString("project.accounting_code"));
				file.addValue(bundle.getString("investment_manager"));
				file.addValue(bundle.getString("project_manager"));
				file.addValue(bundle.getString("program"));
				file.addValue(bundle.getString("category"));
				file.addValue(bundle.getString("business_manager"));
				file.addValue(bundle.getString("sponsor"));
				file.addValue(bundle.getString("customer"));
				file.addValue(bundle.getString("customer_type"));
				file.addValue(bundle.getString("contract_type"));
				file.addValue(bundle.getString("funding_source"));
				file.addValue(bundle.getString("stage_gates"));
                file.addValue(bundle.getString("classificationLevel"));
				file.addValue(bundle.getString("project.net_value"));
				file.addValue(bundle.getString("activity.budget"));
				file.addValue(bundle.getString("bac"));
				file.addValue(bundle.getString("PROJECT.TOR"));
				file.addValue(bundle.getString("baseline_start"));
				file.addValue(bundle.getString("baseline_finish"));
				file.addValue(bundle.getString("planned_start"));
				file.addValue(bundle.getString("planned_finish"));
                file.addValue(bundle.getString("sac"));
				file.addValue(bundle.getString("change.priority"));
				file.addValue(bundle.getString("project.external_cost"));
				file.addValue(bundle.getString("geography"));
				file.addValue(bundle.getString("status.investment"));
				file.addValue(bundle.getString("perf_organization"));
                file.addValue(bundle.getString("program"));
				file.addValue(bundle.getString("rag"));
				file.addValue(bundle.getString("status"));
				file.addValue(bundle.getString("percent_complete"));
				file.addValue(bundle.getString("start"));
				file.addValue(bundle.getString("finish"));
				file.addValue(bundle.getString("project.internal"));
				file.addValue(bundle.getString("sellerscosts"));
				file.addValue(bundle.getString("infrastructurecosts"));
				file.addValue(bundle.getString("licensescosts"));
				file.addValue(bundle.getString("project.internal_cost") + " " + bundle.getString("currency.euro"));
				file.addValue(bundle.getString("project.internal_cost")+ " " + bundle.getString("workingcosts.units"));
				
				String[] departments = SettingUtil.getString(getSettings(req), Settings.SETTING_WORKINGCOSTS_DEPARTMENTS, Settings.DEFAULT_WORKINGCOSTS_DEPARTMENTS).split(","); 
				
				if(!departments[0].equals(StringPool.BLANK)) {
					for(String s : departments) {
						file.addValue(s);	
					}
					file.addValue(bundle.getString("workingcosts.total"));
				}	
				
				// Real external cost
				file.addValue(bundle.getString("actual.external.cost"));
				
				// Externals cost for years
				//
				// Select range dates to payments for projects
				RangeDateWrap rangeDates = procurementpaymentsLogic.findMinAndMaxActualDatesByProjects(ids);
				
				List<Integer> yearsExternalCost = new ArrayList<Integer>();
				
				// Set list years
				if (rangeDates != null && rangeDates.getMinDate() != null
						&& rangeDates.getMaxDate() != null) {
					
					Integer minActualDate = Integer.parseInt(date.format(rangeDates.getMinDate()));
					Integer maxActualDate = Integer.parseInt(date.format(rangeDates.getMaxDate()));
					
					// Secuence of years
					for (int year = minActualDate; year <= maxActualDate; year++) {
						
						yearsExternalCost.add(year);
						
						file.addValue(bundle.getString("actual.external.cost") + StringPool.SPACE + year);
					}
				}
				
				// Real internal cost
				file.addValue(bundle.getString("actual.internal.cost"));

				file.addValue(bundle.getString("hours")+StringPool.SPACE+bundle.getString("applevel.app3"));

				// Project dates
				file.addValue(bundle.getString("project.date.csv.initiation"));
				file.addValue(bundle.getString("project.date.csv.planning"));
				file.addValue(bundle.getString("project.date.csv.execution"));
				file.addValue(bundle.getString("project.date.csv.closing"));
				
				List<String> joins = new ArrayList<String>();
				joins.add(Project.CONTRACTTYPE);
				joins.add(Project.CUSTOMER);
				joins.add(Project.CUSTOMER + "." + Customer.CUSTOMERTYPE);
				joins.add(Project.PROGRAM);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER + "." + Employee.CONTACT);
				joins.add(Project.EMPLOYEEBYSPONSOR);
				joins.add(Project.EMPLOYEEBYSPONSOR + "." + Employee.CONTACT);
				joins.add(Project.CATEGORY);
				joins.add(Project.GEOGRAPHY);
				joins.add(Project.EMPLOYEEBYINVESTMENTMANAGER);
				joins.add(Project.EMPLOYEEBYINVESTMENTMANAGER+ "." + Employee.CONTACT);
				joins.add(Project.EMPLOYEEBYFUNCTIONALMANAGER);
				joins.add(Project.EMPLOYEEBYFUNCTIONALMANAGER + "." + Employee.CONTACT);
				joins.add(Project.PROJECTFUNDINGSOURCES);
				joins.add(Project.PROJECTFUNDINGSOURCES + "." + Fundingsource.ENTITY);
				joins.add(Project.PERFORMINGORG);
				joins.add(Project.PROJECTLABELS);
				joins.add(Project.PROJECTLABELS + "." + Label.ENTITY);
				joins.add(Project.STAGEGATE);
                joins.add(Project.CLASSIFICATIONLEVEL);
				joins.add(Project.WORKINGCOSTSES);

				List<Project> projects = projectLogic.consList(ids, null, null, joins);
				
				// Labels
				LabelLogic labelLogic = new LabelLogic();
				
				List<Label> labels = labelLogic.findByProjects(projects);
				
				if (ValidateUtil.isNotNull(labels)) {
					file.addValue(bundle.getString("label"));
					
					for (Label label : labels) {
						file.addValue(label.getName());
					}
				}

				// Sellers
				ActivitysellerLogic activitysellerLogic = new ActivitysellerLogic();

                joins = new ArrayList<String>();
                joins.add(Activityseller.PROJECTACTIVITY+"."+Activityseller.PROJECT);
                joins.add(Activityseller.SELLER);


                // Array of projects for find
                Project[] projectsArray = projects.toArray(new Project[projects.size()]);

                // Activity sellers of projects
                List<Activityseller> activitySellers = null;

                // Sellers fo projects
                List<Seller> sellers = sellerLogic.findByProcurement(projectsArray);

                if (ValidateUtil.isNotNull(sellers)) {

                    // Activity sellers of projects
                    activitySellers = activitysellerLogic.consActivitySellerByProject(joins, projectsArray);

                    // Add column name
                    file.addValue(bundle.getString("seller"));

                    // Add diferents columns of seller names
                    for (Seller seller : sellers) {
                        file.addValue(seller.getName());
                    }
                }

				// NEW LINE
				//
				file.newLine();
				
				for (Project project : projects) {
					
					Projectfollowup followup = project.getLastFollowup();
					ChargescostsLogic chargescostsLogic = new ChargescostsLogic();
					
					Character risk = (followup != null && followup.getGeneralFlag() != null
							? followup.getGeneralFlag() : null);
					
					String generalRisk = StringPool.BLANK;	
					
					if (risk != null) {
						
						if (risk.equals(Constants.RISK_LOW)) { generalRisk = Character.toString(Constants.RISK_LOW); }
						else if (risk.equals(Constants.RISK_MEDIUM)) { generalRisk = Character.toString(Constants.RISK_MEDIUM); }
						else if (risk.equals(Constants.RISK_HIGH)) { generalRisk = Character.toString(Constants.RISK_HIGH); }
						else if (risk.equals(Constants.RISK_NORMAL)) { generalRisk = Character.toString(Constants.RISK_NORMAL); }						
					}
					if (showIdproject) { file.addValue(project.getIdProject()+StringPool.BLANK); }	
					
					file.addValue(project.getChartLabel());
					file.addValue(project.getProjectName());
					file.addValue((project.getBudgetYear() == null ? StringPool.BLANK : project.getBudgetYear().toString()));
					file.addValue(project.getAccountingCode() == null ? StringPool.BLANK : project.getAccountingCode());
					file.addValue(project.getEmployeeByInvestmentManager() != null
							?project.getEmployeeByInvestmentManager().getContact().getFullName()
							:StringPool.BLANK);
					file.addValue((project.getEmployeeByProjectManager() == null || project.getEmployeeByProjectManager().getContact() == null ? StringPool.BLANK : project.getEmployeeByProjectManager().getContact().getFullName()));
					file.addValue((project.getProgram() == null ? StringPool.BLANK : project.getProgram().getProgramName()));
					file.addValue((project.getCategory() == null ? StringPool.BLANK : project.getCategory().getName()));
					file.addValue(project.getEmployeeByFunctionalManager() != null
							?project.getEmployeeByFunctionalManager().getContact().getFullName()
							:StringPool.BLANK);
					file.addValue((project.getEmployeeBySponsor()== null || project.getEmployeeBySponsor().getContact() == null ? StringPool.BLANK : project.getEmployeeBySponsor().getContact().getFullName()));
					file.addValue((project.getCustomer() == null ? StringPool.BLANK : project.getCustomer().getName()));
					file.addValue((project.getCustomer() == null || project.getCustomer().getCustomertype() == null ? StringPool.BLANK : project.getCustomer().getCustomertype().getName()));
					file.addValue(project.getContracttype() == null ? StringPool.BLANK : project.getContracttype().getDescription());
					
					String strFundingSources = StringPool.BLANK;
					for(Projectfundingsource fundingSource : project.getProjectfundingsources()){
						strFundingSources += fundingSource.getFundingsource().getName();
						strFundingSources += StringPool.COMMA;
					}
					if (strFundingSources.endsWith(StringPool.COMMA)){
						strFundingSources = strFundingSources.substring(0,strFundingSources.length()-1);
					}
					file.addValue(strFundingSources);
					// Stage gates
                    file.addValue(project.getStagegate() == null ? StringPool.BLANK : project.getStagegate().getName());
                    file.addValue(project.getClassificationlevel() == null ? StringPool.BLANK : project.getClassificationlevel().getName());
                    file.addValue(project.getNetIncome() == null ? StringPool.BLANK : ValidateUtil.toCurrency(project.getNetIncome()));
                    file.addValue(project.getTcv() == null ? StringPool.BLANK : ValidateUtil.toCurrency(project.getTcv()));
                    file.addValue((project.getBac() == null ? StringPool.BLANK : ValidateUtil.toCurrency(project.getBac())));

                    Double totalOR = calculateTotalOR(project);
                    file.addValue((totalOR == null ? StringPool.BLANK : ValidateUtil.toCurrency(totalOR)));

                    file.addValue(project.getPlannedInitDate() != null ? getDateFormat(req).format(project.getPlannedInitDate()) : StringPool.BLANK);
                    file.addValue(project.getPlannedFinishDate() != null ? getDateFormat(req).format(project.getPlannedFinishDate()) : StringPool.BLANK);
                    file.addValue(project.getCalculatedPlanStartDate() != null ? getDateFormat(req).format(project.getCalculatedPlanStartDate()) : StringPool.BLANK);
                    file.addValue(project.getCalculatedPlanFinishDate() != null ? getDateFormat(req).format(project.getCalculatedPlanFinishDate()) : StringPool.BLANK);
                    file.addValue(project.getDuration() == null ? StringPool.BLANK  : project.getDuration().toString());
                    file.addValue(project.getPriority() == null ? StringPool.BLANK : project.getPriority().toString());
                    file.addValue(ValidateUtil.toCurrency(chargescostsLogic.consExternalCostByProject(project)));
                    file.addValue((project.getGeography() == null ? StringPool.BLANK : project.getGeography().getName()));

                    String state = bundle.getString("investments.status." + (ValidateUtil.isNull(project.getInvestmentStatus()) ?
                                    Constants.INVESTMENT_IN_PROCESS : project.getInvestmentStatus())
                    );
					file.addValue(state);
					file.addValue(project.getPerformingorg().getName());
                    file.addValue(project.getProgram().getProgramName());
					file.addValue(generalRisk);
					file.addValue(bundle.getString("project_status." + project.getStatus()));
					file.addValue(project.getPoc() != null ? ValidateUtil.toPercent(project.getPoc()) : StringPool.BLANK);
					file.addValue(project.getStartDate() != null ? getDateFormat(req).format(project.getStartDate()) : StringPool.BLANK);
					file.addValue(project.getFinishDate() != null ? getDateFormat(req).format(project.getFinishDate()) : StringPool.BLANK);
					file.addValue((project.getInternalProject() != null && project.getInternalProject() ? Constants.YES : Constants.NO));
					
					double result = 0;

                    // Seller costs
                    //
                    List <Chargescosts> list = chargescostsLogic.consChargescostsByProject(project, Constants.SELLER_CHARGE_COST);

					if (ValidateUtil.isNotNull(list)) {

						for(Chargescosts c : list) {

                            if (c.getCost() != null) {
                                result += c.getCost();
                            }
						}
					}

					file.addValue(ValidateUtil.toCurrency(result));

                    // Infrastructure costs
                    //
					result = 0;
					list = chargescostsLogic.consChargescostsByProject(project, Constants.INFRASTRUCTURE_CHARGE_COST);

					if(ValidateUtil.isNotNull(list)) {

						for(Chargescosts c : list) {

                            if (c.getCost() != null) {
                                result += c.getCost();
                            }
						}
					}

					file.addValue(ValidateUtil.toCurrency(result));

                    // License costs
                    //
					result = 0;
					list = chargescostsLogic.consChargescostsByProject(project, Constants.LICENSE_CHARGE_COST);

					if (ValidateUtil.isNotNull(list)) {

						for(Chargescosts c : list) {

                            if (c.getCost() != null) {
                                result += c.getCost();
                            }
						}
					}

					file.addValue(ValidateUtil.toCurrency(result));
					
					double resultEuro = 0;
					
					if (ValidateUtil.isNotNull(project.getWorkingcostses())) {
						
						for (Workingcosts cost : project.getWorkingcostses()) {

                            if (cost.getEffort() != null && cost.getRate() != null) {
                                resultEuro += Double.valueOf(cost.getEffort()) * cost.getRate();
                            }
						}
					}
					
					file.addValue(ValidateUtil.toCurrency(resultEuro));
					
					file.addValue(new Integer(workingcostsLogic.consInternalCostByProject(project)).toString());
					
					if(!departments[0].equals(StringPool.BLANK)) {
						int total = 0;
						for(String s : departments) {
							int temp = workingcostsLogic.getCostByDeptAndProject(project, s);
							file.addValue(new Integer(temp).toString());
							total += temp;
						}
						file.addValue(new Integer(total).toString());
					}								

					// Export Followup information
					if (exportFollowup) { executeExportFollowup(file, project); }
					
					// Actual external and internal cost
					//
					ProjectActivityLogic projectActivityLogic 			= new ProjectActivityLogic(getSettings(req), bundle);
					
					// Actual payments
					//
					joins = new ArrayList<String>();
					joins.add(Procurementpayments.SELLER);
					
					List<Procurementpayments> procPayments = procurementpaymentsLogic.consProcurementPaymentsByProject(project, joins);
					
					double totalActualPayment 	= 0.0;
					for (Procurementpayments procPayment : procPayments) {
						totalActualPayment += procPayment.getActualPayment();
					}
					file.addValue(ValidateUtil.toCurrency(totalActualPayment));	
					
					// Externals cost for years
					//
					if (ValidateUtil.isNotNull(yearsExternalCost)) {
						
						for (Integer yearExternalCost : yearsExternalCost) {
							
							totalActualPayment = 0.0;
					
							if (ValidateUtil.isNotNull(procPayments)) {
								
								for (Procurementpayments procPayment : procPayments) {
									
									if (procPayment.getActualDate() != null && yearExternalCost.equals(Integer.parseInt(date.format(procPayment.getActualDate())))) {
										totalActualPayment += procPayment.getActualPayment();
									}
								}
							}
							
							file.addValue(ValidateUtil.toCurrency(totalActualPayment));	
						}
					}
					
					// AC activities 
					//
					List<Projectactivity> activities = projectActivityLogic.consActivities(project, null);
					
					// Find setting
					boolean exludeWithProvider = SettingUtil.getBoolean(getSettings(req),
							Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER,
							Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER_DEFAULT);
					
					// Calculate AC
					double	acApp3 				= 0.0;
					for (Projectactivity activity : activities) {
						
						acApp3 += timesheetLogic.calcACforActivityAndStatus(activity.getIdActivity(), Constants.TIMESTATUS_APP3, null, exludeWithProvider);
						
					}
					
					file.addValue(ValidateUtil.toCurrency(acApp3));

                    // Hours in app3
                    double hours = timesheetLogic.getHoursResource(project, Constants.TIMESTATUS_APP3);
                    file.addValue(ValidateUtil.toCurrency(hours));

					// Project dates
					file.addValue(project.getInitDate() != null ? getDateFormat(req).format(project.getInitDate()) : StringPool.BLANK);
					file.addValue(project.getPlanDate() != null ? getDateFormat(req).format(project.getPlanDate()) : StringPool.BLANK);
					file.addValue(project.getExecDate() != null ? getDateFormat(req).format(project.getExecDate()) : StringPool.BLANK);
					file.addValue(project.getEndDate() != null ? getDateFormat(req).format(project.getEndDate()) : StringPool.BLANK);


					// Labels
					if (ValidateUtil.isNotNull(labels)) {
						
						file.addValue(StringPool.SPACE);
						
						boolean found;
						for (Label label : labels) {
							
							found = false;
							
							for (Projectlabel projectlabel : project.getProjectlabels()) {
								
								if (label.getName().equals(projectlabel.getLabel().getName())) {
									found = true;
								}
							}
							
							if (found) {
								file.addValue(label.getName());
							}
							else {
								file.addValue(StringPool.SPACE);
							}
						}
					}

                    // Add value of seller column
					if (ValidateUtil.isNotNull(sellers)) {

						file.addValue(StringPool.SPACE);

						boolean found;
						for (Seller seller : sellers) {

							found = false;

                            int i = 0;
                            while (!found && i < activitySellers.size()) {

                                // Set current activitySeller
                                Activityseller activityseller = activitySellers.get(i++);

                                // Cheeck if is equals
								if (project.getIdProject().equals(activityseller.getProjectactivity().getProject().getIdProject())
                                    && seller.getIdSeller().equals(activityseller.getSeller().getIdSeller())) {
									found = true;
								}
							}

							if (found) {
								file.addValue(seller.getName());
							}
							else {
								file.addValue(StringPool.SPACE);
							}
						}
					}
					
					// NEW LINE
					//
					file.newLine();		
				}
				
				sendFile(req, resp, file.getFileBytes(), fileName + ".csv", Constants.FILE_CONTENT_TYPE_CSV);
			}										
		}
		catch (Exception e) { 
			ExceptionUtil.evalueException(req, bundle, LOGGER, e);
		}		
	}

	/**
	 * Export investments to csv
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void exportInvestmentsToCSV (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String idStr = ParamUtil.getString(req, "ids");
		String fileName = "";
		CsvFile file = null;

		try {
			
			Integer[] ids = StringUtil.splitStrToIntegers(idStr, null);

			if (ids != null) {
				
				// Declare logic
				WorkingcostsLogic workingcostsLogic 				= new WorkingcostsLogic();
				ProjectLogic projectLogic 							= new ProjectLogic(getSettings(req), getResourceBundle(req));
				ProcurementpaymentsLogic procurementpaymentsLogic 	= new ProcurementpaymentsLogic();
				TimesheetLogic timesheetLogic 						= new TimesheetLogic();
				ProjectActivityLogic projectActivityLogic 			= new ProjectActivityLogic(getSettings(req), getResourceBundle(req));
				
				SimpleDateFormat date = new SimpleDateFormat(Constants.DATE_YEAR_PATTERN);
				
				fileName = "Investments";
				file = new CsvFile(Constants.SEPARATOR_CSV);
				
				boolean showIdproject	= SettingUtil.getBoolean(getSettings(req), Settings.SETTING_CSV_COLUMN_IDPROJECT, Settings.DEFAULT_CSV_COLUMN_IDPROJECT);
				boolean exportFollowup	= SettingUtil.getBoolean(getSettings(req), Settings.SETTING_FOLLOWUP_INFORMATION_CSV, Settings.DEFAULT_FOLLOWUP_INFORMATION_CSV);
				
				if (showIdproject) { file.addValue("id"); }
				file.addValue(getResourceBundle(req).getString("project.chart_label"));
				file.addValue(getResourceBundle(req).getString("investment"));
				file.addValue(getResourceBundle(req).getString("project.budget_year"));
				file.addValue(getResourceBundle(req).getString("project.accounting_code"));
				file.addValue(getResourceBundle(req).getString("investment_manager"));
				file.addValue(getResourceBundle(req).getString("project_manager"));
				file.addValue(getResourceBundle(req).getString("program"));
				file.addValue(getResourceBundle(req).getString("category"));
				file.addValue(getResourceBundle(req).getString("business_manager"));
				file.addValue(getResourceBundle(req).getString("sponsor"));
				file.addValue(getResourceBundle(req).getString("customer"));
				file.addValue(getResourceBundle(req).getString("customer_type"));
				file.addValue(getResourceBundle(req).getString("contract_type"));
				file.addValue(getResourceBundle(req).getString("funding_source"));
				file.addValue(getResourceBundle(req).getString("stage_gates"));
                file.addValue(getResourceBundle(req).getString("classificationLevel"));
				file.addValue(getResourceBundle(req).getString("project.net_value"));
				file.addValue(getResourceBundle(req).getString("activity.budget"));
				file.addValue(getResourceBundle(req).getString("bac"));
                file.addValue(getResourceBundle(req).getString("PROJECT.TOR"));
				file.addValue(getResourceBundle(req).getString("baseline_start"));
				file.addValue(getResourceBundle(req).getString("baseline_finish"));
				file.addValue(getResourceBundle(req).getString("planned_start"));
				file.addValue(getResourceBundle(req).getString("planned_finish"));
                file.addValue(getResourceBundle(req).getString("sac"));
				file.addValue(getResourceBundle(req).getString("change.priority"));
				file.addValue(getResourceBundle(req).getString("project.external_cost"));
				file.addValue(getResourceBundle(req).getString("geography"));				
				file.addValue(getResourceBundle(req).getString("status"));
				file.addValue(getResourceBundle(req).getString("perf_organization"));
				file.addValue(getResourceBundle(req).getString("comments"));
				file.addValue(getResourceBundle(req).getString("proposal.win_probability"));
				file.addValue(getResourceBundle(req).getString("start"));
				file.addValue(getResourceBundle(req).getString("finish"));
				file.addValue(getResourceBundle(req).getString("project.internal"));
				file.addValue(getResourceBundle(req).getString("sellerscosts"));
				file.addValue(getResourceBundle(req).getString("infrastructurecosts"));
				file.addValue(getResourceBundle(req).getString("licensescosts"));				
				file.addValue(getResourceBundle(req).getString("project.internal_cost") + " " + getResourceBundle(req).getString("currency.euro"));
				file.addValue(getResourceBundle(req).getString("project.internal_cost")+ " " + getResourceBundle(req).getString("workingcosts.units"));
				
				String[] departments = SettingUtil.getString(getSettings(req), Settings.SETTING_WORKINGCOSTS_DEPARTMENTS, Settings.DEFAULT_WORKINGCOSTS_DEPARTMENTS).split(",");
								
				if(!departments[0].equals(StringPool.BLANK)) {
					for(String s : departments) {
						file.addValue(s);	
					}
					file.addValue(getResourceBundle(req).getString("workingcosts.total"));
				}
				
				// External cost
				file.addValue(getResourceBundle(req).getString("actual.external.cost"));
				
				// Externals cost for years
				//
				// Select range dates to payments for projects
				RangeDateWrap rangeDates = procurementpaymentsLogic.findMinAndMaxActualDatesByProjects(ids);
				
				List<Integer> yearsExternalCost = new ArrayList<Integer>();
				
				// Set list years
				if (rangeDates != null && rangeDates.getMinDate() != null && rangeDates.getMaxDate() != null) {
					
					Integer minActualDate = Integer.parseInt(date.format(rangeDates.getMinDate()));
					Integer maxActualDate = Integer.parseInt(date.format(rangeDates.getMaxDate()));
					
					// Secuence of years
					for (int year = minActualDate; year <= maxActualDate; year++) {
						
						yearsExternalCost.add(year);
						
						file.addValue(getResourceBundle(req).getString("actual.external.cost") + StringPool.SPACE + year);
					}
				}
				
				file.addValue(getResourceBundle(req).getString("actual.internal.cost"));
				
				// Project dates
				file.addValue(getResourceBundle(req).getString("project.date.csv.initiation"));
				file.addValue(getResourceBundle(req).getString("project.date.csv.planning"));
				file.addValue(getResourceBundle(req).getString("project.date.csv.execution"));
				file.addValue(getResourceBundle(req).getString("project.date.csv.closing"));
				
				// Project Charter labels
				file.addValue(getResourceBundle(req).getString("project_charter.project_description"));
				file.addValue(getResourceBundle(req).getString("project_charter.project_objectives"));
				file.addValue(getResourceBundle(req).getString("project_charter.business_need"));
				file.addValue(getResourceBundle(req).getString("project_charter.main_constraints"));
				file.addValue(getResourceBundle(req).getString("project_charter.main_risks"));
				file.addValue(getResourceBundle(req).getString("project_charter.main_assumptions"));
				file.addValue(getResourceBundle(req).getString("project_charter.main_deliverables"));
				file.addValue(getResourceBundle(req).getString("project_charter.exclusions"));
			
				
				List<String> joins = new ArrayList<String>();
				joins.add(Project.CONTRACTTYPE);
				joins.add(Project.CUSTOMER);
				joins.add(Project.CUSTOMER + "." + Customer.CUSTOMERTYPE);
				joins.add(Project.PROGRAM);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER + "." + Employee.CONTACT);
				joins.add(Project.EMPLOYEEBYSPONSOR);
				joins.add(Project.EMPLOYEEBYSPONSOR + "." + Employee.CONTACT);
				joins.add(Project.CATEGORY);
				joins.add(Project.GEOGRAPHY);
				joins.add(Project.EMPLOYEEBYINVESTMENTMANAGER);
				joins.add(Project.EMPLOYEEBYINVESTMENTMANAGER+ "." + Employee.CONTACT);
				joins.add(Project.EMPLOYEEBYFUNCTIONALMANAGER);
				joins.add(Project.EMPLOYEEBYFUNCTIONALMANAGER + "." + Employee.CONTACT);
				joins.add(Project.PROJECTFUNDINGSOURCES);
				joins.add(Project.PROJECTFUNDINGSOURCES + "." + Fundingsource.ENTITY);
				joins.add(Project.PERFORMINGORG);
				joins.add(Project.PROJECTCHARTERS);
				joins.add(Project.PROJECTLABELS);
				joins.add(Project.PROJECTLABELS + "." + Label.ENTITY);
				joins.add(Project.STAGEGATE);
                joins.add(Project.CLASSIFICATIONLEVEL);
				joins.add(Project.WORKINGCOSTSES);
				
				
				List<Project> projects = projectLogic.consList(ids, null, null, joins);
				
				// Leads and dependents projects
				file.addValue(getResourceBundle(req).getString("project.lead"));
				
				List<Project> allLeads = projectLogic.leads(ids);
				
				for (Project lead : allLeads) {
					file.addValue(lead.getProjectName());
				}
				
				file.addValue(getResourceBundle(req).getString("project.dependent"));
				
				List<Project> allDependents = projectLogic.dependents(ids);
				
				for (Project dependent : allDependents) {
					file.addValue(dependent.getProjectName());
				}
				
				// Labels
				LabelLogic labelLogic = new LabelLogic();
				
				List<Label> labels = labelLogic.findByProjects(projects);
				
				if (ValidateUtil.isNotNull(labels)) {
					
					file.addValue(getResourceBundle(req).getString("labels"));
					
					
					for (Label label : labels) {
						file.addValue(label.getName());
					}
				}
				
				// NEW LINE
				//
				file.newLine();

				for (Project project : projects) {
					
					ChargescostsLogic chargescostsLogic = new ChargescostsLogic();
					
					String state = getResourceBundle(req).getString("investments.status." + (ValidateUtil.isNull(project.getInvestmentStatus()) ?
							Constants.INVESTMENT_IN_PROCESS : project.getInvestmentStatus())
					);
					
					if (showIdproject) { file.addValue(project.getIdProject()+StringPool.BLANK); }
					file.addValue(project.getChartLabel());
					file.addValue(project.getProjectName());
					file.addValue((project.getBudgetYear() == null ? StringPool.BLANK : project.getBudgetYear().toString()));
					file.addValue(project.getAccountingCode() == null ? StringPool.BLANK : project.getAccountingCode());
					file.addValue(project.getEmployeeByInvestmentManager() != null
							?project.getEmployeeByInvestmentManager().getContact().getFullName()
							:StringPool.BLANK);
					file.addValue((project.getEmployeeByProjectManager() == null || project.getEmployeeByProjectManager().getContact() == null ? StringPool.BLANK : project.getEmployeeByProjectManager().getContact().getFullName()));
					file.addValue((project.getProgram() == null ? StringPool.BLANK : project.getProgram().getProgramName()));
					file.addValue((project.getCategory() == null ? StringPool.BLANK : project.getCategory().getName()));
					file.addValue(project.getEmployeeByFunctionalManager() != null
							?project.getEmployeeByFunctionalManager().getContact().getFullName()
							:StringPool.BLANK);
					file.addValue((project.getEmployeeBySponsor()== null || project.getEmployeeBySponsor().getContact() == null ? StringPool.BLANK : project.getEmployeeBySponsor().getContact().getFullName()));
					file.addValue((project.getCustomer() == null ? StringPool.BLANK : project.getCustomer().getName()));
					file.addValue((project.getCustomer() == null || project.getCustomer().getCustomertype() == null ? StringPool.BLANK : project.getCustomer().getCustomertype().getName()));
					file.addValue(project.getContracttype() == null ? StringPool.BLANK : project.getContracttype().getDescription());
					
					String strFundingSources = StringPool.BLANK;
					for(Projectfundingsource fundingSource : project.getProjectfundingsources()){
						strFundingSources += fundingSource.getFundingsource().getName();
						strFundingSources += StringPool.COMMA;
					}
					if (strFundingSources.endsWith(StringPool.COMMA)){
						strFundingSources = strFundingSources.substring(0,strFundingSources.length()-1);
					}
					file.addValue(strFundingSources);
					// Stage gates
					file.addValue(project.getStagegate() == null ? StringPool.BLANK : project.getStagegate().getName());
                    file.addValue(project.getClassificationlevel() == null ? StringPool.BLANK : project.getClassificationlevel().getName());
                    file.addValue(project.getNetIncome() == null ? StringPool.BLANK : ValidateUtil.toCurrency(project.getNetIncome()));
                    file.addValue(project.getTcv() == null ? StringPool.BLANK : ValidateUtil.toCurrency(project.getTcv()));
                    file.addValue((project.getBac() == null ? StringPool.BLANK : ValidateUtil.toCurrency(project.getBac())));

                    Double totalOR = calculateTotalOR(project);
                    file.addValue((totalOR == null ? StringPool.BLANK : ValidateUtil.toCurrency(totalOR)));

                    file.addValue(project.getPlannedInitDate() != null ? getDateFormat(req).format(project.getPlannedInitDate()):StringPool.BLANK);
                    file.addValue(project.getPlannedFinishDate() != null ? getDateFormat(req).format(project.getPlannedFinishDate()):StringPool.BLANK);
                    file.addValue(project.getCalculatedPlanStartDate() != null ? getDateFormat(req).format(project.getCalculatedPlanStartDate()):StringPool.BLANK);
                    file.addValue(project.getCalculatedPlanFinishDate() != null ? getDateFormat(req).format(project.getCalculatedPlanFinishDate()):StringPool.BLANK);
                    file.addValue(project.getDuration() == null ? StringPool.BLANK  : project.getDuration().toString());
                    file.addValue(project.getPriority() == null ? StringPool.BLANK  : project.getPriority().toString());
                    file.addValue(ValidateUtil.toCurrency(chargescostsLogic.consExternalCostByProject(project)));
                    file.addValue((project.getGeography() == null ? StringPool.BLANK : project.getGeography().getName()));
                    file.addValue(state);
                    file.addValue(project.getPerformingorg().getName());

					String strComments = project.getClientComments() == null? StringPool.BLANK : project.getClientComments();
					file.addValue(strComments.replaceAll("[\n\r]", StringPool.SPACE));
					
					file.addValue(project.getProbability() == null ? StringPool.BLANK  : project.getProbability().toString());

					file.addValue(project.getStartDate() != null ? getDateFormat(req).format(project.getStartDate()) : StringPool.BLANK);
					file.addValue(project.getFinishDate() != null ? getDateFormat(req).format(project.getFinishDate()) : StringPool.BLANK);
					
					file.addValue((project.getInternalProject() != null && project.getInternalProject() ? Constants.YES : Constants.NO));
					double result = 0;
					List <Chargescosts> list = null;
					list = chargescostsLogic.consChargescostsByProject(project, Constants.SELLER_CHARGE_COST);
					if(!list.isEmpty()) {
						for(Chargescosts c : list) {
							result += c.getCost();
						}
					}					
					file.addValue(ValidateUtil.toCurrency(result));
					result = 0;
					list = chargescostsLogic.consChargescostsByProject(project, Constants.INFRASTRUCTURE_CHARGE_COST);
					if(!list.isEmpty()) {
						for(Chargescosts c : list) {
							result += c.getCost();
						}
					}					
					file.addValue(ValidateUtil.toCurrency(result));
					result = 0;
					list = chargescostsLogic.consChargescostsByProject(project, Constants.LICENSE_CHARGE_COST);
					if(!list.isEmpty()) {
						for(Chargescosts c : list) {
							result += c.getCost();
						}
					}	
					file.addValue(ValidateUtil.toCurrency(result));
					
					// Project internal cost currency
					double resultEuro = 0;
					
					if (ValidateUtil.isNotNull(project.getWorkingcostses())) {
						
						for (Workingcosts cost : project.getWorkingcostses()) {
							
							resultEuro += Double.valueOf(cost.getEffort()) * cost.getRate();
						}
					}
					
					file.addValue(ValidateUtil.toCurrency(resultEuro));
					
					// Project internal cost units
					file.addValue(new Integer(workingcostsLogic.consInternalCostByProject(project)).toString());
					if(!departments[0].equals(StringPool.BLANK)) {
						int total = 0;
						for(String s : departments) {
							int temp = workingcostsLogic.getCostByDeptAndProject(project, s);
							file.addValue(new Integer(temp).toString());
							total += temp;
						}
						file.addValue(new Integer(total).toString());
					}
					
					// Export Followup information
					if (exportFollowup) { executeExportFollowup(file, project); }
					
					// Actual external and internal cost
					//
					// Actual payments
					joins = new ArrayList<String>();
					joins.add(Procurementpayments.SELLER);
					
					List<Procurementpayments> procPayments = procurementpaymentsLogic.consProcurementPaymentsByProject(project, joins);
					
					double totalActualPayment 	= 0.0;
					for (Procurementpayments procPayment : procPayments) {
						totalActualPayment += procPayment.getActualPayment();
					}
					file.addValue(ValidateUtil.toCurrency(totalActualPayment));	
					
					// Externals cost for years
					//
					if (ValidateUtil.isNotNull(yearsExternalCost)) {
						
						for (Integer yearExternalCost : yearsExternalCost) {
							
							totalActualPayment = 0.0;
					
							if (ValidateUtil.isNotNull(procPayments)) {
								
								for (Procurementpayments procPayment : procPayments) {
									
									if (procPayment.getActualDate() != null && yearExternalCost.equals(Integer.parseInt(date.format(procPayment.getActualDate())))) {
										totalActualPayment += procPayment.getActualPayment();
									}
								}
							}
							
							file.addValue(ValidateUtil.toCurrency(totalActualPayment));	
						}
					}
					
					// AC activities 
					//
					List<Projectactivity> activities = projectActivityLogic.consActivities(project, null);
					
					// Find setting
					boolean exludeWithProvider = SettingUtil.getBoolean(getSettings(req),
							Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER,
							Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER_DEFAULT);
					
					// Calculate AC
					double	acApp3 				= 0.0;
					for (Projectactivity activity : activities) {
						
						acApp3 += timesheetLogic.calcACforActivityAndStatus(activity.getIdActivity(), Constants.TIMESTATUS_APP3, null, exludeWithProvider);
						
					}
					file.addValue(ValidateUtil.toCurrency(acApp3));
					
					// Project dates
					file.addValue(project.getInitDate() != null ? getDateFormat(req).format(project.getInitDate()) : StringPool.BLANK);
					file.addValue(project.getPlanDate() != null ? getDateFormat(req).format(project.getPlanDate()) : StringPool.BLANK);
					file.addValue(project.getExecDate() != null ? getDateFormat(req).format(project.getExecDate()) : StringPool.BLANK);
					file.addValue(project.getEndDate() != null ? getDateFormat(req).format(project.getEndDate()) : StringPool.BLANK);
					
					// Project Charter
					Projectcharter projectCharter = null;
					
					for (Projectcharter projCharter : project.getProjectcharters()) {
						projectCharter = projCharter;
					}
					
					if (projectCharter != null) {
						
						String regEx = "[\t\n\f\r]";
						
						String escapeCharacter = StringPool.SPACE;
						
						file.addValue(projectCharter.getSucessCriteria() != null ? escapeCharacter + projectCharter.getSucessCriteria().replaceAll(regEx, StringPool.SPACE) : StringPool.BLANK);
						file.addValue(projectCharter.getProjectObjectives() != null ? escapeCharacter + projectCharter.getProjectObjectives().replaceAll(regEx, StringPool.SPACE) : StringPool.BLANK);
						file.addValue(projectCharter.getBusinessNeed() != null ? escapeCharacter + projectCharter.getBusinessNeed().replaceAll(regEx, StringPool.SPACE) : StringPool.BLANK);
						file.addValue(projectCharter.getMainConstraints() != null ? escapeCharacter + projectCharter.getMainConstraints().replaceAll(regEx, StringPool.SPACE) : StringPool.BLANK);
						file.addValue(projectCharter.getMainRisks() != null ? escapeCharacter + projectCharter.getMainRisks().replaceAll(regEx, StringPool.SPACE) : StringPool.BLANK);
						file.addValue(projectCharter.getMainAssumptions() != null ? escapeCharacter + projectCharter.getMainAssumptions().replaceAll(regEx, StringPool.SPACE) : StringPool.BLANK);
						file.addValue(projectCharter.getMainDeliverables() != null ? escapeCharacter + projectCharter.getMainDeliverables().replaceAll(regEx, StringPool.SPACE) : StringPool.BLANK);
						file.addValue(projectCharter.getExclusions() != null ? escapeCharacter + projectCharter.getExclusions().replaceAll(regEx, StringPool.SPACE) : StringPool.BLANK);
					}
					
					// Project leads and project dependents
					List<Project> projectLeads = projectLogic.consLeads(project);
					
					file.addValue(StringPool.SPACE);
					
					Integer indexLead;
					
					for (Project lead : allLeads) {
						
						indexLead = -1;
						
						for (int i = 0; i < projectLeads.size(); i++) {
							
							if (lead.equals(projectLeads.get(i))) {
								
								indexLead = i;
								
								break;
							}
						}
						
						if (indexLead != -1) {
							file.addValue(projectLeads.get(indexLead).getProjectName());
						}
						else {
							file.addValue(StringPool.SPACE);
						}
					}
					
					List<Project> projectDependents = projectLogic.consDependents(project);
					
					file.addValue(StringPool.SPACE);
					
					Integer indexDependent;
					
					for (Project dependent : allDependents) {
						
						indexDependent = -1;
						
						for (int i = 0; i < projectDependents.size(); i++) {
							
							if (dependent.equals(projectDependents.get(i))) {
								
								indexDependent = i;
								
								break;
							}
						}
						
						if (indexDependent != -1) {
							file.addValue(projectDependents.get(indexDependent).getProjectName());
						}
						else {
							file.addValue(StringPool.SPACE);
						}
					}
					
					// Labels
					if (ValidateUtil.isNotNull(labels)) {
						
						file.addValue(StringPool.SPACE);
						
						boolean found;
						for (Label label : labels) {
							
							found = false;
							
							for (Projectlabel projectlabel : project.getProjectlabels()) {
								
								if (label.getName().equals(projectlabel.getLabel().getName())) {
									found = true;
								}
							}
							
							if (found) {
								file.addValue(label.getName());
							}
							else {
								file.addValue(StringPool.SPACE);
							}
						}
					}

					// NEW LINE
					file.newLine();		
				}
				
				sendFile(req, resp, file.getFileBytes(), fileName + ".csv", Constants.FILE_CONTENT_TYPE_CSV);
			}										
		}
		catch (Exception e) { 
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); 
		}		
	}

    /**
     * Calculate total out of reach
     *
     * @param project
     * @return
     * @throws Exception
     */
    private Double calculateTotalOR(Project project) throws Exception {

        Double totalTO = 0.0;

        ChangeControlSearch search = new ChangeControlSearch(project.getIdProject(), true);
        ChangeControlLogic changeControlLogic = new ChangeControlLogic();
        List<Changecontrol> changeControls = changeControlLogic.find(search);
        if (ValidateUtil.isNotNull(changeControls)) {
            for (Changecontrol changeControl : changeControls) {

                if (ValidateUtil.isNotNull(changeControl.getChangerequestwbsnodes())) {
                    for (Changerequestwbsnode changerequestwbsnode : changeControl.getChangerequestwbsnodes()) {

                        if (changerequestwbsnode.getEstimatedCost() != null) {
                            totalTO += changerequestwbsnode.getEstimatedCost();
                        }
                    }
                }
            }
        }

        return totalTO;
    }

    /**
	 * Export Followup information
	 * 
	 * @param file
	 * @param project
	 * @throws Exception
	 */
	private void executeExportFollowup(CsvFile file, Project project) throws Exception {
		
		ProjectFollowupLogic followupLogic = new ProjectFollowupLogic();
		
		Calendar since = DateUtil.getCalendar();
		Calendar until = DateUtil.getCalendar();
		since.setTime(new Date());
		
		if (since.get(Calendar.DAY_OF_MONTH) <= 7){ since.add(Calendar.MONTH, -1); }
		since.set(Calendar.DAY_OF_MONTH, 1);
		
		Projectfollowup followup = followupLogic.findLast(project);
		until.setTime(followup.getFollowupDate());
		
		double probability = new Double(project.getProbability()) / 100;
		
		int month		= 0;
		double lastPv	= 0;
		
		boolean findFollowup = false;
		
		while (!since.after(until)) {
			
			followup = followupLogic.findLasInMonth(project, since.getTime());
			
			if (followup == null && !findFollowup) { file.addValue(ValidateUtil.toCurrency(0)); }
			else if (followup == null && findFollowup) { month++; }
			else if (followup != null && !findFollowup && (followup.getPv() == null || followup.getPv() == 0)) {
				findFollowup = true;
				month++;
			}
			else if (month > 0) {
				
				findFollowup = true;
				
				double calc = ((followup.getPv() - lastPv) * probability) / ++month;
				
				for (int i = 0; i < month; i++) {
					file.addValue(ValidateUtil.toCurrency(calc));
				}
				month	= 0;
				lastPv	= followup.getPv();
			}
			else {
				
				findFollowup = true;
				double calc = (followup.getPv() - lastPv) * probability;
				file.addValue(ValidateUtil.toCurrency(calc));
				lastPv	= followup.getPv();
			}
			
			since.add(Calendar.MONTH, 1);
		}
	}
}

