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
 * File: NotificationProfileDTOConverter.java
 * Create User: daniel.casas
 * Create Date: 19/08/2015 17:18:33
 */

package es.sm2.openppm.api.converter;

import es.sm2.openppm.api.model.NotificationProfileDTO;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.enums.ModeNotificationEnum;
import es.sm2.openppm.api.model.enums.StatusNotificationEnum;
import es.sm2.openppm.core.model.impl.Notificationprofile;

/**
 * Created by daniel.casas on 19/08/2015.
 */
public class NotificationProfileDTOConverter extends AbstractConverter<NotificationProfileDTO, Notificationprofile> {

    @Override
    public NotificationProfileDTO toDTO(Notificationprofile model) {

    	NotificationProfileDTO dto = getInstanceDTO(model, NotificationProfileDTO.class);
    	
    	dto.setMode(ModeNotificationEnum.valueOf(model.getMode()));
    	dto.setStatus(StatusNotificationEnum.valueOf(model.getStatusNotification().getStatus()));
    	
    	// Set Resource Profile
    	if (model.getResourceProfile() != null) {
    		dto.setProfile(
    				new EntityDTO(
    						model.getResourceProfile().getIdProfile(),
    						model.getResourceProfile().getProfileName()));
    	}
    	
        return dto;
    }

    @Override
    public Notificationprofile toModel(NotificationProfileDTO modelDTO) {

        return getInstanceModel(modelDTO, Notificationprofile.class);
    }
}
