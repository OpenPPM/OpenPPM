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
 * File: ChangeRequestNodeDTOConverter.java
 * Create User: javier.hernandez
 * Create Date: 22/09/2015 15:15:27
 */

package es.sm2.openppm.api.converter.project;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.project.ChangeRequestDTO;
import es.sm2.openppm.api.model.project.ChangeRequestNodeDTO;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Changerequestwbsnode;

/**
 * Created by jordi.ripoll on 31/08/2015.
 */
public class ChangeRequestNodeDTOConverter extends AbstractConverter<ChangeRequestNodeDTO, Changerequestwbsnode> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public ChangeRequestNodeDTO toDTO(Changerequestwbsnode model) {

        ChangeRequestNodeDTO dto = getInstanceDTO(model, ChangeRequestNodeDTO.class);
        dto.setCode(model.getIdChangeRequestWbsnode());

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param dto
     * @return
     */
    @Override
    public Changerequestwbsnode toModel(ChangeRequestNodeDTO dto) {

        Changerequestwbsnode model = getInstanceModel(dto, Changerequestwbsnode.class);

        return model;
    }
}
