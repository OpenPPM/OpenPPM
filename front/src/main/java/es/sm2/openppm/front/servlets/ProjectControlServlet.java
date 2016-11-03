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
 * File: ProjectControlServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.charts.ChartJQPLOT;
import es.sm2.openppm.core.charts.ChartWBS;
import es.sm2.openppm.core.charts.PlChartInfo;
import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.exceptions.CostTypeException;
import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.javabean.ResourceCapacityRunning;
import es.sm2.openppm.core.logic.charter.ChangeRequestTemplate;
import es.sm2.openppm.core.logic.impl.ActivitysellerLogic;
import es.sm2.openppm.core.logic.impl.ChangeControlLogic;
import es.sm2.openppm.core.logic.impl.ChangeTypesLogic;
import es.sm2.openppm.core.logic.impl.ChangerequestwbsnodeLogic;
import es.sm2.openppm.core.logic.impl.ChartLogic;
import es.sm2.openppm.core.logic.impl.ChecklistLogic;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.DirectCostsLogic;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.logic.impl.ExecutivereportLogic;
import es.sm2.openppm.core.logic.impl.ExpensesLogic;
import es.sm2.openppm.core.logic.impl.HistorickpiLogic;
import es.sm2.openppm.core.logic.impl.IncomesLogic;
import es.sm2.openppm.core.logic.impl.IwosLogic;
import es.sm2.openppm.core.logic.impl.JobcategoryLogic;
import es.sm2.openppm.core.logic.impl.MilestoneLogic;
import es.sm2.openppm.core.logic.impl.MilestonecategoryLogic;
import es.sm2.openppm.core.logic.impl.MilestonetypeLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProjectActivityLogic;
import es.sm2.openppm.core.logic.impl.ProjectCostsLogic;
import es.sm2.openppm.core.logic.impl.ProjectFollowupLogic;
import es.sm2.openppm.core.logic.impl.ProjectKpiLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.ResourceCapacityRunningLogic;
import es.sm2.openppm.core.logic.impl.ResourcepoolLogic;
import es.sm2.openppm.core.logic.impl.RiskRegisterLogic;
import es.sm2.openppm.core.logic.impl.SellerLogic;
import es.sm2.openppm.core.logic.impl.SkillLogic;
import es.sm2.openppm.core.logic.impl.TeamMemberLogic;
import es.sm2.openppm.core.logic.impl.TimelineLogic;
import es.sm2.openppm.core.logic.impl.TimesheetLogic;
import es.sm2.openppm.core.logic.impl.WBSNodeLogic;
import es.sm2.openppm.core.model.enums.ImportanceEnum;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Changerequestwbsnode;
import es.sm2.openppm.core.model.impl.Changetype;
import es.sm2.openppm.core.model.impl.Checklist;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Directcosts;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Executivereport;
import es.sm2.openppm.core.model.impl.Expenses;
import es.sm2.openppm.core.model.impl.Historickpi;
import es.sm2.openppm.core.model.impl.Incomes;
import es.sm2.openppm.core.model.impl.Iwo;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Milestonecategory;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectcosts;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Projectkpi;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Skill;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Timeline;
import es.sm2.openppm.core.model.impl.TimelineType;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.reports.GenerateReportInterface;
import es.sm2.openppm.core.reports.annotations.ReportType;
import es.sm2.openppm.core.reports.beans.ReportFile;
import es.sm2.openppm.core.reports.beans.ReportParams;
import es.sm2.openppm.core.utils.JSONModelUtil;
import es.sm2.openppm.front.utils.BeanUtil;
import es.sm2.openppm.front.utils.ChartUtil;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.OpenppmUtil;
import es.sm2.openppm.front.utils.RequestUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.Info;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringPool.InfoType;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.CsvFile;
import es.sm2.openppm.utils.json.Exclusion;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Servlet implementation class ProjectServer
 */
public class ProjectControlServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger LOGGER = Logger.getLogger(ProjectControlServlet.class);
	private static final SimpleDateFormat dfYear = new SimpleDateFormat("yy");
	
	public final static String REFERENCE = "projectcontrol";
	
	/***************** Actions ****************/
	public final static String SAVE_ACTIVITY_CONTROL	= "save-activity";
	public final static String SAVE_SCOPE_ACTIVITY_CONTROL	= "save-scope-activity";
	public final static String SAVE_REQUEST_CHANGE 		= "save-request-change";
	public final static String EXPORT_EVM_CSV			= "export-evm-table";
	public final static String EXPORT_KPI_CSV			= "export-kpi-table";
	public final static String EXPORT_SR_CSV			= "export-sr-table";
	public final static String EXPORT_CHANGE			= "export-change";	
	public final static String UPDATE_FOLLOWUP			= "update-followup";
	public final static String NEW_FOLLOWUP				= "new-followup";
	
	
	/************** Actions AJAX*************/
	public final static String JX_UPDATE_STAFFING_TABLE		= "ajax-update-staffing-table";
	public final static String JX_SAVE_FOLLOWUP 			= "ajax-save-followup";
	public final static String JX_SAVE_PROJECT 				= "ajax-save-project";
	public final static String JX_SAVE_MILESTONE			= "ajax-save-milestone";
	public final static String JX_CONS_CHANGE				= "ajax-cons-change";
	public final static String JX_CONS_FOLLOWUP				= "ajax-cons-followup";
	public final static String JX_COST_CONTROL_CHART		= "ajax-update-chart-control-costs";
	public final static String JX_WBS_CHART					= "ajax-generate-chart-wbs";
	public final static String JX_CONTROL_GANTT_CHART		= "ajax-update-chart-Schedule-Control-Gantt";
	public final static String JX_PL_CHART					= "ajax-update-chart-pl";
	public final static String JX_SAVE_COST 				= "ajax-save-cost";
	public final static String JX_SAVE_IWO					= "ajax-save-iwo";
	public final static String JX_SAVE_INCOME				= "ajax-save-income";
	public final static String JX_UPDATE_CHECKLIST 			= "ajax-update-checklist";
	public final static String JX_UPDATE_STATUS_DATE		= "ajax-change-status-date";
	public final static String JX_UPDATE_KPI				= "ajax-update-kpi";
	public final static String JX_HISTOGRAM_CHART			= "ajax-histogram-chart";
	public final static String JX_FINANCE_CHART 			= "ajax-finance-chart";
	public final static String JX_EXIST_FOLLOWUP			= "ajax-exist-followup";
	public final static String JX_HISTORIC_KPI				= "ajax-historic-kpi";
	public final static String JX_KPI_CHART					= "ajax-update-chart-kpis";
	public final static String JX_CONS_SELLER				= "ajax-consult-seller";
	public final static String JX_CONS_ACTIVITY_TIMESHEET	= "ajax-consult-activity-timesheet";
	public final static String JX_CALCULATE_ACTIVITY_AC     = "CALCULATE-ACTIVITY-AC";
	public final static String JX_UPDATE_AC					= "ajax-update-ac";
	public final static String JX_DELETE_MILESTONE 		= "ajax-delete-milestone";
	//TIMELINE
	public final static String JX_SAVE_TIMELINE				= "ajax-save-timeline";
	public final static String JX_DELETE_TIMELINE			= "ajax-delete-timeline";
	//EXECUTIVE REPORT
	public final static String JX_SAVE_EXECUTIVEREPORT		= "ajax-save-executivereport";
	// CHANGE REQUEST
	public final static String JX_SAVE_REQUEST_CHANGE			= "ajax-save-request-change";
	public final static String JX_DELETE_REQUEST_CHANGE			= "ajax-delete-request-change";
	public final static String JX_CONSULT_CHANGEREQUEST_WBSNODE	= "ajax-consult-changerequest-wbsnode";
	public final static String JX_SAVE_CHANGEREQUEST_WBSNODE	= "ajax-save-changerequest-wbsnode";
	public final static String JX_DELETE_CHANGEREQUEST_WBSNODE	= "ajax-delete-changerequest-wbsnode";
    
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @SuppressWarnings("rawtypes")
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");

		if (SecurityUtil.consUserRole(req) != -1) {
			if(ServletFileUpload.isMultipartContent(req) && StringPool.BLANK.equals(accion)) {

                accion = getMultipartField("accion");
			}
			
			LOGGER.debug("Accion: " + accion);
			
			/***************** Actions ****************/
			if (accion == null || StringPool.BLANK.equals(accion))  { viewControlProject(null, req, resp); }
			else if (SAVE_SCOPE_ACTIVITY_CONTROL.equals(accion)) { saveScopeActivity(req, resp); }
			else if (SAVE_ACTIVITY_CONTROL.equals(accion)) { saveActivity(req, resp); }
			else if (UPDATE_FOLLOWUP.equals(accion)) { updateFollowup(req, resp); }
			else if (NEW_FOLLOWUP.equals(accion)) { newFollowup(req, resp); }
			else if (SAVE_REQUEST_CHANGE.equals(accion)) { saveRequestChange(req, resp); }
			else if (EXPORT_EVM_CSV.equals(accion)) { exportEvmCsv(req, resp); }
			else if (EXPORT_KPI_CSV.equals(accion)) { exportKpiCsv(req, resp); }
			else if (EXPORT_SR_CSV.equals(accion)) { exportSrCsv(req, resp); }	
			else if (EXPORT_CHANGE.equals(accion)) { exportChange(req, resp); }
			
			/************** Actions AJAX **************/
			else if (JX_UPDATE_STAFFING_TABLE.equals(accion)) { updateStaffingTableJX(req, resp); }
			else if (JX_SAVE_MILESTONE.equals(accion)) { saveMilestoneJX(req, resp); }
			else if (JX_SAVE_INCOME.equals(accion)) { saveIncomeJX(req, resp); }
			else if (JX_SAVE_FOLLOWUP.equals(accion)) { saveFollowupJX(req, resp); }
			else if (JX_SAVE_COST.equals(accion)) { saveCostJX(req, resp); }
			else if (JX_SAVE_IWO.equals(accion)) { saveIwoJX(req, resp); }
			else if (JX_UPDATE_CHECKLIST.equals(accion)) { updateCheckListJX(req, resp); }
			else if (JX_COST_CONTROL_CHART.equals(accion)) { costChartsJX(req, resp); }
			else if (JX_WBS_CHART.equals(accion)) { wbsChartJX(req, resp); }
			else if (JX_UPDATE_STATUS_DATE.equals(accion)) { updateStatusDateJX(req, resp); }
			else if (JX_UPDATE_KPI.equals(accion)) { updateKpiJX(req, resp); }
			else if (JX_HISTOGRAM_CHART.equals(accion)) { histogramChartJX(req, resp); }
			else if (JX_CONS_CHANGE.equals(accion)) { consultRequestChangeJX(req, resp); }			
			else if (JX_CONS_FOLLOWUP.equals(accion)) { consultFollowupJX(req, resp); }			
			else if (JX_CONTROL_GANTT_CHART.equals(accion)) { controlGanttChartJX(req, resp); }			
			else if (JX_PL_CHART.equals(accion)) { plChartJX(req, resp); }
			else if (JX_FINANCE_CHART.equals(accion)) {	financeChartJX(req, resp); }
			else if (JX_EXIST_FOLLOWUP.equals(accion)) { existFollowupJX(req, resp); }
			else if (JX_HISTORIC_KPI.equals(accion)) { historicKpiJX(req, resp); }
			else if (JX_KPI_CHART.equals(accion)) { kpiChartJX(req, resp); }
			else if (JX_CONS_SELLER.equals(accion)) { consultSellerJX(req, resp); }
			else if (JX_CONS_ACTIVITY_TIMESHEET.equals(accion)) { consultActivityTimesheetJX(req, resp); }
			else if (JX_CALCULATE_ACTIVITY_AC.equals(accion)) { calculateActivityACJX(req, resp); }
			else if (JX_UPDATE_AC.equals(accion)) { updateACJX(req, resp); }
			else if (JX_DELETE_MILESTONE.equals(accion)) { deleteMilestoneJX(req, resp); }
			// TIMELINE
			else if (JX_SAVE_TIMELINE.equals(accion)) { saveTimelineJX(req, resp); }
			else if (JX_DELETE_TIMELINE.equals(accion)) { deleteTimelineJX(req, resp); }
			// EXECUTIVE REPORT
			else if (JX_SAVE_EXECUTIVEREPORT.equals(accion)) { saveExecutivereportJX(req, resp); }
			// CHANGE REQUEST
			else if (JX_SAVE_REQUEST_CHANGE.equals(accion)) { saveRequestChangeJX(req, resp); }
			else if (JX_DELETE_REQUEST_CHANGE.equals(accion)) { deleteRequestChangeJX(req, resp); }
			else if (JX_CONSULT_CHANGEREQUEST_WBSNODE.equals(accion)) { consultChangeRequestWBSNodeJX(req, resp); }
			else if (JX_SAVE_CHANGEREQUEST_WBSNODE.equals(accion)) { saveChangeRequestWBSNodeJX(req, resp); }
			else if (JX_DELETE_CHANGEREQUEST_WBSNODE.equals(accion)) { deleteChangeRequestWBSNodeJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Delete change request
     * 
	 * @param req
	 * @param resp
     * @throws IOException 
	 */
	private void deleteRequestChangeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject infoJSON	= null;
			
			// Request
			Integer idChangeRequest = ParamUtil.getInteger(req, "id", -1);
			
			if (idChangeRequest != -1) {
				
				// Declare logic
				ChangeControlLogic changeControlLogic = new ChangeControlLogic();
				
				// Logics
				//
				changeControlLogic.delete(changeControlLogic.findById(idChangeRequest));
				
				infoJSON = infoDeleted(getResourceBundle(req), "change_request");
			}
			else {
				throw new Exception("No data");
			}

			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e, "idError"); }
		finally { out.close(); }	   	
	}

	/**
     * Delete milestone
     * 
	 * @param req
	 * @param resp
     * @throws IOException 
	 */
	private void deleteMilestoneJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer idMilestone = ParamUtil.getInteger(req, "milestone_id", -1);
		PrintWriter out = resp.getWriter();
		
		try {
			MilestoneLogic milestoneLogic = new MilestoneLogic();
			
			milestoneLogic.delete(milestoneLogic.findById(idMilestone));
			
			out.print(infoDeleted(getResourceBundle(req), "milestone"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Delete change request wbs node
     * 
	 * @param req
	 * @param resp
     * @throws IOException 
	 */
	private void deleteChangeRequestWBSNodeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idChangeRequestWbsnode = ParamUtil.getInteger(req, Changerequestwbsnode.IDCHANGEREQUESTWBSNODE, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject infoJSON	= null;
			
			if (idChangeRequestWbsnode != -1){
				
				// Declare logic
				ChangerequestwbsnodeLogic changerequestwbsnodeLogic = new ChangerequestwbsnodeLogic();
				
				Changerequestwbsnode changerequestwbsnode = new Changerequestwbsnode();
				
				// Logic
				changerequestwbsnode = changerequestwbsnodeLogic.findById(idChangeRequestWbsnode);
				
				changerequestwbsnodeLogic.delete(changerequestwbsnode);
				
				infoJSON = infoDeleted(getResourceBundle(req), "wbs.ca_long");
			}
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
		
	}

	/**
     * Save change request wbs node
     * 
	 * @param req
	 * @param resp
     * @throws IOException 
	 */
	private void saveChangeRequestWBSNodeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		int idChangeRequestWbsnode	= ParamUtil.getInteger(req, Changerequestwbsnode.IDCHANGEREQUESTWBSNODE, -1);
		int idWbsnode				= ParamUtil.getInteger(req, Wbsnode.IDWBSNODE, -1);
		int idChange				= ParamUtil.getInteger(req, Changecontrol.IDCHANGE, -1);
		Double estimatedEffort 		= ParamUtil.getDouble(req, Changerequestwbsnode.ESTIMATEDEFFORT, null);
		Double estimatedCost 		= ParamUtil.getDouble(req, Changerequestwbsnode.ESTIMATEDCOST, null);

		PrintWriter out	= resp.getWriter();
		
		try {
			
			JSONObject infoJSON	= new JSONObject();
			
			String error = null;
			
			if (idWbsnode != -1 && idChange != -1 && estimatedEffort != null && estimatedCost != null) {
				
				Changerequestwbsnode changerequestwbsnode = null;
				
				// Declare logic
				ChangerequestwbsnodeLogic changerequestwbsnodeLogic = new ChangerequestwbsnodeLogic();
				WBSNodeLogic wbsNodeLogic 							= new WBSNodeLogic();
				
				// Control error duplicate CA
				List<Changerequestwbsnode> changerequestswbsnode = changerequestwbsnodeLogic.findByChangeControlAndWBSNode(new Changecontrol(idChange), new Wbsnode(idWbsnode));
				
				if (changerequestswbsnode.size() > 0 && changerequestswbsnode.get(0).getIdChangeRequestWbsnode() != idChangeRequestWbsnode) {
					error = getResourceBundle(req).getString("msg.error_duplicate_ca");
				}
				
				if (ValidateUtil.isNull(error)) {
					
					// Create entity
					if (idChangeRequestWbsnode == -1) {
						
						changerequestwbsnode = new Changerequestwbsnode();
						
						changerequestwbsnode.setChangecontrol(new Changecontrol(idChange));
						
						infoJSON = infoCreated(getResourceBundle(req), infoJSON, "wbs.ca_long");
					}
					else {
						// Find for update entity
						changerequestwbsnode = changerequestwbsnodeLogic.findById(idChangeRequestWbsnode, false);
						
						// Response
						infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "wbs.ca_long");
					}
					
					// Set parameters
					//
					Wbsnode wbsnode = wbsNodeLogic.findById(idWbsnode);
					
					changerequestwbsnode.setWbsnode(wbsnode);
					changerequestwbsnode.setEstimatedEffort(estimatedEffort);
					changerequestwbsnode.setEstimatedCost(estimatedCost);
					
					// Save
					changerequestwbsnode = changerequestwbsnodeLogic.save(changerequestwbsnode);
					
					// Response
					infoJSON.put(Changerequestwbsnode.IDCHANGEREQUESTWBSNODE, changerequestwbsnode.getIdChangeRequestWbsnode());
				}
			}
			else {
				error = getResourceBundle(req).getString("data_not_found");
			}
			
			if (ValidateUtil.isNotNull(error)) {
				infoJSON.put(InfoType.ERROR.getName(), error);
				infoJSON.put("idError", "changeRequestWBSNodeAjaxError");
			}
		
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
		
	}

	/**
     * Consult changeRequestWBSNode
     * 
	 * @param req
	 * @param resp
     * @throws IOException 
	 */
	private void consultChangeRequestWBSNodeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		int idChange = ParamUtil.getInteger(req, "idChange");
		
		// Declare writer for send response by AJAX
		PrintWriter out = resp.getWriter();
		
		
		try {
			
			JSONArray changesReqWBSJSON = new JSONArray();
			
			// Declare logic
			ChangerequestwbsnodeLogic changerequestwbsnodeLogic = new ChangerequestwbsnodeLogic();
			
			// Logic
			List<Changerequestwbsnode> changesRequestWBS = changerequestwbsnodeLogic.findByChangeControl(new Changecontrol(idChange));
			
			// Parse to JSON
			if (ValidateUtil.isNotNull(changesRequestWBS)) {
				
				for (Changerequestwbsnode changeReqWBS : changesRequestWBS) {
					
					JSONObject changeReqWBSJSON = new JSONObject();
					
					changeReqWBSJSON.put(Changerequestwbsnode.IDCHANGEREQUESTWBSNODE, changeReqWBS.getIdChangeRequestWbsnode());
					changeReqWBSJSON.put(Wbsnode.IDWBSNODE, changeReqWBS.getWbsnode().getIdWbsnode());
					changeReqWBSJSON.put("wbsNodeName", changeReqWBS.getWbsnode().getName());
					changeReqWBSJSON.put(Changerequestwbsnode.ESTIMATEDEFFORT, ValidateUtil.toCurrency(changeReqWBS.getEstimatedEffort()));
					changeReqWBSJSON.put(Changerequestwbsnode.ESTIMATEDCOST, ValidateUtil.toCurrency(changeReqWBS.getEstimatedCost()));
					
					changesReqWBSJSON.add(changeReqWBSJSON);
				}
			}
			
			// Create response
			JSONObject updateJSON = new JSONObject();
			updateJSON.put("changesReqWBS", changesReqWBSJSON);
			
			// Send data to client
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Update AC
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void updateACJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
    	// Declare writer for send response by AJAX
		PrintWriter out = resp.getWriter();
		
		try {
			
			// Find parameters
			int idProject 	= ParamUtil.getInteger(req, "id");
			Date statusDate	= ParamUtil.getDate(req, "statusDate", getDateFormat(req));
			
			// Declare logic
			TimesheetLogic timesheetLogic = new TimesheetLogic();
			
			// Find setting for calculate EVM
			String calculateEVMSetting = SettingUtil.getString(getSettings(req), Settings.SETTING_CALCULATE_EVM, Settings.SETTING_CALCULATE_EVM_DEFAULT);
			
			// Calculate AC of Project
			double acApp3 = timesheetLogic.calcProjectAC(new Project(idProject), statusDate, calculateEVMSetting);
			
			// Create response
			JSONObject updateJSON = new JSONObject();
			updateJSON.put("sumTimeSheet", ValidateUtil.toCurrency(acApp3));
			
			// Send data to client
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Timesheet for activity
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void consultActivityTimesheetJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
    	Integer idActivity = ParamUtil.getInteger(req, Projectactivity.IDACTIVITY);

		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject updateJSON 			= new JSONObject();
			TimesheetLogic timesheetLogic 	= new TimesheetLogic();
			
			double	hoursapp3 		= 0.0;
			double	hoursapp2 		= 0.0;
			double	hoursapp1 		= 0.0;
			double	acApp3 			= 0.0;
			double	acApp2 			= 0.0;
			double	acApp1 			= 0.0;
			
			hoursapp1 	= timesheetLogic.calcHoursActivityForStatus(idActivity, Constants.TIMESTATUS_APP1);
			hoursapp2 	= timesheetLogic.calcHoursActivityForStatus(idActivity, Constants.TIMESTATUS_APP2);
			hoursapp3 	= timesheetLogic.calcHoursActivityForStatus(idActivity, Constants.TIMESTATUS_APP3);

			acApp1 		= timesheetLogic.calcACforActivityAndStatus(idActivity, Constants.TIMESTATUS_APP1, null, false);
			acApp2 		= timesheetLogic.calcACforActivityAndStatus(idActivity, Constants.TIMESTATUS_APP2, null, false);
			acApp3 		= timesheetLogic.calcACforActivityAndStatus(idActivity, Constants.TIMESTATUS_APP3, null, false);
			
			
			updateJSON.put("hoursapp1", ValidateUtil.toCurrency(hoursapp1));
			updateJSON.put("hoursapp2", ValidateUtil.toCurrency(hoursapp2));
			updateJSON.put("hoursapp3", ValidateUtil.toCurrency(hoursapp3));
			
			updateJSON.put("ACapp1", ValidateUtil.toCurrency(acApp1));
			updateJSON.put("ACapp2", ValidateUtil.toCurrency(acApp2));
			updateJSON.put("ACapp3", ValidateUtil.toCurrency(acApp3));
			
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}    	
	}

    /**
     * Calculate activity AC
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void calculateActivityACJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {

            // Get parameters form request
            Projectactivity projectactivity = BeanUtil.loadBean(req, Projectactivity.class, getDateFormat(req));

            // Calculate ac for activity
            TimesheetLogic timesheetLogic 	= new TimesheetLogic();
            double acApp3 		= timesheetLogic.calcACforActivityAndStatus(projectactivity.getIdActivity(), Constants.TIMESTATUS_APP3, null, false);

            // Send data
            JSONObject updateJSON 			= new JSONObject();
            updateJSON.put("ac", acApp3);

			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}


	/**
     * Consult seller by projectActivity
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void consultSellerJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
    	Integer idActivity = ParamUtil.getInteger(req, Projectactivity.IDACTIVITY);

		PrintWriter out = resp.getWriter();
		
		try {
			
			ActivitysellerLogic activitysellerLogic = new ActivitysellerLogic();
			List<String> joins 						= new ArrayList<String>();
			JSONObject updateJSON 					= new JSONObject();
			JSONArray sellersJSON					= new JSONArray();
			
    		joins.add(Activityseller.PROJECT);
    		joins.add(Projectactivity.ENTITY);
    		joins.add(Activityseller.SELLER);
    		
			List<Activityseller> activitysellers = activitysellerLogic.findSellerAssociatedProject(new Projectactivity(idActivity), joins);
			
			for (Activityseller activityseller : activitysellers) {
				
				JSONObject sellerJSON = new JSONObject();
				
				Double poc = activityseller.getProject().getPoc();
				
				sellerJSON.put(Seller.NAME, activityseller.getSeller().getName());
				sellerJSON.put(Project.PROJECTNAME, activityseller.getProject().getProjectName());
				sellerJSON.put(Projectactivity.POC, poc == null?0:poc*100);
				sellerJSON.put(Project.STATUSDATE, formatDate(req, activityseller.getProject().getStatusDate()));
				
				sellersJSON.add(sellerJSON);
			}
			
			updateJSON.put("sellers", sellersJSON);
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}    	
	}


	/**
     * Chart kpis
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void kpiChartJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
    	int idProject							= ParamUtil.getInteger(req, "id");
    	
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
    		Boolean insufficientDates			= true;
    		
    		HistorickpiLogic historickpiLogic 	= new HistorickpiLogic();
    		ProjectLogic projectLogic			= new ProjectLogic(getSettings(req), getResourceBundle(req));
    		
    		JSONArray kpisValuesJSON 			= new JSONArray();
    		JSONArray kpisNamesJSON 			= new JSONArray();
    		JSONArray kpisColorsJSON			= new JSONArray();
    		JSONObject updateJSON 				= new JSONObject();
    		
    		List<String> joins = new ArrayList<String>();
    		joins.add(Projectkpi.METRICKPI);
    		Project project = projectLogic.findById(idProject);
    		
    		ProjectKpiLogic projectKpiLogic = new ProjectKpiLogic();
    		List<Projectkpi> projectKpis = projectKpiLogic.findByRelation(Projectkpi.PROJECT, project, joins);
			
    		
    		//colors
    		for(String defaultColor : defaultColors){
    			kpisColorsJSON.add(defaultColor);
    		}
    		
			int varianceColors = projectKpis.size() >  defaultColors.length ? projectKpis.size() - defaultColors.length : 0;
			
			for(int i=0; i < varianceColors; i++){
				random.setSeed(new Date().getTime());
        		r = random.nextFloat();
        		g = random.nextFloat();
        		b = random.nextFloat();
        		Color randomColor = new Color(r, g, b);
        		String rgb = Integer.toHexString(randomColor.getRGB());
        		rgb = rgb.substring(2, rgb.length());
        		rgb = StringPool.POUND.concat(rgb);
        		
    			kpisColorsJSON.add(rgb);
			}
    		
			List<String> joinsHistoricKpi = new ArrayList<String>();
    		joinsHistoricKpi.add(Historickpi.EMPLOYEE);
    		joinsHistoricKpi.add(Historickpi.EMPLOYEE+"."+ Employee.CONTACT);
    		
    		for (Projectkpi kpi: projectKpis) {
    			JSONArray kpiJSON = new JSONArray();
    			
    			List<Historickpi> historic = historickpiLogic.findByRelation(Historickpi.PROJECTKPI, new Projectkpi(kpi.getIdProjectKpi()), Historickpi.ACTUALDATE, Constants.ASCENDENT, joinsHistoricKpi);
    			
    			if (!historic.isEmpty()) {
	    				
	    			String tempDateStr = tempDate.format(historic.get(0).getActualDate());
	    			
	    			for (Historickpi hKpi: historic) {
	    				JSONArray hKpiJSON = new JSONArray();
	    				
	    				hKpiJSON.add(date.format(hKpi.getActualDate()));
	    				hKpiJSON.add(hKpi.getValueKpi() == null ? 0 : hKpi.getValueKpi());
	    				
	    				if (!tempDateStr.equals(date.format(hKpi.getActualDate()))) {
	    					insufficientDates = false;
	    				}
	    				
	    				kpiJSON.add(hKpiJSON);
	    			}
    			}
    			kpisValuesJSON.add(kpiJSON);
    			
    			if (kpi.getMetrickpi() != null) {
    				kpisNamesJSON.add(kpi.getMetrickpi().getName());
    			}
    			else {
    				kpisNamesJSON.add(kpi.getSpecificKpi());
    			}
    			
    			
    			/* min and max dates*/
    			if (!insufficientDates) {
    				Date tempMinDate = historic.get(0).getActualDate();
    				Date tempMaxDate = historic.get(historic.size() - 1).getActualDate();
    				
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
			
    		updateJSON.put("kpisColors", kpisColorsJSON);
    		updateJSON.put("kpisNames", kpisNamesJSON);
    		updateJSON.put("kpisValues", kpisValuesJSON);
    		updateJSON.put("insufficientDates", insufficientDates);
    		
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Historic kpi
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void historicKpiJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	int idKpi	= ParamUtil.getInteger(req, Projectkpi.IDPROJECTKPI);
		PrintWriter out = resp.getWriter();
	
		try {
			HistorickpiLogic historickpiLogic = new HistorickpiLogic();
			
			List<String> joins = new ArrayList<String>();
			joins.add(Historickpi.EMPLOYEE);
			joins.add(Historickpi.EMPLOYEE+"."+Employee.CONTACT);

			List<Historickpi> historic = historickpiLogic.findByRelation(Historickpi.PROJECTKPI, new Projectkpi(idKpi),joins);

            // Override updatedType only for show
            for (Historickpi historickpi : historic) {

                if (historickpi.getEnumUpdatedType() != null) {

                    String updatedType = historickpi.getEnumUpdatedType().getLabel();

                    historickpi.setUpdatedType(getResourceBundle(req).getString(updatedType));
                }
            }

			out.print(JsonUtil.toJSON(historic, "dd/MM/yyyy HH:mm:ss",
                    // Exclude from historickpi
					new Exclusion(Projectkpi.class),
                    // Exclude from employee
                    new Exclusion(Calendarbase.class),
                    new Exclusion(Seller.class),
                    new Exclusion(Performingorg.class),
                    new Exclusion(Resourcepool.class),
                    new Exclusion(Resourceprofiles.class),
                    // Exclude from contact
                    new Exclusion(Company.class)));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * look if there is a followup
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
	private void existFollowupJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idProject	= ParamUtil.getInteger(req, "id");
		Date date		= ParamUtil.getDate(req, "date", getDateFormat(req));
		PrintWriter out = resp.getWriter();
	
		try {
			JSONObject updateJSON 						= new JSONObject();
			ProjectFollowupLogic projectFollowupLogic 	= new ProjectFollowupLogic();
			
			Projectfollowup followup = projectFollowupLogic.findByDate(null, new Project(idProject), Projectfollowup.FOLLOWUPDATE, date);
			
			if (followup != null) {
				throw new LogicException("msg.error.followup_repeat");
			}
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Update Staffing Table
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void updateStaffingTableJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
    	int idProject			= ParamUtil.getInteger(req, Project.IDPROJECT);
    	Date since				= ParamUtil.getDate(req, "since", getDateFormat(req));
    	Date until				= ParamUtil.getDate(req, "until", getDateFormat(req));
    	Boolean showOperations 	= ParamUtil.getBoolean(req, Configurations.SHOW_OPERATIONS, Boolean.TRUE);
    	
		ResourceCapacityRunning resourceCapacityRunning = null;
		
    	try {
    		// Find configurations
    		HashMap<String, String> configurations = RequestUtil.getConfigurationValues(
    				req,
    				Configurations.SHOW_OPERATIONS
    			);

    		// Save configuration
    		ConfigurationLogic configurationLogic = new ConfigurationLogic();
    		configurationLogic.saveConfigurations(getUser(req), configurations, Configurations.TYPE_CAPACITY_RUNNING);
    		
    		// Declare logic
    		ResourceCapacityRunningLogic resourceCapacityRunningLogic = new ResourceCapacityRunningLogic();
    		
    		// Set project
    		Integer[] idProjects = new Integer[1];
    		idProjects[0] = idProject;
    		
    		// Logic
    		resourceCapacityRunning = resourceCapacityRunningLogic.updateCapacityRunning(
    				idProjects,
    				null,
    				null,
    				null,
    				null,
    				null,
    				null,
    				since,
    				until,
    				Constants.ASCENDENT,
    				getResourceBundle(req),
    				getUser(req),
    				showOperations
    				);
    	}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

    	if (resourceCapacityRunning != null && ValidateUtil.isNotNull(resourceCapacityRunning.getFtEs())) {
    		// Response
    		req.setAttribute("listDates", resourceCapacityRunning.getListDates());
    		req.setAttribute("ftEs", resourceCapacityRunning.getFtEs());
    	}
    	
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		
		forward("/project/common/staffing_table.ajax.jsp", req, resp);
	}
    
	/**
     * Generate Histogram Chart
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void histogramChartJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

    	int idProject	= ParamUtil.getInteger(req, Project.IDPROJECT);
    	Date since		= ParamUtil.getDate(req, "since", getDateFormat(req));
    	Date until		= ParamUtil.getDate(req, "until", getDateFormat(req));

    	JSONArray dataSeries = new JSONArray();
    	JSONArray categories = new JSONArray();
    	JSONArray legend	 = new JSONArray();

    	PrintWriter out = resp.getWriter();

    	try {

    		TimesheetLogic timesheetLogic	= new TimesheetLogic();
    		TeamMemberLogic memberLogic		= new TeamMemberLogic();
    		JobcategoryLogic jobLogic		= new JobcategoryLogic();

			Calendar sinceCal = DateUtil.getCalendar();
			sinceCal.setTime(DateUtil.getFirstWeekDay(since));

			Calendar untilCal = DateUtil.getCalendar();
			untilCal.setTime(DateUtil.getLastWeekDay(until));

			while (!sinceCal.after(untilCal)) {

				int sinceDay = sinceCal.get(Calendar.DAY_OF_MONTH);
				Calendar calWeek = DateUtil.getLastWeekDay(sinceCal);
				int untilDay = calWeek.get(Calendar.DAY_OF_MONTH);

				categories.add(sinceDay+"-"+untilDay+" "+getResourceBundle(req).getString("month.min_"
						+ (calWeek.get(Calendar.MONTH) +1)) +
						" " + dfYear.format(calWeek.getTime()));

				sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
			}

			List<Teammember> members	= memberLogic.consStaffinActualsFtes(new Project(idProject), since, until);
			
			List<Jobcategory> jobs		= jobLogic.findByRelation(Jobcategory.COMPANY, getCompany(req), Jobcategory.NAME, Constants.ASCENDENT);

			int i = 0;
			for (Jobcategory job : jobs) {

				sinceCal.setTime(DateUtil.getFirstWeekDay(since));
				
				JSONArray series = new JSONArray();
				
				while (!sinceCal.after(untilCal)) {

					double resources = 0;

					for (Teammember member : members) {
						
						if (member.getJobcategory() != null &&
								job.getIdJobCategory().equals(member.getJobcategory().getIdJobCategory())
								&& DateUtil.betweenWeek(member.getDateIn(), member.getDateOut(), sinceCal.getTime())) {

							Double fte = timesheetLogic.getFte(
									new Project(idProject), member,
									DateUtil.getFirstWeekDay(sinceCal.getTime()),
									DateUtil.getLastWeekDay(sinceCal.getTime()));

							resources += fte;
						}
					}
					series.add((resources>0?resources/100:0));

					sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
				}
				
				// Paint if values exist
				Boolean existValues = false;
				int index = 0;
				
				while (index < series.size() && !existValues) {
					
					if (series.getDouble(index) != 0.0) {
						existValues = true;
					}
					
					index += 1;
				}
				
				if (existValues) {
					
					legend.add(ChartUtil.getLegend(job.getName(), ++i));
					
					dataSeries.add(series);
				}
			}

			legend.add(ChartUtil.getLegend(getResourceBundle(req).getString("not_defined"), 0));
			
			sinceCal.setTime(DateUtil.getFirstWeekDay(since));
			
			JSONArray series = new JSONArray();
			
			while (!sinceCal.after(untilCal)) {

				double resources	= 0;
				
				for (Teammember member : members) {
					
					if (member.getJobcategory() == null &&
							DateUtil.betweenWeek(member.getDateIn(), member.getDateOut(),
							sinceCal.getTime())) {

						Double fte = timesheetLogic.getFte(
								new Project(idProject), member,
								DateUtil.getFirstWeekDay(sinceCal.getTime()),
								DateUtil.getLastWeekDay(sinceCal.getTime()));

						resources += fte;
					}

				}
				series.add((resources>0?resources/100:0));

				sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
			}

			dataSeries.add(series);

			JSONObject chartJSON = new JSONObject();

			chartJSON.put("categories", categories);
			chartJSON.put("dataSeries", dataSeries);
			chartJSON.put("legend", legend);

			out.print(chartJSON);
    	}
    	catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req,getResourceBundle(req), LOGGER, e); }
    	finally { out.close(); }
	}
    
    /**
     * Save Requested change
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveRequestChangeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
    	Changecontrol change = null;
		
		PrintWriter out = resp.getWriter();
		
		try {
			// Declare logic
			ChangeControlLogic changeControlLogic = new ChangeControlLogic();
			
			// Set request
			change = setChangeFromRequest(req);
			
			// Logics
			//
			changeControlLogic.save(change);
			
			change = changeControlLogic.consChangeControl(change);
			
			// Response
			out.print(JSONModelUtil.changeControlToJSON(getResourceBundle(req), change));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}    	
    }
    
    /**
     * Consult Requested change
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void consultRequestChangeJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
    	Integer idChange = ParamUtil.getInteger(req, "change_id", -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			ChangeControlLogic changeControlLogic = new ChangeControlLogic();
			Changecontrol change = changeControlLogic.consChangeControl(new Changecontrol(idChange));

			out.print(JSONModelUtil.changeControlToJSON(getResourceBundle(req), change));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}    	
    }
    
    /**
     * Consult Project Followup
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void consultFollowupJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
    	Integer idFollowup 	= ParamUtil.getInteger(req, "followup_id", -1);
    	Double globalBudget = ParamUtil.getDouble(req, "globalBudget");
		
		PrintWriter out = resp.getWriter();
		
		try {
			ProjectFollowupLogic projectFollowupLogic = new ProjectFollowupLogic();

			Projectfollowup followup = projectFollowupLogic.consFollowupWithProject(new Projectfollowup(idFollowup));
			
			// Set settings and globalBudget for POC and EAC
			followup.setSettings(getSettings(req));
			followup.setBudget(globalBudget);
			
			out.print(JSONModelUtil.followupToJSON(getResourceBundle(req), followup, null));
			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}    	
    }

    /**
     * Update chart Gantt Schedule-Control
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void controlGanttChartJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
    	PrintWriter out = resp.getWriter();
		
		Integer idProject		= ParamUtil.getInteger(req, "id");
		Date since				= ParamUtil.getDate(req, "filter_start", getDateFormat(req),null);
		Date until				= ParamUtil.getDate(req, "filter_finish", getDateFormat(req),null);
		String idsBySorting 	= ParamUtil.getString(req, "idsBySorting");
		
		try {	
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			JSONObject updateJSON = ChartLogic.consChartGantt(getResourceBundle(req), projectLogic.consProject(idProject), true, true, 
					since, until, idsBySorting, getSettings(req));
			
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }    	
    }    
    
    
    /**
     * Update project-costs chart P&L
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void plChartJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
    	
    	PrintWriter out = resp.getWriter();

		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			Integer idProject = ParamUtil.getInteger(req, "id");

			Project project = projectLogic.consProject(idProject);

			PlChartInfo plChartInfo = ChartLogic.consActualInfoPlChart(project);
			double sumIwos			= plChartInfo.getSumIwos();
			double sumExpenses		= plChartInfo.getSumExpenses();
			double sumDirectCost	= plChartInfo.getSumDirectCost();
			double dm				= plChartInfo.getDirectMargin();
			double dmPer			= Math.round(plChartInfo.getDirectMarginPercent());
			double tcv				= plChartInfo.getTcv();
			double netIncome		= plChartInfo.getCalculatedNetIncome();

			double divDouble = new Double(1000);

			JSONObject chartJSON = new JSONObject();
			if (!project.getNetIncome().equals(netIncome)) {
				chartJSON.put("warning", true);
			}

			chartJSON.put("s1",ChartUtil.createSeriesPL(
					Double.toString(tcv/divDouble),
					Double.toString(netIncome/divDouble),
					StringPool.BLANK,
					1));

			chartJSON.put("s2",ChartUtil.createSeriesPL(
					Double.toString((tcv-sumExpenses)/divDouble),
					Double.toString((tcv-sumExpenses-sumIwos)/divDouble),
					Double.toString((netIncome-sumDirectCost)/divDouble),
					2));

			chartJSON.put("s3",ChartUtil.createSeriesPL(		
					Double.toString(sumDirectCost/divDouble), StringPool.BLANK,StringPool.BLANK, 3));

			chartJSON.put("s4",ChartUtil.createSeriesPL(
					Double.toString(sumExpenses/divDouble), StringPool.BLANK,StringPool.BLANK, 4));

			chartJSON.put("s5",ChartUtil.createSeriesPL(
					Double.toString(sumIwos/divDouble), StringPool.BLANK,StringPool.BLANK, 5));

			chartJSON.put("s6",ChartUtil.createSeriesPL(
					Double.toString(dm/divDouble),
					StringPool.BLANK, StringPool.BLANK, 6));

			//JQPLOT
			final  List<String> colors = new ArrayList<String>();
			colors.add("#BDBDBD");
			colors.add("#FFFFFF");
			colors.add("#B40404");
			colors.add("#688BB4");
			colors.add("#FFFF00");
			colors.add("#7E7E7E");
			chartJSON.put("colors", colors);

			final  List<String> seriesParameters = new ArrayList<String>();
			seriesParameters.add("{}");
			seriesParameters.add("{show:false,shadow:false}");
			seriesParameters.add("{}");
			seriesParameters.add("{}");
			seriesParameters.add("{}");
			seriesParameters.add("{}");
			chartJSON.put("seriesParameters", seriesParameters);

			final  List<String> categories = new ArrayList<String>();
			categories.add(getResourceBundle(req).getString("tcv"));
			categories.add(getResourceBundle(req).getString("project_pl.expenses"));
			categories.add(getResourceBundle(req).getString("iwo"));
			categories.add(getResourceBundle(req).getString("project_pl.net_income"));
			categories.add(getResourceBundle(req).getString("project_pl.direct_costs"));
			categories.add(getResourceBundle(req).getString("project_pl.dm")+" "+dmPer+"%");
			chartJSON.put("categories", categories);

			out.print(chartJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e); }
		finally { out.close(); }
    }
    
    
	/**
     * Update Project Kpi
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void updateKpiJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		int idProjectKPI	= ParamUtil.getInteger(req, Projectkpi.IDPROJECTKPI, -1);
		double value		= ParamUtil.getDouble(req, Projectkpi.VALUE, 0.0);
    	
    	PrintWriter out = resp.getWriter();
    	
		try {
			JSONObject returnJSON = null;
			
			List<String> joins = new ArrayList<String>();
			joins.add(Projectkpi.PROJECT);
			
			// Declare logic
			ProjectKpiLogic projectKpiLogic = new ProjectKpiLogic();
			ProjectLogic projectLogic 		= new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			// Update kpi 
			//
			Projectkpi projKpi = projectKpiLogic.findById(idProjectKPI, joins);
			projKpi.setValue(value);
			
			projKpi = projectKpiLogic.saveProjectKpi(projKpi, getUser(req), Historickpi.UpdatedType.MANUAL);
			
			// Update kpi status for project
			Project project = projectLogic.updateKPIStatus(projKpi.getProject().getIdProject());
			
			// Return
			returnJSON = infoUpdated(getResourceBundle(req), returnJSON, "kpi");
			returnJSON.put("adjustedValue", projKpi.getAdjustedValue());
			returnJSON.put("score", projKpi.getScore());
			returnJSON.put("thereKPISEmpty", projectKpiLogic.thereKPISEmpty(projKpi.getProject()));
			returnJSON.put("kpiStatus", project.getKpiStatus());
			
			out.print(returnJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Create new followup
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void newFollowup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
    	int idProject		= ParamUtil.getInteger(req, Project.IDPROJECT);
		Date followupDate	= ParamUtil.getDate(req, Projectfollowup.FOLLOWUPDATE, getDateFormat(req));
		double pv 			= ParamUtil.getCurrency(req, Projectfollowup.PV);
		
		try {
			Calendar cal = DateUtil.getCalendar();
			cal.add(Calendar.DAY_OF_MONTH, 1);
			
			Projectfollowup followup 					= new Projectfollowup();
			ProjectFollowupLogic projectFollowupLogic 	= new ProjectFollowupLogic();
			RiskRegisterLogic riskRegisterLogic			= new RiskRegisterLogic();
			
			followup.setProject(new Project(idProject));
			followup.setFollowupDate(followupDate);
			followup.setPv(pv != 0?pv:null);
			
			// Calculate actual riskRating
			List<Riskregister> riskregisters = riskRegisterLogic.findByProjectAndOpen(new Project(idProject));
			
			int riskRating = 0; 
			for (Riskregister riskregister : riskregisters) {
				riskRating += riskregister.getRiskRating();
			}

			if (riskRating >= 0) {
				followup.setRiskRating(riskRating);
			}
			
			projectFollowupLogic.save(followup);
			
			infoCreated(req, "followup");

			req.setAttribute("idNewFollowup", followup.getIdProjectFollowup());
			
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewControlProject(idProject, req, resp);
	}
    
    /**
     * Save request change
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void saveRequestChange(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	Changecontrol change = null;
		
		try {
			
			ChangeControlLogic changeControlLogic = new ChangeControlLogic();
			change = setChangeFromRequest(req);
			changeControlLogic.save(change);
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		Integer idProject = ParamUtil.getInteger(req, "id");
		
		infoUpdated(req, "change_request");
		viewControlProject(idProject, req, resp);
    }
    
    /**
     * Export EVM to CSV
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void exportEvmCsv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	Integer idProject 	= ParamUtil.getInteger(req, "id");
    	Double globalBudget = ParamUtil.getDouble(req, "globalBudget");
		String fileName 	= "";
		CsvFile file 		= null;
		
		try {
			ProjectFollowupLogic projectFollowupLogic 	= new ProjectFollowupLogic();
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			Project project = projectLogic.consProject(idProject);
			
			fileName = project.getChartLabel();
			
			file = new CsvFile(Constants.SEPARATOR_CSV);
			List<Projectfollowup> followups = projectFollowupLogic.consFollowups(project);
			
			file.addValue(getResourceBundle(req).getString("followup"));
			file.addValue(getResourceBundle(req).getString("followup.days_date"));
			file.addValue(getResourceBundle(req).getString("followup.es"));
			file.addValue(getResourceBundle(req).getString("followup.pv"));
			file.addValue(getResourceBundle(req).getString("followup.ev"));
			file.addValue(getResourceBundle(req).getString("followup.ac"));
			file.addValue(getResourceBundle(req).getString("followup.complete"));
			file.addValue(getResourceBundle(req).getString("followup.cpi"));
			file.addValue(getResourceBundle(req).getString("followup.spi"));
			file.addValue(getResourceBundle(req).getString("followup.spit"));
			file.addValue(getResourceBundle(req).getString("followup.cv"));
			file.addValue(getResourceBundle(req).getString("followup.sv"));
			file.addValue(getResourceBundle(req).getString("followup.svt"));
			file.addValue(getResourceBundle(req).getString("followup.eac"));					
			file.newLine();
			for (Projectfollowup followup : followups) {
				
				// Set settings and globalBudget for POC and EAC
				followup.setSettings(getSettings(req));
				followup.setBudget(globalBudget);
				
				Integer daysToDate = followup.getDaysToDate();
				daysToDate = (daysToDate == null?0:daysToDate);
				
				file.addValue(getDateFormat(req).format(followup.getFollowupDate()));
				file.addValue((followup.getDaysToDate()== null ? StringPool.BLANK : getNumberFormat().format(followup.getDaysToDate())));					
				file.addValue((OpenppmUtil.getES(followups, followup)== null ? StringPool.BLANK : getNumberFormat().format(OpenppmUtil.getES(followups, followup))));
				file.addValue((followup.getPv()== null ? StringPool.BLANK : getNumberFormat().format(followup.getPv())));
				file.addValue((followup.getEv()== null ? StringPool.BLANK : getNumberFormat().format(followup.getEv())));
				file.addValue((followup.getAc()== null ? StringPool.BLANK : getNumberFormat().format(followup.getAc())));
				file.addValue((followup.getPoc()== null ? StringPool.BLANK : getNumberFormat().format(followup.getPoc())));
				file.addValue((followup.getCpi()== null ? StringPool.BLANK : getNumberFormat().format(followup.getCpi())));
				file.addValue((followup.getSpi()== null ? StringPool.BLANK : getNumberFormat().format(followup.getSpi())));
				file.addValue(
						OpenppmUtil.getES(followups, followup)== null || followup.getDaysToDate() == null
							? StringPool.BLANK :
								getNumberFormat().format(OpenppmUtil.getES(followups, followup)/daysToDate)
				);
				file.addValue((followup.getCv()== null ? StringPool.BLANK : getNumberFormat().format(followup.getCv())));						
				file.addValue((followup.getSv()== null ? StringPool.BLANK : getNumberFormat().format(followup.getSv())));						
				file.addValue((OpenppmUtil.getES(followups, followup)== null ? StringPool.BLANK : getNumberFormat().format(OpenppmUtil.getES(followups, followup) - daysToDate)));						
				file.addValue((followup.getEac()== null ? StringPool.BLANK : getNumberFormat().format(followup.getEac())));						
				file.newLine();						
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
        sendFile(req, resp, file.getFileBytes(), fileName + ".csv");    	
    }
    
    /**
     * Export KPI to CSV
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void exportKpiCsv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	Integer idProject = ParamUtil.getInteger(req, "id");
		String fileName = "";
		CsvFile file = null;
		
		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			Project project = projectLogic.consProject(idProject);
			
			fileName = project.getChartLabel();
			
			file = new CsvFile(Constants.SEPARATOR_CSV);
			
			List<String> joins = new ArrayList<String>();
			joins.add(Projectkpi.METRICKPI);
			
			ProjectKpiLogic kpiLogic = new ProjectKpiLogic();
			List<Projectkpi> kpis = kpiLogic.findByRelation(Projectkpi.PROJECT, project, joins);
			
			file.addValue(getResourceBundle(req).getString("kpi"));
			file.addValue(getResourceBundle(req).getString("kpi.metric"));
			file.addValue(getResourceBundle(req).getString("kpi.definition"));
			file.addValue(getResourceBundle(req).getString("kpi.upper_threshold") + "%");
			file.addValue(getResourceBundle(req).getString("kpi.lower_threshold") + "%");
            file.addValue(getResourceBundle(req).getString("kpi.value") + "%");
            file.addValue(getResourceBundle(req).getString("kpi.normalized_value") + "%");
            file.addValue(getResourceBundle(req).getString("kpi.weight") + "%");
            file.addValue(getResourceBundle(req).getString("kpi.score") + "%");

			file.newLine();

			for (Projectkpi kpi : kpis) {

                file.addValue(getResourceBundle(req).getString(kpi.getRAG()));
				file.addValue((kpi.getMetrickpi()== null ? StringPool.BLANK : kpi.getMetrickpi().getName()));
				file.addValue((kpi.getMetrickpi()== null ? StringPool.BLANK : kpi.getMetrickpi().getDefinition()));					
				file.addValue((kpi.getUpperThreshold()== null ? StringPool.BLANK : getNumberFormat().format(kpi.getUpperThreshold())));
				file.addValue((kpi.getLowerThreshold()== null ? StringPool.BLANK : getNumberFormat().format(kpi.getLowerThreshold())));
                file.addValue((kpi.getValue()== null ? StringPool.BLANK : getNumberFormat().format(kpi.getValue())));
                file.addValue(getNumberFormat().format(kpi.getAdjustedValue()));
                file.addValue((kpi.getWeight()== null ? StringPool.BLANK : getNumberFormat().format(kpi.getWeight())));
                file.addValue((kpi.getScore()== null ? StringPool.BLANK : getNumberFormat().format(kpi.getScore())));

				file.newLine();						
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
        sendFile(req, resp, file.getFileBytes(), fileName + ".csv");    	
    }
    
    /**
     * Export Status Report to CSV
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void exportSrCsv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	Integer idProject = ParamUtil.getInteger(req, "id");
		String fileName = "";
		CsvFile file = null;
		
		try {
			ProjectFollowupLogic projectFollowupLogic 	= new ProjectFollowupLogic();
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			Project project = projectLogic.consProject(idProject);
			
			fileName = project.getChartLabel();
			
			file = new CsvFile(Constants.SEPARATOR_CSV);
			List<Projectfollowup> followups = projectFollowupLogic.consFollowups(project);
			
			file.addValue(getResourceBundle(req).getString("date"));
			file.addValue(getResourceBundle(req).getString("status"));
			file.addValue(getResourceBundle(req).getString("type"));
			file.addValue(getResourceBundle(req).getString("desc"));												
			file.newLine();
			file.newLine();
			
			for (Projectfollowup followup : followups) {

				file.addValue(StringPool.BLANK);
				file.addValue((followup.getGeneralFlag()== null ? StringPool.BLANK : getResourceBundle(req).getString("followup.status_" + followup.getGeneralFlag().toString())));
				file.addValue(getResourceBundle(req).getString("general"));
				file.addValue((followup.getGeneralComments()== null ? StringPool.BLANK : followup.getGeneralComments().replaceAll("\\s", StringPool.SPACE)));
				file.newLine();
				
				file.addValue(followup.getFollowupDate() == null?StringPool.BLANK:getDateFormat(req).format(followup.getFollowupDate()));
				file.addValue((followup.getRiskFlag()== null ? StringPool.BLANK : getResourceBundle(req).getString("followup.status_" + followup.getRiskFlag().toString())));
				file.addValue(getResourceBundle(req).getString("risk"));
				file.addValue((followup.getRisksComments()== null ? StringPool.BLANK : followup.getRisksComments().replaceAll("\\s", StringPool.SPACE)));
				file.newLine();
				
				file.addValue(StringPool.BLANK);
				file.addValue((followup.getScheduleFlag()== null ? StringPool.BLANK : getResourceBundle(req).getString("followup.status_" + followup.getScheduleFlag().toString())));
				file.addValue(getResourceBundle(req).getString("schedule"));
				file.addValue((followup.getScheduleComments()== null ? StringPool.BLANK : followup.getScheduleComments().replaceAll("\\s", StringPool.SPACE)));
				file.newLine();
				
				file.addValue(StringPool.BLANK);
				file.addValue((followup.getCostFlag()== null ? StringPool.BLANK : getResourceBundle(req).getString("followup.status_" + followup.getCostFlag().toString())));
				file.addValue(getResourceBundle(req).getString("cost"));
				file.addValue((followup.getCostComments()== null ? StringPool.BLANK : followup.getCostComments().replaceAll("\\s", StringPool.SPACE)));
				file.newLine();
				file.newLine();							
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
        sendFile(req, resp, file.getFileBytes(), fileName + ".csv");    	
    }
    
    /**
     * Export control change to ODT
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void exportChange(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	Integer idChangeControl = ParamUtil.getInteger(req, "change_export_id", -1);
		Integer idProject		= getIdProject(req);

		try {

            GenerateReportInterface generateReport = CacheStatic.getGenerateReport(ReportType.CHANGE_REQUEST);

            if (generateReport != null) {

                // Call to generate report file
                ReportParams reportParams = new ReportParams(new Project(idProject), getResourceBundle(req), getSettings(req), getUser(req));
                reportParams.addParam(ReportParams.Type.CHANGE_ID, idChangeControl);

                ReportFile reportFile = generateReport.generate(reportParams);

                if (reportFile != null) {
                    // Send report
                    sendFile(req, resp, reportFile.getFile(), reportFile.getName()+ reportFile.getExtension());
                }
            }
            else {

                // TODO javier.hernandez - 21/03/2015 - deprecated code migrate to GenerateReportInterface
                ChangeRequestTemplate changeRequestTemplate = ChangeRequestTemplate.getChangeRequestTemplate(getResourceBundle(req), getUser(req));

                File changeRequestFile = changeRequestTemplate.generateChangeRequest(getResourceBundle(req), new Project(idProject), new Changecontrol(idChangeControl), getUser(req));

                byte[] requestDoc		= null;

                if (changeRequestFile != null) {
                    requestDoc = DocumentUtils.getBytesFromFile(changeRequestFile);

                    // delete temporal file
                    DocumentUtils.deleteFile(changeRequestFile);
                }

                sendFile(req, resp, requestDoc, getResourceBundle(req).getString("change_request") + "_" + idChangeControl + ".odt");

            }
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		

    }

	/**
     * Update Project Status Date
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void updateStatusDateJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
    	PrintWriter out = resp.getWriter();
		
		int idProject	= ParamUtil.getInteger(req, Project.IDPROJECT);
		Date statusDate	= ParamUtil.getDate(req, Project.STATUSDATE, getDateFormat(req), null);
		
		try {
			ProjectLogic projectLogic 	= new ProjectLogic(getSettings(req), getResourceBundle(req));
			JSONObject updateJSON 		= new JSONObject();
			
			if (statusDate == null) {
				
				statusDate = new Date();
				
				updateJSON = info(getResourceBundle(req), StringPool.INFORMATION, "project.status_date.is_empty",updateJSON, "project.status_date",getDatePattern(req), getDateFormat(req).format(statusDate));
			}
			else {
				updateJSON = infoUpdated(getResourceBundle(req), updateJSON, "project.status_date");
			}
			
			updateJSON.put(Project.STATUSDATE, getDateFormat(req).format(statusDate));
			
			Project proj = projectLogic.consProject(idProject);
			proj.setStatusDate(statusDate);
			projectLogic.save(proj);
			
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
     * Generate WBS Chart
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void wbsChartJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
    	PrintWriter out = resp.getWriter();
		
		Integer idProject 	= ParamUtil.getInteger(req, "id");
		Double globalBudget = ParamUtil.getDouble(req, "globalBudget", 0.0);
		
		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();
			
			Project project = projectLogic.consProject(idProject);
			
			Wbsnode wbsNodeParent = wbsNodeLogic.findFirstWBSnode(project);
			
			Double poc = project.getPoc();
			ChartWBS chartWBS = new ChartWBS(wbsNodeParent, true, poc == null?0:poc*100, globalBudget, getResourceBundle(req), getSettings(req));
			
			out.print(chartWBS.generate());
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
     * Update Checklist
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void updateCheckListJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
    	int idWbsnode		= ParamUtil.getInteger(req, Wbsnode.IDWBSNODE);
    	int idChecklist		= ParamUtil.getInteger(req, Checklist.IDCHECKLIST,-1);
    	String code			= ParamUtil.getString(req, Checklist.CODE);
    	String name			= ParamUtil.getString(req, Checklist.NAME);
    	String description	= ParamUtil.getString(req, Checklist.DESCRIPTION);
    	String comments		= ParamUtil.getString(req, Checklist.COMMENTS);
    	int percentage		= ParamUtil.getInteger(req, Checklist.PERCENTAGECOMPLETE,0);
    	Date actDate		= ParamUtil.getDate(req, Checklist.ACTUALIZATIONDATE,getDateFormat(req) , new Date());
    	
    	PrintWriter out = resp.getWriter();
		
		try {
			ChecklistLogic checklistLogic = new ChecklistLogic();
			Checklist checklist = null;
			
			if (idChecklist == -1) { checklist = new Checklist(); }
			else {
				ChecklistLogic checkLogic = new ChecklistLogic();
				checklist = checkLogic.findById(idChecklist); 
			}
			
			checklist.setCode(code);
			checklist.setName(name);
			checklist.setDescription(description);
			checklist.setComments(comments);
			checklist.setWbsnode(new Wbsnode(idWbsnode));
			checklist.setPercentageComplete(percentage);
			checklist.setActualizationDate(actDate);
			
			checklist = checklistLogic.save(checklist);
			
			JSONObject datJSON = JsonUtil.toJSON(Checklist.IDCHECKLIST, checklist.getIdChecklist());
			datJSON.put(Checklist.ACTUALIZATIONDATE, getDateFormat(req).format(actDate));
			if (idChecklist == -1) { 
				datJSON = infoCreated(getResourceBundle(req), datJSON, "checklist"); 
			}
			else {
				datJSON = infoUpdated(getResourceBundle(req), datJSON, "checklist");			 
			}
			
			out.print(datJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
     * Save IWO
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void saveIwoJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();
		
		try {
			IwosLogic iwosLogic = new IwosLogic();
			
			Integer idIwo 	= ParamUtil.getInteger(req, "iwo_id", -1);
			double actual	= ParamUtil.getCurrency(req, "iwo_actual", 0);
			String desc		= ParamUtil.getString(req, "iwo_desc", null);
			Date iwoDate	= ParamUtil.getDate(req, "iwo_date", getDateFormat(req), new Date());
			
			Iwo iwo = iwosLogic.consIwo(idIwo);
			iwo.setActual(actual);
			iwo.setDescription(desc);

			iwosLogic.saveIwo(iwo, iwoDate);
			
			JSONObject costJSON = new JSONObject();
			costJSON.put("actual", iwo.getActual());
			costJSON.put("desc", iwo.getDescription());
			costJSON.put("iwoDate", getDateFormat(req).format(iwoDate));
			
			out.print(costJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
     * Save cost
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void saveCostJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
    	PrintWriter out = resp.getWriter();
		try {
			// Determine what kind of cost detail is
			Integer costType	= ParamUtil.getInteger	(req, "cost_cost_type", -1);
			int costId			= ParamUtil.getInteger	(req, "cost_id", -1);
			double actual	 	= ParamUtil.getCurrency(req, "cost_actual", 0);
			String desc		 	= ParamUtil.getString	(req, "desc", null);
			Date costDate		= ParamUtil.getDate(req, "cost_date", getDateFormat(req), new Date());
			
			JSONObject costJSON 			 	= new JSONObject();
			DirectCostsLogic directCostsLogic	= new DirectCostsLogic();
			
			if (costType.equals(Constants.COST_TYPE_DIRECT)) {
				Directcosts cost = directCostsLogic.consDirectcosts(costId);
				cost.setActual(actual);
				cost.setDescription(desc);
				
				directCostsLogic.saveDirectcost(cost, costDate);
				
				costJSON.put("actual", cost.getActual());
				costJSON.put("desc", cost.getDescription());
				costJSON.put("costDate", getDateFormat(req).format(costDate));
			}
			else if (costType.equals(Constants.COST_TYPE_EXPENSE)) {
				ExpensesLogic expensesLogic = new ExpensesLogic();
				
				Expenses expense = expensesLogic.consExpenses(costId);
				expense.setActual(actual);
				expense.setDescription(desc);
				
				expensesLogic.saveExpenses(expense, costDate);
				
				costJSON.put("actual", expense.getActual());
				costJSON.put("desc", expense.getDescription());
				costJSON.put("costDate", getDateFormat(req).format(costDate));
			}
			else {
				throw new CostTypeException();
			}
			
			costJSON.put("type", costType);
			
			out.print(costJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Generate Cost Charts for Control
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void costChartsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
    	JSONArray listValuesPv	= new JSONArray();
    	JSONArray listValuesEv	= new JSONArray();
    	JSONArray listValuesAc	= new JSONArray();
    	JSONArray listValuesCv	= new JSONArray();
    	JSONArray listValuesSVt = new JSONArray();

    	PrintWriter out = resp.getWriter();
		Integer idProject = ParamUtil.getInteger(req, "id");

		try {
			ProjectFollowupLogic projectFollowupLogic 	= new ProjectFollowupLogic();
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			Project project = projectLogic.consProject(idProject);
			List<Projectfollowup> projectFollowups = projectFollowupLogic.consFollowups(project);

			Integer daysToDate = null;

			JSONArray arrayJSON = null;
			for (Projectfollowup item : projectFollowups) {

				daysToDate = item.getDaysToDate();
				daysToDate = (daysToDate == null?0:daysToDate);

				if (item.getPv() != null) {
					if (listValuesPv.size() == 0) {
						arrayJSON = new JSONArray();
						arrayJSON.add(0);
						arrayJSON.add(0);
						
						listValuesPv.add(arrayJSON);
					}
					arrayJSON = new JSONArray();
					arrayJSON.add(daysToDate);
					arrayJSON.add(item.getPv());
					
					listValuesPv.add(arrayJSON);
				}
				if (item.getEv() != null) {
					if (listValuesEv.size() == 0) {
						arrayJSON = new JSONArray();
						arrayJSON.add(0);
						arrayJSON.add(0);
						
						listValuesEv.add(arrayJSON);
					}
					arrayJSON = new JSONArray();
					arrayJSON.add(daysToDate);
					arrayJSON.add(item.getEv());
					
					listValuesEv.add(arrayJSON);
				}
				if (item.getAc() != null) {
					if (listValuesAc.size() == 0) {
						arrayJSON = new JSONArray();
						arrayJSON.add(0);
						arrayJSON.add(0);
						
						listValuesAc.add(arrayJSON);
					}
					arrayJSON = new JSONArray();
					arrayJSON.add(daysToDate);
					arrayJSON.add(item.getAc());
					
					listValuesAc.add(arrayJSON);
				}

				if (item.getCv() != null) {
					if (listValuesCv.size() == 0) {
						arrayJSON = new JSONArray();
						arrayJSON.add(0);
						arrayJSON.add(0);
						
						listValuesCv.add(arrayJSON);
					}
					arrayJSON = new JSONArray();
					arrayJSON.add(daysToDate);
					arrayJSON.add(item.getCv());
					
					listValuesCv.add(arrayJSON);
				}


				Double es = OpenppmUtil.getES(projectFollowups, item);
				
				if (es != null) {
					double svt = es - daysToDate;

					if (listValuesSVt.size() == 0) {
						arrayJSON = new JSONArray();
						arrayJSON.add(0);
						arrayJSON.add(0);
						
						listValuesSVt.add(arrayJSON);
					}
					arrayJSON = new JSONArray();
					arrayJSON.add(daysToDate);
					arrayJSON.add(svt);
					
					listValuesSVt.add(arrayJSON);
				}
			}

			JSONObject chartJSON = new JSONObject();
			chartJSON.put("listValuesPv", listValuesPv);
			chartJSON.put("listValuesEv", listValuesEv);
			chartJSON.put("listValuesAc", listValuesAc);
			chartJSON.put("listValuesSVt", listValuesSVt);
			chartJSON.put("listValuesCv", listValuesCv);

			out.print(chartJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}
    
	/**
     * Save followup
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void saveFollowupJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
    	int idFollowup	= ParamUtil.getInteger(req, "followup_id", -1);
    	int idProject	= ParamUtil.getInteger(req, "id", -1);
		
    	PrintWriter out = resp.getWriter();
    	
		try {
			ProjectFollowupLogic projectFollowupLogic 	= new ProjectFollowupLogic();
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));
			RiskRegisterLogic riskRegisterLogic 		= new RiskRegisterLogic();
			JSONObject info 							= new JSONObject();

			Projectfollowup followup = projectFollowupLogic.consFollowup(idFollowup);
			Project proj = projectLogic.consProject(idProject);
			
			setFollowupFromRequest(req, followup);
			followup.setProject(proj);
			
			if (followup.getRiskFlag() != null) {
				// Calculate actual riskRating
				List<Riskregister> riskregisters = riskRegisterLogic.findByProjectAndOpen(proj);
				
				int riskRating = 0; 
				for (Riskregister riskregister : riskregisters) {
					riskRating += riskregister.getRiskRating();
				}
				
				if (riskRating > 0) {
					followup.setRiskRating(riskRating);
				}
				
			}
			projectFollowupLogic.save(followup);
			
			projectFollowupLogic.updateRAG(proj);
			
			info.put("riskRating", followup.getRiskRating());
			
			out.print(infoUpdated(getResourceBundle(req), info,"followup"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Update Followup
     * 
     * @param req
     * @param resp
	 * @throws IOException 
	 * @throws ServletException 
     */
    private void updateFollowup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Integer idFollowup = ParamUtil.getInteger(req, "followup_id", -1);
		
		Double ev = ParamUtil.getCurrency(req, "ev", null);
		Double ac = ParamUtil.getCurrency(req, "ac", null);
		
		try {
            // Declare logics
			ProjectFollowupLogic projectFollowupLogic   = new ProjectFollowupLogic(getSettings(req), getResourceBundle(req));

            // Consult followup
			Projectfollowup followup = projectFollowupLogic.consFollowup(idFollowup);

            // Update followup
            //
			followup.setAc(ac);
			followup.setEv(ev);

            // Settings for values calculated
            followup.setSettings(getSettings(req));

            // Logic save followup
            List<Info> infos = projectFollowupLogic.saveFollowup(followup, getUser(req));

            // Messages
            createInfos(infos, req);
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewControlProject(null, req, resp);
	}
    
    /**
     * Generate chart finance
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void financeChartJX(HttpServletRequest req,
    		HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();

		Integer idProject = ParamUtil.getInteger(req, "id");
		
		List<Incomes> actualIncomes 	= null;
		List<Incomes> plannedIncomes 	= null;
		
		try {
			//ACTUAL
			HashMap<Integer, Double> actMap = new HashMap<Integer, Double>();
			JSONArray actualValues	= new JSONArray();
			IncomesLogic incomesLogic = new IncomesLogic();
			
			actualIncomes = incomesLogic.consIncomes(idProject, Incomes.ACTUALBILLDATE);
			
			int maxActDay = 0;
			
			for (Incomes item : actualIncomes) {
				if (item.getActualBillAmmount() != null && item.getActDaysToDate() != null) {
					actMap.put(item.getActDaysToDate(), item.getActualBillAmmount());
					maxActDay = item.getActDaysToDate();
				}
			}
			
			//PLANNED
			JSONArray plannedValues	= new JSONArray();
			HashMap<Integer, Double> planMap = new HashMap<Integer, Double>();
			
			plannedIncomes = incomesLogic.consIncomes(idProject, Incomes.PLANNEDBILLDATE);
			
			int maxPlantDay = 0;
			for (Incomes item : plannedIncomes) {
				planMap.put(item.getPlanDaysToDate(), item.getPlannedBillAmmount());
				maxPlantDay = item.getPlanDaysToDate();
			}
			
			int maxDay = (maxActDay >= maxPlantDay?maxActDay: maxPlantDay);
			Double plannedAfterValue = 0.0;
			Double actualAfterValue = 0.0;
			
			for (int i = 0 ;i <= maxDay; i++) {
				
				if (planMap.containsKey(i)) { 
					for(int j=0;j<2;j++){
						JSONArray tupla = new JSONArray();
						tupla.add(i);
						tupla.add(j == 0 ? plannedAfterValue : planMap.get(i) + plannedAfterValue);
						plannedValues.add(tupla);
					}
					plannedAfterValue = planMap.get(i) + plannedAfterValue;
				}
				
				if (actMap.containsKey(i)) { 
					for(int j=0;j<2;j++){
						JSONArray tupla = new JSONArray();
						tupla.add(i);
						tupla.add(j == 0 ? actualAfterValue : actMap.get(i) + actualAfterValue);
						actualValues.add(tupla);
					}
					actualAfterValue = actMap.get(i) + actualAfterValue;
				}
				
			}
			
			JSONArray lastPlannedValue 	= new JSONArray();
			JSONArray lastActualValue 	= new JSONArray();
			
			if (plannedValues.size() != 0 && actualValues.size() != 0) {
				lastPlannedValue 	= plannedValues.getJSONArray(plannedValues.size() - 1);
				lastActualValue		= actualValues.getJSONArray(actualValues.size() - 1);
				
				if((Integer) lastPlannedValue.get(0) < (Integer) lastActualValue.get(0)){
					JSONArray tupla = new JSONArray();
					tupla.add(lastActualValue.get(0));
					tupla.add(lastPlannedValue.get(1));
					plannedValues.add(tupla);
				}
				else {
					JSONArray tupla = new JSONArray();
					tupla.add(lastPlannedValue.get(0));
					tupla.add(lastActualValue.get(1));
					actualValues.add(tupla);
				}
			}
			
			JSONObject updateJSON = new JSONObject();
			
			if (ValidateUtil.isNotNull(plannedValues)) {
				updateJSON.put("plannedValues", plannedValues);
			}
			if (ValidateUtil.isNotNull(actualValues)) {
				updateJSON.put("actualValues", actualValues);
			}
			
			out.print(updateJSON);
			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e); }
		finally { out.close(); }
    }	
		
    

	/**
     * Save income
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveIncomeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
    	int idIncome			 = ParamUtil.getInteger(req, Incomes.IDINCOME, -1);
    	double actualBillAmmount = ParamUtil.getCurrency(req, Incomes.ACTUALBILLAMMOUNT,0);
    	Date actualBillDate		 = ParamUtil.getDate(req, Incomes.ACTUALBILLDATE,getDateFormat(req),null);
    	String actualDescription = ParamUtil.getString(req, Incomes.ACTUALDESCRIPTION);
		
		PrintWriter out = resp.getWriter();
		
		try {
			IncomesLogic incomesLogic = new IncomesLogic();
			
			Incomes income = incomesLogic.consIncome(idIncome);
			income.setActualBillAmmount(actualBillAmmount);
			income.setActualBillDate(actualBillDate);
			income.setActualDescription(actualDescription);
			
			income = incomesLogic.save(income);
			
			JSONObject returnJSON = null;
			returnJSON = infoUpdated(getResourceBundle(req), returnJSON, "funding");
			
			returnJSON.put("actDaysToDate", income.getActDaysToDate());
			returnJSON.put(Incomes.PLANNEDBILLDATE, getDateFormat(req).format(income.getPlannedBillDate()));
			returnJSON.put(Incomes.PLANNEDBILLAMMOUNT, income.getPlannedBillAmmount());
			
			out.print(returnJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Save Scope Activity
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void saveScopeActivity(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
    	Integer idProject	= ParamUtil.getInteger(req, "id", null);
		int idActivity		= ParamUtil.getInteger(req, "activity_id", -1);
		double ev			= ParamUtil.getCurrency (req, "ev", 0);
		double poc			= ParamUtil.getCurrency (req, "poc", 0);
		String commentsPoc 	= ParamUtil.getString(req, Projectactivity.COMMENTSPOC, null);
		
		try {
			// Declare logics
			ProjectActivityLogic projectActivityLogic = new ProjectActivityLogic(getSettings(req), getResourceBundle(req));
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			// Logic get activity
			Projectactivity activity = projectActivityLogic.consActivity(idActivity);
			
			// Set activity data
			activity.setEv(ev);
			activity.setPoc(poc);
			activity.setCommentsPoc(commentsPoc);

			// Logic save activity
			projectActivityLogic.save(activity);
			
			// Logic update project
			projectLogic.updatePoc(new Project(idProject), getSettings(req));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewControlProject(idProject, req, resp);
	}

	/**
     * Update Milestone
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveMilestoneJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
    	// Request 
		//
    	Integer idMilestone			= ParamUtil.getInteger(req, "milestone_id", -1);
    	Date estimatedDate			= ParamUtil.getDate(req, "estimatedDate", getDateFormat(req), null);
		Date achieved				= ParamUtil.getDate(req, "achieved", getDateFormat(req), null);
		String achievedComments 	= ParamUtil.getString(req, Milestones.ACHIEVEDCOMMENTS, StringPool.BLANK);
		Integer idMilestoneType 	= ParamUtil.getInteger(req, Milestones.MILESTONETYPE, -1);
		Integer idMilestoneCategory = ParamUtil.getInteger(req, Milestones.MILESTONECATEGORY, -1);
		String description 			= ParamUtil.getString(req, Milestones.DESCRIPTION, StringPool.BLANK);
		Character reportType		= ParamUtil.getString(req, "report_type", StringPool.BLANK).charAt(0);
		Integer idActivity			= ParamUtil.getInteger(req, "activity", -1);
		String name					= ParamUtil.getString(req, "milestoneName", StringPool.BLANK);
		Integer idProject			= ParamUtil.getInteger(req, Project.IDPROJECT, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			Milestonetype milestonetype 		= null;
			Milestonecategory milestonecategory = null;
			Milestones milestone 				= new Milestones();
			
			// Declare logics
			MilestoneLogic milestoneLogic 				= new MilestoneLogic();
			
			// Joins
			List<String> joins = new ArrayList<String>();
			joins.add(Milestones.PROJECTACTIVITY);
			joins.add(Milestones.MILESTONETYPE);
			
			// Logic consult milestone
			if (idMilestone != -1) {
				milestone = milestoneLogic.findById(idMilestone, joins);
			}
			else {
				milestone.setProject(new Project(idProject));
			}
			
			// Set milestone
			//
			milestone.setDescription(description);
			milestone.setReportType(reportType);
			milestone.setProjectactivity(new Projectactivity(idActivity));
			milestone.setName(name);
			milestone.setEstimatedDate(estimatedDate);
			milestone.setAchieved(achieved);
			milestone.setAchievedComments(achievedComments);
			
			// Milestone type 
			//
			if (idMilestoneType != -1) {
				
				// Declare logic
				MilestonetypeLogic milestonetypeLogic = new MilestonetypeLogic();
				
				// Logic
				milestonetype = milestonetypeLogic.findById(idMilestoneType);
				
				milestone.setMilestonetype(milestonetype);
			}
			else {
				milestone.setMilestonetype(milestonetype);
			}
			
			// Milestone category 
			//
			if (idMilestoneCategory != -1) {
				
				// Declare logic
				MilestonecategoryLogic milestonecategoryLogic = new MilestonecategoryLogic();
				
				// Logic
				milestonecategory = milestonecategoryLogic.findById(idMilestoneCategory);
				
				// Set
				milestone.setMilestonecategory(milestonecategory);
			}
			else {
				milestone.setMilestonecategory(milestonecategory);
			}
			
			// Logic save milestone
			milestoneLogic.save(milestone);
			
			// Response
			//
			JSONObject infoJSON = JSONModelUtil.milestoneToJSON(getResourceBundle(req), milestone);
			
			if (idMilestone != -1) {
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "milestone");
			}
			else {
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "milestone");
			}
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}


	/**
     * Save Activity in control
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void saveActivity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    	Integer idProject		= ParamUtil.getInteger(req, "id");
    	Integer idActivity		= ParamUtil.getInteger(req, "activity_id", -1);
    	Date actualInitDate 	= ParamUtil.getDate(req, Projectactivity.ACTUALINITDATE, getDateFormat(req), null);
    	Date actualEndDate		= ParamUtil.getDate(req, Projectactivity.ACTUALENDDATE, getDateFormat(req), null);
    	String commentsDates	= ParamUtil.getString(req, Projectactivity.COMMENTSDATES, null);
		
		try {
			
			// Declare logic
			ProjectActivityLogic projectActivityLogic = new ProjectActivityLogic(getSettings(req), getResourceBundle(req));
			
			// Logic 
			projectActivityLogic.saveActivityInControl(idProject, idActivity, actualInitDate, actualEndDate, commentsDates);
			
			infoUpdated(req, "activity");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewControlProject(idProject, req, resp);
	}
    

    /**
     * Save timeline
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
	private void saveTimelineJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
    	
		// Get parameters
		Integer idProject		= ParamUtil.getInteger(req, "idProject");
    	Integer idTimeline		= ParamUtil.getInteger(req, Timeline.IDTIMELINE, -1);
    	Date date 				= ParamUtil.getDate(req, Timeline.TIMELINEDATE, getDateFormat(req), null);
    	String name				= ParamUtil.getString(req, Timeline.NAME, null);
    	String description		= ParamUtil.getString(req, Timeline.DESCRIPTION, null);
        ImportanceEnum importance = ParamUtil.getEnum(req, "importance", ImportanceEnum.class);
        Integer idTimelineType	= ParamUtil.getInteger(req, Timeline.TIMELINETYPE, -1);

		try {
			
			// Instance logic
			TimelineLogic logic = new TimelineLogic(getSettings(req), getResourceBundle(req));
			Timeline timeline;
			JSONObject infoJSON = new JSONObject();
			
			if (idProject == -1) {
				throw new Exception(getResourceBundle(req).getString("msg.error.data"));
			}
			
			if (idTimeline != -1) {
				timeline = logic.findById(idTimeline);
				infoUpdated(getResourceBundle(req), infoJSON, "timeline");
			}
			else {
				timeline = new Timeline();
				infoCreated(getResourceBundle(req), infoJSON, "timeline");
			}
			
			timeline.setProject(new Project(idProject));
			timeline.setTimelineDate(date);
			timeline.setName(name);
			timeline.setDescription(description);
            timeline.setImportanceEnum(importance);

            if (idTimelineType != -1) {
                timeline.setTimelineType(new TimelineType(idTimelineType));
            }
			
			// Save timeline
			timeline = logic.save(timeline);
			
			// Send data
			infoJSON.put(Timeline.IDTIMELINE, timeline.getIdTimeline());
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Delete timeline
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void deleteTimelineJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		try {
			
			// Get parameter
			int idTimeline	= ParamUtil.getInteger(req, Timeline.IDTIMELINE, -1);
			
			// Instance logic
			TimelineLogic timelineLogic = new TimelineLogic(getSettings(req), getResourceBundle(req));

            timelineLogic.remove(new Timeline(idTimeline));

			// Send data
			out.print(infoDeleted(getResourceBundle(req), "timeline"));
		} 
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}
	
	/**
     * Save executive report
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
	private void saveExecutivereportJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		PrintWriter out = resp.getWriter();
    	
		// Get parameters
		Integer idProject			= ParamUtil.getInteger(req, "idProject", -1);
    	Date statusDate 			= ParamUtil.getDate(req, Executivereport.ENTITY+Executivereport.STATUSDATE, getDateFormat(req), null);
    	String internal				= ParamUtil.getString(req, Executivereport.INTERNAL, null);
    	String external				= ParamUtil.getString(req, Executivereport.EXTERNAL, null);

		try {
			
			// Instance logic
			ExecutivereportLogic logic = new ExecutivereportLogic();
			Executivereport executivereport = null;
			JSONObject infoJSON = new JSONObject();
			
			if (idProject == -1) {
				throw new Exception(getResourceBundle(req).getString("msg.error.data"));
			}
			
			Project project = new Project(idProject);
			executivereport = logic.findByProject(project);
			
			if (executivereport != null) {
				infoUpdated(getResourceBundle(req), infoJSON, "executive_report");
			}
			else {
				executivereport = new Executivereport();
				infoCreated(getResourceBundle(req), infoJSON, "executive_report");
			}
			
			executivereport.setProject(project);
			executivereport.setStatusDate(statusDate);
			executivereport.setInternal(internal);
			executivereport.setExternal(external);
			
			// Save executive report
			logic.save(executivereport);
			
			// Send data
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Set Change control object from Http Request parameters of control_change_popup
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	private Changecontrol setChangeFromRequest(HttpServletRequest req) throws LogicException {
		Changecontrol change = null;
		
		Integer idProject		= ParamUtil.getInteger(req, "id", -1);
		Integer idChange		= ParamUtil.getInteger(req, "change_id", -1);
		String desc 			= ParamUtil.getString(req, "desc", null);
		Character priority 		= ParamUtil.getString(req, "priority", null).charAt(0);
		Date date				= ParamUtil.getDate(req, "date", getDateFormat(req));
		Integer idType			= ParamUtil.getInteger(req, "type", -1);
		String recSolution		= ParamUtil.getString(req, "solution", null);
		String originator		= ParamUtil.getString(req, "originator", null);
		String impact			= ParamUtil.getString(req, "impact", null);
		String[] aResolution	= ParamUtil.getStringValues(req, "resolution", null);
		String resolution		= (aResolution == null ? null : aResolution[0]);
		Date resolutionDate	 	= ParamUtil.getDate(req, "resolution_date", getDateFormat(req), null);
		String reason			= ParamUtil.getString(req, "resolution_reason", null);
		String resolutionName	= ParamUtil.getString(req, "resolution_name", null);
		
		if (idProject == -1 || idType == -1) {
			throw new NoDataFoundException();
		}
		
		Changetype type = new Changetype();
		type.setIdChangeType(idType);
		
		change = new Changecontrol();
		if (idChange != -1) {
			change.setIdChange(idChange);
		}
		change.setProject(new Project(idProject));
		change.setChangeDate(date);
		change.setDescription(desc);
		change.setPriority(priority);
		change.setChangetype(type);
		change.setRecommendedSolution(recSolution);
		change.setOriginator(originator);
		change.setImpactDescription(impact);
		
		if (resolution != null) { // Resolve change control
			change.setResolution("Y".equals(resolution));
			change.setResolutionDate(resolutionDate);
			change.setResolutionReason(reason);
			change.setResolutionName(resolutionName);
		}
		
		return change;
	}
	
	/**
	 * Recover Control-Project info and forward to control-project.jsp
	 *  
	 * @param idProject
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewControlProject(Integer idProject, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String orderFollowup = ParamUtil.getString(req, "orderFollowup", SettingUtil.getString(getSettings(req), Settings.SETTING_STATUS_REPORT_ORDER, Settings.DEFAULT_STATUS_REPORT_ORDER));

		// Information to recover
		Project project								= null;	
		List<Projectfollowup> followups				= null;
		List<Projectfollowup> followupsES			= null;
		List<Projectfollowup> followupsSel			= null;
		List<Milestones> milestones					= null;
		List<Changecontrol> changes					= null;
		List<Changetype> changeTypes				= null;
		Double wip									= 0D;
		Integer dsoBilled							= null;
		Integer dsoUnbilled							= null;
		List<Projectcosts> costs					= null;
		List<Iwo> iwos								= null;		
		Projectactivity rootActivity				= null;		
		List<Checklist> checklists					= null;
		List<Wbsnode> wbsnodes						= null;
		List<Documentproject> docs					= null;
		Calendar calendar 							= DateUtil.getCalendar();
		List<Projectactivity> activities			= null;
		List<Timeline> timelines					= null;
		Executivereport executivereport				= null;
		HashMap<String, String> configurations 		= null;
		List<Milestonetype> milestoneTypes			= null;
		List<Milestonecategory> milestoneCategories	= null;
		List<Teammember> team						= null;
		List<Jobcategory> jobcategories				= null;
		List<Skill> skills							= null;
		List<Performingorg> perfOrgs				= null;
		List<Resourcepool> resourcePools			= null;
		List<Seller> sellers						= null;
		
		if (idProject == null) {
			idProject = ParamUtil.getInteger(req, "id", (Integer)req.getSession().getAttribute("idProject"));
		}
		
		project = new Project(idProject);
		
		boolean hasPermission = false;
		
		try {
			
			hasPermission = (SecurityUtil.hasPermission(req, project, Constants.TAB_CONTROL)
					&& SecurityUtil.hasPermissionProject(req, Constants.STATUS_CONTROL, Constants.STATUS_CLOSED, Constants.STATUS_ARCHIVED));
			
			if (hasPermission) {
				
				// Declare logics
				ProjectFollowupLogic followupLogic				= new ProjectFollowupLogic();
				ChangeTypesLogic changeLogic					= new ChangeTypesLogic();
				ChangeControlLogic changeControlLogic 			= new ChangeControlLogic();
				ChecklistLogic checklistLogic 					= new ChecklistLogic();
				DocumentprojectLogic documentprojectLogic 		= new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
				IwosLogic iwosLogic								= new IwosLogic();
				MilestoneLogic milestoneLogic					= new MilestoneLogic();
				ProjectActivityLogic projectActivityLogic 		= new ProjectActivityLogic(getSettings(req), getResourceBundle(req));
				ProjectCostsLogic projectCostsLogic				= new ProjectCostsLogic();
				ProjectLogic projectLogic 						= new ProjectLogic(getSettings(req), getResourceBundle(req));
				WBSNodeLogic wbsNodeLogic 						= new WBSNodeLogic();
				TimelineLogic timelineLogic						= new TimelineLogic(getSettings(req), getResourceBundle(req));
				ExecutivereportLogic executivereportLogic		= new ExecutivereportLogic();
				ConfigurationLogic configurationLogic			= new ConfigurationLogic();
				MilestonetypeLogic milestonetypeLogic			= new MilestonetypeLogic();
				MilestonecategoryLogic milestonecategoryLogic 	= new MilestonecategoryLogic();
				TeamMemberLogic teamMemberLogic					= new TeamMemberLogic();
				JobcategoryLogic jobcategoryLogic				= new JobcategoryLogic();
				SkillLogic skillLogic							= new SkillLogic();
				ResourcepoolLogic resourcepoolLogic				= new ResourcepoolLogic();
				SellerLogic sellerLogic 						= new SellerLogic();
				PerformingOrgLogic performingOrgLogic			= new PerformingOrgLogic();
				
				List<String> joins = new ArrayList<String>();
				joins.add(Project.PROGRAM);
                joins.add(Project.PROGRAM + "." + Program.EMPLOYEE);
                joins.add(Project.PROGRAM + "." + Program.EMPLOYEE + "." + Employee.CONTACT);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
				joins.add(Project.PROJECTKPIS);
				joins.add(Project.PROJECTKPIS+"."+Projectkpi.METRICKPI);
				joins.add(Project.INCOMESES);
				joins.add(Project.STAKEHOLDERS);
				joins.add(Project.STAKEHOLDERS + "." + Stakeholder.EMPLOYEE);
				joins.add(Project.STAKEHOLDERS + "." + Stakeholder.EMPLOYEE + "." + Employee.CONTACT);

				project = projectLogic.consProject(project, joins);
				
				joins = new ArrayList<String>();
				joins.add(Projectfollowup.PROJECT);
				
				//check that the date of the followups is not greater than tomorrow
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(SettingUtil.getString(getSettings(req), Settings.SETTING_DAYS_STATUS_REPORT, Settings.DEFAULT_SETTING_DAYS_STATUS_REPORT)));
				followupsSel 	= followupLogic.findByProject(project, calendar.getTime(), Projectfollowup.FOLLOWUPDATE, orderFollowup, joins);
				
				calendar.setTime(new Date());
				calendar.add(Calendar.YEAR, +1);
				followups 		= followupLogic.findByProject(project, calendar.getTime(), Projectfollowup.FOLLOWUPDATE, orderFollowup, joins);
				followupsES 	= followupLogic.findByProject(project, calendar.getTime(), Projectfollowup.FOLLOWUPDATE, Constants.ASCENDENT, joins);
				
				// Get milestones
				//
				joins = new ArrayList<String>();
				joins.add(Milestones.PROJECTACTIVITY);
				joins.add(Milestones.MILESTONETYPE);
				joins.add(Milestones.MILESTONECATEGORY);
				
				milestones			= milestoneLogic.findByRelation(Milestones.PROJECT, project, Milestones.PLANNED, Constants.ASCENDENT, joins);
				changes				= changeControlLogic.consChangesControl(project);
				changeTypes			= changeLogic.findByRelation(Changetype.COMPANY, getCompany(req));
				wip					= projectLogic.calculateWIP(project);
				dsoBilled			= projectLogic.calculateDSOBilled(project);
				dsoUnbilled			= projectLogic.calculateDSOUnbilled(project);
				costs				= projectCostsLogic.consCosts(project);
				iwos				= iwosLogic.consIwos(project);		
				checklists			= checklistLogic.findByProject(project);
				wbsnodes			= wbsNodeLogic.findByProject(project);

                joins = new ArrayList<String>();
                joins.add(Timeline.TIMELINETYPE);
                joins.add(Timeline.DOCUMENTPROJECT);
				timelines			= timelineLogic.findByRelation(Timeline.PROJECT, project, joins);

				executivereport 	= executivereportLogic.findByProject(project);
				team				= teamMemberLogic.consTeamMembers(new Project(idProject));
				jobcategories		= jobcategoryLogic.findByRelation(Jobcategory.COMPANY, getCompany(req), Jobcategory.NAME, Constants.ASCENDENT);
				skills				= skillLogic.findByRelation(Skill.COMPANY, getCompany(req), Skill.NAME, Constants.ASCENDENT);
				perfOrgs  			= performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req), Performingorg.NAME, Constants.ASCENDENT);
				resourcePools 		= resourcepoolLogic.findByRelation(Resourcepool.COMPANY, getCompany(req));
				sellers				= sellerLogic.findByRelation(Seller.COMPANY, getCompany(req), Seller.NAME, Constants.ASCENDENT);
				
				// Configurations
				configurations 	= configurationLogic.findByTypes(getUser(req), Configurations.TYPE_CAPACITY_RUNNING, Configurations.TYPE_CAPACITY_PLANNING);

				joins = new ArrayList<String>();
				joins.add(Projectactivity.WBSNODE);
				
				activities 		= projectActivityLogic.consActivities(project, joins);
				
				String documentStorage = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_STORAGE, Settings.DEFAULT_PROJECT_DOCUMENT_STORAGE);
				
				// Get documents
				docs = documentprojectLogic.getDocuments(getSettings(req), project, Constants.DOCUMENT_CONTROL);
				
				// Milestone types
				milestoneTypes	= milestonetypeLogic.findByRelation(Milestonetype.COMPANY,  getCompany(req), Milestonetype.NAME, Constants.ASCENDENT);
				
				// Milestone categories
				milestoneCategories	= milestonecategoryLogic.findByRelation(Milestonecategory.COMPANY,  getCompany(req), Milestonecategory.NAME, Constants.ASCENDENT);
				
				req.setAttribute("docs", docs);
				req.setAttribute("documentStorage", documentStorage);
				rootActivity = projectActivityLogic.consRootActivity(project);
			}
		}
		catch (Exception e) {
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}
		
		if (hasPermission) {
			req.setAttribute("project", project);
			req.setAttribute("activities", activities);
			req.setAttribute("followups", followups);
			req.setAttribute("followupsES", followupsES);
			req.setAttribute("followupsSel", followupsSel);
			req.setAttribute("milestones", milestones);
			req.setAttribute("changes", changes);
			req.setAttribute("changeTypes", changeTypes);
			req.setAttribute("wip", wip);
			req.setAttribute("dso_billed", dsoBilled);
			req.setAttribute("dso_unbilled", dsoUnbilled);
			req.setAttribute("costs", costs);
			req.setAttribute("iwos", iwos);
			req.setAttribute("rootActivity", rootActivity);			
			req.setAttribute("checklists", checklists);
			req.setAttribute("wbsnodes", wbsnodes);
			req.setAttribute("orderFollowup", orderFollowup);
			req.setAttribute("timelines", timelines);
			req.setAttribute("executivereport", executivereport);
			req.setAttribute("milestoneTypes", milestoneTypes);
			req.setAttribute("milestoneCategories", milestoneCategories);
			req.setAttribute("team", team);
			req.setAttribute("jobcategories", jobcategories);
			req.setAttribute("skills", skills);
			req.setAttribute("perforgs", perfOrgs);		
			req.setAttribute("resourcePools", resourcePools);
			req.setAttribute("sellers", sellers);

			req.setAttribute("title", ValidateUtil.isNotNull(project.getChartLabel()) ? 
					project.getProjectName() + " ("+project.getChartLabel()+")" : project.getProjectName());
			
			req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));

            // Send actual date
            SimpleDateFormat date = new SimpleDateFormat(Constants.DATE_PATTERN);

            req.setAttribute("actualDate", date.format(new Date()));

			// Configurations
			req.setAttribute("configurations", configurations);
			
			req.setAttribute("tab", Constants.TAB_CONTROL);
			forward("/index.jsp?nextForm=project/control/control_project", req, resp);
		}
		else {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}
	
	/**
	 * Set ProjectFollowup from Control-Project request
	 *  
	 * @param req
	 * @param followup
	 */
	private void setFollowupFromRequest(HttpServletRequest req,
			Projectfollowup followup) {
		
		String sGeneralFlag		= ParamUtil.getString(req, "general_status", null);
		Character generalFlag	= (sGeneralFlag != null && sGeneralFlag.length() > 0) ? sGeneralFlag.charAt(0) : null;
		String generalComments	= ParamUtil.getString(req, "general_desc", null);
		String sRiskFlag		= ParamUtil.getString(req, "risk_status", null);
		Character riskFlag		= (sRiskFlag != null && sRiskFlag.length() > 0) ? sRiskFlag.charAt(0) : null;
		String riskComments		= ParamUtil.getString(req, "risk_desc", null);
		String sScheduleFlag	= ParamUtil.getString(req, "schedule_status", null);
		Character scheduleFlag	= (sScheduleFlag != null && sScheduleFlag.length() > 0) ? sScheduleFlag.charAt(0) : null;
		String scheduleComments	= ParamUtil.getString(req, "schedule_desc", null);
		String sCostFlag		= ParamUtil.getString(req, "cost_status", null);
		Character costFlag		= (sCostFlag != null && sCostFlag.length() > 0) ? sCostFlag.charAt(0) : null;
		String costComments		= ParamUtil.getString(req, "cost_desc", null);
		
		followup.setGeneralFlag(generalFlag);
		followup.setGeneralComments(generalComments);
		followup.setRiskFlag(riskFlag);
		followup.setRisksComments(riskComments);
		followup.setScheduleFlag(scheduleFlag);
		followup.setScheduleComments(scheduleComments);
		followup.setCostFlag(costFlag);
		followup.setCostComments(costComments);
	}
}
