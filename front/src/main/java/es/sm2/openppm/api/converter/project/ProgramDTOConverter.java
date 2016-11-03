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
 * File: ProgramDTOConverter.java
 * Create User: jordi.ripoll
 * Create Date: 04/09/2015 12:48:38
 */

package es.sm2.openppm.api.converter.project;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.core.model.impl.Program;

/**
 * Created by javier.hernandez on 31/07/2015.
 */
public class ProgramDTOConverter extends AbstractConverter<EntityDTO, Program> {

    @Override
    public EntityDTO toDTO(Program model) {

        EntityDTO dto = new EntityDTO(model.getIdProgram(), model.getProgramName());

        return dto;
    }

    @Override
    public Program toModel(EntityDTO dto) {

        Program model = getInstanceModel(dto, Program.class);

        model.setIdProgram(dto.getCode());
        model.setProgramName(dto.getName());

        return model;
    }
}
