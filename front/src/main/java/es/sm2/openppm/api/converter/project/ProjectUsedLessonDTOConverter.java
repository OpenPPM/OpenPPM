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
 * File: ProjectUsedLessonDTOConverter.java
 * Create User: jordi.ripoll
 * Create Date: 08/09/2015 12:18:09
 */

package es.sm2.openppm.api.converter.project;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.project.LearnedLessonDTO;
import es.sm2.openppm.api.model.project.ProjectDTO;
import es.sm2.openppm.api.model.project.LearnedLessonLinkDTO;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.LearnedLessonLink;

/**
 * Created by jordi.ripoll on 08/09/2015.
 */
public class ProjectUsedLessonDTOConverter extends AbstractConverter<LearnedLessonLinkDTO, LearnedLessonLink> {

    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public LearnedLessonLinkDTO toDTO(LearnedLessonLink model) {

        LearnedLessonLinkDTO dto = new LearnedLessonLinkDTO();

        dto.setCode(model.getIdLearnedLessonLink());

        // Set project linked
        //
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setCode(model.getProject().getIdProject());

        dto.setProject(projectDTO);

        // Generic
        if (model.getLearnedLesson() != null) {

            LearnedLesson learnedLesson = model.getLearnedLesson();

            // Converter learnedlesson
            //
            LearnedLessonDTOConverter learnedLessonDTOConverter = new LearnedLessonDTOConverter();

            LearnedLessonDTO lessonDTO = learnedLessonDTOConverter.toDTO(learnedLesson);

            dto.setGeneric(lessonDTO);
        }
//        // Specific
//        else if (model.getLearnedLessonProject() != null) {
//
//            // Converter projectgenlesson
//            //
//            ProjectGenLessonDTOConverter projectGenLessonDTOConverter = new ProjectGenLessonDTOConverter();
//
//            ProjectGenLessonDTO specific = projectGenLessonDTOConverter.toDTO(model.getLearnedLessonProject());
//
//            dto.setSpecific(specific);
//        }

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param modelDTO
     * @return
     */
    @Override
    public LearnedLessonLink toModel(LearnedLessonLinkDTO modelDTO) {
        return null;
    }
}
