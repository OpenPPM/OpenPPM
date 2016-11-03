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
 * File: BaseIncomes.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Incomes;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Incomes
 * @see es.sm2.openppm.core.model.base.Incomes
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Incomes
 */
public class BaseIncomes  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "incomes";
	
	public static final String IDINCOME = "idIncome";
	public static final String PROJECT = "project";
	public static final String PLANNEDBILLDATE = "plannedBillDate";
	public static final String PLANNEDBILLAMMOUNT = "plannedBillAmmount";
	public static final String ACTUALBILLDATE = "actualBillDate";
	public static final String ACTUALBILLAMMOUNT = "actualBillAmmount";
	public static final String PLANNEDDESCRIPTION = "plannedDescription";
	public static final String ACTUALDESCRIPTION = "actualDescription";
	public static final String ACTUALPAYMENTDATE = "actualPaymentDate";

     private Integer idIncome;
     private Project project;
     private Date plannedBillDate;
     private Double plannedBillAmmount;
     private Date actualBillDate;
     private Double actualBillAmmount;
     private String plannedDescription;
     private String actualDescription;
     private Date actualPaymentDate;

    public BaseIncomes() {
    }
    
    public BaseIncomes(Integer idIncome) {
    	this.idIncome = idIncome;
    }
   
    public Integer getIdIncome() {
        return this.idIncome;
    }
    
    public void setIdIncome(Integer idIncome) {
        this.idIncome = idIncome;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public Date getPlannedBillDate() {
        return this.plannedBillDate;
    }
    
    public void setPlannedBillDate(Date plannedBillDate) {
        this.plannedBillDate = plannedBillDate;
    }
    public Double getPlannedBillAmmount() {
        return this.plannedBillAmmount;
    }
    
    public void setPlannedBillAmmount(Double plannedBillAmmount) {
        this.plannedBillAmmount = plannedBillAmmount;
    }
    public Date getActualBillDate() {
        return this.actualBillDate;
    }
    
    public void setActualBillDate(Date actualBillDate) {
        this.actualBillDate = actualBillDate;
    }
    public Double getActualBillAmmount() {
        return this.actualBillAmmount;
    }
    
    public void setActualBillAmmount(Double actualBillAmmount) {
        this.actualBillAmmount = actualBillAmmount;
    }
    public String getPlannedDescription() {
        return this.plannedDescription;
    }
    
    public void setPlannedDescription(String plannedDescription) {
        this.plannedDescription = plannedDescription;
    }
    public String getActualDescription() {
        return this.actualDescription;
    }
    
    public void setActualDescription(String actualDescription) {
        this.actualDescription = actualDescription;
    }
    public Date getActualPaymentDate() {
        return this.actualPaymentDate;
    }
    
    public void setActualPaymentDate(Date actualPaymentDate) {
        this.actualPaymentDate = actualPaymentDate;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Incomes) )  { result = false; }
		 else if (other != null) {
		 	Incomes castOther = (Incomes) other;
			if (castOther.getIdIncome().equals(this.getIdIncome())) { result = true; }
         }
		 return result;
   }


}


