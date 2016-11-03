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
 * File: UserDTOConverter.java
 * Create User: jordi.ripoll
 * Create Date: 20/08/2015 13:47:17
 */

package es.sm2.openppm.api.converter.common;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.common.UserDTO;
import es.sm2.openppm.core.model.impl.Employee;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
public class UserDTOConverter extends AbstractConverter<UserDTO, Employee> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public UserDTO toDTO(Employee model) {

        UserDTO dto = new UserDTO();
        dto.setCode(model.getIdEmployee());

        // Set data po
        //
        if (model.getPerformingorg() != null) {

            dto.setPerformingOrganization(
                    new EntityDTO(
                            model.getPerformingorg().getIdPerfOrg(),
                            model.getPerformingorg().getName()));
        }

        // Set data contact
        //
        if (model.getContact() != null) {

            dto.setName(model.getContact().getFullName());
            dto.setLocale(model.getContact().getLocale());
        }

        // Set data profile
        //
        if (model.getResourceprofiles() != null) {
            dto.setProfile(
                    new EntityDTO(
                            model.getResourceprofiles().getIdProfile(),
                            model.getResourceprofiles().getProfileName()));
        }

        return dto;
    }

    /**
     * Convert DTO model into BD model
     *
     * @param modelDTO
     * @return
     */
    @Override
    public Employee toModel(UserDTO modelDTO) {
        return getInstanceModel(modelDTO, Employee.class);
    }


}
