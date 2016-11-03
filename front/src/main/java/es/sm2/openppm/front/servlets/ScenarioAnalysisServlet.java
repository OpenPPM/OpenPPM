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
 * File: ScenarioAnalysisServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.javabean.Line;
import es.sm2.openppm.core.javabean.Simplex;
import es.sm2.openppm.core.logic.impl.ChartLogic;
import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.utils.JSONModelUtil;
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
public class ScenarioAnalysisServlet extends AbstractGenericServlet {
	
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(ScenarioAnalysisServlet.class);
	
	public final static String REFERENCE = "scenarioanalysis";
	
	/***************** Actions ****************/
	
	/************** Actions AJAX **************/
	public final static String JX_SCENARIO_ANALYSIS_CHART 	= "ajax-scenario-anaylisis-chart";
	public final static String JX_EFFICIENT_FRONTIER_CHART 	= "ajax-efficient-frontier-chart";
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
    	super.service(req, resp);
    	
    	String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1 && 
			SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, REFERENCE)) && 
			Boolean.parseBoolean(SettingUtil.getString(getSettings(req), Settings.SETTING_SHOW_SCENARIO_ANALYSIS, Settings.DEFAULT_SETTING_SHOW_SCENARIO_ANALYSIS))) {
			
			/***************** Actions ****************/
			if (accion == null || StringPool.BLANK.equals(accion)) {
				viewProjects(req, resp);
			}
			
			/************** Actions AJAX **************/
			else if (JX_SCENARIO_ANALYSIS_CHART.equals(accion)) { scenarioAnalysisChartJX(req, resp); }
			else if (JX_EFFICIENT_FRONTIER_CHART.equals(accion)) { efficientFrontierChartJX(req, resp); }
			
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE + "?accion=403", req, resp);
		}
	}

	/**
	 * Efficient frontier chart
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void efficientFrontierChartJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idProgram 					= ParamUtil.getInteger(req, Program.IDPROGRAM, null);
		String idsSelectedAndForcedInString = ParamUtil.getString(req, "idsSelectedAndForcedIn", StringPool.BLANK);
		
		PrintWriter out = resp.getWriter();
		
		try {
			Integer[] idsSelectedAndForcedIn 	= null;
			JSONObject updateJSON 				= new JSONObject();
			ProgramLogic programLogic 			= new ProgramLogic();
			ProjectLogic projectLogic			= new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			if (!StringPool.BLANK.equals(idsSelectedAndForcedInString)) {
				idsSelectedAndForcedIn = StringUtil.splitStrToIntegers(idsSelectedAndForcedInString, null);
			}
			
			if (idProgram != null) {
				
				List<String> investmentStatus 	= new ArrayList<String>();
				JSONArray efficientFrontierJSON = new JSONArray();
				JSONArray selectionJSON 		= new JSONArray();

				Line lineSelection 				= null;
				
				Program program = programLogic.findById(idProgram);

				investmentStatus.add(Constants.INVESTMENT_APPROVED);
				investmentStatus.add(Constants.INVESTMENT_IN_PROCESS);
				
				List<Project> projects = projectLogic.findByProgramAndInvestmentStatus(program, investmentStatus);
				
				// Efficient Frontier
				for (double numberOfProjects = 0.0 ; numberOfProjects <= projects.size(); numberOfProjects++) {
					
					// SIMPLEX
					// max cx : Ax <= b, x >= 0
					double[] c 		= new double[projects.size()];
					double[] b		= new double[projects.size()+1];
					double[][] A 	= new double[projects.size()+1][projects.size()];
					
					// Restricciones binarias
					for (int i = 0; i < projects.size(); i++) {
						
						Project project = projects.get(i);
						
						if (new Double(0.0).equals(project.getTcv())) {
							c[i] = project.getPriority().doubleValue()/100.0;
						}
						else {
							c[i] = project.getPriority().doubleValue()/project.getTcv();
						}
						
						b[i] = 1.0;
						
						for (int j = 0; j < projects.size(); j++) {
							
							if (i == j) {
								A[i][j] = 1.0; 
							}
							else {
								A[i][j] = 0.0;
							}
						}
					}
					
					// Numero de proyectos a elegir
					b[projects.size()] = numberOfProjects;
					
					// Restriccion del numero de proyectos 1*x1 + 1*x2 + 1*x3 +... = b[0] 
					for (int j = 0; j < projects.size(); j++) {
						A[projects.size()][j] = 1.0;
					}
					
					// Solucion al problema
					double[] solution = new double[projects.size()];
					solution = Simplex.solution(A, b, c);
					
					// Calculamos el valor del presupuesto y prioridad para los resultados dados
					JSONArray xyJSON 	= new JSONArray();
					double budget 		= 0.0;
					double priority 	= 0.0;
					
					for (int i = 0; i < solution.length; i++) {
						
						if (solution[i] == 1.0) {
							
							Project project = projects.get(i);
							
							budget 		+= project.getTcv() == null ? 0.0 : project.getTcv()/1000.0;
							priority 	+= project.getPriority() == null ? 0.0 : project.getPriority().doubleValue();
						}
					}
					
					xyJSON.add(budget);
					xyJSON.add(priority);
					
					efficientFrontierJSON.add(xyJSON);
				}
				
				// Selection
				JSONArray xyJSON 	= new JSONArray();
				double budget		= 0.0;
				double priority 	= 0.0;
				
				for (Integer id : idsSelectedAndForcedIn) {
					
					Project project = projectLogic.findById(id);
					
					budget 		+= project.getTcv() == null ? 0.0 : project.getTcv()/1000.0;
					priority 	+= project.getPriority() == null ? 0.0 : project.getPriority().doubleValue();
				}
				
				xyJSON.add(budget);
				xyJSON.add(priority);
				
				selectionJSON.add(xyJSON);
				
				
				updateJSON.put("efficientFrontier", efficientFrontierJSON);
				updateJSON.put("selection", selectionJSON);
				
				// Efficient Frontier intersaction horizontal Selection
				lineSelection = new Line(efficientFrontierJSON.getJSONArray(0).getDouble(0) - 1, selectionJSON.getJSONArray(0).getDouble(1), efficientFrontierJSON.getJSONArray(efficientFrontierJSON.size() - 1).getDouble(0) + 1, selectionJSON.getJSONArray(0).getDouble(1));
				
				for (int i = 0; i < efficientFrontierJSON.size() - 1; i++) {
					
					JSONArray p1JSON = efficientFrontierJSON.getJSONArray(i);
					JSONArray p2JSON = efficientFrontierJSON.getJSONArray(i + 1);
					
					Line lineFrontier = new Line(p1JSON.getDouble(0), p1JSON.getDouble(1), p2JSON.getDouble(0), p2JSON.getDouble(1));
					
					if (lineFrontier.intersectsLine(lineSelection)) {
						
						Point2D pIntersection = lineFrontier.lineIntersection(lineSelection);
						
						// Non Coincident lines
						if (pIntersection != null) {
							
							JSONArray EFXAxeJSON = new JSONArray();
							
							EFXAxeJSON.add(pIntersection.getX());
							EFXAxeJSON.add(pIntersection.getY());
							
							updateJSON.put("EFXAxe", EFXAxeJSON);
						}
						
						break;
					}
				}
				
				// Efficient Frontier intersaction vertical Selection
				lineSelection = new Line(selectionJSON.getJSONArray(0).getDouble(0), efficientFrontierJSON.getJSONArray(efficientFrontierJSON.size() - 1).getDouble(1) + 1, selectionJSON.getJSONArray(0).getDouble(0), -1.0);
				
				for (int i = 0; i < efficientFrontierJSON.size() - 1; i++) {
					
					JSONArray p1JSON = efficientFrontierJSON.getJSONArray(i);
					JSONArray p2JSON = efficientFrontierJSON.getJSONArray(i + 1);
					
					Line lineFrontier = new Line(p1JSON.getDouble(0), p1JSON.getDouble(1), p2JSON.getDouble(0), p2JSON.getDouble(1));
					
					if (lineFrontier.intersectsLine(lineSelection)) {
						
						Point2D pIntersection = lineFrontier.lineIntersection(lineSelection);
						
						// Non Coincident lines
						if (pIntersection != null) {
							
							JSONArray EFYAxeJSON = new JSONArray();
							
							EFYAxeJSON.add(pIntersection.getX());
							EFYAxeJSON.add(pIntersection.getY());
							
							updateJSON.put("EFYAxe", EFYAxeJSON);
						}
						
						break;
					}
				}
			}
			
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}
	

	/**
	 * Scenario analysis chart
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void scenarioAnalysisChartJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idProgram = ParamUtil.getInteger(req, Program.IDPROGRAM, null);
		
		PrintWriter out = resp.getWriter();
		
		try {
			
			JSONObject updateJSON 	= new JSONObject();
			JSONArray projectsJSON 	= new JSONArray();
			
			if (idProgram != null) {
				
				ProgramLogic programLogic = new ProgramLogic();
				ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
				
				Program program = programLogic.findById(idProgram);
				
				List<String> investmentStatus = new ArrayList<String>();
				
				investmentStatus.add(Constants.INVESTMENT_APPROVED);
				investmentStatus.add(Constants.INVESTMENT_IN_PROCESS);
				
				List<Project> projects = projectLogic.findByProgramAndInvestmentStatus(program, investmentStatus);
				
				if (!projects.isEmpty()) {
					
					projectsJSON = ChartLogic.createBubbleChart(getResourceBundle(req), projects, Project.INVESTMENTSTATUS, Project.TCV, false, null);
					
					// Solution for correct padding
					projectsJSON.add(JSONModelUtil.createBubbleProject(50, 50, 0, "", "#ffffff", null, null, null));
					
					updateJSON.put("projects", projectsJSON);
				}
			}
			
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
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
				
				req.setAttribute("program", program);
				req.setAttribute("title", program.getProgramName());
			}
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		forward("/index.jsp?nextForm=program/scenario_analysis", req, resp);
	}
   
}