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
 * File: AuthenticateDTO.java
 * Create User: javier.hernandez
 * Create Date: 05/08/2015 09:17:06
 */

package es.sm2.openppm.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * Created by javier.hernandez on 05/08/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticateDTO {

    private String token;
    private UserDTO user;

    public AuthenticateDTO() {
        super();
    }

    public AuthenticateDTO(String token, UserDTO userDTO) {

        this.token = token;
        this.user = userDTO;
    }

    /**
     * Getter for property 'token'.
     *
     * @return Value for property 'token'.
     */
    public String getToken() {

        return token;
    }

    /**
     * Setter for property 'token'.
     *
     * @param token Value to set for property 'token'.
     */
    public void setToken(String token) {

        this.token = token;
    }

    /**
     * Getter for property 'user'.
     *
     * @return Value for property 'user'.
     */
    public UserDTO getUser() {

        return user;
    }

    /**
     * Setter for property 'user'.
     *
     * @param user Value to set for property 'user'.
     */
    public void setUser(UserDTO user) {

        this.user = user;
    }
}
