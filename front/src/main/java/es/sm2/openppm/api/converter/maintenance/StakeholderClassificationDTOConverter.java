package es.sm2.openppm.api.converter.maintenance;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.maintenance.StakeholderClassificationDTO;
import es.sm2.openppm.core.model.impl.Stakeholderclassification;

/**
 * Created by jose.llobera on 13/10/2015.
 */
public class StakeholderClassificationDTOConverter extends AbstractConverter<StakeholderClassificationDTO, Stakeholderclassification> {

    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public StakeholderClassificationDTO toDTO(Stakeholderclassification model) {

        StakeholderClassificationDTO stakeholderCLassificationDTO = getInstanceDTO(model, StakeholderClassificationDTO.class);

        // Set id
        stakeholderCLassificationDTO.setCode(model.getIdStakeholderClassification());

        return stakeholderCLassificationDTO;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param modelDTO
     * @return
     */
    @Override
    public Stakeholderclassification toModel(StakeholderClassificationDTO modelDTO) {

        Stakeholderclassification stakeholderClassificationModel = getInstanceModel(modelDTO, Stakeholderclassification.class);

        // Set id
        stakeholderClassificationModel.setIdStakeholderClassification(modelDTO.getCode());

        return stakeholderClassificationModel;
    }
}
