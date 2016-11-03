
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
 * File: UtilServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.BscdimensionLogic;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.ContactLogic;
import es.sm2.openppm.core.logic.impl.ContactnotificationLogic;
import es.sm2.openppm.core.logic.impl.ContentFileLogic;
import es.sm2.openppm.core.logic.impl.DocumentationLogic;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.MetrickpiLogic;
import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.security.actions.UtilAction;
import es.sm2.openppm.core.model.impl.Bscdimension;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contactnotification;
import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.Documentation;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Metrickpi;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servlet implementation class UtilServlet
 */
public class UtilServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(UtilServlet.class);
	
	public final static String REFERENCE = "util";
	
	public final static String SHOW_DOCUMENTATION			= "show-documentation";
	/************** Actions AJAX **************/
	public final static String JX_SEARCH_EMPLOYEE			= "ajax-search-employee";
	public final static String JX_SEARCH_CONTACT			= "ajax-search-contact";
	public final static String JX_SEARCH_PROJECTS			= "ajax-search-projects";
	public final static String JX_CHANGE_CLOSED_TO_CONTROL	= "ajax-change-closed-to-control";
	public final static String JX_SEARCH_METRIC_KPI			= "ajax-search-metric-kpi";
	public final static String JX_SEARCH_PROGRAM			= "ajax-search-program";
	public final static String JX_GET_BSCDIMENSIONS			= "ajax-get-bscdimensions";
	public final static String JX_MARK_READ_NOTIFICATION	= "ajax-mark-read-notification";
    public static final String JX_CHANGE_ARCHIVED_TO_CLOSED = "changeArchivedToClosedJX";
    public static final String JX_SAVE_CONFIGURATION		= "JX_SAVE_CONFIGURATION";

    /**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1) {
			
			if (SHOW_DOCUMENTATION.equals(accion)) { showDocument(req, resp); }
			/************** Actions AJAX **************/
			else if (JX_SEARCH_EMPLOYEE.equals(accion)) { searchEmployeeJX(req, resp); }
			else if (JX_SEARCH_CONTACT.equals(accion)) { searchContactJX(req, resp); }
			else if (JX_SEARCH_PROJECTS.equals(accion)) { searchProjectsJX(req, resp); }
			else if (JX_CHANGE_CLOSED_TO_CONTROL.equals(accion)) { changeClosedToControl(req, resp); }
			else if (JX_CHANGE_ARCHIVED_TO_CLOSED.equals(accion)) { changeArchivedToClosedJX(req, resp); }
			else if (JX_SEARCH_METRIC_KPI.equals(accion)) { searchMetricKpiJX(req, resp); }
			else if (JX_SEARCH_PROGRAM.equals(accion)) { searchProgramJX(req, resp); }
			else if (JX_GET_BSCDIMENSIONS.equals(accion)) { getBSCDimensionsJX(req, resp); }
			else if (JX_MARK_READ_NOTIFICATION.equals(accion)) { markReadNotificationJX(req, resp); }


			// TODO ir poniendo la seguridad asi
            else if (SecurityUtil.hasPermission(req, UtilAction.JX_CONSULT_DOCUMENTS, accion)) {
                consultDocumentsJX(req, resp);
            }
			else if (JX_SAVE_CONFIGURATION.equals(accion)) { // Security for all User logged
				saveConfigurationJX(req, resp);
			}
        }
        else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Save configuration
     *
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void saveConfigurationJX(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		PrintWriter out = resp.getWriter();

		try {

			// Request
			String type = ParamUtil.getString(req, "type", null);
			String name = ParamUtil.getString(req, "name", null);
			String value = ParamUtil.getString(req, "value", null);

			// Save or update configuration
			ConfigurationLogic configurationLogic = new ConfigurationLogic();
			configurationLogic.saveConfiguration(getUser(req), type, name, value);

			out.print(JsonUtil.infoTrue());
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }

    }

	/**
     * Consult documents
     *
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void consultDocumentsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        try {

            // Request
            Integer idProject        = ParamUtil.getInteger(req, "idProject", -1);
            String documentationType = ParamUtil.getString(req, "documentationType", null);
            int tab                  = ParamUtil.getInteger(req, "tab", -1);


            if (idProject != -1 && documentationType != null) {

                // Declare logic
                ProjectLogic projectLogic 							= new ProjectLogic(getSettings(req), getResourceBundle(req));
                DocumentprojectLogic documentprojectLogic 			= new DocumentprojectLogic(getSettings(req), getResourceBundle(req));

                List<String> joins = new ArrayList<String>();
                Project project  = projectLogic.consProject(new Project(idProject), joins);


                // Get documents
                //
                List<Documentproject> docs 	 = documentprojectLogic.getDocuments(getSettings(req), project, documentationType);

                // Response
                //
                req.setAttribute("project", project);
                req.setAttribute("docs", docs);
                req.setAttribute("documentStorage", SettingUtil.getString(getSettings(req), Settings.SettingType.DOCUMENT_STORAGE));
                req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
                req.setAttribute("tab", tab);

                forward("/project/common/project_documentation.jsp?documentationType="+documentationType, req, resp);
            }
        }
        catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    }


    /**
     * Change status archived of project to closed
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void changeArchivedToClosedJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {
            ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

            projectLogic.changeArchivedToClosed(new Project(ParamUtil.getInteger(req, "idProject")), getUser(req));

            out.print(JsonUtil.infoTrue());
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
    }

    /**
     * Mark read notification
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void markReadNotificationJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
    	JSONObject infoJSON = new JSONObject();
    	PrintWriter out 	= resp.getWriter();
     	
		try {
			// Request
			int idContactNotification = ParamUtil.getInteger(req, "idContactNotification", -1);
			
			// Declare Logic
			ContactnotificationLogic contactnotificationLogic = new ContactnotificationLogic();
			
			// Logic Find contact notification
			Contactnotification contactnotification = contactnotificationLogic.findById(idContactNotification);
			
			if (contactnotification != null) {
				
				// Set data 
				contactnotification.setReadDate(new Date());
				contactnotification.setReadNotify(true);
				
				// Logic save contact notification
				contactnotificationLogic.save(contactnotification);
				
				// Message response
				infoJSON.put("info", "Successfully");
			}
			else {
				// Message response
				infoJSON.put("info", "Contact Notification is null");
			}
			
			// Response
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
	}

	/**
     * Get BSCDimensions
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void getBSCDimensionsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
    	PrintWriter out = resp.getWriter();
     	
		try {
			
			JSONArray bscdimensionsJSON 		= new JSONArray();
			BscdimensionLogic bscdimensionLogic = new BscdimensionLogic();

			List<Bscdimension> bscdimensions	= bscdimensionLogic.findByCompany(getUser(req));
			
			for (Bscdimension bscdimension : bscdimensions) {
				
				JSONObject bscdimensionJSON = new JSONObject();
				
				bscdimensionJSON.put(Bscdimension.IDBSCDIMENSION, bscdimension.getIdBscDimension());
				bscdimensionJSON.put(Bscdimension.NAME, bscdimension.getName());
				
				bscdimensionsJSON.add(bscdimensionJSON);
			}
			
			out.print(bscdimensionsJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
	}

	/**
     * 
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void searchProgramJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
     	PrintWriter out = resp.getWriter();
     	
     	String name		= ParamUtil.getString(req, "name",StringPool.BLANK);
		int idPerfOrg 	= ParamUtil.getInteger(req, "idPerfOrg", -1);
		
		try {
			ProgramLogic programLogic = new ProgramLogic();
			
			List<Program> programs = programLogic.searchPrograms(name, new Performingorg(idPerfOrg), getCompany(req));
			
			JSONArray programsJSON = new JSONArray();
			
			for(Program program : programs){
				JSONObject programJSON = new JSONObject();
				
				programJSON.put("idProgram", program.getIdProgram());
				programJSON.put("programName", program.getProgramName());
				programJSON.put("perfOrg", program.getPerformingorg().getName());
				
				programsJSON.add(programJSON);
			}
			
			out.print(programsJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
}

	/**
     * Search kpi by filter
     * @param req
     * @param resp
     * @throws IOException 
     */
    private void searchMetricKpiJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
    	String name				= ParamUtil.getString(req, "search_name",null);
    	Integer idProject		= ParamUtil.getInteger(req, "idProject",-1);
    	Integer idBSCDimension	= ParamUtil.getInteger(req, Metrickpi.BSCDIMENSION, -1);
        String type				= ParamUtil.getString(req, Metrickpi.TYPE, null);
    	
    	PrintWriter out = resp.getWriter();
		
		try {
			
			MetrickpiLogic metrickpiLogic = new MetrickpiLogic();
			
			List<String> joins = new ArrayList<String>();
			joins.add(Metrickpi.BSCDIMENSION);
			
			List<Metrickpi> metricKpis	= metrickpiLogic.searchMetricKpis(name, idBSCDimension, type, new Project(idProject), getCompany(req), joins);
			
			JSONArray listJSON = new JSONArray();
			
			for (Metrickpi metrickpi : metricKpis) {
				
				JSONObject itemJSON = new JSONObject();
				
				itemJSON.put(Metrickpi.IDMETRICKPI, metrickpi.getIdMetricKpi());
				itemJSON.put(Metrickpi.NAME, metrickpi.getName());
				itemJSON.put(Metrickpi.DEFINITION, metrickpi.getDefinition());
                itemJSON.put(Metrickpi.TYPE, metrickpi.getTypeKPI());
				itemJSON.put(Metrickpi.BSCDIMENSION, metrickpi.getBscdimension() == null ? StringPool.BLANK : metrickpi.getBscdimension().getName());
				
				listJSON.add(itemJSON);
			}
			out.print(listJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
	}

	/**
     * Change status closed of project to control
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void changeClosedToControl(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
    	PrintWriter out = resp.getWriter();
		
		int idProject = ParamUtil.getInteger(req, "idProject");
		
		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			projectLogic.changeToControl(new Project(idProject), getUser(req));
			
			out.print(JsonUtil.infoTrue());
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
	}

	/**
     * Search projects
     * @param req
     * @param resp
     * @throws IOException
     */
    private void searchProjectsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
    	PrintWriter out = resp.getWriter();
		
		String search 		= ParamUtil.getString(req, Project.PROJECTNAME,null);
		Integer idPerfOrg 	= ParamUtil.getInteger(req, "perf_org", null);
		
		try {
			ProjectLogic projectLogic 	= new ProjectLogic(getSettings(req), getResourceBundle(req));
			Performingorg po 			= null;
            List<Project> list;

			if (SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)) {
				
				if (idPerfOrg != null) { po = new Performingorg(idPerfOrg); }
				
				list = projectLogic.find(search, po, getCompany(req));
			}
			else {
				list = projectLogic.find(search,getUser(req).getPerformingorg(), getCompany(req));
			}
			
			JSONArray listJSON = new JSONArray();
			for (Project item : list) {
				
				JSONObject itemJSON = new JSONObject();
				
				itemJSON.put(Project.IDPROJECT, item.getIdProject());
				itemJSON.put(Project.PROJECTNAME, item.getProjectName());
				itemJSON.put(Project.ACCOUNTINGCODE, item.getAccountingCode());
				itemJSON.put(Project.CHARTLABEL, item.getChartLabel());
				
				if (SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)){
					itemJSON.put(Project.PERFORMINGORG, item.getPerformingorg().getName());
				}
				
				listJSON.add(itemJSON);
			}
			
			out.print(listJSON);
			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
	}


	/**
     * Search contact by filter
     * @param req
     * @param resp
     * @throws IOException
     */
    private void searchContactJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
    	PrintWriter out = resp.getWriter();
		
		String fullName		= ParamUtil.getString(req, "search_name",null);
		String fileAs		= ParamUtil.getString(req, "search_fileas",null);
		Integer idPerfOrg	= ParamUtil.getInteger(req, "search_perforg",-1);
		
		try {
			ContactLogic contactLogic = new ContactLogic();
			List<Contact> contacts = contactLogic.searchContacts(fullName, fileAs, new Performingorg(idPerfOrg), getCompany(req), null, null, null);

			JSONArray contactsJson = new JSONArray();
			for (Contact contact : contacts) {
				
				JSONObject itemJSON = new JSONObject();
				
				itemJSON.put("idContact", contact.getIdContact());
				itemJSON.put("fullname", StringUtil.nullEmpty(contact.getFullName()));
				itemJSON.put("phone", StringUtil.nullEmpty(contact.getMobilePhone()));
				itemJSON.put("jobTitle", StringUtil.nullEmpty(contact.getJobTitle()));
				itemJSON.put("fileAs", StringUtil.nullEmpty(contact.getFileAs()));
				
				contactsJson.add(itemJSON);
			}
			
			out.print(contactsJson);
			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
	}


    /**
     * Search employee by filter
     * @param req
     * @param resp
     * @throws IOException
     */
	private void searchEmployeeJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		String name			= ParamUtil.getString(req, "name",StringPool.BLANK);
		String jobTitle		= ParamUtil.getString(req, "jobTitle",StringPool.BLANK);
		Integer idProfile	= ParamUtil.getInteger(req, "idProfile",-1);
		Integer idPerfOrg	= ParamUtil.getInteger(req, "idPerfOrg",-1);
		
		try {
	    	EmployeeLogic employeeLogic = new EmployeeLogic();
	    	
			List<Employee> employees = employeeLogic.searchEmployees(name, jobTitle, idProfile, idPerfOrg, getUser(req));
			
			JSONArray employeesJson = new JSONArray();
			for (Employee employee : employees) {
				
				JSONObject itemJSON = new JSONObject();
				
				itemJSON.put("idEmployee", employee.getIdEmployee());
				itemJSON.put("fullname", employee.getContact().getFullName());
				itemJSON.put("company", employee.getContact().getCompany().getName());
				itemJSON.put("jobTitle", employee.getContact().getJobTitle());
				itemJSON.put("perfOrg", employee.getPerformingorg().getName());
				
				employeesJson.add(itemJSON);
			}
			
			out.print(employeesJson);
			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showDocument(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Integer docId = ParamUtil.getInteger(req, "docId");

            DocumentationLogic docLogic = new DocumentationLogic();
            ContentFileLogic fileLogic = new ContentFileLogic();
            Documentation doc = docLogic.findById(docId);
            Contentfile docFile = fileLogic.findByDocumentation(doc);

            sendFile(req, resp, docFile.getContent(), doc.getNameFile());
        }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	}
}
