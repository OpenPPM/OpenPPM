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
 * File: ProcurementBudget.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.javabean;

public class ProcurementBudget {

	private String seller;
	private int nPayments;
	private String purchaseOrder;
	private Double plannedPayment;
	private Double actualPayment;
	
	public String getSeller() {
		return seller;
	}
	
	public void setSeller(String seller) {
		this.seller = seller;
	}
	
	public int getnPayments() {
		return nPayments;
	}
	
	public void setnPayments(int nPayments) {
		this.nPayments = nPayments;
	}
	
	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	
	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public Double getPlannedPayment() {
		return plannedPayment;
	}
	
	public void setPlannedPayment(Double plannedPayment) {
		this.plannedPayment = plannedPayment;
	}
	
	public Double getActualPayment() {
		return actualPayment;
	}
	
	public void setActualPayment(Double actualPayment) {
		this.actualPayment = actualPayment;
	}	
}