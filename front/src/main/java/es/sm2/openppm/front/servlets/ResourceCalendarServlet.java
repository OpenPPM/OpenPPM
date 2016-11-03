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
 * File: ResourceCalendarServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.EmployeeoperationdateLogic;
import es.sm2.openppm.core.logic.impl.OperationLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Employeeoperationdate;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;

/**
 * Servlet implementation class ResourceManagerServlet
 */
public class ResourceCalendarServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(ResourceCalendarServlet.class);
	
	public final static String REFERENCE = "resourcecalendar";
	
	/***************** Actions ****************/
	
	/************** Actions AJAX **************/
	public final static String JX_SAVE_DATES = "ajax-save-dates";
	
    
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
			if (ValidateUtil.isNull(accion) || REFERENCE.equals(accion)) { viewResourceCalendar(req, resp); }
			
			/************** Actions AJAX **************/
			else if (JX_SAVE_DATES.equals(accion)) { saveDatesJX(req, resp); }
			
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Save dates for employee in one operation
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveDatesJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
    	Calendar yearCal = DateUtil.getCalendar();
    	
    	int idEmployee	= ParamUtil.getInteger(req, Employee.IDEMPLOYEE,-1);
    	int idOperation = ParamUtil.getInteger(req, Operation.IDOPERATION,-1);
    	String[] dates	= ParamUtil.getStringValues(req, "dates[]", null);
    	int year		= ParamUtil.getInteger(req, "year", yearCal.get(Calendar.YEAR));
    	
    	Calendar since	= DateUtil.getFirstYearDay(year);
    	since.add(Calendar.MONTH, -1);
    	Calendar until	= DateUtil.getLastYearDay(year);
    	until.add(Calendar.MONTH, 1);
		
		PrintWriter out = resp.getWriter();

    	try {
    		EmployeeoperationdateLogic empOpeDateLogic = new EmployeeoperationdateLogic();
    		
    		List<Employeeoperationdate> emplDates = empOpeDateLogic.findForCalendar(new Employee(idEmployee), new Operation(idOperation), since.getTime(), until.getTime());
    		
    		if (!emplDates.isEmpty() && dates == null) {
    			
    			LOGGER.debug("Delete dates");
    			empOpeDateLogic.deleteDates(new Employee(idEmployee), new Operation(idOperation), emplDates);
    		}
    		else if (!emplDates.isEmpty()) {
    			
    			LOGGER.debug("Update dates");
                empOpeDateLogic.updateDates(new Employee(idEmployee), new Operation(idOperation), emplDates, dates,
                        getDateFormat(req), getSettings(req), getResourceBundle(req));
    		}
    		else if (dates != null) {
    			
    			LOGGER.debug("Create dates");
    			empOpeDateLogic.createDates(new Employee(idEmployee), new Operation(idOperation), dates, getDateFormat(req));
    		}
			
    		out.print(infoUpdated(getResourceBundle(req), new JSONObject(), "dates"));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * View Resource Calendar Operations
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
	private void viewResourceCalendar(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		Calendar yearCal = DateUtil.getCalendar();
		
		int idEmployee		= ParamUtil.getInteger(req, Employee.IDEMPLOYEE,-1);
		Integer idOperation = ParamUtil.getInteger(req, Operation.IDOPERATION,null);
		int year			= ParamUtil.getInteger(req, "year", yearCal.get(Calendar.YEAR));
    	int showMonths		= ParamUtil.getInteger(req, "showMonths",4);
		
    	Calendar since	= DateUtil.getFirstYearDay(year);
    	since.add(Calendar.MONTH, -1);
    	Calendar until	= DateUtil.getLastYearDay(year);
    	until.add(Calendar.MONTH, 1);
		
		Employee employee			= null;
		List<Operation> operations	= null;
		List<Employeeoperationdate> dates = null;
		List<Integer> years			= new ArrayList<Integer>();
		
		try {
			EmployeeLogic employeeLogic		= new EmployeeLogic();
			OperationLogic operationLogic	= new OperationLogic();
			EmployeeoperationdateLogic empOpeDateLogic = new EmployeeoperationdateLogic();
			
			List<String> joins = new ArrayList<String>();
			joins.add(Employee.CONTACT);
			
			employee = employeeLogic.findById(idEmployee, joins);
			
			operations = operationLogic.findForRM(getUser(req));
			
			if (idOperation == null && !operations.isEmpty()) {
				idOperation = operations.get(0).getIdOperation();
			}
			
			if (idOperation != null) {
				
				dates = empOpeDateLogic.findForCalendar(employee, new Operation(idOperation), since.getTime(), until.getTime());
			}
			
			Integer tempYear = yearCal.get(Calendar.YEAR) - 4;
			for (int i = tempYear; i < (tempYear+6);i++) { years.add(i); }
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("employee", employee);
		req.setAttribute("operations", operations);
		req.setAttribute("idOperation", idOperation);
		req.setAttribute("dates", dates);
		req.setAttribute("since", since.getTime());
		req.setAttribute("until", until.getTime());
		req.setAttribute("year", year);
		req.setAttribute("years", years);
		req.setAttribute("showMonths", showMonths);
		
		req.setAttribute("title", getResourceBundle(req).getString("menu.resource_calendar"));
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		
		forward("/index.jsp?nextForm=resource/calendar/calendar", req, resp);
	}

}
