
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
 * File: HomeServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.RequestUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.params.ParamUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Servlet implementation class TimeSheetServlet
 */
public class HomeServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(HomeServlet.class);

	public final static String REFERENCE = "home";
	
	/***************** Actions ****************/
	public final static String CHOOSE_ROL	= "choose-rol";
	public final static String SELECT_ROL	= "select-rol";
	
	/************** Actions AJAX **************/
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");
		LOGGER.debug("Accion: " + accion);
		
		if (CHOOSE_ROL.equals(accion)) {
			
			chooseRol(req, resp);
			accion = StringPool.BLANK;
		}
		else if (SELECT_ROL.equals(accion)) {
			
			SecurityUtil.unsetUserRole(req);
			
			accion = "";
			setRolSession(req, resp);
			
			if (SecurityUtil.consUserRole(req) != -1) {
				info(StringPool.INFORMATION, req, "msg.info.one-profile");
			}
		}
		
		if (SecurityUtil.consUserRole(req) != -1) {
			
			/************** Actions AJAX **************/
			if (accion == null || StringPool.BLANK.equals(accion)) { viewInitPage(req, resp); }
			
			/************** Actions AJAX **************/
			
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * View init page
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
	private void viewInitPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		if (SecurityUtil.isUserInRole (req, Constants.ROLE_PMO)) {
			
			req.setAttribute("url", ProjectServlet.REFERENCE);
			forward("/redirect.jsp", req, resp);
		}
		else if (SecurityUtil.isUserInRole (req, Constants.ROLE_PORFM)) {
			
			req.setAttribute("url", ProjectServlet.REFERENCE+"?accion="+ProjectServlet.LIST_INVESTMENTS);
			forward("/redirect.jsp", req, resp);
		}
		else if (SecurityUtil.isUserInRole (req, Constants.ROLE_FM)) {

			req.setAttribute("url", ProjectServlet.REFERENCE+"?accion="+ProjectServlet.LIST_INVESTMENTS);
			forward("/redirect.jsp", req, resp);
		}
		else if (SecurityUtil.isUserInRole (req, Constants.ROLE_SPONSOR)) {

			req.setAttribute("url", ProjectServlet.REFERENCE);
			forward("/redirect.jsp", req, resp);
		}
		else if (SecurityUtil.isUserInRole (req, Constants.ROLE_IM)) {

			req.setAttribute("url", ProjectServlet.REFERENCE+"?accion="+ProjectServlet.LIST_INVESTMENTS);
			forward("/redirect.jsp", req, resp);
		}
		else if (SecurityUtil.isUserInRole (req, Constants.ROLE_PROGM)) {

			req.setAttribute("url", ProjectServlet.REFERENCE+"?accion="+ProjectServlet.LIST_INVESTMENTS);
			forward("/redirect.jsp", req, resp);
		}
		else if (SecurityUtil.isUserInRole (req, Constants.ROLE_PM)) {

			req.setAttribute("url", ProjectServlet.REFERENCE);
			forward("/redirect.jsp", req, resp);
		}
		else if (SecurityUtil.isUserInRole (req, Constants.ROLE_RM)) {
			
			req.setAttribute("url", ResourceServlet.REFERENCE);
			forward("/redirect.jsp", req, resp);
		}
		else if (SecurityUtil.isUserInRole (req, Constants.ROLE_RESOURCE)) {

			req.setAttribute("url", AssignmentServlet.REFERENCE);
			forward("/redirect.jsp", req, resp);
		}
		else if (SecurityUtil.isUserInRole (req, Constants.ROLE_ADMIN)) {

			req.setAttribute("url", AdministrationServlet.REFERENCE);
			forward("/redirect.jsp", req, resp);
		}
		else if (SecurityUtil.isUserInRole (req, Constants.ROLE_STAKEHOLDER)) {

			req.setAttribute("url", ProjectServlet.REFERENCE);
			forward("/redirect.jsp", req, resp);
		}
        else if (SecurityUtil.isUserInRole (req, Constants.ROLE_LOGISTIC)) {

            req.setAttribute("url", ProjectServlet.REFERENCE);
            forward("/redirect.jsp", req, resp);
        }
		else {
			
			req.setAttribute("url", ErrorServlet.REFERENCE);
			forward("/redirect.jsp", req, resp);
		}
	}

	private void chooseRol(HttpServletRequest req, HttpServletResponse resp) {
		
    	int idEmployee = ParamUtil.getInteger(req, Configurations.CR_EMPLOYEE,-1);
    	
    	try {
        	EmployeeLogic employeeLogic = new EmployeeLogic();
        	
    		Employee user = employeeLogic.consEmployee(idEmployee);
			req.getSession().setAttribute("user", user);
			req.getSession().setAttribute("rolPrincipal", user.getResourceprofiles().getIdProfile());
			
			// Find configurations
			HashMap<String, String> configurations = RequestUtil.getConfigurationValues(
					req,
					Configurations.CR_PO,
					Configurations.CR_EMPLOYEE
				);
			
			// Save configuration
			ConfigurationLogic configurationLogic = new ConfigurationLogic();
			configurationLogic.saveConfigurations(getUser(req), configurations, Configurations.TYPE_CHOOSE_ROLE);
			
    	}
    	catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	}
}
