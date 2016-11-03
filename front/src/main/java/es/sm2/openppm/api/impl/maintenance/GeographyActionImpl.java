package es.sm2.openppm.api.impl.maintenance;

import es.sm2.openppm.api.converter.maintenance.GeographyDTOConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.common.ErrorDTO;
import es.sm2.openppm.api.model.maintenance.GeographyDTO;
import es.sm2.openppm.core.logic.impl.GeographyLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.utils.LogManager;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by daniel.salamanca on 02/09/2015.
 */

//TODO daniel.salamanca - 02/09/2015 - Implementar el resto de metodos

public class GeographyActionImpl implements MaintenanceAction {

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
            //Declare logic
            GeographyLogic geographyLogic = new GeographyLogic();

            //Logic
            List<Geography> geographies = geographyLogic.findByRelation(Geography.COMPANY, company, Geography.NAME, Order.ASC);

            // Converter model object to dto object
            //
            GeographyDTOConverter geographyDTOConverter = new GeographyDTOConverter();

            List<GeographyDTO> geographiesDTO = geographyDTOConverter.toDTOList(geographies);

            // Constructor response
            response = Response.ok().entity(geographiesDTO).build();
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
