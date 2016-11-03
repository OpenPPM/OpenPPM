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
 * File: AuditDTOConverter.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:49:34
 */

package es.sm2.openppm.api.converter.project.audit;

import com.thoughtworks.xstream.XStream;
import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.project.ActionLessonDTO;
import es.sm2.openppm.api.model.project.AuditDTO;
import es.sm2.openppm.core.audit.XStreamAuditUtil;
import es.sm2.openppm.core.audit.model.ProjectAudit;
import es.sm2.openppm.core.model.impl.ActionLesson;
import es.sm2.openppm.core.model.impl.Audit;

/**
 * Created by jordi.ripoll on 31/08/2015.
 */
public class AuditDTOConverter extends AbstractConverter<AuditDTO, Audit> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public AuditDTO toDTO(Audit model) {

        AuditDTO dto = getInstanceDTO(model, AuditDTO.class);
        dto.setCode(model.getIdAudit());

        // Load from xml
        XStream xstream = XStreamAuditUtil.createXStrean();
        ProjectAudit projectAudit = (ProjectAudit) xstream.fromXML(new String(model.getDataObject()));

        // Convert to DTO
        ProjectDTOConverter converter = new ProjectDTOConverter();
        dto.setProject(converter.toDTO(projectAudit));

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param dto
     * @return
     */
    @Override
    public Audit toModel(AuditDTO dto) {

        Audit model = getInstanceModel(dto, Audit.class);

        return model;
    }
}
