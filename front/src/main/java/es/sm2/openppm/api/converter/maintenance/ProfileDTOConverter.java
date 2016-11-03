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
 * Create User: jordi.ripoll
 * Create Date: 03/08/2015 12:16:54
 */

package es.sm2.openppm.api.converter.maintenance;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.maintenance.ProfileDTO;
import es.sm2.openppm.core.model.impl.Profile;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
public class ProfileDTOConverter extends AbstractConverter<ProfileDTO, Profile> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public ProfileDTO toDTO(Profile model) {

        ProfileDTO dto = getInstanceDTO(model, ProfileDTO.class);

        // Set id
        dto.setCode(model.getIdProfile()); //TODO jordi.ripoll - 06/08/2015 - falta hacer lo de la id generica

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param dto
     * @return
     */
    @Override
    public Profile toModel(ProfileDTO dto) {

        Profile model = getInstanceModel(dto, Profile.class);

        // Set id
        model.setIdProfile(dto.getCode()); //TODO jordi.ripoll - 06/08/2015 - falta hacer lo de la id generica

        return model;
    }
}
