package es.sm2.openppm.api.converter.maintenance;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.maintenance.GeographyDTO;
import es.sm2.openppm.core.model.impl.Geography;

/**
 * Created by daniel.salamanca on 02/09/2015.
 */
public class GeographyDTOConverter extends AbstractConverter<GeographyDTO, Geography> {

    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public GeographyDTO toDTO(Geography model) {

        GeographyDTO dto = getInstanceDTO(model, GeographyDTO.class);

        // Set id
        dto.setCode(model.getIdGeography());

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param modelDTO
     * @return
     */
    @Override
    public Geography toModel(GeographyDTO modelDTO) {

        Geography model = getInstanceModel(modelDTO, Geography.class);

        // Set id
        model.setIdGeography((modelDTO.getCode()));

        return model;
    }
}
