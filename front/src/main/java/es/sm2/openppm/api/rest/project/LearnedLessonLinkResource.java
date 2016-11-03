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
 * File: ProjectUsedLessonResource.java
 * Create User: jordi.ripoll
 * Create Date: 07/09/2015 16:52:16
 */

package es.sm2.openppm.api.rest.project;

import es.sm2.openppm.api.converter.project.ProjectUsedLessonDTOConverter;
import es.sm2.openppm.api.model.common.ErrorDTO;
import es.sm2.openppm.api.model.project.LearnedLessonLinkDTO;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.LearnedLessonLinkLogic;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.LearnedLessonLink;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jordi.ripoll on 07/09/2015.
 */
@Path("project/{CODE}/learnedlesson/")

@Produces({MediaType.APPLICATION_JSON})
public class LearnedLessonLinkResource extends BaseResource {

    /**
     * Find all
     *
     * @return
     */
    @GET
    public Response findAll(@PathParam("CODE") Integer idProject) {

        // Check permissions
        Response response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM);

        if (response == null) {

            try {

                // Declare logic
                LearnedLessonLinkLogic learnedLessonLinkLogic = new LearnedLessonLinkLogic();

                // Logic
                List<LearnedLessonLink> useds = learnedLessonLinkLogic.findByProject(new Project(idProject));

                // Converter model object to dto object
                //
                ProjectUsedLessonDTOConverter usedDTOConverter = new ProjectUsedLessonDTOConverter();

                List<LearnedLessonLinkDTO> usedsDTO = usedDTOConverter.toDTOList(useds);

                // Constructor response
                response = Response.ok().entity(usedsDTO).build();
            }
            catch (Exception e) {

                LogManager.getLog(this.getClass()).error(e);

                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
            }
        }

        return response;
    }

    @DELETE
    @Path("{CODE_USED}")
    public Response remove(@PathParam("CODE") Integer idProject, @PathParam("CODE_USED") Integer idProjectUsedLesson) {

        // Check permissions
        Response response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM);

        if (response == null) {

            try {

                // Set data
                LearnedLessonLink learnedLessonLink = new LearnedLessonLink(idProjectUsedLesson);
                learnedLessonLink.setProject(new Project(idProject));

                // Logic
                LearnedLessonLinkLogic learnedLessonLinkLogic = new LearnedLessonLinkLogic();

                learnedLessonLinkLogic.delete(learnedLessonLink);

                // Constructor response
                response = Response.ok().build();
            }
            catch (Exception e) {

                LogManager.getLog(this.getClass()).error(e);

                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
            }
        }

        return response;
    }

}