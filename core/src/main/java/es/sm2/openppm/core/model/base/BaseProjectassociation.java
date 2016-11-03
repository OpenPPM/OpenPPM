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
 * File: BaseProjectassociation.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectassociation;


/**
 * Base Pojo object for domain model class Projectassociation
 * @see es.sm2.openppm.core.model.base.Projectassociation
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Projectassociation
 */
public class BaseProjectassociation  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "projectassociation";
	
	public static final String IDPROJECTASSOCIATION = "idProjectAssociation";
	public static final String PROJECTBYLEAD = "projectByLead";
	public static final String PROJECTBYDEPENDENT = "projectByDependent";
	public static final String UPDATEDATES = "updateDates";

     private Integer idProjectAssociation;
     private Project projectByLead;
     private Project projectByDependent;
     private Boolean updateDates;

    public BaseProjectassociation() {
    }
    
    public BaseProjectassociation(Integer idProjectAssociation) {
    	this.idProjectAssociation = idProjectAssociation;
    }
   
    public Integer getIdProjectAssociation() {
        return this.idProjectAssociation;
    }
    
    public void setIdProjectAssociation(Integer idProjectAssociation) {
        this.idProjectAssociation = idProjectAssociation;
    }
    public Project getProjectByLead() {
        return this.projectByLead;
    }
    
    public void setProjectByLead(Project projectByLead) {
        this.projectByLead = projectByLead;
    }
    public Project getProjectByDependent() {
        return this.projectByDependent;
    }
    
    public void setProjectByDependent(Project projectByDependent) {
        this.projectByDependent = projectByDependent;
    }
    public Boolean getUpdateDates() {
        return this.updateDates;
    }
    
    public void setUpdateDates(Boolean updateDates) {
        this.updateDates = updateDates;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Projectassociation) )  { result = false; }
		 else if (other != null) {
		 	Projectassociation castOther = (Projectassociation) other;
			if (castOther.getIdProjectAssociation().equals(this.getIdProjectAssociation())) { result = true; }
         }
		 return result;
   }


}


