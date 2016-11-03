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
 * File: ProjectDTOConverter.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:56:58
 */

package es.sm2.openppm.api.converter.project.audit;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.project.ProgramDTO;
import es.sm2.openppm.api.model.project.ProjectDTO;
import es.sm2.openppm.core.audit.model.ProjectAudit;
import es.sm2.openppm.core.model.impl.Project;

/**
 * Created by javier.hernandez on 31/07/2015.
 */
public class ProjectDTOConverter extends AbstractConverter<ProjectDTO, ProjectAudit> {

    @Override
    public ProjectDTO toDTO(ProjectAudit model) {

        ProjectDTO dto = getInstanceDTO(model, ProjectDTO.class);

        dto.setName(model.getProjectName());

        // Set Employee
        //
        if (model.getEmployeeByProjectManager() != null && model.getEmployeeByProjectManager().getFullName() != null) {

            dto.setProjectManager(new EntityDTO(null, model.getEmployeeByProjectManager().getFullName()));
        }

        if (model.getEmployeeByFunctionalManager() != null && model.getEmployeeByFunctionalManager().getFullName() != null) {

            dto.setFunctionalManager(new EntityDTO(null, model.getEmployeeByFunctionalManager().getFullName()));
        }

        if (model.getEmployeeByInvestmentManager() != null && model.getEmployeeByInvestmentManager().getFullName() != null) {

            dto.setInvestmentManager(new EntityDTO(null, model.getEmployeeByInvestmentManager().getFullName()));
        }

        if (model.getEmployeeBySponsor() != null && model.getEmployeeBySponsor().getFullName() != null) {

            dto.setSponsor(new EntityDTO(null, model.getEmployeeBySponsor().getFullName()));
        }

        if (model.getProgram() != null) {

            dto.setProgram(new ProgramDTO(model.getProgram()));
        }

        if (model.getCategory() != null) {

            dto.setCategory(new EntityDTO(model.getCategory()));
        }

        if (model.getCustomer() != null) {

            dto.setCustomer(new EntityDTO(model.getCustomer()));
        }

        if (model.getContracttype() != null) {

            dto.setContracttype(new EntityDTO(model.getContracttype()));
        }

        if (model.getGeography() != null) {

            dto.setGeography(new EntityDTO(model.getGeography()));
        }

        if (model.getStageGate() != null) {

            dto.setStagegate(new EntityDTO(model.getStageGate().getName()));
        }

        if (model.getClassificationlevel() != null) {

            dto.setClassificationlevel(new EntityDTO(model.getClassificationlevel().getName()));
        }


        // Set PO
        //
        if (model.getPerformingorg() != null) {

            dto.setPerformingorg(new EntityDTO(model.getPerformingorg()));
        }

        return dto;
    }

    @Override
    public ProjectAudit toModel(ProjectDTO dto) {

        ProjectAudit model = getInstanceModel(dto, ProjectAudit.class);

        return model;
    }
}
