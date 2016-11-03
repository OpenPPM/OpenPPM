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
 * File: ChangeRequest.java
 * Create User: javier.hernandez
 * Create Date: 22/09/2015 13:56:52
 */

package es.sm2.openppm.api.model.project;

import es.sm2.openppm.api.model.common.EntityDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by javier.hernandez on 22/09/2015.
 */
public class ChangeRequestDTO extends EntityDTO {

    private EntityDTO changetype;
    private EntityDTO wbsnode;
    private String description;
    private Character priority;
    private Date changeDate;
    private String originator;
    private String recommendedSolution;
    private String impactDescription;
    private Boolean resolution;
    private String resolutionReason;
    private Date resolutionDate;
    private String changeDocLink;
    private String resolutionName;
    private Double estimatedEffort;
    private Double estimatedCost;
    private List<ChangeRequestNodeDTO> nodeDTOList;

    /**
     * Getter for property 'nodeDTOList'.
     *
     * @return Value for property 'nodeDTOList'.
     */
    public List<ChangeRequestNodeDTO> getNodeDTOList() {

        return nodeDTOList;
    }

    /**
     * Setter for property 'nodeDTOList'.
     *
     * @param nodeDTOList Value to set for property 'nodeDTOList'.
     */
    public void setNodeDTOList(List<ChangeRequestNodeDTO> nodeDTOList) {

        this.nodeDTOList = nodeDTOList;
    }

    /**
     * Getter for property 'changetype'.
     *
     * @return Value for property 'changetype'.
     */
    public EntityDTO getChangetype() {

        return changetype;
    }

    /**
     * Setter for property 'changetype'.
     *
     * @param changetype Value to set for property 'changetype'.
     */
    public void setChangetype(EntityDTO changetype) {

        this.changetype = changetype;
    }

    /**
     * Getter for property 'wbsnode'.
     *
     * @return Value for property 'wbsnode'.
     */
    public EntityDTO getWbsnode() {

        return wbsnode;
    }

    /**
     * Setter for property 'wbsnode'.
     *
     * @param wbsnode Value to set for property 'wbsnode'.
     */
    public void setWbsnode(EntityDTO wbsnode) {

        this.wbsnode = wbsnode;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {

        return description;
    }

    /** {@inheritDoc} */
    @Override
    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * Getter for property 'priority'.
     *
     * @return Value for property 'priority'.
     */
    public Character getPriority() {

        return priority;
    }

    /**
     * Setter for property 'priority'.
     *
     * @param priority Value to set for property 'priority'.
     */
    public void setPriority(Character priority) {

        this.priority = priority;
    }

    /**
     * Getter for property 'changeDate'.
     *
     * @return Value for property 'changeDate'.
     */
    public Date getChangeDate() {

        return changeDate;
    }

    /**
     * Setter for property 'changeDate'.
     *
     * @param changeDate Value to set for property 'changeDate'.
     */
    public void setChangeDate(Date changeDate) {

        this.changeDate = changeDate;
    }

    /**
     * Getter for property 'originator'.
     *
     * @return Value for property 'originator'.
     */
    public String getOriginator() {

        return originator;
    }

    /**
     * Setter for property 'originator'.
     *
     * @param originator Value to set for property 'originator'.
     */
    public void setOriginator(String originator) {

        this.originator = originator;
    }

    /**
     * Getter for property 'recommendedSolution'.
     *
     * @return Value for property 'recommendedSolution'.
     */
    public String getRecommendedSolution() {

        return recommendedSolution;
    }

    /**
     * Setter for property 'recommendedSolution'.
     *
     * @param recommendedSolution Value to set for property 'recommendedSolution'.
     */
    public void setRecommendedSolution(String recommendedSolution) {

        this.recommendedSolution = recommendedSolution;
    }

    /**
     * Getter for property 'impactDescription'.
     *
     * @return Value for property 'impactDescription'.
     */
    public String getImpactDescription() {

        return impactDescription;
    }

    /**
     * Setter for property 'impactDescription'.
     *
     * @param impactDescription Value to set for property 'impactDescription'.
     */
    public void setImpactDescription(String impactDescription) {

        this.impactDescription = impactDescription;
    }

    /**
     * Getter for property 'resolution'.
     *
     * @return Value for property 'resolution'.
     */
    public Boolean getResolution() {

        return resolution;
    }

    /**
     * Setter for property 'resolution'.
     *
     * @param resolution Value to set for property 'resolution'.
     */
    public void setResolution(Boolean resolution) {

        this.resolution = resolution;
    }

    /**
     * Getter for property 'resolutionReason'.
     *
     * @return Value for property 'resolutionReason'.
     */
    public String getResolutionReason() {

        return resolutionReason;
    }

    /**
     * Setter for property 'resolutionReason'.
     *
     * @param resolutionReason Value to set for property 'resolutionReason'.
     */
    public void setResolutionReason(String resolutionReason) {

        this.resolutionReason = resolutionReason;
    }

    /**
     * Getter for property 'resolutionDate'.
     *
     * @return Value for property 'resolutionDate'.
     */
    public Date getResolutionDate() {

        return resolutionDate;
    }

    /**
     * Setter for property 'resolutionDate'.
     *
     * @param resolutionDate Value to set for property 'resolutionDate'.
     */
    public void setResolutionDate(Date resolutionDate) {

        this.resolutionDate = resolutionDate;
    }

    /**
     * Getter for property 'changeDocLink'.
     *
     * @return Value for property 'changeDocLink'.
     */
    public String getChangeDocLink() {

        return changeDocLink;
    }

    /**
     * Setter for property 'changeDocLink'.
     *
     * @param changeDocLink Value to set for property 'changeDocLink'.
     */
    public void setChangeDocLink(String changeDocLink) {

        this.changeDocLink = changeDocLink;
    }

    /**
     * Getter for property 'resolutionName'.
     *
     * @return Value for property 'resolutionName'.
     */
    public String getResolutionName() {

        return resolutionName;
    }

    /**
     * Setter for property 'resolutionName'.
     *
     * @param resolutionName Value to set for property 'resolutionName'.
     */
    public void setResolutionName(String resolutionName) {

        this.resolutionName = resolutionName;
    }

    /**
     * Getter for property 'estimatedEffort'.
     *
     * @return Value for property 'estimatedEffort'.
     */
    public Double getEstimatedEffort() {

        return estimatedEffort;
    }

    /**
     * Setter for property 'estimatedEffort'.
     *
     * @param estimatedEffort Value to set for property 'estimatedEffort'.
     */
    public void setEstimatedEffort(Double estimatedEffort) {

        this.estimatedEffort = estimatedEffort;
    }

    /**
     * Getter for property 'estimatedCost'.
     *
     * @return Value for property 'estimatedCost'.
     */
    public Double getEstimatedCost() {

        return estimatedCost;
    }

    /**
     * Setter for property 'estimatedCost'.
     *
     * @param estimatedCost Value to set for property 'estimatedCost'.
     */
    public void setEstimatedCost(Double estimatedCost) {

        this.estimatedCost = estimatedCost;
    }
}
