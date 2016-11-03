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
 * File: BaseBusinessdriverset.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Businessdriver;
import es.sm2.openppm.core.model.impl.Businessdriverset;
import es.sm2.openppm.core.model.impl.Company;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Businessdriverset
 * @see es.sm2.openppm.core.model.base.Businessdriverset
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Businessdriverset
 */
public class BaseBusinessdriverset  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "businessdriverset";
	
	public static final String IDBUSINESSDRIVERSET = "idBusinessDriverSet";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String BUSINESSDRIVERS = "businessdrivers";

     private int idBusinessDriverSet;
     private Company company;
     private String name;
     private Set<Businessdriver> businessdrivers = new HashSet<Businessdriver>(0);

    public BaseBusinessdriverset() {
    }
    
    public BaseBusinessdriverset(int idBusinessDriverSet) {
    	this.idBusinessDriverSet = idBusinessDriverSet;
    }
   
    public int getIdBusinessDriverSet() {
        return this.idBusinessDriverSet;
    }
    
    public void setIdBusinessDriverSet(int idBusinessDriverSet) {
        this.idBusinessDriverSet = idBusinessDriverSet;
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
    public Set<Businessdriver> getBusinessdrivers() {
        return this.businessdrivers;
    }
    
    public void setBusinessdrivers(Set<Businessdriver> businessdrivers) {
        this.businessdrivers = businessdrivers;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Businessdriverset) )  { result = false; }
		 else if (other != null) {
		 	Businessdriverset castOther = (Businessdriverset) other;
			if (castOther.getIdBusinessDriverSet() == this.getIdBusinessDriverSet()) { result = true; }
         }
		 return result;
   }


}


