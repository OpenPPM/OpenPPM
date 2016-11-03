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
 * File: LoginServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
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
* Fecha : Miercoles, 20 de Julio, 2011
*
* -------------------------------------------------------------------------
*/

package es.sm2.openppm.front.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.SecurityLogic;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.params.ParamUtil;

public class LoginServlet extends AbstractGenericServlet {

	private static final long serialVersionUID = 1L;
	
	private static final  Logger LOGGER = Logger.getLogger(LoginServlet.class);
	
	public static final String REFERENCE = "login";
	
	/***************** Actions ****************/
	public static final String ERROR_LOGIN = "error-login";
	public static final String LOGOFF = Settings.DEFAULT_LOGOFF_URL_ACTION;
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.service(req, resp);
		
		String accion = ParamUtil.getString(req, "a");
		LOGGER.debug("Accion: " + accion);
		
		/***************** Actions ****************/
		if (accion == null || StringPool.BLANK.equals(accion)) {
			
			if (isAjaxCall(req)) {
				PrintWriter out = resp.getWriter();
				try {
					JSONObject errorJSON = new JSONObject();
					errorJSON.put(StringPool.ERROR, getResourceBundle(req).getString("msg.error.session_expired")+"<a href='javascript:location.reload();'>"+getResourceBundle(req).getString("sign_in")+"</a>");
					out.print(errorJSON);
				}
				catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
				finally { out.close(); }
			}
			else {
				forward("/login.jsp", req, resp);
			}
		}
		else if (ERROR_LOGIN.equals(accion)) { viewLoginError(req, resp); }
		else if (LOGOFF.equals(accion)) { forward("/logoff.jsp", req, resp); }
	}
	
	private void viewLoginError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			SecurityLogic securityLogic = new SecurityLogic();
			Security sec 				= new Security();
			
			sec.setLogin(req.getParameter("j_username"));
			
			securityLogic.preLogin(sec);
			
			req.setAttribute(StringPool.ERROR, StringPool.TRUE);
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		forward("/login.jsp", req, resp);
	}
}
