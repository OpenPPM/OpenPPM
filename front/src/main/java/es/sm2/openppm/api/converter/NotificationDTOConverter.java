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
 * Create User: daniel.casas
 * Create Date: 19/08/2015 17:05:33
 */

package es.sm2.openppm.api.converter;

import java.util.ArrayList;
import java.util.List;

import es.sm2.openppm.api.model.NotificationDTO;
import es.sm2.openppm.api.model.enums.StatusNotificationEnum;
import es.sm2.openppm.core.model.impl.Notificationprofile;
import es.sm2.openppm.core.model.impl.Notificationtype;

/**
 * Created by daniel.casas on 19/08/2015.
 */
public class NotificationDTOConverter extends AbstractConverter<NotificationDTO, Notificationtype> {

	@Override
	public NotificationDTO toDTO(Notificationtype model) {

		NotificationDTO dto = getInstanceDTO(model, NotificationDTO.class);

		dto.setStatus(StatusNotificationEnum.valueOf(
				model.getStatusNotification().getStatus()));
		
		if (model.getProfiles() != null) {
			NotificationProfileDTOConverter converter = new NotificationProfileDTOConverter();

			List<Notificationprofile> profilesList = new ArrayList<Notificationprofile>(model.getProfiles());
			dto.setProfiles(converter.toDTOList(profilesList));
		}

		return dto;
	}

	@Override
	public Notificationtype toModel(NotificationDTO modelDTO) {

		Notificationtype model = getInstanceModel(modelDTO, Notificationtype.class);
		
		model.setStatusNotification(es.sm2.openppm.core.model.enums.StatusNotificationEnum.valueOf(
				modelDTO.getStatus().getStatus()));
		
		return model;
	}
}
