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
 * Module: plugin-project-synchronize
 * File: AssumptionlogSync.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 13:25:36
 */

package es.sm2.openppm.plugin.synchronize.beans;

import es.sm2.openppm.core.model.impl.Assumptionreassessmentlog;

import java.util.Date;

public class AssumptionlogSync {

	private Date assumptionDate;
	private String assumptionChange;

	public AssumptionlogSync(Assumptionreassessmentlog assumptionlog) {
		
		setAssumptionDate(assumptionlog.getAssumptionDate());
		setAssumptionChange(assumptionlog.getAssumptionChange());
	}

	public Date getAssumptionDate() {
		return this.assumptionDate;
	}

	public void setAssumptionDate(Date assumptionDate) {
		this.assumptionDate = assumptionDate;
	}

	public String getAssumptionChange() {
		return this.assumptionChange;
	}

	public void setAssumptionChange(String assumptionChange) {
		this.assumptionChange = assumptionChange;
	}
}
