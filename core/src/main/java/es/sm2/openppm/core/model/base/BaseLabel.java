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
 * File: BaseLabel.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Label;
import es.sm2.openppm.core.model.impl.Projectlabel;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Label
 * @see es.sm2.openppm.core.model.base.Label
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Label
 */
public class BaseLabel  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "label";
	
	public static final String IDLABEL = "idLabel";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String DISABLE = "disable";
	public static final String PROJECTLABELS = "projectlabels";

     private Integer idLabel;
     private Company company;
     private String name;
     private String description;
     private Boolean disable;
     private Set<Projectlabel> projectlabels = new HashSet<Projectlabel>(0);

    public BaseLabel() {
    }
    
    public BaseLabel(Integer idLabel) {
    	this.idLabel = idLabel;
    }
   
    public Integer getIdLabel() {
        return this.idLabel;
    }
    
    public void setIdLabel(Integer idLabel) {
        this.idLabel = idLabel;
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
    public Boolean getDisable() {
        return this.disable;
    }
    
    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
    public Set<Projectlabel> getProjectlabels() {
        return this.projectlabels;
    }
    
    public void setProjectlabels(Set<Projectlabel> projectlabels) {
        this.projectlabels = projectlabels;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Label) )  { result = false; }
		 else if (other != null) {
		 	Label castOther = (Label) other;
			if (castOther.getIdLabel().equals(this.getIdLabel())) { result = true; }
         }
		 return result;
   }


}


