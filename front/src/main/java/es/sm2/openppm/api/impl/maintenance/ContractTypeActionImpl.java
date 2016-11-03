package es.sm2.openppm.api.impl.maintenance;

import es.sm2.openppm.api.converter.maintenance.ContractTypeDTOConverter;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.core.logic.impl.ContractTypeLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by daniel.salamanca on 02/09/2015.
 */

public class ContractTypeActionImpl implements MaintenanceAction {

    /**
     * Save
     *
     * @param object
     * @param company
     * @return
     */
    @Override
    public Response save(Object object, Company company) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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
            // Find data
            ContractTypeLogic contractTypeLogic = new ContractTypeLogic();
            List<Contracttype> contractTypes = contractTypeLogic.findByRelation(Contracttype.COMPANY, company, Contracttype.DESCRIPTION, Order.ASC);

            // Convert to DTO and Send data
            ContractTypeDTOConverter contractTypeDTOConverter = new ContractTypeDTOConverter();
            response = Response.ok().entity(contractTypeDTOConverter.toDTOList(contractTypes)).build();
        }
        catch (Exception e) {
            response = BaseResource.generateErrorResponse(LogManager.getLog(getClass()), "findAll()", e);
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

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
