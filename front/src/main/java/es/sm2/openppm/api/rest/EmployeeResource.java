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
 * File: EmployeeResource.java
 * Create User: jordi.ripoll
 * Create Date: 06/10/2015 13:21:36
 */

package es.sm2.openppm.api.rest;

import es.sm2.openppm.api.converter.common.UserDTOConverter;
import es.sm2.openppm.api.model.common.UserDTO;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.search.EmployeeSearch;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jordi.ripoll on 06/10/2015.
 */
@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource extends BaseResource {

    /**
     * List of contacts
     *
     * @param codeProfile
     * @param codePerformingOrganization
     * @return
     * @throws es.sm2.openppm.api.rest.error.RestServiceException
     */
    @GET
    public Response getEmployees(@QueryParam(value = "codeProfile") Integer codeProfile,
                                @QueryParam(value = "codePerformingOrganization") Integer codePerformingOrganization) throws RestServiceException {

        // Check permissions
        isUserLogged();

        // Set filters
        EmployeeSearch search = new EmployeeSearch(getCompany());

        search.setCodeProfile(codeProfile);
        search.setCodePerformingOrganization(codePerformingOrganization);

        List<UserDTO> employeesDTO = null;

        try {

            // Logic Find
            EmployeeLogic logic = new EmployeeLogic();

            List<Employee> employees = logic.find(search);

            // Converter
            UserDTOConverter converter = new UserDTOConverter();
            employeesDTO = converter.toDTOList(employees);
        }
        catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "find()", e);
        }

        return Response.ok().entity(employeesDTO).build();
    }

}
