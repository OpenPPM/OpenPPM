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
 * Create User: javier.hernandez
 * Create Date: 24/09/2015 13:39:12
 */

package es.sm2.openppm.api.converter.management;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.EmployeeDTO;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.utils.LogManager;
import org.hibernate.LazyInitializationException;

/**
 * Created by javier.hernandez on 24/09/2015.
 */
public class EmployeeDTOConverter extends AbstractConverter<EmployeeDTO, Employee> {

    @Override
    public EmployeeDTO toDTO(Employee model) {

        EmployeeDTO dto = new EmployeeDTO(model.getIdEmployee(), model.getContact().getFullName());

        try {

            if (model.getResourcepool() != null) {
                dto.setPool(new EntityDTO(model.getResourcepool().getIdResourcepool(), model.getResourcepool().getName()));
            }

            if (model.getSeller() != null) {
                dto.setSeller(new EntityDTO(model.getSeller().getIdSeller(), model.getSeller().getName()));
            }
        }
        catch (LazyInitializationException e) {
            LogManager.getLog(getClass()).debug("Lazy Initialization", e);
        }

        WeekDTOConverter converter = new WeekDTOConverter();
        dto.setWeekList(converter.toDTOList(model.getTimesheets()));

        return dto;
    }

    @Override
    public Employee toModel(EmployeeDTO modelDTO) {

        Employee model = getInstanceModel(modelDTO, Employee.class);

        WeekDTOConverter converter = new WeekDTOConverter();
        model.setTimesheets(converter.toModelSet(modelDTO.getWeekList()));

        return getInstanceModel(modelDTO, Employee.class);
    }
}
