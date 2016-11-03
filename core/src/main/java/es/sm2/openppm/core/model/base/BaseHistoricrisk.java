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
 * File: BaseHistoricrisk.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Historicrisk;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Historicrisk
 * @see es.sm2.openppm.core.model.base.Historicrisk
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Historicrisk
 */
public class BaseHistoricrisk  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "historicrisk";
	
	public static final String IDHISTORICRISK = "idHistoricrisk";
	public static final String RISKREGISTER = "riskregister";
	public static final String EMPLOYEE = "employee";
	public static final String PROBABILITY = "probability";
	public static final String IMPACT = "impact";
	public static final String ACTUALDATE = "actualDate";

     private Integer idHistoricrisk;
     private Riskregister riskregister;
     private Employee employee;
     private Integer probability;
     private Integer impact;
     private Date actualDate;

    public BaseHistoricrisk() {
    }
    
    public BaseHistoricrisk(Integer idHistoricrisk) {
    	this.idHistoricrisk = idHistoricrisk;
    }
   
    public Integer getIdHistoricrisk() {
        return this.idHistoricrisk;
    }
    
    public void setIdHistoricrisk(Integer idHistoricrisk) {
        this.idHistoricrisk = idHistoricrisk;
    }
    public Riskregister getRiskregister() {
        return this.riskregister;
    }
    
    public void setRiskregister(Riskregister riskregister) {
        this.riskregister = riskregister;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Integer getProbability() {
        return this.probability;
    }
    
    public void setProbability(Integer probability) {
        this.probability = probability;
    }
    public Integer getImpact() {
        return this.impact;
    }
    
    public void setImpact(Integer impact) {
        this.impact = impact;
    }
    public Date getActualDate() {
        return this.actualDate;
    }
    
    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Historicrisk) )  { result = false; }
		 else if (other != null) {
		 	Historicrisk castOther = (Historicrisk) other;
			if (castOther.getIdHistoricrisk().equals(this.getIdHistoricrisk())) { result = true; }
         }
		 return result;
   }


}


