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
 * File: PanelTag.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

/**
 * @autor Javier Hernández
 */

package es.sm2.openppm.front.tags.visual;

import javax.servlet.http.HttpServletRequest;

import es.sm2.openppm.utils.taglib.CommonBodyTag;

public class PanelTag extends CommonBodyTag {
	
	private static final long serialVersionUID = 7988422677092457610L;
	
	private static final String _PAGE		= "/jsp/taglib/panel/startTag.jsp";
	private static final String _ENDPAGE	= "/jsp/taglib/panel/endTag.jsp";
	
	private String id;
	private String cssClass;
	private String title;
	private String callback;
	private String buttons;
	private String showTiltePanel;
	
	@Override
	protected String getPage() {
		return _PAGE;
	}
	
	@Override
	protected String getEndPage() {
		return _ENDPAGE;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getCallback() {
		return callback;
	}

	public void setButtons(String buttons) {
		this.buttons = buttons;
	}

	public String getButtons() {
		return buttons;
	}
	
	public void setShowTiltePanel(String showTiltePanel) {
		this.showTiltePanel = showTiltePanel;
	}

	public String getShowTiltePanel() {
		return showTiltePanel;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		
		if (getCssClass() == null) { setCssClass(""); }
		if (getButtons() == null) { setButtons(""); }
		if (getShowTiltePanel() == null) { setShowTiltePanel("true"); }
		
		request.setAttribute("id", getId());
		request.setAttribute("cssClass", getCssClass());
		request.setAttribute("title", getTitle());
		request.setAttribute("callback", getCallback());
		request.setAttribute("buttons", getButtons());
		request.setAttribute("showTiltePanel", "true".equals(getShowTiltePanel()));
	}
}
