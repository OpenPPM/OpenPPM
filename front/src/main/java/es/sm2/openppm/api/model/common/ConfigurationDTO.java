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
 * File: ConfigurationDTO.java
 * Create User: jordi.ripoll
 * Create Date: 19/08/2015 12:51:38
 */

package es.sm2.openppm.api.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * Created by jordi.ripoll on 03/08/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationDTO {

    private String key;
    private String value;
    private String type;

    public ConfigurationDTO() {
        super();
    }

    public ConfigurationDTO(String key, String value) {

        this.key = key;
        this.value = value;
    }

    /**
     * Getter for property 'key'.
     *
     * @return Value for property 'key'.
     */
    public String getKey() {

        return key;
    }

    /**
     * Setter for property 'key'.
     *
     * @param key Value to set for property 'key'.
     */
    public void setKey(String key) {

        this.key = key;
    }

    /**
     * Getter for property 'value'.
     *
     * @return Value for property 'value'.
     */
    public String getValue() {

        return value;
    }

    /**
     * Setter for property 'value'.
     *
     * @param value Value to set for property 'value'.
     */
    public void setValue(String value) {

        this.value = value;
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
}
