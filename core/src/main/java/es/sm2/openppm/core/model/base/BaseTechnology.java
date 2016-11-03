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
 * File: BaseTechnologies.java
 * Create User: jordi.ripoll
 * Create Date: 29/06/2015 12:32:31
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.ProjectTechnology;
import es.sm2.openppm.core.model.impl.Technology;

import java.util.HashSet;
import java.util.Set;

/**
* Base Pojo object for domain model class Technology
* @see es.sm2.openppm.core.model.base.BaseTechnology
* @author Hibernate Generator by Javier Hernandez
* For implement your own methods use class Technology
*/
public class BaseTechnology implements java.io.Serializable {


   private static final long serialVersionUID = 1L;

   public static final String ENTITY = "TECHNOLOGY";

   public static final String IDTECHNOLOGY = "idTechnology";
   public static final String COMPANY = "company";
   public static final String NAME = "name";
   public static final String DESCRIPTION = "description";
    public static final String PROJECTTECHNOLOGIES = "projecttechnologies";

    private Integer idTechnology;
    private Company company;
    private String name;
    private String description;
    private Set<ProjectTechnology> projecttechnologies = new HashSet<ProjectTechnology>(0);

   public BaseTechnology() {
   }

   public BaseTechnology(Integer idTechnology) {
       this.idTechnology = idTechnology;
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdTechnology() {
        return idTechnology;
    }

    public void setIdTechnology(Integer idTechnology) {
        this.idTechnology = idTechnology;
    }


    public Set<ProjectTechnology> getProjecttechnologies() {
        return projecttechnologies;
    }

    public void setProjecttechnologies(Set<ProjectTechnology> projecttechnologies) {
        this.projecttechnologies = projecttechnologies;
    }

   @Override
   public boolean equals(Object other) {
       boolean result = false;
        if (this == other) { result = true; }
        else if (other == null) { result = false; }
        else if (!(other instanceof Technology) )  { result = false; }
        else if (other != null) {
            Technology castOther = (Technology) other;
           if (castOther.getIdTechnology().equals(this.getIdTechnology())) { result = true; }
        }
        return result;
  }

}


