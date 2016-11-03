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
 * File: WorkingCostSync.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:24:00
 */

package es.sm2.openppm.core.audit.model;

import es.sm2.openppm.core.model.impl.Workingcosts;

public class WorkingCostAudit {

	private Integer idCurrency;
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

	public WorkingCostAudit(Workingcosts workingcost) {

		if (workingcost.getCurrency() != null) {
			setIdCurrency(workingcost.getCurrency().getIdCurrency());
		}
		setResourceName(workingcost.getResourceName());
		setResourceDepartment(workingcost.getResourceDepartment());
		setEffort(workingcost.getEffort());
		setRate(workingcost.getRate());
		setWorkCost(workingcost.getWorkCost());
		setQ1(workingcost.getQ1());
		setQ2(workingcost.getQ2());
		setQ3(workingcost.getQ3());
		setQ4(workingcost.getQ4());
		setRealEffort(workingcost.getRealEffort());
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

	/**
	 * @return the idCurrency
	 */
	public Integer getIdCurrency() {
		return idCurrency;
	}

	/**
	 * @param idCurrency
	 *            the idCurrency to set
	 */
	public void setIdCurrency(Integer idCurrency) {
		this.idCurrency = idCurrency;
	}
}
