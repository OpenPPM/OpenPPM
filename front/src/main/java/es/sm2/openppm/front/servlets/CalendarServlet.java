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
 * File: CalendarServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.CalendarBaseLogic;
import es.sm2.openppm.core.logic.impl.CalendarbaseexceptionsLogic;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Calendarbaseexceptions;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Projectcalendar;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.json.Exclusion;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;


/**
 * Servlet implementation class CalendarServlet
 */
public class CalendarServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger LOGGER = Logger.getLogger(CalendarServlet.class);
	
	public final static String REFERENCE = "calendar";
	
	/***************** Actions ****************/
	public final static String DEL_CALENDAR_BASE = "delete-calendar-base";
	public final static String CREATE_CALENDAR				= "create-calendar";
	
	/************** Actions AJAX **************/
	public final static String JX_SAVE_CALENDAR				= "ajax-save-calendar";
	public final static String JX_GET_CALENDAR				= "ajax-get-calendar";
	public final static String JX_SAVE_CALENDAR_EXCEPTION	= "ajax-save-calendar-exception";
	public final static String JX_DELETE_CALENDAR_EXCEPTION = "ajax-delete-calendar-exception";
    
    /**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)) {
			
			/***************** Actions ****************/
			if (accion == null || "".equals(accion)) { viewCalendar(req, resp); }
			else if (DEL_CALENDAR_BASE.equals(accion)) { deleteCalendarBase(req, resp); }
			else if (CREATE_CALENDAR.equals(accion)) { createCalendar(req, resp); }
			
			/************** Actions AJAX **************/
			else if (JX_SAVE_CALENDAR.equals(accion)) { saveCalendarJX(req, resp); }
			else if (JX_GET_CALENDAR.equals(accion)) { getCalendarJX(req, resp); }
			else if (JX_SAVE_CALENDAR_EXCEPTION.equals(accion)) { saveCalendarExceptionJX(req, resp); }
			else if (JX_DELETE_CALENDAR_EXCEPTION.equals(accion)) { deleteCalendarExceptionJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
    }
	
	/**
	 * Delete Calendar Base
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteCalendarBase(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		Integer idCalendarBase = ParamUtil.getInteger(req, "idCalendarBase", -1);
		
		try {
			CalendarBaseLogic calendarBaseLogic = new CalendarBaseLogic();
			calendarBaseLogic.delete(new Calendarbase(idCalendarBase));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewCalendar(req, resp);
	}
	
	
	/**
	 * Delete Calendar Exception
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteCalendarExceptionJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter out			= resp.getWriter();		
		
		Integer idCalendarBaseException = ParamUtil.getInteger(req, "idCalendarBaseException",-1);
		
		try {	
			CalendarbaseexceptionsLogic calendarbaseexceptionsLogic = new CalendarbaseexceptionsLogic();
			
			calendarbaseexceptionsLogic.delete(new Calendarbaseexceptions(idCalendarBaseException));

			JSONObject sucessJSON = new JSONObject();
			sucessJSON.put("sucess", "true");
			
			out.print(sucessJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}	
	}

	/**
	 * Save Calendar Base Exception
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveCalendarExceptionJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		Integer idCalendarBase 			= ParamUtil.getInteger(req, "idCalendarBase", -1);
		Integer idCalendarBaseException	= ParamUtil.getInteger(req, "idCalendarBaseException", -1);
		Date startDate 					= ParamUtil.getDate(req, "startDate", getDateFormat(req));
		Date finishDate					= ParamUtil.getDate(req, "endDate", getDateFormat(req));
		String description				= ParamUtil.getString(req, "name");
		
		PrintWriter out			= resp.getWriter();		
		
		try {	
	    	Calendarbaseexceptions exception = null;
	    	
	    	if (idCalendarBase != -1) {
	    		exception = new Calendarbaseexceptions();
	    		
	    		if (idCalendarBaseException != -1){	exception.setIdCalendarBaseException(idCalendarBaseException);}
	    		
	    		exception.setStartDate(startDate);
    			exception.setFinishDate(finishDate);
    			exception.setDescription(description);	    		
    			exception.setCalendarbase(new Calendarbase(idCalendarBase));
	    	}
			
			if (exception != null) {
				CalendarbaseexceptionsLogic calendarbaseexceptionsLogic = new CalendarbaseexceptionsLogic();
				exception = calendarbaseexceptionsLogic.save(exception);
			}
			
			out.print(JsonUtil.toJSON(exception));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}	
	}

	/**
	 * Get calendar
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void getCalendarJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter out			= resp.getWriter();		
		Integer idCalendarBase 	= ParamUtil.getInteger(req, "idCalendarBase",-1);
		
		try {	
			CalendarBaseLogic calendarBaseLogic = new CalendarBaseLogic();
			Calendarbase calendarBase = calendarBaseLogic.consCalendarBaseWithExceptions(new Calendarbase(idCalendarBase));
			
			String pattern = DateUtil.getDatePattern(getResourceBundle(req));
			
			calendarBase.setEmployees(null);
			calendarBase.setProjectcalendars(null);
			calendarBase.setCompany(getCompany(req));
			
			JSONObject calendarJSON = JSONObject.fromObject(JsonUtil.toJSON(calendarBase, pattern));
			
			JSONArray exceptions = new JSONArray();
			
			for (Calendarbaseexceptions item : calendarBase.getCalendarbaseexceptionses()) {
				item.setCalendarbase(null);
				
				exceptions.add(JsonUtil.toJSON(item, pattern));
			}
			
			calendarJSON.put("calendarbaseexceptionses", exceptions);
			
			out.print(calendarJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Save calendar
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveCalendarJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Integer idCalendarBase 	= ParamUtil.getInteger(req, "idCalendarBase",-1);
		Integer idTemplate	 	= ParamUtil.getInteger(req, "idTemplate",-1);
		String name 			= ParamUtil.getString(req, "name");
		Integer weekStart 		= ParamUtil.getInteger(req, "weekStart",1);
		Integer fiscalYearStart = ParamUtil.getInteger(req, "fiscalYearStart",1);
		double startTime1 		= ParamUtil.getDouble(req, "startTime1");
		double startTime2 		= ParamUtil.getDouble(req, "startTime2");
		double endTime1 		= ParamUtil.getDouble(req, "endTime1");
		double endTime2 		= ParamUtil.getDouble(req, "endTime2");
		double hoursDay			= ParamUtil.getDouble(req, "hoursDay");
		double hoursWeek 		= ParamUtil.getDouble(req, "hoursWeek");
		Integer daysMonth 		= ParamUtil.getInteger(req, "daysMonth");
		
		PrintWriter out = resp.getWriter();		
		
		try {	
			CalendarBaseLogic calendarBaseLogic = new CalendarBaseLogic();
			CalendarbaseexceptionsLogic calendarbaseexceptionsLogic = new CalendarbaseexceptionsLogic();
			Calendarbase calendarBase = new Calendarbase();
			calendarBase.setCompany(getCompany(req));
			
			if (idCalendarBase == -1) {
				
				if (idTemplate == -1) {
					calendarBase.setName(name);
					calendarBase.setCompany(getCompany(req));
					
					calendarBase = calendarBaseLogic.save(calendarBase);
				}
				else {
					calendarBase = calendarBaseLogic.consCalendarBase(new Calendarbase(idTemplate));
					calendarBase.setIdCalendarBase(null);
					calendarBase.setName(name);
					
					Set<Calendarbaseexceptions> exceptions = calendarBase.getCalendarbaseexceptionses();
					
					calendarBase = calendarBaseLogic.save(calendarBase);
					
					for (Calendarbaseexceptions item :exceptions) {
						
						item.setIdCalendarBaseException(null);
						item.setCalendarbase(calendarBase);
						
						calendarbaseexceptionsLogic.save(item);
					}
				}
				
			}
			else {
				calendarBase.setIdCalendarBase(idCalendarBase);
				calendarBase.setWeekStart((weekStart == -1 ? null : weekStart));
				calendarBase.setFiscalYearStart((fiscalYearStart == -1 ? null : fiscalYearStart));
				calendarBase.setDaysMonth((daysMonth == -1 ? null : daysMonth));
				calendarBase.setStartTime1((startTime1 == -1 ? null : startTime1));
				calendarBase.setStartTime2((startTime2 == -1 ? null : startTime2));
				calendarBase.setEndTime1((endTime1 == -1 ? null : endTime1));
				calendarBase.setEndTime2((endTime2 == -1 ? null : endTime2));
				calendarBase.setHoursDay((hoursDay == -1 ? null : hoursDay));
				calendarBase.setHoursWeek((hoursWeek == -1 ? null : hoursWeek));
				
				if (name != null) {
					calendarBase.setName(name);
				}				
				calendarBase = calendarBaseLogic.save(calendarBase);
				
				for (Calendarbaseexceptions item : calendarBase.getCalendarbaseexceptionses()) {
					item.setCalendarbase(null);
				}
			}

			out.print(JsonUtil.toJSON(calendarBase, new Exclusion(Company.class), new Exclusion(Projectcalendar.class)));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void createCalendar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Integer idTemplate	 	= ParamUtil.getInteger(req, "template_calendar",-1);
		String name 			= ParamUtil.getString(req, "new_calendar_name");
		
		Calendarbase calendarBase = null;

		try {
			CalendarBaseLogic calendarBaseLogic = new CalendarBaseLogic();
			CalendarbaseexceptionsLogic calendarbaseexceptionsLogic = new CalendarbaseexceptionsLogic();
			if (idTemplate == -1) {
				calendarBase = new Calendarbase();
				calendarBase.setName(name);
				calendarBase.setCompany(getCompany(req));
				
				calendarBase = calendarBaseLogic.save(calendarBase);
			}
			else {
				calendarBase = calendarBaseLogic.consCalendarBase(new Calendarbase(idTemplate));
				calendarBase.setIdCalendarBase(null);
				calendarBase.setName(name);
				calendarBase.setCompany(getCompany(req));
				
				Set<Calendarbaseexceptions> exceptions = calendarBase.getCalendarbaseexceptionses();
				
				calendarBase = calendarBaseLogic.save(calendarBase);
				
				for (Calendarbaseexceptions item :exceptions) {
					
					item.setIdCalendarBaseException(null);
					item.setCalendarbase(calendarBase);
					
					calendarbaseexceptionsLogic.save(item);
				}
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("calendarId", calendarBase.getIdCalendarBase());
		req.setAttribute("calendarName", calendarBase.getName());		
		
		viewCalendar(req, resp);
	
	}
	
	/**
     * View Calendar
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void viewCalendar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    	Integer idCalendar = ParamUtil.getInteger(req, "calendarId", null);
    	
		List<Calendarbase> listCalendars = null;
		
		try {
			CalendarBaseLogic baseLogic = new CalendarBaseLogic();
			listCalendars = baseLogic.findByRelation(Calendarbase.COMPANY, getCompany(req));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("title", getResourceBundle(req).getString("calendar.base"));
		req.setAttribute("listCalendars", listCalendars);
		
		if (idCalendar != null) { req.setAttribute("calendarId", idCalendar); }
		
		forward("/index.jsp?nextForm=calendar/calendar", req, resp);	
	}
}