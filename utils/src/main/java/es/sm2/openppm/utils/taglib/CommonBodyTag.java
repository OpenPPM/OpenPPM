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
 * Module: utils
 * File: CommonBodyTag.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.taglib;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class CommonBodyTag extends BodyTagSupport {
	
	private static final long serialVersionUID = 1L;
	
	private ServletContext _servletContext;
	private String _page;
	private String _endPage;
	
	@Override
	public int doStartTag() throws JspException {
		String page = getPage();

		if (page == null || page == "") {
			return EVAL_BODY_BUFFERED;
		}
		
		setAttributes(getServletRequest());
		_doInclude(page);

		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		int returnValue;
		if (getEndPage() == null) {
			returnValue = super.doEndTag();
		}
		else {
			
			setEndAttributes(getServletRequest());
			_doInclude(getEndPage());
			
			returnValue = EVAL_BODY_INCLUDE;
		}
		
		return returnValue;
	}
	
	protected void setAttributes(HttpServletRequest request) {
	}
	
	protected void setEndAttributes(HttpServletRequest request) {
	}

	protected String getPage() {
		return _page;
	}
	
	protected String getEndPage() {
		return _endPage;
	}
	
	public ServletContext getServletContext() {
		if (_servletContext != null) {
			return _servletContext;
		}

		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

		ServletContext servletContext = (ServletContext)request.getAttribute("CTX");

		if (servletContext == null) {
			servletContext = pageContext.getServletContext();
		}

		return servletContext;
	}
	

	public HttpServletRequest getServletRequest() {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		return request;
	}
	
	
	private void _doInclude(String page) throws JspException {
		
		try {
			
			ServletContext servletContext = getServletContext();
			HttpServletRequest request = getServletRequest();

			RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(page);
			
			requestDispatcher.include(request, new ResponseWrapper(pageContext));
			
		}
		catch (Exception e) {
			//TODO Logger
			e.printStackTrace();

			if (e instanceof JspException) {
				throw (JspException)e;
			}
		}
	}

}
