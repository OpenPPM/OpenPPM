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
 * File: AuthenticateException.java
 * Create User: javier.hernandez
 * Create Date: 05/08/2015 08:26:43
 */

package es.sm2.openppm.api.rest.error;

import es.sm2.openppm.api.model.common.ErrorDTO;

import javax.ws.rs.core.Response;

/**
 * Created by javier.hernandez on 05/08/2015.
 */
public class AuthenticateException extends RestServiceException {

    public AuthenticateException(String message) {
        this(message, null);
    }

    public AuthenticateException(String message, Throwable cause) {
        super(new ErrorDTO(Response.Status.UNAUTHORIZED, message), cause);
    }
}
