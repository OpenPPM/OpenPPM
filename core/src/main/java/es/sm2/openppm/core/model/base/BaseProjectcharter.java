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
 * File: BaseProjectcharter.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Projectcharter
 * @see es.sm2.openppm.core.model.base.Projectcharter
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Projectcharter
 */
public class BaseProjectcharter  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "projectcharter";
	
	public static final String IDPROJECTCHARTER = "idProjectCharter";
	public static final String PROJECT = "project";
	public static final String BUSINESSNEED = "businessNeed";
	public static final String PROJECTOBJECTIVES = "projectObjectives";
	public static final String SUCESSCRITERIA = "sucessCriteria";
	public static final String MAINCONSTRAINTS = "mainConstraints";
	public static final String MILESTONES = "milestones";
	public static final String MAINASSUMPTIONS = "mainAssumptions";
	public static final String MAINRISKS = "mainRisks";
	public static final String EXCLUSIONS = "exclusions";
	public static final String MAINDELIVERABLES = "mainDeliverables";

     private Integer idProjectCharter;
     private Project project;
     private String businessNeed;
     private String projectObjectives;
     private String sucessCriteria;
     private String mainConstraints;
     private String milestones;
     private String mainAssumptions;
     private String mainRisks;
     private String exclusions;
     private String mainDeliverables;

    public BaseProjectcharter() {
    }
    
    public BaseProjectcharter(Integer idProjectCharter) {
    	this.idProjectCharter = idProjectCharter;
    }
   
    public Integer getIdProjectCharter() {
        return this.idProjectCharter;
    }
    
    public void setIdProjectCharter(Integer idProjectCharter) {
        this.idProjectCharter = idProjectCharter;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public String getBusinessNeed() {
        return this.businessNeed;
    }
    
    public void setBusinessNeed(String businessNeed) {
        this.businessNeed = businessNeed;
    }
    public String getProjectObjectives() {
        return this.projectObjectives;
    }
    
    public void setProjectObjectives(String projectObjectives) {
        this.projectObjectives = projectObjectives;
    }
    public String getSucessCriteria() {
        return this.sucessCriteria;
    }
    
    public void setSucessCriteria(String sucessCriteria) {
        this.sucessCriteria = sucessCriteria;
    }
    public String getMainConstraints() {
        return this.mainConstraints;
    }
    
    public void setMainConstraints(String mainConstraints) {
        this.mainConstraints = mainConstraints;
    }
    public String getMilestones() {
        return this.milestones;
    }
    
    public void setMilestones(String milestones) {
        this.milestones = milestones;
    }
    public String getMainAssumptions() {
        return this.mainAssumptions;
    }
    
    public void setMainAssumptions(String mainAssumptions) {
        this.mainAssumptions = mainAssumptions;
    }
    public String getMainRisks() {
        return this.mainRisks;
    }
    
    public void setMainRisks(String mainRisks) {
        this.mainRisks = mainRisks;
    }
    public String getExclusions() {
        return this.exclusions;
    }
    
    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }
    public String getMainDeliverables() {
        return this.mainDeliverables;
    }
    
    public void setMainDeliverables(String mainDeliverables) {
        this.mainDeliverables = mainDeliverables;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Projectcharter) )  { result = false; }
		 else if (other != null) {
		 	Projectcharter castOther = (Projectcharter) other;
			if (castOther.getIdProjectCharter().equals(this.getIdProjectCharter())) { result = true; }
         }
		 return result;
   }


}


