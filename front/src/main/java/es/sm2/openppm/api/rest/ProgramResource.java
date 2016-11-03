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
 * File: ProgramResource.java
 * Create User: jordi.ripoll
 * Create Date: 04/09/2015 12:40:08
 */

package es.sm2.openppm.api.rest;

import es.sm2.openppm.api.converter.project.ProgramDTOConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.common.ErrorDTO;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
@Path("/program")

@Produces({MediaType.APPLICATION_JSON})
public class ProgramResource extends BaseResource {

    /**
     * Find all by po
     *
     * @return
     */
    @GET
    public Response findAll() {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_FM,
                Constants.ROLE_IM, Constants.ROLE_SPONSOR, Constants.ROLE_PROGM);

        if (response == null) {

            try {

                if (getUser() != null && getUser().getPerformingorg() != null) {

                    //Declare logic
                    ProgramLogic programLogic = new ProgramLogic();

                    //Logic
                    List<Program> programs = programLogic.consByPO(getUser());

                    LogManager.getLog(this.getClass()).info("Find all Programs");

                    // Converter model object to dto object
                    //
                    ProgramDTOConverter programDTOConverter = new ProgramDTOConverter();

                    List<EntityDTO> programsDTO = programDTOConverter.toDTOList(programs);

                    // Constructor response
                    response = Response.ok().entity(programsDTO).build();
                }
                else {
                    LogManager.getLog(this.getClass()).error("PO is null");

                    response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, "PO is null")).build();
                }
            }
            catch (Exception e) {

                response = getErrorResponse(LogManager.getLog(getClass()), "findAll()", e);
            }
        }

        return response;
    }

    @GET
    @Path("{idProgramManager}")
    public Response findByProgramManager(@PathParam("idProgramManager") Integer idProgramManager) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PROGM);

        if (response == null) {

            try {

                if (getUser() != null && getUser().getPerformingorg() != null) {

                    //Declare logic
                    ProgramLogic programLogic = new ProgramLogic();

                    // Employee
                    Employee employee = new Employee();
                    employee.setIdEmployee(idProgramManager);

                    //Logic
                    List<Program> programs = programLogic.consByProgramManager(employee);

                    LogManager.getLog(this.getClass()).info("Find program manager Programs");

                    // Converter model object to dto object
                    //
                    ProgramDTOConverter programDTOConverter = new ProgramDTOConverter();

                    List<EntityDTO> programsDTO = programDTOConverter.toDTOList(programs);

                    // Constructor response
                    response = Response.ok().entity(programsDTO).build();
                }
                else {
                    LogManager.getLog(this.getClass()).error("PO is null");

                    response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, "PO is null")).build();
                }
            }
            catch (Exception e) {

                response = getErrorResponse(LogManager.getLog(getClass()), "findAll()", e);
            }
        }

        return response;
    }
}
