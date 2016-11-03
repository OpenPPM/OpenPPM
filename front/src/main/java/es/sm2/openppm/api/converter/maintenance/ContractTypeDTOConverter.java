package es.sm2.openppm.api.converter.maintenance;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.maintenance.ContractTypeDTO;
import es.sm2.openppm.core.model.impl.Contracttype;

/**
 * Created by daniel.salamanca on 02/09/2015.
 */
public class ContractTypeDTOConverter extends AbstractConverter<ContractTypeDTO, Contracttype> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public ContractTypeDTO toDTO(Contracttype model) {

        ContractTypeDTO dto = getInstanceDTO(model, ContractTypeDTO.class);

        // Set id
        dto.setCode(model.getIdContractType());

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param modelDTO
     * @return
     */
    @Override
    public Contracttype toModel(ContractTypeDTO modelDTO) {

        Contracttype model = getInstanceModel(modelDTO, Contracttype.class);

        // Set id
        model.setIdContractType(modelDTO.getCode());

        return model;
    }
}
