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
 * File: MilestonSync.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:24:00
 */

package es.sm2.openppm.core.audit.model;

import es.sm2.openppm.core.model.impl.Milestones;

import java.util.Date;

public class MilestonAudit {

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

    public MilestonAudit(Milestones mileston) {
    	
    	setDescription(mileston.getDescription());
    	setLabel(mileston.getLabel());
    	setReportType(mileston.getReportType());
    	setPlanned(mileston.getPlanned());
    	setAchieved(mileston.getAchieved());
    	setAchievedComments(mileston.getAchievedComments());
    	setNotify(mileston.getNotify());
    	setNotifyDays(mileston.getNotifyDays());
    	setNotificationText(mileston.getNotificationText());
    	setNotifyDate(mileston.getNotifyDate());
    	setName(mileston.getName());
    	setEstimatedDate(mileston.getEstimatedDate());
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

	/**
	 * @return the estimatedDate
	 */
	public Date getEstimatedDate() {
		return estimatedDate;
	}

	/**
	 * @param estimatedDate the estimatedDate to set
	 */
	public void setEstimatedDate(Date estimatedDate) {
		this.estimatedDate = estimatedDate;
	}
}


