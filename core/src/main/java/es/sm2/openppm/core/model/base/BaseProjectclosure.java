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
 * File: BaseProjectclosure.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectclosure;


/**
 * Base Pojo object for domain model class Projectclosure
 * @see es.sm2.openppm.core.model.base.Projectclosure
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Projectclosure
 */
public class BaseProjectclosure  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "projectclosure";
	
	public static final String IDPROJECTCLOUSE = "idProjectClouse";
	public static final String PROJECT = "project";
	public static final String PROJECTRESULTS = "projectResults";
	public static final String GOALACHIEVEMENT = "goalAchievement";
	public static final String LESSONSLEARNED = "lessonsLearned";

     private Integer idProjectClouse;
     private Project project;
     private String projectResults;
     private String goalAchievement;
     private String lessonsLearned;

    public BaseProjectclosure() {
    }
    
    public BaseProjectclosure(Integer idProjectClouse) {
    	this.idProjectClouse = idProjectClouse;
    }
   
    public Integer getIdProjectClouse() {
        return this.idProjectClouse;
    }
    
    public void setIdProjectClouse(Integer idProjectClouse) {
        this.idProjectClouse = idProjectClouse;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public String getProjectResults() {
        return this.projectResults;
    }
    
    public void setProjectResults(String projectResults) {
        this.projectResults = projectResults;
    }
    public String getGoalAchievement() {
        return this.goalAchievement;
    }
    
    public void setGoalAchievement(String goalAchievement) {
        this.goalAchievement = goalAchievement;
    }
    public String getLessonsLearned() {
        return this.lessonsLearned;
    }
    
    public void setLessonsLearned(String lessonsLearned) {
        this.lessonsLearned = lessonsLearned;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Projectclosure) )  { result = false; }
		 else if (other != null) {
		 	Projectclosure castOther = (Projectclosure) other;
			if (castOther.getIdProjectClouse().equals(this.getIdProjectClouse())) { result = true; }
         }
		 return result;
   }


}


