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
 * File: BaseProcurementpayments.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Procurementpayments;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Seller;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Procurementpayments
 * @see es.sm2.openppm.core.model.base.BaseProcurementpayments
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Procurementpayments
 */
public class BaseProcurementpayments  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "procurementpayments";
	
	public static final String IDPROCUREMENTPAYMENT = "idProcurementPayment";
	public static final String SELLER = "seller";
	public static final String PROJECT = "project";
	public static final String PURCHASEORDER = "purchaseOrder";
	public static final String PLANNEDDATE = "plannedDate";
	public static final String ACTUALDATE = "actualDate";
	public static final String PLANNEDPAYMENT = "plannedPayment";
	public static final String ACTUALPAYMENT = "actualPayment";
	public static final String PAYMENTSCHEDULEINFO = "paymentScheduleInfo";
    public static final String CONCEPT = "concept";
     public static final String DEDUCTIBLE = "deductible";

     private Integer idProcurementPayment;
     private Seller seller;
     private Project project;
     private String purchaseOrder;
     private Date plannedDate;
     private Date actualDate;
     private Double plannedPayment;
     private Double actualPayment;
     private String paymentScheduleInfo;
     private String concept;
     private Boolean deductible;

    public BaseProcurementpayments() {
    }
    
    public BaseProcurementpayments(Integer idProcurementPayment) {
    	this.idProcurementPayment = idProcurementPayment;
    }
   
    public Integer getIdProcurementPayment() {
        return this.idProcurementPayment;
    }
    
    public void setIdProcurementPayment(Integer idProcurementPayment) {
        this.idProcurementPayment = idProcurementPayment;
    }
    public Seller getSeller() {
        return this.seller;
    }
    
    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public String getPurchaseOrder() {
        return this.purchaseOrder;
    }
    
    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
    public Date getPlannedDate() {
        return this.plannedDate;
    }
    
    public void setPlannedDate(Date plannedDate) {
        this.plannedDate = plannedDate;
    }
    public Date getActualDate() {
        return this.actualDate;
    }
    
    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }
    public Double getPlannedPayment() {
        return this.plannedPayment;
    }
    
    public void setPlannedPayment(Double plannedPayment) {
        this.plannedPayment = plannedPayment;
    }
    public Double getActualPayment() {
        return this.actualPayment;
    }
    
    public void setActualPayment(Double actualPayment) {
        this.actualPayment = actualPayment;
    }
    public String getPaymentScheduleInfo() {
        return this.paymentScheduleInfo;
    }
    
    public void setPaymentScheduleInfo(String paymentScheduleInfo) {
        this.paymentScheduleInfo = paymentScheduleInfo;
    }

     /**
      * Getter for property 'concept'.
      *
      * @return Value for property 'concept'.
      */
     public String getConcept() {
         return concept;
     }

     /**
      * Setter for property 'concept'.
      *
      * @param concept Value to set for property 'concept'.
      */
     public void setConcept(String concept) {
         this.concept = concept;
     }

     /**
      * Getter for property 'deductible'.
      *
      * @return Value for property 'deductible'.
      */
     public Boolean getDeductible() {
         return deductible;
     }

     /**
      * Setter for property 'deductible'.
      *
      * @param deductible Value to set for property 'deductible'.
      */
     public void setDeductible(Boolean deductible) {
         this.deductible = deductible;
     }

     @Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Procurementpayments) )  { result = false; }
		 else if (other != null) {
		 	Procurementpayments castOther = (Procurementpayments) other;
			if (castOther.getIdProcurementPayment().equals(this.getIdProcurementPayment())) { result = true; }
         }
		 return result;
   }


}


