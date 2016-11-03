
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
 * File: ProjectPlanServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import com.granule.json.JSON;
import com.granule.json.JSONString;
import es.sm2.openppm.core.charts.ChartWBS;
import es.sm2.openppm.core.charts.PlChartInfo;
import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.common.enums.PeriodicityEnum;
import es.sm2.openppm.core.javabean.OperationFte;
import es.sm2.openppm.core.javabean.TeamCalendar;
import es.sm2.openppm.core.javabean.TeamMembersFTEs;
import es.sm2.openppm.core.listener.ProcessListener;
import es.sm2.openppm.core.listener.impl.project.SavePlanActivityAbstractListener;
import es.sm2.openppm.core.listener.impl.project.UpdateTeammemberAbstractListener;
import es.sm2.openppm.core.logic.impl.BudgetaccountsLogic;
import es.sm2.openppm.core.logic.impl.CalendarBaseLogic;
import es.sm2.openppm.core.logic.impl.CalendarbaseexceptionsLogic;
import es.sm2.openppm.core.logic.impl.ChartLogic;
import es.sm2.openppm.core.logic.impl.ChecklistLogic;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.DirectCostsLogic;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.ExpensesLogic;
import es.sm2.openppm.core.logic.impl.IncomesLogic;
import es.sm2.openppm.core.logic.impl.IwosLogic;
import es.sm2.openppm.core.logic.impl.JobcategoryLogic;
import es.sm2.openppm.core.logic.impl.MetrickpiLogic;
import es.sm2.openppm.core.logic.impl.MilestoneLogic;
import es.sm2.openppm.core.logic.impl.MilestonecategoryLogic;
import es.sm2.openppm.core.logic.impl.MilestonetypeLogic;
import es.sm2.openppm.core.logic.impl.NotificationLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.logic.impl.ProjectActivityLogic;
import es.sm2.openppm.core.logic.impl.ProjectCalendarExceptionsLogic;
import es.sm2.openppm.core.logic.impl.ProjectCostsLogic;
import es.sm2.openppm.core.logic.impl.ProjectFollowupLogic;
import es.sm2.openppm.core.logic.impl.ProjectKpiLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.ProjectcalendarLogic;
import es.sm2.openppm.core.logic.impl.ResourceProfilesLogic;
import es.sm2.openppm.core.logic.impl.ResourcepoolLogic;
import es.sm2.openppm.core.logic.impl.SellerLogic;
import es.sm2.openppm.core.logic.impl.SkillLogic;
import es.sm2.openppm.core.logic.impl.TeamMemberLogic;
import es.sm2.openppm.core.logic.impl.TimesheetLogic;
import es.sm2.openppm.core.logic.impl.WBSNodeLogic;
import es.sm2.openppm.core.logic.impl.WBSTemplateLogic;
import es.sm2.openppm.core.logic.security.actions.PlanTabAction;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.model.impl.Budgetaccounts;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Calendarbaseexceptions;
import es.sm2.openppm.core.model.impl.Checklist;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Directcosts;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Expenses;
import es.sm2.openppm.core.model.impl.Historickpi;
import es.sm2.openppm.core.model.impl.Incomes;
import es.sm2.openppm.core.model.impl.Iwo;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Metrickpi;
import es.sm2.openppm.core.model.impl.Milestonecategory;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectcalendar;
import es.sm2.openppm.core.model.impl.Projectcalendarexceptions;
import es.sm2.openppm.core.model.impl.Projectcosts;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Projectkpi;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Skill;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.impl.Wbstemplate;
import es.sm2.openppm.core.utils.DataUtil;
import es.sm2.openppm.core.utils.JSONModelUtil;
import es.sm2.openppm.front.utils.ChartUtil;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.RequestUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.Info;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.CsvFile;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectCalendarException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.LogManager;

/**
 * Servlet implementation class ProjectServer
 */
public class ProjectPlanServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(ProjectPlanServlet.class);

	public final static String REFERENCE = "projectplan";

	/***************** Actions ****************/
	public final static String SAVE_PROJECT 					= "save-project";
	public final static String APPROVE_PROJECT 					= "approve-project";
	public final static String DELETE_WBSNODE 					= "delete-wbsnode";
	public final static String SAVE_ACTIVITY 					= "save-activity";
	public final static String CALCULATED_PV					= "calculated-pv";
	public final static String EXPORT_FINANCE_PLANNING_CSV		= "export-finance-planning";


	//Cost Actions
	public final static String DEL_INCOME 				= "delete-income";
	public final static String DEL_FOLLOWUP 			= "delete-followup";
	public final static String DEL_COST 				= "delete_cost";
	public final static String DEL_IWO 					= "delete_iwo";
	// Calendar
	public final static String CHANGE_CALENDAR_BASE		= "change-calendar-base";
	//KPI
	public final static String IMPORT_KPIS				= "import-kpis";

	/************** Actions AJAX **************/
	public final static String JX_CONS_WBSNODES 		= "ajax-cons-wbsnodes";
	public final static String JX_SAVE_MILESTONE 		= "ajax-save-milestone";
	public final static String JX_DELETE_MILESTONE 		= "ajax-delete-milestone";
	public final static String JX_PLAN_GANTT_CHART 		= "ajax-update-chart-gantt-planning";
	public final static String JX_SAVE_CHECKLIST 		= "ajax-save-checklist";
	public final static String JX_DELETE_CHECKLIST 		= "ajax-delete-checklist";
	// Cost Actions AJAX
	public final static String JX_SAVE_INCOME 			            = "ajax-save-income";
	public final static String JX_SAVE_FOLLOWUP 		            = "ajax-save-followup";
	public final static String JX_SAVE_COST 			            = "ajax-save-cost";
	public final static String JX_SAVE_IWO 				            = "ajax-save-iwo";
	public final static String JX_PROJECT_COSTS_CHART 	            = "ajax-update-chart-project-costs";
	public final static String JX_PL_CHART 				            = "ajax-update-chartPL";
	public final static String JX_FINANCE_CHART 		            = "ajax-finance-chart";
    public final static String JX_CHECK_VIABILITY_RECALCULATE       = "check-viability-pv-recalculate";
    public final static String JX_RECALCULATE_PV                    = "recalculate-pv";
	// Plan HR Actions AJAX
    public final static String JX_SAVE_MEMBER 			    = "ajax-save-team-member";
    public final static String JX_UPDATE_MEMBER			    = "ajax-update-team-member";
    public final static String JX_FIND_MEMBER 			    = "ajax-find-team-member";
    public final static String JX_UPDATE_STAFFING_TABLE	    = "ajax-update-staffing-table";
    public final static String JX_RELEASED_RESOURCE		    = "ajax-released-resource";
    public final static String JX_OBS_CHART 			    = "ajax-generate-chart-obs";
	public final static String JX_CHECK_RELEASED_RESOURCE   = "ajax-check-released-resource";
    public final static String JX_CONSULT_OPERATIONS        = "ajax-consult-operations";
	// Calendar
	public final static String JX_SAVE_CALENDAR 			= "ajax-save-calendar";
	public final static String JX_SAVE_CALENDAR_EXCEPTION	= "ajax-save-calendar-exception";
	public final static String JX_DELETE_CALENDAR_EXCEPTION = "ajax-delete-calendar-exception";
	public final static String JX_UPDATE_TEAM_CALENDAR		= "ajax-update-team-calendar";
	// KPI
	public final static String JX_SAVE_KPI			= "ajax-save-kpi";
	public final static String JX_DELETE_KPI		= "ajax-delete-kpi";
	public final static String JX_UPDATE_THRESHOLD	= "ajax-update-threshold";
	public final static String JX_CHECK_KPI			= "ajax-check-kpi";
	// WBS
	public final static String JX_WBS_CHART 					= "ajax-generate-chart-wbs";
	public final static String JX_WBS_VALIDATE 					= "ajax-wbs-validate";
	public final static String JX_EXPORT_WBS 					= "ajax-wbs-export";
	public final static String JX_VIEW_TEMPLATES_WBS			= "ajax-wbs-view-templates";
	public final static String JX_WBS_NODE_VALIDATE_AND_SAVE 	= "ajax-wbs-node-validate-and-save";
	public final static String JX_WBS_NODE_VALIDATE 			= "ajax-wbs-node-validate";
	public final static String JX_IMPORT_WBS 					= "ajax-wbs-import";



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

			// Consult projects of logged project manager
            //
            if (accion == null || StringPool.BLANK.equals(accion)
                    || DELETE_WBSNODE.equals(accion)
                    || SAVE_PROJECT.equals(accion) || DEL_INCOME.equals(accion)
                    || DEL_COST.equals(accion) || DEL_FOLLOWUP.equals(accion)
                    || DEL_IWO.equals(accion))
            { projectActions(req, resp, accion); }
            else if (SAVE_ACTIVITY.equals(accion)) { saveActivity(req, resp); }
            else if (CHANGE_CALENDAR_BASE.equals(accion)) { changeCalendarBase(req, resp); }
            else if (APPROVE_PROJECT.equals(accion)) { approveProject(req, resp);  }
            else if (IMPORT_KPIS.equals(accion)) { importKpis(req, resp); }
            else if (EXPORT_FINANCE_PLANNING_CSV.equals(accion)) { exportFinancePlanningCSV(req, resp); }

            /************** Actions AJAX **************/
            else if (JX_WBS_VALIDATE.equals(accion)) { validateWBS(req, resp); }
            else if (JX_SAVE_MILESTONE.equals(accion)) { saveMilestoneJX(req, resp); }
            else if (JX_SAVE_CHECKLIST.equals(accion)) { saveCheckListJX(req, resp); }
            else if (JX_DELETE_CHECKLIST.equals(accion)) { deleteCheckListJX(req, resp); }
            else if (JX_SAVE_CALENDAR.equals(accion)) { saveCalendarJX(req, resp); }
            else if (JX_SAVE_CALENDAR_EXCEPTION.equals(accion)) { saveCalendarExceptionJX(req, resp); }
            else if (JX_DELETE_CALENDAR_EXCEPTION.equals(accion)) { deleteCalendarExceptionJX(req, resp); }
            else if (JX_UPDATE_TEAM_CALENDAR.equals(accion)) { updateTeamCalendarJX(req, resp); }
            else if (JX_SAVE_MEMBER.equals(accion)) { saveMemberJX(req, resp); }
            else if (JX_UPDATE_MEMBER.equals(accion)) { updateMemberJX(req, resp); }
            else if (JX_FIND_MEMBER.equals(accion)) { findMemberJX(req, resp); }
            else if (JX_SAVE_KPI.equals(accion)) { saveKpiJX(req, resp); }
            else if (JX_DELETE_KPI.equals(accion)) { deleteKpiJX(req, resp); }
            else if (JX_UPDATE_THRESHOLD.equals(accion)) { updateThresholdJX(req, resp); }
            else if (JX_UPDATE_STAFFING_TABLE.equals(accion)) { updateStaffingTableJX(req, resp); }
            else if (JX_RELEASED_RESOURCE.equals(accion)) { releasedResourceJX(req, resp); }
            else if (JX_CHECK_RELEASED_RESOURCE.equals(accion)) { checkReleasedResourceJX(req, resp); }
            else if (JX_CONS_WBSNODES.equals(accion)) { consWbsNodesJX(req, resp); }
            else if (JX_DELETE_MILESTONE.equals(accion)) { deleteMilestoneJX(req, resp); }
            else if (JX_PLAN_GANTT_CHART.equals(accion)) { planGanttChartJX(req, resp); }
            else if (JX_WBS_CHART.equals(accion)) { wbsChartJX(req, resp); }
            else if (JX_SAVE_INCOME.equals(accion)) { saveIncomeJX(req, resp); }
            else if (JX_SAVE_FOLLOWUP.equals(accion)) { saveFollowupJX(req, resp); }
            else if (JX_SAVE_COST.equals(accion)) { saveCostJX(req, resp); }
            else if (JX_SAVE_IWO.equals(accion)) { saveIwoJX(req, resp); }
            else if (JX_PROJECT_COSTS_CHART.equals(accion)) { updateProjectCostChartJX(req, resp); }
            else if (JX_FINANCE_CHART.equals(accion)) {	financeChartJX(req, resp); }
            else if (JX_PL_CHART.equals(accion)) { updatePlChartJX(req, resp); }
            else if (CALCULATED_PV.equals(accion)) { calculatedPV(req, resp); }
            else if (JX_WBS_NODE_VALIDATE_AND_SAVE.equals(accion)) { saveWbsNodeJX(req, resp); }
            else if (JX_WBS_NODE_VALIDATE.equals(accion)) { validateWbsNodeJX(req, resp); }
            else if (JX_EXPORT_WBS.equals(accion)) { exportWBSJX(req, resp); }
            else if (JX_VIEW_TEMPLATES_WBS.equals(accion)) { viewTemplatesWBSJX(req, resp); }
            else if (JX_IMPORT_WBS.equals(accion)) { importWBSJX(req, resp); }
            else if (JX_OBS_CHART.equals(accion)) { obsChartJX(req, resp); }
            else if (JX_CHECK_KPI.equals(accion)) { checkKPIJX(req, resp); }
            else if (JX_CONSULT_OPERATIONS.equals(accion)) { consultOperationsJX(req, resp); }
            else if (JX_RECALCULATE_PV.equals(accion)) { recalculatePlannedValue(req, resp); }
            else if (JX_CHECK_VIABILITY_RECALCULATE.equals(accion)) { checkViabilityRecalculatePV(req, resp); }
            // New security
            else if (SecurityUtil.hasPermission(req, PlanTabAction.JX_CREATE_FOLLOWUPS, accion)) { createFollowupsJX(req, resp); }
        }
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE + "?accion=403", req, resp);
		}
	}

    /**
     * Create followups by periodicity
     *
     * @param req
     * @param resp
     */
    private void createFollowupsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {
            // Request
            //
            Date dateSince	            = ParamUtil.getDate(req, "dateSince", getDateFormat(req), null);
            Date dateUntil	            = ParamUtil.getDate(req, "dateUntil", getDateFormat(req), null);
            PeriodicityEnum periodicity = ParamUtil.getEnum(req, "periodicity", PeriodicityEnum.class);
            int periodicityCount        = ParamUtil.getInteger(req, "periodicityCount", -1);
            Integer idProject 	        = ParamUtil.getInteger(req, "idProject", -1);

            ProjectFollowupLogic projectFollowupLogic = new ProjectFollowupLogic(getSettings(req), getResourceBundle(req));

            List <Projectfollowup> followups = projectFollowupLogic.createFollowups(idProject, dateSince, dateUntil, periodicity, periodicityCount);
            // Response
            //
            JSONObject responseJSON = new JSONObject();
            JSONArray followupsJSON = new JSONArray();
            for (Projectfollowup followup : followups) {

                // Parse to JSON object
                JSONObject followupJSON = JSONModelUtil.followupToJSON(getResourceBundle(req), followup, null);

                // Add JSON list
                followupsJSON.add(followupJSON);
            }

            if (followups.size() > 0) {
                responseJSON = infoCreated(getResourceBundle(req), responseJSON, "followup.periodicity");
            }
            else {
                responseJSON = info(StringPool.INFORMATION, getResourceBundle(req).getString("not_created"), responseJSON);
            }

            responseJSON.put("followups", followupsJSON);

            out.print(responseJSON);
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally { out.close(); }
    }


    /**
     * Consult operations
     *
     * @param req
     * @param resp
     */
    private void consultOperationsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        List<OperationFte> operationsFte    = null;
        List<String> labelsDates            = null;

        try {
            // Request
            Integer idEmployee		= ParamUtil.getInteger(req, "idEmployee", null);
            Date dateIn				= ParamUtil.getDate(req, "datein", getDateFormat(req), null);
            Date dateOut			= ParamUtil.getDate(req, "dateout", getDateFormat(req), null);

            if (idEmployee != null && dateIn != null && dateOut != null) {

                // Declare logic
                TimesheetLogic timesheetLogic = new TimesheetLogic();

                // Joins
                List<String> joins = new ArrayList<String>();
                joins.add(Timesheet.OPERATION);

                // Set dates
                List<Date> dates = DateUtil.lisDates(dateIn, dateOut);

                // Labels dates
                //
                labelsDates = new ArrayList<String>();

                if (ValidateUtil.isNotNull(dates)) {

                    for (Date date : dates) {
                        labelsDates.add(DateUtil.format(Constants.DATE_PATTERN_NARROW, date));
                    }

                    // Logic
                    operationsFte = timesheetLogic.consultOperations(new Employee(idEmployee), dateIn, dateOut, joins);
                }
            }
        }
        catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

        req.setAttribute("dates", labelsDates);
        req.setAttribute("operationsFte", operationsFte);

        forward("/project/common/operations_table.ajax.jsp", req, resp);
    }

    /**
     * Export finance planning to CSV
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void exportFinancePlanningCSV(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    	Integer idProject = ParamUtil.getInteger(req, "id");
		String fileName = "";
		CsvFile file = null;

		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

			Project project = projectLogic.consProject(idProject);

			// File
			fileName = project.getChartLabel();

			file = new CsvFile(Constants.SEPARATOR_CSV);


			IncomesLogic incomesLogic = new IncomesLogic();

			List<Incomes> incomes = incomesLogic.consIncomes(idProject, Incomes.PLANNEDBILLDATE);

			// Headers
			file.addValue(getResourceBundle(req).getString("funding.due_date"));
			file.addValue(getResourceBundle(req).getString("funding.days_date"));
			file.addValue(getResourceBundle(req).getString("funding.due_value"));
			file.addValue(getResourceBundle(req).getString("income.description"));
			file.newLine();

			// Content
			for (Incomes income : incomes) {

				file.addValue((income.getPlannedBillDate() == null ? StringPool.BLANK : getDateFormat(req).format(income.getPlannedBillDate())));
				file.addValue((income.getPlanDaysToDate() == null ? StringPool.BLANK : getNumberFormat().format(income.getPlanDaysToDate())));
				file.addValue((income.getPlannedBillAmmount() == null ? StringPool.BLANK : ValidateUtil.toCurrency(income.getPlannedBillAmmount())));
				file.addValue((income.getPlannedDescription()== null ? StringPool.BLANK : income.getPlannedDescription()));
				file.newLine();
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

        sendFile(req, resp, file.getFileBytes(), fileName + ".csv"); 
	}

	/**
     * Validate if WBS node has hours in APP0 and not in APP1, APP2 or APP3
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void validateWbsNodeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	Integer idWbsnode	= ParamUtil.getInteger(req, "wbs_id", -1);
    	Boolean isCA		= ParamUtil.getBoolean	(req, "wbs_ca", false);

    	PrintWriter out = resp.getWriter();

    	try {
    		WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();

    		JSONObject responseJSON = wbsNodeLogic.validateWBSnode(new Wbsnode(idWbsnode), isCA, getResourceBundle(req));

			out.print(responseJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Generate OBS chart
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void obsChartJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();
		Integer idProject = ParamUtil.getInteger(req, "id");

		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();

			Project project 		= projectLogic.consProject(idProject);

			Wbsnode wbsNodeParent 	= wbsNodeLogic.findFirstWBSnode(project);

			ChartWBS chartOBS 		= new ChartWBS(wbsNodeParent, false, getResourceBundle(req));

			out.print(chartOBS.generateOBS());

		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Import template WBS
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void importWBSJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	Integer idWBSTemplate 	= ParamUtil.getInteger(req, "idWBSTemplate", null);
    	Integer idWBSParent 	= ParamUtil.getInteger(req, "idWBSParent", null);
    	Integer idProject		= ParamUtil.getInteger(req, "idProject", null);
    	Boolean replace			= ParamUtil.getBoolean	(req, "replace", false);

    	PrintWriter out = resp.getWriter();
    	try {
    		JSONObject updateJSON = new JSONObject();

    		if(idWBSTemplate != null && idWBSParent != null && idProject != null){
    			WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();
        		ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

    			Project project = projectLogic.findById(idProject);

    			Wbsnode wbsnodeParent = wbsNodeLogic.findById(idWBSParent);

    			if(replace){
    				WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();

    				Wbstemplate rootTemplate = wbsTemplateLogic.findById(idWBSTemplate);

    				List<Wbstemplate> childsRootTemplate = wbsTemplateLogic.findChildsWbsnode(rootTemplate);

    				for(Wbstemplate childRootTemplate : childsRootTemplate){
    					wbsNodeLogic.saveWBSTemplate(wbsnodeParent, childRootTemplate.getIdWbsnode(), project);
    				}
    			}
    			else {
    				wbsNodeLogic.saveWBSTemplate(wbsnodeParent, idWBSTemplate, project);
    			}

    		}

    		ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			projectLogic.updateDataProject(idProject);
			projectLogic.updatePoc(new Project(idProject), getSettings(req));

    		updateJSON.put("INFO","WBS successfully imported.");

    		out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e); }
		finally { out.close(); }

	}

	/**
     * View templates WBS
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void viewTemplatesWBSJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();
    	try {
    		JSONObject updateJSON = new JSONObject();

    		Company company = getCompany(req);

    		WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();

    		List<Wbstemplate> wbstemplates = wbsTemplateLogic.findByCompanyAndRelationNull(company, Wbstemplate.WBSTEMPLATE);

    		JSONArray wbstemplatesJSON = new JSONArray();

    		for(Wbstemplate wbstemplate: wbstemplates){
    			JSONObject wbstemplateJSON = new JSONObject();

    			wbstemplateJSON.put(Wbstemplate.IDWBSNODE, wbstemplate.getIdWbsnode());
    			wbstemplateJSON.put(Wbstemplate.NAME, wbstemplate.getName());
    			wbstemplateJSON.put(Company.ENTITY, company.getName());

    			wbstemplatesJSON.add(wbstemplateJSON);
    		}

    		updateJSON.put("wbsTemplates", wbstemplatesJSON);

    		out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Export as template the WBS
     * @param req
     * @param resp
     * @throws IOException
     */
    private void exportWBSJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer idWBSNode = ParamUtil.getInteger(req, "idWBSNode", null);

		PrintWriter out = resp.getWriter();
    	try {
    		JSONObject updateJSON = new JSONObject();

    		WBSTemplateLogic wbsTemplateLogic 	= new WBSTemplateLogic();

    		if(idWBSNode != null){
				wbsTemplateLogic.saveWBSTemplate(idWBSNode, getCompany(req));
    		}

    		updateJSON.put("INFO", "WBS template saved.");
    		out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Import project/s kpis
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void importKpis(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

    	// Getting params
    	Integer idProject		= ParamUtil.getInteger(req, "id",null);
    	String idImportProjects = ParamUtil.getString(req, "idImportProjects",null);
    
    	// IDs from different projects
    	Integer[] idImportProjectsArray	= StringUtil.splitStrToIntegers(idImportProjects, null);

    	double weight = 0.0;
    	boolean isOverWeight = false;
    	try {

    		// Declare logics
    		ProjectLogic projectLogic 		= new ProjectLogic(getSettings(req), getResourceBundle(req));
    		ProjectKpiLogic projectKpiLogic = new ProjectKpiLogic();

    		// Import actual project by ID
    		Project actualProject = projectLogic.findById(idProject);

    		// Set joins
			List<String> joins = new ArrayList<String>();
			joins.add(Project.PROJECTKPIS);

    		if (idImportProjectsArray != null) {

    			// Check kpi weight
    			for (int id : idImportProjectsArray) {

    				// Import project by ID
	    			Project importProject = projectLogic.findById(id, joins);

    				weight = weight + projectKpiLogic.calcWeightKpi(importProject, actualProject);
    			}

    			// If weight is over 100
    			if (weight > 100) {
    				isOverWeight = true;
    			}

    			// Make KPI persistent
	    		for (int id : idImportProjectsArray) {

	    			// Import project by ID
	    			Project importProject = projectLogic.findById(id, joins);

					// Save KPI in DataBase
	    			 projectKpiLogic.importProjectKpi(importProject, actualProject, isOverWeight, getUser(req));
	    		}
    		}
    	}
    	catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

    	// Redirect to Project Planning
    	viewProjectPlanning(req, resp, null);
	}

	/**
     * Validate node WBS
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void saveWbsNodeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	Integer idProject	= ParamUtil.getInteger(req, "id",null);
    	Integer idWbsnode	= ParamUtil.getInteger(req, "wbs_id", -1);

    	PrintWriter out = resp.getWriter();
    	JSONObject updateJSON = new JSONObject();

    	try {

            // Logics
            //
            ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
    		WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();

    		Wbsnode wbsNode 	= null;
    		boolean isCa		= false;
    		Double oldBudget	= null;

    		if (idWbsnode != null && idWbsnode > 0) {

    			List<String> joins = new ArrayList<String>();
    			joins.add(Wbsnode.WBSNODE);
    			joins.add(Wbsnode.WBSNODES);

    			wbsNode = wbsNodeLogic.findById(idWbsnode, joins);
    			isCa = wbsNode.getIsControlAccount();
    			oldBudget = wbsNode.getBudget();
    		}

			wbsNode = setWbsNodeFromRequest(req, wbsNode);

			// Update milestones
    		if (idWbsnode != -1 && isCa && !wbsNode.getIsControlAccount()) {

    			ProjectActivityLogic projectActivityLogic = new ProjectActivityLogic(getSettings(req), getResourceBundle(req));

    			Projectactivity activity = projectActivityLogic.findByWBSnode(new Project(idProject), idWbsnode);

    			MilestoneLogic milestoneLogic = new MilestoneLogic();

				Projectactivity rootActivity = projectActivityLogic.consRootActivity(activity.getProject());

    			boolean hasMilestonesUpdated = milestoneLogic.changeActivity(activity, rootActivity);

    			updateJSON.put("updateMilestones", hasMilestonesUpdated);
    		}

			wbsNodeLogic.saveWBSnode(wbsNode, new Project(idProject), isCa);

            if (isCa && !wbsNode.getIsControlAccount()) {

                // Update root dates
                //
                ProjectActivityLogic projectActivityLogic = new ProjectActivityLogic(getSettings(req), getResourceBundle(req));

                projectActivityLogic.updateRootDates(idProject);

                // Update project
                projectLogic.updateDataProject(idProject);
            }

            // Update Poc
			projectLogic.updatePoc(new Project(idProject), getSettings(req));

			if(wbsNode.getBudget() != null && oldBudget != null){
				updateJSON.put("updateEV", oldBudget.compareTo(wbsNode.getBudget()) != 0 ? true : false);
			}
			else {
				updateJSON.put("updateEV", false);
			}

			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Calculated PV
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void calculatedPV(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();
    	Date date = ParamUtil.getDate(req, "date",getDateFormat(req));
    	Integer idProject = ParamUtil.getInteger(req, "idProject");

    	try {
    		ProjectFollowupLogic projectFollowupLogic 	= new ProjectFollowupLogic();
			JSONObject updateJSON 						= new JSONObject();
			Double pv									= null;

            // Changing Calculate PV method
//			Projectfollowup before  = projectFollowupLogic.beforeFollowup(new Project(idProject), date);
//			Projectfollowup after	= projectFollowupLogic.afterFollowup(new Project(idProject),date);

//			if (before != null && after != null){
//				Calendar calendarBefore 				= DateUtil.getCalendar();
//				Calendar calendarAfter 					= DateUtil.getCalendar();
//
//				beforeFollowup.put("FollowupDate", before.getFollowupDate().toString());
//				beforeFollowup.put("PV", before.getPv());
//				afterFollowup.put("FollowupDate", after.getFollowupDate().toString());
//				afterFollowup.put("PV", after.getPv());
//
//				Double pvBefore = before.getPv() == null? 0 : before.getPv();
//				Double pvAfter	= after.getPv() == null? 0 : after.getPv();
//				calendarBefore.setTime(before.getFollowupDate());
//				calendarAfter.setTime(after.getFollowupDate());
//
//				pv = pvBefore + ((pvAfter - pvBefore)*DateUtil.daysBetween(calendarBefore.getTime(), date)) / DateUtil.daysBetween(calendarBefore.getTime(), calendarAfter.getTime()) ;
//			}
//			else {
//				pv = -1.0;
//			}

            pv = projectFollowupLogic.calculatePV(idProject, date);
			updateJSON.put("pv", pv);
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}

	}

	/**
     * Cons WBS Nodes
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void consWbsNodesJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();
		Integer idProject = ParamUtil.getInteger(req, "id");
		int idNode		= ParamUtil.getInteger(req, "idNode", -1);

		try {
			WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();

			List<Wbsnode> wbsnodes = wbsNodeLogic.consWBSNodes(new Project(idProject));

			JSONArray array = new JSONArray();
			for (Wbsnode wbs : wbsnodes) {
				if (wbs.getIdWbsnode() != idNode) {
					JSONObject obj = JSONModelUtil.wbsnodeTOJSON(wbs);
					array.add(obj);
				}
			}

			out.print(array);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
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
    	String activityName = ParamUtil.getString(req, "activity_name", StringPool.BLANK);

		PrintWriter out = resp.getWriter();

		JSONObject infoJSON = new JSONObject();

		Milestones milestone = null;

		try {
			// Declare logic
			MilestoneLogic milestoneLogic = new MilestoneLogic(getSettings(req), getResourceBundle(req));

			if (idMilestone != -1) {

				// Logic find
				milestone = milestoneLogic.findById(idMilestone);
			}

			if (milestone != null && milestone.getProject() != null) {

				// Logic delete and create notification
				milestoneLogic.deleteMilestone(milestone, milestone.getProject().getIdProject(), activityName);

				infoJSON = infoDeleted(getResourceBundle(req), "milestone");

				out.print(infoJSON);
			}
			else {

				infoJSON.put("error", getResourceBundle(req).getObject("data_not_found"));

				out.print(infoJSON);
			}
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
    }
    
    /**
     * Update chart Gantt planning
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void planGanttChartJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	PrintWriter out = resp.getWriter();

		Integer idProject		= ParamUtil.getInteger(req, "id");
		Date since				= ParamUtil.getDate(req, "filter_start", getDateFormat(req), new Date());
		Date until				= ParamUtil.getDate(req, "filter_finish", getDateFormat(req), new Date());
		String idsBySorting 	= ParamUtil.getString(req, "idsBySorting");

		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

			JSONObject updateJSON = ChartLogic.consChartGantt(getResourceBundle(req), projectLogic.consProject(idProject),
                    false, false, since, until, idsBySorting, getSettings(req));

			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
    }
    
    /**
     * Generate chart WBS Planning-Scope
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    private void wbsChartJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();
		Integer idProject = ParamUtil.getInteger(req, "id");

		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();

			Project project = projectLogic.consProject(idProject);

			Wbsnode wbsNodeParent = wbsNodeLogic.findFirstWBSnode(project);
			ChartWBS chartWBS = new ChartWBS(wbsNodeParent, false, getResourceBundle(req));

			out.print(chartWBS.generate());
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
    }
    
    /**
     * Save Income (Planning costs)
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveIncomeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	Integer idIncome = ParamUtil.getInteger(req, "income_id", -1);
		Incomes income = null;
		IncomesLogic logic = null;
		PrintWriter out = resp.getWriter();

		try {
			logic = new IncomesLogic();
			income =  logic.findById(idIncome);
			if (income == null) {
				income = new Incomes();
			}

			setIncomeFromRequest(req, income);
			logic.save(income);

			JSONObject incomeJSON = JSONModelUtil.incomeToJSON(getResourceBundle(req), income);
			out.print(incomeJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
    }
    
    /**
     * Save Followup (Planning costs - Cost baseline plan)
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveFollowupJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	Integer idProject 	= ParamUtil.getInteger(req, "id", -1);
    	Date date			= ParamUtil.getDate(req, "date", getDateFormat(req));

		Projectfollowup followup;

		PrintWriter out = resp.getWriter();

		try {

			if (idProject == -1) {
				throw new Exception(getResourceBundle(req).getString("msg.error.data"));
			}

			// Declare logic
			ProjectFollowupLogic projectFollowupLogic 	= new ProjectFollowupLogic();
			WBSNodeLogic wbsNodeLogic 					= new WBSNodeLogic();

			Integer idFollowup = ParamUtil.getInteger(req, "followup_id", -1);

			//followup control
			Projectfollowup followupExist = projectFollowupLogic.findByDate(idFollowup, new Project(idProject), Projectfollowup.FOLLOWUPDATE, date);

			if (followupExist != null) {
				throw new LogicException("msg.error.followup_repeat");
			}

			followup = projectFollowupLogic.consFollowup(idFollowup);
			if (followup == null) {
				followup = new Projectfollowup();
			}
			setFollowupFromRequest(req, followup);
			followup.setProject(new Project(idProject));

			projectFollowupLogic.save(followup);

			// Set settings and globalBudget for POC and EAC
			followup.setSettings(getSettings(req));
			followup.setBudget(wbsNodeLogic.consTotalBudget(new Project(idProject)));

			JSONObject followupJSON = JSONModelUtil.followupToJSON(getResourceBundle(req), followup, null);

			out.print(followupJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
    }
    
	/**
     * Approve Project
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void approveProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Integer idProject 			= ParamUtil.getInteger(req, "id");
		String scopeStatement 		= ParamUtil.getString(req, "scope_statement", StringPool.BLANK);

		boolean error 						= false;
		Project project						= null;
		List<ParamResourceBundle> errors 	= new ArrayList<ParamResourceBundle>();
		ProjectLogic projectLogic 			= new ProjectLogic(getSettings(req), getResourceBundle(req));
		try {
			MilestoneLogic milestoneLogic = new MilestoneLogic();
			ProjectKpiLogic kpiLogic = new ProjectKpiLogic();
			WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();

			project = projectLogic.consProject(idProject);

			List<Projectkpi> kpis = kpiLogic.findByRelation(Projectkpi.PROJECT, project);

			if (kpis != null && !kpis.isEmpty()) {

				int weight = 0;
				for (Projectkpi kpi : kpis) {

					weight += (kpi.getWeight() == null ?0:kpi.getWeight());
				}
				if (weight != 100) { throw new LogicException("msg.error.kpi_total_weight"); }
			}

			project.setScopeStatement(scopeStatement);
			projectLogic.saveAndUpdateDataProject(project);

			errors = wbsNodeLogic.checkErrorsWBS(project);

			Double budget = wbsNodeLogic.consTotalBudget(project);
			if (project.getBac() == null || budget == null || budget.doubleValue()!= project.getBac().doubleValue()) {
				info(StringPool.INFORMATION, req, "msg.info.bac_equals_budget");
			}

			if (milestoneLogic.checkMilestones(project)) {
				errors.add(new ParamResourceBundle("msg.error.milestones_without_activity"));
			}
			if (!errors.isEmpty()) {
				error = true;
			}

		}
		catch (Exception e) {
			error = true;
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}

		if (!error) {
			try {
				projectLogic.approveProject(project, getUser(req));
			}
			catch (Exception e) {
				error = true;
				ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
			}
		}

		if (error) {
			if (!errors.isEmpty()) {
				StringBuilder infoMsg = new StringBuilder();
				for (ParamResourceBundle er : errors) {
					infoMsg.append(er.toString(getResourceBundle(req)) + "<br>");
				}
				req.setAttribute(StringPool.ERROR, infoMsg.toString());
			}

			viewProjectPlanning(req, resp, idProject);
		}
		else {
			forwardServlet(ProjectControlServlet.REFERENCE+"?accion=", req, resp);
		}
	}

	/**
     * Save Cost (Planning costs - Detailed cost plan)
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void saveCostJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	Integer idProject = ParamUtil.getInteger(req, "id", -1);
		Projectcosts costs = null;

		PrintWriter out = resp.getWriter();
		try {
			if (idProject == -1) {
				throw new Exception(getResourceBundle(req).getString("msg.error.project"));
			}
			ProjectCostsLogic projectCostsLogic	= new ProjectCostsLogic();

			costs = new Projectcosts();
			costs.setProject(new Project(idProject));

			costs = projectCostsLogic.saveCosts(costs);

			// Determine what kind of cost detail is
			Integer costType = ParamUtil.getInteger(req, "cost_type", -1);

			JSONObject costJSON = new JSONObject();
			if (costType == Constants.COST_TYPE_DIRECT) {
				Directcosts cost = saveDirectCostFromRequest(req, costs);

				costJSON.put("id", cost.getIdDirectCosts());
				costJSON.put("actual", cost.getActual());
				costJSON.put("planned", cost.getPlanned());
				costJSON.put("desc", cost.getDescription());
				costJSON.put("budgetaccount", cost.getBudgetaccounts().getIdBudgetAccount());
			}
			else if (costType == Constants.COST_TYPE_EXPENSE) {
				Expenses cost = saveExpenseCostFromRequest(req, costs);

				costJSON.put("id", cost.getIdExpense());
				costJSON.put("actual", cost.getActual());
				costJSON.put("planned", cost.getPlanned());
				costJSON.put("desc", cost.getDescription());
				costJSON.put("budgetaccount", cost.getBudgetaccounts().getIdBudgetAccount());
			}
			else {
				throw new Exception(getResourceBundle(req).getString("msg.error.cost_type"));
			}

			costJSON.put("type", costType);

			out.print(costJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    /**
     * Save IWO (Planning costs - IWO's)
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveIwoJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	Integer idProject = ParamUtil.getInteger(req, "id", -1);
		Projectcosts costs = null;

		PrintWriter out = resp.getWriter();

		try {
			if (idProject == -1) {
				throw new Exception(getResourceBundle(req).getString("msg.error.project"));
			}
			ProjectCostsLogic projectCostsLogic	= new ProjectCostsLogic();

			costs = new Projectcosts();
			costs.setProject(new Project(idProject));

			costs = projectCostsLogic.saveCosts(costs);

			Iwo iwo = saveIwoFromRequest(req, costs);

			JSONObject costJSON = new JSONObject();
			costJSON.put("id", iwo.getIdIwo());
			costJSON.put("planned", iwo.getPlanned());
			costJSON.put("desc", iwo.getDescription());

			out.print(costJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
    }
    /**
     * Update project-costs chart
     * @param req
     * @param resp
     * @throws IOException
     */
    private void updateProjectCostChartJX(HttpServletRequest req,
    		HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();

		Integer idProject = ParamUtil.getInteger(req, "id");

		JSONArray values = new JSONArray();

		JSONArray value = new JSONArray();
		value.add(0);
		value.add(0);
		values.add(value);

		try {
			ProjectFollowupLogic projectFollowupLogic 	= new ProjectFollowupLogic();
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));

			Project project = projectLogic.consProject(idProject);
			List<Projectfollowup> projectFollowups = projectFollowupLogic.consFollowups(project);

			for (Projectfollowup item : projectFollowups) {

				if (item.getPv() != null) {

					Integer daysToDate = item.getDaysToDate();

					if (daysToDate != null) {
						value = new JSONArray();
						value.add(daysToDate);
						value.add(item.getPv());

						values.add(value);
					}
				}
			}
			out.print(values);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e); }
		finally { out.close(); }
    }
   
    
    /**
     * Generate chart finance
     * @param req
     * @param resp
     * @throws IOException
     */
    private void financeChartJX(HttpServletRequest req,
    		HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();

		Integer idProject = ParamUtil.getInteger(req, "id");

		List<Incomes> incomes 	= null;
		JSONObject updateJSON 	= new JSONObject();

		try {
			JSONArray values = new JSONArray();
			IncomesLogic incomesLogic = new IncomesLogic();

			incomes = incomesLogic.consIncomes(idProject, Incomes.PLANNEDBILLDATE);

			Double afterValue = 0.0;

			for (Incomes item : incomes) {

				for(int i=0;i<2;i++){
					JSONArray value = new JSONArray();
					value.add(item.getPlanDaysToDate());
					value.add(i == 0 ? afterValue : item.getPlannedBillAmmount() + afterValue);

					/*
					//pointLabel
					String pointLabel = value.get(0).toString();
					pointLabel = pointLabel.concat(StringPool.COMMA_AND_SPACE);
					pointLabel = pointLabel.concat(value.get(1).toString());
					value.add(pointLabel);
					*/

					values.add(value);
				}
				afterValue = (item.getPlannedBillAmmount() + afterValue) ;
			}

			updateJSON.put("values", values);
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e); }
		finally { out.close(); }
    }	


    
    /**
     * Update project-costs chart P&L
     * @param req
     * @param resp
     * @throws IOException
     */
    private void updatePlChartJX(HttpServletRequest req,
    		HttpServletResponse resp) throws IOException {

		PrintWriter out = resp.getWriter();


		try {
			Integer idProject = ParamUtil.getInteger(req, "id");

			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

			Project project = projectLogic.consProject(idProject);

			PlChartInfo plChartInfo = ChartLogic.consInfoPlChart(project);
			double sumIwos			= plChartInfo.getSumIwos();
			double sumExpenses		= plChartInfo.getSumExpenses();
			double sumDirectCost	= plChartInfo.getSumDirectCost();
			double dm				= plChartInfo.getDirectMargin();
			double dmPer			= Math.round(plChartInfo.getDirectMarginPercent());
			double tcv				= plChartInfo.getTcv();
			double netIncome		= plChartInfo.getCalculatedNetIncome();

			double divDouble	= new Double(1000);

			JSONObject updateJSON = new JSONObject();
			if (netIncome != (project.getNetIncome() == null?0:project.getNetIncome())) {
				updateJSON.put("warning", true);
			}

			updateJSON.put("s1", ChartUtil.createSeriesPL(
                    Double.toString(tcv / divDouble),
                    Double.toString(netIncome / divDouble),
                    StringPool.BLANK, 1));

			updateJSON.put("s2",ChartUtil.createSeriesPL(
					Double.toString((tcv-sumExpenses)/divDouble),
					Double.toString((tcv-sumExpenses-sumIwos)/divDouble),
					Double.toString((netIncome-sumDirectCost)/divDouble),
					2));

			updateJSON.put("s3",ChartUtil.createSeriesPL(
					Double.toString(sumDirectCost/divDouble),
					StringPool.BLANK, StringPool.BLANK, 3));

			updateJSON.put("s4",ChartUtil.createSeriesPL(
					Double.toString(sumExpenses/divDouble),
					StringPool.BLANK, StringPool.BLANK, 4));

			updateJSON.put("s5",ChartUtil.createSeriesPL(
					Double.toString(sumIwos/divDouble),
					StringPool.BLANK, StringPool.BLANK, 5));

			updateJSON.put("s6",ChartUtil.createSeriesPL(
					Double.toString(dm/divDouble),
					StringPool.BLANK, StringPool.BLANK, 6));

			//JQPLOT
			final  JSONArray colors = new JSONArray();
			colors.add("#BDBDBD");
			colors.add("#FFFFFF");
			colors.add("#B40404");
			colors.add("#688BB4");
			colors.add("#FFFF00");
			colors.add("#7E7E7E");
			updateJSON.put("colors", colors);

			final  JSONArray seriesParameters = new JSONArray();
			seriesParameters.add("{}");
			seriesParameters.add("{show:false,shadow:false}");
			seriesParameters.add("{}");
			seriesParameters.add("{}");
			seriesParameters.add("{}");
			seriesParameters.add("{}");
			updateJSON.put("seriesParameters", seriesParameters);

			final JSONArray categories = new JSONArray();
			categories.add(getResourceBundle(req).getString("tcv"));
			categories.add(getResourceBundle(req).getString("project_pl.expenses"));
			categories.add(getResourceBundle(req).getString("iwo"));
			categories.add(getResourceBundle(req).getString("project_pl.net_income"));
			categories.add(getResourceBundle(req).getString("project_pl.direct_costs"));
			categories.add(getResourceBundle(req).getString("project_pl.dm")+" "+dmPer+"%");
			updateJSON.put("categories", categories);

			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e); }
		finally { out.close(); }
    }
    
    /**
     * Check for release resource
     * @param req
     * @param resp
     * @throws IOException
     */
    private void checkReleasedResourceJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

    	int idResource		= ParamUtil.getInteger(req, "idResource",-1);

		PrintWriter out = resp.getWriter();

    	try {


    		TeamMemberLogic memberLogic = new TeamMemberLogic();
    		TimesheetLogic timesheetLogic = new TimesheetLogic();

    		// Find info of resource
    		Teammember member = memberLogic.findById(idResource, DataUtil.toList(Teammember.PROJECTACTIVITY));

    		JSONObject infoJSON = new JSONObject();

    		if (timesheetLogic.hoursInState(member, Constants.TIMESTATUS_APP1, Constants.TIMESTATUS_APP2)) {

    			infoJSON.put(StringPool.ERROR, getResourceBundle(req).getString("msg.error.hours_pending"));
    		}
    		// Check if resource have hour in APP0
    		else if (timesheetLogic.hoursInState(member, Constants.TIMESTATUS_APP0, Constants.TIMESTATUS_APP0)) {

    			infoJSON.put("showWarning", "true");
    		}


    		out.print(infoJSON);
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}
    
	/**
     * Change Resource to Released
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void releasedResourceJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		// Declare writer for send info to client by AJAX
		PrintWriter out = resp.getWriter();

    	try {

    		// Collect parameters
    		int idResource		= ParamUtil.getInteger(req, "idResource",-1);
    		String commentsPm	= ParamUtil.getString(req, "commentsPm");

    		// Declare logic
    		TeamMemberLogic memberLogic = new TeamMemberLogic();

    		// Change to Released
    		memberLogic.changeToReleased(idResource, commentsPm);

    		// Send response
    		out.print(info(getResourceBundle(req), StringPool.SUCCESS, "resource.is_released", null));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Update Threshold of project
     * @param req
     * @param resp
     * @throws IOException
     */
    private void updateThresholdJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

    	int idProject	= ParamUtil.getInteger(req, Project.IDPROJECT);
    	double lower	= ParamUtil.getDouble(req, Project.LOWERTHRESHOLD, 0.0);
    	double upper	= ParamUtil.getDouble(req, Project.UPPERTHRESHOLD, 0.0);

		PrintWriter out = resp.getWriter();

		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

			Project proj = projectLogic.consProject(idProject);
			proj.setLowerThreshold(lower);
			proj.setUpperThreshold(upper);

			projectLogic.save(proj);

			projectLogic.updateKPIStatus(idProject);

			out.print(info(getResourceBundle(req), StringPool.SUCCESS, "msg.info.saved_upper_and_lower_threshold", null));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Update Staffing Table
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void updateStaffingTableJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

    	int idProject	        = ParamUtil.getInteger(req, Project.IDPROJECT);
    	Date since		        = ParamUtil.getDate(req, "since", getDateFormat(req));
    	Date until		        = ParamUtil.getDate(req, "until", getDateFormat(req));
    	boolean showDisabled    = ParamUtil.getBoolean(req, Configurations.FILTER_DISABLED, false);

    	List<TeamMembersFTEs> ftEs 	= null;
		List<String> listDates		= new ArrayList<String>();

    	try {
    		TeamMemberLogic teamMemberLogic = new TeamMemberLogic();

    		SimpleDateFormat dfYear = new SimpleDateFormat("yy");

    		Calendar sinceCal		= DateUtil.getCalendar();
			Calendar untilCal		= DateUtil.getCalendar();

    		sinceCal.setTime(DateUtil.getFirstWeekDay(since));
			untilCal.setTime(DateUtil.getLastWeekDay(until));

			List<Teammember> teammembers = teamMemberLogic.consStaffinFtes(new Project(idProject), since, until, showDisabled);

			ftEs = generateFTEsMembersByProject(teammembers, sinceCal.getTime(), untilCal.getTime());

			while (!sinceCal.after(untilCal)) {

				int sinceDay = sinceCal.get(Calendar.DAY_OF_MONTH);
				Calendar calWeek = DateUtil.getLastWeekDay(sinceCal);
				int untilDay = calWeek.get(Calendar.DAY_OF_MONTH);

				listDates.add(sinceDay+"-"+untilDay +" "+getResourceBundle(req).getString("month.min_"+(calWeek.get(Calendar.MONTH)+1))+" "+dfYear.format(calWeek.getTime()));

				sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
			}

            // Find configurations
            HashMap<String, String> configurations = RequestUtil.getConfigurationValues(
                    req, Configurations.FILTER_DISABLED);

            // Save configuration
            ConfigurationLogic configurationLogic = new ConfigurationLogic();
            configurationLogic.saveConfigurations(getUser(req), configurations, Configurations.TYPE_CAPACITY_PLANNING);

    	}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

		req.setAttribute("listDates", listDates);
		req.setAttribute("ftEs", ftEs);

		forward("/project/common/staffing_table.ajax.jsp", req, resp);
	}
    
    /**
	 * Generate Team Members Ftes by Project
	 * @param teammembers
	 * @param sinceDate
	 * @param untilDate
	 * @return
	 * @throws LogicException
	 */
	private List<TeamMembersFTEs> generateFTEsMembersByProject(List<Teammember> teammembers, Date sinceDate, Date untilDate) throws Exception {

		List<TeamMembersFTEs> listMembers 	= new ArrayList<TeamMembersFTEs>();
		Calendar sinceCal			= DateUtil.getCalendar();
		Calendar untilCal			= DateUtil.getCalendar();
		int idEmploye				= -1;
		TeamMembersFTEs memberFtes	= null;
		int[] listFTES				= null;

		if (sinceDate != null || untilDate != null) {
			sinceCal.setTime(sinceDate);
			untilCal.setTime(untilDate);

			int weeks = 0;
			while (!sinceCal.after(untilCal)) {
				weeks++;
				sinceCal.add(Calendar.DAY_OF_MONTH, +7);
			}

			for (Teammember member : teammembers) {

				if (memberFtes == null || !(idEmploye == member.getEmployee().getIdEmployee())) {

					if (memberFtes != null) {
						memberFtes.setFtes(listFTES);
						listMembers.add(memberFtes);
					}

					listFTES	= new int[weeks];
					memberFtes	= new TeamMembersFTEs(member);
					idEmploye	= member.getEmployee().getIdEmployee();
				}

				int i = 0;
				sinceCal.setTime(sinceDate);

				Calendar dayCal		= null;
				Calendar weekCal	= null;

				while (!sinceCal.after(untilCal)) {

					dayCal	= DateUtil.getCalendar();
					dayCal.setTime(sinceCal.getTime());
					weekCal = DateUtil.getLastWeekDay(dayCal);

					int fte			= 0;
					int workDays	= 5;

					while (!dayCal.after(weekCal)) {

						if (DateUtil.between(member.getDateIn(), member.getDateOut(), dayCal.getTime()) && !DateUtil.isWeekend(dayCal)) {
							fte += (member.getFte() == null?0:member.getFte());
						}

						dayCal.add(Calendar.DAY_OF_MONTH, 1);
					}

					listFTES[i] +=  (workDays > 0?fte/workDays:0);

					i++;
					sinceCal.add(Calendar.DAY_OF_MONTH, +7);
				}
			}
			if (memberFtes != null) {
				memberFtes.setFtes(listFTES);
				listMembers.add(memberFtes);
			}
		}
		return listMembers;
	}

    /**
     * Delete Project Kpi
     * @param req
     * @param resp
     * @throws IOException
     */
    private void deleteKpiJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	int idProjectKPI	= ParamUtil.getInteger(req, Projectkpi.IDPROJECTKPI,-1);
    	int idProject		= ParamUtil.getInteger(req, Project.IDPROJECT);

		PrintWriter out = resp.getWriter();

		try {
			ProjectKpiLogic kpiLogic = new ProjectKpiLogic();
			kpiLogic.delete(kpiLogic.findById(idProjectKPI));

			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			projectLogic.updateKPIStatus(idProject);

			out.print(infoDeleted(getResourceBundle(req), "kpi"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Save Project KPI
     * @param req
     * @param resp
     * @throws IOException 
     */
	private void saveKpiJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		int idProject			= ParamUtil.getInteger(req, Project.IDPROJECT);
		int idProjectKPI		= ParamUtil.getInteger(req, Projectkpi.IDPROJECTKPI,-1);
		int idMetricKpi			= ParamUtil.getInteger(req, Metrickpi.IDMETRICKPI,-1);
		double upperThreshold	= ParamUtil.getDouble(req, Projectkpi.UPPERTHRESHOLD,0.0);
		double lowerThreshold	= ParamUtil.getDouble(req, Projectkpi.LOWERTHRESHOLD,0.0);
		double weight			= ParamUtil.getDouble(req, Projectkpi.WEIGHT,0.0);
		Boolean aggregateKpi	= ParamUtil.getBoolean(req, Projectkpi.AGGREGATEKPI,false);
		String specificKpi		= ParamUtil.getString(req, Projectkpi.SPECIFICKPI, null);

		PrintWriter out = resp.getWriter();

		try {
			ProjectLogic projectLogic 		= new ProjectLogic(getSettings(req), getResourceBundle(req));
			ProjectKpiLogic projectKpiLogic = new ProjectKpiLogic();

			Projectkpi projKpi 				= null;
			JSONObject returnJSON 			= null;

			if (projectKpiLogic.metricExist(new Project(idProject), idProjectKPI, new Metrickpi(idMetricKpi))) {
				throw new LogicException("msg.error.metric_is_in_use");
			}

			if (idProjectKPI == -1) {
				projKpi = new Projectkpi();
				projKpi.setProject(new Project(idProject));

				returnJSON = infoCreated(getResourceBundle(req), returnJSON, "kpi");
			}
			else {
				projKpi = projectKpiLogic.findById(idProjectKPI);

				returnJSON = infoUpdated(getResourceBundle(req), returnJSON, "kpi");
			}

			Metrickpi metrickpi = null;
			if (idMetricKpi != -1){
				metrickpi = new Metrickpi(idMetricKpi);
				specificKpi = null;
			}

			projKpi.setSpecificKpi(specificKpi);
			projKpi.setMetrickpi(metrickpi);
			projKpi.setUpperThreshold(upperThreshold);
			projKpi.setLowerThreshold(lowerThreshold);
			projKpi.setWeight(weight);
			projKpi.setAggregateKpi(aggregateKpi);

			projKpi = projectKpiLogic.saveProjectKpi(projKpi, getUser(req), Historickpi.UpdatedType.MANUAL);

			returnJSON.put(Projectkpi.IDPROJECTKPI, projKpi.getIdProjectKpi());

			projectLogic.updateKPIStatus(idProject);

			out.print(returnJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Update Team Calendar
     * @param req
     * @param resp
     * @throws IOException 
	 * @throws ServletException
     */
    private void updateTeamCalendarJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {

    	int idProject	= ParamUtil.getInteger(req, Project.IDPROJECT);
    	Date since		= ParamUtil.getDate(req, "since", getDateFormat(req));
    	Date until		= ParamUtil.getDate(req, "until", getDateFormat(req));

    	try {
    		ProjectcalendarLogic projectcalendarLogic 	= new ProjectcalendarLogic();
    		TeamMemberLogic teamMemberLogic				= new TeamMemberLogic();

    		Projectcalendar calendar = projectcalendarLogic.consCalendarByProject(new Project(idProject));

    		List<Teammember> members = teamMemberLogic.consStaffinFtes(new Project(idProject), since, until, true);

    		List<TeamCalendar> teamCalendars = new ArrayList<TeamCalendar>();

    		Contact contact				= null;
    		TeamCalendar teamCalendar	= null;

    		for (Teammember member : members) {

    			if (contact == null || contact.getIdContact() != member.getEmployee().getContact().getIdContact()) {

    				if (teamCalendar != null) { teamCalendars.add(teamCalendar); }

    				teamCalendar = new TeamCalendar(member.getEmployee());
    				contact		 = member.getEmployee().getContact();
    			}

    			Calendar sinceCal = DateUtil.getCalendar();
        		sinceCal.setTime(member.getDateIn());

        		Calendar untilCal = DateUtil.getCalendar();
        		untilCal.setTime(member.getDateOut());

        		while (!sinceCal.after(untilCal)) {

        			teamCalendar.addWork(sinceCal.getTime());
        			sinceCal.add(Calendar.DAY_OF_MONTH, 1);
        		}
    		}
    		if (teamCalendar != null) { teamCalendars.add(teamCalendar); }

    		List<String> dates = new ArrayList<String>();

    		boolean isCreateDates = false;

    		for (TeamCalendar teamCal : teamCalendars) {

    			JSONObject teamJSON = new JSONObject();
    			teamJSON.put("name", teamCal.getName());

	    		Calendar sinceCal = DateUtil.getCalendar();
	    		sinceCal.setTime(since);

	    		Calendar untilCal = DateUtil.getCalendar();
	    		untilCal.setTime(until);

	    		while (!sinceCal.after(untilCal)) {

	    			if (!isCreateDates) {
	    				dates.add(sinceCal.get(Calendar.DAY_OF_MONTH)+"-"+(sinceCal.get(Calendar.MONTH)+1));
	    			}

	    			boolean isException = false;
	    			if (calendar != null) {
		    			for (Projectcalendarexceptions exception : calendar.getProjectcalendarexceptionses()) {

		    				if (DateUtil.between(exception.getStartDate(), exception.getFinishDate(),sinceCal.getTime())) {

		    					teamCal.addValue("exceptionDay");
		    					isException = true;
		    					break;
		    				}
		    			}
		    			if (!isException) {
		    				if (calendar.getCalendarbase() != null) {
				    			for (Calendarbaseexceptions exception : calendar.getCalendarbase().getCalendarbaseexceptionses()) {

				    				if (DateUtil.between(exception.getStartDate(), exception.getFinishDate(),sinceCal.getTime())) {

				    					teamCal.addValue("exceptionDay");
				    					isException = true;
				    					break;
				    				}
				    			}
		    				}
		    			}
		    			if (!isException) {

		    				CalendarbaseexceptionsLogic exceptionsLogic = new CalendarbaseexceptionsLogic();
		    				List<Calendarbaseexceptions> exceptions = exceptionsLogic.findByEmployee(teamCal.getIdEmployee());

			    			for (Calendarbaseexceptions exception : exceptions) {

			    				if (DateUtil.between(exception.getStartDate(), exception.getFinishDate(),sinceCal.getTime())) {

			    					teamCal.addValue("exceptionDay");
			    					isException = true;
			    					break;
			    				}
			    			}
		    			}
	    			}

	    			if (!isException) {

	    				if (DateUtil.isWeekend(sinceCal)) { teamCal.addValue("nonWorkingDay"); }
	    				else if (teamCal.getWork().contains(sinceCal.getTime())) { teamCal.addValue("workDay"); }
	    				else { teamCal.addValue("notWorkDay"); }
	    			}

	    			sinceCal.add(Calendar.DAY_OF_MONTH, 1);
	    		}

	    		isCreateDates = true;
    		}

    		req.setAttribute("dates", dates);
    		req.setAttribute("teamCalendars", teamCalendars);
    	}
    	catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

    	req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
    	forward("/project/plan/plan_team_calendar.ajax.jsp", req, resp);
	}

	/**
     * Find Members
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void findMemberJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	String idJobCategories	 	= ParamUtil.getString(req, Jobcategory.IDJOBCATEGORY, null);
    	String idSkills	 			= ParamUtil.getString(req, Skill.IDSKILL, null);
    	int idPerfOrg 	 			= ParamUtil.getInteger(req, Performingorg.IDPERFORG, -1);
    	int idResourcePool 			= ParamUtil.getInteger(req, "idResourcePool", -1);
    	int idSeller 				= ParamUtil.getInteger(req, "idSeller", -1);

    	PrintWriter out = resp.getWriter();

		try {
	    	EmployeeLogic employeeLogic = new EmployeeLogic();

	    	List<Employee> employees = employeeLogic.searchEmployees(Constants.ROLE_RESOURCE, idJobCategories, new Performingorg(idPerfOrg), getCompany(req), idResourcePool, idSkills, idSeller);

			JSONArray employeesJSONArray = new JSONArray();

			for (Employee employee: employees) {

				JSONObject memberJSON = JSONModelUtil.employeeToJSON(getResourceBundle(req), employee);

				employeesJSONArray.add(memberJSON);
			}

			out.print(employeesJSONArray);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
     * Save Team Member
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveMemberJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();

		Integer idProject		= ParamUtil.getInteger(req, "id", -1);
		Integer idTeamMember 	= ParamUtil.getInteger(req, "member_id", -1);

		Teammember member = null;

		try {
			TeamMemberLogic teamMemberLogic	= new TeamMemberLogic();

			if (idTeamMember == -1) {
				member = new Teammember();
			}
			else {
				member = teamMemberLogic.consTeamMember(idTeamMember);
			}

			member.setStatus(Constants.RESOURCE_PRE_ASSIGNED);

			member = setMemberFromRequest(req,member);

			member = teamMemberLogic.saveResource(member);

			// Create notification to RM
			boolean notificationToRM = true;

			// Approbation automatic by setting
			//
			if (Constants.TYPE_APPROBATION_AUTOMATIC.equals(SettingUtil.getString(getSettings(req),
                    Settings.SETTING_TYPE_APPROBATION_RESOURCE, Settings.SETTING_TYPE_APPROBATION_RESOURCE_DEFAULT))) {

				teamMemberLogic.approveTeamMember(getSettings(req), member.getIdTeamMember(), getSettings(req).get("setting.type_approbation_automatic"));
				notificationToRM = false;
			}
			else if (Constants.TYPE_APPROBATION_AUTOMATIC_PM.equals(SettingUtil.getString(getSettings(req),
					Settings.SETTING_TYPE_APPROBATION_RESOURCE, Settings.SETTING_TYPE_APPROBATION_RESOURCE_DEFAULT))) {

				EmployeeLogic employeeLogic = new EmployeeLogic();

				// Check if resource is PM of project
				if (employeeLogic.checkResourceIsPM(member.getEmployee(), new Project(idProject))) {

					teamMemberLogic.approveTeamMember(getSettings(req), member.getIdTeamMember(), getSettings(req).get("setting.type_approbation_automatic_pm"));
					notificationToRM = false;
				}
			}

			// Reload Member
			member = teamMemberLogic.consTeamMember(member);

			JSONObject memberJSON = JSONModelUtil.memberToJSON(getResourceBundle(req), member);

			if (notificationToRM) {

				// Consult resource managers
				//
				EmployeeLogic employeeLogic = new EmployeeLogic();

				List<Employee> resourceManagers = employeeLogic.consResourceManagers(member.getEmployee());

				// To
				//
				List<Contact> contacts = new ArrayList<Contact>();

				if (ValidateUtil.isNotNull(resourceManagers)) {

					for (Employee resourceManager : resourceManagers) {

						if (!contacts.contains(resourceManager.getContact())) {
							contacts.add(resourceManager.getContact());
						}
					}
				}

				try {

					if (SettingUtil.getBoolean(getSettings(req), NotificationType.TEAMMEMBER_ASSIGNATION)) {

						// Find project data
						ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
						Project project = projectLogic.consProject(idProject);

						// Create body
						ParamResourceBundle bundle = new ParamResourceBundle("msg.mail.bodySaveMember", getUser(req).getContact().getFullName(),
								member.getEmployee().getContact().getFullName(), project.getProjectName());

						// Add notification
						NotificationLogic notificationLogic = new NotificationLogic();
						notificationLogic.createNotification(
								getResourceBundle(req).getString("msg.mail.subjectSaveMember"),
								bundle.getMessage(getResourceBundle(req)),
								NotificationType.TEAMMEMBER_ASSIGNATION,
								contacts);
					}

				}
				catch (Exception e) {
					ExceptionUtil.sendToLogger(LOGGER, e, null);
				}
			}

			if (idTeamMember == -1) { memberJSON = info(getResourceBundle(req), StringPool.SUCCESS, "msg.info.preassign", memberJSON, "team_member");}
			else { memberJSON = infoUpdated(getResourceBundle(req), memberJSON, "team_member"); }

			out.print(memberJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
    
    /**
     * Update end date
     * @param req
     * @param resp
     * @throws IOException
     */
    private void updateMemberJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	// Declare writer for send data to client by AJAX
    	PrintWriter out = resp.getWriter();

		try {
			// Collect parameters
			Integer idProject		= ParamUtil.getInteger(req, "id", -1);
			Integer idTeamMember 	= ParamUtil.getInteger(req, "member_id", -1);
			Date dateIn				= ParamUtil.getDate(req, "member_datein", getDateFormat(req), null);
			Date dateOut			= ParamUtil.getDate(req, "member_dateout", getDateFormat(req), null);
            int fte					= ParamUtil.getInteger(req, "member_fte", 0);

			TeamMemberLogic teamMemberLogic	= new TeamMemberLogic(getResourceBundle(req));

            Teammember member = teamMemberLogic.findById(idTeamMember);

            // Data for notification
            Date oldDateIn  = member.getDateIn();
            Date oldDateOut = member.getDateOut();
            Integer oldFte  = member.getFte();
            Integer newFte  = SettingUtil.getBoolean(getSettings(req), Settings.SettingType.PRELOAD_HOURS_WITH_WORKLOAD)? member.getFte():fte;

			// Update team member and remove time sheets out of date
			member = teamMemberLogic.updateTeamMember(getSettings(req), idTeamMember, dateIn, dateOut, fte);

			// Prepare data for send to front
			JSONObject memberJSON = JSONModelUtil.memberToJSON(getResourceBundle(req), member);

            try {

                if (SettingUtil.getBoolean(getSettings(req), NotificationType.TEAMMEMBER_ASSIGNATION_UPDATE)) {

                    // Consult resource managers
                    //
                    EmployeeLogic employeeLogic = new EmployeeLogic();

                    List<Employee> resourceManagers = employeeLogic.consResourceManagers(member.getEmployee());

                    // To
                    //
                    List<Contact> contacts = new ArrayList<Contact>();

                    // If member is assigned is notified
                    if (Constants.RESOURCE_ASSIGNED.equals(member.getStatus())) {
                        contacts.add(member.getEmployee().getContact());
                    }

                    if (ValidateUtil.isNotNull(resourceManagers)) {

                        for (Employee resourceManager : resourceManagers) {

                            if (!contacts.contains(resourceManager.getContact())) {
                                contacts.add(resourceManager.getContact());
                            }
                        }
                    }

                    // Find project data
                    ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
                    Project project = projectLogic.consProject(idProject);

                    // Create subject
                    ParamResourceBundle bundleSubject = new ParamResourceBundle("msg.mail.subjectUpdateMember",
                            member.getEmployee().getContact().getFullName(),
                            project.getProjectName(),
                            getUser(req).getContact().getFullName());

                    // Create body
                    ParamResourceBundle bundleBody = new ParamResourceBundle("msg.mail.bodyUpdateMember",
                            member.getEmployee().getContact().getFullName(),
                            project.getProjectName(),
                            getUser(req).getContact().getFullName(),
                            formatDate(req, oldDateIn),
                            formatDate(req, oldDateOut),
                            oldFte,
                            formatDate(req, dateIn),
                            formatDate(req, dateOut),
                            newFte);

                    // Add notification
                    NotificationLogic notificationLogic = new NotificationLogic();
                    notificationLogic.createNotification(
                            bundleSubject.getMessage(getResourceBundle(req)),
                            bundleBody.getMessage(getResourceBundle(req)),
                            NotificationType.TEAMMEMBER_ASSIGNATION_UPDATE,
                            contacts);
                }
            }
            //TODO - 14/01/2015 - jordi.ripoll - el catch estaba puesto porque no se puede crear la notificacion en el logic
            // da algun problema hibernate, mirar el notificationtype el resto de casos
            catch (Exception e) {
                ExceptionUtil.sendToLogger(LOGGER, e, null);
            }

			if (idTeamMember == -1) { memberJSON = infoCreated(getResourceBundle(req), memberJSON, "team_member"); }
			else { memberJSON = infoUpdated(getResourceBundle(req), memberJSON, "team_member"); }

			out.print(memberJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    /**
     * Delete Calendar Exception
     * @param req
     * @param resp
     * @throws IOException 
     */
	private void deleteCalendarExceptionJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		PrintWriter out			= resp.getWriter();

		int idProjectCalendarException = ParamUtil.getInteger(req, Projectcalendarexceptions.IDPROJECTCALENDAREXCEPTION,-1);

		try {
			ProjectCalendarExceptionsLogic projectCalendarExceptionsLogic = new ProjectCalendarExceptionsLogic();
			projectCalendarExceptionsLogic.delete(new Projectcalendarexceptions(idProjectCalendarException));

			out.print(infoDeleted(getResourceBundle(req), "calendar.exceptions"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}


	/**
	 * Save Project Calendar Exception
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void saveCalendarExceptionJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		int idProject			= ParamUtil.getInteger(req, Project.IDPROJECT, -1);
		int idProjectException	= ParamUtil.getInteger(req, "idExceptionWork", -1);
		Date startDate 			= ParamUtil.getDate(req, "calendar_start", getDateFormat(req));
		Date finishDate			= ParamUtil.getDate(req, "calendar_end", getDateFormat(req));
		String description		= ParamUtil.getString(req, "calendar_name");

		PrintWriter out	= resp.getWriter();

		try {
			JSONObject outJSON 							= null;
			ProjectcalendarLogic projectcalendarLogic 	= new ProjectcalendarLogic();
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));

			Projectcalendar calendar = projectcalendarLogic.consCalendarByProject(new Project(idProject));

			if (calendar == null) {
				calendar = new Projectcalendar();
				calendar = projectcalendarLogic.save(calendar);

				Project proj = projectLogic.consProject(idProject);
				proj.setProjectcalendar(calendar);

				projectLogic.save(proj);

				outJSON = infoCreated(getResourceBundle(req), outJSON, "project.calendar");
			}


	    	Projectcalendarexceptions exception 							= null;
	    	ProjectCalendarExceptionsLogic projectCalendarExceptionsLogic 	= new ProjectCalendarExceptionsLogic();

	    	if (idProjectException != -1) {

	    		exception = projectCalendarExceptionsLogic.findById(idProjectException);
	    		outJSON = infoUpdated(getResourceBundle(req), outJSON, "calendar.exceptions");
	    	}
	    	else {

	    		exception = new Projectcalendarexceptions();
	    		exception.setProjectcalendar(calendar);
	    		outJSON = infoCreated(getResourceBundle(req), outJSON, "calendar.exceptions");
	    	}

	    	exception.setStartDate(startDate);
    		exception.setFinishDate(finishDate);
    		exception.setDescription(description);

			exception = projectCalendarExceptionsLogic.save(exception);

			outJSON.put(Projectcalendarexceptions.IDPROJECTCALENDAREXCEPTION, exception.getIdProjectCalendarException());
			out.print(outJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

    /**
     * Save Project Calendar
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void saveCalendarJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	Integer idProject		= ParamUtil.getInteger(req, "idProject",-1);
    	Integer weekStart 		= ParamUtil.getInteger(req, "weekStart",1);
    	Integer fiscalYearStart = ParamUtil.getInteger(req, "fiscalYearStart",1);
    	double startTime1 		= ParamUtil.getDouble(req, "startTime1");
    	double startTime2 		= ParamUtil.getDouble(req, "startTime2");
    	double endTime1 		= ParamUtil.getDouble(req, "endTime1");
    	double endTime2 		= ParamUtil.getDouble(req, "endTime2");
    	double hoursDay			= ParamUtil.getDouble(req, "hoursDay");
    	double hoursWeek 		= ParamUtil.getDouble(req, "hoursWeek");
    	Integer daysMonth 		= ParamUtil.getInteger(req, "daysMonth");

    	PrintWriter out			= resp.getWriter();

		try {
			JSONObject outJSON 							= null;
			ProjectcalendarLogic projectcalendarLogic 	= new ProjectcalendarLogic();
			ProjectLogic projectLogic					= new ProjectLogic(getSettings(req), getResourceBundle(req));

			Projectcalendar calendar = projectcalendarLogic.consCalendarByProject(new Project(idProject));

			if (calendar == null) {
				calendar = new Projectcalendar();
				outJSON = infoCreated(getResourceBundle(req), outJSON, "project.calendar");
			}

			calendar.setWeekStart((weekStart == -1 ? null : weekStart));
			calendar.setFiscalYearStart((fiscalYearStart == -1 ? null : fiscalYearStart));
			calendar.setDaysMonth((daysMonth == -1 ? null : daysMonth));
			calendar.setStartTime1((startTime1 == -1 ? null : startTime1));
			calendar.setStartTime2((startTime2 == -1 ? null : startTime2));
			calendar.setEndTime1((endTime1 == -1 ? null : endTime1));
			calendar.setEndTime2((endTime2 == -1 ? null : endTime2));
			calendar.setHoursDay((hoursDay == -1 ? null : hoursDay));
			calendar.setHoursWeek((hoursWeek == -1 ? null : hoursWeek));

			calendar = projectcalendarLogic.save(calendar);

			if (outJSON != null) {
				Project proj = projectLogic.consProject(idProject);
				proj.setProjectcalendar(calendar);

				projectLogic.save(proj);
			}
			else {

				outJSON = infoUpdated(getResourceBundle(req), outJSON, "project.calendar");
			}


			out.print(outJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

    /**
     * Change Calendar Base
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void changeCalendarBase(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

    	int idProject		= ParamUtil.getInteger(req, "id");
    	int idCalendarBase	= ParamUtil.getInteger(req, Calendarbase.IDCALENDARBASE, -1);

    	try {
    		ProjectcalendarLogic projectcalendarLogic 	= new ProjectcalendarLogic();
    		ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));

    		List<String> joins = new ArrayList<String>();
			joins.add(Project.PROJECTCALENDAR);

			Project project = projectLogic.consProject(new Project(idProject), joins);

    		Projectcalendar projCalendar = project.getProjectcalendar();

    		if (projCalendar == null && idCalendarBase != -1) {

    			projCalendar = new Projectcalendar();
    			CalendarBaseLogic calendarBaseLogic = new CalendarBaseLogic();

    			Calendarbase base = calendarBaseLogic.consCalendarBase(new Calendarbase(idCalendarBase));
    			projCalendar.setDaysMonth(base.getDaysMonth());
    			projCalendar.setEndTime1(base.getEndTime1());
    			projCalendar.setEndTime2(base.getEndTime2());
    			projCalendar.setFiscalYearStart(base.getFiscalYearStart());
    			projCalendar.setHoursDay(base.getHoursDay());
    			projCalendar.setHoursWeek(base.getHoursWeek());
    			projCalendar.setStartTime1(base.getStartTime1());
    			projCalendar.setStartTime2(base.getStartTime2());
    			projCalendar.setWeekStart(base.getWeekStart());

    			projCalendar.setCalendarbase(new Calendarbase(idCalendarBase));
    			projCalendar = projectcalendarLogic.save(projCalendar);

    			project.setProjectcalendar(projCalendar);

    			projectLogic.save(project);

    			infoCreated(req, "project.calendar");
    		}
    		else if (idCalendarBase != -1){

    			infoUpdated(req, "project.calendar");

    			projCalendar.setCalendarbase(new Calendarbase(idCalendarBase));
    			projectcalendarLogic.save(projCalendar);
    		}
    		else if (projCalendar != null && idCalendarBase == -1) {

    			projCalendar.setCalendarbase(null);
    			projectcalendarLogic.save(projCalendar);

    			infoUpdated(req, "project.calendar");
    		}
    	}
    	catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

    	viewProjectPlanning(req, resp, idProject);
	}

	/**
     * Delete Checklist
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void deleteCheckListJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

    	int idChecklist	= ParamUtil.getInteger(req, Checklist.IDCHECKLIST,-1);
    	PrintWriter out = resp.getWriter();

		try {
			ChecklistLogic checklistLogic = new ChecklistLogic();
			checklistLogic.delete(checklistLogic.findById(idChecklist));

			out.print(infoDeleted(getResourceBundle(req), "checklist"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Save Checklist
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void saveCheckListJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

    	int idWbsnode		= ParamUtil.getInteger(req, Wbsnode.IDWBSNODE);
    	int idChecklist		= ParamUtil.getInteger(req, Checklist.IDCHECKLIST,-1);
    	String code			= ParamUtil.getString(req, Checklist.CODE);
    	String description	= ParamUtil.getString(req, Checklist.DESCRIPTION);
    	String name			= ParamUtil.getString(req, Checklist.NAME);

    	PrintWriter out = resp.getWriter();

		try {

			ChecklistLogic checklistLogic = new ChecklistLogic();

			Checklist checklist = null;

			if (idChecklist == -1) { checklist = new Checklist(); }
			else {
				checklist = checklistLogic.findById(idChecklist, false);
			}

			checklist.setCode(code);
			checklist.setName(name);
			checklist.setDescription(description);
			checklist.setWbsnode(new Wbsnode(idWbsnode));

			checklist = checklistLogic.save(checklist);

			out.print(JsonUtil.toJSON(Checklist.IDCHECKLIST, checklist.getIdChecklist()));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
     * Save Milestone
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveMilestoneJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

    	Character reportType		= ParamUtil.getString(req, "report_sign", StringPool.BLANK).charAt(0);
		Date plannedDate			= ParamUtil.getDate(req, "planned_date", getDateFormat(req), null);
		String label				= ParamUtil.getString(req, "label", StringPool.BLANK);
		String name					= ParamUtil.getString(req, Milestones.NAME, StringPool.BLANK);
		String description			= ParamUtil.getString(req, "description", StringPool.BLANK);
		Integer idMilestone 		= ParamUtil.getInteger(req, "milestone_id", -1);
		Integer idProject			= ParamUtil.getInteger(req, Project.IDPROJECT, -1);
		Integer idActivity			= ParamUtil.getInteger(req, "activity", -1);
		boolean notify				= ParamUtil.getBoolean(req, Milestones.NOTIFY,false);
		int	notifyDays				= ParamUtil.getInteger(req, Milestones.NOTIFYDAYS,0);
		String notificationText 	= ParamUtil.getString(req, Milestones.NOTIFICATIONTEXT);
		Integer idMilestoneType		= ParamUtil.getInteger(req, Milestones.MILESTONETYPE, -1);
		Integer idMilestoneCategory	= ParamUtil.getInteger(req, Milestones.MILESTONECATEGORY, -1);

		PrintWriter out = resp.getWriter();

		try {
			// Declare logics
			MilestoneLogic milestoneLogic 				= new MilestoneLogic();
			ProjectActivityLogic projectActivityLogic 	= new ProjectActivityLogic(getSettings(req), getResourceBundle(req));

			Projectactivity activity = projectActivityLogic.consActivity(idActivity);

			JSONObject returnJSON	= null;
			Milestones milestone	= null;

			// Set data
			//
			if (idMilestone != -1) {
				milestone = milestoneLogic.consMilestone(new Milestones(idMilestone));
				returnJSON = infoUpdated(getResourceBundle(req), returnJSON, "milestone");
			}
			else {
				milestone = new Milestones();
				milestone.setProject(new Project(idProject));
				returnJSON = infoCreated(getResourceBundle(req), returnJSON, "milestone");
			}

			milestone.setReportType(reportType);
			milestone.setPlanned(plannedDate);

			if (milestone.getEstimatedDate() == null) {
				milestone.setEstimatedDate(plannedDate != null ? plannedDate : new Date());
			}

			milestone.setLabel(label);
			milestone.setName(name);
			milestone.setDescription(description);
			milestone.setNotify(notify);
			milestone.setNotifyDays(notifyDays);
			milestone.setNotificationText(notificationText);

			// Date for notification
			if (notify && plannedDate != null) {

				Calendar notifyDate = Calendar.getInstance();
				notifyDate.setTime(plannedDate);
				notifyDate.add(Calendar.DAY_OF_MONTH, 0-notifyDays);
				milestone.setNotifyDate(notifyDate.getTime());
			}
			else {
				milestone.setNotifyDate(null);
			}

			milestone.setProjectactivity(activity);

			// set milestone type
			if (idMilestoneType != -1) {

				// Declare logic
				MilestonetypeLogic milestonetypeLogic = new MilestonetypeLogic();

				// Logic
				Milestonetype milestonetype = milestonetypeLogic.findById(idMilestoneType);

				// Set
				milestone.setMilestonetype(milestonetype);
			}
			else {
				milestone.setMilestonetype(null);
			}

			// Set milestone category
			//
			if (idMilestoneCategory != -1) {

				// Declare logic
				MilestonecategoryLogic milestonecategoryLogic = new MilestonecategoryLogic();

				// Logic
				Milestonecategory milestonecategory = milestonecategoryLogic.findById(idMilestoneCategory);

				// Set
				milestone.setMilestonecategory(milestonecategory);
			}
			else {
				milestone.setMilestonecategory(null);
			}

			// Logic
			milestoneLogic.save(milestone);

			// Response
			//
			JSONObject milestoneJSON 	= JSONModelUtil.milestoneToJSON(getResourceBundle(req), milestone);
			JSONObject activityJSON 	= JSONModelUtil.activityToJSON(getResourceBundle(req), activity);

			returnJSON.put("milestone", milestoneJSON);
			returnJSON.put("activity", activityJSON);

			out.print(returnJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
     * Send information of structure
     * @param req
     * @param resp
     * @throws IOException
     */
    private void validateWBS(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();

    	int idProject = ParamUtil.getInteger(req, Project.IDPROJECT);

    	List<ParamResourceBundle> info = null;
    	try {
    		WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();

    		info = wbsNodeLogic.checkErrorsWBS(new Project(idProject));

    		JSONObject infoJSON = new JSONObject();

    		for (ParamResourceBundle e : info) {
    			info(getResourceBundle(req), StringPool.INFORMATION, e.toString(getResourceBundle(req)), infoJSON);
			}

			if (info.isEmpty()) { info(getResourceBundle(req), StringPool.INFORMATION, "wbs.info", infoJSON); }

    		out.print(infoJSON.toString());
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    private void projectActions(HttpServletRequest req, HttpServletResponse resp, String accion) throws IOException, ServletException {
    	Integer idProject = ParamUtil.getInteger(req, "id",null);

		try {
			if (DELETE_WBSNODE.equals(accion)) { deleteWbsNode(req, resp); }
			else if (SAVE_PROJECT.equals(accion)) { saveProject(req, resp); }
			else if (DEL_INCOME.equals(accion)) { deleteIncome(req, resp); }
			else if (DEL_FOLLOWUP.equals(accion)) { deleteFollowup(req, resp); }
			else if (DEL_COST.equals(accion)) { deleteCost(req, resp); }
			else if (DEL_IWO.equals(accion)) { deleteIwo(req, resp); }
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

		viewProjectPlanning(req, resp, idProject);
    }
    
    /**
     * Cascade delete wbsNode
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void deleteWbsNode(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    	int idWbsnode = ParamUtil.getInteger(req, "wbs_id", -1);

    	try {
    		WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();

    		wbsNodeLogic.deleteWBSNodeAndActivities(idWbsnode, getSettings(req));

			infoDeleted(req, "change.wbs_node");
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void saveProject(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    	Integer idProject = ParamUtil.getInteger(req, "id",null);
		String scopeStatement 	= ParamUtil.getString(req, "scope_statement", StringPool.BLANK);
		String hdDescription 	= ParamUtil.getString(req, "schedule_hd_description", StringPool.BLANK);

		try {
			MilestoneLogic milestoneLogic 	= new MilestoneLogic();
			ProjectLogic projectLogic 		= new ProjectLogic(getSettings(req), getResourceBundle(req));
			WBSNodeLogic wbsNodeLogic 		= new WBSNodeLogic();

			Project project = projectLogic.consProject(idProject);
			project.setScopeStatement(scopeStatement);
			project.setHdDescription(hdDescription);
			projectLogic.save(project);

			List<ParamResourceBundle> info = wbsNodeLogic.checkErrorsWBS(project);

			for (ParamResourceBundle e : info) {
				info(StringPool.INFORMATION, req, e.toString(getResourceBundle(req)));
			}

			if (milestoneLogic.checkMilestones(project)) {
				info(StringPool.INFORMATION, req, "msg.error.milestones_without_activity");
			}
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void deleteIncome(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {    	
    	Integer idIncome = ParamUtil.getInteger(req, "income_id", -1);

		try {
			IncomesLogic incomesLogic = new IncomesLogic();

			incomesLogic.delete(incomesLogic.findById(idIncome));
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void deleteFollowup(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {    	
    	Integer idFollowup	= ParamUtil.getInteger(req, "followup_id", -1);
    	int idProject		= ParamUtil.getInteger(req, "id", -1);

		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			ProjectFollowupLogic projectFollowupLogic = new ProjectFollowupLogic();

			projectFollowupLogic.delete(new Projectfollowup(idFollowup));

			Project project = projectLogic.findById(idProject);
			projectFollowupLogic.updateRAG(project);
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void deleteCost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {    	
    	Integer idCost = ParamUtil.getInteger(req, "cost_id", -1);
		Integer costType = ParamUtil.getInteger(req, "cost_type", -1);

		try {
			ProjectCostsLogic projectCostsLogic	= new ProjectCostsLogic();

			projectCostsLogic.deleteCost(idCost, costType);
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void deleteIwo(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {    	
    	Integer idIwo = ParamUtil.getInteger(req, "iwo_id", -1);

		try {
			IwosLogic iwosLogic = new IwosLogic();

			iwosLogic.delete(new Iwo(idIwo));
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    }
    
	/**
     * Save activity
     * 
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void saveActivity(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		Integer idActivity 	    = ParamUtil.getInteger(req, "idactivity", -1);
		Date initDate 		    = ParamUtil.getDate(req, "init_date", getDateFormat(req), null);
		Date endDate 		    = ParamUtil.getDate(req, "end_date", getDateFormat(req), null);
		String dictionary 	    = ParamUtil.getString(req, "wbs_dictionary", null);
		double pv		 	    = ParamUtil.getCurrency(req, Projectactivity.PV, 0);
        Resourceprofiles role   = getUser(req).getResourceprofiles();

		try {
			// Declare logic
			ProjectActivityLogic projectActivityLogic = new ProjectActivityLogic(getSettings(req), getResourceBundle(req));

            // Old Activity
            Projectactivity activityOld = projectActivityLogic.consActivity(idActivity);

            // New Activity
            Projectactivity activity    = new Projectactivity(idActivity);

            // Set new activity
            activity.setPlanInitDate(initDate);
            activity.setPlanEndDate(endDate);
            activity.setWbsdictionary(dictionary);
            activity.setPv(pv);


            Map<String, Object> extraData = getExtraData(req);

            // Call to listeners validations
            ProcessListener.getInstance().process(
                    SavePlanActivityAbstractListener.class,
                    activityOld,
                    activity,
                    extraData);

			// Logic save activity
			projectActivityLogic.saveActivityInPlan(activity, role);

            // Call to listeners updates
            ProcessListener.getInstance().process(
                    UpdateTeammemberAbstractListener.class,
                    activityOld,
                    activity,
                    extraData);

            // Info messages
            if (extraData != null && extraData.containsKey(ProcessListener.EXTRA_DATA_INFO)) {
                createInfos((List<Info>) extraData.get(ProcessListener.EXTRA_DATA_INFO), req);
            }
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

		viewProjectPlanning(req, resp, null);
	}

	/**
	 * Set Team Member instance from request
	 * @param req
	 * @return
	 */
	private Teammember setMemberFromRequest(HttpServletRequest req, Teammember member) {

		Integer idActivity		= ParamUtil.getInteger(req, "idActivity", -1);
		Date dateIn				= ParamUtil.getDate(req, "member_datein", getDateFormat(req), null);
		Date dateOut			= ParamUtil.getDate(req, "member_dateout", getDateFormat(req), null);
		Integer idEmployee 		= ParamUtil.getInteger(req, "employee_id", -1);
		int fte					= ParamUtil.getInteger(req, "member_fte", 0);
		double sellRate			= ParamUtil.getCurrency(req, Teammember.SELLRATE, 0);
		int idJobCategory				= ParamUtil.getInteger(req, Jobcategory.IDJOBCATEGORY,-1);
		String commentsPm		= ParamUtil.getString(req, "commentsPm");

		member.setJobcategory(idJobCategory == -1 ? null : new Jobcategory(idJobCategory));
		member.setFte(fte);
		member.setSellRate(sellRate);
		member.setDateIn(dateIn);
		member.setDateOut(dateOut);
		member.setProjectactivity(new Projectactivity(idActivity));
		member.setEmployee(new Employee(idEmployee));
		member.setCommentsPm(commentsPm);

		return member;
	}


	/**
     * Redirect to Project Planning
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
	private void viewProjectPlanning(HttpServletRequest req,
			HttpServletResponse resp, Integer idProject) throws ServletException, IOException {

		// Information to recover
		Project project								= null;
		List<Program> programs						= null;
		List<Milestones> milestones					= null;
		List<Projectfollowup> followups				= null;
		List<Projectcosts> costs					= null;
		List<Iwo> iwos								= null;
		List<Budgetaccounts> budgetaccounts			= null;
		List<Teammember> team						= null;
		List<Resourceprofiles> profiles 			= null;
		List<Jobcategory> jobcategories				= null;
		List<Performingorg> perfOrgs				= null;
		List<Checklist> checklists					= null;
		List<Wbsnode> wbsnodes						= null;
		List<Calendarbase> listCalendars			= null;
		Projectactivity rootActivity				= null;
		List<Metrickpi> metricKpis					= null;
		List<Resourcepool> resourcePools			= null;
		List<Projectactivity> activities			= null;
		List<Milestonetype> milestoneTypes			= null;
		List<Skill> skills							= null;
		List<Seller> sellers						= null;
		List<Milestonecategory> milestoneCategories	= null;

		if (idProject == null) {
			idProject = ParamUtil.getInteger(req, "id", (Integer)req.getSession().getAttribute("idProject"));
		}

		boolean hasPermission = false;

		try {
			List<Documentproject> docs = new ArrayList<Documentproject>();

			hasPermission = SecurityUtil.hasPermission(req, new Project(idProject), Constants.TAB_PLAN);

			if (hasPermission) {
				// Declare logics
				BudgetaccountsLogic budgetaccountsLogic 		= new BudgetaccountsLogic();
				ChecklistLogic checklistLogic 					= new ChecklistLogic();
				CalendarBaseLogic baseLogic						= new CalendarBaseLogic();
				ResourceProfilesLogic resourceProfilesLogic		= new ResourceProfilesLogic();
				PerformingOrgLogic performingOrgLogic			= new PerformingOrgLogic();
				ProjectActivityLogic activityLogic				= new ProjectActivityLogic(getSettings(req), getResourceBundle(req));
				DocumentprojectLogic documentprojectLogic		= new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
		    	IwosLogic iwosLogic 							= new IwosLogic();
		    	MetrickpiLogic metrickpiLogic 					= new MetrickpiLogic();
		    	MilestoneLogic milestoneLogic 					= new MilestoneLogic();
		    	ProgramLogic programLogic						= new ProgramLogic();
		    	ProjectActivityLogic projectActivityLogic		= new ProjectActivityLogic(getSettings(req), getResourceBundle(req));
		    	ProjectCostsLogic projectCostsLogic				= new ProjectCostsLogic();
		    	ProjectFollowupLogic projectFollowupLogic 		= new ProjectFollowupLogic();
		    	ProjectLogic projectLogic 						= new ProjectLogic(getSettings(req), getResourceBundle(req));
		    	TeamMemberLogic teamMemberLogic					= new TeamMemberLogic();
				WBSNodeLogic wbsNodeLogic 						= new WBSNodeLogic();
				JobcategoryLogic jobcategoryLogic				= new JobcategoryLogic();
				SkillLogic skillLogic							= new SkillLogic();
				SellerLogic sellerLogic 						= new SellerLogic();
				ResourcepoolLogic resourcepoolLogic				= new ResourcepoolLogic();
				MilestonetypeLogic milestonetypeLogic			= new MilestonetypeLogic();
				MilestonecategoryLogic milestonecategoryLogic 	= new MilestonecategoryLogic();

				// Joins and logics
				//
				List<String> joins = new ArrayList<String>();
				joins.add(Project.PROGRAM);
                joins.add(Project.PROGRAM+"."+ Program.EMPLOYEE);
				joins.add(Project.PROGRAM + "." + Program.EMPLOYEE + "." + Employee.CONTACT);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
				joins.add(Project.PROJECTCALENDAR);
				joins.add(Project.PROJECTKPIS);
				joins.add(Project.PROJECTKPIS+"."+Projectkpi.METRICKPI);
				joins.add(Project.PROJECTCALENDAR+"."+Projectcalendar.PROJECTCALENDAREXCEPTIONSES);
				joins.add(Project.PROJECTCALENDAR+"."+Projectcalendar.CALENDARBASE);
				joins.add(Project.PROJECTCALENDAR+"."+Projectcalendar.CALENDARBASE+"."+Calendarbase.CALENDARBASEEXCEPTIONSES);
				joins.add(Project.INCOMESES);

				project = projectLogic.consProject(new Project(idProject), joins);

				// Get activities
				//
				joins = new ArrayList<String>();
				joins.add(Projectactivity.WBSNODE);

				activities		= activityLogic.findByRelation(Projectactivity.PROJECT, project, Projectactivity.ACTIVITYNAME, Constants.ASCENDENT, joins);

				// Get milestones
				//
				joins = new ArrayList<String>();
				joins.add(Milestones.PROJECTACTIVITY);
				joins.add(Milestones.MILESTONETYPE);
				joins.add(Milestones.MILESTONECATEGORY);

				milestones		= milestoneLogic.findByRelation(Milestones.PROJECT, project, Milestones.PLANNED, Constants.ASCENDENT, joins);

				programs		= programLogic.consByPO(getUser(req));
				checklists		= checklistLogic.findByProject(project);
				wbsnodes		= wbsNodeLogic.findByProject(project);
				followups		= projectFollowupLogic.consFollowups(project);
				costs			= projectCostsLogic.consCosts(project);
				iwos			= iwosLogic.consIwos(project);
				profiles		= resourceProfilesLogic.findAll();
				team			= teamMemberLogic.consTeamMembers(new Project(idProject));

				// List of company
				budgetaccounts		= budgetaccountsLogic.findByRelation(Budgetaccounts.COMPANY, getCompany(req), Budgetaccounts.DESCRIPTION, Constants.ASCENDENT);
				listCalendars 		= baseLogic.findByRelation(Calendarbase.COMPANY, getCompany(req), Calendarbase.NAME, Constants.ASCENDENT);
				jobcategories		= jobcategoryLogic.findByRelation(Jobcategory.COMPANY, getCompany(req), Jobcategory.NAME, Constants.ASCENDENT);
				metricKpis			= metrickpiLogic.findByRelation(Performingorg.COMPANY, getCompany(req), Metrickpi.NAME, Constants.ASCENDENT);
				perfOrgs  			= performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req), Performingorg.NAME, Constants.ASCENDENT);
				skills				= skillLogic.findByRelation(Skill.COMPANY, getCompany(req), Skill.NAME, Constants.ASCENDENT);
				sellers				= sellerLogic.findByRelation(Seller.COMPANY, getCompany(req), Seller.NAME, Constants.ASCENDENT);
				milestoneTypes		= milestonetypeLogic.findByRelation(Milestonetype.COMPANY,  getCompany(req), Milestonetype.NAME, Constants.ASCENDENT);
				milestoneCategories	= milestonecategoryLogic.findByRelation(Milestonecategory.COMPANY,  getCompany(req), Milestonetype.NAME, Constants.ASCENDENT);

				joins = new ArrayList<String>();
				joins.add(Employee.CONTACT);

				resourcePools = resourcepoolLogic.findByRelation(Resourcepool.COMPANY, getCompany(req));

				rootActivity = projectActivityLogic.consRootActivity(project);

				String documentStorage = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_STORAGE, Settings.DEFAULT_PROJECT_DOCUMENT_STORAGE);

				// Get documents
				docs = documentprojectLogic.getDocuments(getSettings(req), project, Constants.DOCUMENT_PLANNING);

				req.setAttribute("docs", docs);
				req.setAttribute("documentStorage", documentStorage);
			}
		}
		catch (Exception e) {
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}

		if (hasPermission) {

			// Messages
			String message = ParamUtil.getString(req, "message", StringPool.BLANK);
			if (!message.equals(StringPool.BLANK)) { info(StringPool.INFORMATION, req, message); }

			if (Constants.STATUS_CONTROL.equals(project.getStatus())) {
				String messageUpdateEV = ParamUtil.getString(req, "messageUpdateEV", StringPool.BLANK);
				if (messageUpdateEV != StringPool.BLANK) { info(StringPool.INFORMATION, req, messageUpdateEV); }
			}

			String messageUpdateMilestones = ParamUtil.getString(req, "messageUpdateMilestones", StringPool.BLANK);
			if (messageUpdateMilestones != StringPool.BLANK) { info(StringPool.INFORMATION, req, messageUpdateMilestones); }

			String validate = ParamUtil.getString(req, "validate", StringPool.BLANK);
			if (!validate.equals(StringPool.BLANK)) { req.setAttribute("validateWBS", true); }


			req.setAttribute("project", project);
			req.setAttribute("activities", activities);
			req.setAttribute("milestones", milestones);
			req.setAttribute("programs", programs);
			req.setAttribute("rootActivity", rootActivity);
			req.setAttribute("checklists", checklists);
			req.setAttribute("wbsnodes", wbsnodes);
			req.setAttribute("listCalendars", listCalendars);
			req.setAttribute("followups", followups);
			req.setAttribute("costs", costs);
			req.setAttribute("iwos", iwos);
			req.setAttribute("budgetaccounts", budgetaccounts);
			req.setAttribute("team", team);
			req.setAttribute("profiles", profiles);
			req.setAttribute("jobcategories", jobcategories);
			req.setAttribute("perforgs", perfOrgs);
			req.setAttribute("metricKpis", metricKpis);
			req.setAttribute("resourcePools", resourcePools);
			req.setAttribute("skills", skills);
			req.setAttribute("sellers", sellers);
			req.setAttribute("milestoneTypes", milestoneTypes);
			req.setAttribute("milestoneCategories", milestoneCategories);

			req.setAttribute("title", ValidateUtil.isNotNull(project.getChartLabel()) ?
					project.getProjectName() + " ("+project.getChartLabel()+")" : project.getProjectName());

			req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));

			req.setAttribute("tab", Constants.TAB_PLAN);
			forward("/index.jsp?nextForm=project/plan/planning_project", req, resp);
		}
		else {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

	/**
	 * Set Wbs Node instance from request
	 * @param req
	 * @param wbsNode
	 * @return
	 * @throws Exception
	 */
	private Wbsnode setWbsNodeFromRequest(HttpServletRequest req, Wbsnode wbsNode) throws Exception {

		String code			= ParamUtil.getString	(req, "wbs_code", null);
		Integer idParent	= ParamUtil.getInteger	(req, "wbs_parent", -1);
		String name			= ParamUtil.getString	(req, "wbs_name", null);
		String desc			= ParamUtil.getString	(req, "wbs_desc", null);
		Boolean isCA		= ParamUtil.getBoolean	(req, "wbs_ca", false);
		double budget		= ParamUtil.getCurrency	(req, "wbs_budget", 0);

		Wbsnode parent = null;
		if (idParent != -1) {
			List<String> joins 			= new ArrayList<String>();
			WBSNodeLogic wbsNodeLogic 	= new WBSNodeLogic();

			parent = new Wbsnode();
			parent.setIdWbsnode(idParent);

			joins.add(Wbsnode.WBSNODE);
			Wbsnode tempWbsNode = wbsNodeLogic.findById(idParent, joins);
			Boolean isCa = (tempWbsNode.getIsControlAccount() == null ? false : tempWbsNode.getIsControlAccount());

			parent.setIsControlAccount(isCa);
		}

		if (wbsNode == null) {
			Integer idProject	= ParamUtil.getInteger(req, "id");

			wbsNode = new Wbsnode();
			wbsNode.setProject(new Project(idProject));
		}

		if (wbsNode.getIdWbsnode() == null || wbsNode.getWbsnode() != null) {
			wbsNode.setWbsnode(parent);
		}

		wbsNode.setCode(code);
		wbsNode.setName(name);
		wbsNode.setDescription(desc);
		wbsNode.setIsControlAccount(isCA);

		if (isCA) { wbsNode.setBudget(budget); }
		else { wbsNode.setBudget(null); }

		return wbsNode;
	}


	/**
	 * Save IWO cost from request
	 * @param req
	 * @param costs
	 * @return
	 * @throws LogicException
	 */
	private Iwo saveIwoFromRequest(HttpServletRequest req, Projectcosts costs) throws Exception {
		IwosLogic iwosLogic = new IwosLogic();

		Integer idIwo 	= ParamUtil.getInteger	(req, "iwo_id", -1);
		double planned	= ParamUtil.getCurrency	(req, "planned", 0);
		String desc		= ParamUtil.getString	(req, "desc", null);

		Iwo iwo = iwosLogic.consIwo(idIwo);
		if (iwo == null) {
			iwo = new Iwo();
		}
		iwo.setProjectcosts(costs);
		iwo.setDescription(desc);
		iwo.setPlanned(planned);
		iwo.setActual(new Double(0));

		iwosLogic.saveIwo(iwo, null);

		return iwo;
	}

	/**
	 * Save Expense cost detail from request
	 * @param req
	 * @param costs
	 * @return
	 * @throws LogicException
	 */
	private Expenses saveExpenseCostFromRequest(HttpServletRequest req, Projectcosts costs) throws Exception {

		Integer idProject 	= ParamUtil.getInteger(req, "id", -1);
		Integer idCost		= ParamUtil.getInteger(req, "cost_id", -1);
		double planned		= ParamUtil.getCurrency(req, "planned", 0);
		Integer idBudget	= ParamUtil.getInteger(req, "budget_account", -1);
		String desc			= ParamUtil.getString(req, "desc", null);

		ExpensesLogic expensesLogic = new ExpensesLogic();

		if (expensesLogic.isBudgetInUse(new Project(idProject), new Expenses(idCost), new Budgetaccounts(idBudget))) {
			BudgetaccountsLogic budgetaccountsLogic = new BudgetaccountsLogic();
			Budgetaccounts budgetaccounts = budgetaccountsLogic.findById(idBudget);

			throw new LogicException("msg.info.in_use", "cost.charge_account", budgetaccounts.getDescription());
		}
		Expenses cost = null;
		if (idCost == -1) {
			cost = new Expenses();
		}
		else {
			cost = expensesLogic.consExpenses(idCost);
		}

		cost.setProjectcosts(costs);
		cost.setDescription(desc);
		cost.setPlanned(planned);
		cost.setBudgetaccounts(new Budgetaccounts(idBudget));

		expensesLogic.saveExpenses(cost, null);

		return cost;
	}


	/**
	 * Save direct cost detail from request
	 * @param req
	 * @param costs
	 * @return
	 * @throws LogicException
	 */
	private Directcosts saveDirectCostFromRequest(HttpServletRequest req, Projectcosts costs) throws Exception {

		Integer idCost		= ParamUtil.getInteger	(req, "cost_id", -1);
		double planned 		= ParamUtil.getCurrency	(req, "planned", 0);
		Integer idBudget	= ParamUtil.getInteger	(req, "budget_account", -1);
		String desc			= ParamUtil.getString	(req, "desc", null);

		Budgetaccounts budget 				= null;
		DirectCostsLogic directCostsLogic 	= new DirectCostsLogic();

		if (idBudget != -1) {
			budget = new Budgetaccounts();
			budget.setIdBudgetAccount(idBudget);
		}

		Directcosts cost = directCostsLogic.consDirectcosts(idCost);
		if (cost == null) {
			cost = new Directcosts();
		}
		cost.setProjectcosts(costs);
		cost.setDescription(desc);
		cost.setPlanned(planned);
		cost.setBudgetaccounts(budget);

		directCostsLogic.saveDirectcost(cost, null);

		return cost;
	}


	/**
	 * Save followup cost from request
	 * @param req
	 * @param followup
	 */
	private void setFollowupFromRequest(HttpServletRequest req,
			Projectfollowup followup) {

		Date date			= ParamUtil.getDate		(req, "date", getDateFormat(req), null);
		Double pv			= ParamUtil.getCurrency	(req, "pv", null);

		followup.setFollowupDate(date);
		followup.setPv(pv);
	}


	/**
	 * Set Income instance from request
	 * @param req
	 * @param income
	 */
	private void setIncomeFromRequest(HttpServletRequest req, Incomes income) {

		Integer idProject 		= ParamUtil.getInteger(req, "id", -1);
		Date plannedDate		= ParamUtil.getDate		(req, "planned_date", getDateFormat(req), null);
		double plannedAmount	= ParamUtil.getCurrency	(req, "planned_amount", 0);
		String description		= ParamUtil.getString (req, "description", "");

		income.setPlannedBillDate(plannedDate);
		income.setPlannedBillAmmount(plannedAmount);
		income.setPlannedDescription(description);
		income.setProject(new Project(idProject));
	}

	/**
	 * Check kpi weight
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void checkKPIJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PrintWriter out = resp.getWriter();

		Integer idProject		= ParamUtil.getInteger(req, "id",null);
		String idImportProjects = ParamUtil.getString(req, "idImportProjects",null);

    	// IDs from different projects
    	Integer[] idImportProjectsArray	= StringUtil.splitStrToIntegers(idImportProjects, null);

    	Boolean isOverWeight = false;
    	double totalWeight = 0;
    	try {

	    	// Declare logics
	    	ProjectLogic projectLogic 		= new ProjectLogic(getSettings(req), getResourceBundle(req));
			ProjectKpiLogic projectKpiLogic = new ProjectKpiLogic();

    		// Import actual project by ID
    		Project actualProject = projectLogic.findById(idProject);

			JSONObject infoJSON = new JSONObject();

			for (int id : idImportProjectsArray) {

				// Set joins
				List<String> joins = new ArrayList<String>();
				joins.add(Project.PROJECTKPIS);

				// Import project by ID
				Project importProject;
				importProject = projectLogic.findById(id, joins);

				if (!isOverWeight) {

					totalWeight = totalWeight + projectKpiLogic.calcWeightKpi(importProject, actualProject);

					if (totalWeight > 100) {
						isOverWeight = true;
					}
				}
			}

			infoJSON.put("isOverWeight", isOverWeight);
			out.print(infoJSON);

    	} catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e);}
    	finally {out.close();}
	}

    private void checkViabilityRecalculatePV (HttpServletRequest req, HttpServletResponse resp) {
        PrintWriter out = null;
        int projectID;
        try {

            out = resp.getWriter();
            projectID = ParamUtil.getInteger(req, "id");

            // Init Logics
            ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
            projectLogic.checkRecalculatePvViability(projectID);

            out.print(projectID);
        }

        catch (IOException e) {

            System.err.println("Error de entrada/salida: " + e);
            ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e);
        }catch (Exception e) {

            ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e);
        } finally{

            out.close();
        }
    }

    private void recalculatePlannedValue(HttpServletRequest req, HttpServletResponse resp) {

        PrintWriter out = null;
        int projectID = 0;

        try {

            out = resp.getWriter();
            projectID = ParamUtil.getInteger(req, "id");

            ProjectFollowupLogic projectFollowupLogic = new ProjectFollowupLogic(getSettings(req), getResourceBundle(req));

            List<Projectfollowup> followups = projectFollowupLogic.recalculatePV(projectID);

            // prepare a JSON for refreshing the table
            JSONArray data = new JSONArray();
            for (Projectfollowup followup: followups) {

                JSONArray info = new JSONArray();

                info.add(0, followup.getIdProjectFollowup().toString());
                info.add(1, followup.getFollowupDate().toString());
                info.add(2, followup.getDaysToDate());
                info.add(3, followup.getPv());
                info.add(4, "<img onclick=\"editFollowup(this)\" title=\"Ver\" class=\"link\" src=\"images/view.png\"> &nbsp;&nbsp;&nbsp; <img onclick=\"deleteFollowup(this)\" title=\"Borrar\" class=\"link\" src=\"images/delete.jpg\">");

                data.add(info);
            }
            String jsonOutput = data.toString();
            byte [] byteArray = jsonOutput.getBytes("UTF-8");
            out.print(new String(byteArray));

        } catch (IOException e) {

            System.err.println("Error de entrada/salida: " + e);
            ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e);

        } catch (Exception e) {

            System.err.println("Error:" + e);
            ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req),LOGGER, e);
        } finally {

            out.close();
        }

    }
}

