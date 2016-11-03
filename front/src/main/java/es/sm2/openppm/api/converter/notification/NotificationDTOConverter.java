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
 * File: NotificationDTOConverter.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 19:36:33
 */

package es.sm2.openppm.api.converter.notification;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.notification.NotificationDTO;
import es.sm2.openppm.core.model.impl.Contactnotification;

/**
 * Created by daniel.casas on 19/08/2015.
 */
public class NotificationDTOConverter extends AbstractConverter<NotificationDTO, Contactnotification> {


    @Override
    public NotificationDTO toDTO(Contactnotification model) {

        NotificationDTO dto = getInstanceDTO(model, NotificationDTO.class);
        dto.setCode(model.getIdContactNotification());
        dto.setSubject(model.getNotification().getSubject());
        dto.setBody(model.getNotification().getBody());
        dto.setCreationDate(model.getNotification().getCreationDate());
        dto.setStatus(model.getNotification().getStatus());
        dto.setMessageError(model.getNotification().getMessageError());

        return dto;
    }

    @Override
    public Contactnotification toModel(NotificationDTO modelDTO) {

        return getInstanceModel(modelDTO, Contactnotification.class);
    }
}
