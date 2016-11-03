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
 * File: BaseClassificationlevel.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Classificationlevel;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Project;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Classificationlevel
 * @see es.sm2.openppm.core.model.base.Classificationlevel
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Classificationlevel
 */
public class BaseClassificationlevel  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "classificationlevel";
	
	public static final String IDCLASSIFICATIONLEVEL = "idClassificationlevel";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String THRESHOLDMIN = "thresholdMin";
	public static final String THRESHOLDMAX = "thresholdMax";
	public static final String PROJECTS = "projects";

     private Integer idClassificationlevel;
     private Company company;
     private String name;
     private String description;
     private Integer thresholdMin;
     private Integer thresholdMax;
     private Set<Project> projects = new HashSet<Project>(0);

    public BaseClassificationlevel() {
    }
    
    public BaseClassificationlevel(Integer idClassificationlevel) {
    	this.idClassificationlevel = idClassificationlevel;
    }
   
    public Integer getIdClassificationlevel() {
        return this.idClassificationlevel;
    }
    
    public void setIdClassificationlevel(Integer idClassificationlevel) {
        this.idClassificationlevel = idClassificationlevel;
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
    public Integer getThresholdMin() {
        return this.thresholdMin;
    }
    
    public void setThresholdMin(Integer thresholdMin) {
        this.thresholdMin = thresholdMin;
    }
    public Integer getThresholdMax() {
        return this.thresholdMax;
    }
    
    public void setThresholdMax(Integer thresholdMax) {
        this.thresholdMax = thresholdMax;
    }
    public Set<Project> getProjects() {
        return this.projects;
    }
    
    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Classificationlevel) )  { result = false; }
		 else if (other != null) {
		 	Classificationlevel castOther = (Classificationlevel) other;
			if (castOther.getIdClassificationlevel().equals(this.getIdClassificationlevel())) { result = true; }
         }
		 return result;
   }


}


