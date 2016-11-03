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
 * File: ChecklistSync.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:24:00
 */

package es.sm2.openppm.core.audit.model;

import es.sm2.openppm.core.model.impl.Checklist;

import java.util.Date;

public class ChecklistAudit {

     private String code;
     private String description;
     private Integer percentageComplete;
     private Date actualizationDate;
     private String name;
     private String comments;

    public ChecklistAudit(Checklist checklist) {
    	
    	setCode(checklist.getCode());
    	setDescription(checklist.getDescription());
    	setPercentageComplete(checklist.getPercentageComplete());
    	setActualizationDate(checklist.getActualizationDate());
    	setName(checklist.getName());
    	setComments(checklist.getComments());
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getPercentageComplete() {
        return this.percentageComplete;
    }
    
    public void setPercentageComplete(Integer percentageComplete) {
        this.percentageComplete = percentageComplete;
    }
    public Date getActualizationDate() {
        return this.actualizationDate;
    }
    
    public void setActualizationDate(Date actualizationDate) {
        this.actualizationDate = actualizationDate;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
}


