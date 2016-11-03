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
 * File: AuditResource.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:45:17
 */

package es.sm2.openppm.api.rest.project;

import es.sm2.openppm.api.converter.project.audit.AuditDTOConverter;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.common.enums.LocationAuditEnum;
import es.sm2.openppm.core.logic.impl.AuditLogic;
import es.sm2.openppm.core.model.impl.Audit;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.search.AuditSearch;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
@Path("/project/audit")

@Produces({MediaType.APPLICATION_JSON})
public class AuditResource extends BaseResource {

    /**
     * Find all by po
     *
     * @return
     */
    @GET
    public Response find(@QueryParam(value = "codeProject") Integer codeProject,
                         @QueryParam(value = "codeUser") Integer codeUser,
                         @QueryParam(value = "projectStatus") String projectStatus,
                         @QueryParam(value = "since") String since,
                         @QueryParam(value = "until") String until) {

        // Check permissions
        isUserLogged(Resourceprofiles.Profile.ADMIN);

        Response response = null;

        // Search data
        AuditSearch search = new AuditSearch(LocationAuditEnum.PROJECT_DATA, codeProject, getCompany());
        search.setIdContact(codeUser);
        search.setProjectStatus(projectStatus);
        search.setSince(formatDate(since));
        search.setUntil(formatDate(until));

        try {

            AuditLogic auditLogic = new AuditLogic();

            // Validations
            if (auditLogic.exceededQuota(search)) {
                throw new RestServiceException(Response.Status.PRECONDITION_FAILED, "The search is returning too many results, please restrict your search using the parameters");
            }

            List<Audit> audits = auditLogic.find(search);

            AuditDTOConverter converter = new AuditDTOConverter();
            response = Response.ok().entity(converter.toDTOList(audits)).build();
        }
        catch (Exception e) {

            sendError(LogManager.getLog(getClass()), "find()", e);
        }

        return response;
    }
}
