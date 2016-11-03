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
 * File: BaseResourceprofiles.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Resourceprofiles
 * @see es.sm2.openppm.core.model.base.Resourceprofiles
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Resourceprofiles
 */
public class BaseResourceprofiles  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "resourceprofiles";
	
	public static final String IDPROFILE = "idProfile";
	public static final String PROFILENAME = "profileName";
	public static final String DESCRIPTION = "description";
	public static final String EMPLOYEES = "employees";

     private int idProfile;
     private String profileName;
     private String description;
     private Set<Employee> employees = new HashSet<Employee>(0);

    public BaseResourceprofiles() {
    }
    
    public BaseResourceprofiles(int idProfile) {
    	this.idProfile = idProfile;
    }
   
    public int getIdProfile() {
        return this.idProfile;
    }
    
    public void setIdProfile(int idProfile) {
        this.idProfile = idProfile;
    }
    public String getProfileName() {
        return this.profileName;
    }
    
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<Employee> getEmployees() {
        return this.employees;
    }
    
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Resourceprofiles) )  { result = false; }
		 else if (other != null) {
		 	Resourceprofiles castOther = (Resourceprofiles) other;
			if (castOther.getIdProfile() == this.getIdProfile()) { result = true; }
         }
		 return result;
   }


}


