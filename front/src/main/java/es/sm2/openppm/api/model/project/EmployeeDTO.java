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
 * File: EmployeeDTO.java
 * Create User: jordi.ripoll
 * Create Date: 07/09/2015 16:05:18
 */

package es.sm2.openppm.api.model.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.sm2.openppm.api.model.common.EntityDTO;

/**
 * Created by jordi.ripoll on 07/09/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDTO extends EntityDTO {

    private EntityDTO contact;

    /**
     * Getter for property 'contact'.
     *
     * @return Value for property 'contact'.
     */
    public EntityDTO getContact() {
        return contact;
    }

    /**
     * Setter for property 'contact'.
     *
     * @param contact Value to set for property 'contact'.
     */
    public void setContact(EntityDTO contact) {
        this.contact = contact;
    }


}
