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
 * File: BaseBusinessdriver.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Businessdriver;
import es.sm2.openppm.core.model.impl.Businessdriverset;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Businessdriver
 * @see es.sm2.openppm.core.model.base.Businessdriver
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Businessdriver
 */
public class BaseBusinessdriver  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "businessdriver";
	
	public static final String IDBUSINESSDRIVER = "idBusinessDriver";
	public static final String BUSINESSDRIVERSET = "businessdriverset";
	public static final String COMPANY = "company";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String RELATIVEPRIORIZATION = "relativePriorization";

     private Integer idBusinessDriver;
     private Businessdriverset businessdriverset;
     private Company company;
     private String code;
     private String name;
     private Double relativePriorization;

    public BaseBusinessdriver() {
    }
    
    public BaseBusinessdriver(Integer idBusinessDriver) {
    	this.idBusinessDriver = idBusinessDriver;
    }
   
    public Integer getIdBusinessDriver() {
        return this.idBusinessDriver;
    }
    
    public void setIdBusinessDriver(Integer idBusinessDriver) {
        this.idBusinessDriver = idBusinessDriver;
    }
    public Businessdriverset getBusinessdriverset() {
        return this.businessdriverset;
    }
    
    public void setBusinessdriverset(Businessdriverset businessdriverset) {
        this.businessdriverset = businessdriverset;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Double getRelativePriorization() {
        return this.relativePriorization;
    }
    
    public void setRelativePriorization(Double relativePriorization) {
        this.relativePriorization = relativePriorization;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Businessdriver) )  { result = false; }
		 else if (other != null) {
		 	Businessdriver castOther = (Businessdriver) other;
			if (castOther.getIdBusinessDriver().equals(this.getIdBusinessDriver())) { result = true; }
         }
		 return result;
   }


}


