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
 * File: ProjectDTOConverterTest.java
 * Create User: javier.hernandez
 * Create Date: 31/07/2015 13:06:36
 */

package es.sm2.openppm.api.converter.test;

import es.sm2.openppm.api.converter.project.ProjectDTOConverter;
import es.sm2.openppm.api.model.project.ProjectDTO;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.model.impl.Project;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by javier.hernandez on 31/07/2015.
 */
public class ProjectDTOConverterTest {

    private ProjectDTOConverter projectDTOConverter;

    @Before
    public void init() {

        // Instance converter
        projectDTOConverter = new ProjectDTOConverter();
    }

    @Test
    public void testToDTO() {

        // BD instance
        Project project = new Project();
        project.setIdProject(1);
        project.setStatus(Constants.STATUS_CONTROL);
        project.setProjectName("Project Name");

        ProjectDTO dto = projectDTOConverter.toDTO(project);

        // Check conversion
        Assert.assertEquals("Project DTO getIdProject not equals", project.getIdProject(), dto.getCode());

        //TODO PONER CONVERTERS
    }

    @Test
    public void testToModelBD() {

        // DTO Instance
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setCode(2);

        Project model = projectDTOConverter.toModel(projectDTO);

        // Check conversion
        Assert.assertEquals("Project BD getIdProject not equals", projectDTO.getCode(), model.getIdProject());
    }

}
