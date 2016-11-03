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
 * File: MaintenanceAction.java
 * Create User: jordi.ripoll
 * Create Date: 30/06/2015 09:28:08
 */

package es.sm2.openppm.core.logic.security.actions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.security.SecurityAction;
import es.sm2.openppm.core.logic.security.SecurityContainer;

import java.util.Arrays;
import java.util.List;

/**
 * SM2 Baleares
 */
public enum MaintenanceAction implements SecurityAction {

    JX_SAVE_TECHNOLOGY(new SecurityContainer(Constants.ROLE_PMO)),
    JX_DELETE_TECHNOLOGY(new SecurityContainer(Constants.ROLE_PMO));


    private List<SecurityContainer> securityContainers;


    MaintenanceAction(SecurityContainer... securityContainers) {

        this.securityContainers = Arrays.asList(securityContainers);
    }

    @Override
    public String getAction() {
        return name();
    }

    @Override
    public boolean hasPermission(int role) {

        boolean permission = false;

        int i = 0;
        while (!permission && i < securityContainers.size()) {

            SecurityContainer securityContainer = securityContainers.get(i);

            permission = securityContainer.hasPermission(role);

            i++;
        }

        return permission;
    }
}
