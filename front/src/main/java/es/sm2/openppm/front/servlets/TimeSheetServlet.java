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
 * File: TimeSheetServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Employeeoperationdate;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.impl.Timesheetcomment;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.EmployeeoperationdateLogic;
import es.sm2.openppm.core.logic.impl.OperationLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.TimesheetLogic;
import es.sm2.openppm.core.logic.impl.TimesheetcommentLogic;
import es.sm2.openppm.core.model.wrap.ApprovalWrap;
import es.sm2.openppm.core.model.wrap.TimesheetWrap;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.json.Exclusion;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;

/**
 * Servlet implementation class TimeSheetServlet
 */
public class TimeSheetServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(TimeSheetServlet.class);
	
	public final static String REFERENCE				= "timesheet";
	
	/***************** Actions ****************/
	public final static String VIEW_TIMESHEET			= "view-timesheet";
	public final static String SAVE_ALL_TIMESHEET		= "save-all-timesheet";
	public final static String APPROVE_ALL_TIMESHEET	= "approve-all-timesheet";
	public final static String APPROVE_SEL_TIMESHEET	= "approve-sel-timesheet";
	public final static String REJECT_SEL_TIMESHEET		= "reject-sel-timesheet";
	public final static String REJECT_ALL_TIMESHEET		= "reject-all-timesheet";
	public final static String SUGGEST_REJECT			= "suggestRejectSelTimesheet";
	public final static String ADD_OPERATION			= "add-operation-time-sheet";
	public final static String DEL_OPERATION			= "del-operation-time-sheet";
	public final static String CHANGE_WEEK				= "change-week-time-sheet";
	public final static String NEXT_WEEK				= "next-week-time-sheet";
	public final static String PREV_WEEK				= "prev-week-time-sheet";
	
	/************** Actions AJAX **************/
	public final static String JX_SAVE_TIMESHEET		= "ajax-save-timesheet";
	public final static String JX_APPROVE_TIMESHEET		= "ajax-approve-timesheet";
	public final static String JX_REJECT_TIMESHEET		= "ajax-reject-timesheet";
	public final static String JX_VIEW_COMMENTS			= "ajax-view-comments-timesheet";
	public final static String JX_SUGGEST_REJECT		= "ajaxSuggestReject";

	
	

	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, VIEW_TIMESHEET))) {
			
			/***************** Actions ****************/
			if (ValidateUtil.isNull(accion) || VIEW_TIMESHEET.equals(accion)) { viewTime(req, resp, null); }
			else if (SAVE_ALL_TIMESHEET.equals(accion)) { updateALLTimeSheet(req, resp, false, accion); }
			else if (APPROVE_SEL_TIMESHEET.equals(accion)) { changeSelTimeSheet(req, resp, true); }
			else if (REJECT_SEL_TIMESHEET.equals(accion)) { changeSelTimeSheet(req, resp, false); }
			else if (APPROVE_ALL_TIMESHEET.equals(accion)) {
				if (SecurityUtil.isUserInRole(req, Constants.ROLE_RESOURCE)) { updateALLTimeSheet(req, resp, true, accion); }
				else { changeALLTimeSheet(req, resp, true); }
			}
			else if (REJECT_ALL_TIMESHEET.equals(accion)) { changeALLTimeSheet(req, resp, false); }
			else if (ADD_OPERATION.equals(accion) || DEL_OPERATION.equals(accion)) { updateALLTimeSheet(req, resp, false, accion); }
			else if (CHANGE_WEEK.equals(accion)) { changeWeek(req, resp, null); }
			else if (NEXT_WEEK.equals(accion)) { changeWeek(req, resp, 7); }
			else if (PREV_WEEK.equals(accion)) { changeWeek(req, resp, -7); }
			
			/************** Actions AJAX **************/
			else if (JX_APPROVE_TIMESHEET.equals(accion)) {
				if (SecurityUtil.isUserInRole(req, Constants.ROLE_RESOURCE)) { updateTimeSheetJX(req, resp, true); }
				else { changeTimeSheetJX(req, resp, true); }
			}
			else if (JX_REJECT_TIMESHEET.equals(accion)) { changeTimeSheetJX(req, resp, false); }
			else if (JX_SUGGEST_REJECT.equals(accion)) { suggestRejectTimeSheetJX(req, resp, false); }
			else if (JX_SAVE_TIMESHEET.equals(accion)) { updateTimeSheetJX(req, resp, false); }
			else if (JX_VIEW_COMMENTS.equals(accion)) { viewCommentsJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}
    
	/**
     * Change selected time sheets
     * 
     * @param req
     * @param resp
     * @param approve
     * @throws IOException
     * @throws ServletException 
     */
    private void changeSelTimeSheet(HttpServletRequest req,
			HttpServletResponse resp, boolean approve) throws ServletException, IOException {
    	
    	try {
    		
    		Date date		= ParamUtil.getDate(req, "date", getDateFormat(req), new Date());
    		
    		String idProjectsStr	= ParamUtil.getString(req, "idProjects",null);
    		String idEmployeesStr	= ParamUtil.getString(req, "idEmployees",null);
    		String comments			= ParamUtil.getString(req, "comments");

    		Integer[] idProjects	= StringUtil.splitStrToIntegers(idProjectsStr, null);
    		Integer[] idEmployees	= StringUtil.splitStrToIntegers(idEmployeesStr, null);
			
    		// Declare logic
    		TimesheetLogic timesheetLogic = new TimesheetLogic();
    		
    		timesheetLogic.changeHours(getUser(req), idEmployees, idProjects, date, comments, approve, getSettings(req), getExtraData(req), getResourceBundle(req));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewTime(req, resp, null);
	}
    
	/**
     * Change state of time Sheet
     * @param req
     * @param resp
     * @param approve
     * @throws IOException
     */
    private void changeTimeSheetJX(HttpServletRequest req, HttpServletResponse resp, boolean approve) throws IOException {
    	
    	PrintWriter out = resp.getWriter();
    	
    	// Request
		int idTimeSheet = ParamUtil.getInteger(req, "idTimeSheet",-1);
		String comments = ParamUtil.getString(req, "comments");
		
    	try {
    		
    		// Declare logic
    		TimesheetLogic timesheetLogic 		= new TimesheetLogic();
    		
    		// Logic find timesheet
    		Timesheet timesheet = timesheetLogic.findById(idTimeSheet);

    		// Logic save
    		JSONObject returnJSON = timesheetLogic.changeTimeSheet(getUser(req), getSettings(req), getResourceBundle(req), timesheet, comments, getExtraData(req), approve);
    		
    		// Response
    		out.print(returnJSON);
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    /**
     * Change all time sheet state
     * @param req
     * @param resp
     * @param approve
     * @throws ServletException
     * @throws IOException
     */
	private void changeALLTimeSheet(HttpServletRequest req,
			HttpServletResponse resp, boolean approve) throws ServletException, IOException {
		
		// Request
    	Integer[] ids	= ParamUtil.getIntegerList(req, "idTimeSheet");
    	String comments = ParamUtil.getString(req, "comments");
    	
    	try {
		
    		// Declare logic
    		TimesheetLogic timesheetLogic = new TimesheetLogic();
    		
    		// Logic
    		timesheetLogic.changeHours(getUser(req), ids, comments, approve, getSettings(req), getExtraData(req), getResourceBundle(req));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewTime(req, resp, null);
	}

	/**
     * View
     * @param req
     * @param resp
     * @param date
     * @throws ServletException
     * @throws IOException
     */
    private void viewTime(HttpServletRequest req, HttpServletResponse resp, 
    		Date date) throws ServletException, IOException {
    	
    	if (SecurityUtil.isUserInRole(req, Constants.ROLE_PM)) {
    		viewApprovalsByProject(req, resp, date);
    	}
    	else if (SecurityUtil.isUserInRole(req, SettingUtil.getApprovalRol(req))) {
			viewApprovals(req, resp, date);
		}
		else if (SecurityUtil.isUserInRole(req, Constants.ROLE_RESOURCE)) {
			viewTimeSheet(req, resp, date);
		}
	}

	/**
     * View comments of time sheet
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void viewCommentsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
    	int idTimeSheet = ParamUtil.getInteger(req, "idTimeSheet",-1);
    	
		PrintWriter out = resp.getWriter();
    	
    	try {
    		
    		TimesheetcommentLogic commentLogic = new TimesheetcommentLogic();
    		
    		List<Timesheetcomment> comments = commentLogic.findByRelation(Timesheetcomment.TIMESHEET, new Timesheet(idTimeSheet));
    		
    		out.print(JsonUtil.toJSON(comments, Constants.TIME_PATTERN, new Exclusion(Timesheet.class)));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    /**
     * View time approvals
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void viewApprovalsByProject(HttpServletRequest req, HttpServletResponse resp, Date date) throws ServletException, IOException {
    	
    	if (date == null) { date = ParamUtil.getDate(req, "date", getDateFormat(req), new Date()); }
    	
    	Integer idEmployee	= ParamUtil.getInteger(req, "idEmployee", null);
    	Integer idProject 	= ParamUtil.getInteger(req, "idProject", null);
    	
    	List<TimesheetWrap> timeSheets		= new ArrayList<TimesheetWrap>();
    	Employee employee					= null;
    	Project project						= null;
    	Set<Project> projects 				= new HashSet<Project>(0);
    	List<Integer> weekDays				= new ArrayList<Integer>();
    	Date initWeek						= null;
    	Date endWeek						= null;
    	List<ApprovalWrap> approvalsWraps   = null;
    	
    	try {
        	initWeek 	= DateUtil.getFirstWeekDay(date);
        	endWeek 	= DateUtil.getLastWeekDay(date);
    		
    		EmployeeLogic employeeLogic = new EmployeeLogic();
    		TimesheetLogic timesheetLogic	= new TimesheetLogic();
    		    		
    		approvalsWraps = timesheetLogic.findAllTimesheetsByProject(initWeek, endWeek, getUser(req));

    		// Adding projects for the filter
    		for (ApprovalWrap approval : approvalsWraps) {
    			
    			Project proj = new Project(approval.getIdProject());
    			proj.setProjectName(approval.getProjectName());
    			
    			if (proj != null && proj.getIdProject() != null) {
	    			boolean found = false;
	    			for (Project tmpProj : projects) {
	    				if (proj.equals(tmpProj)) { found = true; }
	    			}
	    			
	    			if (!found) { projects.add(proj);  }
    			}
    		}
    		
    		// Ask timesheets
    		if (idEmployee != null) {
    			
    			ProjectLogic projectLogic		= new ProjectLogic(getSettings(req), getResourceBundle(req));
    			
    			project = idProject != null?projectLogic.findById(idProject):null;
    			
    			timeSheets = timesheetLogic.findHoursDetailByProject(new Employee(idEmployee), date, project);
    			
    			List<String> joins = new ArrayList<String>();
    			joins.add(Employee.CONTACT);
    			employee = employeeLogic.findById(idEmployee, joins);
    			
    			Calendar tempCal = DateUtil.getCalendar();
    			tempCal.setTime(initWeek);
    			for (int i = 1; i < 8;i++) {
    				weekDays.add(tempCal.get(Calendar.DAY_OF_MONTH));
    				tempCal.add(Calendar.DAY_OF_MONTH, 1);
    			}
    		}
    	}
    	catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    	
    	req.setAttribute("date",getDateFormat(req).format(date));
    	req.setAttribute("initWeek", getDateFormat(req).format(initWeek));
    	req.setAttribute("endWeek", getDateFormat(req).format(endWeek));
    	req.setAttribute("initTypeDate", initWeek);
    	req.setAttribute("endTypeDate", endWeek);
    	req.setAttribute("timeSheets", timeSheets);
		req.setAttribute("approvals", approvalsWraps);
    	req.setAttribute("employee", employee);
    	req.setAttribute("project", project);
    	req.setAttribute("projects", projects);
    	req.setAttribute("weekDays", weekDays);
    	
    	req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
    	req.setAttribute("title", getResourceBundle(req).getString("menu.time_approvals"));
    	forward("/index.jsp?nextForm=timesheet/approve_project_timesheet", req, resp);
    }
    
	/**
     * View time approvals
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void viewApprovals(HttpServletRequest req, HttpServletResponse resp, Date date) throws ServletException, IOException {
    	
    	if (date == null) { date = ParamUtil.getDate(req, "date", getDateFormat(req), new Date()); }
		
		Integer idEmployee	= ParamUtil.getInteger(req, "idEmployee", null);
		
		List<TimesheetWrap> timeSheets	= null;
		Employee employee			= null;
		List<Integer> weekDays		= new ArrayList<Integer>();
		Date initWeek				= null;
    	Date endWeek				= null;
    	List<ApprovalWrap> approvalWrapers = new ArrayList<ApprovalWrap>();
    
		try {
			// Ask approvals
			initWeek = DateUtil.getFirstWeekDay(date);
        	endWeek = DateUtil.getLastWeekDay(date);
			
			EmployeeLogic employeeLogic = new EmployeeLogic();
			
			TimesheetLogic timesheetLogic	= new TimesheetLogic();
			
			List<ApprovalWrap> approvalsSR = timesheetLogic.findAllApprovals(initWeek, endWeek, getUser(req));
			
			approvalWrapers.addAll(approvalsSR);
			
			// Ask Timesheets.
			if (idEmployee != null) {
				
				timeSheets = timesheetLogic.findTimesheets(new Employee(idEmployee), date);
				
				List<String> joins = new ArrayList<String>();
				joins.add(Employee.CONTACT);
				employee = employeeLogic.findById(idEmployee, joins);
				
				Calendar tempCal = DateUtil.getCalendar();
				tempCal.setTime(initWeek);
				for (int i = 1; i < 8;i++) {
					weekDays.add(tempCal.get(Calendar.DAY_OF_MONTH));
					tempCal.add(Calendar.DAY_OF_MONTH, 1);
				}
			}

			// Load configurations
			ConfigurationLogic configurationLogic = new ConfigurationLogic();
			req.setAttribute("configurations", configurationLogic.findByTypes(getUser(req), Configurations.TYPE_TIME_APPROVALS));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	
		// Create data for send.
		req.setAttribute("date", getDateFormat(req).format(date));
		req.setAttribute("initWeek", getDateFormat(req).format(initWeek));
		req.setAttribute("endWeek", getDateFormat(req).format(endWeek));
		req.setAttribute("initTypeDate", initWeek);
		req.setAttribute("endTypeDate", endWeek);
		req.setAttribute("timeSheets", timeSheets);
		req.setAttribute("approvals", approvalWrapers);
		req.setAttribute("employee", employee);
		req.setAttribute("weekDays", weekDays);
		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("title", getResourceBundle(req).getString("menu.time_approvals"));
		
		// Send to JSP.
		forward("/index.jsp?nextForm=timesheet/approve_timesheet", req, resp);
	}
    
    /**
     * Add operation in time sheet
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void addOperation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
    	int idOperation = ParamUtil.getInteger(req, "idOperation", -1);
    	Date date 		= ParamUtil.getDate(req, "date", getDateFormat(req), new Date());
    	
		try {
			TimesheetLogic timesheetLogic = new TimesheetLogic();
			
			Timesheet timesheet = timesheetLogic.findByOperation(new Operation(idOperation), getUser(req),
					DateUtil.getFirstWeekDay(date), DateUtil.getLastWeekDay(date));
			
			if (timesheet == null) {
			
				timesheet = new Timesheet();
				timesheet.setInitDate(DateUtil.getFirstWeekDay(date));
				timesheet.setEndDate(DateUtil.getLastWeekDay(date));
				
	    		timesheet.setStatus(Constants.TIMESTATUS_APP0);
	    		timesheet.setOperation(new Operation(idOperation));
	    		timesheet.setEmployee(getUser(req));
				
	    		timesheetLogic.save(timesheet);
				infoCreated(req, "operation");
			}
			else {
				info(StringPool.INFORMATION, req, "msg.info.operation_exists");
			}
			
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	}

	/**
     * Next Week for Time Sheet
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void changeWeek(HttpServletRequest req, HttpServletResponse resp, Integer days) throws ServletException, IOException {
    	
    	Date date		= ParamUtil.getDate(req, "date",getDateFormat(req));
    	
    	Calendar dateCal = DateUtil.getCalendar();
    	dateCal.setTime(DateUtil.getFirstWeekDay(date));
    	
    	if (days != null) {
    		dateCal.add(Calendar.DAY_OF_YEAR, days);
    	}
    	
    	viewTime(req, resp, dateCal.getTime());
    }
    
	/**
     * Save Time Sheet 
     * @param req
     * @param resp
     * @param approve
     * @throws IOException 
     * @throws ServletException 
     */
    private void updateALLTimeSheet(HttpServletRequest req, HttpServletResponse resp,
    		boolean approve, String accion) throws ServletException, IOException {
		
    	Integer[] ids	= ParamUtil.getIntegerList(req, "idTimeSheet");
    	String comments = ParamUtil.getString(req, "comments");
    	
    	try {
		
    		if (ids != null) {
    			
    			TimesheetLogic timesheetLogic = new TimesheetLogic();
    			
    			for (int idTimeSheet : ids) {
    				
    				List<String> joins = new ArrayList<String>();
    	    		joins.add(Timesheet.OPERATION);
					joins.add(Timesheet.PROJECTACTIVITY);
					joins.add(Timesheet.PROJECTACTIVITY + "." + Projectactivity.PROJECT);
                    joins.add(Timesheet.EMPLOYEE);
                    joins.add(Timesheet.EMPLOYEE + "." + Employee.PERFORMINGORG);
                    joins.add(Timesheet.EMPLOYEE + "." + Employee.CONTACT);

                    Timesheet timesheet = timesheetLogic.findById(idTimeSheet, joins);
    	    		
    	    		if (Constants.TIMESTATUS_APP0.equals(timesheet.getStatus())) {
    	    			
	    				Double day1		= ParamUtil.getDouble(req, "day1_"+idTimeSheet,null);
	    				Double day2		= ParamUtil.getDouble(req, "day2_"+idTimeSheet,null);
	    				Double day3		= ParamUtil.getDouble(req, "day3_"+idTimeSheet,null);
	    				Double day4		= ParamUtil.getDouble(req, "day4_"+idTimeSheet,null);
	    				Double day5		= ParamUtil.getDouble(req, "day5_"+idTimeSheet,null);
	    				Double day6		= ParamUtil.getDouble(req, "day6_"+idTimeSheet,null);
	    				Double day7		= ParamUtil.getDouble(req, "day7_"+idTimeSheet,null);
	    				
	    				timesheet.setHoursDay1(day1);
	    	    		timesheet.setHoursDay2(day2);
	    	    		timesheet.setHoursDay3(day3);
	    	    		timesheet.setHoursDay4(day4);
	    	    		timesheet.setHoursDay5(day5);
	    	    		timesheet.setHoursDay6(day6);
	    	    		timesheet.setHoursDay7(day7);
	    	    		
	    	    		// Logic save
                        timesheetLogic.updateTimeSheet(getUser(req), getResourceBundle(req), getSettings(req), getExtraData(req), approve, timesheet, comments);
                    }
    			}
    		}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		if (ADD_OPERATION.equals(accion)) { addOperation(req, resp); }
		else if (DEL_OPERATION.equals(accion)) { delOperation(req, resp); }
		
		viewTimeSheet(req, resp, null);
	}

    /**
     * Delete Operation
     * @param req
     * @param resp
     */
	private void delOperation(HttpServletRequest req, HttpServletResponse resp) {
		
		int idTimeSheet = ParamUtil.getInteger(req, "idTimeSheetOperation", -1);
		
		try {
			TimesheetLogic timesheetLogic = new TimesheetLogic();
			EmployeeoperationdateLogic employeeoperationdateLogic = new EmployeeoperationdateLogic();
			
			Timesheet timesheet = timesheetLogic.findById(idTimeSheet);
			
			if (timesheet != null) {
			
				List<Employeeoperationdate> emplDates = employeeoperationdateLogic
					.findForCalendar(getUser(req),
							timesheet.getOperation(),
							timesheet.getInitDate(),
							timesheet.getEndDate());
				
				for (Employeeoperationdate emplDate : emplDates) {
					employeeoperationdateLogic.delete(emplDate);
				}
				
				timesheetLogic.delete(timesheet);
				infoDeleted(req, "operation");
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	}

	/**
     * Save TimeSheet
     * 
     * @param req
     * @param resp
     * @param approve 
     * @throws IOException 
     */
	private void updateTimeSheetJX(HttpServletRequest req,
			HttpServletResponse resp, boolean approve) throws IOException {
		
		PrintWriter out = resp.getWriter();
    	
		// Request
		int idTimeSheet = ParamUtil.getInteger(req, "idTimeSheet",-1);
		Double day1		= ParamUtil.getDouble(req, "day1",null);
		Double day2		= ParamUtil.getDouble(req, "day2",null);
		Double day3		= ParamUtil.getDouble(req, "day3",null);
		Double day4		= ParamUtil.getDouble(req, "day4",null);
		Double day5		= ParamUtil.getDouble(req, "day5",null);
		Double day6		= ParamUtil.getDouble(req, "day6",null);
		Double day7		= ParamUtil.getDouble(req, "day7",null);
		String comments = ParamUtil.getString(req, "comments");
		
    	try {
    		
    		// Declare logic
    		TimesheetLogic timesheetLogic = new TimesheetLogic();
    		
    		// Joins
    		List<String> joins = new ArrayList<String>();
    		joins.add(Timesheet.OPERATION);
            joins.add(Timesheet.EMPLOYEE);
			joins.add(Timesheet.PROJECTACTIVITY);
			joins.add(Timesheet.PROJECTACTIVITY + "." + Projectactivity.PROJECT);
            joins.add(Timesheet.EMPLOYEE + "." + Employee.PERFORMINGORG);
            joins.add(Timesheet.EMPLOYEE + "." + Employee.CONTACT);

            // Logic find timesheet
    		Timesheet timesheet = timesheetLogic.findById(idTimeSheet, joins);
    		
    		// Set hours
    		timesheet.setHoursDay1(day1);
    		timesheet.setHoursDay2(day2);
    		timesheet.setHoursDay3(day3);
    		timesheet.setHoursDay4(day4);
    		timesheet.setHoursDay5(day5);
    		timesheet.setHoursDay6(day6);
    		timesheet.setHoursDay7(day7);

    		// Logic save
            JSONObject returnJSON = timesheetLogic.updateTimeSheet(getUser(req), getResourceBundle(req),
                    getSettings(req), getExtraData(req), approve, timesheet, comments);

            // Return
    		out.print(returnJSON);
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}
	
	/**
	 * Suggest Reject TimeSheet Ajax 
	 * 
	 * @param req
	 * @param resp
	 * @param a
	 * @throws IOException
	 */
	private void suggestRejectTimeSheetJX(HttpServletRequest req, HttpServletResponse resp , boolean a)
				throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		// Request
		int idTimeSheet 			 = ParamUtil.getInteger(req, "idTimeSheet",-1);
		String suggestRejectComment  = ParamUtil.getString(req, "comments","");
		
		try {
			
			// Declare logic
			TimesheetLogic timesheetLogic = new TimesheetLogic();
			
			// Logic
			timesheetLogic.suggestRejectTimeSheet(idTimeSheet, suggestRejectComment, getSettings(req), getResourceBundle(req));
			
			// Send data to client
			out.print(info(getResourceBundle(req),StringPool.SUCCESS, "msg.suggestReject", new JSONObject()));
		}
		catch(Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e);}
		finally { out.close(); }
	}
	
	/**
	 * View Time Sheet for resources
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void viewTimeSheet(HttpServletRequest req, HttpServletResponse resp, Date date) throws ServletException, IOException {

		if (date == null) { date = ParamUtil.getDate(req, "date", getDateFormat(req), new Date()); }
		
		List<Timesheet> timeSheets	= null;
		List<Operation> operations	= null;
		List<Integer> weekDays		= new ArrayList<Integer>();
		Date initWeek				= null;
		Date endWeek				= null;
		
		try {
			initWeek = DateUtil.getFirstWeekDay(date);
        	endWeek = DateUtil.getLastWeekDay(date);
			
			TimesheetLogic timesheetLogic 	= new TimesheetLogic();
			OperationLogic operationLogic 	= new OperationLogic();
			
			timeSheets = timesheetLogic.findHoursForResource(getUser(req), date, getSettings(req));
            operations = operationLogic.findByCompanyAndUser(getCompany(req), getUser(req));

			Calendar tempCal = DateUtil.getCalendar();
			tempCal.setTime(initWeek);
			for (int i = 1; i < 8;i++) {
				weekDays.add(tempCal.get(Calendar.DAY_OF_MONTH));
				tempCal.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("initWeek", getDateFormat(req).format(initWeek));
		req.setAttribute("endWeek", getDateFormat(req).format(endWeek));
		req.setAttribute("date", getDateFormat(req).format(date));
		req.setAttribute("timeSheets", timeSheets);
		req.setAttribute("operations", operations);
		req.setAttribute("weekDays", weekDays);
		req.setAttribute("sheetDate", initWeek);
		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("title", getResourceBundle(req).getString("timesheet.time_tracking"));
		
		forward("/index.jsp?nextForm=timesheet/timesheet", req, resp);
	}
}

