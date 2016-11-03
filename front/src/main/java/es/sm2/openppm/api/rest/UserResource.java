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
 * File: UserResource.java
 * Create User: jordi.ripoll
 * Create Date: 20/08/2015 09:27:48
 */

package es.sm2.openppm.api.rest;

import es.sm2.openppm.api.converter.common.ConfigurationUserDTOConverter;
import es.sm2.openppm.api.converter.common.ProfileDTOConverter;
import es.sm2.openppm.api.converter.common.UserDTOConverter;
import es.sm2.openppm.api.model.common.ConfigurationDTO;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.common.ProfileDTO;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.model.impl.Configuration;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jordi.ripoll on 19/08/2015.
 */
@Path("/user")

@Produces({MediaType.APPLICATION_JSON})
public class UserResource extends BaseResource {

    /**
     * Get user
     *
     * @return
     */
    @GET
    public Response findUser() {

        // Check permissions
        isUserLogged();

        // Converter model object to dto object
        UserDTOConverter userDTOConverter = new UserDTOConverter();
        return Response.ok().entity(userDTOConverter.toDTO(getUser())).build();
    }

                
    @GET
    @Path(value = "/profile")
    public Response getProfiles() {

        // Check permissions
        isUserLogged();

        List<ProfileDTO> roles = null;
        try {

            // Find data
            EmployeeLogic employeeLogic = new EmployeeLogic();
            List<Employee> employees = employeeLogic.consEmployeesByUser(getUser().getContact());

            // Convert data tu send
            ProfileDTOConverter profileDTOConverter = new ProfileDTOConverter();
            roles = profileDTOConverter.toDTOList(employees);

        } catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "getProfiles()", e);
        }

        return Response.ok().entity(roles).build();
    }

    @PUT
    @Path(value = "/profile")
    public Response changeProfile(EntityDTO entityDTO) {

        // Check permissions
        isUserLogged();

        try {

            // Find User
            EmployeeLogic employeeLogic = new EmployeeLogic();
            Employee user = employeeLogic.consEmployee(entityDTO.getCode());

            // Update session user
            getRequest().getSession().setAttribute(SecurityUtil.USER, user);
            getRequest().getSession().setAttribute(SecurityUtil.ROL_PRINCIPAL, user.getResourceprofiles().getIdProfile());

        } catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "changeProfile()", e);
        }

        return findUser();
    }

    @GET
    @Path(value = "/configuration")
    public Response getUserSettings(@QueryParam(value = "type") List<String> types) {

        // Check permissions
        isUserLogged();

        List<ConfigurationDTO> configurationDTOs = null;

        try {

            // Find data
            ConfigurationLogic configurationLogic = new ConfigurationLogic();

            List<Configuration> configurations;

            if (ValidateUtil.isNotNull(types)) {
                configurations = configurationLogic.findByTypes(getUser(), types);
            }
            else {
                configurations = configurationLogic.findByRelation(Configuration.CONTACT, getUser().getContact());
            }

            // Convert data
            ConfigurationUserDTOConverter converter = new ConfigurationUserDTOConverter();
            configurationDTOs = converter.toDTOList(configurations);

        } catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "getUserSettings()", e);
        }

        return Response.ok().entity(configurationDTOs).build();
    }

    @PUT
    @Path(value = "/configuration")
    public Response updateUserSettings(List<ConfigurationDTO> configurationsDTO) {

        // Check permissions
        isUserLogged();

        try {

            // Converter dto to model
            //
            ConfigurationUserDTOConverter converter = new ConfigurationUserDTOConverter();

            List<Configuration> configurations = converter.toModelList(configurationsDTO);

            // Save or update configurations
            //
            if (ValidateUtil.isNotNull(configurations)) {

                ConfigurationLogic configurationLogic = new ConfigurationLogic();

                for (Configuration configuration : configurations) {

                    configurationLogic.saveConfiguration(getUser(), configuration.getType(), configuration.getName(), configuration.getValue());
                }
            }
        }
        catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "updateUserSettings()", e);
        }

        return Response.ok().build();
    }
}
