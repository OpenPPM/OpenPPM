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
 * File: TimelineActionImpl.java
 * Create User: jordi.ripoll
 * Create Date: 18/11/2015 09:23:45
 */

package es.sm2.openppm.api.impl.maintenance;

import es.sm2.openppm.api.converter.maintenance.TimelineTypeDTOConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.common.ErrorDTO;
import es.sm2.openppm.core.logic.impl.TimelineTypeLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.TimelineType;
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
public class TimelineTypeActionImpl implements MaintenanceAction {

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
            EntityDTO timelineTypeDTO = new ObjectMapper().convertValue(object, EntityDTO.class);

            // Converter DTO to model
            TimelineTypeDTOConverter timelineTypeDTOConverter = new TimelineTypeDTOConverter();
            TimelineType timelineType = timelineTypeDTOConverter.toModel(timelineTypeDTO);
            timelineType.setCompany(company);

            // Declare logic
            TimelineTypeLogic timelineTypeLogic = new TimelineTypeLogic();
            timelineType = timelineTypeLogic.save(timelineType);

            // Converter model to DTO
            timelineTypeDTO = timelineTypeDTOConverter.toDTO(timelineType);

            // Constructor response
            response = Response.ok().entity(timelineTypeDTO).build();

            LogManager.getLog(this.getClass()).debug("Save " + TimelineType.class.getSimpleName());
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
            TimelineTypeLogic timelineTypeLogic = new TimelineTypeLogic();

            //Logic
            List<TimelineType> timelineTypes = timelineTypeLogic.findByRelation(TimelineType.COMPANY, company, TimelineType.NAME, Order.ASC);

            // Converter model object to dto object
            //
            TimelineTypeDTOConverter timelineTypeDTOConverter = new TimelineTypeDTOConverter();

            List<EntityDTO> timelineTypesDTO = timelineTypeDTOConverter.toDTOList(timelineTypes);

            // Constructor response
            response = Response.ok().entity(timelineTypesDTO).build();

            LogManager.getLog(this.getClass()).debug("Find all " + TimelineType.class.getSimpleName());
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
            TimelineTypeLogic timelineTypeLogic = new TimelineTypeLogic();

            //Logic
            List<String> joins = new ArrayList<String>();
            joins.add(TimelineType.COMPANY);

            TimelineType timelineType = timelineTypeLogic.findById(id, joins);

            if (timelineType != null) {

                // Converter model object to dto object
                //
                TimelineTypeDTOConverter timelineTypeDTOConverter = new TimelineTypeDTOConverter();

                EntityDTO timelineTypeDTO = timelineTypeDTOConverter.toDTO(timelineType);

                // Constructor response
                response = Response.ok().entity(timelineTypeDTO).build();

                LogManager.getLog(this.getClass()).debug("Find by id " + TimelineType.class.getSimpleName());
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
            TimelineTypeLogic timelineTypeLogic = new TimelineTypeLogic(resourceBundle);

            // Logic
            timelineTypeLogic.remove(id);

            // Constructor response
            response = Response.ok().entity(new EntityDTO(id)).build();

            LogManager.getLog(this.getClass()).debug("Remove " + TimelineType.class.getSimpleName());
        }
        catch (Exception e) {

            LogManager.getLog(this.getClass()).error(e);

            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
        }

        return response;
    }


}