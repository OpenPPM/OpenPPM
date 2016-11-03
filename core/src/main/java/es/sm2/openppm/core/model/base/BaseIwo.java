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
 * File: BaseIwo.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Iwo;
import es.sm2.openppm.core.model.impl.Projectcosts;


/**
 * Base Pojo object for domain model class Iwo
 * @see es.sm2.openppm.core.model.base.Iwo
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Iwo
 */
public class BaseIwo  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "iwo";
	
	public static final String IDIWO = "idIwo";
	public static final String PROJECTCOSTS = "projectcosts";
	public static final String PLANNED = "planned";
	public static final String ACTUAL = "actual";
	public static final String DESCRIPTION = "description";
	public static final String IWODOC = "iwodoc";

     private Integer idIwo;
     private Projectcosts projectcosts;
     private Double planned;
     private Double actual;
     private String description;
     private String iwodoc;

    public BaseIwo() {
    }
    
    public BaseIwo(Integer idIwo) {
    	this.idIwo = idIwo;
    }
   
    public Integer getIdIwo() {
        return this.idIwo;
    }
    
    public void setIdIwo(Integer idIwo) {
        this.idIwo = idIwo;
    }
    public Projectcosts getProjectcosts() {
        return this.projectcosts;
    }
    
    public void setProjectcosts(Projectcosts projectcosts) {
        this.projectcosts = projectcosts;
    }
    public Double getPlanned() {
        return this.planned;
    }
    
    public void setPlanned(Double planned) {
        this.planned = planned;
    }
    public Double getActual() {
        return this.actual;
    }
    
    public void setActual(Double actual) {
        this.actual = actual;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIwodoc() {
        return this.iwodoc;
    }
    
    public void setIwodoc(String iwodoc) {
        this.iwodoc = iwodoc;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Iwo) )  { result = false; }
		 else if (other != null) {
		 	Iwo castOther = (Iwo) other;
			if (castOther.getIdIwo().equals(this.getIdIwo())) { result = true; }
         }
		 return result;
   }


}


