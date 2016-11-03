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
 * File: UserDTO.java
 * Create User: javier.hernandez
 * Create Date: 05/08/2015 08:38:59
 */

package es.sm2.openppm.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * Created by javier.hernandez on 05/08/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginDTO {

    private String username;

    /**
     * Getter for property 'username'.
     *
     * @return Value for property 'username'.
     */
    public String getUsername() {

        return username;
    }

    /**
     * Setter for property 'username'.
     *
     * @param username Value to set for property 'username'.
     */
    public void setUsername(String username) {

        this.username = username;
    }
}
