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
 * File: BaseProblemcheckproject.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Problemcheck;
import es.sm2.openppm.core.model.impl.Problemcheckproject;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Problemcheckproject
 * @see es.sm2.openppm.core.model.base.Problemcheckproject
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Problemcheckproject
 */
public class BaseProblemcheckproject  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "problemcheckproject";
	
	public static final String IDPROBLEMCHECKPROJECT = "idProblemCheckProject";
	public static final String PROBLEMCHECK = "problemcheck";
	public static final String PROJECT = "project";

     private Integer idProblemCheckProject;
     private Problemcheck problemcheck;
     private Project project;

    public BaseProblemcheckproject() {
    }
    
    public BaseProblemcheckproject(Integer idProblemCheckProject) {
    	this.idProblemCheckProject = idProblemCheckProject;
    }
   
    public Integer getIdProblemCheckProject() {
        return this.idProblemCheckProject;
    }
    
    public void setIdProblemCheckProject(Integer idProblemCheckProject) {
        this.idProblemCheckProject = idProblemCheckProject;
    }
    public Problemcheck getProblemcheck() {
        return this.problemcheck;
    }
    
    public void setProblemcheck(Problemcheck problemcheck) {
        this.problemcheck = problemcheck;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Problemcheckproject) )  { result = false; }
		 else if (other != null) {
		 	Problemcheckproject castOther = (Problemcheckproject) other;
			if (castOther.getIdProblemCheckProject().equals(this.getIdProblemCheckProject())) { result = true; }
         }
		 return result;
   }


}


