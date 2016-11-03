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
 * File: BaseRiskcategories.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Riskcategories;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.impl.Riskregistertemplate;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Riskcategories
 * @see es.sm2.openppm.core.model.base.Riskcategories
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Riskcategories
 */
public class BaseRiskcategories  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "riskcategories";
	
	public static final String IDRISKCATEGORY = "idRiskCategory";
	public static final String DESCRIPTION = "description";
	public static final String RISKTYPE = "riskType";
	public static final String MITIGATE = "mitigate";
	public static final String ACCEPT = "accept";
	public static final String RISKREGISTERTEMPLATES = "riskregistertemplates";
	public static final String RISKREGISTERS = "riskregisters";

     private int idRiskCategory;
     private String description;
     private int riskType;
     private boolean mitigate;
     private boolean accept;
     private Set<Riskregistertemplate> riskregistertemplates = new HashSet<Riskregistertemplate>(0);
     private Set<Riskregister> riskregisters = new HashSet<Riskregister>(0);

    public BaseRiskcategories() {
    }
    
    public BaseRiskcategories(int idRiskCategory) {
    	this.idRiskCategory = idRiskCategory;
    }
   
    public int getIdRiskCategory() {
        return this.idRiskCategory;
    }
    
    public void setIdRiskCategory(int idRiskCategory) {
        this.idRiskCategory = idRiskCategory;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public int getRiskType() {
        return this.riskType;
    }
    
    public void setRiskType(int riskType) {
        this.riskType = riskType;
    }
    public boolean isMitigate() {
        return this.mitigate;
    }
    
    public void setMitigate(boolean mitigate) {
        this.mitigate = mitigate;
    }
    public boolean isAccept() {
        return this.accept;
    }
    
    public void setAccept(boolean accept) {
        this.accept = accept;
    }
    public Set<Riskregistertemplate> getRiskregistertemplates() {
        return this.riskregistertemplates;
    }
    
    public void setRiskregistertemplates(Set<Riskregistertemplate> riskregistertemplates) {
        this.riskregistertemplates = riskregistertemplates;
    }
    public Set<Riskregister> getRiskregisters() {
        return this.riskregisters;
    }
    
    public void setRiskregisters(Set<Riskregister> riskregisters) {
        this.riskregisters = riskregisters;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Riskcategories) )  { result = false; }
		 else if (other != null) {
		 	Riskcategories castOther = (Riskcategories) other;
			if (castOther.getIdRiskCategory() == this.getIdRiskCategory()) { result = true; }
         }
		 return result;
   }


}


