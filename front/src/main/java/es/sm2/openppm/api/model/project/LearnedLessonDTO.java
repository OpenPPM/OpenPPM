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
 * File: LearnedLessonDTO.java
 * Create User: jordi.ripoll
 * Create Date: 03/08/2015 10:02:50
 */

package es.sm2.openppm.api.model.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.sm2.openppm.api.model.common.EntityDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearnedLessonDTO extends EntityDTO {

    private ProjectDTO project;
    private Date dateRaised;
    private EntityDTO owner;
    private Double impactTime;
    private Double impactCost;
    private String impactSatisfaction;
    private EntityDTO profile;
    private EntityDTO program;
    private EntityDTO customer;
    private EntityDTO seller;
    private EntityDTO contractType;
    private EntityDTO geography;
    private EntityDTO fundingSource;
    private String type;
    private EntityDTO knowledgeArea;
    private String processGroup;
    private Integer ranking;
    private List<ActionLessonDTO> actions;
    private List<RecommendationLessonDTO> recommendations;

    /**
     * Getter for property 'owner'.
     *
     * @return Value for property 'owner'.
     */
    public EntityDTO getOwner() {
        return owner;
    }

    /**
     * Setter for property 'owner'.
     *
     * @param owner Value to set for property 'owner'.
     */
    public void setOwner(EntityDTO owner) {
        this.owner = owner;
    }

    /**
     * Getter for property 'dateRaised'.
     *
     * @return Value for property 'dateRaised'.
     */
    public Date getDateRaised() {
        return dateRaised;
    }

    /**
     * Setter for property 'dateRaised'.
     *
     * @param dateRaised Value to set for property 'dateRaised'.
     */
    public void setDateRaised(Date dateRaised) {
        this.dateRaised = dateRaised;
    }

    /**
     * Getter for property 'impactTime'.
     *
     * @return Value for property 'impactTime'.
     */
    public Double getImpactTime() {
        return impactTime;
    }

    /**
     * Setter for property 'impactTime'.
     *
     * @param impactTime Value to set for property 'impactTime'.
     */
    public void setImpactTime(Double impactTime) {
        this.impactTime = impactTime;
    }

    /**
     * Getter for property 'impactCost'.
     *
     * @return Value for property 'impactCost'.
     */
    public Double getImpactCost() {
        return impactCost;
    }

    /**
     * Setter for property 'impactCost'.
     *
     * @param impactCost Value to set for property 'impactCost'.
     */
    public void setImpactCost(Double impactCost) {
        this.impactCost = impactCost;
    }

    /**
     * Getter for property 'impactSatisfaction'.
     *
     * @return Value for property 'impactSatisfaction'.
     */
    public String getImpactSatisfaction() {
        return impactSatisfaction;
    }

    /**
     * Setter for property 'impactSatisfaction'.
     *
     * @param impactSatisfaction Value to set for property 'impactSatisfaction'.
     */
    public void setImpactSatisfaction(String impactSatisfaction) {
        this.impactSatisfaction = impactSatisfaction;
    }

    /**
     * Getter for property 'profile'.
     *
     * @return Value for property 'profile'.
     */
    public EntityDTO getProfile() {
        return profile;
    }

    /**
     * Setter for property 'profile'.
     *
     * @param profile Value to set for property 'profile'.
     */
    public void setProfile(EntityDTO profile) {
        this.profile = profile;
    }

    /**
     * Getter for property 'program'.
     *
     * @return Value for property 'program'.
     */
    public EntityDTO getProgram() {
        return program;
    }

    /**
     * Setter for property 'program'.
     *
     * @param program Value to set for property 'program'.
     */
    public void setProgram(EntityDTO program) {
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
     * Getter for property 'seller'.
     *
     * @return Value for property 'seller'.
     */
    public EntityDTO getSeller() {
        return seller;
    }

    /**
     * Setter for property 'seller'.
     *
     * @param seller Value to set for property 'seller'.
     */
    public void setSeller(EntityDTO seller) {
        this.seller = seller;
    }

    /**
     * Getter for property 'contractType'.
     *
     * @return Value for property 'contractType'.
     */
    public EntityDTO getContractType() {
        return contractType;
    }

    /**
     * Setter for property 'contractType'.
     *
     * @param contractType Value to set for property 'contractType'.
     */
    public void setContractType(EntityDTO contractType) {
        this.contractType = contractType;
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
     * Getter for property 'fundingSource'.
     *
     * @return Value for property 'fundingSource'.
     */
    public EntityDTO getFundingSource() {
        return fundingSource;
    }

    /**
     * Setter for property 'fundingSource'.
     *
     * @param fundingSource Value to set for property 'fundingSource'.
     */
    public void setFundingSource(EntityDTO fundingSource) {
        this.fundingSource = fundingSource;
    }

    /**
     * Getter for property 'actions'.
     *
     * @return Value for property 'actions'.
     */
    public List<ActionLessonDTO> getActions() {
        return actions;
    }

    /**
     * Setter for property 'actions'.
     *
     * @param actions Value to set for property 'actions'.
     */
    public void setActions(List<ActionLessonDTO> actions) {
        this.actions = actions;
    }

    /**
     * Getter for property 'recommendations'.
     *
     * @return Value for property 'recommendations'.
     */
    public List<RecommendationLessonDTO> getRecommendations() {
        return recommendations;
    }

    /**
     * Setter for property 'recommendations'.
     *
     * @param recommendations Value to set for property 'recommendations'.
     */
    public void setRecommendations(List<RecommendationLessonDTO> recommendations) {
        this.recommendations = recommendations;
    }

    /**
     * Getter for property 'type'.
     *
     * @return Value for property 'type'.
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for property 'type'.
     *
     * @param type Value to set for property 'type'.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for property 'project'.
     *
     * @return Value for property 'project'.
     */
    public ProjectDTO getProject() {
        return project;
    }

    /**
     * Setter for property 'project'.
     *
     * @param project Value to set for property 'project'.
     */
    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    /**
     * Getter for property 'processGroup'.
     *
     * @return Value for property 'processGroup'.
     */
    public String getProcessGroup() {
        return processGroup;
    }

    /**
     * Setter for property 'processGroup'.
     *
     * @param processGroup Value to set for property 'processGroup'.
     */
    public void setProcessGroup(String processGroup) {
        this.processGroup = processGroup;
    }

    /**
     * Getter for property 'knowledgeArea'.
     *
     * @return Value for property 'knowledgeArea'.
     */
    public EntityDTO getKnowledgeArea() {
        return knowledgeArea;
    }

    /**
     * Setter for property 'knowledgeArea'.
     *
     * @param knowledgeArea Value to set for property 'knowledgeArea'.
     */
    public void setKnowledgeArea(EntityDTO knowledgeArea) {
        this.knowledgeArea = knowledgeArea;
    }

    /**
     * Getter for property 'ranking'.
     *
     * @return Value for property 'ranking'.
     */
    public Integer getRanking() {
        return ranking;
    }

    /**
     * Setter for property 'ranking'.
     *
     * @param ranking Value to set for property 'ranking'.
     */
    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
}
