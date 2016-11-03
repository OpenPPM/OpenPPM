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
 * File: StakeholderSync.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 13:25:36
 */

package es.sm2.openppm.plugin.synchronize.beans;
import es.sm2.openppm.core.model.impl.Stakeholder;

public class StakeholderSync {

     private String projectRole;
     private String requirements;
     private String expectations;
     private String influence;
     private String mgtStrategy;
     private String classification;
     private Character type;
     private String contactName;
     private String department;
     private Integer orderToShow;
     private String comments;

    public StakeholderSync(Stakeholder stakeholder) {
    	
    	setProjectRole(stakeholder.getProjectRole());
    	setRequirements(stakeholder.getRequirements());
    	setExpectations(stakeholder.getExpectations());
    	setInfluence(stakeholder.getInfluence());
    	setMgtStrategy(stakeholder.getMgtStrategy());
    	setType(stakeholder.getType());
    	setDepartment(stakeholder.getDepartment());
    	setOrderToShow(stakeholder.getOrderToShow());
    	setComments(stakeholder.getComments());

        // Set stakeholder classification if exists
        if (stakeholder.getStakeholderclassification() != null) {
            setClassification(stakeholder.getStakeholderclassification().getName());
        }

    	if (stakeholder.getEmployee() == null) {
    		setContactName(stakeholder.getContactName());
    	}
    	else {
    		setContactName(stakeholder.getEmployee().getContact().getFullName());
    	}
    }
    
    public String getProjectRole() {
        return this.projectRole;
    }
    
    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }
    public String getRequirements() {
        return this.requirements;
    }
    
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    public String getExpectations() {
        return this.expectations;
    }
    
    public void setExpectations(String expectations) {
        this.expectations = expectations;
    }
    public String getInfluence() {
        return this.influence;
    }
    
    public void setInfluence(String influence) {
        this.influence = influence;
    }
    public String getMgtStrategy() {
        return this.mgtStrategy;
    }
    
    public void setMgtStrategy(String mgtStrategy) {
        this.mgtStrategy = mgtStrategy;
    }
    public String getClassification() {
        return this.classification;
    }
    
    public void setClassification(String classification) {
        this.classification = classification;
    }
    public Character getType() {
        return this.type;
    }
    
    public void setType(Character type) {
        this.type = type;
    }
    public String getContactName() {
        return this.contactName;
    }
    
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getDepartment() {
        return this.department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    public Integer getOrderToShow() {
        return this.orderToShow;
    }
    
    public void setOrderToShow(Integer orderToShow) {
        this.orderToShow = orderToShow;
    }
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
}


