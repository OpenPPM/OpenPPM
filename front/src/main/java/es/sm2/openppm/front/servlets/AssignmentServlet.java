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
 * File: AssignmentServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.sm2.openppm.core.logic.impl.TimesheetLogic;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.wrap.ImputationWrap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.javabean.TeamMembersFTEs;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.JobcategoryLogic;
import es.sm2.openppm.core.logic.impl.TeamMemberLogic;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.utils.EntityComparator;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.RequestUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.CsvFile;
import es.sm2.openppm.utils.params.ParamUtil;

/**
 * Servlet implementation class AssignmentServlet
 */
public class AssignmentServlet extends AbstractGenericServlet {

	private static final long serialVersionUID = 3334829605751579755L;
	
	private static final Logger LOGGER = Logger.getLogger(AssignmentServlet.class);
	
	public final static String REFERENCE = "assignment";
	
	/***************** Actions ****************/
	public final static String VIEW_ASSIGNMENTS				= "view-assignments";
	public final static String FILTER_ASSIGNMENTS			= "filter-assignments";
	public final static String EXPORT_CAPACITY_PLANNING_CSV	= "EXPORT_CAPACITY_PLANNING_CSV";
    public final static String EXPORT_IMPUTATIONS	        = "EXPORT_IMPUTATIONS";


	
	/************** Actions AJAX **************/
	public final static String JX_FILTER_CAPACITY_PLANNING	= "ajax-filter-capacity-planning";
    public final static String JX_VIEW_IMPUTATIONS	        = "ajax-view-imputations";
	
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		super.service(req, resp);
		
		String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, VIEW_ASSIGNMENTS))) {
			
			/***************** Actions ****************/
			if (ValidateUtil.isNull(accion) || VIEW_ASSIGNMENTS.equals(accion)) { viewAssignments(req, resp); }
			else if (FILTER_ASSIGNMENTS.equals(accion)) { filterAssignments(req, resp); }
			else if (EXPORT_CAPACITY_PLANNING_CSV.equals(accion)) { exportCPCsv(req, resp); }
            else if (EXPORT_IMPUTATIONS.equals(accion)) { exportImputationsCsv(req, resp); }
			
			/************** Actions AJAX **************/
			else if (JX_FILTER_CAPACITY_PLANNING.equals(accion)) { filterCapacityPlanningJX(req, resp); }
            else if (JX_VIEW_IMPUTATIONS.equals(accion)) { viewImputationsJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Create csv to imputations
     *
     * @param req
     * @param resp
     */
    private void exportImputationsCsv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Create file
        CsvFile file = new CsvFile(Constants.SEPARATOR_CSV);

        try {

            // Get request
            Date initDate   = ParamUtil.getDate(req, "imputationDateSince", getDateFormat(req), null);
            Date endDate    = ParamUtil.getDate(req, "imputationDateUntil", getDateFormat(req), null);

            // Declare logic
            TimesheetLogic timesheetLogic = new TimesheetLogic();

            // Logic find imputations
            List<ImputationWrap> imputations = timesheetLogic.findImputations(getUser(req), initDate, endDate);

            if (ValidateUtil.isNotNull(imputations)) {

                // Generate header CSV
                //
                file.addValue(getResourceBundle(req).getString("user"));
                file.addValue(getResourceBundle(req).getString("project") + StringPool.FORWARD_SLASH + getResourceBundle(req).getString("operation"));
                file.addValue(getResourceBundle(req).getString("project.chart_label"));
                file.addValue(getResourceBundle(req).getString("hours"));

                file.newLine();

                // Generate information file
                //
                double total = 0.0;
                for (ImputationWrap imputation : imputations) {

                    file.addValue(getUser(req).getContact().getFullName());
                    file.addValue(ValidateUtil.isNotNull(imputation.getProjectName()) ? imputation.getProjectName() : imputation.getOperationName());
                    file.addValue(imputation.getShortName());
                    file.addValue(ValidateUtil.toCurrency(imputation.getHoursAPP3()));

                    file.newLine();

                    // Sum hours
                    total += imputation.getHoursAPP3();
                }

                // Add totals to file
                file.addValue(getResourceBundle(req).getString("total"));
                file.addValue(StringPool.BLANK);
                file.addValue(StringPool.BLANK);
                file.addValue(ValidateUtil.toCurrency(total));
            }
        }
        catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

        // Send file
        sendFile(req, resp, file.getFileBytes(), "Export "+getResourceBundle(req).getString("imputations") + ".csv");
    }

    /**
     * View imputations hours in APP3
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void viewImputationsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {

            // Get request
            Date initDate   = ParamUtil.getDate(req, Timesheet.INITDATE, getDateFormat(req), null);
            Date endDate    = ParamUtil.getDate(req, Timesheet.ENDDATE, getDateFormat(req), null);

            // Declare logic
            TimesheetLogic timesheetLogic = new TimesheetLogic();

            // Logic find imputations
            List<ImputationWrap> imputations = timesheetLogic.findImputations(getUser(req), initDate, endDate);

            // Constructor response
            //
            JSONArray imputationsJSON = new JSONArray();

            if (ValidateUtil.isNotNull(imputations)) {

                for (ImputationWrap imputation : imputations) {

                    JSONObject imputationJSON = new JSONObject();

                    if (ValidateUtil.isNotNull(imputation.getProjectName())) {

                        imputationJSON.put(ImputationWrap.PROJECTNAME, imputation.getProjectName());
                        imputationJSON.put(ImputationWrap.SHORTNAME, imputation.getShortName());
                    }

                    if (ValidateUtil.isNotNull(imputation.getOperationName())) {
                        imputationJSON.put(ImputationWrap.OPERATIONNAME, imputation.getOperationName());
                    }

                    imputationJSON.put(ImputationWrap.HOURSAPP3, imputation.getHoursAPP3());

                    imputationsJSON.add(imputationJSON);
                }
            }

            out.print(imputationsJSON);
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally {
            out.close();
        }
    }

    /**
	 * Export capacity planning to CSV file
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void exportCPCsv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	// Create file
    	CsvFile file = new CsvFile(Constants.SEPARATOR_CSV);
    	
    	try {
			
    		// Find parameters
    		Date since						= ParamUtil.getDate(req, Configurations.SINCE, getDateFormat(req), null);
    		Date until						= ParamUtil.getDate(req, Configurations.UNTIL, getDateFormat(req), null);
    		String type						= ParamUtil.getString(req, Configurations.TYPE,Project.ENTITY);
    		String order					= ParamUtil.getString(req, Configurations.ORDER, Constants.ASCENDENT);
    		
    		List<Integer> projects			= ParamUtil.getIntegers(req, Configurations.PROJECT, null);
    		List<Integer> jobCategories		= ParamUtil.getIntegers(req, Configurations.JOBCATEGORY, null);
    		String[] statusList				= ParamUtil.getStringValues(req, Configurations.STATUS, null);
    		
    		// Check setting activated
    		if (!SettingUtil.getBoolean(req, Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE, Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE_DEFAULT)) {
    			
    			// Default value
    			statusList = new String[] { Constants.RESOURCE_ASSIGNED };
    		}
    		
    		// Create data in format array
    		Integer[] idProjects = ValidateUtil.isNull(projects)?null:projects.toArray(new Integer[projects.size()]);
    		Integer[] idJobCategories = ValidateUtil.isNull(jobCategories)?null:jobCategories.toArray(new Integer[jobCategories.size()]);
    		
    		// Declare logic
    		TeamMemberLogic memberLogic = new TeamMemberLogic(getResourceBundle(req));
    		
    		// Format year
    		SimpleDateFormat dfYear = new SimpleDateFormat("yy");
    		
    		// Create instance of calendar for generate dates
    		Calendar sinceCal		= DateUtil.getCalendar();
			Calendar untilCal		= DateUtil.getCalendar();
			
    		sinceCal.setTime(DateUtil.getFirstWeekDay(since));
			untilCal.setTime(DateUtil.getLastWeekDay(until));

			// Instance of user logged
			Employee user = getUser(req);
			
			// Data for generate CSV
			List<TeamMembersFTEs> ftEs 	= null;
			String titleTable = null;
			
			// Group by project
			//
			if (Project.ENTITY.equals(type)) {
				
				// Find team members
				//
				List<Teammember> teammembers = memberLogic.consStaffinFtes(
						null, idProjects, null, idJobCategories, statusList,
						null, since, until, Project.IDPROJECT, order, user.getIdEmployee(), user, null, null, getSettings(req));
				
				// Generate data to print
				ftEs = memberLogic.generateFTEsMembersByProject(teammembers, sinceCal.getTime(), untilCal.getTime());
				
				// Title header of CSV
				titleTable = getResourceBundle(req).getString("project");
			}
			// Group by job category
			//
			else {
				
				// Find team members
				//
				List<Teammember> teammembers = memberLogic.consStaffinFtes(
						null, idProjects, null, idJobCategories, statusList,
						null, since, until, Jobcategory.IDJOBCATEGORY, order, user.getIdEmployee(), user, null, null, getSettings(req));
				
				if (idJobCategories == null) {
					teammembers.addAll(memberLogic.consStaffinFtes(
						null, idProjects, null, idJobCategories, statusList,
						null, since, until, null, order, user.getIdEmployee(), user, null, null, getSettings(req)));
				}
				
				// Generate data to print
				ftEs = memberLogic.generateFTEsMembersByJob(teammembers, sinceCal.getTime(), untilCal.getTime());
				
				// Title header of CSV
				titleTable = getResourceBundle(req).getString("job_category");
			}
			
			// Generate header CSV
			file.addValue(titleTable);
			while (!sinceCal.after(untilCal)) {

				int sinceDay = sinceCal.get(Calendar.DAY_OF_MONTH);
				Calendar calWeek = DateUtil.getLastWeekDay(sinceCal);
				int untilDay = calWeek.get(Calendar.DAY_OF_MONTH);
				
				file.addValue(sinceDay+"-"+untilDay +" "+getResourceBundle(req).getString("month.min_"+(calWeek.get(Calendar.MONTH)+1))+" "+dfYear.format(calWeek.getTime()));
				
				sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
			}
			
			file.newLine();
			
			// Totals of employee
			int[] totals = null;
			
			// Generate information file
			for (TeamMembersFTEs memberFte : ftEs) {

				// Legend for file
				String title = null;
				
				if (memberFte.getProject() != null) {
					
					// Group by Project
					title = memberFte.getProject().getProjectName() + " ("
							+ memberFte.getProject().getEmployeeByProjectManager().getContact().getFullName()+ ")";
				}
				else {
					
					// Group by Job Category
					file.addValue(memberFte.getMember().getJobcategory().getName());
				}
				
				// Add legend to file
				file.addValue(title);
				
				// Add information
				for (int fte : memberFte.getFtes()) {
					String fteStr = (fte > 0?fte+StringPool.BLANK:StringPool.BLANK); 
					file.addValue(fteStr);
				}
				
				file.newLine();
				
				// Initialize totals
				if (totals == null) { totals = new int[memberFte.getFtes().length]; }
				
				// Sum totals for these member
				int i = 0;
				for (int fte : memberFte.getFtes()) { totals[i++] += fte; }
			}
			
			// Add totals to file
			if (totals != null) {
				
				// Add title
				file.addValue(getResourceBundle(req).getString("total")+": "+getUser(req).getContact().getFullName());
				
				// Add totals
				for (int total : totals) {
					String fteStr = (total > 0?total+StringPool.BLANK:StringPool.BLANK); 
					file.addValue(fteStr);
				}
			}
			
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
    	// Send file
		sendFile(req, resp, file.getFileBytes(), "Export "+getResourceBundle(req).getString("menu.resource_capacity_planning") + ".csv");
	}

	/**
	 * Filter capacity planning
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void filterCapacityPlanningJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	// Declare response
    	List<TeamMembersFTEs> ftEs 	= null;
		List<String> listDates		= new ArrayList<String>();
		
    	try {
    		
    		// Find parameters
    		Date since						= ParamUtil.getDate(req, Configurations.SINCE, getDateFormat(req), null);
    		Date until						= ParamUtil.getDate(req, Configurations.UNTIL, getDateFormat(req), null);
    		String type						= ParamUtil.getString(req, Configurations.TYPE,Project.ENTITY);
    		String order					= ParamUtil.getString(req, Configurations.ORDER, Constants.ASCENDENT);
    		List<Integer> projects			= ParamUtil.getIntegersSplit(req, Configurations.PROJECT);
    		List<Integer> jobCategories		= ParamUtil.getIntegersSplit(req, Configurations.JOBCATEGORY);
    		String status					= ParamUtil.getString(req, Configurations.STATUS, null);
        	
    		// Prepare list of status
    		String[] statusList = null;
    		
    		// Check setting activated
    		if (!SettingUtil.getBoolean(req, Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE, Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE_DEFAULT)) {
    			
    			// Default value
    			statusList = new String[] { Constants.RESOURCE_ASSIGNED };
    		}
    		else if (ValidateUtil.isNotNull(status)) {
    			statusList = status.split(",");
    		}
    		
    		// Create data in format array
    		Integer[] idProjects = ValidateUtil.isNull(projects)?null:projects.toArray(new Integer[projects.size()]);
    		Integer[] idJobCategories = ValidateUtil.isNull(jobCategories)?null:jobCategories.toArray(new Integer[jobCategories.size()]);
    		
        	// Declare logic
    		TeamMemberLogic memberLogic = new TeamMemberLogic(getResourceBundle(req));
    		
    		// Format year
    		SimpleDateFormat dfYear = new SimpleDateFormat("yy");
    		
    		// Create instance of calendar for generate dates
    		Calendar sinceCal		= DateUtil.getCalendar();
			Calendar untilCal		= DateUtil.getCalendar();
			
    		sinceCal.setTime(DateUtil.getFirstWeekDay(since));
			untilCal.setTime(DateUtil.getLastWeekDay(until));
			
			// Instance of user logged
			Employee user = getUser(req);
			
			// Group by project
			//
			if (Project.ENTITY.equals(type)) {
				
				// Find team members
				//
				List<Teammember> teammembers = memberLogic.consStaffinFtes(
						null, idProjects, null, idJobCategories, statusList,
						null, since, until, Project.IDPROJECT, order, user.getIdEmployee(), user, null, null, getSettings(req));
				
				// Generate data to print
				ftEs = memberLogic.generateFTEsMembersByProject(teammembers, sinceCal.getTime(), untilCal.getTime());
				
				// Order
				Collections.sort(ftEs, new EntityComparator<TeamMembersFTEs>(new Order("project.projectName", order)));
				
				// Send title header of table
				req.setAttribute("titleTable", getResourceBundle(req).getString("project"));
			}
			// Group by job category
			//
			else {
				
				// Find team members
				//
				List<Teammember> teammembers = memberLogic.consStaffinFtes(
						null, idProjects, null, idJobCategories, statusList,
						null, since, until, Jobcategory.IDJOBCATEGORY, order, user.getIdEmployee(), user, null, null, getSettings(req));
				
				if (idJobCategories == null) {
					teammembers.addAll(memberLogic.consStaffinFtes(
						null, idProjects, null, idJobCategories, statusList,
						null, since, until, null, order, user.getIdEmployee(), user, null, null, getSettings(req)));
				}
				
				// Generate data to print
				ftEs = memberLogic.generateFTEsMembersByJob(teammembers, sinceCal.getTime(), untilCal.getTime());
				
				// Order
				Collections.sort(ftEs, new EntityComparator<TeamMembersFTEs>(new Order("title", order)));
				
				// Send title header of table
				req.setAttribute("titleTable", getResourceBundle(req).getString("job_category"));
			}
			
			// Create dates for head of table
			while (!sinceCal.after(untilCal)) {

				int sinceDay = sinceCal.get(Calendar.DAY_OF_MONTH);
				Calendar calWeek = DateUtil.getLastWeekDay(sinceCal);
				int untilDay = calWeek.get(Calendar.DAY_OF_MONTH);
				
				listDates.add(sinceDay+"-"+untilDay +" "+getResourceBundle(req).getString("month.min_"+(calWeek.get(Calendar.MONTH)+1))+" "+dfYear.format(calWeek.getTime()));
				
				sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
			}
			
			// Find configurations
			HashMap<String, String> configurations = RequestUtil.getConfigurationValues(
					req,
					Configurations.SINCE,
					Configurations.UNTIL,
					Configurations.TYPE,
					Configurations.STATUS,
					Configurations.ORDER,
					Configurations.PROJECT,
					Configurations.JOBCATEGORY
				);
			
			// Save configuration
			ConfigurationLogic configurationLogic = new ConfigurationLogic();
			configurationLogic.saveConfigurations(getUser(req), configurations, Configurations.TYPE_ASSIGNMENTS);
			
			
			// Generate totals
			int[] totals = null;
			
			if (ValidateUtil.isNotNull(ftEs)) {
				
				for (TeamMembersFTEs member : ftEs) {
					
					// Initialize totals
					if (totals == null) { totals = new int[member.getFtes().length]; }
					
					// Sum totals for these member
					int i = 0;
					for (int fte : member.getFtes()) { 
						totals[i++] += fte > 0 ? fte : 0; 
					}
				}
			}
			else if (ValidateUtil.isNotNull(listDates)) {
				
				// Initialize to list of totals
				totals = new int[listDates.size()];
			}
			
			// Add totals to request
			req.setAttribute("totals", totals);
    	}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
    	// Add data to request
		req.setAttribute("listDates", listDates);
		req.setAttribute("ftEs", ftEs);
		
		// Send data
		forward("/assignment/capacity_planning_table.ajax.jsp", req, resp);
	}

	private void filterAssignments(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// TODO metodo duplicado -> filterAssignments = viewAssignments
		
		 List<Teammember> teamMembers = null;
		 List<Project> projects = null;		 
		 Date since = ParamUtil.getDate(req, "since", getDateFormat(req), null);
		 Date until = ParamUtil.getDate(req, "until", getDateFormat(req), null);
		 
		 try {
			// Declare logic
			TeamMemberLogic teamLogic				= new TeamMemberLogic();
			ConfigurationLogic configurationLogic	= new ConfigurationLogic();
			JobcategoryLogic jobcategoryLogic		= new JobcategoryLogic();
			
			List<String> joins = new ArrayList<String>();
			joins.add(Teammember.PROJECTACTIVITY);
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT);			
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT + "." + Project.EMPLOYEEBYPROJECTMANAGER);
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT + "." + Project.EMPLOYEEBYPROJECTMANAGER + "." + Employee.CONTACT);
			
			teamMembers = teamLogic.findByEmployeeOrderByProject(getUser(req), since, until, joins);
			
			joins = new ArrayList<String>();
			joins.add(Teammember.PROJECTACTIVITY);
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT);							
			
			projects = new ArrayList<Project>();
			// Add projects for filter
			for (Teammember member : teamMembers) {
				
				if (member.getProjectactivity() != null
						&& !projects.contains(member.getProjectactivity().getProject())) {
					
					projects.add(member.getProjectactivity().getProject());
				}
			}
			
			// Order list of projects
			Collections.sort(projects, new EntityComparator<Project>(new Order(Project.PROJECTNAME, Order.ASC)));
			
			// Send configurations
			req.setAttribute("configurations", configurationLogic.findByTypes(getUser(req), Configurations.TYPE_ASSIGNMENTS));
			
			// Send data for filters
			req.setAttribute("jobs", jobcategoryLogic.findByRelation(Jobcategory.COMPANY, getCompany(req), Jobcategory.NAME, Constants.ASCENDENT));
		 }
		 catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		 
		 req.setAttribute("teamMembers", teamMembers);
		 req.setAttribute("projects", projects);				
		 req.setAttribute("title", getResourceBundle(req).getString("menu.assignments"));
		 req.setAttribute("dateIn", since);
		 req.setAttribute("dateOut", until);
		 
		 forward("/index.jsp?nextForm=assignment/assignment", req, resp);
	 }
	
	/**
	 * View assignments
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewAssignments(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<Teammember> teamMembers = null;
		List<Project> projects = null;
		Date dateIn = null;
		Date dateOut = null;

		try {

			// Declare logic
			TeamMemberLogic teamLogic				= new TeamMemberLogic();
			ConfigurationLogic configurationLogic	= new ConfigurationLogic();
			JobcategoryLogic jobcategoryLogic		= new JobcategoryLogic();
			
			List<String> joins = new ArrayList<String>();
			joins.add(Teammember.PROJECTACTIVITY);
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT);
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT + "." + Project.EMPLOYEEBYPROJECTMANAGER);
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT + "." + Project.EMPLOYEEBYPROJECTMANAGER + "." + Employee.CONTACT);

			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dateIn = sdf.parse(sdf.format(cal.getTime()));
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dateOut = sdf.parse(sdf.format(cal.getTime()));

			teamMembers = teamLogic.findByEmployeeOrderByProject(getUser(req), dateIn, dateOut, joins);

			joins = new ArrayList<String>();
			joins.add(Teammember.PROJECTACTIVITY);
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT);

			projects = new ArrayList<Project>();

			// Add projects for filter
			for (Teammember member : teamMembers) {

				if (member.getProjectactivity() != null && !projects.contains(member.getProjectactivity().getProject())) {

					projects.add(member.getProjectactivity().getProject());
				}
			}

			// Order list of projects
			Collections.sort(projects, new EntityComparator<Project>(new Order(Project.PROJECTNAME, Order.ASC)));

			// Send configurations
			req.setAttribute("configurations", configurationLogic.findByTypes(getUser(req), Configurations.TYPE_ASSIGNMENTS));
			
			// Send data for filters
			req.setAttribute("jobs", jobcategoryLogic.findByRelation(Jobcategory.COMPANY, getCompany(req), Jobcategory.NAME, Constants.ASCENDENT));
		}
		catch (Exception e) {
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}

		req.setAttribute("teamMembers", teamMembers);
		req.setAttribute("projects", projects);
		req.setAttribute("title", getResourceBundle(req).getString("menu.assignments"));
		req.setAttribute("dateIn", dateIn);
		req.setAttribute("dateOut", dateOut);

		forward("/index.jsp?nextForm=assignment/assignment", req, resp);
	}
}