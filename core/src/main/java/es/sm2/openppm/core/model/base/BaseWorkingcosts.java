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
 * File: BaseWorkingcosts.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Currency;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Workingcosts;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Workingcosts
 * @see es.sm2.openppm.core.model.base.Workingcosts
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Workingcosts
 */
public class BaseWorkingcosts  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "workingcosts";
	
	public static final String IDWORKINGCOSTS = "idWorkingCosts";
	public static final String CURRENCY = "currency";
	public static final String PROJECT = "project";
	public static final String RESOURCENAME = "resourceName";
	public static final String RESOURCEDEPARTMENT = "resourceDepartment";
	public static final String EFFORT = "effort";
	public static final String RATE = "rate";
	public static final String WORKCOST = "workCost";
	public static final String Q1 = "q1";
	public static final String Q2 = "q2";
	public static final String Q3 = "q3";
	public static final String Q4 = "q4";
	public static final String REALEFFORT = "realEffort";

     private Integer idWorkingCosts;
     private Currency currency;
     private Project project;
     private String resourceName;
     private String resourceDepartment;
     private Integer effort;
     private Double rate;
     private Double workCost;
     private Integer q1;
     private Integer q2;
     private Integer q3;
     private Integer q4;
     private Integer realEffort;

    public BaseWorkingcosts() {
    }
    
    public BaseWorkingcosts(Integer idWorkingCosts) {
    	this.idWorkingCosts = idWorkingCosts;
    }
   
    public Integer getIdWorkingCosts() {
        return this.idWorkingCosts;
    }
    
    public void setIdWorkingCosts(Integer idWorkingCosts) {
        this.idWorkingCosts = idWorkingCosts;
    }
    public Currency getCurrency() {
        return this.currency;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public String getResourceName() {
        return this.resourceName;
    }
    
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    public String getResourceDepartment() {
        return this.resourceDepartment;
    }
    
    public void setResourceDepartment(String resourceDepartment) {
        this.resourceDepartment = resourceDepartment;
    }
    public Integer getEffort() {
        return this.effort;
    }
    
    public void setEffort(Integer effort) {
        this.effort = effort;
    }
    public Double getRate() {
        return this.rate;
    }
    
    public void setRate(Double rate) {
        this.rate = rate;
    }
    public Double getWorkCost() {
        return this.workCost;
    }
    
    public void setWorkCost(Double workCost) {
        this.workCost = workCost;
    }
    public Integer getQ1() {
        return this.q1;
    }
    
    public void setQ1(Integer q1) {
        this.q1 = q1;
    }
    public Integer getQ2() {
        return this.q2;
    }
    
    public void setQ2(Integer q2) {
        this.q2 = q2;
    }
    public Integer getQ3() {
        return this.q3;
    }
    
    public void setQ3(Integer q3) {
        this.q3 = q3;
    }
    public Integer getQ4() {
        return this.q4;
    }
    
    public void setQ4(Integer q4) {
        this.q4 = q4;
    }
    public Integer getRealEffort() {
        return this.realEffort;
    }
    
    public void setRealEffort(Integer realEffort) {
        this.realEffort = realEffort;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Workingcosts) )  { result = false; }
		 else if (other != null) {
		 	Workingcosts castOther = (Workingcosts) other;
			if (castOther.getIdWorkingCosts().equals(this.getIdWorkingCosts())) { result = true; }
         }
		 return result;
   }


}


