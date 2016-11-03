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
 * File: OperationDTOConverter.java
 * Create User: javier.hernandez
 * Create Date: 25/09/2015 13:00:56
 */

package es.sm2.openppm.api.converter.management;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.core.model.impl.Operation;

/**
 * Created by javier.hernandez on 25/09/2015.
 */
public class OperationDTOConverter extends AbstractConverter<EntityDTO, Operation> {

    @Override
    public EntityDTO toDTO(Operation model) {

        EntityDTO dto = getInstanceDTO(model, EntityDTO.class);
        dto.setName(model.getOperationName());
        dto.setCode(model.getIdOperation());

        return dto;
    }

    @Override
    public Operation toModel(EntityDTO modelDTO) {

        Operation model = getInstanceModel(modelDTO, Operation.class);
        model.setOperationName(modelDTO.getName());
        model.setIdOperation(modelDTO.getCode());

        return model;
    }
}
