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
 * File: ChangeRequestDTOConverter.java
 * Create User: javier.hernandez
 * Create Date: 22/09/2015 13:56:35
 */

package es.sm2.openppm.api.converter.project;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.project.ChangeRequestDTO;
import es.sm2.openppm.api.model.project.ChangeRequestNodeDTO;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Changerequestwbsnode;
import es.sm2.openppm.utils.functions.ValidateUtil;

import java.util.ArrayList;

/**
 * Created by jordi.ripoll on 31/08/2015.
 */
public class ChangeRequestDTOConverter extends AbstractConverter<ChangeRequestDTO, Changecontrol> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public ChangeRequestDTO toDTO(Changecontrol model) {

        ChangeRequestDTO dto = getInstanceDTO(model, ChangeRequestDTO.class);
        dto.setCode(model.getIdChange());

        // Add change type
        if (model.getChangetype() != null) {
            dto.setChangetype(new EntityDTO(model.getChangetype().getIdChangeType(), model.getChangetype().getDescription()));
        }

        // Add nodes
        ChangeRequestNodeDTOConverter converter = new ChangeRequestNodeDTOConverter();
        dto.setNodeDTOList(converter.toDTOList(model.getChangerequestwbsnodes()));

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param dto
     * @return
     */
    @Override
    public Changecontrol toModel(ChangeRequestDTO dto) {

        Changecontrol model = getInstanceModel(dto, Changecontrol.class);

        return model;
    }
}
