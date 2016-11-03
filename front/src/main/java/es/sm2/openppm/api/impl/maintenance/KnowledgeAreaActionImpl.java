
package es.sm2.openppm.api.impl.maintenance;

import es.sm2.openppm.api.converter.maintenance.KnowLedgeAreaDTOConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.common.ErrorDTO;
import es.sm2.openppm.api.model.maintenance.KnowLedgeAreaDTO;
import es.sm2.openppm.core.logic.impl.KnowledgeAreaLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.KnowledgeArea;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.utils.LogManager;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by daniel.salamanca on 20/08/2015.
 */
public class KnowledgeAreaActionImpl implements MaintenanceAction {

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
            KnowLedgeAreaDTO knowLedgeAreaDTO = new ObjectMapper().convertValue(object, KnowLedgeAreaDTO.class);

            // Converter DTO to model
            KnowLedgeAreaDTOConverter knowLedgeAreaDTOConverter = new KnowLedgeAreaDTOConverter();
            KnowledgeArea knowledgeArea = knowLedgeAreaDTOConverter.toModel(knowLedgeAreaDTO);
            knowledgeArea.setCompany(company);

            // Declare logic
            KnowledgeAreaLogic knowledgeAreaLogic = new KnowledgeAreaLogic();
            knowledgeArea = knowledgeAreaLogic.save(knowledgeArea);

            // Converter model to DTO
           knowLedgeAreaDTO = knowLedgeAreaDTOConverter.toDTO(knowledgeArea);

            // Constructor response
            response = Response.ok().entity(knowLedgeAreaDTO).build();
        }
        catch (Exception e) {

            // TODO javier.hernandez - 08/09/2015 - lanzar un RestServiceException
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
            KnowledgeAreaLogic knowledgeAreaLogic = new KnowledgeAreaLogic();

            //Logic
            List<KnowledgeArea> knowledgeAreas = knowledgeAreaLogic.findByRelation(KnowledgeArea.COMPANY, company, KnowledgeArea.NAME, Order.ASC);

            // Converter model object to dto object
            //
            KnowLedgeAreaDTOConverter knowLedgeAreaDTOConverter = new KnowLedgeAreaDTOConverter();

            List<KnowLedgeAreaDTO> knowledgeAreasDTO = knowLedgeAreaDTOConverter.toDTOList(knowledgeAreas);

            // Constructor response
            response = Response.ok().entity(knowledgeAreasDTO).build();
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
            KnowledgeAreaLogic knowledgeAreaLogic = new KnowledgeAreaLogic();

            //Logic
            List<String> joins = new ArrayList<String>();
            joins.add(KnowledgeArea.COMPANY);

            KnowledgeArea knowledgeArea = knowledgeAreaLogic.findById(id, joins);

            if (knowledgeArea != null) {

                // Converter model object to dto object
                //
                KnowLedgeAreaDTOConverter knowLedgeAreaDTOConverter = new KnowLedgeAreaDTOConverter();

                KnowLedgeAreaDTO knowLedgeAreaDTO = knowLedgeAreaDTOConverter.toDTO(knowledgeArea);

                // Constructor response
                response = Response.ok().entity(knowLedgeAreaDTO).build();
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
            KnowledgeAreaLogic knowledgeAreaLogic = new KnowledgeAreaLogic(resourceBundle);

            // Logic
            knowledgeAreaLogic.remove(id);

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
