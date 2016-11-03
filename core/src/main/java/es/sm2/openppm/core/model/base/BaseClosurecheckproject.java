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
 * File: BaseClosurecheckproject.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Closurecheck;
import es.sm2.openppm.core.model.impl.Closurecheckproject;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Closurecheckproject
 * @see es.sm2.openppm.core.model.base.Closurecheckproject
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Closurecheckproject
 */
public class BaseClosurecheckproject  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "closurecheckproject";
	
	public static final String IDCLOSURECHECKPROJECT = "idClosureCheckProject";
	public static final String CLOSURECHECK = "closurecheck";
	public static final String DOCUMENTPROJECT = "documentproject";
	public static final String PROJECT = "project";
	public static final String COMMENTS = "comments";
	public static final String REALIZED = "realized";
	public static final String MANAGER = "manager";
	public static final String DEPARTAMENT = "departament";
	public static final String DATEREALIZED = "dateRealized";

     private Integer idClosureCheckProject;
     private Closurecheck closurecheck;
     private Documentproject documentproject;
     private Project project;
     private String comments;
     private boolean realized;
     private String manager;
     private String departament;
     private Date dateRealized;

    public BaseClosurecheckproject() {
    }
    
    public BaseClosurecheckproject(Integer idClosureCheckProject) {
    	this.idClosureCheckProject = idClosureCheckProject;
    }
   
    public Integer getIdClosureCheckProject() {
        return this.idClosureCheckProject;
    }
    
    public void setIdClosureCheckProject(Integer idClosureCheckProject) {
        this.idClosureCheckProject = idClosureCheckProject;
    }
    public Closurecheck getClosurecheck() {
        return this.closurecheck;
    }
    
    public void setClosurecheck(Closurecheck closurecheck) {
        this.closurecheck = closurecheck;
    }
    public Documentproject getDocumentproject() {
        return this.documentproject;
    }
    
    public void setDocumentproject(Documentproject documentproject) {
        this.documentproject = documentproject;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    public boolean isRealized() {
        return this.realized;
    }
    
    public void setRealized(boolean realized) {
        this.realized = realized;
    }
    public String getManager() {
        return this.manager;
    }
    
    public void setManager(String manager) {
        this.manager = manager;
    }
    public String getDepartament() {
        return this.departament;
    }
    
    public void setDepartament(String departament) {
        this.departament = departament;
    }
    public Date getDateRealized() {
        return this.dateRealized;
    }
    
    public void setDateRealized(Date dateRealized) {
        this.dateRealized = dateRealized;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Closurecheckproject) )  { result = false; }
		 else if (other != null) {
		 	Closurecheckproject castOther = (Closurecheckproject) other;
			if (castOther.getIdClosureCheckProject().equals(this.getIdClosureCheckProject())) { result = true; }
         }
		 return result;
   }


}


