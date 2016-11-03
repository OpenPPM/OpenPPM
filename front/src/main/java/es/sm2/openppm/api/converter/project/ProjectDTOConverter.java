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
 * Create Date: 31/07/2015 12:05:33
 */

package es.sm2.openppm.api.converter.project;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.project.ProgramDTO;
import es.sm2.openppm.api.model.project.ProjectDTO;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.LogManager;
import org.hibernate.LazyInitializationException;

/**
 * Created by javier.hernandez on 31/07/2015.
 */
public class ProjectDTOConverter extends AbstractConverter<ProjectDTO, Project> {


    @Override
    public ProjectDTO toDTO(Project model) {

        ProjectDTO dto = getInstanceDTO(model, ProjectDTO.class);

        dto.setCode(model.getIdProject());
        dto.setName(model.getProjectName());
        dto.setPoc(dto.getPoc() == null ? 0 : dto.getPoc());

        try {

            // Employee
            //
            if (model.getEmployeeByProjectManager() != null && model.getEmployeeByProjectManager().getContact() != null) {

                dto.setProjectManager(new EntityDTO(model.getEmployeeByProjectManager().getIdEmployee(),
                        model.getEmployeeByProjectManager().getContact().getFullName()));
            }

            // PO
            //
            if (model.getPerformingorg() != null) {

                dto.setPerformingorg(new EntityDTO(model.getPerformingorg().getIdPerfOrg(), model.getPerformingorg().getName()));
            }

            // Program
            //
            if (model.getProgram() != null) {

                ProgramDTO programDTO = new ProgramDTO();

                programDTO.setCode(model.getProgram().getIdProgram());
                programDTO.setName(model.getProgram().getProgramName());

                if (model.getProgram().getEmployee() != null && model.getProgram().getEmployee().getContact() != null) {
                    programDTO.setProgramManager(new EntityDTO(null, model.getProgram().getEmployee().getContact().getFullName()));
                }

                dto.setProgram(programDTO);
            }

            // Customer
            //
            if (model.getCustomer() != null) {
                dto.setCustomer(new EntityDTO(model.getCustomer().getIdCustomer(), model.getCustomer().getName()));
            }

            // Sponsor
            //
            if (model.getEmployeeBySponsor() != null && model.getEmployeeBySponsor().getContact() != null) {
                dto.setSponsor(new EntityDTO(model.getEmployeeBySponsor().getIdEmployee(), model.getEmployeeBySponsor().getContact().getFullName()));
            }

            // Geographic area
            //
            if (model.getGeography() != null) {
                dto.setGeography(new EntityDTO(model.getGeography().getIdGeography(), model.getGeography().getName()));
            }

            // Contract type
            //
            if (model.getContracttype() != null) {
                dto.setContracttype(new EntityDTO(model.getContracttype().getIdContractType(), model.getContracttype().getDescription()));
            }
        }
        catch (LazyInitializationException e) {
            LogManager.getLog(getClass()).debug("Lazy Initialization", e);
        }

        return dto;
    }

    @Override
    public Project toModel(ProjectDTO dto) {

        Project model = getInstanceModel(dto, Project.class);

        model.setIdProject(dto.getCode());

        return model;
    }
}
