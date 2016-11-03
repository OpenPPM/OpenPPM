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
 * File: BaseMilestones.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Milestonecategory;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Milestones
 * @see es.sm2.openppm.core.model.base.Milestones
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Milestones
 */
public class BaseMilestones  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "milestones";
	
	public static final String IDMILESTONE = "idMilestone";
	public static final String PROJECTACTIVITY = "projectactivity";
	public static final String MILESTONECATEGORY = "milestonecategory";
	public static final String MILESTONETYPE = "milestonetype";
	public static final String PROJECT = "project";
	public static final String DESCRIPTION = "description";
	public static final String LABEL = "label";
	public static final String REPORTTYPE = "reportType";
	public static final String PLANNED = "planned";
	public static final String ACHIEVED = "achieved";
	public static final String ACHIEVEDCOMMENTS = "achievedComments";
	public static final String NOTIFY = "notify";
	public static final String NOTIFYDAYS = "notifyDays";
	public static final String NOTIFICATIONTEXT = "notificationText";
	public static final String NOTIFYDATE = "notifyDate";
	public static final String NAME = "name";
	public static final String ESTIMATEDDATE = "estimatedDate";

     private Integer idMilestone;
     private Projectactivity projectactivity;
     private Milestonecategory milestonecategory;
     private Milestonetype milestonetype;
     private Project project;
     private String description;
     private String label;
     private Character reportType;
     private Date planned;
     private Date achieved;
     private String achievedComments;
     private Boolean notify;
     private Integer notifyDays;
     private String notificationText;
     private Date notifyDate;
     private String name;
     private Date estimatedDate;

    public BaseMilestones() {
    }
    
    public BaseMilestones(Integer idMilestone) {
    	this.idMilestone = idMilestone;
    }
   
    public Integer getIdMilestone() {
        return this.idMilestone;
    }
    
    public void setIdMilestone(Integer idMilestone) {
        this.idMilestone = idMilestone;
    }
    public Projectactivity getProjectactivity() {
        return this.projectactivity;
    }
    
    public void setProjectactivity(Projectactivity projectactivity) {
        this.projectactivity = projectactivity;
    }
    public Milestonecategory getMilestonecategory() {
        return this.milestonecategory;
    }
    
    public void setMilestonecategory(Milestonecategory milestonecategory) {
        this.milestonecategory = milestonecategory;
    }
    public Milestonetype getMilestonetype() {
        return this.milestonetype;
    }
    
    public void setMilestonetype(Milestonetype milestonetype) {
        this.milestonetype = milestonetype;
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
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    public Character getReportType() {
        return this.reportType;
    }
    
    public void setReportType(Character reportType) {
        this.reportType = reportType;
    }
    public Date getPlanned() {
        return this.planned;
    }
    
    public void setPlanned(Date planned) {
        this.planned = planned;
    }
    public Date getAchieved() {
        return this.achieved;
    }
    
    public void setAchieved(Date achieved) {
        this.achieved = achieved;
    }
    public String getAchievedComments() {
        return this.achievedComments;
    }
    
    public void setAchievedComments(String achievedComments) {
        this.achievedComments = achievedComments;
    }
    public Boolean getNotify() {
        return this.notify;
    }
    
    public void setNotify(Boolean notify) {
        this.notify = notify;
    }
    public Integer getNotifyDays() {
        return this.notifyDays;
    }
    
    public void setNotifyDays(Integer notifyDays) {
        this.notifyDays = notifyDays;
    }
    public String getNotificationText() {
        return this.notificationText;
    }
    
    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }
    public Date getNotifyDate() {
        return this.notifyDate;
    }
    
    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Date getEstimatedDate() {
        return this.estimatedDate;
    }
    
    public void setEstimatedDate(Date estimatedDate) {
        this.estimatedDate = estimatedDate;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Milestones) )  { result = false; }
		 else if (other != null) {
		 	Milestones castOther = (Milestones) other;
			if (castOther.getIdMilestone().equals(this.getIdMilestone())) { result = true; }
         }
		 return result;
   }


}


