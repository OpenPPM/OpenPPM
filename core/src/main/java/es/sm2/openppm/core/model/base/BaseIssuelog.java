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
 * File: BaseIssuelog.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Issuelog;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Issuelog
 * @see es.sm2.openppm.core.model.base.Issuelog
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Issuelog
 */
public class BaseIssuelog  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "issuelog";
	
	public static final String IDISSUE = "idIssue";
	public static final String PROJECT = "project";
	public static final String DESCRIPTION = "description";
	public static final String PRIORITY = "priority";
	public static final String DATELOGGED = "dateLogged";
	public static final String ORIGINATOR = "originator";
	public static final String ASSIGNEDTO = "assignedTo";
	public static final String TARGETDATE = "targetDate";
	public static final String RESOLUTION = "resolution";
	public static final String DATECLOSED = "dateClosed";
	public static final String ISSUEDOC = "issueDoc";
    public static final String OWNER = "owner";

     private Integer idIssue;
     private Project project;
     private String description;
     private Character priority;
     private Date dateLogged;
     private String originator;
     private String assignedTo;
     private Date targetDate;
     private String resolution;
     private Date dateClosed;
     private String issueDoc;
     private String owner;

    public BaseIssuelog() {
    }
    
    public BaseIssuelog(Integer idIssue) {
    	this.idIssue = idIssue;
    }
   
    public Integer getIdIssue() {
        return this.idIssue;
    }
    
    public void setIdIssue(Integer idIssue) {
        this.idIssue = idIssue;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
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

    public String getOwner() {
         return this.owner;
     }
    public void setOwner(String owner) {
         this.owner = owner;
     }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Issuelog) )  { result = false; }
		 else if (other != null) {
		 	Issuelog castOther = (Issuelog) other;
			if (castOther.getIdIssue().equals(this.getIdIssue())) { result = true; }
         }
		 return result;
   }


}


