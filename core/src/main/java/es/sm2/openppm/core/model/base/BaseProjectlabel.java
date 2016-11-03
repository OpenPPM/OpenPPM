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
 * File: BaseProjectlabel.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Label;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectlabel;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Projectlabel
 * @see es.sm2.openppm.core.model.base.Projectlabel
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Projectlabel
 */
public class BaseProjectlabel  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "projectlabel";
	
	public static final String IDPROJECTLABEL = "idProjectLabel";
	public static final String LABEL = "label";
	public static final String PROJECT = "project";

     private Integer idProjectLabel;
     private Label label;
     private Project project;

    public BaseProjectlabel() {
    }
    
    public BaseProjectlabel(Integer idProjectLabel) {
    	this.idProjectLabel = idProjectLabel;
    }
   
    public Integer getIdProjectLabel() {
        return this.idProjectLabel;
    }
    
    public void setIdProjectLabel(Integer idProjectLabel) {
        this.idProjectLabel = idProjectLabel;
    }
    public Label getLabel() {
        return this.label;
    }
    
    public void setLabel(Label label) {
        this.label = label;
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
		 else if (!(other instanceof Projectlabel) )  { result = false; }
		 else if (other != null) {
		 	Projectlabel castOther = (Projectlabel) other;
			if (castOther.getIdProjectLabel().equals(this.getIdProjectLabel())) { result = true; }
         }
		 return result;
   }


}


