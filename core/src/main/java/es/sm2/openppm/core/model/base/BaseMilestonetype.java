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
 * File: BaseMilestonetype.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Milestonetype
 * @see es.sm2.openppm.core.model.base.Milestonetype
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Milestonetype
 */
public class BaseMilestonetype  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "milestonetype";
	
	public static final String IDMILESTONETYPE = "idMilestoneType";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String MILESTONESES = "milestoneses";

     private Integer idMilestoneType;
     private Company company;
     private String name;
     private String description;
     private Set<Milestones> milestoneses = new HashSet<Milestones>(0);

    public BaseMilestonetype() {
    }
    
    public BaseMilestonetype(Integer idMilestoneType) {
    	this.idMilestoneType = idMilestoneType;
    }
   
    public Integer getIdMilestoneType() {
        return this.idMilestoneType;
    }
    
    public void setIdMilestoneType(Integer idMilestoneType) {
        this.idMilestoneType = idMilestoneType;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<Milestones> getMilestoneses() {
        return this.milestoneses;
    }
    
    public void setMilestoneses(Set<Milestones> milestoneses) {
        this.milestoneses = milestoneses;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Milestonetype) )  { result = false; }
		 else if (other != null) {
		 	Milestonetype castOther = (Milestonetype) other;
			if (castOther.getIdMilestoneType().equals(this.getIdMilestoneType())) { result = true; }
         }
		 return result;
   }


}


