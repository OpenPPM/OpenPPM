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
 * Module: core
 * File: PluginResponse.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.plugin;

public class PluginResponse {
	
	private String urlServlet;
	private String urlJSP;
	private String information;
	private String informationType;
	private Object response;
	
	/**
	 * @return the urlServlet
	 */
	public String getUrlServlet() {
		return urlServlet;
	}
	/**
	 * @param urlServlet the urlServlet to set
	 */
	public void setUrlServlet(String urlServlet) {
		this.urlServlet = urlServlet;
	}
	/**
	 * @return the urlJSP
	 */
	public String getUrlJSP() {
		return urlJSP;
	}
	/**
	 * @param urlJSP the urlJSP to set
	 */
	public void setUrlJSP(String urlJSP) {
		this.urlJSP = urlJSP;
	}
	/**
	 * @return the information
	 */
	public String getInformation() {
		return information;
	}
	/**
	 * @param information the information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}
	/**
	 * @return the response
	 */
	public Object getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(Object response) {
		this.response = response;
	}
	/**
	 * @return the informationType
	 */
	public String getInformationType() {
		return informationType;
	}
	/**
	 * @param informationType the informationType to set
	 */
	public void setInformationType(String informationType) {
		this.informationType = informationType;
	}

}
