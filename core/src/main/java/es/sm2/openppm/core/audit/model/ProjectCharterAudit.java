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
 * File: ProjectCharterSync.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:24:00
 */

package es.sm2.openppm.core.audit.model;

import es.sm2.openppm.core.model.impl.Projectcharter;

public class ProjectCharterAudit {

    private String businessNeed;
    private String projectObjectives;
    private String sucessCriteria;
    private String mainConstraints;
    private String milestones;
    private String mainAssumptions;
    private String mainRisks;
    private String exclusions;
    private String mainDeliverables;
    
    public ProjectCharterAudit(Projectcharter projectcharter) {
    	
    	setBusinessNeed(projectcharter.getBusinessNeed());
    	setProjectObjectives(projectcharter.getProjectObjectives());
    	setSucessCriteria(projectcharter.getSucessCriteria());
    	setMainConstraints(projectcharter.getMainConstraints());
    	setMilestones(projectcharter.getMilestones());
    	setMainAssumptions(projectcharter.getMainAssumptions());
    	setMainRisks(projectcharter.getMainRisks());
    	setExclusions(projectcharter.getExclusions());
    	setMainDeliverables(projectcharter.getMainDeliverables());
    }
    
	/**
	 * @return the businessNeed
	 */
	public String getBusinessNeed() {
		return businessNeed;
	}
	/**
	 * @param businessNeed the businessNeed to set
	 */
	public void setBusinessNeed(String businessNeed) {
		this.businessNeed = businessNeed;
	}
	/**
	 * @return the projectObjectives
	 */
	public String getProjectObjectives() {
		return projectObjectives;
	}
	/**
	 * @param projectObjectives the projectObjectives to set
	 */
	public void setProjectObjectives(String projectObjectives) {
		this.projectObjectives = projectObjectives;
	}
	/**
	 * @return the sucessCriteria
	 */
	public String getSucessCriteria() {
		return sucessCriteria;
	}
	/**
	 * @param sucessCriteria the sucessCriteria to set
	 */
	public void setSucessCriteria(String sucessCriteria) {
		this.sucessCriteria = sucessCriteria;
	}
	/**
	 * @return the mainConstraints
	 */
	public String getMainConstraints() {
		return mainConstraints;
	}
	/**
	 * @param mainConstraints the mainConstraints to set
	 */
	public void setMainConstraints(String mainConstraints) {
		this.mainConstraints = mainConstraints;
	}
	/**
	 * @return the milestones
	 */
	public String getMilestones() {
		return milestones;
	}
	/**
	 * @param milestones the milestones to set
	 */
	public void setMilestones(String milestones) {
		this.milestones = milestones;
	}
	/**
	 * @return the mainAssumptions
	 */
	public String getMainAssumptions() {
		return mainAssumptions;
	}
	/**
	 * @param mainAssumptions the mainAssumptions to set
	 */
	public void setMainAssumptions(String mainAssumptions) {
		this.mainAssumptions = mainAssumptions;
	}
	/**
	 * @return the mainRisks
	 */
	public String getMainRisks() {
		return mainRisks;
	}
	/**
	 * @param mainRisks the mainRisks to set
	 */
	public void setMainRisks(String mainRisks) {
		this.mainRisks = mainRisks;
	}
	/**
	 * @return the exclusions
	 */
	public String getExclusions() {
		return exclusions;
	}
	/**
	 * @param exclusions the exclusions to set
	 */
	public void setExclusions(String exclusions) {
		this.exclusions = exclusions;
	}
	/**
	 * @return the mainDeliverables
	 */
	public String getMainDeliverables() {
		return mainDeliverables;
	}
	/**
	 * @param mainDeliverables the mainDeliverables to set
	 */
	public void setMainDeliverables(String mainDeliverables) {
		this.mainDeliverables = mainDeliverables;
	}
}
