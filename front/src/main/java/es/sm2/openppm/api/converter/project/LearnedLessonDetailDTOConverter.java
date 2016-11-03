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
 * File: LearnedLessonDetailDTOConverter.java
 * Create User: jordi.ripoll
 * Create Date: 31/08/2015 10:52:47
 */

package es.sm2.openppm.api.converter.project;

import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.project.LearnedLessonDTO;
import es.sm2.openppm.api.model.project.ProjectDTO;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.LearnedLessonProject;
import es.sm2.openppm.utils.functions.ValidateUtil;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
public class LearnedLessonDetailDTOConverter extends LearnedLessonDTOConverter {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    public LearnedLessonDTO toDTODetail(LearnedLesson model) {

        // Converter basic data
        LearnedLessonDTO lessonDTO = toDTO(model);

        // Detail data
        //
        lessonDTO.setDateRaised(model.getDateRaised());

        if (model.getContact() != null) {
            lessonDTO.setOwner(new EntityDTO(model.getContact().getIdContact(), model.getContact().getFullName()));
        }

        lessonDTO.setImpactTime(model.getImpactTime());
        lessonDTO.setImpactCost(model.getImpactCost());
        lessonDTO.setImpactSatisfaction(model.getImpactSatisfaction());

        // Project
        //
        if (ValidateUtil.isNotNull(model.getLearnedLessonProjects())) {

            ProjectDTOConverter projectDTOConverter = new ProjectDTOConverter();

            for (LearnedLessonProject learnedLessonProject : model.getLearnedLessonProjects()) { // only one

                ProjectDTO projectDTO = projectDTOConverter.toDTO(learnedLessonProject.getProject());

                lessonDTO.setProject(projectDTO);
            }
        }

        // Profile
        //
        if (model.getProfile() != null) {

            EntityDTO profileDTO = new EntityDTO(model.getProfile().getIdProfile(), model.getProfile().getName());

            lessonDTO.setProfile(profileDTO);
        }

        // Program
        //
        if (model.getProgram() != null) {

            EntityDTO programDTO = new EntityDTO(model.getProgram().getIdProgram(), model.getProgram().getProgramName());

            lessonDTO.setProgram(programDTO);
        }

        // Customer
        //
        if (model.getCustomer() != null) {

            EntityDTO customerDTO = new EntityDTO(model.getCustomer().getIdCustomer(), model.getCustomer().getName());

            lessonDTO.setCustomer(customerDTO);
        }

        // Seller
        //
        if (model.getSeller() != null) {

            EntityDTO sellerDTO = new EntityDTO(model.getSeller().getIdSeller(), model.getSeller().getName());

            lessonDTO.setSeller(sellerDTO);
        }

        // Geography
        //
        if (model.getGeography() != null) {

            EntityDTO geographyDTO = new EntityDTO(model.getGeography().getIdGeography(), model.getGeography().getName());

            lessonDTO.setGeography(geographyDTO);
        }

        // Contract type
        //
        if (model.getContracttype() != null) {

            EntityDTO contractDTO = new EntityDTO(model.getContracttype().getIdContractType(), model.getContracttype().getDescription());

            lessonDTO.setContractType(contractDTO);
        }

        // Funding source
        //
        if (model.getFundingSource() != null) {

            EntityDTO fundingDTO = new EntityDTO(model.getFundingSource().getIdFundingSource(), model.getFundingSource().getName());

            lessonDTO.setFundingSource(fundingDTO);
        }

        // Knowledge Area
        //
        if (model.getKnowledgeArea() != null) {

            EntityDTO knowLedgeAreaDTO = new EntityDTO(model.getKnowledgeArea().getIdKnowledgeArea(), model.getKnowledgeArea().getName());

            lessonDTO.setKnowledgeArea(knowLedgeAreaDTO);
        }

        return lessonDTO;
    }

}
