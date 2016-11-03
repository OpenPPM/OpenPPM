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
 * File: WeekDTOConverter.java
 * Create User: javier.hernandez
 * Create Date: 24/09/2015 13:48:55
 */

package es.sm2.openppm.api.converter.management;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.management.WeekDTO;
import es.sm2.openppm.core.model.impl.Timesheet;

/**
 * Created by javier.hernandez on 24/09/2015.
 */
public class WeekDTOConverter extends AbstractConverter<WeekDTO, Timesheet> {

    @Override
    public WeekDTO toDTO(Timesheet model) {

        WeekDTO dto = getInstanceDTO(model, WeekDTO.class);
        dto.setCode(model.getIdTimeSheet());
        dto.setStatus(WeekDTO.Status.valueOf(model.getStatus().toUpperCase()));
        dto.setSumHours(model.sumHours());

        return dto;
    }

    @Override
    public Timesheet toModel(WeekDTO modelDTO) {

        Timesheet model = getInstanceModel(modelDTO, Timesheet.class);
        model.setIdTimeSheet(modelDTO.getCode());
        model.setStatus(modelDTO.getStatus().name());

        return model;
    }
}
