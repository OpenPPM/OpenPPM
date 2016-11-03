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
 * File: DocumentDTOConverter.java
 * Create User: javier.hernandez
 * Create Date: 23/09/2015 10:03:31
 */

package es.sm2.openppm.api.converter.common;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.DocumentDTO;
import es.sm2.openppm.core.model.impl.Documentation;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
public class DocumentDTOConverter extends AbstractConverter<DocumentDTO, Documentation> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public DocumentDTO toDTO(Documentation model) {

        DocumentDTO dto = getInstanceDTO(model, DocumentDTO.class);
        dto.setCode(model.getIdDocumentation());
        dto.setFileName(model.getNameFile());
        dto.setName(model.getNamePopup());

        return dto;
    }

    /**
     * Convert DTO model into BD model
     *
     * @param modelDTO
     * @return
     */
    @Override
    public Documentation toModel(DocumentDTO modelDTO) {
        return getInstanceModel(modelDTO, Documentation.class);
    }


}
