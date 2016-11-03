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
 * File: ChangePOServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.ResourceProfilesLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;

/**
 * Servlet implementation class ProjectServer
 */
public class ChangePOServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(ChangePOServlet.class);
	
	public final static String REFERENCE = "changepo";
	
	/***************** Actions ****************/
	
	/************** Actions AJAX **************/
	public final static String JX_VIEW_PO 	= "ajax-view-po";
	public final static String JX_UPDATE_PO = "ajax-update-po";
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
    	String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, REFERENCE))) {
			
			/***************** Actions ****************/
			if (accion == null || StringPool.BLANK.equals(accion)) {
				viewPOs(req, resp);
			}
			
			/************** Actions AJAX **************/
			else if (JX_VIEW_PO.equals(accion)) { viewPOJX(req, resp); }
			else if (JX_UPDATE_PO.equals(accion)) { updatePOJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE + "?accion=403", req, resp);
		}
	}

	/**
	 * Change PO, im, pm, fm employees and program
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void updatePOJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer id					= ParamUtil.getInteger(req, "id", null);
		Integer idPerfOrg 			= ParamUtil.getInteger(req, "idPerfOrg", null);
		Integer idInvestmentManager = ParamUtil.getInteger(req, "idInvestmentManager", null);
		Integer idProjectManager 	= ParamUtil.getInteger(req, "idProjectManager", null);
		Integer idFunctionalManager = ParamUtil.getInteger(req, "idFunctionalManager", null);
		Integer idSponsor 			= ParamUtil.getInteger(req, "idSponsor", null);
		Integer idProgram 			= ParamUtil.getInteger(req, "idProgram", null);
		
		PrintWriter out = resp.getWriter();
    	
    	try {
     		JSONObject updateJSON = new JSONObject();
     		
     		ProjectLogic projectLogic 				= new ProjectLogic(getSettings(req), getResourceBundle(req));
     		PerformingOrgLogic performingOrgLogic 	= new PerformingOrgLogic();
     		EmployeeLogic employeeLogic				= new EmployeeLogic();
     		ProgramLogic programLogic				= new ProgramLogic();
     		
			if (id != null && idPerfOrg != null) {
				Project project = projectLogic.findById(id);
				Performingorg po = performingOrgLogic.findById(idPerfOrg);

				project.setPerformingorg(po);

				if (idInvestmentManager != null) {
					Employee im = employeeLogic.findById(idInvestmentManager);

					project.setEmployeeByInvestmentManager(im);
				}

				if (idProjectManager != null) {
					Employee pm = employeeLogic.findById(idProjectManager);

					project.setEmployeeByProjectManager(pm);
				}

				if (idFunctionalManager != null) {
					Employee fm = employeeLogic.findById(idFunctionalManager);

					project.setEmployeeByFunctionalManager(fm);
				}

				if (idSponsor != null) {
					Employee sp = employeeLogic.findById(idSponsor);

					project.setEmployeeBySponsor(sp);
				}

				if (idProgram != null) {
					Program program = programLogic.findById(idProgram);

					project.setProgram(program);
				}

				projectLogic.save(project);

				updateJSON.put("INFO", getResourceBundle(req).getString("project.updated"));
			}
			else {
				updateJSON.put("INFO", getResourceBundle(req).getString("project.not_updated"));
			}
     		
    		out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
		
	}

	/**
	 * View select PO
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void viewPOJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idPerfOrg 			= ParamUtil.getInteger(req, "idPerfOrg", null);
		Integer idInvestmentManager = ParamUtil.getInteger(req, "idInvestmentManager", null);
		Integer idProjectManager 	= ParamUtil.getInteger(req, "idProjectManager", null);
		Integer idFunctionalManager = ParamUtil.getInteger(req, "idFunctionalManager", null);
		Integer idSponsor 			= ParamUtil.getInteger(req, "idSponsor", null);
		Integer idProgram 			= ParamUtil.getInteger(req, "idProgram", null);
		
		PrintWriter out = resp.getWriter();
    	
    	try {
     		JSONObject updateJSON 				= new JSONObject();
     		JSONObject investmentManagerJSON 	= new JSONObject();
     		JSONObject projectManagerJSON 		= new JSONObject();
     		JSONObject functionalManagerJSON 	= new JSONObject();
     		JSONObject sponsorJSON 				= new JSONObject();
     		JSONObject programJSON 				= new JSONObject();
     		
     		Employee imNewPO					= null;
     		Employee pmNewPO					= null;
     		Employee fmNewPO					= null;
     		Employee spNewPO					= null;
     		Program programNewPO				= null;
     		
     		EmployeeLogic employeeLogic 		= new EmployeeLogic();
     		ProgramLogic programLogic			= new ProgramLogic();
     		
     		List<String> joins = new ArrayList<String>();
     		joins.add(Employee.CONTACT);
     		joins.add(Employee.RESOURCEPROFILES);
     		
     		Employee im 	= employeeLogic.findById(idInvestmentManager, joins);
     		Employee pm 	= employeeLogic.findById(idProjectManager, joins);
     		Employee fm 	= employeeLogic.findById(idFunctionalManager, joins);
     		Employee sp 	= employeeLogic.findById(idSponsor, joins);
     		Program program = programLogic.findById(idProgram);
     		
     		if(idPerfOrg != null){
     			
     			 if(idInvestmentManager != null){
      				imNewPO = employeeLogic.findByContactAndPOAndRol(im.getContact().getIdContact(), new Performingorg(idPerfOrg), im.getResourceprofiles().getIdProfile()) ;
     	     		
 	     			if(imNewPO != null){
 		     			investmentManagerJSON.put("id", imNewPO.getIdEmployee());
 		     			investmentManagerJSON.put("name", imNewPO.getContact().getFullName());
 	     			}
     			 }
     			 
     			 if(idProjectManager != null){
	     			pmNewPO = employeeLogic.findByContactAndPOAndRol(pm.getContact().getIdContact(), new Performingorg(idPerfOrg), pm.getResourceprofiles().getIdProfile()) ;
	     		
	     			if(pmNewPO != null){
		     			projectManagerJSON.put("id", pmNewPO.getIdEmployee());
		         		projectManagerJSON.put("name", pmNewPO.getContact().getFullName());
	     			}
     			}
     			
     			 if(idFunctionalManager != null){
     				fmNewPO = employeeLogic.findByContactAndPOAndRol(fm.getContact().getIdContact(), new Performingorg(idPerfOrg), fm.getResourceprofiles().getIdProfile()) ;
    	     		
	     			if(fmNewPO != null){
	     				functionalManagerJSON.put("id", fmNewPO.getIdEmployee());
	     				functionalManagerJSON.put("name", fmNewPO.getContact().getFullName());
	     			}
     			}
     			 
     			if(idSponsor != null){
      				spNewPO = employeeLogic.findByContactAndPOAndRol(sp.getContact().getIdContact(), new Performingorg(idPerfOrg), sp.getResourceprofiles().getIdProfile()) ;
     	     		
 	     			if(spNewPO != null){
 	     				sponsorJSON.put("id", spNewPO.getIdEmployee());
 	     				sponsorJSON.put("name", spNewPO.getContact().getFullName());
 	     			}
      			}
     			
     			if(idProgram != null){
     				programNewPO = programLogic.findByNameProgramAndPO(program.getProgramName(), new Performingorg(idPerfOrg));
     				
     				if(programNewPO != null){
     					programJSON.put("id", programNewPO.getIdProgram());
     					programJSON.put("name", programNewPO.getProgramName());
     				}
     			}
     			 
     		}
     		
     		updateJSON.put("investmentManager", investmentManagerJSON);
     		updateJSON.put("projectManager", projectManagerJSON);
     		updateJSON.put("functionalManager", functionalManagerJSON);
     		updateJSON.put("sponsor", sponsorJSON);
     		updateJSON.put("program", programJSON);
     		
    		out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * View other POs
	 * @param req
	 * @param resp
	 */
	private void viewPOs(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Request
		int idProject = ParamUtil.getInteger(req, "id");
		
		try {
			
			// Declare logics
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));
			PerformingOrgLogic performingOrgLogic 		= new PerformingOrgLogic();
			ResourceProfilesLogic resourceProfilesLogic = new ResourceProfilesLogic();
			
			// Joins
			List<String> joins = new ArrayList<String>();
			joins.add(Project.EMPLOYEEBYFUNCTIONALMANAGER);
			joins.add(Project.EMPLOYEEBYFUNCTIONALMANAGER +"."+Employee.CONTACT);			
			
			joins.add(Project.EMPLOYEEBYINVESTMENTMANAGER);
			joins.add(Project.EMPLOYEEBYINVESTMENTMANAGER+"."+Employee.CONTACT);
			
			joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
			joins.add(Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
			
			joins.add(Project.EMPLOYEEBYSPONSOR);
			joins.add(Project.EMPLOYEEBYSPONSOR +"."+Employee.CONTACT);
			
			joins.add(Project.PROGRAM);
			
			// Logic Project
			Project project = projectLogic.findById(idProject, joins);
			
			// Logic Profiles
			List<Resourceprofiles> profiles = resourceProfilesLogic.findAll();
			
			// Logic PO
			List<Performingorg> perfOrgs = performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req));
			
			// Response
			req.setAttribute("profiles", profiles);
			req.setAttribute("perforgs", perfOrgs);
			req.setAttribute("project", project);
			req.setAttribute("title", project.getProjectName());
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		forward("/index.jsp?nextForm=project/performing_organization/change_po", req, resp);
	}
   
}