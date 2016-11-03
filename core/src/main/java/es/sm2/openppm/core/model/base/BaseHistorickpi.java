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
 * File: BaseHistorickpi.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Historickpi;
import es.sm2.openppm.core.model.impl.Projectkpi;

import java.util.Date;

 /**
 * Base Pojo object for domain model class Historickpi
 * @see es.sm2.openppm.core.model.base.BaseHistorickpi
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Historickpi
 */
public class BaseHistorickpi  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "historickpi";
	
	public static final String IDHISTORICKPI = "idHistoricKpi";
	public static final String PROJECTKPI = "projectkpi";
	public static final String EMPLOYEE = "employee";
	public static final String UPPERTHRESHOLD = "upperThreshold";
	public static final String LOWERTHRESHOLD = "lowerThreshold";
	public static final String WEIGHT = "weight";
	public static final String VALUEKPI = "valueKpi";
	public static final String ACTUALDATE = "actualDate";
    public static final String UPDATEDTYPE = "updatedType";


     private Integer idHistoricKpi;
     private Projectkpi projectkpi;
     private Employee employee;
     private Double upperThreshold;
     private Double lowerThreshold;
     private Double weight;
     private Double valueKpi;
     private Date actualDate;
     private String updatedType;

    public BaseHistorickpi() {
    }
    
    public BaseHistorickpi(Integer idHistoricKpi) {
    	this.idHistoricKpi = idHistoricKpi;
    }
   
    public Integer getIdHistoricKpi() {
        return this.idHistoricKpi;
    }
    
    public void setIdHistoricKpi(Integer idHistoricKpi) {
        this.idHistoricKpi = idHistoricKpi;
    }
    public Projectkpi getProjectkpi() {
        return this.projectkpi;
    }
    
    public void setProjectkpi(Projectkpi projectkpi) {
        this.projectkpi = projectkpi;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Double getUpperThreshold() {
        return this.upperThreshold;
    }
    
    public void setUpperThreshold(Double upperThreshold) {
        this.upperThreshold = upperThreshold;
    }
    public Double getLowerThreshold() {
        return this.lowerThreshold;
    }
    
    public void setLowerThreshold(Double lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
    }
    public Double getWeight() {
        return this.weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public Double getValueKpi() {
        return this.valueKpi;
    }
    
    public void setValueKpi(Double valueKpi) {
        this.valueKpi = valueKpi;
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
		 else if (!(other instanceof Historickpi) )  { result = false; }
		 else if (other != null) {
		 	Historickpi castOther = (Historickpi) other;
			if (castOther.getIdHistoricKpi().equals(this.getIdHistoricKpi())) { result = true; }
         }
		 return result;
   }


     public String getUpdatedType() {
         return updatedType;
     }

     public void setUpdatedType(String updatedType) {
         this.updatedType = updatedType;
     }
 }


