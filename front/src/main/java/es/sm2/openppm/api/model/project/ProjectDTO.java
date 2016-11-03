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
 * Module: front
 * File: ProjectDTO.java
 * Create User: javier.hernandez
 * Create Date: 31/07/2015 12:05:21
 */

package es.sm2.openppm.api.model.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.sm2.openppm.api.model.common.EntityDTO;

import java.util.Date;

/**
 * Created by javier.hernandez on 31/07/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDTO extends EntityDTO {

    private ProgramDTO program;
    private EntityDTO customer;
    private EntityDTO projectManager;
    private EntityDTO investmentManager;
    private EntityDTO performingorg;
    private EntityDTO functionalManager;
    private EntityDTO sponsor;
    private EntityDTO geography;
    private EntityDTO contracttype;
    private EntityDTO category;
    private EntityDTO stagegate;
    private EntityDTO classificationlevel;
    private EntityDTO projectcalendar;

    private String status;
    private Character risk;
    private Integer priority;
    private Double bac;
    private Double netIncome;
    private Double tcv;
    private Date initDate;
    private Date endDate;
    private Integer duration;
    private Integer effort;
    private Date plannedFinishDate;
    private Date planDate;
    private Date execDate;
    private Date plannedInitDate;
    private String closeComments;
    private String closeStakeholderComments;
    private String closeUrlLessons;
    private String closeLessons;
    private Boolean internalProject;
    private String projectDoc;
    private Integer budgetYear;
    private String chartLabel;
    private Integer probability;
    private Boolean isGeoSelling;
    private String investmentStatus;
    private Boolean sended;
    private Integer numCompetitors;
    private Integer finalPosition;
    private String clientComments;
    private String canceledComments;
    private String comments;
    private String linkDoc;
    private String accountingCode;
    private Date statusDate;
    private Double lowerThreshold;
    private Double upperThreshold;
    private String linkComment;
    private String scopeStatement;
    private String hdDescription;
    private Character rag;
    private Double currencyOptional1;
    private Double currencyOptional2;
    private Double currencyOptional3;
    private Double currencyOptional4;
    private Date startDate;
    private Date finishDate;
    private Double poc;
    private String kpiStatus;
    private Boolean disable;
    private Integer riskRatingAdjustament;
    private Integer strategicAdjustament;
    private Date calculatedPlanStartDate;
    private Date calculatedPlanFinishDate;
    private Boolean useRiskAdjust;
    private Boolean useStrategicAdjust;
    private Date archiveDate;

    /**
     * Getter for property 'category'.
     *
     * @return Value for property 'category'.
     */
    public EntityDTO getCategory() {

        return category;
    }

    /**
     * Setter for property 'category'.
     *
     * @param category Value to set for property 'category'.
     */
    public void setCategory(EntityDTO category) {

        this.category = category;
    }

    /**
     * Getter for property 'stagegate'.
     *
     * @return Value for property 'stagegate'.
     */
    public EntityDTO getStagegate() {

        return stagegate;
    }

    /**
     * Setter for property 'stagegate'.
     *
     * @param stagegate Value to set for property 'stagegate'.
     */
    public void setStagegate(EntityDTO stagegate) {

        this.stagegate = stagegate;
    }

    /**
     * Getter for property 'classificationlevel'.
     *
     * @return Value for property 'classificationlevel'.
     */
    public EntityDTO getClassificationlevel() {

        return classificationlevel;
    }

    /**
     * Setter for property 'classificationlevel'.
     *
     * @param classificationlevel Value to set for property 'classificationlevel'.
     */
    public void setClassificationlevel(EntityDTO classificationlevel) {

        this.classificationlevel = classificationlevel;
    }

    /**
     * Getter for property 'projectcalendar'.
     *
     * @return Value for property 'projectcalendar'.
     */
    public EntityDTO getProjectcalendar() {

        return projectcalendar;
    }

    /**
     * Setter for property 'projectcalendar'.
     *
     * @param projectcalendar Value to set for property 'projectcalendar'.
     */
    public void setProjectcalendar(EntityDTO projectcalendar) {

        this.projectcalendar = projectcalendar;
    }

    /**
     * Getter for property 'investmentManager'.
     *
     * @return Value for property 'investmentManager'.
     */
    public EntityDTO getInvestmentManager() {

        return investmentManager;
    }

    /**
     * Setter for property 'investmentManager'.
     *
     * @param investmentManager Value to set for property 'investmentManager'.
     */
    public void setInvestmentManager(EntityDTO investmentManager) {

        this.investmentManager = investmentManager;
    }

    /**
     * Getter for property 'program'.
     *
     * @return Value for property 'program'.
     */
    public ProgramDTO getProgram() {
        return program;
    }

    /**
     * Setter for property 'program'.
     *
     * @param program Value to set for property 'program'.
     */
    public void setProgram(ProgramDTO program) {
        this.program = program;
    }

    /**
     * Getter for property 'customer'.
     *
     * @return Value for property 'customer'.
     */
    public EntityDTO getCustomer() {
        return customer;
    }

    /**
     * Setter for property 'customer'.
     *
     * @param customer Value to set for property 'customer'.
     */
    public void setCustomer(EntityDTO customer) {
        this.customer = customer;
    }

    /**
     * Getter for property 'projectManager'.
     *
     * @return Value for property 'projectManager'.
     */
    public EntityDTO getProjectManager() {
        return projectManager;
    }

    /**
     * Setter for property 'projectManager'.
     *
     * @param projectManager Value to set for property 'projectManager'.
     */
    public void setProjectManager(EntityDTO projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * Getter for property 'functionalManager'.
     *
     * @return Value for property 'functionalManager'.
     */
    public EntityDTO getFunctionalManager() {
        return functionalManager;
    }

    /**
     * Setter for property 'functionalManager'.
     *
     * @param functionalManager Value to set for property 'functionalManager'.
     */
    public void setFunctionalManager(EntityDTO functionalManager) {
        this.functionalManager = functionalManager;
    }

    /**
     * Getter for property 'sponsor'.
     *
     * @return Value for property 'sponsor'.
     */
    public EntityDTO getSponsor() {
        return sponsor;
    }

    /**
     * Setter for property 'sponsor'.
     *
     * @param sponsor Value to set for property 'sponsor'.
     */
    public void setSponsor(EntityDTO sponsor) {
        this.sponsor = sponsor;
    }

    /**
     * Getter for property 'geography'.
     *
     * @return Value for property 'geography'.
     */
    public EntityDTO getGeography() {
        return geography;
    }

    /**
     * Setter for property 'geography'.
     *
     * @param geography Value to set for property 'geography'.
     */
    public void setGeography(EntityDTO geography) {
        this.geography = geography;
    }

    /**
     * Getter for property 'contracttype'.
     *
     * @return Value for property 'contracttype'.
     */
    public EntityDTO getContracttype() {
        return contracttype;
    }

    /**
     * Setter for property 'contracttype'.
     *
     * @param contracttype Value to set for property 'contracttype'.
     */
    public void setContracttype(EntityDTO contracttype) {
        this.contracttype = contracttype;
    }

    /**
     * Getter for property 'performingorg'.
     *
     * @return Value for property 'performingorg'.
     */
    public EntityDTO getPerformingorg() {
        return performingorg;
    }

    /**
     * Setter for property 'performingorg'.
     *
     * @param performingorg Value to set for property 'performingorg'.
     */
    public void setPerformingorg(EntityDTO performingorg) {
        this.performingorg = performingorg;
    }

    /**
     * Getter for property 'status'.
     *
     * @return Value for property 'status'.
     */
    public String getStatus() {

        return status;
    }

    /**
     * Setter for property 'status'.
     *
     * @param status Value to set for property 'status'.
     */
    public void setStatus(String status) {

        this.status = status;
    }

    /**
     * Getter for property 'risk'.
     *
     * @return Value for property 'risk'.
     */
    public Character getRisk() {

        return risk;
    }

    /**
     * Setter for property 'risk'.
     *
     * @param risk Value to set for property 'risk'.
     */
    public void setRisk(Character risk) {

        this.risk = risk;
    }

    /**
     * Getter for property 'priority'.
     *
     * @return Value for property 'priority'.
     */
    public Integer getPriority() {

        return priority;
    }

    /**
     * Setter for property 'priority'.
     *
     * @param priority Value to set for property 'priority'.
     */
    public void setPriority(Integer priority) {

        this.priority = priority;
    }

    /**
     * Getter for property 'bac'.
     *
     * @return Value for property 'bac'.
     */
    public Double getBac() {

        return bac;
    }

    /**
     * Setter for property 'bac'.
     *
     * @param bac Value to set for property 'bac'.
     */
    public void setBac(Double bac) {

        this.bac = bac;
    }

    /**
     * Getter for property 'netIncome'.
     *
     * @return Value for property 'netIncome'.
     */
    public Double getNetIncome() {

        return netIncome;
    }

    /**
     * Setter for property 'netIncome'.
     *
     * @param netIncome Value to set for property 'netIncome'.
     */
    public void setNetIncome(Double netIncome) {

        this.netIncome = netIncome;
    }

    /**
     * Getter for property 'tcv'.
     *
     * @return Value for property 'tcv'.
     */
    public Double getTcv() {

        return tcv;
    }

    /**
     * Setter for property 'tcv'.
     *
     * @param tcv Value to set for property 'tcv'.
     */
    public void setTcv(Double tcv) {

        this.tcv = tcv;
    }

    /**
     * Getter for property 'initDate'.
     *
     * @return Value for property 'initDate'.
     */
    public Date getInitDate() {

        return initDate;
    }

    /**
     * Setter for property 'initDate'.
     *
     * @param initDate Value to set for property 'initDate'.
     */
    public void setInitDate(Date initDate) {

        this.initDate = initDate;
    }

    /**
     * Getter for property 'endDate'.
     *
     * @return Value for property 'endDate'.
     */
    public Date getEndDate() {

        return endDate;
    }

    /**
     * Setter for property 'endDate'.
     *
     * @param endDate Value to set for property 'endDate'.
     */
    public void setEndDate(Date endDate) {

        this.endDate = endDate;
    }

    /**
     * Getter for property 'duration'.
     *
     * @return Value for property 'duration'.
     */
    public Integer getDuration() {

        return duration;
    }

    /**
     * Setter for property 'duration'.
     *
     * @param duration Value to set for property 'duration'.
     */
    public void setDuration(Integer duration) {

        this.duration = duration;
    }

    /**
     * Getter for property 'effort'.
     *
     * @return Value for property 'effort'.
     */
    public Integer getEffort() {

        return effort;
    }

    /**
     * Setter for property 'effort'.
     *
     * @param effort Value to set for property 'effort'.
     */
    public void setEffort(Integer effort) {

        this.effort = effort;
    }

    /**
     * Getter for property 'plannedFinishDate'.
     *
     * @return Value for property 'plannedFinishDate'.
     */
    public Date getPlannedFinishDate() {

        return plannedFinishDate;
    }

    /**
     * Setter for property 'plannedFinishDate'.
     *
     * @param plannedFinishDate Value to set for property 'plannedFinishDate'.
     */
    public void setPlannedFinishDate(Date plannedFinishDate) {

        this.plannedFinishDate = plannedFinishDate;
    }

    /**
     * Getter for property 'planDate'.
     *
     * @return Value for property 'planDate'.
     */
    public Date getPlanDate() {

        return planDate;
    }

    /**
     * Setter for property 'planDate'.
     *
     * @param planDate Value to set for property 'planDate'.
     */
    public void setPlanDate(Date planDate) {

        this.planDate = planDate;
    }

    /**
     * Getter for property 'execDate'.
     *
     * @return Value for property 'execDate'.
     */
    public Date getExecDate() {

        return execDate;
    }

    /**
     * Setter for property 'execDate'.
     *
     * @param execDate Value to set for property 'execDate'.
     */
    public void setExecDate(Date execDate) {

        this.execDate = execDate;
    }

    /**
     * Getter for property 'plannedInitDate'.
     *
     * @return Value for property 'plannedInitDate'.
     */
    public Date getPlannedInitDate() {

        return plannedInitDate;
    }

    /**
     * Setter for property 'plannedInitDate'.
     *
     * @param plannedInitDate Value to set for property 'plannedInitDate'.
     */
    public void setPlannedInitDate(Date plannedInitDate) {

        this.plannedInitDate = plannedInitDate;
    }

    /**
     * Getter for property 'closeComments'.
     *
     * @return Value for property 'closeComments'.
     */
    public String getCloseComments() {

        return closeComments;
    }

    /**
     * Setter for property 'closeComments'.
     *
     * @param closeComments Value to set for property 'closeComments'.
     */
    public void setCloseComments(String closeComments) {

        this.closeComments = closeComments;
    }

    /**
     * Getter for property 'closeStakeholderComments'.
     *
     * @return Value for property 'closeStakeholderComments'.
     */
    public String getCloseStakeholderComments() {

        return closeStakeholderComments;
    }

    /**
     * Setter for property 'closeStakeholderComments'.
     *
     * @param closeStakeholderComments Value to set for property 'closeStakeholderComments'.
     */
    public void setCloseStakeholderComments(String closeStakeholderComments) {

        this.closeStakeholderComments = closeStakeholderComments;
    }

    /**
     * Getter for property 'closeUrlLessons'.
     *
     * @return Value for property 'closeUrlLessons'.
     */
    public String getCloseUrlLessons() {

        return closeUrlLessons;
    }

    /**
     * Setter for property 'closeUrlLessons'.
     *
     * @param closeUrlLessons Value to set for property 'closeUrlLessons'.
     */
    public void setCloseUrlLessons(String closeUrlLessons) {

        this.closeUrlLessons = closeUrlLessons;
    }

    /**
     * Getter for property 'closeLessons'.
     *
     * @return Value for property 'closeLessons'.
     */
    public String getCloseLessons() {

        return closeLessons;
    }

    /**
     * Setter for property 'closeLessons'.
     *
     * @param closeLessons Value to set for property 'closeLessons'.
     */
    public void setCloseLessons(String closeLessons) {

        this.closeLessons = closeLessons;
    }

    /**
     * Getter for property 'internalProject'.
     *
     * @return Value for property 'internalProject'.
     */
    public Boolean getInternalProject() {

        return internalProject;
    }

    /**
     * Setter for property 'internalProject'.
     *
     * @param internalProject Value to set for property 'internalProject'.
     */
    public void setInternalProject(Boolean internalProject) {

        this.internalProject = internalProject;
    }

    /**
     * Getter for property 'projectDoc'.
     *
     * @return Value for property 'projectDoc'.
     */
    public String getProjectDoc() {

        return projectDoc;
    }

    /**
     * Setter for property 'projectDoc'.
     *
     * @param projectDoc Value to set for property 'projectDoc'.
     */
    public void setProjectDoc(String projectDoc) {

        this.projectDoc = projectDoc;
    }

    /**
     * Getter for property 'budgetYear'.
     *
     * @return Value for property 'budgetYear'.
     */
    public Integer getBudgetYear() {

        return budgetYear;
    }

    /**
     * Setter for property 'budgetYear'.
     *
     * @param budgetYear Value to set for property 'budgetYear'.
     */
    public void setBudgetYear(Integer budgetYear) {

        this.budgetYear = budgetYear;
    }

    /**
     * Getter for property 'chartLabel'.
     *
     * @return Value for property 'chartLabel'.
     */
    public String getChartLabel() {

        return chartLabel;
    }

    /**
     * Setter for property 'chartLabel'.
     *
     * @param chartLabel Value to set for property 'chartLabel'.
     */
    public void setChartLabel(String chartLabel) {

        this.chartLabel = chartLabel;
    }

    /**
     * Getter for property 'probability'.
     *
     * @return Value for property 'probability'.
     */
    public Integer getProbability() {

        return probability;
    }

    /**
     * Setter for property 'probability'.
     *
     * @param probability Value to set for property 'probability'.
     */
    public void setProbability(Integer probability) {

        this.probability = probability;
    }

    /**
     * Getter for property 'isGeoSelling'.
     *
     * @return Value for property 'isGeoSelling'.
     */
    public Boolean getIsGeoSelling() {

        return isGeoSelling;
    }

    /**
     * Setter for property 'isGeoSelling'.
     *
     * @param isGeoSelling Value to set for property 'isGeoSelling'.
     */
    public void setIsGeoSelling(Boolean isGeoSelling) {

        this.isGeoSelling = isGeoSelling;
    }

    /**
     * Getter for property 'investmentStatus'.
     *
     * @return Value for property 'investmentStatus'.
     */
    public String getInvestmentStatus() {

        return investmentStatus;
    }

    /**
     * Setter for property 'investmentStatus'.
     *
     * @param investmentStatus Value to set for property 'investmentStatus'.
     */
    public void setInvestmentStatus(String investmentStatus) {

        this.investmentStatus = investmentStatus;
    }

    /**
     * Getter for property 'sended'.
     *
     * @return Value for property 'sended'.
     */
    public Boolean getSended() {

        return sended;
    }

    /**
     * Setter for property 'sended'.
     *
     * @param sended Value to set for property 'sended'.
     */
    public void setSended(Boolean sended) {

        this.sended = sended;
    }

    /**
     * Getter for property 'numCompetitors'.
     *
     * @return Value for property 'numCompetitors'.
     */
    public Integer getNumCompetitors() {

        return numCompetitors;
    }

    /**
     * Setter for property 'numCompetitors'.
     *
     * @param numCompetitors Value to set for property 'numCompetitors'.
     */
    public void setNumCompetitors(Integer numCompetitors) {

        this.numCompetitors = numCompetitors;
    }

    /**
     * Getter for property 'finalPosition'.
     *
     * @return Value for property 'finalPosition'.
     */
    public Integer getFinalPosition() {

        return finalPosition;
    }

    /**
     * Setter for property 'finalPosition'.
     *
     * @param finalPosition Value to set for property 'finalPosition'.
     */
    public void setFinalPosition(Integer finalPosition) {

        this.finalPosition = finalPosition;
    }

    /**
     * Getter for property 'clientComments'.
     *
     * @return Value for property 'clientComments'.
     */
    public String getClientComments() {

        return clientComments;
    }

    /**
     * Setter for property 'clientComments'.
     *
     * @param clientComments Value to set for property 'clientComments'.
     */
    public void setClientComments(String clientComments) {

        this.clientComments = clientComments;
    }

    /**
     * Getter for property 'canceledComments'.
     *
     * @return Value for property 'canceledComments'.
     */
    public String getCanceledComments() {

        return canceledComments;
    }

    /**
     * Setter for property 'canceledComments'.
     *
     * @param canceledComments Value to set for property 'canceledComments'.
     */
    public void setCanceledComments(String canceledComments) {

        this.canceledComments = canceledComments;
    }

    /**
     * Getter for property 'comments'.
     *
     * @return Value for property 'comments'.
     */
    public String getComments() {

        return comments;
    }

    /**
     * Setter for property 'comments'.
     *
     * @param comments Value to set for property 'comments'.
     */
    public void setComments(String comments) {

        this.comments = comments;
    }

    /**
     * Getter for property 'linkDoc'.
     *
     * @return Value for property 'linkDoc'.
     */
    public String getLinkDoc() {

        return linkDoc;
    }

    /**
     * Setter for property 'linkDoc'.
     *
     * @param linkDoc Value to set for property 'linkDoc'.
     */
    public void setLinkDoc(String linkDoc) {

        this.linkDoc = linkDoc;
    }

    /**
     * Getter for property 'accountingCode'.
     *
     * @return Value for property 'accountingCode'.
     */
    public String getAccountingCode() {

        return accountingCode;
    }

    /**
     * Setter for property 'accountingCode'.
     *
     * @param accountingCode Value to set for property 'accountingCode'.
     */
    public void setAccountingCode(String accountingCode) {

        this.accountingCode = accountingCode;
    }

    /**
     * Getter for property 'statusDate'.
     *
     * @return Value for property 'statusDate'.
     */
    public Date getStatusDate() {

        return statusDate;
    }

    /**
     * Setter for property 'statusDate'.
     *
     * @param statusDate Value to set for property 'statusDate'.
     */
    public void setStatusDate(Date statusDate) {

        this.statusDate = statusDate;
    }

    /**
     * Getter for property 'lowerThreshold'.
     *
     * @return Value for property 'lowerThreshold'.
     */
    public Double getLowerThreshold() {

        return lowerThreshold;
    }

    /**
     * Setter for property 'lowerThreshold'.
     *
     * @param lowerThreshold Value to set for property 'lowerThreshold'.
     */
    public void setLowerThreshold(Double lowerThreshold) {

        this.lowerThreshold = lowerThreshold;
    }

    /**
     * Getter for property 'upperThreshold'.
     *
     * @return Value for property 'upperThreshold'.
     */
    public Double getUpperThreshold() {

        return upperThreshold;
    }

    /**
     * Setter for property 'upperThreshold'.
     *
     * @param upperThreshold Value to set for property 'upperThreshold'.
     */
    public void setUpperThreshold(Double upperThreshold) {

        this.upperThreshold = upperThreshold;
    }

    /**
     * Getter for property 'linkComment'.
     *
     * @return Value for property 'linkComment'.
     */
    public String getLinkComment() {

        return linkComment;
    }

    /**
     * Setter for property 'linkComment'.
     *
     * @param linkComment Value to set for property 'linkComment'.
     */
    public void setLinkComment(String linkComment) {

        this.linkComment = linkComment;
    }

    /**
     * Getter for property 'scopeStatement'.
     *
     * @return Value for property 'scopeStatement'.
     */
    public String getScopeStatement() {

        return scopeStatement;
    }

    /**
     * Setter for property 'scopeStatement'.
     *
     * @param scopeStatement Value to set for property 'scopeStatement'.
     */
    public void setScopeStatement(String scopeStatement) {

        this.scopeStatement = scopeStatement;
    }

    /**
     * Getter for property 'hdDescription'.
     *
     * @return Value for property 'hdDescription'.
     */
    public String getHdDescription() {

        return hdDescription;
    }

    /**
     * Setter for property 'hdDescription'.
     *
     * @param hdDescription Value to set for property 'hdDescription'.
     */
    public void setHdDescription(String hdDescription) {

        this.hdDescription = hdDescription;
    }

    /**
     * Getter for property 'rag'.
     *
     * @return Value for property 'rag'.
     */
    public Character getRag() {

        return rag;
    }

    /**
     * Setter for property 'rag'.
     *
     * @param rag Value to set for property 'rag'.
     */
    public void setRag(Character rag) {

        this.rag = rag;
    }

    /**
     * Getter for property 'currencyOptional1'.
     *
     * @return Value for property 'currencyOptional1'.
     */
    public Double getCurrencyOptional1() {

        return currencyOptional1;
    }

    /**
     * Setter for property 'currencyOptional1'.
     *
     * @param currencyOptional1 Value to set for property 'currencyOptional1'.
     */
    public void setCurrencyOptional1(Double currencyOptional1) {

        this.currencyOptional1 = currencyOptional1;
    }

    /**
     * Getter for property 'currencyOptional2'.
     *
     * @return Value for property 'currencyOptional2'.
     */
    public Double getCurrencyOptional2() {

        return currencyOptional2;
    }

    /**
     * Setter for property 'currencyOptional2'.
     *
     * @param currencyOptional2 Value to set for property 'currencyOptional2'.
     */
    public void setCurrencyOptional2(Double currencyOptional2) {

        this.currencyOptional2 = currencyOptional2;
    }

    /**
     * Getter for property 'currencyOptional3'.
     *
     * @return Value for property 'currencyOptional3'.
     */
    public Double getCurrencyOptional3() {

        return currencyOptional3;
    }

    /**
     * Setter for property 'currencyOptional3'.
     *
     * @param currencyOptional3 Value to set for property 'currencyOptional3'.
     */
    public void setCurrencyOptional3(Double currencyOptional3) {

        this.currencyOptional3 = currencyOptional3;
    }

    /**
     * Getter for property 'currencyOptional4'.
     *
     * @return Value for property 'currencyOptional4'.
     */
    public Double getCurrencyOptional4() {

        return currencyOptional4;
    }

    /**
     * Setter for property 'currencyOptional4'.
     *
     * @param currencyOptional4 Value to set for property 'currencyOptional4'.
     */
    public void setCurrencyOptional4(Double currencyOptional4) {

        this.currencyOptional4 = currencyOptional4;
    }

    /**
     * Getter for property 'startDate'.
     *
     * @return Value for property 'startDate'.
     */
    public Date getStartDate() {

        return startDate;
    }

    /**
     * Setter for property 'startDate'.
     *
     * @param startDate Value to set for property 'startDate'.
     */
    public void setStartDate(Date startDate) {

        this.startDate = startDate;
    }

    /**
     * Getter for property 'finishDate'.
     *
     * @return Value for property 'finishDate'.
     */
    public Date getFinishDate() {

        return finishDate;
    }

    /**
     * Setter for property 'finishDate'.
     *
     * @param finishDate Value to set for property 'finishDate'.
     */
    public void setFinishDate(Date finishDate) {

        this.finishDate = finishDate;
    }

    /**
     * Getter for property 'poc'.
     *
     * @return Value for property 'poc'.
     */
    public Double getPoc() {

        return poc;
    }

    /**
     * Setter for property 'poc'.
     *
     * @param poc Value to set for property 'poc'.
     */
    public void setPoc(Double poc) {

        this.poc = poc;
    }

    /**
     * Getter for property 'kpiStatus'.
     *
     * @return Value for property 'kpiStatus'.
     */
    public String getKpiStatus() {

        return kpiStatus;
    }

    /**
     * Setter for property 'kpiStatus'.
     *
     * @param kpiStatus Value to set for property 'kpiStatus'.
     */
    public void setKpiStatus(String kpiStatus) {

        this.kpiStatus = kpiStatus;
    }

    /**
     * Getter for property 'disable'.
     *
     * @return Value for property 'disable'.
     */
    public Boolean getDisable() {

        return disable;
    }

    /**
     * Setter for property 'disable'.
     *
     * @param disable Value to set for property 'disable'.
     */
    public void setDisable(Boolean disable) {

        this.disable = disable;
    }

    /**
     * Getter for property 'riskRatingAdjustament'.
     *
     * @return Value for property 'riskRatingAdjustament'.
     */
    public Integer getRiskRatingAdjustament() {

        return riskRatingAdjustament;
    }

    /**
     * Setter for property 'riskRatingAdjustament'.
     *
     * @param riskRatingAdjustament Value to set for property 'riskRatingAdjustament'.
     */
    public void setRiskRatingAdjustament(Integer riskRatingAdjustament) {

        this.riskRatingAdjustament = riskRatingAdjustament;
    }

    /**
     * Getter for property 'strategicAdjustament'.
     *
     * @return Value for property 'strategicAdjustament'.
     */
    public Integer getStrategicAdjustament() {

        return strategicAdjustament;
    }

    /**
     * Setter for property 'strategicAdjustament'.
     *
     * @param strategicAdjustament Value to set for property 'strategicAdjustament'.
     */
    public void setStrategicAdjustament(Integer strategicAdjustament) {

        this.strategicAdjustament = strategicAdjustament;
    }

    /**
     * Getter for property 'calculatedPlanStartDate'.
     *
     * @return Value for property 'calculatedPlanStartDate'.
     */
    public Date getCalculatedPlanStartDate() {

        return calculatedPlanStartDate;
    }

    /**
     * Setter for property 'calculatedPlanStartDate'.
     *
     * @param calculatedPlanStartDate Value to set for property 'calculatedPlanStartDate'.
     */
    public void setCalculatedPlanStartDate(Date calculatedPlanStartDate) {

        this.calculatedPlanStartDate = calculatedPlanStartDate;
    }

    /**
     * Getter for property 'calculatedPlanFinishDate'.
     *
     * @return Value for property 'calculatedPlanFinishDate'.
     */
    public Date getCalculatedPlanFinishDate() {

        return calculatedPlanFinishDate;
    }

    /**
     * Setter for property 'calculatedPlanFinishDate'.
     *
     * @param calculatedPlanFinishDate Value to set for property 'calculatedPlanFinishDate'.
     */
    public void setCalculatedPlanFinishDate(Date calculatedPlanFinishDate) {

        this.calculatedPlanFinishDate = calculatedPlanFinishDate;
    }

    /**
     * Getter for property 'useRiskAdjust'.
     *
     * @return Value for property 'useRiskAdjust'.
     */
    public Boolean getUseRiskAdjust() {

        return useRiskAdjust;
    }

    /**
     * Setter for property 'useRiskAdjust'.
     *
     * @param useRiskAdjust Value to set for property 'useRiskAdjust'.
     */
    public void setUseRiskAdjust(Boolean useRiskAdjust) {

        this.useRiskAdjust = useRiskAdjust;
    }

    /**
     * Getter for property 'useStrategicAdjust'.
     *
     * @return Value for property 'useStrategicAdjust'.
     */
    public Boolean getUseStrategicAdjust() {

        return useStrategicAdjust;
    }

    /**
     * Setter for property 'useStrategicAdjust'.
     *
     * @param useStrategicAdjust Value to set for property 'useStrategicAdjust'.
     */
    public void setUseStrategicAdjust(Boolean useStrategicAdjust) {

        this.useStrategicAdjust = useStrategicAdjust;
    }

    /**
     * Getter for property 'archiveDate'.
     *
     * @return Value for property 'archiveDate'.
     */
    public Date getArchiveDate() {

        return archiveDate;
    }

    /**
     * Setter for property 'archiveDate'.
     *
     * @param archiveDate Value to set for property 'archiveDate'.
     */
    public void setArchiveDate(Date archiveDate) {

        this.archiveDate = archiveDate;
    }
}
