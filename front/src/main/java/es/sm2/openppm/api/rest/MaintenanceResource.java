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
 * File: ProfileRest.java
 * Create User: jordi.ripoll
 * Create Date: 04/08/2015 08:49:03
 */

package es.sm2.openppm.api.rest;

import es.sm2.openppm.api.impl.maintenance.MaintenanceAction;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.common.ErrorDTO;
import es.sm2.openppm.api.model.enums.MaintenanceEnum;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
@Path("/maintenance/{MAINTENANCE_TYPE}")

@Produces({MediaType.APPLICATION_JSON})
public class MaintenanceResource extends BaseResource {

    /**
     * Find all by company
     *
     * @param maintenanceType
     * @return
     */
    @GET
    public Response findAll(@PathParam("MAINTENANCE_TYPE") String maintenanceType) {

        // Check permissions
        isUserLogged(
                Resourceprofiles.Profile.ADMIN,
                Resourceprofiles.Profile.PMO,
                Resourceprofiles.Profile.LOGISTIC,
                Resourceprofiles.Profile.RESOURCE_MANAGER,
                Resourceprofiles.Profile.PROJECT_MANAGER,
                Resourceprofiles.Profile.PROGRAM_MANAGER);

        Response  response = null;

        // Get implementation
        //
        MaintenanceEnum maintenanceEnum = MaintenanceEnum.valueOf(maintenanceType);

        Class<? extends MaintenanceAction> maintenanceAction = maintenanceEnum.getMaintenanceAction();

        MaintenanceAction maintenanceActionImpl;

        try {

            maintenanceActionImpl = maintenanceAction.newInstance();

            // Logic implementation
            response = maintenanceActionImpl.findAll(getCompany(), getUser());
        }
        catch (InstantiationException e) {
            sendError(LogManager.getLog(getClass()), "findAll", e);
        }
        catch (IllegalAccessException e) {
            sendError(LogManager.getLog(getClass()), "findAll", e);
        }

        return response;
    }

    /**
     * Find by id
     *
     * @param maintenanceType
     * @param id
     * @return
     */
    @GET
    @Path("{ID}")
    public Response findByID(@PathParam("MAINTENANCE_TYPE") String maintenanceType, @PathParam("ID") Integer id) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO);

        if (response == null) {

            // Get implementation
            //
            MaintenanceEnum maintenanceEnum = MaintenanceEnum.valueOf(maintenanceType);

            Class<? extends MaintenanceAction> maintenanceAction = maintenanceEnum.getMaintenanceAction();

            MaintenanceAction maintenanceActionImpl;

            try {

                maintenanceActionImpl = maintenanceAction.newInstance();

                // Logic implementation
                response = maintenanceActionImpl.findByID(id);
            }
            catch (InstantiationException e) {
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
            }
            catch (IllegalAccessException e) {
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
            }
        }

        return response;
    }

    /**
     * Create new
     *
     * @param entityDTO
     * @param maintenanceType
     * @return
     */
    @POST
    public Response save(EntityDTO entityDTO, @PathParam("MAINTENANCE_TYPE") String maintenanceType) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO);

        if (response == null) {

            MaintenanceEnum maintenanceEnum = MaintenanceEnum.valueOf(maintenanceType);

            Class<? extends MaintenanceAction> maintenanceAction = maintenanceEnum.getMaintenanceAction();

            MaintenanceAction maintenanceActionImpl;

            try {
                maintenanceActionImpl = maintenanceAction.newInstance();

                response = maintenanceActionImpl.save(entityDTO, getCompany());
            }
            catch (InstantiationException e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "save()", e);
            }
            catch (IllegalAccessException e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "save()", e);
            }

        }

        return response;
    }

    /**
     * Update
     *
     * @param entityDTO
     * @param maintenanceType
     * @return
     */
    @PUT
    public Response update(EntityDTO entityDTO, @PathParam("MAINTENANCE_TYPE") String maintenanceType) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO);

        if (response == null) {

            MaintenanceEnum maintenanceEnum = MaintenanceEnum.valueOf(maintenanceType);

            Class<? extends MaintenanceAction> maintenanceAction = maintenanceEnum.getMaintenanceAction();

            MaintenanceAction maintenanceActionImpl;

            try {
                maintenanceActionImpl = maintenanceAction.newInstance();

                response = maintenanceActionImpl.update(entityDTO, getCompany());
            }
            catch (InstantiationException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
            }
            catch (IllegalAccessException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
            }
        }

        return response;
    }

    /**
     * Remove
     *
     * @param id
     * @param maintenanceType
     * @return
     */
    @DELETE
    @Path("{ID}")
    public Response remove(@PathParam("MAINTENANCE_TYPE") String maintenanceType, @PathParam("ID") Integer id) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO);

        if (response == null) {

            MaintenanceEnum maintenanceEnum = MaintenanceEnum.valueOf(maintenanceType);

            Class<? extends MaintenanceAction> maintenanceAction = maintenanceEnum.getMaintenanceAction();

            MaintenanceAction maintenanceActionImpl;

            try {
                maintenanceActionImpl = maintenanceAction.newInstance();

                response = maintenanceActionImpl.remove(id, getResourceBundle());
            }
            catch (InstantiationException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
            }
            catch (IllegalAccessException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
            }
        }

        return response;
    }
}
