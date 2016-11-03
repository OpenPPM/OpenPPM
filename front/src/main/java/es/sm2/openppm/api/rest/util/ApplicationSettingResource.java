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
 * File: ApplicationSettingResource.java
 * Create User: javier.hernandez
 * Create Date: 01/10/2015 16:32:57
 */

package es.sm2.openppm.api.rest.util;

import es.sm2.openppm.api.converter.common.SettingGroupDTOConverter;
import es.sm2.openppm.api.model.common.SettingGroupDTO;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.common.ApplicationSetting;
import es.sm2.openppm.core.logic.impl.SettingLogic;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Resource to handle notifications.
 */
@Path("/util/config/setting")
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationSettingResource extends BaseResource {


	@GET
    public Response getGroupList() throws RestServiceException {

        // Check permission for user logged
        isUserLogged(Resourceprofiles.Profile.ADMIN);

        // Find application settings
        Set<Class<? extends ApplicationSetting>> applicationSettingList = CacheStatic.getApplicationSettingList();

        SettingGroupDTOConverter converter = new SettingGroupDTOConverter();
        return Response.ok(converter.toDTOList(applicationSettingList)).build();
	}

    @GET
    @Path("{TYPE}")
    public Response getGroupType(@PathParam("TYPE") String type) throws RestServiceException {

        // Check permission for user logged
        isUserLogged(Resourceprofiles.Profile.ADMIN);

        SettingGroupDTOConverter converter = new SettingGroupDTOConverter();

        Class<? extends ApplicationSetting> applicationSetting = converter.toModel(new SettingGroupDTO(type));

        SettingGroupDTO dto = converter.toDTO(applicationSetting);

        try {
            SettingLogic logic = new SettingLogic();
            List<Setting> settingList = logic.findByCompany(getCompany(), applicationSetting);
            dto.setSettingList(new HashMap<String, String>());

            for (Setting setting : settingList) {
                dto.getSettingList().put(setting.getName(), setting.getValue());
            }

        } catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "getGroupType", e);
        }

        return Response.ok(dto).build();
	}

    @PUT
    @Path("{TYPE}")
    public Response updateGroupTypeSetting(@PathParam("TYPE") String type, SettingGroupDTO settingGroupDTO) throws RestServiceException {

        // Check permission for user logged
        isUserLogged(Resourceprofiles.Profile.ADMIN);

        if (settingGroupDTO.getSettingList() == null || settingGroupDTO.getSettingList().isEmpty()) {
            throw new RestServiceException(Response.Status.BAD_REQUEST, "Not settings for update");
        }

        try {

            List<Setting> settingList = new ArrayList<Setting>();

            for (String key : settingGroupDTO.getSettingList().keySet()) {

                // Create setting
                Setting setting = new Setting();
                setting.setCompany(getCompany());
                setting.setName(key);
                setting.setValue(settingGroupDTO.getSettingList().get(key));

                // Add setting for update
                settingList.add(setting);
            }

            SettingLogic logic = new SettingLogic();
            logic.updateSettingList(settingList);

        } catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "updateGroupTypeSetting", e);
        }

        return Response.ok().build();
	}

}
