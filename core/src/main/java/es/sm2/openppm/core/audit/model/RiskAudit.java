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
 * File: RiskSync.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:24:00
 */

package es.sm2.openppm.core.audit.model;

import es.sm2.openppm.core.model.impl.Riskregister;

import java.util.Date;

public class RiskAudit {

	private Integer idRiskCategory;
	private String riskCode;
	private String riskName;
	private String owner;
	private Date dateRaised;
	private Double potentialCost;
	private Integer potentialDelay;
	private String riskTrigger;
	private String description;
	private Integer probability;
	private Integer impact;
	private Boolean materialized;
	private String mitigationActionsRequired;
	private String contingencyActionsRequired;
	private Double actualMaterializationCost;
	private Integer actualMaterializationDelay;
	private String finalComments;
	private String riskDoc;
	private Integer riskType;
	private Double plannedMitigationCost;
	private Double plannedContingencyCost;
	private Boolean closed;
	private Date dateMaterialization;
	private Date dueDate;
	private String status;
	private String responseDescription;
	private String residualRisk;
	private Double residualCost;

	public RiskAudit(Riskregister riskregister) {
		
		if (riskregister.getRiskcategories() != null) {
			setIdRiskCategory(riskregister.getRiskcategories().getIdRiskCategory());
		}
		
		setRiskCode(riskregister.getRiskCode());
		setRiskName(riskregister.getRiskName());
		setOwner(riskregister.getOwner());
		setDateRaised(riskregister.getDateRaised());
		setPotentialCost(riskregister.getPotentialCost());
		setPotentialDelay(riskregister.getPotentialDelay());
		setRiskTrigger(riskregister.getRiskTrigger());
		setDescription(riskregister.getDescription());
		setProbability(riskregister.getProbability());
		setImpact(riskregister.getImpact());
		setMaterialized(riskregister.getMaterialized());
		setMitigationActionsRequired(riskregister.getMitigationActionsRequired());
		setContingencyActionsRequired(riskregister.getContingencyActionsRequired());
		setActualMaterializationCost(riskregister.getActualMaterializationCost());
		setActualMaterializationDelay(riskregister.getActualMaterializationDelay());
		setFinalComments(riskregister.getFinalComments());
		setRiskDoc(riskregister.getRiskDoc());
		setRiskType(riskregister.getRiskType());
		setPlannedMitigationCost(riskregister.getPlannedMitigationCost());
		setPlannedContingencyCost(riskregister.getPlannedContingencyCost());
		setClosed(riskregister.getClosed());
		setDateMaterialization(riskregister.getDateMaterialization());
		setDueDate(riskregister.getDueDate());
		setStatus(riskregister.getStatus());
		setResponseDescription(riskregister.getResponseDescription());
		setResidualRisk(riskregister.getResidualRisk());
		setResidualCost(riskregister.getResidualCost());
	}
	
	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getRiskName() {
		return this.riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getDateRaised() {
		return this.dateRaised;
	}

	public void setDateRaised(Date dateRaised) {
		this.dateRaised = dateRaised;
	}

	public Double getPotentialCost() {
		return this.potentialCost;
	}

	public void setPotentialCost(Double potentialCost) {
		this.potentialCost = potentialCost;
	}

	public Integer getPotentialDelay() {
		return this.potentialDelay;
	}

	public void setPotentialDelay(Integer potentialDelay) {
		this.potentialDelay = potentialDelay;
	}

	public String getRiskTrigger() {
		return this.riskTrigger;
	}

	public void setRiskTrigger(String riskTrigger) {
		this.riskTrigger = riskTrigger;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Boolean getMaterialized() {
		return this.materialized;
	}

	public void setMaterialized(Boolean materialized) {
		this.materialized = materialized;
	}

	public String getMitigationActionsRequired() {
		return this.mitigationActionsRequired;
	}

	public void setMitigationActionsRequired(String mitigationActionsRequired) {
		this.mitigationActionsRequired = mitigationActionsRequired;
	}

	public String getContingencyActionsRequired() {
		return this.contingencyActionsRequired;
	}

	public void setContingencyActionsRequired(String contingencyActionsRequired) {
		this.contingencyActionsRequired = contingencyActionsRequired;
	}

	public Double getActualMaterializationCost() {
		return this.actualMaterializationCost;
	}

	public void setActualMaterializationCost(Double actualMaterializationCost) {
		this.actualMaterializationCost = actualMaterializationCost;
	}

	public Integer getActualMaterializationDelay() {
		return this.actualMaterializationDelay;
	}

	public void setActualMaterializationDelay(Integer actualMaterializationDelay) {
		this.actualMaterializationDelay = actualMaterializationDelay;
	}

	public String getFinalComments() {
		return this.finalComments;
	}

	public void setFinalComments(String finalComments) {
		this.finalComments = finalComments;
	}

	public String getRiskDoc() {
		return this.riskDoc;
	}

	public void setRiskDoc(String riskDoc) {
		this.riskDoc = riskDoc;
	}

	public Integer getRiskType() {
		return this.riskType;
	}

	public void setRiskType(Integer riskType) {
		this.riskType = riskType;
	}

	public Double getPlannedMitigationCost() {
		return this.plannedMitigationCost;
	}

	public void setPlannedMitigationCost(Double plannedMitigationCost) {
		this.plannedMitigationCost = plannedMitigationCost;
	}

	public Double getPlannedContingencyCost() {
		return this.plannedContingencyCost;
	}

	public void setPlannedContingencyCost(Double plannedContingencyCost) {
		this.plannedContingencyCost = plannedContingencyCost;
	}

	public Boolean getClosed() {
		return this.closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	public Date getDateMaterialization() {
		return this.dateMaterialization;
	}

	public void setDateMaterialization(Date dateMaterialization) {
		this.dateMaterialization = dateMaterialization;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseDescription() {
		return this.responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getResidualRisk() {
		return this.residualRisk;
	}

	public void setResidualRisk(String residualRisk) {
		this.residualRisk = residualRisk;
	}

	public Double getResidualCost() {
		return this.residualCost;
	}

	public void setResidualCost(Double residualCost) {
		this.residualCost = residualCost;
	}

	/**
	 * @return the idRiskCategory
	 */
	public Integer getIdRiskCategory() {
		return idRiskCategory;
	}

	/**
	 * @param idRiskCategory the idRiskCategory to set
	 */
	public void setIdRiskCategory(Integer idRiskCategory) {
		this.idRiskCategory = idRiskCategory;
	}
}
