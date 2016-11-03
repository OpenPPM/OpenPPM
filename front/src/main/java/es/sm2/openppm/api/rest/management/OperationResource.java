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
 * File: OperationResource.java
 * Create User: javier.hernandez
 * Create Date: 25/09/2015 12:57:18
 */

package es.sm2.openppm.api.rest.management;

import es.sm2.openppm.api.converter.management.EmployeeDTOConverter;
import es.sm2.openppm.api.converter.management.OperationDTOConverter;
import es.sm2.openppm.api.model.common.EmployeeDTO;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.OperationLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.search.OperationSearch;
import es.sm2.openppm.core.model.search.TimeSheetOperationSearch;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by javier.hernandez on 24/09/2015.
 */
@Path("/management/operation")
@Produces({MediaType.APPLICATION_JSON})
public class OperationResource extends BaseResource {

    /**
     * Get user
     *
     * @return
     */
    @GET
    public Response find(@QueryParam(value = "availableForManager") Boolean availableForManager,
                         @QueryParam(value = "availableForApprove") Boolean availableForApprove) {

        // Check permissions
        isUserLogged();

        OperationSearch search = new OperationSearch(getCompany());
        search.setAvailableForManager(availableForManager != null && availableForManager);
        search.setAvailableForApprove(availableForApprove != null && availableForApprove);

        List<EntityDTO> operationList = null;

        try {

            OperationLogic logic = new OperationLogic();
            List<Operation> operations = logic.find(search);

            OperationDTOConverter converter = new OperationDTOConverter();
            operationList = converter.toDTOList(operations);
        }
        catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "find()", e);
        }

        return Response.ok().entity(operationList).build();
    }
}
