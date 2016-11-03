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
 * File: MaintenanceProfilesActionImpl.java
 * Create User: javier.hernandez
 * Create Date: 04/08/2015 10:00:45
 */

package es.sm2.openppm.api.impl.maintenance;

import es.sm2.openppm.api.converter.maintenance.ProfileDTOConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.common.ErrorDTO;
import es.sm2.openppm.api.model.maintenance.ProfileDTO;
import es.sm2.openppm.core.logic.impl.ProfileLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Profile;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.utils.LogManager;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by javier.hernandez on 04/08/2015.
 */
public class ProfileActionImpl implements MaintenanceAction {

    /**
     * Save
     *
     * @param object
     * @param company
     * @return
     */
    @Override
    public Response save(Object object, Company company) {

        Response response;

        try {

            // Object mapper
            //
            ProfileDTO profileDTO = new ObjectMapper().convertValue(object, ProfileDTO.class);

            // Converter DTO to model
            ProfileDTOConverter profileDTOConverter = new ProfileDTOConverter();
            Profile profile = profileDTOConverter.toModel(profileDTO);
            profile.setCompany(company);

            // Declare logic
            ProfileLogic profileLogic = new ProfileLogic();
            profile = profileLogic.save(profile);

            // Converter model to DTO
            profileDTO = profileDTOConverter.toDTO(profile);

            // Constructor response
            response = Response.ok().entity(profileDTO).build();
        }
        catch (Exception e) {

            LogManager.getLog(this.getClass()).error(e);

            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
        }

        return response;
    }

    /**
     * Update
     *
     * @param object
     * @param company
     * @return
     */
    @Override
    public Response update(Object object, Company company) {

        return save(object, company);
    }

    /**
     * Find all
     *
     * @return
     * @param company
     */
    @Override
    public Response findAll(Company company, Employee user) {

        Response response;

        try {
            //Declare logic
            ProfileLogic profileLogic = new ProfileLogic();

            //Logic
            List<Profile> profiles = profileLogic.findByRelation(Profile.COMPANY, company, Profile.NAME, Order.ASC);

            // Converter model object to dto object
            //
            ProfileDTOConverter profileDTOConverter = new ProfileDTOConverter();

            List<ProfileDTO> profilesDTO = profileDTOConverter.toDTOList(profiles);

            // Constructor response
            response = Response.ok().entity(profilesDTO).build();
        }
        catch (Exception e) {

            LogManager.getLog(this.getClass()).error(e);

            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
        }

        return response;
    }

    /**
     * Find by id
     *
     * @param id
     * @return
     */
    @Override
    public Response findByID(Integer id) {

        Response response;

        try {

            //Declare logic
            ProfileLogic profileLogic = new ProfileLogic();

            //Logic
            List<String> joins = new ArrayList<String>();
            joins.add(Profile.COMPANY);

            Profile profile = profileLogic.findById(id, joins);

            if (profile != null) {

                // Converter model object to dto object
                //
                ProfileDTOConverter profileDTOConverter = new ProfileDTOConverter();

                ProfileDTO profileDTO = profileDTOConverter.toDTO(profile);

                // Constructor response
                response = Response.ok().entity(profileDTO).build();
            }
            else {

                LogManager.getLog(this.getClass()).error("No data");

                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, "No data")).build();
            }
        }
        catch (Exception e) {

            LogManager.getLog(this.getClass()).error(e);

            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
        }

        return response;
    }

    /**
     * Delete
     *
     * @param id
     * @param resourceBundle
     * @return
     */
    @Override
    public Response remove(Integer id, ResourceBundle resourceBundle) {

        Response response;

        try {

            // Declare logic
            ProfileLogic profileLogic = new ProfileLogic(resourceBundle);

            // Logic
            profileLogic.remove(id);

            // Constructor response
            response = Response.ok().entity(new EntityDTO(id)).build();
        }
        catch (Exception e) {

            LogManager.getLog(this.getClass()).error(e);

            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
        }

        return response;
    }


}