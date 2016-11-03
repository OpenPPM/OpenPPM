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
 * File: EntityDTO.java
 * Create User: jordi.ripoll
 * Create Date: 03/08/2015 09:48:20
 */

package es.sm2.openppm.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityDTO implements Serializable {

    private Integer code;
    private String name;
    private String description;
    private EntityDTO performingOrganization;

    public EntityDTO() {
    }

    
    public EntityDTO(String name) {
        this.name = name;
    }
    public EntityDTO(Integer code) {
        this.code = code;
    }
    
    public EntityDTO(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Getter for property 'code'.
     *
     * @return Value for property 'code'.
     */
    public Integer getCode() {

        return code;
    }

    /**
     * Setter for property 'code'.
     *
     * @param code Value to set for property 'code'.
     */
    public void setCode(Integer code) {

        this.code = code;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {

        return name;
    }

    /**
     * Setter for property 'name'.
     *
     * @param name Value to set for property 'name'.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Getter for property 'description'.
     *
     * @return Value for property 'description'.
     */
    public String getDescription() {

        return description;
    }

    /**
     * Setter for property 'description'.
     *
     * @param description Value to set for property 'description'.
     */
    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * Getter for property 'performingOrganization'.
     *
     * @return Value for property 'performingOrganization'.
     */
    public EntityDTO getPerformingOrganization() {

        return performingOrganization;
    }

    /**
     * Setter for property 'performingOrganization'.
     *
     * @param performingOrganization Value to set for property 'performingOrganization'.
     */
    public void setPerformingOrganization(EntityDTO performingOrganization) {

        this.performingOrganization = performingOrganization;
    }
}


