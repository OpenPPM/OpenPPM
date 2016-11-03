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
 * File: SecurityUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.exceptions.ProjectNotFoundException;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.security.ProjectSecurityAction;
import es.sm2.openppm.core.logic.security.SecurityAction;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.utils.StringUtils;
import es.sm2.openppm.front.servlets.AdministrationServlet;
import es.sm2.openppm.front.servlets.AssignmentServlet;
import es.sm2.openppm.front.servlets.ChangePOServlet;
import es.sm2.openppm.front.servlets.ExpenseSheetServlet;
import es.sm2.openppm.front.servlets.FollowProjectsServlet;
import es.sm2.openppm.front.servlets.PriorizationProgramServlet;
import es.sm2.openppm.front.servlets.ProgramServlet;
import es.sm2.openppm.front.servlets.ProjectClosureServlet;
import es.sm2.openppm.front.servlets.ProjectServlet;
import es.sm2.openppm.front.servlets.ResourceCalendarServlet;
import es.sm2.openppm.front.servlets.ResourceServlet;
import es.sm2.openppm.front.servlets.RiskTemplatesServlet;
import es.sm2.openppm.front.servlets.ScenarioAnalysisServlet;
import es.sm2.openppm.front.servlets.TimeSheetServlet;
import es.sm2.openppm.front.servlets.UtilServlet;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public final class SecurityUtil extends StringUtils {

    private static final Logger LOGGER = Logger.getLogger(SecurityUtil.class);
    public static final String ROL_PRINCIPAL = "rolPrincipal";
    public static final String USER = "user";

    private SecurityUtil() {
		super();
	}
	
	/**
	 * Validate logged User
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	public static Employee validateLoggedUser(HttpServletRequest req) throws Exception {
		
		Integer selIdEmployee 		= (Integer) req.getSession().getAttribute("selIdEmployee");
		Employee aux 				= SecurityUtil.consUser(req);
    	Employee user 				= null;
    	EmployeeLogic employeeLogic = new EmployeeLogic();
    	
    	if (aux == null) {
    		try {
    			
    			boolean invalidate = false;
				
    			if (selIdEmployee == null) {
    				invalidate = true;
				}
				else {
					user = employeeLogic.consEmployee(selIdEmployee);
					if (user == null) {
						invalidate = true;
					}
					else { 
						req.getSession().setAttribute(USER, user);
						req.getSession().setAttribute(ROL_PRINCIPAL, user.getResourceprofiles().getIdProfile());
					}
				}
    			if (invalidate) { req.getSession().invalidate(); }
    			
	    	}
	    	catch (LogicException e) {
	    		user = null;
	    	}
    	}
    	else {
    		user = aux;
    	}
    	
    	return user;
	}
	
	/**
	 * Check if project is in these status
	 * @param request
	 * @param status
	 * @return
	 */
	public static boolean hasPermissionProject(HttpServletRequest request, String...status) {
		
		int idProject = ParamUtil.getInteger(request, "id", (Integer)request.getSession().getAttribute("idProject"));
		ProjectLogic projectLogic = new ProjectLogic((HashMap<String, String> )request.getAttribute("settings"), null);
		boolean permission = false;
		
		try {
			permission = projectLogic.hasPermissionProject(new Project(idProject), status);
		}
		catch (Exception e) { ExceptionUtil.sendToLogger(LOGGER, e, request); }
		
		return permission;
	}

    /**
     * Check if user is in list of roles
     *
     * @param request
     * @param roles
     * @return
     */
    @Deprecated
    public static boolean isUserInRole(HttpServletRequest request, Integer...roles) {

        // TODO javier.hernandez - 23/09/2015 - delete method
        boolean foundRole = false;

        if (ValidateUtil.isNotNull(roles)) {

            int i = 0;
            while (i < roles.length && !foundRole) {
                Integer role = roles[i];

                // Check role
                foundRole = isUserInRole(request, role.intValue());
                i++;
            }
        }
        return foundRole;
    }

    /**
     * Check if user is in list of roles
     *
     * @param request
     * @param profiles
     * @return
     */
    public static boolean isUserInRole(HttpServletRequest request, Resourceprofiles.Profile...profiles) {

        boolean foundRole = false;

        if (ValidateUtil.isNotNull(profiles)) {

            int i = 0;
            while (i < profiles.length && !foundRole) {
                Resourceprofiles.Profile role = profiles[i];

                // Check role
                foundRole = isUserInRole(request, role.getID());
                i++;
            }
        }
        return foundRole;
    }

	public static boolean isUserInRole(HttpServletRequest request, int role) {
		
		Integer sessionRol = (Integer)request.getSession().getAttribute(ROL_PRINCIPAL);
		return (sessionRol != null && role == sessionRol.intValue());
	}
	
	public static int consUserRole(HttpServletRequest request) {
		
		Integer sessionRol = (Integer)request.getSession().getAttribute(ROL_PRINCIPAL);
		return (sessionRol == null ? -1 : sessionRol);
	}
	
	public static Employee consUser(HttpServletRequest request) {
		
		return (Employee) request.getSession().getAttribute(USER);
	}

	public static void unsetUserRole(HttpServletRequest request) {
		
		request.getSession().setAttribute(ROL_PRINCIPAL,-1);
	}
	
	public static boolean hasPermissionSetting(HashMap<String, String> settings, Employee user, String status, int tab) {
		
		boolean permission = false;
		
		if (user != null && user.getResourceprofiles() != null) {
					
			int rol = user.getResourceprofiles().getIdProfile();

			if (!Constants.STATUS_INITIATING.equals(status)
					&& rol == Constants.ROLE_PM
					&& tab == Constants.TAB_INITIATION
					&& SettingUtil.getBoolean(settings, Settings.SETTING_DISABLE_EDITION_INITIATING_PM, Settings.DEFAULT_DISABLE_EDITION_INITIATING_PM)) {

				permission = false;
			}
			else {
				permission = hasPermission(user, status, tab);
			}
			
		}
		return permission;
	}

    @Deprecated
	public static boolean hasPermission(Employee user, String status, int tab) {
		
		boolean permission = false;
		
		if (user != null && user.getResourceprofiles() != null) {
			
			int rol = user.getResourceprofiles().getIdProfile();
			
			if ((Constants.STATUS_INITIATING.equals(status) || Constants.STATUS_PLANNING.equals(status) || Constants.STATUS_CONTROL.equals(status))
					&& (tab == Constants.TAB_INITIATION || tab == Constants.TAB_PLAN || tab == Constants.TAB_RISK)
					&& (rol == Constants.ROLE_PM || rol == Constants.ROLE_PMO)) {
				
				permission = true;
			}
			else if ((Constants.STATUS_INITIATING.equals(status) || Constants.STATUS_PLANNING.equals(status) || Constants.STATUS_CONTROL.equals(status))
					&& (tab == Constants.TAB_INVESTMENT)
					&& (rol == Constants.ROLE_PMO || rol == Constants.ROLE_IM)) {
				
				permission = true;
			}
			else if ((Constants.STATUS_PLANNING.equals(status) || Constants.STATUS_CONTROL.equals(status))
					&& (tab == Constants.TAB_PROCURAMENT)
					&& (rol == Constants.ROLE_PM || rol == Constants.ROLE_PMO || rol == Constants.ROLE_LOGISTIC)) {
				
				permission = true;
			}
			else if (Constants.STATUS_CONTROL.equals(status)
					&& (tab == Constants.TAB_CONTROL || tab == Constants.TAB_CLOSURE)
					&& (rol == Constants.ROLE_PM || rol == Constants.ROLE_PMO)) {
				
				permission = true;
			}
            else if (Constants.STATUS_CLOSED.equals(status)
                    && tab == Constants.TAB_CLOSURE
                    && (rol == Constants.ROLE_PM || rol == Constants.ROLE_PMO)) {

                permission = true;
            }
            else if ((Constants.STATUS_CLOSED.equals(status) || Constants.STATUS_ARCHIVED.equals(status))
                    && tab == Constants.TAB_PROCURAMENT
                    && (rol == Constants.ROLE_PMO || rol == Constants.ROLE_LOGISTIC)) {

                permission = true;
            }
			else if ((tab == Constants.TAB_INVESTMENT)
					&& (rol == Constants.ROLE_PM || rol == Constants.ROLE_PMO )) {
				
				permission = true;
			}
		}
		
		return permission;
	}

    /**
     * Check security intantiate action
     *
     * @param request
     * @param securityAction
     * @param action
     * @return
     * @throws Exception
     */
    public static boolean hasPermission(HttpServletRequest request, SecurityAction securityAction, String action) {

        boolean permission = false;

        try {

            permission = securityAction.getAction().equals(action) && hasPermission(request, securityAction);

        } catch (Exception e) {

            LOGGER.error("Security is not configured", e);
        }

        return permission;
    }

    /**
     * Check if user has permission for tab action and project status
     *
     * @param request
     * @param securityAction
     * @return
     * @throws Exception
     */
    public static boolean hasPermission(HttpServletRequest request, SecurityAction securityAction) throws Exception {

        boolean permission;

        // Load user
        Employee user = consUser(request);

        if (securityAction instanceof ProjectSecurityAction) {

            // Load status project
            Project project = consProject(request);

            permission = ((ProjectSecurityAction)securityAction).hasPermission(project.getStatus(), user.getResourceprofiles().getIdProfile());
        }
        else {
            permission = securityAction.hasPermission(user.getResourceprofiles().getIdProfile());
        }

        return permission;
    }

    /**
     * Load project from request
     *
     * @param request
     * @return
     * @throws Exception
     */
    private static Project consProject(HttpServletRequest request) throws Exception {

        // Load project from request
        Project project = (Project) request.getAttribute("project");

        // Load Project from BD
        if (project == null) {

            // Load id from request or session
            Integer idProject = ParamUtil.getInteger(request, "id", (Integer)request.getSession().getAttribute("idProject"));

            // Load project from BD
            ProjectLogic projectLogic = new ProjectLogic((HashMap<String, String> )request.getAttribute("settings"), null);
            project = projectLogic.consProject(idProject);

            if (project == null) {
                throw new ProjectNotFoundException();
            }
            // Add project to request for single call
            request.setAttribute("project", project);
        }

        return project;
    }

    @Deprecated
    public static boolean hasPermission(HttpServletRequest req, String accion) {
		
		boolean permission = false;
		
		Employee user = consUser(req);
		
		if (user != null && user.getResourceprofiles() != null) {
			
			int rol = user.getResourceprofiles().getIdProfile();
			
			if (rol == Constants.ROLE_PMO && (
					accion.equals(ProjectServlet.JX_APPROVE_INVESTMENT				) ||
					accion.equals(ProgramServlet.SAVE_PROGRAM						) ||
					accion.equals(ProgramServlet.DEL_PROGRAM						) ||
					accion.equals(ResourceServlet.JX_VIEW_RESOURCE					) ||
					accion.equals(UtilServlet.JX_CHANGE_CLOSED_TO_CONTROL			) ||
                    accion.equals(UtilServlet.JX_CHANGE_ARCHIVED_TO_CLOSED			) ||
					accion.equals(ChangePOServlet.REFERENCE							) ||
					accion.equals(ChangePOServlet.JX_VIEW_PO						) ||
					accion.equals(ChangePOServlet.JX_UPDATE_PO						) ||
					// Project Closure
					accion.equals(ProjectClosureServlet.REFERENCE					) ||
					accion.equals(ProjectClosureServlet.VIEW_CLOSE					) ||
					accion.equals(ProjectClosureServlet.JX_CHECK_CLOSURE_PROJECT	) ||
					accion.equals(ProjectClosureServlet.JX_SAVE_CLOSURE				) ||
					accion.equals(ProjectClosureServlet.JX_SAVE_DOCUMENTATION		) ||
					accion.equals(ProjectClosureServlet.JX_SAVE_CLOSURE_CHECK_PROJECT) ||
					accion.equals(ProjectClosureServlet.JX_DELETE_CLOSURE_CHECK_PROJECT) ||
					accion.equals(ProjectClosureServlet.JX_SELECT_PROBLEM_CHECK_LIST) ||
					accion.equals(ProjectClosureServlet.JX_SAVE_PROBLEM_CHECK_LIST) ||
					accion.equals(ProjectClosureServlet.JX_DELETE_PROBLEM_CHECK_LIST) ||
					//  Reports
					accion.equals(FollowProjectsServlet.REPORT_PROJECT				) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS				) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS_EXECUTIVE	) ||
					// Maintenance risk templates
					accion.equals(RiskTemplatesServlet.REFERENCE				) ||
					accion.equals(RiskTemplatesServlet.VIEW_RISK_TEMPLATE		) ||
					accion.equals(RiskTemplatesServlet.SAVE_RISK_TEMPLATE		) ||
					accion.equals(RiskTemplatesServlet.JX_DELETE_RISK_TEMPLATE	) ||
					// Priorization projects
					accion.equals(PriorizationProgramServlet.REFERENCE			) ||
					accion.equals(PriorizationProgramServlet.JX_UPDATE_PROJECTS	) ||
					// Scenario Analysis
					accion.equals(ScenarioAnalysisServlet.REFERENCE						) ||
					accion.equals(ScenarioAnalysisServlet.JX_SCENARIO_ANALYSIS_CHART	) ||
					accion.equals(ScenarioAnalysisServlet.JX_EFFICIENT_FRONTIER_CHART	) ||
					
					// List Projects
					accion.equals(ProjectServlet.NEW_PROJECT			             	) ||
					accion.equals(ProjectServlet.NEW_INVERTMENT				         	) ||
					accion.equals(ProjectServlet.CREATE_PROJECT				         	) ||
					accion.equals(ProjectServlet.LIST_PROJECTS				         	) ||
					accion.equals(ProjectServlet.LIST_INVESTMENTS		             	) ||
					accion.equals(ProjectServlet.DELETE_PROJECT				         	) ||
					accion.equals(ProjectServlet.GENERATE_STATUS_REPORT		         	) ||
					accion.equals(ProjectServlet.GENERATE_EXECUTIVE_REPORT	         	) ||
					accion.equals(ProjectServlet.DOWNLOAD_DOC				         	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_RESOURCE_CSV	     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_MONTH_CSV		     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_MONTH_CSV	 	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_RESOURCE_CSV	) ||
					accion.equals(ProjectServlet.JX_CHECK_CODE						 	) ||
					accion.equals(ProjectServlet.JX_SAVE_CUSTOMER					 	) ||
					accion.equals(ProjectServlet.JX_SAVE_DOCUMENT					 	) ||
					accion.equals(ProjectServlet.JX_CONS_PROJECT					 	) ||
					accion.equals(ProjectServlet.JX_APPROVE_INVESTMENT				 	) ||
					accion.equals(ProjectServlet.JX_REJECT_INVESTMENT				 	) ||
					accion.equals(ProjectServlet.JX_CANCEL_INVESTMENT				 	) ||
					accion.equals(ProjectServlet.JX_RESET_INVESTMENT				 	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE					 	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE_INVESTMENTS		 	) ||
					accion.equals(ProjectServlet.JX_UPLOAD_FILESYSTEM				 	) ||
					accion.equals(ProjectServlet.JX_DELETE_DOC						 	) ||
					accion.equals(ProjectServlet.JX_RESOURCE_CAPACITY_RUNNING		 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_MONTH			 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_RESSOURCE	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_MONTH 		) ||
					accion.equals(ProjectServlet.JX_SHOW_AGGREGATE_KPIS				    ) ||
					accion.equals(ProjectServlet.JX_UPDATE_PRIORITY_ADJUSTMENT		    ) ||
					
					// Resource capacities
					accion.equals(ResourceServlet.VIEW_CAPACITY_PLANNING)	||
					accion.equals(ResourceServlet.VIEW_CAPACITY_RUNNING)	||
					accion.equals(ResourceServlet.CAPACITY_PLANNING_TO_CSV)	||
					accion.equals(ResourceServlet.CAPACITY_PLANNING_DETAIL_TO_CSV)	||
					accion.equals(ResourceServlet.CAPACITY_RUNNING_TO_CSV)	||
					accion.equals(ResourceServlet.JX_UPDATE_CAPACITY_PLANNING) ||
					accion.equals(ResourceServlet.JX_UPDATE_CAPACITY_RUNNING) ||
					accion.equals(ResourceServlet.JX_VIEW_CAPACITY_PLANNING_RESOURCE) ||
					accion.equals(ResourceServlet.JX_VIEW_CAPACITY_RUNNING_RESOURCE)
			)) {
				
				permission = true;
			}
			else if (rol == Constants.ROLE_PROGM && (
					accion.equals(ProgramServlet.SAVE_PROGRAM		 ) ||
					accion.equals(ResourceServlet.REFERENCE			 ) ||					
					accion.equals(ResourceServlet.JX_VIEW_RESOURCE	) ||
					// Project Closure
					accion.equals(ProjectClosureServlet.REFERENCE	) ||
					accion.equals(ProjectClosureServlet.VIEW_CLOSE	) ||
					// Reports
					accion.equals(FollowProjectsServlet.REPORT_PROJECT	) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS	) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS_EXECUTIVE) ||
					// Priorization projects
					accion.equals(PriorizationProgramServlet.REFERENCE) ||
					accion.equals(PriorizationProgramServlet.JX_UPDATE_PROJECTS) ||
					
					// List Projects
					accion.equals(ProjectServlet.LIST_PROJECTS				         	) ||
					accion.equals(ProjectServlet.LIST_INVESTMENTS		             	) ||
					accion.equals(ProjectServlet.GENERATE_STATUS_REPORT		         	) ||
					accion.equals(ProjectServlet.GENERATE_EXECUTIVE_REPORT	         	) ||
					accion.equals(ProjectServlet.DOWNLOAD_DOC				         	) ||
					accion.equals(ProjectServlet.JX_UPLOAD_FILESYSTEM				 	) || 
					accion.equals(ProjectServlet.REPORT_PROJECT_RESOURCE_CSV	     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_MONTH_CSV		     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_MONTH_CSV	 	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_RESOURCE_CSV	) ||
					accion.equals(ProjectServlet.JX_CONS_PROJECT					 	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE					 	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE_INVESTMENTS		 	) ||
					accion.equals(ProjectServlet.JX_RESOURCE_CAPACITY_RUNNING		 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_MONTH			 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_RESSOURCE	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_MONTH 		) ||
					accion.equals(ProjectServlet.JX_SHOW_AGGREGATE_KPIS			 		) ||
					// Scenario Analysis
					accion.equals(ScenarioAnalysisServlet.REFERENCE) ||
					accion.equals(ScenarioAnalysisServlet.JX_SCENARIO_ANALYSIS_CHART) ||
					accion.equals(ScenarioAnalysisServlet.JX_EFFICIENT_FRONTIER_CHART)
					
			)){
				
				permission = true;
			}
			else if (rol == Constants.ROLE_IM && (
					
					// List Projects
					accion.equals(ProjectServlet.NEW_INVERTMENT				         	) ||
					accion.equals(ProjectServlet.CREATE_PROJECT				         	) ||
					accion.equals(ProjectServlet.LIST_INVESTMENTS		             	) ||
					accion.equals(ProjectServlet.GENERATE_STATUS_REPORT		         	) ||
					accion.equals(ProjectServlet.GENERATE_EXECUTIVE_REPORT	         	) ||
					accion.equals(ProjectServlet.DOWNLOAD_DOC				         	) ||
					accion.equals(ProjectServlet.JX_UPLOAD_FILESYSTEM				 	) ||
					accion.equals(ProjectServlet.JX_DELETE_DOC						 	) ||
					accion.equals(ProjectServlet.JX_SAVE_DOCUMENT					 	) ||
					accion.equals(ProjectServlet.JX_CHECK_CODE						 	) ||
					accion.equals(ProjectServlet.JX_SAVE_CUSTOMER					 	) ||
					accion.equals(ProjectServlet.JX_CONS_PROJECT					 	) ||
					accion.equals(ProjectServlet.JX_APPROVE_INVESTMENT				 	) ||
					accion.equals(ProjectServlet.JX_REJECT_INVESTMENT				 	) ||
					accion.equals(ProjectServlet.JX_CANCEL_INVESTMENT				 	) ||
					accion.equals(ProjectServlet.JX_RESET_INVESTMENT				 	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE_INVESTMENTS		 	) ||
					
					// Reports
					accion.equals(FollowProjectsServlet.REPORT_PROJECT) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS_EXECUTIVE)
			)) {
				
				permission = true;
			}
			else if (rol == Constants.ROLE_STAKEHOLDER && (
					accion.equals(ResourceServlet.REFERENCE) ||					
					accion.equals(ResourceServlet.JX_VIEW_RESOURCE) ||
					// Project Closure
					accion.equals(ProjectClosureServlet.REFERENCE) ||
					accion.equals(ProjectClosureServlet.VIEW_CLOSE) ||
					
					// List Projects
					accion.equals(ProjectServlet.LIST_PROJECTS				         	) ||
					accion.equals(ProjectServlet.GENERATE_STATUS_REPORT		         	) ||
					accion.equals(ProjectServlet.GENERATE_EXECUTIVE_REPORT	         	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_RESOURCE_CSV	     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_MONTH_CSV		     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_MONTH_CSV	 	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_RESOURCE_CSV	) ||
					accion.equals(ProjectServlet.DOWNLOAD_DOC				         	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE					 	) ||
					accion.equals(ProjectServlet.JX_RESOURCE_CAPACITY_RUNNING		 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_MONTH			 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_RESSOURCE	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_MONTH 		) ||
					accion.equals(ProjectServlet.JX_SHOW_AGGREGATE_KPIS				    ) ||
					
					// Reports
					accion.equals(FollowProjectsServlet.REPORT_PROJECT) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS_EXECUTIVE)
			)) {
				
				permission = true;
			}
			else if (rol == Constants.ROLE_PM && (
					accion.equals(ResourceServlet.JX_VIEW_RESOURCE		) ||
					//Time Sheet Servlet
					accion.equals(TimeSheetServlet.VIEW_TIMESHEET			) ||
					accion.equals(TimeSheetServlet.APPROVE_ALL_TIMESHEET	) ||
					accion.equals(TimeSheetServlet.APPROVE_SEL_TIMESHEET	) ||
					accion.equals(TimeSheetServlet.REJECT_SEL_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.REJECT_ALL_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.CHANGE_WEEK				) ||
			    	accion.equals(TimeSheetServlet.NEXT_WEEK				) ||
					accion.equals(TimeSheetServlet.PREV_WEEK				) ||					
					accion.equals(TimeSheetServlet.JX_APPROVE_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.JX_REJECT_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.JX_VIEW_COMMENTS			) ||
					// List Projects
					accion.equals(ProjectServlet.LIST_PROJECTS				         	) ||
					accion.equals(ProjectServlet.GENERATE_STATUS_REPORT		         	) ||
					accion.equals(ProjectServlet.GENERATE_EXECUTIVE_REPORT	         	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_RESOURCE_CSV	     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_MONTH_CSV		     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_MONTH_CSV	 	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_RESOURCE_CSV	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE					 	) ||
					accion.equals(ProjectServlet.JX_RESOURCE_CAPACITY_RUNNING		 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_MONTH			 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_RESSOURCE	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_MONTH 		) ||
					accion.equals(ProjectServlet.JX_SHOW_AGGREGATE_KPIS				    ) ||
					accion.equals(ProjectServlet.DOWNLOAD_DOC				         	) ||
					accion.equals(ProjectServlet.JX_UPLOAD_FILESYSTEM				 	) ||
					accion.equals(ProjectServlet.JX_DELETE_DOC						 	) ||
					accion.equals(ProjectServlet.JX_SAVE_DOCUMENT					 	) ||
					
					// Project Closure
					accion.equals(ProjectClosureServlet.REFERENCE) ||
					accion.equals(ProjectClosureServlet.VIEW_CLOSE) ||
					accion.equals(ProjectClosureServlet.JX_CHECK_CLOSURE_PROJECT) ||
					accion.equals(ProjectClosureServlet.JX_SAVE_CLOSURE) ||
					accion.equals(ProjectClosureServlet.JX_SAVE_DOCUMENTATION) ||
					accion.equals(ProjectClosureServlet.JX_SAVE_CLOSURE_CHECK_PROJECT) ||
					accion.equals(ProjectClosureServlet.JX_DELETE_CLOSURE_CHECK_PROJECT) ||
                    accion.equals(ProjectClosureServlet.JX_SELECT_PROBLEM_CHECK_LIST) ||
                    accion.equals(ProjectClosureServlet.JX_DELETE_PROBLEM_CHECK_LIST) ||
                    accion.equals(ProjectClosureServlet.JX_SAVE_PROBLEM_CHECK_LIST) ||

					// Reports
					accion.equals(FollowProjectsServlet.REPORT_PROJECT) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS_EXECUTIVE) ||
					
					// Resource capacities
					accion.equals(ResourceServlet.VIEW_CAPACITY_PLANNING)	||
					accion.equals(ResourceServlet.VIEW_CAPACITY_RUNNING)	||
					accion.equals(ResourceServlet.CAPACITY_PLANNING_TO_CSV)	||
					accion.equals(ResourceServlet.CAPACITY_PLANNING_DETAIL_TO_CSV)	||
					accion.equals(ResourceServlet.CAPACITY_RUNNING_TO_CSV)	||
					accion.equals(ResourceServlet.JX_UPDATE_CAPACITY_PLANNING) ||
					accion.equals(ResourceServlet.JX_UPDATE_CAPACITY_RUNNING) ||
					accion.equals(ResourceServlet.JX_VIEW_CAPACITY_PLANNING_RESOURCE) ||
					accion.equals(ResourceServlet.JX_VIEW_CAPACITY_RUNNING_RESOURCE)
			)) {
				
				permission = true;
			}
			else if (rol == Constants.ROLE_RM && (
					accion.equals(ResourceServlet.REFERENCE)				||
					accion.equals(ResourceServlet.VIEW_ASSIGNATIONS)		||
					accion.equals(ResourceServlet.VIEW_RESOURCE_POOL)		||
					accion.equals(ResourceServlet.VIEW_CAPACITY_PLANNING)	||
					accion.equals(ResourceServlet.VIEW_CAPACITY_RUNNING)	||
					accion.equals(ResourceServlet.CAPACITY_PLANNING_TO_CSV)	||
					accion.equals(ResourceServlet.CAPACITY_PLANNING_DETAIL_TO_CSV)	||
					accion.equals(ResourceServlet.CAPACITY_RUNNING_TO_CSV)	||
					accion.equals(ResourceServlet.JX_FILTER_ASSIGNATIONS)	||
					accion.equals(ResourceServlet.JX_APPROVE_RESOURCE	)	||
					accion.equals(ResourceServlet.JX_REJECT_RESOURCE	)	||
					accion.equals(ResourceServlet.JX_PROPOPSED_RESOURCE	)	||
					accion.equals(ResourceServlet.JX_VIEW_RESOURCE		)	||
					accion.equals(ResourceServlet.JX_UPDATE_CAPACITY_PLANNING) ||
					accion.equals(ResourceServlet.JX_VIEW_CAPACITY_PLANNING_RESOURCE) ||
					accion.equals(ResourceServlet.JX_VIEW_CAPACITY_PLANNING_RESOURCE_DETAIL) ||
					accion.equals(ResourceServlet.JX_VIEW_CAPACITY_RUNNING_RESOURCE) ||
					accion.equals(ResourceServlet.JX_FILTER_RESOURCE_POOL) ||
					accion.equals(ResourceServlet.JX_UPDATE_CAPACITY_RUNNING) ||
					// Resource Calendar
					accion.equals(ResourceCalendarServlet.REFERENCE) ||
					accion.equals(ResourceCalendarServlet.JX_SAVE_DATES)
					)) {
				
				permission = true;
			}
			else if (rol == Constants.ROLE_FM && (
					accion.equals(ResourceServlet.REFERENCE) ||					
					accion.equals(ResourceServlet.JX_VIEW_RESOURCE) ||
					// Project Closure
					accion.equals(ProjectClosureServlet.REFERENCE) ||
					accion.equals(ProjectClosureServlet.VIEW_CLOSE) ||
					// Reports
					accion.equals(FollowProjectsServlet.REPORT_PROJECT) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS_EXECUTIVE)||
					
					// List Projects
					accion.equals(ProjectServlet.LIST_PROJECTS				         	) ||
					accion.equals(ProjectServlet.LIST_INVESTMENTS		             	) ||
					accion.equals(ProjectServlet.GENERATE_STATUS_REPORT		         	) ||
					accion.equals(ProjectServlet.GENERATE_EXECUTIVE_REPORT	         	) ||
					accion.equals(ProjectServlet.DOWNLOAD_DOC				         	) ||
					accion.equals(ProjectServlet.JX_UPLOAD_FILESYSTEM				 	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_RESOURCE_CSV	     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_MONTH_CSV		     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_MONTH_CSV	 	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_RESOURCE_CSV	) ||
					accion.equals(ProjectServlet.JX_CONS_PROJECT					 	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE					 	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE_INVESTMENTS		 	) ||
					accion.equals(ProjectServlet.JX_RESOURCE_CAPACITY_RUNNING		 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_MONTH			 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_RESSOURCE	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_MONTH 		) ||
					accion.equals(ProjectServlet.JX_SHOW_AGGREGATE_KPIS			 		) ||
					
					//Time Sheet Servlet
					accion.equals(TimeSheetServlet.CHANGE_WEEK				) || 
					accion.equals(TimeSheetServlet.NEXT_WEEK				) ||
					accion.equals(TimeSheetServlet.PREV_WEEK				) 
					)) {
				
				permission = true;
			}
			else if (rol == Constants.ROLE_SPONSOR && (
					accion.equals(ResourceServlet.REFERENCE) ||					
					accion.equals(ResourceServlet.JX_VIEW_RESOURCE) ||
					// Project Closure
					accion.equals(ProjectClosureServlet.REFERENCE) ||
					accion.equals(ProjectClosureServlet.VIEW_CLOSE) ||
					
					// List Projects
					accion.equals(ProjectServlet.LIST_PROJECTS				         	) ||
					accion.equals(ProjectServlet.GENERATE_STATUS_REPORT		         	) ||
					accion.equals(ProjectServlet.GENERATE_EXECUTIVE_REPORT	         	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_RESOURCE_CSV	     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_MONTH_CSV		     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_MONTH_CSV	 	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_RESOURCE_CSV	) ||
					accion.equals(ProjectServlet.DOWNLOAD_DOC				         	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE					 	) ||
					accion.equals(ProjectServlet.JX_RESOURCE_CAPACITY_RUNNING		 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_MONTH			 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_RESSOURCE	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_MONTH 		) ||
					accion.equals(ProjectServlet.JX_SHOW_AGGREGATE_KPIS				    ) ||
					
					// Reports
					accion.equals(FollowProjectsServlet.REPORT_PROJECT) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS_EXECUTIVE)
					)) {
				
				permission = true;
			}
			else if (rol == Constants.ROLE_PORFM && (
					// Investments
					accion.equals(ProjectServlet.LIST_INVESTMENTS)		||
					// Resource 
					accion.equals(ResourceServlet.REFERENCE)			||
					accion.equals(ResourceServlet.JX_VIEW_RESOURCE) 	||
					// Project Closure
					accion.equals(ProjectClosureServlet.REFERENCE)		||
					accion.equals(ProjectClosureServlet.VIEW_CLOSE) ||
					
					// List Projects
					accion.equals(ProjectServlet.LIST_PROJECTS				         	) ||
					accion.equals(ProjectServlet.LIST_INVESTMENTS		             	) ||
					accion.equals(ProjectServlet.GENERATE_STATUS_REPORT		         	) ||
					accion.equals(ProjectServlet.GENERATE_EXECUTIVE_REPORT	         	) ||
					accion.equals(ProjectServlet.DOWNLOAD_DOC				         	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_RESOURCE_CSV	     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_MONTH_CSV		     	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_MONTH_CSV	 	) ||
					accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_RESOURCE_CSV	) ||
					accion.equals(ProjectServlet.JX_CONS_PROJECT					 	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE					 	) ||
					accion.equals(ProjectServlet.JX_FILTER_TABLE_INVESTMENTS		 	) ||
					accion.equals(ProjectServlet.JX_RESOURCE_CAPACITY_RUNNING		 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_MONTH			 	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_RESSOURCE	) ||
					accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_MONTH 		) ||
					accion.equals(ProjectServlet.JX_SHOW_AGGREGATE_KPIS			 		) ||
					
					// Reports
					accion.equals(FollowProjectsServlet.REPORT_PROJECT) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS) ||
					accion.equals(FollowProjectsServlet.REPORT_PROJECTS_EXECUTIVE)
					)) {
				
				permission = true;
			}
			else if (rol == SettingUtil.getApprovalRol(req) && (
					//Time Sheet Servlet
					accion.equals(TimeSheetServlet.VIEW_TIMESHEET			) ||
					accion.equals(TimeSheetServlet.APPROVE_ALL_TIMESHEET	) ||
					accion.equals(TimeSheetServlet.APPROVE_SEL_TIMESHEET	) ||
					accion.equals(TimeSheetServlet.REJECT_SEL_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.REJECT_ALL_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.CHANGE_WEEK				) ||
					accion.equals(TimeSheetServlet.NEXT_WEEK				) ||
					accion.equals(TimeSheetServlet.PREV_WEEK				) ||
					accion.equals(TimeSheetServlet.JX_APPROVE_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.JX_REJECT_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.JX_VIEW_COMMENTS			) ||
					//Expense Sheet Servlet
					accion.equals(ExpenseSheetServlet.VIEW_EXPENSE_SHEET		) ||
					accion.equals(ExpenseSheetServlet.CHANGE_MONTH 				) ||
					accion.equals(ExpenseSheetServlet.APPROVE_ALL_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.REJECT_ALL_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.APPROVE_SEL_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.REJECT_SEL_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.JX_VIEW_COMMENTS			)
				)) {
				
				permission = true;
			}
			else if (rol == Constants.ROLE_RESOURCE && (
					//Time Sheet Servlet
					accion.equals(TimeSheetServlet.VIEW_TIMESHEET			) ||
					accion.equals(TimeSheetServlet.SAVE_ALL_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.APPROVE_ALL_TIMESHEET	) ||
					accion.equals(TimeSheetServlet.CHANGE_WEEK				) ||
					accion.equals(TimeSheetServlet.NEXT_WEEK				) ||
					accion.equals(TimeSheetServlet.PREV_WEEK				) ||
					accion.equals(TimeSheetServlet.ADD_OPERATION			) ||
					accion.equals(TimeSheetServlet.DEL_OPERATION			) ||
					accion.equals(TimeSheetServlet.JX_SAVE_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.JX_APPROVE_TIMESHEET		) ||
					accion.equals(TimeSheetServlet.JX_VIEW_COMMENTS			) ||
					accion.equals(TimeSheetServlet.JX_SUGGEST_REJECT		) ||
					//Expense Sheet Servlet
					accion.equals(ExpenseSheetServlet.VIEW_EXPENSE_SHEET		) ||
					accion.equals(ExpenseSheetServlet.CHANGE_MONTH 				) ||
					accion.equals(ExpenseSheetServlet.ADD_EXPENSE				) ||
					accion.equals(ExpenseSheetServlet.SAVE_ALL_EXPENSESHEET		) ||
					accion.equals(ExpenseSheetServlet.APPROVE_ALL_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.REJECT_ALL_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.DELETE_ALL_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.APPROVE_SEL_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.REJECT_SEL_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.JX_SAVE_EXPENSESHEET		) ||
					accion.equals(ExpenseSheetServlet.JX_APPROVE_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.JX_DELETE_EXPENSESHEET	) ||
					accion.equals(ExpenseSheetServlet.JX_VIEW_COMMENTS			) ||
					// Assignaments Servlet
					accion.equals(AssignmentServlet.VIEW_ASSIGNMENTS			) ||
					accion.equals(AssignmentServlet.FILTER_ASSIGNMENTS			) ||
					accion.equals(AssignmentServlet.EXPORT_CAPACITY_PLANNING_CSV) ||
					accion.equals(AssignmentServlet.JX_FILTER_CAPACITY_PLANNING	) ||
                    accion.equals(AssignmentServlet.JX_VIEW_IMPUTATIONS	) ||
                    accion.equals(AssignmentServlet.EXPORT_IMPUTATIONS	)
			)) {
				
				permission = true;
			}
			else if (rol == Constants.ROLE_ADMIN && (
					accion.equals(AdministrationServlet.REFERENCE) ||
					accion.equals(AdministrationServlet.SAVE_GENERAL_SETTINGS) ||
					accion.equals(AdministrationServlet.SAVE_MAIL_SETTINGS) ||
					accion.equals(AdministrationServlet.SAVE_SECURITY_SETTINGS) ||
					accion.equals(AdministrationServlet.SAVE_VISIBILITY_SETTINGS) ||
					accion.equals(AdministrationServlet.CREATE_PO) ||
					accion.equals(AdministrationServlet.JX_TEST_LDAP) ||
					accion.equals(AdministrationServlet.JX_USERNAME_IN_USE) ||
					accion.equals(AdministrationServlet.CREATE_ADMIN) ||
					accion.equals(AdministrationServlet.SAVE_REQUIRED_SETTINGS)
			)) {
			
				permission = true;
            }
            else if (rol == Constants.ROLE_LOGISTIC && (

                    // List Projects
                    accion.equals(ProjectServlet.LIST_PROJECTS				         	) ||
                    accion.equals(ProjectServlet.JX_CHECK_CODE						 	) ||
                    accion.equals(ProjectServlet.JX_CONS_PROJECT					 	) ||
                    accion.equals(ProjectServlet.JX_FILTER_TABLE					 	) ||
                    accion.equals(ProjectServlet.JX_RESOURCE_CAPACITY_RUNNING		 	) ||
                    accion.equals(ProjectServlet.JX_REPORT_PROJECT_MONTH			 	) ||
                    accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_RESSOURCE	) ||
                    accion.equals(ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_MONTH 		) ||
                    accion.equals(ProjectServlet.JX_SHOW_AGGREGATE_KPIS				    ) ||

                    //  Reports
                    accion.equals(ProjectServlet.GENERATE_STATUS_REPORT		         	) ||
                    accion.equals(ProjectServlet.GENERATE_EXECUTIVE_REPORT	         	) ||
                    accion.equals(ProjectServlet.REPORT_PROJECT_RESOURCE_CSV	     	) ||
                    accion.equals(ProjectServlet.REPORT_PROJECT_MONTH_CSV		     	) ||
                    accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_MONTH_CSV	 	) ||
                    accion.equals(ProjectServlet.REPORT_PROJECT_ACTIVITY_RESOURCE_CSV	) ||
                    accion.equals(FollowProjectsServlet.REPORT_PROJECT				    ) ||
                    accion.equals(FollowProjectsServlet.REPORT_PROJECTS				    ) ||
                    accion.equals(FollowProjectsServlet.REPORT_PROJECTS_EXECUTIVE	    ) ||

                    // Project Closure
                    accion.equals(ProjectClosureServlet.REFERENCE					) ||
                    accion.equals(ProjectClosureServlet.VIEW_CLOSE					) ||
                    accion.equals(ProjectClosureServlet.JX_CHECK_CLOSURE_PROJECT	) ||
                    accion.equals(ProjectClosureServlet.JX_SELECT_PROBLEM_CHECK_LIST) ||

                    // Documents
                    accion.equals(ProjectServlet.DOWNLOAD_DOC				        ) ||
                    accion.equals(ProjectServlet.JX_DELETE_DOC						) ||
                    accion.equals(ProjectServlet.JX_UPLOAD_FILESYSTEM				) ||
                    accion.equals(ProjectServlet.JX_SAVE_DOCUMENT					)
            )) {

                permission = true;
            }
		}
		
		
		return permission;
	}
	
	/**
	 * Has permission user in these project
	 * @param req
	 * @param project
	 * @param tab
	 * @return
	 * @throws Exception
	 */
	public static boolean hasPermission(HttpServletRequest req, Project project, int tab) throws Exception {
		
		boolean permission = false;
		
		Employee user = consUser(req);
		
		ProjectLogic projectLogic = new ProjectLogic((HashMap<String, String> )req.getAttribute("settings"), null);
		
		if (user != null && user.getResourceprofiles() != null) {
			
			permission = projectLogic.hasPermission(project,user, tab);
			
			if (permission) { req.getSession().setAttribute("idProject", project.getIdProject()); }
		}
		return permission;
	}
	
	/**
	 * Get user by token
	 * @param token
	 * @return
	 * @throws Exception 
	 */
	public static Employee consUserByToken(String token) throws Exception {

		if (ValidateUtil.isNull(token)) {
			throw new LogicException("The token is empty");
		}
		
		EmployeeLogic employeeLogic = new EmployeeLogic();
		Employee user = employeeLogic.findByToken(token);
		
		if (user == null) {
			throw new LogicException("Not user found for thesse token: "+token);
		}
		
		LOGGER.debug("Authenticated user for use API");
		
		return user;
	}
}
