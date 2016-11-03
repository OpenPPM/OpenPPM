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
 * File: ProjectClosureServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

/** -------------------------------------------------------------------------
*
* Desarrollado por SM2 Baleares S.A.
*
* -------------------------------------------------------------------------
* Este fichero solo podra ser copiado, distribuido y utilizado
* en su totalidad o en parte, de acuerdo con los terminos y
* condiciones establecidas en el acuerdo/contrato bajo el que se
* suministra.
* -------------------------------------------------------------------------
*
* Proyecto : OpenPPM
* Autor : Javier Hernandez
* Fecha : Miercoles, 22 de Septiembre, 2011
*
* -------------------------------------------------------------------------
*/

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.charter.ProjectCloseTemplate;
import es.sm2.openppm.core.logic.impl.ClosurecheckLogic;
import es.sm2.openppm.core.logic.impl.ClosurecheckprojectLogic;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.logic.impl.ProblemcheckLogic;
import es.sm2.openppm.core.logic.impl.ProblemcheckprojectLogic;
import es.sm2.openppm.core.logic.impl.ProjectCharterLogic;
import es.sm2.openppm.core.logic.impl.ProjectDataLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.ProjectclosureLogic;
import es.sm2.openppm.core.logic.impl.RiskRegisterLogic;
import es.sm2.openppm.core.logic.impl.TimesheetLogic;
import es.sm2.openppm.core.logic.security.actions.ClosureTabAction;
import es.sm2.openppm.core.model.impl.Closurecheck;
import es.sm2.openppm.core.model.impl.Closurecheckproject;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Problemcheck;
import es.sm2.openppm.core.model.impl.Problemcheckproject;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.ProjectData;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Projectclosure;
import es.sm2.openppm.core.reports.GenerateReportInterface;
import es.sm2.openppm.core.reports.annotations.ReportType;
import es.sm2.openppm.core.reports.beans.ReportFile;
import es.sm2.openppm.core.reports.beans.ReportParams;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import java.util.List;

public class ProjectClosureServlet extends AbstractGenericServlet {

	private static final long serialVersionUID = 1L;
	
	private static final  Logger LOGGER = Logger.getLogger(ProjectClosureServlet.class);
	
	public static final String REFERENCE = "projectclosure";
	
	/***************** Actions ****************/
	public static final String VIEW_CLOSE		= "view-close";

	/************** Actions AJAX **************/
	public static final String JX_SAVE_CLOSURE 						= "ajax-save-closure";
	public static final String JX_SAVE_DOCUMENTATION 				= "ajax-save-documentation";
	public static final String JX_CHECK_CLOSURE_PROJECT 			= "ajax-check-closure-project";
	public static final String JX_SAVE_CLOSURE_CHECK_PROJECT 		= "ajax-save-closure-check-project";
	public static final String JX_DELETE_CLOSURE_CHECK_PROJECT 		= "ajax-delete-closure-check-project";
	public static final String JX_SELECT_PROBLEM_CHECK_LIST 		= "ajax-select-problem-check-list";
	public static final String JX_SAVE_PROBLEM_CHECK_LIST 			= "ajax-save-problem-check-list";
	public static final String JX_DELETE_PROBLEM_CHECK_LIST 		= "ajax-delete-problem-check-list";

    /**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.service(req, resp);
		
		String accion = ParamUtil.getString(req, "accion");
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, REFERENCE))) {

			/***************** Actions ****************/
            if (ValidateUtil.isNull(accion)) { viewClosure(req, resp, null); }
            else if (VIEW_CLOSE.equals(accion)){ viewProjectClose(req, resp); }

            /************** Actions AJAX **************/
            else if (JX_SAVE_CLOSURE.equals(accion)){ saveProjectClosureJX(req, resp); }
            else if (JX_SAVE_DOCUMENTATION.equals(accion)){ saveDocumentationJX(req, resp); }
            else if (JX_CHECK_CLOSURE_PROJECT.equals(accion)){ validateCloseProject(req, resp); }
            else if (JX_SAVE_CLOSURE_CHECK_PROJECT.equals(accion)){ saveClosureCheckProjectJX(req, resp); }
            else if (JX_DELETE_CLOSURE_CHECK_PROJECT.equals(accion)){ deleteClosureCheckProjectJX(req, resp); }
            else if (JX_SELECT_PROBLEM_CHECK_LIST.equals(accion)){ selectProblemCheckListJX(req, resp); }
            else if (JX_SAVE_PROBLEM_CHECK_LIST.equals(accion)){ saveProblemCheckListJX(req, resp); }
            else if (JX_DELETE_PROBLEM_CHECK_LIST.equals(accion)){ deleteProblemCheckListJX(req, resp); }

        }
        else if (SecurityUtil.hasPermission(req, ClosureTabAction.CLOSE_PROJECT, accion)) { closeProject(req,resp); }
        else if (SecurityUtil.hasPermission(req, ClosureTabAction.ARCHIVE_PROJECT, accion)) { archiveProject(req, resp); }
		else if (!isForward()) {
            forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Archive project
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void archiveProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int idProject 		= ParamUtil.getInteger(req, "id");

        try {

            // Declare logic
            ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

            // Logic
            projectLogic.archiveProject(new Project(idProject), getUser(req));
        }
        catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

        viewClosure(req, resp, idProject);
    }

    /**
	 * Delete problem check list
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void deleteProblemCheckListJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		//
		Integer idProblemCheckProject = ParamUtil.getInteger(req, Problemcheckproject.IDPROBLEMCHECKPROJECT, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject returnJSON = new JSONObject();
			
			// Declare logic
			ProblemcheckprojectLogic problemcheckprojectLogic = new ProblemcheckprojectLogic();
			
			if (idProblemCheckProject != -1) {
				
				// Logic
				problemcheckprojectLogic.delete(problemcheckprojectLogic.findById(idProblemCheckProject));
				
				// Response 
				//
				returnJSON = infoDeleted(getResourceBundle(req), "problem_check_list"); 
			}
			else {
				returnJSON.put("error", "data_not_found");
			}
			
			out.print(returnJSON);			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Save problem check list
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveProblemCheckListJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		//
		Integer idProject 		= ParamUtil.getInteger(req, Project.IDPROJECT, -1);
		Integer idProblemCheck 	= ParamUtil.getInteger(req, Problemcheck.IDPROBLEMCHECK, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject returnJSON = new JSONObject();
			
			// Declare logic
			ProblemcheckprojectLogic problemcheckprojectLogic = new ProblemcheckprojectLogic();
			
			// Set data
			Problemcheckproject problemcheckproject = new Problemcheckproject();
			problemcheckproject.setProject(new Project(idProject));
			problemcheckproject.setProblemcheck(new Problemcheck(idProblemCheck));
			
			// Logic
			problemcheckproject = problemcheckprojectLogic.save(problemcheckproject);
			
			// Response 
			//
			returnJSON.put(Problemcheckproject.IDPROBLEMCHECKPROJECT, problemcheckproject.getIdProblemCheckProject());
			returnJSON = infoCreated(getResourceBundle(req), returnJSON, "problem_check_list"); 
			out.print(returnJSON);			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Select problem check list
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void selectProblemCheckListJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		// Request
		//
		Integer idProject = ParamUtil.getInteger(req, Project.IDPROJECT, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONArray checksJSON = new JSONArray();
			
			// Declare logic
			ProblemcheckLogic problemcheckLogic = new ProblemcheckLogic();
			
			// Logic
			List<Problemcheck> problemchecks = problemcheckLogic.findbyRemainAndActive(new Project(idProject), getCompany(req));
			
			// Parse to JSON 
			//
			if (ValidateUtil.isNotNull(problemchecks)) {
				
				for (Problemcheck problemcheck : problemchecks) {
					
					JSONObject checkJSON = new JSONObject();
					
					checkJSON.put(Problemcheck.IDPROBLEMCHECK , problemcheck.getIdProblemCheck());
					checkJSON.put(Problemcheck.NAME , problemcheck.getName());
					checkJSON.put(Problemcheck.DESCRIPTION , problemcheck.getDescription());
					
					checksJSON.add(checkJSON);
				}
			}
			
			// Response 
			//
			out.print(checksJSON);			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Delete closure check project
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void deleteClosureCheckProjectJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		//
		Integer idClosureCheckProject 	= ParamUtil.getInteger(req, Closurecheckproject.IDCLOSURECHECKPROJECT, -1);
		Integer idDocumentProject	 	= ParamUtil.getInteger(req, Documentproject.IDDOCUMENTPROJECT, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			
			Closurecheckproject closurecheckproject = new Closurecheckproject(idClosureCheckProject);
			JSONObject info = new JSONObject();
			
			// Declare logic
			ClosurecheckprojectLogic closurecheckprojectLogic = new ClosurecheckprojectLogic();
			
			// Set document 
			//
			closurecheckproject.setDocumentproject(new Documentproject(idDocumentProject));
			
			// Logic
			closurecheckprojectLogic.deleteEntityAndDocument(closurecheckproject);
			
			// Response 
			//
			info = infoDeleted(getResourceBundle(req), "closure_check_list");
			
			out.print(info);			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
		
	}

	/**
	 * Save closure check project
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveClosureCheckProjectJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		//
		Integer idClosureCheckProject 	= ParamUtil.getInteger(req, Closurecheckproject.IDCLOSURECHECKPROJECT, -1);
		Integer idClosureCheck 			= ParamUtil.getInteger(req, Closurecheckproject.CLOSURECHECK, -1);
		Integer idProject	 			= ParamUtil.getInteger(req, Project.IDPROJECT, -1);
		String comments 				= ParamUtil.getString(req, Closurecheckproject.COMMENTS, StringPool.BLANK);
		Boolean realized				= ParamUtil.getBoolean(req, Closurecheckproject.REALIZED, false);
		Date dateRealized				= ParamUtil.getDate(req, Closurecheckproject.DATEREALIZED, getDateFormat(req), null);
		String manager 					= ParamUtil.getString(req, Closurecheckproject.MANAGER, StringPool.BLANK);
		String departament 				= ParamUtil.getString(req, Closurecheckproject.DEPARTAMENT, StringPool.BLANK);
		
		PrintWriter out = resp.getWriter();
		
		try {
			
			JSONObject info = new JSONObject();
			Closurecheckproject closurecheckproject = new Closurecheckproject();
			
			// Declare logic
			ClosurecheckprojectLogic closurecheckprojectLogic = new ClosurecheckprojectLogic();
			
			// Set data
			//
			if (idClosureCheckProject != -1) {
				
				closurecheckproject = closurecheckprojectLogic.findById(idClosureCheckProject);
				
				info = infoUpdated(getResourceBundle(req), info, "closure_check_list");
			}
			else {
				
				info = infoCreated(getResourceBundle(req), info, "closure_check_list");
				
				closurecheckproject.setProject(new Project(idProject));
			}
			
			// Restriction
			if ((idClosureCheckProject == -1 && closurecheckprojectLogic.hasClosureCheck(new Closurecheck(idClosureCheck), new Project(idProject)) )|| 
					(idClosureCheckProject != -1 &&  closurecheckproject.getClosurecheck() != null && idClosureCheck != -1 && 
						!closurecheckproject.getClosurecheck().getIdClosureCheck().equals(idClosureCheck) &&
						closurecheckprojectLogic.hasClosureCheck(new Closurecheck(idClosureCheck), new Project(idProject)))) {
				
				throw new Exception(getResourceBundle(req).getString("msg.duplicated_checklist"));
			}
			
			closurecheckproject.setClosurecheck(new Closurecheck(idClosureCheck));
			closurecheckproject.setComments(comments);
			closurecheckproject.setRealized(realized);
			closurecheckproject.setDateRealized(dateRealized);
			closurecheckproject.setManager(manager);
			closurecheckproject.setDepartament(departament);
			
			// Logic
			closurecheckproject = closurecheckprojectLogic.save(closurecheckproject);
			
			if (closurecheckproject != null) {
				info.put(Closurecheckproject.IDCLOSURECHECKPROJECT, closurecheckproject.getIdClosureCheckProject());
			}
			
			out.print(info);			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Check hours in APP2 or APP1 for close project
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
    private void validateCloseProject(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
    	PrintWriter out = resp.getWriter();
		
		try {
			
			// Instance of project
			Project project = new Project(ParamUtil.getInteger(req, "id", -1));
			
			// Check for risks closed
			RiskRegisterLogic riskRegisterLogic = new RiskRegisterLogic();
			if (riskRegisterLogic.risksNotClosed(project)) {
				throw new LogicException("msg.error.project.risks_closed");
			}

			// Check for hours pending approval
			TimesheetLogic timesheetLogic = new TimesheetLogic();
			if (timesheetLogic.pendingApproval(project)) {
				throw new LogicException("msg.error.project.hours_pending");
			}
			
			// Create warnings
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			List<String> warnings = projectLogic.checkForCloseProject(project);
			
			JSONObject infoJSON = new JSONObject();
			
			if (ValidateUtil.isNotNull(warnings)) {
				
				for (String warning : warnings) {
					
					// Add warnings
					info("showWarning", warning, infoJSON);
				}
			}
			
			out.print(infoJSON.toString());			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Close project
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void closeProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
    	int idProject 		= ParamUtil.getInteger(req, "id");
		String comments 	= ParamUtil.getString(req, "comments", null);
		String stkComments	= ParamUtil.getString(req, "stk_comments", null);
		
		try {
			
			// Declare logic
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			Project project = projectLogic.consProject(idProject);
			project.setCloseComments(comments);
			project.setCloseStakeholderComments(stkComments);
			
			// Logic
			projectLogic.closeProject(getSettings(req), project, getUser(req));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewClosure(req, resp, idProject);
	}
    
	/**
	 * View Project Closure
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewClosure(HttpServletRequest req, HttpServletResponse resp, Integer idProject) throws ServletException, IOException {
		
		Project project							= null;
		List<Documentproject> docs				;
		List<Documentproject> docsRepository	;
		List<Documentproject> docInv			= new ArrayList<Documentproject>();
		List<Documentproject> docInit			= new ArrayList<Documentproject>();
		List<Documentproject> docRisk			= new ArrayList<Documentproject>();
		List<Documentproject> docPlan			= new ArrayList<Documentproject>();
		List<Documentproject> docControl		= new ArrayList<Documentproject>();
		List<Documentproject> docProcurement	= new ArrayList<Documentproject>();
		List<Documentproject> docClosure		= new ArrayList<Documentproject>();
		List<Closurecheck> closureChecks		= new ArrayList<Closurecheck>();
		List<Projectclosure> closures			;
		Projectclosure closure					= null;
		String[] departaments 					= null;
		List<ProjectData> projectsData			= null;

		if (idProject == null) {
			idProject = ParamUtil.getInteger(req, "id", (Integer)req.getSession().getAttribute("idProject"));
		}
		
		boolean hasPermission = false;
		
		try {
			
			hasPermission = (SecurityUtil.hasPermission(req, new Project(idProject), Constants.TAB_CLOSURE) &&
					SecurityUtil.hasPermissionProject(req, Constants.STATUS_CONTROL, Constants.STATUS_CLOSED, Constants.STATUS_ARCHIVED));
			
			if (hasPermission) {
				
				// Declare logic
				DocumentprojectLogic documentprojectLogic 			= new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
				ProjectLogic projectLogic 							= new ProjectLogic(getSettings(req), getResourceBundle(req));
				ProjectclosureLogic closureLogic					= new ProjectclosureLogic();;
				ClosurecheckLogic closurecheckLogic					= new ClosurecheckLogic();
				ClosurecheckprojectLogic closurecheckprojectLogic 	= new ClosurecheckprojectLogic();
				ProjectDataLogic projectDataLogic					= new ProjectDataLogic();
				
				List<String> joins = new ArrayList<String>();
				joins.add(Project.PROGRAM);
                joins.add(Project.PROGRAM+"."+ Program.EMPLOYEE);
				joins.add(Project.PROGRAM + "." + Program.EMPLOYEE + "." + Employee.CONTACT);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
				joins.add(Project.WORKINGCOSTSES);
				joins.add(Project.CLOSURECHECKPROJECTS);
				joins.add(Project.CLOSURECHECKPROJECTS+"."+Closurecheckproject.CLOSURECHECK);
				joins.add(Project.CLOSURECHECKPROJECTS+"."+Closurecheckproject.DOCUMENTPROJECT);
				joins.add(Project.PROBLEMCHECKPROJECTS);
				joins.add(Project.PROBLEMCHECKPROJECTS+"."+Problemcheckproject.PROBLEMCHECK);
				joins.add(Project.PROJECSTDATA);
				
				// Logic get closure checks
				closureChecks = closurecheckLogic.findByCompanyAndActive(getCompany(req));
				
				closurecheckprojectLogic.createNews(closureChecks, new Project(idProject));
				
				// Logic
				project = projectLogic.consProject(new Project(idProject), joins);
				
				String docStorage = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_STORAGE, Settings.DEFAULT_PROJECT_DOCUMENT_STORAGE);
				
				// Get documents to repository
				//
				docsRepository = documentprojectLogic.getDocuments(getSettings(req), project, null);
				
				if (ValidateUtil.isNotNull(docsRepository)) {
					
					// Order
					for (Documentproject doc : docsRepository) {
						
						if (Constants.DOCUMENT_INVESTMENT.equals(doc.getType())) {
							docInv.add(doc);
						}
						else if (Constants.DOCUMENT_INITIATING.equals(doc.getType())) {
							docInit.add(doc);
						}
						else if (Constants.DOCUMENT_PLANNING.equals(doc.getType())) {
							docPlan.add(doc);
						}
						else if (Constants.DOCUMENT_RISK.equals(doc.getType())) {
							docRisk.add(doc);
						}
						else if (Constants.DOCUMENT_CONTROL.equals(doc.getType())) {
							docControl.add(doc);
						}
						else if (Constants.DOCUMENT_PROCUREMENT.equals(doc.getType())) {
							docProcurement.add(doc);
						}
						else if (Constants.DOCUMENT_CLOSURE.equals(doc.getType())) {
							docClosure.add(doc);
						}
					}
					
					docsRepository = new ArrayList<Documentproject>();
					
					docsRepository.addAll(docInv);
					docsRepository.addAll(docInit);
					docsRepository.addAll(docPlan);
					docsRepository.addAll(docRisk);
					docsRepository.addAll(docControl);
					docsRepository.addAll(docProcurement);
					docsRepository.addAll(docClosure);
				}
				
				// Get documents to closure
				docs = documentprojectLogic.getDocuments(getSettings(req), project, Constants.DOCUMENT_CLOSURE);
				
				req.setAttribute("docsRepository", docsRepository);	
				req.setAttribute("docs", docs);	
				req.setAttribute("documentStorage", docStorage);
				
				// Logic get closures
				closures = closureLogic.findByRelation(Projectclosure.PROJECT, new Project(idProject));

				// Logic get project data
				projectsData = projectDataLogic.findByRelation(ProjectData.PROJECT, new Project(idProject));

				// Get departaments
				departaments = SettingUtil.getString(getSettings(req), Settings.SETTING_WORKINGCOSTS_DEPARTMENTS, Settings.DEFAULT_WORKINGCOSTS_DEPARTMENTS).split(",");
				
				// If empty
				if (departaments.length == 1 && departaments[0].equals(StringPool.BLANK)) {
					departaments = null;
				}
				
				if (!closures.isEmpty()) { closure = closures.get(0); }
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		if (hasPermission) {
			
			req.setAttribute("title", ValidateUtil.isNotNull(project.getChartLabel()) ? 
					project.getProjectName() + " ("+project.getChartLabel()+")" : project.getProjectName());
			req.setAttribute("project", project);
			req.setAttribute("closure", closure);
			req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
			req.setAttribute("closureChecks", closureChecks);
			req.setAttribute("departaments", departaments);
            req.setAttribute("projectData", ValidateUtil.isNotNull(projectsData) ? projectsData.get(0) : null);

            // Send actual date
            SimpleDateFormat date = new SimpleDateFormat(Constants.DATE_PATTERN);

            req.setAttribute("actualDate", date.format(new Date()));

            req.setAttribute("tab", Constants.TAB_CLOSURE);


			forward("/index.jsp?nextForm=project/closure/closure_project", req, resp);
		}
		else {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewProjectClose(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Flag for erros
		boolean error				= false;

		// Id of project
		int idProject = getIdProject(req);
		
		try {

            GenerateReportInterface generateReport = CacheStatic.getGenerateReport(ReportType.PROJECT_CLOSE);

            if (generateReport != null) {

                // Call to generate report file
                ReportFile reportFile = generateReport.generate(new ReportParams(new Project(idProject), getResourceBundle(req), getSettings(req), getUser(req)));

                if (reportFile == null) {
                    error = true;
                }
                else {
                    // Send report
                    sendFile(req, resp, reportFile.getFile(), reportFile.getName()+ reportFile.getExtension());
                }
            }
            else {

                // TODO javier.hernandez - 21/03/2015 - deprecated code migrate to GenerateReportInterface
                ProjectCharterLogic projectCharterLogic = new ProjectCharterLogic();
                ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

                List<String> joins = new ArrayList<String>();
                joins.add(Project.PROGRAM);
                joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
                joins.add(Project.EMPLOYEEBYPROJECTMANAGER + "." + Employee.CONTACT);

                Project proj = projectLogic.consProject(new Project(idProject), joins);

                Projectcharter projClose = projectCharterLogic.findByProject(proj);

                ProjectCloseTemplate projCloseTemplate = ProjectCloseTemplate.getProjectCloseTemplate(getSettings(req));

                File charterDocFile = projCloseTemplate.generateClose(
                        getResourceBundle(req), proj, projClose, getSettings(req), getUser(req).getContact().getFullName());

                byte[] charterDoc = null;

                if (charterDocFile != null) {
                    charterDoc = DocumentUtils.getBytesFromFile(charterDocFile);

                    // delete temporal file
                    DocumentUtils.deleteFile(charterDocFile);
                }

                proj = projectLogic.consProject(proj.getIdProject());

                if (charterDoc != null) {
                    sendFile(req, resp, charterDoc, proj.getProjectName() + "_" + proj.getAccountingCode() + SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_CLOSE_EXTENSION, Settings.DEFAULT_PROJECT_CLOSE_EXTENSION));
                }
            }
		}
		catch (Exception e) {
			error = true;
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}

		if (error) {
			viewClosure(req, resp, idProject);
		}
	}

    /**
     * Save project clousure by AJAX
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void saveProjectClosureJX (HttpServletRequest req, HttpServletResponse resp) 
	throws IOException {

		PrintWriter out = resp.getWriter();

        try {
            Integer idProject       = ParamUtil.getInteger(req, "id", -1);
            String result           = ParamUtil.getString(req, "results", "");
            String goals            = ParamUtil.getString(req, "goal", "");
            String lessons          = ParamUtil.getString(req, "lessons", "");
            Boolean projectCanceled = ParamUtil.getBoolean(req, "canceled", false);
            Date dateCancel         = ParamUtil.getDate(req, "dateCanceled", getDateFormat(req), null);
            String commentCanceled  = ParamUtil.getString(req, "commentCanceled");

            Projectclosure closure;
            List<Projectclosure> closures;
            Project project;
            ProjectData projectData;

            if (idProject == -1) {
                throw new Exception("No data project");
            }

            // Logics
			ProjectclosureLogic closureLogic    = new ProjectclosureLogic();
            ProjectDataLogic projectDataLogic   = new ProjectDataLogic();
            ProjectLogic projectLogic           = new ProjectLogic(getSettings(req), getResourceBundle(req));

			closures = closureLogic.findByRelation(Projectclosure.PROJECT, new Project(idProject));			
			
			if(closures.size() > 0) {
				closure = closures.get(0);
			}
			else {
				closure = new Projectclosure();				
			}
			
			closure.setProject(new Project(idProject));
			closure.setProjectResults(result);
			closure.setGoalAchievement(goals);
			closure.setLessonsLearned(lessons);
			
			closureLogic.save(closure);

            // Get Project
            project = projectLogic.consProject(idProject);

            // Save project data
            //
            List<ProjectData> projectsData = projectDataLogic.findByRelation(ProjectData.PROJECT, project);

            if(ValidateUtil.isNotNull(projectsData)) {
                projectData = projectsData.get(0);
            }
            else {
                projectData = new ProjectData();
            }

            projectData.setProject(new Project(idProject));
            projectData.setCanceled(projectCanceled);
            projectData.setDateCanceled(dateCancel);
            projectData.setCommentCanceled(commentCanceled);

            projectDataLogic.save(projectData);

            // Info
			out.print(infoUpdated(getResourceBundle(req), null, "closure.project"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    /**
     * Save documentation
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void saveDocumentationJX (HttpServletRequest req, HttpServletResponse resp) 
			throws IOException {

		PrintWriter out = resp.getWriter();
		
		Integer idProject = ParamUtil.getInteger(req, "project", -1);
		Integer idDocument = ParamUtil.getInteger(req, "document", -1);
		String comment = ParamUtil.getString(req, "comment", "");		
		
		Project project 	= null;
		Documentproject doc = null;
		
		try {		
			DocumentprojectLogic documentprojectLogic 	= new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			if(idDocument == -1) {
				project = projectLogic.consProject(idProject);
				project.setLinkComment(comment);
				projectLogic.save(project);
			}
			else {		
				doc = documentprojectLogic.findById(idDocument, false);
				doc.setContentComment(comment);
				documentprojectLogic.save(doc);
			}
									
			JSONObject info = null;
			out.print(infoUpdated(getResourceBundle(req), info, "documment"));			
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}
}

