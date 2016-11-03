package es.sm2.openppm.api.impl.maintenance;

import es.sm2.openppm.api.converter.maintenance.StakeholderClassificationDTOConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.common.ErrorDTO;
import es.sm2.openppm.api.model.maintenance.StakeholderClassificationDTO;
import es.sm2.openppm.core.logic.impl.StakeholderclassificationLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Stakeholderclassification;
import es.sm2.openppm.utils.LogManager;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by jose.llobera on 13/10/2015.
 */
public class StakeholderClassificationActionImpl implements MaintenanceAction{

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
            StakeholderClassificationDTO stakeholderClassificationDTO = new ObjectMapper().convertValue(object, StakeholderClassificationDTO.class);

            // Converter DTO to model
            StakeholderClassificationDTOConverter stakeholderClassificationDTOConverter = new StakeholderClassificationDTOConverter();
            Stakeholderclassification stakeholderclassification = stakeholderClassificationDTOConverter.toModel(stakeholderClassificationDTO);
            stakeholderclassification.setCompany(company);

            // Declare logic
            StakeholderclassificationLogic stakeholderclassificationLogic = new StakeholderclassificationLogic();
            stakeholderclassification = stakeholderclassificationLogic.save(stakeholderclassification);

            // Converter model to DTO
            stakeholderClassificationDTO = stakeholderClassificationDTOConverter.toDTO(stakeholderclassification);

            // Constructor response
            response = Response.ok().entity(stakeholderClassificationDTO).build();
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
            StakeholderclassificationLogic stakeholderclassificationLogic = new StakeholderclassificationLogic();

            //Logic
            List<Stakeholderclassification> stakeholderclassifications = stakeholderclassificationLogic.findByRelation(Stakeholderclassification.COMPANY, company);

            // Converter model object to dto object
            //
            StakeholderClassificationDTOConverter stakeholderClassificationDTOConverter = new StakeholderClassificationDTOConverter();

            List<StakeholderClassificationDTO> stakeholderClassificationDTOs = stakeholderClassificationDTOConverter.toDTOList(stakeholderclassifications);

            // Constructor response
            response = Response.ok().entity(stakeholderClassificationDTOs).build();
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
            StakeholderclassificationLogic stakeholderclassificationLogic = new StakeholderclassificationLogic();

            //Logic
            List<String> joins = new ArrayList<String>();
            joins.add(Stakeholderclassification.COMPANY);

            Stakeholderclassification stakeholderclassification = stakeholderclassificationLogic.findById(id, joins);

            if (stakeholderclassification != null) {

                // Converter model object to dto object
                //
                StakeholderClassificationDTOConverter stakeholderClassificationDTOConverter = new StakeholderClassificationDTOConverter();

                StakeholderClassificationDTO stakeholderClassificationDTO = stakeholderClassificationDTOConverter.toDTO(stakeholderclassification);

                // Constructor response
                response = Response.ok().entity(stakeholderClassificationDTO).build();
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
     * Remove
     *
     * @param idStakeholderClassification
     * @param resourceBundle
     * @return
     */
    @Override
    public Response remove(Integer idStakeholderClassification, ResourceBundle resourceBundle) {

        Response response;

        try {

            // Declare logic
            StakeholderclassificationLogic stakeholderclassificationLogic = new StakeholderclassificationLogic(resourceBundle);

            // Logic
            Stakeholderclassification stakeholderclassification = stakeholderclassificationLogic.findById(idStakeholderClassification);

            stakeholderclassificationLogic.deleteStakeholderClassification(stakeholderclassification,idStakeholderClassification);

            // Constructor response
            response = Response.ok().entity(new EntityDTO(idStakeholderClassification)).build();
        }

        catch (Exception e) {

            LogManager.getLog(this.getClass()).error(e);

            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();
        }

        return response;
    }
}
