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
 * File: AssumptionSync.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:24:00
 */

package es.sm2.openppm.core.audit.model;

import es.sm2.openppm.core.model.impl.Assumptionreassessmentlog;
import es.sm2.openppm.core.model.impl.Assumptionregister;

import java.util.ArrayList;
import java.util.List;

public class AssumptionAudit {

	private String description;
	private String assumptionCode;
	private String assumptionName;
	private String originator;
	private String assumptionDoc;
	private List<AssumptionlogAudit> assumptionlogs = new ArrayList<AssumptionlogAudit>();

	public AssumptionAudit(Assumptionregister assumption) {
		
		setDescription(assumption.getDescription());
		setAssumptionCode(assumption.getAssumptionCode());
		setAssumptionName(assumption.getAssumptionName());
		setOriginator(assumption.getOriginator());
		setAssumptionDoc(assumption.getAssumptionDoc());
		
		// SET ASSUMPTION LOG
		if (assumption.getAssumptionreassessmentlogs() != null && !assumption.getAssumptionreassessmentlogs().isEmpty()) {
			
			for (Assumptionreassessmentlog assumptionlog : assumption.getAssumptionreassessmentlogs()) {
				
				getAssumptionlogs().add(new AssumptionlogAudit(assumptionlog));
			}
		}
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssumptionCode() {
		return this.assumptionCode;
	}

	public void setAssumptionCode(String assumptionCode) {
		this.assumptionCode = assumptionCode;
	}

	public String getAssumptionName() {
		return this.assumptionName;
	}

	public void setAssumptionName(String assumptionName) {
		this.assumptionName = assumptionName;
	}

	public String getOriginator() {
		return this.originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public String getAssumptionDoc() {
		return this.assumptionDoc;
	}

	public void setAssumptionDoc(String assumptionDoc) {
		this.assumptionDoc = assumptionDoc;
	}

	/**
	 * @return the assumptionlogs
	 */
	public List<AssumptionlogAudit> getAssumptionlogs() {
		return assumptionlogs;
	}

	/**
	 * @param assumptionlogs the assumptionlogs to set
	 */
	public void setAssumptionlogs(List<AssumptionlogAudit> assumptionlogs) {
		this.assumptionlogs = assumptionlogs;
	}
}
