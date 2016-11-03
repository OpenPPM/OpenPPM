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
 * File: ExceptionUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.params.ParamUtil;


public class ExceptionUtil {

	private ExceptionUtil() {}
	
	/**
	 * Evalua la excepcion para AJAX 
	 * 
	 * @param out
	 * @param req
	 * @param idioma
	 * @param LOGGER
	 * @param e
	 */
	public static void evalueExceptionJX(PrintWriter out, HttpServletRequest req, ResourceBundle idioma, Logger LOGGER, Exception e) {
		
		if (e instanceof LogicException) {
			JSONObject errorJSON = new JSONObject();
			errorJSON.put(StringPool.ERROR, ((LogicException)e).getMessage(idioma));
			out.print(errorJSON);
		}
		else {
			sendToLogger(LOGGER, e, req);
			JSONObject errorJSON = new JSONObject();
			errorJSON.put(StringPool.ERROR, e.getMessage());
			out.print(errorJSON);
		}
	}
	
	/**
	 * Ajax exception evaluate and paint in to the idError
	 * 
	 * @param out
	 * @param req
	 * @param idioma
	 * @param LOGGER
	 * @param e
	 * @param idError
	 */
	public static void evalueExceptionJX(PrintWriter out, HttpServletRequest req, ResourceBundle idioma, Logger LOGGER, Exception e, String idError) {
		
		if (e instanceof LogicException) {
			JSONObject errorJSON = new JSONObject();
			errorJSON.put(StringPool.ERROR, ((LogicException)e).getMessage(idioma));
			errorJSON.put("idError", idError);
			out.print(errorJSON);
		}
		else {
			sendToLogger(LOGGER, e, req);
			JSONObject errorJSON = new JSONObject();
			errorJSON.put(StringPool.ERROR, e.getMessage());
			out.print(errorJSON);
		}
	}
	
	/**
	 * Evalua la excepcion
	 * @param req
	 * @param idioma
	 * @param LOGGER
	 * @param e
	 */
	public static void evalueException(HttpServletRequest req, ResourceBundle idioma, Logger LOGGER, Exception e) {
		
		if (e instanceof LogicException) {
			LOGGER.info(StringPool.ERROR, e);
			req.setAttribute(StringPool.ERROR, ((LogicException)e).getMessage(idioma));
		}
		else {
			sendToLogger(LOGGER, e, req);
			req.setAttribute(StringPool.ERROR, e.getMessage());
		}
	}
	
	/**
	 * Send error to logger
	 * @param idioma 
	 * @param out 
	 * @param req
	 * @param idioma
	 * @param LOGGER
	 * @param e
	 */
	public static void sendToLogger(Logger LOGGER, Exception e, HttpServletRequest req) {
		
		StringBuffer msg = new StringBuffer();
		
		if (req != null) {
			Employee user	 = SecurityUtil.consUser(req);
			
			msg.append("Error\n");
			if (user != null) {
				if (user.getContact() != null) {
					msg.append("\nUsuario: "+user.getContact().getFullName()+" ID:"+user.getIdEmployee());
				}
				if (user.getResourceprofiles() != null) {
					msg.append("\nRol: "+user.getResourceprofiles().getProfileName());
				}
				if (user.getPerformingorg() != null) {
					msg.append("\nPO: "+user.getPerformingorg().getName()+" ID: "+user.getPerformingorg().getIdPerfOrg());
				}
			}
			msg.append("\nAccion: "+ParamUtil.getString(req, "accion", ""));
			
			Integer idProject = ParamUtil.getInteger(req, "id", null);
			if (idProject == null) { idProject = ParamUtil.getInteger(req, Project.IDPROJECT, null); }
			if (idProject != null) { msg.append("\nidProject: "+idProject); }
			
		}
		
		msg.append("\n");
		
		LOGGER.error(msg.toString(), e);
	}
	
	public static void sendToLogger(Logger LOGGER, String e, Project proj) {
		
		StringBuffer msg = new StringBuffer();
		
		if (proj != null) {
			msg.append("Error\n");
			
			Integer idProject = proj.getIdProject();
			msg.append("\nidProject: "+idProject);
			msg.append("\nProject: "+proj.getProjectName());
		}
		
		msg.append("\n");
		
		LOGGER.error(msg.toString(), new Exception(e));
	}
}
