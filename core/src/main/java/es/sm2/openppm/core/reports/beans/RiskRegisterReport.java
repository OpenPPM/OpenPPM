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
 * File: RiskRegisterReport.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.reports.beans;

/**
 * Created by jaume.sastre on 19/11/2014.
 */
public class RiskRegisterReport {

    private String indexRisk;
    private String riskCode;
    private String riskName;
    private String dateRaised;
    private String dateMaterialization;
    private String status;
    private String responseDescription;

    public RiskRegisterReport( String indexRisk, String riskCode, String riskName, String dateRaised,
                               String dateMaterialization, String status, String responseDescription){

        this.indexRisk = indexRisk;
        this.riskCode = riskCode;
        this.riskName = riskName;
        this.dateRaised = dateRaised;
        this.dateMaterialization = dateMaterialization;
        this.status = status;
        this.responseDescription = responseDescription;

    }

    /**
     * @return the indexRisk
     */
    public String getIndexRisk() {
        return indexRisk;
    }

    /**
     * @param indexRisk the indexRisk to set
     */
    public void setIndexRisk(String indexRisk) {
        this.indexRisk = indexRisk;
    }

    /**
     * @return the riskCode
     */
    public String getRiskCode() {
        return riskCode;
    }

    /**
     * @param riskCode the riskCode to set
     */
    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    /**
     * @return the riskName
     */
    public String getRiskName() {
        return riskName;
    }

    /**
     * @param riskName the riskCode to set
     */
    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }

    /**
     * @return the dateRaised
     */
    public String getDateRaised() {
        return dateRaised;
    }

    /**
     * @param dateRaised the dateRaised to set
     */
    public void setDateRaised(String dateRaised) {
        this.dateRaised = dateRaised;
    }

    /**
     * @return the dateMaterialization
     */
    public String getDateMaterialization() {
        return dateMaterialization;
    }

    /**
     * @param dateMaterialization the dateMaterialization to set
     */
    public void setDateMaterialization(String dateMaterialization) {
        this.dateMaterialization = dateMaterialization;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the responseDescription
     */
    public String getResponseDescription() {
        return responseDescription;
    }

    /**
     * @param responseDescription the responseDescription to set
     */
    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }



}
