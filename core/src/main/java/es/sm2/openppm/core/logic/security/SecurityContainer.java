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
 * File: SecurityContainer.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.security;

import es.sm2.openppm.core.logic.exceptions.SecurityException;
import es.sm2.openppm.utils.functions.ValidateUtil;

import java.util.Arrays;
import java.util.List;

/**
 * SM2 Baleares
 * Created by javier.hernandez on 02/07/2014.
 */
public class SecurityContainer {

    private String status;
    private List<Integer> roles;

    public SecurityContainer(Integer...roles) {

        this.roles = Arrays.asList(roles);
    }

    public SecurityContainer(String status, Integer...roles) {

        this.status = status;
        this.roles = Arrays.asList(roles);
    }

    /**
     * Return if has permission
     *
     * @param status
     * @param role
     * @return
     */
    public boolean hasPermission(String status, int role) throws SecurityException {

        if (ValidateUtil.isNull(status)) {
            throw new SecurityException("Status send to SecurityContainer is required");
        }
        else if (ValidateUtil.isNull(this.status)) {
            throw new SecurityException("SecurityContainer not has status for check security by "+status);
        }

        return this.status.equals(status) && this.roles.contains(role);
    }

    /**
     * Return if has permission
     *
     * @param role
     * @return
     */
    public boolean hasPermission(int role) {

        return this.roles.contains(role);
    }
}
