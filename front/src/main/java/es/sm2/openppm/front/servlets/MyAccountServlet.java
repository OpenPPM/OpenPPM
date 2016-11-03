
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
 * File: MyAccountServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.logic.impl.ContactLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.SecurityLogic;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;

/**
 * Servlet implementation class UtilServlet
 */
public class MyAccountServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(MyAccountServlet.class);
	
	public final static String REFERENCE = "myaccount";
	
	/***************** Actions ****************/
	public final static String UPDATE_MY_ACCOUNT = "update-my-account";
	
	/************** Actions AJAX **************/
	public final static String JX_GENERATE_TOKEN = "ajax-generate-token";

    
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1) {
			
			/***************** Actions ****************/
			if (ValidateUtil.isNull(accion)) { viewMyAccount(req, resp, null); }
			else if (UPDATE_MY_ACCOUNT.equals(accion)) { updateMyAccount(req, resp); }
			
			/************** Actions AJAX **************/
			else if (JX_GENERATE_TOKEN.equals(accion)) { generateTokenJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}
	
    /**
     * Generate token for employee
     * @param req
     * @param resp
     * @throws IOException
     */
    private void generateTokenJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
    	
		int idEmployee = ParamUtil.getInteger(req, Employee.IDEMPLOYEE,-1);
		
    	try {
    		
    		EmployeeLogic employeeLogic = new EmployeeLogic();
    		
    		String token = employeeLogic.addToken(new Employee(idEmployee));
    		
    		out.print(JsonUtil.toJSON("token", token));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
     * Update My Account
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
	private void updateMyAccount(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		String fullName 		= ParamUtil.getString(req, Contact.FULLNAME);
		String jobTitle 		= ParamUtil.getString(req, Contact.JOBTITLE);
		String fileAs 			= ParamUtil.getString(req, Contact.FILEAS);
		String businessAddress	= ParamUtil.getString(req, Contact.BUSINESSADDRESS);
		String businessPhone 	= ParamUtil.getString(req, Contact.BUSINESSPHONE);
		String mobilePhone		= ParamUtil.getString(req, Contact.MOBILEPHONE);
		String email			= ParamUtil.getString(req, Contact.EMAIL);
		String notes			= ParamUtil.getString(req, Contact.NOTES);
		String password			= ParamUtil.getString(req, "password", null);
		String passwordNew		= ParamUtil.getString(req, "passwordNew");
		String passwordConfirm	= ParamUtil.getString(req, "passwordConfirm");
		String locale 			= ParamUtil.getString(req, "locale", null);
		
		Security security = null;
		
		try {
			ContactLogic contactLogic 	= new ContactLogic();
	    	EmployeeLogic employeeLogic = new EmployeeLogic();
	    	SecurityLogic securityLogic = new SecurityLogic();
	    	
			Contact contact = contactLogic.consultContact(getUser(req).getContact().getIdContact());
			
			if (!ValidateUtil.isNull(password)) {
				security = securityLogic.getByContact(getUser(req).getContact());
				
				if (!security.getPassword().equals(SecurityUtil.md5(password))) {					
					throw new LogicException("msg.error.pass_incorrect");
				}
				else if (!ValidateUtil.isNull(passwordNew) && !passwordNew.equals(passwordConfirm)) {
					throw new LogicException("msg.error.pass_not_match");
				}
				else {					 
					security.setPassword(SecurityUtil.md5(passwordNew));
					securityLogic.updateSecurity(security);
					infoUpdated(req, "pass");
				}
			}
			String oldLocale = contact.getLocale();
			
			contact.setFullName(fullName);
			contact.setJobTitle(jobTitle);
			contact.setFileAs(fileAs);
			contact.setBusinessAddress(businessAddress);
			contact.setBusinessPhone(businessPhone);
			contact.setMobilePhone(mobilePhone);
			contact.setEmail(email);
			contact.setNotes(notes);
			contact.setLocale(locale);
			
			contactLogic.save(contact);
			
			Employee user = employeeLogic.consEmployee(getUser(req).getIdEmployee());
			req.getSession().setAttribute("user", user);
			req.getSession().setAttribute("rolPrincipal", user.getResourceprofiles().getIdProfile());
			
			if (oldLocale != null && !oldLocale.equals(locale)) { setLocale(req); }
			
			infoUpdated(req, "contact");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewMyAccount(req, resp, security);
	}


	/**
	 * View My Account
	 * @param req
	 * @param resp
	 * @param security 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewMyAccount(HttpServletRequest req, HttpServletResponse resp, Security security) throws ServletException, IOException {
		
		List<Employee> employees	= null;
		
		try {
			SecurityLogic securityLogic = new SecurityLogic();
			EmployeeLogic employeeLogic = new EmployeeLogic();
			
			if (security == null) {
				security = securityLogic.getByContact(getUser(req).getContact());
			}
			
			employees = employeeLogic.findForToken(getUser(req).getContact());
			
			req.setAttribute("username", security.getLogin());
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("employees", employees);
		
		req.setAttribute("title", getResourceBundle(req).getString("my_account"));
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		forward("/index.jsp?nextForm=myaccount/my_account", req, resp);
	}
}
