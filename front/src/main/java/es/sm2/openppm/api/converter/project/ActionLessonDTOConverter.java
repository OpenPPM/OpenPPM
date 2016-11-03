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
 * File: ActionLessonDTOConverter.java
 * Create User: jordi.ripoll
 * Create Date: 31/08/2015 16:04:44
 */

package es.sm2.openppm.api.converter.project;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.project.ActionLessonDTO;
import es.sm2.openppm.core.model.impl.ActionLesson;

/**
 * Created by jordi.ripoll on 31/08/2015.
 */
public class ActionLessonDTOConverter extends AbstractConverter<ActionLessonDTO, ActionLesson> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public ActionLessonDTO toDTO(ActionLesson model) {

        ActionLessonDTO dto = getInstanceDTO(model, ActionLessonDTO.class);

        dto.setCode(model.getIdActionLesson());

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param dto
     * @return
     */
    @Override
    public ActionLesson toModel(ActionLessonDTO dto) {

        ActionLesson model = getInstanceModel(dto, ActionLesson.class);

        return model;
    }
}
