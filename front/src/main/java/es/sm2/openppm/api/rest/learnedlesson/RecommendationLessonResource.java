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
 * File: RecommendationLessonResource.java
 * Create User: jordi.ripoll
 * Create Date: 07/09/2015 10:45:39
 */

package es.sm2.openppm.api.rest.learnedlesson;

import es.sm2.openppm.api.converter.project.RecLessonDTOConverter;
import es.sm2.openppm.api.model.project.RecommendationLessonDTO;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.RecommendationLessonLogic;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.RecommendationLesson;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jordi.ripoll on 07/09/2015.
 */
@Path("/learnedlesson/{IDLLAA}/recommendation")

@Produces({MediaType.APPLICATION_JSON})
public class RecommendationLessonResource extends BaseResource {

    /**
     * Find all by idLearnedLesson
     *
     * @return
     */
    @GET
    public Response findAll(@PathParam("IDLLAA") Integer idLearnedLesson) {

        // Check permissions
        Response response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_PROGM);

        if (response == null) {

            try {

                // Declare logic
                RecommendationLessonLogic recLessonLogic = new RecommendationLessonLogic();

                // Logic
                List<RecommendationLesson> recommendations = recLessonLogic.findByRelation(RecommendationLesson.LEARNEDLESSON, new LearnedLesson(idLearnedLesson));

                LogManager.getLog(this.getClass()).info("Find all Recommendations");

                // Converter model object to dto object
                //
                RecLessonDTOConverter recLessonDTOConverter = new RecLessonDTOConverter();

                List<RecommendationLessonDTO> recommendationsDTO = recLessonDTOConverter.toDTOList(recommendations);

                // Constructor response
                response = Response.ok().entity(recommendationsDTO).build();
            }
            catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "findAll()", e);
            }
        }

        return response;
    }

    /**
     * Save recommendation lesson
     *
     * @return
     */
    @POST
    public Response save(@PathParam("IDLLAA") Integer idLearnedLesson, RecommendationLessonDTO recommendationDTO) {

        // Check permissions
        Response response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_PROGM);

        if (response == null) {

            try {

                // Converter dto to model
                //
                RecLessonDTOConverter recLessonDTOConverter = new RecLessonDTOConverter();

                RecommendationLesson recLesson = recLessonDTOConverter.toModel(recommendationDTO);

                // Set id Learned Lesson
                recLesson.setLearnedLesson(new LearnedLesson(idLearnedLesson));

                // Declare logic
                RecommendationLessonLogic recLessonLogic = new RecommendationLessonLogic();

                // Logic save
                recLesson = recLessonLogic.save(recLesson);

                LogManager.getLog(this.getClass()).info("Recommendation Lesson saved");

                response = Response.ok().entity(recLessonDTOConverter.toDTO(recLesson)).build();
            }
            catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "save()", e);
            }
        }

        return response;
    }

    /**
     * Update
     *
     * @return
     */
    @PUT
    @Path("{IDRECOMMENDATION}")
    public Response update(@PathParam("IDLLAA") Integer idLearnedLesson, @PathParam("IDRECOMMENDATION") Integer idRecommendationLesson, RecommendationLessonDTO recommendationDTO) {

        // Check permissions
        Response response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_PROGM);

        if (response == null) {

            try {

                // Converter dto to model
                //
                RecLessonDTOConverter recLessonDTOConverter = new RecLessonDTOConverter();

                RecommendationLesson recLesson = recLessonDTOConverter.toModel(recommendationDTO);

                // Set ids
                //
                recLesson.setIdRecommendationLesson(idRecommendationLesson);
                recLesson.setLearnedLesson(new LearnedLesson(idLearnedLesson));

                // Declare logic
                RecommendationLessonLogic recLessonLogic = new RecommendationLessonLogic();

                // Logic
                recLessonLogic.save(recLesson);

                LogManager.getLog(this.getClass()).info("Recommendation Lesson updated");

                response = Response.ok().build();
            }
            catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "update()", e);
            }
        }

        return response;
    }

    /**
     * Remove
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("{ID}")
    public Response remove(@PathParam("ID") Integer id) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_PROGM);

        if (response == null) {

            try {

                // Declare logic
                RecommendationLessonLogic recommendationLessonLogic = new RecommendationLessonLogic();

                // Logic
                recommendationLessonLogic.delete(recommendationLessonLogic.findById(id));

                LogManager.getLog(getClass()).info("Recommendation Lesson deleted");

                // Constructor response
                response = Response.ok().build();
            }
            catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "remove()", e);
            }
        }

        return response;
    }
}
