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
 * File: ProfileDTOConverter.java
 * Create User: javier.hernandez
 * Create Date: 07/09/2015 18:17:59
 */

package es.sm2.openppm.api.converter.common;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.common.ProfileDTO;
import es.sm2.openppm.core.model.impl.Employee;

public class ProfileDTOConverter extends AbstractConverter<ProfileDTO, Employee> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public ProfileDTO toDTO(Employee model) {

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setCodeEmployee(model.getIdEmployee());

        if (model.getResourceprofiles() != null) {
            profileDTO.setName(model.getResourceprofiles().getProfileName());
            profileDTO.setCode(model.getResourceprofiles().getIdProfile());
        }

        if (model.getPerformingorg() != null) {

            profileDTO.setPerformingOrganization(
                    new EntityDTO(
                            model.getPerformingorg().getIdPerfOrg(),
                            model.getPerformingorg().getName()));
        }

        if (model.getResourceprofiles() != null) {
            profileDTO.setName(model.getResourceprofiles().getProfileName());
            profileDTO.setCode(model.getResourceprofiles().getIdProfile());
        }

        return profileDTO;
    }

    /**
     * Convert DTO model into BD model
     *
     * @param modelDTO
     * @return
     */
    @Override
    public Employee toModel(ProfileDTO modelDTO) {

        return getInstanceModel(modelDTO, Employee.class);
    }
}
