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
 * File: EntityAudit.java
 * Create User: javier.hernandez
 * Create Date: 18/09/2015 08:28:30
 */

package es.sm2.openppm.core.audit.model;

/**
 * Created by javier.hernandez on 18/09/2015.
 */
public class EntityAudit {

    private Integer code;
    private String name;

    public EntityAudit() {

    }

    public EntityAudit(Integer code) {

        this.code = code;
    }

    public EntityAudit(String name) {

        this.name = name;
    }

    public EntityAudit(Integer code, String name) {

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
}
