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
 * File: ResourceServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.comparator.WorkloadComparator;
import es.sm2.openppm.core.javabean.CapacityPlanningDetail;
import es.sm2.openppm.core.javabean.DatoColumna;
import es.sm2.openppm.core.javabean.FiltroTabla;
import es.sm2.openppm.core.javabean.ResourceCapacityRunning;
import es.sm2.openppm.core.javabean.TeamMembersFTEs;
import es.sm2.openppm.core.logic.impl.CapacityPlanningLogic;
import es.sm2.openppm.core.logic.impl.CategoryLogic;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.JobcategoryLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.ResourceCapacityRunningLogic;
import es.sm2.openppm.core.logic.impl.ResourcepoolLogic;
import es.sm2.openppm.core.logic.impl.SellerLogic;
import es.sm2.openppm.core.logic.impl.SkillLogic;
import es.sm2.openppm.core.logic.impl.TeamMemberLogic;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Jobcatemployee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Skill;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.utils.EntityComparator;
import es.sm2.openppm.core.utils.JSONModelUtil;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.PaginacionUtil;
import es.sm2.openppm.front.utils.RequestUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.HtmlUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.CsvFile;
import es.sm2.openppm.utils.json.Exclusion;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Servlet implementation class ResourceManagerServlet
 */
public class ResourceServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(ResourceServlet.class);
	
	public final static String REFERENCE = "resource";
	
	/***************** Actions ****************/
	public final static String VIEW_ASSIGNATIONS				= "view-assignations";
	public final static String VIEW_RESOURCE_POOL				= "view-resource-pool";
	public final static String VIEW_CAPACITY_PLANNING			= "view-capacity-planning";
	public final static String VIEW_CAPACITY_RUNNING			= "view-capacity-running";
	public final static String CAPACITY_PLANNING_TO_CSV			= "capacity-planning-to-csv";
	public final static String CAPACITY_PLANNING_DETAIL_TO_CSV	= "capacity-planning-detail-to-csv";
	public final static String CAPACITY_RUNNING_TO_CSV			= "capacity-running-to-csv";
	
	/************** Actions AJAX **************/
	public final static String JX_FILTER_ASSIGNATIONS						= "ajax-filter-assignations";
	public final static String JX_APPROVE_RESOURCE							= "ajax-approve-resource";
	public final static String JX_REJECT_RESOURCE							= "ajax-reject-resource";
	public final static String JX_PROPOPSED_RESOURCE						= "ajax-proposed-resource";
	public final static String JX_VIEW_RESOURCE								= "ajax-view-resource";
	public final static String JX_UPDATE_CAPACITY_PLANNING					= "ajax-update-capacity-planning";
	public final static String JX_UPDATE_CAPACITY_RUNNING					= "ajax-update-capacity-running";
	public final static String JX_VIEW_CAPACITY_PLANNING_RESOURCE			= "ajax-view-capacity-planning-resource";
	public final static String JX_VIEW_CAPACITY_PLANNING_RESOURCE_DETAIL	= "ajax-view-capacity-planning-resource-detail";
	public final static String JX_VIEW_CAPACITY_RUNNING_RESOURCE			= "ajax-view-capacity-running-resource";
	public final static String JX_FILTER_RESOURCE_POOL						= "ajax-filter-resource-pool";
    
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);

		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, REFERENCE))) {
			/***************** Actions ****************/
			if (ValidateUtil.isNull(accion) || VIEW_ASSIGNATIONS.equals(accion)) { viewAssignations(req, resp); }
			else if (VIEW_RESOURCE_POOL.equals(accion)) { viewResourcePool(req, resp); }
			else if (VIEW_CAPACITY_PLANNING.equals(accion)) { viewCapacityPlanning(req, resp); }
			else if (VIEW_CAPACITY_RUNNING.equals(accion)) { viewCapacityRunning(req, resp); }
			else if (CAPACITY_PLANNING_TO_CSV.equals(accion)) { capacityPlaningToCsv(req, resp); }
			else if (CAPACITY_PLANNING_DETAIL_TO_CSV.equals(accion)) { capacityPlaningDetailToCsv(req, resp); }
			else if (CAPACITY_RUNNING_TO_CSV.equals(accion)) { capacityRunningToCsv(req, resp); }
			
			/************** Actions AJAX **************/
			
			else if (JX_FILTER_ASSIGNATIONS.equals(accion)) { filterAssignationsJX(req, resp); }
			else if (JX_APPROVE_RESOURCE.equals(accion)) { approveResourceJX(req, resp); }
			else if (JX_REJECT_RESOURCE.equals(accion)) { rejectResourceJX(req, resp); }
			else if (JX_VIEW_RESOURCE.equals(accion)) { viewResourceJX(req, resp); }
			else if (JX_PROPOPSED_RESOURCE.equals(accion)) { proposedResourceJX(req, resp); }
			else if (JX_UPDATE_CAPACITY_PLANNING.equals(accion)) {updateCapacityPlanningJX(req, resp); }
			else if (JX_UPDATE_CAPACITY_RUNNING.equals(accion)) { updateCapacityRunningJX(req, resp); }
			else if (JX_VIEW_CAPACITY_PLANNING_RESOURCE.equals(accion)) { updateCapacityPlanningResourceJX(req, resp); }
			else if (JX_VIEW_CAPACITY_PLANNING_RESOURCE_DETAIL.equals(accion)) { updateCapacityPlanningResourceDetailJX(req, resp); }
			else if (JX_VIEW_CAPACITY_RUNNING_RESOURCE.equals(accion)) { updateCapacityRunningResourceJX(req, resp); }
			else if (JX_FILTER_RESOURCE_POOL.equals(accion)) { filterResourcePoolJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Capacity Planning Resource Detail
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void updateCapacityPlanningResourceDetailJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	List<TeamMembersFTEs> ftEs 							 = null;
		List<String> listDates								 = null;
		List<CapacityPlanningDetail> capacityPlanningDetails = null;
		
    	try {
    		// Request
    		Date since					= ParamUtil.getDate(req, "dateIn", getDateFormat(req), null);
    		Date until					= ParamUtil.getDate(req, "dateOut", getDateFormat(req), null);
    		Integer idTeammember 		= ParamUtil.getInteger(req, "idResource", -1);

    		if (idTeammember != -1) {
    			
    			// Declare logics
    			CapacityPlanningLogic capacityPlanningLogic = new CapacityPlanningLogic();
    			
    			TeamMemberLogic teamMemberLogic = new TeamMemberLogic();
    			
    			List<String> joins = new ArrayList<String>();
    			joins.add(Teammember.EMPLOYEE);
    			
    			Teammember resource = teamMemberLogic.findById(idTeammember, joins);
    			
    			// Set unique employee
    			Integer[] idsEmployees = new Integer[1];
    			idsEmployees[0] = resource.getEmployee().getIdEmployee();
    			
    			// Filter by Status
    			String[] statusList = new String[2];
    			statusList[0] = Constants.RESOURCE_ASSIGNED;
    			statusList[1] = Constants.RESOURCE_PRE_ASSIGNED;
    			
    			// Logic
    			ftEs = capacityPlanningLogic.capacityPlanningDetail(idsEmployees, since, until, statusList, getSettings(req), getUser(req));
    			
    			if (ValidateUtil.isNotNull(ftEs)) {
    				// Set title weeks
    				listDates = getListDates(since, until, getResourceBundle(req));
    			}
    			
    			// Parse
    			capacityPlanningDetails = capacityPlanningLogic.parse(ftEs);
    		}
    	}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
    	// Response
		req.setAttribute("listDates", listDates);
		req.setAttribute("capacityPlanningDetails", capacityPlanningDetails);
		forward("/resource/assignations/capacity_planning.ajax.jsp", req, resp);
	}

    /**
     * Get list weeks for dates
     * 
     * @param since
     * @param until
     * @param resourceBundle 
     * @return
     */
	private List<String> getListDates(Date since, Date until, ResourceBundle resourceBundle) {
		
		SimpleDateFormat dfYear = new SimpleDateFormat("yy");
		List<String> listDates	= new ArrayList<String>();
		
		// Dates adjust
		Calendar sinceCal		= DateUtil.getCalendar();
		Calendar untilCal		= DateUtil.getCalendar();
		
		if (since == null) {
			since = DateUtil.getFirstMonthDay(new Date());
		}
		
		if (until == null ) {
			
			untilCal.add(Calendar.MONTH, +1);
			
			until = DateUtil.getLastMonthDay(untilCal.getTime());
		}
		
		sinceCal.setTime(DateUtil.getFirstWeekDay(since));
		untilCal.setTime(DateUtil.getLastWeekDay(until));
		
		// Set title weeks
		while (!sinceCal.after(untilCal)) {
			
			int sinceDay = sinceCal.get(Calendar.DAY_OF_MONTH);
			
			Calendar calWeek = DateUtil.getLastWeekDay(sinceCal);
			
			int untilDay = calWeek.get(Calendar.DAY_OF_MONTH);
			
			listDates.add(sinceDay+"-"+untilDay +" "+resourceBundle.getString("month.min_"+(calWeek.get(Calendar.MONTH)+1))+" "+dfYear.format(calWeek.getTime()));
			
			sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
		}
		
		return listDates;
	}

	/**
     * Export capacity planning detail to csv
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void capacityPlaningDetailToCsv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	String idsEmpl				= ParamUtil.getString(req, "idsEmployees", null);
    	Integer[] idsEmployees 		= StringUtil.splitStrToIntegers(idsEmpl, null);
    	Date since					= ParamUtil.getDate(req, "since", getDateFormat(req), null);
    	Date until					= ParamUtil.getDate(req, "until", getDateFormat(req), null);
    	String[] statusList			= ParamUtil.getStringValues(req, "filter", null);
    	
    	CsvFile file 				= new CsvFile(Constants.SEPARATOR_CSV);
    	SimpleDateFormat dfYear 	= new SimpleDateFormat("yy");
    	
    	try {
			// Declare logics
			CapacityPlanningLogic capacityPlanningLogic = new CapacityPlanningLogic();
			
			// Logic
			List<TeamMembersFTEs> ftEs = capacityPlanningLogic.capacityPlanningDetail(idsEmployees, since, until, statusList, getSettings(req), getUser(req));
			
			if (ValidateUtil.isNotNull(ftEs)) {
				
				// Generate CSV
				//
				file.addValue(getResourceBundle(req).getString("contact.fullname"));
				
				file.addValue(getResourceBundle(req).getString("project"));
				
				file.addValue(getResourceBundle(req).getString("activity"));
				
				file.addValue(getResourceBundle(req).getString("status"));
				
				// Dates adjust
				Calendar sinceCal		= DateUtil.getCalendar();
				Calendar untilCal		= DateUtil.getCalendar();
				
				if (since == null) {
					since = DateUtil.getFirstMonthDay(new Date());
				}
				
				if (until == null ) {
					
					untilCal.add(Calendar.MONTH, +1);
					
					until = DateUtil.getLastMonthDay(untilCal.getTime());
				}
				
				sinceCal.setTime(DateUtil.getFirstWeekDay(since));
				untilCal.setTime(DateUtil.getLastWeekDay(until));
				
				// Set title weeks
				while (!sinceCal.after(untilCal)) {
					
					int sinceDay = sinceCal.get(Calendar.DAY_OF_MONTH);
					
					Calendar calWeek = DateUtil.getLastWeekDay(sinceCal);
					
					int untilDay = calWeek.get(Calendar.DAY_OF_MONTH);
					
					file.addValue(sinceDay+"-"+untilDay +" "+getResourceBundle(req).getString("month.min_"+(calWeek.get(Calendar.MONTH)+1))+" "+dfYear.format(calWeek.getTime()));
					
					sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
				}
				
				file.newLine();
				
				if (ValidateUtil.isNotNull(ftEs)) {
					
					for (TeamMembersFTEs memberFte : ftEs) {
						
						// Set employee
						if (memberFte.getMember() != null && memberFte.getMember().getEmployee() != null 
								&& memberFte.getMember().getEmployee().getContact() != null
								&& ValidateUtil.isNotNull(memberFte.getMember().getEmployee().getContact().getFullName())) {
							
							file.addValue(memberFte.getMember().getEmployee().getContact().getFullName());
						}
						else {
							file.addValue(StringPool.BLANK);
						}
						
						// Set project
						if (memberFte.getProject() != null && ValidateUtil.isNotNull(memberFte.getProject().getProjectName())) {
							file.addValue(memberFte.getProject().getProjectName());
						}
						else {
							file.addValue(StringPool.BLANK);
						}
						
						// Set activity
						if (memberFte.getMember() != null 
								&& memberFte.getMember().getProjectactivity() != null 
								&& ValidateUtil.isNotNull(memberFte.getMember().getProjectactivity().getActivityName())) {
							
							file.addValue(memberFte.getMember().getProjectactivity().getActivityName());
						}
						else {
							file.addValue(StringPool.BLANK);
						}
						
						// Set status
						if (memberFte.getMember() != null && ValidateUtil.isNotNull(memberFte.getMember().getStatus())) {
							file.addValue(getResourceBundle(req).getString("resource."+memberFte.getMember().getStatus()));
						}
						else {
							file.addValue(StringPool.BLANK);
						}
						
						// Set percents
						for (int fte : memberFte.getFtes()) {
							String fteStr = (fte > 0?fte+StringPool.BLANK:StringPool.BLANK); 
							file.addValue(fteStr);
						}
						
						file.newLine();
					}
				}
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		sendFile(req, resp, file.getFileBytes(), "Export "+getResourceBundle(req).getString("menu.resource_capacity_planning_detail") + ".csv");
	}

	/**
     * Update capacity planning resource
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void updateCapacityRunningResourceJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	Date since					= ParamUtil.getDate(req, "since", getDateFormat(req), null);
    	Date until					= ParamUtil.getDate(req, "until", getDateFormat(req), null);
    	String type					= ParamUtil.getString(req, "resourceType",Project.ENTITY);
    	Integer idEmployee 			= ParamUtil.getInteger(req, "idEmployee", -1);
    	
		ResourceCapacityRunning resourceCapacityRunning = new ResourceCapacityRunning();
		
    	try {
    		
    		// Declare logic
    		ResourceCapacityRunningLogic resourceCapacityRunningLogic = new ResourceCapacityRunningLogic();
    		
			if (Project.ENTITY.equals(type)) {
				
				// Logic
				resourceCapacityRunning = resourceCapacityRunningLogic.updateCapacityRunningResourceByProject(
						idEmployee,
						since,
						until,
						getResourceBundle(req),
                        getUser(req),
                        getSettings(req)
						);
				
				req.setAttribute("titleTable", getResourceBundle(req).getString("project") + StringPool.SPACE + 
						StringPool.FORWARD_SLASH +  StringPool.SPACE + getResourceBundle(req).getString("operation"));
			}
			else {
				
				// Logic
				resourceCapacityRunning = resourceCapacityRunningLogic.updateCapacityRunningResourceByJobCategory(
						idEmployee,
						since,
						until,
						getResourceBundle(req),
                        getUser(req),
                        getSettings(req)
						);
				
				req.setAttribute("titleTable", getResourceBundle(req).getString("job_category"));
			}
    	}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("listDates", resourceCapacityRunning.getListDates());
		req.setAttribute("ftEs", resourceCapacityRunning.getFtEs());
		
		forward("/resource/capacity_running/staffing_table_popup.ajax.jsp", req, resp);
	}

	/**
     * Capacity running to CSV
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void capacityRunningToCsv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
		Integer[] idProjects		= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.PROJECT), null);
		Integer[] idPMs				= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_PM), null);
		Integer[] idJobCategories	= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.JOBCATEGORY), null);
		Integer[] idResourcePools	= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_RESOURCE_POOL), null);
		Integer[] idSellers			= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_SELLERS), null);
		Integer[] idCategories		= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_CATEGORIES), null);
    	String fullName				= ParamUtil.getString(req, Contact.FULLNAME, StringPool.BLANK);
    	Date since					= ParamUtil.getDate(req, "since", getDateFormat(req), null);
    	Date until					= ParamUtil.getDate(req, "until", getDateFormat(req), null);
    	String order				= ParamUtil.getString(req, "orderName", Constants.ASCENDENT);
    	
    	CsvFile file 				= new CsvFile(Constants.SEPARATOR_CSV);
    	List<TeamMembersFTEs> ftEs 	= null;
    	
    	ResourceCapacityRunning resourceCapacityRunning = new ResourceCapacityRunning();
    	
    	try {
			
    		// Declare logic
    		ResourceCapacityRunningLogic resourceCapacityRunningLogic = new ResourceCapacityRunningLogic();
    		
			// For Project Manager 
			if (SecurityUtil.isUserInRole(req, Constants.ROLE_PM)) {
				
				idPMs = new Integer[1];
				
				idPMs[0] = getUser(req).getIdEmployee();
			}
    		
    		// Logic
    		resourceCapacityRunning = resourceCapacityRunningLogic.updateCapacityRunning(
    				idProjects,
    				idPMs,
    				idJobCategories,
    				idSellers,
    				idResourcePools,
    				idCategories,
    				fullName,
    				since,
    				until,
    				order,
    				getResourceBundle(req),
    				getUser(req),
    				Boolean.TRUE
    				);
    		
    		if (resourceCapacityRunning != null && ValidateUtil.isNotNull(resourceCapacityRunning.getFtEs())) {
    			
    			ftEs = resourceCapacityRunning.getFtEs();
    			
    			// Generate CSV
    			//
    			SimpleDateFormat dfYear = new SimpleDateFormat("yy");
    			
    			// Dates
        		//
        		Calendar sinceCal		= DateUtil.getCalendar();
    			Calendar untilCal		= DateUtil.getCalendar();
    			
    			if (since == null) {
    				since = DateUtil.getFirstMonthDay(new Date());
    			}
    			
    			if (until == null ) {
    				
    				untilCal.add(Calendar.MONTH, +1);
    				
    				until = DateUtil.getLastMonthDay(untilCal.getTime());
    			}
    			
    			sinceCal.setTime(DateUtil.getFirstWeekDay(since));
    			untilCal.setTime(DateUtil.getLastWeekDay(until));
    			
    			// Add values
    			file.addValue(getResourceBundle(req).getString("contact.fullname"));
    			
    			while (!sinceCal.after(untilCal)) {
    				
    				int sinceDay = sinceCal.get(Calendar.DAY_OF_MONTH);
    				Calendar calWeek = DateUtil.getLastWeekDay(sinceCal);
    				int untilDay = calWeek.get(Calendar.DAY_OF_MONTH);
    				
    				file.addValue(sinceDay+"-"+untilDay +" "+getResourceBundle(req).getString("month.min_"+(calWeek.get(Calendar.MONTH)+1))+" "+dfYear.format(calWeek.getTime()));
    				file.addValue(StringPool.BLANK);
    				
    				sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
    			}
    			
    			file.newLine();
    			
    			for (TeamMembersFTEs memberFte : ftEs) {
    				
    				file.addValue(memberFte.getMember().getEmployee().getContact().getFullName());
    				
    				int iterate = 0;
    				for (int fte : memberFte.getFtes()) {
    					
    					String fteStr = (fte > 0 ? fte + StringPool.BLANK : StringPool.BLANK);
    					
    					file.addValue(fteStr);
    					
    					String hours = memberFte.getListHours() != null && memberFte.getListHours()[iterate] > 0 ? memberFte.getListHours()[iterate] + StringPool.BLANK : StringPool.BLANK;
    					
    					file.addValue(hours);
    					
    					iterate ++;
    				}
    				
    				file.newLine();
    			}
    		}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		sendFile(req, resp, file.getFileBytes(), "Export "+getResourceBundle(req).getString("menu.resource_capacity_running") + ".csv");
	}

	/**
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void updateCapacityRunningJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	// Request
		Integer[] idProjects		= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.PROJECT), null);
		Integer[] idPMs				= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_PM), null);
		Integer[] idJobCategories	= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.JOBCATEGORY), null);
		Integer[] idResourcePools	= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_RESOURCE_POOL), null);
		Integer[] idSellers			= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_SELLERS), null);
		Integer[] idCategories		= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_CATEGORIES), null);
    	String fullName				= ParamUtil.getString(req, Contact.FULLNAME, StringPool.BLANK);
    	Date since					= ParamUtil.getDate(req, "since", getDateFormat(req), null);
    	Date until					= ParamUtil.getDate(req, "until", getDateFormat(req), null);
    	String order				= ParamUtil.getString(req, "orderName", Constants.ASCENDENT);
    	
    	ResourceCapacityRunning resourceCapacityRunning = new ResourceCapacityRunning();
    	
    	try {
    		
    		// Find configurations
    		HashMap<String, String> configurations = RequestUtil.getConfigurationValues(
    				req,
    				Configurations.PROJECT,
    				Configurations.LIST_FILTERS_PM,
    				Configurations.JOBCATEGORY,
    				Configurations.LIST_FILTERS_RESOURCE_POOL,
    				Configurations.LIST_FILTERS_SELLERS,
    				Configurations.LIST_FILTERS_CATEGORIES
    			);
    		
    		// Save configuration
    		ConfigurationLogic configurationLogic = new ConfigurationLogic();
    		configurationLogic.saveConfigurations(getUser(req), configurations, Configurations.TYPE_CAPACITY_RUNNING);
    		
    		// Declare logic
    		ResourceCapacityRunningLogic resourceCapacityRunningLogic = new ResourceCapacityRunningLogic();
    		
			// For Project Manager 
			if (SecurityUtil.isUserInRole(req, Constants.ROLE_PM)) {
				
				idPMs = new Integer[1];
				
				idPMs[0] = getUser(req).getIdEmployee();
			}
			
    		// Logic
    		resourceCapacityRunning = resourceCapacityRunningLogic.updateCapacityRunning(
    				idProjects,
    				idPMs,
    				idJobCategories,
    				idSellers,
    				idResourcePools,
    				idCategories,
    				fullName,
    				since,
    				until,
    				order,
    				getResourceBundle(req),
    				getUser(req),
    				Boolean.TRUE
    				);
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
    	if (resourceCapacityRunning != null && ValidateUtil.isNotNull(resourceCapacityRunning.getFtEs())) {
    		
    		// Response
    		req.setAttribute("listDates", resourceCapacityRunning.getListDates());
    		req.setAttribute("ftEs", resourceCapacityRunning.getFtEs());
    	}
		
		forward("/resource/capacity_running/staffing_table.ajax.jsp", req, resp);
	}

	/**
     * Update capacity planning resource
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void updateCapacityPlanningResourceJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
    	Date since					= ParamUtil.getDate(req, "since", getDateFormat(req), null);
    	Date until					= ParamUtil.getDate(req, "until", getDateFormat(req), null);
    	String type					= ParamUtil.getString(req, "resourceType",Project.ENTITY);
    	Integer idEmployee 			= ParamUtil.getInteger(req, "idEmployee", -1);
    	String[] statusList			= ParamUtil.getStringValues(req, "filter", null);
    
    	List<TeamMembersFTEs> ftEs 	= null;
		List<String> listDates		= new ArrayList<String>();
		
    	try {
    		
    		TeamMemberLogic memberLogic = new TeamMemberLogic(getResourceBundle(req));
    		
    		SimpleDateFormat dfYear = new SimpleDateFormat("yy");
    		
    		// Dates
    		//
    		Calendar sinceCal		= DateUtil.getCalendar();
			Calendar untilCal		= DateUtil.getCalendar();
			
			if (since == null) {
				since = DateUtil.getFirstMonthDay(new Date());
			}
			
			if (until == null ) {
				
				untilCal.add(Calendar.MONTH, +1);
				
				until = DateUtil.getLastMonthDay(untilCal.getTime());
			}
			
			sinceCal.setTime(DateUtil.getFirstWeekDay(since));
			untilCal.setTime(DateUtil.getLastWeekDay(until));
			
			// Resource by project 
			//
			if (Project.ENTITY.equals(type)) {
				
				// Joins
				List<String> joins = new ArrayList<String>();
				joins.add(Teammember.PROJECTACTIVITY+"."+Projectactivity.PROJECT+"."+Project.EMPLOYEEBYPROJECTMANAGER);
				joins.add(Teammember.PROJECTACTIVITY+"."+Projectactivity.PROJECT+"."+Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);

				// Logic
				List<Teammember> teammembers = memberLogic.capacityPlanningResource(idEmployee, since, until, statusList, joins, getSettings(req), getUser(req));
				
				// Parse
				ftEs = memberLogic.generateFTEsMembersByProject(teammembers, sinceCal.getTime(), untilCal.getTime());
				
				req.setAttribute("titleTable", getResourceBundle(req).getString("project"));
			}
			// Resource by job category 
			//
			else {
				
				// Joins
				List<String> joins = new ArrayList<String>();
				joins.add(Teammember.JOBCATEGORY);
				
				// Logic
				List<Teammember> teammembers = memberLogic.capacityPlanningResource(idEmployee, since, until, statusList, joins, getSettings(req), getUser(req));
				
				// Parse
				ftEs = memberLogic.generateFTEsMembersByJob(teammembers, sinceCal.getTime(), untilCal.getTime());
				
				req.setAttribute("titleTable", getResourceBundle(req).getString("job_category"));
			}
			
			// Dates
			//
			while (!sinceCal.after(untilCal)) {

				int sinceDay = sinceCal.get(Calendar.DAY_OF_MONTH);
				Calendar calWeek = DateUtil.getLastWeekDay(sinceCal);
				int untilDay = calWeek.get(Calendar.DAY_OF_MONTH);
				
				listDates.add(sinceDay+"-"+untilDay +" "+getResourceBundle(req).getString("month.min_"+(calWeek.get(Calendar.MONTH)+1))+" "+dfYear.format(calWeek.getTime()));
				
				sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
			}
    	}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("listDates", listDates);
		req.setAttribute("ftEs", ftEs);
		
		forward("/resource/capacity_planning/staffing_table_popup.ajax.jsp", req, resp);
	}

	/**
     * Capacity Planning to CSV
     * 
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void capacityPlaningToCsv(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		Integer[] idProjects		= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.PROJECT), null);
		Integer[] idPMs				= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_PM), null);
		Integer[] idJobCategories	= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.JOBCATEGORY), null);
		Integer[] idResourcePools	= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_RESOURCE_POOL), null);
		Integer[] idSellers			= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_SELLERS), null);
		Integer[] idCategories		= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_CATEGORIES), null);
    	String[] statusList			= ParamUtil.getStringValues(req, "filter", null);
    	String fullName				= ParamUtil.getString(req, Contact.FULLNAME, StringPool.BLANK);
    	Date since					= ParamUtil.getDate(req, "since", getDateFormat(req), null);
    	Date until					= ParamUtil.getDate(req, "until", getDateFormat(req), null);
    	String order				= ParamUtil.getString(req, "orderName", Constants.ASCENDENT);
    	
    	CsvFile file 				= new CsvFile(Constants.SEPARATOR_CSV);
    	
    	try {
			
    		TeamMemberLogic memberLogic = new TeamMemberLogic();
    		
    		SimpleDateFormat dfYear = new SimpleDateFormat("yy");
    		
    		// Dates
    		//
    		Calendar sinceCal		= DateUtil.getCalendar();
			Calendar untilCal		= DateUtil.getCalendar();
			
			if (since == null) {
				since = DateUtil.getFirstMonthDay(new Date());
			}
			
			if (until == null ) {
				
				untilCal.add(Calendar.MONTH, +1);
				
				until = DateUtil.getLastMonthDay(untilCal.getTime());
			}
			
			sinceCal.setTime(DateUtil.getFirstWeekDay(since));
			untilCal.setTime(DateUtil.getLastWeekDay(until));

			// For Project Manager 
			if (SecurityUtil.isUserInRole(req, Constants.ROLE_PM)) {
				
				idPMs = new Integer[1];
				
				idPMs[0] = getUser(req).getIdEmployee();
			}

			List<Teammember> teammembers = memberLogic.consStaffinFtes(
					idResourcePools, idProjects, idPMs, idJobCategories, statusList,
					fullName, since, until, Contact.FULLNAME, order, null, getUser(req), idSellers, idCategories, getSettings(req));
			
			List<TeamMembersFTEs> ftEs = memberLogic.generateFTEsMembers(teammembers, sinceCal.getTime(), untilCal.getTime());
			
			// Generate CSV
			file.addValue(getResourceBundle(req).getString("contact.fullname"));
			
			while (!sinceCal.after(untilCal)) {

				int sinceDay = sinceCal.get(Calendar.DAY_OF_MONTH);
				Calendar calWeek = DateUtil.getLastWeekDay(sinceCal);
				int untilDay = calWeek.get(Calendar.DAY_OF_MONTH);
				
				file.addValue(sinceDay+"-"+untilDay +" "+getResourceBundle(req).getString("month.min_"+(calWeek.get(Calendar.MONTH)+1))+" "+dfYear.format(calWeek.getTime()));
				
				sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
			}
			
			file.newLine();
			
			for (TeamMembersFTEs memberFte : ftEs) {
				
				file.addValue(memberFte.getMember().getEmployee().getContact().getFullName());
				
				for (int fte : memberFte.getFtes()) {
					String fteStr = (fte > 0?fte+StringPool.BLANK:StringPool.BLANK); 
					file.addValue(fteStr);
				}
				file.newLine();
			}
			
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		sendFile(req, resp, file.getFileBytes(), "Export "+getResourceBundle(req).getString("menu.resource_capacity_planning") + ".csv");
	}

	/**
     * Update table Capacity Planning
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
	private void updateCapacityPlanningJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Integer[] idProjects		= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.PROJECT), null);
            Integer[] idPMs				= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_PM), null);
            Integer[] idJobCategories	= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.JOBCATEGORY), null);
            Integer[] idResourcePools	= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_RESOURCE_POOL), null);
            Integer[] idSellers			= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_SELLERS), null);
            Integer[] idCategories		= StringUtil.splitStrToIntegers(ParamUtil.getString(req, Configurations.LIST_FILTERS_CATEGORIES), null);
            String[] statusList			= ParamUtil.getStringValues(req, "filter", null);
            String fullName				= ParamUtil.getString(req, Contact.FULLNAME, StringPool.BLANK);
            Date since					= ParamUtil.getDate(req, "since", getDateFormat(req), null);
            Date until					= ParamUtil.getDate(req, "until", getDateFormat(req), null);
            String order				= ParamUtil.getString(req, "orderName", Constants.ASCENDENT);

            List<TeamMembersFTEs> ftEs 	= null;
            List<String> listDates		= new ArrayList<String>();

    		// Find configurations
    		HashMap<String, String> configurations = RequestUtil.getConfigurationValues(
    				req,
    				Configurations.PROJECT,
    				Configurations.LIST_FILTERS_PM,
    				Configurations.JOBCATEGORY,
    				Configurations.LIST_FILTERS_RESOURCE_POOL,
    				Configurations.LIST_FILTERS_SELLERS,
    				Configurations.LIST_FILTERS_CATEGORIES
    			);
    		
    		// Save configuration
    		ConfigurationLogic configurationLogic = new ConfigurationLogic();
    		configurationLogic.saveConfigurations(getUser(req), configurations, Configurations.TYPE_CAPACITY_PLANNING);
    		
    		// Declare logic
    		TeamMemberLogic memberLogic = new TeamMemberLogic();
    		
    		SimpleDateFormat dfYear = new SimpleDateFormat("yy");
    		
    		// Dates
    		//
    		Calendar sinceCal		= DateUtil.getCalendar();
			Calendar untilCal		= DateUtil.getCalendar();
			
			if (since == null) {
				since = DateUtil.getFirstMonthDay(new Date());
			}
			
			if (until == null ) {

				// If until date is not set, until date will be 1 month after since date
				untilCal.setTime(since);

				untilCal.add(Calendar.MONTH, +1);

				until = DateUtil.getLastMonthDay(untilCal.getTime());

			}
			
			sinceCal.setTime(DateUtil.getFirstWeekDay(since));
			untilCal.setTime(DateUtil.getLastWeekDay(until));
			
			// For Project Manager 
			if (SecurityUtil.isUserInRole(req, Constants.ROLE_PM)) {
				
				idPMs = new Integer[1];
				
				idPMs[0] = getUser(req).getIdEmployee();
			}

            // For RM
            if (SecurityUtil.isUserInRole(req, Constants.ROLE_RM) && !ValidateUtil.isNotNull(idResourcePools)) {

                ResourcepoolLogic resourcepoolLogic = new ResourcepoolLogic();

                List<Resourcepool> resourcePools = resourcepoolLogic.findByResourceManager(getUser(req));

                if (ValidateUtil.isNotNull(resourcePools)) {

                    idResourcePools = new Integer[resourcePools.size()];

                    for (int i = 0; i < resourcePools.size(); i++) {
                        idResourcePools[i] = resourcePools.get(i).getIdResourcepool();
                    }
                }
            }

			// FTEs
			//
			List<Teammember> teammembers = memberLogic.consStaffinFtes(
					idResourcePools, idProjects, idPMs, idJobCategories, statusList,
					fullName, since, until, Contact.FULLNAME, order, null, getUser(req), idSellers, idCategories, getSettings(req));

            // Get all resources(assigned and unassigned) for RM
            //
            boolean showUnassigned = false;

            // Search unassigned
            if (ValidateUtil.isNotNull(statusList)) {

                for (String status : statusList) {

                    if (Constants.RESOURCE_UNASSIGNED.equals(status)) {
                        showUnassigned = true;
                    }
                }
            }

            if (showUnassigned) {
                teammembers = memberLogic.addUnassigneds(teammembers, since, until, fullName, order, getUser(req), idResourcePools, idJobCategories);
            }

            // Calculate ftes
			ftEs = memberLogic.generateFTEsMembers(teammembers, sinceCal.getTime(), untilCal.getTime());

			//Use WORKLOAD_AVERAGE order if specified in the filter
			if(order.equals(Constants.WORKLOAD_AVERAGE)) {
				Collections.sort(ftEs, new WorkloadComparator());
			}

			// Dates 
			//
			while (!sinceCal.after(untilCal)) {

				int sinceDay = sinceCal.get(Calendar.DAY_OF_MONTH);
				Calendar calWeek = DateUtil.getLastWeekDay(sinceCal);
				int untilDay = calWeek.get(Calendar.DAY_OF_MONTH);
				
				listDates.add(sinceDay+"-"+untilDay +" "+getResourceBundle(req).getString("month.min_"+(calWeek.get(Calendar.MONTH)+1))+" "+dfYear.format(calWeek.getTime()));
				
				sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
			}

            if (ValidateUtil.isNotNull(ftEs)) {

                req.setAttribute("listDates", listDates);
                req.setAttribute("ftEs", ftEs);
            }
        }
		catch (Exception e) {
            ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
        }

		forward("/resource/capacity_planning/staffing_table.ajax.jsp", req, resp);
	}

	/**
     * View resource pool
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void viewCapacityPlanning(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
    	List<Employee> projectManagers		= null;
    	List<Project> projects				= null;
		List<Jobcategory> jobs				= null;
		List<Resourcepool> resourcePools	= null;
		List<Seller> sellers				= null;
		List<Category> categories			= null;
		String firstMonthDay 				= StringPool.BLANK;
		String lastMonthDay 				= StringPool.BLANK; 
		
		try {
			
			// Declare logics
			ProjectLogic projectLogic				= new ProjectLogic(getSettings(req), getResourceBundle(req));
			JobcategoryLogic jobcategoryLogic		= new JobcategoryLogic();
	    	EmployeeLogic employeeLogic				= new EmployeeLogic();
	    	ResourcepoolLogic resourcepoolLogic		= new ResourcepoolLogic();
	    	SellerLogic sellerLogic					= new SellerLogic();
	    	CategoryLogic categoryLogic 			= new CategoryLogic();
	    	ConfigurationLogic configurationLogic 	= new ConfigurationLogic();
			
			List<String> joins = new ArrayList<String>();
			joins.add(Employee.CONTACT);
			Performingorg performingorg = getUser(req).getPerformingorg();

			// Find by user role
			projects		= projectLogic.findByUser(getUser(req), getSettings(req));

			// Filters 
			//
			projectManagers	= employeeLogic.searchEmployees(new Resourceprofiles(Constants.ROLE_PM), getCompany(req), performingorg, joins);
			jobs			= jobcategoryLogic.findByRelation(Jobcategory.COMPANY, getCompany(req), Jobcategory.NAME, Constants.ASCENDENT);
			sellers			= sellerLogic.findByRelation(Seller.COMPANY, getCompany(req), Seller.NAME, Constants.ASCENDENT);
			categories		= categoryLogic.findByRelation(Category.COMPANY, getCompany(req), Category.NAME, Constants.ASCENDENT);
			
			// Resource pools
			if (SecurityUtil.isUserInRole(req, Constants.ROLE_RM)) {
				resourcePools = resourcepoolLogic.findByResourceManager(getUser(req));
			}
			else if (SecurityUtil.isUserInRole(req, Constants.ROLE_PM)) {
				resourcePools = resourcepoolLogic.findByProjectManager(getUser(req));
			}
			else {
				resourcePools = resourcepoolLogic.findByRelation(Resourcepool.COMPANY, getCompany(req));
			}
			
			// Filter dates
			Calendar calendar = DateUtil.getCalendar();
			
			calendar.setTime(DateUtil.getFirstMonthDay(new Date()));
			
			firstMonthDay = DateUtil.format(Constants.DATE_PATTERN, calendar.getTime());
			
			calendar.add(Calendar.MONTH, +1);
			
			lastMonthDay = DateUtil.format(Constants.DATE_PATTERN, DateUtil.getLastMonthDay(calendar.getTime()));
			
			// Configurations
			req.setAttribute("configurations", configurationLogic.findByTypes(getUser(req), Configurations.TYPE_CAPACITY_PLANNING));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("projectManagers", projectManagers);
		req.setAttribute("projects", projects);
		req.setAttribute("jobs", jobs);
		req.setAttribute("firstMonthDay", firstMonthDay);
		req.setAttribute("lastMonthDay", lastMonthDay);
		req.setAttribute("resourcePools", resourcePools);
		req.setAttribute("sellers", sellers);
		req.setAttribute("categories", categories);
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("title", getResourceBundle(req).getString("menu.resource_capacity_planning"));
		forward("/index.jsp?nextForm=resource/capacity_planning/capacity_planning", req, resp);
	}
    
	/**
     * View resource pool
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void viewCapacityRunning(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
    	List<Employee> projectManagers		= null;
    	List<Project> projects				= null;
    	List<Jobcategory> jobs				= null;
    	List<Resourcepool> resourcePools	= null;
    	List<Seller> sellers				= null;
    	List<Category> categories				= null;
    	String firstMonthDay 				= StringPool.BLANK;
		String lastMonthDay 				= StringPool.BLANK; 
		
		try {
			
			// Declare logics
			JobcategoryLogic jobcategoryLogic		= new JobcategoryLogic();
	    	EmployeeLogic employeeLogic 			= new EmployeeLogic();
	    	ProjectLogic projectLogic				= new ProjectLogic(getSettings(req), getResourceBundle(req));
	    	ResourcepoolLogic resourcepoolLogic		= new ResourcepoolLogic();
	    	SellerLogic sellerLogic					= new SellerLogic();
	    	CategoryLogic categoryLogic 			= new CategoryLogic();
	    	ConfigurationLogic configurationLogic	= new ConfigurationLogic();
			
			List<String> joins = new ArrayList<String>();
			joins.add(Employee.CONTACT);
			Performingorg performingorg = getUser(req).getPerformingorg();
			
			// Find by user role
			projects		= projectLogic.findByUser(getUser(req), getSettings(req));
						
			// Filters
			//
			projectManagers = employeeLogic.searchEmployees(new Resourceprofiles(Constants.ROLE_PM), getCompany(req), performingorg, joins);
			jobs 			= jobcategoryLogic.findByRelation(Jobcategory.COMPANY, getCompany(req), Jobcategory.NAME, Constants.ASCENDENT);
			sellers			= sellerLogic.findByRelation(Seller.COMPANY, getCompany(req), Seller.NAME, Constants.ASCENDENT);
			categories		= categoryLogic.findByRelation(Category.COMPANY, getCompany(req), Category.NAME, Constants.ASCENDENT);
			
			// Resource pools
			if (SecurityUtil.isUserInRole(req, Constants.ROLE_RM)) {
				resourcePools = resourcepoolLogic.findByResourceManager(getUser(req));
			}
			else if (SecurityUtil.isUserInRole(req, Constants.ROLE_PM)) {
				resourcePools = resourcepoolLogic.findByProjectManager(getUser(req));
			}
			else {
				resourcePools = resourcepoolLogic.findByRelation(Resourcepool.COMPANY, getCompany(req));
			}
			
			// Filter dates
			Calendar calendar = DateUtil.getCalendar();
			
			calendar.setTime(DateUtil.getFirstMonthDay(new Date()));
			
			firstMonthDay = DateUtil.format(Constants.DATE_PATTERN, calendar.getTime());
			
			calendar.add(Calendar.MONTH, +1);
			
			lastMonthDay = DateUtil.format(Constants.DATE_PATTERN, DateUtil.getLastMonthDay(calendar.getTime()));
			
			// Configurations
			req.setAttribute("configurations", configurationLogic.findByTypes(getUser(req), Configurations.TYPE_CAPACITY_RUNNING));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("projectManagers", projectManagers);
		req.setAttribute("projects", projects);
		req.setAttribute("jobs", jobs);
		req.setAttribute("firstMonthDay", firstMonthDay);
		req.setAttribute("lastMonthDay", lastMonthDay);
		req.setAttribute("resourcePools", resourcePools);
		req.setAttribute("sellers", sellers);
		req.setAttribute("categories", categories);
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("title", getResourceBundle(req).getString("menu.resource_capacity_running"));
		forward("/index.jsp?nextForm=resource/capacity_running/capacity_running", req, resp);
	}
    
	/**
     * View resource pool
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void viewResourcePool(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("title", getResourceBundle(req).getString("menu.resource_pool"));
		
		forward("/index.jsp?nextForm=resource/pool/pool", req, resp);
	}

	/**
     * Proposed resource
     * @param req
     * @param resp
     * @throws IOException
     */
    private void proposedResourceJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
    	int idResource			= ParamUtil.getInteger(req, "idResource", -1);
    	int idResourceProposed	= ParamUtil.getInteger(req, "idResourceProposed", -1);
    	Date dateIn				= ParamUtil.getDate(req, "dateIn", getDateFormat(req));
    	Date dateOut			= ParamUtil.getDate(req, "dateOut", getDateFormat(req));
    	int fte					= ParamUtil.getInteger(req, "fte", 0);
    	Integer idJobCategory 	= ParamUtil.getInteger(req, Jobcategory.IDJOBCATEGORY, null);
    	String commentsRm		= ParamUtil.getString(req, "commentsRm");
    	
    	PrintWriter out = resp.getWriter();
    	
    	try {
    		// Declare logics
    		TeamMemberLogic teamMemberLogic	= new TeamMemberLogic();
    		EmployeeLogic employeeLogic 	= new EmployeeLogic();
    		
    		Teammember resource = teamMemberLogic.findById(idResource);
    		
    		if (resource.getEmployee().getIdEmployee() == idResourceProposed) {
    			
    			resource.setStatus(Constants.RESOURCE_PROPOSED);
    			resource.setDateIn(dateIn);
    			resource.setDateOut(dateOut);
    			resource.setFte(fte);
    			resource.setCommentsRm(commentsRm);
    			if (idJobCategory == null) { resource.setJobcategory(null); }
    			else { resource.setJobcategory(new Jobcategory(idJobCategory)); }
    			
    			teamMemberLogic.save(resource);
    		}
    		else {
    			
    			resource.setStatus(Constants.RESOURCE_TURNED_DOWN);
    			teamMemberLogic.save(resource);
    			
    			Teammember resourceProposed = new Teammember();
    			resourceProposed.setProjectactivity(resource.getProjectactivity());
    			
    			Employee employeeProposed = employeeLogic.findById(idResourceProposed);
    			
    			resourceProposed.setEmployee(employeeProposed);
    			resourceProposed.setSellRate(employeeProposed.getCostRate());
    			resourceProposed.setStatus(Constants.RESOURCE_PROPOSED);
    			resourceProposed.setDateIn(dateIn);
    			resourceProposed.setDateOut(dateOut);
    			resourceProposed.setFte(fte);
    			resourceProposed.setCommentsRm(commentsRm);
    			
    			if (idJobCategory == null) { resourceProposed.setJobcategory(null); }
    			else { resourceProposed.setJobcategory(new Jobcategory(idJobCategory)); }
    			
    			teamMemberLogic.saveResource(resourceProposed);
    		}
    		
    		out.print(info(getResourceBundle(req), StringPool.SUCCESS, "resource.proposed.new", null));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
		
	}

    /**
     * View resource data
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
	private void viewResourceJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
    	// Declare writer for send information to client by AJAX
    	PrintWriter out = resp.getWriter();
    	
    	try {
    		
    		// Collect data
    		int idResource = ParamUtil.getInteger(req, "idResource",-1);
    		
    		// Declare logic
    		TeamMemberLogic memberLogic = new TeamMemberLogic();
    		
    		// Information needed
    		List<String> joins = new ArrayList<String>();
    		joins.add(Teammember.JOBCATEGORY);
    		joins.add(Teammember.EMPLOYEE);
    		joins.add(Teammember.EMPLOYEE+"."+Employee.JOBCATEMPLOYEES);
    		joins.add(Teammember.EMPLOYEE+"."+Employee.JOBCATEMPLOYEES+"."+Jobcatemployee.JOBCATEGORY);
    		joins.add(Teammember.EMPLOYEE+"."+Employee.CONTACT);
    		joins.add(Teammember.PROJECTACTIVITY);
    		joins.add(Teammember.PROJECTACTIVITY+"."+Projectactivity.PROJECT);    		
    		joins.add(Teammember.PROJECTACTIVITY+"."+Projectactivity.PROJECT+"."+Project.PROGRAM);
    		joins.add(Teammember.PROJECTACTIVITY+"."+Projectactivity.PROJECT+"."+Project.PERFORMINGORG);
    		joins.add(Teammember.PROJECTACTIVITY+"."+Projectactivity.PROJECT+"."+Project.EMPLOYEEBYPROJECTMANAGER);
    		joins.add(Teammember.PROJECTACTIVITY+"."+Projectactivity.PROJECT+"."+Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
    		
    		// Find member with data
    		Teammember member	= memberLogic.findById(idResource, joins);
    		
    		// Prepare data to send client
    		JSONObject memberJSON = JSONModelUtil.toJSON(getResourceBundle(req), getDateFormat(req), member);
    		
    		// Send data to client
    		out.print(memberJSON);
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Reject Resource
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void rejectResourceJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
    	int idResource		= ParamUtil.getInteger(req, "idResource",-1);
    	String commentsRm	= ParamUtil.getString(req, "commentsRm");
		
		PrintWriter out = resp.getWriter();
    	
    	try {
    		
    		TeamMemberLogic memberLogic = new TeamMemberLogic();
    		
    		Teammember memeber = memberLogic.findById(idResource);
    		memeber.setStatus(Constants.RESOURCE_TURNED_DOWN);
    		memeber.setCommentsRm(commentsRm);
    		
    		memberLogic.save(memeber);
    		
    		out.print(info(getResourceBundle(req), StringPool.SUCCESS, "reject.resource.turn_down", null));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    /**
     * Approve Resource
     * @param req
     * @param resp
     * @throws IOException 
     */
	private void approveResourceJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		int idResource		= ParamUtil.getInteger(req, "idResource",-1);
		String commentsRm	= ParamUtil.getString(req, "commentsRm");
		
		PrintWriter out = resp.getWriter();
    	
    	try {
    		TeamMemberLogic teamMemberLogic = new TeamMemberLogic();
    		
    		teamMemberLogic.approveTeamMember(getSettings(req), idResource, commentsRm);
    		
    		out.print(info(getResourceBundle(req), StringPool.SUCCESS, "approve.resource.assigned", null));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Filter table assignations
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void filterAssignationsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
    	PrintWriter out = resp.getWriter();
    	
    	try {
    		TeamMemberLogic teamMemberLogic = new TeamMemberLogic();
    		
			ArrayList<DatoColumna > columnas = new ArrayList<DatoColumna>();
			
			// Real Columns
			columnas.add(new DatoColumna(StringPool.BLANK,String.class));
			columnas.add(new DatoColumna(Teammember.STATUS,List.class));
			columnas.add(new DatoColumna(Contact.FULLNAME,String.class));
			columnas.add(new DatoColumna("jobcategory.idJobCategory",String.class,List.class));
			columnas.add(new DatoColumna(Resourcepool.NAME,String.class));
            columnas.add(new DatoColumna(Configurations.PROJECT_NAME,String.class));
			columnas.add(new DatoColumna(Projectactivity.ACTIVITYNAME,String.class));
			columnas.add(new DatoColumna(Project.EMPLOYEEBYPROJECTMANAGER,String.class,List.class));
			columnas.add(new DatoColumna(Teammember.DATEIN,Date.class));
			columnas.add(new DatoColumna(Teammember.DATEOUT,Date.class));
			columnas.add(new DatoColumna(Teammember.FTE,Date.class));
			columnas.add(new DatoColumna(Teammember.SELLRATE,String.class));
			
			// Filter since until
			String[] sinceUntil = {Teammember.DATEIN,Teammember.DATEOUT};
			columnas.add(new DatoColumna(sinceUntil,List.class,Date.class));
			
			ArrayList<String > joins = new ArrayList<String>();
			joins.add(Teammember.EMPLOYEE);
			joins.add(Teammember.EMPLOYEE + "." + Employee.CONTACT);
			joins.add(Teammember.EMPLOYEE + "." + Employee.RESOURCEPOOL);
			joins.add(Teammember.JOBCATEGORY);
			joins.add(Teammember.PROJECTACTIVITY);
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT);
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT + "." + Project.EMPLOYEEBYPROJECTMANAGER);
			joins.add(Teammember.PROJECTACTIVITY + "." + Projectactivity.PROJECT + "." + Project.EMPLOYEEBYPROJECTMANAGER + "." + Employee.CONTACT);
			
			PaginacionUtil paginacion = new PaginacionUtil(req, columnas, Teammember.class, joins);
			
			FiltroTabla filter		    = paginacion.crearFiltro();
			List<Teammember> members	= teamMemberLogic.filter(filter, joins, getUser(req));
			
			JSONArray membersJSON = new JSONArray();
			
			// Order by sell rate
			for (DatoColumna item: filter.getOrden()) {
				
				if (Teammember.SELLRATE.equals(item.getNombre())) {
					Collections.sort(members, new EntityComparator<Teammember>(new Order("calculatedSellRate", item.getValor())));
				}
			}
			
			for (Teammember member : members) {
				
				JSONArray memberJSON = new JSONArray();
				memberJSON.add(member.getIdTeamMember());
				memberJSON.add(member.getStatus() != null?getResourceBundle(req).getString("resource."+member.getStatus()):StringPool.BLANK);
				memberJSON.add(HtmlUtil.escape(member.getEmployee().getContact().getFullName()));
				memberJSON.add(HtmlUtil.escape(member.getJobcategory() != null? member.getJobcategory().getName():StringPool.BLANK));
				memberJSON.add(HtmlUtil.escape(member.getEmployee() != null && member.getEmployee().getResourcepool().getName() != null ?
						member.getEmployee().getResourcepool().getName() : StringPool.BLANK));
				
				memberJSON.add(HtmlUtil.escape(member.getProjectactivity().getProject().getProjectName()));
                if (ValidateUtil.isNotNull(HtmlUtil.escape(member.getProjectactivity().getProject().getChartLabel()))){
                    memberJSON.add(HtmlUtil.escape(member.getProjectactivity().getProject().getChartLabel()));
                }
                else{
                    memberJSON.add(StringPool.BLANK);
                }
				memberJSON.add(HtmlUtil.escape(member.getProjectactivity().getActivityName()));
				
				if (member.getProjectactivity().getProject().getEmployeeByProjectManager() != null) {
					memberJSON.add(HtmlUtil.escape(member.getProjectactivity().getProject()
							.getEmployeeByProjectManager().getContact().getFullName()));
				}
				else {
					memberJSON.add("");
				}
				
				memberJSON.add(member.getDateIn() != null? getDateFormat(req).format(member.getDateIn()):StringPool.BLANK);
				memberJSON.add(member.getDateOut() != null? getDateFormat(req).format(member.getDateOut()):StringPool.BLANK);
				memberJSON.add(member.getFte());
				
				Double sellRate = member.getCalculatedSellRate();

				memberJSON.add(ValidateUtil.toCurrency(sellRate));
				memberJSON.add(member.getProjectactivity().getProject().getStatus());
				memberJSON.add(getResourceBundle(req).getString("project_status."+member.getProjectactivity().getProject().getStatus()));
                memberJSON.add(StringPool.BLANK);
				// TODO check
						
				membersJSON.add(memberJSON);
			}
			
			int total		= teamMemberLogic.findTotal(paginacion.getFiltros(), getUser(req));
			int totalFilter = teamMemberLogic.findTotalFiltered(filter, getUser(req));
			
			out.print(paginacion.obtenerDatos(membersJSON, total, totalFilter, null, null));

            // Find configurations
            HashMap<String, String> configurations = RequestUtil.getConfigurationValues(
                    req,
                    Configurations.PROJECT_NAME
            );
            // Save configuration
            ConfigurationLogic configurationLogic = new ConfigurationLogic();
            configurationLogic.saveConfigurations(getUser(req), configurations, Configurations.TYPE_ASSIGNATION_APPROVALS);
        }
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    
    /**
     * Filter table resource pool
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void filterResourcePoolJX(HttpServletRequest req,
    		HttpServletResponse resp) throws IOException {
    	
    	PrintWriter out = resp.getWriter();
		
    	String fullName = ParamUtil.getString(req, Contact.FULLNAME, "");
    	
    	try {
    		
    		EmployeeLogic employeeLogic = new EmployeeLogic();
    		
    		List<String> joins = new ArrayList<String>();
    		joins.add(Employee.CONTACT);
    		
    		List<Employee> employees = employeeLogic.findByManager(getUser(req), fullName, joins, Order.ASC, null);
    		
    		for (Employee employee : employees) {
    			employee.getContact().setCompany(null);
    		}
			
			out.print(JsonUtil.toJSON(employees,
					new Exclusion(Seller.class),
					new Exclusion(Calendarbase.class),
					new Exclusion(Performingorg.class),
					new Exclusion(Resourcepool.class),
					new Exclusion(Resourceprofiles.class)));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}
    
    
	/**
     * View Assignations Approvals
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
	private void viewAssignations(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Employee> projectManagers	= null;
		List<Performingorg> perfOrgs	= null;
		List<Jobcategory> jobs			= null;
		List<Skill> skills				= null;
		
		try {
	    	EmployeeLogic employeeLogic				= new EmployeeLogic();
	    	JobcategoryLogic jobcategoryLogic		= new JobcategoryLogic();
	    	PerformingOrgLogic performingOrgLogic	= new PerformingOrgLogic();
	    	SkillLogic skillLogic 					= new SkillLogic();
            ConfigurationLogic configurationLogic   = new ConfigurationLogic();
	    	
			List<String> joins = new ArrayList<String>();
			joins.add(Employee.CONTACT);
			Performingorg performingorg = getUser(req).getPerformingorg();

			
			projectManagers = employeeLogic.searchEmployees(new Resourceprofiles(Constants.ROLE_PM), getCompany(req), performingorg, joins);
			jobs			= jobcategoryLogic.findByRelation(Jobcategory.COMPANY, getCompany(req), Jobcategory.NAME, Constants.ASCENDENT);
			perfOrgs 		= performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req));
            skills 			= skillLogic.findByRelation(Performingorg.COMPANY, getCompany(req));

            req.setAttribute("configurations", configurationLogic.findByTypes(getUser(req), Configurations.TYPE_ASSIGNATION_APPROVALS));
        }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

        req.setAttribute("projectManagers", projectManagers);
        req.setAttribute("skills", skills);
        req.setAttribute("perforgs", perfOrgs);
        req.setAttribute("jobs", jobs);

        req.setAttribute("title", getResourceBundle(req).getString("menu.assignations_approvals"));
        req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));


		forward("/index.jsp?nextForm=resource/assignations/assignations", req, resp);
	}

}
