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
 * File: ChangeRequestResource.java
 * Create User: javier.hernandez
 * Create Date: 22/09/2015 13:02:28
 */

package es.sm2.openppm.api.rest.project;

import es.sm2.openppm.api.converter.project.ChangeRequestDTOConverter;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.core.logic.impl.ChangeControlLogic;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.search.ChangeControlSearch;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jordi.ripoll on 19/08/2015.
 */
@Path("/project/{"+BaseResource.CODE_PROJECT+"}/change-request")
@Produces({MediaType.APPLICATION_JSON})
public class ChangeRequestResource extends BaseResource {

    @PathParam(BaseResource.CODE_PROJECT)
    protected Integer codeProject;

    /**
     * Get change request of project
     *
     * @return
     */
    @GET
    public Response findOfProject(@QueryParam("resolution") Boolean resolution) {

        // Check permissions
        isUserLogged(
                Resourceprofiles.Profile.PROJECT_MANAGER,
                Resourceprofiles.Profile.INVESTMENT_MANAGER,
                Resourceprofiles.Profile.FUNCTIONAL_MANAGER,
                Resourceprofiles.Profile.PROGRAM_MANAGER,
                Resourceprofiles.Profile.PMO,
                Resourceprofiles.Profile.SPONSOR,
                Resourceprofiles.Profile.PORFOLIO_MANAGER,
                Resourceprofiles.Profile.STAKEHOLDER,
                Resourceprofiles.Profile.LOGISTIC
        );

        Response  response;

        try {

            ChangeControlSearch search = new ChangeControlSearch(codeProject, resolution);

            ChangeControlLogic changeControlLogic = new ChangeControlLogic();
            List<Changecontrol> changeControls = changeControlLogic.find(search);

            // Constructor response
            ChangeRequestDTOConverter converter = new ChangeRequestDTOConverter();
            response = Response.ok().entity(converter.toDTOList(changeControls)).build();

        }
        catch (Exception e) {
            response = getErrorResponse(LogManager.getLog(getClass()), "findOfProject()", e);
        }

        return response;
    }




}
