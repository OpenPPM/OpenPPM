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
 * File: IssuelogSync.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:24:00
 */

package es.sm2.openppm.core.audit.model;

import es.sm2.openppm.core.model.impl.Issuelog;

import java.util.Date;

public class IssuelogAudit {

	private String description;
	private Character priority;
	private Date dateLogged;
	private String originator;
	private String assignedTo;
	private Date targetDate;
	private String resolution;
	private Date dateClosed;
	private String issueDoc;

	public IssuelogAudit(Issuelog issuelog) {
		
		setDescription(issuelog.getDescription());
		setPriority(issuelog.getPriority());
		setDateLogged(issuelog.getDateLogged());
		setOriginator(issuelog.getOriginator());
		setAssignedTo(issuelog.getAssignedTo());
		setTargetDate(issuelog.getTargetDate());
		setResolution(issuelog.getResolution());
		setDateClosed(issuelog.getDateClosed());
		setIssueDoc(issuelog.getIssueDoc());
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Character getPriority() {
		return this.priority;
	}

	public void setPriority(Character priority) {
		this.priority = priority;
	}

	public Date getDateLogged() {
		return this.dateLogged;
	}

	public void setDateLogged(Date dateLogged) {
		this.dateLogged = dateLogged;
	}

	public String getOriginator() {
		return this.originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public String getAssignedTo() {
		return this.assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Date getTargetDate() {
		return this.targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public String getResolution() {
		return this.resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Date getDateClosed() {
		return this.dateClosed;
	}

	public void setDateClosed(Date dateClosed) {
		this.dateClosed = dateClosed;
	}

	public String getIssueDoc() {
		return this.issueDoc;
	}

	public void setIssueDoc(String issueDoc) {
		this.issueDoc = issueDoc;
	}
}
