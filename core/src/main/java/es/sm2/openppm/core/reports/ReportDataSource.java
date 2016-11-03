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
 * File: ReportDataSource.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.reports;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import es.sm2.openppm.core.utils.EntityUtil;

public class ReportDataSource<T> implements JRDataSource, JRRewindableDataSource {

	private List<T> items;
	private int index;
	private EntityUtil entityUtil;
	
	public ReportDataSource(List<T> items) {
		
		this.items = items;
		this.index = -1;
		this.entityUtil = new EntityUtil();
	}
	
	@Override
	public boolean next() throws JRException {
	
		return (items != null && !items.isEmpty()  && ++index< items.size());
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		
		return entityUtil.getFieldValue(items.get(index), jrField.getName());
	}

	@Override
	public void moveFirst() throws JRException {
	}
}
