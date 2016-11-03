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
 * File: EmployeeDTOConverter.java
 * Create User: jordi.ripoll
 * Create Date: 07/09/2015 16:11:58
 */

package es.sm2.openppm.api.converter.project;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.project.EmployeeDTO;
import es.sm2.openppm.core.model.impl.Employee;

/**
 * Created by javier.hernandez on 31/07/2015.
 */
public class EmployeeDTOConverter extends AbstractConverter<EmployeeDTO, Employee> {

    @Override
    public EmployeeDTO toDTO(Employee model) {

        EmployeeDTO dto = getInstanceDTO(model, EmployeeDTO.class);

        dto.setCode(model.getIdEmployee());

        if (model.getContact() != null) {
            dto.setContact(new EntityDTO(model.getContact().getIdContact(), model.getContact().getFullName()));
        }

        return dto;
    }

    @Override
    public Employee toModel(EmployeeDTO dto) {

        Employee model = getInstanceModel(dto, Employee.class);

        model.setIdEmployee(dto.getCode());

        return model;
    }
}
