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
 * File: ChangeRequestNodeDTO.java
 * Create User: javier.hernandez
 * Create Date: 22/09/2015 15:12:30
 */

package es.sm2.openppm.api.model.project;

import es.sm2.openppm.api.model.common.EntityDTO;

import java.util.Date;

/**
 * Created by javier.hernandez on 22/09/2015.
 */
public class ChangeRequestNodeDTO extends EntityDTO {

    private EntityDTO wbsnode;
    private Double estimatedEffort;
    private Double estimatedCost;

    public ChangeRequestNodeDTO() {

    }

    public ChangeRequestNodeDTO(Integer code) {

        super(code);
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
