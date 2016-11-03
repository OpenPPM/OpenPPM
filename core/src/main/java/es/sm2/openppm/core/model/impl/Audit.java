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
 * File: Audit.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 16:54:01
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.common.enums.LocationAuditEnum;
import es.sm2.openppm.core.model.base.BaseAudit;

import java.util.Date;

/**
 * Created by jordi.ripoll on 10/08/2015.
 */
public class Audit extends BaseAudit {

    public Audit() {

        super();
    }

    public Audit(LocationAuditEnum location, Employee user, Project project) {
        super();

        setCreationDate(new Date());
        setLocation(location.name());

        // User Data
        setIdEmployee(user.getIdEmployee());
        if (user.getContact() != null) {
            setUsername(user.getContact().getFullName());
            setIdContact(user.getContact().getIdContact());

            if (user.getContact().getCompany() != null) {
                setIdCompany(user.getContact().getCompany().getIdCompany());
            }
        }

        // Project Data
        setIdProject(project.getIdProject());
        setProjectStatus(project.getStatus());

    }

    public Audit(Integer idAudit) {
        super(idAudit);
    }
}
