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
 * File: ProjectGenLessonDTOConverter.java
 * Create User: jordi.ripoll
 * Create Date: 08/09/2015 12:53:37
 */

package es.sm2.openppm.api.converter.project;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.project.LearnedLessonProjectDTO;
import es.sm2.openppm.api.model.project.ProjectDTO;
import es.sm2.openppm.core.model.impl.LearnedLessonProject;
import es.sm2.openppm.core.model.impl.Project;

/**
 * Created by jordi.ripoll on 08/09/2015.
 */
public class LearnedLessonProjectDTOConverter extends AbstractConverter<LearnedLessonProjectDTO, LearnedLessonProject> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public LearnedLessonProjectDTO toDTO(LearnedLessonProject model) {

        LearnedLessonProjectDTO dto = new LearnedLessonProjectDTO();

        // Set id
        dto.setCode(model.getIdLearnedLessonProj());

        //TODO jordi.ripoll - 08/09/2015 - convertir todos los campos que se anyadan especificos

        // Converter learned lesson
        //
        if (model.getLearnedLesson() != null) {

            LearnedLessonDTOConverter learnedLessonDTOConverter = new LearnedLessonDTOConverter();

            dto.setGeneric(learnedLessonDTOConverter.toDTO(model.getLearnedLesson()));
        }

        // Converter project
        //
        if (model.getProject() != null) {

            ProjectDTO projectDTO = new ProjectDTO();

            projectDTO.setCode(model.getProject().getIdProject());

            dto.setProject(projectDTO);
        }

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param dto
     * @return
     */
    @Override
    public LearnedLessonProject toModel(LearnedLessonProjectDTO dto) {

        LearnedLessonProject model = getInstanceModel(dto, LearnedLessonProject.class);

        model.setIdLearnedLessonProj(dto.getCode());

        // Project
        if (dto.getProject() != null) {
            model.setProject(new Project(dto.getProject().getCode()));
        }


        return model;
    }
}
