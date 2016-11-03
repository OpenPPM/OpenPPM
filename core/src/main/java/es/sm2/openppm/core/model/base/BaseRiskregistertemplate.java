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
 * File: BaseRiskregistertemplate.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Riskcategories;
import es.sm2.openppm.core.model.impl.Riskcategory;
import es.sm2.openppm.core.model.impl.Riskregistertemplate;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Riskregistertemplate
 * @see es.sm2.openppm.core.model.base.Riskregistertemplate
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Riskregistertemplate
 */
public class BaseRiskregistertemplate  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "riskregistertemplate";
	
	public static final String IDRISK = "idRisk";
	public static final String RISKCATEGORIES = "riskcategories";
	public static final String RISKCATEGORY = "riskcategory";
	public static final String COMPANY = "company";
	public static final String RISKCODE = "riskCode";
	public static final String RISKNAME = "riskName";
	public static final String OWNER = "owner";
	public static final String POTENTIALCOST = "potentialCost";
	public static final String POTENTIALDELAY = "potentialDelay";
	public static final String RISKTRIGGER = "riskTrigger";
	public static final String DESCRIPTION = "description";
	public static final String PROBABILITY = "probability";
	public static final String IMPACT = "impact";
	public static final String MATERIALIZED = "materialized";
	public static final String MITIGATIONACTIONSREQUIRED = "mitigationActionsRequired";
	public static final String CONTINGENCYACTIONSREQUIRED = "contingencyActionsRequired";
	public static final String ACTUALMATERIALIZATIONCOST = "actualMaterializationCost";
	public static final String ACTUALMATERIALIZATIONDELAY = "actualMaterializationDelay";
	public static final String FINALCOMMENTS = "finalComments";
	public static final String RISKDOC = "riskDoc";
	public static final String RISKTYPE = "riskType";
	public static final String PLANNEDMITIGATIONCOST = "plannedMitigationCost";
	public static final String PLANNEDCONTINGENCYCOST = "plannedContingencyCost";
	public static final String CLOSED = "closed";
	public static final String STATUS = "status";
	public static final String RESPONSEDESCRIPTION = "responseDescription";
	public static final String RESIDUALRISK = "residualRisk";
	public static final String RESIDUALCOST = "residualCost";

     private Integer idRisk;
     private Riskcategories riskcategories;
     private Riskcategory riskcategory;
     private Company company;
     private String riskCode;
     private String riskName;
     private String owner;
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
     private String status;
     private String responseDescription;
     private String residualRisk;
     private Double residualCost;

    public BaseRiskregistertemplate() {
    }
    
    public BaseRiskregistertemplate(Integer idRisk) {
    	this.idRisk = idRisk;
    }
   
    public Integer getIdRisk() {
        return this.idRisk;
    }
    
    public void setIdRisk(Integer idRisk) {
        this.idRisk = idRisk;
    }
    public Riskcategories getRiskcategories() {
        return this.riskcategories;
    }
    
    public void setRiskcategories(Riskcategories riskcategories) {
        this.riskcategories = riskcategories;
    }
    public Riskcategory getRiskcategory() {
        return this.riskcategory;
    }
    
    public void setRiskcategory(Riskcategory riskcategory) {
        this.riskcategory = riskcategory;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
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


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Riskregistertemplate) )  { result = false; }
		 else if (other != null) {
		 	Riskregistertemplate castOther = (Riskregistertemplate) other;
			if (castOther.getIdRisk().equals(this.getIdRisk())) { result = true; }
         }
		 return result;
   }


}


