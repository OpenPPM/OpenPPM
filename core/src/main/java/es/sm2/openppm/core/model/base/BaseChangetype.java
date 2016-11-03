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
 * File: BaseChangetype.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Changetype;
import es.sm2.openppm.core.model.impl.Company;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Changetype
 * @see es.sm2.openppm.core.model.base.Changetype
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Changetype
 */
public class BaseChangetype  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "changetype";
	
	public static final String IDCHANGETYPE = "idChangeType";
	public static final String COMPANY = "company";
	public static final String DESCRIPTION = "description";
	public static final String CHANGECONTROLS = "changecontrols";

     private Integer idChangeType;
     private Company company;
     private String description;
     private Set<Changecontrol> changecontrols = new HashSet<Changecontrol>(0);

    public BaseChangetype() {
    }
    
    public BaseChangetype(Integer idChangeType) {
    	this.idChangeType = idChangeType;
    }
   
    public Integer getIdChangeType() {
        return this.idChangeType;
    }
    
    public void setIdChangeType(Integer idChangeType) {
        this.idChangeType = idChangeType;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<Changecontrol> getChangecontrols() {
        return this.changecontrols;
    }
    
    public void setChangecontrols(Set<Changecontrol> changecontrols) {
        this.changecontrols = changecontrols;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Changetype) )  { result = false; }
		 else if (other != null) {
		 	Changetype castOther = (Changetype) other;
			if (castOther.getIdChangeType().equals(this.getIdChangeType())) { result = true; }
         }
		 return result;
   }


}


