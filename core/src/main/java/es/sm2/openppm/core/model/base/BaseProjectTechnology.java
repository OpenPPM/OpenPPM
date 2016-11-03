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
 * File: BaseProjectTechnology.java
 * Create User: jordi.ripoll
 * Create Date: 29/06/2015 13:26:27
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.ProjectTechnology;
import es.sm2.openppm.core.model.impl.Technology;


/**
* Base Pojo object for domain model class Projecttechnology
* @see es.sm2.openppm.core.model.base.BaseProjectTechnology
* @author Hibernate Generator by Javier Hernandez
* For implement your own methods use class Projecttechnology
*/
public class BaseProjectTechnology implements java.io.Serializable {


   private static final long serialVersionUID = 1L;

   public static final String ENTITY = "projecttechnology";

   public static final String IDPROJECTTECHNOLOGY = "idProjectTechnology";
   public static final String TECHNOLOGY = "technology";
   public static final String PROJECT = "project";

    private Integer idProjectTechnology;
    private Technology technology;
    private Project project;

   public BaseProjectTechnology() {
   }

   public BaseProjectTechnology(Integer idProjectTechnology) {
       this.idProjectTechnology = idProjectTechnology;
   }

   public Integer getIdProjectTechnology() {
       return this.idProjectTechnology;
   }

   public void setIdProjectTechnology(Integer idProjectTechnology) {
       this.idProjectTechnology = idProjectTechnology;
   }
   public Technology getTechnology() {
       return this.technology;
   }

   public void setTechnology(Technology technology) {
       this.technology = technology;
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
        else if (!(other instanceof ProjectTechnology) )  { result = false; }
        else if (other != null) {
            ProjectTechnology castOther = (ProjectTechnology) other;
           if (castOther.getIdProjectTechnology().equals(this.getIdProjectTechnology())) { result = true; }
        }
        return result;
  }

}


