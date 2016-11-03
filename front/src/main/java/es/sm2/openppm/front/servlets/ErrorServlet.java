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
 * File: ErrorServlet.java
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.utils.params.ParamUtil;

public class ErrorServlet extends AbstractGenericServlet {

	private static final long serialVersionUID = 1L;
	
	private static final  Logger LOGGER = Logger.getLogger(ErrorServlet.class);
	
	public static final String REFERENCE = "error";
	
	/***************** Actions ****************/
	public static final String ERROR_403 = "403";
	public static final String ERROR_404 = "404";
	public static final String ERROR_408 = "408";
	public static final String ERROR_400 = "400";
	public static final String ERROR_UNEXPECTED = "UNEXPECTED";
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.service(req, resp);
		
		String accion = ParamUtil.getString(req, "accion",ERROR_403);
		LOGGER.debug("Accion: " + accion);
		
		/***************** Actions ****************/
		if (ERROR_403.equals(accion)) { notPermission(req, resp); }
		else if (ERROR_404.equals(accion)) { notFound(req, resp); }
		else if (ERROR_408.equals(accion)) { timeOut(req, resp); }
		else if (ERROR_400.equals(accion)) { badRequest(req, resp); }
		else if (ERROR_UNEXPECTED.equals(accion)) { unexpectedError(req, resp); }
	}
	
	/**
	 * Send message error
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void unexpectedError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String error = ParamUtil.getString(req, "error", getResourceBundle(req).getString("msg.error.unexpected"));
		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("error", error);
		forward("/index.jsp", req, resp);
	}

	/**
	 * Error HTTP 400 Bad request
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void badRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("error", getResourceBundle(req).getString("msg.error.400"));
		forward("/index.jsp", req, resp);
	}
	
	/**
	 * Error HTTP 408 Request timeout
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void timeOut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("error", getResourceBundle(req).getString("msg.error.408"));
		forward("/index.jsp", req, resp);
	}

	/**
	 * Error HTTP 404 Not found
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void notFound(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("error", getResourceBundle(req).getString("msg.error.404"));
		forward("/index.jsp", req, resp);
	}

	/**
	 * Error HTTP 403 Forbidden
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void notPermission(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("error", getResourceBundle(req).getString("msg.error.without_permission"));
		forward("/index.jsp", req, resp);
	}
}
