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
 * File: SettingsTag.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

/**
 * @autor Javier Hernández
 */

package es.sm2.openppm.front.tags.visual;

import javax.servlet.http.HttpServletRequest;

import es.sm2.openppm.core.common.GenericSetting;
import es.sm2.openppm.utils.taglib.CommonBodyTag;

public class SettingsTag extends CommonBodyTag {
	
	private static final long serialVersionUID = 7988422677092457610L;
	
	private static final String _PAGE		= "/jsp/taglib/settings/startTag.jsp";
	private static final String _ENDPAGE	= "/jsp/taglib/settings/endTag.jsp";
	
	private Integer startColumn;
	private Integer numberOfColumns;
	private GenericSetting[] fields;
	
	@Override
	protected String getPage() {
		return _PAGE;
	}
	
	@Override
	protected String getEndPage() {
		return _ENDPAGE;
	}

	/**
	 * @return the startColumn
	 */
	public Integer getStartColumn() {
		return startColumn;
	}

	/**
	 * @param startColumn the startColumn to set
	 */
	public void setStartColumn(Integer startColumn) {
		this.startColumn = startColumn;
	}

	/**
	 * @return the numberOfColumns
	 */
	public Integer getNumberOfColumns() {
		return numberOfColumns;
	}

	/**
	 * @param numberOfColumns the numberOfColumns to set
	 */
	public void setNumberOfColumns(Integer numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	/**
	 * @return the fields
	 */
	public GenericSetting[] getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(GenericSetting[] fields) {
		this.fields = fields;
	}
	
	@Override
	protected void setAttributes(HttpServletRequest request) {
		
		if (getStartColumn() == null) { setStartColumn(1); }
		if (getNumberOfColumns() == null) { setNumberOfColumns(3); }
		if (getFields() == null) { setFields(new GenericSetting[0]); }
		
		request.setAttribute("startColumn", getStartColumn());
		request.setAttribute("numberOfColumns", getNumberOfColumns());
		request.setAttribute("fields", getFields());
	}
}
