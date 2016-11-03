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
 * File: ConfigurationUserDTOConverter.java
 * Create User: jordi.ripoll
 * Create Date: 13/10/2015 10:25:37
 */

package es.sm2.openppm.api.converter.common;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.ConfigurationDTO;
import es.sm2.openppm.core.model.impl.Configuration;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
public class ConfigurationUserDTOConverter extends AbstractConverter<ConfigurationDTO, Configuration> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public ConfigurationDTO toDTO(Configuration model) {

        ConfigurationDTO dto = new ConfigurationDTO(model.getName(), model.getValue());

        dto.setType(model.getType());

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param dto
     * @return
     */
    @Override
    public Configuration toModel(ConfigurationDTO dto) {

        Configuration model = new Configuration();

        model.setName(dto.getKey());
        model.setValue(dto.getValue());
        model.setType(dto.getType());

        return model;
    }


}
