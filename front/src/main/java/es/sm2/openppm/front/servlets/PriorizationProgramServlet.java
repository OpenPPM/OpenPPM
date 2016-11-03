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
 * File: PriorizationProgramServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
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

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;

/**
 * Servlet implementation class ProjectServer
 */
public class PriorizationProgramServlet extends AbstractGenericServlet {
	
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(PriorizationProgramServlet.class);
	
	public final static String REFERENCE = "priorizationprogram";
	
	/***************** Actions ****************/
	
	/************** Actions AJAX **************/
	public final static String JX_UPDATE_PROJECTS	= "ajax-update-projects";
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
    	super.service(req, resp);
    	
    	String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1 && 
			SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, REFERENCE)) &&
			Boolean.parseBoolean(SettingUtil.getString(getSettings(req), Settings.SETTING_SHOW_PRIORIZATION_PROGRAMS, Settings.DEFAULT_SETTING_SHOW_PRIORIZATION_PROGRAMS))) {
			
			/***************** Actions ****************/
			if (accion == null || StringPool.BLANK.equals(accion)) {
				viewProjects(req, resp);
			}
			
			/************** Actions AJAX **************/
			else if (JX_UPDATE_PROJECTS.equals(accion)) { updateProjectsJX(req, resp); }
			
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE + "?accion=403", req, resp);
		}
	}

	/**
	 * Update priority projects
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void updateProjectsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String idsString 		= ParamUtil.getString(req, "ids", StringPool.BLANK);
		String prioritiesString = ParamUtil.getString(req, "priorities", StringPool.BLANK);
		
		PrintWriter out = resp.getWriter();
		
		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			JSONObject infoJSON 	= new JSONObject();
			Integer[] ids 			= null;
			Integer[] priorities 	= null;
			
			
			if (!StringPool.BLANK.equals(idsString)) {
				ids = StringUtil.splitStrToIntegers(idsString, null);
			}
			
			if (!StringPool.BLANK.equals(prioritiesString)) {
				priorities = StringUtil.splitStrToIntegers(prioritiesString, null);
			}
			
			if (ids != null && priorities != null && ids.length == priorities.length) {
				
				for (int i= 0; i < ids.length; i++) {
					
					Project project = projectLogic.findById(ids[i]);
					
					project.setPriority(priorities[i]);
					
					projectLogic.save(project);
				}
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON,"program");
			}
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}    	
	}

	/**
	 * View  projects for program
	 * @param req
	 * @param resp
	 */
	private void viewProjects(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idProgram = ParamUtil.getInteger(req, "program_id");
		
		try {
			ProgramLogic programLogic = new ProgramLogic();
			
			Program program = programLogic.findById(idProgram);
			
			if (program != null) {

				ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
				
				List<String> investmentStatus = new ArrayList<String>();
				
				investmentStatus.add(Constants.INVESTMENT_APPROVED);
				investmentStatus.add(Constants.INVESTMENT_CLOSED);
				investmentStatus.add(Constants.INVESTMENT_IN_PROCESS);
				
				List<Project> projects = projectLogic.findByProgramAndInvestmentStatus(program, investmentStatus);
				
				req.setAttribute("program", program);
				req.setAttribute("projects", projects);
				req.setAttribute("title", program.getProgramName());
			}
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		forward("/index.jsp?nextForm=program/priorization_program", req, resp);
	}
   
}